package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tenant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by WangChao on 2016-4-12.
 */
public interface SupplierDao extends BaseDao<Object, Long> {
    Page<Map<String, Object>> saleStatisticsList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable);
    List<Map<String, Object>> saleTotal(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);
    
    BigDecimal totalSaleAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);

    BigDecimal totalSettlementAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);

    Page<Map<String, Object>> saleDetailList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable);
    List<Map<String, Object>> saleDetail(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);

    Page<Map<String, Object>> order(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable);

    Page<Map<String, Object>> findManagementAnalysePage(Tenant tenant, Date start_date,Date end_date,Tenant seller,String search_content,Pageable pageable);
    List<Map<String, Object>> managementAnalyse(Tenant tenant, Date start_date,Date end_date,Tenant seller,String search_content);
}
