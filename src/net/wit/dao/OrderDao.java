/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.service.OrderService;

/**
 * Dao - 订单
 * @author rsico Team
 * @version 3.0
 */
public interface OrderDao extends BaseDao<Order, Long> {

	/**
	 * 根据订单编号查找订单
	 * @param sn 订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	Order findBySn(String sn);

	/**
	 * 查找订单
	 * @param member 会员
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 订单
	 */
	List<Order> findList(Member member, Integer count, List<Filter> filters, List<net.wit.Order> orders);

	/**
	 * 查找订单分页
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return 订单分页
	 */
	Page<Order> findPage(Member member, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	/**
	 * 查询订单数量
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @return 订单数量
	 */
	Long count(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	/**
	 * 查询等待支付订单数量
	 * @param member 会员
	 * @return 等待支付订单数量
	 */
	Long waitingPaymentCount(Member member);

	/**
	 * 查询等待发货订单数量
	 * @param member 会员
	 * @return 等待发货订单数量
	 */
	Long waitingShippingCount(Member member);

	/**
	 * 获取销售额
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @return 销售额
	 */
	BigDecimal getSalesAmount(Date beginDate, Date endDate);

	/**
	 * 获取销售量
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @return 销售量
	 */
	Integer getSalesVolume(Date beginDate, Date endDate);
	
	/**
	 * 获取会员购量
	 * @param member 起始日期
	 * @param product 结束日期
	 * @return 销售量
	 */
	Integer getMemberBuyQuantity(Member member, Product product);
	
    Integer getMemberBuyDay(Member member, Product product);

	/**
	 * 释放过期订单库存
	 */
	void releaseStock();

	/**
	 * 查找订单分页
	 * @param member 会员
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	/**
	 * 查找店铺订单分页
	 * @param member 会员
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findPage(Member member, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 查找我的订单分页
	 * @param member 会员
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findPageMember(Member member, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 查找全部订单分页
	 * @param member 会员
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findPageAll(Member member, Date beginDate, Date endDate, Pageable pageable);

	Long countAll(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	long countmy(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	List<Order> myOrder(Member member);

	/**
	 * @see OrderService#findList(Tenant, Date, Date, OrderStatus, PaymentStatus, ShippingStatus)
	 */
	List<Order> findList(Tenant tenant, Date startDate, Date endDate, List<OrderStatus> orderStatuses, List<ShippingStatus> shippingStatuses);

	/**
	 * 查找店铺订单分页
	 * @param member 会员
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findPageTenant(Member member, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 查找会员代付款订单
	 * @param member 会员
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findWaitPay(Member member, Boolean hasExpired, Pageable pageable);

	Page<Order> findWaitShipping(Member member, Boolean hasExpired, Pageable pageable);

	Page<Order> findPageWithoutStatus(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Pageable pageable);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param orderStatus
	 * @param paymentStatus
	 * @param shippingStatus
	 * @param hasExpired
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return  Page<Order>
	 */
	Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate, Pageable pageable);

	Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate,List<Tenant> tenants, Pageable pageable);
	
	/**添加地区筛选参数*/
	Page<Order> findPage(Area area,OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate,String keyword,Pageable pageable);

	boolean isLimit(Member member,Integer count);
	//业务员销售金额
	BigDecimal countmySales(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus);

	Order findByTokenKey(String token_key);
}