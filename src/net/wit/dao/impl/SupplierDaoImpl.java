package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SupplierDao;
import net.wit.entity.Tenant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by WangChao on 2016-4-12.
 */
@Repository("supplierDaoImpl")
public class SupplierDaoImpl extends BaseDaoImpl<Object, Long> implements SupplierDao{
    //销售统计
    public Page<Map<String, Object>> saleStatisticsList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable) {
        String sql = "SELECT oi.create_date," +
                "  p.full_name," +
                "  p.barcode," +
                "  (SELECT te2.name from xx_tenant te2 where te2.id=t.tenant) seller," +
                "  oi.packag_unit_name," +
                "  sum(oi.quantity) quantities," +
                "  p.price," +
                "  sum(oi.quantity)*p.price sale_amount," +
                "  p.cost," +
                "  sum(oi.quantity)*p.cost settlement_amount" +
                "  from xx_order_item oi" +
                "  LEFT JOIN xx_product p on oi.product=p.id" +
                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
                "  where t.order_status in(1,2) and oi.supplier=:tenant";

        String totalSql = "SELECT count(1) total" +
                " FROM (select 1 from xx_order_item oi" +
                " LEFT JOIN xx_product p on oi.product=p.id" +
                " LEFT JOIN xx_trade t on oi.trades=t.id" +
                " where t.order_status in(1,2) and oi.supplier=:tenant";

        if (seller != null) {
            sql += " AND t.tenant=:seller";
            totalSql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(keyWords)) {
            sql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
            totalSql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
        }
        if (begin_date != null) {
            sql += "  AND oi.create_date>:begin_date";
            totalSql += "  AND oi.create_date>:begin_date";
        }
        if (end_date != null) {
            sql += "  AND oi.create_date<:end_date";
            totalSql += "  AND oi.create_date<:end_date";
        }
        sql += "  GROUP BY oi.product";
        totalSql += "  GROUP BY oi.product) su";

        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());

        if (seller != null) {
            query.setParameter("seller", seller.getId());
            totalQuery.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(keyWords)) {
            query.setParameter("keyWords", "%" + keyWords + "%");
            totalQuery.setParameter("keyWords", "%" + keyWords + "%");
        }
        if (begin_date != null) {
            query.setParameter("begin_date", begin_date);
            totalQuery.setParameter("begin_date", begin_date);
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
                map.put("full_name", row[1]);
                map.put("barcode", row[2]);
                map.put("seller", row[3]);
                map.put("packag_unit_name", row[4]);
                map.put("quantities", row[5]);
                map.put("price", row[6]);
                map.put("sale_amount", row[7]);
                map.put("cost", row[8]);
                map.put("settlement_amount", row[9]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }
  //销售统计
    public List<Map<String, Object>> saleTotal(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        String sql = "SELECT oi.create_date," +
                "  p.full_name," +
                "  p.barcode," +
                "  (SELECT te2.name from xx_tenant te2 where te2.id=t.tenant) seller," +
                "  oi.packag_unit_name," +
                "  sum(oi.quantity) quantities," +
                "  p.price," +
                "  sum(oi.quantity)*p.price sale_amount," +
                "  p.cost," +
                "  sum(oi.quantity)*p.cost settlement_amount" +
                "  from xx_order_item oi" +
                "  LEFT JOIN xx_product p on oi.product=p.id" +
                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
                "  where t.order_status in(1,2) and oi.supplier=:tenant";
        if (seller != null) {
            sql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(keyWords)) {
            sql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
        }
        if (begin_date != null) {
            sql += "  AND oi.create_date>:begin_date";
        }
        if (end_date != null) {
            sql += "  AND oi.create_date<:end_date";
        }
        sql += "  GROUP BY oi.product";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        
        if (seller != null) {
            query.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(keyWords)) {
            query.setParameter("keyWords", "%" + keyWords + "%");
        }
        if (begin_date != null) {
            query.setParameter("begin_date", begin_date);
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
                map.put("full_name", row[1]);
                map.put("barcode", row[2]);
                map.put("seller", row[3]);
                map.put("packag_unit_name", row[4]);
                map.put("quantities", row[5]);
                map.put("price", row[6]);
                map.put("sale_amount", row[7]);
                map.put("cost", row[8]);
                map.put("settlement_amount", row[9]);
                maps.add(map);
            }
        }
        return maps;
    }

    //累计销售金额
    public BigDecimal totalSaleAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        String sql = "SELECT sum(oi.quantity*p.price) total_sale_amount" +
                "  FROM xx_order_item oi" +
                "  LEFT JOIN xx_product p ON oi.product=p.id" +
                "  LEFT JOIN xx_trade t ON oi.trades=t.id" +
                "  WHERE t.order_status IN(1,2) AND oi.supplier=:tenant";
        if (seller == null) {
            return BigDecimal.ZERO;
        }else{
            sql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(keyWords)) {
            sql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
        }
        if (begin_date != null) {
            sql += "  AND oi.create_date>:begin_date";
        }
        if (end_date != null) {
            sql += "  AND oi.create_date<:end_date";
        }
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        if (seller != null) {
            query.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(keyWords)) {
            query.setParameter("keyWords", "%" + keyWords + "%");
        }
        if (begin_date != null) {
            query.setParameter("begin_date", begin_date);
        }
        if (end_date != null) {
            query.setParameter("end_date", end_date);
        }
        BigDecimal total_sale_amount = (BigDecimal) query.getSingleResult();
        return total_sale_amount==null?BigDecimal.ZERO:total_sale_amount;
    }

    //累计结算金额
    public BigDecimal totalSettlementAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        String sql = "SELECT sum(oi.quantity*p.cost) total_settlement_amount" +
                "  from xx_order_item oi" +
                "  LEFT JOIN xx_product p on oi.product=p.id" +
                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
                "  where t.order_status in(1,2) and oi.supplier=:tenant";
        if (seller == null) {
            return BigDecimal.ZERO;
        }else{
            sql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(keyWords)) {
            sql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
        }
        if (begin_date != null) {
            sql += "  AND oi.create_date>:begin_date";
        }
        if (end_date != null) {
            sql += "  AND oi.create_date<:end_date";
        }
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        if (seller != null) {
            query.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(keyWords)) {
            query.setParameter("keyWords", "%" + keyWords + "%");
        }
        if (begin_date != null) {
            query.setParameter("begin_date", begin_date);
        }
        if (end_date != null) {
            query.setParameter("end_date", end_date);
        }
        BigDecimal total_settlement_amount = (BigDecimal) query.getSingleResult();
        return total_settlement_amount==null?BigDecimal.ZERO:total_settlement_amount;
    }

    //销售明细
    public Page<Map<String, Object>> saleDetailList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable) {
        String sql = "SELECT oi.create_date," +
                "  p.full_name," +
                "  p.barcode," +
                "  (SELECT te2.name from xx_tenant te2 where te2.id=t.tenant) seller," +
                "  (SELECT m.username FROM xx_member m where m.id=o.member) username," +
                "  oi.packag_unit_name," +
                "  oi.quantity," +
                "  p.price," +
                "  oi.quantity*p.price sale_amount," +
                "  p.cost," +
                "  oi.quantity*p.cost settlement_amount," +
                "  o.sn" +
                "  from xx_order_item oi" +
                "  LEFT JOIN xx_product p on oi.product=p.id" +
                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
                "  LEFT JOIN xx_order o on oi.orders=o.id" +
                "  where t.order_status in(1,2) and oi.supplier=:tenant";

        String totalSql = "SELECT count(1) total" +
                "  FROM xx_order_item oi" +
                "  LEFT JOIN xx_product p on oi.product=p.id" +
                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
                "  LEFT JOIN xx_order o on oi.orders=o.id" +
                "  where t.order_status in(1,2) and oi.supplier=:tenant";
        if (seller != null) {
            sql += " AND t.tenant=:seller";
            totalSql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(keyWords)) {
            sql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords OR p.sn LIKE :keyWords)";
            totalSql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords OR p.sn LIKE :keyWords)";
        }
        if (begin_date != null) {
            sql += "  AND oi.create_date>:begin_date";
            totalSql += "  AND oi.create_date>:begin_date";
        }
        if (end_date != null) {
            sql += "  AND oi.create_date<:end_date";
            totalSql += "  AND oi.create_date<:end_date";
        }
        sql += "  ORDER BY oi.create_date DESC";
        System.out.print(sql);
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());

        if (seller != null) {
            query.setParameter("seller", seller.getId());
            totalQuery.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(keyWords)) {
            query.setParameter("keyWords", "%" + keyWords + "%");
            totalQuery.setParameter("keyWords", "%" + keyWords + "%");
        }
        if (begin_date != null) {
            query.setParameter("begin_date", begin_date);
            totalQuery.setParameter("begin_date", begin_date);
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
                map.put("full_name", row[1]);
                map.put("barcode", row[2]);
                map.put("seller", row[3]);
                map.put("username", row[4]);
                map.put("packag_unit_name", row[5]);
                map.put("quantity", row[6]);
                map.put("price", row[7]);
                map.put("sale_amount", row[8]);
                map.put("cost", row[9]);
                map.put("settlement_amount", row[10]);
                map.put("sn", row[11]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }
    //销售明细
    public List<Map<String, Object>> saleDetail(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        String sql = "SELECT oi.create_date," +
                "  p.full_name," +
                "  p.barcode," +
                "  (SELECT te2.name from xx_tenant te2 where te2.id=t.tenant) seller," +
                "  (SELECT m.username FROM xx_member m where m.id=o.member) username," +
                "  oi.packag_unit_name," +
                "  oi.quantity," +
                "  p.price," +
                "  oi.quantity*p.price sale_amount," +
                "  p.cost," +
                "  oi.quantity*p.cost settlement_amount," +
                "  o.sn" +
                "  from xx_order_item oi" +
                "  LEFT JOIN xx_product p on oi.product=p.id" +
                "  LEFT JOIN xx_trade t on oi.trades=t.id" +
                "  LEFT JOIN xx_order o on oi.orders=o.id" +
                "  where t.order_status in(1,2) and oi.supplier=:tenant";

       
        if (seller != null) {
            sql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(keyWords)) {
            sql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
        }
        if (begin_date != null) {
            sql += "  AND oi.create_date>:begin_date";
        }
        if (end_date != null) {
            sql += "  AND oi.create_date<:end_date";
        }
        sql += "  ORDER BY oi.create_date DESC";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
         if (seller != null) {
            query.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(keyWords)) {
            query.setParameter("keyWords", "%" + keyWords + "%");
        }
        if (begin_date != null) {
            query.setParameter("begin_date", begin_date);
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
                map.put("full_name", row[1]);
                map.put("barcode", row[2]);
                map.put("seller", row[3]);
                map.put("username", row[4]);
                map.put("packag_unit_name", row[5]);
                map.put("quantity", row[6]);
                map.put("price", row[7]);
                map.put("sale_amount", row[8]);
                map.put("cost", row[9]);
                map.put("settlement_amount", row[10]);
                map.put("sn", row[11]);
                maps.add(map);
            }
        }
        return maps;
    }

    //订单查询
    public Page<Map<String, Object>> order(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable) {
        String sql = "SELECT t.create_date," +
                "  o.sn," +
                "  te.name seller," +
                "  o.consignee," +
                "  o.order_status," +
                "  (SELECT sum(oi.price*oi.quantity) FROM xx_order_item oi WHERE oi.trades=t.id AND oi.supplier=:tenant) transaction_amount" +
                "  FROM xx_trade t" +
                "  LEFT JOIN xx_order o ON t.orders=o.id" +
                "  LEFT JOIN xx_tenant te ON t.tenant=te.id" +
                "  WHERE t.id IN (SELECT oi1.trades FROM xx_order_item oi1 WHERE oi1.supplier=:tenant)";

        String totalSql = "SELECT count(1) total" +
                "  FROM xx_trade t" +
                "  LEFT JOIN xx_order o ON t.orders=o.id" +
                "  LEFT JOIN xx_tenant te ON t.tenant=te.id" +
                "  WHERE t.id IN (SELECT oi1.trades FROM xx_order_item oi1 WHERE oi1.supplier=:tenant)";

        if (seller != null) {
            sql += " AND t.tenant=:seller";
            totalSql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(keyWords)) {
//            sql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
//            totalSql += " AND (p.barcode LIKE :keyWords OR p.full_name LIKE :keyWords)";
        }
        if (begin_date != null) {
            sql += "  AND t.create_date>:begin_date";
            totalSql += "  AND t.create_date>:begin_date";
        }
        if (end_date != null) {
            sql += "  AND t.create_date<:end_date";
            totalSql += "  AND t.create_date<:end_date";
        }
        sql += "  ORDER BY t.create_date DESC";

        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());

        if (seller != null) {
            query.setParameter("seller", seller.getId());
            totalQuery.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(keyWords)) {
//            query.setParameter("keyWords", "%" + keyWords + "%");
//            totalQuery.setParameter("keyWords", "%" + keyWords + "%");
        }
        if (begin_date != null) {
            query.setParameter("begin_date", begin_date);
            totalQuery.setParameter("begin_date", begin_date);
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
                map.put("seller", row[2]);
                map.put("consignee", row[3]);
                map.put("order_status", row[4]);
                map.put("transaction_amount", row[5]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }

    //经营分析
    public Page<Map<String, Object>> findManagementAnalysePage(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content,Pageable pageable){
    	String sql="select o.create_date,"
    			+ "p.full_name,"
    			+ "p.barcode,"
    			+ "p.cost,"
    			+ "p.unit,"
    			+ "sum(o.quantity) quantity," 
    			+ "sum(o.quantity)*p.cost totalCost,"
    			+ "(select te.name as te from xx_tenant te where p.tenant=te.id) tenant "
    			+ "from xx_order_item o "
    			+ "left join xx_product p on p.id= o.product "
    			+ "left join xx_trade t on t.id=o.trades "
    			+ "where t.order_status in(1,2) "
    			+ "and o.supplier=:tenant";
    	String sqlTotal="select count(1) total "
    			+ "from (select 1 from xx_order_item o "
    			+ "left join xx_product p on p.id= o.product "
    			+ "left join xx_trade t on t.id=o.trades "
    			+ "where t.order_status in(1,2) "
    			+ "and o.supplier=:tenant";
    			
		if (seller != null) {
            sql += " AND t.tenant=:seller";
            sqlTotal += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(search_content)) {
            sql += " AND (p.barcode LIKE :search_content OR p.full_name LIKE :search_content)";
            sqlTotal += " AND (p.barcode LIKE :search_content OR p.full_name LIKE :search_content)";
        }
        if (start_date != null) {
            sql += "  AND o.create_date>:start_date";
            sqlTotal += "  AND o.create_date>:start_date";
        }
        if (end_date != null) {
            sql += "  AND o.create_date<:end_date";
            sqlTotal += "  AND o.create_date<:end_date";
        }
        sql += "  GROUP BY o.product";
        sqlTotal += "  GROUP BY o.product) su";

        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        Query totalQuery = entityManager.createNativeQuery(sqlTotal).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        if (seller != null) {
            query.setParameter("seller", seller.getId());
            totalQuery.setParameter("seller", seller.getId());
        }
        if (StringUtils.isNotBlank(search_content)) {
            query.setParameter("search_content", "%" + search_content + "%");
            totalQuery.setParameter("search_content", "%" + search_content + "%");
            
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
        List list=new ArrayList();
        Long total=0l;
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
        List<Map<String,Object>> maps = new ArrayList<>();
        if(list.size()>0){
            for (Object obj : list) {
                Map<String,Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("create_date",row[0]);
                map.put("full_name",row[1]);
                map.put("barcode",row[2]);
                map.put("price",row[3]);
                map.put("unit",row[4]);
                map.put("quantity",row[5]);
                map.put("totalPrice",row[6]);
                map.put("tenant",row[7]);
                maps.add(map);
            }
        }
        return new Page<Map<String,Object>>(maps, total, pageable);
    }
    //经营分析（导出）
    public List<Map<String, Object>> managementAnalyse(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content){
    	String sql="select o.create_date,"
    			+ "p.full_name,"
    			+ "p.barcode,"
    			+ "p.cost,"
    			+ "p.unit,"
    			+ "sum(o.quantity) quantity," 
    			+ "sum(o.quantity)*p.cost totalCost,"
    			+ "(select te.name as te from xx_tenant te where p.tenant=te.id) tenant "
    			+ "from xx_order_item o "
    			+ "left join xx_product p on p.id= o.product "
    			+ "left join xx_trade t on t.id=o.trades "
    			+ "where t.order_status in(1,2) "
    			+ "and o.supplier=:tenant";
    	
		if (seller != null) {
            sql += " AND t.tenant=:seller";
        }
        if (StringUtils.isNotBlank(search_content)) {
            sql += " AND (p.barcode LIKE :search_content OR p.full_name LIKE :search_content)";
        }
        if (start_date != null) {
            sql += "  AND o.create_date>:start_date";
        }
        if (end_date != null) {
            sql += "  AND o.create_date<:end_date";
        }
        sql += "  GROUP BY o.product";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
       
        if (seller != null) {
            query.setParameter("seller", seller.getId());
           
        }
        if (StringUtils.isNotBlank(search_content)) {
            query.setParameter("search_content", "%" + search_content + "%");
            
        }
        if (start_date != null) {
            query.setParameter("start_date", start_date);
            
        }
        if (end_date != null) {
            query.setParameter("end_date", end_date);
           
        }
    
        List list=new ArrayList();
        Long total=0l;
        try {
            list = query.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        List<Map<String,Object>> maps = new ArrayList<>();
        if(list.size()>0){
            for (Object obj : list) {
                Map<String,Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("create_date",row[0]);
                map.put("full_name",row[1]);
                map.put("barcode",row[2]);
                map.put("price",row[3]);
                map.put("unit",row[4]);
                map.put("quantity",row[5]);
                map.put("totalPrice",row[6]);
                map.put("tenant",row[7]);
                maps.add(map);
            }
        }
        return maps;
    }
    
}
