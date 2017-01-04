/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.ProductCategory;
import net.wit.entity.Tag;
import net.wit.service.BrandService;
import net.wit.service.ProductCategoryService;
import net.wit.service.TagService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxProductCategoryController")
@RequestMapping("/ajax/product_category")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public Message roots(Long productCategoryTagId, Integer count) {
		Tag tag = tagService.find(productCategoryTagId);
		List<ProductCategory> productCategories;
		if (tag == null) {
			productCategories = productCategoryService.findRoots(count);
		} else {
			productCategories = productCategoryService.findRoots(tag, count);
		}
		return Message.success(JsonUtils.toJson(productCategories));
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public Message childrens(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return Message.error("ajax.productCategory.NotExist");
		}
		List<ProductCategory> childrens = productCategoryService.findChildren(productCategory);
		for (ProductCategory category : childrens) {
			category.setChildList(productCategoryService.findChildren(category));
		}
		return Message.success(JsonUtils.toJson(childrens));
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public Message parents(Long id, Integer count) {
		ProductCategory productCategory = productCategoryService.find(id);
		return Message.success(JsonUtils.toJson(productCategoryService.findParents(productCategory, count)));
	}

}