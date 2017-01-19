package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tenant;
import net.wit.entity.Union;

import java.util.List;

/**
 * 商盟
 * @author Administrator
 *
 */
public interface UnionService extends BaseService<Union,Long>{


	Page<Union> findPage(Pageable pageable);

	/**
	 * 查詢所有商家联盟
	 * @param pageable
	 *            分页信息
     * @return
     */
	Page<Union> findPage(String keyword, Pageable pageable);

	Page<Tenant> findTenant(Union union,Pageable pageable);
}
