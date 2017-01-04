/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.wit.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import net.wit.entity.Order.QueryStatus;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TradeListModel;
import net.wit.controller.app.model.TradeModel;
import net.wit.dao.AdminDao;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.TradeVo;
import net.wit.entity.Area;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.DeliveryCorp;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.OrderType;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;
import net.wit.entity.Shipping;
import net.wit.entity.ShippingItem;
import net.wit.entity.ShippingMethod;
import net.wit.entity.Sn;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.ActivityDetailService;
import net.wit.service.ActivityRulesService;
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
import net.wit.service.TenantService;
import net.wit.service.TradeService;

/**
 * Controller - 商家订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("posMemberTradeController")
@RequestMapping("/pos/member/trade")
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

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tradeDisplay")
	private DisplayEngine<Trade, TradeVo> tradeDisplay;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody DataBlock list(Long tenantId, String keyword,String type,String key, Pageable pageable, ModelMap model, RedirectAttributes redirectAttributes) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+type+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}

		QueryStatus queryStatus = null;
		if ("unshipped".equals(type)) {
			queryStatus = QueryStatus.unshipped;
		} else if ("unpaid".equals(type)) {
			queryStatus = QueryStatus.unpaid;
		} else if ("shipped".equals(type)) {
			queryStatus = QueryStatus.shipped;
		} else if ("unreturned".equals(type)) {
			queryStatus = QueryStatus.unrefunded;
		} else if ("completed".equals(type)) {
			queryStatus = QueryStatus.completed;
		} else if ("cancelled".equals(type)) {
			queryStatus = QueryStatus.cancelled;
		} else {
			queryStatus = QueryStatus.unshipped;
		}
			
		Page<Trade> page =  tradeService.findPage(pageable, tenant, queryStatus, keyword);
		
		return DataBlock.success(TradeListModel.bindData(page.getContent()),"success");
		
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody DataBlock view(Long tenantId,Long id,String key) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Trade trade = tradeService.find(id);
		TradeModel model = new TradeModel();
		model.copyFrom(trade);
		return DataBlock.success(model,"success");
	}

	/**
	 * 统计
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody DataBlock count(Long tenantId,String key,ModelMap model) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		
		Long waitShipping = tradeService.count(tenant, OrderStatus.confirmed, null, ShippingStatus.unshipped,null, null);
		Long Shipped = tradeService.count(tenant, OrderStatus.confirmed, null, ShippingStatus.shipped,null, null);
		Long waitPayment = tradeService.count(tenant, OrderStatus.unconfirmed, PaymentStatus.unpaid, null,null, false);
		Long waitReturn = tradeService.count(tenant,OrderStatus.confirmed, null, null,true, null);
		data.put("waitShipping", waitShipping);
		data.put("Shipped", Shipped);
		data.put("waitPayment", waitPayment);
		data.put("waitReturn", waitReturn);
		return DataBlock.success(data, "success");
	}

	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody DataBlock lock(Long tenantId,String key,String sn) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+sn+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Order order = orderService.findBySn(sn);
		
		if (order != null && !order.isLocked(tenant.getMember())) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(tenant.getMember());
			orderService.update(order);
			return DataBlock.success(true,"success");
		}
		return DataBlock.success(false,"success");
	}

	//确认订单
	@RequestMapping(value="/confirm_order",method = RequestMethod.POST)
	public
	@ResponseBody
	DataBlock confirmOrder(Long tenantId,String key,Long tradeId, Date deliveryDate){
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
		Tenant tenant = tenantService.find(tenantId);
		if (tenant == null) {
			return DataBlock.error("商家信息为空!");
		}
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+tradeId.toString()+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}

		Trade trade = tradeService.find(tradeId);
		trade.setOrderStatus(OrderStatus.confirmed);
		trade.setDeliveryDate(deliveryDate);
		tradeService.update(trade);
		return DataBlock.success("success","确认订单成功");
	}

	//核销提货码
	@RequestMapping(value="/cancel_verification",method = RequestMethod.POST)
	public
	@ResponseBody
	DataBlock cancel_verification(Long tenantId,String key,Long tradeId,String sn){
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
		Tenant tenant = tenantService.find(tenantId);
		if (tenant == null) {
			return DataBlock.error("商家信息为空!");
		}
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+tradeId.toString()+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		Trade trade = tradeService.find(tradeId);
		if (sn!=null) {
			if (!sn.equals(trade.getSn())) {
				return DataBlock.error("提货码无效");
			}
		}
		trade.setOrderStatus(OrderStatus.completed);
		trade.setShippingStatus(ShippingStatus.accept);
		tradeService.update(trade);
		return DataBlock.success("success","核销提货码成功");
	}

	/**
	 * 发货
	 */
	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	public @ResponseBody DataBlock shipping(Long tenantId,String key,Long tradeId,String shopId,Long deliveryCorpId,String trackingNo,String sn,String operator,String [] sns,Integer [] quantitys, RedirectAttributes redirectAttributes, ModelMap model) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+tradeId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Trade trade = tradeService.find(tradeId);
		Order order = trade.getOrder();
		if (order.isLocked(tenant.getMember())) {
			return DataBlock.error("已经锁定,此订单正在处理中");
		}
		if (OrderStatus.cancelled == order.getOrderStatus()) {
			return DataBlock.error("订单已经取消!");
		}

		if (ShippingStatus.shipped == trade.getShippingStatus()) {
			return DataBlock.error("提交订单已经发货!");
		}

		Shipping shipping = new Shipping();

		
		for (int i=0;i<sns.length;i++) {
			Product product = productService.findBySn(sns[i]);
			ShippingItem shippingItem = new ShippingItem();
			shippingItem.setQuantity(quantitys[i]);
			shippingItem.setSn(sns[i]);
			shippingItem.setName(product.getFullName());
			shippingItem.setShipping(shipping);
			shipping.getShippingItems().add(shippingItem);
		}
		shipping.setTrade(trade);
		shipping.setOrder(order);
		ShippingMethod shippingMethod = trade.getOrder().getShippingMethod();
		shipping.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
		
		DeliveryCenter deliveryCenter = deliveryCenterService.findByCode(tenant, shopId);
		if (deliveryCenter == null) {
			deliveryCenter = deliveryCenterService.findDefault(tenant);
		}

		shipping.setDeliveryCenter(deliveryCenter);
		
		shipping.setTrackingNo(trackingNo);
		shipping.setOperator(operator);
		shipping.setPickUpTime(new Date());

		shipping.setDeliveryCorp("ERP发货");
		shipping.setDeliveryCorpUrl(null);
		shipping.setDeliveryCorpCode(null);
		
		if (deliveryCorpId!=null) {
			DeliveryCorp deliveryCorp = deliveryCorpService.find(deliveryCorpId);
			if (deliveryCorp!=null) {
				shipping.setDeliveryCorp(deliveryCorp.getName());
				shipping.setDeliveryCorpUrl(deliveryCorp.getUrl());
				shipping.setDeliveryCorpCode(deliveryCorp.getCode());
			}
		}

		if (sn!=null) {
			if (!sn.equals(trade.getSn())) {
				return DataBlock.error("提货码无效");
			}
		}
		
		
		Area area = order.getArea();
		shipping.setArea(area != null ? area.getFullName() : null);
		shipping.setAddress(order.getAddress());
		shipping.setPhone(order.getPhone());
		shipping.setZipCode(order.getZipCode());
		shipping.setConsignee(order.getConsignee());

		shipping.setSn(snService.generate(Sn.Type.shipping));
		shipping.setOperator("pos");
		orderService.shipping(order, shipping, null);

		return DataBlock.success("success","发货完成");
	}

	/**
	 * 关闭
	 */
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	public @ResponseBody DataBlock close(Long tenantId,String key,Long tradeId, RedirectAttributes redirectAttributes) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+tradeId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}

		Trade trade = tradeService.find(tradeId);
		Order order = trade.getOrder();
		if (order.isLocked(tenant.getMember())) {
			return DataBlock.error("已经锁定,此订单正在处理中");
		}
		if (OrderStatus.cancelled == order.getOrderStatus()) {
			return DataBlock.error("订单已经取消!");
		}
		if (OrderType.composite.equals(order.getOrderType())) {
			return DataBlock.error("组合订单不能取消!");
		}
		if (OrderStatus.unconfirmed != order.getOrderStatus()) {
			return DataBlock.error("不能陬消进行中的订单!");
		}
		orderService.cancel(order, tenant.getMember());

		return DataBlock.success("success","关闭订单完成");
	}

	/**
	 * 拒绝
	 */
	@RequestMapping(value = "/rejected", method = RequestMethod.POST)
	public @ResponseBody DataBlock rejected(Long tenantId,String key,Long tradeId, RedirectAttributes redirectAttributes) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+tradeId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Trade trade = tradeService.find(tradeId);
		Order order = trade.getOrder();

		if (order.isLocked(tenant.getMember())) {
			return DataBlock.error("已经锁定,此订单正在处理中");
		}
		for (SpReturns spReturns:trade.getSpReturns()) {
			if (spReturns.getReturnStatus().equals(ReturnStatus.unconfirmed)) {
				orderService.spRejected(trade, spReturns, tenant.getMember());
			}
		}
		return DataBlock.success("success","拒绝退货完成");
	}

	/**
	 * 调价
	 */
	@RequestMapping(value = "/update_price", method = RequestMethod.POST)
	public @ResponseBody DataBlock updatePrice(Long tenantId,String key,Long tradeId, BigDecimal amount, BigDecimal freight, RedirectAttributes redirectAttributes) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+tradeId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}

		Trade trade = tradeService.find(tradeId);
		Order order = trade.getOrder();
		if (!trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
			return DataBlock.error("只能对末确认的订单进行调价");
		}
		if (order.isLocked(tenant.getMember())) {
			return DataBlock.error("已经锁定,此订单正在处理中");
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
		return DataBlock.success("success","调价成功");
	}

	/**
	 * 退货
	 */
	@RequestMapping(value = "/returns", method = RequestMethod.POST)
	public @ResponseBody DataBlock returns(Long tenantId,String key,Long tradeId, RedirectAttributes redirectAttributes) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!");
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+tradeId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}

	        Trade trade = tradeService.find(tradeId);
	        Order order = trade.getOrder();
            Member member = tenant.getMember();
	        if (order.isLocked(member)) {
	            return DataBlock.error("已经锁定,此订单正在处理中");
	        } else if (OrderStatus.cancelled == order.getOrderStatus()) {
	            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(31L))) {
	                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(31L));
	            }
	            return DataBlock.error("订单已经取消");
	        }
	        try {
	            if (trade.getShippingStatus().equals(ShippingStatus.unshipped) && trade.getPaymentStatus().equals(PaymentStatus.unpaid)) {
	                orderService.cancel(trade, member);
	                return DataBlock.success("success", "退货成功");
	            }
	            if (trade.getShippingStatus().equals(ShippingStatus.returned) || trade.getPaymentStatus().equals(PaymentStatus.refunded)) {
	                return DataBlock.error("已经退货了!");
	            }
	            if (!trade.getShippingStatus().equals(ShippingStatus.shippedApply) && !trade.getPaymentStatus().equals(PaymentStatus.refundApply)) {
	                orderService.spReturns(trade, null, member);
	            }
	            SpReturns current = null;
	            for (SpReturns spReturns : trade.getSpReturns()) {
	                if (spReturns.getReturnStatus().equals(ReturnStatus.unconfirmed)) {
	                    current = spReturns;
	                }
	            }
	            if (current == null) {
	            	return DataBlock.error("退货处理了!");
	            }
	            orderService.spConfirm(trade, current, member);
	 
	            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(32L))) {
	                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(32L));
	            }

	            return DataBlock.success("success", "退货成功");
	        } catch (BalanceNotEnoughException e) {
	            // TODO Auto-generated catch block
	            return DataBlock.success("success", "账户余额不足,不能完成退款");
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            return DataBlock.success("success", "退货失败了");
	        }
	}

}