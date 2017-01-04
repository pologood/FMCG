package net.wit.controller.weixin.model;

import net.wit.entity.CouponCode;
import net.wit.entity.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfirmTradeModel extends BaseModel {
    /**
     * ID
     **/
    private Long id;
    /**
     * 运费
     */
    private BigDecimal freight;
    /**
     * 订单合计
     */
    private BigDecimal amount;
    /**
     * 店铺Id
     */
    private Long tenantId;
    /**
     * 商家名称
     */
    private String tenantName;
    /**
     * 订单商品
     */
    private List<ConfirmOrderItemModel> orderItems;
    /**
     * 商家可用优惠券
     */
    private List<CouponCodeModel> availableCoupons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public List<ConfirmOrderItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<ConfirmOrderItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public List<CouponCodeModel> getAvailableCoupons() {
        return availableCoupons;
    }

    public void setAvailableCoupons(List<CouponCodeModel> availableCoupons) {
        this.availableCoupons = availableCoupons;
    }

    public void copyFrom(Trade trade) {
        this.id = trade.getId();
        this.freight = trade.getFreight();
        this.amount = trade.getAmount();
        this.tenantId = trade.getTenant().getId();
        this.tenantName = trade.getTenant().getName();
        this.orderItems = ConfirmOrderItemModel.bindData(trade.getOrderItems());
    }

    public static List<ConfirmTradeModel> bindData(List<Trade> trades,List<CouponCode> codes) {
        List<ConfirmTradeModel> models = new ArrayList<>();
        for (Trade trade : trades) {
            ConfirmTradeModel model = new ConfirmTradeModel();
            model.copyFrom(trade);
            model.bindAvailableCoupon(codes);
            models.add(model);
        }
        return models;
    }

    public void bindAvailableCoupon(List<CouponCode> codes) {
        List<CouponCode> couponCodes = new ArrayList<>();
        for (CouponCode couponCode : codes) {
            if (Objects.equals(couponCode.getCoupon().getTenant().getId(), getTenantId())) {
                couponCodes.add(couponCode);
            }
        }
        this.availableCoupons = CouponCodeModel.bindData(couponCodes);
    }
}
