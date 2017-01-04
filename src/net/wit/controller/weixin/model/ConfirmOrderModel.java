package net.wit.controller.weixin.model;

import net.wit.entity.CouponCode;
import net.wit.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public class ConfirmOrderModel extends BaseModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 订单合计
     */
    private BigDecimal amount;
    /**
     * 台活动立减
     */
    private BigDecimal discount;
    /**
     * 子订单
     */
    private List<ConfirmTradeModel> trades;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<ConfirmTradeModel> getTrades() {
        return trades;
    }

    public void setTrades(List<ConfirmTradeModel> trades) {
        this.trades = trades;
    }

    public void copyFrom(Order order,List<CouponCode> codes) {
        this.id = order.getId();
        this.amount = order.getAmount();
        this.discount=order.getDiscount();
        this.trades = ConfirmTradeModel.bindData(order.getTrades(),codes);
    }

}
