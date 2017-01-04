package net.wit.service.impl;

import net.wit.dao.PurchaseReturnsItemDao;
import net.wit.entity.PurchaseReturnsItem;
import net.wit.service.PurchaseReturnsItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by My-PC on 16/06/02.
 */
@Service("purchaseReturnsItemServiceImpl")
public class PurchaseReturnsItemServiceImpl extends BaseServiceImpl<PurchaseReturnsItem,Long> implements PurchaseReturnsItemService {

    @Resource(name = "purchaseReturnsItemDaoImpl")
    private PurchaseReturnsItemDao purchaseReturnsItemDao;

    @Resource(name = "purchaseReturnsItemDaoImpl")
    public  void setBaseDao(PurchaseReturnsItemDao purchaseReturnsItemDao){
        super.setBaseDao(purchaseReturnsItemDao);
    }
}
