/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.BuyApp;
import net.wit.entity.Member;

/**
 * Dao - 购买应用
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface BuyAppDao extends BaseDao<BuyApp, Long> {

	/**
	 * 根据编号查找购买单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 购买单，若不存在则返回null
	 */
	BuyApp findBySn(String sn);
	
	Page<BuyApp> findPage(Member member, Pageable pageable);
}