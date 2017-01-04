/**
 *====================================================
 * 文件名称: ConsumerController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Area;
import net.wit.entity.BindUser;
import net.wit.entity.SmsSend;
import net.wit.entity.Tenant;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Consumer;
import net.wit.entity.Consumer.Status;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.ConsumerService;
import net.wit.service.MailService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

/**
 * @ClassName: ConsumerController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appConsumerController")
@RequestMapping("/app/consumer")
public class ConsumerController extends BaseController {

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
	
	@Resource(name = "consumerServiceImpl")
	private ConsumerService consumerService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	@Resource(name = "mailServiceImpl")
	private MailService mailService;

	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sendMobile(String mobile,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		
		Member current = memberService.findByUsername(mobile);
		if (current!=null) {
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(new Filter("tenant", Operator.eq, tenant));
			filters.add(new Filter("member", Operator.eq, current));
		    List<Consumer> consumers = consumerService.findList(10, filters, null);
		    
		    if (consumers.size()>0) {
		    	return DataBlock.error("当前客户已经是会员了");
		    }
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
		smsSend.setContent("验证码 :" + securityCode + ",用于店铺协助会员注册账号，请放心告知店铺。【" + bundle.getString("signature") + "】");
		smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","发送成功");
	}

	/**
	 * 检查验证码
	 */
	@RequestMapping(value = "/check_captcha", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock checkCaptcha(String captcha,Boolean remove,HttpServletRequest request, HttpServletResponse response) {
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
        return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 添加会员
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(String mobile,Long areaId,String captcha, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}

		Member member = null;
		Member current = memberService.getCurrent();
		if (!memberService.usernameExists(mobile) && memberService.mobileExists(mobile)) {
			String password = mobile.substring(mobile.length()-6, mobile.length());

			Setting setting = SettingUtils.get();
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
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
		    member.setShareOwner(current.getTenant());
			member.setMobile(mobile);
			member.setEmail("@");
			member.setMobile(mobile);
			member.setBindMobile(BindStatus.binded);
			memberService.save(member);

			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(member.getUsername());
			smsSend.setContent("注册成功,账号:" + member.getUsername() +" 默认密码:"+password+ "【" + bundle.getString("signature") + "】");
			smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
		} else {
			member = memberService.findByUsername(mobile);
			if (member==null) {
				member = memberService.findByBindTel(mobile);
			}
		}
		
		if (current!=null&&current.getTenant()!=null) {
	    	Consumer consumer = new Consumer();
		    consumer.setMember(member);
		    consumer.setStatus(Status.enable);
		    consumer.setTenant(current.getTenant());
		    consumer.setMemberRank(memberRankService.findDefault());
		    consumerService.save(consumer);
		}
		
		return DataBlock.success("success","注册成功");
	}
	
}
