/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.dao.ModelYearDao;
import net.wit.entity.ModelYear;
import net.wit.service.ModelYearService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 年款
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("modelYearServiceImpl")
public class ModelYearServiceImpl extends BaseServiceImpl<ModelYear, Long> implements ModelYearService {

	@Resource(name = "modelYearDaoImpl")
	private ModelYearDao modelYearDao;

	@Resource(name = "modelYearDaoImpl")
	public void setBaseDao(ModelYearDao modelYearDao) {
		super.setBaseDao(modelYearDao);
	}

	@Override
	@Transactional
	@CacheEvict(value = "modelYear", allEntries = true)
	public void save(ModelYear modelYear) {
		super.save(modelYear);
	}

	@Override
	@Transactional
	@CacheEvict(value = "modelYear", allEntries = true)
	public ModelYear update(ModelYear modelYear) {
		return super.update(modelYear);
	}

	@Override
	@Transactional
	@CacheEvict(value = "modelYear", allEntries = true)
	public ModelYear update(ModelYear modelYear, String... ignoreProperties) {
		return super.update(modelYear, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "modelYear", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "modelYear", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "modelYear", allEntries = true)
	public void delete(ModelYear modelYear) {
		super.delete(modelYear);
	}
}