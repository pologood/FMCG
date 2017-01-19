/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.Calendar;
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
import net.wit.dao.PaymentDao;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Payment;
import net.wit.entity.Trade;
import net.wit.entity.Payment.Type;

import org.springframework.stereotype.Repository;

/**
 * Dao - 收款单
 * @author rsico Team
 * @version 3.0
 */
@Repository("paymentDaoImpl")
public class PaymentDaoImpl extends BaseDaoImpl<Payment, Long> implements PaymentDao {

	public Payment findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select payment from Payment payment where lower(payment.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Payment findByPaySn(String paySn) {
		if (paySn == null) {
			return null;
		}
		String jpql = "select payment from Payment payment where lower(payment.paySn) = lower(:paySn)";
		try {
			return entityManager.createQuery(jpql, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("paySn", paySn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Payment> findWaitReleaseList(Integer first, Integer count) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, 30);

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Payment> criteriaQuery = criteriaBuilder.createQuery(Payment.class);
		Root<Payment> root = criteriaQuery.from(Payment.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), Payment.Status.wait));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), calendar.getTime()));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, first, count, null, null);
	}

	public Page<Payment> findPage(Member member, Pageable pageable, Payment.Type type) {
		if (member == null) {
			return new Page<Payment>(Collections.<Payment> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Payment> criteriaQuery = criteriaBuilder.createQuery(Payment.class);
		Root<Payment> root = criteriaQuery.from(Payment.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("status"), Payment.Status.wait));
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
	public Page<Payment> findPage(String paymentMethod, Type type, Date beginDate, Date endDate, String tenantName, String username, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Payment> criteriaQuery = criteriaBuilder.createQuery(Payment.class);
		Root<Payment> root = criteriaQuery.from(Payment.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (paymentMethod != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod));
		}
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if (tenantName!=null&&!"".equals(tenantName)) {
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("member").get("tenant").<String> get("name"), tenantName));
		}
		if (username!=null&&!"".equals(username)) {
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.equal(root.get("member").<String> get("name"), username),criteriaBuilder.equal(root.get("member").<String> get("username"), username)));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Payment findByTrade(Trade trade,Payment.Status status) {
		if (trade == null) {
			return null;
		}
		if(status==null){
			return null;
		}
		String jpql = "select payment from Payment payment where payment.trade = :trade and payment.status=:status";
		try {
			return entityManager.createQuery(jpql, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("trade", trade).setParameter("status",status).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Payment findByPayment(PayBill payBill) {
		if (payBill == null) {
			return null;
		}
		String jpql = "select payment from Payment payment where payment.payBill = :payBill";
		try {
			return entityManager.createQuery(jpql, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("payBill", payBill).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}