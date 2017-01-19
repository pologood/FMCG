/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * @author Administrator
 * @ClassName: Wifi
 * @Description:
 * @date 2016年12月02日 上午9:08:38
 */
@Entity
@Table(name = "xx_wifi")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_wifi_sequence")
public class Wifi extends BaseEntity {

    private static final long serialVersionUID = 1533130686714725845L;

    /**
     * status
     */
    public enum Status {
        /**
         * pc
         */
        enabled,
        /**
         *
         */
        disabled
    }

    private Status status;

     /** wifi port1 member */
    @Column(updatable = false)
    private String uid1;

    /** wifi port2 tenant */
    @Column(updatable = false)
    private String uid2;

    /** tennet */
    @Column(updatable = false)
    private Long deliveryCenterId;

    /** 店铺 */
    @JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    /** 企业名称 */
    @JsonProperty
    @Column(nullable = false, length = 255)
    private String tenantName;

    /** 实体店名称 */
    @JsonProperty
    @Column(nullable = false, length = 255)
    private String deliveryCenterName;

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public Long getDeliveryCenterId() {
        return deliveryCenterId;
    }

    public void setDeliveryCenterId(Long deliveryCenterId) {
        this.deliveryCenterId = deliveryCenterId;
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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getDeliveryCenterName() {
        return deliveryCenterName;
    }

    public void setDeliveryCenterName(String deliveryCenterName) {
        this.deliveryCenterName = deliveryCenterName;
    }
}