/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.TenantCategoryModel;
import net.wit.entity.TenantCategory;
import net.wit.service.TagService;
import net.wit.service.TenantCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Controller - 商家分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberTenantCategoryController")
@RequestMapping("/assistant/member/tenant_category")
public class TenantCategoryController extends BaseController {

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 分类--获取数据包
	 * timestamp 时间戳
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock get(String timestamp) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("timestamp",348343400);
		data.put("url",bundle.getString("WeiXinSiteUrl")+"/resources/data/tenantCategory.json");
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots() {
		List<TenantCategory> tenantCategories;
		tenantCategories = tenantCategoryService.findRoots();
		return DataBlock.success(TenantCategoryModel.bindData(tenantCategories),"执行成功");
	}

	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/alls", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock alls(Long channelId) {
		List<TenantCategory> tenantCategories;
		tenantCategories = tenantCategoryService.findRoots();
		return DataBlock.success(TenantCategoryModel.bindAllData(tenantCategories),"执行成功");
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock childrens(Long id) {
		TenantCategory tenantCategory = tenantCategoryService.find(id);
		if (tenantCategory == null) {
			return DataBlock.error("传入的上级结点ID无效");
		}
		List<TenantCategory> childrens = tenantCategoryService.findChildren(tenantCategory);
		return DataBlock.success(TenantCategoryModel.bindData(childrens),"执行成功");
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock parents(Long id) {
		TenantCategory tenantCategory = tenantCategoryService.find(id);
		if (tenantCategory == null) {
			return DataBlock.error("传入的结点ID无效");
		}
		List<TenantCategory> parents = tenantCategoryService.findParents(tenantCategory);
		return DataBlock.success(TenantCategoryModel.bindData(parents),"执行成功");
	}

}