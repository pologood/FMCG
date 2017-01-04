/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import net.wit.dao.VideoDemoDao;
import net.wit.entity.Video;

import org.springframework.stereotype.Repository;

/**
 * Dao - 体验账号
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("videoDemoDaoImpl")
public class VideoDemoDaoImpl extends BaseDaoImpl<Video, Long> implements VideoDemoDao {

	@SuppressWarnings("unchecked")
	public Video getPo_VideoDemo4Random() {
		final String sql = "select * from xx_video_demo order by RAND() LIMIT 1";
		
		try {
			Query query = entityManager.createNativeQuery(sql, Video.class);
			List<Video> list = query.getResultList();
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (NoResultException e) {
			return null;
		}
		return null;
	}

}