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
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.Setting;
import net.wit.entity.PaymentMethod.Method;
import net.wit.util.FreemarkerUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import freemarker.template.TemplateException;

/**
 * @ClassName: Coupon
 * @Description: 优惠券
 * @author Administrator
 * @date 2014年10月14日 上午9:07:57
 */
@Entity
@Table(name = "xx_coupon")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_coupon_sequence")
public class Coupon extends BaseEntity {

	private static final long serialVersionUID = -7907808728349149722L;

	/** 兑换券类型 */
	public enum Type {
		/** 平台代金券 */
		coupon,
		/** 礼品券 */
		pickup,
		/** 店内代金券 */
		tenantCoupon,
		/** 店内礼品券 */
		tenantPickup,
		/** 店内红包 */
		tenantBonus,
		/** 平台套券 */
		multipleCoupon
	}

	/**
	 * 优惠券/红包状态
	 */
	public enum Status {
		/** 未确认 */
		unconfirmed,
		/** 已确认 */
		confirmed,
		/** 已关闭 */
		cancelled
	}

	/** 过程状态 */
//	public enum  ProcessStatus {
//		/** 可领用  */
//		canUse,
//		/** 未开始 */
//		unBegin,
//		/** 已领完 */
//		unUsed,
//		/** 已结束 */
//		Expired
//	}

	/** 类型 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Type type;

	/** 状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Status status;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 前缀 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String prefix;

	/** 金额 */
	@Column(nullable = false, precision = 21, scale = 2)
	private BigDecimal amount;


	/** 冻结单价 */
	@Column(precision = 21, scale = 2)
	private BigDecimal  freezePrice;

	/** 有效日期 */
	private Date startDate;

	/** 有效日期 */
	private Date endDate;

	/** 商品分类 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_coupon_product_category")
	private Set<ProductCategory> productCategory;

	/** 支付类型 */
	private Method method;

	/** 最小商品数量 */
	private Integer minimumQuantity;

	/** 最大商品数量 */
	private Integer maximumQuantity;

	/** 提醒数量 */
	private Integer remindQuantity;

