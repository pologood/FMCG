package net.wit.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.ParseException;

import net.wit.entity.Order;
import net.wit.entity.Trade;
import net.wit.entity.Order.ShippingStatus;
import net.wit.service.OrderService;
import net.wit.service.TradeService;
import net.wit.service.impl.TradeServiceImpl;

import org.junit.Test;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job - 系统自动签收
 * 
 * @author shenjc
 * @version 1.0
 */
@Component("acceptJob") 
@Lazy(false)
public class AcceptJob {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	
	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;
	
	private Integer overDay=7;//设置过期天数
	
	/**
	 * 签收过期的订单
	 */
	@Scheduled(cron = "${job.accept_evict_expired.cron}")
	public void evictExpired() {
		
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");//时间格式化
		Calendar   calendar   =   Calendar.getInstance();
		calendar.add(Calendar.DATE,-overDay);
	    List<Trade> overCompleteList;
		overCompleteList = tradeService.getTradList(ShippingStatus.shipped, calendar.getTime(),null,null);
	    if (overCompleteList == null || overCompleteList.isEmpty()) {
	    	return;
		}
	    for (Trade trade : overCompleteList) {
	    	Order order = trade.getOrder();
	    	if (order != null && trade.getShippingStatus() == ShippingStatus.shipped) {
				try{
					orderService.jobSign(trade.getId());
					//System.out.println("订单号："+order.getSn()+"被自动签收。签收时间："+new Date());
				}catch(Exception ex){
					System.out.println("订单号："+order.getSn()+"签收失败。签收时间："+new Date()+"原因：");
					//ex.printStackTrace();
					continue;
				}
			}
	    }
	}
	
	
	
//	@Test
//	public void testCreateSettlement() throws ParseException {
//		 Calendar   cal1   =   Calendar.getInstance();
//	     cal1.add(Calendar.DATE,-2);
//	     System.out.println(cal1.getTime());
//	}
}
