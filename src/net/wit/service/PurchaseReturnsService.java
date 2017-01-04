package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.PurchaseReturns;
import net.wit.entity.Tenant;

import java.util.Date;
import java.util.List;

/**
 * Created by My-PC on 16/06/02.
 */
public interface PurchaseReturnsService extends BaseService<PurchaseReturns,Long> {
    Page<PurchaseReturns> openPage(
            Pageable pageable,                           //根据关键字或者拼音码查询
            Tenant tenant,                                 //采购商
            Tenant supplier,                               //供应商
            Date beginDate,                              //开始时间
            Date endDate,                                //结束时间
            String keyword
    );
    List<PurchaseReturns> exportOpenPage(
            Tenant tenant,                                 //采购商
            Tenant supplier,                               //供应商
            Date beginDate,                              //开始时间
            Date endDate,                                //结束时间
            String keyword                               //根据关键字或者拼音码查询
    );

    /**
     * 按条件查询采购退货单
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
    Page<PurchaseReturns> openPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName, String barcode, String keyword);
}
