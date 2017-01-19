package net.wit.dao;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Equipment;
import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.entity.Tenant.OrderType;
import net.wit.entity.UnionTenant;

import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

public interface UnionTenantDao extends BaseDao<UnionTenant,Long>{

    Page<Map<String,Object>> findTenant(Union union, Pageable pageable, OrderType orderType);

    Page<Map<String,Object>> findTenantByPad(Equipment equipment, Pageable pageable, OrderType orderType);

    List<UnionTenant> findUnionTenant(Union unioin, Tenant tenant, List<Filter> filters);

    Page<Map<String,Object>> findPage(Tenant tenant,String status,Pageable pageable);

    Page<Map<String,Object>> findPage(Equipment equipment,String status,Pageable pageable);

    //
    Page<UnionTenant> findPage(Union union, UnionTenant.Status status, Pageable pageable);

    Long findUnionTenant(Equipment equipment,Tenant tenant);
    /**
     * 查询投放商家的数量
     */
    Long count(Equipment equipment,Tenant tenant,UnionTenant.Status status);

    /**
     * 查询投放商家
     */
    Page<UnionTenant>  findUnionTenantPage(Equipment equipment,Tenant tenant,UnionTenant.Status status,Union union ,Pageable pageable);
    List<UnionTenant>  findUnionTenantList(Equipment equipment,Tenant tenant,UnionTenant.Status status,Union union );
}
