/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Order;
import net.wit.dao.BrandSeriesDao;
import net.wit.entity.ArticleCategory;
import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;
import net.wit.service.BrandSeriesService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 品牌系列
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("brandSeriesServiceImpl")
public class BrandSeriesServiceImpl extends BaseServiceImpl<BrandSeries, Long> implements BrandSeriesService {

	@Resource(name = "brandSeriesDaoImpl")
	private BrandSeriesDao brandSeriesDao;

	@Resource(name = "brandSeriesDaoImpl")
	public void setBaseDao(BrandSeriesDao brandSeriesDao) {
		super.setBaseDao(brandSeriesDao);
	}

	@Transactional(readOnly = true)
	public List<BrandSeries> findRoots() {
		return brandSeriesDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<BrandSeries> findRoots(Integer count) {
		return brandSeriesDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Cacheable("brandSeries")
	public List<BrandSeries> findRoots(Integer count, String cacheRegion) {
		return brandSeriesDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<BrandSeries> findParents(BrandSeries brandSeries) {
		return brandSeriesDao.findParents(brandSeries, null);
	}

	@Transactional(readOnly = true)
	public List<BrandSeries> findParents(BrandSeries brandSeries, Integer count) {
		return brandSeriesDao.findParents(brandSeries, count);
	}

	@Transactional(readOnly = true)
	@Cacheable("brandSeries")
	public List<BrandSeries> findParents(BrandSeries brandSeries, Integer count, String cacheRegion) {
		return brandSeriesDao.findParents(brandSeries, count);
	}

	@Transactional(readOnly = true)
	@Cacheable("brandSeries")
	public List<BrandSeries> findTree(Brand brand) {
		if (brand!=null) {
		    return brandSeriesDao.findTree(brand.getSeries(), null);
		} else {
		    return brandSeriesDao.findTree(null, null);
		}
	}

	@Transactional(readOnly = true)
	public List<BrandSeries> findChildren(BrandSeries brandSeries) {
		return brandSeriesDao.findChildren(brandSeries, null);
	}

	@Transactional(readOnly = true)
	public List<BrandSeries> findChildren(BrandSeries brandSeries, Integer count) {
		return brandSeriesDao.findChildren(brandSeries, count);
	}

	@Transactional(readOnly = true)
	@Cacheable("brandSeries")
	public List<BrandSeries> findChildren(BrandSeries brandSeries, Integer count, String cacheRegion) {
		return brandSeriesDao.findChildren(brandSeries, count);
	}
	 
	@Transactional(readOnly = true)
	@Cacheable("brandSeries")
	public List<BrandSeries> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return brandSeriesDao.findList(null, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable("brandSeries")
	public List<BrandSeries> findByBrand(Brand brand, Integer count, String cacheRegion) {
		return brandSeriesDao.findbyBrand(brand, count, cacheRegion);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brandSeries", allEntries = true)
	public void save(BrandSeries brandSeries) {
		super.save(brandSeries);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brandSeries", allEntries = true)
	public BrandSeries update(BrandSeries brandSeries) {
		return super.update(brandSeries);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brandSeries", allEntries = true)
	public BrandSeries update(BrandSeries brandSeries, String... ignoreProperties) {
		return super.update(brandSeries, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brandSeries", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brandSeries", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "brandSeries", allEntries = true)
	public void delete(BrandSeries brandSeries) {
		super.delete(brandSeries);
	}
	
}