/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.dao.AdminDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.DeliveryCorp;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.OrderItem;
import net.wit.entity.Payment;
import net.wit.entity.Product;
import net.wit.entity.Refunds;
import net.wit.entity.Review;
import net.wit.entity.Review.Flag;
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
import net.wit.service.RefundsService;
import net.wit.service.ReviewService;
import net.wit.service.ShippingMethodService;
import net.wit.service.SnService;
import net.wit.service.TradeService;

/**
 * Controller - 订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cMemberTradeController")
@RequestMapping("/b2c/member/trade")
public class TradeController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;

	@Resource(name = "deliveryCorpServiceImpl")
	private DeliveryCorpService deliveryCorpService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;

	/**
	 * 检查锁定
	 */
	@RequestMapping(value = "/check_lock", method = RequestMethod.POST)
	public @ResponseBody Message checkLock(Long id) {
		Trade trade = tradeService.find(id);
		if (trade == null) {
			return Message.warn("admin.common.invalid");
		}
		Member member = memberService.getCurrent();
		if (trade.isLocked(member)) {
			if (trade.getOperator() != member) {
				return Message.warn("admin.order.adminLocked", trade.getOperator().getUsername());
			} else {
				return Message.warn("admin.order.memberLocked");
			}
		}
		Order order = trade.getOrder();
		if (order == null) {
			return Message.warn("admin.common.invalid");
		}
		if (order.isLocked(member)) {
			if (order.getOperator() != member) {
				return Message.warn("admin.order.adminLocked", order.getOperator().getUsername());
			} else {
				return Message.warn("admin.order.memberLocked");
			}
		} else {
			trade.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			trade.setOperator(member);
			tradeService.update(trade);
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(member);
			orderService.update(order);
			return SUCCESS_MESSAGE;
		}
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.find(id);
		model.addAttribute("member", member);
		model.addAttribute("methods", Payment.Method.values());
		model.addAttribute("refundsMethods", Refunds.Method.values());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("trade", trade);
		model.addAttribute("isReturnAllowed", !trade.isSpReturns());
		return "/b2c/member/trade/view";
	}

	/**
	 * 退货
	 */
	@RequestMapping(value = "/returnTrade", method = RequestMethod.POST)
	public String returnTrade(Long orderId, String sn, String memo, BigDecimal amount, String trackingNo, Long shippingMethodId, Long deliveryCorpId, String deliveryCorpName, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.findBySn(sn);
		if (trade == null) {
			addFlashMessage(redirectAttributes, Message.error("订单不存在"));
			return "redirect:view.jhtml?sn=" + orderService.find(orderId).getSn();
		}
		if (trade.isLocked(member)) {
			addFlashMessage(redirectAttributes, Message.success("订单被锁定"));
			return "redirect:view.jhtml?sn=" + orderService.find(orderId).getSn();
		}
		
		if (trade.getPaymentStatus().equals(PaymentStatus.unpaid) && trade.getShippingStatus().equals(ShippingStatus.unshipped)) {
			orderService.cancel(trade, member);
			addFlashMessage(redirectAttributes, Message.success("订单已取消"));
			return "redirect:view.jhtml?sn=" + orderService.find(orderId).getSn();
		}
		if (trade.isSpReturns() && trade.getPaymentStatus().equals(PaymentStatus.refundApply) && !trade.getShippingStatus().equals(ShippingStatus.shippedApply)) {
		    orderService.spReturns(trade, null, member);
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		} else {
			addFlashMessage(redirectAttributes, Message.error("该订单不能申请退货"));
		}
		return "redirect:view.jhtml?sn=" + orderService.find(orderId).getSn();
	}


	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		model.addAttribute("trade", order.getTrade(memberService.getCurrent().getTenant()));
		return "/b2c/member/trade/edit";
	}

	/**
	 * 签收
	 */
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	@ResponseBody
	public Message complete(String sn, ModelMap model) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.findBySn(sn);
		if (trade == null) {
			return ERROR_MESSAGE;
		}
		Order order = trade.getOrder();
		if (order != null && trade.getShippingStatus() == ShippingStatus.shipped) {
			if (order.isLocked(member)) {
				return Message.warn("b2b.member.order.locked");
			}
			orderService.sign(order,trade,member);
			return SUCCESS_MESSAGE;
		}
		return ERROR_MESSAGE;
	}

	/**
	 * 订单项添加
	 */
	@RequestMapping(value = "/order_item_add", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> orderItemAdd(String productSn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Product product = productService.findBySn(productSn);
		if (product == null) {
			data.put("message", Message.warn("admin.order.productNotExist"));
			return data;
		}
		if (!product.getIsMarketable()) {
			data.put("message", Message.warn("admin.order.productNotMarketable"));
			return data;
		}
		if (product.getIsOutOfStock()) {
			data.put("message", Message.warn("admin.order.productOutOfStock"));
			return data;
		}
		data.put("sn", product.getSn());
		data.put("fullName", product.getFullName());
		data.put("price", product.getPrice());
		data.put("weight", product.getWeight());
		data.put("isGift", product.getIsGift());
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> calculate(Trade trade, Long areaId, Long paymentMethodId, Long shippingMethodId) {
		Order order = trade.getOrder();
		Map<String, Object> data = new HashMap<String, Object>();
		for (Iterator<OrderItem> iterator = order.getOrderItems().iterator(); iterator.hasNext();) {
			OrderItem orderItem = iterator.next();
			if (orderItem == null || StringUtils.isEmpty(orderItem.getSn())) {
				iterator.remove();
			}
		}
		order.setArea(areaService.find(areaId));
		order.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		order.setShippingMethod(shippingMethodService.find(shippingMethodId));
		if (!isValid(order)) {
			data.put("message", Message.warn("admin.common.invalid"));
			return data;
		}
		Order pOrder = orderService.find(order.getId());
		Trade pTrade = tradeService.find(trade.getId());
		if (pOrder == null) {
			data.put("message", Message.error("admin.common.invalid"));
			return data;
		}
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getId() != null) {
				OrderItem pOrderItem = orderItemService.find(orderItem.getId());
				if (pOrderItem == null || !pOrder.equals(pOrderItem.getOrder())) {
					data.put("message", Message.error("admin.common.invalid"));
					return data;
				}
				Product product = pOrderItem.getProduct();
				if (product != null && product.getStock() != null) {
					if (pTrade.getIsAllocatedStock()) {
						if (orderItem.calculateQuantityIntValue() > product.getAvailableStock() + pOrderItem.calculateQuantityIntValue()) {
							data.put("message", Message.warn("admin.order.lowStock"));
							return data;
						}
					} else {
						if (orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
							data.put("message", Message.warn("admin.order.lowStock"));
							return data;
						}
					}
				}
			} else {
				Product product = productService.findBySn(orderItem.getSn());
				if (product == null) {
					data.put("message", Message.error("admin.common.invalid"));
					return data;
				}
				if (product.getStock() != null && orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
					data.put("message", Message.warn("admin.order.lowStock"));
					return data;
				}
			}
		}
		Map<String, Object> orderItems = new HashMap<String, Object>();
		for (OrderItem orderItem : order.getOrderItems()) {
			orderItems.put(orderItem.getSn(), orderItem);
		}
		order.setFee(pOrder.getFee());
		order.setPromotionDiscount(pOrder.getPromotionDiscount());
		order.setCouponDiscount(pOrder.getCouponDiscount());
		order.setAmountPaid(pOrder.getAmountPaid());
		data.put("weight", order.getWeight());
		data.put("price", order.getPrice());
		data.put("quantity", order.getQuantity());
		data.put("amount", order.getAmount());
		data.put("orderItems", orderItems);
		data.put("message", SUCCESS_MESSAGE);
		return data;
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Trade trade, Long areaId, Long paymentMethodId, Long shippingMethodId, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Admin admin = adminDao.findByUsername("admin");
		Order order = trade.getOrder();
		Trade pTrade = tradeService.find(trade.getId());
		for (Iterator<OrderItem> iterator = order.getOrderItems().iterator(); iterator.hasNext();) {
			OrderItem orderItem = iterator.next();
			if (orderItem == null || StringUtils.isEmpty(orderItem.getSn())) {
				iterator.remove();
			}
		}
		order.setArea(areaService.find(areaId));
		order.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		order.setShippingMethod(shippingMethodService.find(shippingMethodId));
		if (!isValid(order)) {
			return ERROR_VIEW;
		}
		Order pOrder = orderService.find(order.getId());
		if (pOrder == null) {
			return ERROR_VIEW;
		}
		if (pOrder.isExpired() || pOrder.getOrderStatus() != OrderStatus.unconfirmed) {
			return ERROR_VIEW;
		}
		if (pOrder.isLocked(member)) {
			return ERROR_VIEW;
		}
		if (!order.getIsInvoice()) {
			order.setInvoiceTitle(null);
			order.setTax(new BigDecimal(0));
		}
		for (OrderItem orderItem : order.getOrderItems()) {
			if (orderItem.getId() != null) {
				OrderItem pOrderItem = orderItemService.find(orderItem.getId());
				if (pOrderItem == null || !pOrder.equals(pOrderItem.getOrder())) {
					return ERROR_VIEW;
				}
				Product product = pOrderItem.getProduct();
				if (product != null && product.getStock() != null) {
					if (pTrade.getIsAllocatedStock()) {
						if (orderItem.calculateQuantityIntValue() > product.getAvailableStock() + pOrderItem.calculateQuantityIntValue()) {
							return ERROR_VIEW;
						}
					} else {
						if (orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
							return ERROR_VIEW;
						}
					}
				}
				BeanUtils.copyProperties(pOrderItem, orderItem, new String[] { "price", "quantity" });
				if (pOrderItem.getIsGift()) {
					orderItem.setPrice(new BigDecimal(0));
				}
			} else {
				Product product = productService.findBySn(orderItem.getSn());
				if (product == null) {
					return ERROR_VIEW;
				}
				if (product.getStock() != null && orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
					return ERROR_VIEW;
				}
				orderItem.setName(product.getName());
				orderItem.setFullName(product.getFullName());
				if (product.getIsGift()) {
					orderItem.setPrice(new BigDecimal(0));
				}
				orderItem.setWeight(product.getWeight());
				orderItem.setThumbnail(product.getThumbnail());
				orderItem.setIsGift(product.getIsGift());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnQuantity(0);
				orderItem.setProduct(product);
				orderItem.setTrade(trade);
				orderItem.setOrder(pOrder);
			}
		}
		order.setSn(pOrder.getSn());
		order.setOrderStatus(pOrder.getOrderStatus());
		order.setPaymentStatus(pOrder.getPaymentStatus());
		order.setShippingStatus(pOrder.getShippingStatus());
		order.setFee(pOrder.getFee());
		order.setPromotionDiscount(pOrder.getPromotionDiscount());
		order.setCouponDiscount(pOrder.getCouponDiscount());
		order.setAmountPaid(pOrder.getAmountPaid());
		order.setPromotion(pOrder.getPromotion());
		order.setExpire(pOrder.getExpire());
		order.setLockExpire(null);
		// order.setIsAllocatedStock(pOrder.getIsAllocatedStock());
		order.setOperator(null);
		order.setMember(pOrder.getMember());
		order.setCouponCode(pOrder.getCouponCode());
		order.setCoupons(pOrder.getCoupons());
		order.setOrderLogs(pOrder.getOrderLogs());
		order.setDeposits(pOrder.getDeposits());
		order.setPayments(pOrder.getPayments());
		order.setRefunds(pOrder.getRefunds());
		order.setShippings(pOrder.getShippings());
		order.setReturns(pOrder.getReturns());
		trade.setOperator(member);
		trade.setShippingStatus(pOrder.getShippingStatus());
		orderService.update(order, admin.getUsername());
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("orderStatus", orderStatus);
		model.addAttribute("paymentStatus", paymentStatus);
		model.addAttribute("shippingStatus", shippingStatus);
		model.addAttribute("hasExpired", hasExpired);
		model.addAttribute("page", tradeService.findPage(member, orderStatus, paymentStatus, shippingStatus, hasExpired, pageable));
		return "/b2c/member/trade/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		Member member = memberService.getCurrent();
		if (ids != null) {
			for (Long id : ids) {
				Order order = orderService.find(id);
				if (order != null && (order.getOrderType() != Order.OrderType.single || order.getOrderStatus() != Order.OrderStatus.unconfirmed || order.isLocked(member))) {
					return Message.error("admin.order.deleteLockedNotAllowed", order.getSn());
				}
			}
			orderService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	/**
	 * 买家评价
	 */
	@RequestMapping(value = "/review", method = RequestMethod.GET)
	public String review(String sn, Model model, RedirectAttributes redirectAttributes) {
		Trade trade = tradeService.findBySn(sn);
		if (trade != null && trade.getOrder() != null && trade.getShippingStatus() == ShippingStatus.accept && memberService.getCurrent().equals(trade.getOrder().getMember()) && !trade.getOrder().isExpired()) {
			model.addAttribute("trade", trade);
			return "/b2c/member/trade/review";
		}
		addFlashMessage(redirectAttributes, Message.error("订单信息错误"));
		return ERROR_VIEW;
	}

	/**
	 * 买家评价
	 */
	/**
	@RequestMapping(value = "/reviewSubmit", method = RequestMethod.POST)
	public String reviewSubmit(String sn, ArrayOrderItemReview orderItemReviews, net.wit.entity.Review.Type type, String content, String productContent, Integer score, Integer assistant, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Trade trade = tradeService.findBySn(sn);
		if (trade != null && trade.getOrder() != null && trade.getShippingStatus() == ShippingStatus.accept && memberService.getCurrent().equals(trade.getOrder().getMember()) && !trade.getOrder().isExpired()) {
			Review review = new Review();
			review.setType(type);
			review.setScore(score);
			review.setAssistant(assistant);
			review.setIp(request.getRemoteAddr().toString());
			review.setContent(content);
			review.setFlag(Flag.trade);
			review.setIsShow(Boolean.TRUE);
			review.setTenant(trade.getTenant());
			review.setMember(memberService.getCurrent());
			review.setMemberTrade(trade);
			reviewService.save(review);
			try {

				List<OrderItem> orderItems = new ArrayList<OrderItem>(trade.getOrderItems());

				for (OrderItemReview orderItemReview : orderItemReviews.getOrderItemReviews()) {
					OrderItem orderItem = orderItemService.find(orderItemReview.getOrderItemId());
					if (orderItemReview.getScore() != null) {
						if (trade.getOrderItems().contains(orderItem)) {
							orderItems.remove(orderItem);
						}
						Review reviewItem = new Review();
						reviewItem.setIp(request.getRemoteAddr().toString());
						reviewItem.setFlag(Flag.product);
						reviewItem.setScore(orderItemReview.getScore());
						reviewItem.setContent(orderItemReview.getContent());
						reviewItem.setMember(memberService.getCurrent());
						reviewItem.setProduct(orderItem.getProduct());
						reviewItem.setIsShow(Boolean.TRUE);
						reviewItem.setTenant(orderItem.getProduct().getTenant());
						reviewItem.setOrderItem(orderItem);
						reviewService.save(reviewItem);
					}
				}

				for (OrderItem orderItem : orderItems) {
					Review reviewItem = new Review();
					reviewItem.setFlag(Flag.product);
					reviewItem.setScore(score);
					reviewItem.setIp(request.getRemoteAddr().toString());
					reviewItem.setContent(productContent);
					reviewItem.setMember(memberService.getCurrent());
					reviewItem.setProduct(orderItem.getProduct());
					reviewItem.setIsShow(Boolean.TRUE);
					reviewItem.setTenant(orderItem.getProduct().getTenant());
					reviewItem.setOrderItem(orderItem);
					reviewService.save(reviewItem);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			trade.setMemberReview(review);
			tradeService.update(trade);
			addFlashMessage(redirectAttributes, Message.success("评价成功"));
			return "redirect:/b2c/member/order/view.jhtml?sn=" + trade.getOrder().getSn();
		}
		addFlashMessage(redirectAttributes, Message.error("评价信息错误"));
		return "redirect:/b2c/member/order/list.jhtml";
	}
	**/

	/**
	 * 买家评价
	 */
	@RequestMapping(value = "/memberReview", method = RequestMethod.POST)
	public String memberReview(Long orderId, String sn, String content, Integer score, Integer assistant, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Trade trade = tradeService.findBySn(sn);
		if (trade != null && trade.getOrder() != null && trade.getShippingStatus() == ShippingStatus.accept && memberService.getCurrent().equals(trade.getOrder().getMember()) && !trade.getOrder().isExpired()) {
			Review review = new Review();
			review.setContent(content);
			review.setMember(memberService.getCurrent());
			review.setMemberTrade(trade);
			review.setFlag(Flag.trade);
			review.setScore(score);
			review.setAssistant(assistant);
			review.setIp(request.getRemoteAddr().toString());
			review.setMember(memberService.getCurrent());
			review.setTenant(trade.getTenant());
			review.setMemberTrade(trade);
			reviewService.save(review);
			addFlashMessage(redirectAttributes, Message.success("评价成功"));
			return "redirect:/b2c/member/order/view.jhtml?sn=" + orderService.find(orderId).getSn();
		}
		addFlashMessage(redirectAttributes, Message.error("评价信息错误"));
		return "redirect:/b2c/member/order/view.jhtml?sn=" + orderService.find(orderId).getSn();
	}

	/**
	 * 调转提货码页面
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("content", "0");
		if (member.getTenant() != null) {
			model.addAttribute("telephone", member.getTenant().getTelephone());
		}
		return "/b2c/member/trade/search";
	}

	/**
	 * 查询提货信息
	 */
	@RequestMapping(value = "/searchSn", method = RequestMethod.GET)
	public String searchSn(String sn, RedirectAttributes redirectAttributes, ModelMap model) {
		model.addAttribute("sn", sn);
		Trade trade = tradeService.findBySn(sn);
		if (trade == null) {
			model.addAttribute("content", "1");
			return "/b2c/member/trade/search";
		}
		Order order = orderService.find(trade.getOrder().getId());
		model.addAttribute("order", order);
		return "/b2c/member/trade/search";
	}

	/**
	 * 提货
	 */
	@RequestMapping(value = "/shippingBysn", method = RequestMethod.POST)
	public String shipping(String searchSn, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long deliveryCenterId, Long areaId, Shipping shipping, RedirectAttributes redirectAttributes, ModelMap model) {
		Member member = memberService.getCurrent();
		Order order = orderService.find(orderId);
		if (order == null) {
			return ERROR_VIEW;
		}
		for (Iterator<ShippingItem> iterator = shipping.getShippingItems().iterator(); iterator.hasNext();) {
			ShippingItem shippingItem = iterator.next();
			if (shippingItem == null || StringUtils.isEmpty(shippingItem.getSn()) || shippingItem.getQuantity() == null || shippingItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(shippingItem.getSn()); // 获取的OrderItem为同一个OrderItem
			if (orderItem == null || shippingItem.getQuantity() > orderItem.getQuantity() - orderItem.getShippedQuantity()) {
				return ERROR_VIEW;
			}
			if (orderItem.getProduct() != null && orderItem.getProduct().getStock() != null && shippingItem.getQuantity() > orderItem.getProduct().getStock()) {
				return ERROR_VIEW;
			}
			shippingItem.setName(orderItem.getFullName());
			shippingItem.setShipping(shipping);

			shipping.setTrade(orderItem.getTrade());
		}
		shipping.setOrder(order);
		ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
		shipping.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
		DeliveryCorp deliveryCorp = deliveryCorpService.find(deliveryCorpId);
		shipping.setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName() : null);
		shipping.setDeliveryCorpUrl(deliveryCorp != null ? deliveryCorp.getUrl() : null);
		shipping.setDeliveryCorpCode(deliveryCorp != null ? deliveryCorp.getCode() : null);
		Area area = areaService.find(areaId);
		shipping.setArea(area != null ? area.getFullName() : null);

		// 默认发货地
		shipping.setDeliveryCenter(deliveryCenterService.findDefault(member.getTenant()));
		if (order.isExpired() || order.getOrderStatus() != OrderStatus.confirmed) {
			return ERROR_VIEW;
		}
		if (order.getShippingStatus() != ShippingStatus.unshipped && order.getShippingStatus() != ShippingStatus.partialShipment) {
			return ERROR_VIEW;
		}
		shipping.setSn(snService.generate(Sn.Type.shipping));
		shipping.setOperator(member.getUsername());
		orderService.shipping(order, shipping, member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		model.addAttribute("order", order);
		model.addAttribute("sn", searchSn);
		return "redirect:/b2c/member/trade/searchSn.jhtml?sn=" + searchSn;
	}
}