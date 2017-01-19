package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantFreightTemplateDao;
import net.wit.entity.TenantFreightTemplate;
import net.wit.entity.Tenant;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;

/**
 * Created by Administrator on 2017/1/11.
 */
@Repository("tenantFreightTemplateDaoImpl")
public class TenantFreightTemplateDaoImpl extends BaseDaoImpl<TenantFreightTemplate,Long> implements TenantFreightTemplateDao {
    public Page<TenantFreightTemplate> findPage(Tenant tenant, Pageable pageable){
        if (tenant == null) {
            return new Page<TenantFreightTemplate>(Collections.<TenantFreightTemplate> emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TenantFreightTemplate> criteriaQuery = criteriaBuilder.createQuery(TenantFreightTemplate.class);
        Root<TenantFreightTemplate> root = criteriaQuery.from(TenantFreightTemplate.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("tenant"), tenant));
        return super.findPage(criteriaQuery, pageable);
    }
}
