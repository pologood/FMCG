package net.wit.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ProfitDao;
import net.wit.entity.Area;
import net.wit.entity.Profit;
import net.wit.entity.Profit.Status;
/**
 * 货郎联盟
 * @author Administrator
 *
 */
@Repository("profitDaoImpl")
public class ProfitDaoImpl extends BaseDaoImpl<Profit,Long> implements ProfitDao{

	public List<Profit> findList(Date startDate,Date endDate,Status status,Integer level,Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Profit> criteriaQuery = criteriaBuilder.createQuery(Profit.class);
		Root<Profit> root = criteriaQuery.from(Profit.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if (status!=null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (level!=null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("level"), level));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, null, null);
	}
	
	public Page<Profit> findPage(Date beginDate, Date endDate,Status status,String keyword,Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Profit> criteriaQuery = criteriaBuilder.createQuery(Profit.class);
        Root<Profit> root = criteriaQuery.from(Profit.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (keyword != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.like(root.get("member").<String>get("username"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("order").<String>get("sn"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.<String>get("memo"), "%" + keyword + "%")
                    )
            );
        }
        if (status!=null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}
