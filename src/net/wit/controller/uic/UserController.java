/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.uic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.Area;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Idcard;
import net.wit.entity.SmsSend;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.entity.SafeKey;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.FileService;
import net.wit.service.IdcardService;
import net.wit.service.MailService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.support.EntitySupport;
import net.wit.util.Base64Util;
import net.wit.util.DESUtil;
import net.wit.util.DateUtil;
import net.wit.util.GsonUtil;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 用户管理
 * @author rsico Team
 * @version 3.0
 */
@Controller("uicUserController")
@RequestMapping("/uic/user/")
public class UserController extends BaseController {

	public static final String ENCODE_TYPE_BASE64 = "BASE64";
	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
	public static final String REGISTER_TYPE_SESSION = "register_type_session";
	public static final String REGISTER_CONTENT_SESSION = "register_content_session";

	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;
	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public Message sendMobile(String mobile,String username, HttpServletRequest request) {
		if (mobile!=null && username==null && memberService.usernameExists(mobile) ) {
		   return Message.error("手机号已经注册");	
		}
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (tmp!=null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
			if (!tmp.canReset()) {
				return Message.error("系统忙，稍等几秒重试");
			}
		}
		if (mobile==null) {
			Member member = memberService.findByUsername(username);
			if (member==null) {
				return Message.error("用户名无效，请先注册");
			}
			if (!member.getBindMobile().equals(Member.BindStatus.binded) ) {
				return Message.error("当前用户没有绑定手机号");
			}
			mobile = member.getMobile();
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(REGISTER_TYPE_SESSION, "mobile");
		session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
		
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setIp(request.getRemoteAddr().toString());
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return Message.success("发送成功");
	}

	/**
	 * 发送邮件
	 */
	@RequestMapping(value = "/send_email", method = RequestMethod.POST)
	@ResponseBody
	public Message sendEmail(String email,String username, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (email!=null && username==null && memberService.usernameExists(email) ) {
			   return Message.error("此邮箱已经注册");	
			}
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (tmp!=null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
		}
		if (email==null) {
			Member member = memberService.findByUsername(username);
			if (member==null) {
				return Message.error("用户名无效，请先注册");
			}
			if (!member.getBindEmail().equals(Member.BindStatus.binded) ) {
				return Message.error("当前用户没有绑定邮箱");
			}
			email = member.getEmail();
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(REGISTER_TYPE_SESSION, "email");
		session.setAttribute(REGISTER_CONTENT_SESSION, email);
		mailService.sendRegisterMail(email, email, safeKey);
		return Message.success("发送成功");
	}

	/**
	 * 检测验证码
	 */
	@RequestMapping(value = "/check_captcha", method = RequestMethod.POST)
	@ResponseBody
	public Message checkCaptcha(String captcha, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return Message.error("验证码不正确");
		}
		return Message.success("验证码正确");
	}
	
	/**
	 * 注册用户
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public Message register(String username, String mobile, String email,Long areaId, String password,String captcha,String Extension, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_TYPE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return Message.error("验证码不正确");
		}


		if (username!=null && memberService.usernameExists(username)) {
			return Message.error("用户已经注册");	
		}
		if (mobile!=null && (memberService.usernameExists(mobile) || memberService.mobileExists(mobile))) {
			return Message.error("手机号已经注册");	
		}
		
		// 用户名注册时 获取的密码
		if (StringUtils.isEmpty(password)) {
			password = Base64Util.decode(request.getParameter("enPassword"));
		}
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
			return Message.error("系统关闭注册");
		}

		if (username.length() < setting.getUsernameMinLength() || username.length() > setting.getUsernameMaxLength()) {
			return Message.error("用户名长度不够");
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return Message.error("密码长度不够");
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			return Message.error("用户名已经被注册");
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
		member.setScore(0f);
		member.setScoreCount(0L);
	    if (Extension!=null) {
	    	member.setMember(memberService.findByUsername(Extension));
	    }

		String domain = setting.getCookieDomain();
		if (domain == null) {
			String host = request.getServerName();
			String[] hosts = host.split("\\.");
			if (hosts.length == 3) {
				domain = "." + hosts[1] + "." + hosts[2];
			}
		}
		
		member.setMobile(username);
		member.setEmail(email);
		member.setMobile(mobile);
		if (mobile != null) {
			member.setBindMobile(BindStatus.binded);
		}
		if (email != null) {
			member.setBindEmail(BindStatus.binded);
		}
			
		if (email==null) {
			member.setEmail("#");
		}
		memberService.save(member);

		String openid = request.getParameter("openid");
		if (openid != null && !"".equals(openid)) {
			BindUser user = new BindUser();
			user.setUsername(openid);
			user.setPassword(DigestUtils.md5Hex(password));
			user.setMember(member);
			user.setType(Type._wx);
			bindUserService.save(user);
		}
		
		Cart cart = cartService.getCurrent();
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
		if (cart != null && cart.getMember() == null) {
			cartService.merge(member, cart);
			WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
			WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
		}
		//smsSendService.sysSend(member.getUsername(), "注册成功,账号:" + member.getUsername() + ",密码:" + password + ".【" + bundle.getString("signature") + "】");
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("注册成功,账号:" + member.getUsername() + ".【" + bundle.getString("signature") + "】");
		smsSend.setIp(request.getRemoteAddr().toString());
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return Message.success(member.getUsername());
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePass", method = RequestMethod.POST)
	public @ResponseBody Message updatePass(String enType, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
		   return Message.error("无效用户名");
		}
		String oldPass = null;
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			oldPass = rsaService.decryptParameter("oldPass", request);
			newPass = rsaService.decryptParameter("newPass", request);
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			oldPass = Base64Util.decode(request.getParameter("oldPass"));
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		if (StringUtils.isEmpty(oldPass) && StringUtils.isEmpty(newPass)) {
			return Message.error("密码无效");
		}

		if (DigestUtils.md5Hex(oldPass).equals(member.getPassword())) {
			member.setPassword(DigestUtils.md5Hex(newPass));
			memberService.update(member);
			return Message.success("修改成功");

		} else {
			return Message.error("密码错误");
		}
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePaymentPass", method = RequestMethod.POST)
	public @ResponseBody Message updatePaymentPass(String enType, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
		   return Message.error("无效用户名");
		}
		String oldPass = null;
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			oldPass = rsaService.decryptParameter("oldPass", request);
			newPass = rsaService.decryptParameter("newPass", request);
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			oldPass = Base64Util.decode(request.getParameter("oldPass"));
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		if (StringUtils.isEmpty(oldPass) && StringUtils.isEmpty(newPass)) {
			return Message.error("支付密码无效");
		}

		if(StringUtils.isEmpty(member.getPaymentPassword()) && DigestUtils.md5Hex(oldPass).equals(member.getPassword())){
			member.setPaymentPassword(DigestUtils.md5Hex(newPass));
			memberService.update(member);
			return Message.success("修改成功");
		}else if (DigestUtils.md5Hex(oldPass).equals(member.getPaymentPassword())) {
			member.setPaymentPassword(DigestUtils.md5Hex(newPass));
			memberService.update(member);
			return Message.success("修改成功");

		} else {
			return Message.error("支付密码错误");
		}
	}

	/**
	 * 找回登录密码
	 */
	@RequestMapping(value = "/retrievePass", method = RequestMethod.POST)
	public @ResponseBody Message retrievePass(String username,String captcha, String enType, HttpServletRequest request) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		String type = (String) session.getAttribute(REGISTER_TYPE_SESSION);
		String content = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_TYPE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);

