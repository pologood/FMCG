/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import net.wit.util.PhoneticZhCNUtil;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: Receiver
 * @Description: 收货地址
 * @author Administrator
 * @date 2014年10月14日 上午9:11:00
 */
@Entity
@Table(name = "xx_receiver")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_receiver_sequence")
public class Receiver extends BaseEntity {

	private static final long serialVersionUID = 2673602067029665976L;

	/** 收货地址最大保存数 */
	public static final Integer MAX_RECEIVER_COUNT = 8;

	/** 收货人 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String consignee;

	/** 地区名称 */
	@JsonProperty
	@Column(nullable = false)
	private String areaName;

	/** 地址 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String address;

	/** 邮编 */
	@JsonProperty
	@Length(max = 200)
	private String zipCode;

	/** 电话 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String phone;

	/** 是否默认 */
	@JsonProperty
	@NotNull
	@Column(nullable = false)
	private Boolean isDefault;

	/** 地区 */
	@NotNull
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 归属商圈 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn()
	@JsonProperty
	private Community community;

	/** 地理位置 */
	@Embedded
	@JsonProperty
	private Location location;

	
	/** 订单 */
	@OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
	private Set<Order> orders = new HashSet<Order>();

	/** 获取邮编 */
	public String getZipCode() {
		if (zipCode == null) {
			return area.getZipCode();
		}
		return zipCode;
	}

	public String getPhonetic() {
		return PhoneticZhCNUtil.getZhCNFirstSpell(this.consignee).substring(0, 1).toUpperCase();
	}

	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取收货人
	 * @return 收货人
	 */
	public String getConsignee() {
		return consignee;
	}

	/**
	 * 设置收货人
	 * @param consignee 收货人
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 获取地区名称
	 * @return 地区名称
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置地区名称
	 * @param areaName 地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 获取地址
	 * @return 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * @param address 地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 设置邮编
	 * @param zipCode 邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * @return 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * @param phone 电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取是否默认
	 * @return 是否默认
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 设置是否默认
	 * @param isDefault 是否默认
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取地区
	 * @return 地区
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * @param area 地区
	 */
	public void setArea(Area area) {
		this.area = area;
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

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	
}