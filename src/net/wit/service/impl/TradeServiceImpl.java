package net.wit.service.impl;
import java.text.ParseException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import net.wit.dao.OrderDao;
import net.wit.dao.OrderItemDao;
import net.wit.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TradeDao;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.QueryStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Payment.Status;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.ShippingService;
import net.wit.service.TradeService;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: www.insuper.com
 * </p>
 * <p>
 * Company: www.insuper.com
 * </p>
 * @author liumx
 * @version 1.0
 * @date 2013年7月2日15:46:16
 */
@Service("tradeServiceImpl")
public class TradeServiceImpl extends BaseServiceImpl<Trade, Long> implements TradeService {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	
	@Resource(name = "tradeDaoImpl")
	private TradeDao tradeDao;

	@Resource(name = "orderItemDaoImpl")
	private OrderItemDao orderItemDao;
	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Resource(name = "tradeDaoImpl")
	public void setBaseDao(TradeDao tradeDao) {
		super.setBaseDao(tradeDao);
	}

	@Transactional(readOnly = true)
	public Trade findBySn(String sn) {
		return tradeDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public Trade findBySn(String sn, Tenant tenant) {
		return tradeDao.findBySn(sn, tenant);
	}

	/**
	 * 供应商后台订单查询
	 * @param supplier 供应商
	 * @param seller 销售商
	 * @param begin_date 开始日期
	 * @param end_date 结束日期
	 * @param pageable 分页信息
     * @return 订单分页
     */
	public Page<Trade> order(Tenant supplier, Tenant seller, Date begin_date, Date end_date,String keyword, Pageable pageable){
		Set<Trade> tradeSet=new HashSet<>();
		if(supplier!=null){
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("supplier", Filter.Operator.eq,supplier));
			List<OrderItem> orderItems=orderItemDao.findList(null,null,filters,null);
			for(OrderItem orderItem:orderItems){
					tradeSet.add(orderItem.getTrade());
			}
		}else{
			tradeSet=null;
		}
		return tradeDao.order(tradeSet,seller,begin_date,end_date,keyword,pageable);
	}

	public List<Trade> findUnreviewList(Integer first,Integer count,Date overDate,List<Filter> filters,List<net.wit.Order> orders){
		return tradeDao.findUnreviewList(first,count,overDate,filters,orders);
	}