		if (StringUtils.isEmpty(username)) {
			return Message.error("无效用户名");
		}
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			newPass = rsaService.decryptParameter("newPass", request);
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		
		Member member = memberService.findByUsername(username);
		if (member==null) {
			return Message.error("无效用户名");
		}
		
		if (type.equals("mobile")) {
			if (!member.getMobile().equals(content) || member.getBindMobile() != BindStatus.binded) {
				return Message.error("该用户手机未绑定");
			}
		}
		
		if (type.equals("email")) {
			if (!member.getEmail().equals(content) || member.getBindEmail() != BindStatus.binded) {
				return Message.error("该用户邮箱未绑定");
			}
		}
		
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return Message.error("验证码不正确");
		}

		member.setPassword(DigestUtils.md5Hex(newPass));
    	memberService.update(member);
		return Message.success("修改成功");
	}
	
	/**
	 * 找回支付密码
	 */
	@RequestMapping(value = "/retrievePayMentPass", method = RequestMethod.POST)
	public @ResponseBody Message retrievePayMentPass(String username,String captcha, String enType, HttpServletRequest request) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		String type = (String) session.getAttribute(REGISTER_TYPE_SESSION);
		String content = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_TYPE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);

		if (StringUtils.isEmpty(username)) {
			return Message.error("无效用户名");
		}
		String newPass = null;
		if (StringUtils.isBlank(enType)) {
			newPass = rsaService.decryptParameter("newPass", request);
			rsaService.removePrivateKey(request);
		} else if (ENCODE_TYPE_BASE64.equals(enType)) {
			newPass = Base64Util.decode(request.getParameter("newPass"));
		}
		
		Member member = memberService.findByUsername(username);
		if (member==null) {
			return Message.error("无效用户名");
		}
		
		if (type.equals("mobile")) {
			if (!member.getMobile().equals(content) || member.getBindMobile() != BindStatus.binded) {
				return Message.error("该用户手机未绑定");
			}
		}
		
		if (type.equals("email")) {
			if (!member.getEmail().equals(content) || member.getBindEmail() != BindStatus.binded) {
				return Message.error("该用户邮箱未绑定");
			}
		}
		
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return Message.error("验证码不正确");
		}

		member.setPaymentPassword(DigestUtils.md5Hex(newPass));
    	memberService.update(member);
		return Message.success("修改成功");
	}

	/**
	 * 绑定手机/邮箱
	 */
	@RequestMapping(value = "/bindUpdate", method = RequestMethod.POST)
	public @ResponseBody Message bind_save(String captcha, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		String type = (String) session.getAttribute(REGISTER_TYPE_SESSION);
		String content = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_TYPE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error("当前会话失败，请重新登录");
		}
		
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return Message.error("验证码不正确");
		}

		if ("email".equals(type)) {
			if (member.getBindEmail().equals(BindStatus.binded)) {
				if (!member.getMobile().equals(content)) {
					return Message.error("无效Email");
				}
				member.setEmail(content);
				member.setBindEmail(BindStatus.unbind);
			} else {
				member.setEmail(content);
				member.setBindEmail(BindStatus.binded);
			}
		} else {
			if (member.getBindMobile().equals(BindStatus.binded)) {
				if (!member.getMobile().equals(content)) {
					return Message.error("无效手机号");
				}
				member.setMobile(content);
				member.setBindMobile(BindStatus.unbind);
			} else {
				member.setMobile(content);
				member.setBindMobile(BindStatus.binded);
			}
		}
		memberService.save(member);
		return Message.success("绑定成功");
	}
	
	
	/**
	 * 修改用户信息
	 */
	@RequestMapping(value = "/editInfo", method = RequestMethod.POST)
	public @ResponseBody Message editInfo(String name, String birth, String address,String phone, String zipCode, String sex, String areaId, String headImg, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("登录超时");
		}
		if (!StringUtils.isEmpty(name)) {
			if (member.getName() != null && !member.getName().equals(name)) {
				if (member.getIdcard() != null && member.getIdcard().getAuthStatus().equals(Idcard.AuthStatus.success)) {
					return Message.error("实名认证通过，不能修改姓名");
				}
			}
			member.setName(name);
		}
		if (!StringUtils.isEmpty(sex) && sex != null) {
			if ("0".equals(sex)) {
				member.setGender(Gender.male);
			} else {
				member.setGender(Gender.female);
			}
		}
		if (areaId != null) {
			Area area = areaService.find(Long.valueOf(areaId));
			member.setArea(area);
		}
		if (!StringUtils.isEmpty(birth) && birth != null) {
			member.setBirth(DateUtil.parseDate(birth));
		}
		if (!StringUtils.isEmpty(address) && address != null) {
			member.setAddress(address);
		}
		if (!StringUtils.isEmpty(phone) && phone != null) {
			member.setPhone(phone);
		}
		if (!StringUtils.isEmpty(zipCode) && zipCode != null) {
			member.setZipCode(zipCode);
		}
		if (!StringUtils.isEmpty(headImg) && headImg != null) {
			member.setHeadImg(headImg);
		}
		memberService.save(member);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 上传文件
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody Message uploadFile(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String imgUrl = "";
		// 图片
		try {
			imgUrl = fileService.upload(FileType.image,request,"default.jpg",true);
		} catch (Exception e) {
			return Message.success("上传失败了");
		}
		return Message.success(imgUrl);
	}

	/**
	 * 上传人头像
	 */
	@RequestMapping(value = "/uploadHeadImg", method = RequestMethod.POST)
	public @ResponseBody Message uploadImg(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		try {
            Message msg = uploadFile(request,redirectAttributes);
            if (msg.getType().equals(Message.Type.success)) {
		    	member.setHeadImg(msg.getContent());
			    memberService.save(member);
				return Message.success("修改成功");
           } else {
                return msg;
            }
 		} catch (Exception e) {
			return Message.success("修改失败了");
		}
	}
	
	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/idcardSave", method = RequestMethod.POST)
	public @ResponseBody Message idcardSave(String username, String name, String idCard, String pathFront, String pathBack, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("登录超时");
		}
		Idcard idcard = member.getIdcard();
		if (pathFront == null || "".equals(pathFront)) {
			return Message.error("无拍照图片");
		}
		if (pathBack == null || "".equals(pathBack)) {
			return Message.error("无拍照图片");
		}
		if (name == null || "".equals(name)) {
			return Message.error("请正确输入姓名");
		}
		if (idcard == null) {
			idcard = new Idcard();
		}
		idcard.setPathFront(pathFront);
		idcard.setPathBack(pathBack);
		idcard.setNo(idCard);
		idcard.setAddress("#");
		idcard.setBeginDate(new Date());
		idcard.setEndDate(new Date());
		idcard.setAuthStatus(AuthStatus.wait);
		idcard.setName(name);
		idcardService.save(idcard);
//		member.setName(name);
		member.setIdcard(idcard);
		memberService.save(member);
		return Message.success("修改成功");
	}

	/**
	 * 通过令牌读取用户
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String getUser(String token,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Member member = memberService.getCurrent();
		return GsonUtil.toJson(member, true);
	}

	
}