/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.b2b.BaseController;
import net.wit.entity.Account;
import net.wit.entity.Area;
import net.wit.entity.Credit;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;
import net.wit.entity.Credit.Type;
import net.wit.entity.Idcard;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.SmsSend;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.AccountService;
import net.wit.service.AreaService;
import net.wit.service.CreditService;
import net.wit.service.MemberBankService;
import net.wit.service.MemberService;
import net.wit.service.PayBankService;
import net.wit.service.SmsSendService;
import net.wit.service.SnService;
import net.wit.service.TenantRelationService;
import net.wit.service.TenantService;
import net.wit.uic.api.UICService;
import net.wit.util.SettingUtils;

/**
 * Controller - 支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberCashController")
@RequestMapping("/b2b/member/cash")
public class CashController extends BaseController {
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;


	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "creditServiceImpl")
	private CreditService creditService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "payBankServiceImpl")
	private PayBankService payBankService;
	
	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;
	@Resource(name = "uicService")
	private UICService uicService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "accountServiceImpl")
    private AccountService accountService;
	@Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;
	@Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

	/**
	 * 计算支付手续费
	 *
	 * @param amount 金额
	 * @return 支付手续费
	 */
	public BigDecimal calcFee(Method method, BigDecimal amount) {
		Setting setting = SettingUtils.get();
		Member member = memberService.getCurrent();
		BigDecimal fillAmount = amount.subtract(member.getClearBalance());
		BigDecimal fee = new BigDecimal(0);
		if (fillAmount.compareTo(BigDecimal.ZERO) > 0) {
			fee = fillAmount.multiply(SettingUtils.get().getPaymentWithdrawCashScale());
			fee = fee.add(member.getClearBalance().multiply(SettingUtils.get().getWithdrawCashScale()));
		} else {
			fee = amount.multiply(SettingUtils.get().getWithdrawCashScale());
		}
		return setting.setScale(fee);
	}
	

	
	
	/**
	 * 计算支付手续费
	 */
	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculateFee(Method method, BigDecimal amount) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (amount == null || amount.compareTo(new BigDecimal(0)) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		BigDecimal fee = calcFee(method,amount);
		data.put("fee", fee);
		data.put("recv", BigDecimal.ZERO);
		return data;
	}

	/**
	 * 提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(Long memberBankId,String bank, String bankname,String mobile, String payer, String account,Method method,BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		MemberBank memberBank=memberBankService.find(memberBankId);
		if (member == null) {
			return ERROR_VIEW;
		}
		model.addAttribute("area",memberService.getCurrentArea());
    	if (amount == null || amount.compareTo(new BigDecimal(0)) < 0 || amount.precision() > 15 || amount.scale() > 2) {
    		model.addAttribute("status",Message.error("提款数值错误"));
 		   	return "redirect:/b2b/member/cash/index.jhtml";
		}
    	
		if (!member.getAuthStatus().equals(Idcard.AuthStatus.success)) {
			addFlashMessage(redirectAttributes, Message.error("没有通过实名认证的账户不能提现"));
			return "redirect:/b2b/member/cash/index.jhtml";
		}
		
		if (!(member.getName().equals(memberBank.getDepositUser()) || (member.getTenant()!=null && member.getTenant().getName().equals(memberBank.getDepositUser()))) ) {
			addFlashMessage(redirectAttributes,Message.error("只能提现到本人银行卡上"));
			return "redirect:/b2b/member/cash/index.jhtml";
		}

    	Setting setting = SettingUtils.get();
    	ResourceBundle bundle=PropertyResourceBundle.getBundle("config");

		Credit credit = new Credit();
		credit.setSn(snService.generate(Sn.Type.credit));
		credit.setType(Type.cash);
		credit.setMethod(Method.fast);
		credit.setStatus(Status.wait);
		credit.setPaymentMethod("账户支付");
		credit.setAccount(memberBank.getCardNo());
		credit.setBank(memberBank.getDepositBank());
		credit.setBankName(memberBank.getDepositBank());
		credit.setBankCode(bank);
		credit.setPayer(memberBank.getDepositUser());
		credit.setMobile(member.getPhone());
		credit.setAmount(amount);
		credit.setFee(calcFee(Method.immediately,amount));
		credit.setRecv(BigDecimal.ZERO);
		BigDecimal clearAmount = amount.add(credit.getRecv());
		if(member.getClearBalance().compareTo(clearAmount)>=0){
			credit.setClearAmount(clearAmount);
		}else{
			credit.setClearAmount(member.getClearBalance());
		}
		credit.setCost(BigDecimal.ZERO);
		credit.setExpire(null);
		credit.setMember(member);
		model.addAttribute("member", member);
		model.addAttribute("area",areaService.getCurrent());
		if (amount.compareTo(new BigDecimal(50000))>0 ) {
		   model.addAttribute("credit", credit);
		   model.addAttribute("status",Message.error("单笔汇款不能超过50000."));
		   return "redirect:/b2b/member/cash/index.jhtml";
		}
			
		if (amount.compareTo(member.getBalance())>0 ) {
			   model.addAttribute("credit", credit);
			   model.addAttribute("status",Message.error("账户余额不足."));
			   return "redirect:/b2b/member/cash/index.jhtml";
			}
		
		if ((amount.add(calcFee(method,amount))).compareTo(member.getBalance())>0 ) {
			   model.addAttribute("credit", credit);
			   model.addAttribute("status",Message.error("账户余额不足."));
			   return "redirect:/b2b/member/cash/index.jhtml";
			}
		try {
			if (member.getBalance().compareTo(credit.getEffectiveAmount())<0) {
				addFlashMessage(redirectAttributes,  Message.error("账户可提现余额不足"));
				return "redirect:/b2b/member/cash/index.jhtml";
			}
			if (member.getBalance().subtract(member.getFreezeCashBalance()).compareTo(credit.getAmount())<0) {
				addFlashMessage(redirectAttributes,  Message.error("提现金额不能大于"+setting.setScale(member.getBalance().subtract(member.getFreezeCashBalance())).toString()) );
				return "redirect:/b2b/member/cash/index.jhtml";
			}
	    	creditService.saveAndUpdate(credit);
	    	Message msg=null;
	    	SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(credit.getMobile());
			smsSend.setType(SmsSend.Type.service);
	    		
			if (credit.getStatus() == Credit.Status.success) {
                smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + credit.getAccount().substring(credit.getAccount().length() - 4, credit.getAccount().length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。【" + bundle.getString("signature") + "】");
                smsSendService.smsSend(smsSend);
                addFlashMessage(redirectAttributes, Message.success("提现成功"));
                model.addAttribute("credit", credit);
 			   	model.addAttribute("status",Message.success("提现成功"));
                return "b2b/member/cash/infomation";
            } else {
                if (credit.getStatus() == Credit.Status.wait) {
                    smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + credit.getAccount().substring(credit.getAccount().length() - 4, credit.getAccount().length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。。【" + bundle.getString("signature") + "】");
                    smsSendService.smsSend(smsSend);
                    addFlashMessage(redirectAttributes, Message.success("提现成功"));
                    model.addAttribute("credit", credit);
     			   	model.addAttribute("status",Message.success("提现成功"));
                    return "b2b/member/cash/infomation";
                } else {
                    addFlashMessage(redirectAttributes, Message.error("提现失败"));
                    return "redirect:/b2b/member/cash/index.jhtml";
                }
            }
		} catch (BalanceNotEnoughException e){
			model.addAttribute("credit", credit);
			model.addAttribute("status",Message.error("账户余额不足"));
			return "redirect:/b2b/member/cash/index.jhtml";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR_VIEW;
		} 
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
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber,ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", creditService.findPage(member, pageable, Credit.Type.cash));
		model.addAttribute("member", member);
		return "b2b/member/cash/list";
	}
	
	/**
	 * 供应商的提现
	 */
	@RequestMapping(value = "/withdraw_index", method = RequestMethod.GET)
	public String index(ModelMap model,RedirectAttributes redirectAttributes,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return "redirect:/b2b/supplier/login.jhtml";
		}
		Page<TenantRelation> tenantRelations=tenantRelationService.findPage(member.getTenant(), net.wit.entity.TenantRelation.Status.success, pageable);
		if(tenantRelations!=null&&tenantRelations.getContent().size()>0){
			TenantRelation tenantRelation=tenantRelations.getContent().get(0);
			model.addAttribute("tenant",tenantRelation.getTenant());
		}else{
			addFlashMessage(redirectAttributes, Message.error("您还没有合作伙伴，暂时不能提现"));
			return "redirect:/b2b/member/supplier/index.jhtml";
		}
		
		Tenant tenant = member.getTenant();
		Member owner = tenant.getMember();
		if(member.getId()!= owner.getId()){
			addFlashMessage(redirectAttributes, Message.warn("不好意思，您不能提现！"));
			return "redirect:withdraw_index.jhtml";
		}
		List<MemberBank> memberBanks = memberBankService.findListByMember(member);
		Map<Long, String> options = new HashMap<Long, String>();
		if (memberBanks.size() > 0) {
			for (MemberBank memberBank : memberBanks) {
				options.put(memberBank.getId(), memberBank.getCardNo());
			}
		}
		model.addAttribute("member", member);
		model.addAttribute("options", options);
		return "b2b/member/cash/withdraw_index";
	}
	
	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	public String notify(ModelMap model) {
		
 		return "redirect:b2b/member/cash/notify";
	}
	/**
	 * 聚德惠商城用户的提现
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String custom_index(ModelMap model,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return "用户未登录";
		}
		Tenant tenant = member.getTenant();
		if(tenant!=null){
			Member owner = tenant.getMember();
			if(member.getId()!= owner.getId()){
				addFlashMessage(redirectAttributes, Message.warn("不好意思，您不能提现！"));
				return "redirect:index.jhtml";
			}
		}
		
		
		List<MemberBank> memberBanks = memberBankService.findListByMember(member);
		Map<Long, String> options = new HashMap<Long, String>();
		if (memberBanks.size() > 0) {
			for (MemberBank memberBank : memberBanks) {
				options.put(memberBank.getId(), memberBank.getCardNo());
			}
		}
		model.addAttribute("member", member);
		model.addAttribute("options", options);
		model.addAttribute("menu","wallet");
		model.addAttribute("area",areaService.getCurrent());
		return "b2b/member/cash/index";
	}
	/**
	 * 商城用户提交
	 */
	@RequestMapping(value = "/enchashment_submit", method = RequestMethod.POST)
	public String submit_custom(String bank, String bankname,String mobile, String payer, String account,Method method,BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		Area area=new Area();
		if(memberService.getCurrentArea()==null){
			area=areaService.getCurrent();
		}else{
			area=memberService.getCurrentArea();
		}
		model.addAttribute("area",area);
    	if (amount == null || amount.compareTo(new BigDecimal(0)) < 0 || amount.precision() > 15 || amount.scale() > 2) {
			
 		   model.addAttribute("status",Message.error("提款数值错误"));
 		   return "b2c/member/cash/infomation";
    		//return ERROR_VIEW;
		}
    	
		if (!member.getAuthStatus().equals(Idcard.AuthStatus.success)) {
			addFlashMessage(redirectAttributes, Message.error("没有通过实名认证的账户不能提现"));
			return "redirect:index.jhtml";
		}
		
		if (!(member.getName().equals(payer) || (member.getTenant()!=null && member.getTenant().getName().equals(payer))) ) {
			addFlashMessage(redirectAttributes,Message.error("只能提现到本人银行卡上"));
			return "redirect:index.jhtml";
		}

    	Setting setting = SettingUtils.get();
    	ResourceBundle bundle=PropertyResourceBundle.getBundle("config");

		Credit credit = new Credit();
		credit.setSn(snService.generate(Sn.Type.credit));
		credit.setType(Type.cash);
		credit.setMethod(method);
		credit.setStatus(Status.wait);
		credit.setPaymentMethod("账户支付");
		credit.setAccount(account);
		credit.setBank(bankname);
		credit.setBankName(bankname);
		credit.setBankCode(bank);
		credit.setPayer(payer);
		credit.setMobile(mobile);
		credit.setAmount(amount);
		credit.setFee(calcFee(method,amount));
		credit.setRecv(BigDecimal.ZERO);
		BigDecimal clearAmount = amount.add(credit.getRecv());
		if(member.getClearBalance().compareTo(clearAmount)>=0){
			credit.setClearAmount(clearAmount);
		}else{
			credit.setClearAmount(member.getClearBalance());
		}
		credit.setCost(BigDecimal.ZERO);
		credit.setExpire(null);
		credit.setMember(member);
		model.addAttribute("member", member);
		//BigDecimal payment = member.getBalance().subtract(member.getClearBalance());
		//System.out.println(payment);
		if (amount.compareTo(new BigDecimal(50000))>0 ) {
		   model.addAttribute("credit", credit);
		   model.addAttribute("status",Message.error("单笔汇款不能超过50000."));
		   return "b2b/member/cash/infomation";
		}
			//int ss = amount.compareTo(member.getBalance());
		if (amount.compareTo(member.getBalance())>0 ) {
			   model.addAttribute("credit", credit);
			   model.addAttribute("status",Message.error("账户余额不足."));
			   return "b2b/member/cash/infomation";
			}
		
		if ((amount.add(calcFee(method,amount))).compareTo(member.getBalance())>0 ) {
			   model.addAttribute("credit", credit);
			   model.addAttribute("status",Message.error("账户余额不足."));
			   return "b2b/member/cash/infomation";
			}
		try {
	    	creditService.saveAndUpdate(credit);
	    	Message msg=null;
	    	SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(credit.getMobile());
			smsSend.setType(SmsSend.Type.service);
	    	if (method==Method.immediately) {
	    		msg = payBankService.payToBank(credit,snService.generate(Sn.Type.epaybank),"0");
	    		if ((credit.getStatus()==Credit.Status.success) || (credit.getStatus()==Credit.Status.wait_success)) {
		    	   smsSend.setContent("您在【"+setting.getSiteName()+"】申请的银行汇款."+credit.getSn()+"(尾号"+account.substring(account.length()-4,account.length())+",金额"+amount.toString()+"元)提交成功，请注意资金变动。【"+bundle.getString("signature")+"】");
		    	   smsSendService.smsSend(smsSend);
	    		} else {
	    			smsSend.setContent("您在【"+setting.getSiteName()+"】申请的银行汇款."+credit.getSn()+"(尾号"+account.substring(account.length()-4,account.length())+",金额"+amount.toString()+"元)汇款失败，请使用普通汇款。【"+bundle.getString("signature")+"】");
	    			smsSendService.smsSend(smsSend);
	    		}
		    	
	    	} else
		 	if (method==Method.fast) {
		 		smsSend.setContent("您在【"+setting.getSiteName()+"】申请的银行汇款."+credit.getSn()+"(尾号"+account.substring(account.length()-4,account.length())+",金额"+amount.toString()+"元)提交成功，预计1-3个工作日到账。【"+bundle.getString("signature")+"】");
		 		smsSendService.smsSend(smsSend);
	    	}
	    		
	    	if (credit.getStatus()==Credit.Status.success) { 	
	    		model.addAttribute("status",SUCCESS_MESSAGE);
	    	} else {
	    		if (msg!=null) {
	    		    model.addAttribute("status",msg);
	    		} else if (credit.getStatus()==Credit.Status.wait) { 	
		    		model.addAttribute("status",SUCCESS_MESSAGE);
		    	} else {
	    			return ERROR_VIEW;
	    		}
	    	}
			model.addAttribute("credit", credit);
			return "b2b/member/cash/infomation";
		} catch (BalanceNotEnoughException e){
			model.addAttribute("credit", credit);
			model.addAttribute("status",Message.error("账户余额不足"));
			return "b2b/member/cash/infomation";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR_VIEW;
		} 
	}
	@RequestMapping(value = "/infomation", method = RequestMethod.GET)
	public String infomation(ModelMap model) {
		
 		return "b2b/member/cash/infomation";
	}
	/**
	 * 供应商提现
	 * @param amount
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/submit_withdraw", method = RequestMethod.POST)
	@ResponseBody
	public Message submit_withdraw(BigDecimal amount,Pageable pageable) {
		Member member=memberService.getCurrent();
		if(member==null){
			return Message.error("没有找到用户");
		}
		if(amount==null){
			return Message.error("请输入金额");
		}
		Account account =new Account();
		Page<TenantRelation> tenantRelations=tenantRelationService.findPage(member.getTenant(), null, pageable);
		if(tenantRelations!=null&&tenantRelations.getContent().size()>0){
			TenantRelation tenantRelation=tenantRelations.getContent().get(0);
			account.setTenant(tenantRelation.getTenant());
		}else{
			return Message.error("您还没有服务商，暂时不能体现");
		}
		if(member.getTenant().getBalance().compareTo(amount)==-1){
			return Message.error("账户余额不足，不能体现");
		}
		member.getTenant().setBalance(member.getTenant().getBalance().subtract(amount));
		tenantService.update(member.getTenant());
		account.setAmount(amount);
		account.setStatus(net.wit.entity.Account.Status.none);
		account.setSn(snService.generate(Sn.Type.account));
		account.setCreateDate(new Date());
		account.setModifyDate(new Date());
		if(member.getTenant()!=null){
			account.setSupplier(member.getTenant());
		}
		accountService.save(account);
		return Message.success("提现成功");
	}
}