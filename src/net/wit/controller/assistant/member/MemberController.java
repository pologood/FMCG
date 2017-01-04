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
package net.wit.controller.assistant.member;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.BalanceModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.MemberModel;
import net.wit.entity.*;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.entity.SmsSend.Type;
import net.wit.service.*;
import net.wit.util.*;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * @ClassName: MemberInfoController
 * @Description: 会员资料 密码等
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("assistantMemberController")
@RequestMapping("/assistant/member")
public class MemberController extends BaseController {

	public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
	public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name ="employeeServiceImpl")
	private EmployeeService employeeService;

	/**
	 * 重置密码时发送手机验证码
	 * params username 用户名
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sendMobile(String username, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
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
				return DataBlock.error("系统忙，稍等几秒重试");
			}
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
		//System.out.println(securityCode);

		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
		smsSend.setType(Type.captcha);
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","发送成功");
	}

	/**
	 * 修改密码
	 * params oldPass 原密码 （需要加密）
	 * params newPass 新密码 （需要加密）
	 */
	@RequestMapping(value = "/password/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock passwordUpdate(String oldPass,String newPass,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		String oldPwd = Base64Util.decode(oldPass);
		String newPwd = Base64Util.decode(newPass);
		if (StringUtils.isEmpty(oldPwd) && StringUtils.isEmpty(newPwd)) {
			return DataBlock.error("密码无效");
		}

		if (DigestUtils.md5Hex(oldPwd).equals(member.getPassword())) {
			member.setPassword(DigestUtils.md5Hex(newPwd));
			memberService.update(member);
			return DataBlock.success("success","修改成功");

		} else {
			return DataBlock.error("原密码错误");
		}
	}

	/**
	 * 修改支付密码
	 * params oldPass 原密码 （需要加密）
	 * params newPass 新密码 （需要加密）
	 */
	@RequestMapping(value = "/password/payment/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock updatePaymentPass(String oldPass,String newPass,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		String oldPwd = Base64Util.decode(oldPass);
		String newPwd = Base64Util.decode(newPass);
		if (StringUtils.isEmpty(oldPwd) && StringUtils.isEmpty(newPwd)) {
			return DataBlock.error("密码无效");
		}

		if (DigestUtils.md5Hex(oldPwd).equals(member.getPaymentPassword())) {
			member.setPaymentPassword(DigestUtils.md5Hex(newPwd));
			memberService.update(member);
			return DataBlock.success("success","修改成功");

		} else {
			return DataBlock.error("原密码错误");
		}
	}

	/**
	 * 找回支付密码
	 * params username 用户名
	 * params captcha  手机发送时收到的验证码
	 * params newPass 新密码 （需要加密）
	 */
	@RequestMapping(value = "/password/retrieve", method = RequestMethod.POST)
	public @ResponseBody DataBlock retrievePayMentPass(String username,String captcha, String newPass, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		String code = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
		session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_CONTENT_SESSION);

		if (StringUtils.isEmpty(username)) {
			return DataBlock.error("无效用户名");
		}
		String newPwd = Base64Util.decode(newPass);

		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}

		if (!member.getMobile().equals(code) || !member.getBindMobile().equals(BindStatus.binded) ) {
			return DataBlock.error("该用户手机未绑定");
		}

		member.setPaymentPassword(DigestUtils.md5Hex(newPwd));
    	memberService.update(member);
		return DataBlock.success("success","修改成功");
	}

	/**
	 * 绑定/取消绑定手机
	 * params captcha  手机发送时收到的验证码
	 */
	@RequestMapping(value = "/mobile/bind", method = RequestMethod.POST)
	public @ResponseBody DataBlock bind_save(String captcha, HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		String content = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
		session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_CONTENT_SESSION);

		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}

		if (member.getBindMobile().equals(BindStatus.binded)) {
			if (!member.getMobile().equals(content)) {
				return DataBlock.error("无效手机号");
			}
			member.setMobile(content);
			member.setBindMobile(BindStatus.unbind);
		} else {
			member.setMobile(content);
			member.setBindMobile(BindStatus.binded);
		}

		memberService.save(member);
		return DataBlock.success("success","绑定成功");
	}


	/**
	 * 修改用户信息
	 * params name 姓名
	 * params birth 生日 2015-05-01
	 * params address 详细地址
	 * params phone 电话
	 * params zipCode 邮政编码
	 * params sex 性别  0 男  1 是女
	 * params areaId 区域地址
	 * params headImg 头像的 URL
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock update(String name, String nickName, String birth, String address,String phone, String zipCode, String sex, String areaId, String headImg, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (!StringUtils.isEmpty(name)) {
			if (member.getName() != null && !member.getName().equals(name)) {
				if (member.getIdcard() != null && member.getIdcard().getAuthStatus().equals(AuthStatus.success)) {
					return DataBlock.error("实名认证通过，不能修改姓名");
				}
			}
			member.setName(name);

			if(member.getTenant()!=null){
				if(!activityDetailService.isActivity(null,member.getTenant(), activityRulesService.find(12L))){
					activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(12L));
				}
			}

		}
		
		if (!StringUtils.isEmpty(nickName) && nickName != null) {
			member.setNickName(nickName);
		}
		if (!StringUtils.isEmpty(sex) && sex != null) {
			if ("0".equals(sex)) {
				member.setGender(Gender.male);
			} else
			if ("1".equals(sex)){
				member.setGender(Gender.female);
			} else
				if ("male".equals(sex)) {
					member.setGender(Gender.male);
				} else {
					member.setGender(Gender.female);
				}
		}
		if (areaId != null) {
			Area area = areaService.find(Long.valueOf(areaId));
			member.setArea(area);
		}
		if (!StringUtils.isEmpty(birth) && birth != null) {
			member.setBirth(DateUtil.parseDate(birth));
		}
		if (!StringUtils.isEmpty(address) && address != null) {
			member.setAddress(address);
		}
		if (!StringUtils.isEmpty(phone) && phone != null) {
			member.setPhone(phone);
		}
		if (!StringUtils.isEmpty(zipCode) && zipCode != null) {
			member.setZipCode(zipCode);
		}
		if (!StringUtils.isEmpty(headImg) && headImg != null) {
			member.setHeadImg(headImg);

			if(member.getTenant()!=null){
				if(!activityDetailService.isActivity(null,member.getTenant(), activityRulesService.find(11L))){
					activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(11L));
				}
			}
		}
		memberService.save(member);
		return DataBlock.success("success","修改成功");
	}
	
	/**
	 * 提交实名认证
	 * params name 姓名
	 * params idcard 身份证号
	 * params pathFront 身份证正面拍照  url
	 * params pathBack 身份证反面提照  url
	 */
	@RequestMapping(value = "/idcard/save", method = RequestMethod.POST)
	public @ResponseBody DataBlock idcardSave(String name, String idCard, String pathFront, String pathBack, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Idcard idcard = member.getIdcard();
		if (pathFront == null || "".equals(pathFront)) {
			return DataBlock.error("无拍照图片");
		}
		if (pathBack == null || "".equals(pathBack)) {
			return DataBlock.error("无拍照图片");
		}
		if (name == null || "".equals(name)) {
			return DataBlock.error("请正确输入姓名");
		}
		if (idcard == null) {
			idcard = new Idcard();
		}
		idcard.setPathFront(pathFront);
		idcard.setPathBack(pathBack);
		idcard.setNo(idCard);
		idcard.setAddress("#");
		idcard.setBeginDate(new Date());
		idcard.setEndDate(new Date());
		idcard.setAuthStatus(AuthStatus.wait);
		idcard.setName(name);
		idcardService.save(idcard);
//		member.setName(name);
		member.setIdcard(idcard);
		memberService.save(member);
		return DataBlock.success("success","提交成功");
	}

	/**
	 * 检测邀请码是否合法
	 */
	@RequestMapping(value = "/invite_code", method = RequestMethod.GET)
	public @ResponseBody DataBlock InviteCode(Long code) {
		Long id = code-100000L;
		Member member = memberService.find(id);
		if (member==null) {
			return DataBlock.error("无效邀请码");
		}
		return DataBlock.success(id,"执行成功");
	}
	
	/**
	 * 通过读取会员详细资料
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody DataBlock view(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		MemberModel model = new MemberModel();
		model.copyFrom(member);
		if (!model.getOwner()) {
			//Pageable pageable = new Pageable();
			//List<Filter> filters = new ArrayList<Filter>();
			//filters.add(new Filter("tenant", Operator.eq, member.getTenant()));
			//filters.add(new Filter("member", Operator.eq, member));
			//pageable.setFilters(filters);
			//Page<Employee> emps = employeeService.findPage(pageable);
			//if (!emps.getContent().isEmpty()) {
			//	model.setRole(emps.getContent().get(0).getRole());
			//}
			Employee employee = employeeService.findMember(member,member.getTenant());
			if(employee!=null){
				model.setRole(employee.getRole());
			}

		}
		else {
			model.setRole(",owner");
		}
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 通过读取会员余额资料
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public @ResponseBody DataBlock balance(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		BalanceModel model = new BalanceModel();
		model.copyFrom(member);
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 通过读取店铺余额资料
	 */
	@RequestMapping(value = "/balance/owner", method = RequestMethod.GET)
	public @ResponseBody DataBlock balanceOwner(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
//		Tenant tenant = member.getTenant();
//		if (tenant==null) {
//			return DataBlock.error(DataBlock.TENANT_INVAILD);
//		}
//		Member owner = tenant.getMember();
		BalanceModel model = new BalanceModel();
		model.copyFrom(member);
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 获取缴请注册二维码地址
	 */
	@RequestMapping(value = "/qrcode/json",method = RequestMethod.GET)
	@ResponseBody
	public DataBlock qrcodeJson(HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/becomevip.jhtml?extension=" + (member != null ? member.getUsername() : ""));
            return DataBlock.success(url,"获取成功");
			
		} catch (Exception e) {
			return DataBlock.error("获取二维码串失败");
		}
	}
	
	/**
	 * 获取缴请注册二维码图片
	 */
	@RequestMapping(value = "/qrcode",method = RequestMethod.GET)
	public void qrcode(HttpServletRequest request, HttpServletResponse response) {
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String url = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/bound/index.jhtml");

			String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
			response.reset();  
			response.setContentType("image/jpeg;charset=utf-8");
			try {
				QRBarCodeUtil.encodeQRCode(url, tempFile, 400, 400);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			ServletOutputStream output = response.getOutputStream();// 得到输出流  
			InputStream imageIn = new FileInputStream(new File(tempFile));  
            // 得到输入的编码器，将文件流进行jpg格式编码  
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);  
            // 得到编码后的图片对象  
            BufferedImage image = decoder.decodeAsBufferedImage();  
            // 得到输出的编码器  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);  
            encoder.encode(image);// 对图片进行输出编码  
            imageIn.close();// 关闭文件流  
            output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
