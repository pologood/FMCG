/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

/**
 * Service - 广告
 * @author rsico Team
 * @version 3.0
 */
public interface AdService extends BaseService<Ad, Long> {
	/**
	 * 查找广告分页
	 * @param tenant 广告分类
	 * @param pageable 分页信息
	 */
	Page<Ad> findMyPage(Tenant tenant, Pageable pageable);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param adPosition
	 * @param pageable
	 * @return Object
	 */
	Page<Ad> findPage(Tenant tenant, AdPosition adPosition, Pageable pageable);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param adPosition
	 * @param pageable
	 * @return Object
	 */
	Page<Ad> findPage(AdPosition adPosition, Pageable pageable);
	/**
	 * @Title：findPage
	 * @Description：
	 * @param adPosition
	 * @param pageable
	 * @return Object
	 */
	Page<Ad> findPage(AdPosition adPosition,Area area,Long linkId, Pageable pageable);


	Page<Ad> openPage(Tenant tenant, AdPosition adPosition, Area area, ProductChannel productChannel, Pageable pageable);
}