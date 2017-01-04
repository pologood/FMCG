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
 * Entity - 会员
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_consumer")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_consumer_sequence")
public class Consumer extends BaseEntity {

	private static final long serialVersionUID = 5395521437312785217L;

	/** 状态 */
	public enum Status {
		/**待审核**/
		none, 
		/**已审核**/
		enable, 
		/**已禁用**/
		disable
	};

	/** 加盟商家 */
	@Expose(serialize = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member")
	private Member member;

	/** 商家 */
	@Expose(serialize = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tenant")
	private Tenant tenant;

	/** 会员等级 */
	@Expose(serialize = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberRank")
	private MemberRank memberRank;

	private Status status;


	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
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