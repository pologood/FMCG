package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Payment;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PayBillDao;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Tenant;

/**
 * Created by Administrator on 2016/9/6.
 */
@Repository("payBillDaoImpl")
public class PayBillDaoImpl extends BaseDaoImpl<PayBill, Long> implements PayBillDao {

    /**
     * 优惠买单统计
     */
    public Page<PayBill> findMyPage(Tenant tenant, Date startDate, Date endDate, String keywords, PayBill.Status status, PayBill.Type type, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PayBill> criteriaQuery = criteriaBuilder.createQuery(PayBill.class);
        Root<PayBill> root = criteriaQuery.from(PayBill.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
        }

        if (type != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }

        if (StringUtils.isNotBlank(keywords)) { // 拼音条件
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.<String>get("sn"), keywords)
                            , criteriaBuilder.equal(root.get("member").<String>get("username"), keywords)
                            , criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keywords + "%")
                            , criteriaBuilder.like(root.get("deliveryCenter").<String>get("name"), "%" + keywords + "%")
                    )
            );
        }

        if (startDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), startDate));
        }

        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    /**
     * 优惠买单统计（导出）
     */
    public List<PayBill> findMyList(Tenant tenant, Date startDate, Date endDate, String keywords, PayBill.Status status, PayBill.Type type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PayBill> criteriaQuery = criteriaBuilder.createQuery(PayBill.class);
        Root<PayBill> root = criteriaQuery.from(PayBill.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
        }

        if (type != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }

        if (StringUtils.isNotBlank(keywords)) { // 拼音条件
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.<String>get("sn"), keywords)
                            , criteriaBuilder.equal(root.get("member").<String>get("username"), keywords)
                            , criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keywords + "%")
                            , criteriaBuilder.like(root.get("deliveryCenter").<String>get("name"), "%" + keywords + "%")
                    )
            );
        }

        if (startDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), startDate));
        }

        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        }
        criteriaQuery.where(restrictions);
        TypedQuery<PayBill> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }


    public boolean isLimit(Member member, Integer count) {
        if (member == null) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date start = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        Date end = calendar.getTime();
        String jpql = "select count(paybill) from PayBill paybill where paybill.member = :member and paybill.createDate>:startDate  and paybill.createDate<:endDate and paybill.status=:status";
        Long _count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("startDate", start).setParameter("endDate", end).setParameter("status", PayBill.Status.success).getSingleResult();
        return _count < count;
    }

    @Override
    public BigDecimal findPayBillSum(Tenant tenant, PayBill.Type type , PayBill.Status status, Date begin_date, Date end_date) {

        String jpql = "select sum(paybill.amount)-sum(case when paybill.tenantDiscount is null then 0 else paybill.tenantDiscount end) " +
                     " -sum(case when paybill.backDiscount is null then 0 else  paybill.backDiscount end) from PayBill paybill "+
                     "where paybill.tenant = :tenant and paybill.createDate>=:begin_date  and paybill.createDate<=:end_date and paybill.status=:status";
        try {
            BigDecimal sumAmount = entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("status",status).setParameter("begin_date",begin_date).setParameter("end_date",end_date).getSingleResult();
            return sumAmount==null?BigDecimal.ZERO:sumAmount;
        } catch (Exception E) {
            return BigDecimal.ZERO;
        }
    }

    public Page<PayBill> findPage(String tenantName, String username, String paymentMethod, Date beginDate, Date endDate, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PayBill> criteriaQuery = criteriaBuilder.createQuery(PayBill.class);
        Root<PayBill> root = criteriaQuery.from(PayBill.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenantName!=null&&!"".equals(tenantName)) {
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + tenantName+ "%"));
        }
        if (username!=null&&!"".equals(username)) {
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.like(root.get("member").<String> get("username"), "%" +username+ "%"));
        }
        if (paymentMethod != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("payment").get("paymentMethod"), paymentMethod));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("payment").<Date> get("paymentDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("payment").<Date> get("paymentDate"), endDate));
        }

//        if (StringUtils.isNotBlank(keyword)) { // 拼音条件
//            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.like(root.get("order").<String> get("sn"), "%" + keyword + "%")));
//        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}

