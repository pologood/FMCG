/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Order.Direction;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ExpertConsultationDao;
import net.wit.entity.Consultation;
import net.wit.entity.ExpertCategory;
import net.wit.entity.ExpertConsultation;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Consultation.Type;

import org.springframework.stereotype.Repository;

/**
 * Dao - 咨询
 * @author rsico Team
 * @version 3.0
 */
@Repository("expertConsultationDaoImpl")
public class ExpertConsultationDaoImpl extends BaseDaoImpl<ExpertConsultation, Long> implements ExpertConsultationDao {

	public List<ExpertConsultation> findList(Boolean hasRepaly, Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExpertConsultation> criteriaQuery = criteriaBuilder.createQuery(ExpertConsultation.class);
		Root<ExpertConsultation> root = criteriaQuery.from(ExpertConsultation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (hasRepaly != null && hasRepaly) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forExpertConsultation")));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (expertCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("expertCategory"), expertCategory));
		}
		if (isShow != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

	public List<ExpertConsultation> findList(Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExpertConsultation> criteriaQuery = criteriaBuilder.createQuery(ExpertConsultation.class);
		Root<ExpertConsultation> root = criteriaQuery.from(ExpertConsultation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forConsultation")));
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (expertCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("expertCategory"), expertCategory));
		}
		if (isShow != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

	public Long count(Member member, ExpertCategory expertCategory, Boolean isShow) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExpertConsultation> criteriaQuery = criteriaBuilder.createQuery(ExpertConsultation.class);
		Root<ExpertConsultation> root = criteriaQuery.from(ExpertConsultation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forExpertConsultation")));
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (expertCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("expertCategory"), expertCategory));
		}
		if (isShow != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public Page<ExpertConsultation> findPage(Member member, ExpertCategory expertCategory, Boolean isShow, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExpertConsultation> criteriaQuery = criteriaBuilder.createQuery(ExpertConsultation.class);
		Root<ExpertConsultation> root = criteriaQuery.from(ExpertConsultation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forExpertConsultation")));
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (expertCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("expertCategory"), expertCategory));
		}
		if (isShow != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public List<ExpertConsultation> findListReply(ExpertConsultation expertConsultation) {
		if (expertConsultation == null) {
			return new ArrayList<ExpertConsultation>();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExpertConsultation> criteriaQuery = criteriaBuilder.createQuery(ExpertConsultation.class);
		Root<ExpertConsultation> root = criteriaQuery.from(ExpertConsultation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("forExpertConsultation"), expertConsultation));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), true));

		criteriaQuery.where(restrictions);
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order("createDate", Direction.desc));
		return super.findList(criteriaQuery, null, null, null, orders);
	}
}