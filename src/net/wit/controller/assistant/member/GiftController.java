/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.ProductListModel;
import net.wit.controller.assistant.model.PromotionBuyfreeModel;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller - 促销
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberGiftController")
@RequestMapping("/assistant/member/gift")
public class GiftController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "snServiceImpl")
	private SnService snService;
	
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name="activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;
	@Resource(name="activityRulesServiceImpl")
	private  ActivityRulesService activityRulesService;

	/**
	 * 赠品管理列表
	 * @param pageable
	 * @param request
     * @return
     */
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
		Page<Product> page = productService.findMyPage(member.getTenant(), null, null, null, null, null, null, null, null, null,null, true, null, true, null, null, OrderType.dateDesc, pageable);
        return DataBlock.success(ProductListModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 赠品管理添加
	 * @param id
	 * @param request
     * @return
     */
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

	/**
	 * 活动商品添加
	 * @param model
	 * @param request
     * @return
     */
	@RequestMapping(value = "/addBuyfree", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock addBuyfree(PromotionBuyfreeModel model, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		Product product = productService.find(model.getProductId());
		Product giftProduct = productService.find(model.getGiftProductId());
		if (product==null) {
			return DataBlock.error("无效促销商品id");
		}
		if (giftProduct==null) {
			return DataBlock.error("无效赠品商品id");
		}
		List<Promotion> promotions =promotionService.findList(Promotion.Type.buyfree,tenant,product);
		Promotion promotion = null;
		for (Promotion p:promotions) {
			//if (promotion==null) {
			//  promotion = p;
			//} else {
			promotionService.delete(p);
			//}
		}
		if (promotion==null) {
			promotion = new Promotion();
		}
		promotion.setTenant(tenant);
		promotion.setName("买"+model.getMinimumQuantity().toString()+"赠"+model.getGiftQuantity().toString());
		promotion.setTitle(model.getName());
		promotion.setMinimumQuantity(model.getMinimumQuantity());
		promotion.setMaximumQuantity(model.getMaximumQuantity());
		promotion.setMinimumPrice(null);
		promotion.setMaximumPrice(null);
		promotion.setIsFreeShipping(false);
		promotion.setIsCouponAllowed(true);
		promotion.setType(Promotion.Type.buyfree);
		promotion.setMember(member);
		promotion.setPriceExpression(null);
		promotion.setBeginDate(model.getBeginDate());
		promotion.setEndDate(model.getEndDate());
		PromotionProduct promotionProduct = new PromotionProduct();
		promotionProduct.setPrice(product.getPrice());
		promotionProduct.setProduct(product);
		promotionProduct.setPromotion(promotion);
		promotionProduct.setQuantity(0);
		promotion.getPromotionProducts().clear();
		promotion.getPromotionProducts().add(promotionProduct);
		GiftItem giftItem = new GiftItem();
		giftItem.setGift(giftProduct);
		giftItem.setQuantity(model.getGiftQuantity());
		giftItem.setPromotion(promotion);
		promotion.getGiftItems().clear();
		promotion.getGiftItems().add(giftItem);
		promotionService.save(promotion);

		if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(25L))) {
			activityDetailService.addPoint(null, tenant, activityRulesService.find(25L));
		}

		return DataBlock.success("success","提交成功");
	}

	/**
	 * 赠品管理删除
	 * @param id
	 * @param request
     * @return
     */
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