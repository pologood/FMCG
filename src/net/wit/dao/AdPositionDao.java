/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.entity.Ad;
import net.wit.entity.AdPosition;
import net.wit.entity.Area;
import net.wit.entity.Tenant;

import java.util.List;
import java.util.Map;

/**
 * Dao - 广告位
 *
 * @author rsico Team
 * @version 3.0
 */
public interface AdPositionDao extends BaseDao<AdPosition, Long> {

    AdPosition find(Long id, Tenant tenant, Area area, Integer count);
    
    Map<String, Object> find(Long id, Long tenantId);
}