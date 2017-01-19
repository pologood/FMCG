package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.TenantFreightTemplate;
import net.wit.entity.Tenant;

/**
 * 运费模板
 * Created by ruanx on 2017/1/11.
 */
public interface TenantFreightTemplateService extends BaseService<TenantFreightTemplate, Long>{
    Page<TenantFreightTemplate> findPage(Tenant tenant, Pageable pageable);
}
