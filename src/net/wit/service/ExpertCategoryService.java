/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.List;

import net.wit.entity.ExpertCategory;

/**
 * Service - 文章分类
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface ExpertCategoryService extends BaseService<ExpertCategory, Long> {

	/**
	 * 查找顶级文章分类
	 * 
	 * @return 顶级文章分类
	 */
	List<ExpertCategory> findRoots();

	/**
	 * 查找顶级文章分类
	 * 
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<ExpertCategory> findRoots(Integer count);

	/**
	 * 查找顶级文章分类(缓存)
	 * 
	 * @param count
	 *            数量
	 * @param cacheRegion
	 *            缓存区域
	 * @return 顶级文章分类(缓存)
	 */
	List<ExpertCategory> findRoots(Integer count, String cacheRegion);

	/**
	 * 查找上级文章分类
	 * 
	 * @param expertCategory
	 *            文章分类
	 * @return 上级文章分类
	 */
	List<ExpertCategory> findParents(ExpertCategory expertCategory);

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
	 * 查找上级文章分类(缓存)
	 * 
	 * @param expertCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @param cacheRegion
	 *            缓存区域
	 * @return 上级文章分类(缓存)
	 */
	List<ExpertCategory> findParents(ExpertCategory expertCategory, Integer count, String cacheRegion);

	/**
	 * 查找文章分类树
	 * 
	 * @return 文章分类树
	 */
	List<ExpertCategory> findTree();

	/**
	 * 查找下级文章分类
	 * 
	 * @param expertCategory
	 *            文章分类
	 * @return 下级文章分类
	 */
	List<ExpertCategory> findChildren(ExpertCategory expertCategory);

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

	/**
	 * 查找下级文章分类(缓存)
	 * 
	 * @param expertCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @param cacheRegion
	 *            缓存区域
	 * @return 下级文章分类(缓存)
	 */
	List<ExpertCategory> findChildren(ExpertCategory expertCategory, Integer count, String cacheRegion);

}