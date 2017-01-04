/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;


import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.dao.MobileDao;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.Mobile;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.MemberService;
import net.wit.service.MobileService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 手机快充
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("mobileServiceImpl")
public class MobileServiceImpl extends BaseServiceImpl<Mobile, Long> implements MobileService {

	@Resource(name = "mobileDaoImpl")
	private MobileDao mobileDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "mobileDaoImpl")
	public void setBaseDao(MobileDao mobileDao) {
		super.setBaseDao(mobileDao);
	}


	@Transactional(readOnly = true)
	public Page<Mobile> findPage(Member member, Pageable pageable) {
		return mobileDao.findPage(member, pageable);
	}
	
	public Mobile findbySn(String sn) {
		return mobileDao.findbySn(sn);
	}
	
	public Message fill(Mobile mobile) {
		Member member = mobile.getMember();
		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		BigDecimal modifyBalance = mobile.getAmount();
		if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0) {
			if (member.getBalance().subtract(modifyBalance).compareTo(new BigDecimal(0)) < 0) {
				return Message.error("账户余额不足");
			}
			member.setBalance(member.getBalance().subtract(modifyBalance));
			Deposit deposit = new Deposit();
			deposit.setType(Deposit.Type.outcome);
			deposit.setStatus(Deposit.Status.complete);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(modifyBalance.abs());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(mobile.getOperator());
			deposit.setMemo("手机充值");
			deposit.setMember(member);
			depositDao.persist(deposit);
		}
		mobile.setRespCode("0000");
		mobileDao.persist(mobile);
		memberDao.merge(member);
		return  Message.success("充值提交成功");
	}
	
	public Message notify(Mobile mobile) {
		Mobile mob = mobileDao.findbySn(mobile.getSn());
		if (!mob.getRespCode().equals("0000") ) {
			return Message.success("已处理");
		}
		if (mobile.getRespCode().equals("0000") ) {
			return Message.success("处理中");
		}
		if (mobile.getRespCode().equals("1")) {
			return Message.success("正在处理中");
		}
		mob.setBusiRefNo(mobile.getBusiRefNo());
		mob.setRespTime(mobile.getRespTime());
		mob.setRespCode(mobile.getRespCode());
		mob.setRespMsg(mobile.getRespMsg());
		
		if (mobile.getRespCode().equals("2") || mobile.getRespCode().equals("3") ) {
			mobileDao.persist(mob);
			return Message.success("充值成功");
		}
		
		Member member = mobile.getMember();
		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		if (mobile.getRespCode().equals("0000")) {
			BigDecimal modifyBalance = mobile.getAmount();
			if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0) {
				member.setBalance(member.getBalance().add(modifyBalance));
				Deposit deposit = new Deposit();
				deposit.setStatus(Deposit.Status.complete);
				deposit.setType(Deposit.Type.income);
				deposit.setCredit(modifyBalance.abs());
				deposit.setDebit(new BigDecimal(0));
				deposit.setBalance(member.getBalance());
				deposit.setOperator(mobile.getOperator());
				deposit.setMemo("手机充值-退款");
				deposit.setMember(member);
				depositDao.persist(deposit);
			}
			memberDao.merge(member);
		}
		mobileDao.persist(mobile);
		return  Message.success("处理成功");
	}
}

