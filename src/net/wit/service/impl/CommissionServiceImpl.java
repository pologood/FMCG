package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CommissionDao;
import net.wit.entity.Admin;
import net.wit.entity.Commission;
import net.wit.service.CommissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by thwapp on 2016/1/15.
 */
@Service("commissionServiceImpl")
public class CommissionServiceImpl extends BaseServiceImpl<Commission,Long> implements CommissionService {

    @Resource(name = "commissionDaoImpl")
    CommissionDao commissionDao;
    @Resource(name = "commissionDaoImpl")
    public void setBaseDao(CommissionDao commissionDao){
        super.setBaseDao(commissionDao);
    }

    @Override
    public Page<Commission> findPage(Admin admin, Pageable pageable) {
        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("admin", Filter.Operator.eq, admin));
        Page<Commission> page=commissionDao.findPage(pageable);
        return page;
    }
}
