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
import net.wit.dao.PlatformCapitalDao;
import net.wit.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: PlatformCapitalDaoImpl
 * @Description:
 * @author Administrator
 * @date 2014年7月30日 上午9:03:54
 */
@Repository("platformCapitalDaoImpl")
public class PlatformCapitalDaoImpl extends BaseDaoImpl<PlatformCapital, Long> implements PlatformCapitalDao {

    public Page<PlatformCapital> findPageByDate( Date begin_date, Date end_date, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlatformCapital> criteriaQuery = criteriaBuilder.createQuery(PlatformCapital.class);
        Root<PlatformCapital> root = criteriaQuery.from(PlatformCapital.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(begin_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),begin_date));
        }
        if(end_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),end_date));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<PlatformCapital> findListByDate(Date begin_date, Date end_date, List<Filter> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PlatformCapital> criteriaQuery = criteriaBuilder.createQuery(PlatformCapital.class);
        Root<PlatformCapital> root = criteriaQuery.from(PlatformCapital.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(begin_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.greaterThan(root.<Date>get("createDate"),begin_date));
        }
        if(end_date!=null){
            restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date>get("createDate"),end_date));
        }
        criteriaQuery.where(restrictions);

        return super.findList(criteriaQuery, null, null, filters, null);
    }


}
