package net.wit.job;

import java.util.Calendar;

import javax.annotation.Resource;
import javax.mail.internet.ParseException;

import net.wit.service.OrderService;
import net.wit.service.TradeService;

import org.junit.Test;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job - 商家打款
 * 
 * @author shenjc
 * @version 1.0
 */
@Component("clearingOrderJob") 
@Lazy(false)
public class ClearingOrderJob {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	
	/**
	 * 结算已完成的订单
	 */
	@Scheduled(cron = "${job.clearingOrder_evict_expired.cron}")
	public void clearingOrder() {
		//orderService.clearingOrder();
	}
	
	
	
//	@Test
//	public void testCreateSettlement() throws ParseException {
//		 Calendar   cal1   =   Calendar.getInstance();
//	     cal1.add(Calendar.DATE,-2);
//	     System.out.println(cal1.getTime());
//	}
}