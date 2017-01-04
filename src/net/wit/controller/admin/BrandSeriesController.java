/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.ArticleCategory;
import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;
import net.wit.entity.BrandSeries.Status;
import net.wit.entity.Product;
import net.wit.service.BrandSeriesService;
import net.wit.service.BrandService;
import net.wit.util.PhoneticZhCNUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 品牌
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminBrandSeriesController")
@RequestMapping("/admin/brand/series")
public class BrandSeriesController extends BaseController {

	@Resource(name = "brandSeriesServiceImpl")
	private BrandSeriesService brandSeriesService;
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long brandId,ModelMap model) {
		Brand brand = brandService.find(brandId);
		model.addAttribute("brand", brand);
		model.addAttribute("brandSeriesTree", brandSeriesService.findTree(brand));
		model.addAttribute("statuses", Status.values());
		//model.addAttribute("brands", brandService.findAll());
		return "/admin/brand/series/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(BrandSeries brandSeries,Long parentId,Long brandId, RedirectAttributes redirectAttributes) {
		Brand brand = brandService.find(brandId);
		brandSeries.setBrand(brand);
		brandSeries.setPhonetic(PhoneticZhCNUtil.getPhoneticZh(brandSeries.getName()));
		brandSeries.setParent(brandSeriesService.find(parentId));
		brandSeries.setTreePath(null);
		brandSeries.setGrade(null);
		brandSeries.setChildren(null);
		brandSeries.setProducts(null);
		if (!isValid(brandSeries)) {
			return ERROR_VIEW;
		}
		brandSeriesService.save(brandSeries);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		BrandSeries brandSeries = brandSeriesService.find(id);
		Brand brand = brandSeries.getBrand();
		model.addAttribute("brand", brand);
		model.addAttribute("brandSeriesTree", brandSeriesService.findTree(brand));
		model.addAttribute("children", brandSeriesService.findChildren(brandSeries));
		model.addAttribute("statuses", Status.values());
		//model.addAttribute("brands", brandService.findAll());
		model.addAttribute("brandSeries",brandSeries);
		return "/admin/brand/series/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(BrandSeries brandSeries,Long parentId, Long brandId, RedirectAttributes redirectAttributes) {
		brandSeries.setParent(brandSeriesService.find(parentId));
		if (brandSeries.getParent() != null) {
			BrandSeries parent = brandSeries.getParent();
			if (parent.equals(brandSeries)) {
				return ERROR_VIEW;
			}
			List<BrandSeries> children = brandSeriesService.findChildren(parent);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		Brand brand = brandService.find(brandId);
		brandSeries.setBrand(brand);
		if (!isValid(brandSeries)) {
			return ERROR_VIEW;
		}
		brandSeriesService.update(brandSeries, "treePath", "grade", "children", "products");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long brandId,Pageable pageable, ModelMap model, RedirectAttributes redirectAttributes) {
		Brand brand =brandService.find(brandId);
		if (brand==null) {
			addFlashMessage(redirectAttributes, Message.success("请选择品牌"));
			return "redirect:../list.jhtml";	
		}
		//model.addAttribute("brands", brandService.findAll());
		model.addAttribute("brand", brand);
		model.addAttribute("brandSeriesTree", brandSeriesService.findTree(brand));
		return "/admin/brand/series/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		BrandSeries brandSeries = brandSeriesService.find(id);
		if (brandSeries == null) {
			return ERROR_MESSAGE;
		}
		Set<BrandSeries> children = brandSeries.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("不能删除存在下级结点的系列");
		}
		Set<Product> products = brandSeries.getProducts();
		if (products != null && !products.isEmpty()) {
			return Message.error("存在关联商品不能删除");
		}
		brandSeriesService.delete(id);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<BrandSeries> search(String name) {
		List<Filter> filters = new ArrayList<Filter>();
		int limit = 10000;
		if (StringUtils.isNotBlank(name)) {
			filters.add(new Filter("name", Operator.like, "%" + name + "%"));
			limit = 100;
		}
		return brandSeriesService.findList(0, limit, filters, null);
	}

}