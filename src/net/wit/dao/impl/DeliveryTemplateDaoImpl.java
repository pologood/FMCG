/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.wit.dao.DeliveryTemplateDao;
import net.wit.entity.DeliveryTemplate;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 快递单模板
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("deliveryTemplateDaoImpl")
public class DeliveryTemplateDaoImpl extends BaseDaoImpl<DeliveryTemplate, Long> implements DeliveryTemplateDao {

	public DeliveryTemplate findDefault() {
		try {
			String jpql = "select deliveryTemplate from DeliveryTemplate deliveryTemplate where deliveryTemplate.isDefault = true";
			return entityManager.createQuery(jpql, DeliveryTemplate.class).setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 处理默认并保存
	 * 
	 * @param deliveryTemplate
	 *            快递单模板
	 */
	@Override
	public void persist(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate);
		if (deliveryTemplate.getIsDefault()) {
			String jpql = "update DeliveryTemplate deliveryTemplate set deliveryTemplate.isDefault = false where deliveryTemplate.isDefault = true";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).executeUpdate();
		}
		super.persist(deliveryTemplate);
	}

	/**
	 * 处理默认并更新
	 * 
	 * @param deliveryTemplate
	 *            快递单模板
	 * @return 快递单模板
	 */
	@Override
	public DeliveryTemplate merge(DeliveryTemplate deliveryTemplate) {
		Assert.notNull(deliveryTemplate);
		if (deliveryTemplate.getIsDefault()) {
			String jpql = "update DeliveryTemplate deliveryTemplate set deliveryTemplate.isDefault = false where deliveryTemplate.isDefault = true and deliveryTemplate != :deliveryTemplate";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("deliveryTemplate", deliveryTemplate).executeUpdate();
		}
		return super.merge(deliveryTemplate);
	}

}