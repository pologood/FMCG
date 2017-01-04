/*
 * Copyright 2005-2013 rsico.net. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Department;
import net.wit.entity.Enterprise;
import net.wit.service.DepartmentService;
import net.wit.service.EnterpriseService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 广告  
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminDepartMentController")
@RequestMapping("/admin/departMent")
public class DepartMentController extends BaseController {

	@Resource(name = "departmentServiceImpl")
	private DepartmentService departmentService;
	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("enterprises", enterpriseService.findAll());
		return "/admin/depart_ment/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Department department, Long enterpriseId, RedirectAttributes redirectAttributes) {
		if (!isValid(department)) {
			return ERROR_VIEW;
		}
		Enterprise enterprise = new Enterprise();
		if(enterpriseId!=0&&enterpriseId!=null){
			enterprise = enterpriseService.find(enterpriseId);
			if(enterprise!=null){
				department.setEnterprise(enterprise);
			}
		}
		departmentService.save(department);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("department", departmentService.find(id));
		model.addAttribute("enterprises", enterpriseService.findAll());
		return "/admin/depart_ment/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Department department, Long enterpriseId, RedirectAttributes redirectAttributes) {
		if (!isValid(department)) {
			return ERROR_VIEW;
		}
		Enterprise enterprise = new Enterprise();
		if(enterpriseId!=0&&enterpriseId!=null){
			enterprise = enterpriseService.find(enterpriseId);
			if(enterprise!=null){
				department.setEnterprise(enterprise);
			}
		}
		departmentService.update(department);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", departmentService.findPage(pageable));
		return "/admin/depart_ment/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		departmentService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}