/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.*;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.entity.Brand;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.entity.Tag;
import net.wit.entity.Tag.Type;
import net.wit.service.BrandService;
import net.wit.service.ProductCategoryService;
import net.wit.service.TagService;

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
@Controller("adminProductCategoryController")
@RequestMapping("/admin/product_category")
public class ProductCategoryController extends BaseController {

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.productCategory));
		model.addAttribute("rootCategory",productCategoryService.findRoots());
		return "/admin/product_category/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProductCategory productCategory, Long parentId, Long[] tagIds, Long[] brandIds, RedirectAttributes redirectAttributes) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}
		productCategory.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		productCategory.setTreePath(null);
		productCategory.setGrade(null);
		productCategory.setChildren(null);
		productCategory.setProducts(null);
		productCategory.setParameterGroups(null);
		productCategory.setAttributes(null);
		productCategory.setPromotions(null);
		productCategoryService.save(productCategory);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(id);
		List<ProductCategory> rootCategory=productCategoryService.findRoots();
		model.addAttribute("rootCategory",rootCategory);
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("productCategory", productCategory);
		model.addAttribute("children", productCategoryService.findChildren(productCategory));
		model.addAttribute("tags", tagService.findList(Type.productCategory));
		return "/admin/product_category/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ProductCategory productCategory, Long parentId, Long[] tagIds, Long[] brandIds, RedirectAttributes redirectAttributes) {
		productCategory.setParent(productCategoryService.find(parentId));
		productCategory.setBrands(new HashSet<Brand>(brandService.findList(brandIds)));
		productCategory.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(productCategory)) {
			return ERROR_VIEW;
		}
		if (productCategory.getParent() != null) {
			ProductCategory parent = productCategory.getParent();
			if (parent.equals(productCategory)) {
				return ERROR_VIEW;
			}
			List<ProductCategory> children = productCategoryService.findChildren(parent);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		productCategoryService.update(productCategory, "treePath", "grade", "children", "products", "parameterGroups", "attributes", "promotions");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {

		List<ProductCategory> productCategories = productCategoryService.findTree();

		List<ProductCategory> productCategories1 = new ArrayList<>();
		List<ProductCategory> productCategories2 = new ArrayList<>();
		List<ProductCategory> productCategories3 = new ArrayList<>();
		for(ProductCategory productCategory:productCategories){
			if(productCategory.getGrade()==0){
				productCategories1.add(productCategory);
			}

			if(productCategory.getGrade()==1){
				productCategories2.add(productCategory);
			}

			if(productCategory.getGrade()==2){
				productCategories3.add(productCategory);
			}
		}
		List<Map<String,Object>> mapList =new ArrayList<>();
		for(ProductCategory productCategory1:productCategories1){
			Map<String,Object> map = new HashMap<>();
			map.put("name",productCategory1.getName());
			map.put("image",productCategory1.getImage());
			map.put("code",productCategory1.getId().toString());
			map.put("assignee",productCategory1.getOrder()==null?"-":productCategory1.getOrder().toString());
			List<Map<String,Object>> mapList1 =new ArrayList<>();
			for(ProductCategory productCategory2:productCategories2){
				if(productCategory2.getParent().equals(productCategory1)){
					Map<String,Object> map1 = new HashMap<>();
					map1.put("name",productCategory2.getName());
					map1.put("image",productCategory2.getImage());
					map1.put("code",productCategory2.getId().toString());
					map1.put("assignee",productCategory2.getOrder()==null?"-":productCategory2.getOrder().toString());
					List<Map<String,Object>> mapList2 =new ArrayList<>();
					for(ProductCategory productCategory3:productCategories3){

						if(productCategory3.getParent().equals(productCategory2)){
							Map<String,Object> map3 = new HashMap<>();
							map3.put("name",productCategory3.getName());
							map3.put("image",productCategory3.getImage());
							map3.put("code",productCategory3.getId().toString());
							map3.put("assignee",productCategory3.getOrder()==null?"-":productCategory3.getOrder().toString());
							//map3.put("children",new ArrayList<>());
							mapList2.add(map3);
						}
					}
					if(mapList2.size()>0){
						map1.put("children",mapList2);
					}
					mapList1.add(map1);
				}
			}
			if(mapList1.size()>0){
				map.put("children",mapList1);
			}

			mapList.add(map);
		}
		model.addAttribute("productCategoryTree", JSONArray.fromObject(mapList));
		return "/admin/product_category/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return ERROR_MESSAGE;
		}
		Set<ProductCategory> children = productCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistChildrenNotAllowed");
		}
		Set<Product> products = productCategory.getProducts();
		if (products != null && !products.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistProductNotAllowed");
		}
		productCategoryService.delete(id);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<ProductCategory> search(String name) {
		List<Filter> filters = new ArrayList<Filter>();
		int limit = 10000;
		if (StringUtils.isNotBlank(name)) {
			filters.add(new Filter("name", Operator.like, "%" + name + "%"));
			limit = 100;
		}
		return productCategoryService.findList(limit, filters, null);
	}

}