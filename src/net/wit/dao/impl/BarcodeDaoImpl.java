/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BarcodeDao;
import net.wit.entity.Barcode;

import net.wit.entity.Qrcode;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 条码库
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("barcodeDaoImpl")
public class BarcodeDaoImpl extends BaseDaoImpl<Barcode, Long> implements BarcodeDao {

	@Resource(name = "barcodeDaoImpl")
	private BarcodeDao barcodeDao;

	/**
	 * 设置值并保存
	 * 
	 * @param barcode
	 *            条码库
	 */
	@Override
	public void persist(Barcode barcode) {
		Assert.notNull(barcode);

		super.persist(barcode);
	}

	/**
	 * 设置值并更新
	 * 
	 * @param barcode
	 *            条码库
	 * @return 条码库
	 */
	@Override
	public Barcode merge(Barcode barcode) {
		Assert.notNull(barcode);

		return super.merge(barcode);
	}
	
	public Barcode findByBarcode(String barcode) {
		if (barcode == null) {
			return null;
		}
		String jpql = "select barcodes from Barcode barcodes where barcodes.barcode = :barcode";
		try {
			return entityManager.createQuery(jpql, Barcode.class).setFlushMode(FlushModeType.COMMIT).setParameter("barcode", barcode).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public Page<Barcode> openPage(String keyword,Pageable pageable){

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Barcode> criteriaQuery = criteriaBuilder.createQuery(Barcode.class);
		Root<Barcode> root = criteriaQuery.from(Barcode.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		try{

			if (keyword != null && keyword != "") {
					restrictions = criteriaBuilder.and(
							restrictions,criteriaBuilder.or(
									criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"),
									criteriaBuilder.like(root.<String>get("barcode"), "%" + keyword + "%")
//									,
//									criteriaBuilder.like(root.get("brand").<String>get("name"), "%" + keyword + "%")
							)
					);

			}
	} catch (Exception e) {
		e.printStackTrace();
		int i=1;
		}
		//restrictions =criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), ActivityRules.Status.enabled));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}