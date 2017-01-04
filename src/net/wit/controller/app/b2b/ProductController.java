/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2b;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ProductDescriptionModel;
import net.wit.controller.app.model.ProductListModel;
import net.wit.controller.app.model.ProductModel;
import net.wit.controller.app.model.PromotionModel;
import net.wit.entity.Area;
import net.wit.entity.Brand;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Promotion;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.TagService;
import net.wit.service.TenantService;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2bProductController")
@RequestMapping("/app/b2b/product")
public class ProductController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 根据id获取商品详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody DataBlock view(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		ProductModel model = new ProductModel();
		model.copyFrom(product);
		model.bind(product.getGoods());
		return DataBlock.success(model,"执行成功");
	}

	/**
	 * 获取推荐，搭配商品列表
	 */
	@RequestMapping(value = "/recommend/{id}", method = RequestMethod.GET)
	public @ResponseBody DataBlock recommend(@PathVariable Long id,Pageable pageable) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
	    Long[] tagIds = {5L};
		List<Tag> tags = tagService.findList(tagIds);
		Page<Product> page = productService.findMyPage(product.getTenant(),null, null, null, product.getBrand(), null, tags, null, null, null, true, true, null, null, null,null, OrderType.weight, pageable);
	    return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"执行成功");
	}

	/**
	 * 单商家商品
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(@PathVariable Long id, Long productCategoryTenantId,String keyword,Long[] tagIds, Long brandId, BigDecimal startPrice, BigDecimal endPrice,OrderType orderType,Pageable pageable, HttpServletRequest request) {
		Tenant tenant = tenantService.find(id);
		if (tenant==null) {
			DataBlock.error("企业ID无效");
		}
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		Brand brand = brandService.find(brandId);
		List<Tag> tags = tagService.findList(tagIds);
		Page<Product> page = productService.findMyPage(tenant,keyword, null, productCategoryTenant, brand, null, tags, null, startPrice, endPrice, true, true, null, null, null,null, orderType, pageable);
	    return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"执行成功");
	}


	/**
	 * 商品列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long productCategoryId,String keyword,Long[] tagIds, Long brandId,  Long areaId, BigDecimal startPrice, BigDecimal endPrice,Boolean isTop,Boolean isGift,Boolean isOutOfStock,OrderType orderType,Pageable pageable, HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Area area = areaService.find(areaId);
		List<Tag> tags = tagService.findList(tagIds);
		Page<Product> page = productService.findPage(productCategory, brand, null, tags, null, startPrice, endPrice, true, true,isTop, isGift, isOutOfStock, null, null, area, null, orderType, null, keyword, pageable);
		return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"执行成功");
	}

	/**
	 * 最近购买
	 */
	@RequestMapping(value = "/history/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock history(Long productCategoryId,Long[] tagIds, Long brandId,  Long areaId, BigDecimal startPrice, BigDecimal endPrice,Boolean isTop,Boolean isGift,Boolean isOutOfStock,OrderType orderType,Pageable pageable, HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Area area = areaService.find(areaId);
		List<Tag> tags = tagService.findList(tagIds);
		Page<Product> page = productService.findPage(productCategory, brand, null, tags, null, startPrice, endPrice, true, true, isTop, isGift, isOutOfStock, null, null, area, null, null, null, orderType, pageable);
	    return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"执行成功");
	}
		
	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits", method = RequestMethod.GET)
	public @ResponseBody DataBlock hits(Long id) {
		return DataBlock.success(productService.viewHits(id),"执行成功");
	}

}