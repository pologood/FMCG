package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Similarity;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import net.wit.constant.SettingConstant;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Promotion.Type;

/**
 * @ClassName: Tenant
 * @Description: 企业实体类
 * @author Administrator
 * @date 2014年8月25日 上午9:42:25
 */
@Indexed
@Similarity(impl = IKSimilarity.class)
@Entity
@Table(name = "xx_tenant")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_tenant_sequence")
public class Tenant extends BaseEntity {


	private static final long serialVersionUID = 1575723192791568482L;
	
	/** 点击数缓存名称 */
	public static final String HITS_CACHE_NAME = "tenantHits";

	/** 点击数缓存更新间隔时间 */
	public static final int HITS_CACHE_INTERVAL = 600000;

	/** 企业类型 */
	public enum TenantType {
		/** 供应商 */
		suppier,
		/** 经销商 */
		tenant,
		/** 零售商 */
		retailer
	}

	/** 店铺状态 */
	public enum Status {
		/** 待审核 */
		none,
		/** 已审核 */
		confirm,
		/** 已开通 */
		success,
		/** 已关闭 */
		fail
	}

	/** 排序类型 */
	public enum OrderType {
		/** 默认排序 */
		weight,
		/** 点击降序 */
		hitsDesc,
		/** 评分降序 */
		scoreDesc,
		/** 日期降序 */
		dateDesc,
		/** 距离优先 */
		distance
	}


	/** 企业编码 */
	@Expose
	@JsonProperty
	@Column(nullable = false, length = 255)
	private String code;

	/** 企业名称 */
	@Expose
	@JsonProperty
	@Column(nullable = false, length = 255)
	private String name;

	/** 店铺昵称 */
	@Expose
	@JsonProperty
	private String shortName;

	/** 企业类型 */
	@Expose
	private TenantType tenantType;

	/** 经营许可证 */
	@Expose
	@Column(length = 255)
	private String licenseCode;

	/** 拼音码 */
	@Column(length = 255)
	private String spell;

	/** 法人代表 */
	@Expose
	@JsonProperty
	@Column(length = 255)
	private String legalRepr;

	/** 联系人 */
	@Expose
	@Column(length = 255)
	private String linkman;

	/** 联系电话 */
	@Expose
	@JsonProperty
	@Column(length = 255)
	private String telephone;

	/** 传真 */
	@Expose
	@JsonProperty
	@Column(length = 255)
	private String faxes;

	/** 主页 */
	@Expose
	@Column(length = 255)
	private String homepage;

	/** 地址 */
	@Expose
	@JsonProperty
	@Column(length = 255)
	private String address;

	/** 邮编 */
	@Expose
	@Column(length = 255)
	private String zipcode;

	/** 企业QQ */
	@Expose
	@JsonProperty
	@Column(length = 20)
	private String qq;

	/** 经营范围 */
	@Column(length = 255)
	private String scopeOfBusiness;

	/** 绑定域名 */
	@Column(length = 255)
	private String domain;

	/** 销售范围 */
	@Column(length = 255)
	private String rangeInfo;

	/** 营业时间 */
	@Column(length = 255)
	private String hours;

	/** 积分 */
	private Long priority;
	
	/** 赠送积分 */
	@Min(0)
	private Long point;
	
	/** 状态 */
	@Expose
	@JsonProperty
	private Status status;
	
	/** 店铺认证 */
	@JsonProperty
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("authenType asc")
	private Set<Authen> authen = new HashSet<Authen>();

	@Embedded
	private Freight freight;
	
	/** 货到付款 */
	@Column(nullable = false)
	private Boolean toPay;
	
	/** 担保交易 */
	@Column(nullable = false)
	private Boolean tamPo;
	
	/** 七天退货 */
	@Column(nullable = false)
	private Boolean noReason;

	/** 未结算货款 */
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	private BigDecimal balance;

