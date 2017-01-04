/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;

import com.google.gson.annotations.Expose;
import net.wit.Setting;
import net.wit.util.SettingUtils;

/**
 * Entity - 买单立减
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_pay_bill")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_buy_bill_sequence")
public class PayBill extends BaseEntity {

	private static final long serialVersionUID = -2135598654835726164L;

	/** 状态 */
	public enum Status {
		/** 等待支付 */
		none,
		/** 支付成功 */
		success,
		/** 支付失败 */
		failure
	}

	/** 类型 */
	public enum  Type {
		/** 代金券 */
		coupon,
		/** 活动优惠 */
		promotion,
		/** 收银台 */
		cashier
	}

	/** 序号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;

	/** 商家活动名称 */
	private String activityName;

	/** 金额《含手续费》 */
	private BigDecimal amount;

	/** 不参与优惠的金额 */
	private BigDecimal noAmount;

	/** 商家活动 */
	private BigDecimal tenantDiscount;
	
	/** 平台活动 */
	private BigDecimal discount;
	
	/** 返现金额 */
	private BigDecimal backDiscount;

	/** 导购佣金 */
	private BigDecimal guideBrokerage;

	/** 导购店主佣金 */
	private BigDecimal guideOwnerBrokerage;

	/** 联盟佣金 */
	private BigDecimal brokerage;
	
	/** 状态 */
	private Status status;
	
	/** 类型 */
	private Type type;
	
	/** 付款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment payment;

	/** 店铺代金券 */
	@ManyToOne(fetch = FetchType.LAZY)
	private CouponCode tenantCouponCode;
	
	/** 平台代金券 */
	@ManyToOne(fetch = FetchType.LAZY)
	private CouponCode couponCode;

	/** 所属会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;


	/** 所属会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 分润 */
	@Expose
	@Valid
	@OneToMany(mappedBy = "payBill", fetch = FetchType.LAZY)
	private List<Rebate> rebate = new ArrayList<Rebate>();

	/** 店铺发货中心 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryCenter deliveryCenter;
	// ===========================================getter/setter===========================================//
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getNoAmount() {
		return noAmount;
	}

	public void setNoAmount(BigDecimal noAmount) {
		this.noAmount = noAmount;
	}

	public BigDecimal getTenantDiscount() {
		return tenantDiscount;
	}

	public void setTenantDiscount(BigDecimal tenantDiscount) {
		this.tenantDiscount = tenantDiscount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getBackDiscount() {
		return backDiscount;
	}

	public void setBackDiscount(BigDecimal backDiscount) {
		this.backDiscount = backDiscount;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public CouponCode getTenantCouponCode() {
		return tenantCouponCode;
	}

	public void setTenantCouponCode(CouponCode tenantCouponCode) {
		this.tenantCouponCode = tenantCouponCode;
	}

	public CouponCode getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}
	
	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public BigDecimal getGuideOwnerBrokerage() {
		return guideOwnerBrokerage;
	}

	public void setGuideOwnerBrokerage(BigDecimal guideOwnerBrokerage) {
		this.guideOwnerBrokerage = guideOwnerBrokerage;
	}

	public BigDecimal getEffectiveAmount() {
		BigDecimal price = getAmount();
		if (getDiscount()!=null) {
			price = price.subtract(getDiscount());
		}
		if (getTenantDiscount()!=null) {
			price = price.subtract(getTenantDiscount());
		}
		return price;
	}
	public BigDecimal getClearingAmount() {
		BigDecimal price = getAmount();
		if (getTenantDiscount()!=null) {
			price = price.subtract(getTenantDiscount());
		}
		if (getBackDiscount()!=null) {
			price = price.subtract(getBackDiscount());
		}
		return price;
	}
	
	public BigDecimal getGuideBrokerage() {
		return guideBrokerage;
	}

	public void setGuideBrokerage(BigDecimal guideBrokerage) {
		this.guideBrokerage = guideBrokerage;
	}

	public List<Rebate> getRebate() {
		return rebate;
	}

	public void setRebate(List<Rebate> rebate) {
		this.rebate = rebate;
	}

	//计算佣金
	public BigDecimal calcBrokerage() {
		BigDecimal price = getClearingAmount();

		Setting setting = SettingUtils.get();
		BigDecimal discount = setting.setScale(price.multiply(getTenant().getAgency()));
		return discount;
	}
	
	//计算导购佣金
	public BigDecimal calcGuideBrokerage() {
		BigDecimal b = calcBrokerage();
		Setting setting = SettingUtils.get();
		BigDecimal guide = setting.setScale(b.multiply(setting.getGuidePercent()));
		return guide;
	}

	//计算导购店主佣金
	public BigDecimal calcGuideOwnerBrokerage() {
		BigDecimal b = calcBrokerage();
		Setting setting = SettingUtils.get();
		BigDecimal guide = setting.setScale(b.multiply(setting.getGuideOwnerPercent()));
		return guide;
	}

	//计算平台佣金
	public BigDecimal calcProvBrokerage() {
		BigDecimal b = calcBrokerage();
		Setting setting = SettingUtils.get();
		BigDecimal prov = setting.setScale(b.multiply(setting.getBrokerage()));
		return prov;
	}
	
	//计算套券的扣减金额
	public BigDecimal calcDiscount(BigDecimal balance) {
		BigDecimal b = calcBrokerage();
		BigDecimal g = calcGuideBrokerage();
		BigDecimal p = calcProvBrokerage();
		BigDecimal d = calcGuideOwnerBrokerage();
		BigDecimal discount = b.subtract(g).subtract(p).subtract(d);
		return discount.compareTo(balance) > 0 ? balance :discount;
	}

}