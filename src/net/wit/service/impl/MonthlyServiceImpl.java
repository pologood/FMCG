/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AdDao;
import net.wit.dao.MonthlyDao;
import net.wit.entity.*;
import net.wit.service.AdService;
import net.wit.service.MonthlyService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * Service - 广告
 * @author rsico Team
 * @version 3.0
 */
@Service("monthlyServiceImpl")
public class MonthlyServiceImpl extends BaseServiceImpl<Monthly, Long> implements MonthlyService {
	@Resource(name = "monthlyDaoImpl")
	private MonthlyDao monthlyDao;

	@Resource(name = "monthlyDaoImpl")
	public void setBaseDao(MonthlyDao monthlyDao) {
		super.setBaseDao(monthlyDao);
	}


	@Transactional(readOnly = true)
	public Page<Monthly> findMonthlyPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable) {
		return monthlyDao.findMonthlyPage(tenant,beginDate, endDate,  pageable);
	}
    //判断当天是否有月结算
    @Transactional(readOnly = true)
    public Boolean isMonthly(Member member,Pageable pageable){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        Date end = calendar.getTime();

        Tenant tenant=member.getTenant();
        if(tenant!=null){
            if(tenant.getTenantType()!= Tenant.TenantType.tenant){
                return false;
            }
        }else{
            return false;
        }
        Page<Monthly> page= monthlyDao.findMonthlyPage(tenant,start,end,pageable);
        if(page.getContent()!=null){
            if(page.getContent().size()>0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    };

}