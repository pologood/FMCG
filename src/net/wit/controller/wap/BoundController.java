package net.wit.controller.wap;

import java.math.BigDecimal;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.entity.*;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member.BindStatus;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.uic.api.UICService;
import net.wit.util.BrowseUtil;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 账号绑定
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapBoundController")
@RequestMapping("/wap/bound")
public class BoundController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "uicService")
    private UICService uicService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
    public static final String REGISTER_CONTENT_SESSION = "register_mobile";

    /**
     * 判断微信是否绑定并跳转绑定页
     */
    @RequestMapping(value = "/indexNew", method = RequestMethod.GET)
    public String indexNew(String redirectUrl, HttpServletRequest request, ModelMap model) {
    	Member member = memberService.getCurrent();
    	if (member!=null && member.getBindMobile().equals(BindStatus.binded)) {
			return "redirect:/wap/member/index.jhtml";
    	}
        RSAPublicKey publicKey = rsaService.generateKey(request);
        model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
        model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        return "/wap/bound/indexNew";
    }

    /**
     * 检查mobile是否存在
     */
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
     * 获取验证码
     */
    @RequestMapping(value = "/getCheckCode", method = RequestMethod.POST)
    @ResponseBody
    public Message getCheckCode(String captchaId,String captcha,String mobile, HttpServletRequest request) {
        if (StringUtils.isEmpty(mobile)) {
            return Message.error("手机为空");
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
     * 注册提交(手机/邮箱)
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public
    @ResponseBody
    Message submit(String mobile, String securityCode,String inviteCode,
                   HttpServletRequest request, HttpServletResponse response) {
    	
        String extension = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);

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


        Member current = null;
        if(inviteCode!=null&&!"".equals(inviteCode)){

            if(inviteCode.length()!=6){
                return Message.error("邀请码不正确");
            }

            Long _inviteCode = Long.valueOf(inviteCode)-100000;
            current = memberService.find(_inviteCode);

            if(current==null){
                return Message.error("您输入的邀请码不是导购或店主邀请码");
            }
        } else {
            current = memberService.findByUsername(extension);
        }

        session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_CONTENT_SESSION);

        Member member = memberService.getCurrent();
        if (member==null) {
        	return Message.error("当前会话无效");
        }
        if (member.getBindMobile().equals(BindStatus.binded)) {
        	return Message.error("已经绑定，不能重复操作");
        }

        Member mobileMember = memberService.findByBindTel(mobile);

        if (mobileMember!=null) {
            BindUser bdUser =  bindUserService.findByMember(mobileMember,Type._wx);
            if (bdUser!=null) {
                return Message.error("当前手机已经被占用，不能重复绑定");
            }
            if (member.getBalance().compareTo(BigDecimal.ZERO)!=0) {
                return Message.error("当前手机已经被占用，不能重复绑定");
            }
            String openId = (String) request.getSession().getAttribute(Member.WEIXIN_OPENT_ID);
            BindUser bindUser = bindUserService.findByUsername(openId, Type._wx);
            bindUser.setMember(mobileMember);
            bindUserService.save(bindUser);
            Principal principal = new Principal(mobileMember.getId(), mobileMember.getUsername());
            session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
            return Message.success("绑定手机成功！！！");
        };

        if (current != null) {
            member.setMember(current);
            member.setShareOwner(current.getTenant());
        }
        
        member.setUsername(mobile);
        member.setMobile(mobile);
        member.setBindMobile(BindStatus.binded);

        memberService.save(member);

        return Message.success("绑定手机成功！！！");

    }
}
