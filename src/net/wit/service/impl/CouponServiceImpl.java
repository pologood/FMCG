/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.dao.*;
import net.wit.entity.*;
import net.wit.entity.Rebate.Status;
import net.wit.entity.Rebate.Type;
import net.wit.entity.model.CouponSumerModel;
import net.wit.service.CouponService;
import net.wit.service.MessageService;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Service - 优惠券
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("couponServiceImpl")
public class CouponServiceImpl extends BaseServiceImpl<Coupon, Long> implements CouponService {

    @Resource(name = "couponDaoImpl")
    private CouponDao couponDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;

    @Resource(name = "productCategoryDaoImpl")
    private ProductCategoryDao productCategoryDao;

    @Resource(name = "paymentDaoImpl")
    private PaymentDao paymentDao;

    @Resource(name = "paymentMethodDaoImpl")
    private PaymentMethodDao paymentMethodDao;

    @Resource(name = "rebateDaoImpl")
    private RebateDao rebateDao;

    @Resource(name = "depositDaoImpl")
    private DepositDao depositDao;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "snDaoImpl")
    private SnDao snDao;

    @Resource(name = "couponDaoImpl")
    public void setBaseDao(CouponDao couponDao) {
        super.setBaseDao(couponDao);
    }

    @Transactional(readOnly = true)
    public Page<Coupon> findPage(Area area, Community community, TenantCategory tenantCategory, Boolean isExpired, Location location, String orderType, Pageable pageable) {
        return couponDao.findPage(area, community, tenantCategory, isExpired, location, orderType, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Coupon> findPage(String status, Pageable pageable) {
        return couponDao.findPage(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
        return couponDao.findPage(isEnabled, isExchange, hasExpired, pageable);
    }

    @Override
    public Set<ProductCategory> getProductCategorySet(List<ProductCategory> productCategorysList) {
        if (productCategorysList == null || productCategorysList.size() == 0) {
            return new HashSet<ProductCategory>();
        }
        Set<ProductCategory> productCategorySet = new HashSet<ProductCategory>();
        productCategorySet.addAll(productCategorysList);
        return productCategorySet;
    }

    @Override
    public List<Coupon> findEnabledCouponList(Tenant tenant){
        return couponDao.findEnabledCouponList(tenant);
    }

    @Override
    public List<Map<String, String>> findEnabledCouponList(Cart cart,
                                                           Member member, Long paymentMethodId) {
        if (cart == null || cart.getEffectiveQuantity() == 0)
            return new ArrayList<Map<String, String>>();
        if (member == null)
            return new ArrayList<Map<String, String>>();
        if (paymentMethodId == null)
            return new ArrayList<Map<String, String>>();

        // 1、查询满足：此会员的、启用的、没有使用的、开始时间比现在早、结束时间比现在迟、支付方式是选定的的coupon集合
        List<CouponCode> enabledCouponCodeList = couponCodeDao
                .findEnabledCouponCodeList(member, paymentMethodDao.find(paymentMethodId));

        if (enabledCouponCodeList == null || enabledCouponCodeList.size() == 0)
            return new ArrayList<Map<String, String>>();

        // 2、组织会员可用的coupon集合
        Map<Long, Map<String, Object>> mapCoupons = new HashMap<Long, Map<String, Object>>();
        Map<String, Object> mapCoupon = null;
        Coupon coupon = null;
        for (CouponCode couponCode : enabledCouponCodeList) {
            coupon = couponCode.getCoupon();
            if (mapCoupons.containsKey(coupon.getId())) {
                (mapCoupons.get(coupon.getId())).put("count", ((int) (mapCoupons.get(coupon.getId())).get("count")) + 1);
                continue;
            }
            mapCoupon = new HashMap<String, Object>();
            mapCoupon.put("minimumPrice", coupon.getMinimumPrice());
            mapCoupon.put("count", 1);
            mapCoupon.put("productCategory", coupon.getProductCategory());
            mapCoupon.put("coupon", coupon);
            mapCoupons.put(coupon.getId(), mapCoupon);
        }

        // 3、按商品分类组织购物车里面商品集合
        Set<CartItem> effectiveCartItems = cart.getEffectiveCartItems();
        Map<Long, Map<String, Object>> mapProducts = new HashMap<Long, Map<String, Object>>();
        Map<String, Object> mapProduct = null;
        ProductCategory productCategory = null;
        Product product = null;
        for (CartItem cartItem : effectiveCartItems) {
            product = cartItem.getProduct();
            productCategory = product.getProductCategory();
            if (mapProducts.containsKey(productCategory.getId())) {
                (mapProducts.get(productCategory.getId()))
                        .put("price",
                                ((BigDecimal) (mapProducts.get(productCategory.getId())).get("price"))
                                        .add(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity()))));
                continue;
            }
            mapProduct = new HashMap<String, Object>();
            mapProduct.put("price", cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            mapProducts.put(productCategory.getId(), mapProduct);
        }

        Map<Long, Object> toBeCheckedMap = new HashMap<Long, Object>();
        // 4、按分类遍历购物车
        Iterator<Entry<Long, Map<String, Object>>> itCoupon = mapCoupons.entrySet().iterator();
        Entry<Long, Map<String, Object>> entryCoupon = null;
        while (itCoupon.hasNext()) { // 遍历红包种类
            entryCoupon = itCoupon.next();
            Long couponId = entryCoupon.getKey();
            Map<String, Object> couponMap = entryCoupon.getValue();
            Set<ProductCategory> productCategorySet = (Set<ProductCategory>) couponMap.get("productCategory");
            if (productCategorySet == null || productCategorySet.size() == 0) {
                toBeCheckedMap.put(couponId, cart.getEffectivePrice());
                continue;
            }
            Iterator<Entry<Long, Map<String, Object>>> itProduct = mapProducts.entrySet().iterator();
            Entry<Long, Map<String, Object>> entryProduct = null;
            while (itProduct.hasNext()) { // 遍历商品类型
                entryProduct = itProduct.next();
                Long productCategoryId = entryProduct.getKey(); // 商品类型ID
                Map<String, Object> pcMap = entryProduct.getValue();

                if (isExistToProductCategory(productCategoryId, productCategorySet)) {
                    if (toBeCheckedMap.containsKey(couponId)) {
                        toBeCheckedMap.put(couponId, ((BigDecimal) toBeCheckedMap.get(couponId)).add((BigDecimal) pcMap.get("price")));
                    } else {
                        toBeCheckedMap.put(couponId, pcMap.get("price"));
                    }
                }
            }
        }

        ArrayList<Map<String, String>> returnArrayList = new ArrayList<Map<String, String>>();
        Map<String, String> returnMap = null;
        // 5、过滤价格
        Iterator<Entry<Long, Object>> itReturnCoupon = toBeCheckedMap.entrySet().iterator();
        Entry<Long, Object> entryReturnCoupon = null;
        while (itReturnCoupon.hasNext()) {
            entryReturnCoupon = itReturnCoupon.next();
            Long couponId = entryReturnCoupon.getKey();
            BigDecimal price = (BigDecimal) entryReturnCoupon.getValue();
            Map<String, Object> map = mapCoupons.get(couponId);
            BigDecimal minimumPrice = (BigDecimal) map.get("minimumPrice");
            if (minimumPrice == null || minimumPrice.equals(BigDecimal.ZERO) || price.compareTo(minimumPrice) >= 0) {
                returnMap = new HashMap<String, String>();
                coupon = (Coupon) map.get("coupon");
                returnMap.put("id", String.valueOf(coupon.getId()));
                returnMap.put("name", coupon.getName());
                returnMap.put("amount", String.valueOf(coupon.getAmount()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                returnMap.put("startDate", sdf.format(coupon.getStartDate()));
                returnMap.put("endDate", sdf.format(coupon.getEndDate()));
                returnMap.put("count", String.valueOf(map.get("count")));
                returnMap.put("introduction", coupon.getIntroduction());
                returnArrayList.add(returnMap);
            }
        }
        return returnArrayList;
    }

    public void refreshStatus(Tenant tenant) {
        couponDao.refreshStatus(tenant);
    }

    /**
     * 判断请求id是否包含在给定id的商品类型子集内
     *
     * @param reqid 请求ID
     * @return 是否包含
     */
    private boolean isExistToProductCategory(Long reqid, Set<ProductCategory> productCategorySet) {
        Set<ProductCategory> allProductCategories = queryAllProductCategoriesById(productCategorySet);
        ProductCategory productCategory = productCategoryDao.find(reqid);
        return allProductCategories.contains(productCategory);
    }

    /**
     * 获取给定ID的商品类型的所有子集，包括子集。
     *
     * @return 所有商品集合
     */
    private Set<ProductCategory> queryAllProductCategoriesById(Set<ProductCategory> productCategorySet) {
        if (productCategorySet == null || productCategorySet.size() == 0)
            return new HashSet<ProductCategory>();

        Set<ProductCategory> allProductCategoriesSet = new HashSet<ProductCategory>();
        allProductCategoriesSet.addAll(productCategorySet);
        for (ProductCategory productCategory : productCategorySet) {
            allProductCategoriesSet.addAll(productCategoryDao.findChildren(productCategory, null, null));
        }
        return allProductCategoriesSet;
    }

    /**
     * 优惠券统计
     */
    public Page<CouponSumerModel> sumer(Coupon coupon, CouponSumerModel.Type type, Pageable pageable) {
        return couponDao.sumer(coupon, type, pageable);
    }


    /**
     * 优惠券统计
     */
    public CouponSumerModel count(Coupon coupon, CouponSumerModel.Type type) {
        return couponDao.count(coupon, type);
    }


    @Transactional
    public void upgrade(Coupon coupon) {
        Payment payment = new Payment();
        payment.setMember(coupon.getTenant().getMember());
        payment.setPayer(coupon.getTenant().getMember().getDisplayName());
        payment.setMemo("发放红包");
        payment.setSn(snDao.generate(Sn.Type.payment));
        payment.setType(Payment.Type.coupon);
        payment.setMethod(Payment.Method.online);
        payment.setStatus(coupon.getFreezeAmount().compareTo(BigDecimal.ZERO) == 0 ? Payment.Status.success : Payment.Status.wait);
        payment.setPaymentMethod("");
        payment.setFee(BigDecimal.ZERO);
        payment.setAmount(coupon.getFreezeAmount());
        payment.setPaymentPluginId("");
        payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
        payment.setTrade(null);
        payment.setOrder(null);
        payment.setCoupon(coupon);
        paymentDao.persist(payment);
        coupon.setPayment(payment);
        couponDao.persist(coupon);
    }

    //红包支付冻结资金，member指发红包的人
    public void payment(Payment payment) {
        Coupon coupon = payment.getCoupon();
        if (payment.getMethod().equals(Payment.Method.deposit)) {
            Member member = coupon.getTenant().getMember();
            memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
            BigDecimal rechargeBalance = member.getBalance().subtract(member.getClearBalance());
            BigDecimal totalBalance = new BigDecimal("0.00");
            // 余额付款
            totalBalance = member.getBalance();
            payment.setAmount(coupon.getFreezeAmount());
            if (payment.getAmount().subtract(rechargeBalance).compareTo(member.getClearBalance()) >= 0) {
                payment.setClearAmount(member.getClearBalance());
                member.setClearBalance(BigDecimal.ZERO);
            } else {
                payment.setClearAmount(payment.getAmount().subtract(rechargeBalance));
                member.setClearBalance(member.getClearBalance().subtract(payment.getAmount().subtract(rechargeBalance)));
            }
            if (member.getFreezeCashBalance().compareTo(payment.getAmount()) < 0) {
                member.setFreezeCashBalance(BigDecimal.ZERO);
            } else {
                member.setFreezeCashBalance(member.getFreezeCashBalance().subtract(payment.getAmount()));
            }
            member.setBalance(member.getBalance().subtract(payment.getAmount()));

            if (totalBalance.compareTo(payment.getAmount()) >= 0) {
                memberDao.merge(member);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.payment);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(BigDecimal.ZERO);
                deposit.setDebit(payment.getAmount());
                deposit.setBalance(member.getBalance());
                deposit.setOperator(member != null ? member.getUsername() : null);
                deposit.setMember(member);
                deposit.setMemo("红包冻结资金");
                deposit.setOrder(null);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                payment.setStatus(Payment.Status.success);

                Setting setting = SettingUtils.get();
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "您的账户，发放红包，冻结" + setting.setScale(payment.getAmount()).toString() + "元，流水号:" + payment.getSn() + "",
                            null, member, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.tenant);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                payment.setAmount(BigDecimal.ZERO);
                payment.setClearAmount(BigDecimal.ZERO);
            }
        } else {
            Member member = payment.getMember();
            Deposit deposit1 = new Deposit();
            deposit1.setType(Deposit.Type.recharge);
            deposit1.setStatus(Deposit.Status.complete);
            deposit1.setCredit(payment.getAmount());
            deposit1.setDebit(BigDecimal.ZERO);
            deposit1.setBalance(member.getBalance().add(payment.getAmount()));
            deposit1.setOperator(member != null ? member.getUsername() : null);
            deposit1.setMember(member);
            deposit1.setOrder(null);
            deposit1.setMemo("红包冻结资金 单号:" + payment.getSn());
            Deposit deposit2 = new Deposit();
            deposit2.setType(Deposit.Type.payment);
            deposit2.setStatus(Deposit.Status.complete);
            deposit2.setCredit(BigDecimal.ZERO);
            deposit2.setDebit(payment.getAmount());
            deposit2.setBalance(member.getBalance());
            deposit2.setOperator(member != null ? member.getUsername() : null);
            deposit2.setMember(member);
            deposit2.setMemo("红包冻结资金 单号:" + payment.getSn());
            deposit2.setOrder(null);
            depositDao.persist(deposit1);
            depositDao.persist(deposit2);
        }
        //对主订单意外更新失败、子订单更新成功对问题，捕获异常
        paymentDao.merge(payment);

        if (payment.getAmount().compareTo(BigDecimal.ZERO) != 0) {
            coupon.setStatus(Coupon.Status.confirmed);
        }
        couponDao.merge(coupon);
    }

    //红包核销完成,member指核人
    public void complete(CouponCode couponCode, Member operator) {
        couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_WRITE);
        if (!couponCode.getIsUsed()) {
            couponCode.setOperateMember(operator);
            couponCode.setOperateDate(new Date());
            couponCode.setIsUsed(true);
            couponCode.setUsedDate(new Date());
            couponCodeDao.merge(couponCode);
            Coupon coupon = couponCode.getCoupon();
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponDao.merge(coupon);
            Setting setting = SettingUtils.get();
            BigDecimal share = setting.setScale(coupon.getFreezePrice().multiply(setting.getSharePercent()));
            BigDecimal guide = setting.setScale(coupon.getFreezePrice().multiply(setting.getGuidePercent()));
            BigDecimal shareOwner = setting.setScale(coupon.getFreezePrice().multiply(setting.getShareOwnerPercent()));
            BigDecimal back = setting.setScale(coupon.getFreezePrice().multiply(setting.getCashBackPercent()));
            BigDecimal bal = share.subtract(guide).subtract(shareOwner).subtract(back);

            //推广佣金
            if (couponCode.getGuideMember() != null && share.compareTo(BigDecimal.ZERO) > 0) {
                Rebate rebate = new Rebate();
                rebate.setType(Type.extension);
                rebate.setBrokerage(coupon.getFreezePrice());
                rebate.setDescription("红包推广佣金");
                rebate.setMember(couponCode.getGuideMember());
                rebate.setOrderType(Rebate.OrderType.coupon);
                rebate.setAmount(share);
                rebate.setPercent(setting.getSharePercent());
                rebate.setStatus(Status.success);
                rebate.setCouponCode(couponCode);
                rebateDao.merge(rebate);

                Member guideMember = couponCode.getGuideMember();
                memberDao.lock(guideMember, LockModeType.PESSIMISTIC_WRITE);
                guideMember.setBalance(guideMember.getBalance().add(rebate.getAmount()));
                memberDao.merge(guideMember);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.profit);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(rebate.getAmount());
                deposit.setDebit(BigDecimal.ZERO);
                deposit.setBalance(guideMember.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(guideMember);
                String msg = "红包推广佣金";
                deposit.setMemo(msg);
                deposit.setOrder(null);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "推广" + couponCode.getCoupon().getTenant().getName() + "的红包，获得" + setting.setScale(rebate.getAmount()).toString() + "元分润。",
                            null, guideMember, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.tenant);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //店主推广佣金
            if (couponCode.getGuideMember() != null && couponCode.getGuideMember().getTenant() != null && couponCode.getGuideMember().getTenant().getMember() != null && shareOwner.compareTo(BigDecimal.ZERO) > 0) {
                Rebate rebate = new Rebate();
                rebate.setType(Type.extension);
                rebate.setBrokerage(coupon.getFreezePrice());
                rebate.setDescription("店主红包推广佣金");
                rebate.setMember(couponCode.getGuideMember().getTenant().getMember());
                rebate.setOrderType(Rebate.OrderType.coupon);
                rebate.setAmount(shareOwner);
                rebate.setPercent(setting.getShareOwnerPercent());
                rebate.setStatus(Status.success);
                rebate.setCouponCode(couponCode);
                rebateDao.merge(rebate);

                Member guideMember = couponCode.getGuideMember().getTenant().getMember();
                memberDao.lock(guideMember, LockModeType.PESSIMISTIC_WRITE);
                guideMember.setBalance(guideMember.getBalance().add(rebate.getAmount()));
                memberDao.merge(guideMember);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.profit);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(rebate.getAmount());
                deposit.setDebit(BigDecimal.ZERO);
                deposit.setBalance(guideMember.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(guideMember);
                String msg = "店主红包推广佣金";
                deposit.setMemo(msg);
                deposit.setOrder(null);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "推广" + couponCode.getCoupon().getTenant().getName() + "的红包，获得" + setting.setScale(rebate.getAmount()).toString() + "元分润。",
                            null, guideMember, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.tenant);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //核销佣金
            if (couponCode.getOperateMember() != null && guide.compareTo(BigDecimal.ZERO) > 0) {
                Rebate rebate = new Rebate();
                rebate.setType(Type.sale);
                rebate.setBrokerage(coupon.getFreezePrice());
                rebate.setDescription("红包核销佣金");
                rebate.setMember(couponCode.getOperateMember());
                rebate.setOrderType(Rebate.OrderType.coupon);
                rebate.setAmount(guide);
                rebate.setPercent(setting.getGuidePercent());
                rebate.setStatus(Status.success);
                rebate.setCouponCode(couponCode);
                rebateDao.merge(rebate);

                Member optMember = couponCode.getOperateMember();
                memberDao.lock(optMember, LockModeType.PESSIMISTIC_WRITE);
                optMember.setBalance(optMember.getBalance().add(rebate.getAmount()));
                memberDao.merge(optMember);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.profit);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(rebate.getAmount());
                deposit.setDebit(BigDecimal.ZERO);
                deposit.setBalance(optMember.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(optMember);
                String msg = "红包核销佣金";
                deposit.setMemo(msg);
                deposit.setOrder(null);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "核销" + couponCode.getMember().getDisplayName() + "的红包，获得" + setting.setScale(rebate.getAmount()).toString() + "元分润。",
                            null, optMember, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.tenant);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //消费返现
            if (couponCode.getMember() != null && back.compareTo(BigDecimal.ZERO) > 0) {
                Rebate rebate = new Rebate();
                rebate.setType(Type.rebate);
                rebate.setBrokerage(coupon.getFreezePrice());
                rebate.setDescription("红包核销返现");
                rebate.setMember(couponCode.getOperateMember());
                rebate.setOrderType(Rebate.OrderType.coupon);
                rebate.setAmount(back);
                rebate.setPercent(setting.getCashBackPercent());
                rebate.setStatus(Status.success);
                rebate.setCouponCode(couponCode);
                rebateDao.merge(rebate);

                Member member = couponCode.getMember();
                memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
                member.setBalance(member.getBalance().add(rebate.getAmount()));
                memberDao.merge(member);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.profit);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(rebate.getAmount());
                deposit.setDebit(BigDecimal.ZERO);
                deposit.setBalance(member.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(member);
                String msg = "红包核销返现";
                deposit.setMemo(msg);
                deposit.setOrder(null);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "核销" + couponCode.getCoupon().getTenant().getName() + "的红包，获得" + setting.setScale(rebate.getAmount()).toString() + "元返现。",
                            null, member, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.member);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //平台佣金
            if (bal.compareTo(BigDecimal.ZERO) > 0) {
                Rebate rebate = new Rebate();
                rebate.setType(Type.platform);
                rebate.setBrokerage(coupon.getFreezePrice());
                rebate.setDescription("红包平台佣金");
                rebate.setMember(couponCode.getOperateMember());
                rebate.setOrderType(Rebate.OrderType.none);
                rebate.setAmount(bal);
                rebate.setPercent(BigDecimal.ZERO);
                rebate.setStatus(Status.success);
                rebate.setCouponCode(couponCode);
                rebateDao.merge(rebate);
            }
        }

    }

}