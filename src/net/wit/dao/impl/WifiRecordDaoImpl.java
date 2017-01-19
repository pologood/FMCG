package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.WifiRecordDao;
import net.wit.entity.Tenant;
import net.wit.entity.WifiRecord;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * wifi
 * @author Administrator
 *
 */
@Repository("wifiRecordDaoImpl")
public class WifiRecordDaoImpl extends BaseDaoImpl<WifiRecord,Long> implements WifiRecordDao {

    @Override
    public List<Map<String,Object>> findList(String datetime,  Tenant tenant) {
        if (tenant == null) {
            return new ArrayList<Map<String,Object>>();
        }
        try {
                String sql = "SELECT hour(a.create_date),COUNT(1) from xx_wifi_record a LEFT JOIN xx_wifi b on b.uid1 = a.wuidd WHERE DATE_FORMAT(a.create_date,'Y%-m%-d%') = DATE_FORMAT('"+datetime+"','Y%-m%-d%') and b.tenant ="+tenant.getId()+"  GROUP BY hour(a.create_date) ORDER BY   hour(a.create_date) asc";
                Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
                List list = query.getResultList();
               List<Map<String, Object>> maps = new ArrayList<>();
               if (list.size() > 0) {
                 for (Object obj : list) {
                    Map<String, Object> map = new HashMap<>();
                    Object[] row = (Object[]) obj;
                    map.put(String.valueOf(row[0]), row[1]);
                    maps.add(map);
                }
            }
            return maps;
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public List<Map<String, Object>> findMemberList(String datetime, Tenant tenant) {
        if (tenant == null) {
            return new ArrayList<Map<String,Object>>();
        }
        try {
            String sql = "SELECT d.name ,count(1) from xx_wifi_record a "+
                        " INNER JOIN xx_bind_user b on a.uuidd = b.username" +
                        " INNER JOIN xx_member c on c.id = b.member" +
                        " INNER JOIN xx_member_rank d ON d.id = c.member_rank "+
                        " WHERE c.tenant = " +tenant.getId();
                        if(datetime!=null){
                           sql+=  " and DATE_FORMAT(a.create_date,'Y%-d%-d%') = DATE_FORMAT('"+datetime+"','Y%-d%-d%')";
                        }
                       sql+= " GROUP BY d.name";
            Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
            List list = query.getResultList();
            List<Map<String, Object>> maps = new ArrayList<>();
            if (list.size() > 0) {
                for (Object obj : list) {
                    Map<String, Object> map = new HashMap<>();
                    Object[] row = (Object[]) obj;
                    map.put(String.valueOf(row[0]), row[1]);
                    maps.add(map);
                }
            }
            return maps;
        } catch (NoResultException e) {
            return null;
        }

    }


    @Override
    public Page<Map<String, Object>> findSummaryPage(Date start_time, Date end_time, Tenant tenant,Pageable pageable) {
        if (tenant == null) {
            return null;
        }
        try {
            String sql = "SELECT c.name ,c.head_img,h.name as vipName,count(1) as visitor ,f.tradeSum from xx_wifi_record a" +
                    " LEFT JOIN xx_bind_user b on a.uuidd = b.username" +
                    " LEFT JOIN xx_member c on b.member = c.id" +
                    " LEFT JOIN xx_member_rank h on h.id = c.member_rank" +
                    " inner JOIN (SELECT count(1) as tradeSum,member from xx_trade d " +
                    " LEFT JOIN xx_order e on d.orders = e.id" +
                    " where d.order_status = 2 GROUP BY e.member) f on f.member = c.id" +
                    " where b.type=6 and c.tenant =" +tenant.getId();
                    if(start_time!=null){
                        sql+=  " and a.create_date >=:start_time" ;
                    }
                    if(end_time!=null){
                        sql+=  " and a.create_date <=:end_time" ;
                    }
                   sql+= " GROUP BY a.uuidd";

            String totalSql = "SELECT count(1) from ("+sql+")w";
            Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
            Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT);
            if(start_time!=null){
                totalQuery.setParameter("start_time",start_time);
            }
            if(end_time!=null){
                totalQuery.setParameter("end_time",end_time);
            }
            if(start_time!=null){
                query.setParameter("start_time",start_time);
            }
            if(end_time!=null){
                query.setParameter("end_time",end_time);
            }

            query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            List list = new ArrayList();
            Long total = 0l;
            try {
                list = query.getResultList();
                total = totalQuery.getSingleResult().equals(0)?0:Long.parseLong(totalQuery.getSingleResult().toString());
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
                    map.put("memberName", row[0]);
                    map.put("headImg", row[1]);
                    map.put("vipName", row[2]);
                    map.put("visitorSum", row[3]);
                    map.put("tradeSum", row[4]);
                    maps.add(map);
                }
            }
            return new Page<Map<String, Object>>(maps, total, pageable);
        } catch (NoResultException e) {
            return null;
        }
    }
}
