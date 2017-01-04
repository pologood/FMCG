package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @ClassName: Account
 * @Description: 结算单
 * @author Administrator
 * @date 2014年10月14日 上午9:10:49
 */
@Entity
@Table(name = "xx_account")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_account_sequence")
public class Account extends BaseEntity {

	private static final long serialVersionUID = -540706734359671149L;

	/** 结算状态 */
	public enum Status {
		/** 待结算 */
		none,
		/** 已结算 */
		success
	}

 	/** 结算金额 */
	private BigDecimal amount;
	
	/** 结算单号*/
	private String sn;

	/** 是否入账 */
	private Status status;

	/** 销售商 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 供应商 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant supplier;
	
	public String getSn() {
		return sn;
	}

	public Tenant getSupplier() {
		return supplier;
	}

	public void setSupplier(Tenant supplier) {
		this.supplier = supplier;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
}
