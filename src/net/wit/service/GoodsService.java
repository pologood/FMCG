/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.entity.Goods;

/**
 * Service - 货品
 * @author rsico Team
 * @version 3.0
 */
public interface GoodsService extends BaseService<Goods, Long> {

	public void save(Goods goods);

	public Goods update(Goods goods);
}