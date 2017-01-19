/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 优惠码
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_coupon_code")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_coupon_code_sequence")
public class CouponCode extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 号码 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String code;

	/** 是否已使用 */
	@Column(nullable = false)
	private Boolean isUsed;

	/** 获取优惠券时使用的积分 */
	private Long point;

	/** 使用日期 */
	private Date usedDate;

	/** 上次提醒时间 */
	private Date reminderTime;

	/** 优惠券类型 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Coupon coupon;

	/** 分销导购 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member guideMember;

	/** 锁定或领取会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 核销人 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member operateMember;

	/** 核销时间 */
	private Date operateDate;

	/** 使用代金券的商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;
	
	/** 券余额 */
	@Column(nullable = false, precision = 21, scale = 2)
	private BigDecimal balance;


	/** 核销金额 */
	@Column(precision = 21, scale = 2)
	private BigDecimal amount;

	/** 到期时间 */
	@Expose
	@JsonProperty
	private Date expire;

	/** 到期时间 */
	@Expose
	@JsonProperty
	private Date lockExpire;
	
	/** 订单 */
	@OneToOne(mappedBy = "couponCode", fetch = FetchType.LAZY)
	@JoinColumn(name = "orders")
	private Order order;

	/** 促销 */
	@OneToOne(mappedBy = "couponCode", fetch = FetchType.LAZY)
	@JoinColumn(name = "promotionMembers")
	private PromotionMember promotionMember;

	/** 使用次数 */
	@Min(0)
	@Column(nullable = false,columnDefinition = "int(11) default 0")
	private Integer useCount = new Integer(0);

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		if (getOrder() != null) {
			getOrder().setCouponCode(null);
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取号码
	 * @return 号码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置号码
	 * @param code 号码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取优惠券抵扣积分
	 * @return 优惠券抵扣积分
	 */
	public Long getPoint() {
		return point;
	}

	/**
	 * 设置优惠券抵扣积分
	 * @param point 优惠券抵扣积分
	 */
	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 获取是否已使用
	 * @return 是否已使用
	 */
	public Boolean getIsUsed() {
		return isUsed;
	}

	/**
	 * 设置是否已使用
	 * @param isUsed 是否已使用
	 */
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	/**
	 * 获取使用日期
	 * @return 使用日期
	 */
	public Date getUsedDate() {
		return usedDate;
	}

	/**
	 * 设置使用日期
	 * @param usedDate 使用日期
	 */
	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	public Date getReminderTime() {
		return reminderTime;
	}

	public void setReminderTime(Date reminderTime) {
		this.reminderTime = reminderTime;
	}

	/**
	 * 获取优惠券
	 * @return 优惠券
	 */
	public Coupon getCoupon() {
		return coupon;
	}

	/**
	 * 设置优惠券
	 * @param coupon 优惠券
	 */
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Member getGuideMember() {
		return guideMember;
	}

	public void setGuideMember(Member guideMember) {
		this.guideMember = guideMember;
	}

	/**
	 * 获取会员
	 * @return 会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置会员
	 * @param member 会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	public Member getOperateMember() {
		return operateMember;
	}

	public void setOperateMember(Member operateMember) {
		this.operateMember = operateMember;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	/**
	 * 获取订单
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * @param order 订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	public PromotionMember getPromotionMember() {
		return promotionMember;
	}

	public void setPromotionMember(PromotionMember promotionMember) {
		this.promotionMember = promotionMember;
	}

	/** 判断是否已可用 */
	public boolean isUsable() {
		return isEnabled() && isStart() && hasExpired();
	}

	/** 判断是否已启用 */
	public boolean isEnabled() {
		return getCoupon().getIsEnabled() != null && getCoupon().getIsEnabled();
	}

	/** 判断是否已开始 */
	public boolean isStart() {
		if(getCoupon().getType().equals(Coupon.Type.multipleCoupon)){
			return true;
		}
		return getCoupon().getStartDate() != null && new Date().after(getCoupon().getStartDate());
	}

	/** 判断是否已过期 */
	public boolean hasExpired() {
		if(getCoupon().getType().equals(Coupon.Type.multipleCoupon)){
			return false;
		}
		return getCoupon().getEndDate() != null && new Date().after(getCoupon().getEndDate());
	}


	public boolean isUsables() {
		return isEnabled() && isStart() && hasExpired()&&hasExpireds();
	}

	public Date getLockExpire() {
		return lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	public boolean isLocked(BigDecimal amount) {
		return (getMember()!=null && amount.compareTo(getCoupon().getMinimumPrice())>=0 && getLockExpire()!=null && getLockExpire().before(DateUtils.addSeconds(new Date(), 60*5)));
	}
	public void locked(Member member) {
		setMember(member);
		setLockExpire(new Date());
	}
	
	public boolean hasExpireds(){
		if(getExpire()!=null){
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(getExpire());

			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(new Date());

			boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
			boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
			boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

			return isSameDate;
		}
		return false;
	}

}