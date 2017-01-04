/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.wit.dao.CameraDao;
import net.wit.entity.Camera;
import net.wit.service.CameraService;

/**
 * Service - 设备管理
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("cameraServiceImpl")
public class CameraServiceImpl extends BaseServiceImpl<Camera, Long> implements CameraService {

	@Resource(name = "cameraDaoImpl")
	private CameraDao cameraDao;

	@Resource(name = "cameraDaoImpl")
	public void setBaseDao(CameraDao cameraDao) {
		super.setBaseDao(cameraDao);
	}

}

