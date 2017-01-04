package net.wit.controller.wap.member;

import java.math.BigDecimal;
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

import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.wap.BaseController;
import net.wit.entity.Credit;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;
import net.wit.entity.Credit.Type;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.SmsSend;
import net.wit.entity.Sn;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.CreditService;
import net.wit.service.MemberBankService;
import net.wit.service.MemberService;
import net.wit.service.PayBankService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.service.SnService;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;

/**
 * Controller - 提现/汇款
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapMemberCashController")
@RequestMapping("/wap/member/cash")
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

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	/**
	 * 提交
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(Long memberBankId, BigDecimal amount, String password, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 2) {
			return DataBlock.error("提现金额无效。");
		}
		MemberBank memberBank = memberBankService.find(memberBankId);
		if (memberBank==null) {
			return DataBlock.error("无效银行卡id");
		}		
		if (!memberBank.getMember().equals(member)) {
			return DataBlock.error("只能提现到本人银行卡");
		}
//		password = Base64Util.decode(request.getParameter("enPassword"));

		password = rsaService.decryptParameter("enPassword", request);
		rsaService.removePrivateKey(request);

		if (!MD5Utils.getMD5Str(password).equals(member.getPaymentPassword()) ) {
			return DataBlock.error("支付密码错误");
		}

		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		Credit credit = new Credit();
		credit.setSn(snService.generate(Sn.Type.credit));
		credit.setType(Type.cash);
		credit.setMethod(Method.fast);
		credit.setStatus(Status.wait);
		credit.setPaymentMethod("账户支付");
		credit.setAccount(memberBank.getCardNo());
		credit.setBank(memberBank.getBankInfo().getDepositBank());
		credit.setBankName(memberBank.getBankInfo().getDepositBank());
		credit.setBankCode(memberBank.getBankInfo().getBankCode());
		credit.setPayer(memberBank.getDepositUser());
		credit.setAcntToName(memberBank.getDepositUser());
		credit.setMobile(member.getMobile());
		credit.setFee(calcFee(Method.fast,amount));
		credit.setAmount(amount.subtract(credit.getFee()));
		credit.setRecv(BigDecimal.ZERO);
		credit.setCost(BigDecimal.ZERO);
		BigDecimal clearAmount = amount;
		if(member.getClearBalance().compareTo(clearAmount)>=0){
			credit.setClearAmount(clearAmount);
		}else{
			credit.setClearAmount(member.getClearBalance());
		}
		credit.setExpire(null);
		credit.setMember(member);
		if (amount.compareTo(new BigDecimal(50000)) > 0) {
			return DataBlock.error("单笔汇款不能超过50000");
		}
		try {
			if (member.getBalance().compareTo(credit.getEffectiveAmount())<0) {
				return DataBlock.error("账户可提现余额不足");
			}
			if (member.getBalance().subtract(member.getFreezeCashBalance()).compareTo(credit.getEffectiveAmount())<0) {
    			return DataBlock.error("提现金额不能大于"+setting.setScale(member.getBalance().subtract(member.getFreezeCashBalance())).toString());
			}
			creditService.saveAndUpdate(credit);
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(credit.getMobile());
			smsSend.setType(net.wit.entity.SmsSend.Type.service);
			if (credit.getStatus() == Credit.Status.success) {
				smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + credit.getAccount().substring(credit.getAccount().length() - 4, credit.getAccount().length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。。【" + bundle.getString("signature") + "】");
				smsSendService.smsSend(smsSend);
				return DataBlock.success("success","提现成功");
			} else {
				if (credit.getStatus() == Credit.Status.wait) {
					smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + credit.getAccount().substring(credit.getAccount().length() - 4, credit.getAccount().length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。。【" + bundle.getString("signature") + "】");
					smsSendService.smsSend(smsSend);
					return DataBlock.success("success","提现成功");
				} else {
					return DataBlock.error("提现失败");
				}
			}
		} catch (BalanceNotEnoughException e) {
			return DataBlock.error("账户余额不足");
		} catch (Exception e) {
			return DataBlock.error("提现失败");
		}
	}

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
}
