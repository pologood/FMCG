package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityRules;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface ActivityRulesService extends BaseService<ActivityRules, Long> {

    Page<ActivityRules> openPage(ActivityRules.Type type, Pageable pageable);
}
