/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Equipment;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;

import java.util.List;

/**
 * Dao - 设备管理
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface EquipmentDao extends BaseDao<Equipment, Long> {

	/**
	 * 根据设备号查找
	 * @param uuid 
	 * @return 设备，若不存在则返回null
	 */
	Equipment findByUUID(String uuid);

	Page<Equipment> findPage(String keyWord,Equipment.Status status, Pageable pageable);
	/**
	 * 根据条件查询设备
	 * @param unionId
	 * @param keyword
	 * @param tags
	 * @param status
	 * @param pageable
	 * @return
	 */
	Page<Equipment> findPage(Long unionId, String keyword, List<Tag> tags, Equipment.Status status, Pageable pageable);

	List<Equipment> findByTenant(Tenant tenant, List<Filter> filters);

	Equipment findEquipment(Tenant tenant, Equipment.Status status);
}