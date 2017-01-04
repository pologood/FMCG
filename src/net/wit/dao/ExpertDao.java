/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Expert;
import net.wit.entity.ExpertCategory;

/**
 * Dao - 专家
 * @author rsico Team
 * @version 3.0
 */
public interface ExpertDao extends BaseDao<Expert, Long> {

	/**
	 * @Title：findPage
	 * @Description：
	 * @param expertCategory
	 * @param pageable
	 * @return Page<Expert>
	 */
	Page<Expert> findPage(ExpertCategory expertCategory, Pageable pageable);

	/**
	 * @Title：findList
	 * @Description：
	 * @param expertCategory
	 * @return Page<Expert>
	 */
	List<Expert> findList(ExpertCategory expertCategory);

}