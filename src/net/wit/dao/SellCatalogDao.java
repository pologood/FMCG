package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.SellCatalog;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
public interface SellCatalogDao extends BaseDao<SellCatalog, Long>{
    List<SellCatalog> findList(Member member);

    Page<Map<String,Object>> findMyRecommendProduct(Long id, Pageable pageable);
}
