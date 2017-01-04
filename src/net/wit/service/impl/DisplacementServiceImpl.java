/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.dao.DisplacementDao;
import net.wit.entity.Displacement;
import net.wit.service.DisplacementService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 排量
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("displacementServiceImpl")
public class DisplacementServiceImpl extends BaseServiceImpl<Displacement, Long> implements DisplacementService {

	@Resource(name = "displacementDaoImpl")
	private DisplacementDao displacementDao;

	@Resource(name = "displacementDaoImpl")
	public void setBaseDao(DisplacementDao displacementDao) {
		super.setBaseDao(displacementDao);
	}

	@Override
	@Transactional
	@CacheEvict(value = "displacement", allEntries = true)
	public void save(Displacement displacement) {
		super.save(displacement);
	}

	@Override
	@Transactional
	@CacheEvict(value = "displacement", allEntries = true)
	public Displacement update(Displacement displacement) {
		return super.update(displacement);
	}

	@Override
	@Transactional
	@CacheEvict(value = "displacement", allEntries = true)
	public Displacement update(Displacement displacement, String... ignoreProperties) {
		return super.update(displacement, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "displacement", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "displacement", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "displacement", allEntries = true)
	public void delete(Displacement displacement) {
		super.delete(displacement);
	}
}