/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.entity.Brand;
import net.wit.entity.ProductCategory;
import net.wit.entity.Tag;
import net.wit.entity.Product.OrderType;

/**
 * Dao - 品牌
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface BrandDao extends BaseDao<Brand, Long> {

	/**
	 * @Title：findAllByProductCategory
	 * @Description：
	 * @param productCategory
	 * @return  List<Brand>
	 */
	List<Brand> findAllByProductCategory(ProductCategory productCategory);
	
	List<Brand> findList(Tag tag);

	List<Brand> search(String keyword,String phonetic, OrderType orderType);
}