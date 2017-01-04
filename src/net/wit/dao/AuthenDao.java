package net.wit.dao;


import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Authen;
import net.wit.entity.Authen.AuthenStatus;
import net.wit.entity.Tenant;
import net.wit.entity.Authen.AuthenType;

public interface AuthenDao  extends BaseDao<Authen, Long> {

	public abstract Authen findByType(Tenant tenant, AuthenType enterprise);

	/**
	 * 保存
	 * @param Authen 店铺认证
	 */
	public abstract void persist(Authen authen);

	/**
	 * 更新
	 * @param Authen 店铺认证
	 * @return 店铺认证
	 */
	public abstract Authen merge(Authen authen);
	/**
	 * 根据认证状态查询认证列表
	 * @param authenStatus 认证状态
	 * @param pageable 分页信息
	 * @return 认证分页
	 */
	public abstract Page<Authen> findPage(Area area,AuthenStatus authenStatus,Date beginDate, Date endDate, Pageable pageable);
	
}