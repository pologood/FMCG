/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.HashSet;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Expert;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tag.Type;
import net.wit.service.ExpertCategoryService;
import net.wit.service.ExpertService;
import net.wit.service.MemberService;
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
@Controller("adminExpertController")
@RequestMapping("/admin/expert")
public class ExpertController extends BaseController {

	@Resource(name = "expertServiceImpl")
	private ExpertService expertService;
	@Resource(name = "expertCategoryServiceImpl")
	private ExpertCategoryService expertCategoryService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("expertCategoryTree", expertCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(Type.expert));
		return "/admin/expert/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Expert expert,Long expertCategoryId,String username, Long[] tagIds, RedirectAttributes redirectAttributes) {
		expert.setExpertCategory(expertCategoryService.find(expertCategoryId));
		expert.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		Member member = memberService.findByUsername(username);
		if (member==null) {
			addFlashMessage(redirectAttributes, Message.error("无效专家账号"));
			return "redirect:add.jhtml";
		}
		expert.setMember(member);
		if (!isValid(expert)) {
			addFlashMessage(redirectAttributes, Message.error("数据无效，请正确填写"));
			return "redirect:add.jhtml";
		}
		expertService.save(expert);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("expertCategoryTree", expertCategoryService.findTree());
		model.addAttribute("tags", tagService.findList(Type.expert));
		model.addAttribute("expert", expertService.find(id));
		return "/admin/expert/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Expert expert, Long areaId, Long expertCategoryId,String username, Long[] tagIds, MultipartFile file, RedirectAttributes redirectAttributes) {
		expert.setExpertCategory(expertCategoryService.find(expertCategoryId));
		expert.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		Member member = memberService.findByUsername(username);
		if (member==null) {
			addFlashMessage(redirectAttributes, Message.error("无效专家账号"));
			return "redirect:add.jhtml";
		}
		expert.setMember(member);
		if (!isValid(expert)) {
			addFlashMessage(redirectAttributes, Message.error("数据无效，请正确填写"));
			return "redirect:add.jhtml";
		}
		expertService.update(expert);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", expertService.findPage(pageable));
		return "/admin/expert/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		expertService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}