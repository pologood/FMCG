package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.AreaModel;
import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.*;
import net.wit.controller.assistant.model.MemberModel;
import net.wit.controller.assistant.model.PaymentMethodModel;
import net.wit.controller.assistant.model.ReceiverModel;
import net.wit.controller.assistant.model.ShippingMethodModel;
import net.wit.controller.assistant.model.TenantListModel;
import net.wit.entity.CouponCode;
import net.wit.entity.Location;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Trade;
import net.wit.support.FinalOrderStatus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradeModel extends BaseModel {
	/**等级ID**/
	private Long id;
	/**订单号**/
	private String sn;
	/**提货码**/
	private String pickUpCode;
	/** 配送状态 */
	private ShippingStatus shippingStatus;
	/** 支付状态 */
	private PaymentStatus paymentStatus;
	/** 订单状态 */
	private OrderStatus orderStatus;
	private FinalOrderStatus finalOrderStatus;
	/** 支付方式 */
	private PaymentMethodModel paymentMethod;
	/** 配送方式 */
	private ShippingMethodModel shippingMethod;
	/** 收货地址 */
	private ReceiverModel receiverModel;
	/** 预计提货时间 */
	private Date deliveryDate;
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
	/** 平台活动立减折扣 */
	private BigDecimal discount;
	/**制单日期**/
	private Date create_date;
	/**支付时间*/
	private Date pay_date;
	/** 附言 */
	private String memo;
	/** 会员 */
	private SingleModel member;

	private  OrderDescModel descModel;

	private List<OrderItemModel> orderItems;

	/**提货门店*/
	private String deliveryCenter;
	/**快递公司*/
	private String deliveryCorpName;
	/**物流单号*/
	private String trackingNo;

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

	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
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

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public SingleModel getMember() {
		return member;
	}

	public void setMember(SingleModel member) {
		this.member = member;
	}

	public List<OrderItemModel> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemModel> orderItems) {
		this.orderItems = orderItems;
	}

	public FinalOrderStatus getFinalOrderStatus() {
		return finalOrderStatus;
	}

	public void setFinalOrderStatus(FinalOrderStatus finalOrderStatus) {
		this.finalOrderStatus = finalOrderStatus;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getPickUpCode() {
		return pickUpCode;
	}

	public void setPickUpCode(String pickUpCode) {
		this.pickUpCode = pickUpCode;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Date getPay_date() {
		return pay_date;
	}

	public void setPay_date(Date pay_date) {
		this.pay_date = pay_date;
	}

	public String getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(String deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public OrderDescModel getDescModel() {
		return descModel;
	}

	public void setDescModel(OrderDescModel descModel) {
		this.descModel = descModel;
	}

	public String getDeliveryCorpName() {
		return deliveryCorpName;
	}

	public void setDeliveryCorpName(String deliveryCorpName) {
		this.deliveryCorpName = deliveryCorpName;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public void copyFrom(Trade trade) {
		this.id = trade.getId();
		this.amount = trade.getAmount();
		this.couponDiscount = trade.getCouponDiscount();
		this.freight = trade.getFreight();
		SingleModel member = new SingleModel();
		member.setId(trade.getOrder().getMember().getId());
		member.setName(trade.getOrder().getMember().getName());
		this.member = member;
		this.memo = trade.getMemo();
		this.offsetAmount = trade.getOffsetAmount();
		if (trade.getOrder().getPaymentMethod()!=null) {
		   PaymentMethodModel paymentMethod = new PaymentMethodModel();
		   paymentMethod.copyFrom(trade.getOrder().getPaymentMethod());
		   this.paymentMethod = paymentMethod;
		}
		if (trade.getOrder().getShippingMethod()!=null) {
		   ShippingMethodModel shippingMethod = new ShippingMethodModel();
		   shippingMethod.copyFrom(trade.getOrder().getShippingMethod());
		   this.shippingMethod = shippingMethod;
		}
		if(trade.getDeliveryCenter()!=null){
			this.deliveryCenter = trade.getDeliveryCenter().getName();
		}
		if(trade.getShippings().size()>0){
			this.deliveryCorpName = trade.getShippings().iterator().next().getDeliveryCorp();
			this.trackingNo = trade.getShippings().iterator().next().getTrackingNo();
		}
		this.promotionDiscount = trade.getPromotionDiscount();
		this.discount = trade.getDiscount();
    	ReceiverModel receiver = new ReceiverModel();
 	    receiver.setAddress(trade.getOrder().getAddress());
 	    AreaModel area = new AreaModel();
 	    if (trade.getOrder().getArea()!=null) {
 	        area.copyFrom(trade.getOrder().getArea());
 	    } else {
 	    	area.setFullName(trade.getOrder().getAreaName());
 	    	area.setName(trade.getOrder().getAreaName());
 	    }
 	    receiver.setArea(area);
 	    receiver.setConsignee(trade.getOrder().getConsignee());
 	    receiver.setPhone(trade.getOrder().getPhone());
 	    receiver.setZipCode(trade.getOrder().getZipCode());
	    this.receiverModel = receiver;
		this.deliveryDate= trade.getDeliveryDate();
		this.sn = trade.getOrder().getSn();
		this.orderStatus = trade.getOrderStatus();
		this.shippingStatus = trade.getShippingStatus();
		this.paymentStatus = trade.getPaymentStatus();
		this.finalOrderStatus = trade.getFinalOrderStatus().get(0);
		OrderDescModel model = new OrderDescModel();
		if(FinalOrderStatus.Status.waitPay == finalOrderStatus.getStatus()){
			model.setDesc("买家已下单，等待付款中");
			model.setStatus(FinalOrderStatus.Status.waitPay);
		}else if(FinalOrderStatus.Status.unconfirmed == finalOrderStatus.getStatus()){
			model.setDesc("买家已付款，等待商家确认");
			model.setStatus(FinalOrderStatus.Status.unconfirmed);
		}else if(FinalOrderStatus.Status.waitShipping == finalOrderStatus.getStatus()){
			model.setDesc("商家已确认，请立即发货");
			model.setStatus(FinalOrderStatus.Status.waitShipping);
		}else if(FinalOrderStatus.Status.sign == finalOrderStatus.getStatus()){
			model.setDesc("商家已发货,等待买家签收");
			model.setStatus(FinalOrderStatus.Status.sign);
		}else if(FinalOrderStatus.Status.waitReturn == finalOrderStatus.getStatus()){
			model.setDesc("买家已申请退货，请立即处理");
			model.setStatus(FinalOrderStatus.Status.waitReturn);
		}else if(FinalOrderStatus.Status.completed == finalOrderStatus.getStatus()){
			model.setDesc("订单已完成");
			model.setStatus(FinalOrderStatus.Status.completed);
		}
		this.descModel = model;
		this.orderItems = OrderItemModel.bindData(trade.getOrderItems(false));
		this.create_date = trade.getCreateDate();
		this.pickUpCode = trade.getSn();
		TenantListModel tenant = new TenantListModel();
		tenant.copyFrom(trade.getTenant());

	}

	public void copyFrom(Trade trade,Location location) {
		copyFrom(trade);
		TenantListModel tenant = new TenantListModel();
		tenant.copyFrom(trade.getTenant(),location);
	}
	
	public static List<TradeModel> bindData(List<Trade> trades) {
		List<TradeModel> models = new ArrayList<TradeModel>();
		for (Trade trade:trades) {
			TradeModel model = new TradeModel();
			model.copyFrom(trade);
			models.add(model);
		}
		return models;
	}

	public static List<TradeModel> bindData(List<Trade> trades, Location location, List<CouponCode> codes) {
		List<TradeModel> models = new ArrayList<TradeModel>();
		for (Trade trade:trades) {
			TradeModel model = new TradeModel();
			model.copyFrom(trade,location);
			models.add(model);
		}
		return models;
	}

}
