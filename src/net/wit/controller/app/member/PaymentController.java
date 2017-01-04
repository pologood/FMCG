/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.member;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.codec.binary.Base64;


import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unionpay.acp.sdk.HttpClient;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.sdk.SDKUtil;
import com.unionpay.acp.sdk.CertUtil;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SecureUtil;



import com.unionpay.acp.sdksample.ic.DemoBase;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import net.wit.Filter;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.BankInfoModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.MemberBankModel;
import net.wit.controller.app.model.PaymentModel;
import net.wit.entity.BankInfo;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.Payment;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plugin.PaymentPlugin;
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.plugin.unionpay.UnionpayPlugin;
import net.wit.service.BankInfoService;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;
import net.wit.util.Base64Util;
import net.wit.util.SettingUtils;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("appMemberPaymentController")
@RequestMapping("/app/member/payment")
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
	 * 提交支付  
	 * paymentPluginId 支付插件编号 {weixinQrcodePayPlugin 微信扫码支付 }
	 * sn 付款单编号
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(String paymentPluginId,String sn, HttpServletRequest request) {
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return DataBlock.error(DataBlock.SESSION_INVAILD);
			}
			Payment payment = paymentService.findBySn(sn);
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
				return DataBlock.error("支付插件无效");
			}
		    payment.setPaymentMethod("微信支付");
		    payment.setPaymentPluginId(paymentPluginId);
			paymentService.save(payment);
			Map<String, Object> data = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request, "/wap");
			data.put("sn", payment.getSn());
			if ("".equals(data.get("code_url"))) {
				return DataBlock.error("提交微信服务器失败");
			} else {
				return DataBlock.success(data, "执行成功");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataBlock.error("提交支付异常");
	}
	
	/**
	 * 查询支付状态
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
	
}