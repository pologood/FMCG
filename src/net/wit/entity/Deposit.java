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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 预存款
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_deposit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_deposit_sequence")
public class Deposit extends BaseEntity {

	private static final long serialVersionUID = -8323452873046981882L;

	/** 类型 */
	public enum Type {
		/** 充值 - 收入 */
		recharge,
		/** 购物 - 支出 */
		payment,
		/** 提现 - 支出 */
		withdraw,
		/** 货款 - 收入 */
		receipts,
		/** 分润 - 收入 */
		profit,
		/** 佣金 - 支出 */
		rebate,
		/** 收款 - 收入 */
		cashier,
		/** 其他 - 收入 */
		income,
		/** 其他 - 支出 */
		outcome,
		/** 红包 - 收入 */
		coupon,
		/** 红包 - 支出 */
		couponuse
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

	/** 状态 */
	@Column(nullable = false, updatable = false)
	private Status status;
	
	/** 收入金额 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal credit;

	/** 支出金额 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal debit;

	/** 当前余额 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal balance;

	/** 操作员 */
	@Column(updatable = false)
	private String operator;

	/** 备注 */
	@Length(max = 200)
	@Column(updatable = false)
	private String memo;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders")
	private Order order;

	/** 收款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment payment;

	/** 付款单 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Credit xCredit;

	// ===========================================getter/setter===========================================//
	/**
	 * 获取类型
	 * @return 类型
	 */
	@JsonProperty
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * @param type 类型
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取收入金额
	 * @return 收入金额
	 */
	@JsonProperty
	public BigDecimal getCredit() {
		return credit;
	}

	/**
	 * 设置收入金额
	 * @param credit 收入金额
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	/**
	 * 获取支出金额
	 * @return 支出金额
	 */
	@JsonProperty
	public BigDecimal getDebit() {
		return debit;
	}

	/**
	 * 设置支出金额
	 * @param debit 支出金额
	 */
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	/**
	 * 获取当前余额
	 * @return 当前余额
	 */
	@JsonProperty
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * 设置当前余额
	 * @param balance 当前余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * 获取操作员
	 * @return 操作员
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * @param operator 操作员
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 获取备注
	 * @return 备注
	 */
	@JsonProperty
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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

	/**
	 * 获取收款单
	 * @return 收款单
	 */
	public Payment getPayment() {
		return payment;
	}

	/**
	 * 设置收款单
	 * @param payment 收款单
	 */
	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	/**
	 * 获取付款单
	 * @return 付款单
	 */
	public Credit getxCredit() {
		return xCredit;
	}

	/**
	 * 设置付款单
	 * @param cedit 付款单
	 */
	public void setxCredit(Credit xCredit) {
		this.xCredit = xCredit;
	}
	
	public BigDecimal getAdCharge() {
		if (this.getType().equals(Type.recharge)) {
			return this.getCredit();
		} else
			
		if (this.getType().equals(Type.payment)) {
			return this.getDebit().multiply(new BigDecimal("-1"));
		} else
		
		if (this.getType().equals(Type.withdraw)) {
			return this.getDebit().multiply(new BigDecimal("-1"));
		} else
		
		if (this.getType().equals(Type.receipts)) {
			return this.getCredit();
		} else
		
		if (this.getType().equals(Type.profit)) {
			return this.getCredit();
		} else
		
		if (this.getType().equals(Type.rebate)) {
			return this.getDebit().multiply(new BigDecimal("-1"));
		} else
		
		if (this.getType().equals(Type.cashier)) {
			return this.getCredit();
		} else
		
		if (this.getType().equals(Type.income)) {
			return this.getCredit();
		} else
		
		if (this.getType().equals(Type.outcome)) {
			return this.getDebit().multiply(new BigDecimal("-1"));
		} else
		
		if (this.getType().equals(Type.coupon)) {
			return this.getCredit();
		} else
		
		if (this.getType().equals(Type.couponuse)) {
			return this.getDebit().multiply(new BigDecimal("-1"));
		} else {
			return BigDecimal.ZERO;
		}
	}
}