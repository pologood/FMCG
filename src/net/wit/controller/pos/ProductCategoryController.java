/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ParameterGroupModel;
import net.wit.controller.app.model.ProductCategoryModel;
import net.wit.entity.Attribute;
import net.wit.entity.ParameterGroup;
import net.wit.entity.ProductCategory;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.service.BrandService;
import net.wit.service.ProductCategoryService;
import net.wit.service.TagService;
import net.wit.service.TenantService;
import net.wit.util.JsonUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("posProductCategoryController")
@RequestMapping("/pos/product_category")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots(Long tenantId,String key) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		List<ProductCategory> productCategories;
		productCategories = productCategoryService.findRoots();
		return DataBlock.success(ProductCategoryModel.bindData(productCategories),"success");
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock childrens(Long tenantId,String key,Long id) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("传入的上级结点ID无效");
		}
		List<ProductCategory> childrens = productCategoryService.findChildren(productCategory);
		return DataBlock.success(ProductCategoryModel.bindData(childrens),"success");
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock parents(Long tenantId,String key,Long id) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("传入的结点ID无效");
		}
		List<ProductCategory> parents = productCategoryService.findParents(productCategory);
		return DataBlock.success(ProductCategoryModel.bindData(parents),"success");
	}
	
    /**
     * 获取参数组
     */
    @RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock parameterGroups(Long id) {
        ProductCategory productCategory = productCategoryService.find(id);
        return DataBlock.success(ParameterGroupModel.bindData(productCategory.getParameterGroups() ),"success");
    }

    /**
     * 获取属性
     */
    @RequestMapping(value = "/attributes", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock attributes(Long id) {
        ProductCategory productCategory = productCategoryService.find(id);
        return DataBlock.success(productCategory.getAttributes(),"success");
    }

	

}