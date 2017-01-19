package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

import java.util.List;

public interface EmployeeDao extends BaseDao<Employee, Long> {
    Page<Employee> findPage(Pageable pageable, TenantCategory tenantCategory, Tag tag, Location location, String keyWord, String orderType);

    List<Employee> findList(Tenant tenant, Tag tag);

    List<Employee> findByDeliveryCenterId(Long id);

    List<Employee> findByMember(Member member);

    List<Employee> findByTenant(Tenant tenant);

    Employee findMember(Member member,Tenant tenant);
}
