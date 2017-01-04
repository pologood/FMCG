/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @ClassName: Credit
 * @Description: 收款单
 * @author Administrator
 * @date 2014年10月14日 上午10:21:13
 */
@Entity
@Table(name = "xx_credit")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_credit_sequence")
public class Credit extends BaseEntity {

	private static final long serialVersionUID = -5053540116564638634L;

	/** 支付方式分隔符 */
	public static final String PAYMENT_METHOD_SEPARATOR = " - ";

	/** 类型 */
	public enum Type {
		/** 信用卡还款 */
		visa,
		/** 账户提现 */
		cash
	}

	/** 方式 */
	public enum Method {
		/** 即时支付 */
		immediately,
		/** 快速支付 */
		fast,
		/** 普通支付 */
		general
	}

	/** 状态 */
	public enum Status {
		/** 等待支付 */
		wait,
		/** 提交银行 */
		wait_success,
		/** 支付成功 */
		success,
		/** 失败待退款 */
		wait_failure,
		/** 支付失败 */
		failure
	}

	/** 编号 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;

	/** 类型 */
	@Column(nullable = false, updatable = false)
	private Type type;

	/** 方式 */
	@NotNull
	@Column(nullable = false, updatable = false)
	private Method method;

	/** 状态 */
	@Column(nullable = false)
	private Status status;

	/** 支付方式 */
	@Column(updatable = false)
	private String paymentMethod;

	/** 收款银行 */
	@Length(max = 200)
	private String bank;

	/** 收款账号 */
	@Length(max = 200)
	private String account;

	/**收款人名称（★）  */
	private String acntToName;
	
	/**收款人开户行行号(大/小额,上海同城汇路此项必填)  */
	private String bankCode;
	
	/**收款人开户行名称（★） */
	@Column(nullable = false)
	private String bankName;
	
	/**收款人开户行地址 */
	private String bankAddr;
	
	/** 支付手续费 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal fee;

	/** 实收手续费 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal recv;

	/** 网关成本 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal cost;

	/** 付款结算金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal clearAmount;
	
	/** 付款金额 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal amount;

	/** 付款人 */
	@Length(max = 200)
	private String payer;

	/** 手机号 */
	@Length(max = 200)
	private String mobile;

	/** 操作员 */
	//@Column(updatable = false)
	private String operator;

	/** 付款日期 */
	private Date creditDate;

	/** 备注 */
	@Length(max = 200)
	private String memo;

	/** 支付插件ID */
	@JoinColumn(updatable = false)
	private String paymentPluginId;

	/** 到期时间 */
	@JoinColumn(updatable = false)
	private Date expire;

	/** 预存款 */
	@OneToOne(mappedBy = "xCredit", fetch = FetchType.LAZY)
	private Deposit deposit;

	/** 预存款 */
	@OneToOne(mappedBy = "xCredit", fetch = FetchType.LAZY)
	private Remittance remittance;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;

	/** 获取短账号 */
	public String getShortAcct() {
		if ((getAccount() != null) && (getAccount().length() > 4)) {
			return getAccount().substring(getAccount().length() - 4, getAccount().length());
		} else {
			return "";
		}
	}

	/** 获取有效金额 */
	public BigDecimal getEffectiveAmount() {
		BigDecimal effectiveAmount = getAmount().add(getFee());
		return effectiveAmount.compareTo(new BigDecimal(0)) > 0 ? effectiveAmount : new BigDecimal(0);
	}

	/** 获取实付金额 */
	public BigDecimal getPaymentAmount() {
		BigDecimal payamentAmount = getAmount().add(getRecv());
		return payamentAmount.compareTo(new BigDecimal(0)) > 0 ? payamentAmount : new BigDecimal(0);
	}

	/** 获取返利金额 */
	public BigDecimal getProfitAmount() {
		BigDecimal profitAmount = getFee().subtract(getRecv());
		return profitAmount.compareTo(new BigDecimal(0)) > 0 ? profitAmount : new BigDecimal(0);
	}

	/** 获取毛利金额 */
	public BigDecimal getRebateAmount() {
		BigDecimal rebateAmount = getRecv().subtract(getCost());
		return rebateAmount.compareTo(new BigDecimal(0)) > 0 ? rebateAmount : new BigDecimal(0);
	}

	/** 判断是否已过期 */
	public boolean hasExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		if (getDeposit() != null) {
			getDeposit().setxCredit(null);
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取编号
	 * @return 编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * @param sn 编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取类型
	 * @return 类型
	 */
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

	/**
	 * 获取方式
	 * @return 方式
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 设置方式
	 * @param method 方式
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

	public String getAcntToName() {
		return acntToName;
	}

	public void setAcntToName(String acntToName) {
		this.acntToName = acntToName;
	}
	
	/**
	 * 获取状态
	 * @return 状态
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * @param status 状态
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取支付方式
	 * @return 支付方式
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * 设置支付方式
	 * @param paymentMethod 支付方式
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 获取收款银行
	 * @return 收款银行
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 设置收款银行
	 * @param bank 收款银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取收款账号
	 * @return 收款账号
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * 设置收款账号
	 * @param account 收款账号
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 获取支付手续费
	 * @return 支付手续费
	 */
	public BigDecimal getFee() {
		return fee;
	}

	/**
	 * 设置支付手续费
	 * @param fee 支付手续费
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	/**
	 * 获取实收手续费
	 * @return 实收手续费
	 */
	public BigDecimal getRecv() {
		return recv;
	}

	/**
	 * 设置实收手续费
	 * @param recv 实收手续费
	 */
	public void setRecv(BigDecimal recv) {
		this.recv = recv;
	}

	/**
	 * 获取网关成本
	 * @return 网关成本
	 */
	public BigDecimal getCost() {
		return cost;
	}

	/**
	 * 设置网关成本
	 * @param fee 网关成本
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getClearAmount() {
		return clearAmount;
	}

	public void setClearAmount(BigDecimal clearAmount) {
		this.clearAmount = clearAmount;
	}

	/**
	 * 获取付款金额
	 * @return 付款金额
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置付款金额
	 * @param amount 付款金额
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获取付款人
	 * @return 付款人
	 */
	public String getPayer() {
		return payer;
	}

	/**
	 * 设置付款人
	 * @param cedit 付款人
	 */
	public void setPayer(String payer) {
		this.payer = payer;
	}

	/**
	 * 获取手机号
	 * @return 手机号
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置手机号
	 * @param mobile 手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	 * 获取付款日期
	 * @return 付款日期
	 */
	public Date getCreditDate() {
		return creditDate;
	}

	/**
	 * 设置付款日期
	 * @param ceditDate 付款日期
	 */
	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
	}

	/**
	 * 获取备注
	 * @return 备注
	 */
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
	 * 获取支付插件ID
	 * @return 支付插件ID
	 */
	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	/**
	 * 设置支付插件ID
	 * @param paymentPluginId 支付插件ID
	 */
	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}

	/**
	 * 获取到期时间
	 * @return 到期时间
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * 设置到期时间
	 * @param expire 到期时间
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}

	/**
	 * 获取预存款
	 * @return 预存款
	 */
	public Deposit getDeposit() {
		return deposit;
	}

	/**
	 * 设置预存款
	 * @param deposit 预存款
	 */
	public void setDeposit(Deposit deposit) {
		this.deposit = deposit;
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
	
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddr() {
		return bankAddr;
	}

	public void setBankAddr(String bankAddr) {
		this.bankAddr = bankAddr;
	}

	public Remittance getRemittance() {
		return remittance;
	}

	public void setRemittance(Remittance remittance) {
		this.remittance = remittance;
	}
}