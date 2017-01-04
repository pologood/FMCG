/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Role;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.entity.Role.RoleType;

/**
 * Dao - 角色
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface RoleDao extends BaseDao<Role, Long> {

	Page<Role> findPage(RoleType roleType, Pageable pageable);
	
	List<Role> findList(RoleType roleType);
	
	List<Role> getRoleList(RoleType roleType);

	boolean isRoleName(String name, RoleType roleType, Tenant tenant, Boolean isSystem);
}