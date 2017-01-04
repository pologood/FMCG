package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Employee;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;

import java.util.List;

public interface EmployeeService extends BaseService<Employee, Long> {
    Page<Employee> findPage(Pageable pageable, Tag tag,String keyWord);

    List<Employee> findList(Tenant tenant, Tag tag);

    List<Employee> findByDeliveryCenterId(Long id);

    List<Employee> findByMember(Member member);

    /*根据店铺和用户查角色等信息*/
    Employee findMember(Member member,Tenant tenant);
}
