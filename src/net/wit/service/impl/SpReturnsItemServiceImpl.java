/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SpReturnsDao;
import net.wit.dao.SpReturnsItemDao;
import net.wit.entity.Member;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.SpReturnsItem;
import net.wit.entity.Tenant;
import net.wit.service.SpReturnsItemService;
import net.wit.service.SpReturnsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service - 退款 申请单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("spReturnsItemServiceImpl")
public class SpReturnsItemServiceImpl extends BaseServiceImpl<SpReturnsItem, Long> implements SpReturnsItemService {

	@Resource
	private SpReturnsItemDao spReturnsItemDao;
	
	@Resource(name = "spReturnsItemDaoImpl")
	public void setBaseDao(SpReturnsItemDao spReturnsItemDao) {
		super.setBaseDao(spReturnsItemDao);
	}

	public Page<SpReturnsItem> findPageByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,ReturnStatus returnStatus,Boolean status,String keyword,Pageable pageable){
		return spReturnsItemDao.findPageByTenant(tenant,supplier,start_date,end_date,returnStatus,status,keyword,pageable);
	}
	public List<SpReturnsItem> findByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,Boolean status,String keywords){
		return spReturnsItemDao.findByTenant(tenant,supplier,start_date,end_date,status,keywords);
	}
	public List<Map<String,Object>> returnProductTotalExp(Tenant tenant, Tenant supplier, Date start_date, Date end_date, Boolean status, String keywords){
		return spReturnsItemDao.returnProductTotalExp(tenant,supplier,start_date,end_date,status,keywords);
	}
	public Page<Map<String,Object>> returnProductTotal(Tenant tenant, Tenant supplier,Date start_date,Date end_date,ReturnStatus returnStatus,Boolean status,String keywords,Pageable pageable){
		return spReturnsItemDao.returnProductTotal(tenant,supplier,start_date,end_date,returnStatus,status,keywords,pageable);
	}


}