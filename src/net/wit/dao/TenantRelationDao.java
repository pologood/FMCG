/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.entity.TenantRelation.Status;

/**
 * Dao - 角色
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface TenantRelationDao extends BaseDao<TenantRelation, Long> {

	/** 查询我的客户 */
	public Page<TenantRelation> findPage(Tenant tenant,Status status,Pageable pageable);

	public Page<TenantRelation> findRelation(Tenant parent,Tenant tenant,Pageable pageable);
	
	/** 查询我的上级 */
	public Page<TenantRelation> findParent(Tenant tenant,Status status,Pageable pageable);
	public Page<TenantRelation> findMyParent(Tenant tenant,Status status,Pageable pageable);

	public Boolean relationExists(Tenant tenant,Tenant parent);
	
	public List<Long> findRelations(Tenant tenant,Tenant parent);

	Boolean relationExists(Tenant tenant,Status status);

	List<TenantRelation> findMyParent(Tenant tenant,Status status);
}