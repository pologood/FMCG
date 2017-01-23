package net.wit.controller.weixin;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.SmsSend;
import net.wit.entity.Tenant;
import net.wit.service.CartService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.util.SettingUtils;
import net.wit.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.*;

/**
 * 首页
 * Created by WangChao on 2016/11/28.
 */
@Controller("weixinIndexController")
@RequestMapping("/weixin/index")
public class IndexController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;
    /**
     * 首页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(String extension,HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/index";
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String redirectUrl, String extension, HttpServletRequest request, HttpServletResponse response) {
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        Member member = memberService.getCurrent();
        WebUtils.addCookie(request, response, "session_member_username", member == null ? "" : member.getUsername());
        return "redirect:" + URLDecoder.decode(redirectUrl).replaceAll("extension=", "extension_back=");
    }

    /**
     * 检测登录
     */
    @RequestMapping(value = "/check_login", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock checkLogin() {
        return DataBlock.success(memberService.getCurrent() != null, "执行成功");
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
            if(member.getTenant().getTenantType()== Tenant.TenantType.retailer){
                if(member.getTenant().getStatus()== Tenant.Status.none){
                    return Message.warn("您的店铺正在审核中...");
                }else if(member.getTenant().getStatus()== Tenant.Status.fail){
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
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }

        session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), username));
        WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
        member.setLoginCount(member.getLoginCount()!=null?member.getLoginCount()+1:0);
        memberService.update(member);
        return Message.success("登录成功");
    }

}
