/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.Setting.StockAllocationTime;
import net.wit.constant.SettingConstant;
import net.wit.dao.*;
import net.wit.domain.StockStrategy;
import net.wit.entity.*;
import net.wit.entity.Order.*;
import net.wit.entity.OrderLog.Type;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.DateUtil;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.webservice.xxkyService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service - 订单
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Resource(name = "orderDaoImpl")
    private OrderDao orderDao;

    @Resource(name = "orderItemDaoImpl")
    private OrderItemDao orderItemDao;

    @Resource(name = "orderLogDaoImpl")
    private OrderLogDao orderLogDao;

    @Resource(name = "cartItemDaoImpl")
    private CartItemDao cartItemDao;

    @Resource(name = "cartDaoImpl")
    private CartDao cartDao;

    @Resource(name = "rebateDaoImpl")
    private RebateDao rebateDao;

    @Resource(name = "couponNumberServiceImpl")
    private CouponNumberService couponNumberService;

    @Resource(name = "couponDaoImpl")
    private CouponDao couponDao;

    @Resource(name = "couponCodeDaoImpl")
    private CouponCodeDao couponCodeDao;

    @Resource(name = "spReturnsDaoImpl")
    private SpReturnsDao spReturnsDao;

    @Resource(name = "snDaoImpl")
    private SnDao snDao;

    @Resource(name = "paymentDaoImpl")
    private PaymentDao paymentDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "tradeDaoImpl")
    private TradeDao tradeDao;

    @Resource(name = "productDaoImpl")
    private ProductDao productDao;

    @Resource(name = "depositDaoImpl")
    private DepositDao depositDao;

    @Resource(name = "refundsDaoImpl")
    private RefundsDao refundsDao;

    @Resource(name = "shippingDaoImpl")
    private ShippingDao shippingDao;

    @Resource(name = "returnsDaoImpl")
    private ReturnsDao returnsDao;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "appointmentServiceImpl")
    private AppointmentService appointmentService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "tenantDaoImpl")
    private TenantDao tenantDao;

    @Resource(name = "productCategoryDaoImpl")
    private ProductCategoryDao productCategoryDao;

    @Resource(name = "taskDaoImpl")
    private TaskDao taskDao;

    @Resource(name = "extendCatalogDaoImpl")
    private ExtendCatalogDao extendCatalogDao;

    @Resource(name="stockStrategy")
    private StockStrategy stockStrategy;

    @Resource(name = "orderDaoImpl")
    public void setBaseDao(OrderDao orderDao) {
        super.setBaseDao(orderDao);
    }

    public void releaseStock() {
        orderDao.releaseStock();
    }

    @Transactional(readOnly = true)
    public Member getExtensionCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String extension = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
            if (extension != null) {
                return memberDao.findByUsername(extension);
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Order build(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Set<CouponCode> couponCodes, boolean isInvoice, String invoiceTitle, boolean useBalance, Long[] tenantIds, String[] memo) {
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.notEmpty(cart.getCartItems());
        Order order = EntitySupport.createInitOrder();
        if (receiver != null) {
            // order.setMember(receiver.getMember());
            order.setConsignee(receiver.getConsignee());
            order.setAreaName(receiver.getAreaName());
            order.setAddress(receiver.getAddress());
            order.setZipCode(receiver.getZipCode());
            order.setPhone(receiver.getPhone());
            order.setArea(receiver.getArea());
            order.setReceiver(receiver);
        }
        order.setMember(cart.getMember());
        if (!cart.getPromotions().isEmpty()) {
            StringBuffer promotionName = new StringBuffer();
            for (Promotion promotion : cart.getPromotions()) {
                if (promotion != null && promotion.getName() != null) {
                    promotionName.append(" " + promotion.getName());
                }
            }
            if (promotionName.length() > 0) {
                promotionName.deleteCharAt(0);
            }
            order.setPromotion(promotionName.toString());
        }
        order.setPoint(cart.getEffectivePoint());
        order.setPaymentMethod(paymentMethod);
        order.setShippingMethod(shippingMethod);
        Member extension = getExtensionCurrent();
        order.setExtension(extension);
        List<Trade> trades = order.getTrades();

        for (CartItem cartItem : cart.getCartItems()) {
            cartItem.setTotal(0);
            if (cartItem != null && cartItem.getProduct() != null && cartItem.getSelected()) {
                Product product = cartItem.getProduct();
                OrderItem orderItem = new OrderItem();
                Tenant tenant = product.getTenant();
                Trade trade = order.getTrade(tenant);
                if (trade == null) {
                    trade = EntitySupport.createInitTrade();
                    trade.setTenant(tenant);
                    int challege = SpringUtils.getIdentifyingCode();
                    String securityCode = String.valueOf(challege);
                    SafeKey safeKey = new SafeKey();
                    safeKey.setValue(securityCode);
                    trade.setSafeKey(safeKey);
                    trade.setOrder(order);
                    trades.add(trade);
                }

                List<OrderItem> tradeItems = trade.getOrderItems();
                List<OrderItem> orderItems = order.getOrderItems();
                orderItem.setTrade(trade);
                orderItem.setSn(product.getSn());
                orderItem.setBarcode(product.getBarcode());
                orderItem.setName(product.getName());
                orderItem.setFullName(product.getFullName());
                orderItem.setThumbnail(product.getThumbnail());
                orderItem.setIsGift(false);
                Promotion promotion = cartItem.getSeckPromotion();
                if (promotion != null && promotion.getMaximumQuantity() > 0) {
                    Integer buy = orderDao.getMemberBuyDay(order.getMember(), cartItem.getProduct());
                    if (promotion.getMaximumQuantity() >= (buy + cartItem.getQuantity())) {
                        orderItem.setPrice(cartItem.getEffectivePrice());
                    } else {
                        orderItem.setPrice(cartItem.getPrice());
                    }
                } else {
                    orderItem.setPrice(cartItem.getEffectivePrice());
                }
                Promotion buyFreepromotion = cartItem.getBuyfreePromotion();
                if (buyFreepromotion != null && buyFreepromotion.getMaximumQuantity() > 0) {
                    Integer buy = orderDao.getMemberBuyDay(order.getMember(), cartItem.getProduct());
                    cartItem.setTotal(buy);
                }
                orderItem.setCost(product.getCost());
                orderItem.setWeight(cartItem.getWeight());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setDiscount(cartItem.getDiscount());
                orderItem.setCalculatePackagUnit(product, cartItem.getPackagUnit());
                orderItem.setShippedQuantity(0);
                orderItem.setReturnQuantity(0);
                orderItem.setProfit(product.calculateFee());
                orderItem.setProduct(product);
                orderItem.setTrade(trade);
                orderItem.setOrder(order);
                orderItem.setPackagUnitName(product.getUnit());
                orderItem.setSupplier(product.getSupplier());
                if (orderItem.getSupplier() == null) {
                    orderItem.setSupplier(product.getTenant());
                }
                tradeItems.add(orderItem);
                orderItems.add(orderItem);
            }
        }

        for (GiftItem giftItem : cart.getGiftItems()) {
            if (giftItem != null && giftItem.getGift() != null) {
                Product gift = giftItem.getGift();
                OrderItem orderItem = new OrderItem();
                Tenant tenant = gift.getTenant();
                Trade trade = order.getTrade(tenant);
                if (trade == null) {
                    trade = EntitySupport.createInitTrade();
                    int challege = SpringUtils.getIdentifyingCode();
                    String securityCode = String.valueOf(challege);
                    SafeKey safeKey = new SafeKey();
                    safeKey.setValue(securityCode);
                    trade.setSafeKey(safeKey);
                    trade.setOrder(order);
                    trade.setClearing(false);
                    trade.setClearingDate(null);
                    trades.add(trade);
                }
                List<OrderItem> tradeItems = trade.getOrderItems();
                List<OrderItem> orderItems = order.getOrderItems();
                orderItem.setSn(gift.getSn());
                orderItem.setName(gift.getName());
                orderItem.setBarcode(gift.getBarcode());
                orderItem.setFullName(gift.getFullName());
                orderItem.setPrice(BigDecimal.ZERO);
                orderItem.setCost(gift.getCost());
                orderItem.setWeight(gift.getWeight());
                orderItem.setThumbnail(gift.getThumbnail());
                orderItem.setIsGift(true);
                orderItem.setQuantity(giftItem.getQuantity());
                orderItem.setShippedQuantity(0);
                orderItem.setReturnQuantity(0);
                orderItem.setProfit(BigDecimal.ZERO);
                orderItem.setProduct(gift);
                orderItem.setCoefficient(BigDecimal.ONE);
                orderItem.setDiscount(BigDecimal.ZERO);
                orderItem.setTrade(trade);
                orderItem.setOrder(order);
                orderItem.setPackagUnitName(gift.getUnit());
                orderItem.setSupplier(gift.getSupplier());
                if (orderItem.getSupplier() == null) {
                    orderItem.setSupplier(giftItem.getGift().getTenant());
                }
                tradeItems.add(orderItem);
                orderItems.add(orderItem);
            }
        }

        // if (receiver == null) {
        BigDecimal totalFreight = BigDecimal.ZERO;
        //order.setPromotionDiscount(cart.getDiscount());
        BigDecimal totalDiscount = BigDecimal.ZERO;
        Long effectivePoint = 0L;
        for (Trade trade : trades) {
            CouponCode couponCode = null;
            if (couponCodes != null) {
                for (CouponCode code : couponCodes) {
                    if (code != null && code.getCoupon().getTenant().equals(trade.getTenant())) {
                        couponCode = code;
                    }
                }
            }

            if (couponCode != null) {
                if (Coupon.Type.pickup == couponCode.getCoupon().getType()) { // 提货券
                    couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_WRITE);
                    if (!couponCode.getIsUsed() && couponCode.getCoupon() != null) {
                        BigDecimal couponDiscount = BigDecimal.ZERO;
                        for (CartItem cartItem : cart.getCartItems()) {
                            if (couponCode.getCoupon().getProducts().contains(cartItem.getProduct())) {
                                couponDiscount = couponDiscount.add(cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
                            }
                        }
                        trade.setCouponDiscount(couponDiscount);
                        trade.setTenantCouponCode(couponCode);
                    }
                } else if (Coupon.Type.coupon == couponCode.getCoupon().getType()) { // 折扣券
                    if (cart.isCouponAllowed()) {
                        couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_WRITE);
                        if (!couponCode.getIsUsed() && couponCode.getCoupon() != null && cart.isValid(couponCode.getCoupon())) {
                            BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectiveQuantity(), cart.getEffectivePrice()));
                            couponDiscount = couponDiscount.compareTo(BigDecimal.ZERO) > 0 ? couponDiscount : BigDecimal.ZERO;
                            trade.setCouponDiscount(couponDiscount);
                            trade.setTenantCouponCode(couponCode);
                        }
                    }
                } else if (Coupon.Type.tenantCoupon == couponCode.getCoupon().getType()) { // 代金券
                    if (cart.isCouponAllowed()) {
                        couponCodeDao.lock(couponCode, LockModeType.PESSIMISTIC_WRITE);
                        if (!couponCode.getIsUsed() && couponCode.getCoupon() != null && cart.isValid(couponCode.getCoupon())) {
                            BigDecimal couponDiscount = cart.getEffectivePrice().subtract(couponCode.getCoupon().calculatePrice(cart.getEffectiveQuantity(), cart.getEffectivePrice()));
                            couponDiscount = couponDiscount.compareTo(BigDecimal.ZERO) > 0 ? couponDiscount : BigDecimal.ZERO;
                            trade.setCouponDiscount(couponDiscount);
                            trade.setTenantCouponCode(couponCode);
                        }
                    }
                }
            }

            // if (shippingMethod != null && paymentMethod != null && paymentMethod.getShippingMethods().contains(shippingMethod)) {
            if (shippingMethod != null && !shippingMethod.getMethod().equals(ShippingMethod.Method.F2F)) {
                BigDecimal freight = trade.getTenant().calculateFreight(trade.getWeight(), trade.getQuantity());
                if(receiver!=null){
                    freight = trade.getTenant().calculateTenantFreight(receiver.getArea(),trade.getWeight(), trade.getQuantity());
                }
                for (Promotion promotion : trade.getMailPromotions()) {
                    if (promotion.getIsFreeShipping()) {
                        freight = BigDecimal.ZERO;
                        break;
                    }
                }
                trade.setFreight(freight);
                totalFreight = totalFreight.add(freight);
            } else {
                trade.setFreight(BigDecimal.ZERO);
            }
            trade.setPromotionDiscount(trade.calcPromotionDiscount());
            totalDiscount = totalDiscount.add(trade.getPromotionDiscount());

            trade.setPoint(cart.getEffectivePoint(trade.getTenant()));

            effectivePoint = effectivePoint + trade.getPoint();

            CouponCode activityCode = cart.getCouponCode();
            if (activityCode != null && activityCode.getTenant().equals(trade.getTenant())) {
                BigDecimal discount = activityCode.getCoupon().calculatePrice(trade.getQuantity(), trade.getAmount());
                discount = trade.getAmount().subtract(discount.compareTo(BigDecimal.ZERO) > 0 ? discount : BigDecimal.ZERO);
                trade.setDiscount(discount);
                trade.setCouponCode(activityCode);
            } 
        }
        order.setCouponDiscount(order.calcCouponDiscount());
        order.setPoint(effectivePoint);
        order.setFreight(totalFreight);
        order.setPromotionDiscount(totalDiscount);
        order.setExtension(getExtensionCurrent());
        Setting setting = SettingUtils.get();
        
       //联盟佣金计算
        for (Trade trade:order.getTrades()) {
        	if (order.getExtension()!=null) {
               trade.setAgencyAmount(trade.calcAgencyAmount());          //联盟佣金
        	} else {
        	   CouponCode pCode = couponCodeDao.findMemberCouponCode(order.getMember());
        	   if (pCode!=null) {
                   trade.setAgencyAmount(trade.calcAgencyAmount());     //联盟佣金
        		   trade.setCouponCode(pCode);
        		   trade.setDiscount(
        				   setting.setScale(trade.getAgencyAmount().multiply(
        						   BigDecimal.ONE.subtract(setting.getBrokerage()).subtract(setting.getGuidePercent()).subtract(setting.getGuideOwnerPercent())
        						   )
        				   )
        		   );
        	   } else {
        		   trade.setAgencyAmount(BigDecimal.ZERO);
        	   }
        	}
        }

        if (setting.getIsInvoiceEnabled() && isInvoice && StringUtils.isNotEmpty(invoiceTitle)) {
            order.setIsInvoice(true);
            order.setInvoiceTitle(invoiceTitle);
            BigDecimal totalTax = BigDecimal.ZERO;

            for (Trade trade : order.getTrades()) {
                trade.setTax(trade.calculateTax());
                totalTax = totalTax.add(trade.getTax());
            }
            order.setTax(totalTax);
        } else {
            order.setIsInvoice(false);
            order.setTax(BigDecimal.ZERO);
        }
        order.setAmountPaid(BigDecimal.ZERO);
        return order;
    }

    @Transactional
    public Order create(Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, Set<CouponCode> couponCodes, boolean isInvoice, String invoiceTitle, boolean useBalance, Long[] tenantIds, String[] memos, List<DeliveryCenter> deliveryCenters, Member operator, Appointment appointment,
                        OrderSource orderSource, Equipment equipment, String token_key) {
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.notEmpty(cart.getCartItems());
//		Assert.notNull(receiver);
        Assert.notNull(paymentMethod);
        Assert.notNull(shippingMethod);
        Order order = build(cart, receiver, paymentMethod, shippingMethod, couponCodes, isInvoice, invoiceTitle, useBalance, tenantIds, memos);
        if (shippingMethod.getMethod() == ShippingMethod.Method.F2F) {
            if (receiver == null) {
                DeliveryCenter deliveryCenter = deliveryCenters.get(0);
                order.setConsignee(deliveryCenter.getName());
                order.setAreaName(deliveryCenter.getAreaName());
                order.setAddress(deliveryCenter.getAddress());
                order.setZipCode(deliveryCenter.getZipCode()==null?"000000":deliveryCenter.getZipCode());
                order.setPhone(deliveryCenter.getPhone()==null?deliveryCenter.getMobile():deliveryCenter.getPhone());
                order.setArea(deliveryCenter.getArea());
            }
        }
        // 订单状态
        if (order.getAmountPayable().compareTo(BigDecimal.ZERO) == 0) {
            order.setOrderStatus(OrderStatus.unconfirmed);
            order.setPaymentStatus(PaymentStatus.paid);
        } else if (order.getAmountPayable().compareTo(BigDecimal.ZERO) > 0 && order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            order.setOrderStatus(OrderStatus.unconfirmed);
            order.setPaymentStatus(PaymentStatus.partialPayment);
        } else {
            if (paymentMethod.getMethod().equals(PaymentMethod.Method.offline)) {
                order.setOrderStatus(OrderStatus.unconfirmed);
            } else {
                order.setOrderStatus(OrderStatus.unconfirmed);
            }
            order.setPaymentStatus(PaymentStatus.unpaid);
        }

        order.setShippingStatus(ShippingStatus.unshipped);
        if (orderSource != null) {
            order.setOrderSource(orderSource);
        } else {
            order.setOrderSource(OrderSource.web);
        }

        for (Trade trade : order.getTrades()) {
            trade.setOrderStatus(order.getOrderStatus());
            trade.setPaymentStatus(order.getPaymentStatus());
            trade.setShippingStatus(ShippingStatus.unshipped);
            trade.setTotalProfit(BigDecimal.ZERO);        //推广佣金
            trade.setProviderAmount(BigDecimal.ZERO);  //平台佣金
            trade.setEquipment(equipment);
            CouponCode tradeCode = trade.getCouponCode();
            if (tradeCode != null) {
                if (tradeCode.getCoupon().getType().equals(Coupon.Type.coupon)) {
                    tradeCode.setMember(order.getMember());
                    tradeCode.setTenant(trade.getTenant());
                }
                tradeCode.setUsedDate(new Date());
                tradeCode.getCoupon().setUsedCount(tradeCode.getCoupon().getUsedCount() + 1);
                tradeCode.setBalance(tradeCode.getBalance().subtract(trade.getDiscount()));
                if (tradeCode.getBalance().compareTo(BigDecimal.ZERO)<=0) {
                    tradeCode.setIsUsed(true);
                } else {
                    tradeCode.setIsUsed(false);
                }
                couponCodeDao.merge(tradeCode);
            }
            CouponCode tenantCode = trade.getTenantCouponCode();
            if (tenantCode != null) {
                tenantCode.setUsedDate(new Date());
                tenantCode.getCoupon().setUsedCount(tenantCode.getCoupon().getUsedCount() + 1);
                tenantCode.setBalance(tenantCode.getBalance().subtract(trade.getCouponDiscount()));
                tenantCode.setIsUsed(true);
                couponCodeDao.merge(tenantCode);
            }
        }

        order.setEquipment(equipment);

        if (paymentMethod != null && paymentMethod.getTimeout() != null && order.getPaymentStatus() == PaymentStatus.unpaid) {
            order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
        }
        if (order.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            order.setExpire(DateUtils.addMinutes(new Date(), 15));
        }
        if (paymentMethod.getMethod() == PaymentMethod.Method.online) {
            order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
            order.setOperator(operator);
        }
        for (Promotion promotion : cart.getPromotions()) {
            for (Coupon coupon : promotion.getCoupons()) {
                order.getCoupons().add(coupon);
            }
        }
        if (order.getTrades().size() > 1) {
            order.setOrderType(OrderType.composite);
        } else {
            order.setOrderType(OrderType.single);
        }
        if (appointment == null) {
            appointment = appointmentService.findDefault();
        }
        order.setAppointment(appointment);
        order.setSn(snDao.generate(Sn.Type.order));
        order.setIsProfitSettlement(false);
        for (Trade trade : order.getTrades()) {
            trade.setSn(snDao.generate(Sn.Type.trade));
        }
        int idx = 0;
        if (tenantIds != null) {
            for (Long tid : tenantIds) {
                Trade trade = order.getTrade(tid);
                trade.setMemo(memos[idx]);
                idx = idx + 1;
            }
        }else{
            if(memos!=null&&memos.length==1){
                order.setMemo(memos[0]);
                for (Trade trade:order.getTrades()){
                    trade.setMemo(memos[0]);
                }
            }
        }

        if(memos!=null&&memos.length==1){
            order.setMemo(memos[0]);
        }

        if (deliveryCenters != null) {
            for (DeliveryCenter deliveryCenter : deliveryCenters) {
                Trade trade = order.getTrade(deliveryCenter.getTenant().getId());
                trade.setDeliveryCenter(deliveryCenter);
            }
        }else{
            for(Trade trade:order.getTrades()){
                trade.setDeliveryCenter(trade.getTenant().getDefaultDeliveryCenter());
            }
        }

        Setting setting = SettingUtils.get();
        if (setting.getStockAllocationTime() == StockAllocationTime.order) {
            for (Trade trade : order.getTrades()) {
                trade.setIsAllocatedStock(true);
                stockStrategy.lockAllocatedTrade(trade);
            }
        } else {
            for (Trade trade : order.getTrades()) {
                trade.setIsAllocatedStock(false);
            }
        }

        Member extension = getExtensionCurrent();
        order.setExtension(extension);
        order.setTokenKey(token_key);
        orderDao.persist(order);
        if (useBalance) {
            Payment payment = new Payment();
            payment.setSn(snService.generate(Sn.Type.payment));
            payment.setType(Payment.Type.payment);
            payment.setMethod(Payment.Method.deposit);
            if (order.getAmountPayable().compareTo(BigDecimal.ZERO) == 0) {
                payment.setStatus(Status.success);
            } else {
                payment.setStatus(Status.wait);
            }
            payment.setPaymentMethod(order.getPaymentMethodName());
            payment.setFee(new BigDecimal(0));
            payment.setClearAmount(BigDecimal.ZERO);
            payment.setPaymentDate(new Date());
            payment.setPaymentPluginId(null);
            payment.setExpire(null);
            payment.setMember(null);
            payment.setMemo("订单-余额支付");
            payment(order, payment, null);
        }

        for (Trade trade : order.getTrades()) {
            OrderLog orderLog = new OrderLog();
            orderLog.setType(Type.create);
            orderLog.setOperator(operator != null ? operator.getUsername() : null);
            orderLog.setOrder(order);
            orderLog.setContent("创建订单完成");
            orderLog.setTrade(trade);
            orderLogDao.persist(orderLog);
        }

        for (CartItem cartItem : cart.getEffectiveCartItems()) {
            cartItemDao.remove(cartItem);
        }
        if (equipment != null) {
            cartDao.clear();
        }
        cart.setCouponCode(null);
        cartDao.merge(cart);
        return order;
    }

    @Transactional
    public void update(Order order, String operator) {
        Assert.notNull(order);
        Order prefixOrder = orderDao.find(order.getId());

        for (Trade trade : prefixOrder.getTrades()) {
            if (trade.getIsAllocatedStock()) {
                stockStrategy.releaseAllocatedTrade(trade);
            }
        }

        orderDao.merge(order);

        Order currentOrder = orderDao.find(order.getId());
        for (Trade trade : currentOrder.getTrades()) {
            if (trade.getIsAllocatedStock()) {
                stockStrategy.lockAllocatedTrade(trade);
            }
        }
    }

    @Transactional
    public void updatePrice(Trade trade, BigDecimal amount, BigDecimal freight, String operator) {
        Order order = trade.getOrder();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (!trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
            return;
        }
        BigDecimal promotionDiscount = BigDecimal.ZERO;
        BigDecimal calcOffsetAmount = BigDecimal.ZERO;
        BigDecimal calcFreight = BigDecimal.ZERO;
        List<Trade> trades = order.getTrades();
        for (Trade saveTrade : trades) {
            if (saveTrade.getId().equals(trade.getId())) {
                saveTrade.setOffsetAmount(amount.subtract(saveTrade.getPrice().add(saveTrade.getTax()).add(saveTrade.getPromotionDiscount())));
                saveTrade.setFreight(freight);
                saveTrade.setTotalProfit(trade.calcTotalProfit()); //推广佣金
                saveTrade.setAgencyAmount(trade.calcAgencyAmount());      //联盟佣金
                saveTrade.setProviderAmount(trade.calcProviderAmount());  //平台佣金
            }
            promotionDiscount = promotionDiscount.add(saveTrade.getPromotionDiscount());
            calcOffsetAmount = calcOffsetAmount.add(saveTrade.getOffsetAmount());
            calcFreight = calcFreight.add(saveTrade.getFreight());
        }
        order.setPromotionDiscount(promotionDiscount);
        order.setOffsetAmount(calcOffsetAmount);
        order.setFreight(calcFreight);
        update(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setContent("商家调价完成");
        orderLog.setType(Type.modify);
        orderLog.setOperator(operator != null ? operator : null);
        orderLog.setOrder(order);
        orderLog.setTrade(trade);
        orderLogDao.persist(orderLog);
    }

    public void confirm(Order order, Member operator) {
        Assert.notNull(order);
        order.setOrderStatus(OrderStatus.confirmed);
        orderDao.merge(order);
    }

    @Transactional
    public void cancel(Order order, Member operator) {
        Assert.notNull(order);
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (order.getPaymentStatus().equals(PaymentStatus.paid)) {
            return;
        }
        if (order.getOrderStatus().equals(OrderStatus.cancelled)) {
            return;
        }
        for (Trade trade : order.getTrades()) {
            if (trade.getIsAllocatedStock()) {
                stockStrategy.releaseAllocatedTrade(trade);
                trade.setIsAllocatedStock(false);
            }
            trade.setOrderStatus(OrderStatus.cancelled);
        }
        order.setOrderStatus(OrderStatus.cancelled);
        order.setExpire(null);
        orderDao.merge(order);

        for (Trade trade : order.getTrades()) {
            CouponCode tenantCouponCode = trade.getTenantCouponCode();
            if (tenantCouponCode != null) {
                tenantCouponCode.setIsUsed(false);
                tenantCouponCode.setUsedDate(null);
                couponCodeDao.merge(tenantCouponCode);
                Coupon coupon = tenantCouponCode.getCoupon();
                coupon.setUsedCount(coupon.getUsedCount() - 1);
                couponDao.merge(coupon);
            }

            CouponCode couponCode = trade.getCouponCode();
            if (couponCode != null && couponCode.getCoupon().getType().equals(Coupon.Type.coupon)) {
                couponCode.setIsUsed(false);
                couponCode.setUsedDate(null);
                couponCode.setMember(null);
                couponCodeDao.merge(couponCode);
                Coupon coupon = couponCode.getCoupon();
                coupon.setUsedCount(coupon.getUsedCount() - 1);
                couponDao.merge(coupon);
            }

            OrderLog orderLog = new OrderLog();
            orderLog.setType(Type.cancel);
            orderLog.setContent("订单已取消");
            orderLog.setOperator(operator != null ? operator.getUsername() : null);
            orderLog.setOrder(order);
            orderLog.setTrade(trade);
            orderLogDao.persist(orderLog);
            Message message = EntitySupport.createInitMessage(Message.Type.order,
                    "您的订单，单号:" + order.getSn() + ",已取消..",
                    order.getSn(), order.getMember(), null);
            message.setTrade(trade);
            message.setWay(Message.Way.member);
            messageService.save(message);
        }
    }

    @Transactional
    public void cancel(Trade trade, Member operator) {
        Order order = trade.getOrder();
        Assert.notNull(order);
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (trade.getOrderStatus().equals(OrderStatus.cancelled)) {
            return;
        }
        if (trade.getPaymentStatus().equals(PaymentStatus.paid)) {
            return;
        }
        for (Trade saveTrade : order.getTrades()) {
            if (saveTrade.equals(trade)) {
                if (trade.getIsAllocatedStock()) {
                    stockStrategy.releaseAllocatedTrade(trade);
                    trade.setIsAllocatedStock(false);
                }
                saveTrade.setOrderStatus(OrderStatus.cancelled);
            }
        }
        if (order.getOrderType().equals(OrderType.single)) {
            order.setOrderStatus(OrderStatus.cancelled);
        }
        order.setExpire(null);
        orderDao.merge(order);

        CouponCode tenantCouponCode = trade.getTenantCouponCode();
        if (tenantCouponCode != null) {
            tenantCouponCode.setIsUsed(false);
            tenantCouponCode.setUsedDate(null);
            couponCodeDao.merge(tenantCouponCode);
            Coupon coupon = tenantCouponCode.getCoupon();
            coupon.setUsedCount(coupon.getUsedCount() - 1);
            couponDao.merge(coupon);
        }

        CouponCode couponCode = trade.getCouponCode();
        if (couponCode != null && couponCode.getCoupon().getType().equals(Coupon.Type.coupon)) {
            couponCode.setIsUsed(false);
            couponCode.setUsedDate(null);
            couponCode.setMember(null);
            couponCodeDao.merge(couponCode);
            Coupon coupon = couponCode.getCoupon();
            coupon.setUsedCount(coupon.getUsedCount() - 1);
            couponDao.merge(coupon);
        }
        
        OrderLog orderLog = new OrderLog();
        orderLog.setType(Type.cancel);
        orderLog.setContent("订单已取消");
        orderLog.setOperator(operator != null ? operator.getUsername() : null);
        orderLog.setOrder(order);
        orderLog.setTrade(trade);
        orderLogDao.persist(orderLog);
        Message message = EntitySupport.createInitMessage(Message.Type.order,
                "您的订单，单号:" + order.getSn() + ",已取消..",
                order.getSn(), order.getMember(), null);
        message.setTrade(trade);
        message.setWay(Message.Way.member);
        messageService.save(message);
    }

    public void pushTo(Order order) {
        for (Trade trade : order.getTrades()) {
            pushTo(trade);
        }
    }

    public void taskPush(final String sn, final String centerSn) {
        taskExecutor.execute(new Runnable() {
            public void run() {
                try {
                    PushMessage.send("您有新订单了，单号:" + sn + ",请尽快处理..", "rzico://shop.dll/TfrmOnOrderManage?sn=" + sn, centerSn);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public void pushTo(Trade trade) {
        Receiver receiver = trade.getOrder().getReceiver();
        Setting setting = SettingUtils.get();
        DeliveryCenter center = null;
        if (receiver!=null&&receiver.getCommunity() != null) {
            center = deliveryCenterService.findByLocation(trade.getTenant(), receiver.getCommunity().getLocation() );
        } else {
            center = deliveryCenterService.findByLocation(trade.getTenant(),null);
        }
        try {
            Message message = EntitySupport.createInitMessage(Message.Type.order,
                    "您有新订单了，单号:" + trade.getOrder().getSn() + ",请尽快处理..",
                    trade.getOrder().getSn(), trade.getTenant().getMember(), null);
            message.setTrade(trade);
            message.setWay(Message.Way.tenant);
            messageService.save(message);
            PushMessage.send("您有新订单了，单号:" + trade.getOrder().getSn() + ",请尽快处理..", "rzico://shop.dll/TfrmOnOrderManage?sn=" + trade.getOrder().getSn(), center.getSn());
        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            //System.out.println("trade.getOrder().getShippingMethod().getMethod()="+trade.getOrder().getShippingMethod().getMethod()+"  ShippingMethod.Method.F2F="+ShippingMethod.Method.F2F);
            if (trade.getOrder().getShippingMethod().getMethod().equals(ShippingMethod.Method.F2F)) {
                ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
                SmsSend smsSend = new SmsSend();
                smsSend.setMobiles(trade.getTenant().getMember().getMobile());
                smsSend.setContent("【" + bundle.getString("signature") + "】尊敬的会员，有人在" + setting.getSiteName() + "上购买了您店铺的商品（订单编号" + trade.getOrder().getSn() + "客户到店提货码" + trade.getSn() + "），请尽快登陆平台处理！如有疑问，请致电官方客服 " + setting.getPhone() + " ");
                smsSend.setType(SmsSend.Type.service);
                smsSendService.smsSend(smsSend);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Transactional
    public void payment(Order order, Payment payment, Member operator) {
        Assert.notNull(order);
        Assert.notNull(payment);
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (payment.getMethod().equals(Payment.Method.deposit)) {
            Member member = order.getMember();
            memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
            BigDecimal rechargeBalance = member.getBalance().subtract(member.getClearBalance());
            BigDecimal totalBalance = new BigDecimal("0.00");
            // 余额付款
            totalBalance = member.getBalance();
            payment.setAmount(order.getAmountPayable());
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

            if (totalBalance.compareTo(order.getAmount()) >= 0) {
                memberDao.merge(member);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.payment);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(BigDecimal.ZERO);
                deposit.setDebit(payment.getAmount());
                deposit.setBalance(member.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(member);
                deposit.setMemo("订单支付 单号:" + order.getSn());
                deposit.setOrder(order);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                payment.setStatus(Payment.Status.success);

                Setting setting = SettingUtils.get();
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "您的账户，支付货款" + setting.setScale(payment.getAmount()).toString() + "元，订单号:" + order.getSn() + "",
                            null, member, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.member);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                payment.setAmount(BigDecimal.ZERO);
                payment.setClearAmount(BigDecimal.ZERO);
            }
        } else {
            Member member = order.getMember();
            Deposit deposit1 = new Deposit();
            deposit1.setType(Deposit.Type.recharge);
            deposit1.setStatus(Deposit.Status.complete);
            deposit1.setCredit(payment.getAmount());
            deposit1.setDebit(BigDecimal.ZERO);
            deposit1.setBalance(member.getBalance().add(payment.getAmount()));
            deposit1.setOperator(operator != null ? operator.getUsername() : null);
            deposit1.setMember(member);
            deposit1.setOrder(order);
            deposit1.setMemo("订单支付 单号:" + order.getSn());
            Deposit deposit2 = new Deposit();
            deposit2.setType(Deposit.Type.payment);
            deposit2.setStatus(Deposit.Status.complete);
            deposit2.setCredit(BigDecimal.ZERO);
            deposit2.setDebit(payment.getAmount());
            deposit2.setBalance(member.getBalance());
            deposit2.setOperator(operator != null ? operator.getUsername() : null);
            deposit2.setMember(member);
            deposit2.setMemo("订单支付 单号:" + order.getSn());
            deposit2.setOrder(order);
            depositDao.persist(deposit1);
            depositDao.persist(deposit2);
        }
        //对主订单意外更新失败、子订单更新成功对问题，捕获异常
        payment.setOrder(order);
        paymentDao.merge(payment);
        Setting setting = SettingUtils.get();
        if (setting.getStockAllocationTime() == StockAllocationTime.payment) {
            for (Trade trade : order.getTrades()) {
                if (!trade.getIsAllocatedStock()) {
                    trade.setIsAllocatedStock(true);
                    stockStrategy.lockAllocatedTrade(trade);
                }
            }
        }

        order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
        order.setFee(payment.getFee());
        order.setExpire(null);
        order.setLockExpire(new Date());

        if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {
            if (order.getOrderStatus().equals(OrderStatus.unconfirmed)) {
                order.setOrderStatus(OrderStatus.unconfirmed);
            }
            order.setPaymentStatus(PaymentStatus.paid);
        } else if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            if (order.getOrderStatus().equals(OrderStatus.unconfirmed)) {
                order.setOrderStatus(OrderStatus.unconfirmed);
            }
            order.setPaymentStatus(PaymentStatus.partialPayment);
        }

        for (Trade trade : order.getTrades()) {
            if (trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
                trade.setOrderStatus(OrderStatus.unconfirmed);
            }
            trade.setPaymentStatus(order.getPaymentStatus());
            tradeDao.merge(trade);
        }
        orderDao.merge(order);

        if (order.getPaymentStatus().equals(PaymentStatus.paid)) {
            for (Trade trade : order.getTrades()) {
                Tenant tenant = trade.getTenant();
                BigDecimal realHairAmount = trade.getClearingAmount();
                if (realHairAmount.compareTo(BigDecimal.ZERO) > 0) {
                    Member tenantMember = tenant.getMember();
                    memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);
                    tenantMember.setFreezeBalance(tenantMember.getFreezeBalance()
                            .add(realHairAmount));
                    memberDao.merge(tenantMember);
                }
                // 供应商冻结改发货动作
                //for (Tenant suppier : trade.getSuppliers()) {
                //	if (!trade.getTenant().equals(suppier)) {
                //		BigDecimal suppierAmount = trade.calcCost(suppier);
                //		if (suppierAmount.compareTo(BigDecimal.ZERO) > 0) {
                //			suppier.setFreezeBalance(suppier.getFreezeBalance().add(suppierAmount));
                //		   tenantDao.merge(suppier);
                //       }
                //	}
                //}

                OrderLog orderLog = new OrderLog();
                orderLog.setType(Type.payment);
                orderLog.setContent("订单已付款");
                orderLog.setOperator(operator != null ? operator.getUsername() : null);
                orderLog.setOrder(order);
                orderLog.setTrade(trade);
                orderLogDao.persist(orderLog);

            }
        }

    }

    @Transactional
    public void payment(Trade trade, Payment payment, Member operator) {
        Order order = trade.getOrder();
        Assert.notNull(order);
        Assert.notNull(payment);
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (payment.getMethod().equals(Payment.Method.deposit)) {
            Member member = order.getMember();
            memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
            BigDecimal rechargeBalance = member.getBalance().subtract(member.getClearBalance());
            BigDecimal totalBalance = new BigDecimal("0.00");
            // 余额付款
            totalBalance = member.getBalance();
            payment.setAmount(trade.getAmount());
            if (trade.getAmount().subtract(rechargeBalance).compareTo(member.getClearBalance()) >= 0) {
                payment.setClearAmount(member.getClearBalance());
                member.setClearBalance(BigDecimal.ZERO);
            } else {
                payment.setClearAmount(trade.getAmount().subtract(rechargeBalance));
                member.setClearBalance(member.getClearBalance().subtract(trade.getAmount().subtract(rechargeBalance)));
            }
            if (member.getFreezeCashBalance().compareTo(payment.getAmount()) < 0) {
                member.setFreezeCashBalance(BigDecimal.ZERO);
            } else {
                member.setFreezeCashBalance(member.getFreezeCashBalance().subtract(payment.getAmount()));
            }
            member.setBalance(member.getBalance().subtract(payment.getAmount()));

            if (totalBalance.compareTo(trade.getAmount()) >= 0) {
                memberDao.merge(member);
                Deposit deposit = new Deposit();
                deposit.setType(Deposit.Type.payment);
                deposit.setStatus(Deposit.Status.complete);
                deposit.setCredit(BigDecimal.ZERO);
                deposit.setDebit(payment.getAmount());
                deposit.setBalance(member.getBalance());
                deposit.setOperator(operator != null ? operator.getUsername() : null);
                deposit.setMember(member);
                deposit.setMemo("订单支付 单号:" + order.getSn());
                deposit.setOrder(order);
                deposit.setStatus(Deposit.Status.none);
                depositDao.persist(deposit);
                payment.setStatus(Payment.Status.success);

                Setting setting = SettingUtils.get();
                try {
                    Message message = EntitySupport.createInitMessage(Message.Type.account,
                            "您的账户，支付货款" + setting.setScale(payment.getAmount()).toString() + "元，订单号:" + order.getSn() + "",
                            null, member, null);
                    message.setDeposit(deposit);
                    message.setWay(Message.Way.member);
                    messageService.save(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                payment.setAmount(BigDecimal.ZERO);
                payment.setClearAmount(BigDecimal.ZERO);
            }
        } else {
            Member member = order.getMember();
            Deposit deposit1 = new Deposit();
            deposit1.setType(Deposit.Type.recharge);
            deposit1.setStatus(Deposit.Status.complete);
            deposit1.setCredit(payment.getAmount());
            deposit1.setDebit(BigDecimal.ZERO);
            deposit1.setBalance(member.getBalance().add(payment.getAmount()));
            deposit1.setOperator(operator != null ? operator.getUsername() : null);
            deposit1.setMember(member);
            deposit1.setOrder(order);
            deposit1.setMemo("账户充值 单号:" + order.getSn());
            Deposit deposit2 = new Deposit();
            deposit2.setType(Deposit.Type.payment);
            deposit2.setStatus(Deposit.Status.complete);
            deposit2.setCredit(BigDecimal.ZERO);
            deposit2.setDebit(payment.getAmount());
            deposit2.setBalance(member.getBalance());
            deposit2.setOperator(operator != null ? operator.getUsername() : null);
            deposit2.setMember(member);
            deposit2.setMemo("订单支付 单号:" + order.getSn());
            deposit2.setOrder(order);
            depositDao.persist(deposit1);
            depositDao.persist(deposit2);
        }
        //对主订单意外更新失败、子订单更新成功对问题，捕获异常
        payment.setOrder(order);
        payment.setTrade(trade);
        paymentDao.merge(payment);
        Setting setting = SettingUtils.get();
        if (setting.getStockAllocationTime() == StockAllocationTime.payment) {
            if (!trade.getIsAllocatedStock()) {
                trade.setIsAllocatedStock(true);
                stockStrategy.lockAllocatedTrade(trade);
            }
        }

        order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
        order.setFee(payment.getFee());
        order.setExpire(null);
        order.setLockExpire(new Date());
        if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {
            if (order.getOrderStatus().equals(OrderStatus.unconfirmed)) {
                order.setOrderStatus(OrderStatus.unconfirmed);
            }
            order.setPaymentStatus(PaymentStatus.paid);
        } else if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            if (order.getOrderStatus().equals(OrderStatus.unconfirmed)) {
                order.setOrderStatus(OrderStatus.unconfirmed);
            }
            order.setPaymentStatus(PaymentStatus.partialPayment);
        }

        if (trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
            trade.setOrderStatus(order.getOrderStatus());
        }
        trade.setPaymentStatus(PaymentStatus.paid);
        for (Trade clearTrade : order.getTrades()) {
            if (clearTrade.equals(trade)) {
                clearTrade.setOrderStatus(trade.getOrderStatus());
                clearTrade.setPaymentStatus(trade.getPaymentStatus());
            }
        }
        tradeDao.merge(trade);
        orderDao.merge(order);

        if (trade.getPaymentStatus().equals(PaymentStatus.paid)) {
            Tenant tenant = trade.getTenant();
            BigDecimal realHairAmount = trade.getClearingAmount();
            if (realHairAmount.compareTo(BigDecimal.ZERO) > 0) {
                Member tenantMember = tenant.getMember();
                memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);
                tenantMember.setFreezeBalance(tenantMember.getFreezeBalance()
                        .add(realHairAmount));
                memberDao.merge(tenantMember);
            }

            // 供应商冻结改发货动作
            //for (Tenant suppier : trade.getSuppliers()) {
            //	if (!trade.getTenant().equals(suppier)) {
            //		BigDecimal suppierAmount = trade.calcCost(suppier);
            //		if (suppierAmount.compareTo(BigDecimal.ZERO) > 0) {
            //			suppier.setFreezeBalance(suppier.getFreezeBalance().add(suppierAmount));
            //		   tenantDao.merge(suppier);
            //       }
            //	}
            //}

            OrderLog orderLog = new OrderLog();
            orderLog.setType(Type.payment);
            orderLog.setContent("订单已付款");
            orderLog.setOperator(operator != null ? operator.getUsername() : null);
            orderLog.setOrder(order);
            orderLog.setTrade(trade);
            orderLogDao.persist(orderLog);

        }

    }

    /**
     * 线下缴款
     *
     * @param trade    订单
     * @param operator 操作员
     */
    public void payment(Trade trade, Member operator) {
        Order order = trade.getOrder();
        Assert.notNull(order);
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        Payment payment = new Payment();
        payment.setMember(order.getMember());
        payment.setPayer(order.getMember().getName());
        payment.setMemo("线下缴款");
        payment.setSn(snService.generate(Sn.Type.payment));
        payment.setType(Payment.Type.payment);
        payment.setMethod(Method.offline);
        payment.setStatus(Status.success);
        payment.setPaymentMethod("线下缴款");
        payment.setFee(new BigDecimal(0));
        payment.setAmount(trade.getAmount());
        payment.setPaymentPluginId("offline");
        payment.setExpire(null);
        payment.setOrder(order);
        payment.setTrade(trade);
        paymentDao.merge(payment);
        Setting setting = SettingUtils.get();
        if (setting.getStockAllocationTime() == StockAllocationTime.payment) {
            if (!trade.getIsAllocatedStock()) {
                trade.setIsAllocatedStock(true);
                stockStrategy.lockAllocatedTrade(trade);
            }
        }

        order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
        order.setFee(payment.getFee());
        order.setExpire(null);
        order.setLockExpire(new Date());
        if (order.getAmountPaid().compareTo(order.getAmount()) >= 0) {
            if (order.getOrderStatus().equals(OrderStatus.unconfirmed)) {
                order.setOrderStatus(OrderStatus.confirmed);
            }
            order.setPaymentStatus(PaymentStatus.paid);
        } else if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            if (order.getOrderStatus().equals(OrderStatus.unconfirmed)) {
                order.setOrderStatus(OrderStatus.confirmed);
            }
            order.setPaymentStatus(PaymentStatus.partialPayment);
        }

        if (trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
            trade.setOrderStatus(order.getOrderStatus());
        }
        trade.setPaymentStatus(PaymentStatus.paid);
        for (Trade clearTrade : order.getTrades()) {
            if (clearTrade.equals(trade)) {
                clearTrade.setOrderStatus(trade.getOrderStatus());
                clearTrade.setPaymentStatus(trade.getPaymentStatus());
            }
        }
        trade.setClearing(true);
        trade.setClearingDate(new Date());
        tradeDao.merge(trade);
        orderDao.merge(order);

        if (trade.getPaymentStatus().equals(PaymentStatus.paid)) {

            //for (Tenant suppier : trade.getSuppliers()) {
            //	if (!trade.getTenant().equals(suppier)) {
            //		BigDecimal suppierAmount = trade.calcCost(suppier);
            //		if (suppierAmount.compareTo(BigDecimal.ZERO) > 0) {
            //			suppier.setFreezeBalance(suppier.getFreezeBalance().add(suppierAmount));
            //		   tenantDao.merge(suppier);
            //        }
            //	}
            //}

            OrderLog orderLog = new OrderLog();
            orderLog.setType(Type.payment);
            orderLog.setContent("订单已线下缴款");
            orderLog.setOperator(operator != null ? operator.getUsername() : null);
            orderLog.setOrder(order);
            orderLog.setTrade(trade);
            orderLogDao.persist(orderLog);

        }

        //if (trade.getOrderStatus().equals(OrderStatus.completed)) {
        //	clearingToSupplier(trade);
        //}

    }


    @Transactional
    public void shipping(Order order, Shipping shipping, Member operator) {
        Assert.notNull(order);
        Assert.notNull(shipping);
        Assert.notEmpty(shipping.getShippingItems());
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        Setting setting = SettingUtils.get();
        Trade trade = shipping.getTrade();
        if (!trade.getIsAllocatedStock() && setting.getStockAllocationTime() == StockAllocationTime.ship) {
            stockStrategy.lockAllocatedTrade(trade);
            trade.setIsAllocatedStock(true);
        }
        shipping.setOrder(order);
        shippingDao.persist(shipping);
        for (ShippingItem shippingItem : shipping.getShippingItems()) {
            OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
            if (orderItem == null) {
                continue;
            }
            stockStrategy.adjustForShipping(order, orderItem, shippingItem, shipping.getDeliveryCenter());
            orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
            orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
        }
        if (order.getShippedQuantity() >= order.getQuantity()) {
            order.setShippingStatus(ShippingStatus.shipped);
        } else if (order.getShippedQuantity() > 0) {
            order.setShippingStatus(ShippingStatus.partialShipment);
        }
        if (trade.getShippedQuantity() >= trade.getQuantity()) {
            trade.setShippingStatus(ShippingStatus.shipped);
            trade.setShippingDate(new Date());
            trade.setIsAllocatedStock(false);
            trade.setConfirmDueDate(DateUtil.transpositionDate(trade.getShippingDate(), Calendar.DATE, SettingConstant.autoTradeAcceptDateLimit));
        } else if (trade.getShippedQuantity() > 0) {
            trade.setShippingStatus(ShippingStatus.partialShipment);
        }

        //if (setting.getClearingTime().equals(ClearingTime.shipping)
        //		&& trade.getShippingStatus().equals(ShippingStatus.shipped)
        //		&& !trade.getClearing()) {
        //	clearing(order, trade, operator, false);
        //}
        if (trade.getShippingStatus().equals(ShippingStatus.shipped)) {
            // 供应商冻结改发货完成后动作
            for (Tenant suppier : trade.getSuppliers()) {
                if (!trade.getTenant().equals(suppier)) {
                    //冻结金额=结算价*发货数量
                    BigDecimal suppierAmount = trade.calcCost(suppier);
                    if (suppierAmount.compareTo(BigDecimal.ZERO) > 0) {
                        suppier.setFreezeBalance(suppier.getFreezeBalance().add(suppierAmount));
                        tenantDao.merge(suppier);
                    }
                }
            }
        }
        // 子订单状态
        tradeDao.persist(trade);
        order.setExpire(null);
        orderDao.merge(order);
        OrderLog orderLog = new OrderLog();
        orderLog.setType(Type.shipping);
        orderLog.setContent("卖家已发货");
        orderLog.setOperator(operator != null ? operator.getUsername() : null);
        orderLog.setOrder(order);
        orderLog.setTrade(shipping.getTrade());
        orderLogDao.persist(orderLog);
        Message message = EntitySupport.createInitMessage(Message.Type.order,
                "【" + setting.getSiteName() + "】尊敬的" + order.getConsignee() + "，您在" + setting.getSiteName() + "购买的商品已发货，"
                        + "即将送到，请注意查收。",
                order.getSn(), trade.getOrder().getMember(), null);
        message.setTrade(trade);
        message.setWay(Message.Way.member);
        messageService.save(message);

    }

    @Transactional
    public void refunds(Trade trade, Refunds refunds, Member operator) throws BalanceNotEnoughException {
        Assert.notNull(refunds);
        Order order = trade.getOrder();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

        if (trade.getPaymentStatus().equals(PaymentStatus.unpaid)) {
            return;
        }
        if (trade.getPaymentStatus().equals(PaymentStatus.refunded)) {
            return;
        }

        refunds.setOrder(order);
        refunds.setStatus(net.wit.entity.Refunds.Status.comfirm);
        refundsDao.persist(refunds);

        Member member = order.getMember();
        memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
        // 买家增加 结算余额  和  余额
        member.setClearBalance(member.getClearBalance().add(refunds.getAmount()));
        member.setBalance(member.getBalance().add(refunds.getAmount()));
        memberDao.merge(member);

        Deposit deposit = new Deposit();
        deposit.setType(Deposit.Type.payment);
        deposit.setStatus(Deposit.Status.complete);
        deposit.setCredit(BigDecimal.ZERO);
        // 设置流水单
        deposit.setDebit(BigDecimal.ZERO.subtract(refunds.getAmount()));
        deposit.setBalance(member.getBalance());
        deposit.setOperator(operator != null ? operator.getUsername() : null);
        deposit.setMember(member);
        deposit.setOrder(order);
        deposit.setMemo("订单退款 单号:" + order.getSn());
        depositDao.persist(deposit);

        Setting setting = SettingUtils.get();

        Message message = EntitySupport.createInitMessage(Message.Type.account,
                "您的账户，退款收入" + setting.setScale(refunds.getAmount()).toString() + "元，订单号:" + order.getSn() + "",
                order.getSn(), member, null);
        message.setTrade(trade);
        message.setDeposit(deposit);
        message.setWay(Message.Way.member);
        messageService.save(message);
        if (!trade.getClearing()) {
            Member tenantMember = trade.getTenant().getMember();
            memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);
            BigDecimal refundsAmount = trade.getClearingAmount();
            if (tenantMember.getFreezeBalance().subtract(refundsAmount).compareTo(BigDecimal.ZERO) < 0) {
                tenantMember.setFreezeBalance(BigDecimal.ZERO);
            } else {
                tenantMember.setFreezeBalance(tenantMember.getFreezeBalance().subtract(refundsAmount));
            }
            memberDao.persist(tenantMember);
        } else if (trade.getClearing()) {
            // 扣取商家货款
            Member tenantMember = trade.getTenant().getMember();
            memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);

            BigDecimal refundsAmount = trade.getClearingAmount();
            BigDecimal refundsProfitAmount = BigDecimal.ZERO;
            if (trade.getOrderStatus().equals(OrderStatus.completed)) {
                refundsProfitAmount = refunds.getTrade().getTotalProfit().add(trade.calcAgencyAmount());
            }


            if (tenantMember.getBalance().subtract(refundsAmount.subtract(refundsProfitAmount)).compareTo(BigDecimal.ZERO) < 0) {
                throw new BalanceNotEnoughException("balance.not.enough");
            }

            Deposit tenantDeposit = new Deposit();
            if (refundsProfitAmount.compareTo(BigDecimal.ZERO) > 0) {
                tenantDeposit.setType(Deposit.Type.rebate);
                tenantDeposit.setStatus(Deposit.Status.complete);
                tenantDeposit.setCredit(BigDecimal.ZERO);
                tenantDeposit.setDebit(BigDecimal.ZERO.subtract(refundsProfitAmount));
                tenantDeposit.setBalance(tenantMember.getBalance().add(refundsProfitAmount));
                tenantDeposit.setOperator(operator != null ? operator.getUsername() : null);
                tenantDeposit.setMember(tenantMember);
                tenantDeposit.setMemo("退还佣金  单号:" + order.getSn());
                tenantDeposit.setOrder(order);
                depositDao.persist(tenantDeposit);
            }
            Deposit tenantDeposit1 = new Deposit();
            tenantDeposit1.setType(Deposit.Type.receipts);
            tenantDeposit1.setStatus(Deposit.Status.complete);
            tenantDeposit1.setCredit(BigDecimal.ZERO.subtract(refundsAmount));
            tenantDeposit1.setDebit(BigDecimal.ZERO);
            tenantDeposit1.setBalance(tenantMember.getBalance().subtract(refundsAmount).add(refundsProfitAmount));
            tenantDeposit1.setOperator(operator != null ? operator.getUsername() : null);
            tenantDeposit1.setMember(tenantMember);
            tenantDeposit1.setMemo("退还买家  单号:" + order.getSn());
            tenantDeposit1.setOrder(order);
            depositDao.persist(tenantDeposit1);

            tenantMember.setBalance(tenantMember.getBalance().subtract(refundsAmount.subtract(refundsProfitAmount)));
            if (tenantMember.getClearBalance().subtract(refundsAmount.subtract(refundsProfitAmount)).compareTo(BigDecimal.ZERO) < 0) {
                tenantMember.setClearBalance(BigDecimal.ZERO);
            } else {
                tenantMember.setClearBalance(tenantMember.getClearBalance().subtract(refundsAmount.subtract(refundsProfitAmount)));
            }
            memberDao.merge(tenantMember);
            if (refundsProfitAmount.compareTo(BigDecimal.ZERO) > 0) {
                Message msg1 = EntitySupport.createInitMessage(Message.Type.account,
                        "您的账户，退还佣金" + setting.setScale(refundsProfitAmount).toString() + "元，订单号:" + order.getSn() + "",
                        order.getSn(), tenantMember, null);
                msg1.setTrade(trade);
                msg1.setDeposit(tenantDeposit);
                messageService.save(msg1);
            }
            Message msg = EntitySupport.createInitMessage(Message.Type.account,
                    "您的账户，支付退款" + setting.setScale(refundsAmount).toString() + "元，订单号:" + order.getSn() + "",
                    order.getSn(), tenantMember, null);
            msg.setTrade(trade);
            msg.setDeposit(tenantDeposit1);
            messageService.save(msg);
        }

        // 将已付金额减去退款金额
        order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
        order.setExpire(null);
        if (order.getAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
            order.setPaymentStatus(PaymentStatus.refunded);
        } else if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            order.setPaymentStatus(PaymentStatus.partialRefunds);
        }

        trade.setPaymentStatus(PaymentStatus.refunded);
        tradeDao.persist(trade);
        order.setExpire(null);
        orderDao.merge(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(Type.refunds);
        orderLog.setContent("卖家已退款");
        orderLog.setOperator(operator != null ? operator.getUsername() : null);
        orderLog.setOrder(order);
        orderLog.setTrade(refunds.getTrade());
        orderLogDao.persist(orderLog);
    }

    @Transactional
    public void spRefunds(Trade trade, Refunds refunds, Member operator) throws BalanceNotEnoughException {
        Assert.notNull(refunds);
        Order order = trade.getOrder();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

        //if (trade.getPaymentStatus().equals(PaymentStatus.refunded)) {
        //	return;
        //}

        refunds.setOrder(order);
        refunds.setStatus(net.wit.entity.Refunds.Status.comfirm);
        refundsDao.persist(refunds);

        Member member = order.getMember();
        memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
        // 买家增加 结算余额  和  余额
        member.setClearBalance(member.getClearBalance().add(refunds.getAmount()));
        member.setBalance(member.getBalance().add(refunds.getAmount()));
        memberDao.merge(member);

        Deposit deposit = new Deposit();
        deposit.setType(Deposit.Type.payment);
        deposit.setStatus(Deposit.Status.complete);
        deposit.setCredit(BigDecimal.ZERO);
        // 设置流水单
        deposit.setDebit(BigDecimal.ZERO.subtract(refunds.getAmount()));
        deposit.setBalance(member.getBalance());
        deposit.setOperator(operator != null ? operator.getUsername() : null);
        deposit.setMember(member);
        deposit.setOrder(order);
        deposit.setMemo("订单退款 单号:" + order.getSn());
        depositDao.persist(deposit);

        Setting setting = SettingUtils.get();

        Message message = EntitySupport.createInitMessage(Message.Type.account,
                "您的账户，退款收入" + setting.setScale(refunds.getAmount()).toString() + "元，订单号:" + order.getSn() + "",
                order.getSn(), member, null);
        message.setTrade(trade);
        message.setDeposit(deposit);
        message.setWay(Message.Way.member);
        messageService.save(message);
        // 扣取商家货款
        Member tenantMember = trade.getTenant().getMember();
        memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);

        BigDecimal refundsAmount = refunds.getAmount();

        if (tenantMember.getBalance().subtract(refundsAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceNotEnoughException("balance.not.enough");
        }

        Deposit tenantDeposit1 = new Deposit();
        tenantDeposit1.setType(Deposit.Type.receipts);
        tenantDeposit1.setStatus(Deposit.Status.complete);
        tenantDeposit1.setCredit(BigDecimal.ZERO.subtract(refundsAmount));
        tenantDeposit1.setDebit(BigDecimal.ZERO);
        tenantDeposit1.setBalance(tenantMember.getBalance().subtract(refundsAmount));
        tenantDeposit1.setOperator(operator != null ? operator.getUsername() : null);
        tenantDeposit1.setMember(tenantMember);
        tenantDeposit1.setMemo("退还买家  单号:" + order.getSn());
        tenantDeposit1.setOrder(order);
        depositDao.persist(tenantDeposit1);

        tenantMember.setBalance(tenantMember.getBalance().subtract(refundsAmount));
        if (tenantMember.getClearBalance().subtract(refundsAmount).compareTo(BigDecimal.ZERO) < 0) {
            tenantMember.setClearBalance(BigDecimal.ZERO);
        } else {
            tenantMember.setClearBalance(tenantMember.getClearBalance().subtract(refundsAmount));
        }
        memberDao.merge(tenantMember);
        Message msg = EntitySupport.createInitMessage(Message.Type.account,
                "您的账户，支付退款" + setting.setScale(refundsAmount).toString() + "元，订单号:" + order.getSn() + "",
                order.getSn(), tenantMember, null);
        msg.setTrade(trade);
        msg.setDeposit(tenantDeposit1);
        messageService.save(msg);
        // 将已付金额减去退款金额
        order.setAmountPaid(order.getAmountPaid().subtract(refunds.getAmount()));
        order.setExpire(null);
        if (order.getAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
            order.setPaymentStatus(PaymentStatus.refunded);
        } else if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            order.setPaymentStatus(PaymentStatus.partialRefunds);
        }

        trade.setPaymentStatus(PaymentStatus.refunded);
        tradeDao.persist(trade);
        order.setExpire(null);
        orderDao.merge(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(Type.refunds);
        orderLog.setContent("卖家已退款");
        orderLog.setOperator(operator != null ? operator.getUsername() : null);
        orderLog.setOrder(order);
        orderLog.setTrade(refunds.getTrade());
        orderLogDao.persist(orderLog);
    }

    @Transactional
    public void returns(Trade trade, Returns returns, Member operator) {
        Assert.notNull(returns);
        Assert.notEmpty(returns.getReturnsItems());
        Order order = trade.getOrder();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (trade.getShippingStatus().equals(ShippingStatus.unshipped)) {
            return;
        }
        if (trade.getShippingStatus().equals(ShippingStatus.returned)) {
            return;
        }
        returns.setOrder(order);
        returnsDao.persist(returns);

        for (ReturnsItem returnsItem : returns.getReturnsItems()) {
            OrderItem orderItem = order.getOrderItem(returnsItem.getSn());

            if (orderItem == null) {
                continue;
            }
            if (orderItem != null) {
                orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
                orderItem.setReturnQuantity(orderItem.getReturnQuantity() + returnsItem.getQuantity());
            }
            Product product = productDao.findBySn(orderItem.getSn());
            if (product != null) {
                productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
                product.setStock(product.getStock() + returnsItem.getQuantity());
                productDao.merge(product);
            }
        }

        if (order.getReturnQuantity() >= order.getShippedQuantity()) {
            order.setShippingStatus(ShippingStatus.returned);
        } else if (order.getReturnQuantity() > 0) {
            order.setShippingStatus(ShippingStatus.partialReturns);
        }
        if (trade.getReturnQuantity() >= trade.getShippedQuantity()) {
            trade.setShippingStatus(ShippingStatus.returned);
        } else if (trade.getReturnQuantity() > 0) {
            trade.setShippingStatus(ShippingStatus.partialReturns);
        }

        if (trade.getShippingStatus().equals(ShippingStatus.returned)) {
            // 供应商冻结改发货完成后动作
            for (Tenant suppier : trade.getSuppliers()) {
                if (!trade.getTenant().equals(suppier)) {
                    //冻结金额=结算价*发货数量
                    BigDecimal suppierAmount = trade.calcCost(suppier);
                    if (suppierAmount.compareTo(BigDecimal.ZERO) > 0) {
                        suppier.setFreezeBalance(suppier.getFreezeBalance().subtract(suppierAmount));
                        tenantDao.merge(suppier);
                    }
                }
            }
        }
        tradeDao.persist(trade);
        order.setExpire(null);
        orderDao.merge(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(Type.returns);
        orderLog.setContent("卖家已退货");
        orderLog.setOperator(operator != null ? operator.getUsername() : null);
        orderLog.setOrder(order);
        orderLog.setTrade(returns.getTrade());
        orderLogDao.persist(orderLog);
    }

    /**
     * 买家申请退货
     *
     * @param trade     订单
     * @param spReturns 子订单
     * @param operator  操作员
     */
    @Transactional
    public void spReturns(Trade trade, SpReturns spReturns, Member operator) {
        Order order = trade.getOrder();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (spReturns == null) {
            spReturns = new SpReturns();
            spReturns.setType(SpReturns.Type.normal);
            spReturns.setAddress(order.getAddress());
            spReturns.setArea(order.getAreaName());
            spReturns.setFreight(BigDecimal.ZERO);
            spReturns.setPhone(order.getPhone());
            spReturns.setReturnStatus(ReturnStatus.unconfirmed);
            spReturns.setTrade(trade);
            spReturns.setZipCode(order.getZipCode());
            spReturns.setSn(snService.generate(Sn.Type.returns));
            spReturns.setOperator(operator.getUsername());
            spReturns.setShipper(order.getConsignee());
            spReturns.setShippingMethod("退货申请");
            spReturns.setSupplier(trade.getTenant());
            for (OrderItem orderItem : trade.getOrderItems()) {
                SpReturnsItem returnsItem = new SpReturnsItem();
                returnsItem.setShippedQuantity(orderItem.getShippedQuantity());
                returnsItem.setReturnQuantity(orderItem.getShippedQuantity() - orderItem.getReturnQuantity());
                returnsItem.setName(orderItem.getName());
                returnsItem.setSn(orderItem.getSn());
                returnsItem.setReturns(spReturns);
                returnsItem.setOrderItem(orderItem);
                spReturns.getReturnsItems().add(returnsItem);
            }
            if (trade.getPaymentStatus().equals(PaymentStatus.paid)) {
                trade.setPaymentStatus(PaymentStatus.refundApply);
                spReturns.setAmount(trade.getAmount());
            } else {
                spReturns.setAmount(BigDecimal.ZERO);
            }
            spReturns.setCost(trade.getCost());
            if (spReturns.getReturnQuantity() > 0) {
                trade.setShippingStatus(ShippingStatus.shippedApply);
            }
            spReturnsDao.persist(spReturns);
            tradeDao.persist(trade);
            order.setExpire(null);
            orderDao.merge(order);

            OrderLog orderLog = new OrderLog();
            orderLog.setType(Type.returns);
            if (operator.getTenant() != null && operator.getTenant().equals(trade.getTenant())) {
                orderLog.setContent("卖家申请退货");
            } else {
                orderLog.setContent("买家申请退货");
            }
            orderLog.setOperator(operator != null ? operator.getUsername() : null);
            orderLog.setOrder(order);
            orderLog.setTrade(trade);
            orderLogDao.persist(orderLog);
        } else {
            for (Tenant supplier : spReturns.getSuppliers()) {
                SpReturns sspReturns = new SpReturns();
                sspReturns.setType(SpReturns.Type.event);
                sspReturns.setAddress(spReturns.getAddress());
                sspReturns.setArea(spReturns.getArea());
                sspReturns.setFreight(BigDecimal.ZERO);
                sspReturns.setPhone(spReturns.getPhone());
                sspReturns.setReturnStatus(ReturnStatus.unconfirmed);
                sspReturns.setTrade(spReturns.getTrade());
                sspReturns.setZipCode(spReturns.getZipCode());
                sspReturns.setSn(snService.generate(Sn.Type.returns));
                sspReturns.setOperator(spReturns.getOperator());
                sspReturns.setShipper(spReturns.getShipper());
                sspReturns.setShippingMethod("退货申请");
                sspReturns.setSupplier(supplier);
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal cost = BigDecimal.ZERO;
                for (SpReturnsItem spReturnsItem : spReturns.getReturnsItems(supplier)) {
                    SpReturnsItem returnsItem = new SpReturnsItem();
                    returnsItem.setShippedQuantity(spReturnsItem.getShippedQuantity());
                    returnsItem.setReturnQuantity(spReturnsItem.getReturnQuantity());
                    returnsItem.setName(spReturnsItem.getName());
                    returnsItem.setSn(spReturnsItem.getSn());
                    returnsItem.setReturns(sspReturns);
                    returnsItem.setOrderItem(spReturnsItem.getOrderItem());
                    sspReturns.getReturnsItems().add(returnsItem);
//					amount=amount.add(new BigDecimal(spReturnsItem.getReturnQuantity())
//							.multiply(new BigDecimal(spReturnsItem.getOrderItem().getPrice().toString())));
//					cost=cost.add(new BigDecimal(spReturnsItem.getReturnQuantity())
//							.multiply(new BigDecimal(spReturnsItem.getOrderItem().getCost().toString())));
                }
                if (trade.getPaymentStatus().equals(PaymentStatus.paid)) {
                    sspReturns.setAmount(sspReturns.calcAmount(supplier));
                } else {
                    sspReturns.setAmount(BigDecimal.ZERO);
                }
                if (spReturns.getReturnQuantity() > 0) {
                    trade.setShippingStatus(ShippingStatus.shippedApply);
                }
                if (spReturns.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    trade.setPaymentStatus(PaymentStatus.refundApply);
                }
                sspReturns.setCost(sspReturns.calcCost(supplier));
                spReturnsDao.persist(sspReturns);
            }
            OrderLog orderLog = new OrderLog();
            orderLog.setType(Type.returns);
            if (operator.getTenant() != null && operator.getTenant().equals(trade.getTenant())) {
                orderLog.setContent("卖家申请退货");
            } else {
                orderLog.setContent("买家申请退货");
            }
            orderLog.setOperator(operator != null ? operator.getUsername() : null);
            orderLog.setOrder(order);
            orderLog.setTrade(trade);
            orderLogDao.persist(orderLog);
        }
    }

    /**
     * 买家同意退货
     *
     * @param trade     订单
     * @param spReturns 子订单
     * @param operator  操作员
     * @throws BalanceNotEnoughException
     */
    @Transactional
    public void spConfirm(Trade trade, SpReturns spReturns, Member operator) throws BalanceNotEnoughException {
        Order order = trade.getOrder();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (spReturns.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            Refunds refund = new Refunds();
            refund.setMethod(Refunds.Method.deposit);
            refund.setOperator(operator.getUsername());
            refund.setSn(snService.generate(Sn.Type.refunds));
            refund.setOrder(trade.getOrder());
            refund.setMethod(Refunds.Method.deposit);
            refund.setAmount(spReturns.getAmount());
            refund.setStatus(Refunds.Status.comfirm);
            refund.setTrade(trade);
            if (spReturns.getType().equals(SpReturns.Type.normal)) {
                refunds(trade, refund, operator);
            } else {
                spRefunds(trade, refund, operator);
            }
        }
        if (spReturns.getReturnQuantity() > 0) {
            Returns returns = new Returns();
            returns.setAddress(spReturns.getAddress());
            returns.setArea(spReturns.getArea());
            returns.setDeliveryCorp(spReturns.getDeliveryCorp());
            returns.setFreight(spReturns.getFreight());
            returns.setMemo(spReturns.getMemo());
            returns.setOperator(spReturns.getOperator());
            returns.setOrder(trade.getOrder());
            returns.setTrade(trade);
            returns.setTrackingNo(spReturns.getTrackingNo());
            returns.setPhone(spReturns.getPhone());
            returns.setSn(spReturns.getSn());
            returns.setZipCode(spReturns.getZipCode());
            returns.setShipper(spReturns.getShipper());
            for (SpReturnsItem returnsItem : spReturns.getReturnsItems()) {
                ReturnsItem item = new ReturnsItem();
                item.setName(returnsItem.getName());
                item.setQuantity(returnsItem.getReturnQuantity());
                item.setReturns(returns);
                item.setSn(returnsItem.getSn());
                returns.getReturnsItems().add(item);
            }
            returns(trade, returns, operator);
        }
        if (spReturns.getType().equals(SpReturns.Type.normal)) {
            if (trade.isComplete()) {
                trade.setCompletedDate(new Date());
                trade.setOrderStatus(OrderStatus.completed);
                tradeDao.persist(trade);
            }
            //complete(trade, operator);
            spReturns.setCompletedDate(new Date());
            spReturns.setReturnStatus(ReturnStatus.completed);
            spReturnsDao.persist(spReturns);
        } else {
            spReturns.setCompletedDate(new Date());
            spReturns.setReturnStatus(ReturnStatus.completed);
            spReturnsDao.persist(spReturns);
        }

        if (trade.getShippingStatus().equals(ShippingStatus.unshipped)) {
            if (trade.getIsAllocatedStock()) {
                stockStrategy.releaseAllocatedTrade(trade);
                trade.setIsAllocatedStock(false);
            }
        }
        trade.setSuppliered(true);
        trade.setSupplierDate(new Date());
        tradeDao.merge(trade);

    }

    /**
     * 买家拒绝退货
     *
     * @param trade    子订单
     * @param operator 操作员
     * @throws BalanceNotEnoughException
     */
    @Transactional
    public void spRejected(Trade trade, SpReturns spReturns, Member operator) {
        Order order = trade.getOrder();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (spReturns.getType().equals(SpReturns.Type.normal)) {
            spReturns.setReturnStatus(ReturnStatus.cancelled);
            if (trade.getReturnQuantity() >= trade.getShippedQuantity() && trade.getShippedQuantity() > 0) {
                trade.setShippingStatus(ShippingStatus.returned);
            } else if (trade.getReturnQuantity() > 0 && trade.getShippedQuantity() > 0) {
                trade.setShippingStatus(ShippingStatus.partialReturns);
            } else if (trade.getShippedQuantity() >= trade.getQuantity()) {
                trade.setShippingStatus(ShippingStatus.shipped);
            } else if (trade.getShippedQuantity() > 0) {
                trade.setShippingStatus(ShippingStatus.partialShipment);
            } else {
                trade.setShippingStatus(ShippingStatus.unshipped);
            }
            if (trade.getOrder().getAmountPaid().compareTo(order.getAmount()) >= 0) {
                trade.setPaymentStatus(PaymentStatus.paid);
            } else {
                order.setPaymentStatus(PaymentStatus.unpaid);
            }

            spReturnsDao.persist(spReturns);
            tradeDao.persist(trade);
            order.setExpire(null);
            orderDao.merge(order);

            OrderLog orderLog = new OrderLog();
            orderLog.setType(Type.returns);
            orderLog.setContent("卖家拒绝退货");
            orderLog.setOperator(operator != null ? operator.getUsername() : null);
            orderLog.setOrder(order);
            orderLog.setTrade(trade);
            orderLogDao.persist(orderLog);
        } else {
            spReturns.setReturnStatus(ReturnStatus.cancelled);
            spReturnsDao.persist(spReturns);
        }
    }

    @Transactional
    public void clearing(Order order, Trade trade, Member operator, Boolean saved) {
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        if (order.getPaymentMethod().getMethod().equals(PaymentMethod.Method.offline)) {
            return;
        }
        if (!trade.getPaymentStatus().equals(PaymentStatus.paid)) {
            return;
        }
        if (trade.getClearing()) {
            return;
        }
        // 给商家打款
        Tenant tenant = trade.getTenant();
        BigDecimal realHairAmount = trade.getClearingAmount();
        BigDecimal realProfitAmount = trade.getTotalProfit().add(trade.getAgencyAmount());
        if (realHairAmount.compareTo(BigDecimal.ZERO) > 0) {
            Member tenantMember = tenant.getMember();
            memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);

            if (tenantMember.getFreezeBalance().subtract(realHairAmount).compareTo(BigDecimal.ZERO) < 0) {
                tenantMember.setFreezeBalance(BigDecimal.ZERO);
            } else {
                tenantMember.setFreezeBalance(tenantMember.getFreezeBalance().subtract(realHairAmount));
            }

            tenantMember.setBalance(tenantMember.getBalance().add(realHairAmount));
            tenantMember.setClearBalance(tenantMember.getClearBalance().add(realHairAmount));
            memberDao.merge(tenantMember);

            Deposit deposit = new Deposit();
            deposit.setType(Deposit.Type.receipts);
            deposit.setStatus(Deposit.Status.complete);
            deposit.setCredit(realHairAmount);
            deposit.setDebit(BigDecimal.ZERO);
            deposit.setBalance(tenantMember.getBalance());
            deposit.setOperator(operator != null ? operator.getUsername() : null);
            deposit.setMember(tenantMember);
            deposit.setMemo("结算货款 单号:" + order.getSn());
            deposit.setOrder(order);
            depositDao.persist(deposit);
            Setting setting = SettingUtils.get();

            Message msg = EntitySupport.createInitMessage(Message.Type.account,
                    "您的账户，收入货款" + setting.setScale(realHairAmount).toString() + "元，订单号：" + order.getSn() + ".",
                    order.getSn(), tenantMember, null);
            msg.setTrade(trade);
            msg.setDeposit(deposit);
            messageService.save(msg);
        }
        if (realProfitAmount.compareTo(BigDecimal.ZERO) > 0) {
            Member tenantMember = tenant.getMember();
            memberDao.lock(tenantMember, LockModeType.PESSIMISTIC_WRITE);
            tenantMember.setBalance(tenantMember.getBalance().subtract(realProfitAmount));
            tenantMember.setClearBalance(tenantMember.getClearBalance().subtract(realProfitAmount));
            memberDao.merge(tenantMember);

            Deposit deposit = new Deposit();
            deposit.setType(Deposit.Type.rebate);
            deposit.setStatus(Deposit.Status.complete);
            deposit.setCredit(BigDecimal.ZERO);
            deposit.setDebit(realProfitAmount);
            deposit.setBalance(tenantMember.getBalance());
            deposit.setOperator(operator != null ? operator.getUsername() : null);
            deposit.setMember(tenantMember);
            deposit.setMemo("佣金结算 单号:" + order.getSn());
            deposit.setOrder(order);
            depositDao.persist(deposit);
            Setting setting = SettingUtils.get();
            Message msg = EntitySupport.createInitMessage(Message.Type.account,
                    "您的账户，支出佣金" + setting.setScale(realProfitAmount).toString() + "元，订单号：" + order.getSn() + ".",
                    order.getSn(), tenantMember, null);
            msg.setTrade(trade);
            msg.setDeposit(deposit);
            messageService.save(msg);
        }
        trade.setClearing(true);
        trade.setClearingDate(new Date());

        if (saved) {
            tradeDao.persist(trade);
        }
    }

    @Transactional
    public void jobSign(Long tradeId) {
        Trade trade = tradeDao.find(tradeId);
        Order order = trade.getOrder();
        sign(order, trade, null);
    }

    @Transactional
    public void sign(Order order, Trade trade, Member operator) {
        Assert.notNull(order);
        Assert.notNull(trade);
        Member member = order.getMember();
        Setting setting = SettingUtils.get();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
        memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
        //if (trade.getIsAllocatedStock()) {
        //	stockStrategy.releaseAllocatedTrade(trade);
        //	trade.setIsAllocatedStock(false);
        //}
        trade.setConfirmDate(new Date());
        trade.setShippingStatus(ShippingStatus.accept);
        trade.setOrderStatus(OrderStatus.completed);
        trade.setCompletedDate(new Date());
        if (!trade.getClearing()) {
            clearing(order, trade, operator, false);
        }

        tradeDao.persist(trade);
        
        calcRebate(trade,member);

        clearingToSupplier(trade);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(Type.sign);
        orderLog.setContent("订单已签收");
        orderLog.setOperator(operator != null ? operator.getUsername() : null);
        orderLog.setOrder(order);
        orderLog.setTrade(trade);
        orderLogDao.persist(orderLog);
        
        complete(trade, member);
    }

    public void calcRebate(Trade trade, Member operator) {
        BigDecimal agencyAmt = trade.getAgencyAmount().subtract(trade.getDiscount());
        if (agencyAmt.compareTo(BigDecimal.ZERO)<0) {
        	agencyAmt = BigDecimal.ZERO; 
        }
        Order order = trade.getOrder();
        BigDecimal resv = BigDecimal.ZERO;
        Setting setting = SettingUtils.get();
    	Member guideMember = trade.getOrder().getExtension();
        BigDecimal guideAmount = BigDecimal.ZERO;
        if (agencyAmt.compareTo(BigDecimal.ZERO) > 0 && trade.getPaymentStatus().equals(PaymentStatus.paid)) {
        	CouponCode pCode = trade.getCouponCode();
            guideAmount = setting.setScale(agencyAmt.multiply(setting.getGuidePercent()));
        	Boolean useMc = false;
        	if (pCode!=null && pCode.getCoupon().getType().equals(Coupon.Type.multipleCoupon)) {
        		useMc = true;
        	}
        	//计算导购分润
            if ((guideAmount.compareTo(BigDecimal.ZERO)>0) && ( guideMember!=null || useMc )) {
            	if (guideMember!=null) {
                    memberDao.lock(guideMember, LockModeType.PESSIMISTIC_WRITE);
                    guideMember.setBalance(guideMember.getBalance().add(guideAmount));
                    memberDao.merge(guideMember);
                    Deposit deposit = new Deposit();
                    deposit.setType(Deposit.Type.profit);
                    deposit.setStatus(Deposit.Status.complete);
                    deposit.setCredit(guideAmount);
                    deposit.setDebit(BigDecimal.ZERO);
                    deposit.setBalance(guideMember.getBalance());
                    deposit.setOperator(operator != null ? operator.getUsername() : null);
                    deposit.setMember(guideMember);
                    deposit.setOrder(order);
                    deposit.setMemo("订单分润 单号:" + order.getSn());
                    depositDao.persist(deposit);
                    
            		Rebate rebate = new Rebate();
            		rebate.setMember(guideMember);
            		rebate.setType(Rebate.Type.sale);
            		rebate.setAmount(trade.getAgencyAmount());
            		rebate.setBrokerage(guideAmount);
            		rebate.setPercent(setting.getGuidePercent());
            		rebate.setStatus(Rebate.Status.success);
            		rebate.setOrderType(Rebate.OrderType.order);
            		rebate.setDescription("推广商品所得返利");
            		rebate.setTrade(trade);
            		rebateDao.persist(rebate);

            		resv = resv.add(guideAmount);
                    Message msg = EntitySupport.createInitMessage(Message.Type.account,
                            "恭喜您，您的客户" + order.getMember().getDisplayName() + "给您带来" + guideAmount.toString() + "元的分润收入，已转入您账户",
                            order.getSn(), guideMember, null);
                    msg.setDeposit(deposit);
                    messageService.save(msg);
            	} else {
                    BigDecimal pAmt = trade.getProviderAmount();
                    BigDecimal guideOwnerAmount = setting.setScale(agencyAmt.multiply(setting.getGuideOwnerPercent()));
            		BigDecimal bkg = couponNumberService.resolveBrokerage(trade.getDiscount(), guideAmount,guideOwnerAmount, order.getMember(), pCode, trade);
            		resv = resv.add(bkg);
                    resv = resv.add(trade.getProviderAmount());
                    trade.setProviderAmount(pAmt);
            	}
            }
        	
            if (guideMember!=null && !useMc && guideMember.getTenant()!=null && guideMember.getTenant().getMember() != null && (guideMember.getTenant().getStatus().equals(Tenant.Status.success) || guideMember.getTenant().getStatus().equals(Tenant.Status.confirm))) {
            	Member guideOwnerMember = guideMember.getTenant().getMember();
            	BigDecimal guideOwnerAmount = setting.setScale(agencyAmt.multiply(setting.getGuideOwnerPercent()));
                if (guideOwnerAmount.compareTo(BigDecimal.ZERO)>0) {
                  	memberDao.lock(guideOwnerMember, LockModeType.PESSIMISTIC_WRITE);
                  	guideOwnerMember.setBalance(guideOwnerMember.getBalance().add(guideOwnerAmount));
                    memberDao.merge(guideOwnerMember);
                    Deposit deposit = new Deposit();
                    deposit.setType(Deposit.Type.profit);
                    deposit.setStatus(Deposit.Status.complete);
                    deposit.setCredit(guideOwnerAmount);
                    deposit.setDebit(BigDecimal.ZERO);
                    deposit.setBalance(guideOwnerMember.getBalance());
                    deposit.setOperator(operator != null ? operator.getUsername() : null);
                    deposit.setMember(guideOwnerMember);
                    deposit.setOrder(order);
                    deposit.setMemo("订单分润 单号:" + order.getSn());
                    depositDao.persist(deposit);
                    
            		Rebate rebate = new Rebate();
            		rebate.setType(Rebate.Type.sale);
            		rebate.setMember(guideOwnerMember);
            		rebate.setAmount(trade.getAgencyAmount());
            		rebate.setBrokerage(guideOwnerAmount);
            		rebate.setPercent(setting.getGuideOwnerPercent());
            		rebate.setStatus(Rebate.Status.success);
            		rebate.setOrderType(Rebate.OrderType.order);
            		rebate.setDescription("推广商品店主所得返利");
            		rebate.setTrade(trade);
            		rebateDao.persist(rebate);
            		resv = resv.add(guideAmount);
                    Message msg = EntitySupport.createInitMessage(Message.Type.account,
                            "恭喜您，您推广的客户" + order.getMember().getDisplayName() + "给您带来" + guideOwnerAmount.toString() + "元的店主分润收入，已转入您账户",
                            order.getSn(), guideOwnerMember, null);
                    msg.setDeposit(deposit);
                    messageService.save(msg);
                }
        		resv = resv.add(guideOwnerAmount);
            }
        	
            if (order.getMember().getMember()!=null && (!useMc)) {
            	Member shareMember = order.getMember().getMember();
            	BigDecimal shareAmount = setting.setScale(agencyAmt.multiply(setting.getSharePercent()));
                if (shareAmount.compareTo(BigDecimal.ZERO)>0) {
                  	memberDao.lock(shareMember, LockModeType.PESSIMISTIC_WRITE);
                  	shareMember.setBalance(shareMember.getBalance().add(shareAmount));
                    memberDao.merge(shareMember);
                    Deposit deposit = new Deposit();
                    deposit.setType(Deposit.Type.profit);
                    deposit.setStatus(Deposit.Status.complete);
                    deposit.setCredit(shareAmount);
                    deposit.setDebit(BigDecimal.ZERO);
                    deposit.setBalance(shareMember.getBalance());
                    deposit.setOperator(operator != null ? operator.getUsername() : null);
                    deposit.setMember(shareMember);
                    deposit.setOrder(order);
                    deposit.setMemo("订单分润 单号:" + order.getSn());
                    depositDao.persist(deposit);
                    
            		Rebate rebate = new Rebate();
            		rebate.setMember(shareMember);
            		rebate.setType(Rebate.Type.extension);
            		rebate.setAmount(trade.getAgencyAmount());
            		rebate.setBrokerage(shareAmount);
            		rebate.setPercent(setting.getSharePercent());
            		rebate.setStatus(Rebate.Status.success);
            		rebate.setOrderType(Rebate.OrderType.order);
            		rebate.setDescription("发展会员所得返利");
            		rebate.setTrade(trade);
            		rebateDao.persist(rebate);
            		resv = resv.add(guideAmount);
                    Message msg = EntitySupport.createInitMessage(Message.Type.account,
                            "恭喜您，您的会员" + order.getMember().getDisplayName() + "给您带来" + shareAmount.toString() + "元的分润收入，已转入您账户",
                            order.getSn(), shareMember, null);
                    msg.setDeposit(deposit);
                    messageService.save(msg);
                }
        		resv = resv.add(shareAmount);
            }
            
            if (order.getMember().getShareOwner()!=null && order.getMember().getShareOwner().getMember()!=null && (!useMc) && (order.getMember().getShareOwner().getStatus().equals(Tenant.Status.confirm) || order.getMember().getShareOwner().getStatus().equals(Tenant.Status.success))) {
            	Member shareOwnerMember = order.getMember().getShareOwner().getMember();
            	BigDecimal shareOwnerAmount = setting.setScale(agencyAmt.multiply(setting.getShareOwnerPercent()));
                if (shareOwnerAmount.compareTo(BigDecimal.ZERO)>0) {
                  	memberDao.lock(shareOwnerMember, LockModeType.PESSIMISTIC_WRITE);
                  	shareOwnerMember.setBalance(shareOwnerMember.getBalance().add(shareOwnerAmount));
                    memberDao.merge(shareOwnerMember);
                    Deposit deposit = new Deposit();
                    deposit.setType(Deposit.Type.profit);
                    deposit.setStatus(Deposit.Status.complete);
                    deposit.setCredit(shareOwnerAmount);
                    deposit.setDebit(BigDecimal.ZERO);
                    deposit.setBalance(shareOwnerMember.getBalance());
                    deposit.setOperator(operator != null ? operator.getUsername() : null);
                    deposit.setMember(shareOwnerMember);
                    deposit.setOrder(order);
                    deposit.setMemo("订单分润 单号:" + order.getSn());
                    depositDao.persist(deposit);
                    
            		Rebate rebate = new Rebate();
            		rebate.setMember(shareOwnerMember);
            		rebate.setType(Rebate.Type.extension);
            		rebate.setAmount(trade.getAgencyAmount());
            		rebate.setBrokerage(shareOwnerAmount);
            		rebate.setPercent(setting.getShareOwnerPercent());
            		rebate.setStatus(Rebate.Status.success);
            		rebate.setOrderType(Rebate.OrderType.order);
            		rebate.setDescription("发展会员店主所得返利");
            		rebate.setTrade(trade);
            		rebateDao.persist(rebate);
            		resv = resv.add(guideAmount);
                    Message msg = EntitySupport.createInitMessage(Message.Type.account,
                            "恭喜您，您发展的会员" + order.getMember().getDisplayName() + "给您带来" + shareOwnerAmount.toString() + "元的店主分润收入，已转入您账户",
                            order.getSn(), shareOwnerMember, null);
                    msg.setDeposit(deposit);
                    messageService.save(msg);
                }
        		resv = resv.add(shareOwnerAmount);
            }
            
            BigDecimal bal = agencyAmt.subtract(resv);
            //生成平台佣金记录
    		Rebate rebate = new Rebate();
    		rebate.setType(Rebate.Type.platform);
    		rebate.setAmount(trade.getAgencyAmount());
    		rebate.setBrokerage(bal);
            rebate.setPercent(BigDecimal.ZERO);
    		rebate.setStatus(Rebate.Status.success);
    		rebate.setDescription("订单消费平台所得佣金");
    		rebate.setOrderType(Rebate.OrderType.order);
    		rebate.setTrade(trade);
    		rebateDao.persist(rebate);

            //消费返现
    		Rebate rebate_member = new Rebate();
    		rebate_member.setMember(order.getMember());
    		rebate_member.setType(Rebate.Type.rebate);
    		rebate_member.setAmount(trade.getAgencyAmount());
    		rebate_member.setBrokerage(trade.getDiscount());

            if(useMc){
                rebate_member.setPercent(BigDecimal.ONE.subtract(setting.getBrokerage()).subtract(setting.getGuidePercent()).subtract(setting.getGuideOwnerPercent()));
            }else {
                rebate_member.setPercent(BigDecimal.ZERO);
            }
    		rebate_member.setStatus(Rebate.Status.success);
    		rebate_member.setDescription("订单消费用户所得返现");
    		rebate_member.setOrderType(Rebate.OrderType.order);
    		rebate_member.setTrade(trade);
    		rebateDao.persist(rebate_member);
        }
        if(guideMember!=null){
            Long month = Long.parseLong(new SimpleDateFormat("yyyyMM").format(order.getCreateDate()));
            Task task = taskDao.findByMember(guideMember, month);
            if (task != null) {
                task.setDoSale(task.getDoSale().add(trade.getAmount()));
                taskDao.merge(task);
            }
            for (OrderItem orderItem : trade.getOrderItems(false)) {
                ExtendCatalog extendCatalog = extendCatalogDao.findExtendCatalog(guideMember, trade.getTenant(), orderItem.getProduct());
                if (extendCatalog == null) {
                    extendCatalog = new ExtendCatalog();
                    extendCatalog.setProduct(orderItem.getProduct());
                    extendCatalog.setTenant(trade.getTenant());
                    extendCatalog.setMember(guideMember);
                    extendCatalog.setAmount(guideAmount);
                    extendCatalog.setTimes(1L);
                    extendCatalog.setVolume(1L);
                    extendCatalog.setSalsePrice(orderItem.getSubtotal());
                    extendCatalog.setType(ExtendCatalog.Type.notRecommended);
                    extendCatalogDao.persist(extendCatalog);
                } else {
                    extendCatalog.setSalsePrice(extendCatalog.getSalsePrice().add(orderItem.getSubtotal()));
                    extendCatalog.setAmount(extendCatalog.getAmount().add(guideAmount));
                    extendCatalog.setVolume(extendCatalog.getVolume() + 1);
                    extendCatalogDao.merge(extendCatalog);
                }
            }
        }
    }
    @Transactional
    public void complete(Trade trade, Member operator) {
    	Order order = trade.getOrder();
        Assert.notNull(order);
        Member member = order.getMember();
        orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);

        //if (PaymentStatus.paid == order.getPaymentStatus() || PaymentStatus.partialPayment == order.getPaymentStatus() || PaymentStatus.partialRefunds == order.getPaymentStatus()) {
        //    member.setPoint(member.getPoint() + order.getPoint());
        //    for (Coupon coupon : order.getCoupons()) {
        //        couponCodeDao.build(coupon, member);
        //    }
        //}

        member.setAmount(member.getAmount().add(trade.getAmount()));
        memberDao.merge(member);

        for (OrderItem orderItem : trade.getOrderItems()) {
            if (orderItem != null) {
                Product product = orderItem.getProduct();
                productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
                if (product != null) {
                    Integer quantity = orderItem.calculateQuantityIntValue();
                    Calendar nowCalendar = Calendar.getInstance();
                    Calendar weekSalesCalendar = DateUtils.toCalendar(product.getWeekSalesDate());
                    Calendar monthSalesCalendar = DateUtils.toCalendar(product.getMonthSalesDate());
                    if (nowCalendar.get(Calendar.YEAR) != weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
                        product.setWeekSales((long) quantity);

                    } else {
                        product.setWeekSales(product.getWeekSales() + quantity);

                    }
                    if (nowCalendar.get(Calendar.YEAR) != monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
                        product.setMonthSales((long) quantity);

                    } else {
                        product.setMonthSales(product.getMonthSales() + quantity);

                    }
                    product.setSales(product.getSales() + quantity);
                    product.setWeekSalesDate(new Date());
                    product.setMonthSalesDate(new Date());
                    productDao.merge(product);
                    orderDao.flush();
                }
                Tenant tenant=orderItem.getTrade().getTenant();
                if(tenant!=null){
                    BigDecimal quantity = new BigDecimal(orderItem.calculateQuantityIntValue());
                    Calendar nowCalendar = Calendar.getInstance();
                    if(tenant.getWeekSalesDate()==null){
                        tenant.setWeekSalesDate(new Date());
                    }
                    if(tenant.getMonthSalesDate()==null){
                        tenant.setMonthSalesDate(new Date());
                    }
                    Calendar weekSalesCalendar = DateUtils.toCalendar(tenant.getWeekSalesDate());
                    Calendar monthSalesCalendar = DateUtils.toCalendar(tenant.getMonthSalesDate());
                    if (nowCalendar.get(Calendar.YEAR) != weekSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekSalesCalendar.get(Calendar.WEEK_OF_YEAR)) {
                        tenant.setWeekSales(quantity);

                    } else {
                        tenant.setWeekSales(tenant.getWeekSales().add(quantity));

                    }
                    if (nowCalendar.get(Calendar.YEAR) != monthSalesCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthSalesCalendar.get(Calendar.MONTH)) {
                        tenant.setMonthSales(quantity);

                    } else {
                        tenant.setMonthSales(tenant.getMonthSales().add(quantity));

                    }
                    tenant.setSales(tenant.getSales().add(quantity));
                    tenant.setWeekSalesDate(new Date());
                    tenant.setMonthSalesDate(new Date());
                    tenantDao.merge(tenant);
                    orderDao.flush();
                }
            }
        }
    }

    //给供应商打款
    @Transactional
    public void clearingToSupplier(Trade trade) {
        Setting setting = SettingUtils.get();
        Member tradeMember = trade.getTenant().getMember();
        memberDao.lock(tradeMember, LockModeType.PESSIMISTIC_WRITE);

        if (trade.getSuppliered() != null && trade.getSuppliered()) {
            return;
        }

        for (Tenant tenant : trade.getSuppliers()) {
            if (trade.getSuppliered() != null || !trade.getSuppliered()) {
                tenantDao.lock(tenant, LockModeType.PESSIMISTIC_WRITE);
                BigDecimal realHairAmount = trade.calcCost(tenant);
                if (realHairAmount.compareTo(BigDecimal.ZERO) != 0) {
                    tenant.setBalance(tenant.getBalance().add(realHairAmount));
                    //if (trade.getPaymentStatus().equals(PaymentStatus.paid) ) {
                    if (tenant.getFreezeBalance().compareTo(realHairAmount) > 0) {
                        tenant.setFreezeBalance(tenant.getFreezeBalance().subtract(realHairAmount));
                    } else {
                        tenant.setFreezeBalance(BigDecimal.ZERO);
                    }
                    //}
                    tenantDao.merge(tenant);
                }
            }
        }

        trade.setSuppliered(true);
        trade.setSupplierDate(new Date());
        tradeDao.persist(trade);
    }

    //从供应商退款
    @Transactional
    public void refundsFromSupplier(SpReturns spReturns) {
        spReturnsDao.lock(spReturns, LockModeType.PESSIMISTIC_WRITE);
        Tenant tenant = spReturns.getSupplier();
        tenantDao.lock(tenant, LockModeType.PESSIMISTIC_WRITE);
        if (spReturns.getType().equals(SpReturns.Type.event)) {
            BigDecimal realHairAmount = spReturns.getCost();

            if (realHairAmount.compareTo(BigDecimal.ZERO) > 0) {
                tenant.setBalance(tenant.getBalance().subtract(realHairAmount));
                tenantDao.merge(tenant);
            }
            spReturns.setSuppliered(true);
            spReturns.setSupplierDate(new Date());
            spReturnsDao.persist(spReturns);
        } else {
            for (Tenant supplier : spReturns.getSuppliers()) {
                BigDecimal realHairAmount = spReturns.calcOrderItemCost(supplier);
                Trade trade = spReturns.getTrade();
                if (realHairAmount.compareTo(BigDecimal.ZERO) > 0) {
                    if (trade.getSuppliered()) {
                        supplier.setBalance(supplier.getBalance().subtract(realHairAmount));
                    } else {
                        if (trade.getPaymentStatus().equals(PaymentStatus.paid) || trade.getPaymentStatus().equals(PaymentStatus.refunded)) {
                            if (supplier.getFreezeBalance().compareTo(realHairAmount) > 0) {
                                supplier.setFreezeBalance(supplier.getFreezeBalance().subtract(realHairAmount));
                            } else {
                                supplier.setFreezeBalance(BigDecimal.ZERO);
                            }
                        }
                    }
                    tenantDao.merge(supplier);
                }
            }
            spReturns.setSuppliered(true);
            spReturns.setSupplierDate(new Date());
            spReturnsDao.persist(spReturns);
        }
    }

    @Override
    public void delete(Order order) {
        for (Trade trade : order.getTrades()) {
            if (trade.getIsAllocatedStock()) {
                stockStrategy.releaseAllocatedTrade(trade);
            }
        }
        super.delete(order);
    }

    /**
     * 发送订单消息到蓝桥(异步)
     */
    public void sendOrderInfoTask(final String orderSn) {
        taskExecutor.execute(new Runnable() {
            public void run() {
                xxkyService xxkyService = new xxkyService();
                Order order = orderDao.findBySn(orderSn);
                System.out.println(order.getSn());
            }
        });
    }

    @Transactional(readOnly = true)
    public Order findBySn(String sn) {
        return orderDao.findBySn(sn);
    }

    @Transactional(readOnly = true)
    public List<Order> findList(Member member, Integer count, List<Filter> filters, List<net.wit.Order> orders) {
        return orderDao.findList(member, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public List<Order> findList(Tenant tenant, Date startDate, Date endDate, List<OrderStatus> orderStatuses, List<ShippingStatus> shippingStatuses) {
        return orderDao.findList(tenant, startDate, endDate, orderStatuses, shippingStatuses);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Member member, Pageable pageable) {
        return orderDao.findPage(member, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
        return orderDao.findPage(member, orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
        return orderDao.findPage(orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);
    }

    @Transactional(readOnly = true)
    public Long count(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
        return orderDao.count(orderStatus, paymentStatus, shippingStatus, hasExpired);
    }

    @Transactional(readOnly = true)
    public Long waitingPaymentCount(Member member) {
        return orderDao.waitingPaymentCount(member);
    }

    @Transactional(readOnly = true)
    public Long waitingShippingCount(Member member) {
        return orderDao.waitingShippingCount(member);
    }

    @Transactional(readOnly = true)
    public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
        return orderDao.getSalesAmount(beginDate, endDate);
    }

    @Transactional(readOnly = true)
    public Integer getSalesVolume(Date beginDate, Date endDate) {
        return orderDao.getSalesVolume(beginDate, endDate);
    }

    @Transactional(readOnly = true)
    public Integer getMemberBuyQuantity(Member member, Product product) {
        return orderDao.getMemberBuyQuantity(member, product);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Member member, Date beginDate, Date endDate, Pageable pageable) {
        return orderDao.findPage(member, beginDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPageTenant(Member member, Date beginDate, Date endDate, Pageable pageable) {
        return orderDao.findPageTenant(member, beginDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPageMember(Member member, Date beginDate, Date endDate, Pageable pageable) {
        return orderDao.findPageMember(member, beginDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPageAll(Member member, Date beginDate, Date endDate, Pageable pageable) {
        return orderDao.findPageAll(member, beginDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Long countAll(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
        return orderDao.countAll(member, orderStatus, paymentStatus, shippingStatus, hasExpired);
    }

    @Transactional(readOnly = true)
    public long countmy(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired) {
        return orderDao.countmy(member, orderStatus, paymentStatus, shippingStatus, hasExpired);
    }

    @Transactional(readOnly = true)
    public List<Order> myOrder(Member member) {
        return orderDao.myOrder(member);
    }

    /**
     * 查找会员代付款订单
     */
    public Page<Order> findWaitPay(Member member, Boolean hasExpired, Pageable pageable) {
        return orderDao.findWaitPay(member, hasExpired, pageable);
    }

    /**
     * 查找会员待发货订单
     */
    public Page<Order> findWaitShipping(Member member, Boolean hasExpired, Pageable pageable) {
        return orderDao.findWaitShipping(member, hasExpired, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPageWithoutStatus(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Pageable pageable) {
        return orderDao.findPageWithoutStatus(member, orderStatus, paymentStatus, shippingStatus, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate, Pageable pageable) {
        return orderDao.findPage(orderStatus, paymentStatus, shippingStatus, hasExpired, beginDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate, List<Tenant> tenants, Pageable pageable) {
        return orderDao.findPage(orderStatus, paymentStatus, shippingStatus, hasExpired, beginDate, endDate, tenants, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Area area, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Boolean hasExpired, Date beginDate, Date endDate,String keyword, Pageable pageable) {
        return orderDao.findPage(area, orderStatus, paymentStatus, shippingStatus, hasExpired, beginDate, endDate,keyword, pageable);
    }

    /**
     * 查找下属所有区域
     */
    private void findAllChildren(Area area, List<Area> areas) {
        if (area != null) {
            areas.add(area);
            List<Area> children = new ArrayList<Area>(area.getChildren());
            if (children != null && children.size() > 0) {
                for (Area area2 : children) {
                    // areas.add(area2);
                    findAllChildren(area2, areas);
                }
            }
        }
    }

    /**
     * 根据订单和子订单计算子订单优惠
     *
     * @param order 订单
     * @param trade 子订单
     * @return 子订单优惠
     */
    private BigDecimal getCouponDiscountOfTrade(Order order, Trade trade, CouponCode couponCode) {
        if (order == null || trade == null)
            return BigDecimal.ZERO;
        BigDecimal couponDiscountOfTrade = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.addAll(order.getOrderItems()); // 订单所有的订单项
        List<OrderItem> orderItems2 = trade.getOrderItems();
        orderItems.removeAll(orderItems2); // 总订单除去退货的自订单所有的订单项
        Coupon coupon = couponCode.getCoupon();
        Set<ProductCategory> productCategory = coupon.getProductCategory();
        if (productCategory == null || productCategory.size() == 0) {
            couponDiscountOfTrade = coupon.getAmount().multiply(trade.getPrice())
                    .divide(order.getPrice(), 2, BigDecimal.ROUND_HALF_UP);
            return couponDiscountOfTrade.compareTo(trade.getPrice()) > 0 ? trade.getPrice() : couponDiscountOfTrade;
        }
        BigDecimal otherBigDecimal = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) { // 除去本次子订单 其他的子订单总和
            if (isExistToProductCategory(orderItem.getProduct().getProductCategory(), productCategory)) {
                otherBigDecimal = otherBigDecimal.add(orderItem.getPrice());
            }
        }
        BigDecimal refundsBigDecimal = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems2) {
            if (isExistToProductCategory(orderItem.getProduct().getProductCategory(), productCategory)) {
                refundsBigDecimal = refundsBigDecimal.add(orderItem.getPrice());
            }
        }
        if (otherBigDecimal.add(refundsBigDecimal).compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        couponDiscountOfTrade = coupon.getAmount().multiply(refundsBigDecimal)
                .divide(otherBigDecimal.add(refundsBigDecimal), 2, BigDecimal.ROUND_HALF_UP);
        return couponDiscountOfTrade.compareTo(trade.getPrice()) > 0 ? trade.getPrice() : couponDiscountOfTrade;
    }

    /**
     * 判断请求id是否包含在给定id的商品类型子集内
     *
     * @return 是否包含
     */
    private boolean isExistToProductCategory(ProductCategory productCategory, Set<ProductCategory> productCategorySet) {
        Set<ProductCategory> allProductCategories = queryAllProductCategoriesById(productCategorySet);
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


    public boolean isLimit(Member member, Integer count) {
        return orderDao.isLimit(member, count);
    }

    @Override
    public BigDecimal countmySales(Member member, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus) {
        return orderDao.countmySales( member, orderStatus, paymentStatus, shippingStatus);
    }

    public Order findByTokenKey(String token_key){
        return orderDao.findByTokenKey(token_key);
    }
}