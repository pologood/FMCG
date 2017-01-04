/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.entity.*;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;
import org.apache.commons.codec.digest.DigestUtils;
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

/**
 * Controller - 会员注册
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeRegisterController")
@RequestMapping("/store/register")
public class RegisterController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";
	/**
	 * 检查用户是否存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	@ResponseBody
	public Message getCheckCode(String username,String captchaId,String captcha, HttpServletRequest request) {
		if (StringUtils.isEmpty(username)) {
			return Message.warn("手机为空");
		}

		Member member = memberService.findByUsername(username);
		if (member==null) {
			member = memberService.findByBindTel(username);
		}
        if(member!=null){
            Tenant tenant=member.getTenant();
            if(tenant!=null){
                if(member==tenant.getMember()){
                    return Message.warn("您已经注册过店铺，不能在注册了");
                }
                if(tenant.getStatus()== Tenant.Status.none){
                    return Message.warn("您的店铺正在审核");
                }
            }
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
		session.setAttribute(REGISTER_CONTENT_SESSION, username);
		System.out.println(securityCode);
		SmsSend smsSend = new SmsSend();
		smsSend.setMobiles(username);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return Message.success("消息发送成功");
	}

	//企业注册
	@RequestMapping(value = "/register_company", method = RequestMethod.POST)
	@ResponseBody
	public Message registerCompany(String mobile, String securityCode,String name,String newpassword, String address, Long tenantCategoryId, String licensePhoto, String linkman,String tenantType, Long areaId,
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
		Area area=null;
		if(areaId==null){
			area= areaService.getCurrent();
		}else{
			area=areaService.find(areaId);
		}
		if (!memberService.usernameExists(mobile) && !memberService.mobileExists(mobile)) {//用户不存在，注册
			Setting setting = SettingUtils.get();
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			if (!setting.getIsRegisterEnabled()) {
				return Message.error("系统关闭注册");
			}
			String password = mobile.substring(mobile.length()-6, mobile.length());
			if (StringUtils.isEmpty(password)) {
				return Message.error("密码不能为空");
			}
			member = EntitySupport.createInitMember();

			member.setArea(area);
			member.setUsername(mobile);
			member.setNickName(name);
			member.setPassword(DigestUtils.md5Hex(password));
			member.setPoint(setting.getRegisterPoint());
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
			member.setRegisterIp(request.getRemoteAddr());
			member.setLoginIp(request.getRemoteAddr());
			member.setLoginDate(new Date());
			member.setSafeKey(null);
			member.setBindEmail(Member.BindStatus.none);
			member.setBindMobile(Member.BindStatus.binded);
			member.setPaymentPassword(DigestUtils.md5Hex(password));
			member.setRebateAmount(new BigDecimal(0));
			member.setProfitAmount(new BigDecimal(0));
			member.setMemberRank(memberRankService.findDefault());
			member.setFavoriteProducts(null);
			member.setPrivilege(0);
			member.setLoginCount(0);
			member.setMember(current);
			member.setMobile(mobile);
			member.setAddress(address);
			member.setMobile(mobile);
			member.setBindMobile(Member.BindStatus.binded);
			member.setSourceType(Member.SourceType.personal);

			String extension = (String) session.getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
			if (extension != null) {
				member.setMember(memberService.findByUsername(extension));
			}
//			Set<Receiver> receivers=new HashSet<Receiver>();
//			member.setReceivers(receivers);
			memberService.save(member);

//			Receiver receiver=new Receiver();
//			receiver.setAddress(address);
//			receiver.setIsDefault(true);
//			receiver.setArea(area);
//			receiver.setConsignee(linkman);
//			receiver.setMember(member);
//			receiver.setPhone(mobile);
//			receiver.setZipCode("000000");
//			receiverService.save(receiver);
//			receivers.add(receiver);
//			member.getReceivers().add(receiver);
//			memberService.update(member);
			SmsSend smsSend = new SmsSend();
			smsSend.setMobiles(member.getUsername());
			smsSend.setContent("注册成功,账号:" + member.getUsername() + " 默认密码:" + password + "【" + bundle.getString("signature") + "】");
			smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
		} else {//用户存在，直接获取
			member = memberService.findByUsername(mobile);
			if (member==null) {
				member = memberService.findByBindTel(mobile);
			}
		}
		//注册完后申请店铺
		Tenant saveTenant = member.getTenant();
		if (saveTenant == null) {
			saveTenant = EntitySupport.createInitTenant();
			saveTenant.setTenantCategory(tenantCategoryService.find(1l));
			saveTenant.setArea(area);
			saveTenant.setName(name);
			saveTenant.setShortName(name);
			saveTenant.setAddress(address);
			saveTenant.setLinkman(linkman);
			saveTenant.setTelephone(mobile);
			saveTenant.setLicensePhoto(licensePhoto);
			saveTenant.setTenantType(TenantType.tenant);
			saveTenant.setStatus(Tenant.Status.none);
			tenantService.save(saveTenant, member, null);
		}else{
			return Message.warn("您已有自己的店铺");
		}

		//// TODO: 2016/7/21 注册账号，并成功设置店铺名称，即完成任务
		activityDetailService.addPoint(null, saveTenant, activityRulesService.find(1L));
		return Message.success("店铺申请成功，等待后台审核...");

	}

	/**
	 * 审核状态
	 */
	@RequestMapping(value = "/status")
	public String find(ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			tenant = EntitySupport.createInitTenant();
		}
		model.addAttribute("tags", tenant.getTags());
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenant", tenant);
		return "/store/register/status";
	}

}