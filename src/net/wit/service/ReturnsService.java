/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Returns;

/**
 * Service - 退货单
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface ReturnsService extends BaseService<Returns, Long> {

	public Page<Returns> findPage(Date beginDate, Date endDate,Pageable pageable);
}