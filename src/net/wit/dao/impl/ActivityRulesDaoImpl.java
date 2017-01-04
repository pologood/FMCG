package net.wit.dao.impl;

import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ActivityRulesDao;
import net.wit.entity.ActivityRules;
import net.wit.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
@Repository("activityRulesDaoImpl")
public class ActivityRulesDaoImpl extends BaseDaoImpl<ActivityRules, Long> implements ActivityRulesDao {

    public Page<ActivityRules> openPage(ActivityRules.Type type , Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityRules> criteriaQuery = criteriaBuilder.createQuery(ActivityRules.class);
        Root<ActivityRules> root = criteriaQuery.from(ActivityRules.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(type!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"),type));
        }
        restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), ActivityRules.Status.enabled));
        criteriaQuery.where(restrictions);
        List<Order> orders = pageable.getOrders();
        orders.add(Order.asc("id"));
        return super.findPage(criteriaQuery, pageable);
    }

    public ActivityRules findByIdAndStatus(Long id){
        if(id==null){
            return  null;
        }
        try{
            String hsql = "select activityRules from ActivityRules activityRules where lower(activityRules.id) = lower(:id) and activityRules.status = 0";
            return  entityManager.createQuery(hsql, ActivityRules.class).setFlushMode(FlushModeType.COMMIT).setParameter("id", id).getSingleResult();
        }catch (NoResultException e){
            return  null;
        }
    }
}
