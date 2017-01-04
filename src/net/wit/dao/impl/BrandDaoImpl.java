/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BrandDao;
import net.wit.entity.Brand;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.entity.Tag;
import net.wit.entity.Product.OrderType;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Dao - 品牌
 * @author rsico Team
 * @version 3.0
 */
@Repository("brandDaoImpl")
public class BrandDaoImpl extends BaseDaoImpl<Brand, Long> implements BrandDao {
	
	private static final Pattern pattern = Pattern.compile("\\d*");

	public List<Brand> findAllByProductCategory(ProductCategory productCategory) {
		if (productCategory == null) {
			return new ArrayList<Brand>();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Brand> criteriaQuery = criteriaBuilder.createQuery(Brand.class);
		Root<Brand> root = criteriaQuery.from(Brand.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("productCategories"), productCategory));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, new ArrayList<Order>());
	}
	
	public List<Brand> findList(Tag tag) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Brand> criteriaQuery = criteriaBuilder.createQuery(Brand.class);
		Root<Brand> root = criteriaQuery.from(Brand.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("tags"), tag));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, new ArrayList<Order>());
	}
	
	//shenjc
	public List<Brand> search(String keyword,String phonetic, OrderType orderType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Brand> criteriaQuery = criteriaBuilder.createQuery(Brand.class);
		Root<Brand> root = criteriaQuery.from(Brand.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (StringUtils.isEmpty(keyword)) {
			criteriaQuery.where(restrictions);
			return super.findList(criteriaQuery, null, null, null, new ArrayList<Order>());
		}
		restrictions = criteriaBuilder.conjunction();
		if (pattern.matcher(keyword).matches()) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.like(root.<String> get("name"), "%" + keyword + "%")));
		} else {
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.like(root.<String> get("name"), "%" + keyword + "%")));
		}
		if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String> get("phonetic"), "%" + phonetic + "%"));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, new ArrayList<Order>());
		}
}