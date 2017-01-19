/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.persistence.Tuple;

import net.wit.dao.CouponNumberDao;
import net.wit.entity.CouponNumber;
import net.wit.service.ConsumerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CouponCodeDao;
import net.wit.dao.CouponDao;
import net.wit.dao.MemberDao;
import net.wit.entity.Coupon;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.service.CouponCodeService;

/**
 * Service - 优惠码
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("couponCodeServiceImpl")
public class CouponCodeServiceImpl extends BaseServiceImpl<CouponCode, Long> implements CouponCodeService {

    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;

    @Resource(name = "couponDaoImpl")
    private CouponDao couponDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "couponNumberDaoImpl")
    private CouponNumberDao couponNumberDao;

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    @Resource(name = "couponCodeDaoImpl")
    public void setBaseDao(CouponCodeDao couponCodeDao) {
        super.setBaseDao(couponCodeDao);
    }

    @Transactional(readOnly = true)
    public boolean codeExists(String code) {
        return couponCodeDao.codeExists(code);
    }

    @Transactional(readOnly = true)
    public CouponCode findByCode(String code) {
        return couponCodeDao.findByCode(code);
    }

    public CouponCode build(Coupon coupon, Member member) {
        return couponCodeDao.build(coupon, member);
    }

    @Override
    @Transactional
    public List<CouponCode> build(Coupon coupon, Member member, Integer count) {
        coupon.setSendCount(coupon.getSendCount() + 1);
        couponDao.merge(coupon);
        if(coupon.getTenant()!=null){
            consumerService.becomvip(coupon.getTenant(), member);
        }
        return couponCodeDao.build(coupon, member, count);
    }

    public CouponCode build(Coupon coupon, Member member, Integer count, Long code, CouponNumber.Status status) {
        CouponCode couponCode = new CouponCode();
        if (status.equals(CouponNumber.Status.receive)) {
            List<CouponCode> couponCodes = couponCodeDao.findCoupon(member, coupon);
            if (couponCodes != null && couponCodes.size() > 0) {
                couponCode = couponCodes.get(0);
                couponCode.setBalance(couponCode.getBalance().add(coupon.getAmount()));
                if (couponCode.getIsUsed()) {
                    couponCode.setIsUsed(false);
                }
            } else {
                couponCode = new CouponCode();
                String uuid = UUID.randomUUID().toString().toUpperCase();
                couponCode.setCode(coupon.getPrefix() + uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24));
                couponCode.setIsUsed(false);
                couponCode.setExpire(coupon.getEndDate());
                couponCode.setCoupon(coupon);
                couponCode.setMember(member);
                couponCode.setBalance(coupon.getAmount());
                couponCode.setAmount(BigDecimal.ZERO);
            }
            coupon.setSendCount(coupon.getSendCount() + 1);
            couponDao.merge(coupon);
            couponCodeDao.persist(couponCode);
        }

        CouponNumber couponNumber = couponNumberDao.findByCode(code);
        if (coupon.getType().equals(Coupon.Type.multipleCoupon)) {
            if (couponNumber == null) {
                couponNumber = new CouponNumber();
            }
            couponNumber.setCode(code);
            if (status.equals(CouponNumber.Status.receive)) {
                couponNumber.setMember(member);
                couponNumber.setCouponCode(couponCode);
                couponNumber.setReceiveDate(new Date());
            } else if (status.equals(CouponNumber.Status.bound)) {
                couponNumber.setGuideMember(member);
                couponNumber.setMarkedDate(new Date());
            }

            couponNumber.setBalance(coupon.getAmount());
            couponNumber.setStatus(status);
            couponNumber.setUseTimes(0L);
            couponNumber.setBrokerage(BigDecimal.ZERO);
            couponNumber.setCoupon(coupon);
            couponNumberDao.persist(couponNumber);

            if(status.equals(CouponNumber.Status.receive)){
                Member guide=couponNumber.getGuideMember();
                if(guide!=null){
                    consumerService.becomvip(guide.getTenant(),member);
                }
            }

        }
        return couponCode;
    }

    public List<CouponCode> buildRed(Coupon coupon, Member member, Integer count, Member guide) {
        coupon.setSendCount(coupon.getSendCount() + 1);
        couponDao.merge(coupon);
        if(coupon.getTenant()!=null){
            consumerService.becomvip(coupon.getTenant(), member);
        }
        return couponCodeDao.buildRed(coupon, member, count, guide);
    }

    public List<CouponCode> build(Coupon coupon, Integer count, Date date) {
        return couponCodeDao.build(coupon, count, date);
    }

    public CouponCode exchange(Coupon coupon, Member member) {
        Assert.notNull(coupon);
        Assert.notNull(member);

        memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
        member.setPoint(member.getPoint() - coupon.getPoint());
        memberDao.merge(member);

        return couponCodeDao.build(coupon, member);
    }

    @Transactional(readOnly = true)
    public Page<CouponCode> findPage(Member member, Pageable pageable) {
        return couponCodeDao.findPage(member, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CouponCode> findPage(Member member, Boolean isUsed, Boolean isExpired, Pageable pageable) {
        return couponCodeDao.findPage(member, isUsed, isExpired, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CouponCode> findPage(Member member, Boolean isUsed, Boolean isExpired, Coupon.Type type, Pageable pageable) {
        return couponCodeDao.findPage(member, isUsed, isExpired, type, pageable);
    }

    @Transactional(readOnly = true)
    public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
        return couponCodeDao.count(coupon, member, hasBegun, hasExpired, isUsed);
    }

    @Transactional(readOnly = true)
    public List<CouponCode> findEnabledList(Member member) {
        return couponCodeDao.findEnabledList(member);
    }

    @Transactional(readOnly = true)
    public CouponCode findMemberCouponCode(Member member) {
        return couponCodeDao.findMemberCouponCode(member);
    }

    @Override
    @Transactional
    public Map<String, String> send(String mids, String cids, String rCounts, String aCount) {
        Map<String, String> returnMap = new HashMap<String, String>();
        if (mids == null || cids == null || rCounts == null || aCount == null) {
            returnMap.put("error", "发送失败！");
            return returnMap;
        }
        String[] memberIds = mids.split(",");
        String[] couponIds = cids.split(",");
        String[] reqCounts = rCounts.split(",");
        String[] availableCount = aCount.split(",");
        if (!checkCouponCodeCount(memberIds, reqCounts,
                availableCount, returnMap)) {
            return returnMap;
        }
        int size = couponIds.length;
        Member member = null;
        Long couponIdLong = 0l;
        Integer reqCount = 0;
        Coupon coupon;
        for (String id : memberIds) {
            member = memberDao.find(Long.valueOf(id));
            for (int i = 0; i < size; i++) {
                couponIdLong = Long.valueOf(couponIds[i]);
                reqCount = Integer.valueOf(reqCounts[i]);
                coupon = couponDao.find(couponIdLong);
                couponCodeDao.build(coupon, member, reqCount);
                coupon.setSendCount(coupon.getSendCount() + reqCount);
                couponDao.merge(coupon);
            }
        }
        returnMap.put("success", "发送成功！");
        return returnMap;
    }

    public boolean checkCouponCodeCount(String[] memberIds, String[] reqCounts,
                                        String[] availableCount, Map<String, String> returnMap) {
        int memberSize = memberIds.length;
        for (int i = 0; i < reqCounts.length; i++) {
            try {
                if (Integer.valueOf(availableCount[i]) < memberSize * Integer.valueOf(reqCounts[i])) {
                    returnMap.put("error", "红包数量不足，请联系 管理员！");
                    return false;
                }
            } catch (Exception e) {
                returnMap.put("error", "发送红包数量过大！");
                return false;
            }
        }
        return true;
    }

    @Override
    public void filterCondition(Page<CouponCode> findPage) {
        List<CouponCode> couponCodes = findPage.getContent();
        List<CouponCode> rtcouponCodes = new ArrayList<CouponCode>();
        for (CouponCode couponCode : couponCodes) {
            if (!couponCode.hasExpired()) {
                rtcouponCodes.add(couponCode);
            }
        }
        couponCodes.clear();
        couponCodes.addAll(rtcouponCodes);
    }

    @Override
    public Page<CouponCode> findCanUseCouponCode(Member member,
                                                 Pageable pageable) {
        return couponCodeDao.findCanUseCouponCode(member, pageable);
    }

    @Override
    public Page<CouponCode> findUsedCouponCode(Member member, Pageable pageable) {
        return couponCodeDao.findUsedCouponCode(member, pageable);
    }

    @Override
    public Page<CouponCode> findOverdueCouponCode(Member member, Pageable pageable) {
        return couponCodeDao.findOverdueCouponCode(member, pageable);
    }

    @Override
    public Long findCanUseCount(Member member) {
        return couponCodeDao.findCanUseCount(member);
    }

    @Override
    public Long findUsedCount(Member member) {
        return couponCodeDao.findUsedCount(member);
    }

    @Override
    public Long findOverdueCount(Member member) {

        return couponCodeDao.findOverdueCount(member);
    }

    @Override
    public Long findAllCount(Member member) {
        return couponCodeDao.findAllCount(member);
    }

    @Override
    public Long findAllCount(Coupon coupon) {
        return couponCodeDao.findAllCount(coupon);
    }

    @Override
    public CouponCode findCouponCodeByCouponAndMember(Coupon coupon, Member member) {
        return couponCodeDao.findCouponCodeByCouponAndMember(coupon, member);
    }

    @Override
    public Page<CouponCode> findUsedCouponCodeByCoupon(Coupon coupon, Pageable pageable) {
        return couponCodeDao.findUsedCouponCodeByCoupon(coupon, pageable);
    }

    @Override
    public Page<Tuple> findSendedCouponCodeByCoupon(Coupon coupon, Pageable pageable) {
        return couponCodeDao.findSendedCouponCodeByCoupon(coupon, pageable);
    }

    @Override
    @Transactional
    public List<CouponCode> findCoupon(Member member, Coupon coupon) {
        return couponCodeDao.findCoupon(member, coupon);
    }

    @Override
    @Transactional
    public List<CouponCode> findRedCouponCode(Member member, Coupon coupon, Boolean isUsed, Boolean isExpired) {
        return couponCodeDao.findRedCouponCode(member, coupon, isUsed, isExpired);
    }


    @Override
    @Transactional
    public List<CouponCode> findCoupon(Member member, String type) {
        return couponCodeDao.findCoupon(member, type);
    }

    @Transactional
    public Page<CouponCode> findCoupon(Member member, String type, Pageable pageable) {
        return couponCodeDao.findCoupon(member, type, pageable);
    }

    public Page<CouponCode> sumerStatistics(Coupon coupon, Boolean isUsed, Pageable pageable) {
        return couponCodeDao.sumerStatistics(coupon, isUsed, pageable);
    }

    public Page<CouponCode> findUsedCouponCodeByKeyword(String keyword,Coupon coupon, Pageable pageable) {
        return couponCodeDao.findUsedCouponCodeByKeyword(keyword,coupon,pageable);
    }

    public Page<Tuple> findDrawedCouponCodeByCoupon(Date begin_date,Date end_date,String keyword,Coupon coupon, Pageable pageable) {
        return couponCodeDao.findDrawedCouponCodeByCoupon(begin_date,end_date,keyword,coupon, pageable);
    }

    public Page<CouponCode> findPage(Date begin_date,Date end_date,String keyword,Coupon coupon,Boolean isUsed , Pageable pageable){
        return couponCodeDao.findPage(begin_date,end_date,keyword,coupon,isUsed , pageable);
    }

}