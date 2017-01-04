/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.service.AreaService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.TenantService;

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
@Controller("deliveryCenterController")
@RequestMapping("/admin/delivery_center")
public class DeliveryCenterController extends BaseController {

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return "/admin/delivery_center/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(DeliveryCenter deliveryCenter, Long areaId, Model model, String lat, String lng, RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng)) {
			BigDecimal blat = new BigDecimal(lat);
			BigDecimal blng = new BigDecimal(lng);
			Location location = new Location(blat,blng);
			deliveryCenter.setLocation(location);
		}
		deliveryCenter.setArea(areaService.find(areaId));
		if (!isValid(deliveryCenter)) {
			return ERROR_VIEW;
		}
		deliveryCenter.setAreaName(null);
		deliveryCenter.setTenant(tenantService.find(Long.parseLong("1")));
		deliveryCenterService.save(deliveryCenter);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, Model model) {
		model.addAttribute("deliveryCenter", deliveryCenterService.find(id));
		return "/admin/delivery_center/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(DeliveryCenter deliveryCenter, Long areaId, String lat, String lng, RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng)) {
			BigDecimal blat = new BigDecimal(lat);
			BigDecimal blng = new BigDecimal(lng);
			Location location = new Location(blat,blng);
			deliveryCenter.setLocation(location);
		}
		deliveryCenter.setArea(areaService.find(areaId));
		if (!isValid(deliveryCenter)) {
			return ERROR_VIEW;
		}
		DeliveryCenter dc = deliveryCenterService.find(deliveryCenter.getId());
		deliveryCenter.setTenant(dc.getTenant());
		deliveryCenterService.update(deliveryCenter, "areaName");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, Pageable pageable) {
		model.addAttribute("page", deliveryCenterService.findPage(pageable));
		return "/admin/delivery_center/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		deliveryCenterService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}