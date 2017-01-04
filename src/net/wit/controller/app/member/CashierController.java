/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.CreditService;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;
import net.wit.util.SettingUtils;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("appMemberCashierController")
@RequestMapping("/app/member/cashier")
public class CashierController extends BaseController {

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
	
	@Resource(name = "creditServiceImpl")
	private CreditService creditService;
	
	/**
	 * 提交  
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(String paymentPluginId,BigDecimal amount, HttpServletRequest request) {
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return DataBlock.error(DataBlock.SESSION_INVAILD);
			}

			Tenant tenant = member.getTenant();
			if (tenant==null) {
				return DataBlock.error(DataBlock.TENANT_INVAILD);
			}
			Member owner = tenant.getMember();
			
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
				return DataBlock.error("支付插件无效");
			}
			Payment payment = new Payment();
			String description = "线下代收";
			Setting setting = SettingUtils.get();
			if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				return DataBlock.error("amount.error");
			}
			payment.setMember(owner);
			payment.setPayer(member.getName());
			payment.setMemo("线下代收");
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.cashier);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(paymentPlugin.getPaymentName());
			payment.setFee(paymentPlugin.calculateFee(amount.setScale(2)));
			payment.setAmount(amount.setScale(2));
			payment.setPaymentPluginId(paymentPluginId);
			payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
			paymentService.save(payment);
			
			if (true ) { // (paymentPluginId.equals("weixinQrcodePayPlugin")) {
				Map<String, Object> data = paymentPlugin.getParameterMap(payment.getSn(), description, request, "/wap");
				data.put("sn",payment.getSn());
	            if ("".equals(data.get("code_url"))) {
				   return DataBlock.error("提交微信服务器失败");
	            } else {
				   return DataBlock.success(data,"执行成功");
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataBlock.error("提交支付异常");
	}
	
	/**
	 * 提交
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock query(String sn, HttpServletRequest request) {
		Payment payment = paymentService.findBySn(sn);
		try {
			String paymentPluginId = payment.getPaymentPluginId();
			Member member = memberService.getCurrent();
			if (member == null) {
				return DataBlock.error(DataBlock.SESSION_INVAILD);
			}

			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
				return DataBlock.error("支付插件无效");
			}
				String resp = paymentPlugin.queryOrder(payment);
				if (resp.equals("0000")) {
					return DataBlock.success("success","收款成功");
				};
				if (resp.equals("0001")) {
					return DataBlock.error("支付失败");
				};
				if (resp.equals("9999")) {
					return DataBlock.warn("等待支付");
				};
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataBlock.error("查询出错");
	}
	
	/**
	 * sumer
	 */
	@RequestMapping(value = "/sumer", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock sumer(){
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		List<Filter> filters = new ArrayList<Filter>();
		List<net.wit.entity.Credit.Status> status=new ArrayList<net.wit.entity.Credit.Status>();
		status.add(net.wit.entity.Credit.Status.wait);
		status.add(net.wit.entity.Credit.Status.wait_success);
		filters.add(new Filter("status", Operator.in, status));
		filters.add(new Filter("member", Operator.eq, member));
		List<Credit> list=creditService.findList(null, filters,null);
		BigDecimal amount=new BigDecimal(0);
		for (Credit credit : list) {
			amount=amount.add(credit.getAmount());
		}
		return DataBlock.success(amount,"执行成功");
	}

	/**
	 * sumer
	 */
	@RequestMapping(value = "/sumer/owner", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock sumerOwner(){
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		Member owner = tenant.getMember();
		List<Filter> filters = new ArrayList<Filter>();
		List<net.wit.entity.Credit.Status> status=new ArrayList<net.wit.entity.Credit.Status>();
		status.add(net.wit.entity.Credit.Status.wait);
		status.add(net.wit.entity.Credit.Status.wait_success);
		filters.add(new Filter("status", Operator.in, status));
		filters.add(new Filter("member", Operator.eq, owner));
		List<Credit> list=creditService.findList(null, filters,null);
		BigDecimal amount=new BigDecimal(0);
		for (Credit credit : list) {
			amount=amount.add(credit.getAmount());
		}
		return DataBlock.success(amount,"执行成功");
	}
	
	
}