package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ActivityRulesDao;
import net.wit.entity.ActivityRules;
import net.wit.service.ActivityRulesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/7/13.
 */
@Service("activityRulesServiceImpl")
public class ActivityRulesServiceImpl extends BaseServiceImpl<ActivityRules, Long> implements ActivityRulesService {

    @Resource(name = "activityRulesDaoImpl")
    private ActivityRulesDao activityRulesDao;

    @Resource(name = "activityRulesDaoImpl")
    public void setBaseDao(ActivityRulesDao activityRulesDao) {
        super.setBaseDao(activityRulesDao);
    }


    public Page<ActivityRules> openPage(ActivityRules.Type type , Pageable pageable){
        return activityRulesDao.openPage(type,pageable);
    }
}
