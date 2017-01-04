/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.entity.Cart;

/**
 * Dao - 购物车
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface CartDao extends BaseDao<Cart, Long> {

	/**
	 * 获取锁定购物车
	 */
	List<Cart> findExpired();
	/**
	 * 清除过期购物车
	 */
	void evictExpired();

	/**
	 * 更新购物车key
	 */
	void updateKey(Cart cart,String token_key);
}