/**
 *====================================================
 * 文件名称: Ad.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月11日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: Ad
 * @Description: 广告内容
 * @author Administrator
 * @date 2014年10月11日 下午6:01:07
 */
@Entity
@Table(name = "xx_ad")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_ad_sequence")
public class Ad extends OrderEntity {

	private static final long serialVersionUID = -1307743303786909390L;

	/** url类型 */
	public enum LinkType {
		/** 定义 */
		none,
		/** 商品 */
		product,
		/** 商家 */
		tenant,
		/** 新品 */
		news,
		/** 特卖 */
		special,
		/**商圈*/
		community,
		/** 联盟活动*/
		unionActivity
	}

	/** 类型 */
	public enum Type {
		/** 文本 */
		text,
		/** 图片 */
		image,
		/** flash */
		flash
	}

	/** 标题 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String title;

	/** 类型 */
	@NotNull
	@Column(nullable = false)
	private Type type;
	
	/** 链接类型 */
	@NotNull
	@Column(nullable = false)
	private LinkType linkType;

	/** 内容 */
	@Lob
	private String content;

	/** 路径 */
	@Length(max = 200)
	private String path;

	/** 开始时间 */
	private Date beginDate;

	/** 结束时间 */
	private Date endDate;

	/** 连接地址 */
	@Length(max = 200)
	private String url;
	
	/** 链接 id */
	private Long linkId;

	/** 广告位 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private AdPosition adPosition;

	/** 所属企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 地区 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;

	/** 频道信息 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_ad_product_channel")
	private Set<ProductChannel> productChannels = new HashSet<ProductChannel>();

	/** 是否开始 */
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	/** 是否已经结束 */
	public boolean hasEnded() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 标题
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 标题
	 * @param title 标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 类型
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 类型
	 * @param type 类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 内容
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 内容
	 * @param content 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 路径
	 * @return 路径
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 路径
	 * @param path 路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 开始时间
	 * @return 开始时间
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 开始时间
	 * @param beginDate 开始时间
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 结束时间
	 * @return 结束时间
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 结束时间
	 * @param endDate 结束时间
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 连接地址
	 * @return 连接地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 连接地址
	 * @param url 连接地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 广告位
	 * @return 广告位
	 */
	public AdPosition getAdPosition() {
		return adPosition;
	}

	/**
	 * 广告位
	 * @param adPosition 广告位
	 */
	public void setAdPosition(AdPosition adPosition) {
		this.adPosition = adPosition;
	}

	/**
	 * 所属企业
	 * @return 企业
	 */
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * 设置企业
	 * @param tenant 企业
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}	
		
	public LinkType getLinkType() {
		return linkType;
	}

	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}
	
	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Set<ProductChannel> getProductChannels() {
		return productChannels;
	}

	public void setProductChannels(Set<ProductChannel> productChannels) {
		this.productChannels = productChannels;
	}

}