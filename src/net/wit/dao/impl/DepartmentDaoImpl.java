/**
 *====================================================
 * 文件名称: DepartmentDaoImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月22日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.dao.impl;

import net.wit.dao.DepartmentDao;
import net.wit.entity.Department;

import org.springframework.stereotype.Repository;

/**
 * @ClassName: DepartmentDaoImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年10月22日 上午9:23:03
 */
@Repository("departmentDaoImpl")
public class DepartmentDaoImpl extends BaseDaoImpl<Department, Long> implements DepartmentDao {

}
