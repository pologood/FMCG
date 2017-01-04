package net.wit.controller.pad.model;

import net.wit.entity.Coupon;

import java.math.BigDecimal;

/**
 * Created by ruanx on 2016/12/6.
 */
public class CouponModel {
    /*ID*/
    private Long id;

    /*name*/
    private String name;

    /*可用最小金额*/
    private BigDecimal minimumPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public void copyFrom(Coupon coupon){
        this.id = coupon.getId();
        this.name = coupon.getName()+"，满"+coupon.getMinimumPrice().doubleValue()+"使用";
        this.minimumPrice = coupon.getMinimumPrice();
    }
}
