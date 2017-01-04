package net.wit.dao.impl;

import net.wit.dao.PurchaseItemDao;
import net.wit.entity.PurchaseItem;
import org.springframework.stereotype.Repository;

/**
 * Created by My-PC on 16/06/02.
 */
@Repository("purchaseItemDaoImpl")
public class PurchaseItemDaoImpl extends BaseDaoImpl<PurchaseItem, Long> implements PurchaseItemDao {
}
