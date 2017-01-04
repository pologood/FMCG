/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.entity.Goods;

/**
 * Dao - 货品
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface GoodsDao extends BaseDao<Goods, Long> {

	void persist(Goods goods);
	
	Goods merge(Goods goods);
}