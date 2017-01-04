package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityPlanning;
import net.wit.entity.CouponCode;
import net.wit.entity.Tenant;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface ActivityPlanningService extends BaseService<ActivityPlanning,Long> {

    Page<ActivityPlanning> openPage(Pageable pageable,String keyword);

    ActivityPlanning getCurrent(Tenant tenant, ActivityPlanning.Type type);

    boolean isAcitivtyName(String name);

    List<ActivityPlanning> findUnionActivity();

    CouponCode lockCoupon(ActivityPlanning activityPlanning, BigDecimal amount);
}
