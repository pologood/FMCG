/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.Collections;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BuyAppDao;
import net.wit.entity.BuyApp;
import net.wit.entity.Member;

import org.springframework.stereotype.Repository;

/**
 * Dao - 购买应用
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("buyAppDaoImpl")
public class BuyAppDaoImpl extends BaseDaoImpl<BuyApp, Long> implements BuyAppDao {

	public BuyApp findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select buyApp from BuyApp buyApp where lower(buyApp.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, BuyApp.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Page<BuyApp> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<BuyApp>(Collections.<BuyApp> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BuyApp> criteriaQuery = criteriaBuilder.createQuery(BuyApp.class);
		Root<BuyApp> root = criteriaQuery.from(BuyApp.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
}