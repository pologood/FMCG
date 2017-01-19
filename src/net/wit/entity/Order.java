/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.Setting;
import net.wit.constant.SettingConstant;
import net.wit.util.SettingUtils;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: Order
 * @Description: 订单
 * @author Administrator
 * @date 2014年10月14日 上午9:09:56
 */
@Entity
@Table(name = "xx_order")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_order_sequence")
public class Order extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 订单名称分隔符 */
	private static final String NAME_SEPARATOR = " ";

	/** 订单类型 */
	public enum OrderType {
		/** 单商家 */
		single,
		/** 多商家 */
		composite
	}

	/** 查询状态 */
	public enum  QueryStatus {
		/** 全部订单 */
		none,
		/** 待发货 */
		unshipped,
		/** 待支付 */
		unpaid,
		/** 已支付 */
		paid,
		/** 待签收/已发货 */
		shipped,
		/** 待退货 */
		unrefunded,
		/** 待评价 */
		unreview,
		/** 已评价 */
		reviewed,
		/** 已完成 */
		completed,
		/** 已取消 */
		cancelled
	}

	/** 订单状态 */
	public enum OrderStatus {
		/** 未确认 */
		unconfirmed,
		/** 已确认 */
		confirmed,
		/** 已完成 */
		completed,
		/** 已取消 */
		cancelled
	}

	/** 订单类型 */
	public enum OrderSource {
		/** 网站 */
		web,
		/** 批发 */
		app,
		/** 零售*/
		wap,
		/** 屏幕*/
		pad,
	}

	/** 支付状态 */
	public enum PaymentStatus {
		/** 未支付 */
		unpaid,
		/** 部分支付 */
		partialPayment,
		/** 已支付 */
		paid,
		/** 部分退款 */
		partialRefunds,
		/** 已退款 */
		refunded,
		/** 退款中 */
		refundApply
	}

	/** 配送状态 */
	public enum ShippingStatus {
		/** 未发货 */
		unshipped,
		/** 部分发货 */
		partialShipment,
		/** 已发货 */
		shipped,
		/** 部分退货 */
		partialReturns,
		/** 已退货 */
		returned,
		/** 已签收 */
		accept,
		/** 退货中 */
		shippedApply

	}

	/** 订单编号 */
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;

	/** 订单状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private OrderStatus orderStatus;

	/** 支付状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private PaymentStatus paymentStatus;

	/** 配送状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private ShippingStatus shippingStatus;

	/** 支付手续费 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal fee;

	/** 运费 */
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal freight;

	/** 促销折扣 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal promotionDiscount;

	/** 优惠券抵扣 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal couponDiscount;

	/** 调整金额 */
	@NotNull
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal offsetAmount;

	/** 已付金额 */
	@Expose
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal amountPaid;

	@Column(nullable = false)
	private Boolean isProfitSettlement;

	/** 业务员*/
	@ManyToOne(fetch = FetchType.LAZY)
 	private Member extension;

	/** 赠送积分 */
	@JsonProperty
	@NotNull
	@Min(0)
	@Column(nullable = false)
	private Long point;

	/** 收货人 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String consignee;

	/** 地区名称 */
	@Expose
	@Column(nullable = false)
	private String areaName;

	/** 地址 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String address;

	/** 邮编 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String zipCode;

	/** 电话 */
	@Expose
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String phone;

	/** 收货地址 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn()
	private Receiver receiver;

	/** 是否开据发票 */
	@Expose
	@NotNull
	@Column(nullable = false)
	private Boolean isInvoice;

	/** 发票抬头 */
	@Expose
	@Length(max = 200)
	private String invoiceTitle;

	/** 税金 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal tax;

	/** 附言 */
	@Expose
	@Length(max = 200)
	private String memo;

	/** 预约时间 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Appointment appointment;

	/** 促销 */
	@Expose
	@Column(updatable = false)
	private String promotion;

	/** 到期时间 */
	@Expose
	@JsonProperty
	private Date expire;

	/** 锁定到期时间 */
	private Date lockExpire;

	/** 是否已分配库存 */
	// @Column(nullable = false)
	// private Boolean isAllocatedStock;

	/** 支付方式名称 */
	@JsonProperty
	@Column(nullable = false)
	private String paymentMethodName;

	/** 配送方式名称 */
	@JsonProperty
	@Column(nullable = false)
	private String shippingMethodName;

	/** 地区 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;

	/** 支付方式 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private PaymentMethod paymentMethod;

	/** 配送方式 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private ShippingMethod shippingMethod;

	/** 操作员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member operator;

	/** 下单设备 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Equipment equipment;

	/** tokenKey */
	@Expose
	private String tokenKey;

	/** 会员 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Member member;

	/** 订单类型 */
	private OrderType orderType;

	/** 订单来源 */
	private OrderSource orderSource;

	/** 优惠码 */
	@OneToOne(fetch = FetchType.LAZY)
	private CouponCode couponCode;

	/** 促销方案(由团购/拍卖生成) */
	@ManyToOne(fetch = FetchType.LAZY)
	private Promotion promotionScheme;

	/** 优惠券 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_order_coupon")
	private List<Coupon> coupons = new ArrayList<Coupon>();

	/** 子订单 */
	@Expose
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Trade> trades = new ArrayList<Trade>();

	/** 订单项 */
	@JsonProperty
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OrderBy("isGift asc")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	/** 订单日志 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("id desc")
	private Set<OrderLog> orderLogs = new HashSet<OrderLog>();

	/** 预存款 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	private Set<Deposit> deposits = new HashSet<Deposit>();

	/** 收款单 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Payment> payments = new HashSet<Payment>();

	/** 退款单 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Refunds> refunds = new HashSet<Refunds>();

	/** 发货单 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Shipping> shippings = new HashSet<Shipping>();

	/** 退货单 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Returns> returns = new HashSet<Returns>();

	/** 获取订单名称 */
	public String getName() {
		StringBuffer name = new StringBuffer();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getFullName() != null) {
					name.append(NAME_SEPARATOR).append(orderItem.getFullName());
				}
			}
			if (name.length() > 0) {
				name.deleteCharAt(0);
			}
		}
		return name.toString();
	}
	
	/** 获取订单描述 */
	public String getDescription() {
		StringBuffer description = new StringBuffer();
		if (getOrderItems() != null) {
			OrderItem orderItem = getOrderItems().get(0);
			if (orderItem != null && orderItem.getFullName() != null) {
				description.append(orderItem.getFullName());
			}
			if (getOrderItems().size() > 1) {
				description.append("等商品");
			}
		}
		return description.toString();
	}

	/** 获取商品重量 */
	public int getWeight() {
		int weight = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null) {
					weight += orderItem.getTotalWeight();
				}
			}
		}
		return weight;
	}

	/** 获取子订单 */
	public Trade getTrade(Tenant tenant) {
		if (getTrades() != null) {
			for (Trade trade : getTrades()) {
				if (tenant != null && tenant.equals(trade.getTenant())) {
					return trade;
				}
			}
		}
		return null;
	}

	/** 获取子订单 */
	public Trade getTrade(Long id) {
		if (getTrades() != null) {
			for (Trade trade : getTrades()) {
				if (trade.getTenant().getId().equals(id)) {
					return trade;
				}
			}
		}
		return null;
	}

	/** 获取商品数量 */
	@JsonProperty
	public int getQuantity() {
		int quantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getQuantity() != null) {
					quantity += orderItem.getQuantity();
				}
			}
		}
		return quantity;
	}

	/** 获取已发货数量 */
	public int getShippedQuantity() {
		int shippedQuantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getShippedQuantity() != null) {
					shippedQuantity += orderItem.getShippedQuantity();
				}
			}
		}
		return shippedQuantity;
	}

	/** 获取已退货数量 */
	public int getReturnQuantity() {
		int returnQuantity = 0;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getReturnQuantity() != null) {
					returnQuantity += orderItem.getReturnQuantity();
				}
			}
		}
		return returnQuantity;
	}

	/** 计算商家活动立减 */
	@JsonProperty
	public BigDecimal calcCouponDiscount() {
		BigDecimal discount = BigDecimal.ZERO;
		for (Trade trade:getTrades()) {
			discount = discount.add(trade.getCouponDiscount());
		};
		return discount;
	}

	/** 获取平台活动立减 */
	@JsonProperty
	public BigDecimal getDiscount() {
		BigDecimal discount = BigDecimal.ZERO;
		for (Trade trade:getTrades()) {
			discount = discount.add(trade.getDiscount());
		};
		return discount;
	}
	

	/** 获取分润金额 */
	@JsonProperty
	public BigDecimal getTotalProfit() {
		BigDecimal agency = BigDecimal.ZERO;
		for (Trade trade:getTrades()) {
			agency = agency.add(trade.getTotalProfit());
		};
		return agency;
	}
	
	/** 获取联盟佣金 */
	@JsonProperty
	public BigDecimal getAgencyAmount() {
		BigDecimal agency = BigDecimal.ZERO;
		for (Trade trade:getTrades()) {
			agency = agency.add(trade.getAgencyAmount());
		};
		return agency;
	}
	
	/** 获取联盟佣金 */
	@JsonProperty
	public BigDecimal getProviderAmount() {
		BigDecimal agency = BigDecimal.ZERO;
		for (Trade trade:getTrades()) {
			agency = agency.add(trade.getProviderAmount());
		};
		return agency;
	}
	
	/** 获取商品价格 */
	@JsonProperty
	public BigDecimal getPrice() {
		BigDecimal price = new BigDecimal(0);
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getSubtotal() != null) {
					price = price.add(orderItem.getSubtotal());
				}
			}
		}
		return price;
	}

	/** 获取订单金额 */
	@JsonProperty
	public BigDecimal getAmount() {
		BigDecimal amount = getPrice();
		if (getFee() != null) {
			amount = amount.add(getFee());
		}
		if (getFreight() != null) {
			amount = amount.add(getFreight());
		}
		if (getPromotionDiscount() != null) {
			amount = amount.subtract(getPromotionDiscount());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getDiscount() != null) {
			amount = amount.subtract(getDiscount());
			amount = amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
		}
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		if (getTax() != null) {
			amount = amount.add(getTax());
		}
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
	}

	/** 获取应付金额 */
	public BigDecimal getAmountPayable() {
		BigDecimal amountPayable = getAmount().subtract(getAmountPaid());
		return amountPayable.compareTo(new BigDecimal(0)) > 0 ? amountPayable : new BigDecimal(0);
	}

	/** 商家退款上限 */
	public BigDecimal getMaxReturnAmount() {
		BigDecimal amount = getAmount();
		amount = amount.subtract(getFee());
		if (ShippingStatus.unshipped != getShippingStatus()) {
			amount = amount.subtract(getFreight());
		}
		for (Refunds re : getRefunds()) {
			amount = amount.subtract(re.getAmount());
		}
		return amount;
	}

	/** 是否已过期 */
	@JsonProperty
	public boolean isExpired() {
		return getExpire() != null && new Date().after(getExpire());
	}

	/** 是否允许退款申请 */
	public boolean isRefundAllowed() {
		if (OrderStatus.confirmed != this.orderStatus) {
			return false;
		}
		return PaymentStatus.paid == this.paymentStatus;
	}

	/**
	 * 获取订单项
	 * @param sn 商品编号
	 * @return 订单项
	 */
	public OrderItem getOrderItem(String sn) {
		if (sn != null && getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && sn.equalsIgnoreCase(orderItem.getSn())) {
					return orderItem;
				}
			}
		}
		return null;
	}

	/**
	 * 获取订单项
	 * @param sn 商品编号
	 * @return 订单项
	 */
	public List<OrderItem> getOrderItem(Trade trade) {
		List<OrderItem> its = new ArrayList<OrderItem>();
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && trade.equals(orderItem.getTrade())) {
					its.add(orderItem);
				}
			}
		return its;
	}

	/** 是否显示完成 */
	public boolean getIsCompleteAllowed() {
		if (OrderStatus.completed == orderStatus) {
			return false;
		}
		// for (Trade trade : trades) {
		// if (trade.getShippingStatus() == ShippingStatus.returned || trade.getShippingStatus() == ShippingStatus.accept) {
		// return false;
		// }
		// }
		return true;
	}

	/** 是否显示评价 */
	public boolean getIsShowReviewAllowed() {
		for (Trade trade : trades) {
			if (trade.getMemberReview() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否已锁定
	 * @param operator 操作员
	 * @return 是否已锁定
	 */
	public boolean isLocked(Member operator) {
		//System.out.println(operator + " ----操作员---->"  +getOperator());
		return getLockExpire() != null && new Date().before(getLockExpire()) && ((operator != null && !operator.equals(getOperator())) || (operator == null && getOperator() != null));
	}

	/** 计算税金 */
	public BigDecimal calculateTax() {
		BigDecimal tax = new BigDecimal(0);
		Setting setting = SettingUtils.get();
		if (setting.getIsTaxPriceEnabled()) {
			BigDecimal amount = getPrice();
			if (getPromotionDiscount() != null) {
				amount = amount.subtract(getPromotionDiscount());
			}
			if (getCouponDiscount() != null) {
				amount = amount.subtract(getCouponDiscount());
			}
			if (getOffsetAmount() != null) {
				amount = amount.add(getOffsetAmount());
			}
			tax = amount.multiply(new BigDecimal(setting.getTaxRate().toString()));
		}
		return setting.setScale(tax);
	}

	/** 使用积分上限 */
	public Long getCouponMaxPoint() {
		Setting setting = SettingUtils.get();
		Double defaultPointScale = setting.getDefaultPointScale();
		if (defaultPointScale <= 0) {
			return 0L;
		}
		return getPrice().multiply(SettingConstant.maxCouponPointPer).divide(SettingConstant.amountPointScale).longValue();
	}

	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentMethod() != null) {
			setPaymentMethodName(getPaymentMethod().getName());
		}
		if (getShippingMethod() != null) {
			setShippingMethodName(getShippingMethod().getName());
		}
	}

	/** 删除前处理 */
	@PreRemove
	public void preRemove() {
		Set<Deposit> deposits = getDeposits();
		if (deposits != null) {
			for (Deposit deposit : deposits) {
				deposit.setOrder(null);
			}
		}
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取订单编号
	 * @return 订单编号
	 */
	@JsonProperty
	public String getSn() {
		return sn;
	}

	/**
	 * 设置订单编号
	 * @param sn 订单编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}

	/**
	 * 获取订单状态
	 * @return 订单状态
	 */
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	/**
	 * 设置订单状态
	 * @param orderStatus 订单状态
	 */
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * 获取支付状态
	 * @return 支付状态
	 */
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * 设置支付状态
	 * @param paymentStatus 支付状态
	 */
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * 获取配送状态
	 * @return 配送状态
	 */
	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	/**
	 * 设置配送状态
	 * @param shippingStatus 配送状态
	 */
	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	/**
	 * 获取支付手续费
	 * @return 支付手续费
	 */
	public BigDecimal getFee() {
		return fee;
	}

	/**
	 * 设置支付手续费
	 * @param fee 支付手续费
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	/**
	 * 获取运费
	 * @return 运费
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * 设置运费
	 * @param freight 运费
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	/**
	 * 获取促销折扣
	 * @return 促销折扣
	 */
	public BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}

	/**
	 * 设置促销折扣
	 * @param promotionDiscount 促销折扣
	 */
	public void setPromotionDiscount(BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	/**
	 * 获取优惠券折扣
	 * @return 优惠券折扣
	 */
	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	/**
	 * 设置优惠券折扣
	 * @param couponDiscount 优惠券折扣
	 */
	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	/**
	 * 获取调整金额
	 * @return 调整金额
	 */
	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	/**
	 * 设置调整金额
	 * @param offsetAmount 调整金额
	 */
	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	/**
	 * 获取已付金额
	 * @return 已付金额
	 */
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	/**
	 * 设置已付金额
	 * @param amountPaid 已付金额
	 */
	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
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
	 * 获取收货人
	 * @return 收货人
	 */
	public String getConsignee() {
		return consignee;
	}

	/**
	 * 设置收货人
	 * @param consignee 收货人
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * 获取地区名称
	 * @return 地区名称
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置地区名称
	 * @param areaName 地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 获取地址
	 * @return 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * @param address 地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取邮编
	 * @return 邮编
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * @param zipCode 邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * @return 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * @param phone 电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取是否开据发票
	 * @return 是否开据发票
	 */
	public Boolean getIsInvoice() {
		return isInvoice;
	}

	/**
	 * 设置是否开据发票
	 * @param isInvoice 是否开据发票
	 */
	public void setIsInvoice(Boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	/**
	 * 获取发票抬头
	 * @return 发票抬头
	 */
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	/**
	 * 设置发票抬头
	 * @param invoiceTitle 发票抬头
	 */
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	/**
	 * 获取税金
	 * @return 税金
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * 设置税金
	 * @param tax 税金
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * 获取附言
	 * @return 附言
	 */
	public String getMemo() {
		return memo;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	/**
	 * 预约时间
	 * @param appointment 预约时间
	 */
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	/**
	 * 设置附言
	 * @param memo 附言
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取促销
	 * @return 促销
	 */
	public String getPromotion() {
		return promotion;
	}

	/**
	 * 设置促销
	 * @param promotion 促销
	 */
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	/**
	 * 获取到期时间
	 * @return 到期时间
	 */
	public Date getExpire() {
		return expire;
	}

	/**
	 * 设置到期时间
	 * @param expire 到期时间
	 */
	public void setExpire(Date expire) {
		this.expire = expire;
	}

	/**
	 * 获取锁定到期时间
	 * @return 锁定到期时间
	 */
	public Date getLockExpire() {
		return lockExpire;
	}

	/**
	 * 设置锁定到期时间
	 * @param lockExpire 锁定到期时间
	 */
	public void setLockExpire(Date lockExpire) {
		//System.out.println(lockExpire.toString());
		this.lockExpire = lockExpire;
	}

	/**
	 * 获取是否已分配库存
	 * @return 是否已分配库存
	 */
	// public Boolean getIsAllocatedStock() {
	// return isAllocatedStock;
	// }

	/**
	 * 设置是否已分配库存
	 * @param isAllocatedStock 是否已分配库存
	 */
	// public void setIsAllocatedStock(Boolean isAllocatedStock) {
	// this.isAllocatedStock = isAllocatedStock;
	// }

	/**
	 * 获取支付方式名称
	 * @return 支付方式名称
	 */
	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	/**
	 * 设置支付方式名称
	 * @param paymentMethodName 支付方式名称
	 */
	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	/**
	 * 获取配送方式名称
	 * @return 配送方式名称
	 */
	public String getShippingMethodName() {
		return shippingMethodName;
	}

	/**
	 * 设置配送方式名称
	 * @param shippingMethodName 配送方式名称
	 */
	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	/**
	 * 获取地区
	 * @return 地区
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * @param area 地区
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * 获取支付方式
	 * @return 支付方式
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * 设置支付方式
	 * @param paymentMethod 支付方式
	 */
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * 获取配送方式
	 * @return 配送方式
	 */
	public ShippingMethod getShippingMethod() {
		return shippingMethod;
	}

	/**
	 * 设置配送方式
	 * @param shippingMethod 配送方式
	 */
	public void setShippingMethod(ShippingMethod shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	/**
	 * 获取操作员
	 * @return 操作员
	 */
	public Member getOperator() {
		return operator;
	}

	/**
	 * 设置操作员
	 * @param operator 操作员
	 */
	public void setOperator(Member operator) {
		this.operator = operator;
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

	/**
	 * 获取订单类型
	 * @return 订单类型
	 */
	public OrderType getOrderType() {
		return orderType;
	}

	/**
	 * 设置订单类型
	 * @param orderType 订单类型
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * 获取优惠码
	 * @return 优惠码
	 */
	public CouponCode getCouponCode() {
		return couponCode;
	}

	/**
	 * 设置优惠码
	 * @param couponCode 优惠码
	 */
	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
	}

	/**
	 * 获取促销方案(由团购/拍卖)
	 * @return 促销方案(由团购/拍卖)
	 */
	public Promotion getPromotionScheme() {
		return promotionScheme;
	}

	/**
	 * 设置促销方案(由团购/拍卖)
	 * @param couponCode 促销方案(由团购/拍卖)
	 */
	public void setPromotionScheme(Promotion promotionScheme) {
		this.promotionScheme = promotionScheme;
	}

	/**
	 * 获取优惠券
	 * @return 优惠券
	 */
	public List<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * 设置优惠券
	 * @param coupons 优惠券
	 */
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * 获取子订单
	 * @return 子订单
	 */
	public List<Trade> getTrades() {
		return trades;
	}

	/**
	 * 设置子订单
	 * @param trades 子订单
	 */
	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}

	/**
	 * 获取订单项
	 * @return 订单项
	 */
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	/**
	 * 设置订单项
	 * @param orderItems 订单项
	 */
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	/**
	 * 获取订单日志
	 * @return 订单日志
	 */
	public Set<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	/**
	 * 设置订单日志
	 * @param orderLogs 订单日志
	 */
	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	/**
	 * 获取预存款
	 * @return 预存款
	 */
	public Set<Deposit> getDeposits() {
		return deposits;
	}

	/**
	 * 设置预存款
	 * @param deposits 预存款
	 */
	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	/**
	 * 获取收款单
	 * @return 收款单
	 */
	public Set<Payment> getPayments() {
		return payments;
	}

	/**
	 * 设置收款单
	 * @param payments 收款单
	 */
	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	/**
	 * 获取退款单
	 * @return 退款单
	 */
	public Set<Refunds> getRefunds() {
		return refunds;
	}

	/**
	 * 设置退款单
	 * @param refunds 退款单
	 */
	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}

	/**
	 * 获取发货单
	 * @return 发货单
	 */
	public Set<Shipping> getShippings() {
		return shippings;
	}

	/**
	 * 设置发货单
	 * @param shippings 发货单
	 */
	public void setShippings(Set<Shipping> shippings) {
		this.shippings = shippings;
	}

	/**
	 * 获取退货单
	 * @return 退货单
	 */
	public Set<Returns> getReturns() {
		return returns;
	}

	/**
	 * 设置退货单
	 * @param returns 退货单
	 */
	public void setReturns(Set<Returns> returns) {
		this.returns = returns;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	/** 订单摘要 */
	public String getGridSummary() {
		StringBuffer sb = new StringBuffer();
		sb.append("购买：");
		for (OrderItem oi : getOrderItems()) {
			sb.append(oi.getFullName() + oi.getQuantity());
		}
		return sb.toString();
	}

	public OrderSource getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(OrderSource orderSource) {
		this.orderSource = orderSource;
	}

	public Boolean getIsProfitSettlement() {
		return isProfitSettlement;
	}

	public void setIsProfitSettlement(Boolean isProfitSettlement) {
		this.isProfitSettlement = isProfitSettlement;
	}

	public Member getExtension() {
		return extension;
	}

	public void setExtension(Member extension) {
	    this.extension = extension;
	}

	public Boolean canCancel() {
	    return this.getPaymentStatus().equals(PaymentStatus.unpaid) && this.getShippingStatus().equals(ShippingStatus.unshipped);
	}
	
}