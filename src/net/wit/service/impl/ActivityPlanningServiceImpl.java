package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ActivityPlanningDao;
import net.wit.entity.ActivityPlanning;
import net.wit.entity.CouponCode;
import net.wit.entity.Tenant;
import net.wit.service.ActivityPlanningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
@Service("activityPlanningServiceImpl")
public class ActivityPlanningServiceImpl extends BaseServiceImpl<ActivityPlanning, Long> implements ActivityPlanningService {

    @Resource(name = "activityPlanningDaoImpl")
    private ActivityPlanningDao activityPlanningDao;

    @Resource(name = "activityPlanningDaoImpl")
    public void setBaseDao(ActivityPlanningDao activityPlanningDao) {
        super.setBaseDao(activityPlanningDao);
    }

    public Page<ActivityPlanning> openPage(Pageable pageable, String keyword) {
        return activityPlanningDao.openPage(pageable, keyword);
    }

    public ActivityPlanning getCurrent(Tenant tenant, ActivityPlanning.Type type) {
        return activityPlanningDao.getCurrent(tenant,type);
    }

    public List<ActivityPlanning> findUnionActivity(){
        return activityPlanningDao.findUnionActivity();
    }

    public boolean isAcitivtyName(String name) {
        return activityPlanningDao.isAcitivtyName(name);
    }

    public CouponCode lockCoupon(ActivityPlanning activityPlanning, BigDecimal amount) {
        return activityPlanningDao.lockCoupon(activityPlanning, amount);
    }
}
