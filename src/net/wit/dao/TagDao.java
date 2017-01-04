/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tag;
import net.wit.entity.Tag.Type;

/**
 * Dao - 标签
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface TagDao extends BaseDao<Tag, Long> {

	/**
	 * 查找标签
	 * 
	 * @param type
	 *            类型
	 * @return 标签 
	 */
	List<Tag> findList(Type type);

	List<Tag> findList(Type type, Integer first, Integer count, List<Filter> filters, List<Order> orders);

	Page<Tag> openPage(Pageable pageable, Type type);
}