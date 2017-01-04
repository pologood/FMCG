/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.SpReturns;
import net.wit.entity.Tenant;
import net.wit.entity.SpReturns.ReturnStatus;

/**
 * Dao - 评论
 * @author rsico Team
 * @version 3.0
 */
public interface SpReturnsDao extends BaseDao<SpReturns, Long> {
	public Page<SpReturns> findPage(Member member, ReturnStatus returnStatus,Pageable pageable);
	public Page<SpReturns> findBySupplier(Date start_date,Date end_date,Tenant supplier,ReturnStatus returnStatus,Pageable pageable);
	public List<SpReturns> returnSettle(Date start_date,Date end_date,Tenant supplier,Boolean status);
	public Page<SpReturns> findByTenant(Tenant tenant,ReturnStatus returnStatus,String keyword,Pageable pageable);
	public Page<SpReturns> findBySettle(Member member, Date start_date,Date end_date,Pageable pageable);
	public List<SpReturns> findByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,Boolean status,String keywords);
	public Page<SpReturns> findPageByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,ReturnStatus returnStatus,Boolean status,String keyword,Pageable pageable);
	long returnApplyCount(List<Filter> filters, Tenant tenant, ReturnStatus returnStatus, String keyword);
	public List<SpReturns> findReturnNumber(Tenant tenant,ReturnStatus returnStatus);
}