	/**
	 * 查找会员订单 V2
	 * @param queryStatus 订单状态
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findPage(Pageable pageable, Member member, QueryStatus queryStatus, String keyword) {
		return tradeDao.findPage(pageable, member, queryStatus, keyword);
	}
	public long count(List<Filter> filters, Member member, QueryStatus queryStatus, String keyword) {
		return tradeDao.count(filters, member, queryStatus, keyword);
	}
	/**
	 * 查找商家订单 V2
	 * @param queryStatus 订单状态
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	public Page<Trade> findPage(Pageable pageable, Tenant tenant, QueryStatus queryStatus, String keyword) {
		return tradeDao.findPage(pageable, tenant, queryStatus, keyword);
	}

	@Override
	public void confirmOrder(Trade trade, Order order) {
		if (trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
			trade.setOrderStatus(OrderStatus.confirmed);
			List<Trade> listTrade = order.getTrades();
			int i = 0;
			for (Trade tra : listTrade) {
				if (tra.getOrderStatus().equals(OrderStatus.unconfirmed)) {
					i++;
				}
			}
			if (i == 0) {
				order.setOrderStatus(OrderStatus.confirmed);
			}
			tradeDao.merge(trade);
			orderDao.merge(order);
		}
	}

	public long count(List<Filter> filters, Tenant tenant, QueryStatus queryStatus, String keyword) {
		return tradeDao.count(filters, tenant, queryStatus, keyword);
	}
	public long tradeyesterdayCount(List<Filter> filters, Tenant tenant,QueryStatus queryStatus, String keyword,Date begin_date,Date end_date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String start_time = sdf.format(new Date());
		String end_time = sdf.format(new Date().getTime()-1 * 24 * 60 * 60 * 1000);
		try {
			begin_date = sdf.parse(start_time);
		}catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		try {
			end_date = sdf.parse(end_time);
		}catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}

		return tradeDao.tradeyesterdayCount(filters, tenant, queryStatus, keyword,begin_date,end_date);
	}
	public long yesterdayAmountTotal(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,Date begin_date,Date end_date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String start_time = sdf.format(new Date());
		String end_time = sdf.format(new Date().getTime()-1 * 24 * 60 * 60 * 1000);

		return tradeDao.findTradeYesterdayTotal(filters, tenant, shippingStatus, keyword,start_time,end_time);

	}
	public Page<Trade> findPage(Pageable pageable, Tenant tenant,Date begin_date,Date end_date, QueryStatus queryStatus, String keyword) {
		return tradeDao.findPage(pageable, tenant, begin_date,end_date,queryStatus, keyword);
	}
	@Transactional(readOnly = true)
	public Page<Trade> findPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findPage(member, orderStatus,paymentStatus, shippingStatus, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Trade> findPage(Tenant tenant, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean returnStatus,String waitShip, Pageable pageable) {
		return tradeDao.findPage(tenant, keyword, orderStatus, paymentStatus, shippingStatus, hasExpired,returnStatus,waitShip, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Trade> findPage(Member member, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean unReview, Pageable pageable) {
		return tradeDao.findPage(member, keyword, orderStatus, paymentStatus, shippingStatus, hasExpired,unReview, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Trade> findPage(Member member, Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findPage(member, tenant, orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Trade> findMemberPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findMemberPage(member, orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
	}

	@Transactional(readOnly = true)
	public Long findMemberCount(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
		return tradeDao.findMemberCount(member, orderStatus, paymentStatus, shippingStatus, hasExpired);
	}

	@Transactional(readOnly = true)
	public Page<Trade> findPage(Member member, Date beginDate, Date endDate, Pageable pageable) {
		return tradeDao.findPage(member, beginDate, endDate, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Trade> findPage(Member member, QueryStatus queryStatus,Date beginDate, Date endDate, String keywords,Pageable pageable) {
		return tradeDao.findPage(member,queryStatus, beginDate, endDate,keywords, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Trade> findPage(Member member, Date beginDate, Date endDate, String keywords,Pageable pageable) {
		return tradeDao.findPage(member, beginDate, endDate,keywords, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Trade> findPage(Member member, Pageable pageable) {
		return tradeDao.findPage(member, pageable);
	}

	public long countTenant(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
		return tradeDao.countTenant(member, orderStatus, paymentStatus, shippingStatus, hasExpired);
	}
	
	public long count(Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus,Boolean returnStatus, Boolean hasExpired) {
		return tradeDao.count(tenant, orderStatus, paymentStatus, shippingStatus,returnStatus, hasExpired);
	}
	
	public BigDecimal countSales(Tenant tenant,String queryDate){
		return tradeDao.countSales(tenant,queryDate);
	}

	/** 查找会员代付款订单 */
	public Page<Trade> findWaitPay(Member member, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findWaitPay(member, hasExpired, pageable);
	}

	/** 查找会员待发货订单 */
	public Page<Trade> findWaitShipping(Member member, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findWaitShipping(member, hasExpired, pageable);
	}

	/**
	 * 查找会员待签收订单
	 */
	public Page<Trade> findWaitSign(Member member, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findWaitSign(member, hasExpired, pageable);
	}

	/**
	 * 查找会员待评价订单
	 */
	public Page<Trade> findWaitReview(Member member, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findWaitReview(member, hasExpired, pageable);
	}
	
	@Override
	public Page<Trade> findRefund(Member member, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findRefund(member, hasExpired, pageable);
	}
	
	public Long findWaitPayCount(Member member, Boolean hasExpired) {
		return tradeDao.findWaitPayCount(member, hasExpired);
	}

	@Override
	public Long findWaitShippingCount(Member member, Boolean hasExpired) {
		return tradeDao.findWaitShippingCount(member, hasExpired);
	}

	@Override
	public Long findWaitSignCount(Member member, Boolean hasExpired) {
		return tradeDao.findWaitSignCont(member, hasExpired);
	}

