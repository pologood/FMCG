package net.wit.controller.wap.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.Coupon;
import net.wit.entity.Coupon.Status;
import net.wit.entity.Coupon.Type;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CouponModel extends BaseModel {

    /*ID*/
    private Long id;
    /*类型*/
    private Type type;
    /*名称*/
    private String name;
    /*面值*/
    private BigDecimal amount;
    /**
     * 有效日期
     */
    private Date startDate;

    /**
     * 有效日期
     */
    private Date endDate;
    /**
     * 最小商品限额
     */
    private BigDecimal minimumPrice;
    /**
     * 是否启用
     */
    private Boolean isEnabled;
    /**
     * 可领取次数
     */
    private Long receiveTimes;
    /**
     * 已使用
     */
    private Integer usedCount;
    /**
     * 已发放
     */
    private Integer sendCount;

    /**
     * 总数
     */
    private Integer count;

    /**
     * 状态
     */
    private Status status;

    /**
     * 描述
     */
    private String introduction;

    /**
     * 有效日期
     */
    private String effectiveDate;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Long getReceiveTimes() {
        return receiveTimes;
    }

    public void setReceiveTimes(Long receiveTimes) {
        this.receiveTimes = receiveTimes;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void copyFrom(Coupon coupon) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.amount = coupon.getAmount();
        this.count = coupon.getCount();
        this.endDate = coupon.getEndDate();
        this.isEnabled = coupon.getIsEnabled();
        this.minimumPrice = coupon.getMinimumPrice();
        this.receiveTimes = coupon.getReceiveTimes();
        this.sendCount = coupon.getSendCount();
        this.startDate = coupon.getStartDate();
        this.status = coupon.getStatus();
        this.type = coupon.getType();
        this.usedCount = coupon.getUsedCount();
        this.introduction = coupon.getIntroduction();
        if (coupon.getStartDate() != null && coupon.getEndDate() != null) {
            this.effectiveDate = sdf.format(coupon.getStartDate()) + "-" + sdf.format(coupon.getEndDate());
        }
    }

    public static Set<CouponModel> bindData(Set<Coupon> coupons) {
        Set<CouponModel> models = new HashSet<CouponModel>();
        for (Coupon coupon : coupons) {
            CouponModel model = new CouponModel();
            model.copyFrom(coupon);
            models.add(model);
        }
        return models;
    }
}
