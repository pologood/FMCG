/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import net.wit.dao.AreaDao;
import net.wit.entity.Area;
import net.wit.entity.Area.Status;
import net.wit.entity.Location;
import net.wit.util.MapUtils;

/**
 * Dao - 地区
 * @author rsico Team
 * @version 3.0
 */
@Repository("areaDaoImpl")
public class AreaDaoImpl extends BaseDaoImpl<Area, Long> implements AreaDao {

	public List<Area> findRoots(Integer count) {
		String jpql = "select area from Area area where area.parent is null order by area.order asc";
		TypedQuery<Area> query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public Area findByCode(String code) {
		String jpql = "select area from Area area where area.code=:code";
		try {
			return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}

	public Area findByZipCode(String zipCode) {
		String jpql = "select area from Area area where area.zipCode=:zipCode";
		try {
			return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("zipCode", zipCode).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
	}

	public Area findByTelCode(String code) {
		String jpql = "select area from Area area where area.telCode=:code";
		try {
			return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getResultList().get(0);
		} catch (Exception e) {
			return null;
		}
	}

	public Area findByAreaName(String areaName) {
		if (StringUtils.isEmpty(areaName)) {
			return null;
		}
		String jpql = "select area from Area area where area.fullName like:areaName";
		try {
			return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("areaName", areaName).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Area> findOpenList() {
		String jpql = "select area from Area area where area.status =:status";
		try {
			return entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("status", Status.enabled).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public Area findByLbs(double lat,double lng) {
		try {
			double distance = 1000000d;  
			MapUtils mapUtils = new MapUtils(distance);
			mapUtils.rectangle4Point(lat,lng);

			String jpql = "select area from Area area where area.location.lat > :lat0 AND area.location.lat < :lat1 AND area.location.lng>:lng0 AND area.location.lng<:lng1 ";  
			List<Area> list = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("lat0",mapUtils.getLeft_bottom().getLat() ).setParameter("lat1",mapUtils.getLeft_top().getLat()).setParameter("lng0",mapUtils.getLeft_top().getLng() ).setParameter("lng1",mapUtils.getRight_bottom().getLng()).getResultList();
			Area area = null;
			for (Area cmy:list) {
			  Location clt = cmy.getLocation();
			  if (clt!=null) {
			    double dis = MapUtils.getDistatce(lat,clt.getLat().doubleValue(),lng,clt.getLng().doubleValue());
			    if (dis<distance) {
				    area = cmy;
			        distance = dis;
			    }
			  }
			}
		    return area;
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
	public List<Area> findChildren(Area area, Integer count) {
		TypedQuery<Area> query;
		if (area != null) {
				String jpql = "select area from Area area where area.treePath like :treePath order by area.order asc";
				query = entityManager
						.createQuery(jpql, Area.class)
						.setFlushMode(FlushModeType.COMMIT)
						.setParameter(
								"treePath",
								"%" + Area.TREE_PATH_SEPARATOR
										+ area.getId()
										+ Area.TREE_PATH_SEPARATOR
										+ "%");
		} else {
			String jpql = "select area from Area area order by area.order asc";
			query = entityManager
					.createQuery(jpql, Area.class)
					.setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), area);
	}

	/**
	 * 排序企业分类
	 * 
	 * @param areas
	 *            地区列表
	 * @param parent
	 *            上级地区
	 * @return 地区列表
	 */
	private List<Area> sort(
			List<Area> areas,
			Area parent) {
		List<Area> result = new ArrayList<Area>();
		if (areas != null) {
			for (Area area: areas) {
				if ((area.getParent() != null && area
						.getParent().equals(parent))
						|| (area.getParent() == null && parent == null)) {
					result.add(area);
					result.addAll(sort(areas,
							area));
				}
			}
		}
		return result;
	}
}