package net.wit.dao.impl;

import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ActivityPlanningDao;
import net.wit.dao.CouponCodeDao;
import net.wit.dao.CouponDao;
import net.wit.entity.ActivityPlanning;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.ActivityPlanning.OnOff;
import net.wit.support.RandomComparator;
import net.wit.support.TenantComparatorByDistance;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import javax.persistence.criteria.Subquery;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/8/1.
 */
@Repository("activityPlanningDaoImpl")
public class ActivityPlanningDaoImpl extends  BaseDaoImpl<ActivityPlanning,Long> implements ActivityPlanningDao {

    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;

    public Page<ActivityPlanning> openPage(Pageable pageable, String keyword){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityPlanning> criteriaQuery = criteriaBuilder.createQuery(ActivityPlanning.class);
        Root<ActivityPlanning> root = criteriaQuery.from(ActivityPlanning.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (keyword != null) {
            restrictions = criteriaBuilder.and(
                    restrictions, criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"))
            );
        }
        //restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), ActivityRules.Status.enabled));
        criteriaQuery.where(restrictions);
        List<Order> orders = pageable.getOrders();
        orders.add(Order.desc("id"));
        return super.findPage(criteriaQuery, pageable);
    }

    public ActivityPlanning getCurrent(Tenant tenant, ActivityPlanning.Type type) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityPlanning> criteriaQuery = criteriaBuilder.createQuery(ActivityPlanning.class);
        Root<ActivityPlanning> root = criteriaQuery.from(ActivityPlanning.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if(tenant!=null){
//            Subquery<ActivityPlanning> tenantsSubquery = criteriaQuery.subquery(ActivityPlanning.class);
//            Root<ActivityPlanning> tenantsSubqueryRoot = tenantsSubquery.from(ActivityPlanning.class);
//            tenantsSubquery.select(tenantsSubqueryRoot);
////            List<Tenant> tenants = new ArrayList<Tenant>();
////            tenants.add(tenant);
//            tenantsSubquery.where(criteriaBuilder.equal(tenantsSubquery, root), tenantsSubqueryRoot.join("tenants").in(tenant));
            restrictions = criteriaBuilder.and(restrictions, root.join("tenants").in(tenant));

       }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("onOff"), OnOff.on));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("beginDate"), new Date()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("endDate"), new Date()));
        criteriaQuery.where(restrictions);
        List<ActivityPlanning> planns = super.findList(criteriaQuery, 0, 1, null, null);
        if (planns.size()>0) {
        	return planns.get(0);
        } else {
        	return null;
        }

    }

    public List<ActivityPlanning> findUnionActivity() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityPlanning> criteriaQuery = criteriaBuilder.createQuery(ActivityPlanning.class);
        Root<ActivityPlanning> root = criteriaQuery.from(ActivityPlanning.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), ActivityPlanning.Type.unionActivity));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("onOff"), OnOff.on));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("beginDate"), new Date()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("endDate"), new Date()));
        criteriaQuery.where(restrictions);
        return  super.findList(criteriaQuery, null, null, null, null);
    }

	/**
	 * @param amount
	 * @param activityPlanning
	 * @return
	 */
    public CouponCode  lockCoupon(ActivityPlanning activityPlanning,BigDecimal amount) {
    	List<Coupon> coupons = activityPlanning.lockCoupon(amount);
    	if (coupons.size()==0) {
    		return null;
    	}
    	List<CouponCode> couponCodes = new ArrayList<CouponCode>();
    	int total =0;
    	for (Coupon coupon:coupons) {
        	total = total + coupon.getEffectiveCount();
    	}
        //System.out.println("total="+total);

        if(total==0){
            return null;
        }
    	for (Coupon coupon:coupons) {
    		int current = coupon.getEffectiveCount()*100 / total;
            //System.out.println("current="+current);
        	//List<CouponCode> codes = coupon.getActivityCouponCodes();
            if(current>0){
                List<CouponCode> codes = couponCodeDao.findUsedCouponCodeByCoupon(coupon,current);
                for (CouponCode code:codes) {
                    couponCodes.add(code);
                }
            }
    	}
    	
        RandomComparator randomComparator = new RandomComparator();
        Collections.sort(couponCodes, randomComparator);

//        for (CouponCode sort:couponCodes) {
//            System.out.println("sort="+sort.getCoupon().getAmount());
//        }

        Random random = new Random(System.currentTimeMillis());

//        int random11=(int) (Math.random()*couponCodes.size()+1);
        //System.out.println("iRandom="+iRandom+" "+random.toString());

     	if (couponCodes.size()==0) {
    		return null;
    	} else {
            int iRandom = random.nextInt(couponCodes.size())+1;
     		return couponCodes.get(iRandom-1);
    	}
    }

    public boolean isAcitivtyName(String name){
        if (name == null) {
            return false;
        }
        String jpql = "select count(activityPlanning) from ActivityPlanning activityPlanning where activityPlanning.name = :name";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("name", name).getSingleResult();
        return count > 0;
    }
}
