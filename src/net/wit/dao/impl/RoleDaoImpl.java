/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.RoleDao;
import net.wit.entity.Member;
import net.wit.entity.Role;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Role.RoleType;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;

import org.springframework.stereotype.Repository;

/**
 * Dao - 角色
 *
 * @author rsico Team
 * @version 3.0
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {
    public Page<Role> findPage(RoleType roleType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (roleType != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("roleType"), roleType));
        }


        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Role> findList(RoleType roleType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("roleType"), roleType));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    public List<Role> getRoleList(RoleType roleType) {
        try {
            String jpql = "select role from Role role where role.roleType > :roleType and role.roleType <> 6 order by role.roleType asc";
            List<Role> roles = entityManager.createQuery(jpql, Role.class).setParameter("roleType", roleType).getResultList();
            return roles;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public boolean isRoleName(String name, RoleType roleType, Tenant tenant, Boolean isSystem) {
        try {
            String jpql = "select count(role) from Role role where 1=1";

            if(name!=null){
                jpql +=" and role.name=:name ";
            }

            if(roleType!=null){
                jpql +=" and role.roleType=:roleType ";
            }

            if(tenant!=null){
                jpql +=" and role.tenant=:tenant ";
            }

            if(isSystem==null){
                isSystem=false;
            }
            jpql +=" and role.isSystem=:isSystem ";
            Long count =  entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("name", name).setParameter("roleType", roleType).setParameter("tenant", tenant).setParameter("isSystem", isSystem).getSingleResult();

            return count>0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}