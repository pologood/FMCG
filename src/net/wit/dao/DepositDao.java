/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Deposit;
import net.wit.entity.Deposit.Type;
import net.wit.entity.Member;
import net.wit.entity.Payment;

/**
 * Dao - 预存款
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface DepositDao extends BaseDao<Deposit, Long> {

	/**
	 * 查找预存款分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 预存款分页
	 */
	Page<Deposit> findPage(Member member, Pageable pageable);

	/**
	 * 查找预存款分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 预存款分页
	 */
	Page<Deposit> findPage(Member member,Date beginTime, Date endTime, Pageable pageable,Type type);
	
	List<Deposit> findList(Member member, Date beginTime, Date endTime,Type type);

	List<Map<String,Object>> findMapList(Member member,String type, Date beginTime, Date endTime);
	
	/**
	 * @Title：income
	 * @Description：
	 * @param member
	 * @return  BigDecimal
	 */
	BigDecimal income(Member member,Type type,Date beginTime, Date endTime, Deposit.Status status);

	/**
	 * @Title：outcome
	 * @Description：
	 * @param member
	 * @return  BigDecimal
	 */
	BigDecimal outcome(Member member,Type type,Date beginTime, Date endTime, Deposit.Status status);

}