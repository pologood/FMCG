/**
 * ====================================================
 * 文件名称: BankController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-5			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.controller.weixin.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Setting;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.BankInfoModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.MemberBankModel;
import net.wit.entity.*;
import net.wit.entity.MemberBank.Type;
import net.wit.service.*;
import net.wit.util.Base64Util;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 银行卡
 * Created by WangChao on 2016-10-20.
 */
@Controller("weixinMemberBankController")
@RequestMapping("/weixin/member/bank")
public class BankController extends BaseController {
    public static final String ENCODE_TYPE_BASE64 = "BASE64";
    public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
    public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";

    @Resource(name = "bankInfoServiceImpl")
    private BankInfoService bankInfoService;

    @Resource(name = "memberBankServiceImpl")
    private MemberBankService memberBankService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    /**
     * 添加银行卡发送手机验证码
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock sendMobile(String mobile, HttpServletRequest request) {
        if (mobile == null) {
            return DataBlock.error("手机号无效");
        }
        HttpSession session = request.getSession();
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = SpringUtils.getIdentifyingCode();
        String securityCode = String.valueOf(challege);
        SafeKey tmp = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
        if (tmp != null && !tmp.hasExpired()) {
            securityCode = tmp.getValue();
            if (!tmp.canReset()) {
                return DataBlock.error("系统忙，稍等几秒重试");
            }
        }
        SafeKey safeKey = new SafeKey();
        safeKey.setValue(securityCode);
        safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
        session.setAttribute(CAPTCHA_SECURITYCODE_SESSION, safeKey);
        session.setAttribute(CAPTCHA_CONTENT_SESSION, mobile);
        SmsSend smsSend = new SmsSend();
        smsSend.setMobiles(mobile);
        smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
        smsSend.setType(SmsSend.Type.captcha);
        smsSendService.smsSend(smsSend);
        return DataBlock.success("success", "发送成功");
    }

    /**
     * 支持的银行
     */
    @RequestMapping(value = "/bank_info/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock bankInfo() {
        List<BankInfo> banks = bankInfoService.findAll();
        return DataBlock.success(BankInfoModel.bindData(banks), "执行成功");
    }

    /**
     * 我的银行卡列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list() {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("flag", Operator.eq, MemberBank.Flag.cashier));
        filters.add(new Filter("member", Operator.eq, member));
        filters.add(new Filter("type", Operator.eq, Type.debit));
        List<MemberBank> memberBanks = memberBankService.findList(null, filters, null);
        return DataBlock.success(MemberBankModel.bindData(memberBanks), "执行成功");
    }

    /**
     * 新增银行卡
     * captcha 验证码
     * bankInfoId 银行Id
     * cardNo 卡号
     * depositUser 开户名
     * idCardNo 证件号
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock save(MemberBank memberBank,Long bankInfoId,String captcha,String idCardNo,HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
//        String code = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
        session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
        session.removeAttribute(CAPTCHA_CONTENT_SESSION);
        if (safeKey == null) {
            return DataBlock.error("验证码过期了");
        }
        if (safeKey.hasExpired()) {
            return DataBlock.error("验证码过期了");
        }
        if (!safeKey.getValue().equals(captcha)) {
            return DataBlock.error("验证码不正确");
        }
        if(member.getIdcard()==null||!member.getIdcard().getAuthStatus().equals(Idcard.AuthStatus.success)){
            return DataBlock.error("未实名认证，暂不能添加银行卡");
        }
        if (memberBank.getDepositUser().compareTo(member.getName()==null?"":member.getName())!=0) {
            return DataBlock.error("姓名不正确");
        }
//		if(idCardNo!=null&&idCardNo.compareTo(member.getIdcard().getNo())!=0){
//			return DataBlock.error("证件号不正确");
//		}
        memberBank.setMember(member);
        BankInfo bankInfo = bankInfoService.find(bankInfoId);
        if(bankInfo==null){
            return DataBlock.error("无效银行id");
        }
        memberBank.setBankInfo(bankInfo);
        memberBank.setType(Type.debit);
        memberBank.setFlag(MemberBank.Flag.cashier);
        memberBank.setDepositBank(bankInfo.getDepositBank());
        memberBankService.save(memberBank);
        if(member.getTenant()!=null){
            if(!activityDetailService.isActivity(null,member.getTenant(),activityRulesService.find(38L))){
                activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(38L));
            }
        }
        return DataBlock.success("success","保存成功");
    }

    /**
     * 删除银行卡
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock delete(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        MemberBank bank = memberBankService.find(id);
        if (bank == null) {
            return DataBlock.error("无效银行卡");
        }
        memberBankService.delete(bank);
        return DataBlock.success("success", "删除成功");
    }

    /**
     * 删除银行卡
     * @param id    银行卡Id
     * @param password    支付密码（加密后）
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock delete(Long id, String password) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        String pwd = Base64Util.decode(password);
        if (!DigestUtils.md5Hex(pwd).equals(member.getPaymentPassword())) {
            return DataBlock.error("支付密码不正确");
        }
        MemberBank bank = memberBankService.find(id);
        if (bank == null) {
            return DataBlock.error("无效银行卡");
        }
        memberBankService.delete(bank);
        return DataBlock.success("success", "删除成功");
    }

}
