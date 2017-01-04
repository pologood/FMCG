/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 购买应用
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_application")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_application_sequence")
public class Application extends BaseEntity {

	private static final long serialVersionUID = -2134598654835727294L;

	/** 类型 */
	public enum Type {
		/** ERP */
		erp,
		/** APP */
		app,
		/** 监控 */
		video		
	}

	/** 状态 */
	public enum Status {
		/** 新装 */
		none,
		/** 开通 */
		opened,
		/** 关闭 */
		closed
	}

	/** 类型 */
	private Type type;
	
	/** 代码 */
	private String code;

	/** 名称 */
	private String name;

	/** 单价 */
	private BigDecimal price;

	private Date validityDate;

	/** 支付状态 */
	private Status status;

	/** 所属会员 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 所属企业 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;
	
	// ===========================================getter/setter===========================================//
	
	
	public String getCode() {
		return code;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取所属会员
	 * @return 所属会员
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置所属会员
	 * @param member 所属会员
	 */
	public void setMember(Member member) {
		this.member = member;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	
}