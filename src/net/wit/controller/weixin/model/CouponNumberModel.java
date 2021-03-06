package net.wit.controller.weixin.model;

import net.wit.entity.CouponCode;
import net.wit.entity.CouponNumber;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 平台券
 * Created by WangChao on 2016/12/28.
 */
public class CouponNumberModel {
    /**
     * 领取时间
     */
    private String createDate;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 余额
     */
    private BigDecimal balance;



    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void copyFrom(CouponNumber couponNumber) {
        if (couponNumber.getCreateDate() != null) {
            this.createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(couponNumber.getCreateDate());
        }
        this.amount = couponNumber.getCoupon().getAmount();
        this.balance=couponNumber.getBalance();
    }

    public static List<CouponNumberModel> bindData(List<CouponNumber> couponNumbers) {
        List<CouponNumberModel> models = new ArrayList<>();
        for (CouponNumber couponNumber : couponNumbers) {
            CouponNumberModel model = new CouponNumberModel();
            model.copyFrom(couponNumber);
            models.add(model);
        }
        return models;
    }
}
