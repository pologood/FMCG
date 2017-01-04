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

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.MemberBankDao;
import net.wit.dao.MemberCapitalDao;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.MemberCapital;
import net.wit.entity.PlatformCapital;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: MemberCapitalDaoImpl
 * @Description:
 * @author Administrator
 * @date 2014年7月30日 上午9:03:54
 */
@Repository("memberCapitalDaoImpl")
public class MemberCapitalDaoImpl extends BaseDaoImpl<MemberCapital, Long> implements MemberCapitalDao {
    public Page<MemberCapital> findPageByDate(Date begin_date, Date end_date, String keyword,Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MemberCapital> criteriaQuery = criteriaBuilder.createQuery(MemberCapital.class);
        Root<MemberCapital> root = criteriaQuery.from(MemberCapital.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(begin_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),begin_date));
        }
        if(end_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),end_date));
        }
        if (StringUtils.isNotBlank(keyword)) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("member").<String> get("name"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("member").<String> get("username"), "%" + keyword + "%")
                            ));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<MemberCapital> findListByDate(Date begin_date, Date end_date, String keyword,List<Filter> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MemberCapital> criteriaQuery = criteriaBuilder.createQuery(MemberCapital.class);
        Root<MemberCapital> root = criteriaQuery.from(MemberCapital.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(begin_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),begin_date));
        }
        if(end_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),end_date));
        }
        if (StringUtils.isNotBlank(keyword)) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("member").<String> get("name"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("member").<String> get("username"), "%" + keyword + "%")
                    ));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, filters, null);
    }

}
