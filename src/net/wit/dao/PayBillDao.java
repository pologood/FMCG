package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.entity.Member;

import net.wit.entity.PayBill;
import net.wit.entity.Payment;
import net.wit.entity.Tenant;

/**P
 * Created by Administrator on 2016/9/6.
 */
public interface PayBillDao extends BaseDao<PayBill, Long> {

	/**
	 * 优惠买单统计
	 */
	 Page<PayBill> findMyPage(Tenant tenant,Date startDate,Date endDate,String keywords,PayBill.Status status,PayBill.Type type,Pageable pageable);
	/**
	 * 优惠买单统计（导出）
	 */
	 List<PayBill> findMyList(Tenant tenant,Date startDate,Date endDate,String keywords,PayBill.Status status,PayBill.Type type);

    boolean isLimit(Member member, Integer count);

	/**
	 * 统计今日收款
	 */
	BigDecimal findPayBillSum(Tenant tenant, PayBill.Type type , PayBill.Status status, Date begin_date, Date end_date);

	Page<PayBill> findPage(String tenantName, String username, String paymentMethod, Date beginDate, Date endDate, Pageable pageable);
}
