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
 * @ClassName: TenantCategory
 * @Description: 商家分类
 * @author Administrator
 * @date 2014年10月14日 上午9:12:04
 */
@Entity
@Table(name = "xx_tenant_category")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_tenant_category_sequence")
public class TenantCategory extends OrderEntity {

	private static final long serialVersionUID = 5095521437302782717L;

	/** 树路径分隔符 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/tenant/list";

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
	private TenantCategory parent;

	/** 下级分类 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("order asc")
	private Set<TenantCategory> children = new HashSet<TenantCategory>();

	/** 分析 */
	@OneToMany(mappedBy = "tenantCategory", fetch = FetchType.LAZY)
	private Set<Tenant> tenants = new HashSet<Tenant>();

	/** 频道 */
	@ManyToMany(mappedBy = "tenantCategorys", fetch = FetchType.LAZY)
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

	/** 获取树路径-频道 */
	public Object getTreeByChannelPaths(ProductChannel productChannel) {
		List<Long> treePaths = new ArrayList<Long>();
		String[] ids = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
		if (ids != null) {
			for (String id : ids) {
				if (productChannel != null) {
					for (TenantCategory tenantCategory : productChannel.getTenantCategorys()) {
						if (tenantCategory.getId().toString().equals(id)) {
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

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<ProductChannel> productChannels = getProductChannels();
		if (productChannels != null) {
			for (ProductChannel p : productChannels) {
				p.getTenantCategorys().remove(this);
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
	public TenantCategory getParent() {
		return parent;
	}

	/**
	 * 设置上级分类
	 * @param parent 上级分类
	 */
	public void setParent(TenantCategory parent) {
		this.parent = parent;
	}

	/**
	 * 获取下级分类
	 * @return 下级分类
	 */
	public Set<TenantCategory> getChildren() {
		return children;
	}

	/**
	 * 设置下级分类
	 * @param children 下级分类
	 */
	public void setChildren(Set<TenantCategory> children) {
		this.children = children;
	}

	/**
	 * 获取分析
	 * @return 分析
	 */
	public Set<Tenant> getTenants() {
		return tenants;
	}

	/**
	 * 设置分析
	 * @param products 分析
	 */
	public void setTenants(Set<Tenant> tenants) {
		this.tenants = tenants;
	}

	public Set<ProductChannel> getProductChannels() {
		return productChannels;
	}

	public void setProductChannels(Set<ProductChannel> productChannels) {
		this.productChannels = productChannels;
	}

}