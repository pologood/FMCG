package net.wit.service;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Tenant.OrderType;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14.
 */
public interface UnionTenantService extends BaseService<UnionTenant,Long>{
    Page<Map<String,Object>> findTenant(Union union, Pageable pageable, OrderType orderType);

    Page<Map<String,Object>> findTenantByPad(Equipment equipment, Pageable pageable, OrderType orderType);

    List<UnionTenant> findUnionTenant(Union unioin, Tenant tenant, List<Filter> filters);
    /**
     * 根据商家查询联盟商家
     */
    Page<Map<String,Object>> findPage(Tenant tenant,String status,Pageable pageable);

    Page<Map<String,Object>> findPage(Equipment equipment,String status,Pageable pageable);

    Long findUnionTenant(Equipment equipment,Tenant tenant);

    Page<UnionTenant> findPage(Union union, UnionTenant.Status status, Pageable pageable);
    /**
     * 查询投放商家的数量
     */
    Long count(Equipment equipment,Tenant tenant,UnionTenant.Status status);
    /**
     * 查询投放商家
     */
    Page<UnionTenant>  findUnionTenantPage(Equipment equipment,Tenant tenant,UnionTenant.Status status,Union union,Pageable pageable);
    List<UnionTenant>  findUnionTenantList(Equipment equipment,Tenant tenant,UnionTenant.Status status,Union union);

    /**
     * 生成支付单号
     */
    void pay(UnionTenant unionTenant,Payment payment);
    void payment(Payment payment, Member operator);
    /**
     * 撤销申请、拒绝申请
     */
    void cancel(UnionTenant unionTenant);

}
