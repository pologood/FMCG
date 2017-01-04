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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 到货通知
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_product_notify")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_notify_sequence")
public class ProductNotify extends BaseEntity {

	private static final long serialVersionUID = 3192904068727393421L;

	/** E-mail */
	@JsonProperty
	@NotEmpty
	@Email
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String email;

	/** 是否已发送 */
	@Column(nullable = false)
	private Boolean hasSent;

	/** 会员 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;

	/** 商品 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Product product;

	// ===========================================getter/setter===========================================//
	/**
	 * 获取E-mail
	 * @return E-mail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置E-mail
	 * @param email E-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取是否已发送
	 * @return 是否已发送
	 */
	public Boolean getHasSent() {
		return hasSent;
	}

	/**
	 * 设置是否已发送
	 * @param hasSent 是否已发送
	 */
	public void setHasSent(Boolean hasSent) {
		this.hasSent = hasSent;
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
	 * 获取商品
	 * @return 商品
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * @param product 商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

}