/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.RebateDao;
import net.wit.entity.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Dao - 收款单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("rebateDaoImpl")
public class RebateDaoImpl extends BaseDaoImpl<Rebate, Long> implements RebateDao {

	public Page<Rebate> findPage(Member member,Pageable pageable) {
		if (member == null) {
			return new Page<Rebate>(Collections.<Rebate> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
		Root<Rebate> root = criteriaQuery.from(Rebate.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Rebate> findPage(Member member, Rebate.Type type,CouponCode couponCode, Pageable pageable) {
		if (member == null) {
			return new Page<Rebate>(Collections.<Rebate> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
		Root<Rebate> root = criteriaQuery.from(Rebate.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));

		if(type!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}

		if(couponCode!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("couponCode"), couponCode));
		}

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * 获取分润佣金
	 */
	public BigDecimal getAmount(Member member, Rebate.Type type){
		try {
			StringBuffer hsql = new StringBuffer("SELECT sum(rebate.amount) from Rebate rebate WHERE rebate.member = :member AND rebate.type=:type");
			javax.persistence.Query query = entityManager.createQuery(hsql.toString());
			query.setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("type", type);
			BigDecimal amount =  (BigDecimal) query.getSingleResult();
			return amount==null?BigDecimal.ZERO:amount;
		} catch (NoResultException e) {
			return BigDecimal.ZERO;
		}
	}

	public long count(Member member, Rebate.Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
		Root<Rebate> root = criteriaQuery.from(Rebate.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}

		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public List<Rebate> openCodeRebate(CouponCode couponCode,Member member){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
		Root<Rebate> root = criteriaQuery.from(Rebate.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("couponCode"), couponCode));
		if(member!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(restrictions);
		return  super.findList(criteriaQuery,null,null,null,null);
	}

	public void calcRebate() {
		String  jpql = "";
		        jpql +="insert into xx_rebate(create_date,modify_date,rebate_date,";
				jpql +="lv1member_count,lv1amount,lv1rebate,";
				jpql +="lv2member_count,lv2amount,lv2rebate,";
				jpql +="status,member) ";
				jpql +="select curdate() as create_date,curdate() as modify_date,date_sub(curdate(),interval 1 day) as rebate_date,";
				jpql +="sum(lv1_member_count),sum(lv1_amount),sum(lv1_rebate),";
				jpql +="sum(lv2_member_count),sum(lv2_amount),sum(lv2_rebate),";
				jpql +="0 as status,";
				jpql +="member from (";
				jpql +="select ";
				jpql +="count(distinct a.id) lv1_member_count,sum(a.amount) lv1_amount,sum(a.recv-a.cost)*0.3 lv1_rebate,";
				jpql +="0 as lv2_member_count,0 as lv2_amount,0 as lv2_rebate,";
				jpql +="b.member as member ";
				jpql +="from xx_credit a,xx_member b where a.member=b.id ";
				jpql +="and status=2 and b.member is not null and ";
				jpql +="date(credit_date) = date_sub(curdate(),interval 1 day) ";
				jpql +="group by b.member ";
				jpql +="union all ";
				jpql +="select ";
				jpql +="0 as lv1_member_count,0 as lv1_amount,0 as lv1_rebate,";
				jpql +="count(distinct a.id) lv2_member_count,sum(a.amount) lv2_amount,sum(a.recv-a.cost)*0.03 lv2_rebate,";
				jpql +="c.member as member ";
				jpql +="from xx_credit a,xx_member b,xx_member c where a.member=b.id and b.member=c.id ";
				jpql +="and status=2 and c.member is not null and ";
				jpql +="date(credit_date) = date_sub(curdate(),interval 1 day) ";
				jpql +="group by c.member ";
				jpql +=") j group by member";
		Query query = entityManager.createNativeQuery(jpql);
		query.executeUpdate();
	}
	public Page<Rebate> findPage(Date beginDate, Date endDate,String keyword, Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
		Root<Rebate> root = criteriaQuery.from(Rebate.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();

		if(beginDate!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),beginDate));
		}
		if(endDate!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),endDate));
		}
		if (StringUtils.isNotBlank(keyword)) {
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(
				criteriaBuilder.like(root.<String> get("description"), "%" + keyword + "%")));
		}

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public List<Rebate> findList(Date beginDate, Date endDate,String keyword,List<Filter> filters){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
		Root<Rebate> root = criteriaQuery.from(Rebate.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();

		if(beginDate!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),beginDate));
		}
		if(endDate!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),endDate));
		}
		if (StringUtils.isNotBlank(keyword)) {
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(
					criteriaBuilder.like(root.<String> get("description"), "%" + keyword + "%")));
		}

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery,null,null,filters,null);
	}

	@Override
	public BigDecimal sumBrokerage(Member member, Rebate.Type type, Rebate.OrderType orderType) {

		String jpql = "SELECT sum(a.brokerage) from Rebate a where  a.type=:type  and a.orderType=:orderType and a.member=:member " ;
		Object result = entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("type",type).setParameter("orderType",orderType).setParameter("member",member).getSingleResult();

		return result==null?BigDecimal.ZERO:new BigDecimal(result.toString());
	}
}