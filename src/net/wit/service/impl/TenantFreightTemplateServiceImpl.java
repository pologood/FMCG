package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantFreightTemplateDao;
import net.wit.entity.TenantFreightTemplate;
import net.wit.entity.Tenant;
import net.wit.service.TenantFreightTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/1/11.
 */
@Service("tenantFreightTemplateServiceImpl")
public class TenantFreightTemplateServiceImpl extends BaseServiceImpl<TenantFreightTemplate, Long> implements TenantFreightTemplateService {

    @Resource(name = "tenantFreightTemplateDaoImpl")
    private TenantFreightTemplateDao dao;

    @Resource(name = "tenantFreightTemplateDaoImpl")
    public void setBaseDao(TenantFreightTemplateDao tenantFreightTemplateDao) {
        super.setBaseDao(tenantFreightTemplateDao);
    }

    public Page<TenantFreightTemplate> findPage(Tenant tenant, Pageable pageable){
        return dao.findPage(tenant,pageable);
    }
}
