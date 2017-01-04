package net.wit.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SpReturnsDao;
import net.wit.entity.Member;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.Tenant;

/**
 * Created by WangChao on 2016-4-12.
 */
@Repository("spReturnsDaoImpl")
public class SpReturnsDaoImpl extends BaseDaoImpl<SpReturns, Long> implements SpReturnsDao{
	public Page<SpReturns> findPage(Member member, ReturnStatus returnStatus,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(member!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("order").get("member"), member));
		}
		if (returnStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returnStatus"), returnStatus ));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	
	public Page<SpReturns> findBySupplier(Date start_date,Date end_date,Tenant supplier, ReturnStatus returnStatus,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (start_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), start_date));
        }
        if (end_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), end_date));
        }
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (returnStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returnStatus"), returnStatus ));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public List<SpReturns> returnSettle(Date start_date,Date end_date,Tenant supplier, Boolean status){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (start_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), start_date));
        }
        if (end_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), end_date));
        }
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("suppliered"), status ));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<SpReturns> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	
	public Page<SpReturns> findByTenant(Tenant tenant, ReturnStatus returnStatus,String keyword,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), tenant));
		}
		if (returnStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returnStatus"), returnStatus ));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("trade").get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("trade").get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("trade").get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("trade").get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public Page<SpReturns> findBySettle(Member member,Date start_date,Date end_date,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), member.getTenant()));
		}
		if (start_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), start_date));
		}
		if (end_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), end_date));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public List<SpReturns> findByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,Boolean status,String keywords){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (start_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("completedDate"), start_date));
        }
        if (end_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("completedDate"), end_date));
        }
        if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), tenant));
		}
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("suppliered"), status ));
		}
		if (StringUtils.isNotBlank(keywords)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("trade").get("order").<String> get("sn"), keywords), criteriaBuilder.equal(root.<String> get("sn"), keywords),
							criteriaBuilder.like(root.get("trade").get("tenant").<String> get("name"), "%" + keywords + "%"), criteriaBuilder.like(root.get("trade").get("order").<String> get("consignee"), "%" + keywords + "%"),
							criteriaBuilder.like(root.get("trade").get("order").<String> get("phone"), "%" + keywords + "%")));
							
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returnStatus"), ReturnStatus.completed));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		TypedQuery<SpReturns> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	public Page<SpReturns> findPageByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,ReturnStatus returnStatus,Boolean status,String keyword,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), tenant));
		}
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (returnStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returnStatus"), returnStatus ));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("suppliered"), status ));
		}
		if (start_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("completedDate"), start_date));
        }
        if (end_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("completedDate"), end_date));
        }
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("trade").get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("trade").get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("trade").get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("trade").get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returnStatus"), ReturnStatus.completed));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public long returnApplyCount(List<Filter> filters, Tenant tenant, ReturnStatus returnStatus, String keyword) {
		if (tenant == null) {
			return 0;
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), tenant));
		if (returnStatus == null) {

				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.get("returnStatus"),ReturnStatus.completed));

			    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("returnStatus"), ReturnStatus.cancelled));

		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, filters);
	}
	public List<SpReturns> findReturnNumber(Tenant tenant,ReturnStatus returnStatus ){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SpReturns> criteriaQuery = criteriaBuilder.createQuery(SpReturns.class);
		Root<SpReturns> root = criteriaQuery.from(SpReturns.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();

		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), tenant));
		}
		if (returnStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returnStatus"), returnStatus));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<SpReturns> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
}
