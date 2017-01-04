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

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.entity.Tenant;
import net.wit.entity.TenantCategory;
import net.wit.service.TenantCategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 企业分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminTenantCategoryController")
@RequestMapping("/admin/tenant_category")
public class TenantCategoryController extends BaseController {

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		return "/admin/tenant_category/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(TenantCategory tenantCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes) {
		tenantCategory.setParent(tenantCategoryService.find(parentId));
		if (!isValid(tenantCategory)) {
			return ERROR_VIEW;
		}
		tenantCategory.setTreePath(null);
		tenantCategory.setGrade(null);
		tenantCategory.setChildren(null);
		tenantCategoryService.save(tenantCategory);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		TenantCategory tenantCategory = tenantCategoryService.find(id);
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenantCategory", tenantCategory);
		model.addAttribute("children", tenantCategoryService.findChildren(tenantCategory));
		return "/admin/tenant_category/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(TenantCategory tenantCategory, Long parentId, Long[] brandIds, RedirectAttributes redirectAttributes) {
		tenantCategory.setParent(tenantCategoryService.find(parentId));
		if (!isValid(tenantCategory)) {
			return ERROR_VIEW;
		}
		if (tenantCategory.getParent() != null) {
			TenantCategory parent = tenantCategory.getParent();
			if (parent.equals(tenantCategory)) {
				return ERROR_VIEW;
			}
			List<TenantCategory> children = tenantCategoryService.findChildren(parent);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		tenantCategoryService.update(tenantCategory, "treePath", "grade", "children", "tenants");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		return "/admin/tenant_category/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		TenantCategory tenantCategory = tenantCategoryService.find(id);
		if (tenantCategory == null) {
			return ERROR_MESSAGE;
		}
		Set<TenantCategory> children = tenantCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.tenantCategory.deleteExistChildrenNotAllowed");
		}
		Set<Tenant> tenants = tenantCategory.getTenants();
		if (tenants != null && !tenants.isEmpty()) {
			return Message.error("admin.tenantCategory.deleteExistProductNotAllowed");
		}
		tenantCategoryService.delete(id);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<TenantCategory> search(String name) {
		List<Filter> filters = new ArrayList<Filter>();
		int limit = 10000;
		if (StringUtils.isNotBlank(name)) {
			filters.add(new Filter("name", Operator.like, "%" + name + "%"));
			limit = 100;
		}
		return tenantCategoryService.findList(limit, filters, null);
	}

}