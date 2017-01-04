/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CouponCodeDao;

/**
 * Dao - 优惠码
 * @author rsico Team
 * @version 3.0
 */
@Repository("couponCodeDaoImpl")
public class CouponCodeDaoImpl extends BaseDaoImpl<CouponCode, Long>implements CouponCodeDao {

	public boolean codeExists(String code) {
		if (code == null) {
			return false;
		}
		String jpql = "select count(couponCode) from CouponCode couponCode where lower(couponCode.code) = lower(:code)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		return count > 0;
	}

	public CouponCode findByCode(String code) {
		if (code == null) {
			return null;
		}
		try {
			String jpql = "select couponCode from CouponCode couponCode where lower(couponCode.code) = lower(:code)";
			return entityManager.createQuery(jpql, CouponCode.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public CouponCode build(Coupon coupon, Member member) {
		Assert.notNull(coupon);
		CouponCode couponCode = new CouponCode();
		String uuid = UUID.randomUUID().toString().toUpperCase();
		couponCode.setCode(coupon.getPrefix() + uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24));
		couponCode.setIsUsed(false);
		couponCode.setExpire(coupon.getEndDate());
		couponCode.setCoupon(coupon);
		couponCode.setMember(member);
		couponCode.setBalance(BigDecimal.ZERO);
		couponCode.setAmount(BigDecimal.ZERO);
		super.persist(couponCode);
		return couponCode;
	}

	public CouponCode buildRed(Coupon coupon, Member member,Member guide) {
		Assert.notNull(coupon);
		CouponCode couponCode = new CouponCode();
		String uuid = UUID.randomUUID().toString().toUpperCase();
		couponCode.setCode(coupon.getPrefix() + uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24));
		couponCode.setIsUsed(false);
		couponCode.setExpire(coupon.getEndDate());
		couponCode.setCoupon(coupon);
		couponCode.setMember(member);
		couponCode.setGuideMember(guide);
		couponCode.setBalance(BigDecimal.ZERO);
		couponCode.setAmount(BigDecimal.ZERO);
		super.persist(couponCode);
		return couponCode;
	}
	
	public List<CouponCode> build(Coupon coupon, Member member, Integer count) {
		Assert.notNull(coupon);
		Assert.notNull(count);
		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = build(coupon, member);
			couponCodes.add(couponCode);
			if (i % 100 == 0) {
				super.flush();
				super.clear();
			}
		}
		return couponCodes;
	}

