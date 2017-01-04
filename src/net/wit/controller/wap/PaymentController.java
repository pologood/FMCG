/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TenantListModel;
import net.wit.domain.AuthenticodeStrategy;
import net.wit.entity.*;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plugin.PaymentPlugin;
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.service.*;
import net.wit.support.TenantComparatorByDistance;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapPaymentController")
@RequestMapping("/wap/payment")
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

	@Resource(name = "tenantRelationServiceImpl")
	private TenantRelationService tenantRelationService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	
	@Resource
	private AuthenticodeStrategy authenticodeStrategy;

	@Resource(name = "activityPlanningServiceImpl")
	private ActivityPlanningService activityPlanningService;

	/**
	 * 支付
	 */
	@RequestMapping(value = "/index",method = RequestMethod.POST)
	public String index(String sn, HttpServletRequest request, ModelMap model) {
		Payment payment = paymentService.findBySn(sn);
		model.addAttribute("member",memberService.getCurrent());
		model.addAttribute("payment",payment);
		return "/wap/payment/index";
	}
	
	/**
	 * 创建 
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody DataBlock create(Type type,String sn, BigDecimal amount) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Payment payment = new Payment();
		if (type == Type.payment) {
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
			payment.setFee(BigDecimal.ZERO);
			payment.setAmount(order.getAmountPayable());
			payment.setPaymentPluginId("");
			payment.setExpire(null);
			payment.setOrder(order);
			paymentService.save(payment);
		} else if (type == Type.recharge) {
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
			payment.setPaymentPluginId("");
			payment.setExpire(null);
			payment.setMember(member);
			paymentService.save(payment);
		} else {
			return DataBlock.error("无效类型");
		}
		return DataBlock.success(payment.getSn(),"success");
	}
	
	/**
	 * 提交
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(String paymentPluginId, String sn,HttpServletRequest request) {
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return DataBlock.error(DataBlock.SESSION_INVAILD);
			}
			Payment payment = paymentService.findBySn(sn);
			if ("weixinPayPlugin".equals(paymentPluginId) ) {
				PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
				if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
					return DataBlock.warn("支付插件无效");
				}
			    payment.setMethod(Payment.Method.online);
			    payment.setPaymentMethod(paymentPlugin.getName());
			    payment.setPaymentPluginId(paymentPluginId);
				paymentService.update(payment);
				Map<String, Object> parameters = paymentPlugin.getParameterMap(payment.getSn(),payment.getMemo(), request, "/wap");
				return DataBlock.success(parameters, "success");
			}
			if ("balancePayPlugin".equals(paymentPluginId) ) {
				String password = rsaService.decryptParameter("enPassword", request);
				if (password==null) {
					return DataBlock.warn("无效的支付密码");
				}
				if (!MD5Utils.getMD5Str(password).equals(member.getPaymentPassword())) {
					return DataBlock.warn("无效的支付密码");
				}
			    if (payment.getType().equals(Payment.Type.recharge)) {
			    	return DataBlock.warn("充值业务不能使用余额支付");
			    }
			    if (payment.getAmount().compareTo(member.getBalance())>0) {
			    	return DataBlock.warn("钱包余额不足，不能完成付款");
			    }
			    if (!payment.getStatus().equals(Status.wait)) {
			    	return DataBlock.warn("已经重复发起付款操作。");
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
						return DataBlock.warn("钱包余额不足");
					}else{
						  // Order order = payment.getOrder();
						  // if (order!=null&&order.getPaymentStatus().equals(PaymentStatus.paid)) {
						  //	  authenticodeStrategy.sendNotify(order);
						  //	  orderService.pushTo(order);
					   	  // }
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
	 * 通知
	 */
	@RequestMapping("/notify/{notifyMethod}/{sn}")
	public String notify(@PathVariable NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request, ModelMap model, Pageable pageable) {
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
			}
			if (paymentPlugin != null && notifyMethod.equals(NotifyMethod.sync)) {
				String result = request.getParameter("result");
				if ("0".equals(result)) {
					try {
					  Boolean sended = payment.getStatus().equals(Payment.Status.wait);
					  if (paymentPlugin.queryOrder(payment).equals("0000")) {
						  paymentService.handle(payment);
					  }
				    } catch (Exception e) {
						System.out.println(e.getMessage());
					}
		   		   // payment.setStatus(Status.success);
				} else if ("-1".equals(result)) {
					payment.setStatus(Status.wait);
				} else if ("1".equals(result)) {
					payment.setStatus(Status.failure);
				}
			}
           if (notifyMethod.equals(NotifyMethod.sync)) {
               Member member = memberService.getCurrent();
			   ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);

			   if (activityPlanning != null) {
				   List<Tenant> tenants = new ArrayList<>();
				   for (Tenant tenant : activityPlanning.getTenants()) {
					   tenants.add(tenant);
				   }
				   pageable.setPageSize(activityPlanning.getTenants().size());
				   TenantComparatorByDistance comparatorByDistance = new TenantComparatorByDistance();
				   Location location = null;
				   if (member!=null) {
					   location = member.getLocation();
					   comparatorByDistance.setLocation(member.getLocation());
					   Collections.sort(tenants, comparatorByDistance);
				   }
				   int fromindex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
				   int endindex = fromindex + pageable.getPageSize();
				   if (endindex > tenants.size()) {
					   endindex = tenants.size();
				   }
				   Page<Tenant> page = null;
				   if (endindex <= fromindex) {
					   page = new Page<Tenant>(new ArrayList<Tenant>(), 0, pageable);
				   }
				   page = new Page<Tenant>(new ArrayList<Tenant>(tenants.subList(fromindex, endindex)), tenants.size(), pageable);

				   model.addAttribute("tenants", TenantListModel.bindData(page.getContent(),location));
			   }
		   }
			model.addAttribute("payment", payment);
		}
		return "/wap/payment/notify";
	}

	@RequestMapping("orderNotify")
	public String orderNotify(){
		return "/wap/payment/order_notify";
	}

}