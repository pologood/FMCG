/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import net.wit.*;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;
import net.wit.entity.SmsSend.Type;
import net.wit.service.*;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 收款单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminCreditController")
@RequestMapping("/admin/credit")
public class CreditController extends BaseController {

	@Resource(name = "creditServiceImpl")
	private CreditService creditService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "minshengTransServiceImpl")
	private MinshengTransService minshengTransService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	@Resource(name = "remittanceServiceImpl")
	private RemittanceService remittanceService;
	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id,Long memberId, Pageable pageable, ModelMap model) {
		Member member=memberService.find(memberId);
		Admin admin = adminService.getCurrent();
		List<Deposit> depositList=depositService.findList(member, null, null,null);
		model.addAttribute("credit", creditService.find(id));
		model.addAttribute("depositList", depositList);
		model.addAttribute("size", depositList.size());
		model.addAttribute("administrator",admin);
		return "/admin/credit/view";
	}

	/**
	 * 支付成功
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String success(Long id, String bankName, String bankCode, BigDecimal amount, String documentFlow, String acntToName, String memo, String date, RedirectAttributes redirectAttributes,
						  ModelMap model) {
		Credit credit = creditService.find(id);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date remittanceDate = null;
		try {
			remittanceDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Remittance remittance = new Remittance();
		remittance.setAcntToName(acntToName);
		remittance.setBankCode(bankCode);
		remittance.setBankName(bankName);
		remittance.setAmount(amount);
		remittance.setDocumentFlow(documentFlow);
		remittance.setCredit(credit);
		//remittance.setCreateDate(new Date());
		//remittance.setModifyDate(new Date());
		remittance.setRemittanceDate(remittanceDate);
		remittance.setMemo(memo);
		remittanceService.save(remittance);

		credit.setCreditDate(remittanceDate);
		Admin admin = adminService.getCurrent();
		credit.setOperator(admin.getName());
		credit.setMemo("线下支付");
		credit.setRemittance(remittance);
		credit.setStatus(Credit.Status.success);
		creditService.save(credit);

		//model.addAttribute("credit", credit);
		return "redirect:view.jhtml?id="+id;
	}

	/**
	 * 查询银行xmlpost
	 * 
	 * @param id
	 *            订单编号
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/querybank", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querybank(Long id,
			RedirectAttributes redirectAttributes, ModelMap model) {
//		Setting setting = SettingUtils.get();
//		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Map<String, Object> data = new HashMap<String, Object>();
		Credit credit = creditService.find(id);
		credit.setCreditDate(new Date());
		Admin admin = adminService.getCurrent();
		credit.setOperator(admin.getName());
		Message msg = null;
		if (Credit.Status.wait.equals(credit.getStatus())) {
			msg = minshengTransService.queryToBank("", credit);
			data.put("msgtype", msg.getType());
			data.put("xmlbody", msg.getContent());
			data.put("xmlbyte", "GB2312");
			data.put("posturl", minshengTransService.getRequestUrl());
		} else {
			msg = Message.error("您当前单据不在提交状态不能完成支付。");
			data.put("msgtype", msg.getType());
		}
		model.addAttribute("credit", credit);
		if (msg != null) {
			addFlashMessage(redirectAttributes, msg);
		}
		return data;
	}

	/**
	 * 提交银行xmlpost
	 * 
	 * @param id
	 *            订单编号
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/paybank", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> paybank(Long id, String gettype,
			RedirectAttributes redirectAttributes, ModelMap model) {
		try
		{
//		Setting setting = SettingUtils.get();
//		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Map<String, Object> data = new HashMap<String, Object>();
		Credit credit = creditService.find(id);
		credit.setCreditDate(new Date());
		Admin admin = adminService.getCurrent();
		credit.setOperator(admin.getName());
		Message msg = null;
		if (Credit.Status.wait.equals(credit.getStatus())) {
			if (gettype == "1") {
				msg = minshengTransService.queryToBank("", credit);
				data.put("msgtype", msg.getType());
				data.put("xmlbody", msg.getContent());
				data.put("xmlbyte", "GB2312");
				data.put("posturl", minshengTransService.getRequestUrl());
			} else {
				
				String arg = "305";
//				  if (credit.getBankCode().substring(0, 3).equals(arg)) {
//					// 是否对公对私 1对公 2对私
//					  if (credit.getPayer().contains(credit.getMember().getName())) {
//						  msg = minshengTransService.sendCostReimbmsg("", credit);
//					  }
//				  }
				
				if (credit.getBankCode().substring(0, 3).equals(arg) && credit.getAccount().length()==16 ) {
					msg = minshengTransService.sendCostReimbmsg("", credit,"","");
				}else {
					msg = minshengTransService.sendBankmsg("", credit);
				}

				data.put("msgtype", msg.getType());
				data.put("xmlbody", msg.getContent());
				data.put("xmlbyte", "GB2312");
				data.put("posturl", minshengTransService.getRequestUrl());
			}
			if (msg.getType().equals(Message.Type.success)) {
				msg = Message.success(msg.getContent());
			} else {
				msg = Message.error(msg.getContent());
			}
		} else if (Credit.Status.wait_success.equals(credit.getStatus())) {
			msg = minshengTransService.queryToBank("", credit);
			data.put("msgtype", msg.getType());
			data.put("xmlbody", msg.getContent());
			data.put("xmlbyte", "GB2312");
			data.put("posturl", minshengTransService.getRequestUrl());
		} else {
			msg = Message.error("您当前单据不在提交状态不能完成支付。");
		}
		model.addAttribute("credit", credit);
		if (msg != null) {
			addFlashMessage(redirectAttributes, msg);
		}
		return data;
		}
		catch (Exception e)
		{
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("msgtype", e.toString());
			System.out.println(e);
			return data ;
		}
	}

	/**
	 * 接收银行回执xmlpost
	 * 
	 * @param id
	 *            订单编号
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/receivebank", method = RequestMethod.GET)
	@ResponseBody
	public Message receiveBank(Long id, String code, String severity,
			String message, String insId, String statusCode,
			String statusSeverity, String statusErrMsg, String receivetype,
			RedirectAttributes redirectAttributes, ModelMap model) {
		Credit credit = creditService.find(id);
		if(credit==null){
			return Message.error("error");
		}
		if(credit.getStatus()!=null && credit.getStatus().equals(Credit.Status.success)){
			return Message.error("订单已支付成功");
		}
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Message msg = null;

		if ("error".equals(code)||"W3317".equals(code)) {
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("交易状态因网络问题未知，请稍后检查到账状态");
			minshengTransService
					.receiveBank(null, credit, "error", receivetype);
			return Message.error("error");
		}
		MinshengBank minshengBank = minshengTransService
				.findMinshengbyNo(insId);
		try {
			minshengBank.setResCode(code);
			minshengBank.setResseverity(severity);
			minshengBank.setResmessage(message);
			minshengBank.setRestrnId(insId);
			if (statusCode != null) {
				minshengBank.setResstatusCode(statusCode);
				minshengBank.setResstatusSeverity(statusSeverity);
				minshengBank.setResstatusErrMsg(statusErrMsg);
			}
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("提交银行成功，请稍后检查到账状态:" + message);
			msg = minshengTransService.receiveBank(minshengBank, credit,
					"success", receivetype);
			if (Credit.Status.wait_success.equals(credit.getStatus())) {
				msg = minshengTransService.receiveBank(minshengBank, credit,
						"success", receivetype);
				if ((credit.getStatus() == Credit.Status.failure)) {
					// 失败完成退款
					failure(id, redirectAttributes, model);
					SmsSend smsSend=new SmsSend();
					smsSend.setMobiles(credit.getMobile());
					smsSend.setContent("您在"
							+ setting.getSiteName()
							+ "申请的银行汇款."
							+ credit.getSn()
							+ "(尾号"
							+ credit.getAccount().substring(
									credit.getAccount().length() - 4,
									credit.getAccount().length())
							+ ",金额" + credit.getAmount()
							+ "元)汇款失败，请重新提交汇款申请。【"
							+ setting.getSiteName() + "】");
					smsSend.setType(Type.service);
					smsSendService.smsSend(smsSend);
				} else if (credit.getStatus() == Credit.Status.wait_failure) {
					System.out.println("Credit.Status.wait_failure-->>receivebank"
									+ credit.getSn() + "退款");
					
					SmsSend smsSend=new SmsSend();
					smsSend.setMobiles(bundle.getString("refundmobile"));
					smsSend.setContent("付款单"
							+ credit.getSn()
							+ "(尾号"
							+ credit.getAccount().substring(
									credit.getAccount().length() - 4,
									credit.getAccount().length())
							+ ",金额" + credit.getAmount()
							+ "元)退款失败，请人工处理。【" + setting.getSiteName()
							+ "】");
					smsSend.setType(Type.service);
					smsSendService.smsSend(smsSend);
			}
				}
			if (msg.getType().equals(Message.Type.success)) {
				return Message.success(msg.getContent());
			} else {
				return Message.error(msg.getContent());
			}
		} catch (Exception e) {
			minshengTransService.queryToBank(insId, credit);
			return Message.error("error");
		}
	}
	
	
	/**
	 * 短信推送
	 */
	@RequestMapping(value = "/postmessage", method = RequestMethod.POST)
	@ResponseBody
	public Message postmessage(Long id,RedirectAttributes redirectAttributes) {
	    //Credit credit = creditService.find(id);
		//SmsSend smsSend=new SmsSend();
		//smsSend.setMobiles(credit.getMobile());
		//smsSend.setContent("尊敬的找汽配用户您好，您的单号为"+ credit.getSn()
		//		+"的提现已由找汽配平台财务部门操作员韦雪向您"
		//		+ "尾号"
		//		+ credit.getAccount().substring(
		//				credit.getAccount().length() - 4,
		//				credit.getAccount().length())
		//		+ "转账"
		//		+ credit.getAmount()
		//		+ "元。本信息为已受理的付款指令信息，入账时间以您的收款银行账户到账时间为准。感谢您对找汽配平台的支持，有任何疑问请拨打客服热线400-086-7676【找汽配】");
		//smsSend.setType(Type.service);
		//smsSendService.smsSend(smsSend);
		//Message msg = null;
		//addFlashMessage(redirectAttributes,msg.success("发送短信成功!"));
		return Message.success("发送短信成功!");
	}

	/**
	 * 支付
	 */
	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public String failure(Long id, RedirectAttributes redirectAttributes,
			ModelMap model) {
		Credit credit = creditService.find(id);
		credit.setCreditDate(new Date());
		Admin admin = adminService.getCurrent();
		credit.setOperator(admin.getName());
		Message msg = null;
		if (Credit.Status.wait_failure.equals(credit.getStatus())) {
			credit.setMemo("手工退款成功");
			credit.setStatus(Credit.Status.failure);
			try {
				creditService.saveAndRefunds(credit);
			} catch (Exception e) {
				return ERROR_VIEW;
			}
		} else if (Credit.Status.failure.equals(credit.getStatus())) {
			String mmsg = credit.getMemo();
			credit.setMemo(mmsg + ". 退款成功");
			try {
				creditService.saveAndRefunds(credit);
			} catch (Exception e) {
				return ERROR_VIEW;
			}
		}

		else {
			msg = Message.error("您当前单据不在待退款状态，不能操作手工退款");
		}
		model.addAttribute("credit", credit);
		if (msg != null) {
			addFlashMessage(redirectAttributes, msg);
		}
		return "/admin/credit/view";
	}

	/**
	 * 撤消
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public String cancel(Long id, RedirectAttributes redirectAttributes,
			ModelMap model) {
		Credit credit = creditService.find(id);
		credit.setCreditDate(new Date());
		Admin admin = adminService.getCurrent();
		credit.setOperator(admin.getName());
		Message msg = null;
		if (Credit.Status.wait.equals(credit.getStatus())) {
			String mmsg = credit.getMemo();
			credit.setMemo(mmsg==null?"":mmsg + "撤消退款成功");
			credit.setStatus(Credit.Status.failure);
			try {
				creditService.saveAndRefunds(credit);
			} catch (Exception e) {
				return ERROR_VIEW;
			}
		} else {
			msg = Message.error("您当前单据不在待支付状态，不能操作手工撤消");
		}
		model.addAttribute("credit", credit);
		if (msg != null) {
			addFlashMessage(redirectAttributes, msg);
		}
		return "redirect:view.jhtml?id="+id;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Date beginDate, Date endDate,Date beginDates, Date endDates, Method method,
			Status status,String searchValue, Pageable pageable, ModelMap model) {
		
//		if(beginDate==null){
//			beginDate=new Date();
//			Long time=beginDate.getTime();
//			Long begin=time-24*60*60*1000*7;
//			beginDate=new Date(begin);
//		}
//		if(endDate!=null){
//			Long time=endDate.getTime();
//			Long end=time+24*60*60*1000-1;
//			endDate=new Date(end);
//		}

//		List<Filter> filters = pageable.getFilters();
//		filters.add(new Filter("payer", Filter.Operator.like,searchValue));
		if(beginDate!=null&&endDate!=null){
			Long time=endDate.getTime();
			Long end=time+24*60*60*1000-1;
			endDate=new Date(end);
		}
		if(beginDates!=null&&endDates!=null){
			Long time=endDates.getTime();
			Long end=time+24*60*60*1000-1;
			endDates=new Date(end);
		}
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("beginDates", beginDates);
		model.addAttribute("endDates", endDates);
		model.addAttribute("method", method);
		model.addAttribute("status", status);
		Page page = creditService.findPage(method, status,
				beginDate, endDate,beginDates, endDates,searchValue, pageable);
		model.addAttribute("page", creditService.findPage(method, status,
				beginDate, endDate,beginDates, endDates,searchValue, pageable));
		return "/admin/credit/list";
	}

	/**
	 *收款数据导出
	 */
	@RequestMapping(value = "/credit_export", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String , Object >> creditExport(Date beginDate, Date endDate,Date beginDates, Date endDates,
			Method method, Status status,String searchValue, Pageable pageable){
		List<Map<String,Object>> maps=new ArrayList<Map<String , Object >>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//最大可导出数
		pageable.setPageSize(1000);
		Page page = creditService.findPage(method, status,beginDate, endDate,beginDates, endDates,searchValue, pageable);
		List<Credit> credits=page.getContent();
		if(credits!=null){
			for(Credit credit:credits){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("createDate",sdf.format(credit.getCreateDate()));
				map.put("sn",credit.getSn());
				map.put("amount",credit.getAmount());
				map.put("fee",credit.getFee());
				map.put("payer", credit.getPayer());
				map.put("mobile",credit.getMobile());
				map.put("tenantName", credit.getMember().getTenant()==null?"未开通":credit.getMember().getTenant().getName());
				map.put("bank",credit.getBank());
				map.put("account", "’"+credit.getAccount());
				if(credit.getStatus()==Status.wait){
					map.put("status", "等待支付");
				}else if(credit.getStatus()==Status.wait_success){
					map.put("status", "提交银行");
				}else if(credit.getStatus()==Status.success){
					map.put("status", "支付成功");
				}else if(credit.getStatus()==Status.wait_failure){
					map.put("status", "时代待退款");
				}else if(credit.getStatus()==Status.failure){
					map.put("status", "支付失败");
				}
				if(credit.getType()==Credit.Type.visa){
					map.put("type", "信用卡还款");
				}else if(credit.getType()==Credit.Type.cash){
					map.put("type", "提现");
				}
				if(credit.getMethod()==Method.immediately){
					map.put("method", "已支付");
				}else if(credit.getMethod()==Method.fast){
					map.put("method", "已支付");
				}else if(credit.getMethod()==Method.general){
					map.put("method", "已退款");
				}
				map.put("creditDate",credit.getCreditDate()==null?"-":sdf.format(credit.getCreditDate()));
				maps.add(map);
			}
		}
		return maps;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Credit credit = creditService.find(id);
				if (credit != null && credit.getExpire() != null
						&& !credit.hasExpired()) {
					return Message
							.error("admin.payment.deleteUnexpiredNotAllowed");
				}
			}
			creditService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

}