	public List<CouponCode> buildRed(Coupon coupon, Member member, Integer count,Member guide) {
		Assert.notNull(coupon);
		Assert.notNull(count);
		List<CouponCode> couponCodes = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = buildRed(coupon, member, guide);
			couponCodes.add(couponCode);
			if (i % 100 == 0) {
				super.flush();
				super.clear();
			}
		}
		return couponCodes;
	}

	public List<CouponCode> build(Coupon coupon,  Integer count,Date date) {
		Assert.notNull(coupon);
		Assert.notNull(count);
		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (int i = 0; i < count; i++) {
			CouponCode couponCode = new CouponCode();
			String uuid = UUID.randomUUID().toString().toUpperCase();
			couponCode.setCode(coupon.getPrefix() + uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24));
			couponCode.setIsUsed(false);
			couponCode.setExpire(date);
			couponCode.setCoupon(coupon);
			couponCode.setMember(null);
			couponCode.setBalance(BigDecimal.ZERO);
			couponCode.setAmount(BigDecimal.ZERO);
			super.persist(couponCode);
			couponCodes.add(couponCode);
			if (i % 100 == 0) {
				super.flush();
				super.clear();
			}
		}
		return couponCodes;
	}

	public Page<CouponCode> findPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		if (member == null) {
			return new Page<CouponCode>(Collections.<CouponCode> emptyList(), 0, pageable);
		}
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findPage(criteriaQuery, pageable);
	}
	
	public Page<CouponCode> findPage(Member member,Boolean isUsed,Boolean isExpired, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member == null) {
			return new Page<CouponCode>(Collections.<CouponCode> emptyList(), 0, pageable);
		}
		
		if (isExpired != null) {
			if (isExpired) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()) ));
			} else {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
			}
		}
		if (isUsed != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUsed"), isUsed));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<CouponCode> findPage(Member member, Boolean isUsed, Boolean isExpired, Coupon.Type type, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(type!=null){
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.equal(root.get("coupon").<Coupon.Type>get("type"), type));
		}
		if (member == null) {
			return new Page<>(Collections.<CouponCode>emptyList(), 0, pageable);
		}
		if (isExpired != null) {
			if (isExpired) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()) ));
			} else {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
			}
		}
		if (isUsed != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUsed"), isUsed));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		Path<Coupon> couponPath = root.get("coupon");
		if (coupon != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(couponPath, coupon));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (hasBegun != null) {
			if (hasBegun) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(couponPath.get("startDate").isNull(), criteriaBuilder.lessThanOrEqualTo(couponPath.<Date> get("startDate"), new Date())));
			} else {
				restrictions = criteriaBuilder.and(restrictions, couponPath.get("startDate").isNotNull(), criteriaBuilder.greaterThan(couponPath.<Date> get("startDate"), new Date()));
			}
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()) ));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
			}
		}
		if (isUsed != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUsed"), isUsed));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}
	
	public List<CouponCode> findEnabledList(Member member) {
		if (member == null) {
			return new ArrayList<CouponCode>();
		}
		try {
			String jpql = "select couponCode from CouponCode couponCode where couponCode.member = :member and couponCode.isUsed=:isUsed and couponCode.coupon.endDate>=now() and couponCode.coupon.startDate<=now() and couponCode.coupon.isEnabled=:isEnabled";
			return entityManager.createQuery(jpql, CouponCode.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", member)
					.setParameter("isUsed", false)
					.setParameter("isEnabled", true).getResultList();
		} catch (NoResultException e) {
			return new ArrayList<CouponCode>();
		}
	}

	public CouponCode findMemberCouponCode(Member member) {
		if (member == null) {
			return null;
		}
		try {
			String jpql = "select couponCode from CouponCode couponCode where couponCode.member = :member and couponCode.isUsed=:isUsed and couponCode.balance>:balance and couponCode.coupon.type=:type";

			List<CouponCode>  couponCodes= entityManager.createQuery(jpql, CouponCode.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", member)
					.setParameter("isUsed", false)
					.setParameter("balance", BigDecimal.ZERO)
					.setParameter("type", Coupon.Type.multipleCoupon).getResultList();

			return (couponCodes!=null&&couponCodes.size()>0)?couponCodes.get(0):null;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Page<CouponCode> findCanUseCouponCode(Member member,
			Pageable pageable) {
		if (member == null) {
			return new Page<CouponCode>(Collections.<CouponCode> emptyList(), 0, pageable);
		}
		CriteriaQuery<CouponCode> criteriaQuery = findCanUseCouponCode(member);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Long findCanUseCount(Member member) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<CouponCode> criteriaQuery = findCanUseCouponCode(member);
		return super.count(criteriaQuery, null);
	}
	
	/** 会员可以用的红包 */
	private CriteriaQuery<CouponCode> findCanUseCouponCode(Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, 
						criteriaBuilder.equal(root.get("coupon").get("isEnabled"), true),
						criteriaBuilder.greaterThanOrEqualTo(root.get("coupon").<Date>get("endDate"), new Date()),
						criteriaBuilder.equal(root.get("member"), member),
						criteriaBuilder.equal(root.get("isUsed"), false),
						criteriaBuilder.isNull(root.get("usedDate")));
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}
	
	@Override
	public Page<CouponCode> findUsedCouponCode(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<CouponCode>(Collections.<CouponCode> emptyList(), 0, pageable);
		}
		CriteriaQuery<CouponCode> criteriaQuery = findUsedCouponCode(member);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Long findUsedCount(Member member) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<CouponCode> criteriaQuery = findUsedCouponCode(member);
		return super.count(criteriaQuery, null);
	}
	
	/** 会员已经使用的红包 */
	private CriteriaQuery<CouponCode> findUsedCouponCode(Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, 
						criteriaBuilder.equal(root.get("member"), member),
						criteriaBuilder.equal(root.get("isUsed"), true),
						criteriaBuilder.isNotNull(root.get("usedDate")));
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}
	
	@Override
	public Page<CouponCode> findOverdueCouponCode(Member member,
			Pageable pageable) {
		if (member == null) {
			return new Page<CouponCode>(Collections.<CouponCode> emptyList(), 0, pageable);
		}
		CriteriaQuery<CouponCode> criteriaQuery = findOverdueCouponCode(member);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Long findOverdueCount(Member member) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<CouponCode> criteriaQuery = findOverdueCouponCode(member);
		return super.count(criteriaQuery, null);
	}
	
	/** 会员已经过期的红包 */
	private CriteriaQuery<CouponCode> findOverdueCouponCode(Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, 
						criteriaBuilder.lessThanOrEqualTo(root.get("coupon").<Date>get("endDate"), new Date()),
						criteriaBuilder.equal(root.get("isUsed"), false),
						criteriaBuilder.equal(root.get("member"), member));
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}

	@Override
	public Long findAllCount(Member member) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.count(criteriaQuery, null);
	}

	@Override
	public Long findAllCount(Coupon coupon) {
		if (coupon == null) {
			return Long.valueOf(0);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("coupon"), coupon));
		return super.count(criteriaQuery, null);
	}

	@Override
	public List<CouponCode> findEnabledCouponCodeList(Member member,PaymentMethod paymentMethod) {
		if (member == null) 
			return new ArrayList<CouponCode>();
		if (paymentMethod == null) 
			return new ArrayList<CouponCode>();
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.and(
				criteriaBuilder.equal(root.get("member"), member), // 指定会员的
				criteriaBuilder.equal(root.get("coupon").get("isEnabled"), true), // 启用的
				criteriaBuilder.equal(root.get("isUsed"), false), // 没有使用的
				criteriaBuilder.lessThanOrEqualTo(root.get("coupon")
						.<Date> get("startDate"), new Date()), // 开始时间比现在早
				criteriaBuilder.greaterThanOrEqualTo(root.get("coupon")
						.<Date> get("endDate"), new Date()), // 结束时间比现在迟
				criteriaBuilder.or(
						criteriaBuilder.isNull(root.get("coupon").get("method")), 
						criteriaBuilder.equal(root.get("coupon").get("method"),paymentMethod.getMethod()))); // 支付方式
		criteriaQuery.where(restrictions);
		
		// 可用的红包
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public CouponCode findCouponCodeByCouponAndMember(Coupon coupon, Member member) {
		if (coupon == null) 
			return null;
		if (member == null) 
			return null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> createQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = createQuery.from(CouponCode.class);
		Predicate restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("member"), member),
				//criteriaBuilder.equal(root.get("isUsed"), false),
				criteriaBuilder.equal(root.get("coupon"), coupon));
		createQuery.where(restrictions);
		List<CouponCode> resultList = entityManager.createQuery(createQuery).getResultList();
		if(resultList==null || resultList.size()==0)
			return null;
		return resultList.get(0);
	}

	@Override
	public Page<CouponCode> findUsedCouponCodeByCoupon(Coupon coupon, Pageable pageable) {
		if (coupon == null) {
			return new Page<CouponCode>();
		} 
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.and(
						criteriaBuilder.equal(root.get("coupon"), coupon),
						criteriaBuilder.isNotNull(root.get("member")),
						criteriaBuilder.equal(root.get("isUsed"), true));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public List<CouponCode> findUsedCouponCodeByCoupon(Coupon coupon,Integer count) {
		if (coupon == null) {
			return new ArrayList<CouponCode>();
		}


		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date start = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);

		Date end = calendar.getTime();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.and(
				criteriaBuilder.equal(root.get("coupon"), coupon),
				criteriaBuilder.isNull(root.get("member")),
				criteriaBuilder.equal(root.get("isUsed"), false),

				criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("expire"), start), // 开始时间比现在早
				criteriaBuilder.lessThanOrEqualTo(root.<Date> get("expire"), end) // 结束时间比现在迟
);
		criteriaQuery.where(restrictions);
		//TypedQuery<CouponCode> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		//return query.getResultList();

		return super.findList(criteriaQuery, null, count, null, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<Tuple> findSendedCouponCodeByCoupon(Coupon coupon, Pageable pageable) {
		if (coupon == null) {
			return new Page<Tuple>();
		} 
		
		// select member,coupon,count(1) from xx_coupon_code group by coupon,member having member is not null;
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		
		criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("member")),criteriaBuilder.equal(root.get("coupon"), coupon)));
		criteriaQuery.groupBy(root.get("member"));
		
		criteriaQuery.select(criteriaBuilder.tuple(criteriaBuilder.count(root.get("code")),root.get("member"),root.get("coupon")));
		
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
		List<Tuple> result = typedQuery.getResultList();
		long total = result == null? 0 : result.size();
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			return new Page<Tuple>(Collections.<Tuple> emptyList(),total,pageable);
		}
		typedQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		typedQuery.setMaxResults(pageable.getPageSize());
		return new Page<Tuple>(typedQuery.getResultList(), total, pageable);
	}
	
	@Override
	public List<CouponCode> findCoupon(Member member,Coupon coupon) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(member!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if(coupon!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("coupon"), coupon));
		}
		criteriaQuery.where(restrictions);
		List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
		orderList.add(criteriaBuilder.desc(root.get("coupon").get("type")));
		orderList.add(criteriaBuilder.desc(root.get(OrderEntity.CREATE_DATE_PROPERTY_NAME)));
		criteriaQuery.orderBy(orderList);
		TypedQuery<CouponCode> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
		
	}


	/**
	 * 我推广的红包或者我核销的红包
	 * @param member
	 * @param type  0-推广		1-核销
     * @return
     */
	@Override
	public List<CouponCode> findCoupon(Member member,String type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if("0".equals(type)){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("guideMember"), member));
		}else if("1".equals(type)){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("operateMember"), member));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<CouponCode> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();

	}


	public Page<CouponCode> findCoupon(Member member,String type,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if("0".equals(type)){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("guideMember"), member));
		}else if("1".equals(type)){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("operateMember"), member));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<CouponCode> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		List<CouponCode> result = query.getResultList();
		long total = result == null? 0 : result.size();
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			return new Page<CouponCode>(Collections.<CouponCode> emptyList(),total,pageable);
		}
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		return new Page<CouponCode>(query.getResultList(), total, pageable);

	}

	@Override
	public List<CouponCode> findRedCouponCode(Member member,Coupon coupon,Boolean isUsed,Boolean isExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("coupon").get("type"), Coupon.Type.tenantBonus));
		if(member!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if(coupon!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("coupon"), coupon));
		}
		if(isUsed!=null){
			if(isUsed){
				restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("isUsed"),true));
			}else {
				restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.get("isUsed"),true));
			}
		}
		if (isExpired != null) {
			if (isExpired) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()) ));
			} else {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.isNull(root.<Date>get("expire")),criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		TypedQuery<CouponCode> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();

	}

	/**
	 * 优惠券统计
	 */
	public Page<CouponCode> sumerStatistics(Coupon coupon, Boolean isUsed , Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponCode> criteriaQuery = criteriaBuilder.createQuery(CouponCode.class);
		Root<CouponCode> root = criteriaQuery.from(CouponCode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (coupon != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("coupon"), coupon));
		}
		if (isUsed != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isUsed"), isUsed));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	
	
}