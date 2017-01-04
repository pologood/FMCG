package net.wit.controller.b2b;

import java.math.BigDecimal;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.entity.Area;
import net.wit.entity.Cart;
import net.wit.entity.Community;
import net.wit.entity.Member;
import net.wit.entity.Member.RegType;
import net.wit.entity.Receiver;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.Tenant;
import net.wit.entity.Tenant.Status;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.ActivityDetailService;
import net.wit.service.ActivityRulesService;
import net.wit.service.AreaService;
import net.wit.service.CaptchaService;
import net.wit.service.CartService;
import net.wit.service.CommunityService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.ReceiverService;
import net.wit.service.SmsSendService;
import net.wit.service.TenantCategoryService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.uic.api.UICService;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

@Controller("b2bRegisterController")
@RequestMapping("/b2b/register")
public class RegisterController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "uicService")
	private UICService uicService;
	
	@Resource(name = "cartServiceImpl")
	private CartService cartService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;
    
    @Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
    
    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;
    
    @Resource(name = "receiverServiceImpl")
    private ReceiverService receiverService;
   
		
	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";

	public static final String REGISTER_MOBILE_SESSION = "register_mobile_session";

	public static final String REGISTER_EMAIL_SESSION = "register_email_session";
	
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";

	/**
	 * 获取社区信息
	 */
	@RequestMapping(value = "/get_community", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> getCommunity(Long areaId) {
		Map<Long, String> data = new HashMap<Long, String>();
		Area area = areaService.find(areaId);
		List<Community> communitys = communityService.findList(area);
		for (Community community : communitys) {
			data.put(community.getId(), community.getName());
		}
		return data;
	}

	/**
	 * 注册页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Area area = areaService.getCurrent();
		model.addAttribute("area", area);
		RSAPublicKey publicKey = rsaService.generateKey(request);
		model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		model.addAttribute("jsessionid", "");
		return "/b2b/register/index";
	}
	
	/**
	 * 注册页面(聚德汇-个人)
	 */
	@RequestMapping(value="/register_person", method = RequestMethod.GET)
	public String registerPerson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Area area = areaService.getCurrent();
		model.addAttribute("area", area);
		RSAPublicKey publicKey = rsaService.generateKey(request);
		model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		model.addAttribute("jsessionid", "");
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/b2b/register/register_person";
	}
	/**
	 * 注册页面(聚德汇-企业)
	 */
	@RequestMapping(value="/register_company", method = RequestMethod.GET)
	public String registerCompany(HttpServletRequest request, HttpServletResponse response,String redirectUrl, ModelMap model) {
		Area area = areaService.getCurrent();
		model.addAttribute("area", area);
		RSAPublicKey publicKey = rsaService.generateKey(request);
		model.addAttribute("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		model.addAttribute("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		model.addAttribute("jsessionid", "");
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		model.addAttribute("redirectUrl",redirectUrl);
		model.addAttribute("area",areaService.getCurrent());
		return "/b2b/register/register_company";
	}

	/**
	 * 注册提交(手机/邮箱)
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody Message submit(String username, String mobile, String email, String securityCode, Long areaId, String password, String enPassword, String jsessionid, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, RedirectAttributes redirectAttributes) {
		if (StringUtils.isEmpty(password)) {
			password = rsaService.decryptParameter("enPassword", request);
		}
		rsaService.removePrivateKey(request);
		if (mobile!=null) {
			return uicService.register(mobile, password, securityCode, areaId, RegType.mobile, request, response);
		} else 
		if (email!=null) {
			return uicService.register(mobile, password, securityCode, areaId, RegType.email, request, response);
		} else
		    return Message.error("不支持的注册方式");
	}

	/**
	 * 申请安装商
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			tenant = EntitySupport.createInitTenant();
			if (member.getArea() != null) {
				tenant.setArea(member.getArea());
			}
			tenant.setTenantType(Tenant.TenantType.tenant);
			tenant.setAddress(member.getAddress());
			tenant.setLinkman(member.getName());
			tenant.setTelephone(member.getMobile());
			tenant.setName(member.getUsername() + "的店铺");
		}
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenant", tenant);
		return "/b2b/register/add";
	}

	/**
	 * 店铺申请提交
	 */
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(Tenant tenant, long areaId, long communityId, Long tenantCategoryId, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant saveTenant = null;
		if (tenant.getId() != null && tenant.getId() != 0) {
			saveTenant = tenantService.find(tenant.getId());
		} else {
			saveTenant = EntitySupport.createInitTenant();
			saveTenant.setScore(0F);
			saveTenant.setTotalScore(0L);
			saveTenant.setScoreCount(0L);
			saveTenant.setHits(0L);
			saveTenant.setWeekHits(0L);
			saveTenant.setMonthHits(0L);
			saveTenant.setStatus(Status.success);
		}

		BeanUtils.copyProperties(tenant, saveTenant, new String[] { "code", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "createDate", "modifyDate", "status", "logo", "licensePhoto" });
		if (saveTenant.getCode() == null) {
			saveTenant.setCode("1");
		}
		saveTenant.setArea(areaService.find(areaId));
		saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
		saveTenant.setShortName(tenant.getName());
		saveTenant.setStatus(Tenant.Status.success);
		//if (memberService.dspLogin(member.getUsername(), member.getPassword())) {
		//	tenantService.save(saveTenant, member);
			return "redirect:status.jhtml";
		//} else {
		//	addFlashMessage(redirectAttributes, Message.error("申请开店失败"));
		//	return "redirect:add.jhtml";
		//}
	}

	/**
	 * 查询审核状态
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
		return "/b2b/register/status";
	}

	/**
	 * 检查用户是否存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody boolean checkUsername(String username) {
		return memberService.usernameExists(username);
	}

	/**
	 * 检查E-mail是否存在
	 */
	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody boolean checkEmail(String email) {
		return memberService.usernameExists(email);
	}

	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/getCheckCode", method = RequestMethod.POST)
	@ResponseBody
	public Message getCheckCode(String mobile,HttpServletRequest request) {
		//return uicService.sendAuthcodeByTel(mobile,null, request);
		return Message.error("安全检测不合法");
	}

	/**
	 * 发送邮件
	 */
	@RequestMapping(value = "/send_email", method = RequestMethod.POST)
	@ResponseBody
	public Message sendEmail(String email, HttpServletRequest request) {
		return  uicService.sendAuthcodeByMail(email,null, request);
	}
	/**
	 * 获取验证码
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	@ResponseBody
	public Message getCheckCode(String username,String captchaId,String captcha, HttpServletRequest request) {
		if (StringUtils.isEmpty(username)) {
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
        session.setAttribute(REGISTER_CONTENT_SESSION, username);

        SmsSend smsSend = new SmsSend();
        smsSend.setMobiles(username);
        smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
        smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
        smsSendService.smsSend(smsSend);
        return Message.success("消息发送成功");
	}
	/**
     * 注册提交(手机)
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    Message register(String nickName,String mobile, String securityCode,
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

        if (!memberService.usernameExists(mobile) && !memberService.mobileExists(mobile)) {
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
            member.setNickName(nickName);
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
            if (member==null) {
            	member = memberService.findByBindTel(mobile);
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

        return Message.success("注册成功！！！");

    }
    
    //企业注册（聚德惠）
    @RequestMapping(value = "/register_company", method = RequestMethod.POST)
    public
    @ResponseBody
    Message registerCompany(String mobile, String securityCode,String name,String newpassword, String address, Long tenantCategoryId, String licensePhoto, String linkman,String tenantType, Long areaId,
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
        if (StringUtils.isEmpty(newpassword)) {
        	return Message.error("密码不能为空");
		}
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
            member = EntitySupport.createInitMember();
            member.setArea(area);
            member.setUsername(mobile);
            member.setNickName(name);
            member.setPassword(DigestUtils.md5Hex(rsaService.decryptParameter("newpassword", request)));
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
            member.setPaymentPassword(DigestUtils.md5Hex(rsaService.decryptParameter("newpassword", request)));
            member.setRebateAmount(new BigDecimal(0));
            member.setProfitAmount(new BigDecimal(0));
            member.setMemberRank(memberRankService.findDefault());
            member.setFavoriteProducts(null);
            member.setFreezeBalance(new BigDecimal(0));
            member.setPrivilege(0);
            member.setTotalScore((long) 0);
            member.setMember(current);
            member.setMobile(mobile);
            member.setAddress(address);
            member.setEmail("@");
            member.setMobile(mobile);
            member.setBindMobile(Member.BindStatus.binded);
			member.setLoginCount(0);
			member.setSourceType(Member.SourceType.personal);
            Set<Receiver> receivers=new HashSet<Receiver>();
            String extension = (String) session.getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
            if (extension != null) {
                member.setMember(memberService.findByUsername(extension));
            }
            memberService.save(member);
	        Receiver receiver=new Receiver();
	        receiver.setAddress(address);
	        receiver.setIsDefault(true);
	        receiver.setArea(area);
	        receiver.setConsignee(linkman);
	        receiver.setMember(member);
	        receiver.setPhone(mobile);
	        receiver.setZipCode("000000");
	        receiverService.save(receiver);
	        receivers.add(receiver);
	        member.setReceivers(receivers);
	        memberService.save(member);

            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(member.getUsername());
            smsSend.setContent("注册成功,账号:" + member.getUsername() + " 默认密码:" + rsaService.decryptParameter("newpassword", request) + "【" + bundle.getString("signature") + "】");
            smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
        } else {//用户存在，直接获取
            member = memberService.findByUsername(mobile);
            if (member==null) {
            	member = memberService.findByBindTel(mobile);
            }
//            member.setPassword(DigestUtils.md5Hex(rsaService.decryptParameter("newpassword", request)));
//            memberService.update(member);
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
}
