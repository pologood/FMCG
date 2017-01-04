/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.entity.Expert;
import net.wit.entity.ExpertCategory;
import net.wit.service.ExpertCategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 文章分类
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminExpertCategoryController")
@RequestMapping("/admin/expert_category")
public class ExpertCategoryController extends BaseController {

	@Resource(name = "expertCategoryServiceImpl")
	private ExpertCategoryService expertCategoryService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("expertCategoryTree", expertCategoryService.findTree());
		return "/admin/expert_category/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ExpertCategory expertCategory, Long parentId, RedirectAttributes redirectAttributes) {
		expertCategory.setParent(expertCategoryService.find(parentId));
		if (!isValid(expertCategory)) {
			return ERROR_VIEW;
		}
		expertCategory.setTreePath(null);
		expertCategory.setGrade(null);
		expertCategory.setChildren(null);
		expertCategory.setExperts(null);
		expertCategoryService.save(expertCategory);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		ExpertCategory expertCategory = expertCategoryService.find(id);
		model.addAttribute("expertCategoryTree", expertCategoryService.findTree());
		model.addAttribute("expertCategory", expertCategory);
		model.addAttribute("children", expertCategoryService.findChildren(expertCategory));
		return "/admin/expert_category/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ExpertCategory expertCategory, Long parentId, RedirectAttributes redirectAttributes) {
		expertCategory.setParent(expertCategoryService.find(parentId));
		if (!isValid(expertCategory)) {
			return ERROR_VIEW;
		}
		if (expertCategory.getParent() != null) {
			ExpertCategory parent = expertCategory.getParent();
			if (parent.equals(expertCategory)) {
				return ERROR_VIEW;
			}
			List<ExpertCategory> children = expertCategoryService.findChildren(parent);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		expertCategoryService.update(expertCategory, "treePath", "grade", "children", "experts");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		model.addAttribute("expertCategoryTree", expertCategoryService.findTree());
		return "/admin/expert_category/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		ExpertCategory expertCategory = expertCategoryService.find(id);
		if (expertCategory == null) {
			return ERROR_MESSAGE;
		}
		Set<ExpertCategory> children = expertCategory.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.expertCategory.deleteExistChildrenNotAllowed");
		}
		Set<Expert> experts = expertCategory.getExperts();
		if (experts != null && !experts.isEmpty()) {
			return Message.error("admin.expertCategory.deleteExistExpertNotAllowed");
		}
		expertCategoryService.delete(id);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<ExpertCategory> search(String name) {
		List<Filter> filters = new ArrayList<Filter>();
		int limit = 10000;
		if (StringUtils.isNotBlank(name)) {
			filters.add(new Filter("name", Operator.like, "%" + name + "%"));
			limit = 100;
		}
		return expertCategoryService.findList(limit, filters, null);
	}

}