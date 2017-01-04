/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.Credit;
import net.wit.entity.Idcard;
import net.wit.entity.Member;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Payment.Type;
import net.wit.entity.MinshengBank;
import net.wit.service.CreditService;
import net.wit.service.MinshengTransService;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 民生银行转账
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminMinshengTransController")
@RequestMapping("/admin/minshengbanktrans")
public class MinshengTransController extends BaseController {

	@Resource(name = "creditServiceImpl")
	private CreditService creditService;

	@Resource(name = "MinshengTransServiceImpl")
	private MinshengTransService MinshengTransService;

	/**
	 * 提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(Long id, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		/**
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		Credit credit = creditService.find(id);
		MinshengBank minshengbank = new MinshengBank();
		minshengbank.setTrnId("20130704154320000255");
		minshengbank.setInsId(credit.getSn());
		minshengbank.setAcntToNo(credit.getAccount());
		minshengbank.setAcntToName(credit.getAcntToName());
		if (credit.getBank() == "") {
			minshengbank.getBankCode();
			minshengbank.setExternBank("0");
		} else {
			minshengbank.setExternBank("1");
		}
*/
		// Member member = memberService.getCurrent();
		// if (member == null) {
		// return ERROR_VIEW;
		// }
		// MinshengBank minshengbank = new MinshengBank();
		// //MinshengTransService.payToBank(minshengbank);
		// model.addAttribute("requestUrl",
		// MinshengTransService.getRequestUrl());
		// model.addAttribute("requestMethod",
		// MinshengTransService.getRequestMethod());
		// model.addAttribute("requestCharset",
		// MinshengTransService.getRequestCharset());
		// model.addAttribute("parameterMap", MinshengTransService.payToBank("",
		// MinshengTransService.getRequestUrl(), request, "/b2b"));
		// if (StringUtils.isNotEmpty(MinshengTransService.getRequestCharset()))
		// {
		// response.setContentType("text/html; charset=" +
		// MinshengTransService.getRequestCharset());
		// }
		return "/admin/credit/view";
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String certification(Long id, String memo, AuthStatus status, RedirectAttributes redirectAttributes) {

		return "redirect:list.jhtml";
	}
}