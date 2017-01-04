/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: Employee
 * @Description: 员工管理
 * @author Administrator
 * @date 2014年10月14日 上午10:20:57
 */
@Entity
@Table(name = "xx_employee")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_employee_sequence")
public class Employee extends BaseEntity {

	private static final long serialVersionUID = -3950317762016303385L;

	public static final String RoleSplit = ",";

//	/** 角色类型 */
//	public enum Role {
//		/** 店主 */
//		owner,
//		/** 收银 */
//		cashier,
//		/** 导购 */
//		guide,
//		/** 店长 */
//		manager,
//		/** 财务 */
//		account
//	}
//
	/** 会员 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 店铺发货中心 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryCenter deliveryCenter;

	/** 店铺 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;
	
	/** 标签 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_employee_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();
	
	/** 销量 */
	@Expose
	@Column(nullable = false, precision = 27, scale = 12)
	private Integer quertity;

	/** 介绍 */
	@Length(max = 200)
	private String description;

	/** 角色 */
	private String role;

	public Member getMember() {
		return member;
	}

	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Integer getQuertity() {
		return quertity;
	}

	public void setQuertity(Integer quertity) {
		this.quertity = quertity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}