package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CouponNumberDao;
import net.wit.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 16/11/12.
 */
@Repository("couponNumberDaoImpl")
public class CouponNumberDaoImpl extends BaseDaoImpl<CouponNumber ,Long> implements CouponNumberDao {

    public Long getLastCode(Coupon coupon) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<CouponNumber> root = criteriaQuery.from(CouponNumber.class);
        criteriaQuery.select(criteriaBuilder.max(root.<Long>get("code")));
        Predicate restrictions = criteriaBuilder.conjunction();

        if (coupon != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("coupon"), coupon));
        }

        criteriaQuery.where(restrictions);
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
    }

    public CouponNumber findByCode(Long code){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CouponNumber> criteriaQuery = criteriaBuilder.createQuery(CouponNumber.class);
        Root<CouponNumber> root = criteriaQuery.from(CouponNumber.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (code != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Long>get("code"), code));
        }

        criteriaQuery.where(restrictions);

        try {
            return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CouponNumber> findList(Coupon coupon, Member member,Member guideMember, Long code){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CouponNumber> criteriaQuery = criteriaBuilder.createQuery(CouponNumber.class);
        Root<CouponNumber> root = criteriaQuery.from(CouponNumber.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (coupon != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("coupon"), coupon));
        }
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }

        if (guideMember != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("guideMember"), guideMember));
        }

        if (code != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Long>get("code"), code));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
        return super.findList(criteriaQuery,null,null,null,null);
    }

    public Page<CouponNumber> openPage(Coupon coupon, Member member, Member guideMember, Long code, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CouponNumber> criteriaQuery = criteriaBuilder.createQuery(CouponNumber.class);
        Root<CouponNumber> root = criteriaQuery.from(CouponNumber.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (coupon != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("coupon"), coupon));
        }
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }

        if (guideMember != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("guideMember"), guideMember));
        }

        if (code != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Long>get("code"), code));
        }

        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("status"), CouponNumber.Status.bound));

        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
        return super.findPage(criteriaQuery,pageable);
    }
    
	public List<CouponNumber>  resolveOpen(BigDecimal amount,BigDecimal brokerage,Member member,CouponCode couponCode) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponNumber> criteriaQuery = criteriaBuilder.createQuery(CouponNumber.class);
		Root<CouponNumber> root = criteriaQuery.from(CouponNumber.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (couponCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("couponCode"), couponCode));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"),CouponNumber.Status.receive));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));
		return entityManager.createQuery(criteriaQuery).getResultList();
		
	}

    public Page<Map<String, Object>> findCouponNumberPage(Coupon coupon, String keyword,Pageable pageable){
        String sql =
                "SELECT a.markcount,a.usercount,m.name d,t.name h,c.name f,e.name g,a.bro,a.guide_member " +
                "FROM " +
                "(SELECT guide_member,count(guide_member) markcount,COUNT(member) usercount,SUM(brokerage) bro FROM `xx_coupon_number` where guide_member is NOT null and coupon=:coupon GROUP BY guide_member)a ,xx_tenant t,xx_member m,xx_tenant_category c,xx_area e " +
                "where " +
                "a.guide_member=m.id AND t.id=m.tenant and t.tenant_category=c.id and t.area=e.id ";

        String totalSql = "SELECT count(1)  from "
                + "	(SELECT a.markcount,a.usercount,m.name v,t.name b,c.name n,e.name m,a.bro,a.guide_member " +
                "FROM " +
                "(SELECT guide_member,count(guide_member) markcount,COUNT(member) usercount,SUM(brokerage) bro FROM `xx_coupon_number` where guide_member is NOT null and coupon=:coupon GROUP BY guide_member)a ,xx_tenant t,xx_member m,xx_tenant_category c,xx_area e " +
                "where " +
                "a.guide_member=m.id AND t.id=m.tenant and t.tenant_category=c.id and t.area=e.id  ";

        if(StringUtils.isNotBlank(keyword)){
            sql=sql+"AND (m.name LIKE :keyword OR t.name LIKE :keyword OR e.name LIKE :keyword OR c.name LIKE :keyword)";
            totalSql=totalSql+"AND (m.name LIKE :keyword OR t.name LIKE :keyword OR e.name LIKE :keyword OR c.name LIKE :keyword)";
        }
        totalSql=totalSql+") z";

        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("coupon", coupon);
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("coupon", coupon);
        if (StringUtils.isNotBlank(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
            totalQuery.setParameter("keyword", "%" + keyword + "%");
        }
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List list = new ArrayList();
        Long total = 0l;
        try {
            list = query.getResultList();
            total = Long.parseLong(totalQuery.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
        if (totalPages < pageable.getPageNumber()) {
            return new Page<Map<String, Object>>(Collections.<Map<String, Object>>emptyList(), total, pageable);
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        if (list.size() > 0) {
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("mark_count", row[0]);
                map.put("user_count", row[1]);
                map.put("guider_name", row[2]);
                map.put("tenant_name", row[3]);
                map.put("tenant_category_name", row[4]);
                map.put("area_name", row[5]);
                map.put("brokerage", row[6]);
                map.put("guider_name_id", row[7]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }

    //根据coupon查找导购的登记情况
    public List<Map<String, Object>> findCouponNumberList(Coupon coupon,String keyword){
        String sql =
                "SELECT a.markcount,a.usercount,m.name d,t.name h,c.name f,e.name g,a.bro " +
                "FROM " +
                "(SELECT guide_member,count(guide_member) markcount,COUNT(member) usercount,SUM(brokerage) bro FROM `xx_coupon_number` where guide_member is NOT null and coupon=:coupon GROUP BY guide_member)a ,xx_tenant t,xx_member m,xx_tenant_category c,xx_area e " +
                "where " +
                "a.guide_member=m.id AND t.id=m.tenant and t.tenant_category=c.id and t.area=e.id ";
        if(StringUtils.isNotBlank(keyword)){
            sql=sql+"AND (m.name LIKE :keyword OR t.name LIKE :keyword)";
        }
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("coupon",coupon);
        if(StringUtils.isNotBlank(keyword)){
            query.setParameter("keyword", "%" + keyword + "%");
        }
        List list = new ArrayList();
        try {
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        if (list.size() > 0) {
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("mark_count", row[0]);
                map.put("user_count", row[1]);
                map.put("guider_name", row[2]);
                map.put("tenant_name", row[3]);
                map.put("tenant_category_name", row[4]);
                map.put("area_name", row[5]);
                map.put("brokerage", row[6]);
                maps.add(map);
            }
        }
        return maps;
    }
}
