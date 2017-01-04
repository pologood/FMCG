/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.wit.dao.AreaDao;
import net.wit.dao.MobilePriceDao;
import net.wit.dao.MobileSegmentDao;
import net.wit.entity.Area;
import net.wit.entity.MobilePrice;
import net.wit.entity.MobileSegment;
import net.wit.service.MobilePriceService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 地区
 * 
 * @author rsico++ Team
 * @version 3.0
 */
@Service("mobilePriceServiceImpl")
public class MobilePriceServiceImpl extends BaseServiceImpl<MobilePrice, Long> implements MobilePriceService {

	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;
	@Resource(name = "mobilePriceDaoImpl")
	private MobilePriceDao mobilePriceDao;

	@Resource(name = "mobilePriceDaoImpl")
	public void setBaseDao(MobilePriceDao mobilePriceDao) {
		super.setBaseDao(mobilePriceDao);
	}

	@Override
	@Transactional
	@CacheEvict(value = "mobilePrice", allEntries = true)
	public void save(MobilePrice mobilePrice) {
		super.save(mobilePrice);
	}

	@Override
	@Transactional
	@CacheEvict(value = "mobilePrice", allEntries = true)
	public MobilePrice update(MobilePrice mobilePrice) {
		return super.update(mobilePrice);
	}

	@Override
	@Transactional
	@CacheEvict(value = "mobilePrice", allEntries = true)
	public MobilePrice update(MobilePrice mobilePrice, String... ignoreProperties) {
		return super.update(mobilePrice, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "mobilePrice", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "mobilePrice", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "mobilePrice", allEntries = true)
	public void delete(MobilePrice mobilePrice) {
		super.delete(mobilePrice);
	}
		
	public List<MobilePrice> findbyMobile(String mobile) {
		return null;
	}
	
	public List<MobilePrice> findBySegment(String provincename,String isptype) {
		return mobilePriceDao.findBySegment(provincename, isptype);
	}
	
	public MobilePrice findByProdId(String prodId) {
		return mobilePriceDao.findByProdId(prodId);
	}
}