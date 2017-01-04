/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CreditDao;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.Credit.Method;
import net.wit.entity.Credit.Status;
import net.wit.service.CreditService;
import net.wit.service.MemberService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 付款单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("creditServiceImpl")
public class CreditServiceImpl extends BaseServiceImpl<Credit, Long> implements
		CreditService {

	@Resource(name = "creditDaoImpl")
	private CreditDao creditDao;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "creditDaoImpl")
	public void setBaseDao(CreditDao ceditDao) {
		super.setBaseDao(ceditDao);
	}

	@Transactional(readOnly = true)
	public Credit findBySn(String sn) {
		return creditDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public Page<Credit> findPage(Member member, Pageable pageable,
			Credit.Type type) {
		return creditDao.findPage(member, pageable, type);
	}
	public Page<Credit> findPage(Member member, Date beginDate, Date endDate, Credit.Type type, Pageable pageable) {
		return creditDao.findPage(member, beginDate, endDate,type, pageable);
	}

	public void saveAndUpdate(Credit credit) {
		try {
			Member member = credit.getMember();
			if (member != null) {
				memberService.withdraw(member,null,credit.getClearAmount(),credit.getEffectiveAmount(),"银行汇款支出", null);
			}
			super.save(credit);
		} catch (Exception e) {
			//System.out.println("saveAndUpdate" + e);
		}
	}

	public void saveAndRefunds(Credit credit) throws Exception {
		Member member = credit.getMember();
		if (member != null) {
			memberService.withdraw(member, null,new BigDecimal(0).subtract(credit.getClearAmount()), new BigDecimal(0).subtract(credit.getEffectiveAmount()),
					"汇款失败退回", null);
		}
		super.save(credit);
	}

	public void checkPayment() {

	}

	@Transactional(readOnly = true)
	public Page<Credit> findPage(Method method, Status status, Date beginDate,
			Date endDate,Date beginDates, Date endDates,String searchValue, Pageable pageable) {
		return creditDao.findPage(method, status, beginDate, endDate,beginDates,endDates,searchValue, pageable);
	}

}
