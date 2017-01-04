/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.wit.Setting;
import net.wit.controller.api.HttpClientProxy;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plugin.PaymentPlugin;
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;

/**
 * Controller - 支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cPaymentController")
@RequestMapping("/b2c/payment")
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

	@Resource(name = "buyAppServiceImpl")
	private BuyAppService buyAppService;

	@Value("${vbox.dspServer}")
	private String dspServer;
	
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

    @Resource(name = "paymentMethodServiceImpl")
    private PaymentMethodService paymentMethodService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;
	/**
	 * 提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(Type type, String paymentPluginId, String sn, BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
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
			description = order.getName();
		} else if (type == Type.recharge) {
			Setting setting = SettingUtils.get();
			if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
				return ERROR_VIEW;
			}
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
			description = message("shop.member.deposit.recharge");
		} else {
			return ERROR_VIEW;
		}
		model.addAttribute("requestUrl", paymentPlugin.getRequestUrl());
		System.out.println("B2C 支付空间返回地址：" + paymentPlugin.getRequestUrl());
		model.addAttribute("requestMethod", paymentPlugin.getRequestMethod());
		model.addAttribute("requestCharset", paymentPlugin.getRequestCharset());
		model.addAttribute("parameterMap", paymentPlugin.getParameterMap(payment.getSn(), description, request, "/b2c"));
		if (StringUtils.isNotEmpty(paymentPlugin.getRequestCharset())) {
			response.setContentType("text/html; charset=" + paymentPlugin.getRequestCharset());
		}
		return "b2c/payment/submit";
	}
	
	/**
	 * 通知
	 */
	@RequestMapping("/notify/{notifyMethod}/{sn}")
	public String notify(@PathVariable NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request, ModelMap model) {
		Payment payment = paymentService.findBySn(sn);
		if (payment != null) {
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			if (paymentPlugin != null && notifyMethod.equals(NotifyMethod.async)) {
				if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
					Boolean sended = payment.getStatus().equals(Payment.Status.wait);
					try {
						paymentService.handle(payment);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
				return "b2c/payment/notify";
			}
			if (paymentPlugin != null && notifyMethod.equals(NotifyMethod.sync)) {
				try {
					  Boolean sended = payment.getStatus().equals(Payment.Status.wait);
					  if (paymentPlugin.queryOrder(payment).equals("0000")) {
						  paymentService.handle(payment);
					  }
				    } catch (Exception e) {
						System.out.println(e.getMessage());
					}
				model.addAttribute("payment", payment);
				model.addAttribute("order",payment.getOrder());
				return "redirect:payment_success.jhtml?sn="+payment.getSn();
			}
		} 
		return "b2c/payment/notify";
	}
	
	//-----------------------------now-------------------------------//
	/**
	 * 支付页面
	 * @param sn
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/payment_index",method = RequestMethod.GET)
	public String payment_index(String sn, HttpServletRequest request, ModelMap model) {
		Payment payment = paymentService.findBySn(sn);
		model.addAttribute("member",memberService.getCurrent());
		model.addAttribute("order",payment.getOrder());
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("payment",payment);
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
		return "/b2c/payment/payment_index";
	}
	
	/**
	 * 创建支付编码
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody DataBlock create(String paymentPluginId,Type type,String sn, BigDecimal amount,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Payment payment = new Payment();
//		if (type == Type.payment) {
			Order order = orderService.findBySn(sn);
			if (order == null || !member.equals(order.getMember()) || order.isExpired() ) {
					return DataBlock.error("订单无效");
			}
			if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
					return DataBlock.error("订单已支付");
			}
			if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
					return DataBlock.error("订单已支付");
			}
			payment.setMember(order.getMember());
			payment.setPayer(order.getMember().getName());
			payment.setMemo("购买商品");
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setType(Type.payment);
			payment.setMethod(Method.online);
			payment.setStatus(Status.wait);
			payment.setPaymentMethod("");
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
//			if(paymentPlugin!=null){
//				payment.setPaymentMethod(paymentPlugin.getPaymentName());
//				payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
//			}else{
//				payment.setPaymentMethod("");
//				payment.setExpire(null);
//			}
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(order.getAmountPayable());
			payment.setPaymentPluginId(paymentPluginId);
			payment.setOrder(order);
			paymentService.save(payment);
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

//		} else if (type == Type.recharge) {
//			Setting setting = SettingUtils.get();
//			if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
//				    return DataBlock.error("无效金额");
//			}
//			payment.setMember(member);
//			payment.setPayer(member.getName());
//			payment.setMemo("钱包充值");
//			payment.setSn(snService.generate(Sn.Type.payment));
//			payment.setType(Type.recharge);
//			payment.setMethod(Method.online);
//			payment.setStatus(Status.wait);
//			payment.setPaymentMethod("");
//			payment.setFee(BigDecimal.ZERO);
//			payment.setAmount(amount.setScale(2));
//			payment.setPaymentPluginId("");
//			payment.setExpire(null);
//			payment.setMember(member);
//			paymentService.save(payment);
//		} else {
//			return DataBlock.error("无效类型");
//		}
		return DataBlock.success(payment.getSn(),"success");
	}
	
	/**
	 * 平台支付提交
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/submit_payment", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(String paymentPluginId, String sn,String ordersn,HttpServletRequest request) {
		Date a=new Date();
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return DataBlock.error(DataBlock.SESSION_INVAILD);
			}
			Payment payment = paymentService.findBySn(sn);
			if ("weixinPayPlugin".equals(paymentPluginId) ) {
				PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
				if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
					return DataBlock.error("支付插件无效");
				}
			    payment.setMethod(Payment.Method.online);
			    payment.setPaymentMethod(paymentPlugin.getName());
			    payment.setPaymentPluginId(paymentPluginId);
				paymentService.update(payment);
				Map<String, Object> parameters = paymentPlugin.getParameterMap(payment.getSn(),payment.getMemo(), request, "/wap");
				return DataBlock.success(parameters, "success");
			}
            if ("offlinePayPlugin".equals(paymentPluginId)) {
                Order order = orderService.findBySn(ordersn);
                PaymentMethod paymentMethod = paymentMethodService.find(7L);
                order.setPaymentMethod(paymentMethod);
				for (Trade trade:order.getTrades()){
//					trade.setOrderStatus(Order.OrderStatus.confirmed);
					tradeService.update(trade);
				}
                orderService.update(order);
                return DataBlock.success("success", "线下结算");
            }
			if ("balancePayPlugin".equals(paymentPluginId) ) {
				String password = rsaService.decryptParameter("enPassword", request);
				if (password==null) {
					return DataBlock.error("无效的支付密码");
				}
				if (!MD5Utils.getMD5Str(password).equals(member.getPaymentPassword())) {
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
			    payment.setPaymentPluginId(paymentPluginId);
				paymentService.update(payment);
			    try {
			    	BigDecimal amount = payment.getAmount();
					paymentService.handle(payment);
					if (payment.getAmount().equals(BigDecimal.ZERO)) {
						payment.setAmount(amount);
						payment.setStatus(Status.wait);
						paymentService.update(payment);
						return DataBlock.error("钱包余额不足");
					}else{
						System.out.println(new Date().getTime()-a.getTime());
						return DataBlock.success("success","付款成功");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return DataBlock.error("付款失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataBlock.error("提交出错了");
	}
	
	/**
	 * 微信支付提交
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/weixin_payment", method = RequestMethod.POST)
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
					paymentService.handle(payment);
					return DataBlock.success("success","支付成功");
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
	 * 支付成功页面
	 * @param sn
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/payment_success",method = RequestMethod.GET)
	public String index(String sn, HttpServletRequest request, ModelMap model) {
		Order order=orderService.findBySn(sn);
		model.addAttribute("member",memberService.getCurrent());
		if(order.getPaymentMethod().getMethod()==PaymentMethod.Method.offline){
			model.addAttribute("offline","true");
		}else{
			model.addAttribute("offline","false");
		}
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("order",order);
		return "/b2c/payment/payment_success";
	}
}