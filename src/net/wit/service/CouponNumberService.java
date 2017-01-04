package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.CouponNumber;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Trade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16/11/12.
 */
public interface CouponNumberService extends BaseService<CouponNumber,Long> {

    Long getLastCode(Coupon coupon);

    List<CouponNumber> findList(Coupon coupon, Member member,Member guideMember, Long code);

    Page<CouponNumber> openPage(Coupon coupon, Member member, Member guideMember, Long code, Pageable pageable);
    BigDecimal resolveBrokerage(BigDecimal amount,BigDecimal brokerage,BigDecimal ownerBrokerage,Member member,CouponCode couponCode,PayBill payBill);
    BigDecimal resolveBrokerage(BigDecimal amount,BigDecimal brokerage,BigDecimal ownerBrokerage,Member member,CouponCode couponCode,Trade trade);

    Page<Map<String, Object>> findCouponNumberPage(Coupon coupon, String keyword,Pageable pageable);
    List<Map<String, Object>> findCouponNumberList(Coupon coupon, String keyword);
}
