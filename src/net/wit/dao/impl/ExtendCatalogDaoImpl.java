/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ExtendCatalogDao;
import net.wit.entity.ExtendCatalog;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.service.ExtendCatalogService;
import net.wit.service.impl.BaseServiceImpl;
import org.apache.batik.css.engine.value.StringValue;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Service - 分享商品
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("extendCatalogDaoImpl")
public class ExtendCatalogDaoImpl extends BaseDaoImpl<ExtendCatalog, Long> implements ExtendCatalogDao {

    @Override
    public ExtendCatalog findExtendCatalog(Member member, Tenant tenant, Product product) {

        if (member == null) {
            return null;
        }
        if (tenant == null) {
            return null;
        }
        if (product == null) {
            return null;
        }
        String jpql = "select extendCatalog from ExtendCatalog extendCatalog where extendCatalog.member = :member and extendCatalog.tenant=:tenant and extendCatalog.product=:product";
        try {
            return entityManager.createQuery(jpql, ExtendCatalog.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("tenant", tenant).setParameter("product", product).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Page<Map<String, Object>> findExtendCatalog(Member member, Pageable pageable) {
        if (member == null) {
            return null;
        }
        String sql ="SELECT a.id,a.member,a.name ,a.head_img,(SELECT SUM(e.times) FROM xx_extend_catalog e WHERE e.member=:member) as times,w.volumes,w.totalPrice as salsePrices,w.brokerage ,d.name as memberRank" +
                "  from  xx_member a" +
                " LEFT JOIN (SELECT f.member,sum(f.brokerage*n.totalPrice) as brokerage,n.volumes,n.totalPrice  from xx_rebate f " +
                " LEFT JOIN xx_trade g on g.id = f.trade" +
                " LEFT JOIN (SELECT m.trades,SUM(m.price*m.quantity)as totalPrice ,count(1)as volumes from xx_order_item m GROUP BY m.trades) n on n.trades = g.id" +
                " LEFT JOIN xx_order m on m.id = g.orders" +
                " WHERE m.extension=:member and f.order_type='1' and(f.type='1' or f.type='2')" +
                " GROUP BY f.member) w on w.member = a.id" +
                "  LEFT JOIN xx_member_rank d on d.id = a.member_rank" +
                "  where a.member=:member ";
        String totalSql = "  SELECT count(1) from (SELECT a.id,a.member,a.name ,a.head_img,(SELECT SUM(e.times) FROM xx_extend_catalog e WHERE e.member=:member) as times,w.volumes,w.totalPrice as salsePrices,w.brokerage ,d.name as memberRank " +
                "  from  xx_member a " +
                " LEFT JOIN (SELECT f.member,sum(f.brokerage*n.totalPrice) as brokerage,n.volumes,n.totalPrice  from xx_rebate f  " +
                " LEFT JOIN xx_trade g on g.id = f.trade " +
                " LEFT JOIN (SELECT m.trades,SUM(m.price*m.quantity)as totalPrice ,count(1)as volumes from xx_order_item m GROUP BY m.trades) n on n.trades = g.id " +
                " LEFT JOIN xx_order m on m.id = g.orders\n" +
                " WHERE m.extension=:member and f.order_type='1' and(f.type='1' or f.type='2') " +
                " GROUP BY f.member) w on w.member = a.id " +
                "  LEFT JOIN xx_member_rank d on d.id = a.member_rank " +
                "  where a.member=:member ) z";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("member",member.getId());
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("member",member.getId());
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List list = new ArrayList();
        Long total = 0l;
        try {
            list = query.getResultList();
            total = Long.parseLong(totalQuery.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
        if (totalPages < pageable.getPageNumber()) {
            return new Page<Map<String, Object>>(Collections.<Map<String, Object>>emptyList(), total, pageable);
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        if (list.size() > 0) {
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("id", row[0]);
                map.put("name", row[2]);
                map.put("headImg", row[3]);
                map.put("times", row[4]);
                map.put("volumes", row[5]);
                map.put("salsePrices", row[6]);
                map.put("brokerages", row[7]);
                map.put("memberRank",row[8]);
                if(row[5]!=null&&row[4]!=null){
                   DecimalFormat df = new DecimalFormat("#0.00");
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(row[4]));
                    BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(row[5]));
                    map.put("proportion", df.format(bigDecimal2.divide(bigDecimal,2, BigDecimal.ROUND_HALF_EVEN)));
                }else{
                    map.put("proportion", null);
                }
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }

    @Override
    public Page<ExtendCatalog> findPage(Product product, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExtendCatalog> criteriaQuery = criteriaBuilder.createQuery(ExtendCatalog.class);
        Root<ExtendCatalog> root = criteriaQuery.from(ExtendCatalog.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.get("product"),product));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
}