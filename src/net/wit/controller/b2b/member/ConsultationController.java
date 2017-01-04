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
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Consultation;
import net.wit.entity.Member;
import net.wit.service.ConsultationService;
import net.wit.service.MemberService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 咨询
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberConsultationController")
@RequestMapping("/b2b/member/consultation")
public class ConsultationController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("member", member);
		model.addAttribute("page", consultationService.findPage(null, member, null, null, pageable));
		return "b2b/member/consultation/list";
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("consultation", consultationService.find(id));
		return "b2b/member/consultation/reply";
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(Long id, String content, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(Consultation.class, "content", content)) {
			return ERROR_VIEW;
		}
		Consultation consultation = consultationService.find(id);
		if (consultation == null) {
			return ERROR_VIEW;
		}
		Consultation replyConsultation = new Consultation();
		replyConsultation.setContent(content);
		replyConsultation.setIp(request.getRemoteAddr());
		consultationService.reply(consultation, replyConsultation);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:reply.jhtml?id=" + id;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("consultation", consultationService.find(id));
		return "b2b/member/consultation/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Long id, @RequestParam(defaultValue = "false") Boolean isShow, RedirectAttributes redirectAttributes) {
		Consultation consultation = consultationService.find(id);
		if (consultation == null) {
			return ERROR_VIEW;
		}
		if (isShow != consultation.getIsShow()) {
			consultation.setIsShow(isShow);
			consultationService.update(consultation);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:manager.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member.getTenant() == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("member", member);
		model.addAttribute("page", consultationService.findMyPage(member, null, null, pageable));
		return "b2b/member/consultation/manager";
	}

	/**
	 * 删除回复
	 */
	@RequestMapping(value = "/delete_reply", method = RequestMethod.POST)
	public @ResponseBody Message deleteReply(Long id) {
		Consultation consultation = consultationService.find(id);
		if (consultation == null || consultation.getForConsultation() == null) {
			return ERROR_MESSAGE;
		}
		consultationService.delete(consultation);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		if (ids != null) {
			consultationService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}