/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.model.CouponSumerModel;

import java.util.Date;
import java.util.List;

/**
 * Dao - 优惠券
 *
 * @author rsico Team
 * @version 3.0
 */
public interface CouponDao extends BaseDao<Coupon, Long> {

    Page<Coupon> findPage(Area area, Community community, TenantCategory tenantCategory, Boolean isExpired, Location location, String orderType, Pageable pageable);

    Page<Coupon> findPage(String status,Pageable pageable);

    /**
     * 查找优惠券分页
     *
     * @param isEnabled  是否启用
     * @param isExchange 是否允许积分兑换
     * @param hasExpired 是否已过期
     * @param pageable   分页信息
     * @return 优惠券分页
     */
    Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable);

    void refreshStatus(Tenant tenant);

    /**
     * 获取系统内置的积分抵扣券
     */
    Coupon findSystemPointExchange();

    /**
     * 优惠券统计
     */
    Page<CouponSumerModel> sumer(Coupon coupon, CouponSumerModel.Type type, Pageable pageable);


    /**
     * 优惠券统计
     */
    CouponSumerModel count(Coupon coupon, CouponSumerModel.Type type);

    /**
     * 当前优惠券
     */
    List<Coupon> findEnabledCouponList(Tenant tenant);
}