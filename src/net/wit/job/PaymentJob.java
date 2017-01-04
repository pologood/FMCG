package net.wit.job;

import net.wit.entity.Payment;
import net.wit.entity.Review;
import net.wit.entity.Trade;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.ReviewService;
import net.wit.service.TradeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * Job - 供应商后台
 * Created by WangChao on 2016-09-03.
 */
@Component("paymentJob")
@Lazy(false)
public class PaymentJob {

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    private int first=0;
    private int count=50;
    private boolean isCompleted=false;

    /**
     * 评价过期的订单
     */
//    @Scheduled(cron = "${job.review_evict_expired.cron}")
//    public void evictExpired() {
//        List<Payment> payments = paymentService.findWaitReleaseList(0, 50);
//        if (payments != null) {
//            for (Payment payment : payments) {
//                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
//                if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
//                    try {
//                        paymentService.close(payment);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    String resultCode = paymentPlugin.queryOrder(payment);
//                    if (resultCode.equals("0000")) {
//                        try {
//                            paymentService.handle(payment);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else if (!resultCode.equals("9999")) {
//                        try {
//                            paymentService.close(payment);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }
}
