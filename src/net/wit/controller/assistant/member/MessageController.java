/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.*;
import net.wit.entity.Article;
import net.wit.entity.Member;
import net.wit.entity.Message;
import net.wit.service.ArticleService;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 我的消息
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberMessageController")
@RequestMapping("/assistant/member/message")
public class MessageController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

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
			filters.add(new Filter("way", Operator.ne,Message.Way.member));
			pageable.setFilters(filters);
		}
		Page<Message> page = messageService.findPage(member, pageable);
		List<MessageModel> modelList = MessageModel.bindData(page.getContent());
		for (Message message:page.getContent()) {
			message.setReceiverRead(true);
			messageService.update(message);
		}
		return DataBlock.success(modelList,page, "执行成功");
	}
	/**
	 * 订单列表
	 */
	@RequestMapping(value = "/orderList", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock orderList(Message.Type type,Pageable pageable, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if(type!=null){
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq,type));
			filters.add(new Filter("way", Operator.ne,Message.Way.member));
			pageable.setFilters(filters);
		}
		Page<Message> page = messageService.findPage(member, pageable);
		List<OrderMessageModel> modelList = OrderMessageModel.bindData(page.getContent());
		for (Message message:page.getContent()) {
			message.setReceiverRead(true);
			messageService.update(message);
		}
		return DataBlock.success(modelList,page, "执行成功");
	}
	/**
	 * 评价提醒列表
	 */
	@RequestMapping(value = "/reviewList", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock accountList(Message.Type type,Pageable pageable, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if(type!=null){
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq,type));
			filters.add(new Filter("way", Operator.ne,Message.Way.member));
			pageable.setFilters(filters);
		}
		Page<Message> page = messageService.findPage(member, pageable);
		List<ReviewMessageModel> modelList = ReviewMessageModel.bindData(page.getContent());
		for (Message message:page.getContent()) {
			message.setReceiverRead(true);
			messageService.update(message);
		}
		return DataBlock.success(modelList,page, "执行成功");
	}
	/**
	 * 我要赚钱列表
	 */
	@RequestMapping(value = "/activityList", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock activityList(Message.Type type,Pageable pageable, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if(type!=null){
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq,type));
			filters.add(new Filter("way", Operator.ne,Message.Way.member));
			pageable.setFilters(filters);
		}
		Page<Message> page = messageService.findPage(member, pageable);
		List<ActivityMessageModel> modelList = ActivityMessageModel.bindData(page.getContent());
		for (Message message:page.getContent()) {
			message.setReceiverRead(true);
			messageService.update(message);
		}
		return DataBlock.success(modelList,page, "执行成功");
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
	public DataBlock count() {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Object data;
			Map<String, Object> map = new HashMap<>();
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("way", Operator.ne,Message.Way.member));

			map.put("order", messageService.count(member, false, Message.Type.order,filters));
			map.put("account", messageService.count(member, false, Message.Type.account,filters));
			map.put("message", messageService.count(member, false, Message.Type.message,filters));
		    map.put("review",messageService.count(member, false, Message.Type.review,filters));
		    map.put("activity",messageService.count(member, false, Message.Type.activity,filters));

			Pageable pageable=new Pageable(1,1);
			Page<Message> page;

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
		    filters.add(new Filter("way", Operator.ne,Message.Way.member));
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
	      	filters.add(new Filter("way", Operator.ne,Message.Way.member));
			pageable.setFilters(filters);
			page=messageService.findPage(member,pageable);
			model=new MessageModel();
			if(page.getContent().size()==0){
				model=null;
			}else{
				model.copyFrom(page.getContent().get(0));
			}
			map.put("firstMessage", model);

			filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq, Message.Type.review));
		    filters.add(new Filter("way", Operator.ne,Message.Way.member));
			pageable.setFilters(filters);
			page=messageService.findPage(member,pageable);
			model=new MessageModel();
			if(page.getContent().size()==0){
				model=null;
			}else{
				model.copyFrom(page.getContent().get(0));
			}
			map.put("firstReview", model);
			filters=new ArrayList<>();
			filters.add(new Filter("type", Operator.eq, Message.Type.activity));
		    filters.add(new Filter("way", Operator.ne,Message.Way.member));
			pageable.setFilters(filters);
			page=messageService.findPage(member,pageable);
			model=new MessageModel();
			if(page.getContent().size()==0){
				model=null;
			}else{
				model.copyFrom(page.getContent().get(0));
			}
			map.put("firstActivity", model);
			data = map;


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

	/**
	 * 系统消息详情页
	 * @param id
     * @return
     */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content( Long id, ModelMap model) {
		Article article = articleService.find(id);
		if (article == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("article", article);
		return "assistant/content";
	}
}