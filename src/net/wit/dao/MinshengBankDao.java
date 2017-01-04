/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.MinshengBank;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;

/**
 * Dao - 民生银行转账
 * @author rsico Team
 * @version 3.0
 */
public interface MinshengBankDao extends BaseDao<MinshengBank, Long> {

	/**
	 * 根据编号查找转账单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	MinshengBank findbyMinshengPayNo(String sn);

	Page<MinshengBank> findPage(Member member, Pageable pageable);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param method
	 * @param status
	 * @param endDate 
	 * @param beginDate 
	 * @param pageable
	 * @return Page<Payment>
	 */
	Page<MinshengBank> findPage(Method method, Status status, Date beginDate, Date endDate, Pageable pageable);

	/**
	 * findWaitReleaseList
	 */
	List<MinshengBank> findWaitReleaseList(Integer first, Integer count);
	MinshengBank findbyOrderNo(String sn);
	MinshengBank findBySn(String insId);
	
}