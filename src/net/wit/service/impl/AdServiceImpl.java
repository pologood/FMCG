/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AdDao;
import net.wit.entity.*;
import net.wit.service.AdService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 广告
 * @author rsico Team
 * @version 3.0
 */
@Service("adServiceImpl")
public class AdServiceImpl extends BaseServiceImpl<Ad, Long> implements AdService {
	@Resource(name = "adDaoImpl")
	private AdDao adDao;

	@Resource(name = "adDaoImpl")
	public void setBaseDao(AdDao adDao) {
		super.setBaseDao(adDao);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void save(Ad ad) {
		super.save(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad) {
		return super.update(ad);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public Ad update(Ad ad, String... ignoreProperties) {
		return super.update(ad, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "adPosition", allEntries = true)
	public void delete(Ad ad) {
		super.delete(ad);
	}

	@Transactional(readOnly = true)
	public Page<Ad> findMyPage(Tenant tenant, Pageable pageable) {
		return adDao.findMyPage(tenant, pageable);
	}
	
	/*
	 * (non-Javadoc) <p>Title: findPage</p> <p>Description: </p>
	 * @param adPosition
	 * @param pageable
	 * @return
	 * @see net.wit.service.AdService#findPage(net.wit.entity.AdPosition, net.wit.Pageable)
	 */

	@Override
	public Page<Ad> findPage(Tenant tenant,AdPosition adPosition, Pageable pageable) {
		return adDao.findPage(tenant,adPosition, pageable);
	}

	/*
	 * (non-Javadoc) <p>Title: findPage</p> <p>Description: </p>
	 * @param adPosition
	 * @param pageable
	 * @return
	 * @see net.wit.service.AdService#findPage(net.wit.entity.AdPosition, net.wit.Pageable)
	 */

	@Override
	public Page<Ad> findPage(AdPosition adPosition, Pageable pageable) {
		return adDao.findPage(adPosition, pageable);
	}

	@Override
	public Page<Ad> findPage(AdPosition adPosition,Area area,Long linkId, Pageable pageable) {
		return adDao.findPage(adPosition,area,linkId, pageable);
	}



	public Page<Ad> openPage(Tenant tenant, AdPosition adPosition, Area area, ProductChannel productChannel, Pageable pageable){
		return adDao.openPage(tenant,adPosition,area,productChannel, pageable);
	}
}