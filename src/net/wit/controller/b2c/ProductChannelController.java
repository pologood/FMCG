/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.Attribute;
import net.wit.entity.Brand;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductChannel;
import net.wit.entity.Promotion;
import net.wit.entity.Tag;
import net.wit.entity.TenantCategory;
import net.wit.service.AreaService;
import net.wit.service.ArticleService;
import net.wit.service.BrandService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductChannelService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SearchService;
import net.wit.service.TagService;
import net.wit.service.TenantCategoryService;
import net.wit.service.TenantService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cProductChannelController")
@RequestMapping("/b2c/productChannel")
public class ProductChannelController extends BaseController {

	public final static String DEFAULT_CURRENTPAGE = "product";

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	/**
	 * 列表-频道
	 */
	@RequestMapping(value = "/list/{productChannelId}", method = RequestMethod.GET)
	public String listByChannel(@PathVariable Long productChannelId, Long productCategoryId, Long tenantCategoryId, Long productCategoryTagId, Long brandId, Long areaId, Long promotionId, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice,
			Boolean scrollerYesOrNot, OrderType orderType, Integer productPageNumber, Integer tenantPageNumber, Integer articlePageNumber, Integer pageSize, String currentPage, HttpServletRequest request, ModelMap model) {
		try {

			Brand brand = brandService.find(brandId);
			Area area = areaService.find(areaId);
			Promotion promotion = promotionService.find(promotionId);
			ProductCategory productCategory = productCategoryService.find(productCategoryId);
			TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
			List<Tag> tags = tagService.findList(tagIds);
			Pageable productPageable = new Pageable(productPageNumber, pageSize);
			Pageable tenantPageable = new Pageable(tenantPageNumber, pageSize);
			Pageable articlePageable = new Pageable(articlePageNumber, pageSize);
			if (area == null) {
				area = areaService.getCurrent();
			}
			ProductChannel channel = productChannelService.find(productChannelId);
			Set<ProductCategory> productCategorys = new HashSet<ProductCategory>();
			Set<TenantCategory> tenantCategories = new HashSet<TenantCategory>();
			if (channel == null) {
				return ERROR_VIEW;
			}
			Map<Attribute, String> attributeValue = new HashMap<Attribute, String>();
			if (productCategory == null) {
				productCategorys = channel.getProductCategorys();
			} else {
				Set<Attribute> attributes = productCategory.getAttributes();
				for (Attribute attribute : attributes) {
					String value = request.getParameter("attribute_" + attribute.getId());
					if (StringUtils.isNotEmpty(value) && attribute.getOptions().contains(value)) {
						attributeValue.put(attribute, value);
					}
				}
				productCategorys.add(productCategory);
			}
			if (tenantCategory == null) {
				tenantCategories = channel.getTenantCategorys();
			} else {
				tenantCategories.add(tenantCategory);
			}
			if (currentPage == null) {
				currentPage = DEFAULT_CURRENTPAGE;
			}
			model.addAttribute("currentArea", areaService.getCurrent());
			model.addAttribute("orderTypes", OrderType.values());
			model.addAttribute("brand", brand);
			model.addAttribute("area", area);
			model.addAttribute("promotion", promotion);
			model.addAttribute("tags", tags);
			model.addAttribute("startPrice", startPrice);
			model.addAttribute("endPrice", endPrice);
			model.addAttribute("orderType", orderType);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("channel", channel);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("attributeValue", attributeValue);
			model.addAttribute("productCategory", productCategory);
			model.addAttribute("scrollerYesOrNot", scrollerYesOrNot);
			model.addAttribute("tenantCategory", tenantCategory);
			model.addAttribute("productCategoryTagId", tagService.find(productCategoryTagId));
			model.addAttribute("productPage", productService.findPageByChannel(productCategorys, brand, promotion, tags, null, startPrice, endPrice, true, true, null, false, null, null, null, area, false, orderType, null, null, productPageable));
			model.addAttribute("tenantPage", tenantService.findPage(tenantCategories, null, area, null, null, null, null, tenantPageable));
			model.addAttribute("articlePage", articleService.findPage(channel.getArticleCategories(), null, area, articlePageable));
			return "b2c/channel/" + channel.getTemplateId();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR_VIEW;
		}
	}
}