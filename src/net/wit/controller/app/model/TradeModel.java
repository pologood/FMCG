package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.wit.entity.CouponCode;
import net.wit.entity.Location;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.support.FinalOrderStatus;

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
	private String deliveryDate;

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
	/** 分润金额 */
	private BigDecimal profitAmount;
	/** 货款金额 */
	private BigDecimal price;
	/** 订单合计 */
	private BigDecimal amount;
	/** 平台活动立减折扣 */
	private BigDecimal discount;
	/** 赠送积分 */
	private Long point;
	/**制单日期**/
	private Date create_date;

	/** 附言 */
	private String memo;

	/** 会员 */
	private MemberModel member;
	
	private List<OrderItemModel> orderItems;
	
	private List<OrderItemModel> giftItems;
	
	private List<OrderLogModel> orderLogs;
	
	private Boolean isReview;
	
	/**商家名称 **/
	private TenantListModel tenant;
	/**商家可用优惠券 **/
	private List<CouponCodeModel> availableCoupons;
		
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

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
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

	public TenantListModel getTenant() {
		return tenant;
	}

	public void setTenant(TenantListModel tenant) {
		this.tenant = tenant;
	}

	public Boolean getIsReview() {
		return isReview;
	}

	public void setIsReview(Boolean isReview) {
		this.isReview = isReview;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public List<CouponCodeModel> getAvailableCoupons() {
		return availableCoupons;
	}

	public void setAvailableCoupons(List<CouponCodeModel> availableCoupons) {
		this.availableCoupons = availableCoupons;
	}

	public void copyFrom(Trade trade) {
		this.id = trade.getId();
		this.amount = trade.getAmount();
		this.price = trade.getPrice();
		this.couponDiscount = trade.getCouponDiscount();
		this.freight = trade.getFreight();
		this.invoiceTitle = trade.getOrder().getInvoiceTitle();
		this.isInvoice = trade.getOrder().getIsInvoice();
		MemberModel member = new MemberModel();
		member.copyFrom(trade.getOrder().getMember());
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
		this.point = trade.getPoint();
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
		if(trade.getDeliveryDate()==null && trade.getOrderStatus().equals(OrderStatus.confirmed)){
			this.deliveryDate = "营业时间内不限";
		}else if (trade.getDeliveryDate()==null || trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
	    	this.deliveryDate = "等待卖家确认";
	    } else {
	    	SimpleDateFormat dateFormater=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    this.deliveryDate= dateFormater.format(trade.getDeliveryDate())+"以后";
	    }
		this.sn = trade.getOrder().getSn();
		this.tax = trade.getTax();
		this.orderStatus = trade.getOrderStatus();
		this.shippingStatus = trade.getShippingStatus();
		this.paymentStatus = trade.getPaymentStatus();
		this.finalOrderStatus = trade.getFinalOrderStatus().get(0);
		this.orderItems = OrderItemModel.bindData(trade.getOrderItems(false));
		this.profitAmount = trade.getTotalProfit();
		this.create_date = trade.getCreateDate();
		this.pickUpCode = trade.getSn();
		this.isReview = (trade.getMemberReview()!=null);
		TenantListModel tenant = new TenantListModel();
		tenant.copyFrom(trade.getTenant());
		this.tenant = tenant;
		this.orderLogs = OrderLogModel.bindData(trade.getOrderLogs());
		
		this.giftItems = OrderItemModel.bindData(trade.getOrderItems(true));

	}

	public void copyFrom(Trade trade,Location location) {
		copyFrom(trade);
		TenantListModel tenant = new TenantListModel();
		tenant.copyFrom(trade.getTenant(),location);
		this.tenant=tenant;
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

	public void bindAvailableCoupon(List<CouponCode> codes){
		List<CouponCode> couponCodes=new ArrayList<>();
		for(CouponCode couponCode:codes){
			if(couponCode.getCoupon().getTenant().getId()==getTenant().getId()){
				couponCodes.add(couponCode);
			}
		}
		this.availableCoupons=CouponCodeModel.bindData(couponCodes);
	}

	public static List<TradeModel> bindData(List<Trade> trades,Location location,List<CouponCode> codes) {
		List<TradeModel> models = new ArrayList<TradeModel>();
		for (Trade trade:trades) {
			TradeModel model = new TradeModel();
			model.copyFrom(trade,location);
			model.bindAvailableCoupon(codes);
			models.add(model);
		}
		return models;
	}

	public List<OrderLogModel> getOrderLogs() {
		return orderLogs;
	}

	public void setOrderLogs(List<OrderLogModel> orderLogs) {
		this.orderLogs = orderLogs;
	}

	public List<OrderItemModel> getGiftItems() {
		return giftItems;
	}

	public void setGiftItems(List<OrderItemModel> giftItems) {
		this.giftItems = giftItems;
	}
	
}
