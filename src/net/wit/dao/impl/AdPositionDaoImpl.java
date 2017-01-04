/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import net.wit.dao.AdPositionDao;
import net.wit.entity.Ad;
import net.wit.entity.AdPosition;
import net.wit.entity.Area;
import net.wit.entity.Tenant;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Dao - 广告位
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("adPositionDaoImpl")
public class AdPositionDaoImpl extends BaseDaoImpl<AdPosition, Long> implements AdPositionDao {

	public AdPosition find(Long id, Tenant tenant, Area area, Integer count) {
		Session session = (Session) entityManager.getDelegate();
		if (tenant!=null) {
		   session.enableFilter("tenantFilter").setParameter("tenantId", tenant.getId());
		}
		Query query = session.createQuery("from AdPosition where id="+id);
		query.setFirstResult(0).setFetchSize(10);
		@SuppressWarnings("unchecked")
		List<AdPosition> result = query.list();
		session.disableFilter("tenantFilter");
		if(result.isEmpty()){
			return null;
		}
		AdPosition adPosition = result.get(0);
		Set<Ad> ads = new HashSet<Ad>();
		if(count==null){
			if(!adPosition.getAds().isEmpty()){
				for(Ad ad :adPosition.getAds()){
					if (ad.getArea()==null || ad.getArea().equals(area)) {
					   ads.add(ad);
					}
				}
			}
		}else{
			if(!adPosition.getAds().isEmpty()){
				int tmpCount=0;
				Iterator<Ad> iterator = adPosition.getAds().iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getArea()==null || ad.getArea().equals(area)) {
						ads.add(ad);
						tmpCount++;
						if(tmpCount==count){
							break;
						};
					}
				}
			}
		}
		adPosition.setAds(ads);
		return adPosition;
	}

	public Map<String ,Object> find(Long id, Long tenantId) {
		StringBuffer hsql = new StringBuffer("select a.path from xx_ad a where a.ad_position=:id and a.tenant=:tenant order by orders");
		javax.persistence.Query query = entityManager.createNativeQuery(hsql.toString());
		query.setFlushMode(FlushModeType.COMMIT);
		query.setParameter("id", id);
		query.setParameter("tenant", tenantId);

		try {
			List<?> data = query.getResultList();

			Map<String, Object> map = new HashMap<String, Object>();

			if(data.size()>0){
				map.put("path",data.get(0));
			}else {
				map.put("path","");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}