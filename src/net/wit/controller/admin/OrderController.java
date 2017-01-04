/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.service.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 订单
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "orderItemServiceImpl")
    private OrderItemService orderItemService;

    @Resource(name = "shippingMethodServiceImpl")
    private ShippingMethodService shippingMethodService;

    @Resource(name = "deliveryCorpServiceImpl")
    private DeliveryCorpService deliveryCorpService;

    @Resource(name = "paymentMethodServiceImpl")
    private PaymentMethodService paymentMethodService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "rebateServiceImpl")
    private RebateService rebateService;

    /**
     * 检查锁定
     */
    @RequestMapping(value = "/check_lock", method = RequestMethod.POST)
    public
    @ResponseBody
    Message checkLock(Long id) {
        Order order = orderService.find(id);
        if (order == null) {
            return Message.warn("admin.common.invalid");
        }
        if (order.isLocked(null)) {
            if (order.getOperator() != null) {
                return Message.warn("admin.order.adminLocked", order.getOperator().getUsername());
            } else {
                return Message.warn("admin.order.memberLocked");
            }
        } else {
            order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
            order.setOperator(null);
            orderService.update(order);
            return SUCCESS_MESSAGE;
        }
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        model.addAttribute("methods", Payment.Method.values());
        model.addAttribute("refundsMethods", Refunds.Method.values());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        model.addAttribute("trade", tradeService.find(id));
        Set<Role> roles = adminService.getCurrent().getRoles();
        String ro = "0";
        for (Role role : roles) {
            List<String> authorities = role.getAuthorities();
            for (String authoritie : authorities) {
                if ("admin:orderPayment".equals(authoritie)) {
                    ro = "1";
                }
            }
        }
        model.addAttribute("role", ro);
        return "/admin/order/view";
    }


    /**
     * 查看
     */
    @RequestMapping(value = "/snview", method = RequestMethod.GET)
    public String snview(String sn, ModelMap model) {
        model.addAttribute("methods", Payment.Method.values());
        model.addAttribute("refundsMethods", Refunds.Method.values());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        model.addAttribute("order", orderService.findBySn(sn));
        return "/admin/order/view";
    }

    /**
     * 确认
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public String confirm(Long id, RedirectAttributes redirectAttributes) {
        Order order = orderService.find(id);
        if (order != null && !order.isExpired() && order.getOrderStatus() == OrderStatus.unconfirmed && !order.isLocked(null)) {
            orderService.confirm(order, null);
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        } else {
            addFlashMessage(redirectAttributes, Message.warn("admin.common.invalid"));
        }
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 完成
     */
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public String complete(Long id, RedirectAttributes redirectAttributes) {
        Order order = orderService.find(id);
        //if (order != null && !order.isExpired() && order.getOrderStatus() == OrderStatus.confirmed && !order.isLocked(null)) {
        //    orderService.complete(order, null);
        //    addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        //} else {
        //    addFlashMessage(redirectAttributes, Message.warn("admin.common.invalid"));
        //}
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 取消
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(Long id, RedirectAttributes redirectAttributes) {
        Order order = orderService.find(id);
        if (order != null && !order.isExpired() && order.getOrderStatus() == OrderStatus.unconfirmed && !order.isLocked(null)) {
            orderService.cancel(order, null);
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        } else {
            addFlashMessage(redirectAttributes, Message.warn("admin.common.invalid"));
        }
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 支付
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public String payment(Long orderId, Long paymentMethodId, Payment payment, RedirectAttributes redirectAttributes) {
        Order order = orderService.find(orderId);
        payment.setOrder(order);
        PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod.getName() : null);
        if (!isValid(payment)) {
            return ERROR_VIEW;
        }
        if (order.isExpired() || order.getOrderStatus() != OrderStatus.confirmed) {
            return ERROR_VIEW;
        }
        if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
            return ERROR_VIEW;
        }
        if (payment.getAmount().compareTo(new BigDecimal(0)) <= 0 || payment.getAmount().compareTo(order.getAmountPayable()) > 0) {
            return ERROR_VIEW;
        }
        Member member = order.getMember();
        if (payment.getMethod() == Payment.Method.deposit && payment.getAmount().compareTo(member.getBalance()) > 0) {
            return ERROR_VIEW;
        }
        Admin admin = adminService.getCurrent();
        if (order.isLocked(null)) {
            return ERROR_VIEW;
        }
        payment.setClearAmount(BigDecimal.ZERO);
        payment.setSn(snService.generate(Sn.Type.payment));
        payment.setType(Type.payment);
        payment.setStatus(Status.success);
        payment.setFee(new BigDecimal(0));
        payment.setOperator(admin.getUsername());
        payment.setPaymentDate(new Date());
        payment.setPaymentPluginId(null);
        payment.setExpire(null);
        payment.setDeposit(null);
        payment.setMember(null);
        orderService.payment(order, payment, null);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:view.jhtml?id=" + orderId;
    }


    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("order", orderService.find(id));
        return "/admin/order/edit";
    }

    /**
     * 订单项添加
     */
    @RequestMapping(value = "/order_item_add", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> orderItemAdd(String productSn) {
        Map<String, Object> data = new HashMap<String, Object>();
        Product product = productService.findBySn(productSn);
        if (product == null) {
            data.put("message", Message.warn("admin.order.productNotExist"));
            return data;
        }
        if (!product.getIsMarketable()) {
            data.put("message", Message.warn("admin.order.productNotMarketable"));
            return data;
        }
        if (product.getIsOutOfStock()) {
            data.put("message", Message.warn("admin.order.productOutOfStock"));
            return data;
        }
        data.put("sn", product.getSn());
        data.put("fullName", product.getFullName());
        data.put("price", product.getPrice());
        data.put("weight", product.getWeight());
        data.put("isGift", product.getIsGift());
        data.put("message", SUCCESS_MESSAGE);
        return data;
    }

    /**
     * 计算
     */
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> calculate(Order order, Long areaId, Long paymentMethodId, Long shippingMethodId) {
        Map<String, Object> data = new HashMap<String, Object>();
        for (Iterator<OrderItem> iterator = order.getOrderItems().iterator(); iterator.hasNext(); ) {
            OrderItem orderItem = iterator.next();
            if (orderItem == null || StringUtils.isEmpty(orderItem.getSn())) {
                iterator.remove();
            }
        }
        order.setArea(areaService.find(areaId));
        order.setPaymentMethod(paymentMethodService.find(paymentMethodId));
        order.setShippingMethod(shippingMethodService.find(shippingMethodId));
        if (!isValid(order)) {
            data.put("message", Message.warn("admin.common.invalid"));
            return data;
        }
        Order pOrder = orderService.find(order.getId());
        if (pOrder == null) {
            data.put("message", Message.error("admin.common.invalid"));
            return data;
        }
        Boolean IsAllocatedStock = false;
        for (Trade trade : pOrder.getTrades()) {
            if (trade.getIsAllocatedStock()) {
                IsAllocatedStock = true;
            }
        }
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getId() != null) {
                OrderItem pOrderItem = orderItemService.find(orderItem.getId());
                if (pOrderItem == null || !pOrder.equals(pOrderItem.getOrder())) {
                    data.put("message", Message.error("admin.common.invalid"));
                    return data;
                }
                Product product = pOrderItem.getProduct();
                if (product != null && product.getStock() != null) {
                    if (IsAllocatedStock) {
                        if (orderItem.calculateQuantityIntValue() > product.getAvailableStock() + pOrderItem.calculateQuantityIntValue()) {
                            data.put("message", Message.warn("admin.order.lowStock"));
                            return data;
                        }
                    } else {
                        if (orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
                            data.put("message", Message.warn("admin.order.lowStock"));
                            return data;
                        }
                    }
                }
            } else {
                Product product = productService.findBySn(orderItem.getSn());
                if (product == null) {
                    data.put("message", Message.error("admin.common.invalid"));
                    return data;
                }
                if (product.getStock() != null && orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
                    data.put("message", Message.warn("admin.order.lowStock"));
                    return data;
                }
            }
        }
        Map<String, Object> orderItems = new HashMap<String, Object>();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItems.put(orderItem.getSn(), orderItem);
        }
        order.setFee(pOrder.getFee());
        order.setPromotionDiscount(pOrder.getPromotionDiscount());
        order.setCouponDiscount(pOrder.getCouponDiscount());
        order.setAmountPaid(pOrder.getAmountPaid());
        data.put("weight", order.getWeight());
        data.put("price", order.getPrice());
        data.put("quantity", order.getQuantity());
        data.put("amount", order.getAmount());
        data.put("orderItems", orderItems);
        data.put("message", SUCCESS_MESSAGE);
        return data;
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Order order, Long areaId, Long paymentMethodId, Long shippingMethodId, RedirectAttributes redirectAttributes) {
        for (Iterator<OrderItem> iterator = order.getOrderItems().iterator(); iterator.hasNext(); ) {
            OrderItem orderItem = iterator.next();
            if (orderItem == null || StringUtils.isEmpty(orderItem.getSn())) {
                iterator.remove();
            }
        }
        order.setArea(areaService.find(areaId));
        order.setPaymentMethod(paymentMethodService.find(paymentMethodId));
        order.setShippingMethod(shippingMethodService.find(shippingMethodId));
        if (!isValid(order)) {
            return ERROR_VIEW;
        }
        Order pOrder = orderService.find(order.getId());

        if (pOrder == null) {
            return ERROR_VIEW;
        }
        if (pOrder.isExpired() || pOrder.getOrderStatus() != OrderStatus.unconfirmed) {
            return ERROR_VIEW;
        }

        Boolean IsAllocatedStock = false;
        for (Trade trade : pOrder.getTrades()) {
            if (trade.getIsAllocatedStock()) {
                IsAllocatedStock = true;
            }
        }
        Admin admin = adminService.getCurrent();
        if (pOrder.isLocked(null)) {
            return ERROR_VIEW;
        }
        if (!order.getIsInvoice()) {
            order.setInvoiceTitle(null);
            order.setTax(new BigDecimal(0));
        }
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getId() != null) {
                OrderItem pOrderItem = orderItemService.find(orderItem.getId());
                if (pOrderItem == null || !pOrder.equals(pOrderItem.getOrder())) {
                    return ERROR_VIEW;
                }
                Product product = pOrderItem.getProduct();
                if (product != null && product.getStock() != null) {
                    if (IsAllocatedStock) {
                        if (orderItem.calculateQuantityIntValue() > product.getAvailableStock() + pOrderItem.calculateQuantityIntValue()) {
                            return ERROR_VIEW;
                        }
                    } else {
                        if (orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
                            return ERROR_VIEW;
                        }
                    }
                }
                BeanUtils.copyProperties(pOrderItem, orderItem, new String[]{"price", "quantity"});
                if (pOrderItem.getIsGift()) {
                    orderItem.setPrice(new BigDecimal(0));
                }
            } else {
                Product product = productService.findBySn(orderItem.getSn());
                if (product == null) {
                    return ERROR_VIEW;
                }
                if (product.getStock() != null && orderItem.calculateQuantityIntValue() > product.getAvailableStock()) {
                    return ERROR_VIEW;
                }
                orderItem.setName(product.getName());
                orderItem.setFullName(product.getFullName());
                if (product.getIsGift()) {
                    orderItem.setPrice(new BigDecimal(0));
                }
                orderItem.setWeight(product.getWeight());
                orderItem.setThumbnail(product.getThumbnail());
                orderItem.setIsGift(product.getIsGift());
                orderItem.setShippedQuantity(0);
                orderItem.setReturnQuantity(0);
                orderItem.setProduct(product);
                orderItem.setOrder(pOrder);
            }
        }
        order.setSn(pOrder.getSn());
        order.setOrderStatus(pOrder.getOrderStatus());
        order.setPaymentStatus(pOrder.getPaymentStatus());
        order.setShippingStatus(pOrder.getShippingStatus());
        order.setFee(pOrder.getFee());
        order.setPromotionDiscount(pOrder.getPromotionDiscount());
        order.setCouponDiscount(pOrder.getCouponDiscount());
        order.setAmountPaid(pOrder.getAmountPaid());
        order.setPromotion(pOrder.getPromotion());
        order.setExpire(pOrder.getExpire());
        order.setLockExpire(null);
        // order.setIsAllocatedStock(pOrder.getIsAllocatedStock());
        order.setOperator(null);
        order.setMember(pOrder.getMember());
        order.setCouponCode(pOrder.getCouponCode());
        order.setCoupons(pOrder.getCoupons());
        order.setOrderLogs(pOrder.getOrderLogs());
        order.setDeposits(pOrder.getDeposits());
        order.setPayments(pOrder.getPayments());
        order.setRefunds(pOrder.getRefunds());
        order.setShippings(pOrder.getShippings());
        order.setReturns(pOrder.getReturns());

        orderService.update(order, admin.getUsername());
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String consignee,Date beginDate, Date endDate, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Long areaId, Boolean hasExpired, Pageable pageable,String tenantName,String userName, ModelMap model) {
//			if(beginDate==null){
//				beginDate=new Date();
//				Long time=beginDate.getTime();
//				Long begin=time-24*60*60*1000*7;
//				beginDate=new Date(begin);
//			}
//			if(endDate!=null){
//				Long time=endDate.getTime();
//				Long end=time+24*60*60*1000-1;
//				endDate=new Date(end);
//			}
        if(beginDate!=null&&endDate!=null){
                Long time=endDate.getTime();
				Long end=time+24*60*60*1000-1;
				endDate=new Date(end);
        }
        Admin admin=adminService.getCurrent();
        Area area = null;
        if(admin!=null&&admin.getId()==1l){
            area=areaService.find(areaId);
        }else{
            if(admin!=null && admin.getEnterprise()!=null){
                area = admin.getEnterprise().getArea();
            }
        }
        Page page = tradeService.findPage(consignee, area, orderStatus, paymentStatus, shippingStatus, hasExpired, beginDate, endDate,pageable.getSearchValue(), tenantName, userName, pageable);
        model.addAttribute("page", page);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("paymentStatus", paymentStatus);
        model.addAttribute("shippingStatus", shippingStatus);
        model.addAttribute("hasExpired", hasExpired);
        model.addAttribute("area", areaService.find(areaId));
        model.addAttribute("consignee", consignee);
        model.addAttribute("tenantName", tenantName);
        model.addAttribute("userName", userName);
        return "/admin/order/list";
    }

    /**
     *订单导出
     */
    @RequestMapping(value = "/order_export", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String , Object >> orderExport(String consignee,String orderId,Date beginDate,Date endDate, OrderStatus orderStatus, PaymentStatus paymentStatus, ShippingStatus shippingStatus, Long areaId, Boolean hasExpired,Pageable pageable,String tenantName,String userName, ModelMap model) {
        List<Map<String,Object>> maps=new ArrayList<Map<String , Object >>();
        if(beginDate!=null&&endDate!=null){
            Long time=endDate.getTime();
            Long end=time+24*60*60*1000-1;
            endDate=new Date(end);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Admin admin=adminService.getCurrent();
        Area area = null;
        if(admin!=null&&admin.getId()==1l){
            area=areaService.find(areaId);
        }else{
            if(admin!=null && admin.getEnterprise()!=null){
                area = admin.getEnterprise().getArea();
            }
        }
        //最大可导出数
        pageable.setPageSize(1000);
        Page page = tradeService.findPage(consignee, area, orderStatus, paymentStatus, shippingStatus, hasExpired, beginDate, endDate,orderId,tenantName,userName, pageable);
        List<Trade> trades=page.getContent();
        if(trades!=null){
            for(Trade trade:trades){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("sn",trade.getOrder().getSn());
                map.put("amount",trade.getAmount());
                Member m = trade.getOrder().getMember();
                map.put("username",trade.getOrder().getMember()==null?"该用户账号不存在":trade.getOrder().getMember().getUsername());
                map.put("consignee",trade.getOrder().getConsignee());
                map.put("tenant",trade.getTenant().getName());
                map.put("paymentMethodName",trade.getOrder().getPaymentMethodName());
                map.put("shippingMethodName",trade.getOrder().getShippingMethodName());
                if(trade.getOrderStatus()==OrderStatus.unconfirmed){
                    map.put("orderStatus", "未确认");
                }else if(trade.getOrderStatus()==OrderStatus.confirmed){
                    map.put("orderStatus", "已确认");
                }else if(trade.getOrderStatus()==OrderStatus.completed){
                    map.put("orderStatus", "已完成");
                }else if(trade.getOrderStatus()==OrderStatus.cancelled){
                    map.put("orderStatus", "已取消");
                    if(trade.getOrder().isExpired()){
                        map.put("orderStatus", "已取消(已过期)");
                    }
                }
                if(trade.getPaymentStatus()==PaymentStatus.paid){
                    map.put("paymentStatus", "已支付");
                }else if(trade.getPaymentStatus()==PaymentStatus.unpaid){
                    map.put("paymentStatus", "已支付");
                }else if(trade.getPaymentStatus()==PaymentStatus.refunded){
                    map.put("paymentStatus", "已退款");
                }else if(trade.getPaymentStatus()==PaymentStatus.refundApply){
                    map.put("paymentStatus", "退款中");
                }
                if(trade.getShippingStatus()==ShippingStatus.accept){
                    map.put("shippingStatus", "已签收");
                }else if(trade.getShippingStatus()==ShippingStatus.returned){
                    map.put("shippingStatus", "已退货");
                }else if(trade.getShippingStatus()==ShippingStatus.shipped){
                    map.put("shippingStatus", "已发货");
                }else if(trade.getShippingStatus()==ShippingStatus.shippedApply){
                    map.put("shippingStatus", "退货中");
                }else if(trade.getShippingStatus()==ShippingStatus.unshipped){
                    map.put("shippingStatus", "未发货");
                }
                map.put("createDate",sdf.format(trade.getOrder().getCreateDate()));
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                Order order = orderService.find(id);
                if (order != null && order.isLocked(null)) {
                    return Message.error("admin.order.deleteLockedNotAllowed", order.getSn());
                }
            }
            orderService.delete(ids);
        }
        return SUCCESS_MESSAGE;
    }

}