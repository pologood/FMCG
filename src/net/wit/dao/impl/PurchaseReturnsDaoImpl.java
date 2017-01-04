package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PurchaseReturnsDao;
import net.wit.entity.Purchase;
import net.wit.entity.PurchaseReturns;
import net.wit.entity.Tenant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 采购退货单
 * Created by My-PC on 16/06/02.
 */
@Repository("purchaseReturnsDaoImpl")
public class PurchaseReturnsDaoImpl extends BaseDaoImpl<PurchaseReturns, Long> implements PurchaseReturnsDao {

    public Page<PurchaseReturns> openPage(
            Pageable pageable,                           //根据关键字或者拼音码查询
            Tenant tenant,                                 //采购商
            Tenant supplier,                               //供应商
            Date beginDate,                              //开始时间
            Date endDate,                                //结束时间
            String supplierName,
            Set<PurchaseReturns> purchaseReturnsSet,
            String keyword
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PurchaseReturns> criteriaQuery = criteriaBuilder.createQuery(PurchaseReturns.class);
        Root<PurchaseReturns> root = criteriaQuery.from(PurchaseReturns.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenant.getId()));
        }
        if (supplier != null) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("supplier").in(supplier.getId()));
        }

        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }

        if (StringUtils.isNotBlank(supplierName)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("supplier").<String>get("name"), "%" + supplierName + "%"));
        }
        if (purchaseReturnsSet != null) {
            if (purchaseReturnsSet.size() == 0) {
                return new Page<>(Collections.<PurchaseReturns>emptyList(), 0, pageable);
            }
            restrictions = criteriaBuilder.and(restrictions, root.in(purchaseReturnsSet));
        }

        if (StringUtils.isNotBlank(keyword)) { // 关键字
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%")));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
    public List<PurchaseReturns> exportOpenPage(
            Tenant tenant,                                 //采购商
            Tenant supplier,                               //供应商
            Date beginDate,                              //开始时间
            Date endDate,   
            String keyword
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PurchaseReturns> criteriaQuery = criteriaBuilder.createQuery(PurchaseReturns.class);
        Root<PurchaseReturns> root = criteriaQuery.from(PurchaseReturns.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenant.getId()));
        }
        if (supplier != null) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("supplier").in(supplier.getId()));
        }

        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }

        if (StringUtils.isNotBlank(keyword)) { // 关键字
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%")));
        }
        criteriaQuery.where(restrictions);
        TypedQuery<PurchaseReturns> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }
}
