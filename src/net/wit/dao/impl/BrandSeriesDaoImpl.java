/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import net.wit.dao.BrandSeriesDao;
import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 品牌
 * @author rsico Team
 * @version 3.0
 */
@Repository("brandSeriesDaoImpl")
public class BrandSeriesDaoImpl extends BaseDaoImpl<BrandSeries, Long> implements BrandSeriesDao {

	public List<BrandSeries> findRoots(Integer count) {
		String jpql = "select brandSeries from BrandSeries brandSeries where brandSeries.parent is null order by brandSeries.order asc";
		TypedQuery<BrandSeries> query = entityManager.createQuery(jpql, BrandSeries.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<BrandSeries> findParents(BrandSeries brandSeries, Integer count) {
		if (brandSeries == null || brandSeries.getParent() == null) {
			return Collections.<BrandSeries> emptyList();
		}
		String jpql = "select brandSeries from BrandSeries brandSeries where brandSeries.id in (:ids) order by brandSeries.grade asc";
		TypedQuery<BrandSeries> query = entityManager.createQuery(jpql, BrandSeries.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", brandSeries.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<BrandSeries> findTree(BrandSeries brandSeries, Integer count) {
		TypedQuery<BrandSeries> query;
		if (brandSeries != null) {
			String jpql = "select brandSeries from BrandSeries brandSeries where brandSeries.treePath like :treePath or brandSeries.id = :id order by brandSeries.order asc";
			query = entityManager.createQuery(jpql, BrandSeries.class).setFlushMode(FlushModeType.COMMIT)
					.setParameter("treePath", "%" + BrandSeries.TREE_PATH_SEPARATOR + brandSeries.getId() + BrandSeries.TREE_PATH_SEPARATOR + "%")
					.setParameter("id", brandSeries.getId() );
		} else {
			String jpql = "select brandSeries from BrandSeries brandSeries order by brandSeries.order asc";
			query = entityManager.createQuery(jpql, BrandSeries.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), null);
	}
	
	public List<BrandSeries> findChildren(BrandSeries brandSeries, Integer count) {
		TypedQuery<BrandSeries> query;
		if (brandSeries != null) {
			String jpql = "select brandSeries from BrandSeries brandSeries where brandSeries.treePath like :treePath and grade=:grade order by brandSeries.order asc";
			query = entityManager.createQuery(jpql, BrandSeries.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + BrandSeries.TREE_PATH_SEPARATOR + brandSeries.getId() + BrandSeries.TREE_PATH_SEPARATOR + "%")
					.setParameter("grade", brandSeries.getGrade() + 1);
		} else {
			String jpql = "select brandSeries from BrandSeries brandSeries order by brandSeries.order asc";
			query = entityManager.createQuery(jpql, BrandSeries.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), brandSeries);
	}

	/**
	 * 查找下级系列
	 * 
	 * @param brandSeries
	 *            系列
	 * @param count
	 *            数量
	 * @return 下级系列
	 */
	public List<BrandSeries> findbyBrand(Brand brand, Integer count, String cacheRegion) {
		if (brand == null) {
			return Collections.<BrandSeries> emptyList();
		}
		String jpql = "select brandSeries from BrandSeries brandSeries where brandSeries.brand=:brand and brandSeries.grade=0 order by brandSeries.grade asc";
		TypedQuery<BrandSeries> query = entityManager.createQuery(jpql, BrandSeries.class).setFlushMode(FlushModeType.COMMIT).setParameter("brand",brand);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}
	
	/**
	 * 设置treePath、grade并保存
	 * @param brandSeries 文章分类
	 */
	@Override
	public void persist(BrandSeries brandSeries) {
		Assert.notNull(brandSeries);
		setValue(brandSeries);
		super.persist(brandSeries);
	}

	/**
	 * 设置treePath、grade并更新
	 * @param brandSeries 文章分类
	 * @return 文章分类
	 */
	@Override
	public BrandSeries merge(BrandSeries brandSeries) {
		Assert.notNull(brandSeries);
		setValue(brandSeries);
		for (BrandSeries category : findChildren(brandSeries, null)) {
			setValue(category);
		}
		return super.merge(brandSeries);
	}

	/**
	 * 排序文章分类
	 * @param brandSerieses 文章分类
	 * @param parent 上级文章分类
	 * @return 文章分类
	 */
	private List<BrandSeries> sort(List<BrandSeries> brandSerieses, BrandSeries parent) {
		List<BrandSeries> result = new ArrayList<BrandSeries>();
		if (brandSerieses != null) {
			for (BrandSeries brandSeries : brandSerieses) {
				if ((brandSeries.getParent() != null && brandSeries.getParent().equals(parent)) || (brandSeries.getParent() == null && parent == null)) {
					result.add(brandSeries);
					result.addAll(sort(brandSerieses, brandSeries));
				}
			}
		}
		return result;
	}

	/**
	 * 设置值
	 * @param brandSeries 文章分类
	 */
	private void setValue(BrandSeries brandSeries) {
		if (brandSeries == null) {
			return;
		}
		BrandSeries parent = brandSeries.getParent();
		if (parent != null) {
			brandSeries.setTreePath(parent.getTreePath() + parent.getId() + BrandSeries.TREE_PATH_SEPARATOR);
		} else {
			brandSeries.setTreePath(BrandSeries.TREE_PATH_SEPARATOR);
		}
		brandSeries.setGrade(brandSeries.getTreePaths().size());
	}


}