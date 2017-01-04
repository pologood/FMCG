package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.PurchaseReturns;
import net.wit.entity.Tenant;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by My-PC on 16/06/02.
 */
public interface PurchaseReturnsDao extends BaseDao<PurchaseReturns, Long> {

    Page<PurchaseReturns> openPage(
            Pageable pageable,                           //根据关键字或者拼音码查询
            Tenant tenant,                                 //采购商
            Tenant supplier,                               //供应商
            Date beginDate,                              //开始时间
            Date endDate,                                //结束时间
            String supplierName,
            Set<PurchaseReturns> purchaseReturnsSet,
            String keyword
    );
    List<PurchaseReturns> exportOpenPage(
            Tenant tenant,                                 //采购商
            Tenant supplier,                               //供应商
            Date beginDate,                              //开始时间
            Date endDate,  
            String keyword
    );
}
