/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.service.AreaService;

/**
 * Controller - 地区
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminAreaController")
@RequestMapping("/admin/area")
public class AreaController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long parentId, ModelMap model) {
		model.addAttribute("parent", areaService.find(parentId));
		return "/admin/area/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Area area, Long parentId, RedirectAttributes redirectAttributes) {
		area.setParent(areaService.find(parentId));
		if (!isValid(area)) {
			return ERROR_VIEW;
		}
		area.setIsAudit(false);
		area.setFullName(null);
		area.setTreePath(null);
		area.setChildren(null);
		area.setMembers(null);
		area.setReceivers(null);
		area.setOrders(null);
		area.setDeliveryCenters(null);
		areaService.save(area);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("area", areaService.find(id));
		return "/admin/area/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Area area, String audit,String open,RedirectAttributes redirectAttributes, ModelMap model) {
		Boolean isAudit,isOpen;
		if("true".equals(audit)){
			isAudit=true;
		}else{
			isAudit=false;
		}

		if("true".equals(open)){
			isOpen=true;
		}else{
			isOpen=false;
		}
		area.setIsOpen(isOpen);
		area.setIsAudit(isAudit);
		if (!isValid(area)) {
			return ERROR_VIEW;
		}
		areaService.update(area, "fullName", "treePath", "parent", "children", "members", "receivers", "orders", "deliveryCenters");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long parentId, ModelMap model) {
		Area parent = areaService.find(parentId);
		if (parent != null) {
			model.addAttribute("parent", parent);
			model.addAttribute("areas", new ArrayList<Area>(parent.getChildren()));
		} else {
			model.addAttribute("areas", areaService.findRoots());
		}
		return "/admin/area/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		areaService.delete(id);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 获取省
	 */
	@RequestMapping(value = "/getProvince", method = RequestMethod.POST)
	@ResponseBody
	public Area getProvince(Long id) {
		Area area = areaService.find(id);
		Area parent = area.getParent();
		return parent;
	}
	
	/**
	 * 获取城市
	 */
	@RequestMapping(value = "/getCitys", method = RequestMethod.POST)
	@ResponseBody
	public Set<Area> getCitys(Long id) {
		Area area = areaService.find(id);
		Set<Area> citys = area.getChildren();
		return citys;
	}
	
	
	/**
	 * 获取全部省
	 */
	@RequestMapping(value = "/chooseProvince", method = RequestMethod.POST)
	@ResponseBody
	public List<Area> chooseProvince() {
		List<Area> provinces = areaService.findRoots();
		return provinces;
	}

}