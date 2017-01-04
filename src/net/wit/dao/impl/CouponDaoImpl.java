/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.dao.CouponDao;
import net.wit.dao.DeliveryCenterDao;
import net.wit.entity.*;
import net.wit.entity.Coupon.Status;
import net.wit.entity.model.CouponSumerModel;

import net.wit.support.CouponComparatorByDistance;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 优惠券
 *
 * @author rsico Team
 * @version 3.0
 */
@Repository("couponDaoImpl")
public class CouponDaoImpl extends BaseDaoImpl<Coupon, Long> implements CouponDao {
    @Resource(name = "deliveryCenterDaoImpl")
    private DeliveryCenterDao deliveryCenterDao;

    /**
     * 设置值并代金券
     *
     * @param coupon 代金券
     */
    @Override
    public void persist(Coupon coupon) {
        Assert.notNull(coupon);
        setValue(coupon);
        super.persist(coupon);
    }

    /**
     * 设置值并更新
     *
     * @param coupon 代金券
     * @return 代金券
     */
    @Override
    public Coupon merge(Coupon coupon) {
        Assert.notNull(coupon);
        setValue(coupon);
        return super.merge(coupon);
    }

    /**
     * 设置值
     *
     * @param coupon 代金券
     */
    private void setValue(Coupon coupon) {
        if (coupon == null) {
            return;
        }
//        if (coupon.getEndDate().compareTo(new Date()) < 0) {
//            coupon.setStatus(Status.Expired);
//        } else {
//            if (coupon.getStartDate().compareTo(new Date()) > 0) {
//                coupon.setStatus(Status.unBegin);
//            } else {
//                if (coupon.getSendCount().compareTo(coupon.getCount()) >= 0) {
//                    coupon.setStatus(Status.unUsed);
//                } else {
//                    coupon.setStatus(Status.canUse);
//                }
//            }
//
//        }
    }

    public void refreshStatus(Tenant tenant) {
        List<Filter> filters = new ArrayList<Filter>();
        if (tenant != null) {
            filters.add(new Filter("tenant", Operator.eq, tenant));
        }
        List<Coupon.Type> couponList=new ArrayList<>();
        couponList.add(Coupon.Type.tenantCoupon);
        couponList.add(Coupon.Type.tenantBonus);
        filters.add(new Filter("type", Operator.in, couponList));
        List<Coupon> list = super.findList(null, null, filters, null);
        for (Coupon coupon : list) {
            this.setValue(coupon);
            this.merge(coupon);
        }
    }

