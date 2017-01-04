/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;

/**
 * Dao - 品牌
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface BrandSeriesDao extends BaseDao<BrandSeries, Long> {


	/**
	 * 查找顶级系列
	 * 
	 * @param count
	 *            数量
	 * @return 顶级系列
	 */
	List<BrandSeries> findRoots(Integer count);

	/**
	 * 查找上级系列
	 * 
	 * @param brandSeries
	 *            系列
	 * @param count
	 *            数量
	 * @return 上级系列
	 */
	List<BrandSeries> findParents(BrandSeries brandSeries, Integer count);

	/**
	 * 查找下级系列
	 * 
	 * @param brandSeries
	 *            系列
	 * @param count
	 *            数量
	 * @return 下级系列
	 */
	List<BrandSeries> findChildren(BrandSeries brandSeries, Integer count);

	/**
	 * 查找下级系列
	 * 
	 * @param brandSeries
	 *            系列
	 * @param count
	 *            数量
	 * @return 下级系列
	 */
	List<BrandSeries> findTree(BrandSeries brandSeries, Integer count);

	/**
	 * 查找下级系列
	 * 
	 * @param brandSeries
	 *            系列
	 * @param count
	 *            数量
	 * @return 下级系列
	 */
	List<BrandSeries> findbyBrand(Brand brand, Integer count, String cacheRegion);

}