/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.OrderItemDao;
import net.wit.entity.Order;
import net.wit.entity.OrderItem;
import net.wit.entity.Tenant;

/**
 * Dao - 订单项
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("orderItemDaoImpl")
public class OrderItemDaoImpl extends BaseDaoImpl<OrderItem, Long> implements OrderItemDao {
	public Page<OrderItem> findPage(Boolean status,Date start_date,Date end_date,Tenant tenant,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderItem> criteriaQuery = criteriaBuilder.createQuery(OrderItem.class);
		Root<OrderItem> root = criteriaQuery.from(OrderItem.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("suppliered"), status));
		}
		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), tenant));
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
	public Page<OrderItem> openPage(Date start_date,Date end_date,Tenant tenant,String keywords,Pageable pageable){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderItem> criteriaQuery = criteriaBuilder.createQuery(OrderItem.class);
		Root<OrderItem> root = criteriaQuery.from(OrderItem.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), tenant));
		}
		if (start_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("trade").<Date> get("shippingDate"), start_date));
		}
		if (end_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("trade").<Date> get("shippingDate"), end_date));
		}
		if (StringUtils.isNotBlank(keywords)) { // 拼音条件
			restrictions = criteriaBuilder.and(restrictions,
				criteriaBuilder.or(
					criteriaBuilder.like(root.get("trade").get("order").<String> get("sn"), "%" + keywords + "%")
					,criteriaBuilder.like(root.<String> get("sn"), "%" + keywords + "%")
					,criteriaBuilder.like(root.<String> get("fullName"), "%" + keywords + "%")
					,criteriaBuilder.like(root.get("supplier").<String> get("name"), "%" + keywords + "%")
					)
			);
		}
		restrictions = criteriaBuilder.and(restrictions, 
				criteriaBuilder.notEqual(root.get("trade").get("shippingStatus"), Order.ShippingStatus.unshipped));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
				criteriaBuilder.equal(root.get("trade").get("orderStatus"), Order.OrderStatus.completed),
				criteriaBuilder.equal(root.get("trade").get("orderStatus"), Order.OrderStatus.confirmed)
				));

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}
	public List<OrderItem> openList(Date start_date,Date end_date,Tenant tenant,String keywords){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderItem> criteriaQuery = criteriaBuilder.createQuery(OrderItem.class);
		Root<OrderItem> root = criteriaQuery.from(OrderItem.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("tenant"), tenant));
		}
		if (start_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("trade").<Date> get("shippingDate"), start_date));
		}
		if (end_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("trade").<Date> get("shippingDate"), end_date));
		}
		if (StringUtils.isNotBlank(keywords)) { // 拼音条件
			restrictions = criteriaBuilder.and(restrictions,
				criteriaBuilder.or(
					criteriaBuilder.like(root.get("trade").get("order").<String> get("sn"), "%" + keywords + "%")
					,criteriaBuilder.like(root.<String> get("sn"), "%" + keywords + "%")
					,criteriaBuilder.like(root.<String> get("fullName"), "%" + keywords + "%")
					,criteriaBuilder.like(root.get("supplier").<String> get("name"), "%" + keywords + "%")
					)
			);
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("trade").get("shippingStatus"), Order.ShippingStatus.unshipped));

		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
				criteriaBuilder.equal(root.get("trade").get("orderStatus"), Order.OrderStatus.completed),
				criteriaBuilder.equal(root.get("trade").get("orderStatus"), Order.OrderStatus.confirmed)
				));
		criteriaQuery.where(restrictions);
		TypedQuery<OrderItem> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	public List<OrderItem> orderSettle(Boolean status,Date start_date,Date end_date,Tenant tenant){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderItem> criteriaQuery = criteriaBuilder.createQuery(OrderItem.class);
		Root<OrderItem> root = criteriaQuery.from(OrderItem.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("trade").get("suppliered"), status));
		}
		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), tenant));
		}
		if (start_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), start_date));
		}
		if (end_date != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), end_date));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<OrderItem> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		return query.getResultList();
	}
	public Page<Map<String, Object>> openPageGroupBy(Date start_date,Date end_date,Tenant tenant,String keywords,Pageable pageable){
		String sql = "SELECT case WHEN t.shipping_date is NULL THEN '--' ELSE t.shipping_date END AS dat," +
			                "  oi.sn," +
			                "  oi.barcode," +
			                "  oi.full_name," +
			                "  oi.price," +
			                "  oi.cost," +
			                "  oi.is_gift," +
			                "  oi.packag_unit_name," +
			                "  oi.supplier," +
			                "  sum(oi.shipped_quantity) shippedQuantitiy," +
			                "  sum(oi.return_quantity) returnQuantity," +
			                "  sum(oi.shipped_quantity*oi.price) pirceTotal," +
			                "  sum(oi.shipped_quantity*oi.cost) costTotal," +
			                "  sum(oi.return_quantity*oi.price) rePriceTotal," +
			                "  sum(oi.return_quantity*oi.cost) reCostTotal," +
                            "  oi.create_date" +
			                "  from xx_order_item oi" +
			                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
			                "  where t.tenant=:tenant";
			               
        String totalSql = "SELECT count(1) total from"
        		+ "	(select 1 from xx_order_item oi" 
        		+ " LEFT JOIN xx_trade t on oi.trades=t.id" 
        		+ " where t.tenant=:tenant";
        
        if (start_date != null) {
            sql += "  AND t.shipping_date>:start_date";
            totalSql += "  AND t.shipping_date>:start_date";
        }
        if (end_date != null) {
            sql += "  AND t.shipping_date<:end_date";
            totalSql += "  AND t.shipping_date<:end_date";
        }       			
        if (StringUtils.isNotBlank(keywords)) {
            sql += " AND (oi.barcode LIKE :keywords OR oi.full_name LIKE :keywords OR oi.sn LIKE :keywords)";
            totalSql += " AND (oi.barcode LIKE :keywords OR oi.full_name LIKE :keywords OR oi.sn LIKE :keywords)";
        }
        
        sql+=" AND (t.order_status=1 OR t.order_status=2) AND t.shipping_status!=0";
        totalSql += " AND (t.order_status=1 OR t.order_status=2) AND t.shipping_status!=0";
        
        sql += "  GROUP BY oi.sn";
        totalSql += "  GROUP BY oi.sn) u";
        
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        if (StringUtils.isNotBlank(keywords)) {
            query.setParameter("keywords", "%" + keywords + "%");
            totalQuery.setParameter("keywords", "%" + keywords + "%");
        }
        if (start_date != null) {
            query.setParameter("start_date", start_date);
            totalQuery.setParameter("start_date", start_date);
        }
        if (end_date != null) {
            query.setParameter("end_date", end_date);
            totalQuery.setParameter("end_date", end_date);
        }
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List list = new ArrayList();
        Long total = 0l;
        try {
            list = query.getResultList();
            total = Long.parseLong(totalQuery.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
        if (totalPages < pageable.getPageNumber()) {
            return new Page<Map<String, Object>>(Collections.<Map<String, Object>>emptyList(), total, pageable);
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        if (list.size() > 0) {
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("shipping_date", row[0]);
                map.put("sn", row[1]);
                map.put("barcode", row[2]);
                map.put("fullName", row[3]);
                map.put("price", row[4]);
                map.put("cost", row[5]);
                map.put("isGift", row[6]);
                map.put("unit", row[7]);
                map.put("supplier", row[8]);
                map.put("shippedQuantity", row[9]);
                map.put("returnQuantity", row[10]);
                map.put("totalPrice", row[11]);
                map.put("totalCost", row[12]);
                map.put("reTotalPrice", row[13]);
                map.put("reTotalCost", row[14]);
                map.put("create_date", row[15]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
	}
	
	public List<Map<String, Object>> openListGroupBy(Date start_date,Date end_date,Tenant tenant,String keywords){
		String sql = "SELECT case WHEN t.shipping_date is NULL THEN '--' ELSE t.shipping_date END AS dat," +
                "  oi.sn," +
                "  oi.barcode," +
                "  oi.full_name," +
                "  oi.price," +
                "  oi.cost," +
                "  oi.is_gift," +
                "  oi.packag_unit_name," +
                "  oi.supplier," +
                "  sum(oi.shipped_quantity) shippedQuantitiy," +
                "  sum(oi.return_quantity) returnQuantity," +
                "  sum(oi.shipped_quantity*oi.price) pirceTotal," +
                "  sum(oi.shipped_quantity*oi.cost) costTotal," +
                "  sum(oi.return_quantity*oi.price) rePriceTotal," +
                "  sum(oi.return_quantity*oi.cost) reCostTotal," +
                "  oi.create_date" +
                "  from xx_order_item oi" +
                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
                "  where t.tenant=:tenant";
           
		if (start_date != null) {
			sql += "  AND t.shipping_date>:start_date";
		}
		if (end_date != null) {
			sql += "  AND t.shipping_date<:end_date";
		}       			
		if (StringUtils.isNotBlank(keywords)) {
			sql += " AND (oi.barcode LIKE :keywords OR oi.full_name LIKE :keywords OR oi.sn LIKE :keywords)";
		}
		
		sql += " AND (t.order_status=1 OR t.order_status=2) AND t.shipping_status!=0";
		
		sql += "  GROUP BY oi.sn";
		Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
		if (StringUtils.isNotBlank(keywords)) {
			query.setParameter("keywords", "%" + keywords + "%");
		}
		if (start_date != null) {
			query.setParameter("start_date", start_date);
		}
		if (end_date != null) {
			query.setParameter("end_date", end_date);
		}
		List list = new ArrayList();
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> maps = new ArrayList<>();
		if (list.size() > 0) {
			for (Object obj : list) {
			    Map<String, Object> map = new HashMap<>();
			    Object[] row = (Object[]) obj;
			    map.put("shipping_date", row[0]);
			    map.put("sn", row[1]);
			    map.put("barcode", row[2]);
			    map.put("fullName", row[3]);
			    map.put("price", row[4]);
			    map.put("cost", row[5]);
			    map.put("isGift", row[6]);
			    map.put("unit", row[7]);
			    map.put("supplier", row[8]);
			    map.put("shippedQuantity", row[9]);
			    map.put("returnQuantity", row[10]);
			    map.put("totalPrice", row[11]);
			    map.put("totalCost", row[12]);
			    map.put("reTotalPrice", row[13]);
			    map.put("reTotalCost", row[14]);
                map.put("create_date", row[15]);
			    maps.add(map);
			}
		}
		return maps;
	}	
}