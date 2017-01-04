/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

import java.util.Date;

/**
 * Dao - 广告
 * @author rsico Team
 * @version 3.0
 */
public interface MonthlyDao extends BaseDao<Monthly, Long> {
	/**
	 * 查找月结帐分页
	 *
	 */
	Page<Monthly> findMonthlyPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable);


}