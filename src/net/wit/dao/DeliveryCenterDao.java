/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
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
import net.wit.entity.TenantCategory;

/**
 * Dao - 发货点
 * @author rsico Team
 * @version 3.0
 */
public interface DeliveryCenterDao extends BaseDao<DeliveryCenter, Long> {

	/**
	 * 查找默认发货点
	 * @return 默认发货点，若不存在则返回null
	 */
	DeliveryCenter findDefault();

	/**
	 * 查找默认发货点
	 * @return 默认发货点，若不存在则返回null
	 */
	DeliveryCenter findDefault(Tenant tenant);

	List<DeliveryCenter> findMyAll(Member member);

	List<DeliveryCenter> findList(Area area, Community community);

	List<DeliveryCenter> findList(Area area, Location location, BigDecimal distatce);

	public Page<DeliveryCenter> findPage(Member member, Location location, Pageable pageable);

	/**
	 * 查找发货点
	 * @param code 实体店编号
	 * @return
	 */
	DeliveryCenter findByCode(Tenant tenant, String code);

	List<DeliveryCenter> findListByLocation(Location location, Area area, Community community, BigDecimal distance);

	Page<DeliveryCenter> findPage(Set<TenantCategory> tenantCategories, Area area, Community community, Location location, Double distance, Pageable pageable);

	/**
	 * @Title：findList
	 * @Description：
	 * @param tenantCategories
	 * @param area
	 * @param community
	 * @param location
	 * @param distance
	 * @return List<DeliveryCenter>
	 */
	List<DeliveryCenter> findList(Set<TenantCategory> tenantCategories, Area area, Community community, Location location, Double distance);

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
	List<DeliveryCenter> findList(TenantCategory tenantCategory, List<Tag> tags, Area area,Boolean isDefault, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * @Title：findList
	 * @Description：
	 * @param tenantCategorys
	 * @param tenantTags
	 * @param area
	 * @param community
	 * @param count
	 * @return List<DeliveryCenter>
	 */
	List<DeliveryCenter> findList(Set<TenantCategory> tenantCategorys, List<Tag> tenantTags, Area area, Community community, Integer count);

	/**
	 * @Title：findList
	 * @Description：
	 * @param tenantCategory
	 * @param beginDate
	 * @param endDate
	 * @param first
	 * @param count
	 * @return  List<DeliveryCenter>
	 */
	List<DeliveryCenter> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count);

	List<DeliveryCenter> findourStoreList(Tenant tenant);//我们的门店

}