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
import net.wit.entity.ProductCategory;
import net.wit.entity.Tag;
import net.wit.entity.Product.OrderType;

/**
 * Service - 品牌
 * @author rsico Team
 * @version 3.0
 */
public interface BrandService extends BaseService<Brand, Long> {

	/**
	 * 查找品牌(缓存)
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @param cacheRegion 缓存区域
	 * @return 品牌(缓存)
	 */
	List<Brand> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	List<Brand> findAllByProductCategory(ProductCategory productCategory);

	List<Brand> findList(Tag tag);
	
	List<Brand> search(String keyword,String phonetic, OrderType orderType);
}