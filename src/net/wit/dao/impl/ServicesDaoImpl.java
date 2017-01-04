package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ServicesDao;
import net.wit.entity.Services;
import net.wit.entity.Tenant;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by My-PC on 16/05/31.
 */
@Repository("servicesDaoImpl")
public class ServicesDaoImpl extends BaseDaoImpl<Services,Long> implements ServicesDao{

    @Override
    public Page<Services> findPage(Date beginDate, Date endDate,Services.State state,
                                   Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Services> criteriaQuery = criteriaBuilder.createQuery(Services.class);
        Root<Services> root = criteriaQuery.from(Services.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
        }
        if(state!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("state"), state));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


    public boolean checkServicesType(Tenant tenant, Services.Status status) {
        if (tenant == null) {
            return false;
        }
        String jpql = "select count(*) from Services service where service.tenant=:tenant and service.status=:status";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("status", status).getSingleResult();
        return count > 0;
    }

    public Services findServicesByTenant(Tenant tenant, Services.Status status) {
        if (tenant == null) {
            return null;
        }
        String jpql = "select service from Services service where service.tenant=:tenant and service.status=:status";
        try {
            return entityManager.createQuery(jpql, Services.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("status", status).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Map<String,String> getType(Tenant tenant, Services.Status status) {
        String jpql = "select service.type from Services service where service.tenant=:tenant and service.status=:status";
        List<?> data = entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("status", status).getResultList();

        Map<String, String> map = new HashMap<String, String>();
        if(data.size()>0){
            for(Object obj : data){
                map.put("type",obj.toString());
            }
        }else {
            map.put("type","none");
        }

        return map;
    }
}
