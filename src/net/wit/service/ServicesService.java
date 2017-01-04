package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Services;
import net.wit.entity.Tenant;

import java.util.Date;
import java.util.Map;

/**
 *
 * Created by My-PC on 16/05/31.
 */
public interface ServicesService extends BaseService<Services,Long> {

    Page<Services> findPage(Date beginDate, Date endDate,Services.State state, Pageable pageable);

    boolean checkServicesType(Tenant tenant, Services.Status status);

    Services findServicesByTenant(Tenant tenant, Services.Status status);

    Map<String,String> getType(Tenant tenant, Services.Status status);
}
