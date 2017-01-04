/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.wit.dao.SpecificationValueDao;
import net.wit.entity.Specification;
import net.wit.entity.SpecificationValue;

import org.springframework.stereotype.Repository;

/**
 * Dao - 规格值
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("specificationValueDaoImpl")
public class SpecificationValueDaoImpl extends BaseDaoImpl<SpecificationValue, Long> implements SpecificationValueDao {

	public SpecificationValue findByName (Specification specification,String name) {
		if (name == null) {
			return null;
		}
		try {
			String jpql = "select specificationValue from SpecificationValue specificationValue where specificationValue.name = :name and specification=:specification";
			return entityManager.createQuery(jpql, SpecificationValue.class).setFlushMode(FlushModeType.COMMIT).setParameter("name", name).setParameter("specification", specification).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}