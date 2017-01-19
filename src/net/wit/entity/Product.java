/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
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
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.util.NumericUtils;
import org.dom4j.io.SAXReader;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Similarity;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.io.ClassPathResource;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import freemarker.template.TemplateException;
import net.wit.BigDecimalNumericFieldBridge;
import net.wit.CommonAttributes;
import net.wit.Setting;
import net.wit.util.FreemarkerUtils;
import net.wit.util.PhoneticZhCNUtil;
import net.wit.util.SettingUtils;

/**
 * Entity - 商品
 * @author rsico Team
 * @version 3.0
 */
@Indexed
@Similarity(impl = IKSimilarity.class)
@Entity
@Table(name = "xx_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_sequence")
public class Product extends OrderEntity {

	private static final long serialVersionUID = 2167830430439593293L;

	/** 点击数缓存名称 */
	public static final String HITS_CACHE_NAME = "productHits";

	/** 点击数缓存更新间隔时间 */
	public static final int HITS_CACHE_INTERVAL = 600000;

	/** 商品属性值属性个数 */
	public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;

	/** 商品属性值属性名称前缀 */
	public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

	/** 全称规格前缀 */
	public static final String FULL_NAME_SPECIFICATION_PREFIX = "[";

	/** 全称规格后缀 */
	public static final String FULL_NAME_SPECIFICATION_SUFFIX = "]";

	/** 全称规格分隔符 */
	public static final String FULL_NAME_SPECIFICATION_SEPARATOR = " ";

	/** 静态路径 */
	private static String staticPath;

	/** 排序类型 */
	public enum OrderType {
		/** 综合排序 */
		weight,
		/** 置顶降序 */
		topDesc,
		/** 价格升序 */
		priceAsc,
		/** 价格降序 */
		priceDesc,
		/** 销量降序 */
		salesDesc,
		/** 评分降序 */
		scoreDesc,
		/** 日期降序 */
		dateDesc,
		/** 人气降序 */
		hitsDesc
	}

	/** 编号 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 100)
	@Column(nullable = false, unique = true, length = 100)
	private String sn;

	/** 名称 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 全称 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Column(nullable = false)
	private String fullName;

	/** 拼音 */
	private String phonetic;

	/** 销售价 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal price;

	/** 批发价 */
	@Expose
	@JsonProperty
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal wholePrice;

	/** 成本价 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	private BigDecimal cost;

	/** 分润金额 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	private BigDecimal fee;

	/** 市场价 */
	@Expose
	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal marketPrice;

	/** 展示图片 */
	@Expose
	@JsonProperty
	@Length(max = 200)
	private String image;

	/** 单位 */
	@Expose
	@JsonProperty
	@Length(max = 200)
	private String unit;

	/** 重量 */
	@Min(0)
	private Integer weight;

	/** 库存 */
	@Expose
	@JsonProperty
	@Min(0)
	private Integer stock;

	/** 已分配库存 */
	@Column(nullable = false)
	private Integer allocatedStock;

	/** 库存备注 */
	@Length(max = 200)
	private String stockMemo;

	/** 规格 */
	@Length(max = 255)
	private String spec;
	
	/** 产地  */
	@Length(max = 255)
	private String madeIn;

	/** 赠送积分 */
	@Expose
	@JsonProperty
	@Min(0)
	@Column()
	private Long point;

	/** 是否上架 */
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	private Boolean isMarketable;
	
	/** 是否列出 */
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	private Boolean isList;

	/** 是否置顶 */
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	private Boolean isTop;

	/** 是否为赠品 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	private Boolean isGift;

	/** 介绍 */
	@Expose
	@JsonProperty
	@Lob
	private String introduction;

	/** APP介绍 */
	@Lob
	private String descriptionapp;

	/** 是否限购 */
	private Boolean isLimit;

	/** 限购数量 */
	private Long limitCounts;

	/** 特权购买标识 */
	private Boolean privilege;

	/** 备注 */
	@Length(max = 200)
	private String memo;

	/** 搜索关键词 */
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
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

	/** 评分 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false, precision = 12, scale = 6)
	private Float score;

	/** 总评分 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Long totalScore;

	/** 评分数 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Long scoreCount;

	/** 点击数 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Column(nullable = false)
	private Long hits;

	/** 周点击数 */
	@Column(nullable = false)
	private Long weekHits;

	/** 月点击数 */
	@Column(nullable = false)
	private Long monthHits;

	/** 权重优先级 */
	private Long priority;
	
	/** 销量 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	@Field(store = Store.YES, index = Index.NO)
	private Long sales;

	/** 周销量 */
	@Column(nullable = false)
	private Long weekSales;

	/** 月销量 */
	@Column(nullable = false)
	private Long monthSales;

	/** 周点击数更新日期 */
	@Column(nullable = false)
	private Date weekHitsDate;

	/** 月点击数更新日期 */
	@Column(nullable = false)
	private Date monthHitsDate;

	/** 周销量更新日期 */
	@Column(nullable = false)
	private Date weekSalesDate;

	/** 月销量更新日期 */
	@Column(nullable = false)
	private Date monthSalesDate;

	/** 商品条形码 */
	@Expose
	@Length(max = 50)
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	private String barcode;

	/** 原厂件号 */
	@Expose
	@Length(max = 50)
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	private String pn;
	
	/** 最小起订量 */
	@Column(columnDefinition = "int default 1")
	private Integer minReserve;

	/** 商品属性值0 */
	@Length(max = 200)
	private String attributeValue0;

	/** 商品属性值1 */
	@Length(max = 200)
	private String attributeValue1;

	/** 商品属性值2 */
	@Length(max = 200)
	private String attributeValue2;

	/** 商品属性值3 */
	@Length(max = 200)
	private String attributeValue3;

	/** 商品属性值4 */
	@Length(max = 200)
	private String attributeValue4;

	/** 商品属性值5 */
	@Length(max = 200)
	private String attributeValue5;

	/** 商品属性值6 */
	@Length(max = 200)
	private String attributeValue6;

	/** 商品属性值7 */
	@Length(max = 200)
	private String attributeValue7;

	/** 商品属性值8 */
	@Length(max = 200)
	private String attributeValue8;

	/** 商品属性值9 */
	@Length(max = 200)
	private String attributeValue9;

