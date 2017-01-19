/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import net.wit.constant.SettingConstant;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.MemberAttribute.Type;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 * @ClassName: VisitRecord
 * @Description: 访问记录表
 * @date 2016年12月02日 上午9:08:38
 */
@Entity
@Table(name = "xx_visit_record")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_visit_record_sequence")
public class VisitRecord extends BaseEntity {

    private static final long serialVersionUID = 1533130686714725835L;
    /**
     * 访问类型
     */
    public enum VisitType {
        /**
         * pc
         */
        web,
        /**
         * C端
         */
        app,
        /**
         * 购物屏
         */
        pad,
        /**
         * 微信
         */
        weixin,
    }
    /** 手机类型*/
    @Column(updatable = false)
    private String machineType;

    /** 访问类型* */
    @Column(nullable = false, updatable = false)
    private VisitType visitType;

    /** 内容 */
    @Column(updatable = false)
    private String content;

    /**
     * 会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private Member member;

    /**
     * 店铺
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private Tenant tenant;

    /** 商品编号 */
     @Column( updatable = false)
    private String sn;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}