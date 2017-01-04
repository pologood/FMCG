package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.wit.entity.Rebate.OrderType;

/**
 * @ClassName: Rebate
 * @Description: 利润分配
 * @author Administrator
 * @date 2014年10月14日 上午9:10:49
 */
@Entity
@Table(name = "xx_rebate")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_rebate_sequence")
public class Rebate extends BaseEntity {

	private static final long serialVersionUID = -541766724349671149L;

	/** 绑定状态 */
	public enum Status {
		/** 没入账 */
		none,
		/** 已入账 */
		success
	}
	
	/** 类型 */
	public enum Type {
		/** 平台佣金 */
		platform,
		/** 推广佣金 */
		extension,
		/** 销售佣金 */
		sale,
		/** 消费返利 */
		rebate
	}

	/** 订单类型 */
	public enum OrderType {
		/** 无单据 */
		none,
		/** 订单 */
		order,
		/** 优惠买单 */
		payBill,
		/** 代金券 */
		coupon
	}

	/** 订单佣金 */
	private BigDecimal brokerage;

	/** 分配金额 */
	private BigDecimal amount;

	/** 分配比例 */
	private BigDecimal percent;

	/** 是否入账 */
	private Type type;

	/** 是否入账 */
	private Status status;

	/** 是否入账 */
	private OrderType orderType;
	
	/** 描述 */
	private String description;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Trade trade;

	/** 优惠买单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private PayBill payBill;

	/** 代金券 */
	@ManyToOne(fetch = FetchType.LAZY)
	private CouponCode couponCode;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public CouponCode getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	
}
