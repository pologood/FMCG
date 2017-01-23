/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.helper.BaseController;
import net.wit.entity.Credit.Method;
import net.wit.entity.Idcard;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.SmsSend.Type;
import net.wit.service.CaptchaService;
import net.wit.service.IdcardService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.uic.api.UICService;
import net.wit.util.IdcardValidator;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

/**
 * Controller - 会员中心 - 密码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberSafeController")
@RequestMapping("/helper/member/safe")
public class SafeController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;
	@Resource(name = "uicService")
	private UICService uicService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
	public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";
	
	/**
	 * 主页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/helper/member/safe/index";
	}
	
	/**
	 * 主页面
	 */
	@RequestMapping(value = "/index1", method = RequestMethod.GET)
	public String index1(String keyword, HttpServletRequest request, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/helper/member/safe/index1";
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String password( HttpServletRequest request,ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "helper/member/safe/password";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/paymentPassword", method = RequestMethod.GET)
	public String passwordPayment( HttpServletRequest request,ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "helper/member/safe/paymentPassword";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String passwordUpdate(String currentPassword, String password, HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		password = rsaService.decryptParameter("password", request);
		currentPassword = rsaService.decryptParameter("currentPassword", request);
		rsaService.removePrivateKey(request);
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(currentPassword)) {
			addFlashMessage(redirectAttributes, Message.error("密码无效"));
			return "redirect:password.jhtml";
		}
		if (!isValid(Member.class, "password", password)) {
			addFlashMessage(redirectAttributes, Message.error("密码无效"));
			return "redirect:password.jhtml";
		}
		Setting setting = SettingUtils.get();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			addFlashMessage(redirectAttributes, Message.error("密码无效"));
			return "redirect:password.jhtml";
		}
		Member member = memberService.getCurrent();

		// 更新用户中心密码
		Message msg = uicService.updatePass(password, currentPassword, request);

		if (msg.getType().equals(Message.Type.error)) {

			addFlashMessage(redirectAttributes, msg);
			return "redirect:password.jhtml";
		}
		member.setPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(redirectAttributes, msg);
		return "redirect:index.jhtml";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/paymentPassword", method = RequestMethod.POST)
	public String paymentPasswordUpdate(String currentPassword, String password, HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		password = rsaService.decryptParameter("password", request);
		currentPassword = rsaService.decryptParameter("currentPassword", request);
		rsaService.removePrivateKey(request);
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(currentPassword)) {
			addFlashMessage(redirectAttributes, Message.error("密码无效"));
			return "redirect:paymentPassword.jhtml";
		}
		if (!isValid(Member.class, "password", password)) {
			addFlashMessage(redirectAttributes, Message.error("密码无效"));
			return "redirect:paymentPassword.jhtml";
		}
		Setting setting = SettingUtils.get();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			addFlashMessage(redirectAttributes, Message.error("新密码无效"));
			return "redirect:paymentPassword.jhtml";
		}
		Member member = memberService.getCurrent();
		Message msg = uicService.updatePaymentPass(password, currentPassword, request);

		if (msg.getType().equals(Message.Type.error)) {
			addFlashMessage(redirectAttributes, msg);
			return "redirect:paymentPassword.jhtml";
		}
		member.setPaymentPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		addFlashMessage(redirectAttributes, msg);
		return "redirect:index.jhtml";
	}

	/**
	 * 找回密码
	 */
	@RequestMapping(value = "/find_payment", method = RequestMethod.GET)
	public String findPayment(Model model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "helper/member/safe/find_payment";
	}

	/**
	 * 找回支付密码
	 */
	@RequestMapping(value = "/find_payment", method = RequestMethod.POST)
	public String findPayment(String username, String checkCode, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("checkCode", checkCode);
		return "/helper/member/safe/resetPayment";
	}
	
	/**
	 * 检查验证码
	 * @param username
	 * @param securityCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public Message checkCode(String username, String securityCode, HttpServletRequest request) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(securityCode)) {
			return Message.error("验证码不正确");
		}
		return Message.success("验证成功！！！");
	}
	
	/**
	 * 找回支付密码
	 */
	@RequestMapping(value = "/resetSave", method = RequestMethod.POST)
	public String resetSave(String newpassword, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		member.setPaymentPassword(DigestUtils.md5Hex(newpassword));
		memberService.update(member);
		addFlashMessage(redirectAttributes, Message.success("修改成功"));
		return "redirect:index.jhtml";
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	public @ResponseBody DataBlock sendSecurityCode(String username,  HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (!member.getUsername().equals(username)) {
			return DataBlock.error("用户名无效");
		}
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		if (tmp!=null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
			if (!tmp.canReset()) {
				return DataBlock.error("系统忙，稍等几秒重试");
			}
		}
		String mobile = member.getMobile();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(CAPTCHA_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(CAPTCHA_CONTENT_SESSION, mobile);
		System.out.println(securityCode);
		
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(Type.captcha);
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","发送成功");
	}

	/**
	 * 发送邮件验证
	 */
	@RequestMapping(value = "/send_email", method = RequestMethod.POST)
	public @ResponseBody Message sendEmail(String paymentPassword, String email, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if ( member.getBindMobile().equals(BindStatus.binded) ) {
		   return uicService.sendAuthcodeByMail(null,member.getUsername(), request);
		} else {
		   return uicService.sendAuthcodeByMail(email,member.getUsername(), request);
		}
	}

	/**
	 * 绑定手机
	 */
	@RequestMapping(value = "/bindmobile", method = RequestMethod.GET)
	public String bindMobile(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "helper/member/safe/bindmobile";
	}

	/**
	 * 提交手机绑定
	 */
	@RequestMapping(value = "/bindmobile", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock mobileupdate(String captcha, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error("error","用户不存在");
		}

		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		String content = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
		session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_CONTENT_SESSION);
		
		if (safeKey == null) {
			return DataBlock.error("error","验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("error","验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("error","验证码不正确");
		}

		if (member.getBindMobile().equals(BindStatus.binded)) {
			if (!member.getMobile().equals(content)) {
				return DataBlock.error("error","无效手机号");
			}
			member.setMobile(content);
			member.setBindMobile(BindStatus.unbind);
		} else {
			member.setMobile(content);
			member.setBindMobile(BindStatus.binded);
		}

		memberService.save(member);
		return DataBlock.success("success","操作成功");
	}

	/**
	 * 绑定邮箱
	 */
	@RequestMapping(value = "/bindemail", method = RequestMethod.GET)
	public String bindEmail(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "helper/member/safe/bindemail";
	}

	/**
	 * 绑定mail
	 */
	@RequestMapping(value = "/bindemail", method = RequestMethod.POST)
	public String bindMail(String paymentPassword, String email, String securityCode, HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		Message msg = uicService.bindUpdate(securityCode, request);
		if (msg.getType().equals(Message.Type.success)) {
			addFlashMessage(redirectAttributes, Message.success("绑定手机成功"));
			return "redirect:index.jhtml";
		} else {
			addFlashMessage(redirectAttributes, msg);
			return "redirect:bindemail.jhtml";
		}
	}

	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/idcard", method = RequestMethod.GET)
	public String idcard(ModelMap model) {
		Member member = memberService.getCurrent();
		Idcard idcard = member.getIdcard();
		if (idcard == null) {
			idcard = new Idcard();
		}
		model.addAttribute("member", member);
		model.addAttribute("idcard", idcard);
		return "helper/member/safe/idcard";
	}

	
	/**
	 * 验证身份证号码是否合法
	 * @param method
	 * @param no  银行卡号码
	 * @return
	 */
	@RequestMapping(value = "/IdcardCheck", method = RequestMethod.POST)
	public @ResponseBody boolean IdcardCheck(Method method, String no) {
		no = no.replaceAll(" ", "");
		IdcardValidator idCheck =new IdcardValidator();
		boolean isIdcard = idCheck.isValidatedAllIdcard(no);
		return isIdcard;
	}
	
	/**
	 * 提交手机绑定
	 */
	@RequestMapping(value = "/idcard", method = RequestMethod.POST)
	public String idcardupdate(String no,String name,String address, Date beginDate, Date endDate, String pathBack, String pathFront, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Idcard entity = member.getIdcard();
		if (entity == null) {
			entity = new Idcard();
		}

//		if (imageBack != null && !imageBack.isEmpty()) {
//			if (!fileService.isValid(FileType.image, imageBack)) {
//				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
//				return "redirect:idcard.jhtml";
//			}
//		}
//		if (imageFront != null && !imageFront.isEmpty()) {
//			if (!fileService.isValid(FileType.image, imageFront)) {
//				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
//				return "redirect:idcard.jhtml";
//			}
//		}
		entity.setNo(no);
		entity.setAddress(address);
		entity.setBeginDate(beginDate);
		entity.setEndDate(endDate);
		entity.setAuthStatus(AuthStatus.wait);
		//ProductImage img = new ProductImage();
		//img.setFile(imageBack);
		//productImageService.build(img);
		//entity.setPathBack(img.getSource());
		//img.setFile(imageFront);
		//productImageService.build(img);
		//entity.setPathFront(img.getSource());
		entity.setPathBack(pathBack);
		entity.setPathFront(pathFront);
		member.setName(name);
		idcardService.update(entity, member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../profile/edit.jhtml";
	}

	
}