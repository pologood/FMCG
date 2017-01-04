package net.wit.service.impl;

import net.wit.dao.SingleProductPositionDao;
import net.wit.entity.ActivityPlanning;
import net.wit.entity.SingleProductPosition;
import net.wit.service.SingleProductPositionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 16/11/30.
 */
@Service("singleProductPositionServiceImpl")
public class SingleProductPositionServiceImpl extends BaseServiceImpl<SingleProductPosition,Long> implements SingleProductPositionService {

    @Resource(name = "singleProductPositionDaoImpl")
    private SingleProductPositionDao singleProductPositionDao;

    @Resource(name = "singleProductPositionDaoImpl")
    public void setBaseDao(SingleProductPositionDao singleProductPositionDao) {
        super.setBaseDao(singleProductPositionDao);
    }

    public List<SingleProductPosition> findList(SingleProductPosition.Type type){
        return singleProductPositionDao.findList(type);
    }

    public SingleProductPosition findByActivityPlanning(ActivityPlanning activityPlanning){
        return singleProductPositionDao.findByActivityPlanning(activityPlanning);
    }
}
