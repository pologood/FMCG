package net.wit.dao;

import java.math.BigDecimal;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityPlanning;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Tenant;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public interface ActivityPlanningDao extends  BaseDao<ActivityPlanning,Long> {

    Page<ActivityPlanning> openPage(Pageable pageable, String keyword);

    ActivityPlanning getCurrent(Tenant tenant, ActivityPlanning.Type type);
	/**
	 * @param amount
	 * @param activityPlanning
	 * @return
	 */
    CouponCode lockCoupon(ActivityPlanning activityPlanning,BigDecimal amount);

	List<ActivityPlanning> findUnionActivity();
	boolean isAcitivtyName(String name);
}
