/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.*;

/**
 * @author Administrator
 * @ClassName: WifiRecord
 * @Description: 访问记录表
 * @date 2016年12月02日 上午9:08:38
 */
@Entity
@Table(name = "xx_wifi_record")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_wifi_record_sequence")
public class WifiRecord extends BaseEntity {

    private static final long serialVersionUID = 1533130686714725835L;

    /**
     * 访问类型
     */
    public enum WifiType {
        /**
         * member
         */
        member,
     }

    /** 访问类型* */
    @Column(nullable = false, updatable = false)
    private WifiType wifiType;

    /** member */
    @Column(updatable = false)
    private String uuid;


    /** tennet */
    @Column(updatable = false)
    private String wuid;


    /** tennet */
    @Column(updatable = false)
    private Long signal;


    public WifiType getWifiType() {
        return wifiType;
    }

    public void setWifiType(WifiType wifiType) {
        this.wifiType = wifiType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWuid() {
        return wuid;
    }

    public void setWuid(String wuid) {
        this.wuid = wuid;
    }

    public Long getSignal() {
        return signal;
    }

    public void setSignal(Long signal) {
        this.signal = signal;
    }
}