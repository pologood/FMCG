/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;
import net.wit.dao.AdminDao;
import net.wit.dao.TenantDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Enterprise;
import net.wit.entity.Role;
import net.wit.entity.Tenant;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.service.AdminService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 管理员
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("adminServiceImpl")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;
	
	@Resource(name = "tenantDaoImpl")
	private TenantDao tenantDao;
	
	@Resource(name = "adminDaoImpl")
	public void setBaseDao(AdminDao adminDao) {
		super.setBaseDao(adminDao);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return adminDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		return adminDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<String> findAuthorities(Long id) {
		List<String> authorities = new ArrayList<String>();
		Admin admin = adminDao.find(id);
		if (admin != null) {
			for (Role role : admin.getRoles()) {
				authorities.addAll(role.getAuthorities());
			}
		}
		return authorities;
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			return subject.isAuthenticated();
		}
		return false;
	}

	@Transactional(readOnly = true)
	public Admin getCurrent() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return adminDao.find(principal.getId());
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return principal.getUsername();
			}
		}
		return null;
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void save(Admin admin) {
		super.save(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin) {
		return super.update(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin update(Admin admin, String... ignoreProperties) {
		return super.update(admin, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Admin admin) {
		super.delete(admin);
	}

	@Override
	public List<Tenant> findTenantsByAdmin(Admin admin) {
//		Pageable pageable=new Pageable(1,100);
//		List<Filter> filters = pageable.getFilters();
		Enterprise enterprise=admin.getEnterprise();
		
		if(enterprise!=null){
			EnterpriseType enterprisetype=enterprise.getEnterprisetype();
			
			Area area=enterprise.getArea();
			List<Area> areaList=new ArrayList<Area>();
			areaList.add(area);
			List<Area> list=findAllChildren(area, areaList);
			/*分类查找*/
			if(enterprisetype==EnterpriseType.proxy){
				return tenantDao.findListByAreas(null);
			}else if(enterprisetype==EnterpriseType.provinceproxy){
				return tenantDao.findListByAreas(list);
			}else if(enterprisetype==EnterpriseType.cityproxy){
				return tenantDao.findListByAreas(list);
			}else if(enterprisetype==EnterpriseType.countyproxy){
				return tenantDao.findListByAreas(list);
			}else if(enterprisetype==EnterpriseType.personproxy){
				List<Tenant> tenants=new ArrayList<Tenant>(enterprise.getTenants());
				return tenants;
			}else{
				return null;
			}
		}else{
			if(admin.getUsername().equals("admin")){
				return tenantDao.findListByAreas(null);
			}else{
				return null;
			}
		}
		
	}
	
	/**区域代理查找下属所有区域*/
	private List<Area> findAllChildren(Area area,List<Area> areaList){
		if(area!=null){
			List<Area> children=new ArrayList<Area>(area.getChildren());;
			if(children!=null&&children.size()>0){
				for (Area area2 : children) {
					areaList.add(area2);
					findAllChildren(area2,areaList);
				}
			}
		}
		return areaList;
	}

	public Page<Admin> findPage(List<EnterpriseType> enterpriseTypes,List<Area> areas,Pageable pageable){
		return adminDao.findPage(enterpriseTypes,areas, pageable);
	}

	public Map<String,BigDecimal> getAmount(){
		return adminDao.getAmount();
	}

	public Map<String ,Object> findPlatformCapital(Date beginDate, Date endDate){
		return adminDao.findPlatformCapital(beginDate,endDate);
	}
}