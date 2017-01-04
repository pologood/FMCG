/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;

/**
 * Service - 收款单
 * @author rsico Team
 * @version 3.0
 */
public interface CreditService extends BaseService<Credit, Long> {

	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Credit findBySn(String sn);

	Page<Credit> findPage(Member member, Pageable pageable, Credit.Type type);
	Page<Credit> findPage(Member member, Date beginDate, Date endDate, Credit.Type type, Pageable pageable);

	void saveAndUpdate(Credit credit) throws Exception;

	void saveAndRefunds(Credit credit) throws Exception;

	void checkPayment();

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
	Page<Credit> findPage(Method method, Status status, Date beginDate, Date endDate,Date beginDates, Date endDates,String searchValue, Pageable pageable);
}