package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tenant;
import net.wit.entity.Union;

import java.util.List;

public interface UnionDao extends BaseDao<Union,Long>{
    Page<Union> findPage(Pageable pageable);
    /**
     * 查詢所有商家联盟
     * @param pageable
     *            分页信息
     * @return
     */
    Page<Union> findPage(Union.Type type,String keyword, Pageable pageable);

    Page<Tenant> findTenant(Union union, Pageable pageable);
}