	/** 商品属性值10 */
	@Length(max = 200)
	private String attributeValue10;

	/** 商品属性值11 */
	@Length(max = 200)
	private String attributeValue11;

	/** 商品属性值12 */
	@Length(max = 200)
	private String attributeValue12;

	/** 商品属性值13 */
	@Length(max = 200)
	private String attributeValue13;

	/** 商品属性值14 */
	@Length(max = 200)
	private String attributeValue14;

	/** 商品属性值15 */
	@Length(max = 200)
	private String attributeValue15;

	/** 商品属性值16 */
	@Length(max = 200)
	private String attributeValue16;

	/** 商品属性值17 */
	@Length(max = 200)
	private String attributeValue17;

	/** 商品属性值18 */
	@Length(max = 200)
	private String attributeValue18;

	/** 商品属性值19 */
	@Length(max = 200)
	private String attributeValue19;

	/** 商品分类 */
	@Expose
	@JsonProperty
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private ProductCategory productCategory;
	
	/** 本店分类 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductCategoryTenant productCategoryTenant;

	/** 货品 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Goods goods;

	/** 品牌 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Brand brand;

	/** 品牌系列 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_series")
	@OrderBy("order asc")
	private Set<BrandSeries> brandSeries = new HashSet<BrandSeries>();
	
	/** 所属店铺 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;
	
	/** 供应商 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant supplier;
	
	/** 供应商-货号 */
	private String supplierProductSn;

	/** 商品图片 */
	@Expose
	@JsonProperty
	@Valid
	@ElementCollection
	@CollectionTable(name = "xx_product_product_image")
	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	/** 评论 */
	@Expose
	@JsonProperty
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Review> reviews = new HashSet<Review>();

	/** 咨询 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Consultation> consultations = new HashSet<Consultation>();

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ExtendCatalog> extendCatalogs = new HashSet<ExtendCatalog>();

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<SingleProduct> singleProducts = new HashSet<SingleProduct>();

	/** 标签 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();

	/** 收藏会员 */
	@ManyToMany(mappedBy = "favoriteProducts", fetch = FetchType.LAZY)
	private Set<Member> favoriteMembers = new HashSet<Member>();

	/** 设置优惠券 */
	@ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
	private Set<Coupon> coupons = new HashSet<Coupon>();

	/** 规格 */
	@Expose
	@JsonProperty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_specification")
	@OrderBy("order asc")
	private Set<Specification> specifications = new HashSet<Specification>();

	/** 规格值 */
	@Expose
	@JsonProperty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_product_specification_value")
	@OrderBy("specification asc")
	private Set<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();

	/** 促销商品 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<PromotionProduct> promotionProducts = new HashSet<PromotionProduct>();

	/** 购物车项 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<CartItem> cartItems = new HashSet<CartItem>();

	/** 订单项 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	
	/** 套餐商品项 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ProductItem> productItems = new HashSet<ProductItem>();

	/** 赠品项 */
	@OneToMany(mappedBy = "gift", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<GiftItem> giftItems = new HashSet<GiftItem>();

	/** 到货通知 */
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ProductNotify> productNotifies = new HashSet<ProductNotify>();

	/** 会员价 */
	 
	@Expose
	@JsonProperty
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "xx_product_member_price")
	@OrderBy("member_price_key asc")
	private Map<MemberRank, BigDecimal> memberPrice = new HashMap<MemberRank, BigDecimal>();

	/** 参数值 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "xx_product_parameter_value")
	private Map<Parameter, String> parameterValue = new HashMap<Parameter, String>();

	/** 商品包装单位 */
	@Expose
	@JsonProperty
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PackagUnit> packagUnits = new ArrayList<PackagUnit>();
//
//	/** 行业标签 */
//	@JsonProperty
//	@ManyToOne(fetch = FetchType.LAZY)
//	private Tag tag;

