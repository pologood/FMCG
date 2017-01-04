/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap.member;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.wap.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.TenantService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员中心 - 商品收藏
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapMemberFavoriteController")
@RequestMapping("/wap/member/favorite")
public class FavoriteController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 添加收藏
	 * @param id 商品/商家编号
	 * @param type 1  商家   2 商品
     * @return
     */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock add(Long id, String type) {
		if ("1".equals(type)) {
			Tenant tenant = tenantService.find(id);
			if (tenant == null) {
				return DataBlock.error("商家编号无效");
			}
			Member member = memberService.getCurrent();
			if (member.getFavoriteTenants().contains(tenant)) {
				return DataBlock.error("您已经收藏了");
			}
			if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteTenants().size() >= Member.MAX_FAVORITE_COUNT) {
				return DataBlock.error("shop.member.favorite.addCountNotAllowed", Member.MAX_FAVORITE_COUNT);
			}
			tenant.getFavoriteMembers().add(member);
			tenantService.update(tenant);
			member.getFavoriteTenants().add(tenant);
			memberService.update(member);
			return DataBlock.success(tenant.getFavoriteMembers().size(),"收藏成功");
		} else {
			Product product = productService.find(id);
			if (product == null) {
				return DataBlock.error("商品编号无效");
			}
			Member member = memberService.getCurrent();
			if (member.getFavoriteProducts().contains(product)) {
				return DataBlock.error("您已经收藏了");
			}
			if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteProducts().size() >= Member.MAX_FAVORITE_COUNT) {
				return DataBlock.error("shop.member.favorite.addCountNotAllowed", Member.MAX_FAVORITE_COUNT);
			}
			member.getFavoriteProducts().add(product);
			memberService.update(member);
			return DataBlock.success(member.getFavoriteProducts().size(),"收藏成功");
		}
	}

	/**
	 * 添加收藏
	 * @param ids 商品
	 * @param type 1  商家   2 商品
     * @return
     */
	@RequestMapping(value = "/addAll", method = RequestMethod.GET)
	public String  addAll(String ids, String type) {
		Member member = memberService.getCurrent();
		String[] idss = ids.split(",");
 		for (String id:idss) {
			Product product = productService.find(Long.valueOf(id).longValue());
            if (product!=null) {
    			if (!member.getFavoriteProducts().contains(product)) {
        			member.getFavoriteProducts().add(product);
    			}
            }
		}
		memberService.update(member);
		return "redirect:favoriteList.jhtml?type=product";

	}
	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return NO_LOGIN;
		}
		// int PAGE_SIZE=1;
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("member", member);
		model.addAttribute("page", productService.findPage(member, pageable));
		model.addAttribute("pageTenant", tenantService.findPage(member, pageable));
		return "wap/member/favorite/list";
	}

	/**
	 * 加载更多(商品)
	 */
	@RequestMapping(value = "/addMore", method = RequestMethod.GET)
	@ResponseBody
	public Page<Product> addMore(Integer pageNumber) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Product> page = productService.findPage(member, pageable);
		return page;
	}

	/**
	 * 加载更多(商家)
	 */
	@RequestMapping(value = "/addMoreTenant", method = RequestMethod.GET)
	@ResponseBody
	public Page<Tenant> addMoreTenant(Integer pageNumber) {
		Member member = memberService.getCurrent();
		// int PAGE_SIZE=1;
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Tenant> page = tenantService.findPage(member, pageable);
		return page;
	}

	/**
	 * 取消收藏
	 * @param id 商品/商家编号
	 * @param type  1   商家   2 商品
     * @return
     */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock delete(Long id, String type) {
		if ("1".equals(type)) {
			Tenant tenant = tenantService.find(id);
			if (tenant == null) {
				return DataBlock.error("商家ID无效");
			}
			Member member = memberService.getCurrent();
			if(!member.getFavoriteTenants().contains(tenant)){
				return DataBlock.error("请先收藏");
			}
			tenant.getFavoriteMembers().remove(member);
			member.getFavoriteTenants().remove(tenant);
			tenantService.update(tenant);
			memberService.update(member);
			return DataBlock.success(tenant.getFavoriteMembers().size(),"已取消收藏");
		} else {
			Product product = productService.find(id);
			if (product == null) {
				return DataBlock.error("商品ID无效");
			}
			Member member = memberService.getCurrent();
			if (!member.getFavoriteProducts().contains(product)) {
				return DataBlock.error("请先收藏");
			}
			member.getFavoriteProducts().remove(product);
			memberService.update(member);
			return DataBlock.success(member.getFavoriteProducts().size(),"已取消收藏");
		}
	}


	/**
	 * 我的收藏
	 */
	@RequestMapping(value = "/favoriteList", method = RequestMethod.GET)
	public String favoriteList(String type, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return NO_LOGIN;
		}
		Pageable pageable = new Pageable(20, PAGE_SIZE);
		model.addAttribute("member", member);
		if("product".equals(type)){
			model.addAttribute("page", productService.findPage(member, pageable));
		}else if("tenant".equals(type)){
			model.addAttribute("pageTenant", tenantService.findPage(member, pageable));
		}
		// int PAGE_SIZE=1;
		model.addAttribute("type",type);
		return "wap/member/favorite/favoriteList";
	}
}