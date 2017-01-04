/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Review;
import net.wit.entity.Review.Type;
import net.wit.entity.Trade;
import net.wit.service.MemberService;
import net.wit.service.ReviewService;

import net.wit.service.TradeService;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 评论
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberReviewController")
@RequestMapping("/b2b/member/review")
public class ReviewController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", reviewService.findPage(member, null, null, null, pageable));
		return "b2c/member/review/list";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("review", reviewService.find(id));
		return "b2b/member/review/edit";
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
		if  (member.getTenant()==null) {
	    	return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("type", type);
		model.addAttribute("types", Type.values());
		model.addAttribute("page", reviewService.findTenantPage(member.getTenant(), type, null, pageable));
		return "b2b/member/review/manager";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		reviewService.delete(ids);
		return SUCCESS_MESSAGE;
	}


	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Message save(Long tradeId, Review review, HttpServletRequest request) {
		Setting setting = SettingUtils.get();
		if (!setting.getIsReviewEnabled()) {
			return Message.error("shop.review.disabled");
		}
		if (review == null || !isValid(Review.class, "score", review.getScore()) || !isValid(Review.class, "content", review.getContent()) || !isValid(Review.class, "assistant", review.getAssistant())) {
			return ERROR_MESSAGE;
		}
		Trade trade = tradeService.find(tradeId);
		if (trade == null) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (setting.getReviewAuthority() != Setting.ReviewAuthority.anyone && member == null) {
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
		review.setIp(request.getRemoteAddr());
		reviewService.reviewTrade("b2b", member, trade, null,review);
		return Message.success("shop.review.success");
	}
}