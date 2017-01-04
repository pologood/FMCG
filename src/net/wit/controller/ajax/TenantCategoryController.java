/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.TenantCategory;
import net.wit.service.TagService;
import net.wit.service.TenantCategoryService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商家分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxTenantCategoryController")
@RequestMapping("/ajax/tenant_category")
public class TenantCategoryController extends BaseController {

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public Message roots(Integer count) {
		List<TenantCategory> tenantCategories = tenantCategoryService.findRoots(count);
		return Message.success(JsonUtils.toJson(tenantCategories));
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public Message childrens(Long id, Integer count) {
		TenantCategory tenantCategory = tenantCategoryService.find(id);
		if (tenantCategory == null) {
			return Message.error("ajax.tenantCategory.NotExist");
		}
		List<TenantCategory> tenantCategorys = tenantCategoryService.findChildren(tenantCategory, count);
		return Message.success(JsonUtils.toJson(tenantCategorys));
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public Message parents(Long id, Integer count) {
		TenantCategory tenantCategory = tenantCategoryService.find(id);
		List<TenantCategory> tenantCategorys = tenantCategoryService.findParents(null, tenantCategory, count);
		return Message.success(JsonUtils.toJson(tenantCategorys));
	}

}