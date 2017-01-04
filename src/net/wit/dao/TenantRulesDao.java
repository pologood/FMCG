/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ProductChannel;
import net.wit.entity.TenantCategory;
import net.wit.entity.TenantRules;

import java.util.List;

/**
 * Dao - 商家规则
 * @author rsico Team
 * @version 3.0
 */
public interface TenantRulesDao extends BaseDao<TenantRules, Long> {
	/**
	 * 查找顶级规则
	 * @return 顶级规则
	 */
	List<TenantRules> findRoots();



	/**
	 * 查找上级规则
	 * @param rules 规则
	 * @return 上级规则
	 */
	List<TenantRules> findParents(TenantRules tenantRules);



	/**
	 * 查找下级规则
	 * @param rules 规则
	 * @return 下级规则
	 */
	List<TenantRules> findTree(int depth);


	Page<TenantRules> openPage(String keyword, Pageable pageable);
}