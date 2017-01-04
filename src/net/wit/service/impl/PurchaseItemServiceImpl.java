package net.wit.service.impl;

import net.wit.dao.PurchaseItemDao;
import net.wit.entity.PurchaseItem;
import net.wit.service.PurchaseItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by My-PC on 16/06/02.
 */
@Service("purchaseItemServiceImpl")
public class PurchaseItemServiceImpl extends BaseServiceImpl<PurchaseItem,Long> implements PurchaseItemService {

    @Resource(name = "purchaseItemDaoImpl")
    private PurchaseItemDao purchaseItemDao;

    @Resource(name = "purchaseItemDaoImpl")
    public  void setBaseDao(PurchaseItemDao purchaseItemDao){
        super.setBaseDao(purchaseItemDao);
    }
}
