package net.wit.dao.impl;

import com.fr.base.Inter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TenantRulesRoleDao;
import net.wit.entity.*;
import org.nutz.dao.sql.Sql;
import org.springframework.expression.Expression;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.List;

@Repository("tenantRulesRoleDaoImpl")
public class TenantRulesRoleDaoImpl extends BaseDaoImpl<TenantRulesRole, Long> implements TenantRulesRoleDao {
    public Page<TenantRulesRole> openPage(Pageable pageable, String keyWord) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TenantRulesRole> criteriaQuery = criteriaBuilder.createQuery(TenantRulesRole.class);
        Root<TenantRulesRole> root = criteriaQuery.from(TenantRulesRole.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (keyWord != null) {
//            restrictions = criteriaBuilder.and(
//                    restrictions, criteriaBuilder.or(
//                            criteriaBuilder.like(root.get("rules").<String>get("oper"), "%" + keyWord + "%"),
//                            criteriaBuilder.like(root.get("rules").<String>get("url"),"%" + keyWord + "%" )
//                    )
//            );
//        restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("rules").<String>get("url"),"%" + keyWord + "%" ));
        }

        criteriaQuery.where(restrictions);

        return super.findPage(criteriaQuery, pageable);
    }


    public List<TenantRulesRole> openList(Inter count, Role role, TenantRules tenantRules) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TenantRulesRole> criteriaQuery = criteriaBuilder.createQuery(TenantRulesRole.class);
        Root<TenantRulesRole> root = criteriaQuery.from(TenantRulesRole.class);
//        Root<TenantRules> rootTenantRules = criteriaQuery.from(TenantRules.class);
//        Root<Role> rootRole = criteriaQuery.from(Role.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
//        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("role").<String>get("id"),rootRole.<String>get("id")));
//        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("rules").<String>get("id"),rootTenantRules.<String>get("id")));
//        restrictions =  criteriaBuilder.add(new Sql(""));
//        if (keyWord != null) {
//            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("member").<String>get("username"), "%" + keyWord + "%"));
//        }
        criteriaQuery.where(restrictions);

        return super.findList(null, 0, null, null, null);
    }

    public TenantRulesRole find(Role role, TenantRules tenantRules) {
        try {
            String jpql = "select tenantRulesRole from TenantRulesRole tenantRulesRole ,TenantRules tenantRules,Role " +
                    "  role where tenantRulesRole.role.id=role.id and tenantRulesRole.rules.id=tenantRules.id  " +
                    "and " +
                    " role=:roleId and " +
                    " tenantRules=:rulesId " +
                    " order" +
                    " by tenantRulesRole.id asc";
            List<TenantRulesRole> tenantRulesRole = entityManager.createQuery(jpql, TenantRulesRole.class)
                    .setParameter("roleId", role)
                    .setParameter("rulesId", tenantRules).getResultList();

            if (tenantRulesRole.size() > 0)
                return tenantRulesRole.get(0);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public List<TenantRulesRole> findByRulesId(Long id) {
        try {
            String jpql = "select tenantRulesRole from TenantRulesRole tenantRulesRole ,TenantRules tenantRules,Role " +
                    "  role where tenantRulesRole.role.id=role.id and tenantRulesRole.rules.id=tenantRules.id and " +
                    "tenantRules.id=:rulesId order" +
                    " by tenantRulesRole.id asc";
            List<TenantRulesRole> tenantRulesRole = entityManager.createQuery(jpql, TenantRulesRole.class).setParameter("rulesId", id).getResultList();

            return tenantRulesRole;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<TenantRulesRole> findByRoleId(Long id, String url,String type) {
        try {


            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<TenantRulesRole> criteriaQuery = criteriaBuilder.createQuery(TenantRulesRole.class);
            Root<TenantRulesRole> root = criteriaQuery.from(TenantRulesRole.class);
            criteriaQuery.select(root);
            Predicate restrictions = criteriaBuilder.conjunction();
            if (url!=null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("rules").<String>get("url"), url));
            }
            if (type!=null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(type + "Authority"), true));
            }
            restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("role").<String>get("id"),id));
            criteriaQuery.where(restrictions);

            return super.findList(criteriaQuery, null, null, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Long> findRulesIdsByRole(Role role, String type) {
        try {
            String jpql = "select tenantRulesRole.rules.id from TenantRulesRole tenantRulesRole  where " ;
            boolean isHave=false;
            if(type!=null) {
                for (TenantRules.Type type1 : TenantRules.Type.values()) {

                    if (type1.name().equals(type)) {
                        jpql += "tenantRulesRole."+type1.name()+"Authority=true ";
                        isHave=true;
                        break;
                    }
                }
            }

            if (type==null||!isHave){
                jpql += " 1=1 ";
            }

            jpql += "and tenantRulesRole.role=:roleId   order  by tenantRulesRole.id asc";
            List<Long> rulesIds = entityManager.createQuery(jpql, Long.class).setParameter("roleId", role).getResultList();

            return rulesIds;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
