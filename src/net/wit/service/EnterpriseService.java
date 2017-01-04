/**
 *====================================================
 * 文件名称: EnterpriseService.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月22日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.service;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.EnterpriseDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Enterprise;
import net.wit.entity.Enterprise.EnterpriseType;

/**
 * @ClassName: EnterpriseService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年10月22日 上午9:24:07
 */
public interface EnterpriseService extends BaseService<Enterprise, Long> {

	Page<Enterprise> findPage(EnterpriseType enterpriseType,Area area,Pageable pageable);
	
	Page<Enterprise> findPage(EnterpriseType enterpriseType,Pageable pageable);
	
	Page<Enterprise> findPage(Admin admin,Pageable pageable);
	
	void charge(Enterprise currentEnterprise, Enterprise chargeEnterprise, int snNumber, String operator) throws Exception;
}
