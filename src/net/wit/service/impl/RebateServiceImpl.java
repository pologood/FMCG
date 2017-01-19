/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;


import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.RebateDao;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.entity.Rebate;
import net.wit.service.RebateService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Service - 代理商返利
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("rebateServiceImpl")
public class RebateServiceImpl extends BaseServiceImpl<Rebate, Long> implements RebateService {

	@Resource(name = "rebateDaoImpl")
	private RebateDao rebateDao;

	@Resource(name = "rebateDaoImpl")
	public void setBaseDao(RebateDao rebateDao) {
		super.setBaseDao(rebateDao);
	}

	@Transactional(readOnly = true)
	public Page<Rebate> findPage(Member member,Pageable pageable) {
		return rebateDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Rebate> findPage(Member member, Rebate.Type type,CouponCode couponCode, Pageable pageable) {
		return rebateDao.findPage(member,type, couponCode, pageable);
	}

	@Override
	public List<Rebate> findList(Member member, Rebate.Type type, CouponCode couponCode) {
		return rebateDao.findList(member,type, couponCode);
	}

	public BigDecimal getAmount(Member member, Rebate.Type type){
		return rebateDao.getAmount(member, type);
	}

	public long count(Member member, Rebate.Type type){
		return rebateDao.count(member, type);
	}

	public List<Rebate> openCodeRebate(CouponCode couponCode,Member member){
		return rebateDao.openCodeRebate(couponCode,member);
	}

	public void calcRebate() {
		
		rebateDao.calcRebate();
	}

	public Page<Rebate> findPage(Date beginDate, Date endDate, String keyword, Pageable pageable){
		return rebateDao.findPage(beginDate,endDate,keyword,pageable);
	}
	public List<Rebate> findList(Date beginDate, Date endDate, String keyword, List<Filter> filters){
		return rebateDao.findList(beginDate,endDate,keyword,filters);
	}

	@Override
	public BigDecimal sumBrokerage(Member member, Rebate.Type type, Rebate.OrderType orderType) {
		return rebateDao.sumBrokerage(member,type,orderType);
	}
}

