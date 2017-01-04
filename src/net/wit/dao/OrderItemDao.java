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
import net.wit.entity.OrderItem;
import net.wit.entity.Tenant;

/**
 * Dao - 订单项
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface OrderItemDao extends BaseDao<OrderItem, Long> {
	public Page<OrderItem> findPage(Boolean status,Date start_date,Date end_date,Tenant supplier,Pageable pageable);
	public Page<OrderItem> openPage(Date start_date,Date end_date,Tenant supplier,String keywords,Pageable pageable);
	public List<OrderItem> openList(Date start_date,Date end_date,Tenant supplier,String keywords);
	public List<OrderItem> orderSettle(Boolean status,Date start_date,Date end_date,Tenant supplier);
	public Page<Map<String, Object>> openPageGroupBy(Date start_date,Date end_date,Tenant supplier,String keywords,Pageable pageable);
	public List<Map<String, Object>> openListGroupBy(Date start_date,Date end_date,Tenant supplier,String keywords);
}