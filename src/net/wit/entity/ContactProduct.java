/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @ClassName: ContactProduct
 * @Description: 社交圈商品
 * @author Administrator
 * @date 2014年10月14日 上午9:10:08
 */
@Embeddable
public class ContactProduct implements Serializable, Comparable<ContactProduct> {

	private static final long serialVersionUID = -673885610094536107L;

	/** 商品编号 */
	private String sn;

	/** 商品名称 */
	private String name;

	/** 商品全称 */
	private String fullName;

	/** 商品价格 */
	private BigDecimal price;

	/** 商品原价 */
	private BigDecimal marketPrice;
	
	/** 大图片 */
	private String large;

	/** 中图片 */
	private String medium;

	/** 缩略图 */
	private String thumbnail;

	/**介绍*/
	private String introduction;
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/** 排序 */
	@Min(0)
	@Column(name = "orders")
	private Integer order;

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	/** 判断是否为空 */
	public boolean isEmpty() {
		return (getSn() == null || StringUtils.isEmpty(getLarge()) || StringUtils.isEmpty(getMedium()) || StringUtils.isEmpty(getThumbnail()));
	}

	/** 实现compareTo方法 */
	public int compareTo(ContactProduct contactProduct) {
		return new CompareToBuilder().append(getOrder(), contactProduct.getOrder()).toComparison();
	}

}