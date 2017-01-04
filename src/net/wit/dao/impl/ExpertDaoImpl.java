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

import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ExpertDao;
import net.wit.entity.Expert;
import net.wit.entity.ExpertCategory;

import org.springframework.stereotype.Repository;

/**
 * Dao - 专家
 * @author rsico Team
 * @version 3.0
 */
@Repository("expertDaoImpl")
public class ExpertDaoImpl extends BaseDaoImpl<Expert, Long> implements ExpertDao {

	public Page<Expert> findPage(ExpertCategory expertCategory, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Expert> criteriaQuery = criteriaBuilder.createQuery(Expert.class);
		Root<Expert> root = criteriaQuery.from(Expert.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (expertCategory != null) {
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("expertCategory"), expertCategory),
							criteriaBuilder.like(root.get("expertCategory").<String> get("treePath"), "%" + ExpertCategory.TREE_PATH_SEPARATOR + expertCategory.getId() + ExpertCategory.TREE_PATH_SEPARATOR + "%")));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	public List<Expert> findList(ExpertCategory expertCategory) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Expert> criteriaQuery = criteriaBuilder.createQuery(Expert.class);
		Root<Expert> root = criteriaQuery.from(Expert.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (expertCategory != null) {
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("expertCategory"), expertCategory),
							criteriaBuilder.like(root.get("expertCategory").<String> get("treePath"), "%" + ExpertCategory.TREE_PATH_SEPARATOR + expertCategory.getId() + ExpertCategory.TREE_PATH_SEPARATOR + "%")));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findList(criteriaQuery, null, null, null, new ArrayList<Order>());
	}

}