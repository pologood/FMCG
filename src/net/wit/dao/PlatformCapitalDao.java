/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.PlatformCapital;

import java.util.Date;
import java.util.List;

/**
 * Dao -
 *
 * @author rsico Team
 * @version 3.0
 */
public interface PlatformCapitalDao extends BaseDao<PlatformCapital, Long> {

    Page<PlatformCapital> findPageByDate(Date begin_date, Date end_date, Pageable pageable);
    List<PlatformCapital> findListByDate(Date begin_date, Date end_date, List<Filter> filters);

}