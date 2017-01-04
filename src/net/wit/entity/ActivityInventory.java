package net.wit.entity;

import com.google.gson.annotations.Expose;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/15.
 */
@Entity
@Table(name = "xx_activity_inventory")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_activity_inventory_sequence")
public class ActivityInventory extends BaseEntity {


    private static final long serialVersionUID = 1L;

    public enum Status{
        /** 待处理*/
        wait,
        /** 已完成*/
        success
    }

    /** 兑换的商品/服务 */
    private String description;

    /** 店铺剩余积分 */
    @Min(0)
    private Long tenantPoint;

    /** 需要的积分 */
    @Min(0)
    private Long point;

    /** 店铺 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    /** 状态 */
    private Status status;

    /** 申请日期 */
    private Date applyDate;

    /** 处理日期 */
    private Date processingDate;

    /** 备注 */
    private String remarks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTenantPoint() {
        return tenantPoint;
    }

    public void setTenantPoint(Long tenantPoint) {
        this.tenantPoint = tenantPoint;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
