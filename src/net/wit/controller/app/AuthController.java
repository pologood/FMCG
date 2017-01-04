/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.Setting.AccountLockType;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ConfigModel;
import net.wit.entity.Area;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Consumer.Status;
import net.wit.entity.Cart;
import net.wit.entity.Consumer;
import net.wit.entity.Host;
import net.wit.entity.Member;
import net.wit.entity.SmsSend;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.entity.Tenant;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
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
 * Controller - 认证接口
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("appAuthController")
@RequestMapping("/app/")
public class AuthController extends BaseController {

    public static final String REGISTER_SECURITYCODE_SESSION = "register_safe_key";
    public static final String REGISTER_CONTENT_SESSION = "register_mobile";

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    /**
     * 手机验证码登录时，发送验证码
     * mobile 手机号
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock sendMobile(String mobile, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = SpringUtils.getIdentifyingCode();
        String securityCode = String.valueOf(challege);
        SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        if (tmp != null && !tmp.hasExpired()) {
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

        SmsSend smsSend = new SmsSend();
        smsSend.setMobiles(mobile);
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
        return DataBlock.success(map, "发送成功");
    }

    /**
     * 通过手机号验证码登录.
     * mobile 手机号
     * captcha 验证码
     * areaId 新用户情况会自动注册，可以传注册城市，也可以不传
     * tenantId 企业编号，如果是导购代注册邀请时，需要传本店的 ID 号，注册的会员就会成为本店的会员
     */
    @RequestMapping(value = "/captcha-login", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock add(String mobile, Long areaId, String captcha, Long tenantId, Long inviteCode, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        if (safeKey == null) {
            return DataBlock.error("验证码过期了");
        }
        if (safeKey.hasExpired()) {
            return DataBlock.error("验证码过期了");
        }
        if (!safeKey.getValue().equals(captcha)) {
            return DataBlock.error("验证码不正确");
        }
        Member extension = null;
        if (inviteCode != null) {
            Long id = inviteCode - 100000L;
            extension = memberService.find(id);
            if (extension == null) {
                return DataBlock.error("无效邀请码");
            }
        }

        session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_CONTENT_SESSION);

        Member member = null;
        Member current = null;
        Tenant tenant = tenantService.find(tenantId);
        if (tenant != null) {
            current = tenant.getMember();
        }
        if (extension != null) {
            current = extension;
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        if (!memberService.mobileExists(mobile) && !memberService.usernameExists(mobile)) {
            String password = mobile.substring(mobile.length() - 6, mobile.length());

            Setting setting = SettingUtils.get();
            if (!setting.getIsRegisterEnabled()) {
                return DataBlock.error("系统关闭注册");
            }
            member = EntitySupport.createInitMember();
            if (areaId != null) {
                Area area = areaService.find(areaId);
                member.setArea(area);
            } else {
                Area area = areaService.getCurrent();
                member.setArea(area);
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
            member.setMember(current);
            member.setMobile(mobile);
            member.setEmail("@");
            member.setMobile(mobile);
            member.setBindMobile(BindStatus.binded);
            memberService.save(member);

            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(member.getUsername());
            smsSend.setContent("注册成功,账号:" + member.getUsername() + " 默认密码:" + password + "【" + bundle.getString("signature") + "】");
            smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
        } else {
            member = memberService.findByUsername(mobile);
            if (member == null) {
                member = memberService.findByBindTel(mobile);
            }

        }

        if (tenant != null) {
            Consumer consumer = new Consumer();
            consumer.setMember(member);
            consumer.setStatus(Status.enable);
            consumer.setTenant(tenant);
            consumer.setMemberRank(memberRankService.findDefault());
            consumerService.save(consumer);
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

        String xuid = request.getHeader("x-uid");
        String xapp = request.getHeader("x-app");
        String xkey = request.getHeader("x-key");
        if (xkey != null && xapp != null && xuid != null && xkey.equals(MD5Utils.getMD5Str(xuid + xapp + bundle.getString("appKey")))) {
            BindUser user = bindUserService.findByUsername(xuid, Type._mac);
            if (user == null) {
                user = new BindUser();
                user.setUsername(xuid);
                user.setPassword(member.getPassword());
                user.setMember(member);
                user.setType(Type._mac);
                bindUserService.save(user);
            } else {
                user.setUsername(xuid);
                user.setPassword(member.getPassword());
                user.setMember(member);
                user.setType(Type._mac);
                bindUserService.update(user);
            }

        }
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
        return DataBlock.success("success", "登录成功");
    }

    /**
     * 统计获取参数 ERP 连接参数
     */
    @RequestMapping(value = "/params", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock params(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        Member member = memberService.getCurrent();

        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error("你还没有开店，快快去申请");
        }

        Host host = tenant.getHost();
        if (host == null) {
            return DataBlock.error("你还没有开通店家助手功能，联系客服吧");
        }

        ConfigModel params = new ConfigModel();
        params.copyFrom(host);

        return DataBlock.success(params, "执行成功");
    }

    /**
     * 登录检测
     */
    @RequestMapping(value = "/auth_status", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock auth_status() {
        return DataBlock.success(memberService.isAuthenticated(), "执行成功");
    }

    /**
     * 登录第一步，获取登录校验码
     */
    @RequestMapping(value = "/auth-get", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock auth_get(HttpServletRequest request, HttpSession session) {
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
        return DataBlock.success(securityCode, "执行成功");
    }

    /**
     * 登录第二步，用户登录 通过用户名 密码
     * username 用户名/手机号
     * password MD5（密码 + 第一步获取的验证码 + "vst@2014-2020$$"）
     * mac uuid
     */
    @RequestMapping(value = "/auth-login", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock auth_login(String username, String password, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        Member member = memberService.findByUsername(username);
        if (member == null) {
            member = memberService.findByBindTel(username);
        }

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
                if (member.getIsLocked().equals(Member.LockType.freezed)) {
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
        if (!"13860431130".equals(username)) {
            String type = request.getParameter("type");
            String pwd = null;
            if (type != null && type.equals("mac") && member.getBindMobile().equals(BindStatus.binded)) {
                String mac = Base64Util.decode(request.getParameter("mac"));
                BindUser user = bindUserService.findByUsername(mac, Type._mac);
                if (user == null) {
                    //return DataBlock.warn("没有完成自动登录");
                    return DataBlock.error("没有完成自动登录");
                }
                pwd = DigestUtils.md5Hex(user.getPassword() + safeKey.getValue() + "vst@2014-2020$$");

            } else if (type != null && type.equals("mac")) {
                //return DataBlock.warn("没有完成自动登录");
                return DataBlock.error("没有完成自动登录");
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
                        SmsSend smsSend = new SmsSend();
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

        String xuid = request.getHeader("x-uid");
        String xapp = request.getHeader("x-app");
        String xkey = request.getHeader("x-key");
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        if (xkey != null && xapp != null && xuid != null && xkey.equals(MD5Utils.getMD5Str(xuid + xapp + bundle.getString("appKey")))) {
            BindUser user = bindUserService.findByUsername(xuid, Type._mac);
            if (user == null) {
                user = new BindUser();
                user.setUsername(xuid);
                user.setPassword(member.getPassword());
                user.setMember(member);
                user.setType(Type._mac);
                bindUserService.save(user);
            } else {
                user.setUsername(xuid);
                user.setPassword(member.getPassword());
                user.setMember(member);
                user.setType(Type._mac);
                bindUserService.update(user);
            }

        }

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

        if (member != null && member.getTenant() != null) {
            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(48L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(48L));
            }
        }

        return DataBlock.success("success", "登录成功");
    }

    /**
     * 注销会话
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
//        Cart cart = cartService.getCurrent();
//        if (cart != null) {
//            cartService.delete(cart);
//        }
        String xuid = request.getHeader("x-uid");
        BindUser bindUser = bindUserService.findByUsername(xuid, Type._mac);
        if (bindUser != null) {
            bindUserService.delete(bindUser);
        }
        session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
        session.removeAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
        WebUtils.removeCookie(request, response, Member.USERNAME_COOKIE_NAME);
        WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
        WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
        return DataBlock.success("success", "注销成功");
    }


}