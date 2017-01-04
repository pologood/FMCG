/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import javax.annotation.Resource;

import net.wit.Template;
import net.wit.service.StaticService;
import net.wit.service.TemplateService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - Sitemap
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminSitemapController")
@RequestMapping("/admin/sitemap")
public class SitemapController extends BaseController {

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	/**
	 * 生成Sitemap
	 */
	@RequestMapping(value = "/build", method = RequestMethod.GET)
	public String build(ModelMap model) {
		Template sitemapIndexTemplate = templateService.get("sitemapIndex");
		model.addAttribute("sitemapIndexPath", sitemapIndexTemplate.getStaticPath());
		return "/admin/sitemap/build";
	}

	/**
	 * 生成Sitemap
	 */
	@RequestMapping(value = "/build", method = RequestMethod.POST)
	public String build(RedirectAttributes redirectAttributes) {
		staticService.buildSitemap();
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:build.jhtml";
	}

}