/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import org.springframework.stereotype.Repository;

import net.wit.dao.EquipmentDao;

import java.util.List;

/**
 * Dao - 
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("equipmentDaoImpl")
public class EquipmentDaoImpl extends BaseDaoImpl<Equipment, Long> implements EquipmentDao {

	/**
	 * 根据设备号查找
	 * @param uuid 
	 * @return 设备，若不存在则返回null
	 */
	public Equipment findByUUID(String uuid) {
		if (uuid == null) {
			return null;
		}
		String jpql = "select equipment from Equipment equipment where lower(equipment.uuid) = lower(:uuid)";
		try {
			return entityManager.createQuery(jpql, Equipment.class).setFlushMode(FlushModeType.COMMIT).setParameter("uuid", uuid).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
	}

	public Page<Equipment> findPage(String keyWord, Equipment.Status status, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Equipment> criteriaQuery = criteriaBuilder.createQuery(Equipment.class);
		Root<Equipment> root = criteriaQuery.from(Equipment.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (keyWord != null) {
			for (Filter filter : pageable.getFilters()){
				if(filter.getProperty().equals("tenant")){
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("store").<String>get("name"), "%" + keyWord + "%"));
					break;
				}
				if(filter.getProperty().equals("store")){
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keyWord + "%"));
					break;
				}
			}

			restrictions = criteriaBuilder.and(
					restrictions,criteriaBuilder.or(
							criteriaBuilder.like(root.<String>get("uuid"), "%" + keyWord + "%"),
							criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keyWord + "%"),
							criteriaBuilder.like(root.get("tenant").<String>get("telephone"), "%" + keyWord + "%"))
			);
		}

		if(status!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Equipment.Status>get("status"),status));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("tenant")));
		return super.findPage(criteriaQuery, pageable);
	}

	public List<Equipment> findByTenant(Tenant tenant,List<Filter> filters){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Equipment> criteriaQuery = criteriaBuilder.createQuery(Equipment.class);
		Root<Equipment> root = criteriaQuery.from(Equipment.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(tenant!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));;
		}
		criteriaQuery.where(restrictions);
		return  super.findList(criteriaQuery,null,null,filters,null);
	}

	public Equipment findEquipment(Tenant tenant,Equipment.Status status){

		if (tenant==null){
			return null;
		}
		String jpql = "SELECT equipment FROM Equipment equipment where equipment.tenant = :tenant";
		if(status!=null){
			jpql+=" and equipment.status =:status ";
		}
		try {
			Query query =  entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT);
			query.setParameter("tenant", tenant);
			if(status!=null){
				query.setParameter("status", status);
			}
			return  (Equipment)query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Page<Equipment> findPage(Long unionId, String keyWord, List<Tag> tags, Equipment.Status status, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Equipment> criteriaQuery = criteriaBuilder.createQuery(Equipment.class);
		Root<Equipment> root = criteriaQuery.from(Equipment.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(unionId!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("unions"),unionId));
		}
		if (keyWord != null) {
			restrictions = criteriaBuilder.and(
					restrictions,criteriaBuilder.or(
							criteriaBuilder.like(root.<String>get("uuid"), "%" + keyWord + "%"),
							criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keyWord + "%"),
							criteriaBuilder.like(root.get("tenant").<String>get("telephone"), "%" + keyWord + "%"))
			);
		}
		if (tags != null && tags.size() > 0) {
				Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
				Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
				subquery.select(subqueryRoot);
				subquery.where(criteriaBuilder.equal(subqueryRoot, root.get("tenant")), subqueryRoot.join("tags").in(tags));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));

		}
		if(status!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Equipment.Status>get("status"),status));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("tenant")));
		return super.findPage(criteriaQuery, pageable);
	}
}