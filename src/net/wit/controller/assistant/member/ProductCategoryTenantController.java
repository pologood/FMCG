/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.ProductCategoryTenantModel;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tenant;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberProductCategoryTenantController")
@RequestMapping("/assistant/member/product_category_tenant")
public class ProductCategoryTenantController extends BaseController {

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots(Long tenantId) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		List<ProductCategoryTenant> productCategories;
		productCategories = productCategoryTenantService.findRoots(tenant);
		return DataBlock.success(ProductCategoryTenantModel.bindData(productCategories),"执行成功");
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock childrens(Long tenantId,Long id) {
		Tenant tenant = tenantService.find(tenantId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		if (productCategoryTenant == null) {
			return DataBlock.error("传入的上级结点ID无效");
		}
		List<ProductCategoryTenant> childrens = productCategoryTenantService.findChildren(productCategoryTenant,tenant);
		return DataBlock.success(ProductCategoryTenantModel.bindData(childrens),"执行成功");
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock parents(Long tenantId,Long id) {
		Tenant tenant = tenantService.find(tenantId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		if (productCategoryTenant == null) {
			return DataBlock.error("传入的上级结点ID无效");
		}
		List<ProductCategoryTenant> parents = productCategoryTenantService.findParents(productCategoryTenant,tenant);
		return DataBlock.success(ProductCategoryTenantModel.bindData(parents),"执行成功");
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock save(ProductCategoryTenant productCategoryTenant, Long parentId,Long[] tagIds, Long[] brandIds, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		productCategoryTenant.setParent(productCategoryTenantService.find(parentId));
		if (!isValid(productCategoryTenant)) {
			return DataBlock.error("无效参数");
		}
		productCategoryTenant.setTreePath(null);
		productCategoryTenant.setGrade(null);
		productCategoryTenant.setChildren(null);
		productCategoryTenant.setProducts(null);
		productCategoryTenant.setTenant(tenant);
		productCategoryTenantService.save(productCategoryTenant);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return DataBlock.success("success","保存成功");
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(ProductCategoryTenant productCategoryTenant, Long parentId, Long[] tagIds, Long[] brandIds, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		productCategoryTenant.setParent(productCategoryTenantService.find(parentId));
		productCategoryTenant.getId();
		if (!isValid(productCategoryTenant)) {
			return DataBlock.error("无效参数");
		}
		if (productCategoryTenant.getParent() != null) {
			ProductCategoryTenant parent = productCategoryTenant.getParent();
			if (parent.equals(productCategoryTenant)) {
				return DataBlock.error("无效参数");
			}
			List<ProductCategoryTenant> children = productCategoryTenantService.findChildren(parent,tenant);
			if (children != null && children.contains(parent)) {
				return DataBlock.error("无效参数");
			}
		}
		productCategoryTenantService.update(productCategoryTenant, "treePath", "grade", "children", "products", "parameterGroups", "attributes", "promotions","tenant");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return DataBlock.success("success","保存成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock delete(Long[] ids) {
		List<ProductCategoryTenant> productCategories = productCategoryTenantService.findList(ids);
		for (ProductCategoryTenant productCategoryTenant:productCategories) {
			if (productCategoryTenant == null) {
				return DataBlock.error("无效id");
			}
			Set<ProductCategoryTenant> children = productCategoryTenant.getChildren();
			if (children != null && !children.isEmpty()) {
				return DataBlock.error("有下级分类不能删除");
			}
			Set<Product> products = productCategoryTenant.getProducts();
			if (products != null && !products.isEmpty()) {
				return DataBlock.error("有商品的分类不能删除");
			}
			productCategoryTenantService.delete(productCategoryTenant);
		}
		return DataBlock.success("success","删除成功");
	}
	/**
	 * 分类移动
	 */
	@RequestMapping(value = "/move", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock mve(Long[] ids,Long id) {
		List<Product> products = productService.findList(ids);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		for (Product product:products) {
			product.setProductCategoryTenant(productCategoryTenant);
			productService.update(product);
		}
		return DataBlock.success("success","更新成功");
	}
}