	/** 设置全称 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
		this.phonetic = PhoneticZhCNUtil.getZhCNFirstSpell(this.fullName);
	}

	/** 权重优先级 */
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Column(nullable = true)
	public Long getPriority() {
		if (priority == null) {
			priority = 0L;
		}else{
			priority = 0L;
		}
		if(unit!=null){
			priority = priority+1l;
		}
		if(memo!=null){
			priority = priority+1l;
		}
		if(stock!=null){
			priority = priority+1l;
		}
		if(madeIn!=null){
			priority = priority+1l;
		}
		if(introduction!=null){
			priority = priority+3l;
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
		if(score!=null){
			if(score<=1){
				priority = priority-5l;
			}else if(score<=2){
				priority = priority-3l;
			}else if(score<=3){
				priority=priority-1l;
			}else if(score<=4){
				priority=priority+1l;
			}else if(score<=4.5){
				priority=priority+3l;
			}else{
				priority=priority+5l;
			}
			if(priority<0){
				priority=0l;
			}
		}
		if(scoreCount!=null){
			if(scoreCount>40){
				priority=priority+5l;
			}else if(scoreCount>30){
				priority=priority+4l;
			}else if(scoreCount>20){
				priority=priority+3l;
			}else if(scoreCount>10){
				priority=priority+2l;
			}else if(scoreCount>5){
				priority=priority+1l;
			}
		}
		if(weekHits!=null){
			if(weekHits>150){
				priority=priority+4l;
			}else if(weekHits>100){
				priority=priority+3l;
			}else if(weekHits>60){
				priority=priority+2l;
			}else if(weekHits>30){
				priority=priority+1l;
			}
		}
		if(monthHits!=null){
			if(monthHits>650){
				priority=priority+4l;
			}else if(monthHits>500){
				priority=priority+3l;
			}else if(monthHits>350){
				priority=priority+2l;
			}else if(monthHits>200){
				priority=priority+1l;
			}
		}
		if(sales!=null){
			if(sales>55){
				priority=priority+4l;
			}else if(sales>40){
				priority=priority+3l;
			}else if(sales>25){
				priority=priority+2l;
			}else if(sales>10){
				priority=priority+1l;
			}
		}
		if(weekSales!=null){
			if(weekSales>25){
				priority=priority+3l;
			}else if(weekSales>15){
				priority=priority+2l;
			}else if(weekSales>7){
				priority=priority+1l;
			}
		}
		if(monthSales!=null){
			if(monthSales>70){
				priority=priority+3l;
			}else if(monthSales>50){
				priority=priority+2l;
			}else if(monthSales>25){
				priority=priority+1l;
			}
		}
		if(brand!=null){
			priority=priority+1l;
		}
		if(brandSeries!=null){
			priority=priority+1l;
		}
		if(reviews!=null){
			if(reviews.size()>50){
				priority=priority+4l;
			}else if(reviews.size()>30){
				priority=priority+3l;
			}else if(reviews.size()>15){
				priority=priority+2l;
			}else if(reviews.size()>5){
				priority=priority+1l;
			}
		}
		if(consultations!=null){
			if(consultations.size()>25){
				priority=priority+4l;
			}else if(consultations.size()>15){
				priority=priority+3l;
			}else if(consultations.size()>8){
				priority=priority+2l;
			}else if(consultations.size()>3){
				priority=priority+1l;
			}
		}
		if(favoriteMembers!=null){
			if(favoriteMembers.size()>50){
				priority=priority+5l;
			}else if(favoriteMembers.size()>40){
				priority=priority+4l;
			}else if(favoriteMembers.size()>30){
				priority=priority+3l;
			}else if(favoriteMembers.size()>20){
				priority=priority+2l;
			}else if(favoriteMembers.size()>10){
				priority=priority+1l;
			}
		}
		if(specifications!=null){
			priority=priority+1l;
		}
		if(image!=null){
			priority = priority+3l;
		}
		if(productImages==null){
			priority=0l;
		}else{
			if(productImages.size()<1){
				priority=0l;
			}else if(productImages.size()<2){
				priority=priority+2l;
			}else if(productImages.size()<3){
				priority=priority+4l;
			}else if(productImages.size()<4){
				priority=priority+6l;
			}else if(productImages.size()<5){
				priority=priority+8l;
			}else{
				priority=priority+10l;
			}
		}
		return priority;
	}

	/** 权重优先级 */
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public Long getTenantId() {
		if (getTenant() == null) {
			return 0L;
		}
		return getTenant().getId();
	}

	/**
	 * 设置搜索关键词
	 * @param keyword 搜索关键词
	 */
	public void setKeyword(String keyword) {
		if (keyword != null) {
			keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.keyword = keyword;
	}

	/**
	 * 设置页面关键词
	 * @param seoKeywords 页面关键词
	 */
	public void setSeoKeywords(String seoKeywords) {
		if (seoKeywords != null) {
			seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
		}
		this.seoKeywords = seoKeywords;
	}

	/**
	 * 获取促销
	 * @return 促销
	 */
	@Transient
	public Set<Promotion> getPromotions() {
		Set<Promotion> promotions = new HashSet<Promotion>(this.promotionProducts.size());
		for (PromotionProduct promotionProduct : this.getPromotionProducts()) {
			promotions.add(promotionProduct.getPromotion());
		}
		return promotions;
	}

	/** 获取商品属性值 */
	public String getAttributeValue(Attribute attribute) {
		if (attribute != null && attribute.getPropertyIndex() != null) {
			try {
				String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
				return (String) PropertyUtils.getProperty(this, propertyName);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/** 设置商品属性值 */
	public void setAttributeValue(Attribute attribute, String value) {
		if (attribute != null && attribute.getPropertyIndex() != null) {
			if (StringUtils.isEmpty(value)) {
				value = null;
			}
			if (value == null || (attribute.getOptions() != null && attribute.getOptions().contains(value))) {
				try {
					String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
					PropertyUtils.setProperty(this, propertyName, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取同货品商品
	 * @return 同货品商品，不包含自身
	 */
	@Transient
	public List<Product> getSiblings() {
		List<Product> siblings = new ArrayList<Product>();
		if (getGoods() != null && getGoods().getProducts() != null) {
			for (Product product : getGoods().getProducts()) {
//				for (Product product :getBrand().getProducts()){
					if (!this.equals(product)) {
						siblings.add(product);
//					}
				}

			}
		}
		return siblings;
	}

	/**
	 * 获取商品的规格及规格值
	 * @return 规格-规格值
	 */
	@JsonProperty
	@Transient
	public List<String> getSpecification_value() {
		List<String> specification_value = new ArrayList<String>();
		String tmp = "";
		for (SpecificationValue value : specificationValues) {
			tmp = value.getSpecification().getName() + ":" + value.getName();
			specification_value.add(tmp);
		}
		return specification_value;
	}

	/**
	 * 获取访问路径
	 * @return 访问路径
	 */
	@JsonProperty
	@Transient
	public String getPath() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", getId());
		model.put("createDate", getCreateDate());
		model.put("modifyDate", getModifyDate());
		model.put("sn", getSn());
		model.put("name", getName());
		model.put("fullName", getFullName());
		model.put("isMarketable", getIsMarketable());
		model.put("seoTitle", getSeoTitle());
		model.put("seoKeywords", getSeoKeywords());
		model.put("seoDescription", getSeoDescription());
		model.put("productCategory", getProductCategory());
		try {
			return FreemarkerUtils.process(staticPath, model);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
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

	/** 获取有效促销 */
	public Set<Promotion> getValidPromotions() {
		Set<Promotion> allPromotions = new HashSet<Promotion>();
		if (getPromotions() != null) {
			allPromotions.addAll(getPromotions());
		}
		Set<Promotion> validPromotions = new TreeSet<Promotion>();
		for (Promotion promotion : allPromotions) {
			if (promotion != null && promotion.hasBegun() && !promotion.hasEnded() ) {
				validPromotions.add(promotion);
			}
		}
		return validPromotions;
	}

	/** 获取可用库存 */
	public Integer getAvailableStock() {
		Integer availableStock = null;
		Integer stock = getStock();
		Integer allocatedStock = getAllocatedStock();
		if (stock != null && allocatedStock != null) {
			availableStock = stock - allocatedStock;
			if (availableStock < 0) {
				availableStock = 0;
			}
		}
		return availableStock;
	}

	/** 获取是否缺货 */
	public Boolean getIsOutOfStock() {
		return getStock() != null && getAllocatedStock() != null && getAllocatedStock() >= getStock();
	}

	/**
	 * 判断促销是否有效
	 * @param promotion 促销
	 * @return 促销是否有效
	 */
	public boolean isValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded() ) {
			return false;
		}
		if (getValidPromotions().contains(promotion)) {
			return true;
		}
		return false;
	}

	/** 最小起订量说明 */
	public String getMinReserveMemo() {
		String reserveMemo = getMinReserve() + getUnit();
		if (getPackagUnits() == null || getPackagUnits().size() <= 0 || getPackagUnits().isEmpty()) {
			return reserveMemo;
		}
		for (PackagUnit packag : getPackagUnits()) {
			if (getMinReserve() <= packag.getCoefficient().intValue()) {
				return reserveMemo;
			} else {
				if(packag.getCoefficient().intValue()!=0){
					int times= getMinReserve() / packag.getCoefficient().intValue();
					reserveMemo = times + packag.getName();
					int remainder = getMinReserve() % packag.getCoefficient().intValue();
					if (remainder > 0) {
						reserveMemo = reserveMemo + remainder + getUnit();
					}
				}
			}
		}
		return reserveMemo;
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Member> favoriteMembers = getFavoriteMembers();
		if (favoriteMembers != null) {
			for (Member favoriteMember : favoriteMembers) {
				favoriteMember.getFavoriteProducts().remove(this);
			}
		}
		//Set<Promotion> promotions = getPromotions();
		//if (promotions != null) {
		//	for (Promotion promotion : promotions) {
		//		promotion.getProducts().remove(this);
		//	}
		//}
		Set<OrderItem> orderItems = getOrderItems();
		if (orderItems != null) {
			for (OrderItem orderItem : orderItems) {
				orderItem.setProduct(null);
			}
		}
		Set<ProductItem> productItems = getProductItems();
		if (productItems != null) {
			for (ProductItem productItem : productItems) {
				productItem.setProduct(null);
			}
		}
		//Set<ProductCategoryTenant> categoryTenants = getProductCategoryTenants();
		//for (ProductCategoryTenant categoryTenant : categoryTenants) {
		//	categoryTenant.getProducts().remove(this);
		//}
	}

	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		if (getStock() == null) {
			setAllocatedStock(0);
		}
		setScore(0F);
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		if (getStock() == null) {
			setAllocatedStock(0);
		}
		if (getTotalScore() != null && getScoreCount() != null && getScoreCount() != 0) {
			setScore((float) getTotalScore() / getScoreCount());
		} else {
			setScore(0F);
		}
	}

	static {
		try {
			File witXmlFile = new ClassPathResource(CommonAttributes.WIT_XML_PATH).getFile();
			org.dom4j.Document document = new SAXReader().read(witXmlFile);
			org.dom4j.Element element = (org.dom4j.Element) document.selectSingleNode("/wit/template[@id='productContent']");
			staticPath = element.attributeValue("staticPath");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ===========================================getter/setter===========================================//

	public List<PackagUnit> getPackagUnits() {
		return packagUnits;
	}

	public void setPackagUnits(List<PackagUnit> packagUnits) {
		this.packagUnits = packagUnits;
	}
	
	
	/**
	 * 获取编号
	 * @return 编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置编号
	 * @param sn 编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取名称
	 * @return 名称
	 */
	@JsonProperty
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
	 * 获取全称
	 * @return 全称
	 */
	@JsonProperty
	public String getFullName() {
		return fullName;
	}

	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	/**
	 * 获取销售价
	 * @return 销售价
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置销售价
	 * @param price 销售价
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取批发价
	 * @return 批发价
	 */
	public BigDecimal getWholePrice() {
		return wholePrice;
	}

	/**
	 * 设置批发价
	 * @param wholePrice 批发价
	 */
	public void setWholePrice(BigDecimal wholePrice) {
		this.wholePrice = wholePrice;
	}

	/**
	 * 获取成本价
	 * @return 成本价
	 */
	public BigDecimal getCost() {
		if (cost==null) {
			return BigDecimal.ZERO;
		} else {
		    return cost;
		}
	}

	/**
	 * 设置成本价
	 * @param cost 成本价
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	/**
	 * 获取市场价
	 * @return 市场价
	 */
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	/**
	 * 设置市场价
	 * @param marketPrice 市场价
	 */
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	/**
	 * 获取展示图片
	 * @return 展示图片
	 */
	public String getImage() {
		return image;
	}

	/**
	 * 设置展示图片
	 * @param image 展示图片
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 获取单位
	 * @return 单位
	 */
	public String getUnit() {
		if (this.unit==null) {
			return "";
		}
		return unit;
	}

	/**
	 * 设置单位
	 * @param unit 单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * 获取重量
	 * @return 重量
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * 设置重量
	 * @param weight 重量
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * 获取库存
	 * @return 库存
	 */
	public Integer getStock() {
		return stock;
	}

	/**
	 * 设置库存
	 * @param stock 库存
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * 获取已分配库存
	 * @return 已分配库存
	 */
	public Integer getAllocatedStock() {
		return allocatedStock;
	}

	/**
	 * 设置已分配库存
	 * @param allocatedStock 已分配库存
	 */
	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	/**
	 * 获取库存备注
	 * @return 库存备注
	 */
	public String getStockMemo() {
		return stockMemo;
	}

	/**
	 * 设置库存备注
	 * @param stockMemo 库存备注
	 */
	public void setStockMemo(String stockMemo) {
		this.stockMemo = stockMemo;
	}

	/**
	 * 获取赠送积分
	 * @return 赠送积分
	 */
	public Long getPoint() {
		return point;
	}

	/**
	 * 设置赠送积分
	 * @param point 赠送积分
	 */
	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 获取是否上架
	 * @return 是否上架
	 */
	public Boolean getIsMarketable() {
		
		return isMarketable;
	}

	/**
	 * 设置是否上架
	 * @param isMarketable 是否上架
	 */
	public void setIsMarketable(Boolean isMarketable) {
	   this.isMarketable = isMarketable;
	}

	/**
	 * 获取是否列出
	 * @return 是否列出
	 */
	public Boolean getIsList() {
		return isList;
	}

	/**
	 * 设置是否列出
	 * @param isList 是否列出
	 */
	public void setIsList(Boolean isList) {
		this.isList = isList;
	}

	/**
	 * 获取是否置顶
	 * @return 是否置顶
	 */
	public Boolean getIsTop() {
		return isTop;
	}

	/**
	 * 设置是否置顶
	 * @param isTop 是否置顶
	 */
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}

	/**
	 * 获取是否为赠品
	 * @return 是否为赠品
	 */
	public Boolean getIsGift() {
		return isGift;
	}

	/**
	 * 设置是否为赠品
	 * @param isGift 是否为赠品
	 */
	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
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
	 * 获取介绍
	 * @return 介绍
	 */
	public String getDescriptionapp() {
		return descriptionapp;
	}

	/**
	 * 设置介绍
	 * @param descriptionapp 介绍
	 */
	public void setDescriptionapp(String descriptionapp) {
		this.descriptionapp = descriptionapp;
	}

	/**
	 * 获取备注
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取搜索关键词
	 * @return 搜索关键词
	 */
	public String getKeyword() {
		return keyword;
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
	 * 获取评分
	 * @return 评分
	 */
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

	/**
	 * 获取销量
	 * @return 销量
	 */
	public Long getSales() {
		return sales;
	}

	/**
	 * 设置销量
	 * @param sales 销量
	 */
	public void setSales(Long sales) {
		this.sales = sales;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	/**
	 * 获取周销量
	 * @return 周销量
	 */
	public Long getWeekSales() {
		return weekSales;
	}

	/**
	 * 设置周销量
	 * @param weekSales 周销量
	 */
	public void setWeekSales(Long weekSales) {
		this.weekSales = weekSales;
	}

	/**
	 * 获取月销量
	 * @return 月销量
	 */
	@JsonProperty
	public Long getMonthSales() {
		return monthSales;
	}

	/**
	 * 设置月销量
	 * @param monthSales 月销量
	 */
	public void setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
	}

	/**
	 * 获取周点击数更新日期
	 * @return 周点击数更新日期
	 */
	public Date getWeekHitsDate() {
		return weekHitsDate;
	}

	/**
	 * 设置周点击数更新日期
	 * @param weekHitsDate 周点击数更新日期
	 */
	public void setWeekHitsDate(Date weekHitsDate) {
		this.weekHitsDate = weekHitsDate;
	}

	/**
	 * 获取月点击数更新日期
	 * @return 月点击数更新日期
	 */
	public Date getMonthHitsDate() {
		return monthHitsDate;
	}

	/**
	 * 设置月点击数更新日期
	 * @param monthHitsDate 月点击数更新日期
	 */
	public void setMonthHitsDate(Date monthHitsDate) {
		this.monthHitsDate = monthHitsDate;
	}

	/**
	 * 获取周销量更新日期
	 * @return 周销量更新日期
	 */
	public Date getWeekSalesDate() {
		return weekSalesDate;
	}

	/**
	 * 设置周销量更新日期
	 * @param weekSalesDate 周销量更新日期
	 */
	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}

	/**
	 * 获取月销量更新日期
	 * @return 月销量更新日期
	 */
	public Date getMonthSalesDate() {
		return monthSalesDate;
	}

	/**
	 * 设置月销量更新日期
	 * @param monthSalesDate 月销量更新日期
	 */
	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
	}

	/**
	 * 获取商品条形码
	 * @return 商品条形码
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * 设置商品条形码
	 * @param barCode 商品条形码
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * 获取最小起订量
	 * @return 最小起订量
	 */
	public Integer getMinReserve() {
		if (minReserve == null) {
			return 1;
		}
		if (minReserve==0) {
			return 1;
		}
		return minReserve;
	}

	/**
	 * 设置最小起订量
	 * @param minReserve 最小起订量
	 */
	public void setMinReserve(Integer minReserve) {
		this.minReserve = minReserve;
	}

	/**
	 * 获取商品属性值0
	 * @return 商品属性值0
	 */
	public String getAttributeValue0() {
		return attributeValue0;
	}

	/**
	 * 设置商品属性值0
	 * @param attributeValue0 商品属性值0
	 */
	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	/**
	 * 获取商品属性值1
	 * @return 商品属性值1
	 */
	public String getAttributeValue1() {
		return attributeValue1;
	}

	/**
	 * 设置商品属性值1
	 * @param attributeValue1 商品属性值1
	 */
	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	/**
	 * 获取商品属性值2
	 * @return 商品属性值2
	 */
	public String getAttributeValue2() {
		return attributeValue2;
	}

	/**
	 * 设置商品属性值2
	 * @param attributeValue2 商品属性值2
	 */
	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}

	/**
	 * 获取商品属性值3
	 * @return 商品属性值3
	 */
	public String getAttributeValue3() {
		return attributeValue3;
	}

	/**
	 * 设置商品属性值3
	 * @param attributeValue3 商品属性值3
	 */
	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}

	/**
	 * 获取商品属性值4
	 * @return 商品属性值4
	 */
	public String getAttributeValue4() {
		return attributeValue4;
	}

	/**
	 * 设置商品属性值4
	 * @param attributeValue4 商品属性值4
	 */
	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}

	/**
	 * 获取商品属性值5
	 * @return 商品属性值5
	 */
	public String getAttributeValue5() {
		return attributeValue5;
	}

	/**
	 * 设置商品属性值5
	 * @param attributeValue5 商品属性值5
	 */
	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}

	/**
	 * 获取商品属性值6
	 * @return 商品属性值6
	 */
	public String getAttributeValue6() {
		return attributeValue6;
	}

	/**
	 * 设置商品属性值6
	 * @param attributeValue6 商品属性值6
	 */
	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}

	/**
	 * 获取商品属性值7
	 * @return 商品属性值7
	 */
	public String getAttributeValue7() {
		return attributeValue7;
	}

	/**
	 * 设置商品属性值7
	 * @param attributeValue7 商品属性值7
	 */
	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}

	/**
	 * 获取商品属性值8
	 * @return 商品属性值8
	 */
	public String getAttributeValue8() {
		return attributeValue8;
	}

	/**
	 * 设置商品属性值8
	 * @param attributeValue8 商品属性值8
	 */
	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}

	/**
	 * 获取商品属性值9
	 * @return 商品属性值9
	 */
	public String getAttributeValue9() {
		return attributeValue9;
	}

	/**
	 * 设置商品属性值9
	 * @param attributeValue9 商品属性值9
	 */
	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}

