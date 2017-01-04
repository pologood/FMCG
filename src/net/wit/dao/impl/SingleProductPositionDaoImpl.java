package net.wit.dao.impl;

import net.wit.dao.SingleProductPositionDao;
import net.wit.entity.ActivityPlanning;
import net.wit.entity.SingleProductPosition;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 16/11/30.
 */
@Repository("singleProductPositionDaoImpl")
public class SingleProductPositionDaoImpl extends BaseDaoImpl<SingleProductPosition,Long> implements SingleProductPositionDao {

    public List<SingleProductPosition> findList(SingleProductPosition.Type type){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SingleProductPosition> criteriaQuery = criteriaBuilder.createQuery(SingleProductPosition.class);
        Root<SingleProductPosition> root = criteriaQuery.from(SingleProductPosition.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("activityPlanning").<Date>get("beginDate"), new Date()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("activityPlanning").<Date>get("endDate"), new Date()));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery,null,null,null,null);
    }


    public SingleProductPosition findByActivityPlanning(ActivityPlanning activityPlanning){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SingleProductPosition> criteriaQuery = criteriaBuilder.createQuery(SingleProductPosition.class);
        Root<SingleProductPosition> root = criteriaQuery.from(SingleProductPosition.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("activityPlanning"), activityPlanning));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("activityPlanning").<Date>get("beginDate"), new Date()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("activityPlanning").<Date>get("endDate"), new Date()));
        criteriaQuery.where(restrictions);

        List<SingleProductPosition> singleProductPositions=super.findList(criteriaQuery,null,null,null,null);

        if(singleProductPositions.size()>0){
            return singleProductPositions.get(0);
        }else {
            return null;
        }

    }
}
