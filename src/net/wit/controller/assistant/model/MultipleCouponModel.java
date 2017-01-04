package net.wit.controller.assistant.model;

import net.wit.entity.CouponNumber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 平台套券Model
 * Created by hujun on 16/11/26.
 */
public class MultipleCouponModel extends BaseModel {

    /** 编号 */
    private Long id;

    /** 用户头像 */
    private String headImg;

    /** 用户昵称 */
    private String name;

    /** 套券余额 */
    private BigDecimal balance;

    /** 登记时间 */
    private Date createDate;

    /** 领取时间 */
    private Date receiveDate;

    /** 消费次数 */
    private Long useTimes;

    /** 获取的佣金 */
    private BigDecimal brokerage;

    /** 是否用完 */
    private boolean used;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Long getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Long useTimes) {
        this.useTimes = useTimes;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void copyFrom(CouponNumber couponNumber){
        this.id=couponNumber.getId();
        this.headImg=couponNumber.getMember().getHeadImg();
        this.name=couponNumber.getMember().getDisplayName();
        this.balance=couponNumber.getBalance();
        this.createDate=couponNumber.getCreateDate();
        this.receiveDate=couponNumber.getReceiveDate();
        this.brokerage=couponNumber.getBrokerage()==null?BigDecimal.ZERO:couponNumber.getBrokerage();
        this.useTimes=couponNumber.getUseTimes()==null?0:couponNumber.getUseTimes();
        this.used=couponNumber.getStatus().equals(CouponNumber.Status.used);
    }

    public static List<MultipleCouponModel> bindData(List<CouponNumber> couponNumbers) {
        List<MultipleCouponModel> models = new ArrayList<MultipleCouponModel>();
        for (CouponNumber couponNumber:couponNumbers) {
            MultipleCouponModel model = new MultipleCouponModel();
            model.copyFrom(couponNumber);
            models.add(model);
        }
        return models;
    }
}
