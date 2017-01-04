/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * Entity - 系列号
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_sn_deposit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_sn_deposit_sequence")
public class SnDeposit extends BaseEntity {

	private static final long serialVersionUID = -8323451873042981882L;

	/** 类型 */
	public enum Type {
		income,
		/** 其他 - 支出 */
		outcome
	}

	/** 类型 */
	public enum Status {
		/** 处理中 */
		none,
		/** 已完成 */
		complete
	}
	
	/** 类型 */
	@Column(nullable = false, updatable = false)
	private Type type;

	/** 收入 */
	@Column(nullable = false, updatable = false)
	private Integer credit;

	/** 支出 */
	@Column(nullable = false, updatable = false)
	private BigDecimal debit;

	/** 结余 */
	@Column(nullable = false, updatable = false)
	private Integer balance;

	/** 操作员 */
	@Column(updatable = false)
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(updatable = false)
	private String memo;

	/** 代理商 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Enterprise enterprise;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMemo() {
		return memo;
	}	

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
	
}