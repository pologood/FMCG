/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Entity - 联盟商家
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_union_tenant")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_union_tenant_sequence")
public class UnionTenant extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/** 状态 */
	public enum Status {
		/** 待确认 */
		unconfirmed,
		/** 已冻结  */
		freezed,
		/** 已确认  */
		confirmed,
		/** 已取消  */
		canceled
	}

	/** 类型 */
	public enum Type {
		/** 商家联盟 */
		tenant,
		/** 购物屏联盟  */
		device
	}


	/** 联盟类型 */
	@NotNull
	private Type type;

	/** 状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Status status;

	/** 所属联盟 */
	@ManyToOne(fetch = FetchType.LAZY,optional=true)
	private Union unions;

	/** 加盟设备，只有设备联盟时必填 */
	@ManyToOne(fetch = FetchType.LAZY,optional=true)
	private Equipment equipment;
	
	/** 加盟商家 */
	@ManyToOne(fetch = FetchType.LAZY,optional=true)
	private Tenant tenant;

	/** 加盟费用 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	private BigDecimal price;

	/** 订单支付 */
	@ManyToOne(fetch = FetchType.LAZY,optional=true)
	private Payment payment;

	/** 销售 */
	private BigDecimal sales;

	/** 支出 */
	private BigDecimal pay;

	/** 成交订单数 */
	private BigDecimal volume;

	/** 客单价 */
	private BigDecimal unitPrice;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/** 有效期 */
	private Date expire;

	public Union getUnion() {
		return unions;
	}

	public void setUnion(Union unions) {
		this.unions = unions;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getPay() {
		return pay;
	}

	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}

	public BigDecimal getSales() {
		return sales;
	}

	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}

}