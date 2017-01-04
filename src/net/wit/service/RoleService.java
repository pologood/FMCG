/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.List;
import java.util.Set;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Role;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.entity.Role.RoleType;

/**
 * Service - 角色
 *
 * @author rsico Team
 * @version 3.0
 */
public interface RoleService extends BaseService<Role, Long> {

    Page<Role> findPage(RoleType roleType, Pageable pageable);

    Page<Role> findPage(Admin admin, Pageable pageable);

    List<Role> getRoleList(RoleType roleType);

    List<Role> findList(RoleType roleType);

    boolean isRoleName(String name, RoleType roleType, Tenant tenant, Boolean isSystem);

    void initializeRole(Tenant tenant);
}