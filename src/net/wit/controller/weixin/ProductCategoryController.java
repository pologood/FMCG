/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.ProductCategoryModel;
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
@Controller("weixinProductCategoryController")
@RequestMapping("/weixin/productCategory")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	
	
	/**
	 * 获取商品分类所有数据
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots() {
		List<ProductCategory> productCategories;
		productCategories = productCategoryService.findRoots();
		return DataBlock.success(ProductCategoryModel.bindAllData(productCategories),"执行成功");
	}

	/**
	 * 获取推荐的商品分类所有数据
	 */
	@RequestMapping(value = "/tag/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock tagRoots() {
		List<ProductCategory> productCategories;
		productCategories = productCategoryService.findRoots(tagService.find(12L),4);
		return DataBlock.success(ProductCategoryModel.bindAllData(productCategories),"执行成功");
	}

}