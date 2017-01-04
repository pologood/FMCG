/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.member;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.ResourceNotFoundException;
import net.wit.Setting;
import net.wit.Setting.ReviewAuthority;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TradeModel;
import net.wit.entity.*;
import net.wit.entity.Review.Type;
import net.wit.service.*;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 评论
 * @author rsico Team
 * @version 3.0
 */
@Controller("appMemberReviewController")
@RequestMapping("/app/member/review")
public class ReviewController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;
	
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;

	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;

	/**
	 * 保存
	 * tradeId	子订单Id
	 * orderItemId	商品项Id
	 * score  商品、商家评分
	 * assistant	导购评分
	 * content	内容
	 * flag  评论对象(trade订单评论)
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock save(Long tradeId, Long orderItemId, Review review, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Setting setting = SettingUtils.get();
		if (!setting.getIsReviewEnabled()) {
			return DataBlock.error("shop.review.disabled");
		}

		for (Iterator<ProductImage> iterator = review.getImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}

		for (ProductImage productImage : review.getImages()) {
			if (productImage.getLocal() != null) {
				productImage.setLocalFile(new File(productImage.getLocal()));
				productImageService.build(productImage);
			}
		}

		if (setting.getIsReviewCheck()) {
			review.setIsShow(false);
		} else {
			review.setIsShow(true);
		}
		review.setIp(request.getRemoteAddr());
		review.setMember(member);

		if (review.getScore() >= 4) {
			review.setType(Type.positive);
		} else if (review.getScore() < 2) {
			review.setType(Type.negative);
		} else {
			review.setType(Type.moderate);
		}

		if(review.getFlag().equals(Review.Flag.trade)){
			Trade trade = tradeService.find(tradeId);
			if (trade == null) {
				return DataBlock.error("订单无效");
			}
			if(reviewService.hasReviewed(member,trade)){
				return DataBlock.error("您已经评价过该订单了");
			}
			reviewService.reviewTrade("app", member, trade, null, review);
		}
		if(review.getFlag().equals(Review.Flag.product)){
			OrderItem orderItem=orderItemService.find(orderItemId);
			if (orderItem == null) {
				return DataBlock.error("商品无效");
			}
			if(orderItem.getReview()!=null){
				return DataBlock.error("您已经评价过该商品了");
			}
			reviewService.reviewTrade("app", member, null, orderItem, review);
		}

		return DataBlock.success("success","您的评论提交成功");
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody DataBlock view(Long tradeId){
		Trade trade = tradeService.find(tradeId);
		if (trade == null) {
			return DataBlock.error("订单无效");
		}

		Map<String,Object> map=new HashMap<>();

		List<Map<String,Object>> orderItems=new ArrayList<>();
		for(OrderItem orderItem:trade.getOrderItems()){
			Map<String,Object> orderItemMap=new HashMap<>();
			orderItemMap.put("name",orderItem.getFullName());
			orderItemMap.put("isReview",orderItem.getReview()!=null);
			orderItemMap.put("thumbnail",orderItem.getThumbnail());
			orderItemMap.put("orderItemId",orderItem.getId());
			orderItems.add(orderItemMap);
		}
		map.put("orderItems",orderItems);

		Map<String,Object> tenant=new HashMap<>();
		Review review=trade.getMemberReview();
		tenant.put("score",review!=null?review.getScore():null);
		map.put("tenant",tenant);

		Map<String,Object> guide=new HashMap<>();
		Member extension=trade.getOrder().getExtension();
		if(extension==null){
			guide=null;
		}else{
			guide.put("headImg",extension.getHeadImg());
			guide.put("name",extension.getDisplayName());
			guide.put("assistant",review!=null?review.getAssistant():null);
		}
		map.put("guide",guide);

		return DataBlock.success(map,"执行成功");

	}

	/**
	 * 订单中待评价商品
	 * @param tradeId	子订单Id
     */
	@RequestMapping(value = "/unreview", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock unReview(Long tradeId){
		Trade trade = tradeService.find(tradeId);
		if (trade == null) {
			return DataBlock.error("订单无效");
		}
		List<Map<String,Object>> orderItems=new ArrayList<>();
		for(OrderItem orderItem:trade.getOrderItems()){
			if(orderItem.getReview()==null){
				Map<String,Object> orderItemMap=new HashMap<>();
				orderItemMap.put("name",orderItem.getFullName());
				orderItemMap.put("thumbnail",orderItem.getThumbnail());
				orderItemMap.put("orderItemId",orderItem.getId());
				orderItems.add(orderItemMap);
			}
		}
		return DataBlock.success(orderItems,"执行成功");
	}

}