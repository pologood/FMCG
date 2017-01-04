/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantRelationDao;
import net.wit.entity.Credit;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.entity.TenantRelation.Status;

import org.springframework.stereotype.Repository;

/**
 * Dao - 加盟关系
 * @author rsico Team
 * @version 3.0
 */
@Repository("tenantRelationDaoImpl")
public class TenantRelationDaoImpl extends BaseDaoImpl<TenantRelation, Long> implements TenantRelationDao {

	/** 查询我的客户 */
	public Page<TenantRelation> findPage(Tenant tenant, Status status, Pageable pageable) {
		if (tenant == null) {
			return new Page<TenantRelation>(Collections.<TenantRelation> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TenantRelation> criteriaQuery = criteriaBuilder.createQuery(TenantRelation.class);
		Root<TenantRelation> root = criteriaQuery.from(TenantRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), tenant));
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<TenantRelation> findRelation(Tenant parent,Tenant tenant,Pageable pageable) {
		if (tenant == null) {
			return new Page<TenantRelation>(Collections.<TenantRelation> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TenantRelation> criteriaQuery = criteriaBuilder.createQuery(TenantRelation.class);
		Root<TenantRelation> root = criteriaQuery.from(TenantRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("parent"), parent));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}


	/** 查询我的上级 */
	public Page<TenantRelation> findParent(Tenant tenant,Status status,Pageable pageable) {
		if (tenant == null) {
			return new Page<TenantRelation>(Collections.<TenantRelation> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TenantRelation> criteriaQuery = criteriaBuilder.createQuery(TenantRelation.class);
		Root<TenantRelation> root = criteriaQuery.from(TenantRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if(status==null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), Status.success));
		}

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	/** 查询我的上级 */
	public Page<TenantRelation> findMyParent(Tenant tenant,Status status,Pageable pageable) {
		if (tenant == null) {
			return new Page<TenantRelation>(Collections.<TenantRelation> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TenantRelation> criteriaQuery = criteriaBuilder.createQuery(TenantRelation.class);
		Root<TenantRelation> root = criteriaQuery.from(TenantRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if(status!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}


	public List<TenantRelation> findMyParent(Tenant tenant,Status status) {
		if (tenant == null) {
			return new ArrayList<>();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TenantRelation> criteriaQuery = criteriaBuilder.createQuery(TenantRelation.class);
		Root<TenantRelation> root = criteriaQuery.from(TenantRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if(status!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery,null,null,null,null);
	}

	public Boolean relationExists(Tenant tenant, Tenant parent) {
		String jpql = "select count(tenantRelations) from TenantRelation tenantRelations where tenantRelations.tenant = :tenant and tenantRelations. parent=:parent";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("parent", parent).getSingleResult();
		return count > 0;
	}

	public Boolean relationExists(Tenant tenant,Status status) {
		String jpql = "select count(tenantRelations) from TenantRelation tenantRelations where tenantRelations.tenant = :tenant and tenantRelations.status=:status";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("status", status).getSingleResult();
		return count > 0;
	}

	@Override
	public List<Long> findRelations(Tenant tenant, Tenant parent) {
		String jpql = "select id from TenantRelation  where tenant = :tenant and parent=:parent";
		List<Long> ids = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("parent", parent).getResultList();
		return ids;
	}

}