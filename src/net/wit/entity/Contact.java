/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: Contact
 * @Description: 社交圈
 * @author Administrator
 * @date 2014年10月14日 上午10:20:57
 */
@Entity
@Table(name = "xx_contact")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_contact_sequence")
public class Contact extends BaseEntity {

	private static final long serialVersionUID = -3950317769006303385L;

	/** 类型 */
	public enum Type {
		/** 订单秀 */
		order,
		/** 魔拍秀 */
		camera,
		/** 任性秀 */
		wayward
	}

	/** 内容 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String content;

	/** 类型 */
	@Column(nullable = false)
	private Type type;

	/** 是否显示 */
	@Column(nullable = false)
	private Boolean isShow;

	/** IP */
	@Column(nullable = false)
	private String ip;

	/** 会员 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;

	/** 咨询 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Contact forContact;

	/** 回复 */
	@JsonProperty
	@OneToMany(mappedBy = "forContact", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Contact> replyContacts = new HashSet<Contact>();

	/** 商品 */
	@Valid
	@ElementCollection
	@CollectionTable(name = "xx_contact_product")
	private List<ContactProduct> products = new ArrayList<ContactProduct>();
	
	/** 点赞 */
	@Valid
	@ElementCollection
	@CollectionTable(name = "xx_contact_praise")
	private List<Member> praises = new ArrayList<Member>();

	/** 图片 */
	@Valid
	@ElementCollection
	@CollectionTable(name = "xx_contact_images")
	private List<ProductImage> images = new ArrayList<ProductImage>();
	
	/** 点击数 */
	@Column(nullable = false)
	private Long hits;

	// ===========================================getter/setter===========================================//
	
	/**
	 * 获取内容
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 设置内容
	 * @param content 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取是否显示
	 * @return 是否显示
	 */
	public Boolean getIsShow() {
		return isShow;
	}

	/**
	 * 设置是否显示
	 * @param isShow 是否显示
	 */
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	/**
	 * 获取IP
	 * @return IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP
	 * @param ip IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
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

	public Contact getForContact() {
		return forContact;
	}

	public void setForContact(Contact forContact) {
		this.forContact = forContact;
	}

	public Set<Contact> getReplyContacts() {
		return replyContacts;
	}

	public void setReplyContacts(Set<Contact> replyContacts) {
		this.replyContacts = replyContacts;
	}

	public List<ContactProduct> getProducts() {
		return products;
	}

	public void setProducts(List<ContactProduct> products) {
		this.products = products;
	}

	public List<Member> getPraises() {
		return praises;
	}

	public void setPraises(List<Member> praises) {
		this.praises = praises;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}


}