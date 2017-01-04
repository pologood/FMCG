/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.uic;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.Area;
import net.wit.entity.Idcard;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.service.AreaService;
import net.wit.service.IdcardService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.util.DateUtil;
import net.wit.util.FileUtil;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员手机接口
 * @author rsico Team
 * @version 3.0
 */
@Controller("uicMemberController")
@RequestMapping("/uicMember")
public class MemberController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";

	public static final String REGISTER_TYPE_SESSION = "register_type_session";

	public static final String REGISTER_CONTENT_SESSION = "register_content_session";

	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/delUser", method = RequestMethod.POST)
	public @ResponseBody Message delUser(String username, String password, HttpServletRequest request) {
		if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
			return Message.error("shop.common.invalid");
		}
		Member member = memberService.findByUsername(username);
		if (member == null || !member.getPassword().equals(password)) {
			return Message.error("用户名不存在或密码错误");
		}
		if ( member.getBalance().compareTo(new BigDecimal(0)) > 0) {
			return Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername());
		}
		memberService.delete(member);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 修改用户信息
	 */
	@RequestMapping(value = "/editInfo", method = RequestMethod.POST)
	public @ResponseBody Message editInfo(String username, String paymentPassword, String email, String name, String birth, String address, String mobile, String phone, String zipCode, String sex, String areaId, HttpServletRequest request,
			HttpServletResponse response) {
		String password = rsaService.decryptParameter("password", request);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("登录超时");
		}
		if (StringUtils.isNotBlank(name)) {
			if (member.getName() != null && !member.getName().equals(name)) {
				if (member.getIdcard() != null && member.getIdcard().getAuthStatus().equals(Idcard.AuthStatus.success)) {
					return Message.error("实名认证通过，不能修改姓名");
				}
				member.setName(name);
			}
		}
		if (StringUtils.isNotBlank(email)) {
			if (member.getEmail() != null && !member.getEmail().equals(email)) {
				if (member.getBindEmail().equals(BindStatus.binded)) {
					return Message.error("邮箱已绑定，不能修改邮箱");
				}
				member.setEmail(email);
			}
		}
		if (StringUtils.isNotBlank(mobile)) {
			if (member.getMobile() != null && !member.getMobile().equals(mobile)) {
				if (member.getBindMobile().equals(BindStatus.binded)) {
					return Message.error("手机已绑定，不能修改手机");
				}
				member.setMobile(mobile);
			}
		}
		rsaService.removePrivateKey(request);
		if (!StringUtils.isEmpty(password) && password != null) {
			member.setPassword(DigestUtils.md5Hex(password));
		}
		if (!StringUtils.isEmpty(sex) && sex != null) {
			if ("0".equals(sex)) {
				member.setGender(Gender.male);
			} else {
				member.setGender(Gender.female);
			}
		}
		if (areaId != null) {
			Area area = areaService.find(Long.valueOf(areaId));
			member.setArea(area);
		}
		if (!StringUtils.isEmpty(paymentPassword) && paymentPassword != null) {
			member.setPaymentPassword(paymentPassword);
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
		memberService.save(member);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 上传证件照
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody Message uploadFile(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String imgUrl = "";
		// 图片
		try {
			Setting setting = SettingUtils.get();
			imgUrl = FileUtil.uploadImage_phone(request);
			imgUrl = imgUrl.replaceAll("\\\\", "/");
			imgUrl = setting.getSiteUrl() + "/" + imgUrl;
		} catch (Exception e) {
			addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
			return ERROR_MESSAGE;
		}
		return Message.success(imgUrl);
	}

	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public @ResponseBody Message apply(String username, String name, String idCard, String pathFront, String pathBack, RedirectAttributes redirectAttributes) {
		// Member member = memberService.findByUsername(username);
		Member member = memberService.getCurrent();
		if (member == null) {
			addFlashMessage(redirectAttributes, Message.error("登录超时"));
			return ERROR_MESSAGE;
		}
		Idcard idcard = member.getIdcard();
		if (pathFront == null || "".equals(pathFront)) {
			addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
			return ERROR_MESSAGE;
		}
		if (pathBack == null || "".equals(pathBack)) {
			addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
			return ERROR_MESSAGE;
		}
		if (idcard == null) {
			idcard = new Idcard();
		}
		idcard.setPathFront(pathFront);
		idcard.setPathBack(pathBack);
		idcard.setNo(idCard);
		idcard.setAuthStatus(AuthStatus.wait);
		idcard.setName(name);
		idcardService.save(idcard);
//		try {
//			member.setName(URLDecoder.decode(name, "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		member.setIdcard(idcard);
		memberService.save(member);
		return Message.success("修改成功");
	}

	/**
	 * 上传头像
	 */
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public @ResponseBody Message uploadImg(String username, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		// Member member = memberService.findByUsername(username);
		// 图片
		Member member = memberService.getCurrent();
		try {
			Setting setting = SettingUtils.get();
			String imgUrl = FileUtil.uploadImage_phone(request);
			imgUrl = imgUrl.replaceAll("\\\\", "/");
			imgUrl = setting.getSiteUrl() + "/" + imgUrl;
			member.setHeadImg(imgUrl);
			memberService.save(member);
		} catch (Exception e) {
			addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
			return ERROR_MESSAGE;
		}
		return Message.success("修改成功");
	}

	/**
	 * 修改手机号码
	 */
	@RequestMapping(value = "/mobileUpdate", method = RequestMethod.POST)
	public @ResponseBody Message mobileUpdate(String username, String mobile, HttpServletRequest request) {
		// Member member = memberService.findByUsername(username);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户不存在");
		}
		member.setMobile(mobile);
		memberService.update(member);
		return Message.success("修改成功");
	}

	/**
	 * 修改电子邮箱
	 */
	@RequestMapping(value = "/emailUpdate", method = RequestMethod.POST)
	public @ResponseBody Message emailUpdate(String username, String email, HttpServletRequest request) {
		// Member member = memberService.findByUsername(username);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户不存在");
		}
		member.setEmail(email);
		memberService.update(member);
		return Message.success("修改成功");
	}

	/**
	 * 修改地址
	 */
	@RequestMapping(value = "/userAddressUpdate", method = RequestMethod.POST)
	public @ResponseBody Message userAddressUpdate(String username, String address, HttpServletRequest request) {
		// Member member = memberService.findByUsername(username);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户不存在");
		}
		try {
			member.setAddress(URLDecoder.decode(address, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		memberService.update(member);
		return Message.success("修改成功");
	}

	/**
	 * 修改真实姓名
	 */
	@RequestMapping(value = "/readyNameUpdate", method = RequestMethod.POST)
	public @ResponseBody Message readyNameUpdate(String username, String name, HttpServletRequest request) {
		// Member member = memberService.findByUsername(username);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户不存在");
		}
		try {
			member.setName(URLDecoder.decode(name, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		memberService.update(member);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 绑定手机
	 */
	@RequestMapping(value = "/bindMobile", method = RequestMethod.POST)
	public @ResponseBody Message bindMobile(String mobile, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户不存在");
		}
		member.setMobile(mobile);
		if (member.getBindMobile() == BindStatus.binded) {
			member.setBindMobile(BindStatus.unbind);
		} else {
			member.setBindMobile(BindStatus.binded);
		}
		memberService.update(member);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 绑定邮箱
	 */
	@RequestMapping(value = "/bindEmail", method = RequestMethod.POST)
	public @ResponseBody Message bindEmail(String email, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户不存在");
		}
		member.setEmail(email);
		if (member.getBindEmail() == BindStatus.binded) {
			member.setBindEmail(BindStatus.unbind);
		} else {
			member.setBindEmail(BindStatus.binded);
		}
		memberService.update(member);
		return SUCCESS_MESSAGE;
	}
}