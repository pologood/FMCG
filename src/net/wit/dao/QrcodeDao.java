/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;

import java.util.List;

/**
 * Dao - 设备管理
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface QrcodeDao extends BaseDao<Qrcode, Long> {
	
	 Qrcode findbyUrl(String url);
	
	 Qrcode findbyTenant(Tenant tenant);

	boolean tenantExists(Tenant tenant);

	Page<Qrcode> openPage(String keword, Pageable pageable);

	List<Qrcode> findUnLockList(Integer count, Member member);
}