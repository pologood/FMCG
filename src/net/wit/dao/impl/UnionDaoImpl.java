package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.UnionDao;
import net.wit.entity.Tenant;
import net.wit.entity.Union;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

/**
 * 货郎联盟
 * @author Administrator
 *
 */
@Repository("unionDaoImpl")
public class UnionDaoImpl extends BaseDaoImpl<Union,Long> implements UnionDao{

    public Page<Union> findPage(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Union> criteriaQuery = criteriaBuilder.createQuery(Union.class);
        Root<Union> root = criteriaQuery.from(Union.class);
        criteriaQuery.select(root);
        return super.findPage(criteriaQuery, pageable);

    }
    public Page<Union> findPage(String keyword, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Union> criteriaQuery = criteriaBuilder.createQuery(Union.class);
        Root<Union> root = criteriaQuery.from(Union.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(StringUtils.isNotBlank(keyword)){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%")));

        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);

    }
    public Page<Tenant> findTenant(Union union, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("unions"), union));
        return null;

    }
}
