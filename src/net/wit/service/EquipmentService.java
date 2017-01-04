/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Equipment;
import net.wit.entity.Tenant;

import java.util.List;

/**
 * Service - 设备管理
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface EquipmentService extends BaseService<Equipment, Long> {

	/**
	 * 根据设备号查找
	 * @param uuid 
	 * @return 设备，若不存在则返回null
	 */
	public Equipment findByUUID(String uuid);

	Page<Equipment> findPage(String keyWord, Equipment.Status status, Pageable pageable);


	List<Equipment> findByTenant(Tenant tenant, List<Filter> filters);

	Equipment findEquipment(Tenant tenant, List<Filter> filters);
}