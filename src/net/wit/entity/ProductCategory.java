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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 商品分类
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_product_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_category_sequence")
public class ProductCategory extends OrderEntity {

	private static final long serialVersionUID = 5095521437302782717L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/product/list";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 名称 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 全称 */
	@Expose
	@JsonProperty
	@Column(nullable = false, length = 500)
	private String fullName;
	
	/** 图标 */
	@JsonProperty
	private String image;

	/** 页面标题 */
	@Length(max = 200)
	private String seoTitle;

	/** 页面关键词 */
	@Length(max = 200)
	private String seoKeywords;

	/** 页面描述 */
	@Length(max = 6000)
	private String seoDescription;

	/** 树路径 */
	@Column(nullable = false)
	private String treePath;

	/** 层级 */
	@Column(nullable = false)
	private Integer grade;

	/** 上级分类 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductCategory parent;

	/** 下级分类 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	private Set<ProductCategory> children = new HashSet<ProductCategory>();

	/** 商品 */
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	private Set<Product> products = new HashSet<Product>();

	/** 筛选品牌 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_category_brand")
	@OrderBy("order asc")
	private Set<Brand> brands = new HashSet<Brand>();

	/** 参数组 */
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	private Set<ParameterGroup> parameterGroups = new HashSet<ParameterGroup>();

	/** 筛选属性 */
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	private Set<Attribute> attributes = new HashSet<Attribute>();

	/** 促销 */
	@ManyToMany(mappedBy = "productCategories", fetch = FetchType.LAZY)
	private Set<Promotion> promotions = new HashSet<Promotion>();

	/** 标签 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_productCategory_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();

	/** 频道 */
	@ManyToMany(mappedBy = "productCategorys", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	private Set<ProductChannel> productChannels = new HashSet<ProductChannel>();

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
	public List<ProductCategory> childList= new ArrayList<ProductCategory>();
	
	public void setChildList(List<ProductCategory> childList){
		this.childList=childList;
	}
	@JsonProperty
	public List<ProductCategory> getChildList(){
		return this.childList;
	}
	
	/** 获取树路径-频道 */
	public List<Long> getTreeByChannelPaths(ProductChannel channel) {
		List<Long> treePaths = new ArrayList<Long>();
		String[] ids = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		if (ids != null) {
			for (String id : ids) {
				if (channel != null) {
					for (ProductCategory productCategory : channel.getProductCategorys()) {
						if (productCategory.getTreePaths().contains(Long.valueOf(id))) {
							treePaths.add(Long.valueOf(id));
							break;
						}
					}
				}
			}
		}
		return treePaths;
	}

	/** 获取访问路径 */
	public String getPath() {
		if (getId() != null) {
			return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
		}
		return null;
	}
	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		ProductCategory parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
			setTreePath(parent.getTreePath() + parent.getId() + TREE_PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(TREE_PATH_SEPARATOR);
		}
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		ProductCategory parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
		} else {
			setFullName(getName());
		}
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Promotion> promotions = getPromotions();
		if (promotions != null) {
			for (Promotion promotion : promotions) {
				promotion.getProductCategories().remove(this);
			}
		}
		Set<ProductChannel> productChannels = getProductChannels();
		if (productChannels != null) {
			for (ProductChannel p : productChannels) {
				p.getProductCategorys().remove(this);
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

	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 获取页面标题
	 * @return 页面标题
	 */

	public String getSeoTitle() {
		return seoTitle;
	}

	/**
	 * 设置页面标题
	 * @param seoTitle 页面标题
	 */
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	/**
	 * 获取页面关键词
	 * @return 页面关键词
	 */
	public String getSeoKeywords() {
		return seoKeywords;
	}

	/**
	 * 设置页面关键词
	 * @param seoKeywords 页面关键词
	 */
	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	/**
	 * 获取页面描述
	 * @return 页面描述
	 */
	public String getSeoDescription() {
		return seoDescription;
	}

	/**
	 * 设置页面描述
	 * @param seoDescription 页面描述
	 */
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	/**
	 * 获取树路径
	 * @return 树路径
	 */
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 * @param treePath 树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取层级
	 * @return 层级
	 */
	public Integer getGrade() {
		return grade;
	}

	/**
	 * 设置层级
	 * @param grade 层级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	/**
	 * 获取上级分类
	 * @return 上级分类
	 */
	public ProductCategory getParent() {
		return parent;
	}

	/**
	 * 设置上级分类
	 * @param parent 上级分类
	 */
	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级分类
	 * @return 下级分类
	 */
	public Set<ProductCategory> getChildren() {
		return children;
	}

	/**
	 * 设置下级分类
	 * @param children 下级分类
	 */
	public void setChildren(Set<ProductCategory> children) {
		this.children = children;
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
	 * 获取筛选品牌
	 * @return 筛选品牌
	 */
	public Set<Brand> getBrands() {
		return brands;
	}

	/**
	 * 设置筛选品牌
	 * @param brands 筛选品牌
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}

	/**
	 * 获取参数组
	 * @return 参数组
	 */
	public Set<ParameterGroup> getParameterGroups() {
		return parameterGroups;
	}

	/**
	 * 设置参数组
	 * @param parameterGroups 参数组
	 */
	public void setParameterGroups(Set<ParameterGroup> parameterGroups) {
		this.parameterGroups = parameterGroups;
	}

	/**
	 * 获取标签
	 * @return 标签
	 */
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * 设置标签
	 * @param tags 标签
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * 获取筛选属性
	 * @return 筛选属性
	 */
	public Set<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * 设置筛选属性
	 * @param attributes 筛选属性
	 */
	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
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

	public Set<ProductChannel> getProductChannels() {
		return productChannels;
	}

	public void setProductChannels(Set<ProductChannel> productChannels) {
		this.productChannels = productChannels;
	}

}