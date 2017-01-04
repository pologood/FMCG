/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.AdPosition;
import net.wit.entity.Area;
import net.wit.entity.AdPosition.Type;
import net.wit.entity.Tenant;

import java.util.Map;

/**
 * Service - 广告位
 * @author rsico Team
 * @version 3.0
 */
public interface AdPositionService extends BaseService<AdPosition, Long> {

	/**
	 * 查找广告位(缓存)
	 * @param id ID
	 * @param tenant 商铺
	 * @param count 数量
	 * @param cacheRegion 缓存区域
	 * @return 广告位(缓存)
	 */
	AdPosition find(Long id, Tenant tenant, Area area, Integer count, String cacheRegion);

	/**
	 * 查找广告位(缓存)
	 * @param id ID
	 * @param tenant 商铺 缓存区域
	 * @param count 数量
	 * @return 广告位(缓存)
	 */
	AdPosition find(Long id, Tenant tenant,Area area,  Integer count);

	Map<String, Object> find(Long id, Long tenantId);



}