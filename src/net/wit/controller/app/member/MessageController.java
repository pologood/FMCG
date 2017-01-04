/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.member;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.MessageModel;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.Message;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.CreditService;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import net.wit.service.OrderService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;
import net.wit.util.SettingUtils;

/**
 * Controller - 我的消息
 * @author rsico Team
 * @version 3.0
 */
@Controller("appMemberMessageController")
@RequestMapping("/app/member/message")
public class MessageController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	/**
	 * 消息列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Message.Type type,Pageable pageable, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if(type!=null){
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq,type));
			pageable.setFilters(filters);
		}
		Page<Message> page = messageService.findPage(member, pageable);
		for (Message message:page.getContent()) {
			message.setReceiverRead(true);
			messageService.update(message);
		}
		return DataBlock.success(MessageModel.bindData(page.getContent()), "执行成功");
	}

	/**
	 * 通过用户获取头像
	 */
	@RequestMapping(value = "/heads", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock heads(String [] users,HttpServletRequest request) {
        Map<String,String> data = new HashMap<String,String>();
		for (String user:users) {
		   Member member = memberService.findByUsername(user);
		   data.put(user, member.getHeadImg());
		}
		return DataBlock.success(data, "执行成功");
	}
	
	/**
	 * 未读消息
	 * type=1为分组消息
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock count(String type) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Object data;
		if (type!=null&&type.equals("1")) {
			Map<String, Object> map = new HashMap<>();
			map.put("order", messageService.count(member, false, Message.Type.order));
			map.put("account", messageService.count(member, false, Message.Type.account));
			map.put("message", messageService.count(member, false, Message.Type.message));
			Pageable pageable=new Pageable(1,1);
			Page<Message> page;

			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq, Message.Type.order));
			pageable.setFilters(filters);
			page=messageService.findPage(member,pageable);
			MessageModel model=new MessageModel();
			if(page.getContent().size()==0){
				model=null;
			}else{
				model.copyFrom(page.getContent().get(0));
			}
			map.put("firstOrder", model);

			filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq, Message.Type.account));
			pageable.setFilters(filters);
			page=messageService.findPage(member,pageable);
			model=new MessageModel();
			if(page.getContent().size()==0){
				model=null;
			}else{
				model.copyFrom(page.getContent().get(0));
			}
			map.put("firstAccount", model);

			filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq, Message.Type.message));
			pageable.setFilters(filters);
			page=messageService.findPage(member,pageable);
			model=new MessageModel();
			if(page.getContent().size()==0){
				model=null;
			}else{
				model.copyFrom(page.getContent().get(0));
			}
			map.put("firstMessage", model);
			data = map;
		} else {
			data = messageService.count(member, false);
		}

		return DataBlock.success(data, "执行成功");
	}
	
	/**
	 * 清除消息
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock clear(Message.Type type,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		List<Message> msgs = messageService.findList(member,null,null,null,type);
		for (Message message:msgs) {
			messageService.delete(message);
		}
		
		return DataBlock.success("success", "执行成功");
	}
	
	/**
	 * 标记已读
	 */
	@RequestMapping(value = "/tag", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock tag(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Message message = messageService.find(id);
		message.setReceiverRead(true);
		messageService.update(message);
		return DataBlock.success("success", "执行成功");
	}

	/**
	 * 标记已读
	 */
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock tag(Long[] ids,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		for(Long id : ids){
			Message message = messageService.find(id);
			message.setReceiverRead(true);
			messageService.update(message);
		}
		return DataBlock.success("success", "执行成功");
	}
}