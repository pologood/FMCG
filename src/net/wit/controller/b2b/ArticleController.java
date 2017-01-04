/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Pageable;
import net.wit.ResourceNotFoundException;
import net.wit.entity.Article;
import net.wit.entity.ArticleCategory;
import net.wit.service.ArticleCategoryService;
import net.wit.service.ArticleService;
import net.wit.service.SearchService;

import org.apache.commons.lang.StringUtils;
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
@Controller("b2bArticleController")
@RequestMapping("/b2b/article")
public class ArticleController extends BaseController {
	/** 每页记录数 */
	private static final int PAGE_SIZE = 20;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	public String list(@PathVariable Long id, Integer pageNumber, ModelMap model) {
		ArticleCategory articleCategory = articleCategoryService.find(id);
		if (articleCategory == null) {
			throw new ResourceNotFoundException();
		}
		Set<ArticleCategory> articleCategories = new HashSet<ArticleCategory>();
		articleCategories.add(articleCategory);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("articleCategory", articleCategory);
		model.addAttribute("page", articleService.findPage(articleCategories, null, null, pageable));
		return "/b2b/article/list";
	}

	/**
	 * 详细页
	 */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, ModelMap model) {
		Article article = articleService.find(id);
		if (article == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("article", article);
		return "/b2b/article/content";
	}

	/**
	 * 详细页
	 */
	@RequestMapping(value = "/topic/{id}", method = RequestMethod.GET)
	public String topic(@PathVariable Long id, ModelMap model) {
		Article article = articleService.find(id);
		if (article == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("article", article);
		return "/b2b/article/topic";
	}
	
	/**
	 * 搜索
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(String keyword, Integer pageNumber, ModelMap model) {
		if (StringUtils.isEmpty(keyword)) {
			return ERROR_VIEW;
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("articleKeyword", keyword);
		model.addAttribute("page", searchService.search(keyword, pageable));
		return "b2b/article/search";
	}

	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Long hits(@PathVariable Long id) {
		return articleService.viewHits(id);
	}

}