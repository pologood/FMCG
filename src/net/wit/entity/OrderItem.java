/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import net.wit.Setting;
import net.wit.util.SettingUtils;

/**
 * @ClassName: OrderItem
 * @Description: 订单项
 * @author Administrator
 * @date 2014年10月14日 上午9:10:08
 */
@Entity
@Table(name = "xx_order_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_order_item_sequence")
public class OrderItem extends BaseEntity {

	private static final long serialVersionUID = -4999926022604479334L;

	/** 商品编号 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Column(nullable = false, updatable = false)
	private String sn;

	/** 商品条码 */
	private String barcode;

	/** 商品名称 */
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private String name;

	/** 商品全称 */
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private String fullName;

	/** 价格 */
	@Expose
	@JsonProperty
	@NotNull
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal price;

	/** 结算成本 */
	@Expose
	@JsonProperty
	@NotNull
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal cost;
	
	/** 分润单价 */
	@NotNull
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal profit;
	/** 商品折扣价格 */
	@Expose
	@NotNull
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal discount = BigDecimal.ZERO;

	/** 商品重量 */
	@Expose
	@JsonProperty
	@Column(updatable = false)
	private Integer weight;

	/** 商品缩略图 */
	@Expose
	@JsonProperty
	@Column(updatable = false)
	private String thumbnail;

	/** 是否为赠品 */
	@JsonProperty
	@Column(nullable = false, updatable = false)
	private Boolean isGift;

	/** 订购数量 */
	@Expose
	@JsonProperty
	@NotNull
	@Min(1)
	@Max(10000)
	@Column(nullable = false)
	private Integer quantity;

	/** 订购单位 */
	@Expose
	@JsonProperty
	@Column
	private String packagUnitName;

	/** 换算系数 */
	@Expose
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal(21,6) default 1")
	private BigDecimal coefficient;

	/** 已发货数量 */
	@Expose
	@Column(nullable = false)
	private Integer shippedQuantity;

	/** 已退货数量 */
	@Expose
	@Column(nullable = false)
	private Integer returnQuantity;

