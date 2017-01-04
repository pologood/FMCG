/**
 *====================================================
 * 文件名称: BankController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-5			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.ajax.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.ajax.model.BankModel;
import net.wit.entity.BankInfo;
import net.wit.entity.Credit;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;
import net.wit.entity.Credit.Type;
import net.wit.entity.Idcard;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.SmsSend;
import net.wit.entity.Sn;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.BankInfoService;
import net.wit.service.CreditService;
import net.wit.service.MemberBankService;
import net.wit.service.MemberService;
import net.wit.service.PayBankService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.service.SnService;
import net.wit.uic.api.UICService;
import net.wit.util.Base64Util;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/** 
 * @ClassName: BankController 
 * @Description: 银行卡
 * @author Administrator 
 * @date 2014-9-5 下午1:45:14  
 */
/**
 * Controller - 地区
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxMemberBankController")
@RequestMapping("/ajax/member/bank")
public class BankController extends BaseController {
	public static final String ENCODE_TYPE_BASE64 = "BASE64";
	
	@Resource(name = "bankInfoServiceImpl")
	private BankInfoService bankInfoService;

	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "creditServiceImpl")
	private CreditService creditService;

	@Resource(name = "payBankServiceImpl")
	private PayBankService payBankService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "uicService")
	private UICService uicService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	
	
	/**
	 * 支持的银行列表
	 */
	@RequestMapping(value = "/bankInfo", method = RequestMethod.GET)
	@ResponseBody
	public Message bankInfo(HttpServletRequest request) {
		List<BankInfo> banks = bankInfoService.findAll();
		return Message.success(JsonUtils.toJson(banks));
	}
	
	/**
	 * 会员银行卡列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			System.out.println("会员银行卡列表session= null");
		} else {
			System.out.println("会员银行卡列表session=" + session.getId());
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户未登录");
		}
		List<MemberBank> memberBanks = memberBankService.findListByMember(member);
		return Message.success(JsonUtils.toJson(covertoBankModelList(memberBanks)));
	}

	/**
	 * 查看银行卡
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public Message view(Long id) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("用户未登录");
		}
		MemberBank memberBank = memberBankService.find(id);
		return Message.success(JsonUtils.toJson(covertoBankModel(memberBank)));
	}

	/**
	 * 新增银行卡
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Message save(MemberBank memberBank) {
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return Message.error("用户未登录");
			}
			memberBank.setMember(member);
			memberBankService.save(memberBank);
			return Message.success("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("保存银行卡错误");
		}
	}

	/**
	 * 更新银行卡
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Long id, MemberBank memberBank) {
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return Message.error("用户未登录");
			}
			MemberBank memberBankOld = memberBankService.find(id);
			memberBankOld.setType(memberBank.getType());
			memberBankOld.setCardNo(memberBank.getCardNo());
			memberBankOld.setDepositUser(memberBank.getDepositUser());
			memberBankOld.setDepositBank(memberBank.getDepositBank());

			memberBankService.update(memberBankOld);
			return Message.success("更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("更新银行卡错误");
		}
	}

	/**
	 * 删除银行卡
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long id) {
		try {
			Member member = memberService.getCurrent();
			if (member == null) {
				return Message.error("用户未登录");
			}
			MemberBank memberBankOld = memberBankService.find(id);
			if (!member.getMemberBanks().contains(memberBankOld)) {
				return Message.error("删除失败！");
			}
			memberBankService.delete(id);
			return Message.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("删除失败！");
		}
	}

	/**
	 * 根据银行卡号 获取开户行
	 */
	@RequestMapping(value = "/getDepositBanks", method = RequestMethod.GET)
	@ResponseBody
	public Message getDepositBanks(String cardNo) {
		try {
			List<BankInfo> bankInfos = bankInfoService.findListByNo(cardNo);
			return Message.success(JsonUtils.toJson(bankInfos));
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("获取银行卡错误");
		}
	}

	/**
	 * 提交
	 */
	@RequestMapping(value = "/cash", method = RequestMethod.POST)
	@ResponseBody
	public Message submit(String bank, String mobile, String payer, String account, Method method, BigDecimal amount,String password,String enType, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_MESSAGE;
		}
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 2) {
			return ERROR_MESSAGE;
		}
		
		if (ENCODE_TYPE_BASE64.equals(enType)) {
			password = Base64Util.decode(request.getParameter("enPassword"));
			account = Base64Util.decode(account);
			bank = Base64Util.decode(bank);
			payer = Base64Util.decode(payer);
			mobile = Base64Util.decode(mobile);
		} else {
			password = rsaService.decryptParameter("enPassword", request);
			account = rsaService.decryptParameter("account", request);
			bank = rsaService.decryptParameter("bank", request);
			payer = rsaService.decryptParameter("payer", request);
			mobile = rsaService.decryptParameter("mobile", request);
			rsaService.removePrivateKey(request);
		}
		
		
		if (!member.getAuthStatus().equals(Idcard.AuthStatus.success)) {
			return Message.error("没有通过实名认证的账户不能提现");
		}
		
		if (!(member.getName().equals(payer) || (member.getTenant()!=null && member.getTenant().getName().equals(payer))) ) {
			return Message.error("只能提现到本人银行卡上");
		}

		Message msg = uicService.checkPaymentPassword(password, request);
		if (msg.getType().equals(Message.Type.error)) {
			return msg;
		}
		
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		Credit credit = new Credit();
		credit.setSn(snService.generate(Sn.Type.credit));
		credit.setType(Type.cash);
		credit.setMethod(method);
		credit.setStatus(Status.wait);
		credit.setPaymentMethod("账户支付");
		credit.setAccount(account);
		credit.setBank(bank);
		credit.setPayer(payer);
		credit.setMobile(mobile);
		credit.setAmount(amount);
		credit.setFee(calcFeeTemp(member, method, amount));
		credit.setRecv(credit.getFee().subtract(calcprofit(method, amount)));
		BigDecimal clearAmount = amount.add(credit.getRecv());
		if(member.getClearBalance().compareTo(clearAmount)>=0){
			credit.setClearAmount(clearAmount);
		}else{
			credit.setClearAmount(member.getClearBalance());
		}
		credit.setCost(new BigDecimal(2));
		credit.setExpire(null);
		credit.setMember(member);
		if (account.length() < 8) {
			return Message.error("您输入的银行卡号无效");
		}
		if (amount.compareTo(new BigDecimal(50000)) > 0) {
			return Message.error("单笔汇款不能超过50000");
		}
		try {
			creditService.saveAndUpdate(credit);
			
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(credit.getMobile());
			smsSend.setType(net.wit.entity.SmsSend.Type.service);
			if (method == Method.immediately) {
				smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + account.substring(account.length() - 4, account.length()) + ",金额" + amount.toString() + "元)提交成功，请注意资金变动。【" + bundle.getString("signature") + "】");
				smsSendService.smsSend(smsSend);
			} else if (method == Method.fast) {
				smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + account.substring(account.length() - 4, account.length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。。【" + bundle.getString("signature") + "】");
				smsSendService.smsSend(smsSend);
			}

			if (credit.getStatus() == Credit.Status.success) {
				return Message.success("提现成功");
			} else {
				if (credit.getStatus() == Credit.Status.wait) {
					return Message.success("提现成功");
				} else {
					return Message.error("提现失败");
				}
			}
		} catch (BalanceNotEnoughException e) {
			return Message.error("账户余额不足");
		} catch (Exception e) {
			return Message.error("提现失败");
		}
	}

	/**
	 * 计算支付手续费
	 */
	@RequestMapping(value = "/calcfee", method = RequestMethod.POST)
	@ResponseBody
	public Message calcfee(Method method, BigDecimal amount, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("登录超时");
		}
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 2) {
			return Message.error("金额无效");
		}
		return Message.success(calcFeeTemp(member, method, amount).toString());
	}

	/**
	 * 验余额是否足够提现
	 */
	@RequestMapping(value = "/validAmount", method = RequestMethod.POST)
	@ResponseBody
	public Message validAmount(Method method, BigDecimal amount) {
		if (method == null) {
			return ERROR_MESSAGE;
		}
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 2) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_MESSAGE;
		}
		Credit credit = new Credit();
		credit.setSn(snService.generate(Sn.Type.credit));
		credit.setType(Type.cash);
		credit.setMethod(method);
		credit.setStatus(Status.wait);
		credit.setPaymentMethod("账户支付");
		credit.setAmount(amount);
		credit.setFee(calcFeeTemp(member, method, amount));
		credit.setRecv(credit.getFee().subtract(calcprofit(method, amount)));
		BigDecimal clearAmount = amount.add(credit.getRecv());
		if(member.getClearBalance().compareTo(clearAmount)>=0){
			credit.setClearAmount(clearAmount);
		}else{
			credit.setClearAmount(member.getClearBalance());
		}
		credit.setCost(new BigDecimal(2));
		credit.setExpire(null);
		credit.setMember(member);
		if (amount.compareTo(new BigDecimal(50000)) > 0) {
			return Message.error("单笔汇款不能超过50000");
		}
		if (member.getBalance().add(new BigDecimal(0).subtract(credit.getEffectiveAmount())).compareTo(new BigDecimal(0)) < 0) {
			return Message.error("余额不足");
		}
		return Message.success("正常");

	}

	/**
	 * 计算支付手续费
	 * @param amount 金额
	 * @return 支付手续费
	 */
	public BigDecimal calcFeeTemp(Member member, Method method, BigDecimal amount) {
		BigDecimal fee = new BigDecimal(0);
		if (method == Method.immediately) {
			fee = amount.multiply(member.getBaseWithdrawCashScale().add(SettingUtils.get().getWithdrawCashAddScale()));
		}
		if (method == Method.fast) {
			fee = amount.multiply(member.getBaseWithdrawCashScale());
		}
		if (method == Method.general) {
			fee = amount.multiply(member.getBaseWithdrawCashScale());
		}

		if (fee.compareTo(new BigDecimal(2)) == -1) {
			fee = new BigDecimal(2);
		}
		return fee.setScale(2, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 计算实收手续费返利
	 * @param amount 金额
	 * @return 实收手续费返利
	 */
	public BigDecimal calcprofit(Method method, BigDecimal amount) {
		return new BigDecimal(0);
	}

	/***
	 * 接口展示信息List
	 * @param memberBanks
	 * @return
	 */
	private List<BankModel> covertoBankModelList(List<MemberBank> memberBanks) {
		List<BankModel> bankModels = new ArrayList<BankModel>();
		for (MemberBank memberBank : memberBanks) {
			BankModel model = covertoBankModel(memberBank);
			bankModels.add(model);
		}
		return bankModels;
	}

	/***
	 * 接口展示信息
	 * @param memberBank
	 * @return
	 */
	private BankModel covertoBankModel(MemberBank memberBank) {
		BankModel model = new BankModel();
		model.setCardNo(memberBank.getCardNo());
		model.setId(memberBank.getId());
		model.setDepositBank(memberBank.getDepositBank());
		model.setType(memberBank.getType());
		model.setDepositUser(memberBank.getDepositUser());
		return model;
	}

}
