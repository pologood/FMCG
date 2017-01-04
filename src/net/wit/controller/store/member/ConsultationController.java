/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store.member;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Consultation;
import net.wit.entity.Consultation.Type;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller - 会员中心 - 咨询
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeMemberConsultationController")
@RequestMapping("/store/member/consultation")
public class ConsultationController extends BaseController {


	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("consultation", consultationService.find(id));
		model.addAttribute("pageActive",2);
		model.addAttribute("member",member);
		return "/store/member/consultation/reply";
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
		Member member = memberService.getCurrent();
		replyConsultation.setMember(member);
		replyConsultation.setContent(content);
		replyConsultation.setIp(request.getRemoteAddr());
		replyConsultation.setType(Type.product);
		replyConsultation.setMember(memberService.getCurrent());
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
		model.addAttribute("consultation", consultationService.find(id));
		model.addAttribute("pageActive",2);
		model.addAttribute("member",member);
		return "/store/member/consultation/edit";
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
	public String manager(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("page", consultationService.findMyPage(member, null, null, pageable));
		model.addAttribute("pageActive",2);
		model.addAttribute("member",member);
		return "/store/member/consultation/manager";
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