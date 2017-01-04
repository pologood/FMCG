/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PromotionDao;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.Location;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;
import net.wit.entity.PromotionProduct;
import net.wit.entity.Tenant;
import net.wit.entity.PromotionProduct.TimeRegion;
import net.wit.service.PromotionService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 促销
 * @author rsico Team
 * @version 3.0
 */
@Service("promotionServiceImpl")
public class PromotionServiceImpl extends BaseServiceImpl<Promotion, Long> implements PromotionService {

	@Resource(name = "promotionDaoImpl")
	private PromotionDao promotionDao;

	@Resource(name = "promotionDaoImpl")
	public void setBaseDao(PromotionDao promotionDao) {
		super.setBaseDao(promotionDao);
	}

	@Transactional(readOnly = true)
	public List<Promotion> findList(Type type, Boolean hasBegun, Boolean hasEnded, Area area, Integer count, List<Filter> filters, List<Order> orders) {
		return promotionDao.findList(type, hasBegun, hasEnded, area, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable("promotion")
	public List<Promotion> findList(Type type, Boolean hasBegun, Boolean hasEnded, Area area, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return promotionDao.findList(type, hasBegun, hasEnded, area, count, filters, orders);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void save(Promotion promotion) {
		super.save(promotion);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public Promotion update(Promotion promotion) {
		return super.update(promotion);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public Promotion update(Promotion promotion, String... ignoreProperties) {
		return super.update(promotion, ignoreProperties);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Transactional
	@CacheEvict(value = { "promotion", "product" }, allEntries = true)
	public void delete(Promotion promotion) {
		super.delete(promotion);
	}

	public Page<Promotion> findPage(Type type, Area area, Boolean hasBegun, Boolean hasEnded, Boolean periferal, Community community, Location location, BigDecimal distance, ProductCategory productCategory,
			Pageable pageable) {
		return promotionDao.findPage(type, area, hasBegun, hasEnded, periferal, community, location, distance, productCategory, pageable);
	}
	/**
	 * 查找指定商品促销
	 * @param type 
	 */
	public List<Promotion> findList(Type type, Tenant tenant,Product product) {
	  return promotionDao.findList(type, tenant, product);
	}

	public Page<Promotion> findPage(Tenant tenant,Pageable pageable) {
		return promotionDao.findPage(tenant,pageable);
	}
	public Page<Promotion> findPage(Tenant tenant,Type type,Pageable pageable) {
		return promotionDao.findPage(tenant,type,pageable);
	}
	public boolean isPromotion(Type type, Tenant tenant, Date beginDate, Date endDate){
		return promotionDao.isPromotion(type,tenant,beginDate,endDate);
	}

	public Promotion getNowPromotion(Type type, Tenant tenant){
		return promotionDao.getNowPromotion(type,tenant);
	}

	public List<Promotion> findEnabledPromotionService(Tenant tenant){
		return promotionDao.findEnabledPromotionService(tenant);
	}
}