package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PurchaseReturnsDao;
import net.wit.dao.PurchaseReturnsItemDao;
import net.wit.entity.PurchaseReturns;
import net.wit.entity.PurchaseReturnsItem;
import net.wit.entity.Tenant;
import net.wit.service.PurchaseReturnsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 采购退货单
 * Created by My-PC on 16/06/02.
 */
@Service("purchaseReturnsServiceImpl")
public class PurchaseReturnsServiceImpl extends BaseServiceImpl<PurchaseReturns,Long> implements PurchaseReturnsService {

    @Resource(name = "purchaseReturnsDaoImpl")
    private PurchaseReturnsDao purchaseReturnsDao;

    @Resource(name = "purchaseReturnsItemDaoImpl")
    private PurchaseReturnsItemDao purchaseReturnsItemDao;

    @Resource(name = "purchaseReturnsDaoImpl")
    public  void setBaseDao(PurchaseReturnsDao purchaseReturnsDao){
        super.setBaseDao(purchaseReturnsDao);
    }

    public Page<PurchaseReturns> openPage(Pageable pageable,                           //根据关键字或者拼音码查询
                                          Tenant tenant,                                 //采购商
                                          Tenant supplier,                               //供应商
                                          Date beginDate,                              //开始时间
                                          Date endDate,                                //结束时间
                                          String keyword
    ) {
        return purchaseReturnsDao.openPage(pageable, tenant, supplier, beginDate, endDate, null, null, keyword);
    }
    public List<PurchaseReturns> exportOpenPage(                          
	            Tenant tenant,                                 //采购商
	            Tenant supplier,                               //供应商
	            Date beginDate,                              //开始时间
	            Date endDate,                                //结束时间
	            String keyword                               //根据关键字或者拼音码查询  
	) {
	return purchaseReturnsDao.exportOpenPage( tenant, supplier, beginDate, endDate,keyword);
	}

    @Override
    public Page<PurchaseReturns> openPage(Pageable pageable, Tenant tenant, Tenant supplier, Date beginDate, Date endDate, String supplierName, String barcode, String keyword) {
        Set<PurchaseReturns> purchaseReturnsSet=new HashSet<>();
        if(StringUtils.isNotBlank(barcode)){
            List<Filter> filters=new ArrayList<>();
            filters.add(new Filter("barcode", Filter.Operator.like, "%" + barcode + "%"));
            List<PurchaseReturnsItem> purchaseReturnsItems=purchaseReturnsItemDao.findList(null,null,filters,null);
            for(PurchaseReturnsItem purchaseReturnsItem:purchaseReturnsItems){
                purchaseReturnsSet.add(purchaseReturnsItem.getPurchaseReturns());
            }
        }else{
            purchaseReturnsSet=null;
        }
        return purchaseReturnsDao.openPage(pageable, tenant, supplier, beginDate, endDate, supplierName, purchaseReturnsSet, keyword);
    }
}
