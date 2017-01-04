/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: Brand
 * @Description: 品牌
 * @author Administrator
 * @date 2014年10月14日 上午9:09:06
 */
@Entity
@Table(name = "xx_brand")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_brand_sequence")
public class Brand extends OrderEntity {

	private static final long serialVersionUID = -6109590619136943215L;

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/brand/content";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 类型 */
	public enum Type {
		/** 文本 */
		text,
		/** 图片 */
		image
	}

	/** 名称 */
	@NotEmpty
	@JsonProperty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 拼音 */
	private String phonetic;
	
	/** 类型 */
	@NotNull
	@Column(nullable = false)
	private Type type;

	/** logo */
	@Length(max = 200)
	private String logo;

	/** 网址 */
	@Length(max = 200)
	private String url;

	/** 介绍 */
	@Lob
	private String introduction;

	/** 标签 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_brand_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();
	
	/** 商品 */
	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
	private Set<Product> products = new HashSet<Product>();

	/** 商品分类 */
	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();

	/** 促销 */
	@ManyToMany(mappedBy = "brands", fetch = FetchType.LAZY)
	private Set<Promotion> promotions = new HashSet<Promotion>();

	/** 促销 */
	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
	private Set<BrandSeries> brandSeries = new HashSet<BrandSeries>();
	
	/** 获取访问路径 */
	public String getPath() {
		if (getId() != null) {
			return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
		}
		return null;
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Product> products = getProducts();
		if (products != null) {
			for (Product product : products) {
				product.setBrand(null);
			}
		}
		Set<ProductCategory> productCategories = getProductCategories();
		if (productCategories != null) {
			for (ProductCategory productCategory : productCategories) {
				productCategory.getBrands().remove(this);
			}
		}
		Set<Promotion> promotions = getPromotions();
		if (promotions != null) {
			for (Promotion promotion : promotions) {
				promotion.getBrands().remove(this);
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

	/**
	 * 获取类型
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * @param type 类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取logo
	 * @return logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置logo
	 * @param logo logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * 获取网址
	 * @return 网址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置网址
	 * @param url 网址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取介绍
	 * @return 介绍
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * 设置介绍
	 * @param introduction 介绍
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * 获取商品
	 * @return 商品
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 设置商品
	 * @param products 商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	/**
	 * 获取商品分类
	 * @return 商品分类
	 */
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	/**
	 * 设置商品分类
	 * @param productCategories 商品分类
	 */
	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	/**
	 * 获取促销
	 * @return 促销
	 */
	public Set<Promotion> getPromotions() {
		return promotions;
	}

	/**
	 * 设置促销
	 * @param promotions 促销
	 */
	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	public Set<BrandSeries> getBrandSeries() {
		return brandSeries;
	}

	public void setBrandSeries(Set<BrandSeries> brandSeries) {
		this.brandSeries = brandSeries;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@Transient
	public static Boolean at(List<Brand> brands,char c) {
		for (Brand brand:brands) {
		   if (brand.getPhonetic()!=null && brand.getPhonetic().charAt(0)==c) {
			  return true;
		   }
		}
		return false;
	}
	
	@Transient
	public String getPhoneticChar() {
		 if (getPhonetic()!=null) {
			 return getPhonetic().substring(0,1);
		 } else {
			 return "";
		 }
	}

	@Transient
	public BrandSeries getSeries() {
		for (BrandSeries brandSeries:getBrandSeries()) {
			   if (brandSeries.getGrade().equals(0)) {
				  return brandSeries;
			   }
			}
		return null;
	}
	
}