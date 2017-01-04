/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Setting;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.service.MemberService;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 绑定email
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cBindMailController")
@RequestMapping("/b2c/bind_email")
public class BindMailController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	/**
	 * 绑定mail
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String bindMail(String redirectUrl, String security,String username, HttpServletRequest request, ModelMap model) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return ERROR_VIEW;
		}
		SafeKey safeKey = member.getSafeKey();
		if (safeKey == null || safeKey.getValue() == null || !safeKey.getValue().equals(security)) {
			return ERROR_VIEW;
		}
		if (safeKey.hasExpired()) {
			return ERROR_VIEW;
		}
		safeKey.setExpire(new Date());
		safeKey.setValue(null);
		if (member.getBindEmail()==BindStatus.binded) {
			member.setBindEmail(BindStatus.unbind);
		} else {
			member.setBindEmail(BindStatus.binded);
		}
		memberService.update(member);
		Setting setting = SettingUtils.get();
		if (redirectUrl != null && !redirectUrl.equalsIgnoreCase(setting.getSiteUrl()) && !redirectUrl.startsWith(request.getContextPath() + "/") && !redirectUrl.startsWith(setting.getSiteUrl() + "/")) {
			redirectUrl = null;
		}
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "redirect:../b2c/member/index.jhtml";
	}

}