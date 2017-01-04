/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AdminDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Enterprise.EnterpriseType;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Repository;

/**
 * Dao - 管理员
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("adminDaoImpl")
public class AdminDaoImpl extends BaseDaoImpl<Admin, Long> implements AdminDao {

	public boolean usernameExists(String username) {
		if (username == null) {
			return false;
		}
		String jpql = "select count(*) from Admin admin where lower(admin.username) = lower(:username)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
		return count > 0;
	}

	public Admin findByUsername(String username) {
		if (username == null) {
			return null;
		}
		try {
			String jpql = "select admin from Admin admin where lower(admin.username) = lower(:username)";
			return entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Page<Admin> findPage(List<EnterpriseType> enterpriseTypes,List<Area> areas,Pageable pageable) {
		try {
			List<Admin> result = new ArrayList<Admin>();
			if(areas==null){
				String jpql = "select admin from Admin admin where admin.enterprise.enterprisetype in (:enterpriseTypes)";
				result = entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT).setParameter("enterpriseTypes", enterpriseTypes).getResultList();
			}else{
				String jpql = "select admin from Admin admin where admin.enterprise.enterprisetype in (:enterpriseTypes) and admin.enterprise.area in (:areas)";
				result = entityManager.createQuery(jpql, Admin.class).setFlushMode(FlushModeType.COMMIT)
							.setParameter("enterpriseTypes", enterpriseTypes)
							.setParameter("areas", areas).getResultList();
			}
			return new Page<Admin>(result,result.size(),pageable);
		} catch (NoResultException e) {
			return null;
		}
	}



	public Map<String,BigDecimal> getAmount(){

		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		StringBuffer hsql1 = new StringBuffer("SELECT sum(product.cost*product.stock) from xx_product product");
		javax.persistence.Query query1 = entityManager.createNativeQuery(hsql1.toString());
		query1.setFlushMode(FlushModeType.COMMIT);
		List<?> data1 =  query1.getResultList();

		if(data1.size()>0){
			for(Object obj : data1){
				map.put("stockAmount",(BigDecimal)obj);
			}
		}else {
			map.put("stockAmount",BigDecimal.ZERO);
		}


		StringBuffer hsql2 = new StringBuffer("select sum(member.balance),sum(member.freeze_balance) from xx_member member");
		javax.persistence.Query query2 = entityManager.createNativeQuery(hsql2.toString());
		query2.setFlushMode(FlushModeType.COMMIT);
		List<?> data2 =  query2.getResultList();
		if(data2.size()>0){
			for(Object obj : data2){
				Object[] row = (Object[]) obj;
				map.put("balance",(BigDecimal)row[0]);
				map.put("freezeBalance",(BigDecimal)row[1]);
			}
		}else {
			map.put("balance",BigDecimal.ZERO);
			map.put("freezeBalance",BigDecimal.ZERO);
		}

		StringBuffer hsql3 = new StringBuffer("select sum(profit.amount),sum(profit.guide_amount),sum(profit.guide_owner_amount),sum(profit.share_amount),sum(profit.share_owner_amount),sum(profit.provider_amount) from xx_profit profit;");
		javax.persistence.Query query3 = entityManager.createNativeQuery(hsql3.toString());
		query3.setFlushMode(FlushModeType.COMMIT);
		List<?> data3 =  query3.getResultList();
		if(data3.size()>0){
			for(Object obj : data3){
				Object[] row = (Object[]) obj;
				map.put("amount",(BigDecimal)row[0]);
				map.put("guideAmount",(BigDecimal)row[1]);
				map.put("guideOwnerAmount",(BigDecimal)row[2]);
				map.put("shareAmount",(BigDecimal)row[3]);
				map.put("shareOwnerAmount",(BigDecimal)row[4]);
				map.put("providerAmount",(BigDecimal)row[5]);
			}
		}else {
			map.put("amount",BigDecimal.ZERO);
			map.put("guideAmount",BigDecimal.ZERO);
			map.put("guideOwnerAmount",BigDecimal.ZERO);
			map.put("shareAmount",BigDecimal.ZERO);
			map.put("shareOwnerAmount",BigDecimal.ZERO);
			map.put("providerAmount",BigDecimal.ZERO);
		}
		return map;
	}

	/**
	 * 查找平台对账资金（xx_payment,xx_credit,xx_rebate管理查询）
	 * @return
	 */
	public Map<String,Object> findPlatformCapital(Date beginDate,Date endDate){
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer hsql1 = new StringBuffer(
				"SELECT SUM(amount) a FROM `xx_payment` " +
						"where STATUS=1 and createDate>="+beginDate+" and createDate <="+endDate+" and method!=2");
		javax.persistence.Query query1 = entityManager.createNativeQuery(hsql1.toString());
		query1.setFlushMode(FlushModeType.COMMIT);
		List<?> data1 =  query1.getResultList();

		if(data1.size()>0){
			for(Object obj : data1){
				map.put("payment_amount",(BigDecimal)obj);
			}
		}else {
			map.put("payment_amount",BigDecimal.ZERO);
		}

		StringBuffer hsql2 = new StringBuffer("SELECT SUM(amount) s,fee f from xx_credit cr where `status`=2 and createDate>="+beginDate+" and createDate <="+endDate);
		javax.persistence.Query query2 = entityManager.createNativeQuery(hsql2.toString());
		query2.setFlushMode(FlushModeType.COMMIT);
		List<?> data2 =  query2.getResultList();
		if(data2.size()>0){
			for(Object obj : data2){
				Object[] row = (Object[]) obj;
				map.put("amount",(BigDecimal)row[0]);
				map.put("fee",(BigDecimal)row[1]);
			}
		}else {
			map.put("amount",BigDecimal.ZERO);
			map.put("fee",BigDecimal.ZERO);
		}

		StringBuffer hsql3 = new StringBuffer("SELECT SUM(brokerage) c from xx_rebate where type=0 and `status`=1 and createDate>="+beginDate+" and createDate <="+endDate);
		javax.persistence.Query query3 = entityManager.createNativeQuery(hsql3.toString());
		query3.setFlushMode(FlushModeType.COMMIT);
		List<?> data3 =  query3.getResultList();
		if(data3.size()>0){
			for(Object obj : data3){

				map.put("brokerage",(BigDecimal)obj);
			}
		}else {
			map.put("brokerage",BigDecimal.ZERO);
		}
		return map;
	}
}