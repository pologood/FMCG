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
import net.wit.entity.OrderItem;
import net.wit.entity.Tenant;

/**
 * Service - 订单项
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface OrderItemService extends BaseService<OrderItem, Long> {
	public Page<OrderItem> findPage(Boolean status,Date start_date,Date end_date,Tenant tenant,Pageable pageable);
	public Page<OrderItem> openPage(Date start_date,Date end_date,Tenant tenant,String keywords,Pageable pageable);
	public List<OrderItem> openList(Date start_date,Date end_date,Tenant tenant,String keywords);
	public List<OrderItem> orderSettle(Boolean status,Date start_date,Date end_date,Tenant tenant);
	public Page<Map<String, Object>> openPageGroupBy(Date start_date,Date end_date,Tenant tenant,String keywords,Pageable pageable);
	public List<Map<String, Object>> openListGroupBy(Date start_date,Date end_date,Tenant tenant,String keywords);
}