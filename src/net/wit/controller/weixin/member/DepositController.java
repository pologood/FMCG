/**
 * @ClassName: DepositController
 * @Description: 我的账单
 * @author Administrator
 * @date 2014-9-5 下午1:45:14
 */
package net.wit.controller.weixin.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.DepositModel;
import net.wit.entity.Deposit;
import net.wit.entity.Deposit.Type;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Sn;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
import net.wit.service.PaymentService;
import net.wit.service.SnService;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 账单、钱包充值
 * Created by WangChao on 2016-10-17.
 */
@Controller("weixinMemberDepositController")
@RequestMapping("/weixin/member/deposit")
public class DepositController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;


    @Resource(name = "depositServiceImpl")
    private DepositService depositService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    /**
     * 本人账单流水明细
     * begin_date 开始时间
     * end_date 结束时间
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock listOwner(Date begin_date, Date end_date, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Page<Deposit> page = depositService.findPage(member, begin_date, end_date, pageable, null);
        return DataBlock.success(DepositModel.bindData(page.getContent()), page, "执行成功");
    }

    /**
     * 本人账单统计
     * begin_date 开始时间
     * end_date 结束时间
     */
    @RequestMapping(value = "/sumer", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock sumerOwner(Date begin_date, Date end_date) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("recharge", depositService.income(member, Type.recharge, begin_date, end_date,null));
        data.put("payment", depositService.outcome(member, Type.payment, begin_date, end_date,null));
        data.put("withdraw", depositService.outcome(member, Type.withdraw, begin_date, end_date,null));
        data.put("receipts", depositService.income(member, Type.receipts, begin_date, end_date,null));
        data.put("profit", depositService.income(member, Type.profit, begin_date, end_date,null));
        data.put("rebate", depositService.outcome(member, Type.rebate, begin_date, end_date,null));
        data.put("income", depositService.income(member, Type.income, begin_date, end_date,null));
        data.put("outcome", depositService.outcome(member, Type.outcome, begin_date, end_date,null));
        data.put("cashier", depositService.income(member, Type.cashier, begin_date, end_date,null));
        data.put("total_income", depositService.income(member, null, begin_date, end_date,null));
        data.put("total_outcome", depositService.outcome(member, null, begin_date, end_date,null));
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 钱包充值
     * params amount 充值金额
     */
    @RequestMapping(value = "/fill", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock fill(BigDecimal amount) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Setting setting = SettingUtils.get();
        if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
            return DataBlock.error("无效金额");
        }
        Payment payment = new Payment();
        payment.setSn(snService.generate(Sn.Type.payment));
        payment.setType(Payment.Type.recharge);
        payment.setMethod(Payment.Method.online);
        payment.setStatus(Payment.Status.wait);
        payment.setMemo("账户充值");
        payment.setPaymentMethod("");
        payment.setFee(BigDecimal.ZERO);
        payment.setAmount(amount.setScale(2));
        payment.setPaymentPluginId(null);
        payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
        payment.setMember(member);
        paymentService.save(payment);
        return DataBlock.success(payment.getSn(), "执行成功");
    }


}
