package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import org.springframework.stereotype.Service;

import com.fr.web.privilege.core.impl.SuperOrg;

import net.wit.dao.EmployeeDao;
import net.wit.entity.Employee;
import net.wit.service.EmployeeService;

import java.util.List;

@Service("employeeServiceImpl")
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Long> implements EmployeeService{
	@Resource(name = "employeeDaoImpl")
	private EmployeeDao employeeDao;

	@Resource(name = "employeeDaoImpl")
	public void setBaseDao(EmployeeDao employeeDao){
		super.setBaseDao(employeeDao);
	}

	public Page<Employee> findPage(Pageable pageable, Tag tag, String keyWord){
		return employeeDao.findPage(pageable,tag,keyWord);
	}

	public List<Employee> findList(Tenant tenant, Tag tag){
		return employeeDao.findList(tenant,tag);
	}

	public List<Employee> findByDeliveryCenterId(Long id){
		return employeeDao.findByDeliveryCenterId(id);
	}


	public List<Employee> findByMember(Member member){
		return employeeDao.findByMember(member);
	}

	public Employee findMember(Member member,Tenant tenant){
		return employeeDao.findMember(member,tenant);
	}
}
