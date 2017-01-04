package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SellCatalogDao;
import net.wit.entity.Member;
import net.wit.entity.SellCatalog;
import org.springframework.stereotype.Repository;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import java.util.*;

/**
 * Created by Administrator on 2016/11/17.
 */
@Repository("sellCatalogDaoImpl")
public class SellCatalogDaoImpl extends BaseDaoImpl<SellCatalog, Long> implements SellCatalogDao {

    public List<SellCatalog> findList(Member member){
        String jpql = "select sellCatalog from SellCatalog sellCatalog where sellCatalog.member = :member ";
        return entityManager.createQuery(jpql, SellCatalog.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).getResultList();
    }

    public Page<Map<String,Object>> findMyRecommendProduct(Long id, Pageable pageable){
        String sql ="SELECT p.id,p.full_name,p.image,p.price,p.month_sales"+
                    " FROM xx_extend_catalog c inner join xx_product p on c.product=p.id"+
                    " where c.member=:id and p.is_marketable = 1 and p.is_gift<> 1 ";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("id", id);
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
                map.put("full_name", row[1]);
                map.put("image", row[2]);
                map.put("price", row[3]);
                map.put("month_sales", row[4]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }
}
