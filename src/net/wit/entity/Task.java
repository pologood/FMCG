/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.entity.Authen.AuthenStatus;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 员工任务计划表
 * @author rsico Team
 * @version 3.0
 */

@Entity
@Table(name = "xx_task")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_task_sequence")
public class Task extends BaseEntity {

	private static final long serialVersionUID = -6314152029623997372L;

	/** 类型 */
	public enum Type {
		/** 月计划      	0*/
		month
	}

	/** 角色类型 */
	@NotNull
	private Type type;

	/** 月份 201611 */
	@NotNull
	private Long month;

	/** 店铺 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 员工 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 销售金额-计划 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal sale;

	/** 发展会员-计划 */
	@Min(0)
	private Long invite;

	/** 推广红包-计划 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private Long coupon;

	/** 分享链接-计划 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private Long share;

	/** 销售金额-执行 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal doSale;

	/** 发展会员-执行 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private Long doInvite;

	/** 推广红包-执行 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private Long doCoupon;

	/** 分享链接-执行 */
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private Long doShare;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BigDecimal getSale() {
		return sale;
	}

	public void setSale(BigDecimal sale) {
		this.sale = sale;
	}

	public Long getInvite() {
		return invite;
	}

	public void setInvite(Long invite) {
		this.invite = invite;
	}

	public Long getCoupon() {
		return coupon;
	}

	public void setCoupon(Long coupon) {
		this.coupon = coupon;
	}

	public Long getShare() {
		return share;
	}

	public void setShare(Long share) {
		this.share = share;
	}

	public BigDecimal getDoSale() {
		return doSale;
	}

	public void setDoSale(BigDecimal doSale) {
		this.doSale = doSale;
	}

	public Long getDoInvite() {
		return doInvite;
	}

	public void setDoInvite(Long doInvite) {
		this.doInvite = doInvite;
	}

	public Long getDoCoupon() {
		return doCoupon;
	}

	public void setDoCoupon(Long doCoupon) {
		this.doCoupon = doCoupon;
	}

	public Long getDoShare() {
		return doShare;
	}

	public void setDoShare(Long doShare) {
		this.doShare = doShare;
	}

	public Long getMonth() { return month; }

	public void setMonth(Long month) {
		this.month = month;
	}
}