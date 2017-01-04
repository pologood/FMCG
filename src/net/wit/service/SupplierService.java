package net.wit.service;

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

public interface SupplierService extends BaseService<Object, Long> {
    //销售统计
    Page<Map<String, Object>> saleStatisticsList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable);
    //销售统计(导出)
    List<Map<String, Object>> saleTotal(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);
    
    //累计销售金额
    BigDecimal totalSaleAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);

    //累计结算金额
    BigDecimal totalSettlementAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);

    //销售明细
    Page<Map<String, Object>> saleDetailList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable);
  //销售明细
    List<Map<String, Object>> saleDetail(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords);
    
    //订单查询
    Page<Map<String, Object>> order(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable);

    //经营分析
    Page<Map<String, Object>> findManagementAnalysePage(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content, Pageable pageable);
    //经营分析(导出功能)
    List<Map<String, Object>> managementAnalyse(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content);
}
