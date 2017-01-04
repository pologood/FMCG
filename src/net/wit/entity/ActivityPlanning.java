/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 平台活动方案
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_activity_planning")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_activity_planning_sequence")
public class ActivityPlanning extends OrderEntity {
              
	private static final long serialVersionUID = 3536993535267962279L;

	/** 方案类型 */
	public enum Type {
		/** 随机减 */
		random,
		/** 联盟活动 */
		unionActivity
	}

	/** 开关 */
	public enum OnOff{
		/** 待审核*/
		wait,
		/**启用*/
		on,
		/**停用*/
		off,
		/** 已过期*/
		hasRunOut
	}

	/** 活动名称 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;
	
	/** 起始日期 */
	@Expose
	@JsonProperty
	private Date beginDate;

	/** 结束日期 */
	@Expose
	@JsonProperty
	private Date endDate;

	/** 平台限制单数 0 为不限 */
	private Integer activityMaximumOrders;


	/** 商家限制单数 0 为不限 */
	private Integer tenantMaximumOrders;

	/** 活动介绍 */
	@Expose
	@JsonProperty
	private String introduction;

	/** 方案类型 */
	@Expose
	private Type type;

	@Expose
	private OnOff onOff;


	/** 代金券 */
	@OneToMany(mappedBy= "activityPlanning", fetch= FetchType.LAZY,cascade = CascadeType.REMOVE)
	private Set<Coupon> coupons = new HashSet<Coupon>();
	
	/** 报名商家 */
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	@JoinTable(name = "xx_activity_planning_tenant")
	private Set<Tenant> tenants = new HashSet<Tenant>();

	/** 活动栏位 */
	@OneToMany(mappedBy = "activityPlanning", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	private Set<AdPosition> adPositions = new HashSet<AdPosition>();

	/** 商品栏位 */
	@OneToMany(mappedBy = "activityPlanning", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	@OrderBy("id asc")
	private Set<SingleProductPosition> singleProductPositions = new HashSet<SingleProductPosition>();

	/** 活动预算 */
	@Expose
	@JsonProperty
	private BigDecimal totalPrice = BigDecimal.ZERO;

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		if (getCoupons() != null) {
			setCoupons(getCoupons());
		}
		if (getTenants() != null) {
			setTenants(getTenants());
		}
		if (getAdPositions() != null) {
			setAdPositions(getAdPositions());
		}

		if(getSingleProductPositions()!=null){
			setSingleProductPositions(getSingleProductPositions());
		}
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		if (getTenants() != null) {
			getTenants().clear();
		}
		if(getCoupons()!=null){
			getCoupons().clear();
		}
		if(getAdPositions()!=null){
			getAdPositions().clear();
		}

		if(getSingleProductPositions()!=null){
			getSingleProductPositions().clear();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getActivityMaximumOrders() {
		return activityMaximumOrders;
	}

	public void setActivityMaximumOrders(Integer activityMaximumOrders) {
		this.activityMaximumOrders = activityMaximumOrders;
	}

	public Integer getTenantMaximumOrders() {
		return tenantMaximumOrders;
	}

	public void setTenantMaximumOrders(Integer tenantMaximumOrders) {
		this.tenantMaximumOrders = tenantMaximumOrders;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	public Set<Tenant> getTenants() {
		return tenants;
	}

	public void setTenants(Set<Tenant> tenants) {
		this.tenants = tenants;
	}

	public Set<AdPosition> getAdPositions() {
		return adPositions;
	}

	public void setAdPositions(Set<AdPosition> adPositions) {
		this.adPositions = adPositions;
	}

	public Set<SingleProductPosition> getSingleProductPositions() {
		return singleProductPositions;
	}

	public void setSingleProductPositions(Set<SingleProductPosition> singleProductPositions) {
		this.singleProductPositions = singleProductPositions;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public OnOff getOnOff() {

		if(getEndDate().before(new Date())){
			return OnOff.hasRunOut;
		}

		return onOff;
	}

	public void setOnOff(OnOff onOff) {
		if(getEndDate().before(new Date())){
			onOff = OnOff.hasRunOut;
		}

		this.onOff = onOff;
	}

	public String getStatus() {
		if(getOnOff()==OnOff.on){
			if(getEndDate().before(new Date())){
				return "end";
			}else {
				if(getBeginDate().before(new Date())){
					return "begin";
				}else {
					return "wait";
				}
			}
		}else {
			return "end";
		}
	}
	
	public List<Coupon> lockCoupon(BigDecimal amount) {
		List<Coupon> coupons = new ArrayList<Coupon>();
		for (Coupon coupon:getCoupons()) {
			if (coupon.getAmount().compareTo(amount)<=0 && coupon.getMinimumPrice().compareTo(amount)<=0) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}
}