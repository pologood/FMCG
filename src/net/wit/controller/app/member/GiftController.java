/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.member;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ProductListModel;
import net.wit.controller.app.model.PromotionBuyfreeModel;
import net.wit.controller.app.model.PromotionMailModel;
import net.wit.controller.app.model.PromotionSecKillModel;
import net.wit.entity.Brand;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.GiftItem;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;
import net.wit.entity.PromotionProduct;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.Product.OrderType;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SnService;
import net.wit.weixin.main.MenuManager;

/**
 * Controller - 促销
 * @author rsico Team
 * @version 3.0
 */
@Controller("appMemberGiftController")
@RequestMapping("/app/member/gift")
public class GiftController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "snServiceImpl")
	private SnService snService;
	
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Pageable pageable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant()==null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		Page<Product> page = productService.findMyPage(member.getTenant(), null, null, null, null, null, null, null, null, null,null, true, null, true, null, null, Product.OrderType.dateDesc, pageable);
        return DataBlock.success(ProductListModel.bindData(page.getContent()),"执行成功");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		
		Product product = productService.find(id);
		if (product==null) {
			return DataBlock.error("无效商品id");
		}
		product.setIsGift(true);
		productService.update(product);
		return DataBlock.success("success","添加成功");
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock delete(Long id,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		
		Product product = productService.find(id);
		if (product==null) {
			return DataBlock.error("无效商品id");
		}
		product.setIsGift(false);
		productService.update(product);
		return DataBlock.success("success","删除成功");
	}
	
}