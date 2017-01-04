package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.UnionDao;
import net.wit.dao.VisitRecordDao;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.entity.VisitRecord;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 货郎联盟
 * @author Administrator
 *
 */
@Repository("visitRecordDaoImpl")
public class VisitRecordDaoImpl extends BaseDaoImpl<VisitRecord,Long> implements VisitRecordDao {
    @Override
    public Page<VisitRecord> findByVisitRecordPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable) {
        if (tenant == null) {
            return new Page<VisitRecord>(Collections.<VisitRecord>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VisitRecord> criteriaQuery = criteriaBuilder.createQuery(VisitRecord.class);
        Root<VisitRecord> root = criteriaQuery.from(VisitRecord.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("product")));
        if(beginDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),beginDate));
        }
        if(endDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),endDate));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public List<VisitRecord> findByVisitRecordList(Tenant tenant, Date beginDate, Date endDate) {
        if (tenant == null) {
            return new ArrayList<VisitRecord>(Collections.<VisitRecord>emptyList());
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VisitRecord> criteriaQuery = criteriaBuilder.createQuery(VisitRecord.class);
        Root<VisitRecord> root = criteriaQuery.from(VisitRecord.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("product")));
        if(beginDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),beginDate));
        }
        if(endDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),endDate));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }
}
