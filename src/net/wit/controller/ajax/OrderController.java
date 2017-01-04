/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.PaymentMethod;
import net.wit.entity.ShippingMethod;
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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 订单
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxOrderController")
@RequestMapping("/ajax/order")
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

	@RequestMapping(value = "/get_order_count", method = RequestMethod.GET)
	@ResponseBody
	public Message getOrderCount(String username,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.findByUsername(username);
		data.put("count", orderService.countmy(member, OrderStatus.confirmed, null, null, null));
		return Message.success(JsonUtils.toJson(data));
	}

	
	/**
	 * 获取支付方式
	 */
	@RequestMapping(value = "/getPaymentMethod", method = RequestMethod.GET)
	@ResponseBody
	public Message getPaymentMethod() {
		List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
		return Message.success(JsonUtils.toJson(paymentMethods));
	}
	/**
	 * 获取配送方式
	 */
	@RequestMapping(value = "/getShippingMethod", method = RequestMethod.GET)
	@ResponseBody
	public Message getShippingMethod() {
		List<ShippingMethod> shippingMethods = shippingMethodService.findAll();
		return Message.success(JsonUtils.toJson(shippingMethods));
	}

}