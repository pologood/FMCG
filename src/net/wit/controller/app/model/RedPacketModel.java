package net.wit.controller.app.model;

import net.wit.entity.Coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RedPacketModel extends BaseModel {

    /**
     * ID
     */
    private Long id;
    /**
     * 面值
     */
    private BigDecimal amount;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 最小商品限额
     */
    private BigDecimal minimumPrice;
    /**
     * 领取次数
     */
    private Integer receiveCount;
    /**
     * 剩余次数
     */
    private Integer restCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Integer getRestCount() {
        return restCount;
    }

    public void setRestCount(Integer restCount) {
        this.restCount = restCount;
    }

    public void copyFrom(Coupon coupon) {
        this.id = coupon.getId();
        this.amount = coupon.getAmount();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.minimumPrice = coupon.getMinimumPrice();
        this.receiveCount = coupon.getCouponCodes().size();
        this.restCount = coupon.getCount() - this.receiveCount;
    }

    public static List<RedPacketModel> bindData(List<Coupon> coupons) {
        List<RedPacketModel> models = new ArrayList<>();
        for (Coupon coupon:coupons) {
            RedPacketModel model = new RedPacketModel();
            model.copyFrom(coupon);
            models.add(model);
        }
        return models;
    }

}
