/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.Tenant.Status;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * Controller - 会员登录
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeLoginController")
@RequestMapping("/store/login")
public class LoginController extends BaseController {

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";

	/**
	 * 登录检测
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public @ResponseBody
	Boolean check() {
		return memberService.isAuthenticated();
	}

	/**
	 * 登录页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(String redirectUrl, HttpServletRequest request, ModelMap model) {
		Tenant tenant = tenantService.getCurrent();
		if (tenant!=null) {
			return "redirect:"+tenant.getId()+"/login.jhtml?redirectUrl="+redirectUrl;
		}
		Setting setting = SettingUtils.get();
		if (redirectUrl != null && !redirectUrl.equalsIgnoreCase(setting.getSiteUrl()) && !redirectUrl.startsWith(request.getContextPath() + "/") && !redirectUrl.startsWith(setting.getSiteUrl() + "/")) {
			redirectUrl = null;
		}
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("captchaId", UUID.randomUUID().toString());//用于找回密码/重置密码
		model.addAttribute("captchaId2", UUID.randomUUID().toString());//用于注册
		model.addAttribute("area",areaService.getCurrent());
		return "/store/login/index";
	}

	@RequestMapping(value = "/getUUID", method = RequestMethod.GET)
	public @ResponseBody
	Message getUUID(HttpServletRequest request) {
		return Message.success(UUID.randomUUID().toString());
	}

	/**
	 * 帐号登录提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody
	Message submit(String username, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String password = rsaService.decryptParameter("enPassword", request);
		rsaService.removePrivateKey(request);

		Member member = memberService.findByUsername(username);
		if (member==null) {
			member = memberService.findByBindTel(username);
		}
		if (member == null) {
			return Message.error("该会员不存在，可以【立即注册】");
		}

		if (!member.getIsEnabled()) {
			return Message.error("该会员不可用");
		}

        if (member.getTenant()!=null) {
            if(member.getTenant().getTenantType()==TenantType.tenant){
                if(member.getTenant().getStatus()==Status.none){
                    return Message.warn("您的店铺正在审核中...");
                }else if(member.getTenant().getStatus()==Status.fail){
                    return Message.warn("您的店铺已被关闭·");
                }
            }else{
                return Message.warn("您不是经销商，无法登陆！");
            }
        }else{
            return Message.warn("您还没有自己的店铺，请先申请");
        }

		Setting setting = SettingUtils.get();
		if (!member.getIsLocked().equals(Member.LockType.none)) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				if (member.getIsLocked().equals(Member.LockType.freezed)) {
					return Message.error("你的账户被封禁15天。");
				}
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					return Message.error("用户已锁定，请稍候再重试");
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(Member.LockType.none);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					return Message.error("用户已锁定，请稍候再重试");
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(Member.LockType.none);
				member.setLockedDate(null);
				member.setLoginIp(request.getRemoteAddr());
				member.setLoginDate(new Date());
				member.setLoginFailureCount(0);
				memberService.update(member);
			}

		}

		if (!member.getPassword().equals(DigestUtils.md5Hex(password))) {
			int loginFailureCount = member.getLoginFailureCount() + 1;
			if (loginFailureCount >= setting.getAccountLockCount()) {
				member.setIsLocked(Member.LockType.locked);
				member.setLockedDate(new Date());
				if (member.getMobile() != null) {
					ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
					SmsSend smsSend = new SmsSend();
					smsSend.setMobiles(member.getMobile());
					smsSend.setContent("密码输入错误，累计超过" + member.getLoginFailureCount().toString() + "次，为保证您的账户安全，您的账号已经被锁定。【" + bundle.getString("signature") + "】");
					smsSend.setType(SmsSend.Type.captcha);
					smsSendService.smsSend(smsSend);
				}
				return Message.error("登录密码无效");
			}

			member.setLoginFailureCount(loginFailureCount);
			memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				return Message.error("shop.login.accountLockCount", setting.getAccountLockCount());
			} else {
				return Message.error("shop.login.incorrectCredentials");
			}
		}

		Cart cart = cartService.getCurrent();

		if (cart != null) {
			if (cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
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

		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), username));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		//TODO 登录店铺领取积分，每日一次
		activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(48L));
		member.setLoginCount(member.getLoginCount()!=null?member.getLoginCount()+1:0);
		memberService.update(member);
		return Message.success("success");
	}
	/**
	 * 手机验证码登录时，发送验证码
	 * mobile 手机号
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public Message sendMobile(String mobile, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (tmp != null && !tmp.hasExpired()) {
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
		smsSend.setMobiles(mobile);System.out.println(securityCode);
		smsSend.setContent("验证码 :" + securityCode + ",只用于登录使用。【" + bundle.getString("signature") + "】");
		smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		boolean isNewMember = false;
		Member member = memberService.findByUsername(mobile);
		if (member == null) {
			member = memberService.findByTel(mobile);
		}
		if (member == null) {
			isNewMember = true;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("isNewMember", isNewMember);
		return Message.success("发送成功");
	}

	/**
	 * 通过手机号验证码登录.
     * 店家助手登录条件，会员存在，并且有自己的tenant店铺（已开通或者已审核）
	 */
	@RequestMapping(value = "/phone_login_submit", method = RequestMethod.POST)
	@ResponseBody
	public Message phoneLogin(String mobile, Long areaId, String captcha, Long tenantId, Long inviteCode, HttpServletRequest request, HttpServletResponse response) {
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
		Member member = memberService.findByUsername(mobile);
		if (member==null) {
			member = memberService.findByBindTel(mobile);
		}
		if (member == null) {
			return Message.error("该会员不存在，可以【立即注册】");
		}
		if (!member.getIsEnabled()) {
			return Message.error("无效账户");
		}

        if (member.getTenant()!=null) {
            if(member.getTenant().getTenantType()==TenantType.tenant){
                if(member.getTenant().getStatus()==Status.none){
                    return Message.warn("您的店铺正在审核中...");
                }else if(member.getTenant().getStatus()==Status.fail){
                    return Message.warn("您的店铺已被关闭·");
                }
            }else{
                return Message.warn("您不是经销商，无法登陆！");
            }
        }else{
            return Message.warn("您还没有自己的店铺，请先注册");
        }

		Setting setting = SettingUtils.get();
		if (!member.getIsLocked().equals(Member.LockType.none)) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				if (member.getIsLocked().equals(Member.LockType.freezed)) {
					return Message.error("你的账户被封禁15天。");
				}
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					return Message.error("用户已锁定，请稍候再重试");
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(Member.LockType.none);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					return Message.error("用户已锁定，请稍候再重试");
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(Member.LockType.none);
				member.setLockedDate(null);
				member.setLoginIp(request.getRemoteAddr());
				member.setLoginDate(new Date());
				member.setLoginFailureCount(0);
				memberService.update(member);
			}
		}


		Cart cart = cartService.getCurrent();
		if (cart != null) {
			if (cart.getMember() == null) {
				cartService.merge(member, cart);
				WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
				WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
			}
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
		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), mobile));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());

		return Message.success("登录成功");
	}

	/**
	 * 根据图片验证码获取到手机验证码
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	@ResponseBody
	public Message getCheckCode(String username,String captchaId,String captcha, HttpServletRequest request) {

		if (!captchaService.isValid(Setting.CaptchaType.memberRegister, captchaId, captcha)) {
			return Message.error("图片验证码不正确");
		}
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (tmp != null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(REGISTER_CONTENT_SESSION, username);
		System.out.println(securityCode);
		SmsSend smsSend = new SmsSend();
		smsSend.setMobiles(username);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return Message.success("消息发送成功");
	}

    /**
     * 验证用户手机接收的验证码
     */
    @RequestMapping(value = "/check_captcha", method = RequestMethod.POST)
    @ResponseBody
    public Message checkCode(String username, String securityCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        if (safeKey == null) {
            return Message.error("验证码过期了");
        }
        if (safeKey.hasExpired()) {
            return Message.error("验证码过期了");
        }
        if (!safeKey.getValue().equals(securityCode)) {
            return Message.error("验证码不正确");
        }
        if (!memberService.mobileExists(username)) {
            return Message.error("当前用户不存在，清先绑定手机号");
        }
        return Message.success("验证成功！！！");
    }

	/**
	 * 重置密码
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	@ResponseBody
	public Message resetSave(String mobile, String securityCode, HttpServletRequest request) {
		Member member = memberService.findByUsername(mobile);
		if (member==null) {
			member = memberService.findByBindTel(mobile);
		}
		if (member == null) {
			return Message.error("会员不存在");
		}
		if (!member.getIsEnabled()) {
			return Message.error("无效账户，重置密码失败！");
		}
		String newpassword = rsaService.decryptParameter("newpassword", request);
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		String mo = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(securityCode)) {
			return Message.error("验证码不正确");
		}
		if (!mo.equals(mobile)) {
			return Message.error("手机号码不匹配");
		}

		member.setSafeKey(null);
		member.setPassword(DigestUtils.md5Hex(newpassword));
		member.setLoginFailureCount(0);
		member.setIsLocked(Member.LockType.none);
		member.setLockedDate(null);
		memberService.update(member);

		return Message.success("密码重置成功");
	}

	/**
	 * 检查用户类型
	 */
	@RequestMapping(value = "/check_usertype", method = RequestMethod.GET)
	public @ResponseBody Member.RegType checkUsertype(String username) {
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		if (memberService.usernameExists(username)) {

			if (memberService.findByTel(username)!=null) {
				return Member.RegType.mobile;
			}
			if (memberService.findByEmail(username)!=null) {
				return Member.RegType.email;
			}
			return Member.RegType.account;
		}
		return null;
	}
}