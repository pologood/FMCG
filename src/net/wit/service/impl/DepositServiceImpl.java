/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DepositDao;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.Deposit.Type;
import net.wit.entity.Payment;
import net.wit.service.DepositService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 预存款
 * @author rsico Team
 * @version 3.0
 */
@Service("depositServiceImpl")
public class DepositServiceImpl extends BaseServiceImpl<Deposit, Long> implements DepositService {

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "depositDaoImpl")
	public void setBaseDao(DepositDao depositDao) {
		super.setBaseDao(depositDao);
	}

	@Transactional(readOnly = true)
	public Page<Deposit> findPage(Member member, Pageable pageable) {
		return depositDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Deposit> findPage(Member member,Date beginTime, Date endTime, Pageable pageable,Type type) {
		return depositDao.findPage(member,beginTime,endTime, pageable,type);
	}
	
	@Transactional(readOnly = true)
	public List<Deposit> findList(Member member, Date beginTime, Date endTime,Type type) {
		return depositDao.findList(member, beginTime, endTime,type);
	}

	@Transactional(readOnly = true)
	public List<Map<String,Object>> findMapList(Member member,String type, Date beginTime, Date endTime) {
		return depositDao.findMapList(member,type, beginTime, endTime);
	}
	
	/*
	 * (non-Javadoc) <p>Title: income</p> <p>Description: </p>
	 * @param member
	 * @return
	 * @see net.wit.service.DepositService#income(net.wit.entity.Member)
	 */

	@Override
	public BigDecimal income(Member member,Type type,Date beginTime, Date endTime, Deposit.Status status) {
		return depositDao.income(member,type,beginTime,endTime,status);
	}

	/*
	 * (non-Javadoc) <p>Title: outcome</p> <p>Description: </p>
	 * @param member
	 * @return
	 * @see net.wit.service.DepositService#outcome(net.wit.entity.Member)
	 */

	@Override
	public BigDecimal outcome(Member member,Type type,Date beginTime, Date endTime, Deposit.Status status) {
		return depositDao.outcome(member,type,beginTime,endTime,status);
	}

}