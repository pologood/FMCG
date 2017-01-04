/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.ajax.BaseController;
import net.wit.dao.AdminDao;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.TradeVo;
import net.wit.entity.Area;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.OrderType;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.OrderItem;
import net.wit.entity.Shipping;
import net.wit.entity.ShippingItem;
import net.wit.entity.ShippingMethod;
import net.wit.entity.Sn;
import net.wit.entity.Trade;
import net.wit.service.AreaService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.DeliveryCorpService;
import net.wit.service.MemberService;
import net.wit.service.OrderItemService;
import net.wit.service.OrderService;
import net.wit.service.PaymentMethodService;
import net.wit.service.ProductService;
import net.wit.service.ShippingMethodService;
import net.wit.service.SnService;
import net.wit.service.TradeService;

/**
 * Controller - 订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxMemberTradeController")
@RequestMapping("/ajax/member/trade")
public class TradeController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;

	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tradeDisplay")
	private DisplayEngine<Trade, TradeVo> tradeDisplay;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<TradeVo> list(String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable, ModelMap model, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("orderStatus", orderStatus);
		Boolean returnStatus = null;
		if ( (paymentStatus!=null && paymentStatus.equals(PaymentStatus.refundApply)) ||  (shippingStatus!=null && shippingStatus.equals(ShippingStatus.shippedApply)) ) {
		   returnStatus = true;
		   paymentStatus = null;
		   shippingStatus = null;		   
		}
		Page<Trade> page = tradeService.findPage(member.getTenant(), keyword, orderStatus, paymentStatus, shippingStatus, hasExpired,returnStatus, null,pageable);
		List<TradeVo> list = tradeDisplay.convertList(page.getContent());
		return list;
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> view(Long id, ModelMap model) {
		Map<String, Object> data = new HashMap<String, Object>();
		Trade trade = tradeService.find(id);
		data.put("orderItems", trade.getOrderItems());
		data.put("id", trade.getId());
		data.put("creaDate", trade.getCreateDate());
		data.put("sn", trade.getOrder().getSn());
		data.put("username", trade.getOrder().getMember().getUsername());
		data.put("paymentMethodName", trade.getOrder().getPaymentMethodName());
		data.put("finalOrderStatus", trade.getFinalOrderStatus());
		data.put("consignee", trade.getOrder().getConsignee());
		data.put("phone", trade.getOrder().getPhone());
		data.put("address", trade.getOrder().getAddress());
		data.put("memo", trade.getOrder().getMemo());
		data.put("freight", trade.getFreight());
		data.put("promotionDiscount", trade.getPromotionDiscount());
		data.put("offsetAmount", trade.getOffsetAmount());
		data.put("tradeSn", trade.getSn());
		data.put("tax", trade.getTax());
		data.put("amount", trade.getAmount());
		data.put("totalProfit", trade.getTotalProfit());
		data.put("shippingDate", trade.getShippingDate());
		data.put("message", Message.success("success"));
		return data;
	}

	/**
	 * 统计
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> count(Long id, ModelMap model) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		Long waitShipping = tradeService.count(member.getTenant(), OrderStatus.confirmed, null, ShippingStatus.unshipped,null, null);
		Long Shipped = tradeService.count(member.getTenant(), OrderStatus.confirmed, null, ShippingStatus.shipped,null, null);
		Long waitPayment = tradeService.count(member.getTenant(), OrderStatus.unconfirmed, null, null,null, false);
		Long waitReturn = tradeService.count(member.getTenant(), OrderStatus.confirmed, null, null,true, null);
		data.put("waitShipping", waitShipping);
		data.put("Shipped", Shipped);
		data.put("waitPayment", waitPayment);
		data.put("waitReturn", waitReturn);
		return data;
	}

	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody boolean lock(String sn) {
		Member member = memberService.getCurrent();
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isLocked(member)) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(member);
			orderService.update(order);
			return true;
		}
		return false;
	}

	/**
	 * 发货
	 */
	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	public @ResponseBody Message shipping(Long tradeId, RedirectAttributes redirectAttributes, ModelMap model) {
		Trade trade = tradeService.find(tradeId);
		Order order = trade.getOrder();
		Member member = memberService.getCurrent();

		if (order.isLocked(member)) {
			return Message.error("已经锁定,此订单正在处理中");
		}
		if (OrderStatus.cancelled == order.getOrderStatus()) {
			return Message.error("订单已经取消!");
		}

		if (ShippingStatus.shipped == trade.getShippingStatus()) {
			return Message.error("提交订单已经发货!");
		}

		Shipping shipping = new Shipping();

		for (Iterator<OrderItem> iterator = trade.getOrderItems().iterator(); iterator.hasNext();) {
			OrderItem orderItem = iterator.next();
			ShippingItem shippingItem = new ShippingItem();
			shippingItem.setQuantity(orderItem.getQuantity() - orderItem.getShippedQuantity());
			shippingItem.setSn(orderItem.getSn());
			shippingItem.setName(orderItem.getFullName());
			shippingItem.setShipping(shipping);
			shipping.getShippingItems().add(shippingItem);
		}
		shipping.setTrade(trade);
		shipping.setOrder(order);
		ShippingMethod shippingMethod = trade.getOrder().getShippingMethod();
		shipping.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
		DeliveryCenter deliveryCenter = deliveryCenterService.findDefault(member.getTenant());

		shipping.setDeliveryCenter(deliveryCenter);

		// DeliveryCorp deliveryCorp = deliveryCorpService
		shipping.setDeliveryCorp("手机端发货");
		shipping.setDeliveryCorpUrl(null);
		shipping.setDeliveryCorpCode(null);
		Area area = order.getArea();
		shipping.setArea(area != null ? area.getFullName() : null);
		shipping.setAddress(order.getAddress());
		shipping.setPhone(order.getPhone());
		shipping.setZipCode(order.getZipCode());
		shipping.setConsignee(order.getConsignee());

		shipping.setSn(snService.generate(Sn.Type.shipping));
		shipping.setOperator("pos");
		orderService.shipping(order, shipping, null);

		return SUCCESS_MESSAGE;
	}

	/**
	 * 关闭
	 */
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	public @ResponseBody Message close(Long tradeId, RedirectAttributes redirectAttributes) {
		Trade trade = tradeService.find(tradeId);
		Order order = trade.getOrder();
		Member member = memberService.getCurrent();

		if (order.isLocked(member)) {
			return Message.error("已经锁定,此订单正在处理中");
		}
		if (OrderStatus.cancelled == order.getOrderStatus()) {
			return Message.error("订单已经取消!");
		}
		if (OrderType.composite.equals(order.getOrderType())) {
			return Message.error("组合订单不能取消!");
		}
		if (OrderStatus.unconfirmed != order.getOrderStatus()) {
			return Message.error("不能陬消进行中的订单!");
		}
		orderService.cancel(order, member);

		return Message.success("success");
		/**
		 * Trade trade = tradeService.find(id); TradeApply apply = trade.getSubmitApply(); if (apply == null) { addFlashMessage(redirectAttributes,
		 * Message.error("该订单没有申请退换货记录")); return "redirect:view.jhtml?id=" + id; } tradeApplyService.rejected(apply); addFlashMessage(redirectAttributes,
		 * SUCCESS_MESSAGE); return "redirect:view.jhtml?id=" + id;
		 **/
	}
	/**
	 * 调价
	 */
	@RequestMapping(value = "/update_price", method = RequestMethod.POST)
	public @ResponseBody Message updatePrice(Long tradeId, BigDecimal amount, BigDecimal freight, RedirectAttributes redirectAttributes) {
		Trade trade = tradeService.find(tradeId);
		Order order = trade.getOrder();
		if (!trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
			return Message.error("只能对末确认的订单进行调价");
		}
		Member member = memberService.getCurrent();
		if (order.isLocked(member)) {
			return Message.error("已经锁定,此订单正在处理中");
		}
		BigDecimal promotionDiscount = BigDecimal.ZERO;
		BigDecimal calcOffsetAmount = BigDecimal.ZERO;
		BigDecimal calcFreight = BigDecimal.ZERO;
		List<Trade> trades = order.getTrades();
		for (Trade saveTrade : trades) {
			if (saveTrade.getId().equals(tradeId)) {
				saveTrade.setOffsetAmount(amount.subtract(saveTrade.getPrice().add(saveTrade.getTax()).add(saveTrade.getPromotionDiscount())));
				saveTrade.setFreight(freight);
			}
			promotionDiscount = promotionDiscount.add(saveTrade.getPromotionDiscount());
			calcOffsetAmount = calcOffsetAmount.add(saveTrade.getOffsetAmount());
			calcFreight = calcFreight.add(saveTrade.getFreight());
		}
		order.setPromotionDiscount(promotionDiscount);
		order.setOffsetAmount(calcOffsetAmount);
		order.setFreight(calcFreight);
		orderService.update(order);
		return SUCCESS_MESSAGE;
	}
}