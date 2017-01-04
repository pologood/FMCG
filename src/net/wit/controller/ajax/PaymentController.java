/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.entity.PaymentMethod;
import net.wit.entity.Sn;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxPaymentController")
@RequestMapping("/ajax/payment")
public class PaymentController extends BaseController {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	/**
	 * 提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public Message submit(Type type, String paymentPluginId, String sn, BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		paymentPluginId = "chinapayMobilePlugin";
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin("chinapayMobilePlugin");
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return Message.error("ajax.payment.not.plugin");
		}
		Payment payment = new Payment();
		String description = null;
		if (type == Type.payment) {
			Order order = orderService.findBySn(sn);
			if (order == null || !member.equals(order.getMember()) || order.isExpired() || order.isLocked(null)) {
				return Message.error("ajax.order.isExpired");
			}
			if (order.getPaymentMethod() == null || order.getPaymentMethod().getMethod() != PaymentMethod.Method.online) {
				return Message.error("ajax.payment.not.online");
			}
			if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
				return Message.error("ajax.order.not.status");
			}
			if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
				return Message.error("ajax.payment.not.payable");
			}
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.payment);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(order.getPaymentMethodName() + Payment.PAYMENT_METHOD_SEPARATOR + paymentPlugin.getPaymentName());
			// payment.setFee(new BigDecimal(0));
			payment.setFee(paymentPlugin.calculateFee(order.getAmountPayable()));
			payment.setAmount(order.getAmountPayable());
			// payment.setAmount(paymentPlugin.calculateAmount(order.getAmountPayable()));
			payment.setPaymentPluginId("chinapayMobilePlugin");
			payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
			payment.setOrder(order);
			paymentService.save(payment);
			description = order.getName();
		} else if (type == Type.recharge) {
			Setting setting = SettingUtils.get();
			if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				return Message.error("ajax.payment.amount.error");
			}
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.recharge);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(paymentPlugin.getPaymentName());
			payment.setFee(paymentPlugin.calculateFee(amount.setScale(2)));
			payment.setAmount(amount.setScale(2));
			payment.setPaymentPluginId("chinapayMobilePlugin");
			payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
			payment.setMember(member);
			paymentService.save(payment);
			description = message("ajax.member.deposit.recharge");
		} else {
			return Message.error("ajax.payment.type.error");
		}
		String resp = paymentPlugin.post(paymentPlugin.getRequestUrl(), paymentPlugin.getParameterMap(payment.getSn(), description, request, "/ajax"));
		String[] resps = resp.split("&");
		String tn = "";
		if (ArrayUtils.contains(resps, "respCode=00")) {
			for (String arg : resps) {
				if (arg.substring(0, 3).equals("tn=")) {
					tn = arg;
					break;
				}
			}
			return Message.success(tn);
		} else
			return Message.error("提交银行出错");
	}
}