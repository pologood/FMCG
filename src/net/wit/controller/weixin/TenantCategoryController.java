/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.TenantCategoryModel;
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
@Controller("weixinTenantCategoryController")
@RequestMapping("/weixin/tenantCategory")
public class TenantCategoryController extends BaseController {

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 获取商家分类（全部数据）
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots() {
		List<TenantCategory> tenantCategories;
		tenantCategories = tenantCategoryService.findRoots();
		return DataBlock.success(TenantCategoryModel.bindAllData(tenantCategories),"执行成功");
	}

}