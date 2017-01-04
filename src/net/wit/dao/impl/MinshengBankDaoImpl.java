/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.MinshengBankDao;
import net.wit.entity.Member;
import net.wit.entity.MinshengBank;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;

import org.springframework.stereotype.Repository;

/**
 * Dao - 民生银行转账
 * @author rsico Team
 * @version 3.0
 */
@Repository("minshengbankDaoImpl")
public class MinshengBankDaoImpl extends BaseDaoImpl<MinshengBank, Long> implements MinshengBankDao {

	public MinshengBank findBySn(String insId) {
		if (insId == null) {
			return null;
		}
		String jpql = "select minshengbank from MinshengBank minshengbank where lower(minshengbank.insId) = lower(:insId)";
		try {
			return entityManager.createQuery(jpql, MinshengBank.class).setFlushMode(FlushModeType.COMMIT).setParameter("insId", insId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public MinshengBank findbyMinshengPayNo(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select minshengBank from MinshengBank minshengBank where minshengBank.orderNo = :sn order by id desc  limit 1 ";
		try {
			MinshengBank minshengbank =  entityManager.createQuery(jpql, MinshengBank.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
			return minshengbank;
			//return entityManager.createQuery(jpql, MinshengBank.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<MinshengBank> findWaitReleaseList(Integer first, Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MinshengBank> criteriaQuery = criteriaBuilder.createQuery(MinshengBank.class);
		Root<MinshengBank> root = criteriaQuery.from(MinshengBank.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), Payment.Status.wait));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, first, count, null, null);
	}

	public Page<MinshengBank> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<MinshengBank>(Collections.<MinshengBank> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MinshengBank> criteriaQuery = criteriaBuilder.createQuery(MinshengBank.class);
		Root<MinshengBank> root = criteriaQuery.from(MinshengBank.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("status"), Payment.Status.wait));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/*
	 * (non-Javadoc) <p>Title: findPage</p> <p>Description: </p>
	 * @param method
	 * @param status
	 * @param pageable
	 * @return
	 * @see net.wit.dao.PaymentDao#findPage(net.wit.entity.PaymentMethod.Method, net.wit.entity.Payment.Status, net.wit.Pageable)
	 */

	@Override
	public Page<MinshengBank> findPage(Method method, Status status, Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MinshengBank> criteriaQuery = criteriaBuilder.createQuery(MinshengBank.class);
		Root<MinshengBank> root = criteriaQuery.from(MinshengBank.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (method != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("method"), method));
		}
		
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public MinshengBank findbyOrderNo(String sn) {
		// TODO Auto-generated method stub
		return null;
	}
}