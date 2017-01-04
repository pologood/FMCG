package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ActivityDetailDao;
import net.wit.dao.ActivityInventoryDao;
import net.wit.entity.*;
import net.wit.service.ActivityInventoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/15.
 */
@Service("activityInventoryServiceImpl")
public class ActivityInventoryServiceImpl extends BaseServiceImpl<ActivityInventory, Long> implements ActivityInventoryService{

    @Resource(name = "activityInventoryDaoImpl")
    private ActivityInventoryDao activityInventoryDao;

    @Resource(name = "activityInventoryDaoImpl")
    public void setBaseDao(ActivityInventoryDao activityInventoryDao) {
        super.setBaseDao(activityInventoryDao);
    }


    public Page<ActivityInventory> openPage(ActivityInventory.Status status,String keyword, Pageable pageable){
        return activityInventoryDao.openPage(status,keyword,pageable);
    }

}
