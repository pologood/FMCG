/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax.member;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.entity.Receiver;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.service.ReceiverService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 收货地址
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxMemberReceiverController")
@RequestMapping("/ajax/member/receiver")
public class ReceiverController extends BaseController {
	
	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	/**
	 * 获取收货地址
	 */
	@RequestMapping(value = "/getReceivers", method = RequestMethod.GET)
	@ResponseBody
	public Message getReceivers() {
		Member member = memberService.getCurrent();
		if(member==null){
			return Message.error("ajax.member.notLogin");
		}
		Set<Receiver> receivers  = member.getReceivers();
		return Message.success(JsonUtils.toJson(receivers));
	}
	/**
	 * 保存收货地址
	 */
	@RequestMapping(value = "/save_receiver", method = RequestMethod.POST)
	@ResponseBody
	public Message saveReceiver(Receiver receiver, Long areaId,HttpServletRequest request) {
		Area area = areaService.find(areaId);
		if(area==null){
			return Message.error("ajax.area.NotExist");
		}
		receiver.setArea(area);
		if (!isValid(receiver)) {
			return Message.error("ajax.order.receiver.isValid");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("ajax.member.notLogin");
			
		}
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return Message.error("shop.order.addReceiverCountNotAllowed", Receiver.MAX_RECEIVER_COUNT);
		}
		receiver.setMember(member);
		receiverService.save(receiver);
		return Message.success("ajax.message.success");
	}
	
	/**
	 * 修改收货地址
	 */
	@RequestMapping(value = "/update_receiver", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Receiver receiver, Long id, Long areaId) {
		receiver.setArea(areaService.find(areaId));
		if (!isValid(receiver)) {
			return Message.error("ajax.order.receiver.isValid");
		}
		Receiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			return Message.error("ajax.order.receiver.isValid");
		}
		Member member = memberService.getCurrent();
		if (!member.equals(pReceiver.getMember())) {
			return Message.error("ajax.member.login.error");
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
		receiverService.update(receiver, "member");
		return Message.success("ajax.message.success");
	}
	
	/**
	 * 删除收货地址
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return Message.error("ajax.order.receiver.isValid");
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			return Message.error("ajax.member.login.error");
		}
		receiverService.delete(id);
		return Message.success("ajax.message.success");
	}

}