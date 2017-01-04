/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Consumer;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.entity.Consumer.Status;
import net.wit.entity.Member.Gender;

/**
 * Service - 会员
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface ConsumerService extends BaseService<Consumer, Long> {

	/** 查询我的客户 */
	Page<Consumer> findPage(Tenant tenant, Status status, Pageable pageable);

	Page<Consumer> findPage(Tenant tenant, String keyword, Status status,Gender gender, Pageable pageable);

	/** 查询我的客户数*/
	Map<String, String> memberCounts(Long tenantid);

	/** 查询不同等级下我的客户数*/
	Map<String, String> vipcounts(Long tenantid,String viptype);

	/** 查询不同性别下我的客户数*/
	Map<String, String> ganercounts(Long tenantid,int gener);

	boolean consumerExists(Long memberId,Long tenantId);

	void becomvip(Tenant tenant, Member member);

	/**
	 * 根据时间查询新增会员
	 */
	public Page<Consumer> findByAddPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable);
	public List<Consumer> findByAddList(Tenant tenant, Date beginDate, Date endDate);
}