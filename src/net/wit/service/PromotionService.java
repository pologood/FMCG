/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.Location;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;
import net.wit.entity.PromotionMember;
import net.wit.entity.PromotionProduct;
import net.wit.entity.Tenant;
import net.wit.entity.PromotionProduct.TimeRegion;

/**
 * Service - 促销
 * @author rsico Team
 * @version 3.0
 */
public interface PromotionService extends BaseService<Promotion, Long> {

	/**
	 * 查找促销
	 * @param hasBegun 是否已开始
	 * @param hasEnded 是否已结束
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 促销
	 */
	List<Promotion> findList(Type type, Boolean hasBegun, Boolean hasEnded, Area area, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找促销(缓存)
	 * @param type
	 * @param hasBegun 是否已开始
	 * @param hasEnded 是否已结束
	 * @param area
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @param cacheRegion 缓存区域
	 * @return 促销(缓存)
	 */
	List<Promotion> findList(Type type, Boolean hasBegun, Boolean hasEnded, Area area, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	Page<Promotion> findPage(Type type, Area area, Boolean hasBegun, Boolean hasEnded, Boolean periferal, Community community, Location location, BigDecimal distance, ProductCategory productCategory,
			Pageable pageable);
	/**
	 * 查找指定商品促销
	 * @param type
	 */
	List<Promotion> findList(Type type, Tenant tenant,Product product);

	Page<Promotion> findPage(Tenant tenant ,Pageable pageable);
	Page<Promotion> findPage(Tenant tenant ,Type type,Pageable pageable);

	boolean isPromotion(Type type, Tenant tenant, Date beginDate, Date endDate);

	Promotion getNowPromotion(Type type, Tenant tenant);

	/**
	 * 查找商品当前商品促销
	 * @param tenant
	 */
	List<Promotion> findEnabledPromotionService(Tenant tenant);
}