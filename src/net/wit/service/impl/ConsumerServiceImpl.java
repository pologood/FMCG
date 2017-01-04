/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.entity.Member;
import net.wit.service.MemberRankService;
import org.springframework.stereotype.Service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ConsumerDao;
import net.wit.entity.Consumer;
import net.wit.entity.Tenant;
import net.wit.entity.Consumer.Status;
import net.wit.entity.Member.Gender;
import net.wit.service.ConsumerService;

/**
 * Service - 会员
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("consumerServiceImpl")
public class ConsumerServiceImpl extends BaseServiceImpl<Consumer, Long> implements ConsumerService {

	@Resource(name = "consumerDaoImpl")
	private ConsumerDao consumerDao;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "consumerDaoImpl")
	public void setBaseDao(ConsumerDao consumerDao) {
		super.setBaseDao(consumerDao);
	}

	public Page<Consumer> findPage(Tenant tenant,Status status,Pageable pageable) {
		return consumerDao.findPage(tenant, status, pageable);
	}

	public Page<Consumer> findPage(Tenant tenant,String keyword, Status status,Gender gender,Pageable pageable) {
		return consumerDao.findPage(tenant, keyword, status,gender, pageable);
	}

	@Override
	public Map<String, String> memberCounts(Long tenantid) {
		return consumerDao.memberCounts(tenantid);
	}

	@Override
	public Map<String, String> vipcounts(Long tenantid,String viptype) {
		return consumerDao.vipcounts(tenantid,viptype);
	}

	@Override
	public Map<String, String> ganercounts(Long tenantid,int gener) {
		return consumerDao.ganercounts(tenantid,gener);
	}

	public boolean consumerExists(Long memberId,Long tenantId) {
		return consumerDao.consumerExists(memberId,tenantId);
	}

	@Override
	public Page<Consumer> findByAddPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable) {
		return consumerDao.findByAddPage(tenant,beginDate,endDate,pageable);
	}

	@Override
	public List<Consumer> findByAddList(Tenant tenant, Date beginDate, Date endDate) {
		return consumerDao.findByAddList(tenant,beginDate,endDate);
	}

	@Override
	public void becomvip(Tenant tenant, Member member) {
		Consumer consumer = new Consumer();
		consumer.setMember(member);
		consumer.setStatus(Consumer.Status.enable);
		consumer.setTenant(tenant);
		consumer.setMemberRank(memberRankService.findDefault());
		consumerDao.persist(consumer);
	}

}