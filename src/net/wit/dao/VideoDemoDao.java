/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.entity.Video;

/**
 * Dao - 体验账号
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface VideoDemoDao extends BaseDao<Video, Long> {

	/**
	 * 随机获取视频体验账号
	 * @return
	 */
	Video getPo_VideoDemo4Random();

}