package net.wit.controller.wap;

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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.BrowseUtil;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

/**
 * Controller - 账号绑定
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("inviteController")
@RequestMapping("/wap/invite")
public class InviteController extends BaseController {
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

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;


    /**
     * 邀请首页
     *
     * @param extension
     * @param request
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(String extension,  HttpServletRequest request) {
        if (extension != null) {
            request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extension);
        }
        return "/wap/invite/index";
    }

    /**
     * 邀请注册
     *
     * @param model
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap model,  HttpServletRequest request) {
        String extension = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
        Member member = memberService.findByUsername(extension);
        Member member2 = memberService.getCurrent();
        if (member2 != null) {
            return "redirect:/wap/invite/download.jhtml";
        }
        Tenant tenant = null;
        if (member != null) {
            tenant = member.getTenant();
        }
        model.addAttribute("tenant", tenant);
        model.addAttribute("extension", extension);
        return "/wap/invite/register";
    }

    /**
     * 下载
     *
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public String download() {
        return "/wap/invite/download";
    }

    /**
     * 发送手机
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMobile(String mobile, HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
        if (!BrowseUtil.isWeixin(header)) {
        	return Message.error("安全检测不合法");
        }
        if (mobile == null) {
            return Message.error("手机号码不能为空");
        } else {
            if (mobile.length() != 11) {
                return Message.error("手机号码格式不对");
            }
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
            smsSend.setMobiles(mobile);
            smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
            smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
            return Message.success("消息发送成功");
        }
    }

    /**
     * 添加会员
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Message add(String mobile, String captcha, HttpServletRequest request, HttpServletResponse response) {
        String extension = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);

        //检查验证码
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_CONTENT_SESSION);
        if (safeKey == null) {
            return Message.error("请获取验证码");
        }
        if (safeKey.hasExpired()) {
            return Message.error("验证码过期");
        }
        if (!safeKey.getValue().equals(captcha)) {
            return Message.error("验证码不正确");
        }
        /*
         * 注册用户
		 */
        Member member = null;
        //帮用户设置默认密码
        String password = mobile.substring(mobile.length() - 6, mobile.length());
        if (!memberService.usernameExists(mobile) && !memberService.usernameExists(mobile)) {
            Setting setting = SettingUtils.get();
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            if (!setting.getIsRegisterEnabled()) {
                return Message.error("不能注册");
            }
            member = EntitySupport.createInitMember();
            //获取推荐人的地区，设置为被推荐人 的地区
            //Area areaId = current.getArea();
            member.setArea(areaService.getCurrent());

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
            member.setMember(null);
            member.setMobile(mobile);
            member.setEmail("@");
            member.setMobile(mobile);
            member.setBindMobile(BindStatus.binded);

            if (extension != null) {
            	Member current = memberService.findByUsername(extension);
            	if (current!=null) {
                    member.setMember(current);
                
                    member.setShareOwner(current.getTenant());
            	}
            }

            memberService.save(member);

            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(member.getUsername());
            smsSend.setContent("注册成功,账号:" + member.getUsername() + " 默认密码:" + password + "【" + bundle.getString("signature") + "】");
            smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
        } else {
            member = memberService.findByUsername(mobile);
            if (member==null) {
            	member = memberService.findByBindTel(mobile);
            }
        }

		/*
         * 绑定微信
		 */
        String openId = (String) request.getSession().getAttribute(Member.WEIXIN_OPENT_ID);
        if (openId != null) {
            BindUser user = bindUserService.findByUsername(openId, Type._wx);
            BindUser memberUser = bindUserService.findByMember(member, Type._wx);
            if (user!=null && memberUser!=null) {
            	bindUserService.delete(memberUser);
            } else {
            	if (user==null) {
            		user = memberUser;
            	}
            }

            if (user == null) {
                user = new BindUser();
                user.setUsername(openId);
                user.setPassword(password);
                user.setMember(member);
                user.setType(Type._wx);
                bindUserService.save(user);
            } else {
                user.setUsername(openId);
                user.setPassword(password);
                user.setMember(member);
                user.setType(Type._wx);
                bindUserService.update(user);
            }
        }

		/*
         * 用户登录
		 */
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
        return Message.success("注册成功");
    }
}
