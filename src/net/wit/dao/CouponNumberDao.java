package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.CouponNumber;
import net.wit.entity.Member;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16/11/12.
 */
public interface CouponNumberDao extends BaseDao<CouponNumber,Long> {

    Long getLastCode(Coupon coupon);

    CouponNumber findByCode(Long code);

    List<CouponNumber> findList(Coupon coupon, Member member,Member guideMember, Long code);

    Page<CouponNumber> openPage(Coupon coupon, Member member, Member guideMember, Long code, Pageable pageable);
	
    List<CouponNumber>  resolveOpen(BigDecimal amount,BigDecimal brokerage,Member member,CouponCode couponCode);

    Page<Map<String, Object>> findCouponNumberPage(Coupon coupon, String keyword,Pageable pageable);
    List<Map<String, Object>> findCouponNumberList(Coupon coupon, String keyword);
}
