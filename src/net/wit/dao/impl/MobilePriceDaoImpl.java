/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import net.wit.dao.MobilePriceDao;
import net.wit.entity.MobilePrice;

import org.springframework.stereotype.Repository;

/**
 * Dao - 手机快充价格
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("mobilePriceDaoImpl")
public class MobilePriceDaoImpl extends BaseDaoImpl<MobilePrice, Long> implements MobilePriceDao {

    public MobilePrice findByProdId(String prodId) {
       try {
		String jpql = "select mobilePrice from MobilePrice mobilePrice where mobilePrice.prodId=:prodId";
		TypedQuery<MobilePrice> query = entityManager.createQuery(jpql, MobilePrice.class).setFlushMode(FlushModeType.COMMIT).setParameter("prodId", prodId);
		return query.getSingleResult();
       } catch (NoResultException e) {
		return null;
	   }
	}
    public List<MobilePrice> findBySegment(String provincename,String isptype) {
        try {
 		String jpql = "select mobilePrice from MobilePrice mobilePrice where mobilePrice.ispType=:isptype and mobilePrice.province=:provincename order by mobilePrice.denomination";
 		TypedQuery<MobilePrice> query = entityManager.createQuery(jpql, MobilePrice.class).setFlushMode(FlushModeType.COMMIT).setParameter("isptype", isptype).setParameter("provincename", provincename);
 		return query.getResultList();
        } catch (NoResultException e) {
 		return null;
 	   }
 	}
}