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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: Brand
 * @Description: 品牌
 * @author Administrator
 * @date 2014年10月14日 上午9:09:06
 */
@Entity
@Table(name = "xx_brand_series")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_brand_series_sequence")
public class BrandSeries extends OrderEntity {

	private static final long serialVersionUID = -6109592619136943239L;
	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 状态 */
	public enum Status {
		/** 正常   */
		normal,
		/** 停产 */
		stop
	}

	/** 名称 */
	@NotEmpty
	@JsonProperty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 全称 */
	@Expose
	@JsonProperty
	@Column(nullable = false, length = 500)
	private String fullName;

	/** 拼音 */
	private String phonetic;
	
	/** 类型 */
	@NotNull
	@Column(nullable = false)
	private Status status;

	/** 介绍 */
	@Lob
	private String introduction;

	/** 所属品牌 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Brand brand;

	/** 树路径 */
	@Column(nullable = false)
	private String treePath;

	/** 层级 */
	@Column(nullable = false)
	private Integer grade;

	/** 上级分类 */
	@ManyToOne(fetch = FetchType.LAZY)
	private BrandSeries parent;

	/** 下级分类 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	private Set<BrandSeries> children = new HashSet<BrandSeries>();
	
	/** 商品 */
	@ManyToMany(mappedBy = "brandSeries", fetch = FetchType.LAZY)
	private Set<Product> products = new HashSet<Product>();
	
	
	// ===========================================getter/setter===========================================//

	@Transient
	/** 获取树路径 */
	public List<Long> getTreePaths() {
		List<Long> treePaths = new ArrayList<Long>();
		String[] ids = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		if (ids != null) {
			for (String id : ids) {
				treePaths.add(Long.valueOf(id));
			}
		}
		return treePaths;
	}
	
	@Transient
	public List<BrandSeries> childList= new ArrayList<BrandSeries>();
	
	public void setChildList(List<BrandSeries> childList){
		this.childList=childList;
	}
	@JsonProperty
	public List<BrandSeries> getChildList(){
		return this.childList;
	}
	
	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		BrandSeries parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + "/" + getName());
			setTreePath(parent.getTreePath() + parent.getId() + TREE_PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(TREE_PATH_SEPARATOR);
		}
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		BrandSeries parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + "/" + getName());
		} else {
			setFullName(getName());
		}
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

	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public String getTreePath() {
		return treePath;
	}
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public BrandSeries getParent() {
		return parent;
	}
	public void setParent(BrandSeries parent) {
		this.parent = parent;
	}
	public Set<BrandSeries> getChildren() {
		return children;
	}
	public void setChildren(Set<BrandSeries> children) {
		this.children = children;
	}
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	
}