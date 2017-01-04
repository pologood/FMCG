/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap.member;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.wit.controller.app.model.PromotionModel;
import net.wit.controller.wap.model.ProductModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.wap.BaseController;
import net.wit.entity.Order.OrderSource;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.SettingUtils;
import net.wit.util.WebUtils;

/**
 * Controller - 会员中心 - 订单
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapOrderController")
@RequestMapping("/wap/member/order")
public class OrderController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

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

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "shippingServiceImpl")
    private ShippingService shippingService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "appointmentServiceImpl")
    private AppointmentService appointmentService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;


    /**
     * 订单信息
     *
     * @param request
     * @param model
     * @returnt
     */
    @RequestMapping(value = "/order_info", method = RequestMethod.GET)
    public String orderInfo(Long id, String sn, HttpServletRequest request, ModelMap model) {
        Trade trade = tradeService.find(id);
        Set<OrderLog> orderLogs = trade.getOrderLogs();
        model.addAttribute("orderlogs", orderLogs);
        model.addAttribute("member", memberService.getCurrent());
        model.addAttribute("sn", sn);
        model.addAttribute("id", id);
        model.addAttribute("trade", trade);
        return "wap/member/order/order_info";
    }

    /**
     * 订单详情-立即评论页面
     *
     * @param type
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/order_evaluate", method = RequestMethod.GET)
    public String evaluate(Long id, String type, HttpServletRequest request, ModelMap model) {
        model.addAttribute("member", memberService.getCurrent());
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        return "wap/member/order/order_evaluate";
    }

    /**
     * 发送提货验证码
     */
    @RequestMapping(value = "/send_securiy", method = RequestMethod.POST)
    public
    @ResponseBody
    Message sendSecuriy(Long orderId) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Member member = memberService.getCurrent();
        Order order = orderService.find(orderId);
        for (Iterator<Trade> iterator = order.getTrades().iterator(); iterator.hasNext(); ) {
            Trade trade = iterator.next();
            if (order.getShippingMethod().getMethod().equals(ShippingMethod.Method.F2F)) {
                String content = "提货验证码:" + trade.getSafeKey().getValue() + trade.getSn() + ",供应商:"
                        + trade.getTenant().getName() + " 【" + bundle.getString("signature") + "】";
                SmsSend smsSend = new SmsSend();
                smsSend.setMobiles(member.getMobile());
                smsSend.setContent(content);
                smsSend.setType(SmsSend.Type.service);
                smsSendService.smsSend(smsSend);
            }
        }

        return Message.success("发送完毕成功");
    }

    /**
     * 保存收货地址
     */
    @RequestMapping(value = "/save_receiver", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> saveReceiver(Receiver receiver, Long areaId) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (receiver.getIsDefault() == null) {
            receiver.setIsDefault(false);
        }
        Member member = memberService.getCurrent();
        if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
            data.put("message", Message.error("shop.order.addReceiverCountNotAllowed", Receiver.MAX_RECEIVER_COUNT));
            return data;
        }
        receiver.setMember(member);
        Area area = member.getArea();
        receiver.setArea(area);
        if (area != null) {
            receiver.setZipCode(area.getZipCode());
        }
        if (!isValid(receiver)) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        receiverService.save(receiver);
        data.put("message", SUCCESS_MESSAGE);
        data.put("receiver", receiver);
        return data;
    }

    /**
     * 检查支付
     */
    @RequestMapping(value = "/check_payment", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean checkPayment(String sn) {
        Order order = orderService.findBySn(sn);
        if (order != null && memberService.getCurrent().equals(order.getMember())
                && order.getPaymentStatus() == PaymentStatus.paid) {
            return true;
        }
        return false;
    }

    /**
     * 优惠券信息
     */
    @RequestMapping(value = "/coupon_info", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> couponInfo(String code) {
        Map<String, Object> data = new HashMap<String, Object>();
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            data.put("message", Message.warn("shop.order.cartNotEmpty"));
            return data;
        }
        if (!cart.isCouponAllowed()) {
            data.put("message", Message.warn("shop.order.couponNotAllowed"));
            return data;
        }
        CouponCode couponCode = couponCodeService.findByCode(code);
        if (couponCode != null && couponCode.getCoupon() != null) {
            Coupon coupon = couponCode.getCoupon();
            if (!coupon.getIsEnabled()) {
                data.put("message", Message.warn("shop.order.couponDisabled"));
                return data;
            }
            if (couponCode.hasExpired()) {
                data.put("message", Message.warn("shop.order.couponHasExpired"));
                return data;
            }
            if (!cart.isValid(coupon)) {
                data.put("message", Message.warn("shop.order.couponInvalid"));
                return data;
            }
            if (couponCode.getIsUsed()) {
                data.put("message", Message.warn("shop.order.couponCodeUsed"));
                return data;
            }
            data.put("message", SUCCESS_MESSAGE);
            data.put("couponName", coupon.getName());
            return data;
        } else {
            data.put("message", Message.warn("shop.order.couponCodeNotExist"));
            return data;
        }
    }

    /**
     * 信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(HttpServletRequest request, Long receiverId, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return NO_LOGIN;
        }
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            return "redirect:/wap/cart/list.jhtml";
        }
        if (!isValid(cart)) {
            return ERROR_VIEW;
        }
        Receiver receiver = receiverService.find(receiverId);
        if (receiver == null) {
            receiver = receiverService.findDefault(member);
        }

        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);
        if (activityPlanning != null) {
            if (orderService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
                cartService.bindActivity();
            }
        }

        List<CouponCode> couponCodes = couponCodeService.findEnabledList(member);

        List<CouponCode> codes = new ArrayList<CouponCode>();
        for (CouponCode coupon : couponCodes) {
            if (!coupon.getCoupon().getIsEnabled() || coupon.hasExpired() || !cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
            } else {
                codes.add(coupon);
            }
        }


        Order order = orderService.build(cart, receiver, null, null, null, false, null, false, null, null);
        model.addAttribute("order", order);
        model.addAttribute("member", member);
        model.addAttribute("couponCodes", codes);

        model.addAttribute("cartToken", cart.getToken());
        model.addAttribute("receiver", receiver);
        model.addAttribute("appointments", appointmentService.findAll());
        model.addAttribute("appointment", appointmentService.findDefault());
        List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
        model.addAttribute("paymentMethods", paymentMethods);
        if (paymentMethods != null && paymentMethods.size() > 0) {
            model.addAttribute("paymentMethod", paymentMethods.get(0));
            if (paymentMethods.get(0).getMethod() == net.wit.entity.PaymentMethod.Method.offline) {
                model.addAttribute("useBalance", false);
            }
        } else {
            model.addAttribute("paymentMethod", null);
            model.addAttribute("useBalance", true);
        }
        List<ShippingMethod> shippingMethods = shippingMethodService.findAll();
        model.addAttribute("shippingMethods", shippingMethods);
        model.addAttribute("shippingMethod",
                (shippingMethods != null && shippingMethods.size() > 0) ? shippingMethods.get(0) : null);
        return "/wap/member/order/info";
    }


    /**
     * 确认订单
     */
    @RequestMapping(value = "/orderPay", method = RequestMethod.GET)
    public String orderPay(Long receiverId, String token_key, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/wap/bound/indexNew.jhtml?redirectUrl=/wap/member/order/orderPay.jhtml";
        }
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            return "redirect:/wap/cart/list.jhtml";
        }
        if (!isValid(cart)) {
            return ERROR_VIEW;
        }
        Receiver receiver = receiverService.find(receiverId);
        if (receiver == null) {
            receiver = receiverService.findDefault(member);
        }
    	cartService.clearActivity();
        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);
        if (activityPlanning != null) {
            if (payBillService.isLimit(member,activityPlanning.getActivityMaximumOrders())&&orderService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
                cartService.bindActivity();
            }
            //model.addAttribute("activityTenants", activityPlanning.getTenants());
        }

        List<CouponCode> couponCodes = couponCodeService.findEnabledList(member);
        List<CouponCode> codes = new ArrayList<CouponCode>();
        for (CouponCode coupon : couponCodes) {
            if (!coupon.getCoupon().getType().equals(Coupon.Type.tenantCoupon) || !coupon.getCoupon().getIsEnabled() || coupon.hasExpired() || !cart.isValid(coupon.getCoupon()) || coupon.getIsUsed()) {
            } else {
                codes.add(coupon);
            }
        }

        List<Map<String, Object>> mapList = new ArrayList<>();

        List<PromotionModel> promotionModels = new ArrayList<>();

        Order order = orderService.build(cart, receiver, null, null, null, false, null, false, null, null);
        for (Trade trade : order.getTrades()) {
            Long tenantId = trade.getTenant().getId();

            for (OrderItem orderItem : trade.getOrderItems()) {
                ProductModel productModel = new ProductModel();
                productModel.copyFrom(orderItem.getProduct());

                Map<String, Object> map = new HashMap<>();
                map.put("productID", productModel.getId());
                map.put("spec", productModel.getSpec());
                map.put("color", productModel.getColor());
                mapList.add(map);

                List<Promotion> promotions = promotionService.findList(null, trade.getTenant(), orderItem.getProduct());
                promotionModels = PromotionModel.bindData(promotions);
            }
        }

        JSONArray jsonPromtionModel = JSONArray.fromObject(promotionModels);
        JSONArray jsonArray = JSONArray.fromObject(mapList);
        model.addAttribute("jsonArray", jsonArray);
        model.addAttribute("promotionModel", jsonPromtionModel);
        model.addAttribute("order", order);
        model.addAttribute("member", member);
        model.addAttribute("couponCodes", codes);
        model.addAttribute("cartToken", cart.getToken());
        model.addAttribute("receiver", receiver);
        model.addAttribute("appointments", appointmentService.findAll());
        model.addAttribute("appointment", appointmentService.findDefault());
        List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
        model.addAttribute("paymentMethods", paymentMethods);
        if (paymentMethods != null && paymentMethods.size() > 0) {
            model.addAttribute("paymentMethod", paymentMethods.get(0));
            if (paymentMethods.get(0).getMethod() == net.wit.entity.PaymentMethod.Method.offline) {
                model.addAttribute("useBalance", false);
            }
        } else {
            model.addAttribute("paymentMethod", null);
            model.addAttribute("useBalance", true);
        }
        List<ShippingMethod> shippingMethods = shippingMethodService.findAll();
        model.addAttribute("shippingMethods", shippingMethods);
        model.addAttribute("shippingMethod",
                (shippingMethods != null && shippingMethods.size() > 0) ? shippingMethods.get(0) : null);
        model.addAttribute("token_key", token_key);
        return "/wap/member/order/orderPay";
    }


    /**
     * 计算
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> calculate(Long paymentMethodId, Long shippingMethodId, String codeType,
                                  Long receiverId, Long[] couponCodeId, Long point, @RequestParam(defaultValue = "false") Boolean isInvoice,
                                  String invoiceTitle, @RequestParam(defaultValue = "false") Boolean useBalance, Long[] tenantIds, String[] memo) {
        Map<String, Object> data = new HashMap<String, Object>();
        Cart cart = cartService.getCurrent();
        if (cart == null || cart.isEmpty()) {
            data.put("message", Message.error("shop.order.cartNotEmpty"));
            return data;
        }
        PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
        ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);

        Set<CouponCode> couponCodes = new HashSet<CouponCode>();
        for (Long couponId : couponCodeId) {
            CouponCode couponCode = couponCodeService.find(couponId);
            couponCodes.add(couponCode);
        }
        if (paymentMethod != null && paymentMethod.getMethod().equals(PaymentMethod.Method.balance)) {
            useBalance = true;
        }
        Member member = memberService.getCurrent();
        Receiver receiver = receiverService.find(receiverId);
        if (receiver == null) {
            receiver = receiverService.findDefault(member);
        }

        Order order = null;
        order = orderService.build(cart, receiver, paymentMethod, shippingMethod, couponCodes, isInvoice, invoiceTitle,
                useBalance, tenantIds, memo);

        data.put("message", SUCCESS_MESSAGE);
        data.put("quantity", order.getQuantity());
        data.put("price", order.getPrice());
        data.put("freight", order.getFreight());
        data.put("promotionDiscount", order.getPromotionDiscount());
        data.put("couponDiscount", order.getCouponDiscount());
        data.put("tax", order.getTax());
        data.put("amountPayable", order.getAmountPayable());
        return data;
    }

    /**
     * 创建
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public
    @ResponseBody
    Message create(String cartToken, Long receiverId, String codeType, Long point,
                   Long paymentMethodId, Long shippingMethodId, String[] code,
                   @RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle,
                   @RequestParam(defaultValue = "false") Boolean useBalance, Long[] tenantIds, String[] memo, Long appointmentId,
                   HttpServletRequest request, HttpServletResponse response) {
        try {
            Cart cart = cartService.getCurrent();
            if (cart == null || cart.isEmpty()) {
                return Message.error("shop.order.cartNotEmpty");
            }
            if (!StringUtils.equals(cart.getToken(), cartToken)) {
                return Message.error("shop.order.cartHasChanged");
            }
            if (cart.getIsLowStock()) {
                return Message.error("shop.order.cartLowStock");
            }
            Receiver receiver = receiverService.find(receiverId);
            if (receiver == null) {
                return Message.error("shop.order.receiverNotExsit");
            }
            PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
            if (paymentMethod == null) {
                return Message.error("shop.order.paymentMethodNotExsit");
            }
            ShippingMethod shippingMethod = shippingMethodService.find(shippingMethodId);
            if (shippingMethod == null) {
                return Message.error("shop.order.shippingMethodNotExsit");
            }
            if (!paymentMethod.getShippingMethods().contains(shippingMethod)) {
                return Message.error("shop.order.deliveryUnsupported");
            }
            Appointment appointment = appointmentService.find(appointmentId);
            // if (paymentMethod.getMethod() == PaymentMethod.Method.offline) {
            // return Message.error("支付方式与配送方式不匹配！");
            // }
            if (paymentMethod.getMethod().equals(PaymentMethod.Method.balance)) {
                useBalance = true;
            }
            if (useBalance) {
                Member member = memberService.getCurrent();
                String password = rsaService.decryptParameter("enPassword", request);
                rsaService.removePrivateKey(request);
                if (member.getPaymentPassword() == null) {
                    return Message.error("支付密码未设置,请前往设置！");
                }
                if (!member.getPaymentPassword().equals(DigestUtils.md5Hex(password))) {
                    return Message.error("支付密码错误！");
                }
            }
            Set<CouponCode> couponCodes = new HashSet<CouponCode>();
            for (String c : code) {
                CouponCode couponCode = couponCodeService.findByCode(c);
                couponCodes.add(couponCode);
            }
            Order order = orderService.create(cart, receiver, paymentMethod, shippingMethod, couponCodes, isInvoice,
                    invoiceTitle, useBalance, tenantIds, memo, null, null, appointment, OrderSource.wap, null, null);
            WebUtils.addCookie(request, response, Cart.CART_COUNT, cart.getQuantity() + "", Cart.TIMEOUT);
//            if (order.getPaymentStatus().equals(PaymentStatus.paid)
//                    || order.getPaymentMethod().getMethod().equals(PaymentMethod.Method.offline)) {
//                authenticodeStrategy.sendNotify(order);
//                orderService.pushTo(order);
//            }
            return Message.success(order.getSn());
        } catch (Exception e) {
            System.out.print(4);
            e.printStackTrace();
            return Message.error("订单提交失败，系统错误");
        }
    }

    /**
     * 支付
     */
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public String payment(String sn, ModelMap model) {
        Order order = orderService.findBySn(sn);
        if (order == null || !memberService.getCurrent().equals(order.getMember()) || order.isExpired()
                || order.getPaymentMethod() == null) {
            return ERROR_VIEW;
        }
        if (order.getPaymentMethod().getMethod() == PaymentMethod.Method.online) {
            List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
            if (!paymentPlugins.isEmpty()) {
                PaymentPlugin defaultPaymentPlugin = paymentPlugins.get(0);
                if (order.getPaymentStatus() == PaymentStatus.unpaid
                        || order.getPaymentStatus() == PaymentStatus.partialPayment) {
                    // model.addAttribute("fee",
                    // defaultPaymentPlugin.calculateFee(order.getAmountPayable()));
                    // model.addAttribute("amount",
                    // defaultPaymentPlugin.calculateAmount(order.getAmountPayable()));
                    model.addAttribute("fee", new BigDecimal(0));
                    model.addAttribute("amount", order.getAmountPayable());
                }
                model.addAttribute("defaultPaymentPlugin", defaultPaymentPlugin);
                model.addAttribute("paymentPlugins", paymentPlugins);
            }
        }
        model.addAttribute("order", order);
        return "tenant/order/payment";
    }

    /**
     * 计算支付金额
     */
    @RequestMapping(value = "/calculate_amount", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> calculateAmount(String paymentPluginId, String sn) {
        Map<String, Object> data = new HashMap<String, Object>();
        Order order = orderService.findBySn(sn);
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (order == null || !memberService.getCurrent().equals(order.getMember()) || order.isExpired()
                || order.getPaymentMethod() == null
                || order.getPaymentMethod().getMethod() == PaymentMethod.Method.offline || paymentPlugin == null
                || !paymentPlugin.getIsEnabled()) {
            data.put("message", ERROR_MESSAGE);
            return data;
        }
        data.put("message", SUCCESS_MESSAGE);
        data.put("fee", new BigDecimal(0));
        data.put("amount", order.getAmountPayable());
        return data;
    }

    /**
     * 订单详情
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String orderView(String type, ModelMap model) {
        Member member = memberService.getCurrent();
        Long unpaid = tradeService.findWaitPayCount(member, false);//待发货
        Long unshiped = tradeService.findWaitShippingCount(member, null);//待支付
        Long shipped = tradeService.findWaitSignCount(member, null);//已发货
        Long reviewed = tradeService.findWaitReviewCount(member, null);//待评价
        model.addAttribute("unpaid", unpaid);
        model.addAttribute("shipped", shipped);
        model.addAttribute("unreview", reviewed);
        model.addAttribute("unshiped", unshiped);
        model.addAttribute("type", type);
        return "/wap/member/order/list";
    }


    /**
     * 加载更多json
     */
    @RequestMapping(value = "/addMore", method = RequestMethod.GET)
    @ResponseBody
    public Page<Trade> addMore(Pageable pageable, String type) {
        Member member = memberService.getCurrent();
        Page<Trade> page = new Page<Trade>();
        if ("unpaid".equals(type)) {
            page = tradeService.findWaitPay(member, false, pageable);
        } else if ("unshipped".equals(type)) {
            page = tradeService.findWaitShipping(member, false, pageable);
        } else if ("shipped".equals(type)) {
            page = tradeService.findWaitSign(member, false, pageable);
        } else if ("unreview".equals(type)) {
            page = tradeService.findWaitReview(member, false, pageable);
        } else {
            page = tradeService.findMemberPage(member, null, null, null, null, pageable);
        }
        return page;
    }

    /**
     * 取消
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public
    @ResponseBody
    Message cancel(String sn) {
        Order order = orderService.findBySn(sn);
        if (order == null) {
            return Message.error("订单不存在");
        }
        Member member = memberService.getCurrent();
        if (!member.equals(order.getMember())) {
            return Message.error("该订单不属于您");
        }
        if (order.isExpired()) {
            return Message.error("订单已经过期");
        }
        if (order.getOrderStatus() == OrderStatus.completed) {
            return Message.error("订单已完成");
        }
        if (order.getOrderStatus() == OrderStatus.cancelled) {
            return Message.error("订单已取消");
        }
        if ((order.getOrderStatus() == OrderStatus.unconfirmed || order.getOrderStatus() == OrderStatus.confirmed)
                && order.getPaymentStatus() == PaymentStatus.unpaid) {
            if (order.isLocked(member)) {
                return Message.warn("shop.member.order.locked");
            }
            orderService.cancel(order, member);
            return SUCCESS_MESSAGE;
        } else {
            return Message.error("订单无法取消");
        }
    }

    /**
     * 物流动态
     */
    @RequestMapping(value = "/delivery_query", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> deliveryQuery(String sn) {
        Map<String, Object> data = new HashMap<String, Object>();
        Shipping shipping = shippingService.findBySn(sn);
        Setting setting = SettingUtils.get();
        if (shipping != null && shipping.getOrder() != null
                && memberService.getCurrent().equals(shipping.getOrder().getMember())
                && StringUtils.isNotEmpty(setting.getKuaidi100Key())
                && StringUtils.isNotEmpty(shipping.getDeliveryCorpCode())
                && StringUtils.isNotEmpty(shipping.getTrackingNo())) {
            data = shippingService.query(shipping);
        }
        return data;
    }

//    /**
//     * 买家-确认
//     */
//    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
//    public
//    @ResponseBody
//    Message confirm(String sn) {
//        Member member = memberService.getCurrent();
//        try {
//            Order order = orderService.findBySn(sn);
//            if (order != null && member.equals(order.getMember()) && !order.isExpired()
//                    && order.getIsCompleteAllowed()) {
//                if (order.isLocked(member)) {
//                    return Message.warn("b2c.member.order.locked");
//                }
//                orderService.complete(order, memberService.getCurrent());
//                return SUCCESS_MESSAGE;
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return ERROR_MESSAGE;
//    }
//
    /**
     * 获取收货地址
     */
    @RequestMapping(value = "/receivers", method = RequestMethod.GET)
    public String receivers(Model model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        String[] phonetics = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        model.addAttribute("receivers", member.getReceivers());
        model.addAttribute("phonetics", phonetics);
        model.addAttribute("areas", areaService.findRoots());
        return "wap/member/order/receivers";
    }

    /**
     * 提交
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submit(Type type, String paymentPluginId, String sn, BigDecimal amount,
                                      HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (paymentPluginId == null || paymentPluginId == "") {
            paymentPluginId = "weixinPayPlugin";
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            data.put("message", Message.error("您没有登录"));
            return data;
        }
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
        if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
            data.put("message", Message.error("支付插件无效"));
            return data;
        }
        Payment payment = new Payment();
        Order order = orderService.findBySn(sn);
        if (type == Type.payment) {
            if (order == null || !member.equals(order.getMember()) || order.isExpired() || order.isLocked(member)) {
                data.put("message", Message.error("IsExpired"));
                return data;
            }
            if (Order.OrderStatus.cancelled.equals(order.getOrderStatus())) {
                data.put("message", Message.error("订单已取消，无法支付"));
                return data;
            }
            if (order.getPaymentMethod() == null
                    || order.getPaymentMethod().getMethod() != PaymentMethod.Method.online) {
                data.put("message", Message.error("货到付款,请准备好货款."));
                return data;
            }
            if (order.getPaymentStatus() != PaymentStatus.unpaid
                    && order.getPaymentStatus() != PaymentStatus.partialPayment) {
                data.put("message", Message.error("not.status"));
                return data;
            }
            if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
                data.put("message", Message.error("not.payable"));
                return data;
            }
            payment.setMember(order.getMember());
            payment.setPayer(order.getMember().getName());
            payment.setMemo("微信购物");
            payment.setSn(snService.generate(Sn.Type.payment));
            payment.setType(Type.payment);
            payment.setMethod(Method.online);
            payment.setStatus(Status.wait);
            payment.setPaymentMethod(
                    order.getPaymentMethodName() + Payment.PAYMENT_METHOD_SEPARATOR + paymentPlugin.getPaymentName());
            // payment.setFee(new BigDecimal(0));
            payment.setFee(paymentPlugin.calculateFee(order.getAmountPayable()));
            payment.setAmount(order.getAmountPayable());
            // payment.setAmount(paymentPlugin.calculateAmount(order.getAmountPayable()));
            payment.setPaymentPluginId(paymentPluginId);
            payment.setExpire(paymentPlugin.getTimeout() != null
                    ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
            payment.setOrder(order);
            paymentService.save(payment);
        } else if (type == Type.recharge) {
            Setting setting = SettingUtils.get();
            if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15
                    || amount.scale() > setting.getPriceScale()) {
                data.put("message", Message.error("amount.error"));
                return data;
            }
            payment.setMember(member);
            payment.setPayer(member.getName());
            payment.setMemo("微信充值");
            payment.setSn(snService.generate(Sn.Type.payment));
            payment.setType(Type.recharge);
            payment.setMethod(Method.online);
            payment.setStatus(Status.wait);
            payment.setPaymentMethod(paymentPlugin.getPaymentName());
            payment.setFee(paymentPlugin.calculateFee(amount.setScale(2)));
            payment.setAmount(amount.setScale(2));
            payment.setPaymentPluginId(paymentPluginId);
            payment.setExpire(paymentPlugin.getTimeout() != null
                    ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
            payment.setMember(member);
            paymentService.save(payment);
        } else {
            data.put("message", Message.error("amount.error"));
            return data;
        }
        try {
            String parameters = JSONObject
                    .fromObject(paymentPlugin.getParameterMap(payment.getSn(), "rzico", request, "/wap")).toString();
            data.put("sn", payment.getSn());
            data.put("dataValue", parameters);
            data.put("message", SUCCESS_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("message", Message.error("签名失败！"));
        }
        return data;
    }

    @RequestMapping(value = "/check_balance", method = RequestMethod.POST)
    @ResponseBody
    public Message check_balance(Long paymentMethodId, Long shippingMethodId, String codeType, Long receiverId,
                                 Long[] couponCodeId, Long point, @RequestParam(defaultValue = "false") Boolean isInvoice, String invoiceTitle,
                                 @RequestParam(defaultValue = "false") Boolean useBalance, Long[] tenantIds, String[] memo) {
        Map<String, Object> map = calculate(paymentMethodId, shippingMethodId, codeType, receiverId, couponCodeId,
                point, isInvoice, invoiceTitle, useBalance, tenantIds, memo);

        BigDecimal amountPayable = (BigDecimal) map.get("amountPayable");
        Member member = memberService.getCurrent();
        if (member.getBalance().compareTo(amountPayable) < 0) {
            return Message.error("账户余额不足。");
        }
        return SUCCESS_MESSAGE;
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/tradeView", method = RequestMethod.GET)
    public String tradeView(String sn, ModelMap model) {
        Trade trade = tradeService.findBySn(sn);
        if (trade == null) {
            return ERROR_VIEW;
        }
        model.addAttribute("trade", trade);
        return "/wap/member/trade/view";
    }

    /**
     * 充值
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public String recharge(ModelMap model) {
        List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        if (!paymentPlugins.isEmpty()) {
            model.addAttribute("defaultPaymentPlugin", paymentPlugins.get(3));
            model.addAttribute("paymentPlugins", paymentPlugins);
        }
        return "/wap/member/deposit/recharge";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String payment(BigDecimal amount, ModelMap model) {
        model.addAttribute("amount", amount);
        return "wap/member/deposit/pay";
    }
}