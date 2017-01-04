package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityDetail;
import net.wit.entity.ActivityRules;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface ActivityDetailDao extends BaseDao<ActivityDetail, Long> {

    Page<ActivityDetail> openPage(Tenant tenant, ActivityRules activityRules, Member member, Pageable pageable);

    /**
     * 根据用户或者商家查询任务是否完成
     */
    public boolean isActivity(Member member, Tenant tenant, ActivityRules activityRules);
}
