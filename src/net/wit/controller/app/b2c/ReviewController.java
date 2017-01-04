/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2c;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ReviewModel;
import net.wit.entity.Product;
import net.wit.entity.Review.Type;
import net.wit.service.CaptchaService;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.ReviewService;

/**
 * Controller - 评论
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2cReviewController")
@RequestMapping("app/b2c/review")
public class ReviewController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	/**
	 * 评价列表
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public @ResponseBody DataBlock  list(@PathVariable Long id, Pageable pageable, ModelMap model) {
		Product product = productService.find(id);
		if (product == null) {
			DataBlock.error("商品id无效");
		}
		Page page = reviewService.findPage(null, product, null, null, pageable);
		
		return DataBlock.success(ReviewModel.bindData(page.getContent()),"执行成功");
	}
	
}