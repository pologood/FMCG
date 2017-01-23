package net.wit.controller.weixin.model;

import net.wit.controller.weixin.model.BaseModel;
import net.wit.entity.CouponCode;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RedPacketListModel extends BaseModel {

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
     * 结束时间
     */
    private String endDate;

    /**
     * 是否使用
     */
    private boolean used;

    /**
     * 红包码
     */
    private String code;

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

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void copyFrom(CouponCode couponCode, Location location) {
        this.id = couponCode.getId();
        this.amount = couponCode.getCoupon().getAmount();
        if (couponCode.getCoupon().getTenant()!=null) {
            this.tenantName = couponCode.getCoupon().getTenant().getName();
            DeliveryCenter deliveryCenter = couponCode.getCoupon().getTenant().nearDeliveryCenter(location);
            if (deliveryCenter != null) {
                this.address = deliveryCenter.getAddress();
            }
            this.distance = couponCode.getCoupon().getTenant().distatce(location);
            if (this.distance != -1) {
                this.distance = Double.parseDouble(String.format("%.2f", this.distance / 1000));
            }
        }
        this.receiveCount = couponCode.getCoupon().getSendCount();
        this.count = couponCode.getCoupon().getCount();
        if (couponCode.getCoupon().getEndDate() != null) {
//            Long diff = (couponCode.getCoupon().getEndDate().getTime() - new Date().getTime()) / 1000;
//            Long hour = diff / (60 * 60);
//            Long minute = diff / 60 - hour * 60;
//            Long second = diff - hour * 60 * 60 - minute * 60;
//            this.endDate = hour + ":" + minute + ":" + second;
            this.endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(couponCode.getCoupon().getEndDate());
        }
        this.used = couponCode.getIsUsed();
        this.code = couponCode.getCode();
    }

    public static List<RedPacketListModel> bindData(List<CouponCode> couponCodes, Location location) {
        List<RedPacketListModel> models = new ArrayList<>();
        for (CouponCode couponCode : couponCodes) {
            RedPacketListModel model = new RedPacketListModel();
            model.copyFrom(couponCode, location);
            models.add(model);
        }
        return models;
    }

}
