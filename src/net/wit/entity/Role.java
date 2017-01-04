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

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import net.wit.entity.Authen.AuthenStatus;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 角色
 * @author rsico Team
 * @version 3.0
 */

@Entity
@Table(name = "xx_role")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_role_sequence")
public class Role extends BaseEntity {

	private static final long serialVersionUID = -6614052029623997372L;

	/** 认证类型 */
	public enum RoleType {
		/** 超级管理员      	0*/
		admin,
		/** 总代理-平台商 		1*/
		proxy,
		/** 省代理 				2*/
		provinceproxy,
		/** 市代理 				3*/
		cityproxy,
		/** 县代理 				4*/
		countyproxy,
		/** 个人代理 			5*/
		personproxy,
		/** 商家助手专用类型 	6*/
		helper
	}

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;
	
	/** 角色类型 */
	@Expose
	@NotNull
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	private RoleType roleType;

	/** 店铺 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 是否内置 */
	@Column(nullable = false, updatable = false)
	private Boolean isSystem;

	/** 描述 */
	@Length(max = 200)
	private String description;

	/** 企业信息 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Enterprise enterprise;

	/** 权限 */
	@ElementCollection
	@CollectionTable(name = "xx_role_authority")
	private List<String> authorities = new ArrayList<String>();

	/** 管理员 */
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<Admin> admins = new HashSet<Admin>();

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Admin> admins = getAdmins();
		if (admins != null) {
			for (Admin admin : admins) {
				admin.getRoles().remove(this);
			}
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取名称
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

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	/**
	 * 获取是否内置
	 * @return 是否内置
	 */
	public Boolean getIsSystem() {
		return isSystem;
	}

	/**
	 * 设置是否内置
	 * @param isSystem 是否内置
	 */
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	/**
	 * 获取描述
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 * @param description 描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取权限
	 * @return 权限
	 */
	public List<String> getAuthorities() {
		return authorities;
	}

	/**
	 * 设置权限
	 * @param authorities 权限
	 */
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	/**
	 * 获取管理员
	 * @return 管理员
	 */
	public Set<Admin> getAdmins() {
		return admins;
	}

	/**
	 * 设置管理员
	 * @param admins 管理员
	 */
	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
}