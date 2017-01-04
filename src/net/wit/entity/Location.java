/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName: Location
 * @Description: 地理信息经纬度
 * @author Administrator
 * @date 2014年10月14日 上午10:20:28
 */
@Embeddable
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 经度 lng y */
	@Column(name = "lng", precision = 28, scale = 11)
	private BigDecimal lng;

	/** 纬度 lat x */
	@Column(name = "lat", precision = 28, scale = 11)
	private BigDecimal lat;

	/** 最后一次更新时间 */
	// private Date latestTime;

	/** 地区 */
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "location_area")
	// private Area area;
	public Location() {
	}

	public Location(BigDecimal lat, BigDecimal lng) {
		this.lng = lng;
		this.lat = lat;
	}

	@Transient
	@JsonIgnore
	public boolean isExists() {
		return this.lat != null && this.lng != null && (this.lat.compareTo(BigDecimal.ZERO)!=0) && (this.lng.compareTo(BigDecimal.ZERO)!=0);
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取经度
	 * @return 经度
	 */
	public BigDecimal getLng() {
		return lng;
	}

	/**
	 * 设置经度
	 * @param y 经度
	 */
	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	/**
	 * 获取纬度
	 * @return x 纬度
	 */
	public BigDecimal getLat() {
		return lat;
	}

	/**
	 * 设置纬度
	 * @param y 纬度
	 */
	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location) {
			Location bmapPoint = (Location) obj;
			return (bmapPoint.getLng() == lng && bmapPoint.getLat() == lat) ? true : false;
		} else {
			return false;
		}
	}
}