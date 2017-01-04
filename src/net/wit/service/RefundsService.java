/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Refunds;

/**
 * Service - 退款单
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface RefundsService extends BaseService<Refunds, Long> {
	public Page<Refunds> findPage(Date beginDate, Date endDate,Pageable pageable);
}