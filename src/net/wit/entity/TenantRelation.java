/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * Entity - 往来单位
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_tenant_relation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_tenant_relation_sequence")
public class TenantRelation extends BaseEntity {

	private static final long serialVersionUID = 5395521437312782717L;

	/** 状态    */
	public enum Status {
		none, success, fail
	}

	/** 我自已商家 */
	@Expose(serialize = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tenant")
	private Tenant tenant;

	/** 我的供应商 */
	@Expose(serialize = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private Tenant parent;

	/** 会员等级 */
	@Expose(serialize = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberRank")
	private MemberRank memberRank;

	private Status status;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Tenant getParent() {
		return parent;
	}

	public void setParent(Tenant parent) {
		this.parent = parent;
	}

	public MemberRank getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(MemberRank memberRank) {
		this.memberRank = memberRank;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}