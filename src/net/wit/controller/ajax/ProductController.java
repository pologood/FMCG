/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.ajax.model.ProductModel;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.ProductVo;
import net.wit.entity.Area;
import net.wit.entity.Attribute;
import net.wit.entity.Brand;
import net.wit.entity.Cart;
import net.wit.entity.Location;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.Promotion;
import net.wit.entity.Tag;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SearchService;
import net.wit.service.TagService;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxProductController")
@RequestMapping("/ajax/product")
public class ProductController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "productDisplay")
	private DisplayEngine<Product, ProductVo> productDisplay;

	/**
	 * 根据id获取商品
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public @ResponseBody Message detail(@PathVariable Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return Message.error("该商品不存在！");
		}
		return Message.success(JsonUtils.toJson(product));
	}

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody Message checkSn(String previousSn, String sn) {
		if (StringUtils.isEmpty(sn)) {
			return Message.error("ajax.product.sn.empty");
		}
		if (productService.snUnique(previousSn, sn)) {
			return Message.success("ajax.product.sn.enable");
		} else {
			return Message.error("ajax.product.sn.repeat");
		}
	}

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> get(String barcode) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", Message.success("success"));
		ArrayList<ProductModel> products = new ArrayList<ProductModel>();
		ProductModel product = new ProductModel();
		product.setId(29L);
		product.setFullName("test1");
		product.setMarketPrice(BigDecimal.ZERO);
		product.setPrice(BigDecimal.ONE);
		products.add(product);
		data.put("products", products);
		product.setId(30L);
		product.setFullName("test2");
		product.setMarketPrice(BigDecimal.ZERO);
		product.setPrice(BigDecimal.ONE);
		products.add(product);
		data.put("products", products);
		return data;
	}

	/**
	 * 获取参数组
	 */
	@RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
	public @ResponseBody Message parameterGroups(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return Message.error("ajax.productCategory.NotExist");
		}
		return Message.success(JsonUtils.toJson(productCategory.getParameterGroups()));
	}

	/**
	 * 获取属性
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody Message attributes(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return Message.error("ajax.productCategory.NotExist");
		}
		return Message.success(JsonUtils.toJson(productCategory.getAttributes()));
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		productService.delete(id);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 计算默认市场价
	 * @param price 价格
	 */
	private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
		return setting.setScale(price.multiply(new BigDecimal(defaultMarketPriceScale.toString())));
	}

	/**
	 * 计算默认积分
	 * @param price 价格
	 */
	private long calculateDefaultPoint(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultPointScale = setting.getDefaultPointScale();
		return price.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
	}

	/**
	 * 浏览记录
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public @ResponseBody Message history(Long[] ids) {
		return Message.success(JsonUtils.toJson(productService.findList(ids)));
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{productCategoryId}", method = RequestMethod.GET)
	@ResponseBody
	public Message list(@PathVariable Long productCategoryId, Long brandId, BigDecimal startPrice, BigDecimal endPrice, Location location, BigDecimal distance, OrderType orderType, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
			return Message.error("ajax.productCategory.NotExist");
		}
		Brand brand = brandService.find(brandId);
		Area currentArea = areaService.getCurrent();
		Map<Attribute, String> attributeValue = new HashMap<Attribute, String>();
		Set<Attribute> attributes = productCategory.getAttributes();
		for (Attribute attribute : attributes) {
			String value = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(value) && attribute.getOptions().contains(value)) {
				attributeValue.put(attribute, value);
			}
		}
		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(15);
		Page<Product> page = productService.findPage(productCategory, brand, null, null, attributeValue, startPrice, endPrice, true, true, null, false, null, null, null, currentArea, null, location, distance, orderType, pageable);

		Page<ProductVo> pageVo = new Page<ProductVo>(productDisplay.convertList(page.getContent()), page.getTotal(), pageable);
		return Message.success(JsonUtils.toJson(pageVo));
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long productCategoryTagId, Long brandId, Long areaId, Long promotionId, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Location location, BigDecimal distance, Pageable pageable,
			HttpServletRequest request) {
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		List<Tag> tags = tagService.findList(tagIds);
		Area area = areaService.find(areaId);
		Page<Product> page = new Page<Product>();
		page = productService.findPage(null, brand, promotion, tags, null, startPrice, endPrice, true, true, null, false, null, null, null, area, null, location, distance, orderType, pageable);

		Page<ProductVo> pageVo = new Page<ProductVo>(productDisplay.convertList(page.getContent()), page.getTotal(), pageable);
		return Message.success(JsonUtils.toJson(pageVo));
	}

	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
	public @ResponseBody Message hits(@PathVariable Long id) {
		return Message.success(JsonUtils.toJson(productService.viewHits(id)));
	}

}