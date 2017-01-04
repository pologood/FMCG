/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ReviewDao;
import net.wit.dao.RoleDao;
import net.wit.dao.TenantRulesDao;
import net.wit.dao.TenantRulesRoleDao;
import net.wit.entity.*;
import net.wit.entity.Role.RoleType;
import net.wit.service.RoleService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 角色
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    @Resource(name = "tenantRulesDaoImpl")
    private TenantRulesDao tenantRulesDao;

    @Resource(name = "tenantRulesRoleDaoImpl")
    private TenantRulesRoleDao tenantRulesRoleDao;

    @Resource(name = "roleDaoImpl")
    private RoleDao roleDao;

    @Resource(name = "roleDaoImpl")
    public void setBaseDao(RoleDao roleDao) {
        super.setBaseDao(roleDao);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public void save(Role role) {
        super.save(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public Role update(Role role) {
        return super.update(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = "authorization", allEntries = true)
    public Role update(Role role, String... ignoreProperties) {
        return super.update(role, ignoreProperties);
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
    public void delete(Role role) {
        super.delete(role);
    }


    public Page<Role> findPage(RoleType roleType, Pageable pageable) {
        return roleDao.findPage(roleType, pageable);
    }

    public Page<Role> findPage(Admin admin, Pageable pageable) {
        if (admin.getUsername().equals("admin")) {
            return roleDao.findPage(pageable);
        }
        List<Role> roleList = new ArrayList<Role>();
        for (Role role : admin.getRoles()) {
            roleList.addAll(roleDao.findList(role.getRoleType()));
        }
        return new Page<Role>(roleList, roleList.size(), pageable);
    }

    public List<Role> findList(RoleType roleType) {
        return roleDao.findList(roleType);
    }

    public List<Role> getRoleList(RoleType roleType) {
        return roleDao.getRoleList(roleType);
    }

    public boolean isRoleName(String name, RoleType roleType, Tenant tenant, Boolean isSystem) {
        return roleDao.isRoleName(name, roleType, tenant, isSystem);
    }

    @Transactional
    public void initializeRole(Tenant tenant){
        String[] roleNames = {"店主","店长","导购","财务","收银"};
        Long[] ids={1L,2L,19L,20L,21L,22L,23L,31L,32L,33L,34L,35L,36L,37L,38L};

        try {
            for(String roleName:roleNames){
                boolean _isRoleName=isRoleName(roleName,RoleType.helper,tenant,true);

                if(_isRoleName){
                    continue;
                }

                Role role = new Role();
                role.setName(roleName);
                role.setIsSystem(true);
                role.setTenant(tenant);
                role.setRoleType(Role.RoleType.helper);
                save(role);
                //遍历查看权限列表
                for (Long rulesId : ids) {
                    TenantRulesRole tenantRulesRole = new TenantRulesRole();
                    tenantRulesRole.setRole(role);
                    TenantRules tenantRules = tenantRulesDao.find(rulesId);
                    tenantRulesRole.setRules(tenantRules);
                    //查看是否拥有编辑权限
                    tenantRulesRole.setReadAuthority(true);
                    tenantRulesRole.setUpdateAuthority(true);
                    tenantRulesRole.setAddAuthority(true);
                    tenantRulesRole.setDelAuthority(true);;
                    tenantRulesRole.setExpAuthority(false);

                    tenantRulesRole.setRefillAuthority(false);
                    tenantRulesRole.setCashAuthority(false);
                    tenantRulesRole.setConfirmAuthority(false);
                    tenantRulesRole.setDismissalAuthority(false);

                    tenantRulesRole.setModifyPriceAuthority(false);
                    tenantRulesRole.setUpMarketAuthority(false);
                    tenantRulesRole.setDownMarketAuthority(false);
                    tenantRulesRole.setPrintAuthority(false);

                    tenantRulesRole.setPaymentAuthority(false);
                    tenantRulesRole.setAppliedAuthority(false);
                    tenantRulesRole.setStatisticsAuthority(false);

                    tenantRulesRole.setShareAuthority(false);
                    tenantRulesRole.setSuperviseAuthority(false);

                    tenantRulesRole.setRefuseReturnAuthority(false);
                    tenantRulesRole.setAgreeReturnAuthority(false);
                    tenantRulesRole.setSendGoodsAuthority(false);
                    tenantRulesRole.setCancelOrderAuthority(false);

                    tenantRulesRole.setCloseAuthority(false);
                    tenantRulesRole.setOpenAuthority(false);

                    tenantRulesRoleDao.merge(tenantRulesRole);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}