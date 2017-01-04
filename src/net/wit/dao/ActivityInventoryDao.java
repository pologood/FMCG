package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityInventory;

/**
 * Created by Administrator on 2016/7/15.
 */
public interface ActivityInventoryDao extends BaseDao<ActivityInventory ,Long> {

    Page<ActivityInventory> openPage(ActivityInventory.Status status,String keyword, Pageable pageable);
}
