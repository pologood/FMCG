/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Deposit;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

/**
 * Service - 会员
 * @author rsico Team
 * @version 3.0
 */
public interface MemberService extends BaseService<Member, Long> {

	/**
	 * 判断用户名是否存在
	 * @param username 用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 判断用户名是否禁用
	 * @param username 用户名(忽略大小写)
	 * @return 用户名是否禁用
	 */
	boolean usernameDisabled(String username);

	/**
	 * 判断E-mail是否存在
	 * @param email E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExists(String email);

	/**
	 * 判断mobile是否存在
	 * @param mobile mobile
	 * @return mobile是否存在
	 */
	boolean mobileExists(String mobile);

	/**
	 * 判断E-mail是否唯一
	 * @param previousEmail 修改前E-mail(忽略大小写)
	 * @param currentEmail 当前E-mail(忽略大小写)
	 * @return E-mail是否唯一
	 */
	boolean emailUnique(String previousEmail, String currentEmail);

	/**
	 * 保存会员
	 * @param member 会员
	 * @param operator 操作员
	 */
	void save(Member member, Admin operator);
	
	/**
	 * 创建会员
	 * @param member 会员
	 * @param operator 操作员
	 */
	Member createAndBind(String openId,Area area,Member ext, String ipAddr);

	/**
	 * 更新会员余额
	 * @param member 会员
	 * @param modifyBalance 修改结算余额
	 * @param modifyBalance 修改余额
	 * @param depositMemo 修改余额备注
	 * @param operator 操作员
	 */
	public Deposit payment(Member member, Integer modifyPoint,BigDecimal modifyClearBalance, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception;

	
	/**
	 * 充值
	 * @param member 会员
	 * @param modifyPoint 修改积分
	 * @param modifyBalance 修改余额
	 * @param depositMemo 修改余额备注
	 * @param operator 操作员
	 */
	void Recharge(Member member, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception;

	/**
	 * 代收
	 * @param member 会员
	 * @param modifyPoint 修改积分
	 * @param modifyBalance 修改余额
	 * @param depositMemo 修改余额备注
	 * @param operator 操作员
	 */
	void Cashier(Member member, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception;
	/**
	 * 退款
	 * @param member 会员
	 * @param modifyPoint 修改积分
	 * @param modifyBalance 修改余额
	 * @param depositMemo 修改余额备注
	 * @param operator 操作员
	 */
	void withdraw(Member member, Integer modifyPoint, BigDecimal modifyClearBalance, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception;

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
	 * 判断会员是否登录
	 * @return 会员是否登录
	 */
	boolean isAuthenticated();

	/**
	 * 获取当前登录会员
	 * @return 当前登录会员，若不存在则返回null
	 */
	Member getCurrent();

	/**
	 * 获取当前登录区域
	 * @return 当前登录区域，若不存在则返回null
	 */

	Area getCurrentArea();

	/**
	 * 获取当前登录用户名
	 * @return 当前登录用户名，若不存在则返回null
	 */
	String getCurrentUsername();

	String getToken(Member member);

	/**
	 * 当前会员的发展会员-分页
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Member> findPage(Member member, Pageable pageable);

	/**
	 * 查找附近的人
	 * @param location 位置
	 * @param pageable 分页信息
	 * @return 收藏商家分页
	 */
	Page<Member> findNearBy(Location location, Pageable pageable);
	
	/**
	 * 当前会员的发展会员-列表
	 * @param member
	 * @return
	 */
	List<Member> findList(Member member);

	/**
	 * 查找员工
	 * @param member
	 * @return
	 */
	List<Member> findListEmployee(Member member);

	List<Member> findList(Tenant tenant);

	Page<Member> findPage(Tenant tenant,String keyword, Pageable pageable);

	/**
	 * 查找我的会员
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<Member> findPageMyMember(Member member, Pageable pageable);

	/**
	 * 根据手机号查找会员
	 * @param mobile 手机号
	 * @return 会员，若不存在则返回null
	 */
	Member findByTel(String mobile);

	Member findByEmail(String email);

	void upgrade(Member member);

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
	 * @Title：findByWechatId
	 * @Description：
	 * @param wechatId
	 * @return  Member
	 */
	Member findByWechatId(String wechatId);

	
	/**  我发展会员的累计销售
	 */
	BigDecimal sumExtAmount(Member member);
	
	/**  我发展会员的累计返利
	 */
	BigDecimal sumExtProfit(Member member);

	public Page<Member> findPage(Admin admin,Pageable pageable);
	
	public Page<Member> findPage(Date beginDate, Date endDate,Pageable pageable);
	
	/**
	 * 根据城市id和模糊的会员username查询
	 * @param areaId 城市id
	 * @param userName 会员username
	 * @return
	 */
	public List<Member> findByCondition(Long areaId,String userName);

	List<Member> findFans(Member member);
	Page<Member> findFanPage(Member member,Pageable pageable);

	/**
	 *根据店铺查询明星导购ID
	 */
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