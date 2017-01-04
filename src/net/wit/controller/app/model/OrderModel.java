package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;

import com.google.gson.annotations.Expose;

import net.wit.entity.CouponCode;
import net.wit.entity.Location;
import net.wit.entity.Order;

public class OrderModel extends BaseModel {
	/*等级ID*/
	private Long id;
	/*订单号*/
	private String sn;
	
	/** 支付方式 */
	private PaymentMethodModel paymentMethod;

	/** 配送方式 */
	private ShippingMethodModel shippingMethod;
	
	/** 收货地址 */
	private ReceiverModel receiverModel;

	/** 是否开据发票 */
	private Boolean isInvoice;

	/** 发票抬头 */
	private String invoiceTitle;

	/** 税金 */
	private BigDecimal tax;
	/** 运费 */
	private BigDecimal freight;
	/** 促销折扣 */
	private BigDecimal promotionDiscount;
	/** 优惠券折扣 */
	private BigDecimal couponDiscount;
	/** 调整金额 */
	private BigDecimal offsetAmount;
	/** 订单合计 */
	private BigDecimal amount;
	/** 应付合计 */
	private BigDecimal amountPayable;
	/** 平台活动立减折扣 */
	private BigDecimal discount;
	
	/** 赠送积分 */
	private Long point;

	/** 附言 */
	private String memo;

	/** 会员 */
	private MemberModel member;
	
	private List<TradeModel> trades;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public PaymentMethodModel getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethodModel paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public ShippingMethodModel getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(ShippingMethodModel shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public ReceiverModel getReceiverModel() {
		return receiverModel;
	}

	public void setReceiverModel(ReceiverModel receiverModel) {
		this.receiverModel = receiverModel;
	}

	public Boolean getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}

	public void setPromotionDiscount(BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public BigDecimal getOffsetAmount() {
		return offsetAmount;
	}

	public void setOffsetAmount(BigDecimal offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public MemberModel getMember() {
		return member;
	}

	public void setMember(MemberModel member) {
		this.member = member;
	}
	
	public List<TradeModel> getTrades() {
		return trades;
	}

	public void setTrades(List<TradeModel> trades) {
		this.trades = trades;
	}

	public BigDecimal getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(BigDecimal amountPayable) {
		this.amountPayable = amountPayable;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public void copyFrom(Order order) {
		this.id = order.getId();
		this.amount = order.getAmount();
		this.couponDiscount = order.getCouponDiscount();
		this.discount = order.getDiscount();
		this.freight = order.getFreight();
		this.invoiceTitle = order.getInvoiceTitle();
		this.isInvoice = order.getIsInvoice();
		MemberModel member = new MemberModel();
		member.copyFrom(order.getMember());
		this.member = member;
		this.memo = order.getMemo();
		this.offsetAmount = order.getOffsetAmount();
		if (order.getPaymentMethod()!=null) {
			PaymentMethodModel paymentMethod = new PaymentMethodModel();
			paymentMethod.copyFrom(order.getPaymentMethod());
			this.paymentMethod = paymentMethod;
		}
		if (order.getShippingMethod()!=null) {
			ShippingMethodModel shippingMethod = new ShippingMethodModel();
			shippingMethod.copyFrom(order.getShippingMethod());
			this.shippingMethod = shippingMethod;
		}
		this.point = order.getPoint();
		this.promotionDiscount = order.getPromotionDiscount();
		if (order.getReceiver()!=null) {
			ReceiverModel receiver = new ReceiverModel();
			receiver.copyFrom(order.getReceiver());
			this.receiverModel = receiver;
		}
		this.sn = order.getSn();
		this.tax = order.getTax();
	    this.amountPayable = order.getAmountPayable();
		this.trades = TradeModel.bindData(order.getTrades());
	}

	public void copyFrom(Order order, Location location, List<CouponCode> codes) {
		this.id = order.getId();
		this.amount = order.getAmount();
		this.couponDiscount = order.getCouponDiscount();
		this.discount = order.getDiscount();
		this.freight = order.getFreight();
		this.invoiceTitle = order.getInvoiceTitle();
		this.isInvoice = order.getIsInvoice();
		MemberModel member = new MemberModel();
		member.copyFrom(order.getMember());
		this.member = member;
		this.memo = order.getMemo();
		this.offsetAmount = order.getOffsetAmount();
		if (order.getPaymentMethod()!=null) {
			PaymentMethodModel paymentMethod = new PaymentMethodModel();
			paymentMethod.copyFrom(order.getPaymentMethod());
			this.paymentMethod = paymentMethod;
		}
		if (order.getShippingMethod()!=null) {
			ShippingMethodModel shippingMethod = new ShippingMethodModel();
			shippingMethod.copyFrom(order.getShippingMethod());
			this.shippingMethod = shippingMethod;
		}
		this.point = order.getPoint();
		this.promotionDiscount = order.getPromotionDiscount();
		if (order.getReceiver()!=null) {
			ReceiverModel receiver = new ReceiverModel();
			receiver.copyFrom(order.getReceiver());
			this.receiverModel = receiver;
		}
		this.sn = order.getSn();
		this.tax = order.getTax();
		this.amountPayable = order.getAmountPayable();
		this.trades=TradeModel.bindData(order.getTrades(),location,codes);
	}
	
}