	/**
	 * 获取商品属性值10
	 * @return 商品属性值10
	 */
	public String getAttributeValue10() {
		return attributeValue10;
	}

	/**
	 * 设置商品属性值10
	 * @param attributeValue10 商品属性值10
	 */
	public void setAttributeValue10(String attributeValue10) {
		this.attributeValue10 = attributeValue10;
	}

	/**
	 * 获取商品属性值11
	 * @return 商品属性值11
	 */
	public String getAttributeValue11() {
		return attributeValue11;
	}

	/**
	 * 设置商品属性值11
	 * @param attributeValue11 商品属性值11
	 */
	public void setAttributeValue11(String attributeValue11) {
		this.attributeValue11 = attributeValue11;
	}

	/**
	 * 获取商品属性值12
	 * @return 商品属性值12
	 */
	public String getAttributeValue12() {
		return attributeValue12;
	}

	/**
	 * 设置商品属性值12
	 * @param attributeValue12 商品属性值12
	 */
	public void setAttributeValue12(String attributeValue12) {
		this.attributeValue12 = attributeValue12;
	}

	/**
	 * 获取商品属性值13
	 * @return 商品属性值13
	 */
	public String getAttributeValue13() {
		return attributeValue13;
	}

	/**
	 * 设置商品属性值13
	 * @param attributeValue13 商品属性值13
	 */
	public void setAttributeValue13(String attributeValue13) {
		this.attributeValue13 = attributeValue13;
	}

