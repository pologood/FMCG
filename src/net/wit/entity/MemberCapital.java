package net.wit.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity - 会员台帐
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_member_capital")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_member_capital_sequence")
public class MemberCapital extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 会员 */
    @ManyToOne(fetch = FetchType.LAZY,optional=true)
    private Member member;
    /** 充值 - 收入 */
    private BigDecimal recharge;
    /** 购物 - 支出 */
    private BigDecimal payment;
    /** 提现 - 支出 */
    private BigDecimal withdraw;
    /** 货款 - 收入 */
    private BigDecimal receipts;
    /** 分润 - 收入 */
    private BigDecimal profit;
    /** 佣金 - 支出 */
    private BigDecimal rebate;
    /** 收款 - 收入 */
    private BigDecimal cashier;
    /** 其他 - 收入 */
    private BigDecimal income;
    /** 其他 - 支出 */
    private BigDecimal outcome;
    /** 红包 - 收入 */
    private BigDecimal coupon;
    /** 红包 - 支出 */
    private BigDecimal couponuse;
    /** 总收入 */
    private BigDecimal credit;
    /** 总支出 */
    private BigDecimal debit;
    /** 期初结余 */
    private BigDecimal lastCapital;
    /** 结余 */
    private BigDecimal capital;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getLastCapital() {
        return lastCapital;
    }

    public void setLastCapital(BigDecimal lastCapital) {
        this.lastCapital = lastCapital;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(BigDecimal withdraw) {
        this.withdraw = withdraw;
    }

    public BigDecimal getReceipts() {
        return receipts;
    }

    public void setReceipts(BigDecimal receipts) {
        this.receipts = receipts;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public BigDecimal getCashier() {
        return cashier;
    }

    public void setCashier(BigDecimal cashier) {
        this.cashier = cashier;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutcome() {
        return outcome;
    }

    public void setOutcome(BigDecimal outcome) {
        this.outcome = outcome;
    }

    public BigDecimal getCoupon() {
        return coupon;
    }

    public void setCoupon(BigDecimal coupon) {
        this.coupon = coupon;
    }

    public BigDecimal getCouponuse() {
        return couponuse;
    }

    public void setCouponuse(BigDecimal couponuse) {
        this.couponuse = couponuse;
    }
}
