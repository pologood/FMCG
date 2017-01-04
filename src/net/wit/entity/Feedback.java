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
 * @ClassName: Feedback
 * @Description: 意见反馈
 * @author Administrator
 * @date 2014年10月14日 上午10:20:57
 */
@Entity
@Table(name = "xx_feedback")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_feedback_sequence")
public class Feedback extends BaseEntity {

	private static final long serialVersionUID = -3950317769006314385L;

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/feedback/content";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 内容 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, updatable = false)
	private String content;
	
	@Length(max = 200)
	private String replyContent;
	

	/** IP */
	@Column(nullable = false, updatable = false)
	private String ip;


	/** 图片 */
	@Valid
	@ElementCollection
	@CollectionTable(name = "xx_feedback_images")
	private List<ProductImage> images = new ArrayList<ProductImage>();

	/** 会员 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;


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

	
	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
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

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	
}