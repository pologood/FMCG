package net.wit.dao.impl;

import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TaskDao;
import net.wit.entity.Member;
import net.wit.entity.Task;
import net.wit.entity.Tenant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanx on 2016/11/4.
 */
@Repository("taskDaoImpl")
public class TaskDaoImpl extends BaseDaoImpl<Task, Long> implements TaskDao {

    public Task findByMember(Member member, Long month) {
        if (member == null || month ==null) {
            return null;
        }
        String jpql = "select task from Task task where task.member = :member and task.month = :month";
        try {
            return entityManager.createQuery(jpql, Task.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Map<String, Object> findByTenant(Member member,Tenant tenant, Long month) {
        if (tenant == null || month==null) {
            return null;
        }
        //try {
        //String jpql = "select sum(task.coupon),sum(task.sale),sum(task.share),sum(task.invite),sum(task.do_coupon),sum(task.do_sale),sum(task.do_share),sum(task.do_invite) FROM xx_task task where task.tenant = :tenant and task.month = :month";
        //return entityManager.createQuery(jpql, Task.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();

        Map<String, Object> map = new HashMap<String, Object>();
        try {

            for (int i = 0 ; i <8 ; i++ ) {
                if(member!=null){
                    if (i == 0) {
                        String jpql = "select task.coupon FROM Task task where task.member = :member and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("coupon", total == null ? 0 : total);
                    } else if (i == 1) {
                        String jpql = "select task.doCoupon FROM Task task where task.member = :member and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("doCoupon", total == null ? 0 : total);
                    } else if (i == 2) {
                        String jpql = "select task.share FROM Task task where task.member = :member and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("share", total == null ? 0 : total);
                    } else if (i == 3) {
                        String jpql = "select task.doShare FROM Task task where task.member = :member and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("doShare", total == null ? 0 : total);
                    } else if (i == 4) {
                        String jpql = "select task.invite FROM Task task where task.member = :member and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("invite", total == null ? 0 : total);
                    } else if (i == 5) {
                        String jpql = "select task.doInvite FROM Task task where task.member = :member and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("doInvite", total == null ? 0 : total);
                    } else if (i == 6) {
                        String jpql = "select task.sale FROM Task task where task.member = :member and task.month = :month";
                        BigDecimal total = entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("sale", total == null ? 0 : total);
                    } else if (i == 7) {
                        String jpql = "select task.doSale FROM Task task where task.member = :member and task.month = :month";
                        BigDecimal total = entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("month", month).getSingleResult();
                        map.put("doSale", total == null ? 0 : total);
                    } else {

                    }
                }else {
                    if (i == 0) {
                        String jpql = "select sum(task.coupon) FROM Task task where task.tenant = :tenant and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("coupon", total == null ? 0 : total);
                    } else if (i == 1) {
                        String jpql = "select sum(task.doCoupon) FROM Task task where task.tenant = :tenant and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("doCoupon", total == null ? 0 : total);
                    } else if (i == 2) {
                        String jpql = "select sum(task.share) FROM Task task where task.tenant = :tenant and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("share", total == null ? 0 : total);
                    } else if (i == 3) {
                        String jpql = "select sum(task.doShare) FROM Task task where task.tenant = :tenant and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("doShare", total == null ? 0 : total);
                    } else if (i == 4) {
                        String jpql = "select sum(task.invite) FROM Task task where task.tenant = :tenant and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("invite", total == null ? 0 : total);
                    } else if (i == 5) {
                        String jpql = "select sum(task.doInvite) FROM Task task where task.tenant = :tenant and task.month = :month";
                        Long total = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("doInvite", total == null ? 0 : total);
                    } else if (i == 6) {
                        String jpql = "select sum(task.sale) FROM Task task where task.tenant = :tenant and task.month = :month";
                        BigDecimal total = entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("sale", total == null ? 0 : total);
                    } else if (i == 7) {
                        String jpql = "select sum(task.doSale) FROM Task task where task.tenant = :tenant and task.month = :month";
                        BigDecimal total = entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
                        map.put("doSale", total == null ? 0 : total);
                    } else {

                    }
                }
            }
            return map;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Page<Task> findPage(Tenant tenant, Long month, String type, Pageable pageable, String order) {
        if (tenant == null) {
            return new Page<Task>(Collections.<Task> emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
        Root<Task> root = criteriaQuery.from(Task.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("month"), month));
        List<Order> orders = pageable.getOrders();
        orders.clear();
        if ("1".equals(type)&&"1".equals(order)) {
            orders.add(Order.desc("doSale"));
        } else if ("2".equals(type)&&"1".equals(order)) {
            orders.add(Order.desc("doShare"));
        } else if ("3".equals(type)&&"1".equals(order)) {
            orders.add(Order.desc("doInvite"));
        }else if ("4".equals(type)&&"1".equals(order)) {
            orders.add(Order.desc("doCoupon"));
        } else if ("1".equals(type)&&"0".equals(order)) {
            orders.add(Order.asc("doSale"));
        } else if ("2".equals(type)&&"0".equals(order)) {
            orders.add(Order.asc("doShare"));
        }else if ("3".equals(type)&&"0".equals(order)) {
            orders.add(Order.asc("doInvite"));
        }else if ("4".equals(type)&&"0".equals(order)) {
            orders.add(Order.asc("doCoupon"));
        } else {
            orders.add(Order.desc("doSale"));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public BigDecimal sum(String sumParameter,Member member, Tenant tenant, Long month) {

        String jpql = "select sum(task."+sumParameter+") FROM Task task where task.member =:member and task.tenant = :tenant";
        if(month!=null){
        jpql+=" and task.month = :month";
        }
        Object result = null;
        if(month!=null){
            result  = entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("member",member).setParameter("tenant", tenant).setParameter("month", month).getSingleResult();
        }else{
            result =  entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("member",member).setParameter("tenant", tenant).getSingleResult();
        }
        if(result==null){
            return new BigDecimal(0);
        }else{
            return new BigDecimal(result.toString());
        }


    }
}

