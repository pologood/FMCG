/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.util.FreemarkerUtils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import freemarker.template.TemplateException;

/**
 * Entity - 促销方案
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_promotion")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_promotion_sequence")
public class Promotion extends OrderEntity {
              
	private static final long serialVersionUID = 3536993535267962279L;

	/** 访问路径前缀 */
	private static final String PATH_PREFIX = "/promotion/content";

	/** 访问路径后缀 */
	private static final String PATH_SUFFIX = ".jhtml";

	/** 方案类型 */
	public enum Type {
		/** 买赠 单品活动*/
		buyfree,
		/** 秒杀、限时抢购 单品活动 */
		seckill,
		/** 满额折扣  商家活动  */
		discount,
		/** 满额包邮  商家活动 */
		mail,
		/** 积分兑换 */
		points,
		/** 送优惠券 */
		coupon,
		/** 活动立减（平台主办） */
		activity
	}

	/** 名称 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;
	
	/** 标题 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String title;

	/** 起始日期 */
	@Expose
	@JsonProperty
	private Date beginDate;

	/** 结束日期 */
	@Expose
	@JsonProperty
	private Date endDate;

	/** 最小商品数量 */
	@Expose
	@JsonProperty
	private Integer minimumQuantity;

	/** 最大商品数量 */
	@Expose
	@JsonProperty
	private Integer maximumQuantity;

	/** 最小商品价格 */
	@Expose
	@JsonProperty
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	private BigDecimal minimumPrice;

	/** 最大商品价格 */
	@Expose
	@JsonProperty
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	private BigDecimal maximumPrice;

	/** 折扣率 */
	@Min(0)
	@Column(precision = 21, scale = 6)
	private BigDecimal agioRate;
	
	/** 返现比率 */
	@Min(0)
	@Column(precision = 21, scale = 6)
	private BigDecimal backRate;
	
	/** 价格运算表达式 price-200 */
	@Expose
	private String priceExpression;

	/** 积分运算表达式 point*2 */
	@Expose
	private String pointExpression;

	/** 是否免运费 */
	@Expose
	@JsonProperty
	@NotNull
	@Column(nullable = false)
	private Boolean isFreeShipping;

	/** 是否允许使用优惠券 */
	@Expose
	@JsonProperty
	@NotNull
	@Column(nullable = false)
	private Boolean isCouponAllowed;

	/** 介绍 */
	@Expose
	@JsonProperty
	@Lob
	private String introduction;

	/** 方案类型 */
	@Expose
	private Type type;


	/** 促销商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 申请会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member; 

	/** 允许参加会员等级 */
	@Expose
	@JsonProperty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_promotion_member_rank")
	private Set<MemberRank> memberRanks = new HashSet<MemberRank>();

	/** 允许参与商品分类 */
	@Expose
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_promotion_product_category")
	private Set<ProductCategory> productCategories = new HashSet<ProductCategory>();

	/** 允许参与品牌 */
	@Expose
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_promotion_brand")
	private Set<Brand> brands = new HashSet<Brand>();

	/** 赠送  优惠券 */
	@Expose
	@JsonProperty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_promotion_coupon")
	private Set<Coupon> coupons = new HashSet<Coupon>();

	/** 赠品 */
	@Expose
	@JsonProperty
	@Valid
	@OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GiftItem> giftItems = new ArrayList<GiftItem>();

	/** 允许参与商品 */
	@Expose
	@OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PromotionProduct> promotionProducts = new ArrayList<PromotionProduct>(0);

	/** 促销参与会员 */
	@Expose
	@JsonProperty
	@OrderBy("offerPrice desc")
	@OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PromotionMember> promotionMembers = new ArrayList<PromotionMember>(0);

	/** 促销订单 */
	@OneToMany(mappedBy = "promotionScheme", fetch = FetchType.LAZY)
	private Set<Order> orders = new HashSet<Order>(0);

	/** 促销总价 */
	@Expose
	@JsonProperty
	@Transient
	private BigDecimal totalPrice = BigDecimal.ZERO;

	@JsonProperty
	public Product getDefaultProduct() {
		if (promotionProducts == null || promotionProducts.size() == 0) {
			return null;
		}
		return promotionProducts.get(0).getProduct();
	}

	/**
	 * 获取允许参与商品
	 * @return 允许参与商品
	 */
	@JsonProperty
	public Set<Product> getProducts() {
		Set<Product> products = new HashSet<Product>(promotionProducts.size());
		for (PromotionProduct pp : promotionProducts) {
			if (pp.getProduct()!=null) {
			   products.add(pp.getProduct());
			}
		}
		return products;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = getPromotionPrice();
	}

	/**
	 * 判断会员是否已经参与
	 * @param member
	 * @return
	 */
	public boolean isMemberPartake(Member member) {
		for (PromotionMember pm : this.getPromotionMembers()) {
			if (pm.getMember().getId().equals(member.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断产品是否直接在促销方案内
	 * @param product
	 * @return
	 */
	public boolean isProductPartake(Product product) {
		for (PromotionProduct pp : this.getPromotionProducts()) {
			if (pp.getProduct().getId().equals(product.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否已开始
	 * @return 是否已开始
	 */
	public boolean hasBegun() {
		return getBeginDate() == null || new Date().after(getBeginDate());
	}

	/**
	 * 判断是否已结束
	 * @return 是否已结束
	 */
	public boolean hasEnded() {
		return getEndDate() != null && new Date().after(getEndDate());
	}

	/**
	 * 获取访问路径
	 * @return 访问路径
	 */
	public String getPath() {
		if (getId() != null) {
			return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
		}
		return null;
	}

	/**
	 * 计算促销单价
	 * @param quantity 商品数量
	 * @param price 商品价格
	 * @return 促销格
	 */
	public BigDecimal calculatePrice(Integer quantity, BigDecimal price) {
		if (price == null || StringUtils.isEmpty(getPriceExpression())) {
			return price;
		}
		BigDecimal result = new BigDecimal(0);
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", quantity);
			model.put("price", price);
			if (getType().equals(Type.seckill)) { //限时折扣的存储方式改为指定售价
				result = new BigDecimal(FreemarkerUtils.process("#{(" + getPriceExpression() + ");M50}", model));
			} else {
				result = new BigDecimal(FreemarkerUtils.process("#{(" + getPriceExpression() + ");M50}", model));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		if (result.compareTo(price) > 0) {
			return price;
		}
		return result.compareTo(new BigDecimal(0)) > 0 ? result : new BigDecimal(0);
	}

	/** 获取促销价格 */
	@JsonProperty
	public BigDecimal getPromotionPrice() {
		BigDecimal result = new BigDecimal(0);
		for (PromotionProduct p : promotionProducts) {
			if (p.getPrice() != null && p.getQuantity() != null) {
				result = result.add(p.getPrice().multiply(new BigDecimal(p.getQuantity())));
			}
		}
		return result;
	}

	/** 获取促销原价 */
	public BigDecimal getPromotionOriginalPrice() {
		BigDecimal result = new BigDecimal(0);
		for (PromotionProduct p : promotionProducts) {
			result = result.add(p.getProduct().getPrice().multiply(new BigDecimal(p.getQuantity())));
		}
		return result;
	}

	/** 获取销售价格 */
	public BigDecimal getOriginalPrice() {
		BigDecimal result = BigDecimal.ZERO;
		for (PromotionProduct p : promotionProducts) {
			result = result.add(p.getProduct().getPrice().multiply(new BigDecimal(p.getQuantity())));
		}
		return result;
	}

	/** 获取促销人员个数 */
	public Integer getMemberCount() {
		return promotionMembers.size();
	}

	/** 获取促销人员个数 */
	public Integer getEffectiveCount() {
		int count = 0;
		for (PromotionMember pm : promotionMembers) {
			if (PromotionMember.Status.waitpay != pm.getStatus()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 计算促销赠送积分
	 * @param quantity 商品数量
	 * @param point 赠送积分
	 * @return 促销赠送积分
	 */
	public Long calculatePoint(Integer quantity, Long point) {
		if (point == null || StringUtils.isEmpty(getPointExpression())) {
			return point;
		}
		Long result = 0L;
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", quantity);
			model.put("point", point);
			model.put("amount", BigDecimal.ZERO);
			result = Double.valueOf(FreemarkerUtils.process("#{(" + getPointExpression() + ");M50}", model)).longValue();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (result < point) {
			return point;
		}
		return result > 0L ? result : 0L;
	}

	/**
	 * 计算促销赠送积分
	 * @param quantity 商品数量
	 * @param point 赠送积分
	 * @return 促销赠送积分
	 */
	public BigDecimal calculateAmountPoint(Integer quantity, BigDecimal amount) {
		if (amount == null || StringUtils.isEmpty(getPointExpression())) {
			return amount;
		}
		BigDecimal result = BigDecimal.ZERO;
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", quantity);
			model.put("point", 0L);
			model.put("amount", amount);
			result = new BigDecimal(FreemarkerUtils.process("#{(" + getPointExpression() + ");M50}", model));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (result.compareTo(amount) < 0) {
			return amount;
		}
		return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
	}

	/** 获取促销积分 */
	public Long getPromotionPoint() {
		if (getPointExpression() == null) {
			return 0L;
		}
		Long point = 0L;
		BigDecimal amount = BigDecimal.ZERO;
		try {
			for (PromotionProduct p : promotionProducts) {
				if (p.getProduct().getPoint() != null) {
					point = point + p.getProduct().getPoint() * p.getQuantity();
				} else {
					amount = amount.add(p.getPrice());
				}
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", 1L);
			model.put("point", point);
			point = Double.valueOf(FreemarkerUtils.process("#{(" + getPointExpression() + ");M50}", model)).longValue();

			model.clear();
			model.put("quantity", 1L);
			model.put("amount", amount);
			amount = new BigDecimal(FreemarkerUtils.process("#{(" + getPointExpression() + ");M50}", model));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return point + amount.longValue();
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.setPromotionScheme(null);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取起始日期
	 * @return 起始日期
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * 设置起始日期
	 * @param beginDate 起始日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 获取结束日期
	 * @return 结束日期
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 设置结束日期
	 * @param endDate 结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 获取最小商品数量
	 * @return 最小商品数量
	 */
	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	/**
	 * 设置最小商品数量
	 * @param minimumQuantity 最小商品数量
	 */
	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	/**
	 * 获取最大商品数量
	 * @return 最大商品数量
	 */
	public Integer getMaximumQuantity() {
		return maximumQuantity;
	}

	/**
	 * 设置最大商品数量
	 * @param maximumQuantity 最大商品数量
	 */
	public void setMaximumQuantity(Integer maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}

	/**
	 * 获取最小商品价格
	 * @return 最小商品价格
	 */
	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	/**
	 * 设置最小商品价格
	 * @param minimumPrice 最小商品价格
	 */
	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	/**
	 * 获取最大商品价格
	 * @return 最大商品价格
	 */
	public BigDecimal getMaximumPrice() {
		return maximumPrice;
	}

	/**
	 * 设置最大商品价格
	 * @param maximumPrice 最大商品价格
	 */
	public void setMaximumPrice(BigDecimal maximumPrice) {
		this.maximumPrice = maximumPrice;
	}

	/**
	 * 获取价格运算表达式
	 * @return 价格运算表达式
	 */
	public String getPriceExpression() {
		return priceExpression;
	}

	/**
	 * 设置价格运算表达式
	 * @param priceExpression 价格运算表达式
	 */
	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}

	/**
	 * 获取积分运算表达式
	 * @return 积分运算表达式
	 */
	public String getPointExpression() {
		return pointExpression;
	}

	/**
	 * 设置积分运算表达式
	 * @param pointExpression 积分运算表达式
	 */
	public void setPointExpression(String pointExpression) {
		this.pointExpression = pointExpression;
	}

	/**
	 * 获取是否免运费
	 * @return 是否免运费
	 */
	public Boolean getIsFreeShipping() {
		return isFreeShipping;
	}

	/**
	 * 设置是否免运费
	 * @param isFreeShipping 是否免运费
	 */
	public void setIsFreeShipping(Boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}

	/**
	 * 获取是否允许使用优惠券
	 * @return 是否允许使用优惠券
	 */
	public Boolean getIsCouponAllowed() {
		return isCouponAllowed;
	}

	/**
	 * 设置是否允许使用优惠券
	 * @param isCouponAllowed 是否允许使用优惠券
	 */
	public void setIsCouponAllowed(Boolean isCouponAllowed) {
		this.isCouponAllowed = isCouponAllowed;
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
	 * 获取方案类型
	 * @return 方案类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置方案类型
	 * @param type 方案类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取允许参加会员等级
	 * @return 允许参加会员等级
	 */
	public Set<MemberRank> getMemberRanks() {
		return memberRanks;
	}

	/**
	 * 设置允许参加会员等级
	 * @param memberRanks 允许参加会员等级
	 */
	public void setMemberRanks(Set<MemberRank> memberRanks) {
		this.memberRanks = memberRanks;
	}

	/**
	 * 获取允许参与商品分类
	 * @return 允许参与商品分类
	 */
	public Set<ProductCategory> getProductCategories() {
		return productCategories;
	}

	/**
	 * 设置允许参与商品分类
	 * @param productCategories 允许参与商品分类
	 */
	public void setProductCategories(Set<ProductCategory> productCategories) {
		this.productCategories = productCategories;
	}

	public List<PromotionProduct> getPromotionProducts() {
		return promotionProducts;
	}

	public void setPromotionProducts(List<PromotionProduct> promotionProducts) {
		this.promotionProducts = promotionProducts;
	}

	public List<PromotionMember> getPromotionMembers() {
		return promotionMembers;
	}

	public void setPromotionMembers(List<PromotionMember> promotionMembers) {
		this.promotionMembers = promotionMembers;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取允许参与品牌
	 * @return 允许参与品牌
	 */
	public Set<Brand> getBrands() {
		return brands;
	}

	/**
	 * 设置允许参与品牌
	 * @param brands 允许参与品牌
	 */
	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}

	/**
	 * 获取赠送优惠券
	 * @return 赠送优惠券
	 */
	public Set<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * 设置赠送优惠券
	 * @param coupons 赠送优惠券
	 */
	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * 获取赠品
	 * @return 赠品
	 */
	public List<GiftItem> getGiftItems() {
		return giftItems;
	}

	/**
	 * 设置赠品
	 * @param giftItems 赠品
	 */
	public void setGiftItems(List<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public BigDecimal getAgioRate() {
		return agioRate;
	}

	public void setAgioRate(BigDecimal agioRate) {
		this.agioRate = agioRate;
	}

	public BigDecimal getBackRate() {
		return backRate;
	}

	public void setBackRate(BigDecimal backRate) {
		this.backRate = backRate;
	}	

}