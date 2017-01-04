/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store.member;

import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Credit.Method;
import net.wit.entity.Idcard;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.SmsSend.Type;
import net.wit.service.*;
import net.wit.uic.api.UICService;
import net.wit.util.IdcardValidator;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Controller - 会员中心 - 密码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeMemberSafeController")
@RequestMapping("/store/member/safe")
public class SafeController extends net.wit.controller.store.BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;
	@Resource(name = "uicService")
	private UICService uicService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
	public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";

	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";
	
	/**
	 * 个人中心主页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/store/member/safe/index";
	}

	/**
	 * 根据图片验证码获取到手机验证码
	 */
	@RequestMapping(value = "/send_phone", method = RequestMethod.POST)
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
     * 根据手机号发送验证码
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public Message sendSecurityCode(String username, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            Message.error(DataBlock.SESSION_INVAILD);
        }

        HttpSession session = request.getSession();
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = SpringUtils.getIdentifyingCode();
        String securityCode = String.valueOf(challege);
        SafeKey tmp = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
        if (tmp!=null && !tmp.hasExpired()) {
            securityCode = tmp.getValue();
            if (!tmp.canReset()) {
                return Message.error("系统忙，稍等几秒重试");
            }
        }
        SafeKey safeKey = new SafeKey();
        safeKey.setValue(securityCode);
        safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
        session.setAttribute(CAPTCHA_SECURITYCODE_SESSION, safeKey);
        session.setAttribute(CAPTCHA_CONTENT_SESSION, username);
        System.out.println(securityCode);

        SmsSend smsSend=new SmsSend();
        smsSend.setMobiles(username);
        smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
        smsSend.setType(Type.captcha);
        smsSendService.smsSend(smsSend);
        return Message.success("发送成功");
    }

    /**
     * 检查验证码
     * @param username
     * @param securityCode
     * @param request
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
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
        if (memberService.getCurrent()==null) {
            return Message.error("当前用户不存在");
        }
        return Message.success("验证成功！！！");
    }

    /**
     * 修改登录页面
     */
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String password( HttpServletRequest request,ModelMap model) {
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        return "store/member/safe/password";
    }

	/**
	 * 确认重置登录密码
	 */
	@RequestMapping(value = "/reset_login_password", method = RequestMethod.POST)
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
		Member member=memberService.getCurrent();
		if(member!=null){
			member.setSafeKey(null);
			member.setPassword(DigestUtils.md5Hex(newpassword));
			member.setLoginFailureCount(0);
			member.setIsLocked(Member.LockType.none);
			member.setLockedDate(null);
			memberService.update(member);
		}else{
			Message.error("找不到该用户");
		}
		return Message.success("密码重置成功");
	}

    /**
     * 修改支付密码页面
     */
    @RequestMapping(value = "/paymentPassword", method = RequestMethod.GET)
    public String passwordPayment( HttpServletRequest request,ModelMap model) {
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        return "store/member/safe/paymentPassword";
    }

    /**
     * 确认重置支付密码
     */
    @RequestMapping(value = "/reset_payment_password", method = RequestMethod.POST)
    @ResponseBody
    public Message resetPaymentPassword(String mobile, String securityCode, HttpServletRequest request) {
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
        Member member = memberService.getCurrent();
        if (member != null) {
            member.setSafeKey(null);
            member.setPaymentPassword(DigestUtils.md5Hex(newpassword));
            memberService.update(member);
        }else{
            return Message.error("找不到该用户");
        }
        return Message.success("密码重置成功");
    }

	/**
	 * 绑定手机
	 */
	@RequestMapping(value = "/bindmobile", method = RequestMethod.GET)
	public String bindMobile(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "store/member/safe/bindmobile";
	}

	/**
	 * 提交手机绑定
	 */
	@RequestMapping(value = "/bindmobile", method = RequestMethod.POST)
	@ResponseBody
	public Message mobileupdate(String captcha, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error("用户不存在");
		}

		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		String content = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
		session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_CONTENT_SESSION);
		
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return Message.error("验证码不正确");
		}

		if (member.getBindMobile().equals(BindStatus.binded)) {
			if (!member.getMobile().equals(content)) {
				return Message.error("无效手机号");
			}
			member.setBindMobile(BindStatus.unbind);
		} else {
			if(memberService.mobileExists(content)&&!member.getMobile().equals(content)){
				return Message.error("该手机号已被使用");
			}
			if(member.getMobile()==member.getUsername()){
				member.setUsername(content);
			}
			member.setMobile(content);
			member.setBindMobile(BindStatus.binded);
		}

		memberService.save(member);
		return Message.success("操作成功");
	}

	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/idcard", method = RequestMethod.GET)
	public String idcard(ModelMap model) {
		Member member = memberService.getCurrent();
		Idcard idcard = member.getIdcard();
		if (idcard == null) {
			idcard = new Idcard();
		}
		model.addAttribute("member", member);
		model.addAttribute("idcard", idcard);
		return "store/member/safe/idcard";
	}

	
	/**
	 * 验证身份证号码是否合法
	 * @param method
	 * @param no  银行卡号码
	 * @return
	 */
	@RequestMapping(value = "/IdcardCheck", method = RequestMethod.POST)
	public @ResponseBody boolean IdcardCheck(Method method, String no) {
		no = no.replaceAll(" ", "");
		IdcardValidator idCheck =new IdcardValidator();
		boolean isIdcard = idCheck.isValidatedAllIdcard(no);
		return isIdcard;
	}
	
	/**
	 * 实名认证的提交
	 */
	@RequestMapping(value = "/idcard", method = RequestMethod.POST)
	public String idcardupdate(String no,String name,String address, Date beginDate, Date endDate, String pathBack, String pathFront, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Idcard entity = member.getIdcard();
		if (entity == null) {
			entity = new Idcard();
		}
		entity.setNo(no);
		entity.setAddress(address);
		entity.setBeginDate(beginDate);
		entity.setEndDate(endDate);
		entity.setAuthStatus(AuthStatus.wait);

		entity.setPathBack(pathBack);
		entity.setPathFront(pathFront);
        entity.setName(name);
		idcardService.update(entity, member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:../profile/edit.jhtml";
	}

	
}