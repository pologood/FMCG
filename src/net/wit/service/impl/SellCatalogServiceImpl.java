package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SellCatalogDao;
import net.wit.entity.Member;
import net.wit.entity.SellCatalog;
import net.wit.service.SellCatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
@Service("sellCatalogServiceImpl")
public class SellCatalogServiceImpl extends BaseServiceImpl<SellCatalog, Long> implements SellCatalogService {
    @Resource(name = "sellCatalogDaoImpl")
    private SellCatalogDao sellCatalogDao;

    @Resource(name = "sellCatalogDaoImpl")
    public void setBaseDao(SellCatalogDao sellCatalogDao) {
        super.setBaseDao(sellCatalogDao);
    }

    @Transactional(readOnly = true)
    public List<SellCatalog> findList(Member member){
        return sellCatalogDao.findList(member);
    }

    @Transactional(readOnly = true)
    public Page<Map<String,Object>> findMyRecommendProduct(Long id, Pageable pageable){
        return sellCatalogDao.findMyRecommendProduct(id,pageable);
    }
}
