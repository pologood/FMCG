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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: FavoriteCategoryGroup
 * @Description: 收藏分类
 * @author Administrator
 * @date 2014年10月14日 上午9:12:04
 */
@Entity
@Table(name = "xx_favorite_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_favorite_category_sequence")
public class FavoriteCategory extends OrderEntity {

	private static final long serialVersionUID = 5095521457382782717L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/favorite_category/list";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 名称 */
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

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
	@Length(max = 200)
	private String seoDescription;

	/** 树路径 */
	@Column(nullable = false)
	private String treePath;

	/** 层级 */
	@Column(nullable = false)
	private Integer grade;

	/** 上级分类 */
	@ManyToOne(fetch = FetchType.LAZY)
	private FavoriteCategory parent;

	/** 下级分类 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	private Set<FavoriteCategory> children = new HashSet<FavoriteCategory>();

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false)
	private Member member;
	
	/** 分类下的商品 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_favorite_category_product")
	@OrderBy("createDate desc")
	private Set<Product> favoriteCategoryProducts = new HashSet<Product>();

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

	/** 获取访问路径 */
	public String getPath() {
		if (getId() != null) {
			return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
		}
		return null;
	}

	/** 删除前处理 */
//	@PreRemove
//	public void preRemove() {
//		Set<ProductChannel> productChannels = getProductChannels();
//		if (productChannels != null) {
//			for (ProductChannel p : productChannels) {
//				p.getFavoriteCategoryGroups().remove(this);
//			}
//		}
//	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 获取页面标题
	 * 
	 * @return 页面标题
	 */
	public String getSeoTitle() {
		return seoTitle;
	}

	/**
	 * 设置页面标题
	 * 
	 * @param seoTitle
	 *            页面标题
	 */
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	/**
	 * 获取页面关键词
	 * 
	 * @return 页面关键词
	 */
	public String getSeoKeywords() {
		return seoKeywords;
	}

	/**
	 * 设置页面关键词
	 * 
	 * @param seoKeywords
	 *            页面关键词
	 */
	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	/**
	 * 获取页面描述
	 * 
	 * @return 页面描述
	 */
	public String getSeoDescription() {
		return seoDescription;
	}

	/**
	 * 设置页面描述
	 * 
	 * @param seoDescription
	 *            页面描述
	 */
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	/**
	 * 获取树路径
	 * 
	 * @return 树路径
	 */
	public String getTreePath() {
		return treePath;
	}

	/**
	 * 设置树路径
	 * 
	 * @param treePath
	 *            树路径
	 */
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	/**
	 * 获取层级
	 * 
	 * @return 层级
	 */
	public Integer getGrade() {
		return grade;
	}

	/**
	 * 设置层级
	 * 
	 * @param grade
	 *            层级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	/**
	 * 获取上级分类
	 * 
	 * @return 上级分类
	 */
	public FavoriteCategory getParent() {
		return parent;
	}

	/**
	 * 设置上级分类
	 * 
	 * @param parent
	 *            上级分类
	 */
	public void setParent(FavoriteCategory parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级分类
	 * 
	 * @return 下级分类
	 */
	public Set<FavoriteCategory> getChildren() {
		return children;
	}

	/**
	 * 设置下级分类
	 * 
	 * @param children
	 *            下级分类
	 */
	public void setChildren(Set<FavoriteCategory> children) {
		this.children = children;
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
	

	public Set<Product> getFavoriteCategoryProducts() {
		return favoriteCategoryProducts;
	}

	public void setFavoriteCategoryProducts(Set<Product> favoriteCategoryProducts) {
		this.favoriteCategoryProducts = favoriteCategoryProducts;
	}


}