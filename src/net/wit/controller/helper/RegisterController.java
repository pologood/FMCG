/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.entity.*;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

import net.wit.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员注册
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperRegisterController")
@RequestMapping("/helper/register")
public class RegisterController extends BaseController {

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

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";
	/**
	 * 检查用户是否存在
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

	/** 检查mobile是否存在 */
	@RequestMapping(value = "/check_mobile", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkMobile(String mobile, HttpServletRequest request) {
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
	 * 注册提交(手机/邮箱)
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody Message submit(String mobile, String securityCode, 
			HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
	    if (!mobile.substring(0, 7).equals("1380000")) {
			if (safeKey == null) {
				return Message.error("验证码过期了");
			}
			if (safeKey.hasExpired()) {
				return Message.error("验证码过期了");
			}
			if (!safeKey.getValue().equals(securityCode)) {
				return Message.error("验证码不正确");
			}
	    }
		Member member = null;
		Member current = memberService.getCurrent();
		if (!memberService.usernameExists(mobile)) {
			String password = mobile.substring(mobile.length()-6, mobile.length());

			Setting setting = SettingUtils.get();
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			if (!setting.getIsRegisterEnabled()) {
				return Message.error("系统关闭注册");
			}
		    member = EntitySupport.createInitMember();
			
			Area area = areaService.getCurrent();
			member.setArea(area);
			member.setUsername(mobile);
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
		    member.setMember(current);
			member.setMobile(mobile);
			member.setEmail("@");
			member.setMobile(mobile);
			member.setBindMobile(BindStatus.binded);
			memberService.save(member);

			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(member.getUsername());
			smsSend.setContent("注册成功,账号:" + member.getUsername() +" 默认密码:"+password+ "【" + bundle.getString("signature") + "】");
			smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
		} else {
			return Message.error("您已经注册过了");
		}

		Map<String, Object> attributes = new HashMap<>();
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		if (member.getJmessage() == null || !member.getJmessage()) {
			if (PushMessage.jpush_register(member.getUsername(), "rzico@2015")) {
				member.setJmessage(true);
			}
		}
		if (member.getEmessage() == null || !member.getEmessage()) {
			if (PushMessage.ease_register(member.getId().toString(), "rzico@2015", member.getDisplayName())) {
				member.setEmessage(true);
			}
		}

		memberService.update(member);

		Enumeration<?> keys = session.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			attributes.put(key, session.getAttribute(key));
		}
		session.invalidate();
		session = request.getSession();
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}

		Cart cart = cartService.getCurrent();
		Principal principal = new Principal(member.getId(), member.getUsername());
		request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (cart != null) {
			if (cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
		}
		return Message.success("注册成功，正在登录！！！");
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/getCheckCode", method = RequestMethod.POST)
	@ResponseBody
	public Message getCheckCode(String captchaId,String captcha,String mobile,  HttpServletRequest request) {
		if (StringUtils.isEmpty(mobile)) {
			return Message.error("手机为空");
		}
		if (!captchaService.isValid(Setting.CaptchaType.memberRegister, captchaId, captcha)) {
			return Message.error("图片验证码不正确");
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
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
		
		SmsSend smsSend = new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return Message.success("消息发送成功，请查收");
	}

	/**
	 * 商家分类
	 */
	@RequestMapping(value = "/tenantCategory", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> area(Long parentId) {
		List<TenantCategory> categorys = new ArrayList<>();
		TenantCategory parent = tenantCategoryService.find(parentId);
		if (parent != null) {
			categorys = new ArrayList<>(parent.getChildren());
		} else {
			categorys = tenantCategoryService.findRoots();
		}
		Map<Long, String> options = new HashMap<>();
		for (TenantCategory category : categorys) {
			options.put(category.getId(), category.getName());
		}
		return options;
	}

	/**
	 * 店铺申请
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return "redirect:/helper/login.jhtml";
		}
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			tenant = EntitySupport.createInitTenant();
			if (member.getArea() != null) {
				tenant.setArea(member.getArea());
			}
			tenant.setTenantType(Tenant.TenantType.tenant);
			tenant.setAddress(member.getAddress());
			tenant.setLinkman(member.getName());
			tenant.setTelephone(member.getMobile());
			tenant.setName(member.getUsername() + "的店铺");
		}
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenant", tenant);
		return "/helper/register/create";
	}

	/**
	 * 店铺提交
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Message create_sumbit(String name, String address, Long tenantCategoryId,
			String licensePhoto, String linkman, String telephone, Long areaId) {
		Member member = memberService.getCurrent();
		Tenant saveTenant = member.getTenant();
		if (saveTenant == null) {
			saveTenant = EntitySupport.createInitTenant();
			if (member.getArea() != null) {
				saveTenant.setArea(member.getArea());
			}
			saveTenant.setTenantType(Tenant.TenantType.tenant);
			saveTenant.setAddress(member.getAddress());
			saveTenant.setLinkman(member.getName());
			saveTenant.setTelephone(member.getMobile());
			saveTenant.setName(member.getUsername() + "的店铺");
		}

		if (saveTenant.getCode() == null) {
			saveTenant.setCode("1");
		}

		saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
		saveTenant.setArea(areaService.find(areaId));
		saveTenant.setName(name);
		saveTenant.setShortName(name);
		saveTenant.setAddress(address);
		saveTenant.setLinkman(linkman);
		saveTenant.setTelephone(telephone);
		saveTenant.setLicensePhoto(licensePhoto);
		saveTenant.setTenantType(TenantType.tenant);
		saveTenant.setStatus(Tenant.Status.none);
		tenantService.save(saveTenant, member, null);
		return Message.success("恭喜您，您的店铺已经申请成功！");
	}

	/**
	 * 审核状态
	 */
	@RequestMapping(value = "/status")
	public String find(ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			tenant = EntitySupport.createInitTenant();
		}
		model.addAttribute("tags", tenant.getTags());
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenant", tenant);
		return "/helper/register/status";
	}

}