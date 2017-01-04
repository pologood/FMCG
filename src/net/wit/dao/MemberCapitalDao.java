/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.MemberCapital;
import net.wit.entity.MemberRank;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Dao -
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface MemberCapitalDao extends BaseDao<MemberCapital, Long> {

    Page<MemberCapital> findPageByDate(Date begin_date, Date end_date,String keyword, Pageable pageable);
    List<MemberCapital> findListByDate(Date begin_date, Date end_date,String keyword, List<Filter> filters);

}