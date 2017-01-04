/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c.member;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.b2c.BaseController;
import net.wit.controller.uic.UserController;
import net.wit.entity.*;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.SmsSend.Type;
import net.wit.service.*;
import net.wit.uic.api.UICService;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller - 会员中心 - 密码
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cMemberSafeController")
@RequestMapping("/b2c/member/safe")
public class SafeController extends BaseController {

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

	@Resource(name = "uicService")
	private UICService uicService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;


	/**
	 * 账户安全设置首页
	 * @return
     */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model){
		Member member=memberService.getCurrent();
		Idcard idcard=member.getIdcard();
		if(idcard==null){
			idcard=new Idcard();
		}
		model.addAttribute("idcard",idcard);
		model.addAttribute("area",areaService.getCurrent());
		String captchaId=UUID.randomUUID().toString();
		model.addAttribute("captchaId",captchaId);
		model.addAttribute("menu","safe");
		return "b2c/member/safe/index";
	}

	/**
	 * 会员基本信息保存提交
	 * @return
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Message save(Member m,Long areaId){
		try {
			Member member=memberService.getCurrent();
//			if(m.getUsername()==null){
//				return Message.error("用户名不能为空");
//			}
//			member.setUsername(m.getUsername());
//			if(m.getMobile()==null){
//				return Message.error("手机号码不能为空");
//			}
			member.setHeadImg(m.getHeadImg());
//			member.setMobile(m.getMobile());
			if(StringUtils.isNotBlank(m.getNickName())){
				member.setNickName(m.getNickName());
			}
			member.setEmail(m.getEmail());
			Area area=areaService.find(areaId);
			if(area==null){
				return Message.error("区域错误");
			}
			member.setArea(area);
			member.setAddress(m.getAddress());
			memberService.update(member);
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("保存失败");
		}
		return Message.success("保存成功");
	}

	/**
	 * 会员身份认证
	 * @param name 姓名
	 * @param no 身份证号
	 * @param imageFront 身份证正面照片
	 * @param imageBack 身份证反面照片
     */
	@RequestMapping(value = "/identity", method = RequestMethod.POST)
	public String identity(String name, String no, MultipartFile imageFront, MultipartFile imageBack, RedirectAttributes redirectAttributes){
		try {
			Member member=memberService.getCurrent();
			Idcard idcard=member.getIdcard();
			if(idcard==null){
                idcard=new Idcard();
            }
			idcard.setNo(no);
			idcard.setAuthStatus(AuthStatus.wait);
			if (imageFront != null && !imageFront.isEmpty()) {
				if (!fileService.isValid(FileType.image, imageFront)) {
					addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
					return "redirect:index.jhtml";
				}
			}
			if (imageBack != null && !imageBack.isEmpty()) {
				if (!fileService.isValid(FileType.image, imageBack)) {
					addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
					return "redirect:index.jhtml";
				}
			}

			ProductImage img = new ProductImage();
			img.setFile(imageFront);
			productImageService.build(img);
			idcard.setPathFront(img.getSource());

			img.setFile(imageBack);
			productImageService.build(img);
			idcard.setPathBack(img.getSource());
			idcard.setName(name);
			idcardService.save(idcard);
//			member.setName(name);
			member.setIdcard(idcard);
			memberService.update(member);
		} catch (Exception e) {
			e.printStackTrace();
			addFlashMessage(redirectAttributes, Message.error("提交失败"));
			return "redirect:index.jhtml";
		}
		addFlashMessage(redirectAttributes, Message.success("提交成功"));
		return "redirect:index.jhtml";
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/passwordUpdate", method = RequestMethod.POST)
	public @ResponseBody Message passwordUpdate(String captchaId,String captcha,String newPassword,String curPassword,HttpServletRequest request) {
		if (!captchaService.isValid(Setting.CaptchaType.resetPassword, captchaId, captcha)) {
			return Message.error("b2c.captcha.invalid");
		}
//		String password = rsaService.decryptParameter("password", request);
//		String curPassword = rsaService.decryptParameter("curPassword", request);
		rsaService.removePrivateKey(request);
		if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(curPassword)) {
			return Message.error("密码无效");
		}
		if (!isValid(Member.class, "password", newPassword)) {
			return Message.error("密码无效");
		}
		Setting setting = SettingUtils.get();
		if (newPassword.length() < setting.getPasswordMinLength() || newPassword.length() > setting.getPasswordMaxLength()) {
			return Message.error("密码无效");
		}
		Message msg = uicService.updatePass(newPassword, curPassword, request);
		if (msg.getType().equals(Message.Type.error)) {
			return msg;
		}
		return msg;
	}
	
	/**
	 * 修改支付密码
	 */
	@RequestMapping(value = "/payPasswordUpdate", method = RequestMethod.POST)
	public @ResponseBody Message payPasswordUpdate(String captchaId,String captcha,HttpServletRequest request) {
		if (!captchaService.isValid(Setting.CaptchaType.resetPassword, captchaId, captcha)) {
			return Message.error("b2c.captcha.invalid");
		}
		String password = rsaService.decryptParameter("password", request);
		String curPassword = rsaService.decryptParameter("curPassword", request);
		rsaService.removePrivateKey(request);
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(curPassword)) {
			return Message.error("密码无效");
		}
		if (!isValid(Member.class, "password", password)) {
			return Message.error("密码无效");
		}
		Setting setting = SettingUtils.get();
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return Message.error("密码无效");
		}
		Member member = memberService.getCurrent();
		Message msg = uicService.updatePaymentPass(password, curPassword, request);
		member.setPaymentPassword(DigestUtils.md5Hex(password));
		memberService.update(member);
		if (msg.getType().equals(Message.Type.error)) {
			return msg;
		}
		return msg;
	} 
	/**
	 * 找回支付密码
	 */
	@RequestMapping(value = "/find_payment", method = RequestMethod.GET)
	public String findPayment(Model model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "b2c/member/safe/find_payment";
	}

	/**
	 * 找回支付密码提交
	 */
	@RequestMapping(value = "/find/payment", method = RequestMethod.POST)
	public @ResponseBody
	Message findPayment(String captchaId, String captcha, String currentPassword, String password) {
		if (StringUtils.isEmpty(currentPassword) || StringUtils.isEmpty(password)) {
			return Message.error("shop.common.invalid");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("shop.password.memberNotExist");
		}
//		if (!member.getEmail().equalsIgnoreCase(email)) {
//			return Message.error("shop.password.memberNotExist");
//		}
		Setting setting = SettingUtils.get();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		member.setSafeKey(safeKey);
		memberService.update(member);
		mailService.b2cSendFindPaymentPasswordMail(member.getEmail(), member.getUsername(), safeKey);


		//Member member = memberService.getCurrent();
		//Message msg = uicService.updatePaymentPass(password, currentPassword, request);

//		if (msg.getType().equals(Message.Type.error)) {
//			//addFlashMessage(redirectAttributes, msg);
//			return Message.error("shop.password.memberNotExist");
//		}
		member.setPaymentPassword(DigestUtils.md5Hex(password));
		memberService.update(member);

		return Message.success("shop.password.mailSuccess");
	}

	/**
	 * 验证当前密码
	 */
	@RequestMapping(value = "/check_payment_password", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPaymentPassword(String paymentPassword) {
		if (StringUtils.isEmpty(paymentPassword)) {
			return false;
		}
		Member member = memberService.getCurrent();
		if (StringUtils.equals(DigestUtils.md5Hex(paymentPassword), member.getPaymentPassword())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证手机校验码
	 */
	@RequestMapping(value = "/check_security_code", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkSecurityCode(String captchaId, String securityCode, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (StringUtils.isEmpty(securityCode)) {
			return false;
		}
		if (StringUtils.isEmpty(captchaId)) {
			return false;
		}
		SafeKey safeKey = member.getSafeKey();
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(securityCode)) {
			return false;
		}
		if (safeKey.hasExpired()) {
			return false;
		}
		return true;
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping(value = "/send_security_code", method = RequestMethod.POST)
	public @ResponseBody
	Message sendSecurityCode(String mobile, HttpServletRequest request) {
		Member member=memberService.getCurrent();
		return uicService.sendAuthcodeByTel(mobile,member.getUsername(),request);
	}

	/**
	 * 提交手机绑定
	 */
	@RequestMapping(value = "/mobileupdate", method = RequestMethod.POST)
	public @ResponseBody
	Message mobileupdate(String securityCode, String captchaId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Message msg = uicService.bindUpdate(securityCode, request);
		return msg;
	}

	/**
	 * 发送邮件验证
	 */
	@RequestMapping(value = "/send_email", method = RequestMethod.POST)
	public @ResponseBody
	Message sendEmail(String paymentPassword, String email, HttpServletRequest request, RedirectAttributes redirectAttributes) {
//		if (StringUtils.isEmpty(email)) {
//			return Message.error("发送失败");
//		}
//		Setting setting = SettingUtils.get();
//
//		Member member = memberService.getCurrent();
//
//		int challege = SpringUtils.getIdentifyingCode();
//	    String securityCode = String.valueOf(challege);
//		SafeKey safeKey = new SafeKey();
//		safeKey.setValue(securityCode);
//		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
//		member.setSafeKey(safeKey);
//		member.setEmail(email);
//		memberService.update(member);
//
//	    mailService.sendCheckCodeByMailBind(email, member.getUsername(),safeKey);
//	    return Message.success("已发送");
		Member member=memberService.getCurrent();
		return uicService.sendAuthcodeByMail(email,member.getUsername(),request);
	}

	/**
	 * 绑定手机
	 */
	@RequestMapping(value = "/bindmobile", method = RequestMethod.GET)
	public String bindMobile(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "b2c/member/safe/bindmobile";
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
		return "b2c/member/safe/idcard";
	}

	/**
	 * 绑定邮箱
	 */
	@RequestMapping(value = "/bindemail", method = RequestMethod.GET)
	public String bindEmail(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "b2c/member/safe/bindemail";
	}

	/**
	 * 提交手机绑定
	 */
	@RequestMapping(value = "/idcardupdate", method = RequestMethod.POST)
	public String idcardupdate(String no, String address, Date beginDate, Date endDate, MultipartFile imageBack, MultipartFile imageFront, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Idcard entity = member.getIdcard();
		if (entity == null) {
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
		entity.setBeginDate(beginDate);
		entity.setEndDate(endDate);
		entity.setAuthStatus(AuthStatus.wait);
		ProductImage img = new ProductImage();
		img.setFile(imageBack);
		productImageService.build(img);
		entity.setPathBack(img.getSource());
		img.setFile(imageFront);
		productImageService.build(img);
		entity.setPathFront(img.getSource());
		idcardService.update(entity, member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../index.jhtml";
	}

	/**
	 * 绑定mail
	 */
	@RequestMapping(value = "/emailupdate",method = RequestMethod.POST)
	public String bindMail(String paymentPassword, String email, String securityCode, HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		Message msg = uicService.bindUpdate(securityCode, request);
		if (msg.getType().equals(Message.Type.success)) {
	    	addFlashMessage(redirectAttributes, Message.success("绑定手机成功"));
			return "redirect:../index.jhtml";
		} else {
	    	addFlashMessage(redirectAttributes,msg);
	    	return "b2c/member/safe/bindemail";
		}
	}
	


}