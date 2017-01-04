/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantRelationDao;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.entity.TenantRelation.Status;
import net.wit.service.TenantRelationService;

import org.springframework.stereotype.Service;

/**
 * Service - 加盟商
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("tenantRelationServiceImpl")
public class TenantRelationServiceImpl extends BaseServiceImpl<TenantRelation, Long> implements TenantRelationService {

	@Resource(name = "tenantRelationDaoImpl")
	private TenantRelationDao tenantRelationDao;
	
	@Resource(name = "tenantRelationDaoImpl")
	public void setBaseDao(TenantRelationDao tenantRelationDao) {
		super.setBaseDao(tenantRelationDao);
	}

	public Page<TenantRelation> findPage(Tenant tenant,Status status,Pageable pageable) {
		return tenantRelationDao.findPage(tenant, status, pageable);
	}

	public Page<TenantRelation> findRelation(Tenant parent,Tenant tenant,Pageable pageable) {
		return tenantRelationDao.findRelation(parent, tenant, pageable);
	}
	public Page<TenantRelation> findParent(Tenant tenant,Status status,Pageable pageable) {
		return tenantRelationDao.findParent(tenant,status,pageable);
	}
	public Page<TenantRelation> findMyParent(Tenant tenant,Status status,Pageable pageable) {
		return tenantRelationDao.findMyParent(tenant,status,pageable);
	}
	public Boolean relationExists(Tenant tenant,Tenant parent) {
		return tenantRelationDao.relationExists(tenant, parent);
	}

	@Override
	public List<Long> findRelations(Tenant tenant, Tenant parent) {
		return tenantRelationDao.findRelations(tenant, parent);
	}

	public Boolean relationExists(Tenant tenant, Status status) {
		return tenantRelationDao.relationExists(tenant, status);
	}
}