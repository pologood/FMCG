/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.List;

import net.wit.Filter;
import net.wit.Order;
import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;

/**
 * Service - 品牌
 * @author rsico Team
 * @version 3.0
 */
public interface BrandSeriesService extends BaseService<BrandSeries, Long> {
	/**
	 * 查找顶级文章分类
	 * @return 顶级文章分类
	 */
	List<BrandSeries> findRoots();

	/**
	 * 查找顶级文章分类
	 * @param count 数量
	 * @return 顶级文章分类
	 */
	List<BrandSeries> findRoots(Integer count);

	/**
	 * 查找顶级文章分类(缓存)
	 * @param count 数量
	 * @param cacheRegion 缓存区域
	 * @return 顶级文章分类(缓存)
	 */
	List<BrandSeries> findRoots(Integer count, String cacheRegion);

	/**
	 * 查找上级文章分类
	 * @param brandSeries 文章分类
	 * @return 上级文章分类
	 */
	List<BrandSeries> findParents(BrandSeries brandSeries);

	/**
	 * 查找上级文章分类
	 * @param brandSeries 文章分类
	 * @param count 数量
	 * @return 上级文章分类
	 */
	List<BrandSeries> findParents(BrandSeries brandSeries, Integer count);

	/**
	 * 查找上级文章分类(缓存)
	 * @param brandSeries 文章分类
	 * @param count 数量
	 * @param cacheRegion 缓存区域
	 * @return 上级文章分类(缓存)
	 */
	List<BrandSeries> findParents(BrandSeries brandSeries, Integer count, String cacheRegion);

	/**
	 * 查找文章分类树
	 * @return 文章分类树
	 */
	List<BrandSeries> findTree(Brand brand);

	/**
	 * 查找下级文章分类
	 * @param brandSeries 文章分类
	 * @return 下级文章分类
	 */
	List<BrandSeries> findChildren(BrandSeries brandSeries);

	/**
	 * 查找下级文章分类
	 * @param brandSeries 文章分类
	 * @param count 数量
	 * @return 下级文章分类
	 */
	List<BrandSeries> findChildren(BrandSeries brandSeries, Integer count);

	/**
	 * 查找下级文章分类(缓存)
	 * @param brandSeries 文章分类
	 * @param count 数量
	 * @param cacheRegion 缓存区域
	 * @return 下级文章分类(缓存)
	 */
	List<BrandSeries> findChildren(BrandSeries brandSeries, Integer count, String cacheRegion);


	/**
	 * 查找品牌(缓存)
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @param cacheRegion 缓存区域
	 * @return 品牌(缓存)
	 */
	List<BrandSeries> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);


	/**
	 * 查找下级系列
	 * 
	 * @param brandSeries
	 *            系列
	 * @param count
	 *            数量
	 * @return 下级系列
	 */
	List<BrandSeries> findByBrand(Brand brand, Integer count, String cacheRegion);
	
}