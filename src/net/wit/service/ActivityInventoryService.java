package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityInventory;
import net.wit.entity.ActivityRules;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

/**
 * Created by Administrator on 2016/7/15.
 */
public interface ActivityInventoryService extends BaseService<ActivityInventory,Long> {

    Page<ActivityInventory> openPage(ActivityInventory.Status status,String  keyword, Pageable pageable);
}
