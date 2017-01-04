/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.uic;

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

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.Setting.AccountLockType;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Host;
import net.wit.entity.Member;
import net.wit.entity.SmsSend;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.entity.Tenant;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.util.Base64Util;
import net.wit.util.DESUtil;
import net.wit.util.GsonUtil;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 认证API
 * @author rsico Team
 * @version 3.0
 */
@Controller("ssoAuthController")
@RequestMapping("/sso/")
public class AuthController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;

	/**
	 * 登录密码
	 */
	@RequestMapping(value = "/check-password", method = RequestMethod.GET)
	public @ResponseBody Message checkPassword(String password, HttpServletRequest request) {
		if (StringUtils.isEmpty(password)) {
			password = Base64Util.decode(request.getParameter("enPassword"));
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("会话已经失效。");
		}
		String mypwd = member.getPaymentPassword();
		if (mypwd == null) {
			mypwd = member.getPassword();
		}
		if (mypwd.equals(DigestUtils.md5Hex(password))) {
			return Message.success("密码正确。");
		} else {
			return Message.error("密码错误。");
		}
	}

	/**
	 * 获取参数
	 */
	@RequestMapping(value = "/Params", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> params(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if ((tenant != null) && (tenant.getSoftware() != null)) {
			map.put("host", "");
			map.put("industry", tenant.getSoftware().getIndustry());
			Host host = tenant.getHost();
			if (host != null) {
				map.put("hostname", host.getHostname());
				map.put("dbid", host.getDbid());
				map.put("port", host.getPort());
				map.put("host", host.getHost());
			}
		}
		return map;

	}

	/**
	 * 获取登录校验码
	 */
	@RequestMapping(value = "/auth-get", method = RequestMethod.GET)
	public @ResponseBody String getChallege(HttpServletRequest request, HttpSession session) {
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
	 * 用户登录 通过用户名 密码
	 */
	@RequestMapping(value = "/user-login", method = RequestMethod.GET)
	public @ResponseBody Message login(String username, String password, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Member member = memberService.findByUsername(username);
		if (member==null) {
			member = memberService.findByBindTel(username);
		}
		if (member == null) {
			return Message.error("b2b.login.unknownAccount");
		}
		if (!member.getIsEnabled()) {
			return Message.error("b2b.login.disabledAccount");
		}
		SafeKey safeKey = (SafeKey) request.getSession().getAttribute(Member.CHALLEGE_ATTRIBUTE_NAME);
		if (safeKey == null) {
			return Message.error("无效会话");
		}
		if (safeKey.hasExpired()) {
			return Message.error("校验码过期");
		}

		Setting setting = SettingUtils.get();
		if (!member.getIsLocked().equals(Member.LockType.none)) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.member)) {
				if(member.getIsLocked().equals(Member.LockType.freezed)){
					return Message.warn("您好，你的账户行为触发了反作弊系统警报，正在接受系统审查，你的账户被封禁15天。如有疑问请拨打客服热线。");
				}
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					return Message.error("b2b.login.lockedAccount");
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(Member.LockType.none);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					return Message.error("b2b.login.lockedAccount");
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(Member.LockType.none);
				member.setLockedDate(null);
				memberService.update(member);
			}
		}

		String openid = request.getParameter("openid");
		if (openid != null && !"".equals(openid) && (bindUserService.findByUsername(openid, Type._wx) == null)) {
			BindUser user = new BindUser();
			user.setUsername(openid);
			user.setPassword(DigestUtils.md5Hex(password));
			user.setMember(member);
			user.setType(Type._wx);
			bindUserService.save(user);
		}

		String type = request.getParameter("type");
		if (type!=null && type.equals("bind") && member.getBindMobile().equals(BindStatus.binded)) {
			String mac = Base64Util.decode(request.getParameter("mac"));
			if (!"".equals(mac)) {
				BindUser user = bindUserService.findByUsername(mac, Type._mac);
				if (user==null) {
					user = new BindUser();
				}
				user.setUsername(mac);
				user.setPassword(DigestUtils.md5Hex(mac+username));
				user.setMember(member);
				user.setType(Type._mac);
				bindUserService.save(user);
			}
		}
		String pwd = null;
		if (type!=null && type.equals("mac") && member.getBindMobile().equals(BindStatus.binded)) {
			String mac = Base64Util.decode(request.getParameter("mac"));
			BindUser user = bindUserService.findByUsername(mac, Type._mac);
			if (user==null) {
				return Message.error("nothing");
			}
			pwd = DigestUtils.md5Hex(user.getPassword() + safeKey.getValue() + "vst@2014-2020$$");
			
		} else if (type!=null && type.equals("mac")) {
			return Message.error("nothing");
		} else {
			pwd = DigestUtils.md5Hex(member.getPassword() + safeKey.getValue() + "vst@2014-2020$$");
		}
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
					smsSend.setType(SmsSend.Type.captcha);
					smsSend.setIp(request.getRemoteAddr().toString());
					smsSendService.smsSend(smsSend);
				}
				return Message.error("密码校验错误");
			}

			member.setLoginFailureCount(loginFailureCount);
			memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.member)) {
				return Message.error("b2b.login.accountLockCount", setting.getAccountLockCount());
			} else {
				return Message.error("b2b.login.incorrectCredentials");
			}
		}
		Map<String, Object> attributes = new HashMap<String, Object>();
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
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

		String domain = null;// setting.getCookieDomain();
		String host = request.getServerName();
		String[] hosts = host.split("\\.");
		if (hosts.length == 3) {
			domain = "." + hosts[1] + "." + hosts[2];
		}
		Cart cart = cartService.getCurrent();
		WebUtils.addCookie(request, response, Cookies.HOST, setting.getSiteUrl(), -1, "/", domain, null);
		Principal principal = new Principal(member.getId(), member.getUsername());
		request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
		principal.createSign();
		String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), Constant.generateKey);
		WebUtils.addCookie(request, response, Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", domain, null);
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (cart != null) {
			if (cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
		}
		return Message.success("登录成功");
	}

	/**
	 * 通过令牌读取用户
	 */
	@RequestMapping(value = "/user-get", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String getUser(String token, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Principal principal = JsonUtils.toObject(token, Principal.class);
		StringBuilder builder = new StringBuilder();
		builder.append(String.valueOf(principal.getId()));
		builder.append(principal.getUsername());
		builder.append(principal.getTimestamp());
		builder.append("wit@2014-2020$$");
		if (principal.getSign().equals(DigestUtils.md5Hex(builder.toString()))) {
			Member member = memberService.findByUsername(principal.getUsername());
			return GsonUtil.toJson(member, true);
		} else {
			return "error";
		}
	}

	/**
	 * 获取令牌信息
	 */
	@RequestMapping(value = "/token-get", method = RequestMethod.GET)
	public @ResponseBody String getToken(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Principal principal = (Principal) session.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		if (principal != null) {
			principal.createSign();
			return JsonUtils.toJson(principal);
		}
		return "error";
	}

	/**
	 * 检测令牌是否有效
	 */
	@RequestMapping(value = "/token-login", method = RequestMethod.GET)
	public @ResponseBody Message tokenLogin(String token, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Principal principal = JsonUtils.toObject(token, Principal.class);
		StringBuilder builder = new StringBuilder();
		builder.append(String.valueOf(principal.getId()));
		builder.append(principal.getUsername());
		builder.append(principal.getTimestamp());
		builder.append("wit@2014-2020$$");
		if (principal.getSign().equals(DigestUtils.md5Hex(builder.toString()))) {
			Member member = memberService.findByUsername(principal.getUsername());
			Setting setting = SettingUtils.get();
			String domain = null;// setting.getCookieDomain();
			String host = request.getServerName();
			String[] hosts = host.split("\\.");
			if (hosts.length == 3) {
				domain = "." + hosts[1] + "." + hosts[2];
			}
			WebUtils.addCookie(request, response, Cookies.HOST, setting.getSiteUrl(), -1, "/", domain, null);
			Principal newPrincipal = new Principal(member.getId(), member.getUsername());
			request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, newPrincipal);
			newPrincipal.createSign();
			String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(newPrincipal), Constant.generateKey);
			WebUtils.addCookie(request, response, Cookies.UC_TOKEN, uctoken_encrypt, -1, "/", domain, null);
			WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
			return SUCCESS_MESSAGE;
		} else {
			return Message.error("无效令牌");
		}
	}

	/**
	 * 检测令牌是否有效
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public @ResponseBody Message logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		WebUtils.removeCookie(request, response, Member.USERNAME_COOKIE_NAME);
		WebUtils.removeCookie(request, response, Cookies.UC_TOKEN);
		WebUtils.removeCookie(request, response, Cookies.HOST);
		return Message.success("注销成功");
	}
}