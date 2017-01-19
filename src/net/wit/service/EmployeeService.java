package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

import java.util.List;

public interface EmployeeService extends BaseService<Employee, Long> {
    Page<Employee> findPage(Pageable pageable, Tag tag,String keyWord);

    Page<Employee> findPage(Pageable pageable, TenantCategory tenantCategory, Tag tag, Location location, String keyWord, String orderType);

    List<Employee> findList(Tenant tenant, Tag tag);

    List<Employee> findByDeliveryCenterId(Long id);

    List<Employee> findByMember(Member member);

    /*根据店铺和用户查角色等信息*/
    Employee findMember(Member member,Tenant tenant);
}
