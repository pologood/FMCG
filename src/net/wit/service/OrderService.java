/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Order.OrderSource;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.exception.BalanceNotEnoughException;

/**
 * Service - 订单
 * @author rsico Team
 * @version 3.0
 */
public interface OrderService extends BaseService<Order, Long> {

	Member getExtensionCurrent();
	/** 释放过期订单库存 */
	void releaseStock();

	/**
	 * 生成订单
	 * @param cart 购物车
	 * @param receiver 收货地址
	 * @param paymentMethod 支付方式
	 * @param shippingMethod 配送方式
	 * @param couponCode 优惠码
	 * @param isInvoice 是否开据发票
	 * @param invoiceTitle 发票抬头
	 * @param useBalance 是否使用余额
	 * @param memo 附言
	 * @return 订单
	 */
	Order build(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Set<CouponCode> couponCodes, boolean isInvoice, String invoiceTitle, boolean useBalance, Long [] tenantIds,String [] memo);

	/** @see OrderService#build(Cart, Receiver, PaymentMethod, ShippingMethod, CouponCode, boolean, String, boolean, String) */
	Order create(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Set<CouponCode> couponCodes, boolean isInvoice, String invoiceTitle, boolean useBalance, Long [] tenantIds, String [] memo, List<DeliveryCenter> deliveryCenters, Member operator, Appointment appointment,
						OrderSource orderSource, Equipment equipment, String token_key);

	/**
	 * 更新订单
	 * @param order 订单
	 * @param operator 操作员
	 */
	void update(Order order, String operator);

	/**
	 * 调价
	 * @param order 调价
	 * @param operator 操作员
	 */
	void updatePrice(Trade trade,BigDecimal amount, BigDecimal freight, String operator);

    //给供应商打款
	void clearingToSupplier(Trade trade);
    //给供应商退款
	void refundsFromSupplier(SpReturns spReturns);
	/**
	 * 订单确认
	 * @param order 订单
	 * @param operator 操作员
	 */
	void confirm(Order order, Member operator);

	void clearing(Order order, Trade trade, Member operator, Boolean saved);
	
	/**
	 * 订单签收
	 * @param order 订单
	 * @param trade 子订单
	 * @param operator 操作员
	 */
	void sign(Order order, Trade trade, Member operator);
	
	/**
	 * 自动订单签收
	 * @param order 订单
	 * @param trade 子订单
	 * @param operator 操作员
	 */
	void jobSign(Long tradeId);

	/**
	 * 买家申请退货
	 * @param order 订单
	 * @param trade 子订单
	 * @param operator 操作员
	 */
	void spReturns(Trade trade,SpReturns spReturns, Member operator);
	/**
	 * 买家同意退货
	 * @param order 订单
	 * @param trade 子订单
	 * @param operator 操作员
	 * @throws BalanceNotEnoughException 
	 */
	void spConfirm(Trade trade,SpReturns spReturns, Member operator) throws BalanceNotEnoughException;
	/**
	 * 买家拒绝退货
	 * @param order 订单
	 * @param trade 子订单
	 * @param operator 操作员
	 * @throws BalanceNotEnoughException 
	 */
	void spRejected(Trade trade,SpReturns spReturns, Member operator);
	/**
	 * 订单完成
	 * @param order 订单
	 * @param operator 操作员
	 */
	void complete(Trade trade, Member operator);

	/**
	 * 订单取消
	 * @param order 订单
	 * @param operator 操作员
	 */
	void cancel(Order order, Member operator);

	/**
	 * 订单取消
	 * @param order 订单
	 * @param operator 操作员
	 */
	void cancel(Trade trade, Member operator);

	/**
	 * 订单支付
	 * @param order 订单
	 * @param payment 收款单
	 * @param operator 操作员
	 */
	void payment(Order order, Payment payment, Member operator);

	/**
	 * 订单支付
	 * @param order 订单
	 * @param payment 收款单
	 * @param operator 操作员
	 */
	void payment(Trade trade, Payment payment, Member operator);

