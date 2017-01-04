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
 * Entity - 库存表
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_stock")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_stock_sequence")
public class Stock extends BaseEntity {

	private static final long serialVersionUID = -2133598654435526264L;

	/** 商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 发货地 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryCenter deliveryCenter;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	/** 库存 */
	private BigDecimal stock;

	/** 已分配库存 */
	private BigDecimal allocatedStock;

	// ===========================================getter/setter===========================================//
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public BigDecimal getAllocatedStock() {
		return allocatedStock;
	}

	public void setAllocatedStock(BigDecimal allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

}