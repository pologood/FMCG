/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.MessageModel;
import net.wit.entity.Contact;
import net.wit.entity.Member;
import net.wit.entity.Message;
import net.wit.entity.Message.Type;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;

/**
 * Controller - 条码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("posMessageController")
@RequestMapping("/pos/message")
public class MessageController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 消息列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long tenantId, String key, Type type, Pageable pageable,HttpServletRequest request) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		if (type!=null) {
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(new Filter("type", Operator.eq, type));
			pageable.setFilters(filters);
		}
		Page<net.wit.entity.Message> page = messageService.findPage(tenant.getMember(), pageable);
		return DataBlock.success(MessageModel.bindData(page.getContent()), "执行成功");
	}

	/**
	 * 未读消息
	 */
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock count(Long tenantId, String key, HttpServletRequest request) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		
		return DataBlock.success(messageService.count(tenant.getMember(), false), "执行成功");
	}
	
	/**
	 * 清除消息
	 */
	@RequestMapping(value = "/clear", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock clear(Long tenantId, String key,HttpServletRequest request) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		
		List<Message> msgs = messageService.findList(tenant.getMember(),null,null,null);
		for (Message message:msgs) {
			messageService.delete(message);
		}
		
		return DataBlock.success("success", "执行成功");
	}
	
	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public @ResponseBody DataBlock reply(Long tenantId, String key,Long id, String content, HttpServletRequest request) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		if (content==null) {
			return DataBlock.error("无效回复");
		}
		Message message = messageService.find(id);
		if (message == null) {
			return DataBlock.error("无效编号");
		}
		if (!message.getType().equals(Message.Type.consultation)) {
			return DataBlock.error("只有咨询问题，才能回复。");
		}
		Message replyMessage = new Message();
		replyMessage.setReceiver(message.getSender());
		replyMessage.setSender(message.getReceiver());
		replyMessage.setContent(content);
		replyMessage.setForMessage(message);
		replyMessage.setType(message.getType());
		replyMessage.setIsDraft(false);
		replyMessage.setSenderRead(false);
		replyMessage.setReceiverRead(false);
		replyMessage.setReceiverDelete(false);
		replyMessage.setSenderDelete(false);
		messageService.save(replyMessage);
		return DataBlock.success("success", "成功回复");
	}

	/**
	 * 标记已读
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock delete(Long tenantId, String key,Long id,HttpServletRequest request) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		messageService.delete(id);
		return DataBlock.success("success", "执行成功");
	}

	/**
	 * 标记已读
	 */
	@RequestMapping(value = "/tag", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock tag(Long tenantId, String key,Long id,HttpServletRequest request) {
		if (tenantId == null) {
			return DataBlock.error("商家信息标识为空!"); 
		}
			Tenant tenant = tenantService.find(tenantId);
			if (tenant == null) {
				return DataBlock.error("商家信息为空!");
			}
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Message message = messageService.find(id);
		message.setReceiverRead(true);
		messageService.update(message);
		return DataBlock.success("success", "执行成功");
	}

}