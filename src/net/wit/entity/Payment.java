/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.util.SettingUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Administrator
 * @ClassName: Payment
 * @Description: 收款单
 * @date 2014年10月14日 上午9:10:34
 */
@Entity
@Table(name = "xx_payment")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_payment_sequence")
public class Payment extends BaseEntity {

    private static final long serialVersionUID = -5052430116564638634L;

    /**
     * 支付方式分隔符
     */
    public static final String PAYMENT_METHOD_SEPARATOR = " - ";

    /**
     * 类型
     */
    public enum Type {
        /**
         * 订单支付
         */
        payment,
        /**
         * 账户充值
         */
        recharge,
        /**
         * 线下代收
         */
        cashier,
        /**
         * 功能缴费
         */
        function,
        /**
         * 买单立减
         */
        paybill,
        /**
         * 红包支付
         */
        coupon,
        /**
         *联盟支付
         */
        union
    }

    /**
     * 方式
     */
    public enum Method {
        /**
         * 在线支付
         */
        online,
        /**
         * 线下支付
         */
        offline,
        /**
         * 账户支付
         */
        deposit
    }

    /**
     * 状态
     */
    public enum Status {
        /**
         * 等待支付
         */
        wait,
        /**
         * 支付成功
         */
        success,
        /**
         * 支付失败
         */
        failure
    }

    /**
     * 编号
     */
    @Column(nullable = false, updatable = false, unique = true, length = 100)
    private String sn;

    /**
     * 支付公司SN
     */
    private String paySn;

    /**
     * 类型
     */
    @Column(nullable = false, updatable = false)
    private Type type;

    /**
     * 方式
     */
    @NotNull
    @Column(nullable = false)
    private Method method;

    /**
     * 状态
     */
    @Column(nullable = false)
    private Status status;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 收款银行
     */
    @Length(max = 200)
    private String bank;

    /**
     * 收款账号
     */
    @Length(max = 200)
    private String account;

    /**
     * 支付手续费
     */
    @Column(nullable = false, precision = 21, scale = 6)
    private BigDecimal fee;

    /**
     * 付款结算金额
     */
    @NotNull
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(nullable = false, precision = 21, scale = 6)
    private BigDecimal clearAmount;

    /**
     * 付款金额
     */
    @NotNull
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(nullable = false, updatable = false, precision = 21, scale = 6)
    private BigDecimal amount;

    /**
     * 付款人
     */
    @Length(max = 200)
    private String payer;

    /**
     * 操作员
     */
    @Column(updatable = false)
    private String operator;

    /**
     * 付款日期
     */
    private Date paymentDate;

    /**
     * 备注
     */
    @Length(max = 200)
    private String memo;

    /**
     * 支付插件ID
     */
    @JoinColumn(updatable = false)
    private String paymentPluginId;

    /**
     * 到期时间
     */
    @JoinColumn(updatable = false)
    private Date expire;

    /**
     * 预存款
     */
    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Deposit deposit;

    /**
     * 会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private Member member;

    /**
     * 订单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders", updatable = false)
    private Order order;

    /**
     * 子订单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private Trade trade;

    /**
     * 买单记录
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private PayBill payBill;

    /**
     * 买单记录
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private UnionTenant unionTenant;

    /**
     * 冻结红包
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private Coupon coupon;

    public UnionTenant getUnionTenant() {
        return unionTenant;
    }

    public void setUnionTenant(UnionTenant unionTenant) {
        this.unionTenant = unionTenant;
    }

    /**
     * 应用流水
     */
    @JsonProperty
    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private Set<Commission> commissions = new HashSet<Commission>();

    /**
     * 获取有效金额
     */
    public BigDecimal getEffectiveAmount() {
        BigDecimal effectiveAmount = getAmount().subtract(getFee());
        return effectiveAmount.compareTo(new BigDecimal(0)) > 0 ? effectiveAmount : new BigDecimal(0);
    }

    /**
     * 判断是否已过期
     */
    public boolean hasExpired() {
        return getExpire() != null && new Date().after(getExpire());
    }

