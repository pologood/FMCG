package net.wit.service;


import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Authen;
import net.wit.entity.Authen.AuthenStatus;
import net.wit.entity.Authen.AuthenType;
import net.wit.entity.Tenant;

import org.springframework.transaction.annotation.Transactional;

public interface AuthenService  extends BaseService<Authen, Long>{

	@Transactional(readOnly = true)
	public abstract Authen findByType(Tenant tenant, AuthenType enterprise);
	
	@Transactional
	public abstract void save(Authen authen);

	@Transactional
	public abstract Authen update(Authen authen);
	
	/**
	 * 根据登陆用户查询认证列表
	 * @param admin 登陆用户
	 * @param authenStatus 认证状态
	 * @param pageable 分页信息
	 * @return 认证分页
	 */
	@Transactional(readOnly = true)
	public abstract Page<Authen> findPage(Area area,AuthenStatus authenStatus, Date beginDate, Date endDate,Pageable pageable);

}