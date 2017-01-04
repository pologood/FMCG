/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.lucene.util.NumericUtils;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 地区
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_area")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_area_sequence")
public class Area extends OrderEntity {

	private static final long serialVersionUID = 1L;

	/** 类型 */
	public enum Status {
		/** 无定义 */
		none,
		/** 开通 */
		enabled,
		/** 关闭 */
		disabled
	}

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 树路径分隔符 */
	public static final String AREA_NAME = "area_name";

	/** 树路径分隔符 */
	public static final String AREA_ID = "area_id";

	/** 名称 */
	@Expose
	@NotEmpty
	@Length(max = 100)
	@Column(nullable = false, length = 100)
	private String name;

	/** 编码 */
	@Expose
	@JsonProperty
	@Column(length = 6)
	private String code;

	/** 编码 */
	@Expose
	@Column(length = 6)
	private String zipCode;

	/** 编码 */
	@Expose
	@Column(length = 4)
	private String telCode;

	/** 全称 */
	@Expose
	@JsonProperty
	@Column(nullable = false, length = 500)
	private String fullName;

	/** 树路径 */
	@Column(nullable = false, updatable = false)
	private String treePath;

	/** 上级地区 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Area parent;

	@JsonProperty
	private Status status;
	
	/** 地理位置 */
	@Embedded
	@JsonProperty
	private Location location;

	/** 下级地区 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	private Set<Area> children = new HashSet<Area>();

	/** 会员 */
	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	private Set<Member> members = new HashSet<Member>();

	/** 收货地址 */
	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	private Set<Receiver> receivers = new HashSet<Receiver>();

	/** 订单 */
	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	private Set<Order> orders = new HashSet<Order>();
	
	/** 是否审核 */
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	private Boolean isAudit;

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	private Boolean isOpen;

	public Boolean getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(Boolean isAudit) {
		this.isAudit = isAudit;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	/** 发货点 */
	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	private Set<DeliveryCenter> deliveryCenters = new HashSet<DeliveryCenter>();
	
	/** 所属商圈 */
	@OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
	private Set<Community> communities = new HashSet<Community>();

	public Status getStatus() {
		if (this.status == null) {
			return Status.none;
		}
		return status;
	}

	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
			setTreePath(parent.getTreePath() + parent.getId() + TREE_PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(TREE_PATH_SEPARATOR);
		}
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
		} else {
			setFullName(getName());
		}
	}
	
	public Area getCity() {
		Area a = this;
		if (a==null || a.getIsCity()) {
			return a;
		}
		a = a.getParent();
		if (a==null||a.getIsCity()) {
			return a;
		}
		a = a.getParent();
		if (a==null||a.getIsCity()) {
			return a;
		}
		return a;

	}
	
	public Set<Community> getCommunities() {
		return communities;
	}
	public void setCommunities(Set<Community> communities) {
		this.communities = communities;
	}
	
	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Member> members = getMembers();
		if (members != null) {
			for (Member member : members) {
				member.setArea(null);
			}
		}
		Set<Receiver> receivers = getReceivers();
		if (receivers != null) {
			for (Receiver receiver : receivers) {
				receiver.setArea(null);
			}
		}
		Set<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.setArea(null);
			}
		}
		Set<DeliveryCenter> deliveryCenters = getDeliveryCenters();
		if (deliveryCenters != null) {
			for (DeliveryCenter deliveryCenter : deliveryCenters) {
				deliveryCenter.setArea(null);
			}
		}
	}

	/** 重写toString方法 */
	@Override
	public String toString() {
		return getFullName();
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取名称
	 * @return 名称
	 */
	@JsonProperty
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
	 * 获取编码
	 * @return 编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置编码
	 * @param code 编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取编码
	 * @return 编码
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置编码
	 * @param zipCode 编码
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取编码
	 * @return 编码
	 */
	public String getTelCode() {
		return telCode;
	}

	/**
	 * 设置编码
	 * @param telCode 编码
	 */
	public void setTelCode(String telCode) {
		this.telCode = telCode;
	}

	/**
	 * 获取全称
	 * @return 全称
	 */
	@JsonProperty
	public String getFullName() {
		return fullName;
	}

	/**
	 * 设置全称
	 * @param fullName 全称
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * 获取树路径
	 * @return 树路径
	 */
	@JsonProperty
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 * @param treePath 树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取上级地区
	 * @return 上级地区
	 */
	public Area getParent() {
		return parent;
	}

	/**
	 * 设置上级地区
	 * @param parent 上级地区
	 */
	public void setParent(Area parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级地区
	 * @return 下级地区
	 */
	public Set<Area> getChildren() {
		return children;
	}

	/**
	 * 设置下级地区
	 * @param children 下级地区
	 */
	public void setChildren(Set<Area> children) {
		this.children = children;
	}

	/**
	 * 获取会员
	 * @return 会员
	 */
	public Set<Member> getMembers() {
		return members;
	}

	/**
	 * 设置会员
	 * @param members 会员
	 */
	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	/**
	 * 获取收货地址
	 * @return 收货地址
	 */
	public Set<Receiver> getReceivers() {
		return receivers;
	}

	/**
	 * 设置收货地址
	 * @param receivers 收货地址
	 */
	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	/**
	 * 获取订单
	 * @return 订单
	 */
	public Set<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * @param orders 订单
	 */
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取发货点
	 * @return 发货点
	 */
	public Set<DeliveryCenter> getDeliveryCenters() {
		return deliveryCenters;
	}

	/**
	 * 设置发货点
	 * @param deliveryCenters 发货点
	 */
	public void setDeliveryCenters(Set<DeliveryCenter> deliveryCenters) {
		this.deliveryCenters = deliveryCenters;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Boolean getIsCity() {
		if (code==null) {
			return false;
		}
		if (code.substring(code.length()-3,code.length()-1).equals("00")) {
			return true;
		} else {
			return false;
		}
	}
	
}