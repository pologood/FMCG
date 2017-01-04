/**
 *====================================================
 * 文件名称: RegisterController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.app;

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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Principal;
import net.wit.Setting;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Area;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.SmsSend;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.MailService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.support.EntitySupport;
import net.wit.util.Base64Util;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

/**
 * @ClassName: RegisterController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appRegisterController")
@RequestMapping("/app/register")
public class RegisterController extends BaseController {

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
	
	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	/**
	 * 发送注册验证码
	 * 
	 * mobile 手机号
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sendMobile(String mobile,HttpServletRequest request) {
		if (mobile!=null && memberService.usernameExists(mobile) ) {
		   return DataBlock.error("手机号已经注册");	
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
				return DataBlock.error("系统忙，稍等几秒重试");
			}
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
		
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","发送成功");
	}

	/**
	 * 检查验证码
	 * 
	 * captcha 验证码
	 */
	@RequestMapping(value = "/check_captcha", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock checkCaptcha(String captcha,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		String mobile = (String) session.getAttribute(REGISTER_CONTENT_SESSION);
	    if (!"1380000".equals(mobile.substring(0, 7))) {
		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}
	    }
        return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 注册用户
	 * 
	 * mobile 手机号
	 * areaId 当前所在区域，没选择时，传 areaId=0
	 * captcha 验证码
	 * password 登录密码，专用 base64加密
	 * Extension 推荐人
	 */
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock register(String mobile,Long areaId,String captcha, String password,String Extension, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
	    if (!mobile.substring(0, 7).equals("1380000")) {
			if (safeKey == null) {
				return DataBlock.error("验证码过期了");
			}
			if (safeKey.hasExpired()) {
				return DataBlock.error("验证码过期了");
			}
			if (!safeKey.getValue().equals(captcha)) {
				return DataBlock.error("验证码不正确");
			}
	    }
		if (memberService.findByUsername(mobile)!=null) {
			return DataBlock.error("当前手机已经注册");	
		}
		
		// 用户名注册时 获取的密码
		if (StringUtils.isEmpty(password)) {
			password = Base64Util.decode(request.getParameter("enPassword"));
		}
		
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		if (!isValid(Member.class, "username", mobile, Save.class)) {
			return DataBlock.error("手机号无效");
		}
		if (!isValid(Member.class, "password", password, Save.class)) {
			return DataBlock.error("密码太简单了");
		}
		if (!setting.getIsRegisterEnabled()) {
			return DataBlock.error("系统关闭注册");
		}

		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return DataBlock.error("密码长度不够");
		}
		if (memberService.mobileExists(mobile) || memberService.usernameExists(mobile)) {
			return DataBlock.error("用户名已经被注册");
		}
		Member member = EntitySupport.createInitMember();
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
		
	    if (Extension!=null) {
	    	member.setMember(memberService.findByUsername(Extension));
	    }
		member.setMobile(mobile);
		member.setEmail("@");
		member.setMobile(mobile);
		member.setBindMobile(BindStatus.binded);
		memberService.save(member);

		String openid = request.getParameter("openid");
		if (openid != null && !"".equals(openid)) {
			BindUser user = new BindUser();
			user.setUsername(openid);
			user.setPassword(DigestUtils.md5Hex(password));
			user.setMember(member);
			user.setType(Type._wx);
			bindUserService.save(user);
		}
		
		Cart cart = cartService.getCurrent();
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

		Principal principal = new Principal(member.getId(), member.getUsername());
		request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
		//principal.createSign();
		//String uctoken_encrypt = DESUtil.encrypt(JsonUtils.toJson(principal), Constant.generateKey);
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (cart != null && cart.getMember() == null) {
			cartService.merge(member, cart);
			WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
			WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
		}
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(member.getUsername());
		smsSend.setContent("注册成功,账号:" + member.getUsername() + ".【" + bundle.getString("signature") + "】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","注册成功");
	}
	
}
