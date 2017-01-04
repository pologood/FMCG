/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Member;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.SmsSend.Type;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 密码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxPasswordController")
@RequestMapping("/ajax/password")
public class PasswordController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;


	/**
	 * 获取验证码
	 * 
	 * @param mobile
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSecurityCode", method = RequestMethod.POST)
	@ResponseBody
	public Message getSecurityCode(String mobile, HttpServletRequest request) {
		if (StringUtils.isEmpty(mobile)) {
			return Message.error("ajax.mobile.NotExist");
		}
		Setting setting = SettingUtils.get();
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting
				.getSafeKeyExpiryTime()) : null);
		HttpSession session = request.getSession();
		session.setAttribute(PasswordController.class.getName(), safeKey);
		
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("欢迎您使用"+setting.getSiteName()+"找回密码,验证码为：" + securityCode + ",为了您的账户安全请不要转发他人【"+bundle.getString("signature")+"】");
		smsSend.setType(Type.captcha);
		smsSendService.smsSend(smsSend);
		return Message.success(securityCode);
	}


	/**
	 * 重置密码提交
	 */
	@RequestMapping(value = "/resetsave", method = RequestMethod.POST)
	public @ResponseBody
	Message reset(String mobile, String securityCode,String newPassword,HttpServletRequest request) {
		if(StringUtils.isBlank(securityCode)){
			return Message.error("ajax.securityCode.null");
		}	
		HttpSession session = request.getSession();
		SafeKey safeKey=(SafeKey) session.getAttribute(PasswordController.class.getName());
		if(safeKey==null){
			return Message.error("ajax.key.null");
		}
		if(securityCode.equals(safeKey.getValue())){
			Member member = memberService.findByUsername(mobile);
			if (member == null) {
				return ERROR_MESSAGE;
			}
			if (!isValid(Member.class, "password", newPassword, Save.class)) {
				return Message.warn("ajax.password.invalidPassword");
			}
			Setting setting = SettingUtils.get();
			if (newPassword.length() < setting.getPasswordMinLength()
					|| newPassword.length() > setting.getPasswordMaxLength()) {
				return Message.warn("ajax.password.invalidPassword");
			}
			member.setPassword(DigestUtils.md5Hex(newPassword));
			memberService.update(member);
			safeKey.setExpire(new Date());
			safeKey.setValue(null);
			return Message.success("ajax.password.resetSuccess");
		}
		return Message.error("ajax.unknow.error");
		
	}

}