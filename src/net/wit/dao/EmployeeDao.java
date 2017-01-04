package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Employee;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;

import java.util.List;

public interface EmployeeDao extends BaseDao<Employee, Long> {
    Page<Employee> findPage(Pageable pageable, Tag tag, String keyWord);

    List<Employee> findList(Tenant tenant, Tag tag);

    List<Employee> findByDeliveryCenterId(Long id);

    List<Employee> findByMember(Member member);

    List<Employee> findByTenant(Tenant tenant);

    Employee findMember(Member member,Tenant tenant);
}
