/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Role;
import net.wit.entity.Role.RoleType;
import net.wit.service.AdminService;
import net.wit.service.RoleService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 角色
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminModulePermissionsController")
@RequestMapping("/admin/modulePermissions")
public class ModulePermissionsController extends BaseController {

	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add( ModelMap model) {
		Admin admin =adminService.getCurrent();
		model.addAttribute("admin",admin);
		model.addAttribute("roles",admin.getRoles());
		return "/admin/module_permissions/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Role role, RedirectAttributes redirectAttributes) {
		if (!isValid(role)) {
			return ERROR_VIEW;
		}
		role.setIsSystem(false);
		role.setAdmins(null);
		roleService.save(role);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Admin admin = adminService.getCurrent();
		Role role = roleService.find(id);
		if(admin.getUsername().equals("admin") && role.getRoleType()==null){
			try{
				role.setRoleType(RoleType.admin);
				roleService.update(role);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		model.addAttribute("role",roleService.find(id));
		return "/admin/module_permissions/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Role role, RedirectAttributes redirectAttributes) {
		if (!isValid(role)) {
			return ERROR_VIEW;
		}
		Role pRole = roleService.find(role.getId());
		if (pRole == null || pRole.getIsSystem()) {
			return ERROR_VIEW;
		}
		roleService.update(role, "isSystem", "admins");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		//Admin admin = adminService.getCurrent();
		model.addAttribute("page", roleService.findPage(adminService.getCurrent(),pageable));
		return "/admin/module_permissions/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Role role = roleService.find(id);
				if (role != null && (role.getIsSystem() || (role.getAdmins() != null && !role.getAdmins().isEmpty()))) {
					return Message.error("admin.role.deleteExistNotAllowed", role.getName());
				}
			}
			roleService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}