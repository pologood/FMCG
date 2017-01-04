/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Tenant;
import net.wit.entity.Enterprise.EnterpriseType;

/**
 * Service - 管理员
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface AdminService extends BaseService<Admin, Long> {

	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 根据用户名查找管理员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	Admin findByUsername(String username);

	/**
	 * 根据ID查找权限
	 * 
	 * @param id
	 *            ID
	 * @return 权限,若不存在则返回null
	 */
	List<String> findAuthorities(Long id);

	/**
	 * 判断管理员是否登录
	 * 
	 * @return 管理员是否登录
	 */
	boolean isAuthenticated();

	/**
	 * 获取当前登录管理员
	 * 
	 * @return 当前登录管理员,若不存在则返回null
	 */
	Admin getCurrent();

	/**
	 * 获取当前登录用户名
	 * 
	 * @return 当前登录用户名,若不存在则返回null
	 */
	String getCurrentUsername();
	
	/**
	 * 根据登陆用户查找下属商家
	 * @param admin 登陆用户
	 * @return 下属商家列表
	 */
	List<Tenant> findTenantsByAdmin(Admin admin);
	
	Page<Admin> findPage(List<EnterpriseType> enterpriseTypes,List<Area> areas,Pageable pageable);

	Map<String,BigDecimal> getAmount();

	/**
	 * 查找平台对账后的资金
	 * @return
	 */
	Map<String ,Object> findPlatformCapital(Date beginDate, Date endDate);
}