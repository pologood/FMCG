/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.math.BigDecimal;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.Article;
import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;
import net.wit.entity.Community;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tenant;
import net.wit.entity.TenantCategory;

/**
 * Service - 搜索
 * @author rsico Team
 * @version 3.0
 */
public interface SearchService {

	/**
	 * 创建索引
	 */
	void index();

	/**
	 * 创建索引
	 * @param type 索引类型
	 */
	void index(Class<?> type);

	/**
	 * 创建索引
	 * @param article 文章
	 */
	void index(Article article);

	/**
	 * 创建索引
	 * @param product 商品
	 */
	void index(Product product);

	/**
	 * 创建索引
	 * @param delivery 商品
	 */
	void index(DeliveryCenter delivery);

	/**
	 * 删除索引
	 */
	void purge();

	/**
	 * 删除索引
	 * @param type 索引类型
	 */
	void purge(Class<?> type);

	/**
	 * 删除索引
	 * @param article 文章
	 */
	void purge(Article article);

	/**
	 * 删除索引
	 * @param product 商品
	 */
	void purge(Product product);

	/**
	 * 搜索文章分页
	 * @param keyword 关键词
	 * @param pageable 分页信息
	 * @return 文章分页
	 */
	Page<Article> search(String keyword, Pageable pageable);

	/**
	 * 搜索商品分页
	 * @param keyword 关键词
	 * @param startPrice 最低价格
	 * @param endPrice 最高价格
	 * @param orderType 排序类型
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Product> search(String keyword, ProductCategory productCategory, Brand brand, Area area, Community community, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable);

	/**
	 * 搜索商品分页
	 * @param keyword 关键词
	 * @param tenantCategory 最低价格
	 * @param area 最高价格
	 * @param orderType 排序类型
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
    Page<DeliveryCenter> search(String keyword, TenantCategory tenantCategory, Area area, Community community, Tenant.OrderType orderType,Tenant.Status status, Pageable pageable);

}