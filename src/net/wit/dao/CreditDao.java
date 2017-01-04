/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;

/**
 * Dao - 付款单
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface CreditDao extends BaseDao<Credit, Long> {

	/**
	 * 根据编号查找付款单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 付款单，若不存在则返回null
	 */
	Credit findBySn(String sn);

	Page<Credit> findPage(Member member, Pageable pageable, Credit.Type type );
	
	Page<Credit> findPage(Member member, Date beginDate, Date endDate, Credit.Type type, Pageable pageable);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param method
	 * @param status
	 * @param endDate 
	 * @param beginDate 
	 * @param pageable
	 * @return  Page<Credit>
	 */
	Page<Credit> findPage(Method method, Status status, Date beginDate, Date endDate,Date beginDates, Date endDates,String searchValue, Pageable pageable);
}