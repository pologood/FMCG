/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.dao.GoodsDao;
import net.wit.dao.PackagUnitDao;
import net.wit.dao.ProductDao;
import net.wit.dao.impl.PackagUnitDaoImpl;
import net.wit.entity.Goods;
import net.wit.entity.PackagUnit;
import net.wit.entity.Product;
import net.wit.service.GoodsService;
import net.wit.service.StaticService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 货品
 * @author rsico Team
 * @version 3.0
 */
@Service("goodsServiceImpl")
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Long> implements GoodsService {

	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "packagUnitDaoImpl")
	private PackagUnitDao packagUnitDao;

	@Resource(name = "goodsDaoImpl")
	public void setBaseDao(GoodsDao goodsDao) {
		super.setBaseDao(goodsDao);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void save(Goods goods) {
		Assert.notNull(goods);

		// if (goods.getProducts() != null) {
		// for (Product product : goods.getProducts()) {
		// 单位
		// List<PackagUnit> packagUnits = product.getPackagUnits();
		// ArrayList<PackagUnit> pkus = new ArrayList<PackagUnit>();
		// for (PackagUnit packagUnit : packagUnits) {
		// packagUnit.setProduct(product);
		// packagUnitDao.persist(packagUnit);
		// pkus.add(packagUnit);
		// }
		// product.setPackagUnits(pkus);
		// staticService.build(product);
		// }
		// }
		//super.save(goods);
		goodsDao.persist(goods);
		goodsDao.flush();
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Goods update(Goods goods) {
		Assert.notNull(goods);
		//
		// Set<Product> excludes = new HashSet<Product>();
		// CollectionUtils.select(goods.getProducts(), new Predicate() {
		// public boolean evaluate(Object object) {
		// Product product = (Product) object;
		// return product != null && product.getId() != null;
		// }
		// }, excludes);
		// List<Product> products = productDao.findList(goods, excludes);
		// for (Product product : products) {
		// staticService.delete(product);
		// }
		goodsDao.merge(goods);
		goodsDao.flush();
		// if (pGoods.getProducts() != null) {
		// for (Product product : pGoods.getProducts()) {
		// // staticService.build(product);
		// }
		// }
		return goods;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Goods update(Goods goods, String... ignoreProperties) {
		return super.update(goods, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Goods goods) {
		if (goods != null && goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {

				//staticService.delete(product);
			}
		}
		super.delete(goods);
	}

}