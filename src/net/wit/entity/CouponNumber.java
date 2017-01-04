package net.wit.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity - 平台套券记录表
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_coupon_number")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_coupon_number_sequence")
public class CouponNumber extends BaseEntity {

    public  enum Status{
        /** 已绑定 */
        bound,
        /** 已领取 */
        receive,
        /** 已用完 */
        used
    }

    /** 領取日期 */
    private Date receiveDate;

    /** 优惠券 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private Coupon coupon;

    /** 锁定或领取会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /** 获取优惠券时的序号 */
    private Long code;

    /**优惠码*/
    @OneToOne(fetch = FetchType.LAZY)
    private CouponCode couponCode;

    /**余额*/
    @Column(precision = 21, scale = 2)
    private BigDecimal balance;

    /** 分销导购 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member guideMember;

    /** 获取优惠券时使用的次數 */
    private Long useTimes;

    /**累计佣金*/
    @Column(precision = 21, scale = 2)
    private BigDecimal brokerage;

    /**状态*/
    private Status status;

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public CouponCode getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(CouponCode couponCode) {
        this.couponCode = couponCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Member getGuideMember() {
        return guideMember;
    }

    public void setGuideMember(Member guideMember) {
        this.guideMember = guideMember;
    }

    public Long getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Long useTimes) {
        this.useTimes = useTimes;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
