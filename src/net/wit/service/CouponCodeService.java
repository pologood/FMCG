/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.CouponNumber;
import net.wit.entity.Member;

/**
 * Service - 优惠码
 * @author rsico Team
 * @version 3.0
 */
public interface CouponCodeService extends BaseService<CouponCode, Long> {

	/**
	 * 判断优惠码是否存在
	 * @param code 号码(忽略大小写)
	 * @return 优惠码是否存在
	 */
	boolean codeExists(String code);

	/**
	 * 根据号码查找优惠码
	 * @param code 号码(忽略大小写)
	 * @return 优惠码，若不存在则返回null
	 */
	CouponCode findByCode(String code);

	/**
	 * 生成优惠码
	 * @param coupon 优惠券
	 * @param member 会员
	 * @return 优惠码
	 */
	CouponCode build(Coupon coupon, Member member);

	/**
	 * 生成优惠码
	 * @param coupon 优惠券
	 * @param member 会员
	 * @param count 数量
	 * @return 优惠码
	 */
	List<CouponCode> build(Coupon coupon, Member member, Integer count);

	CouponCode build(Coupon coupon, Member member, Integer count,Long code, CouponNumber.Status status);

	List<CouponCode> buildRed(Coupon coupon, Member member, Integer count,Member guide);

	List<CouponCode> build(Coupon coupon, Integer count, Date date);
	/**
	 * 兑换优惠码
	 * @param coupon 优惠券
	 * @param member 会员
	 * @return 优惠码
	 */
	CouponCode exchange(Coupon coupon, Member member);

	/**
	 * 查找优惠码分页
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return 优惠码分页
	 */
	Page<CouponCode> findPage(Member member, Pageable pageable);
	Page<CouponCode> findPage(Member member,Boolean isUsed,Boolean isExpired, Pageable pageable);
	Page<CouponCode> findPage(Member member,Boolean isUsed,Boolean isExpired, Coupon.Type type, Pageable pageable);
	/**
	 * 查找优惠码数量
	 * @param coupon 优惠券
	 * @param member 会员
	 * @param hasBegun 是否已开始
	 * @param hasExpired 是否已过期
	 * @param isUsed 是否已使用
	 * @return 优惠码数量
	 */
	Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);

	/**
	 * 根据会员查询可用红包code列表
	 * @param member
	 * @return
	 */
	List<CouponCode> findEnabledList(Member member);

	CouponCode findMemberCouponCode(Member member);

	/**
	 * 根据红包种类ID、请求数量和会员，发送红包
	 * @param ids 会员ID
	 * @param idsCoupon 红包种类ID
	 * @param reqCount 请求数量
	 */
	Map<String, String> send(String mids, String cids, String rCounts, String aCount);
	
	/**
	 * @param findPage
	 */
	void filterCondition(Page<CouponCode> findPage);

	/**
	 * 会员可以用的红包
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<CouponCode> findCanUseCouponCode(Member member, Pageable pageable);

	/**
	 * 会员已使用的红包
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<CouponCode> findUsedCouponCode(Member member, Pageable pageable);

	/**
	 * 会员已过期的红包
	 * @param member
	 * @param pageable
	 * @return
	 */
	Page<CouponCode> findOverdueCouponCode(Member member, Pageable pageable);

	/**
	 * 会员可用红包数量
	 * @param member
	 * @return
	 */
	Long findCanUseCount(Member member);

	/**
	 * 会员已用红包数量
	 * @param member
	 * @return
	 */
	Long findUsedCount(Member member);

	/**
	 * 会员过期红包数量
	 * @param member
	 * @return
	 */
	Long findOverdueCount(Member member);

	/**
	 * 会员红包总数量
	 * @param member
	 * @return
	 */
	Long findAllCount(Member member);

	Long findAllCount(Coupon coupon);

	/**
	 * 根据号码查找优惠码
	 * @param code 号码(忽略大小写)
	 * @return 优惠码，若不存在则返回null
	 */
	CouponCode findCouponCodeByCouponAndMember(Coupon coupon, Member member);
	
	/**
	 * 根据红包类型查找已经使用的code
	 * @param coupon
	 * @param pageable
	 * @return
	 */
	Page<CouponCode> findUsedCouponCodeByCoupon(Coupon coupon, Pageable pageable);
	
	/**
	 * 根据红包类型查找已经发送的code
	 * @param coupon
	 * @param pageable
	 * @return
	 */
	Page<Tuple> findSendedCouponCodeByCoupon(Coupon coupon, Pageable pageable);
	
	List<CouponCode> findCoupon(Member member,Coupon coupon);

	List<CouponCode> findCoupon( Member member,String type);

	Page<CouponCode> findCoupon( Member member,String type,Pageable pageable);

	List<CouponCode> findRedCouponCode( Member member,Coupon coupon,Boolean isUsed,Boolean isExpired);

	/**
	 * 統計
	 * @param coupon
	 * @param isUsed
	 * @param pageable
     * @return
     */
	Page<CouponCode> sumerStatistics(Coupon coupon, Boolean isUsed, Pageable pageable);

	/**
	 * 运营山后台根据会员关键词查找
	 * @param keyword
	 * @param coupon
	 * @param pageable
	 * @return
	 */
	Page<CouponCode> findUsedCouponCodeByKeyword(String keyword,Coupon coupon, Pageable pageable);

	Page<Tuple> findDrawedCouponCodeByCoupon(Date begin_date,Date end_date,String keyword,Coupon coupon, Pageable pageable);

	Page<CouponCode> findPage(Date begin_date,Date end_date,String keyword,Coupon coupon,Boolean isUsed , Pageable pageable);
}