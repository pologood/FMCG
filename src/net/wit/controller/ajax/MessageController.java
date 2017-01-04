/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.entity.Member;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import net.wit.support.MessageModel;
import net.wit.support.PushMessage;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 手机闪屏
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxMessageController")
@RequestMapping("/ajax/message")
public class MessageController extends BaseController {
	
	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@RequestMapping(value = "/get_message_count", method = RequestMethod.GET)
	@ResponseBody
	public Message getMessageCount(String username,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.findByUsername(username);
		data.put("count", messageService.count(member, false));
		return Message.success(JsonUtils.toJson(data));
	}
		
}