	/** 最小商品价格 */
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 2)
	private BigDecimal minimumPrice;

	/** 最大商品价格 */
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 2)
	private BigDecimal maximumPrice;

	/** 有效天数 */
	@Min(0)
	private Integer effectiveDays;

	/** 价格运算表达式 */
	private String priceExpression;

	/** 是否启用 */
	@NotNull
	@Column(nullable = false)
	private Boolean isEnabled;

	/** 是否允许积分兑换 */
	@NotNull
	@Column(nullable = false)
	private Boolean isExchange;

	/** 是否允许领取多次 */
	@NotNull
	@Column(nullable = false)
	private Boolean isReceiveMore;

	/** 可领取次数 */
	@Min(1)
	private Long receiveTimes;

	/** 积分兑换数 */
	@Min(0)
	private Long point;

	/** 介绍 */
	@Lob
	private String introduction;

	/** 优惠码 */
	@OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<CouponCode> couponCodes = new HashSet<CouponCode>();

	/** 促销 */
	@ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY)
	private Set<Promotion> promotions = new HashSet<Promotion>();

	/** 适用商品 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_coupon_product")
	private Set<Product> products = new HashSet<Product>();

	/** 订单 */
	@ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<Order>();

	/** 已使用 */
	@Min(0)
	@Column(nullable = false,columnDefinition = "int(11) default 0")
	private Integer usedCount = new Integer(0);

	/** 已发放 */
	@Min(0)
	@Column(nullable = false,columnDefinition = "int(11) default 0")
	private Integer sendCount = new Integer(0);

	/** 总数 */
	@Min(0)
	@Column(nullable = false,columnDefinition = "int(11) default 0")
	private Integer count = new Integer(0);

	/** 商家 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	@ManyToOne(fetch = FetchType.LAZY)
	private ActivityPlanning activityPlanning;

	@ManyToOne(fetch = FetchType.LAZY)
	private Payment payment;

	/** 计算优惠价格 */
	public BigDecimal calculatePrice(Integer quantity, BigDecimal price) {
		if (price == null || StringUtils.isEmpty(getPriceExpression())) {
			return price;
		}
		BigDecimal result = BigDecimal.ZERO;
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", quantity);
			model.put("price", price);
			result = new BigDecimal(FreemarkerUtils.process("#{(" + getPriceExpression() + ");M50}", model));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		if (result.compareTo(price) > 0) {
			return price;
		}
		return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
	}

	/** 计算优惠价格 */
	public BigDecimal calculatePrice(Long point) {
		if (point == null || StringUtils.isEmpty(getPriceExpression())) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = BigDecimal.ZERO;
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("price", point);
			result = new BigDecimal(FreemarkerUtils.process("#{(" + getPriceExpression() + ");M50}", model));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Promotion> promotions = getPromotions();
		if (promotions != null) {
			for (Promotion promotion : promotions) {
				promotion.getCoupons().remove(this);
			}
		}
		List<Order> orders = getOrders();
		if (orders != null) {
			for (Order order : orders) {
				order.getCoupons().remove(this);
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
	 * 获取兑换券类型
	 * @return 兑换券类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置兑换券类型
	 * @param type 兑换券类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取前缀
	 * @return 前缀
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 设置前缀
	 * @param prefix 前缀
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
	 * 获取是否启用
	 * @return 是否启用
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * @param isEnabled 是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取是否允许积分兑换
	 * @return 是否允许积分兑换
	 */
	public Boolean getIsExchange() {
		return isExchange;
	}

	/**
	 * 设置是否允许积分兑换
	 * @param isExchange 是否允许积分兑换
	 */
	public void setIsExchange(Boolean isExchange) {
		this.isExchange = isExchange;
	}

	/**
	 * 获取积分兑换数
	 * @return 积分兑换数
	 */
	public Long getPoint() {
		return point;
	}

	/**
	 * 设置积分兑换数
	 * @param point 积分兑换数
	 */
	public void setPoint(Long point) {
		this.point = point;
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
	 * 获取优惠码
	 * @return 优惠码
	 */
	public Set<CouponCode> getCouponCodes() {
		return couponCodes;
	}

	/**
	 * 设置优惠码
	 * @param couponCodes 优惠码
	 */
	public void setCouponCodes(Set<CouponCode> couponCodes) {
		this.couponCodes = couponCodes;
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


	/**
	 *
	 * @return startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Set<ProductCategory> getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(Set<ProductCategory> productCategory) {
		this.productCategory = productCategory;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Integer getMinimumQuantity() {
		return minimumQuantity;
	}

	public void setMinimumQuantity(Integer minimumQuantity) {
		this.minimumQuantity = minimumQuantity;
	}

	public Integer getMaximumQuantity() {
		return maximumQuantity;
	}

	public void setMaximumQuantity(Integer maximumQuantity) {
		this.maximumQuantity = maximumQuantity;
	}

	public BigDecimal getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(BigDecimal minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	public BigDecimal getMaximumPrice() {
		return maximumPrice;
	}

	public void setMaximumPrice(BigDecimal maximumPrice) {
		this.maximumPrice = maximumPrice;
	}

	public Integer getEffectiveDays() {
		return effectiveDays;
	}

	public void setEffectiveDays(Integer effectiveDays) {
		this.effectiveDays = effectiveDays;
	}

	/**
	 * 获取订单
	 * @return 订单
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置订单
	 * @param orders 订单
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public Boolean getIsReceiveMore() {
		return isReceiveMore;
	}

	public void setIsReceiveMore(Boolean isReceiveMore) {
		this.isReceiveMore = isReceiveMore;
	}

	public Long getReceiveTimes() {
		return receiveTimes;
	}

	public void setReceiveTimes(Long receiveTimes) {
		this.receiveTimes = receiveTimes;
	}

	/** 已使用 */
	public Integer getUsedCount() {
		return usedCount;
	}

	/** 已使用 */
	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	/** 已发放 */
	public Integer getSendCount() {
		return sendCount;
	}

	/** 已发放 */
	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	/** 总数 */
	public Integer getCount() {
		return count;
	}

	/** 总数 */
	public void setCount(Integer count) {
		this.count = count;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ActivityPlanning getActivityPlanning() {
		return activityPlanning;
	}

	public void setActivityPlanning(ActivityPlanning activityPlanning) {
		this.activityPlanning = activityPlanning;
	}

	public Integer getRemindQuantity() {
		return remindQuantity;
	}

	public void setRemindQuantity(Integer remindQuantity) {
		this.remindQuantity = remindQuantity;
	}

	public List<CouponCode> getVaildCouponCodes() {
		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (CouponCode couponCode:getCouponCodes()) {
			if (!couponCode.isUsable() && couponCode.getMember()==null) {
				couponCodes.add(couponCode);
			}
		}
		return couponCodes;
	}

	public List<CouponCode> getActivityCouponCodes() {
		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (CouponCode couponCode:getCouponCodes()) {
			if (!couponCode.isUsables() && couponCode.getMember()==null) {
				couponCodes.add(couponCode);
			}
		}
		return couponCodes;
	}

	/** 判断是否已开始 */
	public boolean getHasStart() {
		return getStartDate() != null && new Date().after(getStartDate());
	}

	public Boolean getHasExpired(){
		return getEndDate() != null&&new Date().after(getEndDate());
	}

	public BigDecimal getFreezePrice() {
		return freezePrice;
	}

	public void setFreezePrice(BigDecimal freezePrice) {
		this.freezePrice = freezePrice;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	/**
	 * 计算冻结单价
	 * @return
	 */
	public BigDecimal calcFreezePrice() {
		Setting setting = SettingUtils.get();
		if(getAmount()!=null){
			return setting.setScale(getAmount().multiply(setting.getBonusPercent()));
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 冻结金额
	 * @return
	 */
	public BigDecimal getFreezeAmount() {
		if(getFreezePrice()!=null){
			return getFreezePrice().multiply(new BigDecimal(getCount()));
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 是否过期/是否可用
	 * @return
	 */
	public boolean getExpired(){

		if(getStatus()==Status.cancelled){
			return false;
		}

		if(getType().equals(Type.multipleCoupon)){
			return true;
		}

		if(getStartDate().before(new Date())&&getEndDate().after(new Date())){
			if(getCount().compareTo(getSendCount())>0){
				return true;
			}else {
				return false;
			}
		}
		return false;
	}


	public int getEffectiveCount(){
		return  getCount()-getUsedCount();
	}
}