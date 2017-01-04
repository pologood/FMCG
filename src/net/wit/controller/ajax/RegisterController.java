/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.entity.SmsSend;
import net.wit.entity.SmsSend.Type;
import net.wit.service.AreaService;
import net.wit.service.CaptchaService;
import net.wit.service.CartService;
import net.wit.service.MemberAttributeService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.service.TenantCategoryService;
import net.wit.service.TenantService;
import net.wit.uic.api.UICService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员注册
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxRegisterController")
@RequestMapping("/ajax/register")
public class RegisterController extends BaseController {

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	@Resource(name = "uicService")
	private UICService uicService;
	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;
	
	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
	
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	public @ResponseBody
	Message sendMobile(String mobile,HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		return Message.error("接口禁用了");
		//return uicService.sendAuthcodeByTel(mobile,null, request);
	}
	/**
	 * 注册提交
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	Message register(String mobile,String securityCode,Long areaId,HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Member agent = memberService.getCurrent();
		if (agent == null) {
			return Message.error("您的会话已经失效，请重新登录");
		}

	    String password = mobile.substring(mobile.length()-6);
	    
		Member.RegType regType = Member.RegType.mobile;
		
		Area area = null;
		if (areaId!=null) {
			area = areaService.find(areaId);
		} else {
			area = agent.getArea();
		}

		Message msg = uicService.addMember(mobile, password, securityCode, area.getId(), regType, agent.getUsername(), request, response);
		if (msg.getType().equals(Message.Type.success)) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(mobile);
			smsSend.setContent("您的账号注册成功,账号:" + mobile + ",初始密码:" + password + ",请登录修改密码。【" + bundle.getString("signature") + "】");
			smsSend.setType(Type.captcha);
			smsSendService.smsSend(smsSend);
		}
		return msg;
 	}

}