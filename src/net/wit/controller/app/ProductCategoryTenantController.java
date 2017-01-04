/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ProductCategoryTenantModel;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tenant;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.TagService;
import net.wit.service.TenantService;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("appProductCategoryTenantController")
@RequestMapping("/app/product_category_tenant")
public class ProductCategoryTenantController extends BaseController {

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/alls", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock alls(Long tenantId) {
		Tenant tenant = tenantService.find(tenantId);
		List<ProductCategoryTenant> productCategories;
		productCategories = productCategoryTenantService.findRoots(tenant);
		return DataBlock.success(ProductCategoryTenantModel.bindAllData(productCategories),"执行成功");
	}
	
	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots(Long tenantId) {
		Tenant tenant = tenantService.find(tenantId);
		List<ProductCategoryTenant> productCategories;
		productCategories = productCategoryTenantService.findRoots(tenant);
		return DataBlock.success(ProductCategoryTenantModel.bindData(productCategories),"执行成功");
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock childrens(Long tenantId,Long id) {
		Tenant tenant = tenantService.find(tenantId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		if (productCategoryTenant == null) {
			return DataBlock.error("传入的上级结点ID无效");
		}
		List<ProductCategoryTenant> childrens = productCategoryTenantService.findChildren(productCategoryTenant,tenant);
		return DataBlock.success(ProductCategoryTenantModel.bindData(childrens),"执行成功");
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock parents(Long tenantId,Long id) {
		Tenant tenant = tenantService.find(tenantId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		if (productCategoryTenant == null) {
			return DataBlock.error("传入的上级结点ID无效");
		}
		List<ProductCategoryTenant> parents = productCategoryTenantService.findParents(productCategoryTenant,tenant);
		return DataBlock.success(ProductCategoryTenantModel.bindData(parents),"执行成功");
	}

}