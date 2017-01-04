/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;


import java.util.List;

import javax.annotation.Resource;

import net.wit.dao.DeviceDao;
import net.wit.entity.Device;
import net.wit.service.DeviceService;

import org.springframework.stereotype.Service;

/**
 * Service - 用户体验
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("deviceServiceImpl")
public class DeviceServiceImpl extends BaseServiceImpl<Device, Long> implements DeviceService {

	@Resource(name = "deviceDaoImpl")
	private DeviceDao deviceDao;

	@Resource(name = "deviceDaoImpl")
	public void setBaseDao(DeviceDao deviceDao) {
		super.setBaseDao(deviceDao);
	}

	public List<Device> findListByStoreId(String storeId) {
		return deviceDao.findListByStoreId(storeId);
	}

	public Device find(String equipId) {
		return deviceDao.find(equipId);
	}
}

