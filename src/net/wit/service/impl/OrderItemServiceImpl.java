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

import org.springframework.stereotype.Service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.OrderItemDao;
import net.wit.entity.OrderItem;
import net.wit.entity.Tenant;
import net.wit.service.OrderItemService;

/**
 * Service - 订单项
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("orderItemServiceImpl")
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long> implements OrderItemService {
	
	@Resource(name = "orderItemDaoImpl")
	private OrderItemDao orderItemDao;
	
	@Resource(name = "orderItemDaoImpl")
	public void setBaseDao(OrderItemDao orderItemDao) {
		super.setBaseDao(orderItemDao);
	}
	
	public Page<OrderItem> findPage(Boolean status,Date start_date,Date end_date,Tenant supplier,Pageable pageable){
		return orderItemDao.findPage(status,start_date,end_date,supplier,pageable);
	}
	public Page<OrderItem> openPage(Date start_date,Date end_date,Tenant supplier,String keywords,Pageable pageable){
		return orderItemDao.openPage(start_date,end_date,supplier,keywords,pageable);
	}
	public List<OrderItem> openList(Date start_date,Date end_date,Tenant supplier,String keywords){
		return orderItemDao.openList(start_date,end_date,supplier,keywords);
	}
	public List<OrderItem> orderSettle(Boolean status,Date start_date,Date end_date,Tenant supplier){
		return orderItemDao.orderSettle(status,start_date,end_date,supplier);
	}
	/**
	 * 产品统计
	 */
	public Page<Map<String, Object>> openPageGroupBy(Date start_date,Date end_date,Tenant supplier,String keywords,Pageable pageable){
		return orderItemDao.openPageGroupBy(start_date,end_date,supplier,keywords,pageable);
	}
	/**
	 * 产品统计导出
	 */
	public List<Map<String, Object>> openListGroupBy(Date start_date,Date end_date,Tenant supplier,String keywords){
		return orderItemDao.openListGroupBy(start_date,end_date,supplier,keywords);
	}
}