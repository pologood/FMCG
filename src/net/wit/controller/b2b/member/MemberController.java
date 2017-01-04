/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.wit.*;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SpringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.controller.app.model.DataBlock;
import net.wit.controller.b2c.BaseController;
import net.wit.util.SettingUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员中心
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberController")
@RequestMapping("/b2b/member")
public class MemberController extends BaseController {

	public static final String REGISTER_SECURITYCODE_SESSION = "register_safe_key";
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;


	/** 最新订单数 */
	private static final int NEW_ORDER_COUNT = 6;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "productNotifyServiceImpl")
	private ProductNotifyService productNotifyService;
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;
	@Resource(name = "creditServiceImpl")
	private CreditService creditService;
	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;


	/**
	 * 首页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("waitingPaymentOrderCount", orderService.waitingPaymentCount(member));
		model.addAttribute("waitingShippingOrderCount", orderService.waitingShippingCount(member));
		model.addAttribute("messageCount", messageService.count(member, false));
		model.addAttribute("couponCodeCount", couponCodeService.count(null, member, null, false, false));
		model.addAttribute("favoriteCount", productService.count(member, null, null, null, null, null, null));
		model.addAttribute("productNotifyCount", productNotifyService.count(member, null, null, null));
		model.addAttribute("reviewCount", reviewService.count(member, null, null, null));
		model.addAttribute("consultationCount", consultationService.count(member, null, null));
		model.addAttribute("newOrders", orderService.findList(member, NEW_ORDER_COUNT, null, null));
		return "b2b/member/index";
	}
	
	@RequestMapping(value = "/getCurMember",method = RequestMethod.GET)
	public @ResponseBody Member index(String redirectUrl, HttpServletRequest request, ModelMap model) {
		return memberService.getCurrent();
	}

	@RequestMapping(value = "/wallet/index",method = RequestMethod.GET)
	public String walletIndex(ModelMap model) {
		Member member = memberService.getCurrent();
		BigDecimal fee = calcFeeTemp(member, Credit.Method.fast,member.getBalance());

		List<Filter> filters = new ArrayList<Filter>();
		List<Credit.Status> status=new ArrayList<Credit.Status>();
		status.add(Credit.Status.wait);
		status.add(Credit.Status.wait_success);
		filters.add(new Filter("status", Filter.Operator.in, status));
		filters.add(new Filter("member", Filter.Operator.eq, member));
		List<Credit> list=creditService.findList(null, filters,null);
		BigDecimal uncollected=new BigDecimal(0);
		for (Credit credit : list) {
			uncollected=uncollected.add(credit.getAmount());
		}

		filters = new ArrayList<Filter>();
		filters.add(new Filter("flag", Filter.Operator.eq, MemberBank.Flag.cashier));
		filters.add(new Filter("member", Filter.Operator.eq, member));
		filters.add(new Filter("type", Filter.Operator.eq, MemberBank.Type.debit));
		List<MemberBank> memberBanks = memberBankService.findList(null, filters, null);

		model.addAttribute("memberBanks",memberBanks);
		model.addAttribute("balance",member.getBalance());//余额
		model.addAttribute("freezeBalance",member.getFreezeBalance());//冻结金额
		model.addAttribute("withdrawBalance",member.getBalance().subtract(fee));//可提现金额
		model.addAttribute("uncollected",uncollected);//未到账金额
		model.addAttribute("menu","wallet");
		model.addAttribute("area",areaService.getCurrent());
		Idcard idcard=member.getIdcard();
		if(idcard==null){
			idcard=new Idcard();
		}
		model.addAttribute("idcard",idcard);
		return "b2b/member/wallet/index";
	}

	@RequestMapping(value = "/wallet/bill/list",method = RequestMethod.GET)
	public String bill(Pageable pageable, ModelMap model,String dateType, Deposit.Type type, Date date, Date beginDate, Date endDate){
//		pageable.setPageSize(15);
		Member member = memberService.getCurrent();
		if(date==null){
			date=new Date(new Date().getYear(),new Date().getMonth(),1);
		}
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		if(dateType==null){
			dateType="1";
		}
		if(dateType.equals("1")){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DATE, 1);
			beginDate=null;
			endDate=new Date(cal.getTimeInMillis());
		}else{
			if(type!=null){
				List<Filter> filters=new ArrayList<>();
				filters.add(new Filter("type", Filter.Operator.eq,type));
				pageable.setFilters(filters);
			}
		}
		Page<Deposit> page=depositService.findPage(member,beginDate,endDate,pageable,null);
		model.addAttribute("date",date);
		model.addAttribute("page",page);
		model.addAttribute("recharge", depositService.income(member, Deposit.Type.recharge,null,null,null));
		model.addAttribute("payment", depositService.outcome(member, Deposit.Type.payment,null,null,null));
		model.addAttribute("withdraw", depositService.outcome(member, Deposit.Type.withdraw,null,null,null));
		model.addAttribute("receipts", depositService.income(member, Deposit.Type.receipts,null,null,null));
		model.addAttribute("profit", depositService.income(member, Deposit.Type.profit,null,null,null));
		model.addAttribute("rebate", depositService.outcome(member, Deposit.Type.rebate,null,null,null));
		model.addAttribute("income", depositService.income(member, Deposit.Type.income,null,null,null));
		model.addAttribute("outcome", depositService.outcome(member, Deposit.Type.outcome,null,null,null));
		model.addAttribute("cashier", depositService.income(member, Deposit.Type.cashier,null,null,null));
		model.addAttribute("total_income", depositService.income(member,null,null,null,null));
		model.addAttribute("total_outcome", depositService.outcome(member,null,null,null,null));
		model.addAttribute("types",Deposit.Type.values());
		model.addAttribute("type",type);
		return "b2b/member/wallet/bill/list";
	}

	/**
	 * 计算支付手续费
	 * @param amount 金额
	 * @return 支付手续费
	 */
	public BigDecimal calcFeeTemp(Member member, Credit.Method method, BigDecimal amount) {
		BigDecimal fee = new BigDecimal(0);
		if (method == Credit.Method.immediately) {
			fee = amount.multiply(member.getBaseWithdrawCashScale().add(SettingUtils.get().getWithdrawCashAddScale()));
		}
		if (method == Credit.Method.fast) {
			fee = amount.multiply(member.getBaseWithdrawCashScale());
		}
		if (method == Credit.Method.general) {
			fee = amount.multiply(member.getBaseWithdrawCashScale());
		}
		return fee.setScale(2, BigDecimal.ROUND_DOWN);
	}

	@RequestMapping(value = "/wallet/bank/list",method = RequestMethod.GET)
	public String bankList(ModelMap model){     
		Member member=memberService.getCurrent();
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("flag", Filter.Operator.eq, MemberBank.Flag.cashier));
		filters.add(new Filter("member", Filter.Operator.eq, member));
		filters.add(new Filter("type", Filter.Operator.eq, MemberBank.Type.debit));
		List<MemberBank> memberBanks = memberBankService.findList(null, filters, null);
		model.addAttribute("memberBanks",memberBanks);
		model.addAttribute("menu","wallet");
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("idcard", member.getIdcard());
		return "b2b/member/wallet/bank/list";
	}
	
	
	@RequestMapping(value = "/find_content_of_tradeId",method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getContent(Long tradeId) {
		Trade trade=tradeService.find(tradeId);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("quantity",trade.getQuantity());
		map.put("amount", trade.getPrice());
		return map;
	}

	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public Message sendMobile(String mobile,HttpServletRequest request) {
		if(mobile==null){
			return Message.error("手机号码不能为空");
		}else{
			if(mobile.length()!=11){
				return Message.error("手机号码格式不对");
			}
			HttpSession session = request.getSession();
			Setting setting = SettingUtils.get();
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			int challege = SpringUtils.getIdentifyingCode();
			String securityCode = String.valueOf(challege);
			SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
			if (tmp!=null && !tmp.hasExpired()) {
				securityCode = tmp.getValue();
				if (!tmp.canReset()) {
					return Message.error("系统忙，稍等几秒重试");
				}
			}
			SafeKey safeKey = new SafeKey();
			safeKey.setValue(securityCode);
			safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
			session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
			session.setAttribute(REGISTER_CONTENT_SESSION, mobile);

			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(mobile);
			smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
			smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
			return Message.success("消息发送成功");
		}
	}


	/**
	 * 新增银行卡
	 */
	@RequestMapping(value = "/wallet/bank/saveMemberBank", method = RequestMethod.POST)
	public String saveMemberBank(String bankname,String account,String payer,String securityCode,HttpServletRequest request,
								 RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		Idcard idcard=member.getIdcard();
		if (idcard == null || (idcard.getAuthStatus() != Idcard.AuthStatus.success)) {
			addFlashMessage(redirectAttributes, Message.error("未完成实名认证，暂不能添加银行卡"));
			return "redirect:list.jhtml";
		}
		//检查验证码
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
		if (safeKey == null) {
			addFlashMessage(redirectAttributes,Message.error( "请获取验证码"));
			return "redirect:list.jhtml";
		}
		if (safeKey.hasExpired()) {
			addFlashMessage(redirectAttributes,Message.error( "验证码过期"));
			return "redirect:list.jhtml";
		}
		if (!safeKey.getValue().equals(securityCode)) {
			addFlashMessage(redirectAttributes,Message.error( "验证码不正确"));
			return "redirect:list.jhtml";
		}

		MemberBank memberBank = new MemberBank();
		memberBank.setMember(member);
		memberBank.setCardNo(account.replaceAll(" ", ""));
		memberBank.setDepositBank(bankname);
		memberBank.setDepositUser(payer);
		memberBank.setFlag(MemberBank.Flag.cashier);
		memberBank.setType(MemberBank.Type.debit);
		memberBankService.save(memberBank);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
}