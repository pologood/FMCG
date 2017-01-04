package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.SellCatalog;

import java.util.List;
import java.util.Map;

/**
 * 导购推荐商品
 * Created by Administrator on 2016/11/17.
 */
public interface SellCatalogService extends BaseService<SellCatalog, Long>{
    List<SellCatalog> findList(Member member);

    public Page<Map<String,Object>> findMyRecommendProduct(Long id,Pageable pageable);
}
