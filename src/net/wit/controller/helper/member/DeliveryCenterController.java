/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Ad.LinkType;
import net.wit.util.LBSUtil;

/**
 * Controller - 发货点
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberDeliveryCenterController")
@RequestMapping("/helper/member/delivery_center")
public class DeliveryCenterController extends BaseController {

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "qrcodeServiceImpl")
	private QrcodeService qrcodeService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	/**
	 * 获取社区信息
	 */
	@RequestMapping(value = "/get_community", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String,Object>> getCommunity(Long areaId) {
		List<Map<String, Object>> data = new ArrayList<>();
		Area area = areaService.find(areaId);
		List<Community> communitys = communityService.findList(area);
		for (Community community : communitys) {
			Map<String,Object> map=new HashMap<>();
			map.put("id",community.getId());
			map.put("name",community.getName());
			map.put("location",community.getLocation());
			data.add(map);
		}
		return data;
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		Member member=memberService.getCurrent();
		List<DeliveryCenter> deliveryCenters=deliveryCenterService.findMyAll(member);
		model.addAttribute("deliveryCenters",deliveryCenters);
		model.addAttribute("pageActive",2);
		model.addAttribute("member",member);
		return "/helper/member/delivery_center/add";
	}

	/**
	 * 检查门店是否存在
	 * @param name	门店名称
	 * @return	门店是否存在
     */
	@RequestMapping(value = "/checkName", method = RequestMethod.POST)
	public @ResponseBody Boolean checkName(String name){
		List<Filter> filters=new ArrayList<>();
		filters.add(new Filter("name", Filter.Operator.eq,name));
		List<DeliveryCenter> deliveryCenters = deliveryCenterService.findList(null,filters,null);
		return deliveryCenters.size()==0;
	}

	/** 
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(DeliveryCenter deliveryCenter, Long areaId, String lat, String lng, Model model, RedirectAttributes redirectAttributes) {
		deliveryCenter.setArea(areaService.find(areaId));
		if (!isValid(deliveryCenter)) {
			return ERROR_VIEW;
		}
		if(StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng)){
			BigDecimal blat = new BigDecimal(lat);
			BigDecimal blng = new BigDecimal(lng);
			Location location = new Location(blat,blng);
			deliveryCenter.setLocation(location);
		}
		
		deliveryCenter.setLocation(LBSUtil.bd_decrypt(deliveryCenter.getLocation()));
		deliveryCenter.setAreaName(null);
		deliveryCenter.setScore(0F);
		deliveryCenter.setTotalScore(0L);
		deliveryCenter.setScoreCount(0L);
		Member member = memberService.getCurrent();
		deliveryCenter.setTenant(member.getTenant());
		deliveryCenterService.save(deliveryCenter);

	    DeliveryCenter def = deliveryCenter.getTenant().getDefaultDeliveryCenter();
	    if (def!=null) {
	    	Tenant tenant = def.getTenant();
	    	tenant.setArea(def.getArea());
	    	tenant.setAddress(def.getAddress());
	    	tenantService.save(tenant);
	    }
		activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(9L));
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, Model model) {
		model.addAttribute("deliveryCenter", deliveryCenterService.find(id));
		model.addAttribute("pageActive",2);
		return "/helper/member/delivery_center/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(DeliveryCenter deliveryCenter, Long areaId, String lat, String lng, RedirectAttributes redirectAttributes) {
		if(StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng)){
			BigDecimal blat = new BigDecimal(lat);
			BigDecimal blng = new BigDecimal(lng);
			Location location = new Location(blat,blng);
			deliveryCenter.setLocation(location);
		}
		deliveryCenter.setArea(areaService.find(areaId));
		if (!isValid(deliveryCenter)) {
			return ERROR_VIEW;
		}
		Member member = memberService.getCurrent();
		deliveryCenter.setTenant(member.getTenant());
		
		DeliveryCenter tmpDelivery = deliveryCenterService.find(deliveryCenter.getId());
		
//		if (deliveryCenter.getLocation()!=null) {
//			if (tmpDelivery.getLocation()==null || !tmpDelivery.getLocation().equals(deliveryCenter.getLocation()))
//			{
//				deliveryCenter.setLocation(LBSUtil.bd_decrypt(deliveryCenter.getLocation()));
//			}
//		}
		deliveryCenter.setScore(tmpDelivery.getScore());
		deliveryCenter.setTotalScore(tmpDelivery.getTotalScore());
		deliveryCenter.setScoreCount(tmpDelivery.getScoreCount());
		
		deliveryCenterService.update(deliveryCenter, "areaName");
		
	    
	    DeliveryCenter def = deliveryCenter.getTenant().getDefaultDeliveryCenter();
	    if (def!=null) {
	    	Tenant tenant = def.getTenant();
	    	tenant.setArea(def.getArea());
	    	tenant.setAddress(def.getAddress());
	    	tenantService.save(tenant);
	    }
		
		
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, Pageable pageable) {
		if  (memberService.getCurrent().getTenant()==null) {
	    	return "redirect:/member/tenant/add.jhtml";
		}
		Member member = memberService.getCurrent();
		model.addAttribute("page", deliveryCenterService.findPage(member,null,pageable));
		model.addAttribute("linkTypes", LinkType.values());
		model.addAttribute("pageActive",2);
		model.addAttribute("member",member);
		Qrcode qrcode = qrcodeService.findbyTenant(member.getTenant());
		String ticket="";
		if(qrcode!=null){
			ticket=qrcode.getTicket();
		}
		model.addAttribute("ticket",ticket);
		return "/helper/member/delivery_center/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		for(int i=0;i<ids.length;i++){
			DeliveryCenter deliveryCenter = deliveryCenterService.find(ids[i]);
			if (deliveryCenter==null) {
				return Message.error("无效的门店ID");
			}
			if(deliveryCenter.getIsDefault()){
				return Message.error("不能删除默认发货地址");
			}
			if (deliveryCenter.getEmployees().size()>0) {
				return Message.error("有员工的门店不能删除。");
			}
		}
		deliveryCenterService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}