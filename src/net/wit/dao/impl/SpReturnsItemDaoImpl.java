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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.SpReturns;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SpReturnsItemDao;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.SpReturnsItem;
import net.wit.entity.Tenant;

/**
 * Created by WangChao on 2016-4-12.
 */

@Repository("spReturnsItemDaoImpl")
public class SpReturnsItemDaoImpl extends BaseDaoImpl<SpReturnsItem, Long> implements SpReturnsItemDao {

	public List<Map<String,Object>> returnProductTotalExp(Tenant tenant, Tenant supplier, Date start_date, Date end_date, Boolean status, String keywords){
        String sql = "SELECT ri.create_date," +
                "  oi.sn," +
                "  oi.barcode," +
                "  oi.full_name," +
                "  oi.price," +
                "  oi.cost," +
                "  oi.is_gift," +
                "  oi.packag_unit_name," +
                "  oi.supplier," +
                "  sum(ri.shipped_quantity) shippedQuantitiy," +
                "  sum(ri.return_quantity) returnQuantity," +
                "  sum(ri.shipped_quantity*oi.price) pirceTotal," +
                "  sum(ri.shipped_quantity*oi.cost) costTotal," +
                "  sum(ri.return_quantity*oi.price) rePriceTotal," +
                "  sum(ri.return_quantity*oi.cost) reCostTotal," +
                "  r.completed_date" +
                "  from xx_sp_returns_item ri" +
                "  LEFT JOIN xx_sp_returns r on ri.returns=r.id" +
                "  LEFT JOIN xx_order_item oi on oi.id=ri.order_item" +
                "  LEFT JOIN xx_trade t on t.id=r.trade" +
                "  where t.tenant=:tenant";

        if (start_date != null) {
            sql += "  AND r.completed_date>:start_date";
        }
        if (end_date != null) {
            sql += "  AND r.completed_date<:end_date";
        }
        if (StringUtils.isNotBlank(keywords)) {
            sql += " AND (oi.barcode LIKE :keywords OR oi.full_name LIKE :keywords OR oi.sn LIKE :keywords)";
        }

        sql += " AND r.return_status=3 ";

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
                map.put("create_date", row[0]);
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
                map.put("completed_date", row[15]);
                maps.add(map);
            }
        }
        return maps;
	}
	public Page<Map<String,Object>> returnProductTotal(Tenant tenant, Tenant supplier,Date start_date,Date end_date,ReturnStatus returnStatus,Boolean status,String keywords,Pageable pageable){
        String sql = "SELECT ri.create_date," +
                "  oi.sn," +
                "  oi.barcode," +
                "  oi.full_name," +
                "  oi.price," +
                "  oi.cost," +
                "  oi.is_gift," +
                "  oi.packag_unit_name," +
                "  oi.supplier," +
                "  sum(ri.shipped_quantity) shippedQuantitiy," +
                "  sum(ri.return_quantity) returnQuantity," +
                "  sum(ri.shipped_quantity*oi.price) pirceTotal," +
                "  sum(ri.shipped_quantity*oi.cost) costTotal," +
                "  sum(ri.return_quantity*oi.price) rePriceTotal," +
                "  sum(ri.return_quantity*oi.cost) reCostTotal," +
                "  r.completed_date" +
                "  from xx_sp_returns_item ri" +
                "  LEFT JOIN xx_sp_returns r on ri.returns=r.id" +
                "  LEFT JOIN xx_order_item oi on oi.id=ri.order_item" +
                "  LEFT JOIN xx_trade t on t.id=r.trade" +
                "  where t.tenant=:tenant";

        String totalSql = "SELECT count(1) total from"
                + "	(select 1 from xx_sp_returns_item ri"
                +"  LEFT JOIN xx_sp_returns r on ri.returns=r.id"
                +"  LEFT JOIN xx_order_item oi on oi.id=ri.order_item"
                +"  LEFT JOIN xx_trade t on t.id=r.trade"
                +"  where t.tenant=:tenant";

        if (start_date != null) {
            sql += "  AND r.completed_date>:start_date";
            totalSql += "  AND r.completed_date>:start_date";
        }
        if (end_date != null) {
            sql += "  AND r.completed_date<:end_date";
            totalSql += " AND r.completed_date<:end_date";
        }
        if (StringUtils.isNotBlank(keywords)) {
            sql += " AND (oi.barcode LIKE :keywords OR oi.full_name LIKE :keywords OR oi.sn LIKE :keywords)";
            totalSql += " AND (oi.barcode LIKE :keywords OR oi.full_name LIKE :keywords OR oi.sn LIKE :keywords)";
        }

        sql+=" AND r.return_status=3 ";
        totalSql += " AND r.return_status=3";

        sql += "  GROUP BY ri.sn";
        totalSql += "  GROUP BY ri.sn) u";

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
                map.put("create_date", row[0]);
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
                map.put("completed_date", row[15]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
	}
    public Page<SpReturnsItem> findPageByTenant(Tenant tenant, Tenant supplier, Date start_date, Date end_date, ReturnStatus returnStatus, Boolean status, String keyword, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpReturnsItem> criteriaQuery = criteriaBuilder.createQuery(SpReturnsItem.class);
        Root<SpReturnsItem> root = criteriaQuery.from(SpReturnsItem.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("trade").get("tenant"), tenant));
        }
        if (supplier != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("supplier"), supplier));
        }
        if (returnStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("returnStatus"), returnStatus ));
        }
        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("suppliered"), status ));
        }
        if (start_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("returns").<Date>get("completedDate"), start_date));
        }
        if (end_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("returns").<Date>get("completedDate"), end_date));
        }
        if (StringUtils.isNotBlank(keyword)) { // 拼音条件
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("returns").get("trade").get("order").<String> get("sn"), keyword), criteriaBuilder.equal(root.get("returns").<String> get("sn"), keyword),
                            criteriaBuilder.like(root.get("returns").get("trade").get("order").get("member").<String> get("username"), "%" + keyword + "%"), criteriaBuilder.like(root.get("returns").get("trade").get("order").<String> get("consignee"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("returns").get("trade").get("order").<String> get("phone"), "%" + keyword + "%")));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("returnStatus"), ReturnStatus.completed));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(root.<Integer> get("returnQuantity"), 0));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
    public List<SpReturnsItem> findByTenant(Tenant tenant, Tenant supplier,Date start_date,Date end_date,Boolean status,String keywords){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpReturnsItem> criteriaQuery = criteriaBuilder.createQuery(SpReturnsItem.class);
        Root<SpReturnsItem> root = criteriaQuery.from(SpReturnsItem.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (start_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("returns").<Date>get("completedDate"), start_date));
        }
        if (end_date != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.get("returns").<Date>get("completedDate"), end_date));
        }
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("trade").get("tenant"), tenant));
        }
        if (supplier != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("supplier"), supplier));
        }
        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("suppliered"), status ));
        }
        if (StringUtils.isNotBlank(keywords)) { // 拼音条件
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("returns").get("trade").get("order").<String> get("sn"), keywords), criteriaBuilder.equal(root.get("returns").<String> get("sn"), keywords),
                            criteriaBuilder.like(root.get("returns").get("trade").get("tenant").<String> get("name"), "%" + keywords + "%"), criteriaBuilder.like(root.get("returns").get("trade").get("order").<String> get("consignee"), "%" + keywords + "%"),
                            criteriaBuilder.like(root.get("returns").get("trade").get("order").<String> get("phone"), "%" + keywords + "%")));

        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("returns").get("returnStatus"), ReturnStatus.completed));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(root.<Integer> get("returnQuantity"), 0));
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("returns").get("createDate")));
        TypedQuery<SpReturnsItem> query=entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }
}
