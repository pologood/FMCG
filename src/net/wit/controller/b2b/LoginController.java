/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.entity.*;
import net.wit.entity.Tenant.Status;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
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

/**
 * Controller - 会员登录
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bLoginController")
@RequestMapping("/b2b/login")
public class LoginController extends BaseController {

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
    public static final String REGISTER_CONTENT_SESSION = "register_mobile";

    /**
     * 登录检测
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean check() {
        return memberService.isAuthenticated();
    }

    /**
     * 登录页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(String redirectUrl,String type, HttpServletRequest request, ModelMap model) {
//		Member member = memberService.getCurrent();
//		if (member != null) {
//			return "redirect:/b2b/index.jhtml";
//		}
        request.getSession().invalidate();
        Setting setting = SettingUtils.get();
        if (redirectUrl != null && !redirectUrl.equalsIgnoreCase(setting.getSiteUrl()) && !redirectUrl.startsWith(request.getContextPath() + "/") && !redirectUrl.startsWith(setting.getSiteUrl() + "/")) {
            redirectUrl=null;
        }
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("type", type);
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        model.addAttribute("area", areaService.getCurrent());
        return "/b2b/login/index";
    }

    @RequestMapping(value = "/getUUID", method = RequestMethod.GET)
    public
    @ResponseBody
    Message getUUID(HttpServletRequest request) {
        return Message.success(UUID.randomUUID().toString());
    }

    /**
     * 登录提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public
    @ResponseBody
    Message submit(String username, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//		if (!captchaService.isValid(CaptchaType.memberLogin, captchaId, captcha)) {
//			return Message.error("shop.captcha.invalid");
//		}
        String password = rsaService.decryptParameter("enPassword", request);
        rsaService.removePrivateKey(request);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Message.error("shop.common.invalid");
        }
        Cart cart = cartService.getCurrent();
        Member member = memberService.findByUsername(username);
        if (member==null) {
            member = memberService.findByBindTel(username);
        }
        if (member == null) {
            return Message.error("helper.login.unknownAccount");
        }

        if (!member.getIsEnabled()) {
            return Message.error("helper.login.disabledAccount");
        }

        if (member.getTenant()!=null) {
            if(member.getTenant().getTenantType()==TenantType.retailer){
                if(member.getTenant().getStatus()==Status.none){
                    return Message.warn("您的店铺正在审核中...");
                }else if(member.getTenant().getStatus()==Status.fail){
                    return Message.warn("您的店铺已被关闭·");
                }
            }else{
                return Message.warn("您不是零售商，无法登陆！");
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
                    smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
                    smsSendService.smsSend(smsSend);
                }
                return Message.error("登录密码无效");
            }

            member.setLoginFailureCount(loginFailureCount);
            memberService.update(member);
            if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
                return Message.error("b2b.login.accountLockCount", setting.getAccountLockCount());
            } else {
                return Message.error("b2b.login.incorrectCredentials");
            }
        }

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
        member.setLoginCount(member.getLoginCount()!=null?member.getLoginCount()+1:0);
        memberService.update(member);
        return Message.success("登录成功");
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/getCheckCode", method = RequestMethod.POST)
    @ResponseBody
    public Message getCheckCode(String captchaId,String captcha,String mobile, HttpServletRequest request) {
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
        return Message.success("消息发送成功");
    }

    /**
     * 忘记密码时验证用户信息
     */
    @RequestMapping(value = "/check_mobile", method = RequestMethod.POST)
    public
    @ResponseBody
    Message checkMobile(String mobile, String securityCode, HttpServletRequest request) {
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
        if (!memberService.mobileExists(mobile)) {
            return Message.error("当前用户不存在，您可以【立即注册】");
        }
        return Message.success("验证成功！！！");
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Message resetSave(String mobile, String securityCode, HttpServletRequest request) {

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
        Member member = memberService.findByBindTel(mobile);
        if (member != null) {
            member.setSafeKey(null);
            member.setPassword(DigestUtils.md5Hex(newpassword));
            member.setLoginFailureCount(0);
            member.setIsLocked(Member.LockType.none);
            member.setLockedDate(null);
            memberService.update(member);
        }
        return Message.success("密码重置成功");
    }

    /**
     * 注册提交(手机/邮箱)
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    Message register(String mobile, String securityCode,
                     HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
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

        Member member = null;
        Member current = memberService.getCurrent();

        if (!memberService.usernameExists(mobile)) {
            String password = mobile.substring(mobile.length() - 6, mobile.length());

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
            member.setBindMobile(Member.BindStatus.binded);

            String extension = (String) session.getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
            if (extension != null) {
                member.setMember(memberService.findByUsername(extension));
            }

            memberService.save(member);

            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(member.getUsername());
            smsSend.setContent("注册成功,账号:" + member.getUsername() + " 默认密码:" + password + "【" + bundle.getString("signature") + "】");
            smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
        } else {
            member = memberService.findByUsername(mobile);
        }

        Map<String, Object> attributes = new HashMap<String, Object>();
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
        member.setLoginCount(member.getLoginCount()!=null?member.getLoginCount()+1:0);
        memberService.update(member);
        return Message.success("登录成功！！！");

    }
}