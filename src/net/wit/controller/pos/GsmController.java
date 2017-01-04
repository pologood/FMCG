/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.service.TenantService;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

/**
 * Controller - 条码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("posGsmController")
@RequestMapping("/pos/gsm")
public class GsmController extends BaseController {

	public static final String REGISTER_SECURITYCODE_SESSION = "pos_safe_key";
	public static final String REGISTER_CONTENT_SESSION = "pos_mobile";
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock sendMobile(Long tenantId, String key,String mobile,String content, HttpServletRequest request, HttpSession session) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+mobile+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
			
			
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(mobile);
			smsSend.setContent(content+"【" + bundle.getString("signature") + "】");
			smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
			return DataBlock.success("success","发送成功");
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping(value = "/send_captcha", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock sendMobile(Long tenantId, String key,String mobile, HttpServletRequest request, HttpSession session) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+mobile+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
			
			
			Setting setting = SettingUtils.get();
			int challege = SpringUtils.getIdentifyingCode();
			String securityCode = String.valueOf(challege);
			SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
			if (tmp!=null && !tmp.hasExpired()) {
				securityCode = tmp.getValue();
			}
			SafeKey safeKey = new SafeKey();
			safeKey.setValue(securityCode);
			safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
			session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
			session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
			
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(mobile);
			smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
			smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
			return DataBlock.success("success","发送成功");
	}
	/**
	 * 检查验证码
	 */
	@RequestMapping(value = "/check_captcha", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock checkMobile(Long tenantId, String key,String captcha, HttpServletRequest request, HttpSession session) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+captcha+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
			
			
			SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
			session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
			session.removeAttribute(REGISTER_CONTENT_SESSION);
			if (safeKey == null) {
				return DataBlock.error("验证码过期了");
			}
			if (safeKey.hasExpired()) {
				return DataBlock.error("验证码过期了");
			}
			if (!safeKey.getValue().equals(captcha)) {
				return DataBlock.error("验证码不正确");
			}

			return DataBlock.success("success","验证成功");
	}
}