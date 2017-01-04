/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Consumer;
import net.wit.entity.Tenant;
import net.wit.entity.Consumer.Status;
import net.wit.entity.Member.Gender;

/**
 * Dao -  会员
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface ConsumerDao extends BaseDao<Consumer, Long> {

	public Page<Consumer> findPage(Tenant tenant, Status status, Pageable pageable);

	public Page<Consumer> findPage(Tenant tenant,String keyword, Status status,Gender gender, Pageable pageable);

	public Map<String, String> memberCounts(Long tenantid);
	
	public Map<String, String> vipcounts(Long tenantid,String viptype);
	
	public Map<String, String> ganercounts(Long tenantid,int gener);

	boolean consumerExists(Long memberId,Long tenantId);

	/**
	 * 根据时间查询新增会员
	 */
	public Page<Consumer> findByAddPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable);
	public List<Consumer> findByAddList(Tenant tenant, Date beginDate, Date endDate);
}