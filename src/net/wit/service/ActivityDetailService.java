package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityDetail;
import net.wit.entity.ActivityRules;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface ActivityDetailService extends BaseService<ActivityDetail, Long> {

    Page<ActivityDetail> openPage(Tenant tenant, ActivityRules activityRules, Member member, Pageable pageable);

    /**
     * 用户或者商家添加积分
     * @param tenant
     * @param member
     * @param activityRules
     */
    public void addPoint(Member member, Tenant tenant ,ActivityRules activityRules);


    boolean isActivity(Member member, Tenant tenant, ActivityRules activityRules);
}
