package net.wit.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Payment;
import net.wit.entity.Tenant;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface PayBillService extends BaseService<PayBill, Long> {

    void upgrade(PayBill payBill);

    /**
     * 买单立减
     *
     * @param payment  收款单
     * @param operator 操作员
     */
    void payment(Payment payment, Member operator);

    boolean isLimit(Member member, Integer count);
	/**
	 * 优惠买单统计
	 */
	 Page<PayBill> findMyPage(Tenant tenant, Date startDate, Date endDate, String keywords, PayBill.Status status, PayBill.Type type, Pageable pageable);
	/**
	 * 优惠买单统计(导出)
	 */
	 List<PayBill> findMyList(Tenant tenant,Date startDate,Date endDate,String keywords,PayBill.Status status,PayBill.Type type);
	/**
	 * 统计今日收款
	 */
	BigDecimal findPayBillSum(Tenant tenant, PayBill.Type type , PayBill.Status status, Date begin_date, Date end_date);

	/**
	 * 优惠买单
	 *
	 * @param username  会员名
	 * @param tenantName 店铺名
	 */
	Page<PayBill> findPage(String tenantName, String username, String paymentMethod,Date beginDate, Date endDate, Pageable pageable);
}
