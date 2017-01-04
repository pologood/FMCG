/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.entity.ProductCategory;
import net.wit.service.AreaService;
import net.wit.service.ProductCategoryService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapProductCategoryController")
@RequestMapping("/wap/productCategory")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 跳转商品分类页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String indexNew(ModelMap model, HttpServletRequest request) {
		List<ProductCategory> productCategoryRootList = productCategoryService.findRoots();
		model.addAttribute("area", areaService.getCurrent());
		model.addAttribute("productCategoryRootList", productCategoryRootList);
		model.addAttribute("type","category");
		return "/wap/productCategory/index";
	}
	
}