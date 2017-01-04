/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.crypto.Data;

import net.wit.Filter;
import org.springframework.stereotype.Service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SpReturnsDao;
import net.wit.entity.Member;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.Tenant;
import net.wit.service.SpReturnsService;

/**
 * Service - 退款 申请单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("spReturnsServiceImpl")
public class SpReturnsServiceImpl extends BaseServiceImpl<SpReturns, Long> implements SpReturnsService {

	@Resource
	private SpReturnsDao spReturnsDao;
	
	@Resource(name = "spReturnsDaoImpl")
	public void setBaseDao(SpReturnsDao spReturnsDao) {
		super.setBaseDao(spReturnsDao);
	}
	public Page<SpReturns> findPage(Member member, ReturnStatus returnStatus,Pageable pageable){
		return spReturnsDao.findPage(member,returnStatus,pageable);
	}
	public Page<SpReturns> findBySupplier(Date start_date,Date end_date,Tenant supplier,ReturnStatus returnStatus,Pageable pageable){
		return spReturnsDao.findBySupplier(start_date,end_date,supplier,returnStatus,pageable);
	}
	public List<SpReturns> returnSettle(Date start_date,Date end_date,Tenant supplier,Boolean status){
		return spReturnsDao.returnSettle(start_date,end_date,supplier,status);
	}
	public Page<SpReturns> findByTenant(Tenant tenant,ReturnStatus returnStatus,String keyword,Pageable pageable){
		return spReturnsDao.findByTenant(tenant,returnStatus,keyword,pageable);
	}
	public Page<SpReturns> findBySettle(Member member, Date start_date,Date end_date,Pageable pageable){
		return spReturnsDao.findBySettle(member,start_date,end_date,pageable);
	}
	public List<SpReturns> findByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,Boolean status,String keywords){
		return spReturnsDao.findByTenant(tenant,supplier,start_date,end_date,status,keywords);
	}
	public Page<SpReturns> findPageByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,ReturnStatus returnStatus,Boolean status,String keywords,Pageable pageable){
		return spReturnsDao.findPageByTenant(tenant,supplier,start_date,end_date,returnStatus,status,keywords,pageable);
	}
	public long returnApplyCount(List<Filter> filters,Tenant tenant, ReturnStatus returnStatus, String keyword) {
		return spReturnsDao.returnApplyCount(filters,tenant, returnStatus, keyword);
	}
	public List<SpReturns> findReturnNumber(Tenant tenant,ReturnStatus returnStatus){
		return spReturnsDao.findReturnNumber(tenant,returnStatus);
	}
}