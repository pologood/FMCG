package net.wit.controller.b2c;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.constant.Constant;
import net.wit.entity.*;
import net.wit.entity.Tenant.Status;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.uic.api.UICService;
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
import java.util.*;
import java.util.Map.Entry;

/**
 * 供应商后台
 * Created by WangChao on 2016-4-13.
 */
@Controller("b2cSupplierController")
@RequestMapping("b2c/supplier")
public class SupplierController extends BaseController {

    public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
    public static final String REGISTER_CONTENT_SESSION = "register_content_session";

    @Resource(name = "uicService")
    private UICService uicService;
    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;
    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;
    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;
    @Resource(name = "cartServiceImpl")
    private CartService cartService;
    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;
    @Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;
    @Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;
    /**
     * 登录页
     * @param redirectUrl 重定向Url
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(String redirectUrl, HttpServletRequest request, ModelMap model){
        request.getSession().invalidate();
        Setting setting = SettingUtils.get();
        if (redirectUrl != null && !redirectUrl.equalsIgnoreCase(setting.getSiteUrl()) && !redirectUrl.startsWith(request.getContextPath() + "/") && !redirectUrl.startsWith(setting.getSiteUrl() + "/")) {
            redirectUrl = null;
        }
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        return "b2c/member/supplier/login";
    }
    
    /**
	 * 注销
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		WebUtils.removeCookie(request, response, Member.USERNAME_COOKIE_NAME);
		WebUtils.removeCookie(request, response, Constant.Cookies.UC_TOKEN);
		return "redirect:/b2c/supplier/login.jhtml";
	}
	
    /**
     * 登录提交
     * @param username 用户名
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login/submit",method = RequestMethod.POST)
    public @ResponseBody Message loginSubmit(String username, HttpServletRequest request, HttpServletResponse response,HttpSession session){
        String password = rsaService.decryptParameter("enPassword", request);
//        Message msg=uicService.loginTo(username,password,request,response);
//        if(msg.getType()== Message.Type.success){
//            Member member=memberService.getCurrent();
//            if(member==null){
//                return Message.error("会员不存在");
//            }
//            if(member.getTenant()==null){
//                return Message.error("未开通店铺");
//            }
//            if(member.getTenant().getTenantType()!= Tenant.TenantType.suppier){
//                return Message.error("您不是供应商，请联系管理员");
//            }
//        }
//        return msg;
        rsaService.removePrivateKey(request);
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return Message.error("shop.common.invalid");
		}

		Member member = memberService.findByUsername(username);
        if (member==null) {
            member = memberService.findByBindTel(username);
        }
		if (member == null) {
			return Message.error("shop.login.unknownAccount");
		}

		if (!member.getIsEnabled()) {
			return Message.error("shop.login.disabledAccount");
		}
		ResourceBundle wit = PropertyResourceBundle.getBundle("wit");
//        if(wit.getString("system.version.type").equals("1")){
        	if (member.getTenant()!=null) {
        		if(member.getTenant().getTenantType()==TenantType.suppier){
        			if(member.getTenant().getStatus()==Status.none){
	    				return Message.warn("您的店铺正在审核中..."); 
	    			}
	    			if(member.getTenant().getStatus()==Status.fail){
	    				return Message.warn("您的店铺已被关闭·"); 
	    			}
        		}else{
        			return Message.warn("您不是供货商，无法登陆！");
        		}
    		}else{
    			return Message.warn("您还没有自己的店铺，请先申请");
    		}
//        }
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
		return Message.success("success");
    }

    /**
     * 会员注册
     * @param mobile 手机号
     * @param authcode 验证码
     * @param request
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody Message register(String mobile, String authcode, HttpServletRequest request, HttpServletResponse response){
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
        if (!safeKey.getValue().equals(authcode)) {
            return Message.error("验证码不正确");
        }
        Member member=memberService.findByTel(mobile);
        if(member!=null){
            return Message.error("手机号已注册");
        }
        //注册会员
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        if (!setting.getIsRegisterEnabled()) {
            return Message.error("系统关闭注册");
        }
        try {
            String password = mobile.substring(mobile.length()-6, mobile.length());
            member = EntitySupport.createInitMember();
            member.setArea(areaService.getCurrent());
            member.setUsername(mobile);
            member.setPassword(DigestUtils.md5Hex(password));
            member.setPoint(setting.getRegisterPoint());
            member.setLockedDate(null);
            member.setRegisterIp(request.getRemoteAddr());
            member.setLoginIp(request.getRemoteAddr());
            member.setLoginDate(new Date());
            member.setSafeKey(null);
            member.setPaymentPassword(DigestUtils.md5Hex(password));
            member.setMemberRank(memberRankService.findDefault());
            member.setFavoriteProducts(null);
            member.setMember(null);
            member.setEmail("@");
            member.setMobile(mobile);
            member.setBindMobile(Member.BindStatus.binded);
            memberService.save(member);
            SmsSend smsSend=new SmsSend();
            smsSend.setMobiles(member.getUsername());
            smsSend.setContent("注册成功,账号:" + member.getUsername() +" 默认密码:"+password+ "【" + bundle.getString("signature") + "】");
            smsSend.setType(SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);


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

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("未知错误");
        }
        return Message.success("注册成功");
    }

    /**
     * 注册时发送手机验证码
     * @param mobile 手机号
     * @param request
     * @return
     */
    @RequestMapping(value = "/send_security_code",method = RequestMethod.POST)
    public @ResponseBody Message send_security_code(String captchaId,String captcha,String mobile,HttpServletRequest request){
        if (StringUtils.isEmpty(mobile)) {
            return Message.error("手机号为空");
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
     * 检查验证码、手机号是否正确
     * @param mobile 手机号
     * @param securityCode 验证码
     * @param request
     * @return.
     */
    @RequestMapping(value = "/check_security_code",method = RequestMethod.POST)
    public @ResponseBody Message check_security_code(String mobile, String securityCode,HttpServletRequest request){
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
        session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
        Member member=memberService.findByTel(mobile);
        if(member==null){
            return Message.error("手机号未注册");
        }
        return Message.success("验证通过");
    }

    /**
     * 重置密码
     * @param mobile 手机号
     * @param request
     * @return
     */
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    public @ResponseBody Message resetPassword(String mobile,HttpServletRequest request){
        try {
            String password = rsaService.decryptParameter("enPassword", request);
            Member member = memberService.findByTel(mobile);
            if (member != null) {
                member.setSafeKey(null);
                member.setPassword(DigestUtils.md5Hex(password));
                member.setLoginFailureCount(0);
                member.setIsLocked(Member.LockType.none);
                member.setLockedDate(null);
                memberService.update(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("未知错误");
        }
        return Message.success("密码重置成功");
    }

}
