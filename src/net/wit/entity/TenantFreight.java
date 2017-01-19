/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.wit.Setting;
import net.wit.util.SettingUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @ClassName: Location
 * @Description: 运费
 * @author Administrator
 * @date 2014年10月14日 上午10:20:28
 */
@Entity
@Table(name = "xx_tenant_freight")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_tenant_freight_sequence")
public class TenantFreight extends BaseEntity {
	private static final long serialVersionUID = 123L;
	
	/** 类型 */
	public enum Type {
		/**计距离*/
		distance,
		/** 计重 */
		weight,
		/** 计件 */
		piece
	}

	/** 类型 */
	@Column(nullable = false)
	private Type freightType;
	
	/** 首重量 */
	@Column(nullable = false)
	private Integer firstWeight;

	/** 续重量 */
	@Column(nullable = false)
	@Min(1)
	private Integer continueWeight;

	/** 首重价格 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal firstPrice;

	/** 续重价格 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal continuePrice;

	/** 地区 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;

	/** 店铺 */
	@NotNull
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 模版 */
	@ManyToOne(fetch = FetchType.LAZY)
	private TenantFreightTemplate tenantFreightTemplate;

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Type getFreightType() {
		return freightType;
	}

	public void setFreightType(Type freightType) {
		this.freightType = freightType;
	}

	public TenantFreightTemplate getTenantFreightTemplate() {
		return tenantFreightTemplate;
	}

	public void setTenantFreightTemplate(TenantFreightTemplate tenantFreightTemplate) {
		this.tenantFreightTemplate = tenantFreightTemplate;
	}

	public Integer getFirstWeight() {
		return firstWeight;
	}


	public void setFirstWeight(Integer firstWeight) {
		this.firstWeight = firstWeight;
	}


	public Integer getContinueWeight() {
		return continueWeight;
	}


	public void setContinueWeight(Integer continueWeight) {
		this.continueWeight = continueWeight;
	}


	public BigDecimal getFirstPrice() {
		return firstPrice;
	}


	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}


	public BigDecimal getContinuePrice() {
		return continuePrice;
	}


	public void setContinuePrice(BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}

}