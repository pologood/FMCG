package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Promotion;
import net.wit.entity.PromotionMember;
import net.wit.entity.Promotion.Type;
import net.wit.entity.PromotionMember.Status;
import net.wit.entity.PromotionProduct;

/**
 * @ClassName: PromotionMemberDaoImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年4月28日 上午10:36:32
 */
@Repository("promotionProductDao")
public class PromotionProductDaoImpl extends BaseDaoImpl<PromotionProduct, Long>{

	public Page<PromotionProduct> findPage(Promotion promotion, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PromotionProduct> criteriaQuery = criteriaBuilder.createQuery(PromotionProduct.class);
		Root<PromotionProduct> root = criteriaQuery.from(PromotionProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Promotion> get("promotion"), promotion));

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<PromotionProduct> findPage(Type type,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PromotionProduct> criteriaQuery = criteriaBuilder.createQuery(PromotionProduct.class);
		Root<PromotionProduct> root = criteriaQuery.from(PromotionProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("promotion").get("type"), type));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public PromotionProduct getPromotionMemberBysn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select pm from PromotionMember pm where pm.sn = :sn";
		try {
			return entityManager.createQuery(jpql, PromotionProduct.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
