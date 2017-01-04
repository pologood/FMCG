/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2c;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ProductListModel;
import net.wit.controller.app.model.TenantListModel;
import net.wit.entity.Consumer;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.Consumer.Status;
import net.wit.service.ConsumerService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.TenantService;

/**
 * Controller - 商品收藏
 * 
 * @author rsico Teamc
 * @version 3.0
 */
@Controller("appB2cFavoriteController")
@RequestMapping("/app/b2c/favorite")
public class FavoriteController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	/**
	 * 收藏
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock add(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID无效");
		}
		if (member.getFavoriteProducts().contains(product)) {
			return DataBlock.warn("shop.member.favorite.exist");
		}
		if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteProducts().size() >= Member.MAX_FAVORITE_COUNT) {
			return DataBlock.warn("shop.member.favorite.addCountNotAllowed", Member.MAX_FAVORITE_COUNT);
		}
		member.getFavoriteProducts().add(product);
		memberService.update(member);
		return DataBlock.success(member.getFavoriteProducts().size(),"执行成功");
	}


	/**
	 * 判断是否收
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public @ResponseBody DataBlock status(Long id) {
		Member member = memberService.getCurrent();
		Product product = productService.find(id);
		if (member!=null && product!=null) {
			return DataBlock.success(member.getFavoriteProducts().contains(product),"执行成功");
		} else {
			return DataBlock.success(false,"执行成功");
		}
	}

	/**
	 * 关注商品
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Page<Product> page = productService.findPage(member, pageable);
		return DataBlock.success(ProductListModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 取消收藏
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock delete(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品id无效");
		}
		if (!member.getFavoriteProducts().contains(product)) {
			return DataBlock.error("已经取消关注了");
		}
		member.getFavoriteProducts().remove(product);
		memberService.update(member);
		return DataBlock.success(member.getFavoriteProducts().size(),"执行成功");
	}

	
}