	@Override
	public Long findWaitReviewCount(Member member, Boolean hasExpired) {
		return tradeDao.findWaitReviewCount(member, hasExpired);
	}
	
	@Override
	public Long findRefundCount(Member member, Boolean hasExpired) {
		return tradeDao.findRefundCount(member, hasExpired);
	}

	@Transactional(readOnly = true)
	public Page<Trade> findCustomer(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		return tradeDao.findCustomer(member, orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
	}

	public void setComplete(int overDay){
		try {
			overDay = -overDay;//设置成负数
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");//时间格式化
			Calendar   calendar   =   Calendar.getInstance();
			calendar.add(Calendar.DATE,overDay);
		    List<Trade> overCompleteList;
			overCompleteList = tradeDao.getTradList(ShippingStatus.shipped, calendar.getTime(),null,null);
		    if (overCompleteList == null || overCompleteList.isEmpty()) {
		    	return;
			}
		    for (Trade trade : overCompleteList) {
		    	
		    	Order order = trade.getOrder();
				//if (isSign && !isWaitSign && order != null && trade.getShippingStatus() == ShippingStatus.shipped && !order.isExpired()) {
		    	if (order != null && trade.getShippingStatus() == ShippingStatus.shipped) {
					if (order.isLocked(null)) {
						continue;
					}
					try{
						orderService.sign(order, trade, null);
						System.out.println("订单号："+order.getSn()+"被自动签收。签收时间："+new Date());
					}catch(Exception ex){
						System.out.println("订单号："+order.getSn()+"签收失败。签收时间："+new Date()+"原因：");
						ex.printStackTrace();
						continue;
					}
				}
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Order> findOrdersByTenants(List<Tenant> tenants) {
		List<Order> orders=new ArrayList<Order>();
		for(Trade trade:tradeDao.findTradeListByTenants(tenants)){
			orders.add(trade.getOrder());
		}
		return orders;
	}

	public List<Trade> getTradList(ShippingStatus shippingStatus,Date overDate,Integer first,Integer count) {
		return tradeDao.getTradList(shippingStatus, overDate,first,count);
	}
	@Override
	public BigDecimal countSales(Tenant tenant, OrderStatus orderStatus,Boolean currentMonth) {
		BigDecimal monthSales=new BigDecimal(0);
		Calendar calendar = Calendar.getInstance();
		int month=calendar.get(Calendar.MONTH);
		List<Trade> trades=tradeDao.findList(tenant, orderStatus);
		for (Trade trade : trades) {
			calendar.setTime(trade.getCreateDate());
			int tradeMonth=calendar.get(Calendar.MONTH);
			if(currentMonth){
				if(month==tradeMonth){
					Order order=trade.getOrder();
					if(order!=null&&order.getOrderStatus()==OrderStatus.completed&&order.getShippingStatus()==ShippingStatus.shipped){
						List<Payment> Payments=new ArrayList<Payment>(order.getPayments());
						for (Payment payment : Payments) {
							if(payment.getStatus()==Status.success){
								monthSales=monthSales.add(payment.getEffectiveAmount());
							}
						}
					}
				}
			}else{
				Order order=trade.getOrder();
				if(order!=null&&order.getOrderStatus()==OrderStatus.completed){
					List<Payment> Payments=new ArrayList<Payment>(order.getPayments());
					for (Payment payment : Payments) {
						if(payment.getStatus()==Status.success){
							monthSales=monthSales.add(payment.getEffectiveAmount());
						}
					}
				}
			}
		}
		return monthSales;
	}
	
	public List<Trade> findListByExport(Member member, Date beginDate, Date endDate, String keywords,Pageable pageable){
		return tradeDao.findListByExport(member,beginDate,endDate, keywords,pageable);
	};
	public List<Trade> findListByExport(Member member,QueryStatus queryStatus, Date beginDate, Date endDate, String keywords,Pageable pageable){
		return tradeDao.findListByExport(member,queryStatus,beginDate,endDate, keywords,pageable);
	};
	public List<Trade> findTradeByTenant(Tenant tenant, Date beginDate, Date endDate, String keywords,Boolean status,String type){
		return tradeDao.findTradeByTenant(tenant,beginDate,endDate, keywords,status,type);
	};
	public Page<Trade> findFavorableList(Tenant tenant, String keywords,Coupon coupon ,Pageable pageable){
		return tradeDao.findFavorableList(tenant,keywords,coupon ,pageable);
	};
	public Page<Trade> findTradeTotal(Tenant tenant, QueryStatus queryStatus,Date beginDate, Date endDate,String keywords,Coupon coupon, Pageable pageable){
		return tradeDao.findTradeTotal(tenant,queryStatus,beginDate,endDate,keywords,coupon,pageable);
	}

	public List<Map<String, Object>> sevenDayTradingCount(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,Date begin_date,Date end_date){

		List<Map<String,String>> mapsTime = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		int j=6;
		for(int i=7;i>=1;i--){
			Map<String, String> mapTime = new HashMap<>();
			String start_time = sdf.format(new Date().getTime()-i * 24 * 60 * 60 * 1000);
			String end_time = sdf.format(new Date().getTime()-j * 24 * 60 * 60 * 1000);
			mapTime.put("startTime",start_time);
			mapTime.put("endTime",end_time);
			mapsTime.add(mapTime);
			j--;
		}

		List<Map<String, Object>> maps = new ArrayList<>();
		for(Map<String,String> m:mapsTime){
			try {
				begin_date = sdf.parse(m.get("startTime"));
			}catch (ParseException e)
			{
				System.out.println(e.getMessage());
			}
			try {
				end_date = sdf.parse(m.get("endTime"));
			}catch (ParseException e)
			{
				System.out.println(e.getMessage());
			}

			List<Trade> list =  tradeDao.sevenDayTradingList(filters, tenant, shippingStatus, keyword, begin_date, end_date);
			BigDecimal priceTotal = BigDecimal.ZERO;
			BigDecimal incomeTotal = BigDecimal.ZERO;
			int  tradeSum = list.size();
			if(list.size()>0) {
				for (Trade trade:list) {
					priceTotal =priceTotal.add(trade.getPrice());//交易额
					incomeTotal = incomeTotal.add(trade.getAmount());//收入
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("tradeSum",tradeSum);
			map.put("priceTotal",priceTotal);
			map.put("incomeTotal",incomeTotal);
			maps.add(map);
		}

		return maps;
	}
	//根据时间取订单数量和订单交易额
	public Map<String, Object> findTradingCount(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,Date begin_date,Date end_date){

			List<Trade> list =  tradeDao.sevenDayTradingList(filters, tenant, shippingStatus, keyword, begin_date, end_date);
			BigDecimal priceTotal = BigDecimal.ZERO;
			BigDecimal incomeTotal = BigDecimal.ZERO;
			int  tradeSum = list.size();
			if(list.size()>0) {
				for (Trade trade:list) {
					priceTotal =priceTotal.add(trade.getPrice());//交易额
					incomeTotal = incomeTotal.add(trade.getAmount());//收入
				}
			}
			Map<String, Object> map = new HashMap<>();
			map.put("tradeSum",tradeSum);
			map.put("priceTotal",priceTotal);
			map.put("incomeTotal",incomeTotal);

		return map;
	}
    public List<Trade> findUnshippedList(Tenant tenant){
        return tradeDao.findUnshippedList(tenant);
    };
	public List<Trade> findUnshippedListExport(Tenant tenant,QueryStatus queryStatus){
		return tradeDao.findUnshippedListExport(tenant,queryStatus);
	};

	public Page<Trade> findPage(String consignee,Area area,OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate,String keyword, String tenantName,String userName, Pageable pageable){
		return tradeDao.findPage(consignee,area,orderStatus,paymentStatus,shippingStatus,hasExpired,beginDate,endDate,keyword,tenantName,userName,pageable);
	}

	public long count(Tenant tenant,Date beginDate,Date endDate,QueryStatus queryStatus){
		return tradeDao.count(tenant,beginDate,endDate,queryStatus);
	}
}
