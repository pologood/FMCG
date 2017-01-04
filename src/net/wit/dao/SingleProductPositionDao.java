package net.wit.dao;

import net.wit.entity.ActivityPlanning;
import net.wit.entity.SingleProductPosition;

import java.util.List;

/**
 * Created by Administrator on 16/11/30.
 */
public interface SingleProductPositionDao extends BaseDao<SingleProductPosition,Long> {

    List<SingleProductPosition> findList(SingleProductPosition.Type type);

    SingleProductPosition findByActivityPlanning(ActivityPlanning activityPlanning);
}
