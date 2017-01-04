package net.wit.controller.weixin.model;

import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CouponCodeModel extends BaseModel {

    /**
     * ID
     */
    private Long id;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 最小商品价格
     */
    private BigDecimal minimumPrice;
    /**
     * 店铺名称
     */
    private String tenantName;
    /**
     * 有效日期
     */
    private String endDate;
    /**
     * 是否已使用
     */
    private Boolean used;
    /**
     * 优惠码
     */
    private String code;
    /**
     *
     */
    private Coupon.Type type;


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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Coupon.Type getType() {
        return type;
    }

    public void setType(Coupon.Type type) {
        this.type = type;
    }

    public void copyFrom(CouponCode couponCode) {
        this.id = couponCode.getCoupon().getId();
        this.minimumPrice = couponCode.getCoupon().getMinimumPrice();
        if (couponCode.getCoupon().getTenant()!=null) {
            this.tenantName = couponCode.getCoupon().getTenant().getName();
        }
        if (couponCode.getExpire()!=null) {
            this.endDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(couponCode.getExpire());
        } else
        if (couponCode.getCoupon().getEndDate() != null) {
            this.endDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(couponCode.getCoupon().getEndDate());
        }
        this.type = couponCode.getCoupon().getType();
        this.used = couponCode.getIsUsed();
        this.code = couponCode.getCode();
        if(this.type== Coupon.Type.multipleCoupon){
            this.amount=couponCode.getBalance();
        }else{
            this.amount = couponCode.getCoupon().getAmount();
        }
    }

    public static List<CouponCodeModel> bindData(List<CouponCode> couponCodes) {
        List<CouponCodeModel> models = new ArrayList<>();
        for (CouponCode couponCode : couponCodes) {
            if (couponCode.getCoupon() != null) {
                CouponCodeModel model = new CouponCodeModel();
                model.copyFrom(couponCode);
                models.add(model);
            }
        }
        return models;
    }
}
