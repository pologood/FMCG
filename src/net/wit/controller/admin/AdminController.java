/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Area;
import net.wit.entity.Department;
import net.wit.entity.Role;
import net.wit.entity.Enterprise;
import net.wit.service.AdminService;
import net.wit.service.AreaService;
import net.wit.service.DepartmentService;
import net.wit.service.RoleService;
import net.wit.service.EnterpriseService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 管理员
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 50;
	
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "roleServiceImpl")
	private RoleService roleService;
	@Resource(name = "departmentServiceImpl")
	private DepartmentService departmentService;
	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	/**
	 * 检查用户名是否存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
//		if (adminService.usernameExists(username)) {
//			return false;
//		} else {
//			return true;
//		}

		return !adminService.usernameExists(username);
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Admin admin = adminService.getCurrent();
		Pageable pageable = new Pageable();
		pageable.setPageSize(PAGE_SIZE);
		pageable.setPageNumber(1);
		List<Role> roleChils = new ArrayList<Role>();
		for(Role role:admin.getRoles()){
			List<Role> roles=roleService.findList(role.getRoleType());
			if(roles.size()>roleChils.size()){
				roleChils.addAll(roles);
			}
		}
		model.addAttribute("roles", roleChils);
		model.addAttribute("departments", departmentService.findAll());
		try{
		model.addAttribute("enterprises",enterpriseService.findPage(admin,pageable).getContent());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "/admin/admin/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Admin admin, Long[] roleIds, @RequestParam(defaultValue = "0") Long departMentId, @RequestParam(defaultValue = "0")Long enterpriseId, RedirectAttributes redirectAttributes) {
		admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
		if (!isValid(admin, Save.class)) {
			return ERROR_VIEW;
		}
		if (adminService.usernameExists(admin.getUsername())) {
			return ERROR_VIEW;
		}
		Department department = new Department();
		if(departMentId!=0){
			department = departmentService.find(departMentId);
			if(department!=null){
				admin.setDepartment(department);
			}
		}
		
		Enterprise enterprise = new Enterprise();
		if(enterpriseId!=0){
			enterprise = enterpriseService.find(enterpriseId);
			if(enterprise!=null){
				admin.setEnterprise(enterprise);
			}
		}
		
		admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		admin.setIsLocked(false);
		admin.setLoginFailureCount(0);
		admin.setLockedDate(null);
		admin.setLoginDate(null);
		admin.setLoginIp(null);
		admin.setOrders(null);
		
		adminService.save(admin);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Admin admin = adminService.getCurrent();
		Pageable pageable = new Pageable();
		pageable.setPageSize(PAGE_SIZE);
		pageable.setPageNumber(1);
		List<Role> roleChils = new ArrayList<Role>();
		for(Role role:admin.getRoles()){
			List<Role> roles=roleService.getRoleList(role.getRoleType());
			if(roles.size()>roleChils.size()){
				roleChils.addAll(roles);
			}
		}
		model.addAttribute("roles", roleChils);
		model.addAttribute("admin", adminService.find(id));
		model.addAttribute("departments", departmentService.findAll());
		model.addAttribute("enterprises",enterpriseService.findPage(admin,pageable).getContent());
		return "/admin/admin/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Admin admin, Long[] roleIds,@RequestParam(defaultValue = "0")Long departMentId,@RequestParam(defaultValue = "0")Long enterpriseId, RedirectAttributes redirectAttributes) {
		admin.setRoles(new HashSet<Role>(roleService.findList(roleIds)));
		if (!isValid(admin)) {
			return ERROR_VIEW;
		}
		Admin pAdmin = adminService.find(admin.getId());
		if (pAdmin == null) {
			return ERROR_VIEW;
		}
		Department department = new Department();
		if(departMentId!=0){
			department = departmentService.find(departMentId);
			if(department!=null){
				admin.setDepartment(department);
			}
		}
		
		Enterprise enterprise = new Enterprise();
		if(enterpriseId!=0){
			enterprise = enterpriseService.find(enterpriseId);
			if(enterprise!=null){
				admin.setEnterprise(enterprise);
			}
		}
		
		if (StringUtils.isNotEmpty(admin.getPassword())) {
			admin.setPassword(DigestUtils.md5Hex(admin.getPassword()));
		} else {
			admin.setPassword(pAdmin.getPassword());
		}
		if (pAdmin.getIsLocked() && !admin.getIsLocked()) {
			admin.setLoginFailureCount(0);
			admin.setLockedDate(null);
		} else {
			admin.setIsLocked(pAdmin.getIsLocked());
			admin.setLoginFailureCount(pAdmin.getLoginFailureCount());
			admin.setLockedDate(pAdmin.getLockedDate());
		}
		adminService.update(admin, "username", "loginDate", "loginIp", "orders");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Admin admin = adminService.getCurrent();
		List<EnterpriseType> enterpriseTypes= new ArrayList<Enterprise.EnterpriseType>();
		if(!admin.getUsername().equals("admin")){
			EnterpriseType enterpriseType = admin.getEnterprise().getEnterprisetype();
			for(int i=enterpriseType.ordinal()+1;i<EnterpriseType.values().length;i++){
				enterpriseTypes.add(EnterpriseType.values()[i]);
			}
			Area area=null;
			if(enterpriseType==EnterpriseType.provinceproxy){
				area=admin.getEnterprise().getArea();
				List<Area> areas = areaService.findChildren(area, null);
				model.addAttribute("page", adminService.findPage(enterpriseTypes,areas,pageable));
			}else if(enterpriseType==EnterpriseType.cityproxy){
				area=admin.getEnterprise().getArea().getParent();
				if(area==null){
					area=admin.getEnterprise().getArea();
				}
				List<Area> areas = areaService.findChildren(area, null);
				model.addAttribute("page", adminService.findPage(enterpriseTypes,areas,pageable));
			}else if(enterpriseType==EnterpriseType.countyproxy){
				area=admin.getEnterprise().getArea().getParent().getParent();
				if(area==null){
					area=admin.getEnterprise().getArea().getParent();
				}
				List<Area> areas = new ArrayList<Area>();
				areas.add(area);
				model.addAttribute("page", adminService.findPage(enterpriseTypes,areas,pageable));
			}else{
				model.addAttribute("page", adminService.findPage(enterpriseTypes,null,pageable));
			}
		}else{
			model.addAttribute("page", adminService.findPage(pageable));
		}
		return "/admin/admin/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids.length >= adminService.count()) {
			return Message.error("admin.common.deleteAllNotAllowed");
		}
		adminService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}