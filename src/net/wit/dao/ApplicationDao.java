/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Application;
import net.wit.entity.Application.Type;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

/**
 * Dao - 应用
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface ApplicationDao extends BaseDao<Application, Long> {

	/**
	 * 根据编号查找应用
	 * 
	 * @param sn
	 *            应用编号(忽略大小写)
	 * @return 应用，若不存在则返回null
	 */
	Application findByCode(Member member,String code);
	
	/**
	 * 根据编号查找应用
	 * 
	 * @param sn
	 *            应用编号(忽略大小写)
	 * @return 应用，若不存在则返回null
	 */
	Application findApplication(Tenant tenant,String code,Type type);
	
	/**
	 * 根据用户查找应用
	 * @param member
	 * @return
	 */
	List<Application> findByMember(Member member);
	/**
	 * 根据用户查找应用
	 * @param member
	 * @return
	 */
	List<Application> findList(Tenant tenant,Type type);

	Page<Application> openPage(String keyword, Pageable pageable,Application.Status statu);
}