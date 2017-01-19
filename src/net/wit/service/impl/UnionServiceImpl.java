package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.UnionDao;
import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.service.UnionService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 货郎联盟
 * @author Administrator
 *
 */
@Service("unionServiceImpl")
public class UnionServiceImpl extends BaseServiceImpl<Union,Long> implements UnionService{

	@Resource(name = "unionDaoImpl")
	public void setBaseDao(UnionDao unionDao) {
		super.setBaseDao(unionDao);
	}

	@Resource(name = "unionDaoImpl")
	private UnionDao unionDao;
	@Transactional(readOnly = true)
	public Page<Union> findPage(Pageable pageable){
		return unionDao.findPage(pageable);
	}
	@Transactional(readOnly = true)
	public Page<Union> findPage(String keyword, Pageable pageable){
		return unionDao.findPage(keyword,pageable);
	}

	@Transactional(readOnly = true)
	public Page<Tenant> findTenant(Union union, Pageable pageable){
		return unionDao.findTenant(union,pageable);
	}
}
