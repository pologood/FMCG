/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

/**
 * Dao - 会员
 * @author rsico Team
 * @version 3.0
 */
public interface MemberDao extends BaseDao<Member, Long> {

	/**
	 * 判断用户名是否存在
	 * @param username 用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 判断E-mail是否存在
	 * @param email E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExists(String email);

	/**
	 * 判断mobile是否存在
	 * @param mobile mobile(忽略大小写)
	 * @return mobile是否存在
	 */
	boolean mobileExists(String mobile);

	/**
	 * 根据用户名查找会员
	 * @param username 用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByUsername(String username);

	/**
	 * 根据E-mail查找会员
	 * @param email E-mail(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	List<Member> findListByEmail(String email);

	/**
	 * 查找会员消费信息
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param count 数量
	 * @return 会员消费信息
	 */
	List<Object[]> findPurchaseList(Date beginDate, Date endDate, Integer count);

	/**
	 * 根据代理商查找会员
	 * @return 会员，若不存在则返回null
	 */
	Page<Member> findPage(Member member, Pageable pageable);
	/**
	 * 根据我的会员列表
	 * @return 会员，若不存在则返回null
	 */
	public Page<Member> findPageMyMember(Member member, Pageable pageable);
	/**
	 * 根据店铺查找员工
	 * @param tenant
	 * @return 会员，若不存在则返回null
	 */
	Page<Member> findPage(Tenant tenant,String keyword, Pageable pageable);

	/**
	 * 根据实名认证查找会员
	 * @return 会员，若不存在则返回null
	 */
	Page<Member> findRealnameMemberPage(Pageable pageable);

	Member findByTel(String mobile);

	Member findByEmail(String email);

	List<Member> findList(Member member);

	/**
	 * 判断E-mail是否存在
	 * @param email E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExistsWithoutUser(String email, Member member);

	/**
	 * 判断mobile是否存在
	 * @param mobile mobile(忽略大小写)
	 * @return mobile是否存在
	 */
	boolean mobileExistsWithoutUser(String mobile, Member member);

	/**
	 * 根据手机号查找会员
	 * @param mobile 手机号
	 * @return 会员，若不存在则返回null
	 */
	Member findByBindTel(String mobile);

	/**
	 * 根据邮箱查找会员
	 * @param email 邮箱
	 * @return 会员，若不存在则返回null
	 */
	Member findByBindEmail(String email);

	/**
	 * 查找收藏商家分页
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return 收藏商家分页
	 */
	Page<Member> findFavoritePage(Member member, Pageable pageable);
	List<Member> findFavoriteList(Member member);
	
	/**
	 * 查找附近的人
	 * @param location 位置
	 * @param pageable 分页信息
	 * @return 收藏商家分页
	 */
	Page<Member> findNearBy(Location location, Pageable pageable);

	/**
	 * 查找员工列表
	 * @param member
	 * @return
	 */
	List<Member> findListEmployee(Member member);

	/**
	 * @Title：findByWechatId
	 * @Description：
	 * @param wechatId
	 * @return  Member
	 */
	Member findByWechatId(String wechatId);

	/**
	 * @Title：findRealnameMemberPage
	 * @Description：
	 * @param authStatus
	 * @param pageable
	 * @return  Page<Member>
	 */
	Page<Member> findRealnameMemberPage(AuthStatus authStatus, Pageable pageable);
	
	/**  我发展会员的累计销售
	 */
	BigDecimal sumExtAmount(Member member);
	
	/**  我发展会员的累计返利
	 */
	BigDecimal sumExtProfit(Member member);
	
	Page<Member> findPage(AuthStatus authStatus,Date beginDate, Date endDate, Pageable pageable);
	Page<Member> findPage(Date beginDate, Date endDate, Pageable pageable);

	
	/**
	 * 根据城市查询会员
	 * @param area 城市集合
	 * @return 会员集合
	 */
	List<Member> findByArea(List<Area> area);
	
	/**
	 * 根据会员userName模糊查询会员
	 * @param userName 会员userName
	 * @return 会员集合
	 */
	List<Member> findByLikeUserName(String userName);

	/**
	 * 根据条件查询会员
	 * 
	 * @param area 城市集合
	 * @param userName 会员userName
	 * @return 会员集合
	 */
	List<Member> findByCondition(List<Area> area,
			String userName);

	List<Member> findFans(Member member);
	Page<Member> findFanPage(Member member,Pageable pageable);

	Long findGuiderStar(Tenant tenant);

	/**
	 * 根据时间查询新增会员
	 */
	public Page<Member> findByAddPage(Member member,Date beginDate, Date endDate,Pageable pageable);
	public List<Member> findByAddList(Member member,Date beginDate, Date endDate);

	/**
	 * 导出所有会员信息  2016-12-20 add by yw
	 * @param beginDate
	 * @param endDate
	 * @param keywords
	 * @return
	 */
	public List<Member> memberListExport(Date beginDate, Date endDate,String keywords);
}