    public Page<Coupon> findPage(Area area,Community community,TenantCategory tenantCategory, Boolean isExpired, Location location, String orderType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coupon> criteriaQuery = criteriaBuilder.createQuery(Coupon.class);
        Root<Coupon> root = criteriaQuery.from(Coupon.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions,
                criteriaBuilder.equal(root.get("status"), Status.confirmed),
                criteriaBuilder.equal(root.get("type"), Coupon.Type.tenantBonus));
        if (area != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("area"), area), criteriaBuilder.like(root.get("tenant").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
        }
        if (tenantCategory != null) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory));
        }
        if (isExpired != null) {
            if (isExpired) {
                restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date> get("endDate"), new Date()));
            } else {
                restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("endDate"), new Date()));
            }
        }
        if (community != null) {
            List<DeliveryCenter> dlvs = deliveryCenterDao.findList(area, community);
            List<Tenant> tenants=new ArrayList<>();
            for (DeliveryCenter dc : dlvs) {
                tenants.add(dc.getTenant());
            }
            if (tenants.size() > 0) {
                restrictions = criteriaBuilder.and(restrictions, root.get("tenant").in(tenants));
            } else {
                return new Page<>(new ArrayList<Coupon>(), 0, pageable);
            }
        }
        criteriaQuery.where(restrictions);
        if (orderType != null) {
            if (orderType.equals("distance")) {
                List<Coupon> coupons = super.findList(criteriaQuery, 0, null, null, null);
                CouponComparatorByDistance couponComparatorByDistance = new CouponComparatorByDistance();
                couponComparatorByDistance.setLocation(location);
                try {
                    Collections.sort(coupons, couponComparatorByDistance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int fromIndex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
                int endIndex = fromIndex + pageable.getPageSize();
                if (endIndex > coupons.size()) {
                    endIndex = coupons.size();
                }
                if (endIndex <= fromIndex) {
                    return new Page<>(new ArrayList<Coupon>(), 0, pageable);
                }
                return new Page<>(new ArrayList<>(coupons.subList(fromIndex, endIndex)), coupons.size(), pageable);
            } else if (orderType.equals("amountSize")) {
                List<Order> orders = pageable.getOrders();
                orders.clear();
                orders.add(Order.desc("amount"));
                return super.findPage(criteriaQuery, pageable);
            }
        }
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Coupon> findPage(String status,Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coupon> criteriaQuery = criteriaBuilder.createQuery(Coupon.class);
        Root<Coupon> root = criteriaQuery.from(Coupon.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if(status.equals("cancelled")){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), Status.cancelled));
        }else {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), Status.confirmed));
        }
        if (status.equals("unBegin")) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan(root.<Date>get("startDate"), new Date()));
        }
        if (status.equals("canUse")) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan(root.<Integer>get("sendCount"), root.<Integer>get("count")));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan(root.<Date>get("endDate"), new Date()));
        }
        if (status.equals("unUsed")) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Integer>get("sendCount"), root.<Integer>get("count")));
        }
        if (status.equals("Expired")) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan(root.<Date>get("endDate"), new Date()));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
    public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coupon> criteriaQuery = criteriaBuilder.createQuery(Coupon.class);
        Root<Coupon> root = criteriaQuery.from(Coupon.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (isEnabled != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
        }
        if (isExchange != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isExchange"), isExchange));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Coupon findSystemPointExchange() {
        return find(1L);
    }

    /**
     * 优惠券统计
     */
    public Page<CouponSumerModel> sumer(Coupon coupon, CouponSumerModel.Type type, Pageable pageable) {
        String jpql = "";
        if (type.equals(CouponSumerModel.Type.send)) {
            jpql =
                    "select code.create_date as sumerDate,count(code.id) as sumerCount,count(distinct code.member) as sumerNumber from xx_coupon_code code where code.coupon = :coupon group by code.create_date";
        } else{
            jpql =
                    "select code.used_date as sumerDate,count(code.id) as sumerCount,count(distinct code.member) as sumerNumber from xx_coupon_code code where code.coupon = :coupon and used_date is not null group by code.used_date";
        }
        Query query = entityManager.createNativeQuery(jpql);
        query.setFlushMode(FlushModeType.COMMIT).setParameter("coupon", coupon);
        //TypedQuery<CouponSumerModel> query = entityManager.createQuery(jpql, CouponSumerModel.class).setFlushMode(FlushModeType.COMMIT).setParameter("coupon", coupon);
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        try {
            List data = query.getResultList();
            List<CouponSumerModel> models = new ArrayList<CouponSumerModel>();
            for (Object obj : data) {
                Object[] row = (Object[]) obj;
                CouponSumerModel model = new CouponSumerModel();
                model.setSumerDate((Date) row[0]);
                model.setSumerCount(Integer.parseInt(row[1].toString()));
                model.setSumerNumber(Integer.parseInt(row[2].toString()));
                models.add(model);
            }
            return new Page<CouponSumerModel>(models,count(coupon,type).getSumerCount(), pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<CouponSumerModel>(null, 0, pageable);
        }
    }

    /**
     * 优惠券统计
     */
    public CouponSumerModel count(Coupon coupon, CouponSumerModel.Type type) {
        String jpql = "";
        if (type.equals(CouponSumerModel.Type.send)) {
            jpql =
                    "select count(code.id) as sumerCount,count(distinct code.member) as sumerNumber from xx_coupon_code code where code.coupon = :coupon";
        } else {
            jpql =
                    "select count(code.id) as sumerCount,count(distinct code.member) as sumerNumber from xx_coupon_code code where code.coupon = :coupon and used_date is not null";
        }
        Query query = entityManager.createNativeQuery(jpql);
        query.setFlushMode(FlushModeType.COMMIT).setParameter("coupon", coupon);
        try {
            List data = query.getResultList();
            CouponSumerModel model = new CouponSumerModel();
            for (Object obj : data) {
                Object[] row = (Object[]) obj;
                model.setSumerCount(Integer.parseInt(row[0].toString()));
                model.setSumerNumber(Integer.parseInt(row[1].toString()));
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Coupon> findEnabledCouponList(Tenant tenant){
        Date now = new Date();
        String jpql = "select coupon from Coupon coupon where coupon.tenant = :tenant and :now between coupon.startDate and coupon.endDate";
        return entityManager.createQuery(jpql, Coupon.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("now", now).getResultList();
    }
}