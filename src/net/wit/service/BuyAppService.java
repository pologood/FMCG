/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.BuyApp;
import net.wit.entity.Member;

/**
 * Service - 应用
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface BuyAppService extends BaseService<BuyApp, Long> {

	void saveAndNew(BuyApp entity);
	/**
	 * 根据编号查找购买单
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 购买单，若不存在则返回null
	 */
	BuyApp findBySn(String sn);
	void notify(BuyApp buyApp) throws Exception;

	Page<BuyApp> findPage(Member member, Pageable pageable);
}