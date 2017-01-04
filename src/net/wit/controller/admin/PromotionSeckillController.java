/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Brand;
import net.wit.entity.Coupon;
import net.wit.entity.GiftItem;
import net.wit.entity.MemberRank;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.entity.Promotion;
import net.wit.entity.PromotionProduct;
import net.wit.service.BrandService;
import net.wit.service.CouponService;
import net.wit.service.MemberRankService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.util.FreemarkerUtils;

/**
 * Controller - 秒杀
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminPromotionSeckillController")
@RequestMapping("/admin/promotion/seckill")
public class PromotionSeckillController extends BaseController {

	@Resource(name = "promotionServiceImpl")
	protected PromotionService promotionService;

	@Resource(name = "memberRankServiceImpl")
	protected MemberRankService memberRankService;

	@Resource(name = "productCategoryServiceImpl")
	protected ProductCategoryService productCategoryService;

	@Resource(name = "couponServiceImpl")
	protected CouponService couponService;
	
	@Resource(name = "brandServiceImpl")
	protected BrandService brandService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Filter filter = new Filter("type", Operator.eq, Promotion.Type.seckill);
		pageable.getFilters().add(filter);
		model.addAttribute("page", promotionService.findPage(pageable));
		return "/admin/promotion/seckill/list";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("productCategories", productCategoryService.findAll());
		return "/admin/promotion/seckill/add";
	}
	
	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Promotion promotion, Long[] productCategoryIds, Long[] productIds, RedirectAttributes redirectAttributes, Boolean isDoublePoint) {
		promotion.setProductCategories(new HashSet<ProductCategory>(productCategoryService.findList(productCategoryIds)));
		for (Product product : productService.findList(productIds)) {
			if (!product.getIsGift()) {
				promotion.getProducts().add(product);
			}
		}
		for (Iterator<PromotionProduct> iterator = promotion.getPromotionProducts().iterator(); iterator.hasNext();) {
			PromotionProduct promotionProduct = iterator.next();
			if (promotionProduct == null || promotionProduct.getProduct() == null || promotionProduct.getProduct().getId() == null) {
				iterator.remove();
			} else {
				promotionProduct.setProduct(productService.find(promotionProduct.getProduct().getId()));
				promotionProduct.setPromotion(promotion);
			}
		}
		for (Iterator<GiftItem> iterator = promotion.getGiftItems().iterator(); iterator.hasNext();) {
			GiftItem giftItem = iterator.next();
			if (giftItem == null || giftItem.getGift() == null || giftItem.getGift().getId() == null) {
				iterator.remove();
			} else {
				giftItem.setGift(productService.find(giftItem.getGift().getId()));
				giftItem.setPromotion(promotion);
			}
		}
		if (!isValid(promotion)) {
			return ERROR_VIEW;
		}
		if (promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate())) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumQuantity() != null && promotion.getMaximumQuantity() != null && promotion.getMinimumQuantity() > promotion.getMaximumQuantity()) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumPrice() != null && promotion.getMaximumPrice() != null && promotion.getMinimumPrice().compareTo(promotion.getMaximumPrice()) > 0) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(promotion.getPriceExpression())) {
			try {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("quantity", 111);
				model.put("price", new BigDecimal(9.99));
				new BigDecimal(FreemarkerUtils.process("#{(" + promotion.getPriceExpression() + ");M50}", model));
			} catch (Exception e) {
				return ERROR_VIEW;
			}
		}
		if (isDoublePoint!=null&&isDoublePoint) {
			try {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("quantity", 111);
				model.put("point", 999L);
				promotion.setPointExpression("point*2");
				Double.valueOf(FreemarkerUtils.process("#{("+ promotion.getPointExpression() +");M50}", model)).longValue();
			} catch (Exception e) {
				return ERROR_VIEW;
			}
		}
		promotionService.save(promotion);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("promotion", promotionService.find(id));
		model.addAttribute("productCategories", productCategoryService.findAll());
		return "/admin/promotion/seckill/edit";
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Promotion promotion, Long[] productCategoryIds,Long[] productIds, Boolean isDoublePoint, RedirectAttributes redirectAttributes) {
		promotion.setProductCategories(new HashSet<ProductCategory>(productCategoryService.findList(productCategoryIds)));
		for (Product product : productService.findList(productIds)) {
			if (!product.getIsGift()) {
				promotion.getProducts().add(product);
			}
		}
		for (Iterator<PromotionProduct> iterator = promotion.getPromotionProducts().iterator(); iterator.hasNext();) {
			PromotionProduct promotionProduct = iterator.next();
			if (promotionProduct == null || promotionProduct.getProduct() == null || promotionProduct.getProduct().getId() == null) {
				iterator.remove();
			} else {
				promotionProduct.setProduct(productService.find(promotionProduct.getProduct().getId()));
				promotionProduct.setPromotion(promotion);
			}
		}
		for (Iterator<GiftItem> iterator = promotion.getGiftItems().iterator(); iterator.hasNext();) {
			GiftItem giftItem = iterator.next();
			if (giftItem == null || giftItem.getGift() == null || giftItem.getGift().getId() == null) {
				iterator.remove();
			} else {
				giftItem.setGift(productService.find(giftItem.getGift().getId()));
				giftItem.setPromotion(promotion);
			}
		}
		if (promotion.getBeginDate() != null && promotion.getEndDate() != null && promotion.getBeginDate().after(promotion.getEndDate())) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumQuantity() != null && promotion.getMaximumQuantity() != null && promotion.getMinimumQuantity() > promotion.getMaximumQuantity()) {
			return ERROR_VIEW;
		}
		if (promotion.getMinimumPrice() != null && promotion.getMaximumPrice() != null && promotion.getMinimumPrice().compareTo(promotion.getMaximumPrice()) > 0) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(promotion.getPriceExpression())) {
			try {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("quantity", 111);
				model.put("price", new BigDecimal(9.99));
				System.out.println(new BigDecimal(FreemarkerUtils.process("#{(price*" + promotion.getPriceExpression() + ");M50}", model)));
			} catch (Exception e) {
				return ERROR_VIEW;
			}
		}
		if (isDoublePoint!=null&&isDoublePoint) {
			try {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("quantity", 111);
				model.put("point", 999L);
				promotion.setPointExpression("point*2");
				Double.valueOf(FreemarkerUtils.process("#{(" + promotion.getPointExpression() + ");M50}", model)).longValue();
			} catch (Exception e) {
				return ERROR_VIEW;
			}
		}
		promotionService.update(promotion);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		promotionService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 商品选择
	 */
	@RequestMapping(value = "/product_select", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> productSelect(String q) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotEmpty(q)) {
			List<Product> products = productService.search(q, false, null);
			for (Product product : products) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", product.getId());
				map.put("sn", product.getSn());
				map.put("fullName", product.getFullName());
				map.put("path", product.getPath());
				data.add(map);
			}
		}
		return data;
	}

	/**
	 * 赠品选择
	 */
	@RequestMapping(value = "/gift_select", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> giftSelect(String q) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotEmpty(q)) {
			List<Product> products = productService.search(q, true, null);
			for (Product product : products) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", product.getId());
				map.put("sn", product.getSn());
				map.put("fullName", product.getFullName());
				map.put("path", product.getPath());
				data.add(map);
			}
		}
		return data;
	}

}