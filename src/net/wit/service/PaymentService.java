/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

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
 * Service - 收款单
 * @author rsico Team
 * @version 3.0
 */
public interface PaymentService extends BaseService<Payment, Long> {

	/**
	 * 根据编号查找收款单 
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Payment findBySn(String sn);
	/**
	 * 根据子订单查询付款单
	 */
	Payment findByTrade(Trade trade,Payment.Status status);
	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Payment findByPaySn(String paySn);

	/**
	 * 支付处理
	 * @param payment 收款单
	 */
	void handle(Payment payment) throws Exception;


	/**
	 * 支付关闭
	 * @param payment 收款单
	 */
	void close(Payment payment) throws Exception;
	void opService(Payment payment) throws Exception;
	/**
	 * 待支付查询
	 * @param payment 收款单
	 */
	List<Payment> findWaitReleaseList(Integer first, Integer count);

	Page<Payment> findPage(Member member, Pageable pageable, Payment.Type type);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param method
	 * @param status
	 * @param endDate
	 * @param beginDate
	 * @param pageable
	 * @return Object
	 */
	Page<Payment> findPage(Method method, Status status, Date beginDate, Date endDate,String keyword,   Pageable pageable);

	/**
	 * 根据买单立减查询账单
	 */
	Payment findByPayMent(PayBill payBill);
}