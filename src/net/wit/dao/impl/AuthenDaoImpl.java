package net.wit.dao.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Order.Direction;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.dao.AuthenDao;
import net.wit.entity.Authen;
import net.wit.entity.Authen.AuthenStatus;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.OrderEntity;
import net.wit.entity.Tenant;
import net.wit.entity.Authen.AuthenType;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("authenDaoImpl")
public class AuthenDaoImpl extends BaseDaoImpl<Authen, Long> implements AuthenDao {
	//private static final Pattern pattern = Pattern.compile("\\d*");
	
	/* (non-Javadoc)
	 * @see net.wit.dao.impl.AuthenDao#findByType(net.wit.entity.Tenant, java.lang.String)
	 */
	@Override
	public Authen findByType(Tenant tenant,AuthenType enterprise) {
		if (enterprise == null) {
			return null;
		}
		String jpql = "select authen from Authen authen where authen.tenant = :tenant and authen.authenType = :authenType";
		try {
			return entityManager.createQuery(jpql, Authen.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("authenType", enterprise).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see net.wit.dao.impl.AuthenDao#persist(net.wit.entity.Authen)
	 */
	@Override
	public void persist(Authen authen) {
		Assert.notNull(authen);
		
		super.persist(authen);
	}

	/* (non-Javadoc)
	 * @see net.wit.dao.impl.AuthenDao#merge(net.wit.entity.Authen)
	 */
	@Override
	public Authen merge(Authen authen) {
		Assert.notNull(authen);
		
		return super.merge(authen);
	}

	@Override
	public Page<Authen> findPage(Area area,AuthenStatus authenStatus,Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Authen> criteriaQuery=criteriaBuilder.createQuery(Authen.class);
		Root<Authen> root=criteriaQuery.from(Authen.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		/*设置起止时间查询条件*/
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("modifyDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("modifyDate"), endDate));
		}
		/*设置认证状态查询条件*/
		if (authenStatus!=null) {
	    	restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<AuthenStatus>get("authenStatus"), authenStatus));
		}
		if (area!=null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("area"), area), criteriaBuilder.like(root.get("tenant").get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		CriteriaQuery<Authen> cQuery=criteriaQuery.where(restrictions);
		
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
	}
	
}
