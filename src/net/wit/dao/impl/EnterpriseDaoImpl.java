/**
 *====================================================
 * 文件名称: EnterpriseDaoImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月22日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.EnterpriseDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Enterprise;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Role;
import net.wit.entity.Role.RoleType;

import org.hibernate.search.util.HibernateHelper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: EnterpriseDaoImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年10月22日 上午9:21:09
 */
@Repository("enterpriseDaoImpl")
public class EnterpriseDaoImpl extends BaseDaoImpl<Enterprise, Long> implements EnterpriseDao {

	public Page<Enterprise> findPage(EnterpriseType enterpriseType,Area area,Pageable pageable) {
		try{
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Enterprise> criteriaQuery = criteriaBuilder.createQuery(Enterprise.class);
		Root<Enterprise> root = criteriaQuery.from(Enterprise.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("enterprisetype"), enterpriseType));
		if(area!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("area"), area));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public Page<Enterprise> findPage(EnterpriseType enterpriseType,Pageable pageable) {
		try{
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Enterprise> criteriaQuery = criteriaBuilder.createQuery(Enterprise.class);
		Root<Enterprise> root = criteriaQuery.from(Enterprise.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("enterprisetype"), enterpriseType));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	public Enterprise findEnterPrise(EnterpriseType enterpriseType,Area area) {
		try{
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Enterprise> criteriaQuery = criteriaBuilder.createQuery(Enterprise.class);
			Root<Enterprise> root = criteriaQuery.from(Enterprise.class);
			criteriaQuery.select(root);
			Predicate restrictions = criteriaBuilder.conjunction();
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("enterprisetype"), enterpriseType));
			if (area!=null) {
			   restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("area"), area));
			}
			criteriaQuery.where(restrictions);
			List<Enterprise> enters = super.findList(criteriaQuery, null, 1, null, null);
			if (enters.size()>0) {
				return enters.get(0);
			} else {
				return null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

}
