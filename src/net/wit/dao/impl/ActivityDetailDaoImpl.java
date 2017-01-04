package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ActivityDetailDao;
import net.wit.entity.*;
import net.wit.util.DateUtil;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.FlushModeType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */
@Repository("activityDetailDaoImpl")
public class ActivityDetailDaoImpl extends BaseDaoImpl<ActivityDetail, Long> implements ActivityDetailDao {


    public Page<ActivityDetail> openPage(Tenant tenant, ActivityRules activityRules, Member member, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityDetail> criteriaQuery = criteriaBuilder.createQuery(ActivityDetail.class);
        Root<ActivityDetail> root = criteriaQuery.from(ActivityDetail.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(tenant!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));
        }
        if(activityRules!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("activityRules"),activityRules));
        }

        if(member!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"),member));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


    /**
     * 根据用户或者商家查询任务是否完成
     * @param member
     * @param tenant
     * @param activityRules
     * @return false 未完成  true 已完成
     */
    public  boolean isActivity(Member member, Tenant tenant, ActivityRules activityRules){
        StringBuilder jpql = new StringBuilder("select count(ActivityDetail) from ActivityDetail activityDetail where 1 = 1");
        if(activityRules != null){  //积分规则
            jpql.append(" and  activityDetail.activityRules=:activityRules");
            if(activityRules.getType() == ActivityRules.Type.daily) {  //每日任务
                jpql.append(" and date_format(activityDetail.createDate,'%Y-%c-%d') = date_format(now(),'%Y-%c-%d')");
            }
        }

        Long count = 0L;

        if(member != null){  //会员
            jpql.append(" and  activityDetail.member=:member");
            count = entityManager.createQuery(jpql.toString(),Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("activityRules",activityRules).setParameter("member",member).getSingleResult();
        }else if(tenant != null){  //商家
            jpql.append(" and  activityDetail.tenant=:tenant");
            count = entityManager.createQuery(jpql.toString(),Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("activityRules",activityRules).setParameter("tenant",tenant).getSingleResult();
        }

        if(count > 0){
            return true;
        }else{
            return false;
        }
    }
}
