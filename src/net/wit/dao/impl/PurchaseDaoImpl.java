package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PurchaseDao;
import net.wit.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by My-PC on 16/06/02.
 */
@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl extends BaseDaoImpl<Purchase, Long> implements PurchaseDao {

    /**
     * @param pageable      根据关键字或者拼音码查询
     * @param tenant        采购商
     * @param supplier      供应商
     * @param beginDate     开始时间
     * @param endDate       结束时间
     * @param supplierName  供应商名称
     * @param purchaseSet   采购单集合
     * @param keyword       关键字
     * @return              page
     */
    public Page<Purchase> openPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName, Set<Purchase> purchaseSet, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Purchase> criteriaQuery = criteriaBuilder.createQuery(Purchase.class);
        Root<Purchase> root = criteriaQuery.from(Purchase.class);
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
        if (purchaseSet != null) {
            if(purchaseSet.size()==0){
                return new Page<>(Collections.<Purchase>emptyList(), 0, pageable);
            }
            restrictions = criteriaBuilder.and(restrictions, root.in(purchaseSet));
        }

        if (StringUtils.isNotBlank(keyword)) { // 关键字
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"),
                    		criteriaBuilder.like(root.get("supplier").<String>get("name"), "%" + keyword + "%")
                    		)
                    );
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Purchase> exportOpenPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName, Set<Purchase> purchaseSet, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Purchase> criteriaQuery = criteriaBuilder.createQuery(Purchase.class);
        Root<Purchase> root = criteriaQuery.from(Purchase.class);
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
//        if (purchaseSet != null) {
//            if(purchaseSet.size()==0){
//                return new Page<>(Collections.<Purchase>emptyList(), 0, pageable);
//            }
//            restrictions = criteriaBuilder.and(restrictions, root.in(purchaseSet));
//        }

        if (StringUtils.isNotBlank(keyword)) { // 关键字
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"),
                    		criteriaBuilder.like(root.get("supplier").<String>get("name"), "%" + keyword + "%")
                    		)
                    );
        }
        criteriaQuery.where(restrictions);
        TypedQuery<Purchase> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }
}
