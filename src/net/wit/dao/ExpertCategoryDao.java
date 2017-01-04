/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.entity.ExpertCategory;

/**
 * Dao - 文章分类
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface ExpertCategoryDao extends BaseDao<ExpertCategory, Long> {

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<ExpertCategory> findRoots(Integer count);

	/**
	 * 查找上级文章分类
	 * 
	 * @param expertCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 上级文章分类
	 */
	List<ExpertCategory> findParents(ExpertCategory expertCategory, Integer count);

	/**
	 * 查找下级文章分类
	 * 
	 * @param expertCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 下级文章分类
	 */
	List<ExpertCategory> findChildren(ExpertCategory expertCategory, Integer count);

}