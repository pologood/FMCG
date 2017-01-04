package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PurchaseDao;
import net.wit.dao.PurchaseItemDao;
import net.wit.entity.Purchase;
import net.wit.entity.PurchaseItem;
import net.wit.entity.Tenant;
import net.wit.service.PurchaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by My-PC on 16/06/02.
 */
@Service("purchaseServiceImpl")
public class PurchaseServiceImpl extends BaseServiceImpl<Purchase, Long> implements PurchaseService {

    @Resource(name = "purchaseDaoImpl")
    private PurchaseDao purchaseDao;

    @Resource(name = "purchaseItemDaoImpl")
    private PurchaseItemDao purchaseItemDao;

    @Resource(name = "purchaseDaoImpl")
    public void setBaseDao(PurchaseDao purchaseDao) {
        super.setBaseDao(purchaseDao);
    }

    public Page<Purchase> openPage(Pageable pageable,                           //根据关键字或者拼音码查询
                                   Tenant tenant,                                 //采购商
                                   Tenant supplier,                               //供应商
                                   Date beginDate,                              //开始时间
                                   Date endDate,                                //结束时间
                                   String keyword
    ) {
        return purchaseDao.openPage(pageable,tenant,supplier, beginDate, endDate, null, null, keyword);
    }

    /**
     *
     * @param pageable  根据关键字或者拼音码查询
     * @param tenant    采购商
     * @param supplier  供应商
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param supplierName 供应商名称
     * @param barcode   条形码
     * @param keyword   关键字
     * @return          page
     */
    public Page<Purchase> openPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName, String barcode, String keyword) {
        Set<Purchase> purchaseSet = new HashSet<>();
        if (StringUtils.isNotBlank(barcode)) {
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("barcode", Filter.Operator.like, "%" + barcode + "%"));
            List<PurchaseItem> purchaseItems = purchaseItemDao.findList(null, null, filters, null);
            for (PurchaseItem purchaseItem : purchaseItems) {
                purchaseSet.add(purchaseItem.getPurchase());
            }
        }else{
            purchaseSet=null;
        }
        return purchaseDao.openPage(pageable, tenant, supplier, beginDate, endDate, supplierName, purchaseSet, keyword);
    }
    public List<Purchase> exportOpenPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName, String barcode, String keyword) {
        Set<Purchase> purchaseSet = new HashSet<>();
        if (StringUtils.isNotBlank(barcode)) {
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("barcode", Filter.Operator.like, "%" + barcode + "%"));
            List<PurchaseItem> purchaseItems = purchaseItemDao.findList(null, null, filters, null);
            for (PurchaseItem purchaseItem : purchaseItems) {
                purchaseSet.add(purchaseItem.getPurchase());
            }
        }else{
            purchaseSet=null;
        }
        return purchaseDao.exportOpenPage(pageable, tenant, supplier, beginDate, endDate, supplierName, purchaseSet, keyword);
    }
}
