/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantCategoryDao;
import net.wit.dao.TenantRulesDao;
import net.wit.entity.ProductChannel;
import net.wit.entity.TenantCategory;
import net.wit.entity.TenantRules;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dao - 商家规则
 * @author rsico Team
 * @version 3.0
 */
@Repository("tenantRulesDaoImpl")
public class TenantRulesDaoImpl extends BaseDaoImpl<TenantRules, Long> implements TenantRulesDao {

	public List<TenantRules> findRoots() {
		String jpql = "select tenantRules from TenantRules tenantRules where tenantRules.parent is null order by tenantRules.id asc";
		TypedQuery<TenantRules> query = entityManager.createQuery(jpql, TenantRules.class).setFlushMode(FlushModeType.COMMIT);

		return query.getResultList();
	}

	public List<TenantRules> findParents(TenantRules tenantRules) {
		if (tenantRules == null || tenantRules.getParent() == null) {
			return Collections.<TenantRules> emptyList();
		}
		String jpql = "select tenantRules from TenantRules tenantRules where tenantRules.id in (:ids) order by tenantRules.id asc";
		TypedQuery<TenantRules> query = entityManager.createQuery(jpql, TenantRules.class).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}


	@Override
	public List<TenantRules> findTree(int depth) {
		return getTree(1,depth,null);
	}
	public List<TenantRules> getTree(int level,int depth,List<TenantRules> list) {
		if (level >depth) {
			return list;
		}

		TypedQuery<TenantRules> query;
		if (list == null) {
			//叶子节点
			String jpql = "select tenantRules from TenantRules tenantRules where tenantRules.lev in :lev order by tenantRules.id asc";

			query = entityManager.createQuery(jpql, TenantRules.class).setFlushMode(FlushModeType.COMMIT).setParameter("lev",level);

			list = query.getResultList();
			list = getTree(level+1,depth,list);
		} else if(list.size()>0) {
			int len = list.size();
			for (int i=0;i<len;i++) {
				//获取当前节点的父节点
				Long pid=list.get(i).getId();
				String jpql = "select tenantRules from TenantRules tenantRules where (tenantRules.lev in (:nlevel) and tenantRules.parent.id in (:pid) ) order by tenantRules.id asc";
				query = entityManager.createQuery(jpql, TenantRules.class).setFlushMode(FlushModeType.COMMIT);
				query=query.setParameter("nlevel",level);
				query=query.setParameter("pid",pid);
				List<TenantRules> _list = query.getResultList();
				list.get(i).setChildren(getTree(level+1,depth, _list));
			}
		}

		return list;
	}

	public Page<TenantRules> openPage(String keyword, Pageable pageable){

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TenantRules> criteriaQuery = criteriaBuilder.createQuery(TenantRules.class);
		Root<TenantRules> root = criteriaQuery.from(TenantRules.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();

		if (keyword != null) {
			restrictions = criteriaBuilder.and(
					restrictions, criteriaBuilder.or(
							criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("tenant").get("member").<String>get("username"), "%" + keyword + "%"))
			);
		}

		//restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), ActivityRules.Status.enabled));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
}