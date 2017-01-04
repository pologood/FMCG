/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DepositDao;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.Deposit.Type;
import net.wit.entity.Payment;
import net.wit.util.DateUtil;

import org.springframework.stereotype.Repository;

/**
 * Dao - 预存款
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("depositDaoImpl")
public class DepositDaoImpl extends BaseDaoImpl<Deposit, Long> implements DepositDao {

	public Page<Deposit> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<Deposit>(Collections.<Deposit> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Deposit> criteriaQuery = criteriaBuilder.createQuery(Deposit.class);
		Root<Deposit> root = criteriaQuery.from(Deposit.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Deposit> findPage(Member member, Date beginTime, Date endTime, Pageable pageable,Type type) {
		if (member == null) {
			return new Page<Deposit>(Collections.<Deposit> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Deposit> criteriaQuery = criteriaBuilder.createQuery(Deposit.class);
		Root<Deposit> root = criteriaQuery.from(Deposit.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		if (beginTime != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginTime));
		}
		if (endTime != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endTime));
		}
		if(type != null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		criteriaQuery.where(restrictions);

		return super.findPage(criteriaQuery, pageable);

	}

	public List<Deposit> findList(Member member, Date beginTime, Date endTime,Type type) {
		StringBuffer hql = new StringBuffer("from Deposit  d where 1=1");
		if (member != null) {
			hql.append(" and d.member.id=" + member.getId());
		}
		if (beginTime != null) {
			hql.append(" and d.createDate >='" + DateUtil.changeDateToStr(beginTime, DateUtil.LINK_DISPLAY_DATE_FULL)
					+ "'");
		}
		if (endTime != null) {
			hql.append(
					" and d.createDate<='" + DateUtil.changeDateToStr(endTime, DateUtil.LINK_DISPLAY_DATE_FULL) + "'");
		}
		if (type != null) {
			hql.append(" and d.type=" + type);
		}
		hql.append(" order by d.createDate desc ,d.id desc");
		return entityManager.createQuery(hql.toString(), Deposit.class).setFlushMode(FlushModeType.COMMIT)
				.getResultList();
	}

	/**
	 *     统计当月的收入、支出情况 账单统计
	 */
	@Override
	public List<Map<String, Object>> findMapList(Member member, String type, Date beginTime, Date endTime) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		
		StringBuffer hql = new StringBuffer("select d.type,sum(d.credit),sum(d.debit),(SELECT max(balance) FROM xx_deposit e where e.id=max(d.id)) as balance");
		hql.append(" from xx_deposit d where 1=1");
		if (member != null) {
			hql.append(" and d.member=" + member.getId());
		}
		if(type != null){
			hql.append(" and d.type in " + type);
		}
		if (beginTime != null) {
			hql.append(" and d.create_date >='" + DateUtil.changeDateToStr(beginTime, DateUtil.LINK_DISPLAY_DATE_FULL)
					+ "'");
		}
		if (endTime != null) {
			hql.append(
					" and d.create_date<='" + DateUtil.changeDateToStr(endTime, DateUtil.LINK_DISPLAY_DATE_FULL) + "'");
		}
		hql.append(" group by d.type ");
		
		Query query = entityManager.createNativeQuery(hql.toString());
		query.setFlushMode(FlushModeType.COMMIT);
		
		try {
			List<?> data = query.getResultList();
			if(data.size()>0){
				for(Object obj : data){
					Object[] row = (Object[]) obj;
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("type", getName(row[0].toString()));
					map.put("credit", row[1]);
					map.put("debit", row[2]);
					map.put("balance", row[3]);
					mapList.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapList;
	}	
		
	private String getName(String type){
		String name = "";
		switch(type){
		case "0":
			name = "充值 - 收入";
			break;
		case "1":
			name = "购物 - 支出 ";
			break;
		case "2":
			name = "提现 - 支出";
			break;
		case "3":
			name = "货款 - 收入";
			break;
		case "4":
			name = "分润 - 收入";
			break;
		case "5":
			name = "佣金 - 支出";
			break;
		case "6":
			name = "收款 - 收入";
			break;
		case "7":
			name = "其他 - 收入";
			break;
		case "8":
			name = "其他 - 支出";
			break;
		case "9":
			name = "红包 - 收入";
			break;
		case "10":
			name = "红包 - 支出";
			break;
			default:
				name = type;
				break;
		}
		
		return name; 
	}
	
	/*
	 * (non-Javadoc) <p>Title: income</p> <p>Description: </p>
	 * 
	 * @param member
	 * 
	 * @return
	 * 
	 * @see net.wit.dao.DepositDao#income(net.wit.entity.Member)
	 */

	@Override
	public BigDecimal income(Member member, Type type, Date beginTime, Date endTime, Deposit.Status status) {
		StringBuffer hql = new StringBuffer("select sum(d.credit) from Deposit d where 1=1");
		if (member != null) {
			hql.append(" and d.member=:member ");
		}
		if (type != null) {
			hql.append(" and d.type=:type ");
		}
		if (status != null) {
			hql.append(" and d.status=:status ");
		}
		if (beginTime != null) {
			hql.append(" and d.createDate >='" + DateUtil.changeDateToStr(beginTime, DateUtil.LINK_DISPLAY_DATE_FULL)
					+ "'");
		}
		if (endTime != null) {
			hql.append(
					" and d.createDate<='" + DateUtil.changeDateToStr(endTime, DateUtil.LINK_DISPLAY_DATE_FULL) + "'");
		}
		Query query = entityManager.createQuery(hql.toString()).setFlushMode(FlushModeType.COMMIT);
		if (member != null) {
			query.setParameter("member", member);
		}
		if (type != null) {
			query.setParameter("type", type);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		Object result = query.getSingleResult();
		if (result != null) {
			return (BigDecimal) result;
		}
		return BigDecimal.ZERO;
	}

	/*
	 * (non-Javadoc) <p>Title: outcome</p> <p>Description: </p>
	 * 
	 * @param member
	 * 
	 * @return
	 * 
	 * @see net.wit.dao.DepositDao#outcome(net.wit.entity.Member)
	 */

	@Override
	public BigDecimal outcome(Member member, Type type, Date beginTime, Date endTime, Deposit.Status status) {
		StringBuffer hql = new StringBuffer("select sum(d.debit) from Deposit  d where 1=1");
		if (member != null) {
			hql.append(" and d.member=:member ");
		}
		if (type != null) {
			hql.append(" and d.type=:type ");
		}
		if (status != null) {
			hql.append(" and d.status=:status ");
		}
		if (beginTime != null) {
			hql.append(" and d.createDate >='" + DateUtil.changeDateToStr(beginTime, DateUtil.LINK_DISPLAY_DATE_FULL)
					+ "'");
		}
		if (endTime != null) {
			hql.append(
					" and d.createDate<='" + DateUtil.changeDateToStr(endTime, DateUtil.LINK_DISPLAY_DATE_FULL) + "'");
		}
		Query query = entityManager.createQuery(hql.toString()).setFlushMode(FlushModeType.COMMIT);
		if (member != null) {
			query.setParameter("member", member);
		}
		if (type != null) {
			query.setParameter("type", type);
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		Object result = query.getSingleResult();
		if (result != null) {
			return (BigDecimal) result;
		}
		return BigDecimal.ZERO;
	}

	

}