package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.TenantFreight;
import net.wit.entity.Tenant;

/**
 * Created by Administrator on 2017/1/11.
 */
public interface TenantFreightDao extends BaseDao<TenantFreight, Long>{
    Page<TenantFreight> findByFreightsTemplate(Long id, TenantFreight.Type type, Pageable pageable);

    TenantFreight findByArea(Tenant tenant, Area area);
}
