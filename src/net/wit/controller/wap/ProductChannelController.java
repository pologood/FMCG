/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TenantListModel;
import net.wit.controller.wap.model.ProductCategoryModel;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商品分类
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapProductChannelController")
@RequestMapping("/wap/product_channel")
public class ProductChannelController extends BaseController {

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@RequestMapping(value = "/channel/{id}", method = RequestMethod.GET)
	public String channelNew(@PathVariable Long id,ModelMap model){
		ProductChannel productChannel = productChannelService.find(id);
//		if (productChannel.getAdPositions().size() > 0) {
//			model.addAttribute("adPosition", productChannel.getDefaultAdPosition());
//		}
		model.addAttribute("productChannel",productChannel);
		model.addAttribute("areaId",areaService.getCurrent().getId());
		return "wap/product_channel/channel_page_v3";
	}

	/** 频道详细页 */
	@RequestMapping(value = "/{id}/index", method = RequestMethod.GET)
	public String index(@PathVariable Long id, Long productCategoryId, Long[] tagsIds, Pageable pageable,
			ModelMap model) {
		ProductChannel productChannel = productChannelService.find(id);
		// if (productChannel.getTemplate().getType() ==
		// net.wit.Template.Type.channel_product) {
		Set<ProductCategory> productCategorys = new HashSet<ProductCategory>();
		if (productCategoryId != null) {
			productCategorys.add(productCategoryService.find(productCategoryId));
		} else {
			productCategorys = productChannel.getProductCategorys();
		}
		ArrayList<Tag> tenantTags = new ArrayList<Tag>();
		Tag tag = tagService.find(6l);
		tenantTags.add(tag);
		if (productChannel.getAdPositions().size() > 0) {
			model.addAttribute("adPosition", productChannel.getDefaultAdPosition());
		}
		List<Community> communityList = communityService.findList(areaService.getCurrent());
		model.addAttribute("communityList", communityList);
		model.addAttribute("orderTypes", OrderType.values());
		model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
		model.addAttribute("productChannel", productChannel);
		model.addAttribute("productCategorys", productChannel.getProductCategorys());
		model.addAttribute("productCategoryId", productCategoryId);
		List<DeliveryCenter> findList = deliveryCenterService.findList(productChannel.getTenantCategorys(), tenantTags,
				areaService.getCurrent(), null, 3);
		List<DeliveryCenter> randomList = new ArrayList<DeliveryCenter>();

		Random random = new Random();
		int selected = 3;
		// 先抽取，备选数量的个数
		if (findList.size() >= selected) {
			for (int i = 0; i < selected; i++) {
				// 随机数的范围为0-list.size()-1;
				int target = random.nextInt(findList.size());
				randomList.add(findList.get(target));
				findList.remove(target);
			}
		} else {
			selected = findList.size();
			for (int i = 0; i < selected; i++) {
				// 随机数的范围为0-list.size()-1;
				int target = random.nextInt(findList.size());
				randomList.add(findList.get(target));
				findList.remove(target);
			}
		}
		model.addAttribute("deliveris", randomList);
		model.addAttribute("pageable", pageable);
		return "/wap/product_channel/" + productChannel.getTemplateId();
	}

	/** 频道详细页 */
	@RequestMapping(value = "/{id}/loadmore_product", method = RequestMethod.GET)
	@ResponseBody
	public Page<Product> loadmore_product(@PathVariable Long id, Long productCategoryId, Long[] tagsIds,
			Pageable pageable) {
		ProductChannel productChannel = productChannelService.find(id);
		Set<ProductCategory> productCategorys = new HashSet<ProductCategory>();
		if (productCategoryId != null) {
			productCategorys.add(productCategoryService.find(productCategoryId));
		} else {
			productCategorys = productChannel.getProductCategorys();
		}
		ArrayList<Tag> tenantTags = new ArrayList<Tag>();
		Tag tag = tagService.find(6l);
		tenantTags.add(tag);
		return productService.openPage(pageable,areaService.getCurrent(),productCategorys,null,null,tagService.findList(tagsIds),null,null,null,null,null,null,null,OrderType.topDesc);
	}

	/** 频道详细页 */
	@RequestMapping(value = "/{id}/loadmore_article", method = RequestMethod.GET)
	@ResponseBody
	public Page<Article> loadmore_article(@PathVariable Long id, Pageable pageable) {
		ProductChannel productChannel = productChannelService.find(id);
		Set<ArticleCategory> articleCategories = productChannel.getArticleCategories();
		List<Tag> articleTags = tagService.findList(3l, 4l);
		pageable.getOrders().add(Order.desc("createDate"));
		return articleService.findPage(articleCategories, articleTags, areaService.getCurrent(), pageable);
	}
}