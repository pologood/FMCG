package net.wit.controller.weixin.model;

import net.wit.entity.Shipping;
import net.wit.entity.ShippingMethod;
import net.wit.entity.Trade;
import net.wit.support.FinalOrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TradeListModel extends BaseModel {

    /**
     * ID
     **/
    private Long id;
    /**
     * 订单号
     **/
    private String sn;
    /**
     * 商家名称
     **/
    private String tenantName;
    /**
     * 商家缩略图
     **/
    private String tenantThumbnail;
    /**
     * 订单总价
     **/
    private BigDecimal amount;
    /**
     * 订单件数
     **/
    private int quantity;
    /**
     * 订单状态
     **/
    private FinalOrderStatus finalOrderStatus;
    /**
     * 第一个商品
     **/
    private OrderItemModel orderItem;
    /**
     * 商品项
     **/
    private List<OrderItemModel> orderItems;
    /**
     * 运费
     **/
    private BigDecimal freight;
    /**
     * 配送方式
     */
    private ShippingMethod shippingMethod;
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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantThumbnail() {
        return tenantThumbnail;
    }

    public void setTenantThumbnail(String tenantThumbnail) {
        this.tenantThumbnail = tenantThumbnail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public FinalOrderStatus getFinalOrderStatus() {
        return finalOrderStatus;
    }

    public void setFinalOrderStatus(FinalOrderStatus finalOrderStatus) {
        this.finalOrderStatus = finalOrderStatus;
    }

    public OrderItemModel getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemModel orderItem) {
        this.orderItem = orderItem;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public List<OrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public List<ShippingModel> getShippings() {
        return shippings;
    }

    public void setShippings(List<ShippingModel> shippings) {
        this.shippings = shippings;
    }

    public void copyFrom(Trade trade) {
        this.id = trade.getId();
        this.sn = trade.getOrder().getSn();
        this.tenantName = trade.getTenant().getName();
        this.tenantThumbnail = trade.getTenant().getThumbnail() != null ? trade.getTenant().getThumbnail() : trade.getTenant().getLogo();
        this.amount = trade.getAmount();
        this.quantity = trade.getQuantity(false);
        this.finalOrderStatus = trade.getFinalOrderStatus().get(0);
        OrderItemModel model = new OrderItemModel();
        model.copyFrom(trade.getOrderItems(false).get(0));
        this.orderItem = model;
        this.orderItems = OrderItemModel.bindData(trade.getOrderItems(false));
        this.freight = trade.getFreight();
        this.shippingMethod=trade.getOrder().getShippingMethod();
        this.shippings=ShippingModel.bindData(new ArrayList<Shipping>(trade.getShippings()));
    }

    public static List<TradeListModel> bindData(List<Trade> trades) {
        List<TradeListModel> models = new ArrayList<>();
        for (Trade trade : trades) {
            TradeListModel model = new TradeListModel();
            model.copyFrom(trade);
            models.add(model);
        }
        return models;
    }


}
