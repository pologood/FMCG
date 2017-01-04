/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax.member;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.ajax.BaseController;
import net.wit.entity.Member;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tag;
import net.wit.service.BrandService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.TagService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxMemberProductCategoryTenantController")
@RequestMapping("/ajax/member/product_category_tenant")
public class ProductCategoryTenantController extends BaseController {

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	/**
	 * 获取根结点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public Message roots(Long productCategoryTenantTagId, Integer count) {
		Member member = memberService.getCurrent();
		Tag tag = tagService.find(productCategoryTenantTagId);
		List<ProductCategoryTenant> productCategories;
		if (tag == null) {
			productCategories = productCategoryTenantService.findRoots(count,member.getTenant());
		} else {
			productCategories = productCategoryTenantService.findRoots(tag, count,member.getTenant());
		}
		return Message.success(JsonUtils.toJson(productCategories));
	}

	/**
	 * 获取子结点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public Message childrens(Long id) {
		Member member = memberService.getCurrent();
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		if (productCategoryTenant == null) {
			return Message.error("ajax.productCategoryTenant.NotExist");
		}
		List<ProductCategoryTenant> childrens = productCategoryTenantService.findChildren(productCategoryTenant,member.getTenant());
		for (ProductCategoryTenant category : childrens) {
			category.setChildList(productCategoryTenantService.findChildren(category,member.getTenant()));
		}
		return Message.success(JsonUtils.toJson(childrens));
	}

	/**
	 * 返回父结点
	 */
	@RequestMapping(value = "/parents", method = RequestMethod.GET)
	@ResponseBody
	public Message parents(Long id, Integer count) {
		Member member = memberService.getCurrent();
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		return Message.success(JsonUtils.toJson(productCategoryTenantService.findParents(productCategoryTenant, count,member.getTenant())));
	}

}