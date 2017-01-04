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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: ExpertConsultation
 * @Description: 咨询
 * @author Administrator
 * @date 2014年10月14日 上午10:20:57
 */
@Entity
@Table(name = "xx_expert_consultation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_expert_consultation_sequence")
public class ExpertConsultation extends BaseEntity {

	private static final long serialVersionUID = -3950317769996303385L;

	/** 内容 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String content;

	/** 是否显示 */
	@Column(nullable = false)
	private Boolean isShow;

	/** IP */
	@Column(nullable = false, updatable = false)
	private String ip;

	/** 会员 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;

	/** 专家 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Expert expert;

	/** 分类 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private ExpertCategory expertCategory;

	/** 咨询 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private ExpertConsultation forExpertConsultation;

	/** 回复 */
	@JsonProperty
	@OneToMany(mappedBy = "forExpertConsultation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<ExpertConsultation> replyExpertConsultations = new HashSet<ExpertConsultation>();

	// ===========================================getter/setter===========================================//
	
	/**
	 * 获取内容
	 * @return 内容
	 */
	public String getContent() {
		return content;
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

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
	}

	/**
	 * 获取咨询
	 * @return 咨询
	 */
	public ExpertConsultation getForExpertConsultation() {
		return forExpertConsultation;
	}

	/**
	 * 设置咨询
	 * @param forConsultation 咨询
	 */
	public void setForExpertConsultation(ExpertConsultation forExpertConsultation) {
		this.forExpertConsultation = forExpertConsultation;
	}

	/**
	 * 获取回复
	 * @return 回复
	 */
	public Set<ExpertConsultation> getReplyExpertConsultations() {
		return replyExpertConsultations;
	}

	/**
	 * 设置回复
	 * @param replyConsultations 回复
	 */
	public void setReplyExpertConsultations(Set<ExpertConsultation> replyExpertConsultations) {
		this.replyExpertConsultations = replyExpertConsultations;
	}

	public ExpertCategory getExpertCategory() {
		return expertCategory;
	}

	public void setExpertCategory(ExpertCategory expertCategory) {
		this.expertCategory = expertCategory;
	}

}