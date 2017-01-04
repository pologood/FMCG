/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import net.wit.entity.SpReturns.ReturnStatus;

/**
 * Entity - 退货项
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_sp_returns_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_sp_returns_item_sequence")
public class SpReturnsItem extends BaseEntity {

	private static final long serialVersionUID = -4112374596087084172L;

	/** 商品编号 */
	@NotEmpty
	@Column(nullable = false, updatable = false)
	private String sn;

	/** 商品名称 */
	@NotEmpty
	@Column(nullable = false, updatable = false)
	private String name;

	/** 发货数量 */
	@NotNull
	@Min(1)
	@Column(nullable = false, updatable = false)
	private Integer shippedQuantity;
	
	/** 退货数量 */
	@NotNull
	@Min(1)
	@Column(nullable = false, updatable = false)
	private Integer returnQuantity;

	/** 退货单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private SpReturns returns;
	
	/** 订单项 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private  OrderItem orderItem;
	
	// ===========================================getter/setter===========================================//
	/**
	 * 获取商品编号
	 * @return 商品编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置商品编号
	 * @param sn 商品编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取商品名称
	 * @return 商品名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置商品名称
	 * @param name 商品名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public SpReturns getReturns() {
		return returns;
	}

	public void setReturns(SpReturns returns) {
		this.returns = returns;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	
}