package net.wit.dao;

import net.wit.dao.impl.BaseDaoImpl;
import net.wit.entity.Subsidies;
import net.wit.entity.Trade;
import org.springframework.stereotype.Repository;

/**
 * dao 结算
 * @author cat-vane
 *
 */
public interface SubsidiesDao extends BaseDao<Subsidies, Long>{
//	public Page<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Pageable pageable){
////		if (tenant == null) {
////			return new Page<Account>(Collections.<Account> emptyList(), 0, pageable);
////		}
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
//		Root<Account> root = criteriaQuery.from(Account.class);
//		criteriaQuery.select(root);
//		Predicate restrictions = criteriaBuilder.conjunction();
//		if(start_date!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),start_date));
//		}
//		if(end_date!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),end_date));
//		}
//		if(tenant!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"),tenant));
//		}
//		if(seller!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),seller));
//		}
//		criteriaQuery.where(restrictions);
//		return super.findPage(criteriaQuery, pageable);
//	}
//	public List<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Account.Status status){
////		if (tenant == null) {
////			return new Page<Account>(Collections.<Account> emptyList(), 0, pageable);
////		}
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
//		Root<Account> root = criteriaQuery.from(Account.class);
//		criteriaQuery.select(root);
//		Predicate restrictions = criteriaBuilder.conjunction();
//		if(start_date!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),start_date));
//		}
//		if(end_date!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),end_date));
//		}
//		if(tenant!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"),tenant));
//		}
//		if(seller!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),seller));
//		}
//		if(status!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"),status));
//		}
//		criteriaQuery.where(restrictions);
//		TypedQuery<Account> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
//		return query.getResultList();
//	}
//	public List<Account> withdrawSettle(Tenant tenant,Date start_date,Date end_date,Account.Status status){
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
//		Root<Account> root = criteriaQuery.from(Account.class);
//		criteriaQuery.select(root);
//		Predicate restrictions = criteriaBuilder.conjunction();
//		if(start_date!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),start_date));
//		}
//		if(end_date!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),end_date));
//		}
//		if(tenant!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"),tenant));
//		}
//		if(status!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"),status));
//		}
//		criteriaQuery.where(restrictions);
//		TypedQuery<Account> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
//		return query.getResultList();
//	}
//
//	public List<Account> findTenants(Tenant tenant){
//		if (tenant == null) {
//			return Collections.<Account> emptyList();
//		}
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
//		Root<Account> root = criteriaQuery.from(Account.class);
//		criteriaQuery.select(root);
//		Predicate restrictions = criteriaBuilder.conjunction();
//		if(tenant!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"),tenant));
//		}
//		criteriaQuery.where(restrictions);
//		criteriaQuery.groupBy(root.get("tenant"));
//		return super.findList(criteriaQuery, null, null, null, null);
//	}
}
