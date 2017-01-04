/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.Member;
import net.wit.service.MemberService;
import net.wit.util.JsonUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 条码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("posMemberController")
@RequestMapping("/pos/member")
public class MemberController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 扫码
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody
	Message get(String mobile,String key) {
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(mobile+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return Message.error("无效校验码");
		}
		Member member = memberService.findByUsername(mobile);
		if (member == null) {
			member = memberService.findByBindTel(mobile);
			if (member == null) {
				return Message.error("当前手机没有绑定会员");
			}
		}
		String jsonString = JsonUtils.toJson(member);
		return Message.success(jsonString);
	
	}
}