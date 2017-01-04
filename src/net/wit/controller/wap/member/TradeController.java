/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.wap.BaseController;
import net.wit.dao.AdminDao;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.OrderItem;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Status;
import net.wit.entity.PaymentMethod;
import net.wit.entity.Product;
import net.wit.entity.Review;
import net.wit.entity.Review.Flag;
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
import net.wit.service.ReviewService;
import net.wit.service.ShippingMethodService;
import net.wit.service.SnService;
import net.wit.service.SpReturnsService;
import net.wit.service.TradeService;

/**
 * Controller - 订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapMemberTradeController")
@RequestMapping("/wap/member/trade")
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

	@Resource(name = "spReturnsServiceImpl")
	private SpReturnsService spReturnsService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

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
		trade.setLockExpire(DateUtils.addSeconds(new Date(), 20));
		trade.setOperator(member);
		tradeService.update(trade);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(String sn, ModelMap model) {
		Trade trade = tradeService.findBySn(sn);
		if (trade == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("trade", trade);
		return "/wap/member/trade/view";
	}

	/**
	 * 取消
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancel(Long id, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.find(id);
		Order order = trade.getOrder();
		if (!order.canCancel()) {
			addFlashMessage(redirectAttributes, Message.error("执行中的订单不能取消"));
			return "redirect:view.jhtml?id=" + id;
		}
		if (!order.isLocked(member) && !trade.isLocked(member)) {
			orderService.cancel(order, null);
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		} else {
			addFlashMessage(redirectAttributes, Message.warn("admin.common.invalid"));
		}
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 支付
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String payment(Long id, Long paymentMethodId, Payment payment, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.find(id);
		Order order = trade.getOrder();
		if (order.getOrderType() != Order.OrderType.single) {
			addFlashMessage(redirectAttributes, Message.error("多商家组合订单不能线下付款"));
			return "redirect:view.jhtml?id=" + id;
		}
		payment.setOrder(order);
		PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
		payment.setPaymentMethod(paymentMethod != null ? paymentMethod.getName() : null);
		if (paymentMethod.getMethod() != PaymentMethod.Method.offline) {
			return ERROR_VIEW;
		}
		if (!isValid(payment)) {
			return ERROR_VIEW;
		}
		if (order.isExpired() || order.getOrderStatus() != OrderStatus.confirmed) {
			return ERROR_VIEW;
		}
		if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
			return ERROR_VIEW;
		}
		if (payment.getAmount().compareTo(new BigDecimal(0)) <= 0 || payment.getAmount().compareTo(order.getAmountPayable()) > 0) {
			return ERROR_VIEW;
		}
		if (payment.getMethod() == Payment.Method.deposit && payment.getAmount().compareTo(member.getBalance()) > 0) {
			return ERROR_VIEW;
		}
		if (trade.isLocked(member)) {
			return ERROR_VIEW;
		}
		if (order.isLocked(member)) {
			return ERROR_VIEW;
		}
		payment.setSn(snService.generate(Sn.Type.payment));
		payment.setType(net.wit.entity.Payment.Type.payment);
		payment.setStatus(Status.success);
		payment.setFee(new BigDecimal(0));
		payment.setOperator(member.getUsername());
		payment.setPaymentDate(new Date());
		payment.setPaymentPluginId(null);
		payment.setExpire(null);
		payment.setDeposit(null);
		payment.setMember(null);
		orderService.payment(order, payment, null);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view.jhtml?id=" + id;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		Order order = orderService.find(id);
		model.addAttribute("order", order);
		model.addAttribute("trade", order.getTrade(memberService.getCurrent().getTenant()));
		return "/wap/member/trade/edit";
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
		data.put("price", product.calcEffectivePrice(memberService.getCurrent()));
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
		Trade pTrade = tradeService.find(trade.getId());
		Order pOrder = orderService.find(order.getId());
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
	public String update(Trade trade, Long areaId, String zipCode, String phone, String memo, Boolean isInvoice, String invoiceTitle, String consignee, String address, RedirectAttributes redirectAttributes) {
		try {
			Member member = memberService.getCurrent();
			Trade tradeOld = tradeService.find(trade.getId());
			Order order = tradeOld.getOrder();
			if (order == null) {
				return ERROR_VIEW;
			}
			if (order.isExpired() || order.getOrderStatus() != OrderStatus.unconfirmed) {
				return ERROR_VIEW;
			}
			if (tradeOld.isLocked(member)) {
				return ERROR_VIEW;
			}
			for (Iterator<OrderItem> iterator = trade.getOrderItems().iterator(); iterator.hasNext();) {
				OrderItem orderItem = iterator.next();
				if (orderItem == null || StringUtils.isEmpty(orderItem.getSn())) {
					iterator.remove();
				}
			}
			order.setArea(areaService.find(areaId));
			if (isInvoice) {
				order.setIsInvoice(true);
				order.setInvoiceTitle(invoiceTitle);
			} else {
				order.setIsInvoice(false);
				order.setInvoiceTitle(null);
			}
			order.setConsignee(consignee);
			order.setAddress(address);
			order.setZipCode(zipCode);
			order.setMemo(memo);
			order.setPhone(phone);

			order.setLockExpire(null);
			order.setOperator(null);

			for (OrderItem orderItem : trade.getOrderItems()) {
				if (orderItem.getId() != null) {
					OrderItem pOrderItem = orderItemService.find(orderItem.getId());
					if (pOrderItem == null) {
						return ERROR_VIEW;
					}
					Product product = pOrderItem.getProduct();
					if (product != null && product.getStock() != null) {
						if (tradeOld.getIsAllocatedStock()) {
							if (orderItem.calculateQuantityIntValue() > product.getAvailableStock() + pOrderItem.calculateQuantityIntValue()) {
								return ERROR_VIEW;
							}
						} else {
							if (orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
								return ERROR_VIEW;
							}
						}
					}
					pOrderItem.setPrice(orderItem.getPrice());
					pOrderItem.setQuantity(orderItem.getQuantity());
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
					orderItem.setTrade(tradeOld);
					orderItem.setOrder(order);
					tradeOld.getOrderItems().add(orderItem);
				}
			}

			orderService.update(order, member.getUsername());
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			return "redirect:list.jhtml";
		} catch (RuntimeException e) {
			e.printStackTrace();
			return ERROR_VIEW;
		}
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
		return "/wap/member/trade/list";
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
	 * 退货申请
	 */
	@RequestMapping(value = "/returnTrade", method = RequestMethod.POST)
	public @ResponseBody Message returnTrade(String sn) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.findBySn(sn);
		if (trade == null) {
			return Message.error("订单不存在");
		}
		if (trade.isLocked(member)) {
			return Message.error("订单被锁定");
		}
		
		if (trade.getPaymentStatus().equals(PaymentStatus.unpaid) && trade.getShippingStatus().equals(ShippingStatus.unshipped)) {
			orderService.cancel(trade, member);
			return Message.success("订单已取消");
		}
		if (trade.isSpReturns() && !trade.getPaymentStatus().equals(PaymentStatus.refundApply) && !trade.getShippingStatus().equals(ShippingStatus.shippedApply)) {
		    orderService.spReturns(trade, null, member);
			return SUCCESS_MESSAGE;
		} else {
			return Message.error("该订单不能申请退货");

		}
	}

	/**
	 * 签收
	 */
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public @ResponseBody Message complete(String sn) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.findBySn(sn);
		if (trade == null) {
			return Message.error("订单不存在");
		}
		Order order = trade.getOrder();
		if (order != null && trade.getShippingStatus() == ShippingStatus.shipped && memberService.getCurrent().equals(order.getMember()) && !order.isExpired()) {
			if (order.isLocked(member)) {
//				return Message.warn("wap.member.order.locked");
				return Message.warn("订单已锁定");
			}
			orderService.sign(order, trade, member);
			return SUCCESS_MESSAGE;
		}
		return Message.error("订单不允许签收");
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
			return "redirect:/wap/member/order/view.jhtml?sn=" + trade.getOrder().getSn();
		}
		addFlashMessage(redirectAttributes, Message.error("评价信息错误"));
		return "redirect:/wap/member/order/list.jhtml";
	}
    **/
	/**
	 * 买家评价
	 */
	@RequestMapping(value = "/review", method = RequestMethod.GET)
	public String review(String sn, Model model, RedirectAttributes redirectAttributes) {
		Trade trade = tradeService.findBySn(sn);
		Order order = trade.getOrder();
		if (order == null) {
			return ERROR_VIEW;
		}
		if (trade.getOrderStatus() == OrderStatus.completed && memberService.getCurrent().equals(trade.getOrder().getMember()) && !trade.getOrder().isExpired()) {
			model.addAttribute("trade", trade);
			return "/wap/member/trade/review";
		}
		return ERROR_VIEW;
	}

	/**
	 * 买家评价
	 */
	@RequestMapping(value = "/review_detail", method = RequestMethod.GET)
	public String review_detail(String sn, Model model) {
		Member member = memberService.getCurrent();
		Trade trade = tradeService.findBySn(sn);
		if (member == null) {
			return ERROR_VIEW;
		}
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("memberTrade", Operator.eq, trade));
		filters.add(new Filter("flag", Operator.eq, Flag.trade));
		filters.add(new Filter("isShow", Operator.eq, Boolean.TRUE));
		filters.add(new Filter("member", Operator.eq, member));
		List<Review> memberReviews = reviewService.findList(null, filters, null);
		if (memberReviews != null && memberReviews.size() > 0) {
			Map<Review, List<Review>> reviews = new HashMap<Review, List<Review>>();
			List<Review> list = new ArrayList<Review>();
			List<Filter> filtersMemberItem = new ArrayList<Filter>();
			filtersMemberItem.add(new Filter("flag", Operator.eq, Flag.product));
			filtersMemberItem.add(new Filter("isShow", Operator.eq, Boolean.TRUE));
			filtersMemberItem.add(new Filter("member", Operator.eq, member));
			filtersMemberItem.add(new Filter("orderItem", Operator.in, trade.getOrderItems()));
			list = reviewService.findList(null, filtersMemberItem, null);
			reviews.put(memberReviews.get(0), list);
			model.addAttribute("memberReview", reviews);
		}

		List<Filter> filters1 = new ArrayList<Filter>();
		filters1.add(new Filter("memberTrade", Operator.eq, trade));
		filters1.add(new Filter("flag", Operator.eq, Flag.trade));
		filters1.add(new Filter("tenant", Operator.eq, trade.getTenant()));
		List<Review> tenantReviews = reviewService.findList(null, filters1, null);
		if (tenantReviews != null && tenantReviews.size() > 0) {
			model.addAttribute("tenantReview", tenantReviews.get(0));
		}

		return "/wap/member/trade/review_detail";
	}
}