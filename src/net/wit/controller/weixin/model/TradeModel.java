package net.wit.controller.weixin.model;

import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Trade;
import net.wit.support.FinalOrderStatus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TradeModel extends BaseModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 订单状态
     */
    private FinalOrderStatus finalOrderStatus;
    /**
     * 订单日志
     */
    private List<OrderLogModel> orderLogs;
    /**
     * 支付方式
     */
    private PaymentMethodModel paymentMethod;
    /**
     * 配送方式
     */
    private ShippingMethodModel shippingMethod;
    /**
     * 订单号
     */
    private String sn;
    /**
     * 提货码
     */
    private String pickUpCode;
    /**
     * 提货时间
     */
    private String deliveryDate;
    /**
     * 收货人
     */
    private String consignee;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 提货地址
     */
    private String deliveryAddress;
    /**
     * 买家留言
     */
    private String memo;
    /**
     * 商家Id
     */
    private Long tenantId;
    /**
     * 商家名称
     */
    private String tenantName;
    /**
     * 订单项
     */
    private List<OrderItemModel> orderItems;
    /**
     * 赠品项
     */
    private List<OrderItemModel> giftItems;
    /**
     * 商品金额
     */
    private BigDecimal productPrice;
    /**
     * 促销折扣金额
     */
    private BigDecimal promotionDiscount;
    /**
     * 优惠券优惠金额
     */
    private BigDecimal couponDiscount;
    /**
     * 平台活动立减折扣
     */
    private BigDecimal discount;
    /**
     * 运费
     */
    private BigDecimal freight;
    /**
     * 赠送积分
     */
    private Long point;
    /**
     * 订单合计
     */
    private BigDecimal amount;
    /**
     * 物流公司
     */
    private String deliveryCorp;
    /**
     * 运单号
     */
    private String trackingNo;
    /**
     * 发货单
     */
    private List<ShippingModel> shippings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinalOrderStatus getFinalOrderStatus() {
        return finalOrderStatus;
    }

    public void setFinalOrderStatus(FinalOrderStatus finalOrderStatus) {
        this.finalOrderStatus = finalOrderStatus;
    }

    public List<OrderLogModel> getOrderLogs() {
        return orderLogs;
    }

    public void setOrderLogs(List<OrderLogModel> orderLogs) {
        this.orderLogs = orderLogs;
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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPickUpCode() {
        return pickUpCode;
    }

    public void setPickUpCode(String pickUpCode) {
        this.pickUpCode = pickUpCode;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItemModel> getGiftItems() {
        return giftItems;
    }

    public void setGiftItems(List<OrderItemModel> giftItems) {
        this.giftItems = giftItems;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDeliveryCorp() {
        return deliveryCorp;
    }

    public void setDeliveryCorp(String deliveryCorp) {
        this.deliveryCorp = deliveryCorp;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public List<ShippingModel> getShippings() {
        return shippings;
    }

    public void setShippings(List<ShippingModel> shippings) {
        this.shippings = shippings;
    }

    public void copyFrom(Trade trade) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.id = trade.getId();
        this.finalOrderStatus = trade.getFinalOrderStatus().get(0);
        this.orderLogs = OrderLogModel.bindData(trade.getOrderLogs());
        if (trade.getOrder().getPaymentMethod() != null) {
            PaymentMethodModel paymentMethod = new PaymentMethodModel();
            paymentMethod.copyFrom(trade.getOrder().getPaymentMethod());
            this.paymentMethod = paymentMethod;
        }
        if (trade.getOrder().getShippingMethod() != null) {
            ShippingMethodModel shippingMethod = new ShippingMethodModel();
            shippingMethod.copyFrom(trade.getOrder().getShippingMethod());
            this.shippingMethod = shippingMethod;
        }
        this.sn = trade.getOrder().getSn();
        this.pickUpCode = trade.getSn();
        if (trade.getDeliveryDate() == null && trade.getOrderStatus().equals(OrderStatus.confirmed)) {
            this.deliveryDate = "营业时间内不限";
        } else if (trade.getDeliveryDate() == null || trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
            this.deliveryDate = "等待卖家确认";
        } else {
            this.deliveryDate = sdf.format(trade.getDeliveryDate()) + "以后";
        }
        this.consignee = trade.getOrder().getConsignee();
        this.phone = trade.getOrder().getPhone();
        this.areaName = trade.getOrder().getArea().getFullName();
        this.address = trade.getOrder().getAddress();
        if (trade.getDeliveryCenter() != null) {
            this.deliveryAddress = trade.getDeliveryCenter().getAddress();
        } else {
            this.deliveryAddress = this.address;
        }
        this.memo = trade.getMemo() != null ? trade.getMemo() : trade.getOrder().getMemo();
        this.tenantId = trade.getTenant().getId();
        this.tenantName = trade.getTenant().getName();
        this.orderItems = OrderItemModel.bindData(trade.getOrderItems(false));
        this.giftItems = OrderItemModel.bindData(trade.getGiftItems());
        this.productPrice = trade.getPrice();
        this.promotionDiscount = trade.getPromotionDiscount();
        this.couponDiscount = trade.getCouponDiscount();
        this.discount = trade.getDiscount();
        this.freight = trade.getFreight();
        this.point = trade.getPoint();
        this.amount = trade.getAmount();
        if (trade.getShippings().size() > 0) {
            this.deliveryCorp = trade.getShippings().iterator().next().getDeliveryCorp();
            this.trackingNo = trade.getShippings().iterator().next().getTrackingNo();
        }
        this.shippings = ShippingModel.bindData(new ArrayList<>(trade.getShippings()));

    }

    public static List<TradeModel> bindData(List<Trade> trades) {
        List<TradeModel> models = new ArrayList<>();
        for (Trade trade : trades) {
            TradeModel model = new TradeModel();
            model.copyFrom(trade);
            models.add(model);
        }
        return models;
    }

}
