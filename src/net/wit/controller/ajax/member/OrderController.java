/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Cart;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderSource;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.PaymentMethod;
import net.wit.entity.Receiver;
import net.wit.entity.Shipping;
import net.wit.entity.ShippingMethod;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.AreaService;
import net.wit.service.CartService;
import net.wit.service.CouponCodeService;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PaymentMethodService;
import net.wit.service.PluginService;
import net.wit.service.ReceiverService;
import net.wit.service.ShippingMethodService;
import net.wit.service.ShippingService;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxMemberOrderController")
@RequestMapping("/ajax/member/order")
public class OrderController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "paymentMethodServiceImpl")
	private PaymentMethodService paymentMethodService;

	@Resource(name = "shippingMethodServiceImpl")
	private ShippingMethodService shippingMethodService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;


	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody
	Message lock(String sn) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && !order.isExpired() && !order.isLocked(null) && order.getPaymentMethod() != null
				&& order.getPaymentMethod().getMethod() == PaymentMethod.Method.online
				&& (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment)) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(member);
			orderService.update(order);
			return Message.success("ajax.order.locked");
		}
		return Message.error("ajax.order.locked.fail");
	}

	/**
	 * 检查支付
	 */
	@RequestMapping(value = "/check_payment", method = RequestMethod.POST)
	public @ResponseBody
	Message checkPayment(String sn) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Order order = orderService.findBySn(sn);
		if (order != null && memberService.getCurrent().equals(order.getMember()) && order.getPaymentStatus() == PaymentStatus.paid) {
			return Message.success("ajax.order.payment.success");
		}
		return Message.error("ajax.order.payment.not");
	}

	/**
	 * 优惠券信息
	 */
	@RequestMapping(value = "/coupon_info", method = RequestMethod.POST)
	public @ResponseBody
	Message couponInfo(String code) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Cart cart = cartService.getCurrent();
		if (cart == null || cart.isEmpty()) {
			return Message.error("ajax.cart.notEmpty");
			
		}
		if (!cart.isCouponAllowed()) {
			return Message.error("ajax.order.couponNotAllowed");
			
		}
		CouponCode couponCode = couponCodeService.findByCode(code);
		if (couponCode != null && couponCode.getCoupon() != null) {
			Coupon coupon = couponCode.getCoupon();
			if (!coupon.getIsEnabled()) {
				return Message.error("ajax.order.couponDisabled");
				
			}
			if (couponCode.hasExpired()) {
				return Message.error("ajax.order.couponHasExpired");
				
			}
			if (!cart.isValid(coupon)) {
				return Message.error("ajax.order.couponInvalid");
				
			}
			if (couponCode.getIsUsed()) {
				return Message.error("ajax.order.couponCodeUsed");
			}
			data.put("message", SUCCESS_MESSAGE);
			data.put("couponName", coupon.getName());
			return Message.success(JsonUtils.toJson(data));
		} else {
			data.put("message", Message.error("ajax.order.couponCodeNotExist"));
			return Message.success(JsonUtils.toJson(data));
		}
	}


	/**
	 * 计算支付金额
	 */
	@RequestMapping(value = "/calculate_amount", method = RequestMethod.POST)
	public @ResponseBody
	Message calculateAmount(String paymentPluginId, String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Order order = orderService.findBySn(sn);
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (order == null || !memberService.getCurrent().equals(order.getMember()) || order.isExpired() || order.isLocked(null) || order.getPaymentMethod() == null
				|| order.getPaymentMethod().getMethod() == PaymentMethod.Method.offline || paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return ERROR_MESSAGE;
			
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", new BigDecimal(0));
		data.put("amount", order.getAmountPayable());
		return Message.success(JsonUtils.toJson(data));
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Integer pageNumber) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Order> page = orderService.findPage(member, pageable);
		return Message.success(JsonUtils.toJson(page));
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public Message view(String sn) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Order order = orderService.findBySn(sn);
		if (order == null) {
			return Message.error("ajax.member.order.notExist");
			
		}
		if (!member.getOrders().contains(order)) {
			return Message.error("ajax.member.order.notExist");
		}
		return Message.success(JsonUtils.toJson(order));
	}

	/**
	 * 取消
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public @ResponseBody
	Message cancel(String sn) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Order order = orderService.findBySn(sn);
		if (order != null && member.equals(order.getMember()) && !order.isExpired() && order.getOrderStatus() == OrderStatus.unconfirmed
				&& order.getPaymentStatus() == PaymentStatus.unpaid) {
			if (order.isLocked(null)) {
				return Message.error("ajax.member.order.locked");
			}
			orderService.cancel(order, null);
			return SUCCESS_MESSAGE;
		}
		return ERROR_MESSAGE;
	}

	/**
	 * 物流动态
	 */
	@RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
	public @ResponseBody
	Message deliveryQuery(String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		Shipping shipping = shippingService.findBySn(sn);
		Setting setting = SettingUtils.get();
		if (shipping != null && shipping.getOrder() != null && memberService.getCurrent().equals(shipping.getOrder().getMember())
				&& StringUtils.isNotEmpty(setting.getKuaidi100Key()) && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode()) && StringUtils.isNotEmpty(shipping.getTrackingNo())) {
			data = shippingService.query(shipping);
		}
		return Message.success(JsonUtils.toJson(data));
	}

}