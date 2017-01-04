/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.math.BigDecimal;
import java.util.Calendar;
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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.Setting.AccountLockType;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Application;
import net.wit.entity.Area;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Cart;
import net.wit.entity.Host;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.Tenant;
import net.wit.service.ApplicationService;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.HostService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.service.TenantCategoryService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.uic.api.UICService;
import net.wit.util.Base64Util;
import net.wit.util.DESUtil;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

/**
 * Controller - 会员
 * @author rsico Team
 * @version 3.0
 */
@Controller("auth2Controller")
@RequestMapping("/pos/auth2")
public class Auth2Controller extends BaseController {

	public static final String REGISTER_SECURITYCODE_SESSION = "register_safe_key";
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "hostServiceImpl")
	private HostService hostService;

	@Resource(name = "uicService")
	private UICService uicService;
	
	@Resource(name = "applicationServiceImpl")
	private ApplicationService applicationService;
	
	/**
	 * 检查mobile是否存在
	 */
	@RequestMapping(value = "/check_mobile", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return false;
		}
		if (memberService.usernameDisabled(mobile) || memberService.usernameExists(mobile)||memberService.mobileExists(mobile)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 软件参数
	 */
	@RequestMapping(value = "/params", method = RequestMethod.GET)
	public  @ResponseBody Map<String,Object> software(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if ((tenant!=null) && (tenant.getSoftware()!=null)) {
		  map.put("version",tenant.getSoftware().getVersion());
		  map.put("industry",tenant.getSoftware().getIndustry());
		  Host host = tenant.getHost();
		  if (host==null) {
			 host = hostService.find(new Long(1));
		  }
		  map.put("hostname",host.getHostname());
		  map.put("dbid",host.getDbid());
		  map.put("port",host.getPort());
		}
		return map;
	}
	
	/**
	 * 检查mobile是否存在
	 */
	@RequestMapping(value = "/ready", method = RequestMethod.GET)
	public @ResponseBody String ready(HttpServletRequest request, HttpSession session) {
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
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(DateUtils.addMinutes(new Date(), 60));
		session.setAttribute(Member.CHALLEGE_ATTRIBUTE_NAME, safeKey);
		return securityCode;
	}

	/**
	 * 检查mobile是否存在
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody 
	DataBlock login(String username,String password,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Member member = memberService.findByUsername(username);
		HttpSession session = request.getSession();
		if (member == null) {
			return DataBlock.error("用户名无效");
		}
		if (!member.getIsEnabled()) {
			return DataBlock.error("用户已经禁用了");
		}
		
		SafeKey safeKey = (SafeKey) request.getSession().getAttribute(Member.CHALLEGE_ATTRIBUTE_NAME);
		if (safeKey == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码已过期");
		}
		Setting setting = SettingUtils.get();
		if (!member.getIsLocked().equals(Member.LockType.none)) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.member)) {
				if(member.getIsLocked().equals(Member.LockType.freezed)){
					return DataBlock.error("你的账户被封禁15天。");
				}
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					return DataBlock.error("用户已锁定，请稍候再重试");
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(Member.LockType.none);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					return DataBlock.error("用户已锁定，请稍候再重试");
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(Member.LockType.none);
				member.setLockedDate(null);
				memberService.update(member);
			}
		}

		String pwd = DigestUtils.md5Hex(member.getPassword() + safeKey.getValue() + "vst@2014-2020$$");
		if (!pwd.equals(password)) {
			int loginFailureCount = member.getLoginFailureCount() + 1;
			if (loginFailureCount >= setting.getAccountLockCount()) {
				member.setIsLocked(Member.LockType.locked);
				member.setLockedDate(new Date());
				if (member.getMobile() != null) {
					ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
					SmsSend smsSend=new SmsSend();
					smsSend.setMobiles(member.getMobile());
					smsSend.setContent("密码输入错误，累计超过" + member.getLoginFailureCount().toString() + "次，为保证您的账户安全，您的账号已经被锁定。【" + bundle.getString("signature") + "】");
					smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
					smsSendService.smsSend(smsSend);
				}
				return DataBlock.error("登录密码无效");
			}

			member.setLoginFailureCount(loginFailureCount);
			memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.member)) {
				return DataBlock.error("b2b.login.accountLockCount", setting.getAccountLockCount());
			} else {
				return DataBlock.error("b2b.login.incorrectCredentials");
			}
		}
		
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		if (member.getJmessage()==null || !member.getJmessage()) {
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
		for (Entry<String, Object> entry : attributes.entrySet()) {
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
		principal.createSign();
		return DataBlock.success(JsonUtils.toJson(principal),"登录成功");
	}

	/**
	 * 检查mobile是否存在
	 */
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public String redirect(String username,String token,String redirectUrl,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Member member = memberService.findByUsername(username);
		if (member!=null) {
			Principal principal = JsonUtils.toObject(token, Principal.class);
			StringBuilder builder = new StringBuilder();
			builder.append(String.valueOf(principal.getId()));
			builder.append(principal.getUsername());
			builder.append(principal.getTimestamp());
			builder.append("wit@2014-2020$$");
			if (principal.getSign().equals(DigestUtils.md5Hex(builder.toString()))) {
			    member = memberService.findByUsername(principal.getUsername());
				Setting setting = SettingUtils.get();
				String domain = null;// setting.getCookieDomain();
				String host = request.getServerName();
				String[] hosts = host.split("\\.");
				if (hosts.length == 3) {
					domain = "." + hosts[1] + "." + hosts[2];
				}
				Cart cart = cartService.getCurrent();
				WebUtils.addCookie(request, response, Cookies.HOST, setting.getSiteUrl(), -1, "/", domain, null);
				Principal newPrincipal = new Principal(member.getId(), member.getUsername());
				request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, newPrincipal);
				newPrincipal.createSign();
				String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(newPrincipal), Constant.generateKey);
				WebUtils.addCookie(request, response, Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", domain, null);
				WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
				if (cart != null) {
					if (cart.getMember() == null) {
						cartService.merge(member, cart);
						WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
						WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
					}
				}
			}

		}
		return "redirect:"+redirectUrl;
	}
	
	

	/**
	 * 获取当前会话令牌
	 */
	@RequestMapping(value = "/get_token", method = RequestMethod.GET)
	public @ResponseBody
	DataBlock getToken(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		Tenant	tenant = member.getTenant();
		if (tenant==null) {
		   return DataBlock.error("当前账号没有开通店铺管理功能，");
		}
		
		String token = memberService.getToken(member);
		
		return DataBlock.success(token,"执行成功");
	}
	
	/**
	 * 获取当前门店的开通状态
	 */
	@RequestMapping(value = "/check_status", method = RequestMethod.GET)
	public @ResponseBody
	DataBlock checkStatus(Long tenantId,String shopId,String shopName,HttpServletRequest request) {
		Tenant tenant = tenantService.find(tenantId);
		Application app = applicationService.findApplication(tenant,shopId,Application.Type.erp);
		if (app==null) {
			app = new Application();
			app.setType(Application.Type.erp);
			app.setCode(shopId);
			app.setName(shopName);
			app.setMember(tenant.getMember());
			app.setTenant(tenant);
			
			Setting setting = SettingUtils.get();
			app.setPrice(setting.getFunctionFee()); 
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 15);
			app.setValidityDate(cal.getTime());
			app.setStatus(Application.Status.none);
			applicationService.save(app);
		} else {
			app.setModifyDate(new Date());
			applicationService.save(app);
		}
		if (app.getStatus().equals(Application.Status.closed)) {
			return DataBlock.error("当前店铺已经关闭，不能使用。");
		}
		Calendar vld = Calendar.getInstance();
		vld.setTime(app.getValidityDate());
		vld.add(Calendar.DAY_OF_MONTH, -15);
		if (new Date().compareTo(vld.getTime())>=0) {
		   Long diff = app.getValidityDate().getTime()-new Date().getTime();
		   Long nd = 1000*24*60*60L;
		   Long day = diff/nd+1;
		   app.setStatus(Application.Status.none);
		   applicationService.save(app);
		   if (day.compareTo(0L)<=0) {
			  return DataBlock.error("当前店铺已经到期，请及时缴费");
		   } else {
		      return DataBlock.warn("当前店铺将在"+day.toString()+"天后到期，请及时续费");
		   }
		}
		return DataBlock.success("success","执行成功");
	}

	
	/**
	 * 获取当前会话令牌
	 */
	@RequestMapping(value = "/principal", method = RequestMethod.GET)
	public @ResponseBody
	DataBlock principal(HttpServletRequest request) {
		Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		if (principal != null) {
			principal.createSign();
			return DataBlock.success(JsonUtils.toJson(principal),"success");
		}
		return DataBlock.error("error");
	}

	/**
	 * 注册提交(手机)
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock submit(String mobile, String captcha, Long areaId, String password, String shopName, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}

		if (memberService.findByUsername(mobile)!=null) {
			return DataBlock.error("当前手机已经注册");	
		}
		
		// 用户名注册时 获取的密码
		if (StringUtils.isEmpty(password)) {
			password = Base64Util.decode(request.getParameter("enPassword"));
		}
		
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		if (!isValid(Member.class, "username", mobile, Save.class)) {
			return DataBlock.error("手机号无效");
		}
		if (!isValid(Member.class, "password", password, Save.class)) {
			return DataBlock.error("密码太简单了");
		}
		if (!setting.getIsRegisterEnabled()) {
			return DataBlock.error("系统关闭注册");
		}

		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return DataBlock.error("密码长度不够");
		}
		if (memberService.usernameDisabled(mobile) || memberService.usernameExists(mobile)) {
			return DataBlock.error("用户名已经被注册");
		}
		Member member = EntitySupport.createInitMember();
		if (areaId != null) {
			Area area = areaService.find(areaId);
			member.setArea(area);
		}
		if (member.getArea()==null) {
			member.setArea(areaService.find(0L));
		}
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
		
		member.setMobile(mobile);
		member.setEmail("@");
		member.setMobile(mobile);
		member.setBindMobile(BindStatus.binded);
		memberService.save(member);

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

		Principal principal = new Principal(member.getId(), member.getUsername());
		request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (cart != null && cart.getMember() == null) {
			cartService.merge(member, cart);
			WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
			WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
		}
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(member.getUsername());
		smsSend.setContent("注册成功,账号:" + member.getUsername() + ".【" + bundle.getString("signature") + "】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		
	    Tenant	tenant = EntitySupport.createInitTenant();
		tenant.setArea(member.getArea());
		tenant.setTenantType(Tenant.TenantType.tenant);
		tenant.setAddress("无");
		tenant.setLinkman(member.getMobile());
		tenant.setTelephone(member.getMobile());
		tenant.setName(shopName);
		tenant.setShortName(shopName);
		tenant.setTenantCategory(tenantCategoryService.findFirst());
		Host host = hostService.find(new Long(1));
		tenant.setHost(host);
		tenantService.save(tenant, member, null);
		return DataBlock.success("success","注册成功");

	}

	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock sendMobile(String mobile, HttpServletRequest request, HttpSession session) {
		if (mobile!=null && memberService.usernameExists(mobile) ) {
			   return DataBlock.error("手机号已经注册");	
			}
			Setting setting = SettingUtils.get();
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			int challege = SpringUtils.getIdentifyingCode();
			String securityCode = String.valueOf(challege);
			SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
			if (tmp!=null && !tmp.hasExpired()) {
				securityCode = tmp.getValue();
				if (!tmp.canReset()) {
					return DataBlock.error("系统忙，稍等几秒重试");
				}
			}
			SafeKey safeKey = new SafeKey();
			safeKey.setValue(securityCode);
			safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
			session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
			session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
			
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(mobile);
			smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
			smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
			return DataBlock.success("success","发送成功");
	}

}