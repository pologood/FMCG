package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.QueryStatus;
import net.wit.entity.Order.ShippingStatus;

/**
 * @ClassName: TradeApply
 * @Description: 申请信息
 * @author Administrator
 * @date 2014年8月15日 上午10:16:06
 */
public interface TradeService extends BaseService<Trade, Long> {

	/**
	 * 根据验证码查找订单
	 * @param code 验证码(忽略大小写)
	 * @return 若不存在则返回null
	 */
	public Trade findBySn(String code);

	/**
	 * 根据验证码查找订单
	 * @param code 验证码(忽略大小写)
	 * @return 若不存在则返回null
	 */
	public Trade findBySn(String code, Tenant tenant);

	Page<Trade> order(Tenant supplier, Tenant seller, Date begin_date, Date end_date, String keyword,Pageable pageable);

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
	public long yesterdayAmountTotal(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,Date begin_date,Date end_date);//昨日交易金额
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
	public Page<Trade> findPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findPage(Tenant tenant, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean returnStatus,String waitShip, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findPage(Member member, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean unReview, Pageable pageable);
	/**
	 * 查找订单分页
	 * @param orderStatus 订单状态
	 * @param paymentStatus 支付状态
	 * @param shippingStatus 配送状态
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findPage(Member member, Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

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
	public Page<Trade> findMemberPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	public Long findMemberCount(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	/**
	 * 查找会员代付款订单
	 * @param member 会员
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findWaitPay(Member member, Boolean hasExpired, Pageable pageable);

	public Long findWaitPayCount(Member member, Boolean hasExpired);

	/**
	 * 查找会员待发货订单
	 * @param member 会员
	 * @param hasExpired 是否已过期
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findWaitShipping(Member member, Boolean hasExpired, Pageable pageable);

	public Long findWaitShippingCount(Member member, Boolean hasExpired);

	/**
	 * 查找会员待签收订单
	 */
	public Page<Trade> findWaitSign(Member member, Boolean hasExpired, Pageable pageable);

	public Long findWaitSignCount(Member member, Boolean hasExpired);

	/**
	 * 查找会员待评价订单
	 */
	public Page<Trade> findWaitReview(Member member, Boolean hasExpired, Pageable pageable);

	public Long findWaitReviewCount(Member member, Boolean hasExpired);

	/**
	 * 统计月销售额
	 * @param tenant 商家
	 * @param queryDate 统计日期
	 * @return 商品分页
	 */
	BigDecimal countSales(Tenant tenant,String queryDate);
	/**
	 * 根据会员查找子订单
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return
	 */
	public Page<Trade> findPage(Member member, Pageable pageable);

	/**
	 * 查找订单分页
	 * @param member 会员
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @param pageable 分页信息
	 * @return 订单分页
	 */
	public Page<Trade> findPage(Member member, Date beginDate, Date endDate, Pageable pageable);
	
	//添加了关键字参数
	public Page<Trade> findPage(Member member, Date beginDate, Date endDate, String keywords,Pageable pageable);
	//添加了查询状态参数
	public Page<Trade> findPage(Member member,QueryStatus queryStatus, Date beginDate, Date endDate, String keywords,Pageable pageable);

	public long countTenant(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired);

	public long count(Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shipping,Boolean returnStatus, Boolean hasExpired);

	/**
	 * @Title：findCustomer
	 * @Description：查找 消费者的订单
	 * @param member
	 * @param object
	 * @param object2
	 * @param object3
	 * @param object4
	 * @param pageable
	 * @return Object
	 */
	public Page<Trade> findCustomer(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable);

	List<Trade> getTradList(ShippingStatus shippingStatus,Date overDate,Integer first,Integer count);
	
	public void setComplete(int overDay);
	
	/**
	 * 根据商家查找订单列表
	 * @param tenants 商家列表
	 * @return 订单列表
	 */
	public List<Order> findOrdersByTenants(List<Tenant> tenants);
	
	/**统计商家的销售额*/
	public BigDecimal countSales(Tenant tenant,OrderStatus orderStatus,Boolean currentMonth);
	
	/**
	 * 查找会员退货订单
	 */
	public Page<Trade> findRefund(Member member, Boolean hasExpired, Pageable pageable);

	/**
	 *	确认订单
	 * @param trade
	 * @param order
     * @return
     */
	public void confirmOrder(Trade trade,Order order);
	public Long findRefundCount(Member member, Boolean hasExpired);
	
	public List<Trade> findListByExport(Member member, Date beginDate, Date endDate, String keywords,Pageable pageable);
	public List<Trade> findListByExport(Member member,QueryStatus queryStatus, Date beginDate, Date endDate, String keywords,Pageable pageable);
	public List<Trade> findTradeByTenant(Tenant tenant, Date beginDate, Date endDate, String keywords,Boolean status,String type);
	public Page<Trade> findFavorableList(Tenant tenant,String keywords,Coupon coupon, Pageable pageable);//只针对优惠券的接口
	public Page<Trade> findTradeTotal(Tenant  tenant, QueryStatus queryStatus,Date beginDate, Date endDate,String keywords,Coupon coupon, Pageable pageable);//只针对优惠券的接口
	public List<Map<String, Object>> sevenDayTradingCount(List<Filter> filters, Tenant tenant, ShippingStatus shippingStatus, String keyword, Date begin_date, Date end_date);
    public List<Trade> findUnshippedList(Tenant tenant);
	public Map<String, Object> findTradingCount(List<Filter> filters, Tenant tenant, ShippingStatus shippingStatus, String keyword, Date begin_date, Date end_date);
	public List<Trade> findUnshippedListExport(Tenant tenant,QueryStatus queryStatus);

	Page<Trade> findPage(String consignee,Area area,OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate,String keyword, String tenantName,String userName, Pageable pageable);
}