	/** 商品 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	
	/** 供应商 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant supplier;
	
	/** 商品套餐 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductGroup productGroup;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

	/** 子订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trades", nullable = false, updatable = false)
	private Trade trade;

	/** 买家评价 */
	@OneToOne(mappedBy = "orderItem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, optional = true)
	private Review review;

	/** 发货单 */
	@OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("createDate asc")
	private Set<Authenticode> authenticodes = new HashSet<Authenticode>();

	/**
	 * 获取商品总重量
	 * @return 商品总重量
	 */
	@JsonProperty
	public int getTotalWeight() {
		if (getWeight() != null && getQuantity() != null) {
			return getWeight() * getQuantity();
		} else {
			return 0;
		}
	}

	/**
	 * 获取商品分润金额
	 * @return 商品分润金额
	 */
	public BigDecimal getTotalProfit() {
		if (getProfit() != null && getQuantity() != null) {
			return getProfit().multiply(calculateShippedQuantity().subtract(calculateReturnQuantity()));
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 获取小计
	 * @return 小计
	 */
	@JsonProperty
	public BigDecimal getSubtotal() {
		if (getPrice() != null && getQuantity() != null) {
			return getPrice().multiply(new BigDecimal(getQuantity())) ;
		} else {
			return new BigDecimal(0);
		}
	}

	/** 获取折前单价 */
	@JsonProperty
	public BigDecimal getOriginalPrice() {
		if (getQuantity()==0) {
			return getPrice();
		}
		Setting setting = SettingUtils.get();
		return setting.setScale(getSubtotal().add(getDiscount()).divide(
				                        new BigDecimal(getQuantity()) 
				                        )
				               );
		 
	}
	
	/**
	 * 获取小计
	 * @return 小计
	 */
	@JsonProperty
	public BigDecimal getRealHairSubtotal() {
		if (getPrice() != null && getQuantity() != null) {
			return getPrice().multiply(new BigDecimal(getShippedQuantity()));
		} else {
			return new BigDecimal(0);
		}
	}

	public void setCalculatePackagUnit(Product product, PackagUnit packagUnit) {
		if (packagUnit != null) {
			this.packagUnitName = packagUnit.getName();
			this.coefficient = packagUnit.getCoefficient();
		} else {
			this.packagUnitName = product.getUnit();
			this.coefficient = BigDecimal.ONE;
		}
	}

	/** 计算订货数量 */
	public BigDecimal calculateQuantity() {
		return new BigDecimal(getQuantity()).multiply(this.getCoefficient());
	}

	/** 计算订货数量 */
	public int calculateQuantityIntValue() {
		return calculateQuantity().intValue();
	}

	/** 计算发货数量 */
	public BigDecimal calculateShippedQuantity() {
		return new BigDecimal(getShippedQuantity()).multiply(this.getCoefficient());
	}

	/** 计算发货数量 */
	public int calculateShippedQuantityIntValue() {
		return calculateShippedQuantity().intValue();
	}

	/** 计算退货数量 */
	public BigDecimal calculateReturnQuantity() {
		return new BigDecimal(getReturnQuantity()).multiply(this.getCoefficient());
	}

	/** 计算退货数量 */
	public int calculateReturnQuantityIntValue() {
		return calculateReturnQuantity().intValue();
	}

	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		if (this.packagUnitName == null && product != null) {
			this.packagUnitName = product.getUnit();
		}
		if (this.coefficient == null) {
			this.coefficient = BigDecimal.ONE;
		}
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		if (this.packagUnitName == null && product != null) {
			this.packagUnitName = product.getUnit();
		}
		if (this.coefficient == null) {
			this.coefficient = BigDecimal.ONE;
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取商品编号
	 * @return 商品编号
	 */
	public String getSn() {
		return sn;
	}

	/**
	 * 设置商品编号
	 * @param sn 商品编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取商品名称
	 * @return 商品名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置商品名称
	 * @param name 商品名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取商品全称
	 * @return 商品全称
	 */
	@JsonProperty
	public String getFullName() {
		return fullName;
	}

	/**
	 * 设置商品全称
	 * @param fullName 商品全称
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * 获取商品价格
	 * @return 商品价格
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置商品价格
	 * @param price 商品价格
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	
	/**
	 * 获取商品折扣价格
	 * @return 商品折扣价格
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/**
	 * 设置商品折扣价格
	 * @param price 商品折扣价格
	 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	/**
	 * 获取商品重量
	 * @return 商品重量
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * 设置商品重量
	 * @param weight 商品重量
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * 获取商品缩略图
	 * @return 商品缩略图
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * 设置商品缩略图
	 * @param thumbnail 商品缩略图
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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
	 * 获取数量
	 * @return 数量
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 设置数量
	 * @param quantity 数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取订购单位
	 * @return 订购单位
	 */
	public String getPackagUnitName() {
		return packagUnitName;
	}

	/**
	 * 设置订购单位
	 * @param quantity 订购单位
	 */
	public void setPackagUnitName(String packagUnitName) {
		this.packagUnitName = packagUnitName;
	}

	/**
	 * 获取计量总数
	 * @return 计量总数
	 */
	public BigDecimal getCoefficient() {
		return coefficient;
	}

	/**
	 * 设置计量总数
	 * @param quantity 计量总数
	 */
	public void setCoefficient(BigDecimal coefficient) {
		this.coefficient = coefficient;
	}

	/**
	 * 获取已发货数量
	 * @return 已发货数量
	 */
	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	/**
	 * 设置已发货数量
	 * @param shippedQuantity 已发货数量
	 */
	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	/**
	 * 获取已退货数量
	 * @return 已退货数量
	 */
	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	/**
	 * 设置已退货数量
	 * @param returnQuantity 已退货数量
	 */
	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	/**
	 * 获取商品
	 * @return 商品
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * 设置商品
	 * @param product 商品
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * 获取订单
	 * @return 订单
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * 设置订单
	 * @param order 订单
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * 获取子订单
	 * @return 子订单
	 */
	@JsonProperty
	public Trade getTrade() {
		return trade;
	}

	/**
	 * 设置子订单
	 * @param order 子订单
	 */
	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	/**
	 * 获取订单项评价
	 * @return 订单项评价
	 */
	public Review getReview() {
		return review;
	}

	/**
	 * 设置订单项评价
	 * @param review 订单项评价
	 */
	public void setReview(Review review) {
		this.review = review;
	}

	/**
	 * 获取校验码
	 * @return 校验码
	 */
	public Set<Authenticode> getAuthenticodes() {
		return authenticodes;
	}

	/**
	 * 设置校验码
	 * @param shippings 校验码
	 */
	public void setAuthenticodes(Set<Authenticode> authenticodes) {
		this.authenticodes = authenticodes;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

	public Tenant getSupplier() {
		return supplier;
	}

	public void setSupplier(Tenant supplier) {
		this.supplier = supplier;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	
}