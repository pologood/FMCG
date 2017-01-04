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
package net.wit.controller.weixin.member;

import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;
import net.wit.entity.Credit.Type;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.*;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/** 
 * 提现接口
 */
@Controller("weixinMemberCashController")
@RequestMapping("/weixin/member/cash")
public class CashController extends BaseController {
	public static final String ENCODE_TYPE_BASE64 = "BASE64";

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "creditServiceImpl")
	private CreditService creditService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	
	/**
	 * 提现保存
	 * @param memberBankId 银行编号 对应 bandInfo
	 * @param amount 提现金额
	 * enPassword 加密后的支付密码
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(Long memberBankId,BigDecimal amount, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 ) {
			return DataBlock.error("提现金额无效。");
		}
		MemberBank memberBank = memberBankService.find(memberBankId);
		if (memberBank==null) {
			return DataBlock.error("无效银行卡id");
		}
		
		if (!memberBank.getMember().equals(member)) {
			return DataBlock.error("只能提现到本人银行卡");
		}
		String enPassword=request.getParameter("enPassword");
		if(enPassword==null){
			return DataBlock.error("请输入支付密码");
		}
		String password = rsaService.decryptParameter("enPassword",request);
		rsaService.removePrivateKey(request);
		if(password==null){
			return DataBlock.error("无效支付密码");
		}
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
		credit.setFee(calcFee(Method.fast, amount));
		credit.setAmount(amount.subtract(credit.getFee()));
		credit.setRecv(BigDecimal.ZERO);
		credit.setCost(BigDecimal.ZERO);
		BigDecimal clearAmount = amount.add(credit.getRecv());
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
			smsSend.setType(SmsSend.Type.service);

			if(member.getTenant()!=null){
				if(credit.getStatus() == Status.success||credit.getStatus() == Status.wait){
					if(!activityDetailService.isActivity(null,member.getTenant(),activityRulesService.find(39L))){
						activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(39L));
					}
				}
			}

			if (credit.getStatus() == Status.success) {
				smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + credit.getAccount().substring(credit.getAccount().length() - 4, credit.getAccount().length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。。【" + bundle.getString("signature") + "】");
				smsSendService.smsSend(smsSend);
				return DataBlock.success("success","提现成功");
			} else {
				if (credit.getStatus() == Status.wait) {
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
	 */
	@RequestMapping(value = "/calculate", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock calculate(BigDecimal amount) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		return DataBlock.success(calcFee(Method.fast, amount),"执行成功");
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
		BigDecimal fee;
		if (fillAmount.compareTo(BigDecimal.ZERO) > 0) {
			fee = fillAmount.multiply(SettingUtils.get().getPaymentWithdrawCashScale());
			fee = fee.add(member.getClearBalance().multiply(SettingUtils.get().getWithdrawCashScale()));
		} else {
			fee = amount.multiply(SettingUtils.get().getWithdrawCashScale());
		}
		return setting.setScale(fee);
	}

}
