/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ApplicationDao;
import net.wit.entity.Application;
import net.wit.entity.Member;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.entity.Application.Type;

import org.springframework.stereotype.Repository;

/**
 * Dao - 应用
 *
 * @author rsico Team
 * @version 3.0
 */
@Repository("applicationDaoImpl")
public class ApplicationDaoImpl extends BaseDaoImpl<Application, Long> implements ApplicationDao {

    public Application findByCode(Member member, String code) {
        if (code == null) {
            return null;
        }
        String jpql = "select applications from Application applications where applications.member=:member and applications.code = :code";
        try {
            return entityManager.createQuery(jpql, Application.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).setParameter("member", member).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Application findApplication(Tenant tenant, String code, Type type) {
        if (code == null) {
            return null;
        }
        String jpql = "select applications from Application applications where applications.tenant=:tenant and applications.code = :code and applications.type=:type";
        try {
            return entityManager.createQuery(jpql, Application.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).setParameter("tenant", tenant).setParameter("type", type).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Application> findByMember(Member member) {
        String jpql = "select applications from Application applications where applications.member=:member";
        try {
            Query query = entityManager.createQuery(jpql, Application.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Application> findList(Tenant tenant, Type type) {
        String jpql = "select applications from Application applications where applications.tenant=:tenant and applications.type=:type";
        try {
            Query query = entityManager.createQuery(jpql, Application.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("type", type);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Page<Application> openPage(String keyword, Pageable pageable, Application.Status statu) {
        //========================================================================

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Application> criteriaQuery = criteriaBuilder.createQuery(Application.class);
        Root<Application> root = criteriaQuery.from(Application.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (keyword != null) {
            restrictions = criteriaBuilder.and(
                    restrictions, criteriaBuilder.or(
                            criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("tenant").get("member").<String>get("username"), "%" + keyword + "%"))
            );
        }
        if (statu != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), statu));
        }
        //restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), ActivityRules.Status.enabled));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}