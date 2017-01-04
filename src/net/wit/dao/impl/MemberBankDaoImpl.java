/**
 *====================================================
 * 文件名称: MemberBankDaoImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年7月30日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.wit.dao.MemberBankDao;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;

import org.springframework.stereotype.Repository;

/**
 * @ClassName: MemberBankDaoImpl
 * @Description: 会员银行卡
 * @author Administrator
 * @date 2014年7月30日 上午9:03:54
 */
@Repository("memberBankDaoImpl")
public class MemberBankDaoImpl extends BaseDaoImpl<MemberBank, Long> implements MemberBankDao {

	public List<MemberBank> findListByMember(Member member) {
		if (member == null) {
			return new ArrayList<MemberBank>();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberBank> criteriaQuery = criteriaBuilder.createQuery(MemberBank.class);
		Root<MemberBank> root = criteriaQuery.from(MemberBank.class);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		criteriaQuery.select(root);
		return super.findList(criteriaQuery, null, null, null, null);
	}

}