    /**
     * 持久化前处理
     */
    @PrePersist
    public void prePersist() {
        if (getClearAmount() == null) {
            setClearAmount(BigDecimal.ZERO);
        }
    }

    /**
     * 删除前处理
     */
    @PreRemove
    public void preRemove() {
        if (getDeposit() != null) {
            getDeposit().setPayment(null);
        }
    }

    // ===========================================getter/setter===========================================//

    public Set<Commission> getCommissions() {
        return commissions;
    }

    public void setCommissions(Set<Commission> commissions) {
        this.commissions = commissions;
    }

    /**
     * 获取编号
     *
     * @return 编号
     */
    public String getSn() {
        return sn;
    }

    /**
     * 设置编号
     *
     * @param sn 编号
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    public Type getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * 获取方式
     *
     * @return 方式
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 设置方式
     *
     * @param method 方式
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * 获取状态
     *
     * @return 状态
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * 获取支付方式
     *
     * @return 支付方式
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * 设置支付方式
     *
     * @param paymentMethod 支付方式
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * 获取收款银行
     *
     * @return 收款银行
     */
    public String getBank() {
        return bank;
    }

    /**
     * 设置收款银行
     *
     * @param bank 收款银行
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 获取收款账号
     *
     * @return 收款账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置收款账号
     *
     * @param account 收款账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取支付手续费
     *
     * @return 支付手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置支付手续费
     *
     * @param fee 支付手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getClearAmount() {
        return clearAmount;
    }

    public void setClearAmount(BigDecimal clearAmount) {
        this.clearAmount = clearAmount;
    }

    /**
     * 获取付款金额
     *
     * @return 付款金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置付款金额
     *
     * @param amount 付款金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取付款人
     *
     * @return 付款人
     */
    public String getPayer() {
        return payer;
    }

    /**
     * 设置付款人
     *
     * @param payer 付款人
     */
    public void setPayer(String payer) {
        this.payer = payer;
    }

    /**
     * 获取操作员
     *
     * @return 操作员
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 设置操作员
     *
     * @param operator 操作员
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取付款日期
     *
     * @return 付款日期
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * 设置付款日期
     *
     * @param paymentDate 付款日期
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * 获取备注
     *
     * @return 备注
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置备注
     *
     * @param memo 备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取支付插件ID
     *
     * @return 支付插件ID
     */
    public String getPaymentPluginId() {
        return paymentPluginId;
    }

    /**
     * 设置支付插件ID
     *
     * @param paymentPluginId 支付插件ID
     */
    public void setPaymentPluginId(String paymentPluginId) {
        this.paymentPluginId = paymentPluginId;
    }

    /**
     * 获取到期时间
     *
     * @return 到期时间
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * 设置到期时间
     *
     * @param expire 到期时间
     */
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    /**
     * 获取预存款
     *
     * @return 预存款
     */
    public Deposit getDeposit() {
        return deposit;
    }

    /**
     * 设置预存款
     *
     * @param deposit 预存款
     */
    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    /**
     * 获取会员
     *
     * @return 会员
     */
    public Member getMember() {
        return member;
    }

    /**
     * 设置会员
     *
     * @param member 会员
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * 获取订单
     *
     * @return 订单
     */
    public Order getOrder() {
        return order;
    }

    /**
     * 设置订单
     *
     * @param order 订单
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    public String getPaySn() {
        return paySn;
    }

    public void setPaySn(String paySn) {
        this.paySn = paySn;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public PayBill getPayBill() {
        return payBill;
    }

    public void setPayBill(PayBill payBill) {
        this.payBill = payBill;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getTenantName() {
        String tenantName = null;
        if (getPayBill() != null) {
            if (getPayBill().getTenant() != null) {
                tenantName = getPayBill().getTenant().getName();
            }
        }

        if (getOrder() != null && getOrder().getTrades().size() > 0) {
            Trade trade = getOrder().getTrades().get(0);
            if (trade.getTenant() != null) {
                tenantName = trade.getTenant().getName();
            }
        }

        if (tenantName == null) {
            tenantName = SettingUtils.get().getSiteName();
        }

        return tenantName;
    }
}