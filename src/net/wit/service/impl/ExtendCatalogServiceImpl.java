/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ExtendCatalogDao;
import net.wit.entity.ExtendCatalog;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.service.ExtendCatalogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * Service - 分享商品
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("extendCatalogServiceImpl")
public class ExtendCatalogServiceImpl extends BaseServiceImpl<ExtendCatalog, Long> implements ExtendCatalogService {
	@Resource(name = "extendCatalogDaoImpl")
	private ExtendCatalogDao extendCatalogDao;

	@Resource(name = "extendCatalogDaoImpl")
	public void setBaseDao(ExtendCatalogDao extendCatalogDaoImpl) {
		super.setBaseDao(extendCatalogDao);
	}

	@Override
	public ExtendCatalog findExtendCatalog(Member member, Tenant tenant, Product product) {

		 return extendCatalogDao.findExtendCatalog(member,tenant,product);
	}

	@Override
	public Page<Map<String, Object>> findExtendCatalog(Member member, Pageable pageable) {
		return extendCatalogDao.findExtendCatalog(member,pageable);
	}

	@Override
	public Page<ExtendCatalog> findPage(Product product,Pageable pageable) {
		return extendCatalogDao.findPage(product,pageable);
	}
}