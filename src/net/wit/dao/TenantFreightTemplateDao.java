package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.TenantFreightTemplate;
import net.wit.entity.Tenant;

/**
 * Created by ruanx on 2017/1/11.
 */
public interface TenantFreightTemplateDao extends BaseDao<TenantFreightTemplate, Long>{
    Page<TenantFreightTemplate> findPage(Tenant tenant, Pageable pageable);
}
