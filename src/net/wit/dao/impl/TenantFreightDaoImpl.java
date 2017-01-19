package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantFreightDao;
import net.wit.entity.Area;
import net.wit.entity.TenantFreight;
import net.wit.entity.Tenant;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by Administrator on 2017/1/11.
 */
@Repository("tenantFreightDaoImpl")
public class TenantFreightDaoImpl extends BaseDaoImpl<TenantFreight,Long> implements TenantFreightDao {
    public Page<TenantFreight> findByFreightsTemplate(Long id, TenantFreight.Type type, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TenantFreight> criteriaQuery = criteriaBuilder.createQuery(TenantFreight.class);
        Root<TenantFreight> root = criteriaQuery.from(TenantFreight.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.<Long>get("tenantFreightTemplate"), id),criteriaBuilder.equal(root.get("freightType"), type));
        return super.findPage(criteriaQuery, pageable);
    }

    public TenantFreight findByArea(Tenant tenant, Area area){
        if (tenant == null) {
            return null;
        }
        try {
            String jpql = "select tenantFreight from TenantFreight tenantFreight where tenantFreight.tenant = :tenant and tenantFreight.area = :area";
            return entityManager.createQuery(jpql, TenantFreight.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("area", area).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
