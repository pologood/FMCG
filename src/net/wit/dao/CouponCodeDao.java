/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.entity.PaymentMethod;

/**
 * Dao - 优惠码
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface CouponCodeDao extends BaseDao<CouponCode, Long> {

	/**
	 * 判断优惠码是否存在
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码是否存在
	 */
	boolean codeExists(String code);

	/**
	 * 根据号码查找优惠码
	 * 
	 * @param code
	 *            号码(忽略大小写)
	 * @return 优惠码，若不存在则返回null
	 */
	CouponCode findByCode(String code);

	/**
	 * 生成优惠码
	 * 
	 * @param coupon
	 *            优惠券
	 * @param member
	 *            会员
	 * @return 优惠码
	 */
	CouponCode build(Coupon coupon, Member member);

	/**
	 * 生成优惠码
	 * 
	 * @param coupon
	 *            优惠券
	 * @param member
	 *            会员
	 * @param count
	 *            数量
	 * @return 优惠码
	 */
	List<CouponCode> build(Coupon coupon, Member member, Integer count);

	List<CouponCode> buildRed(Coupon coupon, Member member, Integer count,Member guide);

	List<CouponCode> build(Coupon coupon, Integer count, Date date);


	/**
	 * 查找优惠码分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 优惠码分页
	 */
	Page<CouponCode> findPage(Member member, Pageable pageable);
	
	Page<CouponCode> findPage(Member member,Boolean isUsed,Boolean isExpired, Pageable pageable);

	Page<CouponCode> findPage(Member member, Boolean isUsed, Boolean isExpired, Coupon.Type type, Pageable pageable);

	/**
	 * 查找优惠码数量
	 * 
	 * @param coupon
	 *            优惠券
	 * @param member
	 *            会员
	 * @param hasBegun
	 *            是否已开始
	 * @param hasExpired
	 *            是否已过期
	 * @param isUsed
	 *            是否已使用
	 * @return 优惠码数量
	 */
	Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);
	
	/**
	 * 
	 * @param member
	 * @return
	 */
	List<CouponCode> findEnabledList(Member member);

	CouponCode findMemberCouponCode(Member member);

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

	/**
	 * 优惠券数量
	 */
	Long findAllCount(Coupon coupon);

	/**
	 * 获取会员购物车可用的优惠券码
	 * @param member
	 * @param paymentMethod
	 * @return
	 */
	List<CouponCode> findEnabledCouponCodeList(Member member,PaymentMethod paymentMethod);

	/**
	 * 根据红包类型获取当前用户一张可用红包码
	 * @param coupon
	 * @param member
	 * @return
	 */
	CouponCode findCouponCodeByCouponAndMember(Coupon coupon, Member member);

	/**
	 * @param coupon
	 * @return
	 */
	Page<CouponCode> findUsedCouponCodeByCoupon(Coupon coupon, Pageable pageable);

	List<CouponCode> findUsedCouponCodeByCoupon(Coupon coupon,Integer count);

	/**
	 * @param coupon
	 * @param pageable
	 * @return
	 */
	Page<Tuple> findSendedCouponCodeByCoupon(Coupon coupon, Pageable pageable);
	
	List<CouponCode> findCoupon(Member member,Coupon coupon);

	List<CouponCode> findCoupon(Member member,String type);

	Page<CouponCode> findCoupon(Member member,String type,Pageable pageable);

	List<CouponCode> findRedCouponCode(Member member,Coupon coupon,Boolean isUsed,Boolean isExpired);

	/**
	 * 統計
	 * @param coupon
	 * @param isUsed
	 * @param pageable
     * @return
     */
	Page<CouponCode> sumerStatistics(Coupon coupon,Boolean isUsed, Pageable pageable);


	Page<CouponCode> findUsedCouponCodeByKeyword(String keyword,Coupon coupon, Pageable pageable);

	Page<Tuple> findDrawedCouponCodeByCoupon(Date begin_date,Date end_date,String keyword,Coupon coupon, Pageable pageable);

	Page<CouponCode> findPage(Date begin_date,Date end_date,String keyword,Coupon coupon,Boolean isUsed , Pageable pageable);

}