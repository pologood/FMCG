/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Receiver;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.service.ReceiverService;

/**
 * Controller - 会员中心 - 收货地址
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberReceiverController")
@RequestMapping("/b2b/member/receiver")
public class ReceiverController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("member", member);
		model.addAttribute("page", receiverService.findPage(member, pageable));
		return "b2b/member/receiver/list";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			addFlashMessage(redirectAttributes, Message.warn("shop.member.receiver.addCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
			return "redirect:list.jhtml";
		}
		return "b2b/member/receiver/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Message save(Receiver receiver, Long areaId, RedirectAttributes redirectAttributes) {
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			return Message.error("验证失败");
		}
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return Message.error("您的地址已达到上限");
		}
		receiver.setMember(member);
		receiverService.save(receiver);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return Message.success("添加地址成功");
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			return ERROR_VIEW;
		}
		model.addAttribute("member", member);
		model.addAttribute("receiver", receiver);
		return "b2b/member/receiver/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Receiver receiver, Long id, Long areaId, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member==null) {
			Message.error("当前会员不存在");
		}
		receiver.setArea(areaService.find(areaId));
		Receiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			Message.error("地址不存在");
		}
		receiver.setMember(member);
		pReceiver.setAddress(receiver.getAddress());
		pReceiver.setArea(receiver.getArea());
		pReceiver.setAreaName(receiver.getArea().getFullName());
		pReceiver.setCommunity(receiver.getCommunity());
		pReceiver.setConsignee(receiver.getConsignee());
		pReceiver.setIsDefault(receiver.getIsDefault());
		pReceiver.setPhone(receiver.getPhone());
		pReceiver.setZipCode(receiver.getZipCode());
		pReceiver.setLocation(receiver.getLocation());
		pReceiver.setZipCode(receiver.getZipCode());
		pReceiver.setMember(receiver.getMember());
		pReceiver.setIsDefault(receiver.getIsDefault());
		if (!isValid(pReceiver)) {
			 return Message.error("验证无效");
		}
		receiverService.update(pReceiver);
		return Message.success("修改成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			return ERROR_MESSAGE;
		}
		receiverService.delete(id);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 设置默认地址
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/setDefault", method = RequestMethod.POST)
	public @ResponseBody Message setDefault(Long id) {
		Receiver receiver = receiverService.find(id);
		Member member = memberService.getCurrent();
		for (Receiver receiver2 : member.getReceivers()) {
			if (receiver.getId() != receiver2.getId()) {
				receiver2.setIsDefault(false);
				receiverService.update(receiver2);
			}
		}
		receiver.setIsDefault(true);
		receiverService.update(receiver);
		return SUCCESS_MESSAGE;
	}

}