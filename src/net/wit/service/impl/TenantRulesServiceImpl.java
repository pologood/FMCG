/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantCategoryDao;
import net.wit.dao.TenantRulesDao;
import net.wit.entity.*;
import net.wit.service.TenantCategoryService;
import net.wit.service.TenantRulesService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 商家规则实现
 * @author rsico Team
 * @version 3.0
 */
@Service("tenantRulesServiceImpl")
public class TenantRulesServiceImpl extends BaseServiceImpl<TenantRules, Long> implements TenantRulesService {

	@Resource(name="tenantRulesDaoImpl")
	private TenantRulesDao tenantRulesDao;

	@Resource(name = "tenantCategoryDaoImpl")
	public void setBaseDao(TenantCategoryDao tenantCategoryDao) {
		super.setBaseDao(tenantRulesDao);
	}
	@Transactional(readOnly = true)
	public List<TenantRules> findRoots() {
		return tenantRulesDao.findRoots();
	}

	@Transactional(readOnly = true)
	public List<TenantRules> findParents(TenantRules tenantRules) {
		return tenantRulesDao.findParents(tenantRules);
	}

	@Transactional(readOnly = true)
	public List<TenantRules> findTree(int depth) {
		return tenantRulesDao.findTree(depth);
	}

	@Transactional(readOnly = true)
	public Page<TenantRules> openPage(String searchValue, Pageable pageable){
		return  tenantRulesDao.openPage( searchValue,  pageable);
	}

}