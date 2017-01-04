/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.app.model.ProductImageModel;
import net.wit.entity.Member;
import net.wit.entity.Review;
import net.wit.entity.Review.Type;
import net.wit.service.MemberService;
import net.wit.service.ReviewService;
import net.wit.service.TenantService;

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
@Controller("helperMemberReviewController")
@RequestMapping("/helper/member/review")
public class ReviewController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", reviewService.findPage(member, null, null, null, pageable));
		model.addAttribute("pageActive",2);
		return "/helper/member/review/list";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Review review = reviewService.find(id);
		model.addAttribute("review", review);
		model.addAttribute("pageActive",2);
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("productImages", ProductImageModel.bindData(review.getImages()));
		return "/helper/member/review/edit";
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
	public String list(String searchValue,Type type, Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if  (member.getTenant()==null) {
	    	return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("type", type);
		model.addAttribute("types", Type.values());
		//model.addAttribute("page", reviewService.findTenantPage(member.getTenant(), type, null, pageable));
		model.addAttribute("page", reviewService.findMyTenantPage(searchValue,member.getTenant(), type, null, pageable));
		model.addAttribute("pageActive",2);
		model.addAttribute("member",member);
		return "/helper/member/review/manager";
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
}