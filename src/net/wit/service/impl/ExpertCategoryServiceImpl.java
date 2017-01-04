/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.wit.dao.ExpertCategoryDao;
import net.wit.entity.ExpertCategory;
import net.wit.service.ExpertCategoryService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 文章分类
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("expertCategoryServiceImpl")
public class ExpertCategoryServiceImpl extends BaseServiceImpl<ExpertCategory, Long> implements ExpertCategoryService {

	@Resource(name = "expertCategoryDaoImpl")
	private ExpertCategoryDao expertCategoryDao;

	@Resource(name = "expertCategoryDaoImpl")
	public void setBaseDao(ExpertCategoryDao expertCategoryDao) {
		super.setBaseDao(expertCategoryDao);
	}

	@Transactional(readOnly = true)
	public List<ExpertCategory> findRoots() {
		return expertCategoryDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<ExpertCategory> findRoots(Integer count) {
		return expertCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	@Cacheable("expertCategory")
	public List<ExpertCategory> findRoots(Integer count, String cacheRegion) {
		return expertCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<ExpertCategory> findParents(ExpertCategory expertCategory) {
		return expertCategoryDao.findParents(expertCategory, null);
	}

	@Transactional(readOnly = true)
	public List<ExpertCategory> findParents(ExpertCategory expertCategory, Integer count) {
		return expertCategoryDao.findParents(expertCategory, count);
	}

	@Transactional(readOnly = true)
	@Cacheable("expertCategory")
	public List<ExpertCategory> findParents(ExpertCategory expertCategory, Integer count, String cacheRegion) {
		return expertCategoryDao.findParents(expertCategory, count);
	}

	@Transactional(readOnly = true)
	public List<ExpertCategory> findTree() {
		return expertCategoryDao.findChildren(null, null);
	}

	@Transactional(readOnly = true)
	public List<ExpertCategory> findChildren(ExpertCategory expertCategory) {
		return expertCategoryDao.findChildren(expertCategory, null);
	}

	@Transactional(readOnly = true)
	public List<ExpertCategory> findChildren(ExpertCategory expertCategory, Integer count) {
		return expertCategoryDao.findChildren(expertCategory, count);
	}

	@Transactional(readOnly = true)
	@Cacheable("expertCategory")
	public List<ExpertCategory> findChildren(ExpertCategory expertCategory, Integer count, String cacheRegion) {
		return expertCategoryDao.findChildren(expertCategory, count);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory" }, allEntries = true)
	public void save(ExpertCategory expertCategory) {
		super.save(expertCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory" }, allEntries = true)
	public ExpertCategory update(ExpertCategory expertCategory) {
		return super.update(expertCategory);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory" }, allEntries = true)
	public ExpertCategory update(ExpertCategory expertCategory, String... ignoreProperties) {
		return super.update(expertCategory, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory" }, allEntries = true)
	public void delete(ExpertCategory expertCategory) {
		super.delete(expertCategory);
	}

}