/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.*;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ConsumerDao;
import net.wit.entity.Consumer;
import net.wit.entity.Consumer.Status;
import net.wit.entity.Member.Gender;
import net.wit.entity.Tenant;

/**
 * Dao - 会员
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("consumerDaoImpl")
public class ConsumerDaoImpl extends BaseDaoImpl<Consumer, Long> implements ConsumerDao {

	/** 查询我的客户 */
	public Page<Consumer> findPage(Tenant tenant, Status status, Pageable pageable) {
		if (tenant == null) {
			return new Page<Consumer>(Collections.<Consumer> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Consumer> criteriaQuery = criteriaBuilder.createQuery(Consumer.class);
		Root<Consumer> root = criteriaQuery.from(Consumer.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Consumer> findPage(Tenant tenant,String keyword, Status status,Gender gender, Pageable pageable) {
		if (tenant == null) {
			return new Page<Consumer>(Collections.<Consumer> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Consumer> criteriaQuery = criteriaBuilder.createQuery(Consumer.class);
		Root<Consumer> root = criteriaQuery.from(Consumer.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if(gender!=null){//查询男女
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("member").<String> get("gender"), gender));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(
							criteriaBuilder.like(root.get("member").get("member").<String> get("name"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("member").get("member").<String> get("nickName"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("member").get("member").<String> get("username"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("member").<String> get("name"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("member").<String> get("nickName"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("member").<String> get("username"), "%" + keyword + "%")
					));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * 统计我的客户总数
	 */
	public Map<String, String> memberCounts(Long tenantid) {
		StringBuffer hsql = new StringBuffer(
				"SELECT COUNT(c.member) as membercounts FROM xx_consumer c WHERE c.tenant = :tenant");
		Query query = entityManager.createNativeQuery(hsql.toString()).setFlushMode(FlushModeType.COMMIT)
				.setParameter("tenant", tenantid);
		try {
			Map<String, String> map = new HashMap<String, String>();
			List<?> data = query.getResultList();
			for (Object obj : data) {
				// Object[] row = (Object[]) obj;
				map.put("memberCounts", obj.toString());
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 统计我的客户不同等级会员人数
	 */
	public Map<String, String> vipcounts(Long tenantid, String vittype) {
		StringBuffer hsql = new StringBuffer(
				"SELECT m.name,COUNT(c.member_rank) as vipcounts FROM xx_consumer c , xx_member_rank m WHERE c.tenant = :tenant and m.name=:name  and  c.member_rank = m.id and c.status=1 GROUP BY c.member_rank");
		Query query = entityManager.createNativeQuery(hsql.toString());
		query.setFlushMode(FlushModeType.COMMIT);
		query.setParameter("tenant", tenantid);
		query.setParameter("name", vittype);
		try {
			List<?> data = query.getResultList();
			Map<String, String> map = new HashMap<String, String>();
			if (data.size() > 0) {
				for (Object obj : data) {
					Object[] row = (Object[]) obj;
					map.put(row[0].toString(), row[1].toString());
				}
			} else {
				map.put(vittype, "0");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 统计我的客户中男女数量
	 */
	public Map<String, String> ganercounts(Long tenantid, int gener) {

		StringBuffer hsql = new StringBuffer(
				"SELECT m.gender , COUNT(m.id) as gendercounts FROM xx_member m WHERE m.id in (");
		hsql.append("SELECT c.member FROM xx_consumer c WHERE c.tenant = :tenant and c.status=1");
		hsql.append(") and m.gender=:gender GROUP BY m.gender ");
		Query query = entityManager.createNativeQuery(hsql.toString());
		query.setFlushMode(FlushModeType.COMMIT);
		query.setParameter("tenant", tenantid);
		query.setParameter("gender", gener);
		
		try {
			List<?> data = query.getResultList();
			Map<String, String> map = new HashMap<String, String>();
			
			if(data.size()>0){
				for (Object obj : data) {
					Object[] row = (Object[]) obj;
					String g = gener==1?"女":"男";
					map.put(g, row[1].toString());
				}
			}else{
				String g = gener==1?"女":"男";
				map.put(g, "0");
			}
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 检测当前用户是否是当前店铺的会员
	 */

	public boolean consumerExists(Long memberId,Long tenantId) {
		if (memberId == null) {
			return false;
		}

		if (tenantId == null) {
			return false;
		}
		String jpql = "select count(*) from Consumer consumer where lower(consumer.member) = lower(:memberId) and  lower(consumer.tenant) = lower(:tenantId) ";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("memberId", memberId).setParameter("tenantId", tenantId).getSingleResult();
		return count > 0;
	}

	@Override
	public List<Consumer> findByAddList(Tenant tenant, Date beginDate, Date endDate) {
		if (tenant == null) {
			return new ArrayList<Consumer>(Collections.<Consumer>emptyList());
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Consumer> criteriaQuery = criteriaBuilder.createQuery(Consumer.class);
		Root<Consumer> root = criteriaQuery.from(Consumer.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if(beginDate!=null){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),beginDate));
		}
		if(endDate!=null){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),endDate));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public Page<Consumer> findByAddPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable) {
		if (tenant == null) {
			return new Page<Consumer>(Collections.<Consumer>emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Consumer> criteriaQuery = criteriaBuilder.createQuery(Consumer.class);
		Root<Consumer> root = criteriaQuery.from(Consumer.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if(beginDate!=null){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),beginDate));
		}
		if(endDate!=null){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),endDate));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Long count(Tenant tenant, Status status, Date beginDate, Date endDate) {
		if (tenant == null) {
			return 0l;
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Consumer> criteriaQuery = criteriaBuilder.createQuery(Consumer.class);
		Root<Consumer> root = criteriaQuery.from(Consumer.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(tenant!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("tenant"),tenant));
		}
		if(status!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("status"),status));
		}
		if(beginDate!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),beginDate));
		}
		if(endDate!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),endDate));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}
}