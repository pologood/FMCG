/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.Brand;
import net.wit.entity.ProductCategory;
import net.wit.service.ProductCategoryService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 品牌
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxBrandController")
@RequestMapping("/ajax/brand")
public class BrandController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 40;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	
	/**
	 * 获取商品类别下的品牌集合
	 */
	@RequestMapping(value = "/getBrands", method = RequestMethod.GET)
	@ResponseBody
	public Message getBrands(Long productCategoryId, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if(productCategory==null){
			return Message.error("ajax.brand.NotExist");
		}
		Set<Brand> brands = productCategory.getBrands();
		return Message.success(JsonUtils.toJson(brands));
	}
}