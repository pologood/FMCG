/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Setting;
import net.wit.constant.Constant;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Member;
import net.wit.helper.HttpClientHelper;
import net.wit.service.MemberService;
import net.wit.uic.api.UICService;
import net.wit.util.RSAUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Controller - 会员中心 - 密码
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberPasswordController")
@RequestMapping("/b2b/member/password")
public class PasswordController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "uicService")
	private UICService uicService;
	
	/**
	 * 验证当前密码
	 */
	@RequestMapping(value = "/check_current_password", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkCurrentPassword(String currentPassword) {
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
	public @ResponseBody
	boolean checkCurrentPaymentPassword(String currentPassword) {
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
		return "b2b/member/password/edit";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String payment(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "b2b/member/password/payment";
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

		if (msg.getType().equals(Message.Type.error)) {
			addFlashMessage(redirectAttributes, Message.error("修改密码失败，请检查原密码是否正确"));
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
			addFlashMessage(redirectAttributes, Message.error("输入密码不能为空"));
			return ERROR_VIEW;
		}
		if (!isValid(Member.class, "password", password)) {
			addFlashMessage(redirectAttributes, Message.error("密码错误"));
			return ERROR_VIEW;
		}
		Setting setting = SettingUtils.get();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			addFlashMessage(redirectAttributes, Message.error("密码长度错误"));
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!StringUtils.equals(DigestUtils.md5Hex(currentPassword), member.getPaymentPassword())) {
			addFlashMessage(redirectAttributes, Message.error("输入密码错误"));
			return ERROR_VIEW;
		}
		member.setPaymentPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../index.jhtml";
	}
}