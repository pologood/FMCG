/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import net.wit.Message;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.helper.BaseController;
import net.wit.entity.*;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;
import net.wit.entity.Credit.Type;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.*;
import net.wit.util.SettingUtils;

import net.wit.util.SpringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 支付
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberCashController")
@RequestMapping("/helper/member/cash")
public class CashController extends BaseController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "creditServiceImpl")
    private CreditService creditService;
    @Resource(name = "snServiceImpl")
    private SnService snService;
    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;
    @Resource(name = "memberBankServiceImpl")
    private MemberBankService memberBankService;
    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @Resource(name = "bankInfoServiceImpl")
    private BankInfoService bankInfoService;

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
    public
    @ResponseBody
    Map<String, Object> calculateFee(Method method, BigDecimal amount) {
        Member member = memberService.getCurrent();
        Map<String, Object> data = new HashMap<String, Object>();
        if (member.getBalance().compareTo(amount) < 0) {
            data.put("message", Message.error("余额不足"));
            return data;
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) < 0) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        data.put("message", SUCCESS_MESSAGE);
        BigDecimal fee = calcFee(method, amount);
        data.put("fee", fee);
        data.put("recv", BigDecimal.ZERO);
        return data;
    }

    /**
     * 计算支付手续费
     */
    @RequestMapping(value = "/checkbakecard", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> checkbakecard(Method method, BigDecimal amount) {
        Member member = memberService.getCurrent();
        Map<String, Object> data = new HashMap<String, Object>();
        if (member.getBalance().compareTo(amount) < 0) {
            data.put("message", Message.error("余额不足"));
            return data;
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) < 0) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        data.put("message", SUCCESS_MESSAGE);
        BigDecimal fee = calcFee(method, amount);
        data.put("fee", fee);
        data.put("recv", BigDecimal.ZERO);
        return data;
    }

    /**
     * 提交
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(Long memberBankId, BigDecimal amount, String type, ModelMap model, HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            addFlashMessage(redirectAttributes, Message.warn("无效用户！"));
            return "redirect:/helper/member/index.jhtml";
        }
        if (amount == null || amount.compareTo(new BigDecimal(0)) < 0) {
            addFlashMessage(redirectAttributes, Message.error("提现金额无效。"));
            return "redirect:/helper/member/index.jhtml";
        }

        MemberBank memberBank = memberBankService.find(memberBankId);
        if (memberBank == null) {
            addFlashMessage(redirectAttributes, Message.error("无效银行卡编号"));
            return "redirect:/helper/member/index.jhtml";
        }

        if (!member.getAuthStatus().equals(Idcard.AuthStatus.success)) {
            addFlashMessage(redirectAttributes, Message.error("没有通过实名认证的账户不能提现"));
            return "redirect:/helper/member/index.jhtml";
        }

        if (!memberBank.getMember().equals(member)) {
            addFlashMessage(redirectAttributes, Message.error("只能提现到本人银行卡"));
            return "redirect:/helper/member/index.jhtml";
        }

//		String password = rsaService.decryptParameter("enPassword", request);
//
//		 password = Base64Util.decode(password);
//
//
//		if (!MD5Utils.getMD5Str(password).equals(member.getPaymentPassword()) ) {
//			addFlashMessage(redirectAttributes, Message.error("支付密码错误"));
//			return "redirect:index.jhtml";
//		}

        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        Credit credit = new Credit();
        credit.setSn(snService.generate(Sn.Type.credit));
        credit.setType(Type.cash);
        credit.setMethod(Method.fast);
        credit.setStatus(Status.wait);
        credit.setPaymentMethod("账户支付");
        credit.setAccount(memberBank.getCardNo());
        if (memberBank.getBankInfo() != null) {
            credit.setBank(memberBank.getBankInfo().getDepositBank());
            credit.setBankName(memberBank.getBankInfo().getDepositBank());
            credit.setBankCode(memberBank.getBankInfo().getBankCode());
        }
        credit.setPayer(memberBank.getDepositUser());
        credit.setAcntToName(memberBank.getDepositUser());
        credit.setMobile(member.getMobile());
        credit.setFee(calcFee(Method.fast, amount));
        credit.setAmount(amount.subtract(credit.getFee()));
        credit.setRecv(BigDecimal.ZERO);
        credit.setCost(BigDecimal.ZERO);
        BigDecimal clearAmount = amount;
        if (member.getClearBalance().compareTo(clearAmount) >= 0) {
            credit.setClearAmount(clearAmount);
        } else {
            credit.setClearAmount(member.getClearBalance());
        }
        credit.setExpire(null);
        credit.setMember(member);
        model.addAttribute("member", member);
        model.addAttribute("credit", credit);
        // BigDecimal payment =
        // member.getBalance().subtract(member.getClearBalance());
        // System.out.println(payment);
        if (amount.compareTo(new BigDecimal(50000)) > 0) {
            addFlashMessage(redirectAttributes, Message.error("单笔汇款不能超过50000."));
            return "redirect:/helper/member/index.jhtml";
        }
        // int ss = amount.compareTo(member.getBalance());
        try {
            if (member.getBalance().compareTo(credit.getEffectiveAmount()) < 0) {
                addFlashMessage(redirectAttributes, Message.error("账户可提现余额不足"));
                return "redirect:/helper/member/index.jhtml";
            }

            if (member.getBalance().subtract(member.getFreezeCashBalance()).compareTo(credit.getEffectiveAmount()) < 0) {
                addFlashMessage(redirectAttributes, Message.error("提现金额不能大于" + setting.setScale(member.getBalance().subtract(member.getFreezeCashBalance())).toString()));
                return "redirect:/helper/member/cash/index.jhtml";
            }
            creditService.saveAndUpdate(credit);
            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(credit.getMobile());
            smsSend.setType(SmsSend.Type.service);

            if (credit.getStatus() == Credit.Status.success) {
                smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + credit.getAccount().substring(credit.getAccount().length() - 4, credit.getAccount().length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。【" + bundle.getString("signature") + "】");
                smsSendService.smsSend(smsSend);

                if (!activityDetailService.isActivity(member, null, activityRulesService.find(39L))) {
                    activityDetailService.addPoint(member, null, activityRulesService.find(39L));
                }
                addFlashMessage(redirectAttributes, Message.success("提现成功"));
                return "redirect:../deposit/thismonthlist.jhtml";
            } else {
                if (credit.getStatus() == Credit.Status.wait) {
                    smsSend.setContent("您在【" + setting.getSiteName() + "】申请的银行汇款" + credit.getSn() + "(尾号" + credit.getAccount().substring(credit.getAccount().length() - 4, credit.getAccount().length()) + ",金额" + amount.toString() + "元)提交成功，预计1-3个工作日到账。。【" + bundle.getString("signature") + "】");
                    smsSendService.smsSend(smsSend);

                    if (!activityDetailService.isActivity(member, null, activityRulesService.find(39L))) {
                        activityDetailService.addPoint(member, null, activityRulesService.find(39L));
                    }

                    addFlashMessage(redirectAttributes, Message.success("提现成功"));
                    return "redirect:../deposit/thismonthlist.jhtml";
                } else {
                    addFlashMessage(redirectAttributes, Message.error("提现失败"));
                    return "redirect:/helper/member/index.jhtml";
                }
            }
        } catch (BalanceNotEnoughException e) {
            addFlashMessage(redirectAttributes, Message.error("提现失败"));
            return "redirect:/helper/member/index.jhtml";
        } catch (Exception e) {
            addFlashMessage(redirectAttributes, Message.error("提现失败"));
            return "redirect:/helper/member/index.jhtml";
        }
    }

    @RequestMapping(value = "/notify", method = RequestMethod.GET)
    public String notify(ModelMap model) {
        return "redirect:helper/member/cash/notify";
    }

    /**
     * 检查余额
     */
    @RequestMapping(value = "/check_balance", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> checkBalance() {
        Map<String, Object> data = new HashMap<String, Object>();
        Member member = memberService.getCurrent();
        data.put("balance", member.getBalance());
        return data;
    }

    /**
     * 提现
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "用户未登录";
        }
        Tenant tenant = member.getTenant();
        Member owner = tenant.getMember();
        if (member.getId() != owner.getId()) {
            addFlashMessage(redirectAttributes, Message.warn("不好意思，您不能提现！"));
            return "redirect:/helper/member/index.jhtml";
        }
        List<MemberBank> memberBanks = memberBankService.findListByMember(member);
        Map<String, String> options = new HashMap<String, String>();
        if (memberBanks.size() > 0) {
            for (MemberBank memberBank : memberBanks) {
                options.put(memberBank.getId().toString(), memberBank.getCardNo());
            }
        }
        model.addAttribute("member", member);
        model.addAttribute("options", options);
        model.addAttribute("memberBanks", memberBanks);
        return "helper/member/cash/index";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(Long id, RedirectAttributes redirectAttributes) {
        try {
            MemberBank memberBank = memberBankService.find(id);
            if (memberBank == null) {
                addFlashMessage(redirectAttributes, Message.warn("删除错误"));
                return "redirect:/helper/member/cash/index.jhtml";
            }
            memberBankService.delete(memberBank);
            addFlashMessage(redirectAttributes, Message.success("操作成功"));
            return "redirect:/helper/member/cash/index.jhtml";
        } catch (Exception e) {
            addFlashMessage(redirectAttributes, Message.warn("删除错误"));
            return "redirect:/helper/member/cash/index.jhtml";
        }
    }

    /**
     * 列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String startDate, String status, String endDate, ModelMap model, Pageable pageable) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = null;
        Date end_time = null;
        Date now_date = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate());
        Date back_date = new Date(now_date.getTime() - 24 * 60 * 60 * 1000);
        if (StringUtils.isNotBlank(startDate)) {
            start_time = simpleDateFormat.parse(startDate);
        } else {
            start_time = back_date;
        }

        if (StringUtils.isNotBlank(endDate)) {
            end_time = simpleDateFormat.parse(endDate);
        } else {
            end_time = now_date;
        }
        model.addAttribute("page", creditService.findPage(member, start_time, end_time, Credit.Type.cash, pageable));
        model.addAttribute("member", member);
        model.addAttribute("begin_date", simpleDateFormat.format(start_time));
        model.addAttribute("end_date", simpleDateFormat.format(end_time));
        return "helper/member/cash/list";
    }

    /**
     * 添加银行卡
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(ModelMap model) {
        Member member = memberService.getCurrent();
        Idcard idcard = member.getIdcard();
        if (idcard == null) {
            idcard = new Idcard();
        }

        List<BankInfo> banks = bankInfoService.findAll();
        model.addAttribute("bankInfos", banks);
        model.addAttribute("member", member);
        model.addAttribute("idcard", idcard);
        return "helper/member/cash/edit";
    }

    /**
     * 新增银行卡
     */
    @RequestMapping(value = "/saveMemberBank", method = RequestMethod.POST)
    public String saveMemberBank(String account, String payer, Long bankInfoId,
                                 RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        BankInfo bankInfo = bankInfoService.find(bankInfoId);
        MemberBank memberBank = new MemberBank();
        memberBank.setMember(member);
        memberBank.setBankInfo(bankInfo);
        memberBank.setCardNo(account.replaceAll(" ", ""));
        memberBank.setDepositBank(bankInfo.getDepositBank());
        memberBank.setDepositUser(payer);
        memberBank.setFlag(MemberBank.Flag.cashier);
        memberBank.setType(MemberBank.Type.debit);
        memberBankService.save(memberBank);

        if (member.getTenant() != null) {
            //我的钱包>绑定银行卡
            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(38L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(38L));
            }
        }
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:index.jhtml";
    }

    /**
     * 银行卡校验
     */
    @RequestMapping(value = "/checkbankcard", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> checkbankcard(Method method, String amount) {
        Map<String, Object> data = new HashMap<String, Object>();
        amount = amount.replaceAll(" ", "");
        // 位数校验
        if (amount.length() == 16 || amount.length() == 19) {

        } else {
            data.put("message", Message.error("卡号位数无效"));
            data.put("amount", "卡号位数无效");
            System.out.println("卡号位数无效");
            return data;
        }

        // 校验
        if (CheckBankCard.checkBankCard(amount) == true) {

        } else {
            data.put("message", Message.error("卡号校验失败"));
            data.put("amount", "无效卡");
            System.out.println("卡号校验失败");
            return data;
        }
        String name = BankCardBin.getNameOfBank(amount.substring(0, 6), 0);// 获取银行卡的信息
        data.put("message", SUCCESS_MESSAGE);
        data.put("amount", name);
        return data;
    }

    public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
    public static final String REGISTER_CONTENT_SESSION = "register_mobile";

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/getCheckCode", method = RequestMethod.POST)
    @ResponseBody
    public Message getCheckCode(String mobile, HttpServletRequest request) {
        if (StringUtils.isEmpty(mobile)) {
            return Message.error("手机为空");
        }

        HttpSession session = request.getSession();
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = SpringUtils.getIdentifyingCode();
        String securityCode = String.valueOf(challege);
        SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        if (tmp != null && !tmp.hasExpired()) {
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

        SmsSend smsSend = new SmsSend();
        smsSend.setMobiles(mobile);
        smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
        smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
        smsSendService.smsSend(smsSend);
        return Message.success("消息发送成功，请查收");
    }

}