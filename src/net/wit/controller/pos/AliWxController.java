/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.entity.*;
import net.wit.service.*;
import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
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
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
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
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plugin.PaymentPlugin;
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.plugin.unionpay.UnionpayPlugin;
import net.wit.util.Base64Util;
import net.wit.util.DesUtils;
import net.wit.util.HttpClientProxy;
import net.wit.util.HttpClientUtil;
import net.wit.util.MD5Utils;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("PosAliWxController")
@RequestMapping("/pos/aliwx")
public class AliWxController extends BaseController {

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
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "payBillServiceImpl")
	private PayBillService payBillService;

	/**
	 * 发起支付  
	 * type 1.微信支付 2阿里支付
	 * sn 支付单号流水
	 * safeKey 付款安全码 ，扫阿里或微信的付款码，没有传入时，代表采用用户找你的二维码方式支付
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(Long tenantId,String key,String type,String sn,String safeKey,BigDecimal amount,String operator, HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = "";
		myKey = DigestUtils.md5Hex(tenantId.toString()+type+sn+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		
		Map<String,Object> data = new HashMap<String,Object>();
		Member member = tenant.getMember();
		PayBill payBill = new PayBill();
		Payment payment = new Payment();
		String description = "线下代收";
		Setting setting = SettingUtils.get();
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
			return DataBlock.error("amount.error");
		}
		payBill.setSn(snService.generate(Sn.Type.paybill));
		payBill.setStatus(PayBill.Status.none);
		payBill.setAmount(amount);
		payBill.setMember(member);
		payBill.setTenant(tenant);
		payBill.setType(PayBill.Type.cashier);
		payBillService.save(payBill);
		payment.setMember(member);
		payment.setPayer(member.getName());
		payment.setMemo("线下代收");
		payment.setSn(sn);
		payment.setPaySn(sn);
		payment.setType(Type.cashier);
		payment.setMethod(Method.online);
		payment.setStatus(Status.wait);
		if (type.equals("1")) {
			payment.setPaymentMethod("微信支付");
			payment.setPaymentPluginId("op_service_wechat_submit");
			data.put("methodType", "op_service_wechat_submit");
		} else {
			payment.setPaymentMethod("阿里支付");
			payment.setPaymentPluginId("op_service_alipay_submit");
			data.put("methodType", "op_service_alipay_submit");
		}
		if (safeKey!=null) {
			data.put("pay_type", "barCode");
			data.put("barInfo",safeKey);
		} else {
			data.put("pay_type", "qrCode");
		}
		payment.setFee(BigDecimal.ZERO);
		payment.setAmount(amount.setScale(2));
		payment.setExpire(null);
		payment.setPayBill(payBill);
		paymentService.save(payment);
		
		data.put("ins_cd", "1000004");
		data.put("serviceId", "100010");
		data.put("merch_id", "797000052720002");
		data.put("term_id", "21002001");
		data.put("qrImageType", "image");
		data.put("trans_amt",payment.getAmount().toString());
		
		String json = JSONObject.toJSONString(data);
		String desStr = DesUtils.getEncString(json, "cloudskycloudskycloudskycloudsky".getBytes());
		
			String sign = MD5Utils.getMD5Str(json+"f46b51f5793a3fdb8aac939a9422ba77");
			String params = "data="+URLEncoder.encode(desStr)+"&sign="+sign;
			
			String rtn_msg = HttpClientUtil.doPost("http://222.92.116.42:18089/cloudsskyApi/opservice",params);
            JSONObject rtn = JSONObject.parseObject(rtn_msg);
            if (rtn.get("ret_code").equals("00")) {
            	payment.setPaySn(rtn.get("order_no").toString());
            	paymentService.update(payment);
		        return DataBlock.success(rtn.get("qr_info"),"success");
            } else {
				payment.setStatus(Status.failure);
				paymentService.update(payment);
 		        return DataBlock.error(URLDecoder.decode(rtn.get("ret_msg").toString()));
            }
	}
	
	/**
	 * 查询状态
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock query(Long tenantId,String key,String sn, HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = "";
		myKey = DigestUtils.md5Hex(tenantId.toString()+sn+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		Payment payment = paymentService.findBySn(sn);
		if (payment== null) {
			return DataBlock.success("99","success");
		}
		PayBill payBill = payment.getPayBill();
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("ins_cd", "1000004");
		data.put("serviceId", "900001");
		data.put("order_no", payment.getPaySn());
		data.put("methodType", payment.getPaymentPluginId());
		
		String json = JSONObject.toJSONString(data);
		String desStr = DesUtils.getEncString(json, "cloudskycloudskycloudskycloudsky".getBytes());
		
			String sign = MD5Utils.getMD5Str(json+"f46b51f5793a3fdb8aac939a9422ba77");
			String params = "data="+URLEncoder.encode(desStr)+"&sign="+sign;
			
			String rtn_msg = HttpClientUtil.doPost("http://222.92.116.42:18089/cloudsskyApi/opservice",params);
            JSONObject rtn = JSONObject.parseObject(rtn_msg);
            if (rtn.get("ret_code").equals("00")) {
            	try {
					paymentService.handle(payment);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
            	if (rtn.get("ret_code").equals("01")) {
            	    System.out.println(URLDecoder.decode(rtn.get("ret_msg").toString()));
            	} else {
					payment.setStatus(Status.failure);
					paymentService.update(payment);
					payBill.setStatus(PayBill.Status.failure);
					payBillService.update(payBill);
				}
            }
 		    return DataBlock.success(rtn.get("ret_code"),"success"); 
 		    // 00：成功，01：处理中，其他失败
	}
	
}