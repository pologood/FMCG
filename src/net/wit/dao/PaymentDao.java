/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Trade;

/**
 * Dao - 收款单
 * @author rsico Team
 * @version 3.0
 */
public interface PaymentDao extends BaseDao<Payment, Long> {

	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Payment findBySn(String sn);

	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Payment findByPaySn(String paySn);
	/**
	 * 根据子订单查询付款单
	 */
	Payment findByTrade(Trade trade,Payment.Status status);
	Page<Payment> findPage(Member member, Pageable pageable, Payment.Type type);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param paymentMethod
	 * @param type
	 * @param endDate 
	 * @param beginDate 
	 * @param pageable
	 * @return Page<Payment>
	 */
	Page<Payment> findPage(String paymentMethod, Payment.Type type, Date beginDate, Date endDate,String tenantName, String username, Pageable pageable);

	/**
	 * findWaitReleaseList
	 */
	List<Payment> findWaitReleaseList(Integer first, Integer count);

	/**
	 * 根据买单立减查询账单
	 */
	Payment findByPayment(PayBill payBill);
}