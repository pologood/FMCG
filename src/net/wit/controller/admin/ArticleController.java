/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.HashSet;

import javax.annotation.Resource;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.ProductImage;
import net.wit.entity.Tag;
import net.wit.entity.Tag.Type;
import net.wit.service.AreaService;
import net.wit.service.ArticleCategoryService;
import net.wit.service.ArticleService;
import net.wit.service.FileService;
import net.wit.service.ProductImageService;
import net.wit.service.TagService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 文章
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminArticleController")
@RequestMapping("/admin/article")
public class ArticleController extends BaseController {

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(Type.article));
		return "/admin/article/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Article article,Long areaId, Long articleCategoryId, Long[] tagIds, MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				article.setImage(img.getThumbnail());
			}
		}
		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		article.setArea(areaService.find(areaId));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		article.setHits(0L);
		article.setPageNumber(null);
		articleService.save(article);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(Type.article));
		model.addAttribute("article", articleService.find(id));
		return "/admin/article/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Article article, Long areaId, Long articleCategoryId, Long[] tagIds, MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:edit.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				article.setImage(img.getThumbnail());
			}
		}
		article.setArticleCategory(articleCategoryService.find(articleCategoryId));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		article.setArea(areaService.find(areaId));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		Article art = articleService.find(article.getId());
		article.setTenant(art.getTenant());
		articleService.update(article, "hits", "pageNumber");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", articleService.findPage(pageable));
		return "/admin/article/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		articleService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}