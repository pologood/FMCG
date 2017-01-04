package net.wit.dao.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ActivityInventoryDao;
import net.wit.entity.ActivityInventory;
import net.wit.util.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by Administrator on 2016/7/15.
 */
@Repository("activityInventoryDaoImpl")
public class ActivityInventoryDaoImpl extends BaseDaoImpl<ActivityInventory,Long> implements ActivityInventoryDao {

    public Page<ActivityInventory> openPage(ActivityInventory.Status status,String keyword, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActivityInventory> criteriaQuery = criteriaBuilder.createQuery(ActivityInventory.class);
        Root<ActivityInventory> root = criteriaQuery.from(ActivityInventory.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(status!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"),status));
        }

        if (keyword != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,criteriaBuilder.or(
                            criteriaBuilder.like(root.get("tenant").<String>get("name"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("tenant").get("member").<String>get("username"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("tenant").get("member").<String>get("name"), "%" + keyword + "%"))
            );
        }

        //restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), ActivityRules.Status.enabled));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}
