package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import org.springframework.stereotype.Repository;

import net.wit.dao.EmployeeDao;
import net.wit.entity.Employee;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("employeeDaoImpl")
public class EmployeeDaoImpl extends BaseDaoImpl<Employee, Long> implements EmployeeDao {
    public Page<Employee> findPage(Pageable pageable, Tag tag, String keyWord) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (keyWord != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("member").<String>get("username"), "%" + keyWord + "%"));
        }

        if (tag != null) {
            restrictions = criteriaBuilder.and(restrictions, root.join("tags").in(tag));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Employee> findList(Tenant tenant, Tag tag) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        }

        if (tag != null) {
            restrictions = criteriaBuilder.and(restrictions, root.join("tags").in(tag));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    public List<Employee> findByDeliveryCenterId(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deliveryCenter").<String>get("id"), id));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    public List<Employee> findByMember(Member member) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    public List<Employee> findByTenant(Tenant tenant) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    public Employee findMember(Member member,Tenant tenant){
        if (member == null || tenant ==null) {
            return null;
        }
        String jpql = "select employee from Employee employee where employee.member = :member and employee.tenant = :tenant";
        try {
            return entityManager.createQuery(jpql, Employee.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("tenant", tenant).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
