/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Pageable;
import net.wit.ResourceNotFoundException;
import net.wit.entity.Area;
import net.wit.entity.Attribute;
import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;
import net.wit.entity.Cart;
import net.wit.entity.Community;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.Tag;
import net.wit.service.AreaService;
import net.wit.service.BrandSeriesService;
import net.wit.service.BrandService;
import net.wit.service.CartItemService;
import net.wit.service.CartService;
import net.wit.service.CommunityService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SearchService;
import net.wit.service.TagService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperProductController")
@RequestMapping("/helper/product")
public class ProductController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "brandSeriesServiceImpl")
	private BrandSeriesService brandSeriesService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;


	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	
	/**
	 * 浏览记录
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public @ResponseBody List<Product> history(Long[] ids) {
		return productService.findList(ids);
	}

	/**
	 * cartController IE下 navbar 方法调用不成功(火狐可以)，暂且放在主页面调用
	 */
	@RequestMapping(value = "/get_cart_count", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getCartCount(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = cartService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		if (cart == null) {
			data.put("count", 0);
		} else {
			data.put("count", cart.getQuantity());
		}
		return data;
	}
	
	
	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
	public @ResponseBody Long hits(@PathVariable Long id) {
		return productService.viewHits(id);
	}

	/**
	 * 详情
	 */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model) {
		Product product = productService.find(id);
		if (product == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("member", memberService.getCurrent());
		model.addAttribute("product", product);
		model.addAttribute("productCategory", product.getProductCategory());
		return "helper/product/content";
	}

}