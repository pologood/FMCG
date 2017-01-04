/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Setting;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Pageable;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.PaymentModel;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.plugin.PaymentPlugin;
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.service.BankInfoService;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;
import net.wit.util.Base64Util;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberPaymentController")
@RequestMapping("/assistant/member/payment")
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

	@Resource(name = "bankInfoServiceImpl")
	private BankInfoService bankInfoService;

	Map<String, String> parameterMap = new HashMap<String, String>();

	Map<String,String> customerInfoMap = new HashMap<String,String>();

	public Map<String, String> getCustomerInfoMap() {
		return customerInfoMap;
	}

	public void setCustomerInfoMap(Map<String, String> customerInfoMap) {
		this.customerInfoMap = customerInfoMap;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	/**
	 * 充值
	 * params amount 充值金额
	 */
	@RequestMapping(value = "/fill/owner", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock fillOwner(BigDecimal amount) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(net.wit.controller.app.model.DataBlock.SESSION_INVAILD);
		}
		Setting setting = SettingUtils.get();
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
			return DataBlock.error("无效金额");
		}
		Payment payment = new Payment();
		payment.setSn(snService.generate(Sn.Type.payment));
		payment.setType(Payment.Type.recharge);
		payment.setMethod(Method.online);
		payment.setStatus(Status.wait);
		payment.setPaymentMethod("");
		payment.setMemo("账户充值");
		payment.setFee(BigDecimal.ZERO);
		payment.setAmount(amount.setScale(2));
		payment.setPaymentPluginId(null);
		payment.setExpire(DateUtils.addMinutes(new Date(),3600));
		payment.setMember(member);
		paymentService.save(payment);
		return DataBlock.success(payment.getSn(),"执行成功");
	}
	/**
	 * 1.打开付款单
	 * sn 付款单号
	 */
	@RequestMapping(value = "/view", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock view(String sn,HttpServletRequest request) {
		Payment  payment = paymentService.findBySn(sn);
		PaymentModel model = new PaymentModel();
		model.copyFrom(payment);
		return DataBlock.success(model,"执行成功");
	}

	/**
	 * 4.余额支付
	 * sn 付款单号
	 * password 支付密码 BASE64加密
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/wallet", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock wallet(String sn,String password,HttpServletRequest request) {
		String pwd = Base64Util.decode(password);
		Payment payment = paymentService.findBySn(sn);
		if (payment==null) {
			return DataBlock.error("无效订单号");
		}
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (pwd==null) {
			return DataBlock.error("无效的支付密码");
		}
		if (!pwd.equals(member.getPaymentPassword())) {
			return DataBlock.error("无效的支付密码");
		}
		if (payment.getType().equals(Payment.Type.recharge)) {
			return DataBlock.error("充值业务不能使用余额支付");
		}
		if (payment.getAmount().compareTo(member.getBalance())>0) {
			return DataBlock.error("钱包余额不足，不能完成付款");
		}
		if (!payment.getStatus().equals(Status.wait)) {
			return DataBlock.error("已经重复发起付款操作。");
		}
		payment.setMethod(Payment.Method.deposit);
		payment.setPaymentMethod("钱包支付");
		payment.setPaymentPluginId("balancePayPlugin");
		paymentService.update(payment);
		try {
			BigDecimal amount = payment.getAmount();
			paymentService.handle(payment);
			if (payment.getAmount().equals(BigDecimal.ZERO)) {
				payment.setAmount(amount);
				payment.setStatus(Status.wait);
				paymentService.update(payment);
				return DataBlock.error("钱包余额不足，不能完成付款");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DataBlock.error("付款失败");
		}
		return DataBlock.success("success","付款成功");
	}
	/**
	 * 4.微信支付
	 * @throws UnsupportedEncodingException
	 * sn 付款单号
	 */
	@RequestMapping(value = "/weixin", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock weixin(String sn,String paymentPluginId,HttpServletRequest request) {
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return DataBlock.error(DataBlock.SESSION_INVAILD);
			}

			if (paymentPluginId==null) {
				paymentPluginId = "weixinNativePayPlugin";
			}

			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
				return DataBlock.error("支付插件无效");
			}
			Payment payment = paymentService.findBySn(sn);
			String description = payment.getMemo();
			payment.setMethod(Method.online);
			payment.setPaymentMethod(paymentPlugin.getPaymentName());
			payment.setPaymentPluginId(paymentPluginId);
			paymentService.update(payment);

			Map<String, Object> data = paymentPlugin.getParameterMap(payment.getSn(), description, request, "/assistant");
			if ("".equals(data.get("code_url"))) {
				return DataBlock.error("提交微信服务器失败");
			} else {
				return DataBlock.success(data,"执行成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataBlock.error("提交微信异常");
	}

}