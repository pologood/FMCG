package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SupplierDao;
import net.wit.entity.Tenant;
import net.wit.service.SupplierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by WangChao on 2016-4-12.
 */
@Service("supplierServiceImpl")
public class SupplierServiceImpl extends BaseServiceImpl<Object, Long> implements SupplierService{
    @Resource(name = "supplierDaoImpl")
    private SupplierDao supplierDao;

    @Resource(name = "supplierDaoImpl")
    public void setBaseDao(SupplierDao supplierDao) {
        super.setBaseDao(supplierDao);
    }

    public Page<Map<String, Object>> saleStatisticsList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable) {
        return supplierDao.saleStatisticsList(tenant, seller, begin_date, end_date, keyWords, pageable);
    }
    public List<Map<String, Object>> saleTotal(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        return supplierDao.saleTotal(tenant, seller, begin_date, end_date, keyWords);
    }

    public BigDecimal totalSaleAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        return supplierDao.totalSaleAmount(tenant, seller, begin_date, end_date, keyWords);
    }

    public BigDecimal totalSettlementAmount(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        return supplierDao.totalSettlementAmount(tenant, seller, begin_date, end_date, keyWords);
    }

    public Page<Map<String, Object>> saleDetailList(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable) {
        return supplierDao.saleDetailList(tenant, seller, begin_date, end_date, keyWords, pageable);
    }
    public List<Map<String, Object>> saleDetail(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords) {
        return supplierDao.saleDetail(tenant, seller, begin_date, end_date, keyWords);
    }
    
    public Page<Map<String, Object>> order(Tenant tenant, Tenant seller, Date begin_date, Date end_date, String keyWords, Pageable pageable) {
        return supplierDao.order(tenant, seller, begin_date, end_date, keyWords, pageable);
    }

    public Page<Map<String,Object>> findManagementAnalysePage(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content,Pageable pageable){
        return supplierDao.findManagementAnalysePage(tenant,start_date,end_date,seller,search_content,pageable);
    }
    public List<Map<String,Object>> managementAnalyse(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content){
        return supplierDao.managementAnalyse(tenant,start_date,end_date,seller,search_content);
    }
}
