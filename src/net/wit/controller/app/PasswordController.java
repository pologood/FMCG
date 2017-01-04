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
package net.wit.controller.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.FileInfo.FileType;
import net.wit.Filter.Operator;
import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.ajax.model.MemberInfoModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Area;
import net.wit.entity.BindUser;
import net.wit.entity.Cart;
import net.wit.entity.Host;
import net.wit.entity.Idcard;
import net.wit.entity.Member;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.Tenant;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.entity.Member.LockType;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.CartService;
import net.wit.service.FileService;
import net.wit.service.IdcardService;
import net.wit.service.MailService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;
import net.wit.util.Base64Util;
import net.wit.util.DESUtil;
import net.wit.util.DateUtil;
import net.wit.util.GsonUtil;
import net.wit.util.JsonUtils;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @ClassName: MemberInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appPasswordAreaController")
@RequestMapping("/app/password")
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
		if (!member.getBindMobile().equals(Member.BindStatus.binded) ) {
			return DataBlock.error("当前用户没有绑定手机号");
		}
		String mobile = member.getMobile();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(CAPTCHA_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(CAPTCHA_CONTENT_SESSION, mobile);
		
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
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
		member.setIsLocked(Member.LockType.none);
		member.setLockedDate(null);
		member.setIsLocked(LockType.none);
    	memberService.update(member);
		return DataBlock.success("success","修改成功");
		
	}
	
}