	/**
	 * 获取商品属性值14
	 * @return 商品属性值14
	 */
	public String getAttributeValue14() {
		return attributeValue14;
	}

	/**
	 * 设置商品属性值14
	 * @param attributeValue14 商品属性值14
	 */
	public void setAttributeValue14(String attributeValue14) {
		this.attributeValue14 = attributeValue14;
	}

	/**
	 * 获取商品属性值15
	 * @return 商品属性值15
	 */
	public String getAttributeValue15() {
		return attributeValue15;
	}

	/**
	 * 设置商品属性值15
	 * @param attributeValue15 商品属性值15
	 */
	public void setAttributeValue15(String attributeValue15) {
		this.attributeValue15 = attributeValue15;
	}

	/**
	 * 获取商品属性值16
	 * @return 商品属性值16
	 */
	public String getAttributeValue16() {
		return attributeValue16;
	}

	/**
	 * 设置商品属性值16
	 * @param attributeValue16 商品属性值16
	 */
	public void setAttributeValue16(String attributeValue16) {
		this.attributeValue16 = attributeValue16;
	}

	/**
	 * 获取商品属性值17
	 * @return 商品属性值17
	 */
	public String getAttributeValue17() {
		return attributeValue17;
	}

	/**
	 * 设置商品属性值17
	 * @param attributeValue17 商品属性值17
	 */
	public void setAttributeValue17(String attributeValue17) {
		this.attributeValue17 = attributeValue17;
	}

