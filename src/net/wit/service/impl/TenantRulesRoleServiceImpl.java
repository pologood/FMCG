package net.wit.service.impl;

import com.fr.base.Inter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.EmployeeDao;
import net.wit.dao.TenantRulesRoleDao;
import net.wit.entity.*;
import net.wit.service.EmployeeService;
import net.wit.service.TenantRulesRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("tenantRulesRoleServiceImpl")
public class TenantRulesRoleServiceImpl extends BaseServiceImpl<TenantRulesRole, Long> implements TenantRulesRoleService{
	@Resource(name = "tenantRulesRoleDaoImpl")
	private TenantRulesRoleDao tenantRulesRoleDao;


	@Resource(name = "tenantRulesRoleDaoImpl")
	public void setBaseDao(TenantRulesRoleDao tenantRulesRoleDao) {
		super.setBaseDao(tenantRulesRoleDao);
	}

	public Page<TenantRulesRole> openPage(Pageable pageable,String keyWord){
		return tenantRulesRoleDao.openPage(pageable,keyWord);
	}

	@Override
	public List<TenantRulesRole> openList(Inter count, Role role, TenantRules tenantRules) {
		return tenantRulesRoleDao.openList(count,role,tenantRules);
	}

	public List<TenantRulesRole> findByRoleId(Long id){
		return tenantRulesRoleDao.findByRoleId(id,null,null);
	}
	public List<TenantRulesRole> findByRoleId(Long id,String url){
		return tenantRulesRoleDao.findByRoleId(id,url,null);
	}
	public List<TenantRulesRole> findByRoleId(Long id,String url,String type){
		return tenantRulesRoleDao.findByRoleId(id,url,type);
	}
	public List<TenantRulesRole> findByRulesId(Long id){
		return tenantRulesRoleDao.findByRulesId(id);
	}

	public TenantRulesRole find(Role role, TenantRules tenantRules){
		return tenantRulesRoleDao.find(role, tenantRules);
	}
	public List<Long> findRulesIdsByRole(Role role, String type)
	{
		return tenantRulesRoleDao.findRulesIdsByRole(role,type);
	}

}
