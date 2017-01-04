package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Purchase;
import net.wit.entity.Tenant;

import java.util.Date;
import java.util.List;

/**
 * Created by My-PC on 16/06/02.
 */
public interface PurchaseService extends BaseService<Purchase,Long> {

    Page<Purchase> openPage(
            Pageable pageable,                           //根据关键字或者拼音码查询
            Tenant tenant,                                 //采购商
            Tenant supplier,                               //供应商
            Date beginDate,                              //开始时间
            Date endDate,                                //结束时间
            String keyword
    );

    /**
     * @param pageable      根据关键字或者拼音码查询
     * @param tenant        采购商
     * @param supplier      供应商
     * @param beginDate     开始时间
     * @param endDate       结束时间
     * @param supplierName  供应商名称
     * @param barcode       商品条码
     * @param keyword       关键字
     * @return              page
     */
    Page<Purchase> openPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName,String barcode, String keyword);
    List<Purchase> exportOpenPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName,String barcode, String keyword);
}