	/** 冻结货款 */
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 27, scale = 12)
	private BigDecimal freezeBalance;
	
	/** 店主 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member member;

	/** 搜索关键词 */
	@Length(max = 200)
	private String keyword;

	/** 页面标题 */
	@Length(max = 200)
	private String seoTitle;

	/** 页面关键词 */
	@Length(max = 200)
	private String seoKeywords;

	/** 页面描述 */
	@Length(max = 200)
	private String seoDescription;

	/** 佣金比率 */
	@Column(nullable = true, precision = 12, scale = 6)
	private BigDecimal brokerage;

	/** 联盟佣金 */
	@Column(nullable = true, precision = 12, scale = 6)
	private BigDecimal agency;
	
	/**  推广佣金 */
	@Column(nullable = true, precision = 12, scale = 6)
	private BigDecimal generalize;
	
	/** 商家模版 */
	@Column(nullable = true, length = 200)
	private String template;

	/** 平均评分 */
	@Expose
	@JsonProperty
	@Column(nullable = false, precision = 12, scale = 6)
	private Float score;

	/** 总评分 */
	@Expose
	@Column(nullable = false)
	private Long totalScore;

	/** 总配送评分 */
	@Expose
	@JsonProperty
	@Column(nullable = false, columnDefinition = "integer default 0")
	private Long totalAssistant;

	/** 评分数 */
	@Column(nullable = false)
	private Long scoreCount;

	/** 点击数 */
	@Column(nullable = false)
	private Long hits;

	/** 周点击数 */
	@Column(nullable = false)
	private Long weekHits;

	/** 月点击数 */
	@Column(nullable = false)
	private Long monthHits;
	
	/** 销售额 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private BigDecimal sales;

	/** 周销售额 */
	@Column(nullable = false)
	private BigDecimal weekSales;

	/** 月销售额 */
	@Column(nullable = false)
	private BigDecimal monthSales;
	
	/** 周点击数更新日期 */
	@Column(nullable = false)
	private Date weekHitsDate;

	/** 月点击数更新日期 */
	@Column(nullable = false)
	private Date monthHitsDate;

	/** 周销量更新日期 */
//	@Column(nullable = false)
	private Date weekSalesDate;

	/** 月销量更新日期 */
//	@Column(nullable = false)
	private Date monthSalesDate;

	/** 商家摄像头标识列表 */
	@ElementCollection
	@CollectionTable(name = "xx_tenant_video")
	private List<String> videos = new ArrayList<String>();

	/** 商家微信公众号 */
	@Embedded
	private TenantWechat tenantWechat;

	/** 是否为商盟成员 */
	@NotNull
	@Column(nullable = false)
	private Boolean isUnion;

	/** 是否开通wifi */
	@NotNull
	@Column(nullable = false)
	private Boolean isWifi;

	/** 是否开通云看店 */
	@NotNull
	@Column(nullable = false)
	private Boolean isCloudTenant;

	/** 是否开通购物屏 */
	@NotNull
	@Column(nullable = false)
	private Boolean isEquipment;

	/** 是否自营 */
	@NotNull
	@Column(nullable = false)
	private Boolean isSelf;

	/** 所属商盟 一个商只能加入一个商盟 */
	@ManyToOne(fetch = FetchType.LAZY,optional=true)
	private Union unions;
	
	/** logo */
	@Expose
	@JsonProperty
	@Length(max = 200)
	private String logo;

	/** 缩影图 */
	@Expose
	@JsonProperty
	@Length(max = 255)
	private String thumbnail;

	/** 介绍 */
	@Expose
	@JsonProperty
	@Lob
	private String introduction;

	/** 厂家授权 */
	@Expose
	@JsonProperty
	@Lob
	private String authorization;

	/** 企业分类 */
	@JsonProperty
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private TenantCategory tenantCategory;

	/** 服务主机 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Host host;

	/** 软件类型 */
	@Embedded
	private Software software;

