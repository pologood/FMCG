/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 序列号
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_mobile_price")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_mobile_price_sequence")
public class MobilePrice extends BaseEntity {

	private static final long serialVersionUID = -2330598644835706164L;
	
	/** 产品编 号 */
	@JsonProperty
	@Column(nullable = false)
	private String prodId;

	/** 运营商 */
	@JsonProperty
	@Column(nullable = false)
	private String ispType;

	/** 面值 */
	@JsonProperty
	@Column(nullable = false)
	private Integer denomination;

	/** 售价 */
	@JsonProperty
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal price;

	/** 进价 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal cost;
	
	
	@JsonProperty
	private String province;
	@JsonProperty
	private String prodType;
	@JsonProperty
	private String delaytimes;


	// ===========================================getter/setter===========================================//

	/**
	 * 获取面值
	 * @return面值
	 */
	public Integer getDenomination() {
		return denomination;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	/**
	 * 设置面值
	 * @param denomination 面值
	 */
	public void setDenomination(Integer denomination) {
		this.denomination = denomination;
	}

	/**
	 * 获取售价
	 * @return 售价
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置售价
	 * @param price 售价
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取进价
	 * @return 进价
	 */
	public BigDecimal getCost() {
		return cost;
	}

	/**
	 * 设置进价
	 * @param cost 设置进价
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getIspType() {
		return ispType;
	}

	public void setIspType(String ispType) {
		this.ispType = ispType;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getDelaytimes() {
		return delaytimes;
	}

	public void setDelaytimes(String delaytimes) {
		this.delaytimes = delaytimes;
	}

}