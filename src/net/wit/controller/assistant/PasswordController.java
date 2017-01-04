/**
 *====================================================
 * 文件名称: MemberInfoController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.assistant;

import net.wit.Setting;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.LockType;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.service.*;
import net.wit.util.Base64Util;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @ClassName: MemberInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("assistantPasswordAreaController")
@RequestMapping("/assistant/password")
public class PasswordController extends BaseController {

	public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
	public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";
	
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

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;
	
	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sendMobile(String username, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		if (tmp!=null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
			if (!tmp.canReset()) {
				return DataBlock.error("系统忙，稍等几秒重试");
			}
		}
        Member member = memberService.findByUsername(username);
		if (member==null) {
			member = memberService.findByBindTel(username);
		}
        if (member==null) {
			return DataBlock.error("当前用户名无效");
        }
		if (!member.getBindMobile().equals(BindStatus.binded) ) {
			return DataBlock.error("当前用户没有绑定手机号");
		}
		String mobile = member.getMobile();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(CAPTCHA_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(CAPTCHA_CONTENT_SESSION, mobile);

		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(username);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","发送成功");
	}

	/**
	 * 找回登录密码
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock update(String username,String captcha,String newPass,HttpServletRequest request) {

		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		String code = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
		session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_CONTENT_SESSION);

		if (StringUtils.isEmpty(username)) {
			return DataBlock.error("无效用户名");
		}
		String newPwd = Base64Util.decode(newPass);

		Member member = memberService.findByUsername(username);
		if (member==null) {
			return DataBlock.error("无效用户名");
		}

		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}

		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}

		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}

		if (!member.getMobile().equals(code) || member.getBindMobile() != BindStatus.binded) {
			return DataBlock.error("该用户手机未绑定");
		}

		member.setPassword(DigestUtils.md5Hex(newPwd));
		member.setLoginFailureCount(0);
		member.setIsLocked(LockType.none);
		member.setLockedDate(null);
		member.setIsLocked(LockType.none);
    	memberService.update(member);
		return DataBlock.success("success","修改成功");
		
	}
	
}
