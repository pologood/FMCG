package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "xx_commission")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_commission_sequence")
public class Commission extends BaseEntity {

	private static final long serialVersionUID = 5395521437416725217L;

	/**
	 * 类型
	 */
	public enum Type {
		/** 平台推广 */
		none,
		/** 业务推广 */
		admin,
		/** 店家推广 */
	    member
	}
	
	/**
	 * 类型
	 */
	public enum Status {
		/** 待支付 */
		unpay,
		/** 已支付 */
		paid,
		/** 店家推广 */
		complete
	}


	private Type type;
	
	/**
	 * 企业
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Enterprise enterprise;
	
	/**
	 * 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin", nullable = false)
	private Admin admin;

	/**
	 * 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member", nullable = false)
	private Member member;

	/**
	 * 区域
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area", nullable = false)
	private Area area;

	/**
	 * 支付单号
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment payment;
	
	/**
	 * 购买应用
	 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Application application;
	
	/**
	 * 购买金额
	 */
	private BigDecimal amount;

	/**
	 * 业务提成
	 */
	private BigDecimal brokerage;
	
	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 描述
	 */
	private Status status;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	
}
