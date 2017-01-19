/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.Tenant.OrderType;
import net.wit.entity.TenantCategory;

/**
 * Service - 发货点
 * @author rsico Team
 * @version 3.0
 */
public interface DeliveryCenterService extends BaseService<DeliveryCenter, Long> {

	/**
	 * 查找默认发货点
	 * @return 默认发货点，若不存在则返回null
	 */
	DeliveryCenter findDefault();

	DeliveryCenter findDefault(Tenant tenant);

	List<DeliveryCenter> findMyAll(Member member);
	/**
	 * 查找商家发货点
	 * @param tenantCategory 文章分类
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param first 起始记录
	 * @param count 数量
	 * @return 仅包含已发布文章
	 */
	List<DeliveryCenter> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count);

	public Page<DeliveryCenter> findPage(Member member, Location location, Pageable pageable);

	Page<DeliveryCenter> findPage(Set<TenantCategory> tenantCategories, Area area, Community community, Location location, Double distance, OrderType orderType, Pageable pageable);

	/**
	 * 查找发货点
	 * @param code 实体店编号
	 * @return
	 */
	DeliveryCenter findByCode(Tenant tenant, String code);

	/**
	 * 查找最新发货点
	 * @param location 查找最新发货点
	 * @return
	 */
	DeliveryCenter findByLocation(Tenant tenant, Location location);

	/**
	 * @Title：findList
	 * @Description：
	 * @param tenantCategory
	 * @param tags
	 * @param area
	 * @param community
	 * @param periferal
	 * @param count
	 * @param filters
	 * @param orders
	 * @return List<DeliveryCenter>
	 */
	List<DeliveryCenter> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Boolean isDefault, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * @Title：findList
	 * @Description：
	 * @param tenantCategory
	 * @param tags
	 * @param area
	 * @param community
	 * @param periferal
	 * @param count
	 * @param filters
	 * @param orders
	 * @param cacheRegion
	 * @return List<DeliveryCenter>
	 */
	List<DeliveryCenter> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Boolean isDefault, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	/**
	 * @Title：findList
	 * @Description：
	 * @param tenantCategorys
	 * @param tenantTags
	 * @param current
	 * @param object
	 * @param i void
	 */
	List<DeliveryCenter> findList(Set<TenantCategory> tenantCategorys, List<Tag> tenantTags, Area current, Community community, Integer count);
	List<DeliveryCenter> findourStoreList(Tenant tenant );//我们的门店

	Page<DeliveryCenter> findPage(String keyword, Pageable pageable);

}