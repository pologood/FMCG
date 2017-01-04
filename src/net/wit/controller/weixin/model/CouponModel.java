package net.wit.controller.weixin.model;

import net.wit.entity.Coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券
 * Created by WangChao on 2016/12/1.
 */
public class CouponModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 面额
     */
    private BigDecimal amount;
    /**
     * 最小商品限额
     */
    private BigDecimal minimumPrice;

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

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public void copyFrom(Coupon coupon){
        this.id=coupon.getId();
        this.amount=coupon.getAmount();
        this.minimumPrice=coupon.getMinimumPrice();
    }

    public static List<CouponModel> bindData(List<Coupon> coupons) {
        List<CouponModel> models = new ArrayList<>();
        for (Coupon coupon:coupons) {
            CouponModel model = new CouponModel();
            model.copyFrom(coupon);
            models.add(model);
        }
        return models;
    }
}
