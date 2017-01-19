/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.wit.*;
import net.wit.entity.*;
import net.wit.entity.model.CouponSumerModel;

/**
 * Service - 优惠券
 *
 * @author rsico Team
 * @version 3.0
 */
public interface CouponService extends BaseService<Coupon, Long> {

    Page<Coupon> findPage(Area area, Community community, TenantCategory tenantCategory, Boolean isExpired, Location location, BigDecimal distance, String orderType, Boolean isPromotion, Pageable pageable);

    List<Coupon> findList(Area area,Community community,TenantCategory tenantCategory, Boolean isExpired, Location location, BigDecimal distance, String orderType, Boolean isPromotion, Integer first, Integer count, List<Filter> filters, List<net.wit.Order> orders);

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
     * 将给定的商品种类List集合转换为Set集合
     *
     * @param productCategorysList 商品种类List集合
     * @return 商品种类Set集合
     */
    Set<ProductCategory> getProductCategorySet(List<ProductCategory> productCategorysList);

    /**
     * 根据购物车、会员和支付方式查询红包
     *
     * @param cart
     * @param member
     * @param paymentMethodId
     * @return
     */
    List<Map<String, String>> findEnabledCouponList(Cart cart, Member member, Long paymentMethodId);

    /**
     * 优惠券统计
     */
    Page<CouponSumerModel> sumer(Coupon coupon, CouponSumerModel.Type type, Pageable pageable);


    /**
     * 优惠券统计
     */
    CouponSumerModel count(Coupon coupon, CouponSumerModel.Type type);

    void upgrade(Coupon coupon);
    //红包支付冻结资金，member指发红包的人
    void payment(Payment payment);
    
    //红包核销完成,member指核人
    void complete(CouponCode couponCode, Member operator);

    //
    //Coupon findBy
    /**
     * 根据店铺查询红包活动
     *
     * @param tenant
     * @return
     */
    List<Coupon> findEnabledCouponList(Tenant tenant);
}