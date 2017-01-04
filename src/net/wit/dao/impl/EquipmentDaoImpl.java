/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tenant;
import org.springframework.stereotype.Repository;

import net.wit.dao.EquipmentDao;
import net.wit.entity.Equipment;
import net.wit.entity.Product;

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

	public Equipment findEquipment(Tenant tenant,List<Filter> filters){
		String jpql = "SELECT equipment FROM Equipment equipment where equipment.tenant = :tenant";
		if (tenant==null){
			return null;
		}
		try {
			return entityManager.createQuery(jpql, Equipment.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
}