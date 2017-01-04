/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2c;

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
import net.wit.controller.app.model.AdModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ProductDescriptionModel;
import net.wit.controller.app.model.ProductListModel;
import net.wit.controller.app.model.ProductModel;
import net.wit.controller.app.model.PromotionModel;
import net.wit.entity.Ad;
import net.wit.entity.AdPosition;
import net.wit.entity.Brand;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Promotion;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.service.AdPositionService;
import net.wit.service.AdService;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.TagService;
import net.wit.service.TenantService;

/**
 * Controller - 广告
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2cAdController")
@RequestMapping("/app/b2c/ad")
public class AdController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	/**
	 * 获取商家广告
     * params position {banner 头部横幅广告}
     * id 店铺 id
     * count 读取前几个广告图
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(@PathVariable Long id,String position,Integer count, HttpServletRequest request) {
		Tenant tenant = tenantService.find(id);
		if (tenant==null) {
			DataBlock.error("企业ID无效");
		}
		Long adposition = 80L;
		if ("banner".equals(position)) {
			adposition = 80L;
		}
		AdPosition adPosition = adPositionService.find(adposition, tenant,null, count);
		 
		return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");
		
	}

	/**
	 * 获取城市广告
     * params position {banner 头部横幅广告}
     * count 读取前几个广告图
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(String position,Integer count, HttpServletRequest request) {
		Long adposition = 70L;
		if ("banner".equals(position)) {
			adposition = 70L;
		}
		AdPosition adPosition = adPositionService.find(adposition, null,null, count);
		return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");
		
	}
	

	/**
	 * 获取名星广告
     * params position {dp1 推荐1,dp2 推荐1 dp3 推荐1 dp4 推荐1}
     * id 导购
     * count 读取前几个广告图
	 */
	@RequestMapping(value = "/guider/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock guider(@PathVariable Long id,String position,Integer count, HttpServletRequest request) {
		Long adposition = 70L;
		if ("banner".equals(position)) {
			adposition = 70L;
		}
		AdPosition adPosition = adPositionService.find(adposition, null,null, count);
		return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");
		
	}
	
	/**
	 * 获取频道广告
     * params position { 推荐商品}
     * id 频道
     * count 读取前几个广告图
	 */
	@RequestMapping(value = "/channel/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock channel(@PathVariable Long id,Integer count, HttpServletRequest request) {
		AdPosition adPosition = adPositionService.find(80L, null,null, count);
		 
		return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");
		
	}
	
}