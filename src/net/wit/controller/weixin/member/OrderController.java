/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.*;
import net.wit.entity.*;
import net.wit.entity.Order.QueryStatus;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 会员中心 - 我的订单
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinMemberOrderController")
@RequestMapping("/weixin/member/order")
public class OrderController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "receiverServiceImpl")
    private ReceiverService receiverService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "paymentMethodServiceImpl")
    private PaymentMethodService paymentMethodService;

    @Resource(name = "shippingMethodServiceImpl")
    private ShippingMethodService shippingMethodService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "shippingServiceImpl")
    private ShippingService shippingService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "spReturnsServiceImpl")
    private SpReturnsService spReturnsService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    /**
     * 我的订单
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(String extension, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/member/order";
    }

    /**
     * 订单明细
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(String extension,Long id, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("id",id);
        return "weixin/member/order/detail";
    }

    /**
     * 我的订单列表
     * type    unshipped 待发货; unpaid 待支付; unreciver 待签收; unreview 待评价
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(String type, String keyword, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        QueryStatus queryStatus = null;
        if ("unshipped".equals(type)) {
            queryStatus = QueryStatus.unshipped;
        } else if ("unpaid".equals(type)) {
            queryStatus = QueryStatus.unpaid;
        } else if ("unreciver".equals(type)) {
            queryStatus = QueryStatus.shipped;
        } else if ("unreview".equals(type)) {
            queryStatus = QueryStatus.unreview;
        }
        Page<Trade> page = tradeService.findPage(pageable, member, queryStatus, keyword);
        return DataBlock.success(TradeListModel.bindData(page.getContent()), page, "执行成功");
    }

    /**
     * 订单统计
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock count() {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("unshipped", tradeService.count(null, member, QueryStatus.unshipped, null));//待发货
        data.put("unpaid", tradeService.count(null, member, QueryStatus.unpaid, null));//待支付
        data.put("unreciver", tradeService.count(null, member, QueryStatus.shipped, null));//待收货
        data.put("unreview", tradeService.count(null, member, QueryStatus.unreview, null));//待评价
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 订单明细
     * @param id 子订单Id
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.find(id);
        if (trade == null) {
            return DataBlock.error("订单不存在");
        }
        TradeModel model = new TradeModel();
        model.copyFrom(trade);
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 提醒卖家发货
     *
     * @params id 子订单Id
     */
    @RequestMapping(value = "/remind", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock remind(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.find(id);
        if (trade.getShippingStatus().equals(Order.ShippingStatus.shippedApply)) {
            Message message = EntitySupport.createInitMessage(Message.Type.order, "买家催促处理退货", trade.getOrder().getSn(),
                    trade.getTenant().getMember(), null);
            message.setTrade(trade);
            message.setWay(Message.Way.tenant);
            messageService.save(message);
        } else {
            Message message = EntitySupport.createInitMessage(Message.Type.order, "买家催促尽快发货", trade.getOrder().getSn(),
                    trade.getTenant().getMember(), null);
            message.setTrade(trade);
            message.setWay(Message.Way.tenant);
            messageService.save(message);
        }
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 取消订单(未发货前)
     * id 子订单Id
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock refund(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.find(id);
        if (trade == null) {
            return DataBlock.error("订单不存在");
        }
        if (trade.isLocked(member)) {
            return DataBlock.error("订单被锁定");
        }
        if (trade.getPaymentStatus().equals(Order.PaymentStatus.unpaid) && trade.getShippingStatus().equals(Order.ShippingStatus.unshipped)) {
            orderService.cancel(trade, member);
            return DataBlock.success("success", "订单已取消");
        }
        if (trade.isSpReturns() && !trade.getPaymentStatus().equals(Order.PaymentStatus.refundApply) && !trade.getShippingStatus().equals(Order.ShippingStatus.shippedApply)) {
            orderService.spReturns(trade, null, member);
            return DataBlock.success("success", "已申请退货");
        } else {
            return DataBlock.error("该订单不能申请退货");
        }
    }

    /**
     * 退货
     * id 子订单Id
     */
    @RequestMapping(value = "/return", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock returns(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.find(id);
        if (trade == null) {
            return DataBlock.error("订单不存在");
        }
        if (trade.isLocked(member)) {
            return DataBlock.error("订单被锁定");
        }
        if (trade.getPaymentStatus().equals(Order.PaymentStatus.unpaid) && trade.getShippingStatus().equals(Order.ShippingStatus.unshipped)) {
            orderService.cancel(trade, member);
            return DataBlock.success("success", "订单已取消");
        }
        if (trade.isSpReturns() && !trade.getPaymentStatus().equals(Order.PaymentStatus.refundApply) && !trade.getShippingStatus().equals(Order.ShippingStatus.shippedApply)) {
            orderService.spReturns(trade, null, member);
            return DataBlock.success("success", "已申请退货");
        } else {
            return DataBlock.error("该订单不能申请退货");
        }
    }

    /**
     * 签收（买家）
     * id 子订单Id
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock confirm(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        try {
            Trade trade = tradeService.find(id);
            if (trade != null && memberService.getCurrent().equals(trade.getOrder().getMember())) {
                Order order = trade.getOrder();
                if (order.isLocked(member)) {
                    return DataBlock.error("该订单已被锁定，请稍后再试！");
                }
                orderService.sign(order, trade, member);
                return DataBlock.success("success", "签收成功");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return DataBlock.error("签收出错了");
    }


    /**
     * 发起支付（单个子订单）
     * id 子订单Id
     */
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock tradePayment(@PathVariable Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Payment payment = new Payment();
        Trade trade = tradeService.find(id);
        if (trade.getOrder().isLocked(member)) {
            return DataBlock.error("weixin.member.order.locked");
        }
        if (!member.equals(trade.getOrder().getMember()) || trade.getOrder().isExpired()) {
            return DataBlock.error("订单已过期");
        }
        if (Order.OrderStatus.cancelled.equals(trade.getOrderStatus())) {
            return DataBlock.error("订单已取消");
        }
        if (trade.getPaymentStatus() != Order.PaymentStatus.unpaid && trade.getPaymentStatus() != Order.PaymentStatus.partialPayment) {
            return DataBlock.error("订单已无效");
        }
        if (trade.getOrder().getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
            return DataBlock.error("付款金额不能为0");
        }
        payment.setMember(trade.getOrder().getMember());
        payment.setPayer(trade.getOrder().getMember().getName());
        payment.setMemo("订单支付");
        payment.setSn(snService.generate(Sn.Type.payment));
        payment.setType(Payment.Type.payment);
        payment.setMethod(Payment.Method.online);
        payment.setStatus(Payment.Status.wait);
        payment.setPaymentMethod("");
        payment.setFee(BigDecimal.ZERO);
        payment.setAmount(trade.getAmount());
        payment.setPaymentPluginId("");
        payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
        payment.setTrade(trade);
        payment.setOrder(trade.getOrder());
        paymentService.save(payment);
        return DataBlock.success(payment.getSn(), "执行成功");
    }

    /**
     * 订单确认页
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock info(Location location,HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            return DataBlock.error("购物车不能为空");
        }
        if (!isValid(cart)) {
            return DataBlock.error("购物车非法操作");
        }
        cartService.clearActivity();
        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);
        if (activityPlanning != null) {
            if (orderService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
                cartService.bindActivity();
            }
        }
        ReceiverModel receiverModel = null;
        Receiver receiver = receiverService.findDefault(member);
        if (receiver != null) {
            receiverModel = new ReceiverModel();
            receiverModel.copyFrom(receiver);
        }
        List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
        List<ShippingMethod> shippingMethods = shippingMethodService.findAll();
        PaymentMethod paymentMethod = paymentMethods.size() > 0 ? paymentMethods.get(0) : null;
        ShippingMethod shippingMethod = shippingMethods.size() > 0 ? shippingMethods.get(0) : null;
        Order order = orderService.build(cart, receiver, paymentMethod, shippingMethod, null, false, null, false, null, null);
        ConfirmOrderModel model = new ConfirmOrderModel();
        List<CouponCode> codes = new ArrayList<>();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        List<CouponCode> couponCodes = couponCodeService.findEnabledList(member);
        if (cart.isCouponAllowed()) {
            for (CouponCode coupon : couponCodes) {
                if (!coupon.getCoupon().getType().equals(Coupon.Type.tenantCoupon) || !coupon.getCoupon().getIsEnabled() || coupon.hasExpired() || !cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
                } else {
                    codes.add(coupon);
                }
            }
        }
        model.copyFrom(order, codes);
//        model.copyFrom(order, location, codes);
        data.put("order", model);
        data.put("paymentMethods", PaymentMethodModel.bindData(paymentMethods));
        data.put("shippingMethods", ShippingMethodModel.bindData(shippingMethods));
        data.put("receiver", receiverModel);
        data.put("extension", request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME));
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 订单确认页
     */
    @RequestMapping(value = "/orderPay", method = RequestMethod.GET)
    public String orderPay(String extension,String token_key,HttpServletRequest request,Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("token_key", token_key);
        return "weixin/member/order/orderPay";
    }

    /**
     * 计算费用(选择支付方式、配送方式、优惠券使用)
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock calculate(Long paymentMethodId, Long shippingMethodId, String[] codes,Long receiverId) {
        Map<String, Object> data = new HashMap<>();
        Cart cart = cartService.getCurrent();
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (cart == null || cart.isEmpty()) {
            return DataBlock.error("ajax.cart.notEmpty");
        }
        PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
        ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
        Set<CouponCode> couponCodes = new HashSet<>();
        if (codes != null) {
            for (String c : codes) {
                CouponCode couponCode = couponCodeService.findByCode(c);
                couponCodes.add(couponCode);
            }
        }
        Receiver receiver = null;
        if(receiverId!=null&&!"".equals(receiverId)){
            receiver = receiverService.find(receiverId);
        }
        Order order = orderService.build(cart, receiver, paymentMethod, shippingMethod, couponCodes, false, null, false, null, null);

        data.put("quantity", order.getQuantity());
        data.put("price", order.getPrice());
        data.put("freight", order.getFreight());
        data.put("promotionDiscount", order.getPromotionDiscount());
        data.put("couponDiscount", order.getCouponDiscount());
        data.put("tax", order.getTax());
        data.put("amountPayable", order.getAmountPayable());
        data.put("discount", order.getDiscount());
        List<Map<String, Object>> trades = new ArrayList<>();
        for (Trade trade : order.getTrades()) {
            Map<String, Object> map = new HashMap<>();
            map.put("tenantId", trade.getTenant().getId().toString());
            map.put("freight", trade.getFreight());
            map.put("amount", trade.getAmount());
            trades.add(map);
        }
        data.put("trades", trades);
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 提交订单创建
     * receiverId 收货地址编号
     * paymentMethodId 支付方式编号
     * shippingMethodId 配送方式编号
     * codes 优惠码
     * memo 备注
     * extensionId 导购、分享者编号
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock create(Long receiverId, Long paymentMethodId, Long shippingMethodId, String[] codes, Long[] deliveryCenterIds, String memo, Long extensionId, String token_key, HttpServletRequest request) {
        if (extensionId != null) {
            Member extension = memberService.find(extensionId);
            if (extension != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extension.getUsername());
            }
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (member.getUsername().equals("13860431130")) {
            return DataBlock.error("体验账号不能购物");
        }
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            return DataBlock.error("ajax.cart.notEmpty");
        }
        if (cart.getIsLowStock()) {
            return DataBlock.error("ajax.order.cartLowStock");
        }
        PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
        if (paymentMethod == null) {
            return DataBlock.error("ajax.order.paymentMethodNotExsit");
        }
        ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
        if (shippingMethod == null) {
            return DataBlock.error("ajax.order.shippingMethodNotExsit");
        }
        if (!paymentMethod.getShippingMethods().contains(shippingMethod)) {
            return DataBlock.error("ajax.order.deliveryUnsupported");
        }
        Receiver receiver = null;
        List<DeliveryCenter> deliveryCenters = new ArrayList<>();
        if (shippingMethod.getMethod() == ShippingMethod.Method.F2F) {
            if (deliveryCenterIds != null) {
                for (Long deliveryCenterId : deliveryCenterIds) {
                    deliveryCenters.add(deliveryCenterService.find(deliveryCenterId));
                }
            }
        } else {
            receiver = receiverService.find(receiverId);
            if (receiver == null) {
                return DataBlock.error("请填写收货地址");
            }
        }
        String[] memos=null;
        if(StringUtils.isNotBlank(memo)){
            memos=new String[]{memo};
        }
        Set<CouponCode> couponCodes = new HashSet<>();
        if (codes != null) {
            for (String code : codes) {
                CouponCode couponCode = couponCodeService.findByCode(code);
                couponCodes.add(couponCode);
            }
        }
        Order order = orderService.create(cart, receiver, paymentMethod, shippingMethod, couponCodes, false, null, false, null, memos, deliveryCenters, member, null, Order.OrderSource.app, null, token_key);
        return DataBlock.success(order.getSn(), "执行成功");
    }

    /**
     * 发起支付--整单合并支付
     * sn 订单号
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock payment(String sn) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Payment payment = new Payment();
        Order order = orderService.findBySn(sn);
        if (order.isLocked(member)) {
            return DataBlock.error("weixin.member.order.locked");
        }
        if (order == null || !member.equals(order.getMember()) || order.isExpired()) {
            return DataBlock.error("订单已过期");
        }
        if (Order.OrderStatus.cancelled.equals(order.getOrderStatus())) {
            return DataBlock.error("订单已取消");
        }
        if (order.getPaymentStatus() != Order.PaymentStatus.unpaid && order.getPaymentStatus() != Order.PaymentStatus.partialPayment) {
            return DataBlock.error("订单已无效");
        }
        if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
            return DataBlock.error("付款金额不能为0");
        }
        payment.setMember(order.getMember());
        payment.setPayer(order.getMember().getName());
        payment.setMemo("订单支付");
        payment.setSn(snService.generate(Sn.Type.payment));
        payment.setType(Payment.Type.payment);
        payment.setMethod(Payment.Method.online);
        payment.setStatus(Payment.Status.wait);
        payment.setPaymentMethod("");
        payment.setFee(BigDecimal.ZERO);
        payment.setAmount(order.getAmountPayable());
        payment.setPaymentPluginId("");
        payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
        payment.setOrder(order);
        paymentService.save(payment);
        return DataBlock.success(payment.getSn(), "执行成功");
    }

    /**
     * 打开付款单
     * sn 付款单号
     */
    @RequestMapping(value = "/paymentView", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock paymentView(String sn) {
        Payment payment = paymentService.findBySn(sn);
        PaymentModel model = new PaymentModel();
        model.copyFrom(payment);
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 打开订单
     * @param sn 订单号
     */
    @RequestMapping(value = "/orderView", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock orderView(String sn) {
        Order order=orderService.findBySn(sn);
        if(order==null){
            return DataBlock.error("无效订单号");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("sn",order.getSn());
        map.put("tenantName",order.getTrades().get(0).getTenant().getName());
        map.put("createDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreateDate()));
        return DataBlock.success(map, "执行成功");
    }

}