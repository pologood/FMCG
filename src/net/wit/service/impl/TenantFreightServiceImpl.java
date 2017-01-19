package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantFreightDao;
import net.wit.entity.Area;
import net.wit.entity.TenantFreight;
import net.wit.entity.Tenant;
import net.wit.service.TenantFreightService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/1/11.
 */
@Service("tenantFreightServiceImpl")
public class TenantFreightServiceImpl extends BaseServiceImpl<TenantFreight, Long> implements TenantFreightService {

    @Resource(name = "tenantFreightDaoImpl")
    private TenantFreightDao dao;

    @Resource(name = "tenantFreightDaoImpl")
    public void setBaseDao(TenantFreightDao tenantFreightDao) {
        super.setBaseDao(tenantFreightDao);
    }

    public Page<TenantFreight> findByFreightsTemplate(Long id, TenantFreight.Type type, Pageable pageable){
        return dao.findByFreightsTemplate(id,type,pageable);
    }

    public TenantFreight findByArea(Tenant tenant, Area area){
        return dao.findByArea(tenant,area);
    }
}
