/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AdDao;
import net.wit.dao.MonthlyDao;
import net.wit.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Dao - 广告
 * @author rsico Team
 * @version 3.0
 */
@Repository("monthlyDaoImpl")
public class MonthlyDaoImpl extends BaseDaoImpl<Monthly, Long> implements MonthlyDao {
	public Page<Monthly> findMonthlyPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Monthly> criteriaQuery = criteriaBuilder.createQuery(Monthly.class);
		Root<Monthly> root = criteriaQuery.from(Monthly.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
        }
        criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}



}