//	/** 支付方式 */
//	private PaymentMethod paymentMethod;

	/** 商户号 */
	private String partner;

	/** 支付密钥 */
	private String paymentKey;

	/** 区域 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;

	/** 营业执照 **/
	private String licensePhoto;

	/** 标签 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_tenant_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();


	/** 关注会员 */
	@ManyToMany(mappedBy = "favoriteTenants", fetch = FetchType.LAZY)
	private Set<Member> favoriteMembers = new HashSet<Member>();

	/** 文章 */
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Article> articles = new HashSet<Article>();
	
	/** 商品 */
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Product> products = new HashSet<Product>();
	
	/** 商品套餐 */
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ProductGroup> productGroups = new HashSet<ProductGroup>();

	/** 商家促销方案 */
	@Expose
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Promotion> promotions = new HashSet<Promotion>();

	/** 包邮促销方案 */
	@Expose
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY)
	@Where(clause="type=3")
	private Set<Promotion> mailPromotions = new HashSet<Promotion>();
	
	/** 我的上级 */
	@Expose(serialize = false)
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<TenantRelation> relations = new HashSet<TenantRelation>();
	
	/** 商家商品分类方案 */
	@JsonProperty
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<DeliveryCenter> deliveryCenters = new HashSet<DeliveryCenter>();
	
	/** 发货地址 */
	@JsonProperty
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ProductCategoryTenant> productCategoryTenants = new HashSet<ProductCategoryTenant>();

	/** 商家商品分类方案 */
	@JsonProperty
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Member> members = new HashSet<Member>();

	/** 展示图片 */
	@JsonProperty
	@ElementCollection
	@CollectionTable(name = "xx_tenant_product_image")
	private List<ProductImage> tenantImages = new ArrayList<ProductImage>();

	/** 商家方案 */
	@JsonProperty
	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Coupon> coupons = new HashSet<Coupon>();

	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Qrcode> qrcodes = new ArrayList<>();
	
	/** 设置搜索关键词 */
	public void setKeyword(String keyword) {
		if (keyword != null) {
			keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.keyword = keyword;
	}

	/** 设置页面关键词 */
	public void setSeoKeywords(String seoKeywords) {
		if (seoKeywords != null) {
			seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.seoKeywords = seoKeywords;
	}

	/** 获取默认发货地址 */
	public DeliveryCenter getDefaultDeliveryCenter() {
		DeliveryCenter delivery = null;
		for (DeliveryCenter deliveryCenter : getDeliveryCenters()) {
			if (delivery==null) {
				delivery = deliveryCenter;
			}
			if (deliveryCenter.getIsDefault()) {
				return deliveryCenter;
			}
		}
		return delivery;
	}

	/** 诚信积分等级换算 */
	@JsonProperty
	@Transient
	public int[] getCreditLevel() {
		int[] group = new int[] { 0, 0, 0 };
		Long score = getTotalScore();
		if (score == null || score <= 0) {
			return group;
		}
		BigDecimal big = new BigDecimal(score).subtract(new BigDecimal(Math.max(0, -score + 2)));
		int level = new BigDecimal(Math.sqrt(big.add(SettingConstant.scoreParams20).divide(SettingConstant.scoreParams5).doubleValue())).subtract(SettingConstant.scoreParams1).intValue();
		if (level >= 125) {
			group[0] = 5;
			return group;
		}
		group[0] = level / 25;
		group[1] = level % 25;
		if (group[1] / 5 >= 1) {
			group[2] = group[1] % 5;
			group[1] = group[1] / 5;
		} else {
			group[2] = group[1];
			group[1] = 0;
		}
		return group;
	}

	public Tenant() {
		super();
	}

	public Tenant(long l) {
		super();
		setId(l);
	}

	public BigDecimal getAgency() {
		if (isUnion==null||!isUnion) {
			return BigDecimal.ZERO;
		}
		return agency;
	}

	public void setAgency(BigDecimal agency) {
		this.agency = agency;
	}

	// ===========================================getter/setter===========================================//

	
	/**
	 * 获取营业执照
	 * @return
	 */
	public String getLicensePhoto() {
		return licensePhoto;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * 设置营业执照
	 * @param licensePhoto
	 */
	public void setLicensePhoto(String licensePhoto) {
		this.licensePhoto = licensePhoto;
	}

	/** 商家微信公众号 */
	public TenantWechat getTenantWechat() {
		return tenantWechat;
	}

	/** 商家微信公众号 */
	public void setTenantWechat(TenantWechat tenantWechat) {
		this.tenantWechat = tenantWechat;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取企业分类
	 * @return 企业分类
	 */
	public TenantCategory getTenantCategory() {
		return tenantCategory;
	}

	/**
	 * 设置企业分类
	 * @param tenantCategory 企业分类
	 */
	public void setTenantCategory(TenantCategory tenantCategory) {
		this.tenantCategory = tenantCategory;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public TenantType getTenantType() {
		return tenantType;
	}

	public void setTenantType(TenantType tenantType) {
		this.tenantType = tenantType;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	public Set<Authen> getAuthen() {
		return authen;
	}

	public void setAuthen(Set<Authen> authen) {
		this.authen = authen;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String getLegalRepr() {
		return legalRepr;
	}

	public void setLegalRepr(String legalRepr) {
		this.legalRepr = legalRepr;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	@JsonProperty
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFaxes() {
		return faxes;
	}

	public void setFaxes(String faxes) {
		this.faxes = faxes;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	@JsonProperty
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Boolean getToPay() {
		return toPay;
	}

	public void setToPay(Boolean toPay) {
		this.toPay = toPay;
	}

	public Boolean getTamPo() {
		return tamPo;
	}

	public void setTamPo(Boolean tamPo) {
		this.tamPo = tamPo;
	}

	public Boolean getNoReason() {
		return noReason;
	}

	public void setNoReason(Boolean noReason) {
		this.noReason = noReason;
	}

	/**
	 * 获取logo
	 * @return logo
	 */
	@JsonProperty
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

	public String getScopeOfBusiness() {
		return scopeOfBusiness;
	}

	public void setScopeOfBusiness(String scopeOfBusiness) {
		this.scopeOfBusiness = scopeOfBusiness;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	// 获取地区
	public Area getArea() {
		return area;
	}

	// 设置地区
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * 所属店主
	 * @return 店主
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * 设置店主
	 * @param member 店主
	 */
	public void setMember(Member member) {
		this.member = member;
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


	@JsonProperty
	public Set<ProductCategoryTenant> getProductCategoryTenants() {
		return productCategoryTenants;
	}

	/**
	 * 设置商家商品分类
	 * @param productCategoryTenants 商家商品分类
	 */
	public void setProductCategoryTenants(Set<ProductCategoryTenant> productCategoryTenants) {
		this.productCategoryTenants = productCategoryTenants;
	}

	public Set<Promotion> getPromotions() {
		return promotions;
	}

	/**
	 * 设置促销方案
	 * @param promotions 促销方案
	 */
	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}

	/**
	 * 获取搜索关键词
	 * @return 搜索关键词
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * 获取关注会员
	 * @return 关注商家
	 */
	public Set<Member> getFavoriteMembers() {
		return favoriteMembers;
	}

	/**
	 * 设置关注商家
	 * @param favoriteMembers 关注商家
	 */
	public void setFavoriteMembers(Set<Member> favoriteMembers) {
		this.favoriteMembers = favoriteMembers;
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
	 * 佣金比率
	 * @return 佣金比率
	 */
	public BigDecimal getBrokerage() {
		if (this.brokerage==null) {
			return BigDecimal.ZERO;
		}
		return brokerage;
	}

	/**
	 * 佣金比率
	 * @param brokerage 佣金比率
	 */
	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	/**
	 * 商家模版
	 * @return 商家模版
	 */
	public String getTemplate() {
		return "default";
	}

	/**
	 * 商家模版
	 * @param template 商家模版
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * 获取评分
	 * @return 评分
	 */
	@JsonProperty
	public Float getScore() {
		return score;
	}

	/**
	 * 设置评分
	 * @param score 评分
	 */
	public void setScore(Float score) {
		this.score = score;
	}

	/**
	 * 获取总评分
	 * @return 总评分
	 */
	public Long getTotalScore() {
		return totalScore;
	}

	/**
	 * 设置总评分
	 * @param totalScore 总评分
	 */
	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * 获取总配送评分
	 * @return 总配送评分
	 */
	public Long getTotalAssistant() {
		return totalAssistant;
	}

	/**
	 * 设置总配送评分
	 * @param totalAssistant 总配送评分
	 */
	public void setTotalAssistant(Long totalAssistant) {
		this.totalAssistant = totalAssistant;
	}

	/**
	 * 获取评分数
	 * @return 评分数
	 */
	public Long getScoreCount() {
		return scoreCount;
	}

	/**
	 * 设置评分数
	 * @param scoreCount 评分数
	 */
	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}

	/**
	 * 获取点击数
	 * @return 点击数
	 */
	public Long getHits() {
		return hits;
	}

	/**
	 * 设置点击数
	 * @param hits 点击数
	 */
	public void setHits(Long hits) {
		this.hits = hits;
	}

	/** 商家摄像头标识列表 */
	public List<String> getVideos() {
		return videos;
	}

	/** 商家摄像头标识列表 */
	public void setVideos(List<String> videos) {
		this.videos = videos;
	}

	/**
	 * 获取周点击数
	 * @return 周点击数
	 */
	public Long getWeekHits() {
		return weekHits;
	}

	/**
	 * 设置周点击数
	 * @param weekHits 周点击数
	 */
	public void setWeekHits(Long weekHits) {
		this.weekHits = weekHits;
	}

	/**
	 * 获取月点击数
	 * @return 月点击数
	 */
	public Long getMonthHits() {
		return monthHits;
	}

	/**
	 * 设置月点击数
	 * @param monthHits 月点击数
	 */
	public void setMonthHits(Long monthHits) {
		this.monthHits = monthHits;
	}

	public double distatce(Location location) {
		double d=-1;
		if (location==null || !location.isExists()) {
			return d;
		}
		for (DeliveryCenter deliveryCenter:getDeliveryCenters()) {
			if (deliveryCenter.getLocation()!=null&&deliveryCenter.getLocation().getLat()!=null && deliveryCenter.getLocation().getLng()!=null) {
			   double md = deliveryCenter.distatce(location);
			   if (d==-1) {
				   d = md;
			   } else {
				   if (md<d) {
					   d = md;
				   }
			   }
			}
			
		}
		return d;
	}

	/**
	 * 获取最近发货地址
	 * @return 获取最近地址
	 */
	public DeliveryCenter nearDeliveryCenter(Location location) {
		double d=-1;
		if (location==null || !location.isExists()) {
			return  this.getDefaultDeliveryCenter();
		}
		DeliveryCenter near = null;
		for (DeliveryCenter deliveryCenter:getDeliveryCenters()) {
			if (deliveryCenter.getLocation()!=null&&deliveryCenter.getLocation().getLat()!=null && deliveryCenter.getLocation().getLng()!=null) {
			   double md = deliveryCenter.distatce(location);
			   if (d==-1) {
				   d = md;
				   near = deliveryCenter;
			   } else {
				   if (md<d) {
					   d = md;
					   near = deliveryCenter;
				   }
			   }
			}
			
		}
		return near;
	}
	
	/**
	 * 获取发货地址
	 * @return 发货地址
	 */
	public Set<DeliveryCenter> getDeliveryCenters() {
		return deliveryCenters;
	}

	/**
	 * 设置发货地址
	 * @param deliveryCenters 发货地址
	 */
	public void setDeliveryCenters(Set<DeliveryCenter> deliveryCenters) {
		this.deliveryCenters = deliveryCenters;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	/** 获取商家公告 */
	public Set<Article> getArticles() {
		return articles;
	}

	public List<ProductImage> getTenantImages() {
		return tenantImages;
	}

	public void setTenantImages(List<ProductImage> tenantImages) {
		this.tenantImages = tenantImages;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

//	public PaymentMethod getPaymentMethod() {
//		return paymentMethod;
//	}
//
//	public void setPaymentMethod(PaymentMethod paymentMethod) {
//		this.paymentMethod = paymentMethod;
//	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(String paymentKey) {
		this.paymentKey = paymentKey;
	}

	public Set<TenantRelation> getRelations() {
		return relations;
	}

	public void setRelations(Set<TenantRelation> relations) {
		this.relations = relations;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public Freight getFreight() {
		return freight;
	}

	public void setFreight(Freight freight) {
		this.freight = freight;
	}

	/**
	 * 获取促销购物车项
	 * @param parent 促销
	 * @return 促销购物车项
	 */
	public TenantRelation getRelation(Tenant parent) {
		if (getRelations() != null && (getRelations().size() > 0)) {
			for (TenantRelation relation : getRelations()) {
				if (relation.getParent().equals(parent) && relation.getStatus().equals(TenantRelation.Status.success)) {
					return relation;
				}
			}
		}
		return null;
	}

	/**
	 * 计算运费
	 * @param weight 重量
	 * @return 运费
	 */
	public BigDecimal calculateFreight(Integer weight, Integer amount) {
		if (freight.getFreightType().equals(Freight.Type.weight)) {
			if (weight==0) {
				weight = amount;
			}
			return freight.calculateFreight(weight);
		} else {
			return freight.calculateFreight(amount);
		}
	}

	public String getRangeInfo() {
		return rangeInfo;
	}

	public void setRangeInfo(String rangeInfo) {
		this.rangeInfo = rangeInfo;
	}
		
	public BigDecimal getGeneralize() {
//		if (this.generalize==null) {
//			return new BigDecimal("0.01");
//		} else {
//		   return generalize;
//		}
		return BigDecimal.ZERO;
	}

	public void setGeneralize(BigDecimal generalize) {
		this.generalize = generalize;
	}

	/** 权重优先级 */
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Column(nullable = true)
	public Long getPriority() {
		if (priority == null) {
			priority = 0L;
		}else{
			priority =0l;
		}
		if (logo != null) {
			priority = priority+1l;
		}
		if (name != null) {
			priority = priority+1l;
		}
		if (shortName != null) {
			priority = priority+1l;
		}
		if (licenseCode != null) {
			priority = priority+1l;
		}
		if (authen != null) {
			if(authen.size()==1){
				priority = priority+2l;
			}else if(authen.size()==2){
				priority = priority+4l;
			}else if(authen.size()==3){
				priority = priority+6l;
			}else if(authen.size()==4){
				priority = priority+8l;
			}else {
				priority = priority+10l;
			}
		}
		if (thumbnail != null) {
			priority = priority+2l;
		}
		if (scopeOfBusiness!=null){
			priority = priority+2l;
		}
		if (introduction != null) {
			priority = priority+2l;
		}
		if (licensePhoto != null) {
			priority = priority+2l;
		}
		if (qq != null) {
			priority = priority+2l;
		}
		if (legalRepr != null) {
			priority = priority+1l;
		}
		if (linkman != null) {
			priority = priority+2l;
		}
		if (telephone != null) {
			priority = priority+1l;
		}
		if (address != null) {
			priority = priority+2l;
		}
		if (zipcode != null) {
			priority = priority+1l;
		}
		if (domain != null) {
			priority = priority+1l;
		}
		if (status == Status.success) {
			priority = priority+1l;
		}
		if(keyword!=null){
			priority = priority+1l;
		}
		if(seoTitle!=null){
			priority = priority+1l;
		}
		if(seoKeywords!=null){
			priority = priority+1l;
		}
		if(seoDescription!=null){
			priority = priority+1l;
		}
		if(brokerage!=null){
			priority = priority+1l;
		}
		if(hits!=null){
			if(hits>800){
				priority = priority+3l;
			}else if(hits>500){
				priority = priority+2l;
			}else if(hits>300){
				priority = priority+1l;
			}
		}
		if(weekHits!=null){
			if(weekHits>50){
				priority = priority+3l;
			}else if(weekHits>30){
				priority = priority+2l;
			}else if(weekHits>20){
				priority = priority+1l;
			}
		}
		if(monthHits!=null){
			if(monthHits>200){
				priority = priority+4l;
			}else if(monthHits>120){
				priority = priority+3l;
			}else if(monthHits>80){
				priority = priority+2l;
			}else if(monthHits>50){
				priority = priority+1l;
			}
		}
		if(tenantWechat!=null){
			priority = priority+1l;
		}
		if(authorization!=null){
			priority = priority+1l;
		}
		if(area!=null){
			priority = priority+2l;
		}
		if(tags!=null){
			if(tags.size()==1){
				priority = priority+2l;
			}else if(tags.size()==2){
				priority = priority+4l;
			}else if(tags.size()==3){
				priority = priority+6l;
			}else if(tags.size()==4){
				priority = priority+8l;
			}else if(tags.size()==5){
				priority = priority+10l;
			}else if(tags.size()==6){
				priority = priority+12l;
			}else{
				priority = priority+14l;
			}
		}
		if(isUnion){
			priority = priority+1l;
		}
		if(favoriteMembers!=null){
			if(favoriteMembers.size()>50){
				priority = priority+7l;
			}else if(favoriteMembers.size()>35){
				priority = priority+5l;
			}else if(favoriteMembers.size()>20){
				priority = priority+3l;
			}else if(favoriteMembers.size()>10){
				priority = priority+1l;
			}
		}
		if(productCategoryTenants!=null){
			priority = priority+1l;
		}
		if(tenantImages!=null){
			priority = priority+2l;
		}
		if(products==null){
			priority=0l;
		}else{
			if(products.size()==0){
				priority=0l;
			}else if(products.size()==1){
				priority=1l;
			}else if(products.size()==2){
				priority=2l;
			}else if(products.size()==3){
				priority=3l;
			}else if(products.size()==4){
				priority=priority+1l;
			}else if(products.size()<=10){
				priority = priority+2l;
			}else{
				priority = priority+3l;
			}
		}
		return priority;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@Transient
	private String memberRank;

	@Transient
	public String getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(String memberRank) {
		this.memberRank = memberRank;
	}

	public BigDecimal getSales() {
		return sales;
	}

	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}

	public BigDecimal getWeekSales() {
		return weekSales;
	}

	public void setWeekSales(BigDecimal weekSales) {
		this.weekSales = weekSales;
	}

	public BigDecimal getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(BigDecimal monthSales) {
		this.monthSales = monthSales;
	}

	public Date getWeekHitsDate() {
		return weekHitsDate;
	}

	public void setWeekHitsDate(Date weekHitsDate) {
		this.weekHitsDate = weekHitsDate;
	}

	public Date getMonthHitsDate() {
		return monthHitsDate;
	}

	public void setMonthHitsDate(Date monthHitsDate) {
		this.monthHitsDate = monthHitsDate;
	}

	public Date getWeekSalesDate() {
		return weekSalesDate;
	}

	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}

	public Date getMonthSalesDate() {
		return monthSalesDate;
	}

	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
	}

	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

	public Set<ProductGroup> getProductGroups() {
		return productGroups;
	}

	public void setProductGroups(Set<ProductGroup> productGroups) {
		this.productGroups = productGroups;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	public Set<Promotion> getMailPromotions() {
		return mailPromotions;
	}

	public void setMailPromotions(Set<Promotion> mailPromotions) {
		this.mailPromotions = mailPromotions;
	}

	public Boolean getIsUnion() {
		return isUnion;
	}

	public void setIsUnion(Boolean isUnion) {
		this.isUnion = isUnion;
	}

	public Boolean getSelf() {
		return isSelf;
	}

	public void setSelf(Boolean self) {
		isSelf = self;
	}

	public BigDecimal getFreezeBalance() {
		return freezeBalance;
	}

	public void setFreezeBalance(BigDecimal freezeBalance) {
		this.freezeBalance = freezeBalance;
	}

	public List<Qrcode> getQrcodes() {
		return qrcodes;
	}

	public void setQrcodes(List<Qrcode> qrcodes) {
		this.qrcodes = qrcodes;
	}

	//获取有效活动
	public Set<Promotion> getVaildPromotions() {
		Set<Promotion> promotions = new HashSet<Promotion>();
		Boolean hasBuyfree = false;
		Boolean hasSeckill = false;
		Boolean hasDiscount = false;
		for (Promotion promotion: getPromotions()) {
		  if (promotion.hasBegun() && !promotion.hasEnded()) {
			  if (!promotion.getType().equals(Promotion.Type.buyfree) && !promotion.getType().equals(Promotion.Type.seckill)) {
			      promotions.add(promotion);			  
			  }
		      else
		      {
				  if (promotion.getType().equals(Promotion.Type.seckill) ) {
			    	  hasSeckill = true;
			      }
				  if (promotion.getType().equals(Promotion.Type.buyfree) ) {
			    	  hasBuyfree = true;
			      }
				  if (promotion.getType().equals(Type.discount) ) {
					  hasDiscount = true;
				  }
		      }
			  
		  }
		}
		if (hasBuyfree) {
			Promotion promotion = new Promotion();
			promotion.setType(Type.buyfree);
			promotion.setName("买赠活动");
			promotion.setTitle("买赠活动");
		}
		if (hasSeckill) {
			Promotion promotion = new Promotion();
			promotion.setType(Type.seckill);
			promotion.setName("限时折扣");
			promotion.setTitle("限时折扣");
		}
		if(hasDiscount){
			Promotion promotion = new Promotion();
			promotion.setType(Type.discount);
			promotion.setName("优惠买单");
			promotion.setTitle("优惠买单");
		}
		return promotions;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public Union getUnion() {
		return unions;
	}

	public void setUnion(Union unions) {
		this.unions = unions;
	}

	public Boolean getWifi() {
		return isWifi;
	}

	public void setWifi(Boolean wifi) {
		isWifi = wifi;
	}

	public Boolean getCloudTenant() {
		return isCloudTenant;
	}

	public void setCloudTenant(Boolean cloudTenant) {
		isCloudTenant = cloudTenant;
	}

	public Boolean getEquipment() {
		return isEquipment;
	}

	public void setEquipment(Boolean equipment) {
		isEquipment = equipment;
	}
}
