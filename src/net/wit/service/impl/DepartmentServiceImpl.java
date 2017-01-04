/**
 *====================================================
 * 文件名称: DepartmentServiceImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月22日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.dao.DepartmentDao;
import net.wit.entity.Department;
import net.wit.service.DepartmentService;

import org.springframework.stereotype.Service;

/**
 * @ClassName: DepartmentServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年10月22日 上午9:25:38
 */
@Service("departmentServiceImpl")
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

	@Resource(name = "departmentDaoImpl")
	private DepartmentDao departmentDao;

	@Resource(name = "departmentDaoImpl")
	public void setBaseDao(DepartmentDao departmentDao) {
		super.setBaseDao(departmentDao);
	}

}
