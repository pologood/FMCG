package net.wit.controller.app.model;

import net.wit.entity.CouponCode;
import net.wit.entity.Rebate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
     * 推广导购
     */
    private String guideMemberName;

    /**
     * 领取人
     */
    private String memberName;

    /**
     * 领取头像
     */
    private String memberThumbnail;

    /**
     * 领取日期
     */
    private Date receiveDate;

    /**
     * 核销人
     */
    private String operateMemberName;

    /**
     * 核销日期
     */
    private Date operateDate;

    /**
     * 使用情况
     */
    private boolean isUsed;

    /**
     * 分润金额
     */
    private BigDecimal commission;

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

    public String getGuideMemberName() {
        return guideMemberName;
    }

    public void setGuideMemberName(String guideMemberName) {
        this.guideMemberName = guideMemberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberThumbnail() {
        return memberThumbnail;
    }

    public void setMemberThumbnail(String memberThumbnail) {
        this.memberThumbnail = memberThumbnail;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getOperateMemberName() {
        return operateMemberName;
    }

    public void setOperateMemberName(String operateMemberName) {
        this.operateMemberName = operateMemberName;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public void copyFrom(CouponCode couponCode) {
        this.id=couponCode.getId();
        this.tenantName=couponCode.getTenant()==null?null:couponCode.getTenant().getName();
        this.amount=couponCode.getCoupon().getAmount();
        this.guideMemberName = couponCode.getGuideMember()==null?null:couponCode.getGuideMember().getDisplayName();
        this.memberName=couponCode.getMember()==null?null:couponCode.getMember().getDisplayName();
        this.memberThumbnail=couponCode.getMember()==null?null:couponCode.getMember().getHeadImg();
        this.receiveDate=couponCode.getCreateDate();
        this.operateMemberName=couponCode.getOperateMember()==null?null:couponCode.getOperateMember().getDisplayName();
        this.operateDate=couponCode.getOperateDate()==null?null:couponCode.getOperateDate();
        this.isUsed = couponCode.getIsUsed();
        //this.commission = BigDecimal.ZERO;
    }

    public static List<RedPacketListModel> bindData(List<CouponCode> couponCodes) {
        List<RedPacketListModel> models = new ArrayList<>();
        for (CouponCode couponCode:couponCodes) {
            RedPacketListModel model = new RedPacketListModel();
            model.copyFrom(couponCode);
            models.add(model);
        }
        return models;
    }

    public void copyFrom(Rebate rebate) {
        this.id=rebate.getCouponCode().getId();
        this.tenantName=rebate.getCouponCode().getTenant()==null?null:rebate.getCouponCode().getTenant().getName();
        this.amount=rebate.getCouponCode().getCoupon().getAmount();
        this.guideMemberName = rebate.getCouponCode().getGuideMember()==null?null:rebate.getCouponCode().getGuideMember().getDisplayName();
        this.memberName=rebate.getCouponCode().getMember()==null?null:rebate.getCouponCode().getMember().getDisplayName();
        this.memberThumbnail=rebate.getCouponCode().getMember()==null?null:rebate.getCouponCode().getMember().getHeadImg();
        this.receiveDate=rebate.getCouponCode().getCreateDate();
        this.operateMemberName=rebate.getCouponCode().getOperateMember()==null?null:rebate.getCouponCode().getOperateMember().getDisplayName();
        this.operateDate=rebate.getCouponCode().getOperateDate()==null?null:rebate.getCouponCode().getOperateDate();
        this.isUsed = rebate.getCouponCode().getIsUsed();
        this.commission = rebate.getAmount();
    }

    public static List<RedPacketListModel> bindRebate(List<Rebate> rebates) {
        List<RedPacketListModel> models = new ArrayList<>();
        for (Rebate rebate:rebates) {
            RedPacketListModel model = new RedPacketListModel();
            model.copyFrom(rebate);
            models.add(model);
        }
        return models;
    }

}
