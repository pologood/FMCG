/**
 *====================================================
 * 文件名称: ProductChannel.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年6月13日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import net.wit.Template;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: ProductChannel
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年6月13日 上午8:49:10
 */
@Entity
@Table(name = "xx_product_channel")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_channel_sequence")
public class ProductChannel extends OrderEntity {

	private static final long serialVersionUID = 5095521437302782717L;

	/** 频道类型 */
	public enum Type {
		/** 购物频道 */
		product,
		/** 资讯频道 */
		article,
		/** 专家频道 */
		expert
	}

	/** 名称 */
	@JsonProperty
	@NotNull
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 频道类型 */
	@JsonProperty
	@Column
	private Type type;

	/** 图标 */
	@Expose
	@JsonProperty
	private String image;

	/** 描述 */
	@NotNull
	@Length(max = 6000)
	@Column(nullable = false, length = 6000)
	private String description;

	/** 链接地址 */
	@Length(max = 200)
	@Column(length = 200)
	private String url;

	/** 模版 */
	@NotNull
	@Column(nullable = false)
	private String templateId;

	/** 模版 */
	@Transient
	private Template template;

	/** 商品分类 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_channel_category")
	@OrderBy("order asc")
	private Set<ProductCategory> productCategorys = new HashSet<ProductCategory>();

	/** 专家分类 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_expert_channel_category")
	@OrderBy("order asc")
	private Set<ExpertCategory> expertCategorys = new HashSet<ExpertCategory>();
	
	/** 商家分类 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_channel_tenant")
	@OrderBy("order asc")
	private Set<TenantCategory> tenantCategorys = new HashSet<TenantCategory>();

	/** 文章分类 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_channel_article_category")
	@OrderBy("order asc")
	private Set<ArticleCategory> articleCategories = new HashSet<ArticleCategory>();

	/** 广告信息 */
	@ManyToMany(mappedBy = "productChannels", fetch = FetchType.LAZY)
	private Set<Ad> ads = new HashSet<Ad>();

	@OneToMany(mappedBy = "productChannel", fetch = FetchType.LAZY)
	private Set<AdPosition> adPositions = new HashSet<AdPosition>();

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Ad> ads = getAds();
		if (ads != null) {
			for (Ad ad : ads) {
				ad.getProductChannels().remove(this);
			}
		}
	}

	// ===========================================getter/setter===========================================//

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Set<ProductCategory> getProductCategorys() {
		return productCategorys;
	}

	public void setProductCategorys(Set<ProductCategory> productCategorys) {
		this.productCategorys = productCategorys;
	}

	
	public Set<TenantCategory> getTenantCategorys() {
		return tenantCategorys;
	}

	public void setTenantCategorys(Set<TenantCategory> tenantCategorys) {
		this.tenantCategorys = tenantCategorys;
	}

	public Set<ArticleCategory> getArticleCategories() {
		return articleCategories;
	}

	public void setArticleCategories(Set<ArticleCategory> articleCategories) {
		this.articleCategories = articleCategories;
	}
	public Set<ExpertCategory> getExpertCategorys() {
		return expertCategorys;
	}

	public void setExpertCategorys(Set<ExpertCategory> expertCategorys) {
		this.expertCategorys = expertCategorys;
	}

	public Set<Ad> getAds() {
		return ads;
	}

	public void setAds(Set<Ad> ads) {
		this.ads = ads;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Set<AdPosition> getAdPositions() {
		return adPositions;
	}

	public void setAdPositions(Set<AdPosition> adPositions) {
		this.adPositions = adPositions;
	}

	public AdPosition getDefaultAdPosition() {
		Iterator<AdPosition> iterator = getAdPositions().iterator();
		return iterator.next();
	}

}
