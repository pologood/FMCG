/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.Member;
import net.wit.service.CaptchaService;
import net.wit.service.MailService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.uic.api.UICService;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 密码
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bPasswordController")
@RequestMapping("/b2b/password")
public class PasswordController extends BaseController {
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "uicService")
	private UICService uicService;
	
	public static final String PASSWORD_SECURITYCODE_SESSION = "password_securityCode_session";

	public static final String PASSWORD_MOBILE_SESSION = "password_mobile_session";

	public static final String PASSWORD_EMAIL_SESSION = "password_email_session";

	public static final String PASSWORD_USERNAME_SESSION = "password_username_session";

	public static final String PUBLIC_KEY = "password_public_key";

	/**
	 * 找回密码
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public String find(Model model) {
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/box/password/find";
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
		return "/box/password/resetPwd";
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
	public Message getCheckCode(String username, String type, HttpServletRequest request) {
//		if (type.equals("mobile")) {
//			return uicService.sendAuthcodeByTel(null,username, request);
//		} else {
//			return uicService.sendAuthcodeByMail(null,username, request);
//		}

		return Message.error("获取短信验证码超过5次，请联系客服");
	}


	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public Message checkCode(String username, String securityCode, HttpServletRequest request) {
		return uicService.checkCaptcha(securityCode, request);
	}
	
	/**
	 * 验证当前密码
	 */
	@RequestMapping(value = "/check_current_password", method = RequestMethod.GET)
	public @ResponseBody boolean checkCurrentPassword(String currentPassword) {
		if (StringUtils.isEmpty(currentPassword)) {
			return false;
		}
		Member member = memberService.getCurrent();
		if (StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证当前支付密码
	 */
	@RequestMapping(value = "/check_current_payment_password", method = RequestMethod.GET)
	public @ResponseBody boolean checkCurrentPaymentPassword(String currentPassword) {
		if (StringUtils.isEmpty(currentPassword)) {
			return false;
		}
		Member member = memberService.getCurrent();
		if (StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPaymentPassword())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(ModelMap model) {

		Member member = memberService.getCurrent();
		model.addAttribute("member", member);

		return "box/password/edit";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);

		return "box/password/payment";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String currentPassword, String password, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(currentPassword)) {
			return ERROR_VIEW;
		}
		if (!isValid(Member.class, "password", password)) {
			return ERROR_VIEW;
		}
		Setting setting = SettingUtils.get();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		//if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPassword())) {
		//	return ERROR_VIEW;
		//}

		// 更新用户中心密码
		Message msg = uicService.updatePass(password, currentPassword, request);

		if (msg.getType().equals(Message.Type.success)) {
			addFlashMessage(redirectAttributes, msg);
			return "box/password/edit";
		}
		member.setPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../index.jhtml";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update_payment", method = RequestMethod.POST)
	public String updatePayment(String currentPassword, String password, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(currentPassword)) {
			return ERROR_VIEW;
		}
		if (!isValid(Member.class, "password", password)) {
			return ERROR_VIEW;
		}
		Setting setting = SettingUtils.get();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		//if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPaymentPassword())) {
		//	return ERROR_VIEW;
		//}
		// 更新用户中心支付密码
		Message msg = uicService.updatePaymentPass(password, currentPassword,request);

		if (msg.getType().equals(Message.Type.success)) {
			addFlashMessage(redirectAttributes, msg);
			return "box/password/payment";
		}
		member.setPaymentPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../index.jhtml";
	}

	/**
	 * 检查用户以及用户的email是否存在
	 */
	@RequestMapping(value = "/checkEmail", method = RequestMethod.GET)
	public @ResponseBody boolean checkEmail(String username, String email) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		return memberService.usernameExists(username) && memberService.emailExists(email);
	}

	/**
	 * 检查用户以及用户的mobile是否存在
	 */
	@RequestMapping(value = "/checkMobile", method = RequestMethod.GET)
	public @ResponseBody boolean checkMobile(String username, String mobile) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		return memberService.usernameExists(username) && memberService.emailExists(mobile);
	}
}