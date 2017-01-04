/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.service.CommunityService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.MemberService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 发货点
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberDeliveryCenterController")
@RequestMapping("/b2b/member/delivery_center")
public class DeliveryCenterController extends BaseController {

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 获取社区信息
	 */
	@RequestMapping(value = "/get_community", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> getCommunity(Long areaId) {
		Map<Long, String> data = new HashMap<Long, String>();
		Area area = areaService.find(areaId);
		List<Community> communitys = communityService.findList(area);
		for (Community community : communitys) {
			data.put(community.getId(), community.getName());
		}
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		if (memberService.getCurrent().getTenant() == null) {
			return "redirect:b2b/member/tenant/add.jhtml";
		}
		return "b2b/member/delivery_center/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(DeliveryCenter deliveryCenter, Long areaId, String locationX, String locationY, Model model, RedirectAttributes redirectAttributes) {
		deliveryCenter.setArea(areaService.find(areaId));
		if (!isValid(deliveryCenter)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(locationY) && StringUtils.isNotEmpty(locationX)) {
			BigDecimal x = new BigDecimal(locationX);
			BigDecimal y = new BigDecimal(locationY);
			Location location = new Location(x, y);
			deliveryCenter.setLocation(location);
		}
		deliveryCenter.setAreaName(null);
		Member member = memberService.getCurrent();
		deliveryCenter.setTenant(member.getTenant());
		deliveryCenterService.save(deliveryCenter);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, Model model) {
		Member member = memberService.getCurrent();
		if (member.getTenant() == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("member", member);
		model.addAttribute("deliveryCenter", deliveryCenterService.find(id));
		return "b2b/member/delivery_center/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(DeliveryCenter deliveryCenter, Long areaId, String locationX, String locationY, RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotEmpty(locationY) && StringUtils.isNotEmpty(locationX)) {
			BigDecimal x = new BigDecimal(locationX);
			BigDecimal y = new BigDecimal(locationY);
			Location location = new Location(x, y);
			deliveryCenter.setLocation(location);
		}
		deliveryCenter.setArea(areaService.find(areaId));
		if (!isValid(deliveryCenter)) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		deliveryCenter.setTenant(member.getTenant());
		deliveryCenterService.update(deliveryCenter, "areaName");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, Pageable pageable) {
		if (memberService.getCurrent().getTenant() == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("page", deliveryCenterService.findPage(member, null, pageable));
		return "b2b/member/delivery_center/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		deliveryCenterService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}