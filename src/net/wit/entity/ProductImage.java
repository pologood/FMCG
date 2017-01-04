/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * Entity - 商品图片
 * @author rsico Team
 * @version 3.0
 */
@Embeddable
public class ProductImage implements Serializable, Comparable<ProductImage> {

	private static final long serialVersionUID = -673883300094536107L;

	public enum ImageType {
		/** 普通图片 */
		normal,
		/** 整体图片 */
		whole,
		/** 细节图片 */
		details,
		/** 补充图片 */
		additional
	}
	
	/** 标题 */
	@Length(max = 200)
	private String title;

	/** 原图片 */
	private String source;

	/** 大图片 */
	private String large;

	/** 中图片 */
	private String medium;

	/** 缩略图 */
	private String thumbnail;

	/** 排序 */
	@Min(0)
	@Column(name = "orders")
	private Integer order;

	/** 图片类型 */
	private ImageType imageType;
	
	/** 文件 */
	@Transient
	private MultipartFile file;

	/** 本地文件 */
	@Transient
	private File localFile;

	/** 本地文件 */
	@Transient
	private String local;
	
	
	/** 判断是否为空 */
	public boolean isEmpty() {
		return (getFile() == null || getFile().isEmpty()) && (StringUtils.isEmpty(getLocal())) && (StringUtils.isEmpty(getSource()) || StringUtils.isEmpty(getLarge()) || StringUtils.isEmpty(getMedium()) || StringUtils.isEmpty(getThumbnail()));
	}

	/** 实现compareTo方法 */
	public int compareTo(ProductImage productImage) {
		return new CompareToBuilder().append(getOrder(), productImage.getOrder()).toComparison();
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取标题
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * @param title 标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取原图片
	 * @return 原图片
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 设置原图片
	 * @param source 原图片
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 获取大图片
	 * @return 大图片
	 */
	public String getLarge() {
		//return getSource().concat("@800h_800w_4e");
		return large;
	}

	/**
	 * 设置大图片
	 * @param large 大图片
	 */
	public void setLarge(String large) {
		this.large = large;
	}

	/**
	 * 获取中图片
	 * @return 中图片
	 */
	public String getMedium() {
		//return getSource().concat("@300h_300w_4e");
		return medium;
	}

	/**
	 * 设置中图片
	 * @param medium 中图片
	 */
	public void setMedium(String medium) {
		this.medium = medium;
	}

	/**
	 * 获取缩略图
	 * @return 缩略图
	 */
	public String getThumbnail() {
		//return getSource().concat("@100h_100w_4e");
		return thumbnail;
	}

	/**
	 * 设置缩略图
	 * @param thumbnail 缩略图
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * 获取排序
	 * @return 排序
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * @param order 排序
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * 获取文件
	 * @return 文件
	 */

	public MultipartFile getFile() {
		return file;
	}

	/**
	 * 设置文件
	 * @param file 文件
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public File getLocalFile() {
		return localFile;
	}

	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

}