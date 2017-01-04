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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.Setting;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.PaymentMethod.Method;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.support.FinalOrderStatus;
import net.wit.support.FinalOrderStatus.Status;
import net.wit.util.SettingUtils;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: Trade
 * @Description: 子订单
 * @author Administrator
 * @date 2014年10月14日 上午9:12:16
 */
@Entity
@Table(name = "xx_trade")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_trade_sequence")
public class Trade extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 订单名称分隔符 */
	private static final String NAME_SEPARATOR = " ";

	/** 配送验证码 **/
	@Expose
	@JsonProperty
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	private String sn;

	/** 配送状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private ShippingStatus shippingStatus;

	/** 支付状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private PaymentStatus paymentStatus;

	/** 是否已分配库存 */
	@Column(nullable = false)
	private Boolean isAllocatedStock;

	/** 支付手续费 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal fee;

	/** 订单状态 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private OrderStatus orderStatus;

	/** 附言 */
	@Length(max = 200)
	private String memo;

	/** 结算状态 */
	private Boolean clearing;

	/** 结算日期 */
	private Date clearingDate;

	/** 结算到供应商状态 */
	@Column(nullable = false)
	private Boolean suppliered;

	/** 结算到供应商时间 */
	private Date supplierDate;

	/** 安全密匙 */
	@Embedded
	private SafeKey safeKey;

	/** 商家 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonProperty
	@JoinColumn(nullable = false, updatable = false)
	private Tenant tenant;

	/** 预计提货时间 */
	@Expose
	@JsonProperty
	private Date deliveryDate;

	/** 最终发货时间 */
	@Expose
	@JsonProperty
	private Date shippingDate;

	/** 最终收货时间 */
	private Date confirmDate;

	/** 确认收货截止时间 */
	private Date confirmDueDate;

	/** 完成日期 */
	private Date completedDate;

	/** 申请延期申请 */
	private boolean delayPermit;

	/** 商家优惠码 */
	@OneToOne(fetch = FetchType.LAZY)
	private CouponCode tenantCouponCode;

	/** 平台优惠码 */
	@OneToOne(fetch = FetchType.LAZY)
	private CouponCode couponCode;

	/** 税金 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal tax;

	/** 赠送积分 */
	@NotNull
	@Min(0)
	@Column(nullable = false)
	private Long point;

	/** 运费 */
	@Expose
	@NotNull
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal freight;

	/** 促销折扣 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal promotionDiscount;

	/** 商家优惠券抵扣 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal couponDiscount;

	/** 平台优惠券抵扣 */
	@Column(nullable = false, updatable = false, precision = 21, scale = 6)
	private BigDecimal discount;

	/** 调整金额 */
	@NotNull
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal offsetAmount;

	/** 联盟佣金 */
	@NotNull
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal agencyAmount;

	/** 推广佣金 */
	@NotNull
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal totalProfit;

	/** 平台佣金 */
	@NotNull
	@Column(nullable = false, precision = 21, scale = 6, columnDefinition = "decimal default 0")
	private BigDecimal providerAmount;

	/** 商品 */
	@OneToMany(mappedBy = "memberTrade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Review> reviews = new HashSet<Review>();

	/** 操作员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member operator;

	/** 锁定到期时间 */
	private Date lockExpire;

	/** 打印时间 */
	private Date printDate;
	/** 打印次数 */
	private Integer print;

	/** 订单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders", nullable = false, updatable = false)
	private Order order;

	/** 下单设备 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Equipment equipment;

	/** 订单项 */
	@Expose
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "trade", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("isGift asc")
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	/** 分润 */
	@Expose
	@Valid
	@OneToMany(mappedBy = "trade", fetch = FetchType.LAZY)
	private List<Rebate> rebate = new ArrayList<Rebate>();

	/** 发货单 */
	@OneToMany(mappedBy = "trade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Shipping> shippings = new HashSet<Shipping>();

	/** 退款单 */
	@OneToMany(mappedBy = "trade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Refunds> refunds = new HashSet<Refunds>();

	/** 退货单 */
	@OneToMany(mappedBy = "trade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<Returns> returns = new HashSet<Returns>();

	/** 退货申请单 */
	@OneToMany(mappedBy = "trade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("createDate asc")
	private Set<SpReturns> spReturns = new HashSet<SpReturns>();

	/** 订单日志 */
	@OneToMany(mappedBy = "trade", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("id desc")
	private Set<OrderLog> orderLogs = new HashSet<OrderLog>();

	/** 提货门店 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryCenter deliveryCenter;

	/**
	 * 判断是否已锁定
	 * @param operator 操作员
	 * @return 是否已锁定
	 */
	public boolean isLocked(Member operator) {
		return getLockExpire() != null && new Date().before(getLockExpire()) && ((operator != null && !operator.equals(getOperator())) || (operator == null && getOperator() != null));
	}

	/** 获取订单名称 */
	@JsonProperty
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

	/** 计算税金 */
	public BigDecimal calculateTax() {
		BigDecimal tax = new BigDecimal(0);
		Setting setting = SettingUtils.get();
		if (setting.getIsTaxPriceEnabled()) {
			BigDecimal amount = getPrice();
			if (getPromotionDiscount() != null) {
				amount = amount.subtract(getPromotionDiscount());
			}
			if (getOffsetAmount() != null) {
				amount = amount.add(getOffsetAmount());
			}
			tax = amount.multiply(new BigDecimal(setting.getTaxRate().toString()));
		}
		return setting.setScale(tax);
	}

	/** 获取折扣价 */
	public BigDecimal calcPromotionDiscount() {
		BigDecimal discount = BigDecimal.ZERO;
		for (OrderItem orderItem : getOrderItems()) {
			discount = discount.add(orderItem.getDiscount());
		}
		return discount;
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

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public CouponCode getTenantCouponCode() {
		return tenantCouponCode;
	}

	public void setTenantCouponCode(CouponCode tenantCouponCode) {
		this.tenantCouponCode = tenantCouponCode;
	}

	public CouponCode getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(CouponCode couponCode) {
		this.couponCode = couponCode;
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

	/** 获取商品数量 */
	@JsonProperty
	public int getQuantity(boolean flag) {
		int quantity = 0;
		if (getOrderItems(flag) != null) {
			for (OrderItem orderItem : getOrderItems(flag)) {
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

	/** 获取商品价格 */
	@JsonProperty
	public BigDecimal getPrice() {
		BigDecimal price = BigDecimal.ZERO;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getSubtotal() != null) {
					price = price.add(orderItem.getSubtotal());
				}
			}
		}
		return price;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public BigDecimal getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}

	/** 获取商品成本 */
	@JsonProperty
	public BigDecimal getCost() {
		BigDecimal cost = BigDecimal.ZERO;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && !orderItem.getIsGift() && orderItem.getCost() != null) {
					cost = cost.add(orderItem.getCost().multiply(new BigDecimal(orderItem.getQuantity())));
				}
			}
		}
		return cost;
	}
	/** 获取订单的结算金额 */
	@JsonProperty
	public BigDecimal getSettle() {
		BigDecimal cost = BigDecimal.ZERO;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && !orderItem.getIsGift() && orderItem.getCost() != null) {
					cost = cost.add(orderItem.getCost().multiply(new BigDecimal(orderItem.getShippedQuantity().intValue())));
				}
			}
		}
		return cost;
	}

	public Set<SpReturns> getSpReturns() {
		return spReturns;
	}

	public void setSpReturns(Set<SpReturns> spReturns) {
		this.spReturns = spReturns;
	}

	/** 是否退货申请中 */
	public boolean isSpReturns() {
		for (SpReturns returns : getSpReturns()) { //正在退货
			if (returns.getReturnStatus().equals(ReturnStatus.unconfirmed) || returns.getReturnStatus().equals(ReturnStatus.confirmed) ) {
				return false;
			}
		}
		return true;
	}
	/** 申请退货状态 */
	public String getReturnsStatus(){
		if(getSpReturns().size()<=0){
			return "null";
		}else{
			List<SpReturns> spReturn=new ArrayList<>();
			Iterator<SpReturns> iterator=getSpReturns().iterator();
			while(iterator.hasNext()){
				spReturn.add(iterator.next());
			}
			SpReturns spReturns=spReturn.get(spReturn.size()-1);
			if(spReturns.getReturnStatus()==ReturnStatus.unconfirmed
					||spReturns.getReturnStatus()==ReturnStatus.audited
					||spReturns.getReturnStatus()==ReturnStatus.confirmed){
				return "returning";
			}else if(spReturns.getReturnStatus()==ReturnStatus.completed){
				return "returned";
			}else{
				return "refused";
			}
		}
	}
	public boolean isComplete() {
		if (this.shippingStatus == ShippingStatus.returned || this.paymentStatus == PaymentStatus.refunded || this.shippingStatus == ShippingStatus.accept || this.orderStatus == OrderStatus.completed) {
			return true;
		}
		return false;
	}

	/** 订单显示状态 */
	@JsonProperty
	public List<FinalOrderStatus> getFinalOrderStatus() {
		ArrayList<FinalOrderStatus> finalOrderStatus = new ArrayList<FinalOrderStatus>();
		if ((getOrderStatus() == OrderStatus.unconfirmed && getPaymentStatus().equals(PaymentStatus.unpaid) && getShippingStatus().equals(ShippingStatus.unshipped) && order.isExpired())) {
			FinalOrderStatus orderStatus = new FinalOrderStatus();
			orderStatus.setDesc("已过期");
			orderStatus.setStatus(Status.isExpired);
			finalOrderStatus.add(orderStatus);
		} else {
			if (getOrderStatus() == OrderStatus.cancelled) {
				FinalOrderStatus orderStatus = new FinalOrderStatus();
				orderStatus.setDesc("已取消");
				orderStatus.setStatus(Status.cancelled);
				finalOrderStatus.add(orderStatus);
			} else if (getOrderStatus() == OrderStatus.completed) {
				if (getMemberReview() == null) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					if (shippingStatus.equals(ShippingStatus.returned))
					{
						orderStatus.setDesc("已退货");
					} else if (paymentStatus.equals(PaymentStatus.refunded))
					{
						orderStatus.setDesc("已退款");
					} else {
						orderStatus.setDesc("已签收");
					}
					orderStatus.setStatus(Status.toReview);
					finalOrderStatus.add(orderStatus);
				} else {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					if (shippingStatus.equals(ShippingStatus.returned))
					{
						orderStatus.setDesc("已退货");
					} else if (paymentStatus.equals(PaymentStatus.refunded))
					{
						orderStatus.setDesc("已退款");
					} else {
						orderStatus.setDesc("已签收");
					}
					orderStatus.setStatus(Status.completed);
					finalOrderStatus.add(orderStatus);
				}
			} else /** if (getOrderStatus() == OrderStatus.unconfirmed) {
			 FinalOrderStatus orderStatus = new FinalOrderStatus();
			 orderStatus.setDesc("待付款");
			 orderStatus.setStatus(Status.waitPay);
			 finalOrderStatus.add(orderStatus);
			 } else **/
			{
				if (paymentStatus == PaymentStatus.unpaid && getOrder().getPaymentMethod().getMethod().equals(Method.online)) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待付款");
					orderStatus.setStatus(Status.waitPay);
					finalOrderStatus.add(orderStatus);
				} else
				if (orderStatus == OrderStatus.unconfirmed) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待确认");
					orderStatus.setStatus(Status.unconfirmed);
					finalOrderStatus.add(orderStatus);
				} else
				if (paymentStatus == PaymentStatus.refundApply) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待退款");
					orderStatus.setStatus(Status.waitReturn);
					finalOrderStatus.add(orderStatus);
				} else
				if (shippingStatus == ShippingStatus.shippedApply) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待退货");
					orderStatus.setStatus(Status.waitReturn);
					finalOrderStatus.add(orderStatus);
				} else
				if (shippingStatus == ShippingStatus.unshipped || shippingStatus == ShippingStatus.partialShipment) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待发货");
					orderStatus.setStatus(Status.waitShipping);
					finalOrderStatus.add(orderStatus);
				} else if (shippingStatus == ShippingStatus.shipped) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待签收");
					orderStatus.setStatus(Status.sign);
					finalOrderStatus.add(orderStatus);
				} else if (shippingStatus == ShippingStatus.shippedApply) {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待退货");
					orderStatus.setStatus(Status.waitReturn);
					finalOrderStatus.add(orderStatus);
				} else if (shippingStatus == ShippingStatus.partialReturns){
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("待退货");
					orderStatus.setStatus(Status.waitReturn);
					finalOrderStatus.add(orderStatus);
				} else {
					FinalOrderStatus orderStatus = new FinalOrderStatus();
					orderStatus.setDesc("已签收");
					orderStatus.setStatus(Status.completed);
					finalOrderStatus.add(orderStatus);
				}
			}
		}
		return finalOrderStatus;
	}

	/** 获取订单金额 */
	@JsonProperty
	public BigDecimal getAmount() {
		BigDecimal amount = getPrice();
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getPromotionDiscount() != null) {
			amount = amount.subtract(getPromotionDiscount());
		}
		if (getTax() != null) {
			amount = amount.add(getTax());
		}
		if (getDiscount() != null) {
			amount = amount.subtract(getDiscount());
			amount = amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
		}
		if (getFreight() != null) {
			amount = amount.add(getFreight());
		}
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
	}


	/** 获取订单结算金额，包含平台活动的补贴 */
	@JsonProperty
	public BigDecimal getClearingAmount() {
		BigDecimal amount = getPrice();
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getPromotionDiscount() != null) {
			amount = amount.subtract(getPromotionDiscount());
		}
		if (getTax() != null) {
			amount = amount.add(getTax());
		}
		if (getFreight() != null) {
			amount = amount.add(getFreight());
		}
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
	}

	public BigDecimal getAgencyAmount() {
		if (agencyAmount==null) {
			return BigDecimal.ZERO;
		} else {
			return agencyAmount;
		}
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	/** 获取分润金额 */
	@JsonProperty
	public BigDecimal calcTotalProfit() {
		Setting setting = SettingUtils.get();
		BigDecimal amount = getPrice();
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getPromotionDiscount() != null) {
			amount = amount.subtract(getPromotionDiscount());
		}
		if (getDiscount() != null) {
			amount = amount.subtract(getDiscount());
			amount = amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
		}

		BigDecimal profit = amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);

		profit = profit.multiply(getTenant().getGeneralize());
		return setting.setScale(profit);
	}

	/** 获取联盟佣金 */
	@JsonProperty
	public BigDecimal calcAgencyAmount() {
		Setting setting = SettingUtils.get();
		BigDecimal amount = getPrice();
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getPromotionDiscount() != null) {
			amount = amount.subtract(getPromotionDiscount());
		}

		BigDecimal agency = amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);

		agency = agency.multiply(getTenant().getAgency());
		return setting.setScale(agency);
	}


	/** 获取平台佣金 */
	@JsonProperty
	public BigDecimal calcProviderAmount() {
		Setting setting = SettingUtils.get();
		BigDecimal amount = getPrice();
		if (getOffsetAmount() != null) {
			amount = amount.add(getOffsetAmount());
		}
		if (getCouponDiscount() != null) {
			amount = amount.subtract(getCouponDiscount());
		}
		if (getPromotionDiscount() != null) {
			amount = amount.subtract(getPromotionDiscount());
		}
		if (getDiscount() != null) {
			amount = amount.subtract(getDiscount());
			amount = amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
		}
		BigDecimal agency = amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
		agency = agency.multiply(getTenant().getBrokerage());
		return setting.setScale(agency);
	}

	/** 获取有效商品价格 */
	@JsonProperty
	public BigDecimal getEffectivePrice() {
		BigDecimal effectivePrice = getPrice().subtract(getDiscount());
		return effectivePrice.compareTo(BigDecimal.ZERO) > 0 ? effectivePrice : BigDecimal.ZERO;
	}

	/** 实发商品金额 */
	public BigDecimal getRealHairAmount() {
		BigDecimal price = BigDecimal.ZERO;
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getRealHairSubtotal() != null) {
					price = price.add(orderItem.getRealHairSubtotal());
				}
			}
		}
		return price;
	}

	/** 退款金额 */
	public BigDecimal getRefundAmount() {
		BigDecimal amount = BigDecimal.ZERO;
		for (Refunds refund : getRefunds()) {
			amount = amount.add(refund.getAmount());
		}
		return amount;
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
	public List<OrderItem> getOrderItems(Boolean isGift) {
		List<OrderItem> items = new ArrayList<OrderItem>();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getIsGift().equals(isGift)) {
					items.add(orderItem);
				}
			}
		}
		return items;
	}

	/**
	 * 获取赠品
	 * @return 订单项
	 */
	public List<OrderItem> getGiftItems() {
		List<OrderItem> items = new ArrayList<OrderItem>();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getIsGift().equals(true)) {
					items.add(orderItem);
				}
			}
		}
		return items;
	}

	/**
	 * 获取促销购物车项
	 * @param promotion 促销
	 * @return 促销购物车项
	 */
	@JsonProperty
	private Set<OrderItem> getPromotionItems(Promotion promotion) {
		Set<OrderItem> orderItems = new HashSet<OrderItem>();
		if (promotion != null && getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getProduct() != null && orderItem.getProduct().isValid(promotion)) {
					orderItems.add(orderItem);
				}
			}
		}
		return orderItems;
	}

	/**
	 * 获取促销商品总数量
	 * @param promotion 促销
	 * @return 促销商品总数量
	 */
	private int getQuantity(Promotion promotion) {
		int quantity = 0;
		for (OrderItem orderItem : getPromotionItems(promotion)) {
			if (orderItem != null && orderItem.getQuantity() != null) {
				quantity += orderItem.calculateQuantity().intValue();
			}
		}
		return quantity;
	}

	/**
	 * 获取促销商品总价格
	 * @param promotion 促销
	 * @return 促销商品价格
	 */
	private BigDecimal getPrice(Promotion promotion) {
		BigDecimal price = BigDecimal.ZERO;
		for (OrderItem orderItem : getPromotionItems(promotion)) {
			if (orderItem != null && orderItem.getSubtotal() != null) {
				price = price.add(orderItem.getSubtotal());
			}
		}
		return price;
	}

	/**
	 * 判断促销是否有效
	 * @param promotion 促销
	 * @return 促销是否有效
	 */
	public boolean isValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
			return false;
		}
		Integer quantity = getQuantity(promotion);
		if ((promotion.getMinimumQuantity() != null && promotion.getMinimumQuantity() > quantity) || (promotion.getMaximumQuantity() != null && promotion.getMaximumQuantity() < quantity)) {
			return false;
		}
		BigDecimal price = getPrice(promotion);
		if ((promotion.getMinimumPrice() != null && promotion.getMinimumPrice().compareTo(price) > 0) || (promotion.getMaximumPrice() != null && promotion.getMaximumPrice().compareTo(price) < 0)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断促销是否有效
	 * @param promotion 促销
	 * @return 促销是否有效
	 */
	public boolean isMailValid(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
			return false;
		}
		BigDecimal price = getEffectivePrice();
		if ((promotion.getMinimumPrice() != null && promotion.getMinimumPrice().compareTo(price) > 0) || (promotion.getMaximumPrice() != null && promotion.getMaximumPrice().compareTo(price) < 0)) {
			return false;
		}
		return true;
	}
	/**
	 * 判断优惠券是否有效
	 * @param coupon 优惠券
	 * @return 优惠券是否有效
	 */
	public boolean isValid(Coupon coupon) {
		if (coupon == null || !coupon.getIsEnabled() ) {
			return false;
		}
		if ((coupon.getMinimumQuantity() != null && coupon.getMinimumQuantity() > getQuantity()) || (coupon.getMaximumQuantity() != null && coupon.getMaximumQuantity() < getQuantity())) {
			return false;
		}
		if ((coupon.getMinimumPrice() != null && coupon.getMinimumPrice().compareTo(getEffectivePrice()) > 0) || (coupon.getMaximumPrice() != null && coupon.getMaximumPrice().compareTo(getEffectivePrice()) < 0)) {
			return false;
		}
		return true;
	}

	/** 获取促销 */
	public Set<Promotion> getPromotions() {
		Set<Promotion> allPromotions = new HashSet<Promotion>();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getProduct() != null) {
					allPromotions.addAll(orderItem.getProduct().getValidPromotions());
				}
			}
		}
		Set<Promotion> promotions = new TreeSet<Promotion>();
		for (Promotion promotion : allPromotions) {
			if (isValid(promotion)) {
				promotions.add(promotion);
			}
		}
		return promotions;
	}

	/** 获取包邮方案 */
	public Set<Promotion> getMailPromotions() {
		Set<Promotion> allPromotions = getTenant().getMailPromotions();
		Set<Promotion> promotions = new TreeSet<Promotion>();
		for (Promotion promotion : allPromotions) {
			if (isMailValid(promotion)) {
				promotions.add(promotion);
			}
		}
		return promotions;
	}

	@JsonProperty
	public String getPreThumbnail() {
		String thumbnail = "";
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getProduct() != null) {
					thumbnail = orderItem.getProduct().getThumbnail();
					break;
				}
			}
		}
		return thumbnail;
	}

	@JsonProperty
	public String getPreFullName() {
		String fullName = "";
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null) {
					fullName = orderItem.getFullName();
					break;
				}
			}
		}
		return fullName;
	}

	@JsonProperty
	public List<String> getPreSpecification_value() {
		List<String> specification_value = new ArrayList<String>();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getProduct() != null) {
					specification_value = orderItem.getProduct().getSpecification_value();
					break;
				}
			}
		}
		return specification_value;
	}

	@JsonProperty
	public BigDecimal getPrePrice() {
		BigDecimal price = new BigDecimal("0");
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null) {
					price = orderItem.getPrice();
					break;
				}
			}
		}
		return price;
	}

	@JsonProperty
	public Integer getPreQuantity() {
		Integer quantity = new Integer(0);
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null) {
					quantity = orderItem.getQuantity();
					break;
				}
			}
		}
		return quantity;
	}

	// ===========================================getter/setter===========================================//

	/**
	 * 获取配送状态
	 * @return 配送状态
	 */
	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * 设置配送状态
	 * @param shippingStatus 配送状态
	 */
	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	/**
	 * 获取商家
	 * @return 商家
	 */
	@JsonProperty
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * 设置商家
	 * @param member 商家
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	/**
	 * 获取订单
	 * @return 订单
	 */
	@JsonProperty
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
	 * 获取安全密匙
	 * @return 安全密匙
	 */
	public SafeKey getSafeKey() {
		return safeKey;
	}

	/**
	 * 设置安全密匙
	 * @param safeKey 安全密匙
	 */
	public void setSafeKey(SafeKey safeKey) {
		this.safeKey = safeKey;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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
		this.lockExpire = lockExpire;
	}

	/**
	 * 获取最终发货时间
	 * @return 最终发货时间
	 */
	public Date getShippingDate() {
		return shippingDate;
	}

	/**
	 * 设置最终发货时间
	 * @param shippingDate 最终发货时间
	 */
	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}
	/**
	 * 获取确认收货截止时间
	 * @return 确认收货截止时间
	 */
	public Date getConfirmDueDate() {
		return confirmDueDate;
	}

	/**
	 * 设置确认收货截止时间
	 * @param lockExpire 确认收货截止时间
	 */
	public void setConfirmDueDate(Date confirmDueDate) {
		this.confirmDueDate = confirmDueDate;
	}

	public Boolean getSuppliered() {
		return suppliered;
	}

	public void setSuppliered(Boolean suppliered) {
		this.suppliered = suppliered;
	}

	public Date getSupplierDate() {
		return supplierDate;
	}

	public void setSupplierDate(Date supplierDate) {
		this.supplierDate = supplierDate;
	}

	/**
	 * 获取申请延期申请
	 * @return 申请延期申请
	 */
	public boolean isDelayPermit() {
		return delayPermit;
	}

	/**
	 * 设置申请延期申请
	 * @param delayPermit 申请延期申请
	 */
	public void setDelayPermit(boolean delayPermit) {
		this.delayPermit = delayPermit;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	/**
	 * 获取买家评价
	 * @return 买家评价
	 */
	@JsonProperty
	public Review getMemberReview() {
		if (getReviews()!=null && getReviews().size()>0) {
			return getReviews().iterator().next();
		} else {
			return null;
		}
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

	/**
	 * 获取是否已分配库存
	 * @return 是否已分配库存
	 */
	public Boolean getIsAllocatedStock() {
		return isAllocatedStock;
	}

	/**
	 * 设置是否已分配库存
	 * @param isAllocatedStock 是否已分配库存
	 */
	public void setIsAllocatedStock(Boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
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
	 * 获取调整金额
	 * @return 调整金额
	 */
	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	/**
	 * 设置调整金额
	 * @param offsetAmount 调整金额
	 */
	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	public Boolean getClearing() {
		return clearing;
	}

	public void setClearing(Boolean clearing) {
		this.clearing = clearing;
	}

	public Date getClearingDate() {
		return clearingDate;
	}

	public void setClearingDate(Date clearingDate) {
		this.clearingDate = clearingDate;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Boolean canCancel() {
		return this.getPaymentStatus().equals(PaymentStatus.unpaid) && this.getShippingStatus().equals(ShippingStatus.unshipped);
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Set<OrderLog> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(Set<OrderLog> orderLogs) {
		this.orderLogs = orderLogs;
	}

	public BigDecimal getProviderAmount() {
		return providerAmount;
	}

	public void setProviderAmount(BigDecimal providerAmount) {
		this.providerAmount = providerAmount;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	/** 获取供应商 */
	public Set<Tenant> getSuppliers() {
		Set<Tenant> tenants = new HashSet<Tenant>();
		for (OrderItem orderItem : getOrderItems()) {

			if(orderItem.getSupplier()!=null){
				if (!tenants.contains(orderItem.getSupplier())) {
					tenants.add(orderItem.getSupplier());
				}
			}

		}
		return tenants;
	}

	/**
	 * 获取商家购物车项--不含赠品
	 */
	@JsonProperty
	public Set<OrderItem> getEffectiveOrderItems() {
		Set<OrderItem> orderItems = new HashSet<OrderItem>();
		if (getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && !orderItem.getIsGift()) {
					orderItems.add(orderItem);
				}
			}
		}
		return orderItems;
	}
	/**
	 * 获取商家购物车项
	 */
	@JsonProperty
	public Set<OrderItem> getOrderItems(Tenant supplier) {
		Set<OrderItem> orderItems = new HashSet<OrderItem>();
		if (supplier != null && getOrderItems() != null) {
			for (OrderItem orderItem : getOrderItems()) {
				if (orderItem != null && orderItem.getSupplier().equals(supplier)) {
					orderItems.add(orderItem);
				}
			}
		}
		return orderItems;
	}

	/** 获取商品成本 */
	public BigDecimal calcAllCost() {
		BigDecimal cost = BigDecimal.ZERO;
		for (OrderItem orderItem : getOrderItems()) {
			if (orderItem != null && orderItem.getCost() != null && orderItem.getSupplier()!=null) {
				cost = cost.add(orderItem.getCost().multiply(new BigDecimal(orderItem.getQuantity())));
			}
		}
		return cost;
	}

	/** 获取商品成本 */
	public BigDecimal calcCost(Tenant supplier) {
		BigDecimal cost = BigDecimal.ZERO;
		for (OrderItem orderItem : getOrderItems(supplier)) {
			if (orderItem != null && orderItem.getCost() != null) {
				cost = cost.add(orderItem.getCost().multiply(new BigDecimal(orderItem.getShippedQuantity())));
			}
		}
		return cost;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Integer getPrint() {
		return print;
	}

	public void setPrint(Integer print) {
		this.print = print;
	}

	public List<Rebate> getRebate() {
		return rebate;
	}

	public void setRebate(List<Rebate> rebate) {
		this.rebate = rebate;
	}

}