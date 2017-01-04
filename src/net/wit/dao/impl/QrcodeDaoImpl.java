package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import org.springframework.stereotype.Repository;

import net.wit.dao.QrcodeDao;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;

import java.util.Date;
import java.util.List;

@Repository("qrcodeDaoImpl")
public class QrcodeDaoImpl extends BaseDaoImpl<Qrcode, Long> implements QrcodeDao {

    public Qrcode findbyUrl(String url) {
        if (url == null) {
            return null;
        }
        String jpql = "select qrcode from Qrcode qrcode where lower(qrcode.url) = lower(:url)";
        try {
            return entityManager.createQuery(jpql, Qrcode.class).setFlushMode(FlushModeType.COMMIT).setParameter("url", url).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Qrcode findbyTenant(Tenant tenant) {
        if (tenant == null) {
            return null;
        }
        String jpql = "select qrcode from Qrcode qrcode where qrcode.tenant = :tenant";
        try {
            return entityManager.createQuery(jpql, Qrcode.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean tenantExists(Tenant tenant) {
        if (tenant == null) {
            return false;
        }
        String jpql = "select count(qrcode) from Qrcode qrcode where qrcode.tenant = :tenant";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).getSingleResult();
        return count > 0;
    }


    public Page<Qrcode> openPage(String keyword, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Qrcode> criteriaQuery = criteriaBuilder.createQuery(Qrcode.class);
        Root<Qrcode> root = criteriaQuery.from(Qrcode.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (keyword != null) {
            restrictions = criteriaBuilder.and(
                    restrictions, criteriaBuilder.or(
                            criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("tenant").get("member").<String>get("username"), "%" + keyword + "%"))
            );
        }

        //restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), ActivityRules.Status.enabled));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Qrcode> findUnLockList(Integer count, Member member) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Qrcode> criteriaQuery = criteriaBuilder.createQuery(Qrcode.class);
        Root<Qrcode> root = criteriaQuery.from(Qrcode.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("lockExpire")), criteriaBuilder.lessThanOrEqualTo(root.<Date>get("lockExpire"), new Date())));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery,null,count,null,null);
    }
}
