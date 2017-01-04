/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.math.BigDecimal;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Member;
import net.wit.service.DepositService;
import net.wit.service.MemberService;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 预存款
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxDepositController")
@RequestMapping("/ajax/deposit")
public class DepositController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	/**
	 * 划款
	 */
	@RequestMapping(value = "/recharge", method = RequestMethod.GET)
	@ResponseBody
	public Message recharge(BigDecimal amount, String username, String memo,String operator, String sign) {
		
	   Member member = memberService.findByUsername(username);
	   if (member==null) {
		   return Message.error("无效用户");
	   }
	   
	   ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
	   String mySign = DigestUtils.md5Hex(amount.toString()+username+memo+operator+bundle.getString("appKey"));
	   try {
	   if (mySign.equals(sign)) {
		   memberService.Recharge(member,0, amount, memo, null);
		   return Message.success("划款成功");
	   } else {
		   return Message.error("无效密钥");
	   }
		
	   } catch (Exception e) {
		   return Message.error("未知异常");
	   }
	}


}