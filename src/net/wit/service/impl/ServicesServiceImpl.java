package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ServicesDao;
import net.wit.entity.Services;
import net.wit.entity.Tenant;
import net.wit.service.ServicesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * Created by My-PC on 16/05/31.
 */
@Service("servicesServiceImpl")
public class ServicesServiceImpl extends BaseServiceImpl<Services,Long> implements ServicesService {
    @Resource(name = "servicesDaoImpl")
    private ServicesDao servicesDao;

    @Resource(name = "servicesDaoImpl")
    public void setBaseDao(ServicesDao servicesDao) {
        super.setBaseDao(servicesDao);
    }

    @Override
    public Page<Services> findPage(Date beginDate, Date endDate,Services.State state,
                                   Pageable pageable) {

        return servicesDao.findPage(beginDate, endDate, state, pageable);
    }

    public boolean checkServicesType(Tenant tenant, Services.Status status){
        return servicesDao.checkServicesType(tenant,status);
    }

    public Services findServicesByTenant(Tenant tenant, Services.Status status){
        return servicesDao.findServicesByTenant(tenant,status);
    }

    public Map<String,String> getType(Tenant tenant, Services.Status status){
        return servicesDao.getType(tenant,status);
    }
}
