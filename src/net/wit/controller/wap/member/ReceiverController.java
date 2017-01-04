/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.wap.BaseController;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.entity.Receiver;
import net.wit.entity.Tag;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.service.ReceiverService;
import net.wit.service.TagService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 收货地址
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapReceiverController")
@RequestMapping("/wap/member/receiver")
public class ReceiverController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

//	/**
//	 * 列表
//	 */
//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	public String list(Integer pageNumber, String fromCart, ModelMap model) {
//		Member member = memberService.getCurrent();
//		String[] phonetics = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
//		model.addAttribute("receivers", member.getReceivers());
//		model.addAttribute("phonetics", phonetics);
//		model.addAttribute("areas", areaService.findRoots());
//		model.addAttribute("area", areaService.getCurrent());
//		model.addAttribute("fromCart", fromCart);
//		if (StringUtils.isNotBlank(fromCart)) {
//			List<Filter> filters = new ArrayList<Filter>();
//			List<Order> orders = new ArrayList<Order>();
//			List<Tag> tags = new ArrayList<Tag>();
//			tags.add(tagService.find(23l));
//			List<Receiver> collectings = receiverService.findCollecting(areaService.getCurrent(), tags, filters, orders);
//			model.addAttribute("collectings", collectings);
//		}
//		return "wap/member/receiver/list";
//	}


	/**
	 * 列表
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Receiver get(@PathVariable Long id) {
		return receiverService.find(id);
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(RedirectAttributes redirectAttributes, String fromCart, ModelMap model) {
		Member member = memberService.getCurrent();
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			addFlashMessage(redirectAttributes, Message.warn("shop.member.receiver.addCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
			return "redirect:address_list.jhtml?fromCart=" + fromCart;
		}

		if (StringUtils.isNotEmpty(fromCart)) {
			model.addAttribute("fromCart", fromCart);
		}
		return "wap/address/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Message save(Receiver receiver, Long areaId, String fromCart, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		receiver.setMember(member);
		Area area2 = areaService.find(areaId);
		if (area2 == null) {
			Area area = member.getArea();
			receiver.setArea(area);
		} else {
			receiver.setArea(area2);
		}
		if (receiver.getArea() != null) {
				receiver.setZipCode(receiver.getArea().getZipCode());
		}
		if(receiver.getZipCode()==null){
			receiver.setZipCode("000000");
		}
		if (member.getReceivers().size() == 0) {
			receiver.setIsDefault(true);
		}
		if (!isValid(receiver)) {
			return Message.error("地址不合法");
		}
		if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
			return Message.error("最多只能添加" + Receiver.MAX_RECEIVER_COUNT + "个");
		}
		receiver.setMember(member);
		receiverService.save(receiver);
		if (StringUtils.isEmpty(fromCart)) {
			return Message.success("新增成功");
		}
		return Message.success(fromCart);
	}


	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model, Boolean fromCart, RedirectAttributes redirectAttributes) {
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			return ERROR_VIEW;
		}
		Area area = receiver.getArea();
		// 区的父父节点，省
		if (area.getParent().getParent() != null) {
			model.addAttribute("province", area.getParent().getParent());
			model.addAttribute("city", area.getParent());
			model.addAttribute("district", area);
		}
		// 市的父节点，省
		if (area.getParent() != null && area.getParent().getParent() == null) {
			model.addAttribute("province", area.getParent());
			model.addAttribute("city", area);
			model.addAttribute("district", "");
		}
		model.addAttribute("fromCart", fromCart);
		model.addAttribute("receiver", receiver);
		return "wap/address/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Receiver receiver, Long id, Long areaId) {
		Receiver pReceiver = receiverService.find(id);
		if (pReceiver == null) {
			return Message.error("shop.address.failure");
		}
		// receiver.setZipCode(pReceiver.getZipCode());
		pReceiver.setIsDefault(receiver.getIsDefault());
		Area area = areaService.find(areaId);
		receiver.setArea(area);
		pReceiver.setArea(area);
		pReceiver.setPhone(receiver.getPhone());
		pReceiver.setAddress(receiver.getAddress());
		pReceiver.setConsignee(receiver.getConsignee());
		pReceiver.setZipCode(receiver.getZipCode());
		Member member = memberService.getCurrent();
		if (!member.equals(pReceiver.getMember())) {
			return Message.error("shop.address.failure");
		}
		receiverService.save(pReceiver);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(String idsStr) {
		String[] ids = idsStr.split("-");
		for (int i = 0; i < ids.length; i++) {
			Long id = Long.parseLong(ids[i]);
			Receiver receiver = receiverService.find(id);
			if (receiver == null) {
				return ERROR_MESSAGE;
			}
			Member member = memberService.getCurrent();
			if (!member.equals(receiver.getMember())) {
				return ERROR_MESSAGE;
			}
			receiverService.delete(id);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 删除
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

	/**
	 * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(String fromCart,String backUrl,String addReceiver, ModelMap model) {
		if(StringUtils.isNotBlank(fromCart)){
			model.addAttribute("fromCart",true);
		}
		model.addAttribute("backUrl",backUrl);
		model.addAttribute("addReceiver",addReceiver);
		Receiver receiver = receiverService.findDefault(memberService.getCurrent());
		model.addAttribute("receiverId",receiver==null?"":receiver.getId());
		return "wap/member/receiver/list";
	}

	/**
	 * 编辑
     */
	@RequestMapping(value = "/editReceiver", method = RequestMethod.GET)
	public String editReceiver(Long id, ModelMap model){
		model.addAttribute("id",id);
		Receiver receiver = receiverService.find(id);
		if (receiver == null) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		if (!member.equals(receiver.getMember())) {
			return ERROR_VIEW;
		}
		Area area = receiver.getArea();
		model.addAttribute("area", area);
		model.addAttribute("receiver", receiver);
		return "wap/member/receiver/edit";
	}
	/**
	 * 编辑
     */
	@RequestMapping(value = "/getReceiver", method = RequestMethod.GET)
	public @ResponseBody Receiver getReceiver(Long id){
		Receiver receiver = receiverService.find(id);
		return receiver;
	}

	/**
	 * 添加
     */
	@RequestMapping(value = "addReceiver", method = RequestMethod.GET)
	public String addReceiver(ModelMap model){
		model.addAttribute("area", areaService.getCurrent());
		return "wap/member/receiver/add";
	}
}