/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.weixin.model.DataBlock;
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
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperPaymentController")
@RequestMapping("/helper/payment")
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

	@Value("${vbox.dspServer}")
	private String dspServer;

	/**
	 * 提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(Type type, String paymentPluginId,Long tradeId, String sn, BigDecimal amount,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
			return ERROR_VIEW;
		}
		Payment payment = new Payment();
		String description = null;
		if (type == Type.payment) {
			Order order = orderService.findBySn(sn);
			if (order == null || !member.equals(order.getMember()) || order.isExpired() || order.isLocked(member)) {
					return ERROR_VIEW;
			}
			if (order.getPaymentMethod() == null || order.getPaymentMethod().getMethod() != PaymentMethod.Method.online) {
					return ERROR_VIEW;
			}
			if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
					return ERROR_VIEW;
			}
			if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
					return ERROR_VIEW;
			}
			payment.setMember(order.getMember());
			payment.setPayer(order.getMember().getName());
			payment.setMemo("商城进货");
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.payment);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(order.getPaymentMethodName() + Payment.PAYMENT_METHOD_SEPARATOR + paymentPlugin.getPaymentName());
			// payment.setFee(new BigDecimal(0));
			payment.setFee(paymentPlugin.calculateFee(order.getAmountPayable()));
			payment.setAmount(order.getAmountPayable());
			// payment.setAmount(paymentPlugin.calculateAmount(order.getAmountPayable()));
			payment.setPaymentPluginId(paymentPluginId);
			payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
			payment.setOrder(order);
			paymentService.save(payment);
			if(order.getDescription()==null&&order.getDescription().length()==0){
				description = "未取得商品信息";
			}else if(order.getDescription().length()>80){
				description = order.getDescription().substring(0,80);
			}else{
				description = order.getDescription();
			}
		} else if (type == Type.recharge) {
			Setting setting = SettingUtils.get();
			if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				addFlashMessage(redirectAttributes, Message.warn("操作失败，请稍后再试..."));
				if(tradeId!=null){
					return "redirect:/helper/member/order/view.jhtml?id="+tradeId;
				}else{
					return ERROR_VIEW;
				}
			}
			payment.setMember(member);
			payment.setPayer(member.getName());
			payment.setMemo("商城充值");
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.recharge);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod(paymentPlugin.getPaymentName());
			payment.setFee(paymentPlugin.calculateFee(amount.setScale(2)));
			payment.setAmount(amount.setScale(2));
			payment.setPaymentPluginId(paymentPluginId);
			payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
			payment.setMember(member);
			paymentService.save(payment);
			description = message("helper.member.deposit.recharge");
		} else {
			addFlashMessage(redirectAttributes, Message.warn("操作失败，请稍后再试..."));
			if(tradeId!=null){
				return "redirect:/helper/member/order/view.jhtml?id="+tradeId;
			}else{
				return ERROR_VIEW;
			}
		}
		model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
		model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
		model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
		model.addAttribute("parameterMap", paymentPlugin.getParameterMap(payment.getSn(), description, request, "/helper"));
		if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
			response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		return "helper/payment/submit";
	}
	
	/**
	 * 创建支付编码--weixin充值
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody DataBlock create(String paymentPluginId,Type type,String sn, BigDecimal amount,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Payment payment = new Payment();
		if (type == Type.recharge) {
			Setting setting = SettingUtils.get();
			if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				    return DataBlock.error("无效金额");
			}
			payment.setMember(member);
			payment.setPayer(member.getName());
			payment.setMemo("钱包充值");
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.recharge);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod("");
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(amount.setScale(2));
			payment.setPaymentPluginId(paymentPluginId);
			payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
			paymentService.save(payment);
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			if(paymentPluginId.equals("weixinQrcodePayPlugin")){
				String description="微信支付";
				Map<String, Object> data = paymentPlugin.getParameterMap(payment.getSn(), description, request, "/b2c");
				data.put("sn",payment.getSn());
	            if ("".equals(data.get("code_url"))) {
				   return DataBlock.error("提交微信服务器失败");
	            } else {
				   return DataBlock.success(data,"执行成功");
	            }
			}
		} else {
			return DataBlock.error("无效类型");
		}
		return DataBlock.success(payment.getSn(),"success");
	}
	
	/**
	 * 通知
	 */
	@RequestMapping("/notify/{notifyMethod}/{sn}")
	public String notify(@PathVariable NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request, ModelMap model) {
		Payment payment = paymentService.findBySn(sn);
		if (payment != null) {
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			if (paymentPlugin != null) {
				if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
					try {
						paymentService.handle(payment);
					} catch (Exception e) {
						System.out.println("PaymentController.notify: "+e.getMessage());
					}

				}
				model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
			}
			model.addAttribute("payment", payment);
		}
		return "helper/payment/notify";
	}

}