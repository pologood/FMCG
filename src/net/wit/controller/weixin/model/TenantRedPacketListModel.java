package net.wit.controller.weixin.model;

import net.wit.controller.weixin.model.BaseModel;
import net.wit.entity.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TenantRedPacketListModel extends BaseModel {

    /**
     * ID
     */
    private Long id;

    /**
     * 面值
     */
    private BigDecimal amount;

    /**
     * 店铺名称
     */
    private String tenantName;

    /**
     * 地址
     */
    private String address;

    /**
     * 距离
     */
    private double distance;

    /**
     * 领取人数
     */
    private Integer receiveCount;

    /**
     * 总数
     */
    private Integer count;

    /**
     * 倒计时
     */
    private String endDate;

    /**
     * 是否领取
     */
    private Boolean received;

    /**
     * 是否已领完
     */
    private Boolean unused;

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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Boolean getUnused() {
        return unused;
    }

    public void setUnused(Boolean unused) {
        this.unused = unused;
    }

    public void copyFrom(Coupon coupon, Location location, Member member) {
        this.id = coupon.getId();
        this.amount = coupon.getAmount();
        this.tenantName = coupon.getTenant().getName();
        DeliveryCenter deliveryCenter = coupon.getTenant().nearDeliveryCenter(location);
        if (deliveryCenter != null) {
            this.address = deliveryCenter.getAddress();
        }
        this.distance = coupon.getTenant().distatce(location);
        if (this.distance != -1) {
            this.distance = Double.parseDouble(String.format("%.2f", this.distance / 1000));
        }
        this.receiveCount = coupon.getSendCount();
        this.count = coupon.getCount();
        if (coupon.getEndDate() != null) {
//            Long diff = (coupon.getEndDate().getTime() - new Date().getTime()) / 1000;
//            Long hour = diff / (60 * 60);
//            Long minute = diff / 60 - hour * 60;
//            Long second = diff - hour * 60 * 60 - minute * 60;
//            this.endDate = hour + ":" + minute + ":" + second;
            this.endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coupon.getEndDate());
        }
        this.received = false;
        if (member != null) {
            for (CouponCode couponCode : coupon.getCouponCodes()) {
                if (Objects.equals(couponCode.getMember().getId(), member.getId())) {
                    this.received = true;
                    return;
                }
            }
        }
        this.unused = false;
        if (coupon.getSendCount().compareTo(coupon.getCount()) >= 0) {
            this.unused = true;
        }

    }

    public static List<TenantRedPacketListModel> bindData(List<Coupon> coupons, Location location, Member member) {
        List<TenantRedPacketListModel> models = new ArrayList<>();
        for (Coupon coupon : coupons) {
            TenantRedPacketListModel model = new TenantRedPacketListModel();
            model.copyFrom(coupon, location, member);
            models.add(model);
        }
        return models;
    }

}
