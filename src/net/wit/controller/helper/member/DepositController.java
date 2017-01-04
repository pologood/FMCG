/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.fr.report.script.function.MONTH;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.helper.BaseController;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.util.DateUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心 - 预存款
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberDepositController")
@RequestMapping("/helper/member/deposit")
public class DepositController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	/**
	 * 计算支付手续费
	 */
	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculateFee(String paymentPluginId, BigDecimal amount) {
		Map<String, Object> data = new HashMap<String, Object>();
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null || !paymentPlugin.getIsEnabled() || amount == null || amount.compareTo(new BigDecimal(0)) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee", paymentPlugin.calculateFee(amount));
		return data;
	}

	/**
	 * 检查余额
	 */
	@RequestMapping(value = "/check_balance", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		data.put("balance", member.getBalance());
		return data;
	}

	/**
	 * 充值
	 */
	@RequestMapping(value = "/fill", method = RequestMethod.GET)
	public String recharge(ModelMap model,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		Member owner = tenant.getMember();
		if(member.getId()!= owner.getId()){
			addFlashMessage(redirectAttributes, Message.warn("不好意思，您不能充值！"));
			return "redirect:/helper/member/index.jhtml";
		}
		
		List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
		if (!paymentPlugins.isEmpty()) {
			model.addAttribute("defaultPaymentPlugin", paymentPlugins.get(0));
			model.addAttribute("paymentPlugins", paymentPlugins);
		}
		model.addAttribute("member", member);
		return "helper/member/deposit/fill";
	}

	/**
	 * 当前月详细信息
	 */
	@RequestMapping(value = "/thismonthlist", method = RequestMethod.GET)
	public String thismonthlist(String month, Deposit.Type type, ModelMap model, Date beginDate, Date endDate, Pageable pageable) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		Calendar calendar = null;
		if("currentMonth".equals(month)){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			beginDate =calendar.getTime();
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			endDate=calendar.getTime();
		}else if ("lastMonth".equals(month)){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			beginDate =calendar.getTime();
			calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			endDate = calendar.getTime();
		}
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		model.addAttribute("types", Deposit.Type.values());
		model.addAttribute("_type", type);
		model.addAttribute("page", depositService.findPage(member, beginDate, endDate, pageable,type));
		return "helper/member/deposit/thismonthlist";
	}
	
	/**
	 * 当前月交易统计
	 */
	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public String statistics(String month,String type,Date beginDate, Date endDate, ModelMap model) {
		String newType = null;
		if("0".equals(type)){
			newType = "(0,3,4,6,7,9)";
		}else if("1".equals(type)){
			newType = "(1,2,5,8,10)";
		}
		Calendar calendar = null;
		if("currentMonth".equals(month)){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			beginDate =calendar.getTime();
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			endDate=calendar.getTime();
		}else if ("lastMonth".equals(month)){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			beginDate =calendar.getTime();
			calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			endDate = calendar.getTime();
		}

		Member member = memberService.getCurrent();
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		model.addAttribute("page", depositService.findMapList(member, newType, beginDate, endDate));
		model.addAttribute("member", member);
		return "helper/member/deposit/statistics";
	}
	
	
	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("member", member);
		model.addAttribute("page", depositService.findPage(member, pageable));
		return "helper/member/deposit/list";
	}
	
}