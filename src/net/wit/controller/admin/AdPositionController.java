/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.AdPosition;
import net.wit.entity.AdPosition.Type;
import net.wit.entity.ProductChannel;
import net.wit.service.ActivityPlanningService;
import net.wit.service.AdPositionService;
import net.wit.service.ProductChannelService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 广告位
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminAdPositionController")
@RequestMapping("/admin/ad_position")
public class AdPositionController extends BaseController {

	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	@Resource(name = "activityPlanningServiceImpl")
	private ActivityPlanningService activityPlanningService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Type type, ModelMap model) {
		model.addAttribute("productChannels", productChannelService.findAll());
		model.addAttribute("types", Type.values());
		model.addAttribute("add_type", type);
		return "/admin/ad_position/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(AdPosition adPosition, Long productChannelId, RedirectAttributes redirectAttributes) {
		if (!isValid(adPosition)) {
			return ERROR_VIEW;
		}
		ProductChannel productChannel = productChannelService.find(productChannelId);
		adPosition.setAds(null);
		adPosition.setProductChannel(productChannel);
		adPositionService.save(adPosition);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		AdPosition adPosition = adPositionService.find(id);
		model.addAttribute("types", Type.values());
		model.addAttribute("adPosition", adPosition);
		model.addAttribute("productChannels", productChannelService.findAll());
		return "/admin/ad_position/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(AdPosition adPosition, Long productChannelId,Long activityPlanningId, RedirectAttributes redirectAttributes) {
		if (!isValid(adPosition)) {
			return ERROR_VIEW;
		}
		ProductChannel productChannel = productChannelService.find(productChannelId);

		if(activityPlanningId!=null){
			adPosition.setActivityPlanning(activityPlanningService.find(activityPlanningId));
		}
		adPosition.setProductChannel(productChannel);
		adPositionService.update(adPosition, "ads");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Type type, Pageable pageable, ModelMap model) {
		List<Filter> filters = pageable.getFilters();
		pageable.setSearchProperty("name");
		if (type != null) {
			filters.add(new Filter("type", Operator.eq, type));
		}
		model.addAttribute("page", adPositionService.findPage(pageable));
		model.addAttribute("adPositionTypes", Type.values());
		model.addAttribute("type", type);
		return "/admin/ad_position/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		adPositionService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}