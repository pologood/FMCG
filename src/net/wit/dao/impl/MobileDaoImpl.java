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
import net.wit.dao.MobileDao;
import net.wit.entity.Member;
import net.wit.entity.Mobile;
import net.wit.entity.Order;

import org.springframework.stereotype.Repository;

/**
 * Dao - 手机快充
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("mobileDaoImpl")
public class MobileDaoImpl extends BaseDaoImpl<Mobile, Long> implements MobileDao {

	public Page<Mobile> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<Mobile>(Collections.<Mobile> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Mobile> criteriaQuery = criteriaBuilder.createQuery(Mobile.class);
		Root<Mobile> root = criteriaQuery.from(Mobile.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public Mobile findbySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select mobile from Mobile mobile where mobile.sn = :sn";
		try {
			return entityManager.createQuery(jpql, Mobile.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}