/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.QueryStatus;
import net.wit.entity.Order.ShippingStatus;

/**
 * Dao - 子订单
 * @author rsico Team
 * @version 3.0
 */
public interface TradeDao extends BaseDao<Trade, Long> {

	/**
	 * 验证码查找
	 * @param sn- 验证码
	 * @return 子订单
	 */
	Trade findBySn(String sn);

	/**
	 * 验证码查找
	 * @param sn- 验证码
	 * @return 子订单
	 */
	Trade findBySn(String sn, Tenant tenant);

	Page<Trade> order(Set<Trade> tradeSet, Tenant seller, Date begin_date, Date end_date, String keyword, Pageable pageable);

	List<Trade> findUnreviewList(Integer first,Integer count,Date overDate,List<Filter> filters,List<net.wit.Order> orders);

	/**
	 * 查找会员订单 V2
	 * @param QueryStatus 订单状态
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Trade> findPage(Pageable pageable, Member member, QueryStatus queryStatus, String keyword);
	long count(List<Filter> filters, Member member, QueryStatus queryStatus, String keyword);
	/**
	 * 查找商家订单 V2
	 * @param QueryStatus 订单状态
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Trade> findPage(Pageable pageable, Tenant tenant, QueryStatus queryStatus, String keyword);
	long count(List<Filter> filters, Tenant tenant, QueryStatus queryStatus, String keyword);
	public long tradeyesterdayCount(List<Filter> filters, Tenant tenant,QueryStatus queryStatus, String keyword,Date begin_date,Date end_date);//昨日订单

	Page<Trade> findPage(Pageable pageable, Tenant tenant, Date begin_date,Date end_date,QueryStatus queryStatus, String keyword);
	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Trade> findPage(Member member, OrderStatus orderStatus,PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Trade> findPage(Member member, Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Trade> findPage(Tenant tenant, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean returnStatus,String waitShip, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	
	Page<Trade> findPage(Member member, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean unReview, Pageable pageable);
	/**
	 * 查找会员的订单分页
	 * @param member 会员
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 子订单分页
	 */
	Page<Trade> findMemberPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param member 订单状态
	 * @param listType 支付状态
	 * @param beginDate 配送状态
	 * @param endDate 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findPage(Member member, Date beginDate, Date endDate, Pageable pageable);
	
	public Page<Trade> findPage(Member member, Date beginDate, Date endDate,String keywords, Pageable pageable);
	
	public Page<Trade> findPage(Member member, QueryStatus queryStatus,Date beginDate, Date endDate,String keywords, Pageable pageable);

	/**
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return
	 */
	Page<Trade> findPage(Member member, Pageable pageable);

	long countTenant(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	long count(Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean returnStatus, Boolean hasExpired);

	/**
	 * 统计月销售额
	 * @param member 商家
	 * @param date 日期
	 * @return 商品分页
	 */
	BigDecimal countSales(Tenant tenant,String queryDate);
	/**
	 * 查找会员代付款订单
	 * @param member 会员
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findWaitPay(Member member, Boolean hasExpired, Pageable pageable);

	Long findWaitPayCount(Member member, Boolean hasExpired);

	public Page<Trade> findWaitShipping(Member member, Boolean hasExpired, Pageable pageable);

	Long findWaitShippingCount(Member member, Boolean hasExpired);

	/**
	 * 查找会员待签收订单
	 */
	public Page<Trade> findWaitSign(Member member, Boolean hasExpired, Pageable pageable);

	Long findWaitSignCont(Member member, Boolean hasExpired);

	/**
	 * 查找会员待评价订单
	 */
	public Page<Trade> findWaitReview(Member member, Boolean hasExpired, Pageable pageable);

	Long findWaitReviewCount(Member member, Boolean hasExpired);
	
	/**
	 * 查找会员退货订单
	 */
	public Page<Trade> findRefund(Member member, Boolean hasExpired, Pageable pageable);
	
	Long findRefundCount(Member member, Boolean hasExpired);

	/**
	 * @Title：findMemberCount
	 * @Description：
	 * @param member
	 * @param orderStatus
	 * @param paymentStatus
	 * @param shippingStatus
	 * @param hasExpired
	 * @return Long
	 */
	Long findMemberCount(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	/**
	 * @Title：findCustomer
	 * @Description：消费者 订单
	 * @param member
	 * @param orderStatus
	 * @param paymentStatus
	 * @param shippingStatus
	 * @param hasExpired
	 * @param pageable
	 * @return Page<Trade>
	 */
	Page<Trade> findCustomer(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	List<Trade> getTradList(ShippingStatus shippingStatus,Date overDate,Integer first,Integer count);
	
	/**
	 * 查询为打款的订单
	 * @param confirmDate
	 * @return List<Trade>
	 */
	List<Trade> getTradList(Date confirmDate);
	/**
	 * 根据商家列表查找子订单列表
	 * @param tenants
	 * @return
	 */
	public List<Trade> findTradeListByTenants(List<Tenant> tenants);
	
	public List<Trade> findList(Tenant tenant,OrderStatus orderStatus);
	
	public List<Trade> findListByExport(Member member, Date beginDate, Date endDate, String keywords,Pageable pageable);
	public List<Trade> findListByExport(Member member, QueryStatus queryStatus,Date beginDate, Date endDate, String keywords,Pageable pageable);
	public List<Trade> findTradeByTenant(Tenant tenant, Date beginDate, Date endDate, String keywords,Boolean status,String type);
	public Page<Trade> findFavorableList(Tenant tenant,String keywords,Coupon coupon,Pageable pageable);
	public Page<Trade> findTradeTotal(Tenant tenant, QueryStatus queryStatus,Date beginDate, Date endDate,String keywords,Coupon coupon, Pageable pageable);
	public Long  findTradeYesterdayTotal(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,String begin_date,String end_date);//昨日交易金额
	public List<Trade> sevenDayTradingList(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,Date begin_date,Date end_date);
    public List<Trade> findUnshippedList(Tenant tenant);
	public List<Trade> findUnshippedListExport(Tenant tenant,QueryStatus queryStatus);

	Page<Trade> findPage(String consignee, Area area, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate, String keyword,String tenantName,String userName, Pageable pageable);

	long count(Tenant tenant,Date beginDate,Date endDate,QueryStatus queryStatus);
}