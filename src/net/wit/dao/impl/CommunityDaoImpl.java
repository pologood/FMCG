/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.wit.dao.CommunityDao;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.Location;
import net.wit.entity.Product;
import net.wit.entity.Tag;
import net.wit.util.MapUtils;

import org.springframework.stereotype.Repository;

/**
 * Dao - 社区
 * @author rsico Team
 * @version 3.0
 */
@Repository("communityDaoImpl")
public class CommunityDaoImpl extends BaseDaoImpl<Community, Long> implements CommunityDao {

	public List<Community> findList(Area area) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Community> criteriaQuery = criteriaBuilder.createQuery(Community.class);
		Root<Community> root = criteriaQuery.from(Community.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	public Community findbyCode(String code) {
		if (code == null) {
			return null;
		}
		try {
			String jpql = "select community from Community community where community.code = :code";
			return entityManager.createQuery(jpql, Community.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Community> findHot(Area area, List<Tag> tags) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Community> criteriaQuery = criteriaBuilder.createQuery(Community.class);
		Root<Community> root = criteriaQuery.from(Community.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Community> tagsSubquery = criteriaQuery.subquery(Community.class);
            Root<Community> tagsSubqueryRoot = tagsSubquery.from(Community.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}
	
	public Community findbyLocation(Location location) {
		if (location == null) {
			return null;
		}
		try {
			double distance = 5000d;  
			MapUtils mapUtils = new MapUtils(distance);
			mapUtils.rectangle4Point(location.getLat().doubleValue(), location.getLng().doubleValue());

			String jpql = "select community from Community community where community.location.lat > :lat0 AND community.location.lat < :lat1 AND community.location.lng>:lng0 AND community.location.lng<:lng1 ";  
			List<Community> list = entityManager.createQuery(jpql, Community.class).setFlushMode(FlushModeType.COMMIT).setParameter("lat0",mapUtils.getLeft_bottom().getLat() ).setParameter("lat1",mapUtils.getLeft_top().getLat()).setParameter("lng0",mapUtils.getLeft_top().getLng() ).setParameter("lng1",mapUtils.getRight_bottom().getLng()).getResultList();
			Community community = null;
			for (Community cmy:list) {
			  Location clt = cmy.getLocation();
			  if (clt!=null) {
			    double dis = MapUtils.getDistatce(location.getLat().doubleValue(),clt.getLat().doubleValue(),location.getLng().doubleValue(),clt.getLng().doubleValue());
			    if (dis<distance) {
				    community = cmy;
			        distance = dis;
			    }
			  }
			}
		    return community;
		} catch (NoResultException e) {
			return null;
		}
	}
	
}