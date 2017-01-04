/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.ArticleCategory;
import net.wit.entity.Consultation;
import net.wit.entity.Consultation.Type;
import net.wit.entity.Member;
import net.wit.entity.ProductChannel;
import net.wit.service.ArticleCategoryService;
import net.wit.service.ArticleService;
import net.wit.service.ConsultationService;
import net.wit.service.MemberService;
import net.wit.service.ProductChannelService;
import net.wit.service.SearchService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 文章
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapArticleController")
@RequestMapping("/wap/article")
public class ArticleController extends BaseController {


	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{productChannelId}", method = RequestMethod.GET)
	public String list(@PathVariable Long productChannelId, Long articleCategoryId, Pageable pageable, ModelMap model) {
		ProductChannel productChannel = productChannelService.find(productChannelId);
		List<ArticleCategory> articleCategorys = articleCategoryService.findListByProductChannel(productChannel);
		ArticleCategory articleCategory = null;
		if (articleCategoryId != null) {
			articleCategory = articleCategoryService.find(articleCategoryId);
		} else {
			if (!articleCategorys.isEmpty()) {
				articleCategory = articleCategorys.get(0);
			}
		}
		Set<ArticleCategory> set = new HashSet<ArticleCategory>();
		set.add(articleCategory);
		Page<Article> page = articleService.findPage(set, null, null, pageable);
		for (Article article : page.getContent()) {
			if (article.getImage() != null) {
				model.addAttribute("first", article);
			}
		}

		model.addAttribute("articleCategories", articleCategorys);
		model.addAttribute("articleCategory", articleCategory);
		model.addAttribute("pageable", pageable);
		model.addAttribute("productChannel", productChannel);
		return "/wap/article/list";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/ajax-list", method = RequestMethod.GET)
	@ResponseBody
	public Page<Article> listAjax(Long productChannelId, Long articleCategoryId, Pageable pageable) {
		ProductChannel productChannel = productChannelService.find(productChannelId);
		Set<ArticleCategory> articleCategories = productChannel.getArticleCategories();
		ArticleCategory articleCategory = null;
		if (articleCategoryId != null) {
			articleCategory = articleCategoryService.find(articleCategoryId);
		} else {
			if (!articleCategories.isEmpty()) {
				Iterator<ArticleCategory> it = articleCategories.iterator();
				articleCategory = (ArticleCategory) it.next();
			}
		}
		Set<ArticleCategory> set = new HashSet<ArticleCategory>();
		set.add(articleCategory);
		Page<Article> page = articleService.findPage(set, null, null, pageable);
		return page;
	}

	/**
	 * 搜索
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, ModelMap model) {
		Article article = articleService.find(id);
		if (article == null) {
			return ERROR_VIEW;
		}
		List<Consultation> consultations = consultationService.findListByArticle(article);
		model.addAttribute("article", article);
		model.addAttribute("consultations", consultations);
		return "wap/article/detail";
	}

	/**
	 * 搜索
	 */
	@RequestMapping(value = "/review/{id}", method = RequestMethod.GET)
	public String review(@PathVariable Long id, ModelMap model) {
		Article article = articleService.find(id);
		if (article == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("article", article);
		return "wap/article/review";
	}

	/**
	 * 搜索
	 */
	@RequestMapping(value = "/review/{id}", method = RequestMethod.POST)
	public String submit(@PathVariable Long id, String content, HttpServletRequest request) {
		Article article = articleService.find(id);
		if (article == null) {
			return ERROR_VIEW;
		}
		Consultation consultation = new Consultation();
		consultation.setArticle(article);
		consultation.setContent(content);
		consultation.setIp(request.getRemoteAddr());
		consultation.setIsShow(true);
		consultation.setType(Type.article);
		Member member = memberService.getCurrent();
		consultation.setMember(member);
		consultationService.save(consultation);
		article.getConsultations().add(consultation);
		articleService.update(article);
		return "redirect:/wap/article/detail/" + id + ".jhtml";
	}

	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
	public @ResponseBody Long hits(@PathVariable Long id) {
		return articleService.viewHits(id);
	}

}