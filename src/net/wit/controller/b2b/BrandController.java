/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Pageable;
import net.wit.ResourceNotFoundException;
import net.wit.entity.Brand;
import net.wit.entity.ProductCategory;
import net.wit.entity.Product.OrderType;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductService;
import net.wit.service.TagService;

import net.wit.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 品牌
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bBrandController")
@RequestMapping("/b2b/brand")
public class BrandController extends BaseController {
	/** 每页记录数 */
	private static final int PAGE_SIZE = 40;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	@Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;
	
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "memberServiceImpl")
    private MemberService memberService;

	@RequestMapping(value = "/recommend_brand",method = RequestMethod.GET)
	public String brand(HttpServletRequest request, ModelMap model) {
		List<Filter> filters = new ArrayList<>();
		Date beginDate = DateUtil.getMondayOfThisWeek();
		Date endDate = DateUtil.getSundayOfThisWeek();
		List<ProductCategory> rootCategorys=productCategoryService.findRoots();
		model.addAttribute("rootCategory",rootCategorys);
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("category",productCategoryService.findListByTag(tagService.findList(new Long[]{12l}), 6, null));
		model.addAttribute("brand",brandService.findList(tagService.find(24l)));
		model.addAttribute("dayProducts", productService.openList(10,areaService.getCurrent(),null,null,null,tagService.findList(new Long[]{1l}),null,null,null,null,null,null,filters,null,OrderType.weight));
		model.addAttribute("deliverys", deliveryCenterService.findList(null, tagService.findList(new Long[]{6l}), areaService.getCurrent(), true, null, null, 10, null, null));
		model.addAttribute("type","recommend_brand");
		model.addAttribute("member",memberService.getCurrent());
		return "b2b/brand/recommend_brand";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
	public String list(@PathVariable Integer pageNumber, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", brandService.findPage(pageable));
		return "/b2b/brand/list";
	}

	/**
	 * 内容
	 */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model) {
		Brand brand = brandService.find(id);
		if (brand == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("brand", brand);
		return "/b2b/brand/content";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody List<Brand> search(String name){
		if(name==null){
			name="";
		}
		List<Brand> data = brandService.search(name, null, null);
		//Brand brand = brandService.find(id);
		return data;
	}
}