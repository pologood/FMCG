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
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 促销商品
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_promotion_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_promotion_product_sequence")
public class PromotionProduct extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 时间区间 */
	public enum TimeRegion {
		notstart, progress, past
	}

	/** 促销方案 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotion")
	private Promotion promotion;

	/** 促销产品 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product")
	private Product product;

	/** 每笔方案的产品数量 */
	@Expose
	@JsonProperty
	@Min(0)
	private Integer quantity;

	/** 促销价格 */
	@Expose
	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	private BigDecimal price;

	/** 订单临时价格 */
	@Transient
	private BigDecimal tempPrice;

	// ===========================================getter/setter===========================================//
	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTempPrice() {
		return tempPrice;
	}

	public void setTempPrice(BigDecimal tempPrice) {
		this.tempPrice = tempPrice;
	}

}