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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Entity - 广告位
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_ad_position")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_ad_position_sequence")
@FilterDef(name = "tenantFilter", defaultCondition = "tenant = :tenantId", parameters = @ParamDef(name = "tenantId", type = "long"))
public class AdPosition extends BaseEntity {

	private static final long serialVersionUID = -7849848867030199578L;

	public enum Type {
		/** 手机商城 	0*/
		mobile,
		/** 批发商城 	1*/
		pc_b2b,
		/** 零售商城 	2*/
		pc_b2c,
		/** 店家助手 	3*/
		pc_box,
		/** 供应商平台 	4*/
		pc_supplier,
		/** 微信商城	5*/
		weChatStore,
		/** 活动广告	6*/
		activity,
		/** 频道广告	7*/
		channel,
		/** C端商城     8*/
		chatStore,
		/** 购物屏     9*/
		pad,
		/** 联盟活动	10*/
		unionActivity
	}

	/** 广告位类型 */
	private Type type;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 宽度 */
	@NotNull
	@Min(1)
	@Column(nullable = false)
	private Integer width;

	/** 高度 */
	@NotNull
	@Min(1)
	@Column(nullable = false)
	private Integer height;

	/** 描述 */
	@Length(max = 200)
	private String description;

	/** 模板 */
	@NotEmpty
	@Lob
	@Column(nullable = false)
	private String template;

	/** 推广信息 */
	@Length(max = 200)
	private String spreadmemo;

	@ManyToOne(fetch = FetchType.LAZY)
	private ProductChannel productChannel;

	@ManyToOne(fetch = FetchType.LAZY)
	private ActivityPlanning activityPlanning;

	/** 广告 */
	@OneToMany(mappedBy = "adPosition", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Filters({ @Filter(name = "tenantFilter") })
	@OrderBy("order asc")
	private Set<Ad> ads = new HashSet<Ad>();

	// ===========================================getter/setter===========================================//
	/**
	 * 获取广告位类型
	 * @return 广告位类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置广告位类型
	 * @param type 广告位类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

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

	/**
	 * 获取宽度
	 * @return 宽度
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * 设置宽度
	 * @param width 宽度
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * 获取高度
	 * @return 高度
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * 设置高度
	 * @param height 高度
	 */
	public void setHeight(Integer height) {
		this.height = height;
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
	 * 推广描述
	 * @return 推广描述
	 */
	public String getSpreadmemo() {
		return spreadmemo;
	}

	/**
	 * 设置推广描述
	 * @param spreadmemo 推广描述
	 */
	public void setSpreadmemo(String spreadmemo) {
		this.spreadmemo = spreadmemo;
	}

	/**
	 * 获取模板
	 * @return 模板
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * 设置模板
	 * @param template 模板
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * 获取广告
	 * @return 广告
	 */
	public Set<Ad> getAds() {
		return ads;
	}

	/**
	 * 设置广告
	 * @param ads 广告
	 */
	public void setAds(Set<Ad> ads) {
		this.ads = ads;
	}

	public ProductChannel getProductChannel() {
		return productChannel;
	}

	public void setProductChannel(ProductChannel productChannel) {
		this.productChannel = productChannel;
	}

	@Transient
	public String getPath() {
		String path = "";
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			String redirectUrl = request.getRequestURI().toString();
			path = redirectUrl.substring(request.getContextPath().length() + 1);
			String[] split1 = path.split("/");
			path = request.getContextPath() + "/" + split1[0];
		}
		return path;
	}

	public ActivityPlanning getActivityPlanning() {
		return activityPlanning;
	}

	public void setActivityPlanning(ActivityPlanning activityPlanning) {
		this.activityPlanning = activityPlanning;
	}

	
}