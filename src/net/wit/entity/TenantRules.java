/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: TenantRules
 * @Description: 规则
 * @author Administrator
 * @date 2016年7月28日 上午11:12:04
 */
@Entity
@Table(name = "xx_tenant_rules")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_tenant_rules_sequence")
public class TenantRules extends BaseEntity {

    private static final long serialVersionUID = 5095521437302782717L;

    /** 操作类型
     * 注：需要与
     *          xx_tenant_rules_role表
     *          WebContent\WEB-INF\language\common
     *          RoleController.java update、save
     *          中字段统一
     * **/
    public  enum  Type{
        /**
         * 查看      0
         */
        read,
        /**
         * 增加       1
         */
        add,
        /**
         * 修改       2
         */
        update,
        /**
         * 删除       3
         */
        del,
        /**
         * 导出       4
         */
        exp,
        /**
         * 充值
         */
        refill,
        /**
         * 提现
         */
        cash,
        /**
         * 确认受理
         */
        confirm,
        /**
         * 拒绝受理
         */
        dismissal,
        /**
         * 调价
         */
        modifyPrice,
        /**
         * 商品上架
         */
        upMarket,
        /**
         * 商品下架
         */
        downMarket,
        /**
         * 打印
         */
        print,
        /**
         * 缴款
         */
        payment,
        /**
         * 审核
         */
        applied,
        /**
         * 统计
         */
        statistics,
        /**
         * 分享
         */
        share,
        /**
         * 管理
         */
        supervise,
        /**
         * 拒绝退货
        * */
        refuseReturn,
        /**
         * 同意退货
         * */
        agreeReturn,
        /**
         * 发货
         * */
        sendGoods,
        /**
         * 取消订单
         * */
        cancelOrder,
        /*关闭*/
        close,
        /*开启*/
        open,
        /*二维码*/
        qrCode

    }
    /** 父级 */
    @ManyToOne(fetch = FetchType.LAZY)
    private TenantRules  parent;
    /** 等级 */
    @Min(0)
    private int lev;

    /** 名称 */
    @JsonProperty
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    private String name;
    /** 规则访问路径 */
    @JsonProperty
    @Length(max = 200)
    private String url;

    /**  操作*/
    @JsonProperty
    @Length(max = 200)
    private String oper;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<TenantRules>  children;

    // ===========================================getter/setter===========================================//


    public TenantRules getParent() {
        return parent;
    }

    public void setParent(TenantRules parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TenantRules> getChildren() {
        return children;
    }

    public void setChildren(List<TenantRules> children) {
        this.children = children;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

}