/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;

import net.wit.dao.CartDao;
import net.wit.entity.Cart;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

/**
 * Dao - 购物车
 * 
 * @author rsico Team
 * @version 3.0
 */

@Repository("cartDaoImpl")
public class CartDaoImpl extends BaseDaoImpl<Cart, Long> implements CartDao {

	public List<Cart> findExpired() {
		String jpql = "select cart from Cart cart where cart.modifyDate <= :expire";
		return entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("expire", DateUtils.addSeconds(new Date(), -900)).getResultList();
	}
	
	public void evictExpired() {
		String jpql1 = "delete from Cart cart where cart.modifyDate <= :expire";
		entityManager.createQuery(jpql1).setFlushMode(FlushModeType.COMMIT).setParameter("expire", DateUtils.addSeconds(new Date(), -Cart.TIMEOUT)).executeUpdate();
	}

	public void updateKey(Cart cart,String token_key) {
		String sql = "update xx_cart t set t.cart_key =:token_key where t.id = :id";
		entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("token_key", token_key).setParameter("id", cart.getId()).executeUpdate();
	}
}