/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax.member;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Idcard;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.ProductImage;
import net.wit.entity.SafeKey;
import net.wit.service.FileService;
import net.wit.service.IdcardService;
import net.wit.service.MailService;
import net.wit.service.MemberService;
import net.wit.service.ProductImageService;
import net.wit.service.SmsSendService;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 密码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxEmailController")
@RequestMapping("/ajax/member/email")
public class EmailController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "imageCaptchaService")
	private com.octo.captcha.service.CaptchaService imageCaptchaService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "mailServiceImpl")
	private MailService mailService;
	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	
	/**
	 * 验证当前邮箱
	 */
	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody
	Message checkEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return Message.error("ajax.member.email.empty");
		}
		Member member = memberService.getCurrent();
		Member mem = memberService.findByEmail(email);
		if (mem == null||mem == member) {
			return Message.success("ajax.member.email.enable");
		} else {
			return Message.error("ajax.member.email.not.belong");
		}
	}

	/**
	 * 发送邮件
	 */
	@RequestMapping(value = "/send_email", method = RequestMethod.POST)
	@ResponseBody
	public Message sendEmail(String email,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(email)) {
			return Message.error("ajax.member.email.empty");
		}
		Setting setting = SettingUtils.get();
		Member member = memberService.getCurrent();
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting
				.getSafeKeyExpiryTime()) : null);
		member.setSafeKey(safeKey);
		memberService.update(member);
	    mailService.sendMailInvaild(email, member.getUsername(),safeKey);
		return Message.success("ajax.member.email.securityCode.success");
	}
	
	/**
	 * 校验绑定mail验证码
	 */
	@RequestMapping(value = "/checkEmailCode",method = RequestMethod.GET)
	@ResponseBody
	public Message checkEmailCode(String security,String username, HttpServletRequest request, ModelMap model) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return Message.error("ajax.member.not.exist");
		}
		
		SafeKey safeKey = member.getSafeKey();
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(security)) {
			return Message.error("ajax.member.securityCode.error");
		}
		if (safeKey.hasExpired()) {
			return Message.error("ajax.member.securityCode.isExpired");
		}
		safeKey.setExpire(new Date());
		safeKey.setValue(null);
		
//		if (member.getBindEmail()==BindStatus.binded) {
//			member.setBindEmail(BindStatus.unbind);
//		} else {
//			member.setBindEmail(BindStatus.binded);
//		}
		memberService.update(member);
		return Message.success("ajax.message.success");
	}
	
	
	/**
	 * 提交手机绑定
	 */
	@RequestMapping(value = "/idcardupdate", method = RequestMethod.POST)
	public String idcardupdate(String no,String address,Date beginDate,Date endDate,MultipartFile imageBack,MultipartFile imageFront,ModelMap model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Idcard entity = member.getIdcard();
		if (entity==null) {
			entity = new Idcard();
		}
		
		if (imageBack != null && !imageBack.isEmpty()) {
			if (!fileService.isValid(FileType.image, imageBack)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:idcard.jhtml";
			}
		}
		if (imageFront != null && !imageFront.isEmpty()) {
			if (!fileService.isValid(FileType.image, imageFront)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:idcard.jhtml";
			}
		}
		entity.setNo(no);
		entity.setAddress(address);
		entity.setBeginDate( beginDate);
		entity.setEndDate(endDate);
		entity.setAuthStatus(AuthStatus.wait);
		ProductImage img = new ProductImage();
		img.setFile(imageBack);
		productImageService.build(img);
		entity.setPathBack(img.getSource());
		img.setFile(imageFront);
		productImageService.build(img);
		entity.setPathFront(img.getSource());
		idcardService.update(entity,member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../index.jhtml";
	}
	/**
	 * 提交手机绑定
	 */
	@RequestMapping(value = "/mobileupdate", method = RequestMethod.POST)
	public String mobileupdate(String paymentPassword, String mobile, String securityCode, String captchaId,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(securityCode) || StringUtils.isEmpty(paymentPassword)) {
			return ERROR_VIEW;
		}
		Setting setting = SettingUtils.get();
		if (paymentPassword.length() < setting.getPasswordMinLength() || paymentPassword.length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!StringUtils.equals(DigestUtils.md5Hex(paymentPassword), member.getPaymentPassword())) {
			return ERROR_VIEW;
		}
		
		SafeKey safeKey = member.getSafeKey();
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(securityCode)) {
			return ERROR_VIEW;
		}
		if (safeKey.hasExpired()) {
			return ERROR_VIEW;
		}
		safeKey.setExpire(new Date());
		safeKey.setValue(null);
		member.setMobile(mobile);
		if (member.getBindMobile()==BindStatus.binded) {
			member.setBindMobile(BindStatus.unbind);
		} else {
			member.setBindMobile(BindStatus.binded);
		}
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../index.jhtml";
	}

}