/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.BuyApp;
import net.wit.entity.BuyApp.Type;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Sn;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.BuyAppService;
import net.wit.service.MemberService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
 
/**
 * Controller - Application
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("apiAppController")
@RequestMapping("/api/app")
public class ApplicationController extends BaseController  {
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "buyAppServiceImpl")
	private BuyAppService buyAppService;
	/**
	 * 购买APP
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/buy", method = RequestMethod.GET)
	@ResponseBody
	public Message buy(String username,Type type,String code,String name,BigDecimal quantity, BigDecimal amount,String paymentPluginId) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return Message.error("not.find.username");
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return Message.error("not.plugin");
		}
		Payment payment = new Payment();
		String description = null;
		Setting setting = SettingUtils.get();
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15
				|| amount.scale() > setting.getPriceScale()) {
			return Message.error("amount.error");
		}
		payment.setSn(snService.generate(Sn.Type.payment));
		payment.setType(Payment.Type.recharge);
		payment.setMethod(Method.online);
		payment.setStatus(Status.wait);
		payment.setPaymentMethod(paymentPlugin.getPaymentName());
		payment.setFee(paymentPlugin.calculateFee(amount.setScale(2)));
		payment.setAmount(amount.setScale(2));
		payment.setPaymentPluginId(paymentPluginId);
		payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin
				.getTimeout()) : null);
		payment.setMember(member);
		paymentService.save(payment);
		BuyApp buyApp = new BuyApp();
		buyApp.setAmount(amount);
		buyApp.setCode(code);
		buyApp.setName(name);
		buyApp.setFee(paymentPlugin.calculateFee(amount));
		buyApp.setMember(member);
		buyApp.setQuantity(quantity);
		buyApp.setType(type);
		buyApp.setStatus(BuyApp.Status.wait);
		buyApp.setSn(payment.getSn());
		buyAppService.saveAndNew(buyApp);
		description = message("shop.member.deposit.recharge");
		Map<String, Object> map= paymentPlugin.getParameterMap(payment.getSn(),description, null,"");
		//Logger logger = Logger.getLogger("buy");   
		//logger.warn(map.toString());
		String resp = paymentPlugin.post(paymentPlugin.getRequestUrl(),map);
		//logger.warn(resp);
		String[] resps = resp.split("&");
		String tn = "";
		if (ArrayUtils.contains(resps, "respCode=00")) {
			for (String arg : resps) {
				if (arg.substring(0, 3).equals("tn=")) {
					tn = arg;
					break;
				}
			}
			return Message.success(tn+",sn="+payment.getSn());
		} else
			return Message.error("post.error");
	}
}