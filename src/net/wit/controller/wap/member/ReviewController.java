/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap.member;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.ResourceNotFoundException;
import net.wit.Setting;
import net.wit.Setting.ReviewAuthority;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.wap.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.ProductImage;
import net.wit.entity.Review;
import net.wit.entity.Review.Type;
import net.wit.entity.Trade;
import net.wit.service.MemberService;
import net.wit.service.ProductImageService;
import net.wit.service.ProductService;
import net.wit.service.ReviewService;
import net.wit.service.TradeService;
import net.wit.util.SettingUtils;

/**
 * Controller - 会员中心 - 评论
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapMemberReviewController")
@RequestMapping("/wap/member/review")
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
	/**
	 * 保存
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock save_comment(Long tradeId, String[] url_list,Review review, HttpServletRequest request) {
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
		
		List<ProductImage> productImages=new ArrayList<ProductImage>();

		if(url_list!=null){
			for(int i= 0;i<url_list.length;i++){
				ProductImage productImage=new ProductImage();
				productImage.setLocal(url_list[i]);
				productImages.add(productImage);
			}
			review.setImages(productImages);
			for (ProductImage productImage : review.getImages()) {
				if (productImage.getLocal() != null) {
					productImage.setLocalFile(new File(productImage.getLocal()));
					productImageService.build(productImage);
				}
			}
		}

		if (review == null || !isValid(Review.class, "score", review.getScore()) || !isValid(Review.class, "content", review.getContent()) || !isValid(Review.class, "assistant", review.getAssistant())) {
			return DataBlock.error("传入的参数无效");
		}
		
		
		Trade trade = tradeService.find(tradeId);
		if (trade == null) {
			return DataBlock.error("订单无效");
		}

		if (setting.getIsReviewCheck()) {
			review.setIsShow(false);
		} else {
			review.setIsShow(true);
		}
		review.setIp(request.getRemoteAddr());
		reviewService.reviewTrade("weixin", member, trade, null,review);
		return DataBlock.success("success","shop.review.success");
	}

	/**
	 * 发表
	 */
	@RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
	public String add(@PathVariable Long id, ModelMap model) {
		Setting setting = SettingUtils.get();
		if (!setting.getIsReviewEnabled()) {
			throw new ResourceNotFoundException();
		}
		Product product = productService.find(id);
		if (product == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("product", product);
		return "/wap/member/review/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Message save(Long id, Review review, HttpServletRequest request) {
		Setting setting = SettingUtils.get();
		if (!setting.getIsReviewEnabled()) {
			return Message.error("shop.review.disabled");
		}
		if (review == null || !isValid(Review.class, "score", review.getScore()) || !isValid(Review.class, "content", review.getContent()) || !isValid(Review.class, "assistant", review.getAssistant())) {
			return ERROR_MESSAGE;
		}
		Trade trade = tradeService.find(id);
		if (trade == null) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (setting.getReviewAuthority() != ReviewAuthority.anyone && member == null) {
			return Message.error("shop.review.accessDenied");
		}
		if (reviewService.hasReviewed(member, trade)) {
			return Message.error("shop.review.reviewed");
		}
		if (setting.getIsReviewCheck()) {
			review.setIsShow(false);
		} else {
			review.setIsShow(true);
		}
		reviewService.reviewTrade("weixin", member, trade, null,review);
		return Message.success("shop.review.success");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", reviewService.findPage(member, null, null, null, pageable));
		return "wap/member/review/list";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("review", reviewService.find(id));
		return "wap/member/review/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Long id, @RequestParam(defaultValue = "false") Boolean isShow, RedirectAttributes redirectAttributes) {
		Review review = reviewService.find(id);
		if (review == null) {
			return ERROR_VIEW;
		}
		review.setIsShow(isShow);
		reviewService.update(review);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:manager.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String list(Type type, Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member.getTenant() == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("type", type);
		model.addAttribute("types", Type.values());
		model.addAttribute("page", reviewService.findPage(null, null, type, null, pageable));
		return "wap/member/review/manager";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		reviewService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}