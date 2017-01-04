/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.Member;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.service.CaptchaService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.uic.api.UICService;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller - 密码
 * @author rsico Team
 * @version 3.0
 */
@Controller("storePasswordController")
@RequestMapping("/store/password")
public class PasswordController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "uicService")
	private UICService uicService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";

	public static final String REGISTER_CONTENT_SESSION = "register_mobile";

	/**
	 * 找回密码
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String find(Model model) {
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/store/password/find";
	}

	/**
	 * 重置密码(通过手机/邮箱)
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String resetPwd(String username, String securityCode, Model model, HttpServletRequest request) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("member", member);
		model.addAttribute("securityCode", securityCode);
		return "/store/password/reset";
	}

	/**
	 * 重置密码提交(通过手机/邮箱)
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	@ResponseBody
	public Message resetSave(String username, String newpassword,String securityCode, HttpServletRequest request) {
		Message msg = uicService.retrievePass(username, newpassword, securityCode, request);
		if (msg.getType().equals(Message.Type.success)) {
			Member member = memberService.findByUsername(username);
			if (member != null) {
				member.setSafeKey(null);
				member.setPassword(DigestUtils.md5Hex(newpassword));
				member.setLoginFailureCount(0);
				member.setIsLocked(Member.LockType.none);
				member.setLockedDate(null);
				memberService.update(member);
			}
			return Message.success("密码重置成功");
		} else {
			return Message.error("密码重置失败");
		}
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	@ResponseBody
	public Message getCheckCode(String username,String captchaId,String captcha, HttpServletRequest request) {

		if (!captchaService.isValid(Setting.CaptchaType.memberRegister, captchaId, captcha)) {
			return Message.error("图片验证码不正确");
		}
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (tmp != null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(REGISTER_CONTENT_SESSION, username);

		SmsSend smsSend = new SmsSend();
		smsSend.setMobiles(username);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return Message.success("消息发送成功");
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public Message checkCode(String username, String securityCode, HttpServletRequest request) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(securityCode)) {
			return Message.error("验证码不正确");
		}
		if (!memberService.mobileExists(username)) {
			return Message.error("当前用户不存在，您可以【立即注册】");
		}
		return Message.success("验证成功！！！");
	}
	
}