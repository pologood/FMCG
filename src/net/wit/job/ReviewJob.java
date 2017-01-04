package net.wit.job;

import net.wit.entity.Review;
import net.wit.entity.Trade;
import net.wit.service.ReviewService;
import net.wit.service.TradeService;
import org.junit.Test;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Job - 供应商后台
 * Created by WangChao on 2016-09-03.
 */
@Component("reviewJob")
@Lazy(false)
public class ReviewJob {
	
	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	
	private Integer overDay=15;//设置过期天数
	
	/**
	 * 评价过期的订单
	 */
	@Scheduled(cron = "${job.review_evict_expired.cron}")
	public void evictExpired() {
		Calendar   calendar   =   Calendar.getInstance();
		calendar.add(Calendar.DATE,-overDay);
		List<Trade> list = tradeService.findUnreviewList(null,null,calendar.getTime(),null,null);
		for(Trade trade:list){
			Review review=new Review();
			review.setFlag(Review.Flag.trade);
			review.setScore(5);
			review.setAssistant(5);
			review.setContent("系统默认好评");
			review.setIsShow(false);
			review.setIp("127.0.0.1");
			review.setMember(trade.getOrder().getMember());
			review.setType(Review.Type.positive);
			reviewService.reviewTrade("weixin", trade.getOrder().getMember(), trade, null, review);
		}
	}
	
	
	
	@Test
	public void test() {
		Date date=new Date();
		System.out.println(date);
		date.setDate(date.getDate()-15);
		System.out.println(date);
	}
}
