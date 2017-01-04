/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Expert;
import net.wit.entity.ExpertCategory;

/**
 * Service - 专家
 * @author rsico Team
 * @version 3.0
 */
public interface ExpertService extends BaseService<Expert, Long> {

	/**
	 * @Title：findPage
	 * @Description：
	 * @param expertCategory
	 * @param pageable
	 * @return Object
	 */
	Page<Expert> findPage(ExpertCategory expertCategory, Pageable pageable);

	List<Expert> findList(ExpertCategory expertCategory);

}