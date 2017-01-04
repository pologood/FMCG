package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityRules;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface ActivityRulesDao extends BaseDao<ActivityRules, Long> {


    Page<ActivityRules> openPage(ActivityRules.Type type, Pageable pageable);
}
