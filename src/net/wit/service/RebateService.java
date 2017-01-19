/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.entity.Rebate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service - 收款单
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface RebateService extends BaseService<Rebate, Long> {


	Page<Rebate> findPage(Member member, Pageable pageable);

	Page<Rebate> findPage(Member member, Rebate.Type type, CouponCode couponCode, Pageable pageable);
	List<Rebate> findList(Member member, Rebate.Type type, CouponCode couponCode);

	BigDecimal getAmount(Member member, Rebate.Type type);

	long count(Member member, Rebate.Type type);

	List<Rebate> openCodeRebate(CouponCode couponCode, Member member);

	void calcRebate();

	/*admin-分润报表、导出报表*/
	Page<Rebate> findPage(Date beginDate, Date endDate, String keyword, Pageable pageable);

	List<Rebate> findList(Date beginDate, Date endDate, String keyword, List<Filter> filters);

	/**
	 * 查询赚取金额
	 */
	BigDecimal sumBrokerage(Member member, Rebate.Type type, Rebate.OrderType orderType);
}