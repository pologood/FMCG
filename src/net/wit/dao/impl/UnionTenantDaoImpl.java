package net.wit.dao.impl;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.UnionDao;
import net.wit.dao.UnionTenantDao;
import net.wit.entity.*;
import net.wit.support.TenantComparatorByDistance;
import net.wit.support.TenantDefaultComparatorByDistance;
import org.springframework.stereotype.Repository;
import net.wit.entity.Tenant.OrderType;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by Administrator on 2016/11/14.
 */
@Repository("unionTenantDaoImpl")
public class UnionTenantDaoImpl extends BaseDaoImpl<UnionTenant,Long> implements UnionTenantDao {
    public Page<Map<String,Object>> findTenant(Union union, Pageable pageable,OrderType orderType) {
        String sql ="SELECT t.id,t.name,t.thumbnail,t.score FROM xx_tenant t inner join xx_union_tenant u on t.id=u.tenant where u.unions=:union and u.status =2 ";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("union", union.getId());
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List list = new ArrayList();
        Long total = 0l;
        try {
            list = query.getResultList();
            total = (long)list.size();
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
                map.put("name", row[1]);
                map.put("thumbnail", row[2]);
                map.put("score", row[3]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }
    //
    public Page<UnionTenant> findPage(Union union, UnionTenant.Status status, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnionTenant> criteriaQuery = criteriaBuilder.createQuery(UnionTenant.class);
        Root<UnionTenant> root = criteriaQuery.from(UnionTenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(union!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("unions"),union));
        }
        if(status!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"),status));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Map<String,Object>> findTenantByPad(Equipment equipment, Pageable pageable, OrderType orderType) {
        String sql ="SELECT t.id,t.name,t.thumbnail,t.score FROM xx_tenant t inner join xx_union_tenant u on t.id=u.tenant where u.equipment=:equipment and u.status =2 ";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("equipment", equipment.getId());
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List list = new ArrayList();
        Long total = 0l;
        try {
            list = query.getResultList();
            total = (long)list.size();
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
                map.put("name", row[1]);
                map.put("thumbnail", row[2]);
                map.put("score", row[3]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }

    public List<UnionTenant> findUnionTenant(Union union, Tenant tenant, List<Filter> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnionTenant> criteriaQuery = criteriaBuilder.createQuery(UnionTenant.class);
        Root<UnionTenant> root = criteriaQuery.from(UnionTenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(union!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("unions"),union));
        }
        if(tenant!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery,null,null,filters,null);
    }

    public  Page<Map<String,Object>> findUnionTenantPage(Tenant tenant,Pageable pageable) {
        if (tenant == null ) {
            return null;
        }
        String sql ="select a.id,a.name,a.thumbnail,a.address,a.generalize,a.score,f.count,w.pr_type,g.name as cateName FROM xx_tenant a" +
                " INNER JOIN " +
                " (SELECT e.tenant,count(tenant) as count from xx_product e" +
                " GROUP BY e.tenant) f on f.tenant = a.id" +
                " left join (SELECT group_concat(type) as pr_type ,tenant from xx_promotion where end_date >= NOW()  GROUP BY tenant) w on w.tenant = a.id "+
                " LEFT JOIN xx_tenant_category g ON g.id = a.tenant_category"+
                " WHERE a.id in(SELECT c.tenant from xx_union_tenant c where c.unions = " +
                " (SELECT b.unions from xx_union_tenant b where b.tenant =:tenant)) and a.id not in(:tenant) ";
       String totalSql ="select count(1) FROM xx_tenant a" +
                " INNER JOIN " +
                " (SELECT e.tenant,count(tenant) as count from xx_product e" +
                " GROUP BY e.tenant) f on f.tenant = a.id" +
                " left join (SELECT group_concat(type) as pr_type ,tenant from xx_promotion where end_date >= NOW()  GROUP BY tenant) w on w.tenant = a.id "+
                " WHERE a.id in(SELECT c.tenant from xx_union_tenant c where c.unions = " +
                " (SELECT b.unions from xx_union_tenant b where b.tenant =:tenant)) and a.id not in(:tenant) ";

        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId());
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
                map.put("name", row[1]);
                map.put("thumbnail", row[2]);
                map.put("address", row[3]);
                map.put("generalize", row[4]);
                map.put("score", row[5]);
                map.put("count", row[6]);
                map.put("types", row[7]);
                map.put("cateName", row[8]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }

    public Page<Map<String,Object>> findPage(Tenant tenant ,String status,Pageable pageable){
        int num ;
        if(status.equals("unconfirmed")){
            num=0;
        }else if(status.equals("freezed")){
            num=1;
        }else if(status.equals("confirmed")){
            num=2;
        }else{
            num=3;
        }
        String sql = "select t.name ,t.address,un.brokerage,un.name as uname,u.volume,u.pay,u.id from xx_tenant t inner join xx_equipment e on (e.tenant=t.id) " +
                "inner join  xx_union_tenant u on (u.equipment=e.id) inner join xx_union un on(u.unions=un.id)  where u.status=:status and u.tenant = :tenant";

        String totalSql = "select count(0) from xx_tenant t inner join xx_equipment e on (e.tenant=t.id) inner join  xx_union_tenant u on (u.equipment=e.id) where u.status=:status and u.tenant = :tenant";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("status", num).setParameter("tenant",tenant);
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("status", num).setParameter("tenant",tenant);
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
        if(list.size()>0){
            for (Object obj :list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[])obj;
                map.put("name",row[0]);
                map.put("address",row[1]);
                map.put("brokerage",row[2]);
                map.put("uname",row[3]);
                map.put("volume",row[4]);
                map.put("pay",row[5]);
                map.put("id",row[6]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps,total,pageable);
    }

    public Page<Map<String,Object>> findPage(Equipment equipment ,String status,Pageable pageable){
        int num =0;
        if(status.equals("unconfirmed")){
            num=0;
        }else if(status.equals("freezed")){
            num=1;
        }else if(status.equals("confirmed")){
            num=2;
        }else{
            num=3;
        }
        String sql = "select distinct t.name ,t.address,u.pay,u.sales,u.volume,un.brokerage,un.name as uname,u.id from xx_tenant t inner join xx_equipment e on (e.tenant=t.id) inner join  xx_union_tenant u on (u.tenant=e.tenant) inner join xx_union un on(u.unions=un.id) where u.status=:status and u.equipment = :equipment";

        String totalSql = "select distinct count(0) from xx_tenant t inner join xx_equipment e on (e.tenant=t.id) inner join  xx_union_tenant u on (u.tenant=e.tenant) inner join xx_union un on(u.unions=un.id) where u.status=:status and u.equipment = :equipment";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("status", status).setParameter("equipment",equipment);

        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("status", status).setParameter("equipment",equipment);
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
        if(list.size()>0){
            for (Object obj :list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[])obj;
                map.put("name",row[0]);
                map.put("address",row[1]);
                map.put("pay",row[2]);
                map.put("sales",row[3]);
                map.put("volume",row[4]);
                map.put("brokerage",row[5]);
                map.put("uname",row[6]);
                map.put("id",row[7]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps,total,pageable);
    }
    public Long findUnionTenant(Equipment equipment,Tenant tenant){
        String totalSql="SELECT count(0) FROM xx_union_tenant where equipment=:equipment and tenant=:tenant and status=:status";
        Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("equipment", equipment).setParameter("tenant",tenant).setParameter("status","canceled");
        Long total = null;
        try {
            total = Long.parseLong(totalQuery.getSingleResult().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return total;
    }

    @Override
    public Long count(Equipment equipment, Tenant tenant, UnionTenant.Status status) {
        String hpql = "select count(ut) from UnionTenant ut where 1=1";
        if(equipment !=null){
            hpql+= " and ut.equipment=:equipment";
        }
        if(tenant!=null){
            hpql+= " and ut.tenant=:tenant";
        }
        if(status!=null){
            hpql+= " and ut.status=:status";
        }
        Query query = entityManager.createQuery(hpql).setFlushMode(FlushModeType.COMMIT);

        if(equipment !=null){
            query.setParameter("equipment", equipment);
        }
        if(tenant!=null){
            query.setParameter("tenant", tenant);
        }
        if(status!=null){
            query.setParameter("status", status);
        }
        try {
            Object result = query.getSingleResult();
            if(result!=null){
              return Long.parseLong(result.toString());
            }else{
                return 0l;
            }

        } catch (NumberFormatException e) {
           return 0l;
        }
    }

    @Override
    public Page<UnionTenant> findUnionTenantPage(Equipment equipment, Tenant tenant, UnionTenant.Status status,Union union , Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnionTenant> criteriaQuery = criteriaBuilder.createQuery(UnionTenant.class);
        Root<UnionTenant> root = criteriaQuery.from(UnionTenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(equipment!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("equipment"),equipment));
        }
        if(tenant!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));
        }
        if(status!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"),status));
        }
        if(union!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("unions"),union));
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sales")));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery,pageable);
    }
    @Override
    public List<UnionTenant> findUnionTenantList(Equipment equipment, Tenant tenant, UnionTenant.Status status,Union union) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UnionTenant> criteriaQuery = criteriaBuilder.createQuery(UnionTenant.class);
        Root<UnionTenant> root = criteriaQuery.from(UnionTenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(equipment!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("equipment"),equipment));
        }
        if(tenant!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),tenant));
        }
        if(status!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"),status));
        }
        if(union!=null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("unions"),union));
        }
        if(equipment==null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.get("equipment")));
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sales")));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery,null,null,null,null);
    }
}
