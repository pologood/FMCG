/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import com.google.gson.annotations.Expose;
import freemarker.template.TemplateException;
import net.wit.util.FreemarkerUtils;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @ClassName: Barcode
 * @Description: 序列号
 * @author Administrator
 * @date 2014年10月13日 下午2:21:22
 */
@Entity
@Table(name = "xx_barcode")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_barcode_sequence")
public class Barcode extends BaseEntity {

	private static final long serialVersionUID = -2330598644835706164L;

	/** 名称 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 拼音码 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String spell;

	/** 条码 */
	@JsonProperty
	@NotEmpty
	@Length(max = 13)
	@Column(nullable = false)
	private String barcode;

	/** 单位 */
	@JsonProperty
	@NotEmpty
	@Length(max = 10)
	@Column(nullable = false)
	private String unitName;

	/** 参考进价 */
	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal inPrice;

	/** 参考售价 */
	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal outPrice;

	/** 商品分类 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductCategory productCategory;

	/*品牌*/
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Brand brand;

	/** 图片 */
	@Expose
	@JsonProperty
	@Valid
	@ElementCollection
	@CollectionTable(name = "xx_barcode_images")
	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	/** 商品详情-介绍 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Lob
	private String introduction;

	/** 商品详情-APP介绍 */
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Lob
	private String descriptionapp;

	/** 行业标签 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Tag tag;
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

	/**
	 * 获取拼音码
	 * @return 拼音码
	 */
	public String getSpell() {
		return spell;
	}

	/**
	 * 设置拼音码
	 * @param name 拼音码
	 */
	public void setSpell(String spell) {
		this.spell = spell;
	}

	/**
	 * 获取条码
	 * @return 条码
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * 设置条码
	 * @param barcode 条码
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * 获取单位
	 * @return 单位
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * 设置单位
	 * @param unitName 单位
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public BigDecimal getInPrice() {
		return inPrice;
	}

	/**
	 * 设置参考进价
	 * @param inPrice 设置参考进价
	 */
	public void setInPrice(BigDecimal inPrice) {
		this.inPrice = inPrice;
	}

	public BigDecimal getOutPrice() {
		return outPrice;
	}

	/**
	 * 设置参考售价
	 * @param outPrice 设置参考售价
	 */
	public void setOutPrice(BigDecimal outPrice) {
		this.outPrice = outPrice;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	/** 获取缩略图 */
	@JsonProperty
	public String getThumbnail() {
		if (getProductImages() != null && !getProductImages().isEmpty()) {
			return getProductImages().get(0).getThumbnail();
		}
		return null;
	}

	/** 获取缩略图 */
	@JsonProperty
	public String getMedium() {
		if (getProductImages() != null && !getProductImages().isEmpty()) {
			return getProductImages().get(0).getMedium();
		}
		return null;
	}

	/** 获取缩略图 */
	@JsonProperty
	public String getLarge() {
		if (getProductImages() != null && !getProductImages().isEmpty()) {
			return getProductImages().get(0).getLarge();
		}
		return null;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDescriptionapp() {
		return descriptionapp;
	}

	public void setDescriptionapp(String descriptionapp) {
		this.descriptionapp = descriptionapp;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}