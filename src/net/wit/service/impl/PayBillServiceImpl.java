package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.dao.*;
import net.wit.entity.*;
import net.wit.entity.PayBill.Status;
import net.wit.entity.Rebate.OrderType;
import net.wit.service.CouponNumberService;
import net.wit.service.MessageService;
import net.wit.service.PayBillService;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
@Service("payBillServiceImpl")
public class PayBillServiceImpl extends BaseServiceImpl<PayBill, Long> implements PayBillService {

    @Resource(name = "payBillDaoImpl")
    public void setBaseDao(PayBillDao payBillDao) {
        super.setBaseDao(payBillDao);
    }

    @Resource(name = "payBillDaoImpl")
    private PayBillDao payBillDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "rebateDaoImpl")
    private RebateDao rebateDao;
    
    @Resource(name = "depositDaoImpl")
    private DepositDao depositDao;

    @Resource(name = "paymentDaoImpl")
    private PaymentDao paymentDao;

    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;


    @Resource(name = "couponNumberServiceImpl")
    private CouponNumberService couponNumberService;

    @Resource(name = "couponDaoImpl")
    private CouponDao couponDao;
    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "snDaoImpl")
    private SnDao snDao;

    @Resource(name = "employeeDaoImpl")
    private EmployeeDao employeeDao;

    @Resource(name = "tagDaoImpl")
    private TagDao tagDao;

    @Transactional
    public void upgrade(PayBill payBill) {
        Payment payment = new Payment();
        payment.setMember(payBill.getMember());
        payment.setPayer(payBill.getMember().getDisplayName());
        payment.setMemo("优惠买单");
        payment.setSn(snDao.generate(Sn.Type.payment));
        payment.setType(Payment.Type.paybill);
        payment.setMethod(Payment.Method.online);
        payment.setStatus(payBill.getEffectiveAmount().compareTo(BigDecimal.ZERO) == 0 ? Payment.Status.success : Payment.Status.wait);
        payment.setPaymentMethod("");
        payment.setFee(BigDecimal.ZERO);
        payment.setAmount(payBill.getEffectiveAmount());
        payment.setPaymentPluginId("");
        payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
        payment.setTrade(null);
        payment.setOrder(null);
        payment.setPayBill(payBill);
        paymentDao.persist(payment);
        payBill.setPayment(payment);
        payBillDao.persist(payBill);
    }

    /**
     * 买单立减
     *
     * @param payment  收款单
     * @param operator 操作员
     */
    public void payment(Payment payment, Member operator) {
        PayBill payBill = payment.getPayBill();
        if (payBill.getStatus().equals(Status.success)) {
            return;
        };

        //payBillDao.lock(payBill, LockModeType.PESSIMISTIC_WRITE);
        if (payment.getMethod().equals(Payment.Method.deposit)) {
            Member member = payBill.getMember();
            memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
            BigDecimal rechargeBalance = member.getBalance().subtract(member.getClearBalance());
            BigDecimal totalBalance = new BigDecimal("0.00");
            // 余额付款
            totalBalance = member.getBalance();
            payment.setAmount(payBill.getEffectiveAmount());
            if (payment.getAmount().subtract(rechargeBalance).compareTo(member.getClearBalance()) >= 0) {
                payment.setClearAmount(member.getClearBalance());
                member.setClearBalance(BigDecimal.ZERO);
            } else {
                payment.setClearAmount(payment.getAmount().subtract(rechargeBalance));
                member.setClearBalance(member.getClearBalance().subtract(payment.getAmount().subtract(rechargeBalance)));
            }
            if (member.getFreezeCashBalance().compareTo(payment.getAmount()) < 0) {
                member.setFreezeCashBalance(BigDecimal.ZERO);
            } else {
                member.setFreezeCashBalance(member.getFreezeCashBalance().subtract(payment.getAmount()));
            }
            member.setBalance(member.getBalance().subtract(payment.getAmount()));

            if (totalBalance.compareTo(payment.getAmount()) >= 0) {
                memberDao.merge(member);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.payment);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(BigDecimal.ZERO);
                deposit.setDebit(payment.getAmount());
                deposit.setBalance(member.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(member);
                deposit.setMemo("优惠买单 单号:" + payBill.getSn());
                deposit.setOrder(null);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                payment.setStatus(Payment.Status.success);

                Setting setting = SettingUtils.get();
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "您的账户，优惠买单" + setting.setScale(payment.getAmount()).toString() + "元，流水号:" + payBill.getSn() + "",
                            null, member, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.member);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                payment.setAmount(BigDecimal.ZERO);
                payment.setClearAmount(BigDecimal.ZERO);
            }
        } else {
            Member member = payment.getMember();
            Deposit deposit1 = new Deposit();
            deposit1.setType(Deposit.Type.recharge);
            deposit1.setStatus(Deposit.Status.complete);
            deposit1.setCredit(payment.getAmount());
            deposit1.setDebit(BigDecimal.ZERO);
            deposit1.setBalance(member.getBalance().add(payment.getAmount()));
            deposit1.setOperator(operator != null ? operator.getUsername() : null);
            deposit1.setMember(member);
            deposit1.setOrder(null);
            deposit1.setMemo("优惠买单 单号:" + payBill.getSn());
            Deposit deposit2 = new Deposit();
            deposit2.setType(Deposit.Type.payment);
            deposit2.setStatus(Deposit.Status.complete);
            deposit2.setCredit(BigDecimal.ZERO);
            deposit2.setDebit(payment.getAmount());
            deposit2.setBalance(member.getBalance());
            deposit2.setOperator(operator != null ? operator.getUsername() : null);
            deposit2.setMember(member);
            deposit2.setMemo("优惠买单 单号:" + payBill.getSn());
            deposit2.setOrder(null);
            depositDao.persist(deposit1);
            depositDao.persist(deposit2);
        }
        //对主订单意外更新失败、子订单更新成功对问题，捕获异常
        paymentDao.merge(payment);

        if (payBill.getCouponCode() != null) {
            CouponCode couponCode = payBill.getCouponCode();
            if(!couponCode.getCoupon().getType().equals(Coupon.Type.multipleCoupon)){
                couponCode.setIsUsed(true);
            }
            couponCode.setUsedDate(new Date());
            couponCodeDao.merge(couponCode);

            Coupon coupon = couponCode.getCoupon();
//            if(coupon.getCount()==0){
//                coupon.setStatus(Coupon.Status.unUsed);
//            }
//            coupon.setSendCount(coupon.getSendCount()+1);
//            coupon.setCount(coupon.getCount()-1);
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponDao.merge(coupon);
        }
        if (payBill.getTenantCouponCode() != null) {
            CouponCode couponCode = payBill.getTenantCouponCode();
            couponCode.setIsUsed(true);
            couponCode.setUsedDate(new Date());
            couponCodeDao.merge(couponCode);

            Coupon coupon = couponCode.getCoupon();
//            if(coupon.getCount()==0){
//                coupon.setStatus(Coupon.Status.unUsed);
//            }
//            coupon.setSendCount(coupon.getSendCount()+1);
//            coupon.setCount(coupon.getCount()-1);
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponDao.merge(coupon);
        }

        if (payment.getAmount().compareTo(BigDecimal.ZERO) != 0) {
            payBill.setStatus(Status.success);
        }
        payBillDao.merge(payBill);
        //完了，给商家结算
        if (payBill.getStatus().equals(Status.success)) {
            Tenant tenant = payBill.getTenant();
            BigDecimal realHairAmount = payBill.getClearingAmount();
            if (realHairAmount.compareTo(BigDecimal.ZERO) >= 0) {
                Member tenantMember = tenant.getMember();
                memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);
                tenantMember.setBalance(tenantMember.getBalance().add(payBill.getClearingAmount()));
                memberDao.merge(tenantMember);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.cashier);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(payBill.getClearingAmount());
                deposit.setDebit(BigDecimal.ZERO);
                deposit.setBalance(tenantMember.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(tenantMember);
                String msg = "优惠买单 实付：" + payBill.getEffectiveAmount();

                if (payBill.getBackDiscount() != null) {
                    msg += " 返：" + payBill.getBackDiscount();
                }
                if (payBill.getDiscount() != null) {
                    msg += " 减" + payBill.getDiscount();
                }
                deposit.setMemo(msg);
                deposit.setOrder(null);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                Setting setting = SettingUtils.get();
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            payBill.getMember().getDisplayName() + "在" + payBill.getDeliveryCenter() == null ? payBill.getTenant().getName() : payBill.getDeliveryCenter().getName() + "，优惠买单" + setting.setScale(payBill.getClearingAmount()).toString() + "元，流水号:" + payBill.getSn() + "",
                            null, tenantMember, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.tenant);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Member buyMember = payBill.getMember();
                memberDao.lock(buyMember, LockModeType.PESSIMISTIC_WRITE);
                //tenantMember.setClearBalance(tenantMember.getClearBalance().add(payBill.getClearingAmount()));
                buyMember.setBalance(buyMember.getBalance().add(payBill.getBackDiscount()));
                buyMember.setFreezeCashBalance(buyMember.getFreezeCashBalance().add(payBill.getBackDiscount()));
                memberDao.merge(buyMember);
                Deposit buyDeposit = new Deposit();
                buyDeposit.setType(Deposit.Type.income);
                buyDeposit.setStatus(Deposit.Status.complete);
                buyDeposit.setCredit(payBill.getBackDiscount());
                buyDeposit.setDebit(BigDecimal.ZERO);
                buyDeposit.setBalance(buyMember.getBalance());
                buyDeposit.setOperator(operator != null ? operator.getUsername() : null);
                buyDeposit.setMember(buyMember);
                buyDeposit.setMemo("买单返现 单号:" + payBill.getSn());
                buyDeposit.setOrder(null);
                buyDeposit.setStatus(Deposit.Status.none);
                depositDao.persist(buyDeposit);
                try {
                    Message buyMessage = EntitySupport.createInitMessage(Message.Type.account,
                            "您在" + payBill.getDeliveryCenter() == null ? payBill.getTenant().getName() : payBill.getDeliveryCenter().getName() + "，买单返现" + setting.setScale(payBill.getBackDiscount()).toString() + "元，流水号:" + payBill.getSn() + "",
                            null, buyMember, null);
                    buyMessage.setDeposit(buyDeposit);
                    messageService.save(buyMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (payBill.getDeliveryCenter() != null) {
                    List<Employee> employees = employeeDao.findByDeliveryCenterId(payBill.getDeliveryCenter().getId());
                    if (employees != null) {
                        for (Employee employee : employees) {
                            if (employee.getTags() != null && employee.getTags().contains(tagDao.find(35L))) {
                                try {
                                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                                            payBill.getMember().getDisplayName() + "在" + (payBill.getDeliveryCenter() == null ? payBill.getTenant().getName() : payBill.getDeliveryCenter().getName()) + "，优惠买单" + setting.setScale(payBill.getClearingAmount()).toString() + "元，流水号:" + payBill.getSn() + "",
                                            null, employee.getMember(), null);
                                    message.setDeposit(deposit);
                                    message.setWay(Message.Way.tenant);
                                    messageService.save(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                if (payBill.getCouponCode() != null && payBill.getCouponCode().getCoupon().getType().equals(Coupon.Type.multipleCoupon)) {
                    //扣减套券金额
                    CouponCode couponCode = payBill.getCouponCode();
                    couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_WRITE);
                    if (couponCode.getBalance().compareTo(payBill.getDiscount()) > 0) {
                        couponCode.setBalance(couponCode.getBalance().subtract(payBill.getDiscount()));
                    } else {
                        couponCode.setBalance(BigDecimal.ZERO);
                        couponCode.setIsUsed(true);
                    }
                    couponCode.setUsedDate(new Date());
                    couponCodeDao.merge(couponCode);
                    
                    //扣联盟佣金
                    BigDecimal b = payBill.getBrokerage();
                    if (b.compareTo(BigDecimal.ZERO) > 0) {
                        memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);
                        tenantMember.setBalance(tenantMember.getBalance().subtract(b));
                        memberDao.merge(tenantMember);
                        Deposit bDeposit = new Deposit();
                        bDeposit.setType(Deposit.Type.rebate);
                        bDeposit.setStatus(Deposit.Status.complete);
                        bDeposit.setCredit(BigDecimal.ZERO);
                        bDeposit.setDebit(b);
                        bDeposit.setBalance(tenantMember.getBalance());
                        bDeposit.setOperator(operator != null ? operator.getUsername() : null);
                        bDeposit.setMember(tenantMember);
                        bDeposit.setMemo("联盟佣金");
                        bDeposit.setOrder(null);
                        bDeposit.setStatus(Deposit.Status.none);
                        depositDao.persist(bDeposit);
                        BigDecimal bkg = couponNumberService.resolveBrokerage(payBill.getDiscount(),payBill.getGuideBrokerage(),payBill.getGuideOwnerBrokerage(), payBill.getMember(), couponCode, payBill);
                        payBill.setGuideBrokerage(bkg);
                        payBillDao.merge(payBill);
                        
                        //生成平台佣金记录
                		Rebate rebate = new Rebate();
                		rebate.setType(Rebate.Type.platform);
                		rebate.setAmount(payBill.getBrokerage());
                		rebate.setBrokerage(payBill.getBrokerage().subtract(payBill.getGuideBrokerage()).subtract(payBill.getDiscount()).subtract(payBill.getGuideOwnerBrokerage()) );
                		rebate.setPercent(setting.getBrokerage());
                		rebate.setStatus(Rebate.Status.success);
                		rebate.setOrderType(OrderType.payBill);
                		rebate.setDescription("优惠买单平台所得佣金");
                		rebate.setPayBill(payBill);
                		rebateDao.persist(rebate);

                        //消费返现
                		Rebate rebate_member = new Rebate();
                		rebate_member.setType(Rebate.Type.rebate);
                		rebate_member.setAmount(payBill.getBrokerage());
                		rebate_member.setBrokerage(payBill.getDiscount());
                		rebate_member.setPercent(BigDecimal.ONE.subtract(setting.getBrokerage()).subtract(setting.getGuidePercent()).subtract(setting.getGuideOwnerPercent()));
                		rebate_member.setStatus(Rebate.Status.success);
                		rebate_member.setOrderType(OrderType.payBill);
                		rebate_member.setDescription("优惠买单消费返现");
                		rebate_member.setPayBill(payBill);
                        rebate_member.setMember(payBill.getMember());
                		rebateDao.persist(rebate_member);
                		
                        try {
                            Message message = EntitySupport.createInitMessage(Message.Type.account,
                                    "优惠买单 支出联盟佣金" + b.toString() + "元，流水号:" + payBill.getSn() + "",
                                    null, tenantMember, null);
                            message.setDeposit(bDeposit);
                            message.setWay(Message.Way.tenant);
                            messageService.save(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        
                    }
                }

            }

        }
    }

    /**
     * 优惠买单统计
     */
    public Page<PayBill> findMyPage(Tenant tenant, Date startDate, Date endDate, String keywords, PayBill.Status status, PayBill.Type type, Pageable pageable) {
        return payBillDao.findMyPage(tenant, startDate, endDate, keywords, status, type, pageable);
    }

    /**
     * 优惠买单统计(导出)
     */
    public List<PayBill> findMyList(Tenant tenant, Date startDate, Date endDate, String keywords, PayBill.Status status, PayBill.Type type) {
        return payBillDao.findMyList(tenant, startDate, endDate, keywords, status, type);
    }


    public boolean isLimit(Member member, Integer count) {
        return payBillDao.isLimit(member, count);
    }

    @Override
    public BigDecimal findPayBillSum(Tenant tenant, PayBill.Type type , PayBill.Status status, Date begin_date, Date end_date) {
        return payBillDao.findPayBillSum(tenant,type ,status,begin_date,end_date);
    }

    public Page<PayBill> findPage(String tenantName, String username, String paymentMethod,Date beginDate, Date endDate, Pageable pageable){
        return payBillDao.findPage(tenantName,username,paymentMethod,beginDate,endDate,pageable);
    }
}
