/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.uic;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.Setting.CaptchaType;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.Area;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.SmsSend;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.entity.SafeKey;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CaptchaService;
import net.wit.service.CartService;
import net.wit.service.MailService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.support.EntitySupport;
import net.wit.util.Base64Util;
import net.wit.util.DESUtil;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员注册
 * @author rsico Team
 * @version 3.0
 */
@Controller("registerController")
@RequestMapping("/register")
public class RegisterController extends BaseController {

	private static final String ENCODE_TYPE_BASE64 = "BASE64";

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";

	/**
	 * 检查用户名是否被禁用或已存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查E-mail是否存在
	 */
	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		if (memberService.usernameDisabled(email) || memberService.usernameExists(email)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查mobile是否存在
	 */
	@RequestMapping(value = "/check_mobile", method = RequestMethod.GET)
	public @ResponseBody boolean checkMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		if (memberService.usernameDisabled(mobile) || memberService.usernameExists(mobile)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 注册页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Area area = areaService.getCurrent();
		model.addAttribute("area", area);
		model.addAttribute("genders", Gender.values());
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/uic/register/index";
	}

	/**
	 * 
	 */
	@RequestMapping(value = "/submit-get", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> ready(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		Enumeration<?> keys = session.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			attributes.put(key, session.getAttribute(key));
		}
		session.invalidate();
		session = request.getSession();
		for (Entry<String, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}
		RSAPublicKey publicKey = rsaService.generateKey(request);
		session.setAttribute("privateKey", publicKey);
		Map<String, String> data = new HashMap<String, String>();
		data.put("captchaId", UUID.randomUUID().toString());
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return data;
	}

	/**
	 * 注册提交(手机/邮箱)
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public Message submit(String captchaId, String captcha, String username, String mobile, String email, String securityCode, Long areaId, String password, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		System.out.println(request.getSession().getId());
		// 用户名注册时 获取的密码
		if (StringUtils.isEmpty(password)) {
			password = rsaService.decryptParameter("enPassword", request);
		}
		rsaService.removePrivateKey(request);
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		if (StringUtils.isNotBlank(mobile)) {
			username = mobile;
			if (!isValid(Member.class, "username", username, Save.class)) {
				return Message.error("手机号错误");
			}
			if (!isValid(Member.class, "password", password, Save.class)) {
				return Message.error("密码错误");
			}
		}
		if (StringUtils.isNotBlank(email)) {
			username = email;
			if (!isValid(Member.class, "username", username, Save.class)) {
				return Message.error("邮箱错误");
			}
			if (!isValid(Member.class, "password", password, Save.class)) {
				return Message.error("密码错误");
			}
		}
		if (StringUtils.isNotBlank(username)) {
			if (!isValid(Member.class, "username", username, Save.class)) {
				return Message.error("用户名错误");
			}
			if (!isValid(Member.class, "password", password, Save.class)) {
				return Message.error("密码错误");
			}
		}
		if (!setting.getIsRegisterEnabled()) {
			return Message.error("shop.register.disabled");
		}

		if (username.length() < setting.getUsernameMinLength() || username.length() > setting.getUsernameMaxLength()) {
			return Message.error("shop.username.length");
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return Message.error("shop.password.length");
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			return Message.error("shop.register.disabledExist");
		}
		Member member = EntitySupport.createInitMember();
		if (areaId != null) {
			Area area = areaService.find(areaId);
			member.setArea(area);
		}
		member.setUsername(username);
		member.setPassword(DigestUtils.md5Hex(password));
		member.setPoint(setting.getRegisterPoint());
		member.setAmount(new BigDecimal(0));
		member.setBalance(new BigDecimal(0));
		member.setIsEnabled(true);
		member.setIsLocked(Member.LockType.none);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setSafeKey(null);
		member.setBindEmail(Member.BindStatus.none);
		member.setBindMobile(Member.BindStatus.none);
		member.setPaymentPassword(DigestUtils.md5Hex(password));
		member.setRebateAmount(new BigDecimal(0));
		member.setProfitAmount(new BigDecimal(0));
		member.setMemberRank(memberRankService.findDefault());
		member.setFavoriteProducts(null);
		member.setFreezeBalance(new BigDecimal(0));
		member.setPrivilege(0);
		member.setTotalScore((long) 0);
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);

		String domain = null;// setting.getCookieDomain();
		// if (domain == null) {
		String host = request.getServerName();
		String[] hosts = host.split("\\.");
		if (hosts.length == 3) {
			domain = "." + hosts[1] + "." + hosts[2];
		}
		// }

		if (mobile != null && email == null) {
			if (safeKey == null) {
				return Message.error("shop.captcha.invalid");
			}
			if (!safeKey.getValue().equals(securityCode)) {
				return Message.error("shop.captcha.invalid");
			}
			member.setMobile(username);
			member.setEmail(username + "@139.com");
			member.setMobile(mobile);
			member.setBindMobile(BindStatus.binded);
			memberService.save(member);

			String openid = request.getParameter("openid");
			if (openid != null) {
				BindUser user = new BindUser();
				user.setUsername(openid);
				user.setPassword(DigestUtils.md5Hex(password));
				user.setMember(member);
				user.setType(Type._wx);
				bindUserService.save(user);
			}

			Cart cart = cartService.getCurrent();
			if (cart != null && cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
			Map<String, Object> attributes = new HashMap<String, Object>();
			Enumeration<?> keys = session.getAttributeNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				attributes.put(key, session.getAttribute(key));
			}
			session.invalidate();
			session = request.getSession();
			for (Entry<String, Object> entry : attributes.entrySet()) {
				session.setAttribute(entry.getKey(), entry.getValue());
			}

			WebUtils.addCookie(request, response, Cookies.HOST, setting.getSiteUrl(), -1, "/", domain, null);
			Principal principal = new Principal(member.getId(), member.getUsername());
			request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
			principal.createSign();
			String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), Constant.generateKey);
			WebUtils.addCookie(request, response, Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", domain, null);
			WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
			
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(member.getUsername());
			smsSend.setContent("欢迎您注册【" + setting.getSiteName() + "】,账号:" + member.getUsername() + ",密码:" + password + ",网址:【" + setting.getSiteUrl() + "】,为了您的账户安全请不要转发他人【" + bundle.getString("signature") + "】");
			smsSend.setIp(request.getRemoteAddr().toString());
			smsSend.setType(SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
		}
		if (email != null && mobile == null) {
			if (safeKey == null) {
				return Message.error("shop.captcha.invalid");
			}
			if (!safeKey.getValue().equals(securityCode)) {
				addFlashMessage(redirectAttributes, Message.error("shop.captcha.invalid"));
				return Message.error("shop.captcha.invalid");
			}
			member.setMobile(null);
			member.setEmail(username);
			member.setBindEmail(BindStatus.binded);
			memberService.save(member);
			Cart cart = cartService.getCurrent();
			if (cart != null && cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
			Map<String, Object> attributes = new HashMap<String, Object>();
			Enumeration<?> keys = session.getAttributeNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				attributes.put(key, session.getAttribute(key));
			}
			session.invalidate();
			session = request.getSession();
			for (Entry<String, Object> entry : attributes.entrySet()) {
				session.setAttribute(entry.getKey(), entry.getValue());
			}

			WebUtils.addCookie(request, response, Cookies.HOST, setting.getSiteUrl(), -1, "/", domain, null);
			Principal principal = new Principal(member.getId(), member.getUsername());
			request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
			principal.createSign();
			String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), Constant.generateKey);
			WebUtils.addCookie(request, response, Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", domain, null);
			WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
			SafeKey safeKey1 = new SafeKey();
			safeKey1.setValue("账号:" + username + "," + "密码:" + password);
			mailService.sendRegisterSucessMail(username, username, safeKey1);
		}
		if (email == null && mobile == null) {
			if (!captchaService.isValid(CaptchaType.memberRegister, captchaId, captcha)) {
				return Message.error("shop.captcha.invalid");
			}
			member.setEmail(username + "@139.com");
			memberService.save(member);
			Cart cart = cartService.getCurrent();
			if (cart != null && cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
			Map<String, Object> attributes = new HashMap<String, Object>();
			Enumeration<?> keys = session.getAttributeNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				attributes.put(key, session.getAttribute(key));
			}
			session.invalidate();
			session = request.getSession();
			for (Entry<String, Object> entry : attributes.entrySet()) {
				session.setAttribute(entry.getKey(), entry.getValue());
			}
			WebUtils.addCookie(request, response, Cookies.HOST, setting.getSiteUrl(), -1, "/", domain, null);
			Principal principal = new Principal(member.getId(), member.getUsername());
			request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
			principal.createSign();
			String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), Constant.generateKey);
			WebUtils.addCookie(request, response, Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", domain, null);
			WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		}
		return Message.success(member.getUsername());
	}

	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public Message sendMobile(String mobile, HttpServletRequest request) {
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		HttpSession session = request.getSession();
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		// session.setAttribute(REGISTER_TYPE_SESSION, "mobile");
		// session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("注册验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSend.setIp(request.getRemoteAddr().toString());
		smsSendService.smsSend(smsSend);
		return Message.success("发送成功");
	}

	/**
	 * 发送邮件
	 */
	@RequestMapping(value = "/send_email", method = RequestMethod.POST)
	@ResponseBody
	public Message sendEmail(String email, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(email)) {
			return Message.error("邮箱不能为空");
		}
		Setting setting = SettingUtils.get();
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		HttpSession session = request.getSession();
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		// session.setAttribute(REGISTER_TYPE_SESSION, "email");
		// session.setAttribute(REGISTER_CONTENT_SESSION, email);
		mailService.sendRegisterMail(email, email, safeKey);
		return Message.success("发送成功");
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePass", method = RequestMethod.POST)
	public @ResponseBody Message updatePass(String username, String enType, HttpServletRequest request) {
		System.out.println("======updatePass====" + request.getSession(false).getId());
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("登陆超时");
		}
		String oldPass = null;
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			oldPass = rsaService.decryptParameter("oldPass", request);
			newPass = rsaService.decryptParameter("newPass", request);
			if (StringUtils.isEmpty(oldPass) && StringUtils.isEmpty(newPass)) {
				return Message.error("shop.common.invalid");
			}
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			oldPass = Base64Util.decode(request.getParameter("oldPass"));
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		if (StringUtils.isEmpty(oldPass) && StringUtils.isEmpty(newPass)) {
			return Message.error("shop.common.invalid");
		}

		if (DigestUtils.md5Hex(oldPass).equals(member.getPassword())) {
			member.setPassword(DigestUtils.md5Hex(newPass));

			memberService.update(member);
			return Message.success("修改成功");

		} else {
			return Message.error("用户名不存在或密码错误");
		}
	}

