/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 社区
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_community")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_community_sequence")
public class Community extends BaseEntity {

	private static final long serialVersionUID = 1472373494571485132L;

	/** 状态 */
	public enum Status {
		/** 申请 */
		wait,
		/** 开通 */
		open,
		/** 关闭 */
		close
	}

	/** 编码 */
	@JsonProperty
	private String code;

	/** 名称 */
	@Length(max = 255)
	@JsonProperty
	private String name;

	/** 描述 */
	@Lob
	private String descr;

	/** 地区 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;

	/** 状态 */
	@JsonProperty
	private Status status;

	/** 展示图片 */
	@Length(max = 200)
	@JsonProperty
	private String image;

	/** 归属配送物业公司 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonProperty
	private Enterprise enterprise;

	/** 标签 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_community_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();

	/** 收货地址信息 */
	@OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
	private Set<Receiver> receivers = new HashSet<Receiver>();

	/** 收货地址信息 */
	@OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
	private Set<DeliveryCenter> deliveryCenters = new HashSet<DeliveryCenter>();

	/** 经纬度 */
	@Embedded
	@JsonProperty
	private Location location;

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Receiver> receivers = getReceivers();
		if (receivers != null) {
			for (Receiver receiver : receivers) {
				receiver.setCommunity(null);
			}
		}
		Set<DeliveryCenter> deliveryCenters = getDeliveryCenters();
		if (deliveryCenters != null) {
			for (DeliveryCenter deliveryCenter : deliveryCenters) {
				deliveryCenter.setCommunity(null);
			}
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 编码
	 * @return 编码
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 名称
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取页面描述
	 * @return 页面描述
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * 设置页面描述
	 * @param Descr 页面描述
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * 状态
	 * @return 状态
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * @param status 状态
	 */
	public void setStatus(Status status) {
		this.status = status;
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
	 * 获取展示图片
	 * @return 展示图片
	 */
	public String getImage() {
		return image;
	}

	/**
	 * 设置展示图片
	 * @param image 展示图片
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 获取标签
	 * @return 标签
	 */
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * 设置标签
	 * @param tags 标签
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * 获取经纬度
	 * @return 经纬度
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 设置经纬度
	 * @param location 经纬度
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Set<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	public Set<DeliveryCenter> getDeliveryCenters() {
		return deliveryCenters;
	}

	public void setDeliveryCenters(Set<DeliveryCenter> deliveryCenters) {
		this.deliveryCenters = deliveryCenters;
	}
}