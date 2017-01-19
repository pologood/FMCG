package net.wit.controller.weixin.model;

import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    /**
     * 是否已领取
     */
    private boolean hasReceived;

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

    public boolean isHasReceived() {
        return hasReceived;
    }

    public void setHasReceived(boolean hasReceived) {
        this.hasReceived = hasReceived;
    }

    public void copyFrom(Coupon coupon, Member member){
        this.id=coupon.getId();
        this.amount=coupon.getAmount();
        this.minimumPrice=coupon.getMinimumPrice();
        this.hasReceived = false;
        if (member != null) {
            for (CouponCode couponCode : coupon.getCouponCodes()) {
                if (Objects.equals(couponCode.getMember().getId(), member.getId())) {
                    this.hasReceived = true;
                    break;
                }
            }
        }
    }

    public static List<CouponModel> bindData(List<Coupon> coupons, Member member) {
        List<CouponModel> models = new ArrayList<>();
        for (Coupon coupon:coupons) {
            CouponModel model = new CouponModel();
            model.copyFrom(coupon, member);
            models.add(model);
        }
        return models;
    }
}