	/**
	 * 找回密码验证码
	 */
	@RequestMapping(value = "/send_captcha", method = RequestMethod.POST)
	public @ResponseBody Message sendCaptcha(String username, String type, HttpServletRequest request) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return Message.error("用户不存在");
		}
		if (type.equals("mobile")) {
			if (member.getMobile() == null || member.getBindMobile() != BindStatus.binded) {
				return Message.error("该用户未绑定手机");
			}
		}
		if (type.equals("email")) {
			if (member.getEmail() == null || member.getBindEmail() != BindStatus.binded) {
				return Message.error("该用户未绑定邮箱");
			}
		}
		Setting setting = SettingUtils.get();
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		HttpSession session = request.getSession();
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);

		if (type.equals("mobile")) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// session.setAttribute(REGISTER_TYPE_SESSION, "mobile");
			// session.setAttribute(REGISTER_CONTENT_SESSION, member.getMobile());

			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(member.getMobile());
			smsSend.setContent("找回验证码为 :" + securityCode + ",为了您的账户安全请不要转发他人【" + bundle.getString("signature") + "】");
			smsSend.setType(SmsSend.Type.captcha);
			smsSend.setIp(request.getRemoteAddr().toString());
			smsSendService.smsSend(smsSend);
		}
		if (type.equals("email")) {
			// session.setAttribute(REGISTER_TYPE_SESSION, "email");
			// session.setAttribute(REGISTER_CONTENT_SESSION, member.getEmail());
			mailService.sendGetBackPasswordMail(member.getEmail(), member.getEmail(), safeKey);
		}

		return Message.success("发送成功");
	}

	@RequestMapping(value = "/retrieveCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> retrieveCode(String username, String securityCode, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, String> data = new HashMap<String, String>();
		if (StringUtils.isEmpty(username)) {
			data.put("error", "参数错误");
			return data;
		}
		Member member = memberService.findByUsername(username);
		// String type = (String) session.getAttribute(REGISTER_TYPE_SESSION);
		// String content = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (safeKey == null || member == null || safeKey.hasExpired() || !safeKey.getValue().equals(securityCode)) {
			data.put("error", "无效验证码");
			return data;
		}
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		// if (StringUtils.isNotEmpty(type)) {
		// if ("mobile".equals(type) && content.equals(member.getMobile())) {
		return ready(request, response, session);
		// }
		// if ("email".equals(type) && content.equals(member.getEmail())) {
		// return ready(request, response, session);
		// }
		// }
		// return null;
	}

	/**
	 * 找回密码
	 */
	@RequestMapping(value = "/retrievePass", method = RequestMethod.POST)
	public @ResponseBody Message retrievePass(String username, String enType, HttpServletRequest request) {

		if (StringUtils.isEmpty(username)) {
			return Message.error("shop.common.invalid");
		}
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			newPass = rsaService.decryptParameter("newPass", request);
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		HttpSession session = request.getSession();
		// String type = (String) session.getAttribute(REGISTER_TYPE_SESSION);
		// String content = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
		// session.removeAttribute(REGISTER_TYPE_SESSION);
		// session.removeAttribute(REGISTER_CONTENT_SESSION);

		// Member member = memberService.getCurrent();
		Member member = memberService.findByUsername(username);
		// SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		// if (safeKey == null || member == null || safeKey.hasExpired() || !safeKey.getValue().equals(securityCode)) {
		// return Message.error("验证信息已过期");
		// }
		// session.removeAttribute(REGISTER_SECURITYCODE_SESSION);

		// if (type == null || content == null) {
		// return Message.error("验证信息已过期");
		// }
		// if (type.equals("mobile") && !member.getMobile().equals(content)) {
		// return Message.error("验证信息已过期");
		// }
		// if (type.equals("email") && !member.getEmail().equals(content)) {
		// return Message.error("验证信息已过期");
		// }
		if (member != null) {
			member.setPassword(DigestUtils.md5Hex(newPass));
			memberService.update(member);
			return Message.success("修改成功");
		} else {
			return Message.error("用户名不存在");
		}

	}

	/**
	 * 找回支付密码
	 */
	@RequestMapping(value = "/retrievePayMentPass", method = RequestMethod.POST)
	public @ResponseBody Message retrievePayMentPass(String username, String enType, HttpServletRequest request) {

		if (StringUtils.isEmpty(username)) {
			return Message.error("shop.common.invalid");
		}
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			newPass = rsaService.decryptParameter("newPass", request);
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		HttpSession session = request.getSession();
		// String type = (String) session.getAttribute(REGISTER_TYPE_SESSION);
		// String content = (String) session.getAttribute(REGISTER_CONTENT_SESSION);

		Member member = memberService.getCurrent();

		// if (type == null || content == null) {
		// return Message.error("验证信息已过期");
		// }
		// if (type.equals("mobile") && !member.getMobile().equals(content)) {
		// return Message.error("验证信息已过期");
		// }
		// if (type.equals("email") && !member.getEmail().equals(content)) {
		// return Message.error("验证信息已过期");
		// }

		// session.removeAttribute(REGISTER_TYPE_SESSION);
		// session.removeAttribute(REGISTER_CONTENT_SESSION);

		if (member != null) {
			member.setPaymentPassword(DigestUtils.md5Hex(newPass));
			memberService.update(member);
			return Message.success("修改成功");
		} else {
			return Message.error("用户名不存在");
		}

	}

	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/delUser", method = RequestMethod.POST)
	public @ResponseBody Message delUser(String username, HttpServletRequest request) {
		String password = rsaService.decryptParameter("password", request);
		if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
			return Message.error("shop.common.invalid");
		}
		Member member = memberService.findByUsername(username);
		if (member != null && !DigestUtils.md5Hex(password).equals(member.getPassword())) {
			return Message.error("用户名不存在或密码错误");
		}
		if (member != null && member.getBalance().compareTo(new BigDecimal(0)) > 0) {
			return Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername());
		}
		memberService.delete(member);
		return Message.success("删除成功");
	}

	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public @ResponseBody Message deleteUser(String username, String password, HttpServletRequest request) {
		if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
			return Message.error("shop.common.invalid");
		}
		Member member = memberService.findByUsername(username);
		if (member != null && !password.equals(member.getPassword())) {
			return Message.error("用户名不存在或密码错误");
		}
		if (member != null && member.getBalance().compareTo(new BigDecimal(0)) > 0) {
			return Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername());
		}
		memberService.delete(member);
		return Message.success("删除成功");
	}

	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/deleteUserForKey", method = RequestMethod.GET)
	public @ResponseBody Message deleteUserForKey(String username, String key, HttpServletRequest request) {
		if (StringUtils.isEmpty(username) && StringUtils.isEmpty(key)) {
			return Message.error("shop.common.invalid");
		}
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String mySign = DigestUtils.md5Hex(username + bundle.getString("appKey"));
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return Message.error("用户名没找到");
		}
		if (key.equals(mySign)) {
			return Message.error("签名出错误了");
		}
		if (member.getBalance().compareTo(new BigDecimal(0)) > 0) {
			return Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername());
		}
		memberService.delete(member);
		return Message.success("删除成功");
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePassForKey", method = RequestMethod.GET)
	public @ResponseBody Message updatePassForKey(String username, String enType, String key, HttpServletRequest request) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return Message.error("用户名没找到");
		}
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String mySign = DigestUtils.md5Hex(username + bundle.getString("appKey"));
		System.out.println(mySign);
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			newPass = rsaService.decryptParameter("newPass", request);
			if (StringUtils.isEmpty(newPass)) {
				return Message.error("shop.common.invalid");
			}
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		if (StringUtils.isEmpty(newPass)) {
			return Message.error("shop.common.invalid");
		}
		System.out.println(newPass);
		if (key.equals(mySign)) {
			member.setPassword(DigestUtils.md5Hex(newPass));

			memberService.update(member);
			return Message.success("修改成功");

		} else {
			return Message.error("用户名不存在或密码错误");
		}
	}

	/**
	 * 发送绑定验证码
	 */
	@RequestMapping(value = "/bind_send_captcha", method = RequestMethod.POST)
	public @ResponseBody Message bindSendCaptcha(String bindNo, String type, HttpServletRequest request) {

		Setting setting = SettingUtils.get();
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		HttpSession session = request.getSession();
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);

		if (type.equals("mobile")) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// session.setAttribute(REGISTER_TYPE_SESSION, "mobile");
			// session.setAttribute(REGISTER_CONTENT_SESSION, bindNo);

			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(bindNo);
			smsSend.setContent("绑定验证码为 :" + securityCode + ",为了您的账户安全请不要转发他人【" + bundle.getString("signature") + "】");
			smsSend.setType(SmsSend.Type.captcha);
			smsSend.setIp(request.getRemoteAddr().toString());
			smsSendService.smsSend(smsSend);
		}
		if (type.equals("email")) {
			// session.setAttribute(REGISTER_TYPE_SESSION, "email");
			// session.setAttribute(REGISTER_CONTENT_SESSION, bindNo);
			mailService.sendCheckCodeByMailBind(bindNo, bindNo, safeKey);
		}
		return Message.success("发送成功");
	}

	/**
	 * 绑定手机/邮箱
	 */
	@RequestMapping(value = "/bind_save", method = RequestMethod.POST)
	public @ResponseBody Message bind_save(String bindNo, String type, String securityCode, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if (StringUtils.isEmpty(bindNo) || StringUtils.isEmpty(type)) {
			return Message.error("参数错误");
		}
		Member member = memberService.getCurrent();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (safeKey == null || member == null || safeKey.hasExpired() || !safeKey.getValue().equals(securityCode)) {
			return Message.error("验证信息已过期");
		}
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		// String _type = (String) session.getAttribute(REGISTER_TYPE_SESSION);
		// String _bindNo = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
		// if (_type == null || _bindNo == null) {
		// return Message.error("验证信息已过期");
		// }
		// session.removeAttribute(REGISTER_TYPE_SESSION);
		// session.removeAttribute(REGISTER_CONTENT_SESSION);
		// if (!_type.equals(type) || !_bindNo.equals(bindNo)) {
		// return Message.error("验证信息已过期");
		// }
		// if (member == null) {
		// return Message.error("用户不存在");
		// }
		if ("email".equals(type)) {
			member.setEmail(bindNo);
			member.setBindEmail(BindStatus.binded);
		} else {
			member.setMobile(bindNo);
			member.setBindMobile(BindStatus.binded);
		}
		memberService.save(member);
		return Message.success("绑定成功");
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping(value = "/payMentPass_captcha", method = RequestMethod.POST)
	public @ResponseBody Message payMentPass_captcha(String username, String type, HttpServletRequest request) {
		// Member member = memberService.findByUsername(username);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户不存在");
		}
		if (type.equals("mobile")) {
			if (member.getMobile() == null || member.getBindMobile() != BindStatus.binded) {
				return Message.error("该用户未绑定手机");
			}
		}
		if (type.equals("email")) {
			if (member.getEmail() == null || member.getBindEmail() != BindStatus.binded) {
				return Message.error("该用户未绑定邮箱");
			}
		}
		Setting setting = SettingUtils.get();
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		HttpSession session = request.getSession();
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);

		if (type.equals("mobile")) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// session.setAttribute(REGISTER_TYPE_SESSION, "mobile");
			// session.setAttribute(REGISTER_CONTENT_SESSION, member.getMobile());

			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(member.getMobile());
			smsSend.setContent("找回验证码为 :" + securityCode + ",为了您的账户安全请不要转发他人【" + bundle.getString("signature") + "】");
			smsSend.setType(SmsSend.Type.captcha);
			smsSend.setIp(request.getRemoteAddr().toString());
			smsSendService.smsSend(smsSend);
		}
		if (type.equals("email")) {
			// session.setAttribute(REGISTER_TYPE_SESSION, "email");
			// session.setAttribute(REGISTER_CONTENT_SESSION, member.getEmail());
			mailService.sendGetBackPasswordMail(member.getEmail(), member.getEmail(), safeKey);
		}

		return Message.success("发送成功");
	}

	/**
	 * 修改支付密码
	 */
	@RequestMapping(value = "/updatePayMentPass", method = RequestMethod.POST)
	public @ResponseBody Message updatePayMentPass(String username, String enType, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("登陆超时");
		}
		String oldPass = null;
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			oldPass = rsaService.decryptParameter("oldPass", request);
			newPass = rsaService.decryptParameter("newPass", request);
			if (StringUtils.isEmpty(oldPass) && StringUtils.isEmpty(newPass)) {
				return Message.error("shop.common.invalid");
			}
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			oldPass = Base64Util.decode(request.getParameter("oldPass"));
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		if (StringUtils.isEmpty(oldPass) && StringUtils.isEmpty(newPass)) {
			return Message.error("shop.common.invalid");
		}

		if (DigestUtils.md5Hex(oldPass).equals(member.getPaymentPassword())) {
			member.setPaymentPassword(DigestUtils.md5Hex(newPass));

			memberService.update(member);
			return Message.success("修改成功");

		} else {
			return Message.error("用户名不存在或支付密码错误");
		}
	}

	/**
	 * 检验支付密码
	 */
	@RequestMapping(value = "/checkPayMentPass", method = RequestMethod.POST)
	public @ResponseBody Message checkPayMentPass(String enType, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("登陆超时");
		}
		String payMentPass = "";
		if (StringUtils.isBlank(enType)) {
			payMentPass = rsaService.decryptParameter("payMentPass", request);
			if (StringUtils.isEmpty(payMentPass)) {
				return Message.error("密码无效");
			}
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			payMentPass = Base64Util.decode(request.getParameter("payMentPass"));
		}
		if (StringUtils.isEmpty(payMentPass)) {
			return Message.error("密码无效");
		}

		if (DigestUtils.md5Hex(payMentPass).equals(member.getPaymentPassword())) {
			return Message.success("支付密码正确");

		} else {
			return Message.error("支付密码错误");
		}
	}

	/**
	 * 注册员工
	 */
	@RequestMapping(value = "/submitMember", method = RequestMethod.POST)
	@ResponseBody
	public Message submitMember(String agentUsername, String username, String mobile, String email, Long areaId, String password, String name, String address, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Setting setting = SettingUtils.get();
		Member member = new Member();
		// Member agent = memberService.findByUsername(agentUsername);
		Member agent = memberService.getCurrent();
		if (agent == null) {
			return Message.error("会员中心店长不存在");
		}
		if (areaId != null) {
			Area area = areaService.find(areaId);
			member.setArea(area);
		}
		try {
			member.setUsername(username);
			member.setAddress(URLDecoder.decode(address, "UTF-8"));
			member.setName(URLDecoder.decode(name, "UTF-8"));
			member.setPassword(DigestUtils.md5Hex(password));
			member.setPoint(setting.getRegisterPoint());
			member.setAmount(new BigDecimal(0));
			member.setBalance(new BigDecimal(0));
			member.setIsEnabled(true);
			member.setIsLocked(Member.LockType.none);
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
			member.setRegisterIp(request.getRemoteAddr());
			member.setLoginIp(request.getRemoteAddr());
			member.setLoginDate(new Date());
			member.setSafeKey(null);
			member.setBindEmail(Member.BindStatus.none);
			member.setBindMobile(Member.BindStatus.none);
			member.setPaymentPassword(DigestUtils.md5Hex(password));
			member.setRebateAmount(new BigDecimal(0));
			member.setProfitAmount(new BigDecimal(0));
			member.setMemberRank(memberRankService.findDefault());
			member.setMember(agent);
			member.setFavoriteProducts(null);
			member.setMobile(mobile);
			member.setEmail(email);
			member.setFreezeBalance(new BigDecimal(0));
			member.setPrivilege(0);
			member.setTotalScore((long) 0);
			memberService.save(member);
		} catch (UnsupportedEncodingException e) {
			return Message.error("用户信息有误");
		}
		return Message.success("shop.register.success");
	}

	/**
	 * 注册会员
	 */
	@RequestMapping(value = "/memberSubmit", method = RequestMethod.POST)
	@ResponseBody
	public Message memberSubmit(String username, String mobile, String email, Long areaId, String password, String name, String address, String gender, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		if (!checkUsername(username)) {
			return Message.error("用户名已经被注册");
		}
		Setting setting = SettingUtils.get();
		Member member = EntitySupport.createInitMember();
		Member agent = memberService.getCurrent();
		if (agent == null) {
			return Message.error("会员中心店长不存在");
		}
		if (areaId != null) {
			Area area = areaService.find(areaId);
			member.setArea(area);
		}
		Gender sex = StringUtils.isNotEmpty(gender) ? Gender.valueOf(gender) : null;
		try {
			member.setUsername(username);
			member.setGender(sex);
			if (address != null) {
				member.setAddress(URLDecoder.decode(address, "UTF-8"));
			}
			member.setName(URLDecoder.decode(name, "UTF-8"));
			member.setPassword(DigestUtils.md5Hex(password));
			member.setPoint(setting.getRegisterPoint());
			member.setAmount(new BigDecimal(0));
			member.setBalance(new BigDecimal(0));
			member.setIsEnabled(true);
			member.setIsLocked(Member.LockType.none);
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
			member.setRegisterIp(request.getRemoteAddr());
			member.setLoginIp(request.getRemoteAddr());
			member.setLoginDate(new Date());
			member.setSafeKey(null);
			member.setBindEmail(Member.BindStatus.none);
			member.setBindMobile(Member.BindStatus.binded);
			member.setPaymentPassword(DigestUtils.md5Hex(password));
			member.setRebateAmount(new BigDecimal(0));
			member.setProfitAmount(new BigDecimal(0));
			member.setMemberRank(memberRankService.findDefault());
			member.setFavoriteProducts(null);
			member.setMobile(mobile);
			member.setEmail(username + "@139.com");
			member.setMember(agent);
			member.setFreezeBalance(new BigDecimal(0));
			member.setPrivilege(0);
			member.setTotalScore((long) 0);
			memberService.save(member);
		} catch (UnsupportedEncodingException e) {
			return Message.error("用户信息有误");
		}
		return Message.success("shop.register.success");
	}
}