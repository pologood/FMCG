/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.ProductCategoryModel;
import net.wit.entity.ProductCategory;
import net.wit.service.BrandService;
import net.wit.service.ProductCategoryService;
import net.wit.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantProductCategoryController")
@RequestMapping("/assistant/product_category")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

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
		data.put("url",bundle.getString("WeiXinSiteUrl")+"/resources/data/category.json");
		return DataBlock.success(data,"执行成功");
	}
	
	
	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots() {
		List<ProductCategory> productCategories;
		productCategories = productCategoryService.findRoots();
		return DataBlock.success(ProductCategoryModel.bindData(productCategories),"执行成功");
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock childrens(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("传入的上级结点ID无效");
		}
		List<ProductCategory> childrens = productCategoryService.findChildren(productCategory);
		return DataBlock.success(ProductCategoryModel.bindData(childrens),"执行成功");
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock parents(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("传入的结点ID无效");
		}
		List<ProductCategory> parents = productCategoryService.findParents(productCategory);
		return DataBlock.success(ProductCategoryModel.bindData(parents),"执行成功");
	}

}