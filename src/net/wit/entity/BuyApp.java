/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity - 购买应用
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_buy_app")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_buy_app_sequence")
public class BuyApp extends BaseEntity {

	private static final long serialVersionUID = -2134598654835726164L;

	/** 单位 */
	public enum Type {
		/** 按年缴费 */
		year,
		/** 按月缴费 */
		month
	}

	/** 状态 */
	public enum Status {
		/** 等待支付 */
		wait,
		/** 支付成功 */
		success,
		/** 支付失败 */
		failure
	}

	/** 序号 */
	private String sn;

	/** 代码 */
	private String code;

	/** 名称 */
	private String name;

	/** 单位 */
	private Type type;

	/** 数量 */
	private BigDecimal quantity;

	/** 金额《含手续费》 */
	private BigDecimal amount;

	/** 手续费 */
	private BigDecimal fee;

	/** 支付状态 */
	private Status status;

	/** 应用 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Application application;

	/** 所属会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	// ===========================================getter/setter===========================================//
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getCode() {
		return code;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
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
	public Application getApplication() {
		return application;
	}

	/**
	 * 设置应用
	 * @param application 应用
	 */
	public void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * 获取应用
	 * @return 应用
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

}