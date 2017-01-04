package net.wit.service;

import net.wit.entity.ActivityPlanning;
import net.wit.entity.SingleProductPosition;

import java.util.List;

/**
 * Created by Administrator on 16/11/30.
 */
public interface SingleProductPositionService extends BaseService<SingleProductPosition,Long> {

    List<SingleProductPosition> findList(SingleProductPosition.Type type);

    SingleProductPosition findByActivityPlanning(ActivityPlanning activityPlanning);
}