	/**
	 * 线下缴款
	 * @param order 订单
	 * @param payment 收款单
	 * @param operator 操作员
	 */
	void payment(Trade trade,Member operator);

	/**
	 * 订单退款-并立即退款至会员帐户
	 * @param order 订单
	 * @param refunds 退款单
	 * @param operator 操作员
	 */
	void refunds(Trade trade, Refunds refunds, Member operator) throws Exception;

	/**
	 * 订单发货
	 * @param order 订单
	 * @param shipping 发货单
	 * @param operator 操作员
	 */
	void shipping(Order order, Shipping shipping, Member operator);

	/**
	 * 订单退货
	 * @param order 订单
	 * @param returns 退货单
	 * @param operator 操作员
	 */
	void returns(Trade trade, Returns returns, Member operator) throws Exception;

	/**
	 * 查找店铺订单分页
	 * @param member 会员
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param pageable 分页信息
	 * @return 订单分页
	 */
	Page<Order> findPage(Member member, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 查找我的订单分页
	 * @param member 会员
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param pageable 分页信息
	 * @return 订单分页
	 */
	Page<Order> findPageMember(Member member, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 查找我的订单分页
	 * @param member 会员
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param pageable 分页信息
	 * @return 订单分页
	 */
	public Page<Order> findPageAll(Member member, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 查询订单数量
	 * @param member 业务员
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @return 订单数量
	 */
	public long countmy(Member member, OrderStatus OrderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	/**
	 * 查询我的订单数量
	 * @param member 业务员
	 * @return 订单list
	 */
	public List<Order> myOrder(Member member);

	public Page<Order> findPageTenant(Member member, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 根据订单编号查找订单
	 * @param sn 订单编号(忽略大小写)
	 * @return 若不存在则返回null
	 */
	public Order findBySn(String sn);

	/**
	 * 发送订单提醒
	 * @param order 订单信息
	 */
	void pushTo(Order order);

	/**
	 * 发送订单提醒
	 * @param order 订单信息
	 */
	void pushTo(Trade trade);
	
	
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
	 * 查询订单
	 * @param tenant 子订单商家
	 * @param startDate 订单生成时间范围
	 * @param endDate 订单生成时间范围
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 发货状态
	 * @return
	 */
	List<Order> findList(Tenant tenant, Date startDate, Date endDate, List<OrderStatus> orderStatuses, List<ShippingStatus> shippingStatuses);

	/**
	 * 查找订单分页
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return 订单分页
	 */
	Page<Order> findPage(Member member, Pageable pageable);

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
	 * 查找会员代付款订单
	 * @param member 会员
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findWaitPay(Member member, Boolean hasExpired, Pageable pageable);

	/**
	 * 查找会员待发货订单
	 * @param member 会员
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Order> findWaitShipping(Member member, Boolean hasExpired, Pageable pageable);

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
	 * 查询订单数量
	 * @param member 业务员
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @return 订单数量
	 */
	Long countAll(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

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
	 * 获取会员购买量
	 * @param member 会员
	 * @param product 商品
	 * @return 购买量
	 */
	Integer getMemberBuyQuantity(Member member, Product product);

	/**
	 * 根据状态过滤订单分页
	 * @param member 会员
	 * @param orderStatus
	 * @param paymentStatus
	 * @param shippingStatus
	 * @param pageable
	 * @return
	 */
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
	 * @return Object
	 */
	Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate, Pageable pageable);

	Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate,List<Tenant> tenants, Pageable pageable);
	
	Page<Order> findPage(Area area,OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate,String keyword, Pageable pageable);

	boolean isLimit(Member member,Integer count);
	//业务员销售金额
	BigDecimal countmySales(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus);


	/**
	 * 根据token_key查询购物屏下单
	 * @param token_key
	 * @return Order
	 */
	Order findByTokenKey(String token_key);
}