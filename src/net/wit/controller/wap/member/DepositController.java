/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.controller.support.DepositModel;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;

/**
 * Controller - 会员中心 - 预存款
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapMemberDepositController")
@RequestMapping("/wap/member/deposit")
public class DepositController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;
	
	/**
	 * 账单明细
	 */
	@RequestMapping(value = "/bill_list", method = RequestMethod.GET)
	public String  list(Date begin_date,Date end_date,Pageable pageable,ModelMap model) {
		
		return "wap/member/deposit/bill_list";
	}
	

	/**
	 * 计算支付手续费
	 */
	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> calculateFee(String paymentPluginId, BigDecimal amount) {
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
	public @ResponseBody Map<String, Object> checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		data.put("balance", member.getBalance());
		return data;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		BigDecimal income = depositService.income(member,null,null,null,null);
		 BigDecimal outcome = depositService.outcome(member,null,null,null,null);
//		Page<Deposit> page = depositService.findPage(member, pageable);
		// List<DepositModel> deposits = new ArrayList<DepositModel>();
		// Calendar calendar = Calendar.getInstance();
		// Date date = calendar.getTime();
		// Date beginDayOfMonth = DateUtil.setBeginDayOfMonth(calendar);
		// 当前月个月
		// DepositModel deposit = convertDepositModel(member, date, beginDayOfMonth);
		// deposits.add(deposit);

		// 上个月
		// Date date2 = DateUtil.transpositionDate(date, Calendar.MONTH, -1);
		// calendar.setTime(date2);
		// DepositModel deposit1 = convertDepositModel(member, DateUtil.transpositionDate(beginDayOfMonth, Calendar.MONTH, -1),
		// DateUtil.setLastDayOfMonth(calendar));
		// deposits.add(deposit1);
		// 更多
		// List<Deposit> list = depositService.findList(member, null, DateUtil.transpositionDate(beginDayOfMonth, Calendar.MONTH, -1));
		// DepositModel deposit2 = convertToDepositModel(list);
		// deposits.add(deposit2);

		model.addAttribute("income", income);
		model.addAttribute("outcome", outcome);
//		model.addAttribute("page", page);
		return "wap/member/deposit/list";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/addMore", method = RequestMethod.GET)
	@ResponseBody
	public Page<Deposit> addMore(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			new ArrayList<Deposit>();
		}

		Page<Deposit> page = depositService.findPage(member, pageable);
		// List<DepositModel> deposits = new ArrayList<DepositModel>();
		// Calendar calendar = Calendar.getInstance();
		// Date date = calendar.getTime();
		// Date beginDayOfMonth = DateUtil.setBeginDayOfMonth(calendar);
		// 当前月个月
		// DepositModel deposit = convertDepositModel(member, date, beginDayOfMonth);
		// deposits.add(deposit);

		// 上个月
		// Date date2 = DateUtil.transpositionDate(date, Calendar.MONTH, -1);
		// calendar.setTime(date2);
		// DepositModel deposit1 = convertDepositModel(member, DateUtil.transpositionDate(beginDayOfMonth, Calendar.MONTH, -1),
		// DateUtil.setLastDayOfMonth(calendar));
		// deposits.add(deposit1);
		// 更多
		// List<Deposit> list = depositService.findList(member, null, DateUtil.transpositionDate(beginDayOfMonth, Calendar.MONTH, -1));
		// DepositModel deposit2 = convertToDepositModel(list);
		// deposits.add(deposit2);

		return page;
	}

	private DepositModel convertDepositModel(Member member, Date date, Date beginDayOfMonth,Deposit.Type type) {
		try {
			List<Deposit> list = depositService.findList(member, beginDayOfMonth, date,type);
			return convertToDepositModel(list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private DepositModel convertToDepositModel(List<Deposit> list) {
		if (list != null && list.size() > 0) {
			DepositModel depositModel = new DepositModel();
			BigDecimal outAmount = new BigDecimal(0);
			BigDecimal inAmount = new BigDecimal(0);
			depositModel.setDeposits(list);
			for (Deposit deposit : list) {
				outAmount = outAmount.add(deposit.getDebit());
				inAmount = inAmount.add(deposit.getCredit());
			}
			depositModel.setInAmount(inAmount);
			depositModel.setOutAmount(outAmount);
			return depositModel;
		}
		return null;
	}

	/**
	 * 充值查询
	 */
	@RequestMapping(value = "/recharge/list", method = RequestMethod.GET)
	public String rechargeList(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("member", member);
		model.addAttribute("page", paymentService.findPage(member, pageable, Payment.Type.recharge));
		return "wap/member/deposit/recharge/list";
	}

}