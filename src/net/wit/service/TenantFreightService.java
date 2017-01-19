package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.TenantFreight;
import net.wit.entity.Tenant;

/**
 * 区域运费
 * Created by ruanx on 2017/1/11.
 */
public interface TenantFreightService extends BaseService<TenantFreight, Long>{
    Page<TenantFreight> findByFreightsTemplate(Long id, TenantFreight.Type type, Pageable pageable);

    /**
     * 查找区域邮费
     * tenant 企业
     * area   区域
     * */
    TenantFreight findByArea(Tenant tenant, Area area);
}