	/**
	 * 获取商品属性值18
	 * @return 商品属性值18
	 */
	public String getAttributeValue18() {
		return attributeValue18;
	}

	/**
	 * 设置商品属性值18
	 * @param attributeValue18 商品属性值18
	 */
	public void setAttributeValue18(String attributeValue18) {
		this.attributeValue18 = attributeValue18;
	}

	/**
	 * 获取商品属性值19
	 * @return 商品属性值19
	 */
	public String getAttributeValue19() {
		return attributeValue19;
	}

	/**
	 * 设置商品属性值19
	 * @param attributeValue19 商品属性值19
	 */
	public void setAttributeValue19(String attributeValue19) {
		this.attributeValue19 = attributeValue19;
	}

	/**
	 * 获取商品分类
	 * @return 商品分类
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * 设置商品分类
	 * @param productCategory 商品分类
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * 获取货品
	 * @return 货品
	 */
	public Goods getGoods() {
		return goods;
	}

	/**
	 * 设置货品
	 * @param goods 货品
	 */
	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	/**
	 * 获取品牌
	 * @return 品牌
	 */
	public Brand getBrand() {
		return brand;
	}

	/**
	 * 设置品牌
	 * @param brand 品牌
	 */
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	/**
	 * 获取店铺
	 * @return 店铺
	 */
	@JsonProperty
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * 设置店铺
	 * @param member 店铺
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	/**
	 * 获取商品商家中的分类
	 * @return 商品分类
	 */
	//public Set<ProductCategoryTenant> getProductCategoryTenants() {
	//	return productCategoryTenants;
	//}

	/**
	 * 设置商品商家中分类
	 * @param productCategory 商品分类
	 */
	//public void setProductCategoryTenants(Set<ProductCategoryTenant> productCategoryTenants) {
	//	this.productCategoryTenants = productCategoryTenants;
	//}

	
	/**
	 * 获取商品图片
	 * @return 商品图片
	 */
	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public ProductCategoryTenant getProductCategoryTenant() {
		return productCategoryTenant;
	}

	public void setProductCategoryTenant(ProductCategoryTenant productCategoryTenant) {
		this.productCategoryTenant = productCategoryTenant;
	}

	/**
	 * 设置商品图片
	 * @param productImages 商品图片
	 */
	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	/**
	 * 获取评论
	 * @return 评论
	 */
	public Set<Review> getReviews() {
		return reviews;
	}

	/**
	 * 设置评论
	 * @param reviews 评论
	 */
	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	/**
	 * 获取咨询
	 * @return 咨询
	 */
	public Set<Consultation> getConsultations() {
		return consultations;
	}

