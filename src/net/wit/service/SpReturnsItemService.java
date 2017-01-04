/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.SpReturnsItem;
import net.wit.entity.Tenant;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service - 退货申请单
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface SpReturnsItemService extends BaseService<SpReturnsItem, Long> {
    public Page<SpReturnsItem> findPageByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,ReturnStatus returnStatus,Boolean status,String keyword,Pageable pageable);
    public List<SpReturnsItem> findByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,Boolean status,String keywords);
    public Page<Map<String,Object>> returnProductTotal(Tenant tenant, Tenant supplier, Date start_date, Date end_date, ReturnStatus returnStatus, Boolean status, String keyword, Pageable pageable);
	public List<Map<String,Object>> returnProductTotalExp(Tenant tenant, Tenant supplier, Date start_date, Date end_date, Boolean status, String keywords);

}