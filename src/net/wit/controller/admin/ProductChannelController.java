/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Template;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.ArticleCategory;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductChannel;
import net.wit.entity.TenantCategory;
import net.wit.service.ArticleCategoryService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductChannelService;
import net.wit.service.TemplateService;
import net.wit.service.TenantCategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminProductChannelController")
@RequestMapping("/admin/product_channel")
public class ProductChannelController extends BaseController {

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	/** 列表 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		List<ProductChannel> channels = productChannelService.findAll();
		for (ProductChannel c : channels) {
			c.setTemplate(templateService.get(c.getTemplateId()));
		}
		model.addAttribute("productChannels", channels);
		model.addAttribute("linkTypes", LinkType.values());
		return "/admin/product_channel/list";
	}

	/** 添加 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("types", ProductChannel.Type.values());
		model.addAttribute("templates", templateService.getList(Template.Type.channel));
		return "/admin/product_channel/add";
	}

	/** 保存 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProductChannel productChannel, Long[] productCategoryIds, Long[] tenantCategoryIds, Long[] articleCategoryIds, RedirectAttributes redirectAttributes) {
		productChannel.setProductCategorys(new HashSet<ProductCategory>(productCategoryService.findList(productCategoryIds)));
		productChannel.setTenantCategorys(new HashSet<TenantCategory>(tenantCategoryService.findList(tenantCategoryIds)));
		productChannel.setArticleCategories(new HashSet<ArticleCategory>(articleCategoryService.findList(articleCategoryIds)));
		if (!isValid(productChannel)) {
			return ERROR_VIEW;
		}
		try {
			productChannelService.save(productChannel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/** 编辑 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		ProductChannel productChannel = productChannelService.find(id);
		model.addAttribute("productChannel", productChannel);
		model.addAttribute("templates", templateService.getList(Template.Type.channel));
		model.addAttribute("types", ProductChannel.Type.values());
		return "/admin/product_channel/edit";
	}

	/** 更新 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ProductChannel productChannel, Long[] productCategoryIds, Long[] tenantCategoryIds, Long[] articleCategoryIds, RedirectAttributes redirectAttributes) {
		productChannel.setProductCategorys(new HashSet<ProductCategory>(productCategoryService.findList(productCategoryIds)));
		productChannel.setTenantCategorys(new HashSet<TenantCategory>(tenantCategoryService.findList(tenantCategoryIds)));
		productChannel.setArticleCategories(new HashSet<ArticleCategory>(articleCategoryService.findList(articleCategoryIds)));
		if (!isValid(productChannel)) {
			return ERROR_VIEW;
		}
		try {
			productChannelService.update(productChannel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/** 删除 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		ProductChannel productChannel = productChannelService.find(id);
		if (productChannel == null) {
			return ERROR_MESSAGE;
		}
		productChannelService.delete(id);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<ProductChannel> search(String name) {
		List<Filter> filters = new ArrayList<Filter>();
		int limit = 10000;
		if (StringUtils.isNotBlank(name)) {
			filters.add(new Filter("name", Operator.like, "%" + name + "%"));
			limit = 100;
		}
		return productChannelService.findList(limit, filters, null);
	}

}