	/**
	 * 设置咨询
	 * @param consultations 咨询
	 */
	public void setConsultations(Set<Consultation> consultations) {
		this.consultations = consultations;
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
	 * 获取收藏会员
	 * @return 收藏会员
	 */
	public Set<Member> getFavoriteMembers() {
		return favoriteMembers;
	}

	/**
	 * 设置收藏会员
	 * @param favoriteMembers 收藏会员
	 */
	public void setFavoriteMembers(Set<Member> favoriteMembers) {
		this.favoriteMembers = favoriteMembers;
	}

	/**
	 * 获取规格
	 * @return 规格
	 */
	public Set<Specification> getSpecifications() {
//		for (Specification specification:specifications) {
//			if (specification.getId().equals(1L)) {
//				specification.setName(this.getGoods().getSpecification1Title());
//			}
//			if (specification.getId().equals(2L)) {
//				specification.setName(this.getGoods().getSpecification2Title());
//			}
//		}
		return specifications;
	}

	/**
	 * 设置规格
	 * @param specifications 规格
	 */
	public void setSpecifications(Set<Specification> specifications) {
		this.specifications = specifications;
	}

	/**
	 * 获取规格值
	 * @return 规格值
	 */
	public Set<SpecificationValue> getSpecificationValues() {
//		for (SpecificationValue specificationValue:specificationValues) {
//			if (specificationValue.getSpecification().getId().equals(1L)) {
//				specificationValue.getSpecification().setName(this.getGoods().getSpecification1Title());
//			}
//			if (specificationValue.getSpecification().getId().equals(2L)) {
//				specificationValue.getSpecification().setName(this.getGoods().getSpecification2Title());
//			}
//		}
		return specificationValues;
	}

	/**
	 * 设置规格值
	 * @param specificationValues 规格值
	 */
	public void setSpecificationValues(Set<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	/**
	 * 获取促销
	 * @return 促销
	 */
	public Set<PromotionProduct> getPromotionProducts() {
		return promotionProducts;
	}

	/**
	 * 设置促销
	 * @param promotions 促销
	 */
	public void setPromotionProducts(Set<PromotionProduct> promotionProducts) {
		this.promotionProducts = promotionProducts;
	}

	/**
	 * 设置促销
	 * @param promotions 促销
	 */
	@Transient
	public void setPromotions(Set<Promotion> promotions) {
	}

	/**
	 * 获取购物车项
	 * @return 购物车项
	 */
	public Set<CartItem> getCartItems() {
		return cartItems;
	}

	/**
	 * 设置购物车项
	 * @param cartItems 购物车项
	 */
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	/**
	 * 获取订单项
	 * @return 订单项
	 */
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * 设置订单项
	 * @param orderItems 订单项
	 */
	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	/**
	 * 获取赠品项
	 * @return 赠品项
	 */
	public Set<GiftItem> getGiftItems() {
		return giftItems;
	}

	/**
	 * 设置赠品项
	 * @param giftItems 赠品项
	 */
	public void setGiftItems(Set<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	/**
	 * 获取到货通知
	 * @return 到货通知
	 */
	public Set<ProductNotify> getProductNotifies() {
		return productNotifies;
	}

	/**
	 * 设置到货通知
	 * @param productNotifies 到货通知
	 */
	public void setProductNotifies(Set<ProductNotify> productNotifies) {
		this.productNotifies = productNotifies;
	}

	/**
	 * 获取会员价
	 * @return 会员价
	 */
	public Map<MemberRank, BigDecimal> getMemberPrice() {
		return memberPrice;
	}

	/**
	 * 设置会员价
	 * @param memberPrice 会员价
	 */
	public void setMemberPrice(Map<MemberRank, BigDecimal> memberPrice) {
		this.memberPrice = memberPrice;
	}

	/**
	 * 获取参数值
	 * @return 参数值
	 */
	public Map<Parameter, String> getParameterValue() {
		return parameterValue;
	}

	/**
	 * 设置参数值
	 * @param parameterValue 参数值
	 */
	public void setParameterValue(Map<Parameter, String> parameterValue) {
		this.parameterValue = parameterValue;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	public Boolean getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(Boolean isLimit) {
		this.isLimit = isLimit;
	}

	public Long getLimitCounts() {
		return limitCounts;
	}

	public void setLimitCounts(Long limitCounts) {
		this.limitCounts = limitCounts;
	}

	public Boolean getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Boolean privilege) {
		this.privilege = privilege;
	}

	public BigDecimal getFee() {
		if (fee==null) {
			return BigDecimal.ZERO;	
		} else {
		   return fee;
		}
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	/**
	 * 判断促销是否有效
	 * @param promotion 促销
	 * @return 促销是否有效
	 */
	public boolean promotionIsValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
			return false;
		}
		Integer quantity = 1;
		if ((promotion.getMinimumQuantity() != null && promotion.getMinimumQuantity() > quantity) || (promotion.getMaximumQuantity() != null && promotion.getMaximumQuantity() < quantity)) {
			return false;
		}
		BigDecimal price = getPrice();
		if ((promotion.getMinimumPrice() != null && promotion.getMinimumPrice().compareTo(price) > 0) || (promotion.getMaximumPrice() != null && promotion.getMaximumPrice().compareTo(price) < 0)) {
			return false;
		}
		return true;
	}
	
	/** 获取折后价格 */
	public BigDecimal calcEffectivePrice(Member member) {
		BigDecimal price = getPrice();
		for (Promotion promotion : getValidPromotions()) {
			if (!promotionIsValid(promotion)) {
				continue;
			}
			if (promotion.getType().equals(Promotion.Type.seckill)) {
				BigDecimal currentPromotionPrice = promotion.calculatePrice(1, price);
				if (price.compareTo(currentPromotionPrice)>0) {
					price = currentPromotionPrice;
				}
			}
		}
		Setting setting = SettingUtils.get();
		price = price.compareTo(BigDecimal.ZERO) > 0 ? price : BigDecimal.ZERO;
		if (member != null) { // 会员折扣价
			MemberRank memberRank = null;
			if (member.getTenant() != null) {
				Tenant parent = getTenant();
				TenantRelation relation = member.getTenant().getRelation(parent);
				if (relation != null) {
					memberRank = relation.getMemberRank();
				}

			}
			if (memberRank != null) {
				Map<MemberRank, BigDecimal> memberPrices = getMemberPrice();
				if (memberPrices != null && !memberPrices.isEmpty() && (memberPrices.size()>0)  && memberPrices.containsKey(memberRank) && !memberRank.getIsDefault()) {
					BigDecimal memberPrice = memberPrices.get(memberRank);
					if (memberPrice.compareTo(BigDecimal.ZERO)!=0) {
					   return price.compareTo(BigDecimal.ZERO) > 0 ? setting.setScale(memberPrice):price;
					}
				}
			}
		}
		return setting.setScale(price);
		 
	}

	public BigDecimal calculateMarketPrice(PackagUnit packagUnit) {
		BigDecimal basePrice = getMarketPrice(); // 商品基础价格
		if (packagUnit != null) { // 走包装单位计算
			return basePrice.multiply(packagUnit.getCoefficient());
		} else { // 走商品价格
			return basePrice;
		}
	}

	public BigDecimal calculateFee() {
		BigDecimal fee = getFee();
		if (fee!=null && fee.compareTo(BigDecimal.ZERO)>0) {
			return fee;
		} else {
			BigDecimal basePrice = getPrice(); // 商品基础价格
			if (getTenant().getGeneralize()!=null) {
			   basePrice = basePrice.multiply(getTenant().getGeneralize());
			} else {
			   basePrice = BigDecimal.ZERO;
			}
			return basePrice;
		}
	}

	public BigDecimal calculatePrice(Member member, PackagUnit packagUnit) {
		try {
		Setting setting = SettingUtils.get();
		BigDecimal basePrice = BigDecimal.ZERO; // 商品基础价格
		if (packagUnit != null) { // 走包装单位计算
			basePrice = packagUnit.getPrice() == null ? packagUnit.calculatePrice() : packagUnit.getPrice();
		} else { // 走商品价格
			basePrice = getPrice();
		}
		if (basePrice == null) {
			return BigDecimal.ZERO;    
		}
		if (member != null) { // 会员折扣价
			MemberRank memberRank = null;
			if (member.getTenant() != null && member.getTenant().getTenantType().equals(Tenant.TenantType.retailer)) {
				Tenant parent = getTenant();
				TenantRelation relation = member.getTenant().getRelation(parent);
				if (relation != null) {
					memberRank = relation.getMemberRank();
				}

			}
			if (memberRank != null) {
				Map<MemberRank, BigDecimal> memberPrices = getMemberPrice();
				if (memberPrices != null && !memberPrices.isEmpty() && (memberPrices.size()>0)  && memberPrices.containsKey(memberRank) && !memberRank.getIsDefault()) {
					BigDecimal memberPrice = memberPrices.get(memberRank);
					if (packagUnit != null) {
						memberPrice = memberPrice.multiply(packagUnit.getCoefficient());
					}
					if (memberPrice.compareTo(BigDecimal.ZERO)!=0) {
						   return basePrice.compareTo(BigDecimal.ZERO) > 0 ? setting.setScale(memberPrice):basePrice;
						}
				}
			}
		}
		return setting.setScale(basePrice);
	}
		catch (Exception e) {
			System.out.println(e.toString());
			return new BigDecimal("99999999");
		}
	}

	/**
	 * 计算运费描术
	 */
	public String calculateFreightDesc() {
	    return getTenant().getFreight().getDescr();
	}
	
	
	
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getMadeIn() {
		return madeIn;
	}

	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public void setBrandSeries(Set<BrandSeries> brandSeries) {
		this.brandSeries = brandSeries;
	}

	public Set<BrandSeries> getBrandSeries() {
		return brandSeries;
	}

	public Set<ProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(Set<ProductItem> productItems) {
		this.productItems = productItems;
	}

	public Tenant getSupplier() {
		return supplier;
	}

	public void setSupplier(Tenant supplier) {
		this.supplier = supplier;
	}

	public String getSupplierProductSn() {
		return supplierProductSn;
	}

	public void setSupplierProductSn(String supplierProductSn) {
		this.supplierProductSn = supplierProductSn;
	}

	//以下部份关于全文搜索使用
		
	@Transient
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	public String getTenantName() {
		return getTenant().getName();
	}

	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public Tenant.Status getStatus() {
	    return getTenant().getStatus();
	}
	
	//所属商圈
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getCommunityIds() {
		String ids = ",";
		for (DeliveryCenter deliveryCenter:getTenant().getDeliveryCenters()) {
			if (deliveryCenter.getCommunity()!=null) {
				   ids = ids+deliveryCenter.getCommunityId().toString()+",";
				}
			
		}
		return ids;
	}
	
	//所属城市
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getCityIds() {
		String ids = ",";
		for (DeliveryCenter deliveryCenter:getTenant().getDeliveryCenters()) {
			if (deliveryCenter.getCommunity()!=null) {
				   ids = ids+deliveryCenter.getCityId().toString()+",";
				}
			
		}
		return ids;
	}
	
	//所在地区
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getAreaIds() {
		String ids = ",";
		for (DeliveryCenter deliveryCenter:getTenant().getDeliveryCenters()) {
			if (deliveryCenter.getCommunity()!=null) {
				   ids = ids+deliveryCenter.getAreaId().toString()+",";
				}
			
		}
		return ids;
	}

	//品牌
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getBrandId() {
		if (getBrand() != null) {
			return NumericUtils.longToPrefixCoded(getBrand().getId());
		} else {
			return "";
		}
	}

	//平台分类	
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getProductCategoryIds() {
		if (getProductCategory()!=null) {
			return getProductCategory().getTreePath()+getProductCategory().getId()+TenantCategory.TREE_PATH_SEPARATOR;
		} else {
			return "";
		}
	}

	public Boolean getMarketable() {
		return isMarketable;
	}

	public void setMarketable(Boolean marketable) {
		isMarketable = marketable;
	}

	public Boolean getList() {
		return isList;
	}

	public void setList(Boolean list) {
		isList = list;
	}

	public Boolean getTop() {
		return isTop;
	}

	public void setTop(Boolean top) {
		isTop = top;
	}

	public Boolean getGift() {
		return isGift;
	}

	public void setGift(Boolean gift) {
		isGift = gift;
	}

	public Boolean getLimit() {
		return isLimit;
	}

	public void setLimit(Boolean limit) {
		isLimit = limit;
	}

	public Set<ExtendCatalog> getExtendCatalogs() {
		return extendCatalogs;
	}

	public void setExtendCatalogs(Set<ExtendCatalog> extendCatalogs) {
		this.extendCatalogs = extendCatalogs;
	}

	public Set<SingleProduct> getSingleProducts() {
		return singleProducts;
	}

	public void setSingleProducts(Set<SingleProduct> singleProducts) {
		this.singleProducts = singleProducts;
	}
}