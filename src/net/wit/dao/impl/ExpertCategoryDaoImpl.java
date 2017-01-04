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

import net.wit.dao.ExpertCategoryDao;
import net.wit.entity.ExpertCategory;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 专家小组
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("expertCategoryDaoImpl")
public class ExpertCategoryDaoImpl extends BaseDaoImpl<ExpertCategory, Long> implements ExpertCategoryDao {

	public List<ExpertCategory> findRoots(Integer count) {
		String jpql = "select expertCategory from ExpertCategory expertCategory where expertCategory.parent is null order by expertCategory.order asc";
		TypedQuery<ExpertCategory> query = entityManager.createQuery(jpql, ExpertCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<ExpertCategory> findParents(ExpertCategory expertCategory, Integer count) {
		if (expertCategory == null || expertCategory.getParent() == null) {
			return Collections.<ExpertCategory> emptyList();
		}
		String jpql = "select expertCategory from ExpertCategory expertCategory where expertCategory.id in (:ids) order by expertCategory.grade asc";
		TypedQuery<ExpertCategory> query = entityManager.createQuery(jpql, ExpertCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", expertCategory.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<ExpertCategory> findChildren(ExpertCategory expertCategory, Integer count) {
		TypedQuery<ExpertCategory> query;
		if (expertCategory != null) {
			String jpql = "select expertCategory from ExpertCategory expertCategory where expertCategory.treePath like :treePath and grade=:grade order by expertCategory.order asc";
			query = entityManager.createQuery(jpql, ExpertCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + ExpertCategory.TREE_PATH_SEPARATOR + expertCategory.getId() + ExpertCategory.TREE_PATH_SEPARATOR + "%").setParameter("grade",expertCategory.getGrade()+1);
		} else {
			String jpql = "select expertCategory from ExpertCategory expertCategory order by expertCategory.order asc";
			query = entityManager.createQuery(jpql, ExpertCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), expertCategory);
	}

	/**
	 * 设置treePath、grade并保存
	 * 
	 * @param expertCategory
	 *            文章分类
	 */
	@Override
	public void persist(ExpertCategory expertCategory) {
		Assert.notNull(expertCategory);
		setValue(expertCategory);
		super.persist(expertCategory);
	}

	/**
	 * 设置treePath、grade并更新
	 * 
	 * @param expertCategory
	 *            文章分类
	 * @return 文章分类
	 */
	@Override
	public ExpertCategory merge(ExpertCategory expertCategory) {
		Assert.notNull(expertCategory);
		setValue(expertCategory);
		for (ExpertCategory category : findChildren(expertCategory, null)) {
			setValue(category);
		}
		return super.merge(expertCategory);
	}

	/**
	 * 排序文章分类
	 * 
	 * @param expertCategories
	 *            文章分类
	 * @param parent
	 *            上级文章分类
	 * @return 文章分类
	 */
	private List<ExpertCategory> sort(List<ExpertCategory> expertCategories, ExpertCategory parent) {
		List<ExpertCategory> result = new ArrayList<ExpertCategory>();
		if (expertCategories != null) {
			for (ExpertCategory expertCategory : expertCategories) {
				if ((expertCategory.getParent() != null && expertCategory.getParent().equals(parent)) || (expertCategory.getParent() == null && parent == null)) {
					result.add(expertCategory);
					result.addAll(sort(expertCategories, expertCategory));
				}
			}
		}
		return result;
	}

	/**
	 * 设置值
	 * 
	 * @param expertCategory
	 *            文章分类
	 */
	private void setValue(ExpertCategory expertCategory) {
		if (expertCategory == null) {
			return;
		}
		ExpertCategory parent = expertCategory.getParent();
		if (parent != null) {
			expertCategory.setTreePath(parent.getTreePath() + parent.getId() + ExpertCategory.TREE_PATH_SEPARATOR);
		} else {
			expertCategory.setTreePath(ExpertCategory.TREE_PATH_SEPARATOR);
		}
		expertCategory.setGrade(expertCategory.getTreePaths().size());
	}

}