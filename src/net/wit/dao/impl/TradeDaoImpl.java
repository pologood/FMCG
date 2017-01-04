package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import net.wit.entity.*;
import net.wit.entity.Order;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TradeDao;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.QueryStatus;
import net.wit.entity.Order.ShippingStatus;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: www.insuper.com
 * </p>
 * <p>
 * Company: www.insuper.com
 * </p>
 * @author chenqifu
 * @version 1.0 Apr 3, 2013
 */

@Repository("tradeDaoImpl")
public class TradeDaoImpl extends BaseDaoImpl<Trade, Long> implements TradeDao {

	public Trade findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select trade from Trade trade where trade.sn = :sn";
		try {
			return entityManager.createQuery(jpql, Trade.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Trade findBySn(String sn, Tenant tenant) {
		if (sn == null) {
			return null;
		}
		String jpql = "select trade from Trade trade where trade.sn = :sn and trade.tenant=:tenant";
		try {
			return entityManager.createQuery(jpql, Trade.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).setParameter("tenant", tenant).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Page<Trade> order(Set<Trade> tradeSet, Tenant seller, Date begin_date, Date end_date, String keyword, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(tradeSet!=null){
			if(tradeSet.size()==0){
				return new Page<>(Collections.<Trade>emptyList(), 0, pageable);
			}
			restrictions = criteriaBuilder.and(restrictions, root.in(tradeSet));
		}
		if(seller!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("tenant"),seller));
		}else{
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		if(begin_date!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),begin_date));
		}
		if(end_date!=null){
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),end_date));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public List<Trade> findUnreviewList(Integer first, Integer count, Date overDate, List<Filter> filters, List<net.wit.Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan(root.<Date>get("confirmDate"), overDate));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery,first,count,filters,orders);
	}

	public Page<Trade> findPage(Pageable pageable, Member member, QueryStatus queryStatus, String keyword) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid)));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public long count(List<Filter> filters, Member member, QueryStatus queryStatus, String keyword) {
		if (member == null) {
			return 0;
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid)));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, filters);
	}
	public long count(List<Filter> filters, Tenant tenant, QueryStatus queryStatus, String keyword) {
		if (tenant == null) {
			return 0;
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid)));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, filters);
	}
	public long tradeyesterdayCount(List<Filter> filters, Tenant tenant, QueryStatus queryStatus, String keyword,Date begin_date,Date end_date) {
		if (tenant == null) {
			return 0;
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if(begin_date!=null){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"),begin_date));
		}
		if(end_date!=null){
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),end_date));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, filters);
	}
	public Page<Trade> findPage(Pageable pageable, Tenant tenant, QueryStatus queryStatus, String keyword) {
		if (tenant == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid)));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Pageable pageable, Tenant tenant, Date begin_date,Date end_date,QueryStatus queryStatus, String keyword) {
		if (tenant == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		if (begin_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), begin_date));
		}
		if (end_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), end_date));
		}

		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled)));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(criteriaBuilder.and( criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("order").get("paymentMethod").get("method"), PaymentMethod.Method.offline))));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Member member, OrderStatus orderStatus,PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
		}
		if (shippingStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), member.getTenant()));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Tenant tenant, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean returnStatus,String waitShip,Pageable pageable) {
		if (tenant == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
			if(waitShip.equals("wait")){
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
			if (paymentStatus.equals(PaymentStatus.unpaid)) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
		}
		if (shippingStatus != null) {
			if(shippingStatus.equals(ShippingStatus.unshipped)){
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
			}else{
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
			}
		}
		if (returnStatus!=null && returnStatus) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.like(root.get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Member member, String keyword, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired,Boolean unReview,Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
			if (paymentStatus.equals(PaymentStatus.unpaid)) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
		}
		if (shippingStatus != null) {
			if(shippingStatus.equals(ShippingStatus.unshipped)){
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
			}else{
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
			}
		}
		if (unReview!=null) {
			if(unReview)
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
			else
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword) ));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Member member, Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
		}
		if (shippingStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findMemberPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaQuery<Trade> criteriaQuery = findMemberCriteraBuilder(member, orderStatus, paymentStatus, shippingStatus, hasExpired);
		return super.findPage(criteriaQuery, pageable);
	}

	public Long findMemberCount(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<Trade> criteriaQuery = findMemberCriteraBuilder(member, orderStatus, paymentStatus, shippingStatus, hasExpired);
		return super.count(criteriaQuery, null);
	}

	/**
	 * @Title：findMemberCriteraBuilder
	 * @Description：
	 * @param member
	 * @param orderStatus
	 * @param paymentStatus
	 * @param shippingStatus
	 * @param hasExpired
	 * @return CriteriaQuery<Trade>
	 */
	private CriteriaQuery<Trade> findMemberCriteraBuilder(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
		}
		if (shippingStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}

	public Page<Trade> findPage(Member member, Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant").get("salesman"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Member member, QueryStatus queryStatus,Date beginDate, Date endDate,String keyword, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));

			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));

		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Member member, Date beginDate, Date endDate,String keyword, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));

			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")),criteriaBuilder.exists(subquery));

		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Trade> findPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public long countTenant(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("paymentStatus"), paymentStatus));
		}
		if (shippingStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("shippingStatus"), shippingStatus));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant").get("salesman"), member));
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public long count(Tenant tenant, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus,Boolean returnStatus, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
			if (paymentStatus.equals(PaymentStatus.unpaid)) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
		}
		if (shippingStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
		}
		if (returnStatus!=null && returnStatus) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public BigDecimal countSales(Tenant tenant,String queryDate) {
		if (tenant == null) {
			return BigDecimal.ZERO;
		}
		String jpql = "SELECT sum(IFNULL(t.freight,0)+IFNULL(t.offsetAmount,0)+IFNULL(t.promotionDiscount,0)+IFNULL(t.tax,0)+(SELECT sum(oi.price*oi.quantity) from OrderItem oi where oi.trade = t.id)) from Trade t where date_format(t.createDate,'%Y-%m') = :queryDate and t.tenant = :tenant" +
				" and orderStatus = :orderStatus and paymentStatus = :paymentStatus ";
		try {
			Double result = (Double)entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("queryDate", queryDate).setParameter("tenant", tenant)
					.setParameter("orderStatus", OrderStatus.completed).setParameter("paymentStatus", PaymentStatus.paid).getSingleResult();
			return BigDecimal.valueOf(result);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 查找会员代付款订单
	 */
	public Page<Trade> findWaitPay(Member member, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitPayCriteraBuilder(member, hasExpired);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * @Title：findWaitPayCriteraBuilder
	 * @Description：
	 * @param member
	 * @param hasExpired
	 * @return CriteriaQuery<Trade>
	 */
	private CriteriaQuery<Trade> findWaitPayCriteraBuilder(Member member, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(
				criteriaBuilder.and(restrictions,
						root.get("orderStatus").in(new Object[] { OrderStatus.confirmed, OrderStatus.unconfirmed }),
						criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid),
						criteriaBuilder.equal(root.get("order").get("member"), member))
		);
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}

	@Override
	public Long findWaitPayCount(Member member, Boolean hasExpired) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitPayCriteraBuilder(member, hasExpired);
		return super.count(criteriaQuery, null);
	}

	/**
	 * 查找会员待发货订单
	 */
	public Page<Trade> findWaitShipping(Member member, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitShippingCriteraBuilder(member, hasExpired);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * @Title：findWaitShippingCriteraBuilder
	 * @Description：
	 * @param member
	 * @param hasExpired
	 * @return CriteriaQuery<Trade>
	 */
	private CriteriaQuery<Trade> findWaitShippingCriteraBuilder(Member member, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();

		restrictions = criteriaBuilder.and(criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed)),
				criteriaBuilder.equal(root.get("order").get("member"), member));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));

		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}

	@Override
	public Long findWaitShippingCount(Member member, Boolean hasExpired) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitShippingCriteraBuilder(member, hasExpired);
		return super.count(criteriaQuery, null);
	}

	/**
	 * 查找会员待签收订单
	 */
	public Page<Trade> findWaitSign(Member member, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitSignCriteraBuilder(member, hasExpired);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * @Title：findWaitSignCriteraBuilder
	 * @Description：
	 * @param member
	 * @param hasExpired
	 * @return CriteriaQuery<Trade>
	 */
	private CriteriaQuery<Trade> findWaitSignCriteraBuilder(Member member, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed)), criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped),
				criteriaBuilder.equal(root.get("order").get("member"), member));

		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(root.join("tradeApplys", JoinType.LEFT)),
		// criteriaBuilder.notEqual(root.join("tradeApplys", JoinType.LEFT).get("status"), Status.submited)));
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}

	public Long findWaitSignCont(Member member, Boolean hasExpired) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitSignCriteraBuilder(member, hasExpired);
		return super.count(criteriaQuery, null);
	}

	/**
	 * 查找会员待评价订单
	 */
	public Page<Trade> findWaitReview(Member member, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitReviewCriteraBuilder(member, hasExpired);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * @Title：findWaitReviewCriteraBuilder
	 * @Description：
	 * @param member
	 * @param hasExpired
	 * @return CriteriaQuery<Trade>
	 */
	private CriteriaQuery<Trade> findWaitReviewCriteraBuilder(Member member, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed), criteriaBuilder.equal(root.get("order").get("member"), member)));

		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}

	/**
	 * 查找会员退货订单
	 */
	public Page<Trade> findRefund(Member member, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaQuery<Trade> criteriaQuery = findRefundCriteraBuilder(member, hasExpired);
		return super.findPage(criteriaQuery, pageable);
	}

	public Long findRefundCount(Member member, Boolean hasExpired) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<Trade> criteriaQuery = findRefundCriteraBuilder(member, hasExpired);
		return super.count(criteriaQuery, null);
	}

	/**
	 * @Title：findRefundCriteraBuilder
	 * @Description：
	 * @param member
	 * @param hasExpired
	 * @return CriteriaQuery<Trade>
	 */
	private CriteriaQuery<Trade> findRefundCriteraBuilder(Member member, Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions,
				criteriaBuilder.or(root.get("paymentStatus").in(new Object[] {PaymentStatus.partialRefunds,PaymentStatus.refunded,PaymentStatus.refundApply}),
						root.get("shippingStatus").in(new Object[] {ShippingStatus.partialReturns,ShippingStatus.returned,ShippingStatus.shippedApply})),
				criteriaBuilder.equal(root.get("order").get("member"), member));
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return criteriaQuery;
	}

	public Long findWaitReviewCount(Member member, Boolean hasExpired) {
		if (member == null) {
			return Long.valueOf(0);
		}
		CriteriaQuery<Trade> criteriaQuery = findWaitReviewCriteraBuilder(member, hasExpired);
		return super.count(criteriaQuery, null);
	}

	public Page<Trade> findCustomer(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
		if (member == null) {
			return new Page<Trade>(Collections.<Trade> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
		}
		if (shippingStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.get("order").<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public List<Trade> getTradList(ShippingStatus shippingStatus,Date overDate,Integer first,Integer count) {
		String jpql = "select trade from Trade trade where trade.orderStatus = :orderStatus and trade.shippingStatus = :shippingStatus and trade.shippingDate <= :overDate ";
		TypedQuery query=entityManager.createQuery(jpql, Trade.class).setParameter("orderStatus", OrderStatus.confirmed).setParameter("shippingStatus", shippingStatus).setParameter("overDate", overDate);
		if(first!=null){
			query.setFirstResult(first);
		}
		if(count!=null){
			query.setMaxResults(count);
		}
		List<Trade> trades = query.getResultList();
		return trades;
	}

	public List<Trade> getTradList(Date confirmDate) {
		List<Trade> trades = new ArrayList<Trade>();
		try{
			String jpql = "select trade from Trade trade where trade.orderStatus = :orderStatus and trade.clearing = :isClearing and trade.paymentStatus = :paymentStatus and trade.confirmDate <= :overDate ";
			trades = entityManager.createQuery(jpql, Trade.class).setParameter("orderStatus", OrderStatus.completed).setParameter("isClearing", Boolean.FALSE).setParameter("paymentStatus", PaymentStatus.paid).setParameter("overDate", confirmDate).getResultList();
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		return trades;
	}

	@Override
	public List<Trade> findTradeListByTenants(List<Tenant> tenants) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, root.get("tenant").in(tenants));
		criteriaQuery.where(restrictions);
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}

	@Override
	public List<Trade> findList(Tenant tenant, OrderStatus orderStatus) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		}
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}

	public List<Trade> findListByExport(Member member, Date beginDate, Date endDate, String keyword,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));

			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")),criteriaBuilder.exists(subquery));

		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		criteriaQuery.where(restrictions);
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	public List<Trade> findListByExport(Member member, QueryStatus queryStatus,Date beginDate, Date endDate, String keyword,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));

			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")),criteriaBuilder.exists(subquery));

		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order").get("member"), member));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	public List<Trade> findTradeByTenant(Tenant tenant, Date beginDate, Date endDate, String keywords,Boolean status,String type){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if("order_total".equals(type)){
			if (beginDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("shippingDate"), beginDate));
			}
			if (endDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("shippingDate"), endDate));
			}
		}else{
			if (beginDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
			}
			if (endDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
			}
		}

		if (StringUtils.isNotBlank(keywords)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keywords+"%"));

			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keywords), criteriaBuilder.equal(root.<String> get("sn"), keywords),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keywords + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keywords + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keywords + "%")));

		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		if("order_total".equals(type)){
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.notEqual(root.get("shippingStatus"), Order.ShippingStatus.unshipped));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
					criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.completed),
					criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.confirmed)
			));
		}else if("order_settle".equals(type)){
			if(status!=null){
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<String> get("suppliered"), status));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled)));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(criteriaBuilder.and( criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped)
					,criteriaBuilder.equal(root.get("order").get("paymentMethod").get("method"), PaymentMethod.Method.offline))));
		}


		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}

	public Page<Trade> findFavorableList(Tenant tenant,String keyword,Coupon coupon, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));

			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));

		}
		if(tenant!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));
		}
		if(coupon!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenantCouponCode").get("coupon"),coupon));
		}else{
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.get("tenantCouponCode")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public Page<Trade> findTradeTotal(Tenant tenant, QueryStatus queryStatus,Date beginDate, Date endDate,String keyword,Coupon coupon, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("order").<Date> get("expire"), new Date())));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}
		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			Subquery<Trade> subquery=criteriaQuery.subquery(Trade.class);
			Root<Trade> subqueryRoot=subquery.from(Trade.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot,root),criteriaBuilder.like(subqueryRoot.join("orderItems").<String>get("fullName"),"%"+keyword+"%"));

			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.<String> get("sn"), keyword),
							criteriaBuilder.exists(subquery),
							criteriaBuilder.like(root.get("tenant").<String> get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.get("order").<String> get("consignee"), "%" + keyword + "%"),
							criteriaBuilder.like(root.get("order").<String> get("phone"), "%" + keyword + "%")));

		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("shippingDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("shippingDate"), endDate));
		}
		if(tenant!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));
		}
		if(coupon!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenantCouponCode").get("coupon"),coupon));
		}
		restrictions = criteriaBuilder.and(restrictions,
				criteriaBuilder.notEqual(root.get("shippingStatus"), Order.ShippingStatus.unshipped));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
				criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.completed),
				criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.confirmed)
		));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public Long  findTradeYesterdayTotal(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,String begin_date,String end_date){
		String sql = "SELECT SUM(sale_amount) from"+
				" (select sum(oi.quantity*p.price) as sale_amount ,oi.trades from xx_order_item oi left join xx_product p on oi.product=p.id  GROUP BY oi.product)  w"+
				" left join xx_trade t on w.trades=t.id"+
				" where 1=1";

		if (begin_date != null) {
			sql += "  AND t.create_date>= :begin_date";
		}
		if (end_date != null) {
			sql+=" AND t.create_date< :end_date";
		}
		if (shippingStatus == null) {
			sql += "  AND (t.shipping_status='2' or t.shipping_status='5')";
		}
		if (tenant != null) {
			sql += "  AND t.tenant=:tenant";
		}
		Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
		if (begin_date != null) {
			query.setParameter("begin_date", begin_date);
		}
		if (end_date != null) {
			query.setParameter("end_date", end_date);
		}
		if(query.getSingleResult()!=null){
			return Long.parseLong(query.getSingleResult().toString());
		}else{
			return 0l;
		}

	}

	public List<Trade> sevenDayTradingList(List<Filter> filters, Tenant tenant,ShippingStatus shippingStatus, String keyword,Date begin_date,Date end_date){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(begin_date!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("shippingDate"),begin_date));
		}
		if(end_date!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("shippingDate"),end_date));
		}
		if(tenant!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));
		}
		if(shippingStatus!=null){
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.notEqual(root.get("shippingStatus"), Order.ShippingStatus.unshipped));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
					criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.completed),
					criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.confirmed)
			));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	public List<Trade> findUnshippedList(Tenant tenant){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		//已确认并且已付款并且未发货订单
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	public List<Trade> findUnshippedListExport(Tenant tenant,QueryStatus queryStatus){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("print"), 0));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("print"), 0),criteriaBuilder.isNull(root.get("print"))));
		if (queryStatus != null) {
			if (queryStatus.equals(QueryStatus.unshipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.unshipped),criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.partialShipment)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.paid)));
			}
			if (queryStatus.equals(QueryStatus.unpaid) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.unpaid));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed));
			}
			if (queryStatus.equals(QueryStatus.shipped) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shipped));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed));
			}
			if (queryStatus.equals(QueryStatus.unrefunded) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("shippingStatus"), ShippingStatus.shippedApply),criteriaBuilder.equal(root.get("paymentStatus"), PaymentStatus.refundApply)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.confirmed),criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.unconfirmed)));
			}
			if (queryStatus.equals(QueryStatus.reviewed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.unreview) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.join("reviews", JoinType.LEFT)));
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
			if (queryStatus.equals(QueryStatus.cancelled) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.cancelled));
			}
			if (queryStatus.equals(QueryStatus.completed) ) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.completed));
			}
		}

		criteriaQuery.where(restrictions);
		TypedQuery<Trade> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}

	public Page<Trade> findPage(String consignee, Area area, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate, String keyword,String tenantName,String userName, Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
		Root<Trade> root = criteriaQuery.from(Trade.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (consignee!=null&&!"".equals(consignee)) { // 拼音条件
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("order").<String> get("consignee"), consignee));
		}
		if (tenantName!=null&&!"".equals(tenantName)) { // 拼音条件
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("tenant").<String> get("name"), tenantName));
		}
		if (userName!=null&&!"".equals(userName)) { // 拼音条件
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("order").get("member").<String> get("username"), userName));
		}
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("order").get("area"), area), criteriaBuilder.like(root.get("order").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		if (orderStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (paymentStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
		}
		if (shippingStatus != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
		}
		if (hasExpired != null) {
			if (hasExpired) {
				restrictions = criteriaBuilder.and(restrictions, root.get("order").get("expire").isNotNull(), criteriaBuilder.lessThan(root.<Date> get("expire"), new Date()));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("order").get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("expire"), new Date())));
			}
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}

		if (StringUtils.isNotBlank(keyword)) { // 拼音条件
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.like(root.get("order").<String> get("sn"), "%" + keyword + "%")));
		}

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
}
