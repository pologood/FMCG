/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.DataBlock;
import net.wit.dao.AdminDao;
import net.wit.entity.*;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.QueryStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.entity.PaymentMethod.Method;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.*;
import net.wit.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 订单
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeMemberTradeController")
@RequestMapping("/store/member/trade")
public class TradeController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "shippingMethodServiceImpl")
    private ShippingMethodService shippingMethodService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "deliveryCorpServiceImpl")
    private DeliveryCorpService deliveryCorpService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "adminDaoImpl")
    private AdminDao adminDao;

    @Resource(name = "orderItemServiceImpl")
    private OrderItemService orderItemService;

    @Resource(name = "paymentMethodServiceImpl")
    private PaymentMethodService paymentMethodService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "spReturnsServiceImpl")
    private SpReturnsService spReturnsService;

    @Resource(name = "accountServiceImpl")
    private AccountService accountService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;
    
    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String keyword, String type,
                       Pageable pageable, ModelMap model,
                       RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();

        QueryStatus queryStatus = null;
        if ("unshipped".equals(type)) {
            queryStatus = QueryStatus.unshipped;
        } else if ("unpaid".equals(type)) {
            queryStatus = QueryStatus.unpaid;
        } else if ("shipped".equals(type)) {
            queryStatus = QueryStatus.shipped;
        } else if ("unreturned".equals(type)) {
            queryStatus = QueryStatus.unrefunded;
        } else if ("completed".equals(type)) {
            queryStatus = QueryStatus.completed;
        } else if ("cancelled".equals(type)) {
            queryStatus = QueryStatus.cancelled;
        } else {
            queryStatus = QueryStatus.unshipped;
        }

        model.addAttribute("member", member);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        try {
            model.addAttribute("page", tradeService.findPage(pageable, member.getTenant(), queryStatus, keyword));
            model.addAttribute("unshippedCount", tradeService.count(null, member.getTenant(), QueryStatus.unshipped, null));
            model.addAttribute("shippedCount", tradeService.count(null, member.getTenant(), QueryStatus.shipped, null));
            model.addAttribute("shippedApplyCount", tradeService.count(null, member.getTenant(), QueryStatus.unrefunded, null));
            model.addAttribute("completedCount", tradeService.count(null, member.getTenant(), QueryStatus.completed, null));
            model.addAttribute("cancelledCount", tradeService.count(null, member.getTenant(), QueryStatus.cancelled, null));
            model.addAttribute("unpaidCount", tradeService.count(null, member.getTenant(), QueryStatus.unpaid, null));
            model.addAttribute("shippingMethods", shippingMethodService.findAll());
            model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("pageActive", 2);
        model.addAttribute("menu","order_management");
        return "/store/member/trade/list";
    }
    
    @RequestMapping(value = "/favorable_list", method = RequestMethod.GET)
    public String favorableList(String keyword, String type,Long couponId,
                       Pageable pageable, ModelMap model,
                       RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Tenant tenant=member.getTenant();
        Coupon coupon=couponService.find(couponId);
        Page<Trade> page=tradeService.findFavorableList(tenant,keyword,coupon,pageable);
        model.addAttribute("page",page);
        model.addAttribute("area",areaService.getCurrent());
        model.addAttribute("keywords",keyword);
        return "/store/member/trade/favorable_list";
    }

    @RequestMapping(value = "/viewForSn", method = RequestMethod.GET)
    public String viewForSn(Long id, ModelMap model) {
        Member member = memberService.getCurrent();
        Trade trade = tradeService.find(id);
        model.addAttribute("member", member);
        model.addAttribute("methods", Payment.Method.values());
        model.addAttribute("refundsMethods", Refunds.Method.values());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        model.addAttribute("trade", trade);
        model.addAttribute("isReturnAllowed", !trade.isSpReturns());
        model.addAttribute("pageActive", 2);
        model.addAttribute("menu","order_management");
        model.addAttribute("hideshow","show");
        return "/store/member/trade/view";
    }
    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        Member member = memberService.getCurrent();
        Trade trade = tradeService.find(id);
        model.addAttribute("member", member);
        model.addAttribute("methods", Payment.Method.values());
        model.addAttribute("refundsMethods", Refunds.Method.values());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        model.addAttribute("trade", trade);
        model.addAttribute("isReturnAllowed", !trade.isSpReturns());
        model.addAttribute("pageActive", 2);
        model.addAttribute("menu","order_management");
        model.addAttribute("hideshow","hide");
        return "/store/member/trade/view";
    }

    /**
     * 打印
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String print(Long id, ModelMap model) {
        Trade trade = tradeService.find(id);

//        trade.setPrintDate(new Date());
//
//        Integer iii=1;
//        if(trade.getPrint()!=null){
//             iii=trade.getPrint()+iii;
//        }
//
//        trade.setPrint(iii);

        tradeService.update(trade);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<Object> list = new ArrayList<>();
        int len = trade.getOrderItems().size();
        int count = len/5;

        //够4000的一组处理
        for(int j = 0 ; j < count ; j++){
            List<Map<String,Object>> lists = new ArrayList<>();
            for(int m = 0 ; m<5;m++){
                Map<String,Object> map1 =new HashMap<>();

                map1.put("packagUnitName",trade.getOrderItems().get(m+j*5).getPackagUnitName());
                map1.put("fullName",trade.getOrderItems().get(m+j*5).getFullName());
                map1.put("isGift",trade.getOrderItems().get(m+j*5).getIsGift());
                map1.put("quantity",trade.getOrderItems().get(m+j*5).getQuantity());
                map1.put("price",trade.getOrderItems().get(m+j*5).getPrice());
                if(trade.getOrderItems().get(m+j*5).getProduct()!=null){
                	map1.put("time", sdf.format(trade.getOrderItems().get(m+j*5).getProduct().getCreateDate()));
                	map1.put("barcode",trade.getOrderItems().get(m+j*5).getBarcode());
                }else{
                	map1.put("time", "--");
                	map1.put("barcode","--");
                }
                lists.add(map1);
            }

            if(lists.size()>0){
                list.add(lists);
            }
        }
        //剩下的不够4000的处理
        List<Map<String,Object>> lists1 = new ArrayList<>();
        for(int m = 0 ; m<len%5;m++){
            Map<String,Object> map2 =new HashMap<>();
            map2.put("packagUnitName",trade.getOrderItems().get(m+count*5).getPackagUnitName());
            map2.put("fullName",trade.getOrderItems().get(m+count*5).getFullName());
            map2.put("isGift",trade.getOrderItems().get(m+count*5).getIsGift());
            map2.put("quantity",trade.getOrderItems().get(m+count*5).getQuantity());
            map2.put("price",trade.getOrderItems().get(m+count*5).getPrice());
            if(trade.getOrderItems().get(m+count*5).getProduct()!=null){
            	map2.put("time", sdf.format(trade.getOrderItems().get(m+count*5).getProduct().getCreateDate()));
            	map2.put("barcode",trade.getOrderItems().get(m+count*5).getBarcode());
            }else{
            	map2.put("time", "--");
            	map2.put("barcode","--");
            }
            lists1.add(map2);
        }
        if(lists1.size()>0){
            list.add(lists1);
        }


        model.addAttribute("orders", list);
        model.addAttribute("trade", trade);
        model.addAttribute("menu","order_management");
        return "/store/member/trade/print";
    }

    @RequestMapping(value = "/print/times/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Message printTimes(@PathVariable Long id) {
        Trade trade = tradeService.find(id);

        if(trade==null){
            return Message.error("success");
        }
        trade.setPrintDate(new Date());

        Integer iii=1;
        if(trade.getPrint()!=null){
            iii=trade.getPrint()+iii;
        }

        trade.setPrint(iii);

        tradeService.update(trade);
        return Message.success("success");
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {

        Member member = memberService.getCurrent();
        model.addAttribute("member", member);

        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        Order order = orderService.find(id);
        model.addAttribute("order", order);
        model.addAttribute("trade",
                order.getTrade(memberService.getCurrent().getTenant()));
        model.addAttribute("pageActive", 2);
        return "/store/member/trade/edit";
    }

    /**
     * 检查锁定
     */
    @RequestMapping(value = "/check_lock", method = RequestMethod.POST)
    public
    @ResponseBody
    Message checkLock(Long id) {
        Trade trade = tradeService.find(id);
        if (trade == null) {
            return Message.warn("admin.common.invalid");
        }
        Member member = memberService.getCurrent();
        Order order = trade.getOrder();
        if (order == null) {
            return Message.warn("admin.common.invalid");
        }
        if (order != null && !order.isLocked(member)) {
            order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
            order.setOperator(member);
            orderService.update(order);
            return SUCCESS_MESSAGE;
        }
        return ERROR_MESSAGE;
    }

    /**
     * 调价
     */
    @RequestMapping(value = "/update_price", method = RequestMethod.POST)
    public
    @ResponseBody
    Message updatePrice(Long tradeId, BigDecimal amount, BigDecimal freight,
                        RedirectAttributes redirectAttributes) {
        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();
        if (!trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
            return Message.error("只能对末确认的订单进行调价");
        }
        Member member = memberService.getCurrent();
        if (order.isLocked(member)) {
            return Message.error("已经锁定,此订单正在处理中");
        }
        orderService.updatePrice(trade, amount, freight, member.getUsername());
        //System.out.println("调整金额："+order.getOffsetAmount());

        if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(30L))) {
            activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(30L));
        }

        return SUCCESS_MESSAGE;
    }

    //确认订单
    @RequestMapping(value="/confirm_order",method = RequestMethod.POST)
    public
    @ResponseBody
    Message confirmOrder(long tradeId,Date deliveryDate){
        Member member = memberService.getCurrent();
        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();
        if (order.isLocked(member)) {
            return Message.error("已经锁定,此订单正在处理中");
        }
        trade.setDeliveryDate(deliveryDate);
        tradeService.confirmOrder(trade,order);
        return SUCCESS_MESSAGE;
    }

    //核销提货码
    @RequestMapping(value="/cancel_verification",method = RequestMethod.POST)
    public
    @ResponseBody
    Message cancel_verification(String sn){
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        Trade trade = tradeService.findBySn(sn);
        Tenant tenant2 = trade.getTenant();
        if(tenant != tenant2 ){
            return Message.error("不是本店商品不能提货");
        }
        Trade tradeSn = tradeService.findBySn(sn);
        if(trade != tradeSn ){
            return Message.error("此提货码与订单不匹配");
        }
        if (trade==null) {
            return Message.error("无效的id");
        }
        Order order = trade.getOrder();
        if (order.isLocked(member)) {
            return Message.error("已经锁定,此订单正在处理中");
        }
        if (OrderStatus.cancelled == order.getOrderStatus()) {
            return Message.error("订单已经取消!");
        }
        if (OrderStatus.unconfirmed == order.getOrderStatus()) {
            return Message.error("请商家先确认订单!");
        }
        if (OrderStatus.completed == trade.getOrderStatus()) {
            return Message.error("提交订单已经核销!");
        }
        if (sn != null) {
            if (!sn.equals(trade.getSn())) {
                return Message.error("提货码无效");
            }
        }

        Shipping shipping = new Shipping();

        for (Iterator<OrderItem> iterator = trade.getOrderItems().iterator(); iterator.hasNext(); ) {
            OrderItem orderItem = iterator.next();
            ShippingItem shippingItem = new ShippingItem();
            shippingItem.setQuantity(orderItem.getQuantity() - orderItem.getShippedQuantity());
            shippingItem.setSn(orderItem.getSn());
            shippingItem.setName(orderItem.getFullName());
            shippingItem.setShipping(shipping);
            shipping.getShippingItems().add(shippingItem);
        }
        shipping.setTrade(trade);
        shipping.setOrder(order);
        ShippingMethod shippingMethod = trade.getOrder().getShippingMethod();
        shipping.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
        DeliveryCenter deliveryCenter = deliveryCenterService.findDefault(member.getTenant());

        shipping.setDeliveryCenter(deliveryCenter);

        shipping.setDeliveryCorp("提货码发货");
        shipping.setDeliveryCorpUrl(null);
        shipping.setDeliveryCorpCode(null);

        Area area = order.getArea();
        shipping.setArea(area != null ? area.getFullName() : null);
        shipping.setAddress(order.getAddress());
        shipping.setPhone(order.getPhone());
        shipping.setZipCode(order.getZipCode());
        shipping.setConsignee(order.getConsignee());
        shipping.setPickUpTime(new Date());
        shipping.setTrackingNo(null);

        shipping.setSn(snService.generate(Sn.Type.shipping));
        shipping.setOperator("pos");
        orderService.shipping(order, shipping, null);

        orderService.sign(order, trade, member);

        return Message.success("success", "执行成功");
    }

    /**
     * 退货调价
     */
    @RequestMapping(value = "/update_return_price", method = RequestMethod.POST)
    public
    @ResponseBody
    Message updatePric(Long returnId, BigDecimal amount, BigDecimal cost,
                       RedirectAttributes redirectAttributes) {
        SpReturns spReturns = spReturnsService.find(returnId);
        Member member = memberService.getCurrent();
        Order order = spReturns.getTrade().getOrder();
        if (order.isLocked(member)) {
            return Message.error("已经锁定,此订单正在处理中");
        }
        if (spReturns == null) {
            return Message.error("退货单不存在");
        }
        if (!spReturns.getReturnStatus().equals(ReturnStatus.unconfirmed)) {
            return Message.error("只能对末确认的退货单进行调价");
        }
        if (amount.compareTo(spReturns.getTrade().getAmount()) > 0 || amount.compareTo(BigDecimal.ZERO) < 0) {
            return Message.error("退货金额不能大于订单金额");
        }
        if (cost.compareTo(spReturns.getTrade().getSettle()) > 0 || cost.compareTo(BigDecimal.ZERO) < 0) {
            return Message.error("退货结算金额不能大于订单结算金额");
        }
        spReturns.setAmount(amount);
        spReturns.setCost(cost);
        spReturnsService.update(spReturns);
        return Message.success("调价成功");
    }

    /**
     * 支付
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public String payment(Long id, Long paymentMethodId, Payment payment,
                          RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Admin admin = adminDao.findByUsername("admin");
        Trade trade = tradeService.find(id);
        Order order = trade.getOrder();
        if (order.getOrderType() != Order.OrderType.single) {
            addFlashMessage(redirectAttributes, Message.error("多商家组合订单不能线下付款"));
            return "redirect:view.jhtml?id=" + id;
        }
        payment.setOrder(order);
        PaymentMethod paymentMethod = paymentMethodService
                .find(paymentMethodId);
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod
                .getName() : null);
        // if (paymentMethod.getMethod() != PaymentMethod.Method.offline) {
        // return ERROR_VIEW;
        // }
        if (!isValid(payment)) {
            return ERROR_VIEW;
        }
        if (order.isExpired()
                || order.getOrderStatus() != OrderStatus.confirmed) {
            return ERROR_VIEW;
        }
        if (order.getPaymentStatus() != PaymentStatus.unpaid
                && order.getPaymentStatus() != PaymentStatus.partialPayment) {
            return ERROR_VIEW;
        }
        if (payment.getAmount().compareTo(new BigDecimal(0)) <= 0
                || payment.getAmount().compareTo(order.getAmountPayable()) > 0) {
            return ERROR_VIEW;
        }
        if (payment.getMethod() == Payment.Method.deposit
                && payment.getAmount().compareTo(member.getBalance()) > 0) {
            return ERROR_VIEW;
        }
        if (order.isLocked(member)) {
            return ERROR_VIEW;
        }
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
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Order order, Long tradeId, Long areaId,
                         Long paymentMethodId, Long shippingMethodId,
                         RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Admin admin = adminDao.findByUsername("admin");
        Trade trade = tradeService.find(tradeId);
        // Order order = trade.getOrder();
        for (Iterator<OrderItem> iterator = order.getOrderItems().iterator(); iterator
                .hasNext(); ) {
            OrderItem orderItem = iterator.next();
            if (orderItem == null || StringUtils.isEmpty(orderItem.getSn())) {
                iterator.remove();
            }
        }
        order.setArea(areaService.find(areaId));
        order.setPaymentMethod(paymentMethodService.find(paymentMethodId));
        order.setShippingMethod(shippingMethodService.find(shippingMethodId));
        // if (!isValid(order)) {
        // return ERROR_VIEW;
        // }
        Order pOrder = orderService.find(order.getId());
        if (pOrder == null) {
            return ERROR_VIEW;
        }
        if (pOrder.isExpired()
                || pOrder.getOrderStatus() != OrderStatus.unconfirmed) {
            return ERROR_VIEW;
        }
        if (!pOrder.isLocked(member)) {
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
                    if (trade.getIsAllocatedStock()) {
                        if (orderItem.calculateQuantityIntValue() > product
                                .getAvailableStock()
                                + pOrderItem.calculateQuantityIntValue()) {
                            return ERROR_VIEW;
                        }
                    } else {
                        if (orderItem.calculateQuantityIntValue() > product
                                .getAvailableStock()) {
                            return ERROR_VIEW;
                        }
                    }
                }
                BeanUtils.copyProperties(pOrderItem, orderItem, new String[]{
                        "price", "quantity"});
                if (pOrderItem.getIsGift()) {
                    orderItem.setPrice(new BigDecimal(0));
                }
            } else {
                Product product = productService.findBySn(orderItem.getSn());
                if (product == null) {
                    return ERROR_VIEW;
                }
                if (product.getStock() != null
                        && orderItem.calculateQuantityIntValue() > product
                        .getAvailableStock()) {
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
        order.setOperator(member);
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
     * 调转提货码页面
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(ModelMap model) {
        Member member = memberService.getCurrent();
        model.addAttribute("content", "0");
        if (member.getTenant() != null) {
            model.addAttribute("telephone", member.getTenant().getTelephone());
        }
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        return "/store/member/trade/search";
    }

    /**
     * 查询提货信息
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchSn(String sn, RedirectAttributes redirectAttributes,
                           ModelMap model) {
        Member member = memberService.getCurrent();
        model.addAttribute("sn", sn);
        Trade trade = tradeService.findBySn(sn);
        if (trade == null) {
            model.addAttribute("content", "1");
            return "/store/member/trade/search";
        }
        if (member != trade.getTenant().getMember()) {
            model.addAttribute("content", "1");
            return "/store/member/trade/search";
        }
        model.addAttribute("member", member);
        return "redirect:viewForSn.jhtml?id=" + trade.getId();
    }

    @RequestMapping(value = "/searchBySn", method = RequestMethod.POST)
    public
    @ResponseBody
    Message  searchBySn(String sn, RedirectAttributes redirectAttributes,
                           ModelMap model) {
        Member member = memberService.getCurrent();
        model.addAttribute("sn", sn);
        Trade trade = tradeService.findBySn(sn);
        if (trade == null) {
            model.addAttribute("content", "1");
            return Message.error("提货码不存在");
        }
        if (member != trade.getTenant().getMember()) {
            model.addAttribute("content", "1");
            return Message.error("提货码是不存在的");
        }
        model.addAttribute("member1", member);
        model.addAttribute("searchTrade",trade);
        return SUCCESS_MESSAGE;
    }
    /**
     * 发货
     */
    @RequestMapping(value = "/shipping", method = RequestMethod.POST)
    public String shipping(String searchSn, Long tradeId,
                           Long shippingMethodId, Long deliveryCorpId, Long deliveryCenterId,
                           Long areaId, Shipping shipping, String pick_up_time,
                           RedirectAttributes redirectAttributes, ModelMap model) {

    		Date pick_up_date = null;
	        if (shippingMethodId == 3 && pick_up_time != null) {
	            try {
	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                pick_up_date = simpleDateFormat.parse(pick_up_time);
	            } catch (ParseException e) {
	                e.printStackTrace();
	            }
	            shipping.setPickUpTime(pick_up_date);
	        }
	        Member member = memberService.getCurrent();
	        Trade trade = tradeService.find(tradeId);
	        if (trade == null) {
	            return ERROR_VIEW;
	        }
            trade.setDeliveryDate(shipping.getPickUpTime());
	        Order order = trade.getOrder();
	        for (Iterator<ShippingItem> iterator = shipping.getShippingItems()
	                .iterator(); iterator.hasNext(); ) {
	            ShippingItem shippingItem = iterator.next();
	            if (shippingItem == null
	                    || StringUtils.isEmpty(shippingItem.getSn())
	                    || shippingItem.getQuantity() == null
	                    || shippingItem.getQuantity() <= 0) {
	                iterator.remove();
	                continue;
	            }
	            OrderItem orderItem = trade.getOrderItem(shippingItem.getSn()); // 获取的OrderItem为同一个OrderItem
	
	            if (orderItem == null
	                    || shippingItem.getQuantity() > orderItem.getQuantity()
	                    - orderItem.getShippedQuantity()) {
	                return ERROR_VIEW;
	            }
	            if (orderItem.getProduct() != null
	                    && orderItem.getProduct().getStock() != null
	                    && shippingItem.getQuantity() > orderItem.getProduct()
	                    .getStock()) {
	                return ERROR_VIEW;
	            }
	            shippingItem.setName(orderItem.getFullName());
	            shippingItem.setShipping(shipping);
	
	            shipping.setTrade(trade);
	        }
	        shipping.setOrder(trade.getOrder());
	        ShippingMethod shippingMethod = shippingMethodService
	                .find(shippingMethodId);
	        shipping.setShippingMethod(shippingMethod != null ? shippingMethod
	                .getName() : null);
	        DeliveryCorp deliveryCorp = deliveryCorpService.find(deliveryCorpId);
	        shipping.setDeliveryCorp(deliveryCorp != null ? deliveryCorp.getName()
	                : null);
	        shipping.setDeliveryCorpUrl(deliveryCorp != null ? deliveryCorp
	                .getUrl() : null);
	        shipping.setDeliveryCorpCode(deliveryCorp != null ? deliveryCorp
	                .getCode() : null);
	        Area area = areaService.find(areaId);
	        shipping.setArea(area != null ? area.getFullName() : null);
	
	        // 默认发货地
	        shipping.setDeliveryCenter(deliveryCenterService.findDefault(member
	                .getTenant()));
	        // if (!isValid(shipping)) {
	        // return ERROR_VIEW;
	        // }
	        if (order.isExpired()
	                || trade.getOrderStatus() != OrderStatus.confirmed) {
	            return ERROR_VIEW;
	        }
	        if (trade.getShippingStatus() != ShippingStatus.unshipped
	                && trade.getShippingStatus() != ShippingStatus.partialShipment) {
	            return ERROR_VIEW;
	        }
	        shipping.setSn(snService.generate(Sn.Type.shipping));
	        shipping.setOperator(member.getUsername());
	        orderService.shipping(order, shipping, member);
	        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
	        model.addAttribute("order", order);
	        model.addAttribute("sn", searchSn);
	        model.addAttribute("member", member);
	        return "redirect:list.jhtml?type=shipped";
    }
    
    /**
     * 发货
     */
    @RequestMapping(value = "/batch_shipping", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock batchShipping(Long[] tradeIds, Long deliveryCorpId,Long shippingMethodId, String trackingNo, String sn, RedirectAttributes redirectAttributes, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        for(Long id:tradeIds){
        	Trade trade = tradeService.find(id);
	        Order order = trade.getOrder();
	        if (order.isLocked(member)) {
	            return DataBlock.error("已经锁定,此订单正在处理中");
	        }
	        if (OrderStatus.cancelled == order.getOrderStatus()) {
	            return DataBlock.error("订单已经取消!");
	        }
	
	        if (ShippingStatus.shipped == trade.getShippingStatus()) {
	            return DataBlock.warn("部分订单已经发货!");
	        }
	        Shipping shipping = new Shipping();
	        for (Iterator<OrderItem> iterator = trade.getOrderItems().iterator(); iterator.hasNext(); ) {
	            OrderItem orderItem = iterator.next();
	            ShippingItem shippingItem = new ShippingItem();
	            shippingItem.setQuantity(orderItem.getQuantity() - orderItem.getShippedQuantity());
	            shippingItem.setSn(orderItem.getSn());
	            shippingItem.setName(orderItem.getFullName());
	            shippingItem.setShipping(shipping);
	            shipping.getShippingItems().add(shippingItem);
	        }
	        shipping.setTrade(trade);
	        shipping.setOrder(order);
	        
	        ShippingMethod shippingMethod=null;
	        if(shippingMethodId!=null){
	        	shippingMethod= shippingMethodService.find(shippingMethodId);
	        }else{
	        	shippingMethod = trade.getOrder().getShippingMethod();
	        }
	        shipping.setShippingMethod(shippingMethod != null ? shippingMethod.getName() : null);
	        
	        DeliveryCenter deliveryCenter = deliveryCenterService.findDefault(member.getTenant());
	        shipping.setDeliveryCenter(deliveryCenter);
	
	        shipping.setDeliveryCorp("web端发货");
	        shipping.setDeliveryCorpUrl(null);
	        shipping.setDeliveryCorpCode(null);
	
	        if (deliveryCorpId != null) {
	            DeliveryCorp deliveryCorp = deliveryCorpService.find(deliveryCorpId);
	            if (deliveryCorp != null) {
	                shipping.setDeliveryCorp(deliveryCorp.getName());
	                shipping.setDeliveryCorpUrl(deliveryCorp.getUrl());
	                shipping.setDeliveryCorpCode(deliveryCorp.getCode());
	            }
	        }
	        Area area = order.getArea();
	        shipping.setArea(area != null ? area.getFullName() : null);
	        shipping.setAddress(order.getAddress());
	        shipping.setPhone(order.getPhone());
	        shipping.setZipCode(order.getZipCode());
	        shipping.setConsignee(order.getConsignee());
	        shipping.setPickUpTime(new Date());
	        shipping.setTrackingNo(trackingNo);
	        shipping.setSn(snService.generate(Sn.Type.shipping));
	        shipping.setOperator(member.getUsername());
	        orderService.shipping(order, shipping, null);
        }
        

//        if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(29L))) {
//            activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(29L));
//        }

        return DataBlock.success("success", "全部发货完成");
    }

    /**
     * 拒绝
     */
    @RequestMapping(value = "/rejected", method = RequestMethod.POST)
    public String rejected(Long id, RedirectAttributes redirectAttributes) {
        Trade trade = tradeService.find(id);
        Order order = trade.getOrder();
        Member member = memberService.getCurrent();

        if (order.isLocked(member)) {
            addFlashMessage(redirectAttributes, Message.error("已经锁定,此订单正在处理中"));
            return "redirect:view.jhtml?id=" + id;
        }
        if (OrderStatus.cancelled == order.getOrderStatus()) {
            addFlashMessage(redirectAttributes, Message.error("订单已经取消!"));
            return "redirect:view.jhtml?id=" + id;
        }
        SpReturns current = null;
        for (SpReturns spReturns : trade.getSpReturns()) {
            if (spReturns.getReturnStatus().equals(ReturnStatus.unconfirmed)) {
                current = spReturns;
            }
        }
        if (current == null) {
            addFlashMessage(redirectAttributes, Message.error("没找到退货申请!"));
        } else {
            orderService.spRejected(trade, current, member);
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        }
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public String close(Long id, RedirectAttributes redirectAttributes) {
        Trade trade = tradeService.find(id);
        Order order = trade.getOrder();
        Member member = memberService.getCurrent();

        if (order.isLocked(member)) {
            addFlashMessage(redirectAttributes, Message.error("已经锁定,此订单正在处理中"));
            return "redirect:view.jhtml?id=" + id;
        } else if (OrderStatus.cancelled == order.getOrderStatus()) {
            addFlashMessage(redirectAttributes, Message.error("订单已经取消!"));
            return "redirect:view.jhtml?id=" + id;
        }

        if (!order.canCancel()) {
            addFlashMessage(redirectAttributes, Message.error("执行中的订单不能取消!"));
        } else {
            orderService.cancel(trade, member);

            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(31L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(31L));
            }
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        }
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/cancel_trade", method = RequestMethod.POST)
    @ResponseBody
    public Message cancelTrade(Long tradeId, RedirectAttributes redirectAttributes) {
        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();
        Member member = memberService.getCurrent();
        if (order.isLocked(member)) {
            return Message.error("已经锁定,此订单正在处理中");
        } else if (OrderStatus.cancelled == order.getOrderStatus()) {
            return Message.error("订单已经取消!");
        }
        if (trade.getShippingStatus() == ShippingStatus.shipped
                && trade.getOrder().getPaymentMethod().getMethod() == Method.offline) {
            orderService.cancel(trade, member);
        }
        return Message.success("操作成功");
    }

    /**
     * 退货
     */
    @RequestMapping(value = "/returns", method = RequestMethod.POST)
    public String returns(Long tradeId, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();

        if (order.isLocked(member)) {
            addFlashMessage(redirectAttributes, Message.error("已经锁定,此订单正在处理中"));
            return "redirect:view.jhtml?id=" + tradeId;
        } else if (OrderStatus.cancelled == order.getOrderStatus()) {
            addFlashMessage(redirectAttributes, Message.error("订单已经取消!"));
            return "redirect:view.jhtml?id=" + tradeId;
        }
        try {
            if (trade.getShippingStatus().equals(ShippingStatus.returned) || trade.getPaymentStatus().equals(PaymentStatus.refunded)) {
                addFlashMessage(redirectAttributes, Message.error("已经退货了!"));
                return "redirect:view.jhtml?id=" + tradeId;
            }
            if (!trade.getShippingStatus().equals(ShippingStatus.shippedApply) && !trade.getPaymentStatus().equals(PaymentStatus.refundApply)) {
                orderService.spReturns(trade, null, member);
            }
            SpReturns current = null;
            for (SpReturns spReturns : trade.getSpReturns()) {
                if (spReturns.getReturnStatus().equals(ReturnStatus.unconfirmed)) {
                    current = spReturns;
                }
            }
            if (current == null) {
                addFlashMessage(redirectAttributes, Message.error("退货申请已处理!"));
                return "redirect:view.jhtml?id=" + tradeId;
            }
            orderService.spConfirm(trade, current, member);
            current.setReturnStatus(ReturnStatus.completed);
            spReturnsService.update(current);

            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(32L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(32L));
            }

            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        } catch (BalanceNotEnoughException e) {
            // TODO Auto-generated catch block
            addFlashMessage(redirectAttributes, Message.error("账户余额不足,不能完成退款"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            addFlashMessage(redirectAttributes, Message.error("未知错误"));
        }
        return "redirect:view.jhtml?id=" + tradeId;
    }

    /**
     * 退货列表
     */
    @RequestMapping(value = "/return/list", method = RequestMethod.GET)
    public String list(String keyword, ModelMap model, ReturnStatus returnStatus, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (returnStatus == null) {
            returnStatus = ReturnStatus.unconfirmed;
        }
        Page<SpReturns> page = spReturnsService.findByTenant(member.getTenant(), returnStatus, keyword, pageable);
        model.addAttribute("member", member);
        model.addAttribute("page", page);
        model.addAttribute("returnStatus", returnStatus);
        model.addAttribute("menu","return_management");
        model.addAttribute("unconfirmed_num", spReturnsService.findReturnNumber(member.getTenant(),ReturnStatus.unconfirmed).size());
        model.addAttribute("confirmed_num", spReturnsService.findReturnNumber(member.getTenant(),ReturnStatus.confirmed).size());
        model.addAttribute("audited_num", spReturnsService.findReturnNumber(member.getTenant(),ReturnStatus.audited).size());
        model.addAttribute("completed_num", spReturnsService.findReturnNumber(member.getTenant(),ReturnStatus.completed).size());
        model.addAttribute("cancelled_num", spReturnsService.findReturnNumber(member.getTenant(),ReturnStatus.cancelled).size());
        return "/store/member/trade/return/list";
    }

    /**
     * 退货查看
     */
    @RequestMapping(value = "/return/view", method = RequestMethod.GET)
    public String view(ModelMap model, Long spReturnsId) {
        Member member = memberService.getCurrent();
        SpReturns spReturns = spReturnsService.find(spReturnsId);
        Trade trade = spReturns.getTrade();
        model.addAttribute("member", member);
        model.addAttribute("methods", Payment.Method.values());
        model.addAttribute("refundsMethods", Refunds.Method.values());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        model.addAttribute("shippingMethods", shippingMethodService.findAll());
        model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
        model.addAttribute("trade", trade);
        model.addAttribute("isReturnAllowed", !trade.isSpReturns());
        model.addAttribute("spReturn", spReturns);
        model.addAttribute("pageActive", 2);
        model.addAttribute("menu","return_management");
        return "/store/member/trade/return/view";
    }
    
    @RequestMapping(value = "/return/print", method = RequestMethod.GET)
    public String returnPrint(Long spReturnsId, ModelMap model) {
//        Trade trade = tradeService.find(id);
    	SpReturns spReturns=spReturnsService.find(spReturnsId);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        List<Object> list = new ArrayList<>();
        int len = spReturns.getReturnsItems().size();
        int count = len/5;

        //够4000的一组处理
        for(int j = 0 ; j < count ; j++){
            List<Map<String,Object>> lists = new ArrayList<>();
            for(int m = 0 ; m<len%5;m++){
                Map<String,Object> map1 =new HashMap<>();
                map1.put("fullName",spReturns.getReturnsItems().get(m+j*5).getOrderItem().getFullName());
                map1.put("isGift",spReturns.getReturnsItems().get(m+j*5).getOrderItem().getIsGift());
                map1.put("quantity",spReturns.getReturnsItems().get(m+j*5).getReturnQuantity());
                map1.put("packagUnitName",spReturns.getReturnsItems().get(m+j*5).getOrderItem().getPackagUnitName());
                map1.put("price",spReturns.getReturnsItems().get(m+j*5).getOrderItem().getPrice());
                if(spReturns.getReturnsItems().get(m+j*5).getOrderItem().getProduct()!=null){
                	map1.put("time", sdf.format(spReturns.getReturnsItems().get(m+j*5).getOrderItem().getProduct().getCreateDate()));
                	map1.put("barcode",spReturns.getReturnsItems().get(m+j*5).getOrderItem().getBarcode());
                }else{
                	map1.put("time", "--");
                	map1.put("barcode","--");
                }
                lists.add(map1);
            }

            if(lists.size()>0){
                list.add(lists);
            }
        }
        //剩下的不够4000的处理
        List<Map<String,Object>> lists1 = new ArrayList<>();
        for(int m = 0 ; m<len%5;m++){
            Map<String,Object> map2 =new HashMap<>();
            map2.put("fullName",spReturns.getReturnsItems().get(m+count*5).getOrderItem().getFullName());
            map2.put("isGift",spReturns.getReturnsItems().get(m+count*5).getOrderItem().getIsGift());
            map2.put("quantity",spReturns.getReturnsItems().get(m+count*5).getOrderItem().getQuantity());
            map2.put("packagUnitName",spReturns.getReturnsItems().get(m+count*5).getOrderItem().getPackagUnitName());
            map2.put("price",spReturns.getReturnsItems().get(m+count*5).getOrderItem().getPrice());
            if(spReturns.getReturnsItems().get(m+count*5).getOrderItem().getProduct()!=null){
            	map2.put("time", sdf.format(spReturns.getReturnsItems().get(m+count*5).getOrderItem().getProduct().getCreateDate()));
            	map2.put("barcode",spReturns.getReturnsItems().get(m+count*5).getOrderItem().getBarcode());
            }else{
            	map2.put("time", "--");
            	map2.put("barcode","--");
            }
            lists1.add(map2);
        }
        if(lists1.size()>0){
            list.add(lists1);
        }
        model.addAttribute("orders", list);
        model.addAttribute("spReturns", spReturns);
        model.addAttribute("menu","order_management");
        return "/store/member/trade/return/print";
    }

    @RequestMapping(value = "/return/print/times/{id}", method = RequestMethod.POST)
    public Message returnPrintTimes(@PathVariable Long id) {
        SpReturns spReturns=spReturnsService.find(id);

        if(spReturns==null){
            return Message.error("success");
        }
        spReturns.setPrintDate(new Date());

        Integer iii=1;
        if(spReturns.getPrint()!=null){
            iii=spReturns.getPrint()+iii;
        }

        spReturns.setPrint(iii);

        spReturnsService.update(spReturns);
        return Message.success("success");
    }

    /**
     * 确认/拒绝-退货
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/return/confirm_return", method = RequestMethod.POST)
    public
    @ResponseBody
    Message confirm(Long id, String flag, String content, String app_type) {
        Member member = memberService.getCurrent();
        SpReturns spReturns = spReturnsService.find(id);
        if (spReturns == null) {
            return Message.error("退货单不存在");
        }
        Order order = spReturns.getTrade().getOrder();
        if (order.isLocked(member)) {
            return Message.error("该订单被锁定");
        } else if (OrderStatus.cancelled == order.getOrderStatus()) {
            return Message.error("该订单已取消");
        }

        if (spReturns.getType() == SpReturns.Type.normal) {
            if (flag.equals("true")) {//店家受理退货
                try {
                    if (member.getTenant().getMember().getBalance().compareTo(spReturns.getAmount()) < 0) {
                        return Message.error("您的余额不足");
                    }
                    orderService.spConfirm(spReturns.getTrade(), spReturns, member);//店家同意退货
                    spReturns.setReturnStatus(ReturnStatus.completed);
                    spReturnsService.update(spReturns);
                    return Message.success("确认成功");
                } catch (BalanceNotEnoughException e) {
                    e.printStackTrace();
                    return Message.error("操作失败");
                }
            } else if (flag.equals("false")) {//店家拒绝退货
                orderService.spRejected(spReturns.getTrade(), spReturns, member);
                spReturns.setMemo(content);
                spReturnsService.update(spReturns);
                return Message.success("拒绝成功");
            }
        } else {
            if (spReturns != null && spReturns.getReturnStatus() == ReturnStatus.unconfirmed && member.getTenant().equals(spReturns.getTrade().getTenant())) {
                if (flag.equals("true")) {//店家受理退货
                    if (app_type != null && "jdh".equals(app_type)) {
                        spReturns.setReturnStatus(ReturnStatus.confirmed);
                        spReturnsService.update(spReturns);
                    } else {//非聚德版本的直接同意退货
                        try {
                            if (member.getTenant().getMember().getBalance().compareTo(spReturns.getAmount()) < 0) {
                                return Message.error("您的余额不足");
                            }
                            orderService.spConfirm(spReturns.getTrade(), spReturns, member);
                            spReturns.setReturnStatus(ReturnStatus.completed);
                            spReturnsService.update(spReturns);
                        } catch (BalanceNotEnoughException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }//店家同意退货
                    }
                    return Message.success("确认成功");
                } else if (flag.equals("false")) {//店家拒绝退货
                    orderService.spRejected(spReturns.getTrade(), spReturns, member);
                    spReturns.setMemo(content);
                    spReturnsService.update(spReturns);
                    return Message.success("拒绝成功");
                }
            } else if (spReturns != null && spReturns.getReturnStatus() == ReturnStatus.audited && member.getTenant().equals(spReturns.getTrade().getTenant())) {
                try {
                    if (member.getTenant().getMember().getBalance().compareTo(spReturns.getAmount()) < 0) {
                        return Message.error("您的余额不足");
                    }
                    if (spReturns.getSupplier().getBalance().compareTo(spReturns.getCost()) < 0) {
                        return Message.error("供应商的余额不足");
                    }
                    orderService.spConfirm(spReturns.getTrade(), spReturns, member);//店家同意退货
//					spReturns.setReturnStatus(ReturnStatus.completed);
//			    	spReturnsService.update(spReturns);
                    return Message.success("确认成功");
                } catch (BalanceNotEnoughException e) {
                    e.printStackTrace();
                    return Message.error("操作失败");
                }
            }
        }
        return Message.error("确认失败");
    }

    /**
     * 买家评价
     */
    @RequestMapping(value = "/addView", method = RequestMethod.GET)
    public String review(String sn, Model model) {
        Trade trade = tradeService.findBySn(sn);
        Order order = trade.getOrder();
        if (order == null) {
            return ERROR_VIEW;
        }
        if (trade.getOrderStatus() == OrderStatus.completed
                && memberService.getCurrent().equals(order.getMember())
                && !order.isExpired()) {
            model.addAttribute("trade", trade);
            model.addAttribute("captchaId", UUID.randomUUID().toString());
            return "/store/member/review/add";
        }
        return ERROR_VIEW;
    }

    /**
     * 买家评价
     */
    /**
     @RequestMapping(value = "/saveView", method = RequestMethod.POST)
     public @ResponseBody Message reviewSubmit(String captchaId, String captcha,String sn,
     ArrayOrderItemReview orderItemReviews,
     net.wit.entity.Review.Type type, String content,
     String productContent, Integer score, Integer assistant,
     HttpServletRequest request, RedirectAttributes redirectAttributes) {
     if (!captchaService.isValid(CaptchaType.consultation, captchaId, captcha)) {
     return Message.error("store.captcha.invalid");
     }
     Setting setting = SettingUtils.get();
     if (!setting.getIsConsultationEnabled()) {
     return Message.error("store.consultation.disabled");
     }
     Trade trade = tradeService.findBySn(sn);
     if (trade != null
     && trade.getOrder() != null
     && (trade.getShippingStatus() == ShippingStatus.accept || trade
     .getShippingStatus() == ShippingStatus.returned)
     && memberService.getCurrent().equals(
     trade.getOrder().getMember())
     && !trade.getOrder().isExpired()) {
     Review review = new Review();
     review.setType(type);
     review.setScore(score);
     review.setAssistant(assistant);
     review.setIp(request.getRemoteAddr().toString());
     review.setContent(content);
     review.setFlag(Flag.trade);
     review.setIsShow(Boolean.TRUE);
     review.setTenant(trade.getTenant());
     review.setMember(memberService.getCurrent());
     review.setMemberTrade(trade);
     try {
     reviewService.save(review);
     } catch (Exception e) {
     e.printStackTrace();
     }
     try {

     List<OrderItem> orderItems = new ArrayList<OrderItem>(
     trade.getOrderItems());

     for (OrderItem orderItem : orderItems) {
     Review reviewItem = new Review();
     reviewItem.setFlag(Flag.product);
     reviewItem.setScore(score);
     reviewItem.setIp(request.getRemoteAddr().toString());
     reviewItem.setContent(content);
     reviewItem.setMember(memberService.getCurrent());
     reviewItem.setProduct(orderItem.getProduct());
     reviewItem.setIsShow(Boolean.TRUE);
     reviewItem.setTenant(orderItem.getProduct().getTenant());
     reviewItem.setOrderItem(orderItem);
     reviewService.save(reviewItem);
     }
     } catch (Exception e) {
     e.printStackTrace();
     }
     trade.setMemberReview(review);
     tradeService.update(trade);
     addFlashMessage(redirectAttributes, Message.success("评价成功"));
     return Message.success("评价成功");
     //return "redirect:/b2b/member/order/view.jhtml?id=" + trade.getId();
     }
     addFlashMessage(redirectAttributes, Message.error("评价信息错误"));
     return Message.error("评价信息错误");
     //return "redirect:/b2b/member/order/list.jhtml";
     }
     **/
    /**
     * 订单结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/order_settle_account", method = RequestMethod.GET)
    public String settle_account(Pageable pageable, String startDate, String status,String keywords, String endDate, ModelMap model,
                                 RedirectAttributes redirectAttributes) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(startDate)) {
            start_time = simpleDateFormat.parse(startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            end_time = simpleDateFormat.parse(endDate);
        }
        Boolean b_status = null;
        if ("true".equals(status) || status == "true") {
            b_status = true;
        } else if ("false".equals(status) || status == "false") {
            b_status = false;
        } else {
            b_status = null;
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("suppliered", Filter.Operator.eq, b_status));
        pageable.setFilters(filters);
        model.addAttribute("page", tradeService.findPage(pageable, member.getTenant(), start_time, end_time, null, keywords));
        model.addAttribute("member", member);
        model.addAttribute("begin_date",startDate);
        model.addAttribute("end_date", endDate);
        model.addAttribute("status", status);
        model.addAttribute("keywords",keywords);
        model.addAttribute("pageActive", 2);
        model.addAttribute("menu","settle_account");
        return "/store/member/trade/order_settle_account";
    }
    /**
     * 订单结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/order_settle_account_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> settle_account_export(Pageable pageable, String startDate, String status,String keywords, String endDate, ModelMap model,
                                 RedirectAttributes redirectAttributes) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(startDate)) {
            start_time = simpleDateFormat.parse(startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            end_time = simpleDateFormat.parse(endDate);
        }
        Boolean b_status = null;
        if ("true".equals(status) || status == "true") {
            b_status = true;
        } else if ("false".equals(status) || status == "false") {
            b_status = false;
        } else {
            b_status = null;
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<Trade> trades=tradeService.findTradeByTenant(member.getTenant(), start_time, end_time, keywords, b_status,"order_settle");
        for(Trade trade:trades){
        	for(OrderItem orderItem:trade.getOrderItems()){
        		Map<String, Object> map=new HashMap<String, Object>();
            	map.put("sn", trade.getOrder().getSn());
            	map.put("pSn", orderItem.getSn());
            	map.put("name",orderItem.getFullName());
        		map.put("cost",orderItem.getCost());
        		map.put("price", orderItem.getPrice());
        		map.put("quantity",orderItem.getQuantity());
        		if(trade.getCreateDate()!=null){
            		map.put("time", simpleDateFormat.format(trade.getCreateDate()));
            	}else{
            		map.put("time", "--");
            	}
        		if(orderItem.getProduct()!=null){
	        		map.put("barcode", orderItem.getProduct().getBarcode());
	        	}else{
	        		map.put("barcode", "--");
	        	}
        		map.put("total_price", orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        		map.put("total_cost", orderItem.getCost().multiply(new BigDecimal(orderItem.getQuantity())));
            	if(trade.getOrder().getPaymentMethod().getMethod()==Method.balance){
            		map.put("paymentMethod","余额支付");
            	}else if(trade.getOrder().getPaymentMethod().getMethod()==Method.offline){
            		map.put("paymentMethod","线下支付");
            	}else if(trade.getOrder().getPaymentMethod().getMethod()==Method.online){
            		map.put("paymentMethod","线上支付");
            	}
            	if(trade.getOrder().getOrderStatus()==OrderStatus.unconfirmed){
            		map.put("orderStatus", "未确认");
            	}else if(trade.getOrder().getOrderStatus()==OrderStatus.confirmed){
            		map.put("orderStatus", "已确认");
            	}else if(trade.getOrder().getOrderStatus()==OrderStatus.completed){
            		map.put("orderStatus", "已完成");
            	}else if(trade.getOrder().getOrderStatus()==OrderStatus.cancelled){
            		map.put("orderStatus", "已取消");
            	}
            	if(trade.getPaymentStatus()==PaymentStatus.paid){
            		map.put("paymentStatus","已支付");
            	}else if(trade.getPaymentStatus()==PaymentStatus.unpaid){
            		map.put("paymentStatus","未支付");
            	}else if(trade.getPaymentStatus()==PaymentStatus.partialPayment){
            		map.put("paymentStatus","部分支付");
            	}else if(trade.getPaymentStatus()==PaymentStatus.partialRefunds){
            		map.put("paymentStatus","部分退款");
            	}else if(trade.getPaymentStatus()==PaymentStatus.refunded){
            		map.put("paymentStatus","已退款");
            	}else if(trade.getPaymentStatus()==PaymentStatus.refundApply){
            		map.put("paymentStatus","退款中");
            	}
            	if(trade.getSuppliered()!=null){
            		if(trade.getSuppliered()){
    	        		map.put("suppliered","已结算");
    	        	}else{
    	        		map.put("suppliered","未结算");
    	        	}
            	}else{
            		map.put("suppliered","未结算");
            	}
            	if(trade.getSupplierDate()!=null){
            		map.put("supplierDate", simpleDateFormat.format(trade.getSupplierDate()));
            	}else{
            		map.put("supplierDate", "--");
            	}
        		if(orderItem.getSupplier()!=null){
        			map.put("supplierName",orderItem.getSupplier().getName());
        		}else{
        			map.put("supplierName","--");
        		}
        		maps.add(map);
        	}
        }
        return maps;
    }



    /**
     * 退货结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/return_settle_account", method = RequestMethod.GET)
    public String settle_accoun(Pageable pageable,String keywords, String startDate, String status, String endDate, ModelMap model, RedirectAttributes redirectAttributes) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(startDate)) {
            start_time = simpleDateFormat.parse(startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            end_time = simpleDateFormat.parse(endDate);
        }
        Boolean b_status = null;
        if ("true".equals(status) || status == "true") {
            b_status = true;
        } else if ("false".equals(status) || status == "false") {
            b_status = false;
        } else {
            b_status = null;
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("suppliered", Filter.Operator.eq, b_status));
        pageable.setFilters(filters);
        model.addAttribute("page", spReturnsService.findPageByTenant(member.getTenant(), null, start_time, end_time, null, b_status, keywords, pageable));
        model.addAttribute("member", member);
        model.addAttribute("begin_date", startDate);
        model.addAttribute("end_date", endDate);
        model.addAttribute("status", status);
        model.addAttribute("member", member);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageActive", 2);
        model.addAttribute("menu","settle_account");
        return "/store/member/trade/return_settle_account";
    }

    /**
     * 退货结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/return_settle_account_export", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> settle_accoun_export(String keywords,String startDate, String status, String endDate, ModelMap model, RedirectAttributes redirectAttributes) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(startDate)) {
            start_time = simpleDateFormat.parse(startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            end_time = simpleDateFormat.parse(endDate);
        }
        Boolean b_status = null;
        if ("true".equals(status) || status == "true") {
            b_status = true;
        } else if ("false".equals(status) || status == "false") {
            b_status = false;
        } else {
            b_status = null;
        }
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        List<SpReturns> spReturns=spReturnsService.findByTenant(member.getTenant(), null, start_time, end_time, b_status, keywords);
        for(SpReturns spReturn:spReturns){
        	for(SpReturnsItem returnsItem:spReturn.getReturnsItems()){
        		Map<String, Object> map=new HashMap<String, Object>();
            	map.put("rSn", spReturn.getSn());
            	map.put("oSn", returnsItem.getOrderItem().getTrade().getOrder().getSn());
            	map.put("pSn", returnsItem.getOrderItem().getSn());
            	map.put("name",returnsItem.getOrderItem().getFullName());
        		map.put("shippedQuantity",returnsItem.getShippedQuantity());
        		map.put("returnQuantity",returnsItem.getReturnQuantity());
        		map.put("price", returnsItem.getOrderItem().getPrice());
        		map.put("cost", returnsItem.getOrderItem().getCost());
        		map.put("return_cost", returnsItem.getOrderItem().getCost().multiply(new BigDecimal(returnsItem.getReturnQuantity())));
        		map.put("return_price", returnsItem.getOrderItem().getPrice().multiply(new BigDecimal(returnsItem.getReturnQuantity())));
        		map.put("total_cost", returnsItem.getOrderItem().getCost().multiply(new BigDecimal(returnsItem.getShippedQuantity())));
        		map.put("total_price", returnsItem.getOrderItem().getPrice().multiply(new BigDecimal(returnsItem.getShippedQuantity())));
        		if(spReturn.getCreateDate()!=null){
            		map.put("time", simpleDateFormat.format(spReturn.getCreateDate()));
            	}else{
            		map.put("time", "--");
            	}
            	if(spReturn.getTrade().getOrder().getPaymentMethod().getMethod()==Method.balance){
            		map.put("paymentMethod","余额支付");
            	}else if(spReturn.getTrade().getOrder().getPaymentMethod().getMethod()==Method.offline){
            		map.put("paymentMethod","线下支付");
            	}else if(spReturn.getTrade().getOrder().getPaymentMethod().getMethod()==Method.online){
            		map.put("paymentMethod","线上支付");
            	}
            	if(spReturn.getReturnStatus()==ReturnStatus.audited){
            		map.put("returnStatus","已认证");
            	}else if(spReturn.getReturnStatus()==ReturnStatus.cancelled){
            		map.put("returnStatus","已取消");
            	}else if(spReturn.getReturnStatus()==ReturnStatus.completed){
            		map.put("returnStatus","已完成");
            	}else if(spReturn.getReturnStatus()==ReturnStatus.confirmed){
            		map.put("returnStatus","已确认");
            	}else if(spReturn.getReturnStatus()==ReturnStatus.unconfirmed){
            		map.put("returnStatus","未确认");
            	}
            	if(spReturn.getSuppliered()!=null){
            		if(true==spReturn.getSuppliered()){
    	        		map.put("suppliered","已结算");
    	        	}else{
    	        		map.put("suppliered","未结算");
    	        	}
            	}else{
            		map.put("suppliered","未结算");
            	}
            	if(spReturn.getSupplierDate()!=null){
            		map.put("supplierDate", simpleDateFormat.format(spReturn.getSupplierDate()));
            	}else{
            		map.put("supplierDate", "--");
            	}
            	if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.unconfirmed){
            		map.put("orderStatus", "未确认");
            	}else if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.confirmed){
            		map.put("orderStatus", "已确认");
            	}else if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.completed){
            		map.put("orderStatus", "已完成");
            	}else if(spReturn.getTrade().getOrder().getOrderStatus()==OrderStatus.cancelled){
            		map.put("orderStatus", "已取消");
            	}
            	if(spReturn.getTrade().getShippingStatus()==ShippingStatus.accept){
	        		map.put("shippingStatus", "已签收");
	        	}else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.partialReturns){
	        		map.put("shippingStatus", "部分退货");
	        	}else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.partialShipment){
	        		map.put("shippingStatus", "部分发货");
	        	}else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.returned){
	        		map.put("shippingStatus", "已退货");
	        	}else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.shipped){
	        		map.put("shippingStatus", "已发货");
	        	}else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.shippedApply){
	        		map.put("shippingStatus", "退货中");
	        	}else if(spReturn.getTrade().getShippingStatus()==ShippingStatus.unshipped){
	        		map.put("shippingStatus", "未发货");
	        	}
        		if(returnsItem.getOrderItem().getProduct()!=null){
	        		map.put("barcode", returnsItem.getOrderItem().getProduct().getBarcode());
	        	}else{
	        		map.put("barcode", "--");
	        	}
        		if(returnsItem.getOrderItem().getSupplier()!=null){
        			map.put("supplierName",returnsItem.getOrderItem().getSupplier().getName());
        		}else{
        			map.put("supplierName","--");
        		}
        		maps.add(map);
        	}
        }
        return maps;
    }

    /**
     * 提现结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/withdraw_cash_settle_account", method = RequestMethod.GET)
    public String withdraw_cash_accoun(Pageable pageable, String startDate, String status, String endDate, ModelMap model, RedirectAttributes redirectAttributes) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(startDate)) {
            start_time = simpleDateFormat.parse(startDate);
        }

        if (StringUtils.isNotBlank(endDate)) {
            end_time = simpleDateFormat.parse(endDate);
        }
        Account.Status b_status = null;
        if ("true".equals(status) || status == "true") {
            b_status = Account.Status.success;
        } else if ("false".equals(status) || status == "false") {
            b_status = Account.Status.none;
        } else {
            b_status = null;
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("status", Filter.Operator.eq, b_status));
        pageable.setFilters(filters);
        model.addAttribute("page", accountService.findByTenant(null, start_time, end_time, member.getTenant(), pageable));
        model.addAttribute("member", member);
        model.addAttribute("begin_date", startDate);
        model.addAttribute("end_date", endDate);
        model.addAttribute("status", status);
        model.addAttribute("member", member);
        model.addAttribute("pageActive", 2);
        model.addAttribute("menu","settle_account");
        return "/store/member/trade/withdraw_cash_settle_account";
    }

    /**
     * 提现结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/withdraw_cash_settle_account_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> withdraw_cash_accoun_export(Pageable pageable, String startDate, String status, String endDate, ModelMap model, RedirectAttributes redirectAttributes) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(startDate)) {
            start_time = simpleDateFormat.parse(startDate);
        }

        if (StringUtils.isNotBlank(endDate)) {
            end_time = simpleDateFormat.parse(endDate);
        }
        Account.Status b_status = null;
        if ("true".equals(status) || status == "true") {
            b_status = Account.Status.success;
        } else if ("false".equals(status) || status == "false") {
            b_status = Account.Status.none;
        } else {
            b_status = null;
        }
        List<Account> accounts=accountService.findByTenant(null, start_time, end_time, member.getTenant(),b_status);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Account account:accounts){
        	Map<String, Object> map=new HashMap<String, Object>();
        	if(account.getCreateDate()!=null){
        		map.put("time", simpleDateFormat.format(account.getCreateDate()));
        	}else{
        		map.put("time", "--");
        	}
        	map.put("sn", account.getSn());
        	map.put("amount", account.getAmount());
        	if(account.getStatus()== Account.Status.success){
        		map.put("status", "已结算");
        	}else{
        		map.put("status", "未结算");
        	}
        	map.put("supplier", account.getSupplier().getName());
        	maps.add(map);
        }
        return maps;
    }

    /**
     * 结算订单
     *
     * @param ids
     * @param model
     * @return
     */
    @RequestMapping(value = "/clear_order_settle", method = RequestMethod.POST)
    @ResponseBody
    public Message clear_order(Long[] ids, Model model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error("会员不存在");
        }
        for (Long id : ids) {
            if (id == null) {
                continue;
            }
            Trade trade = tradeService.find(id);
            if (trade == null) {
                return Message.error("订单不存在");
            }
            if (trade.getPaymentStatus().equals(PaymentStatus.unpaid)) {
                if (trade.getOrder().getPaymentMethod().getMethod() != Method.offline) {
                    return Message.error("订单还没支付");
                }
            }
            if (trade.getSuppliered() != null) {
                if (trade.getSuppliered()) {
                    return Message.error("订单已结算");
                }
            }
            Order order = trade.getOrder();
            if (order == null) {
                return Message.error("订单不存在");
            }
            if (trade.getShippingStatus() == ShippingStatus.shipped || trade.getShippingStatus() == ShippingStatus.accept) {
                if (!order.isExpired()) {
                    try {
                        orderService.payment(trade, member);
                    } catch (Exception e) {
                        Message.error("结算失败");
                    }
                }
            } else {
                return Message.error("订单还未完成，不能结算");
            }

        }
        return Message.success("结算成功");
    }

    /**
     * 结算退货单
     *
     * @param ids
     * @param model
     * @return
     */
    @RequestMapping(value = "/clear_return_settle", method = RequestMethod.GET)
    @ResponseBody
    public Message clear_return(Long[] ids, Model model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error("会员不存在");
        }
        for (Long id : ids) {
            if (id == null) {
                continue;
            }
            SpReturns spReturns = spReturnsService.find(id);
            if (spReturns == null) {
                return Message.error("退货单不存在");
            }
            if (spReturns.getSuppliered() != null) {
                if (spReturns.getSuppliered()) {
                    return Message.error("退货单已结算");
                }
            }
            if (spReturns.getSettle().compareTo(spReturns.getSupplier().getBalance()) > 0) {
                return Message.error("账户余额不足");
            }
            if (spReturns.getReturnStatus() != ReturnStatus.completed) {
                return Message.error("退货单还没完成，不能结算");
            }

            try {
                orderService.refundsFromSupplier(spReturns);
            } catch (Exception e) {
                Message.error("结算失败");
            }
        }
        return Message.success("结算成功");
    }

    /**
     * 结算提现
     *
     * @param ids
     * @param model
     * @return
     */
    @RequestMapping(value = "/clear_withdraw_cash", method = RequestMethod.POST)
    @ResponseBody
    public Message clear_withdraw_cash(Long[] ids, Model model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error("会员不存在");
        }
        if (ids == null) {
            return Message.error("未找到提现记录");
        }
        for (Long id : ids) {
            if (id == null) {
                continue;
            }
            Account account = accountService.find(id);
            if (account == null) {
                return Message.error("未找到提现记录");
            }
            account.setStatus(Account.Status.success);
            accountService.update(account);
        }

        return Message.success("结算成功");
    }

    /**
     * 批量导出
     * @param tradeIds
     * @param model
     * @return
     */
    @RequestMapping(value = "/batch_export", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock batchExport(Long tradeId) {
        Member member = memberService.getCurrent();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        List<Trade> trades=new ArrayList<Trade>();
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        if (member == null) {
            return DataBlock.error("会员不存在");
        }
        try {
            Trade trade=tradeService.find(tradeId);
            if(trade==null){
                return DataBlock.error("导出失败");
            }else{
                if(trade.getPrint()!=null){
                    trade.setPrint(trade.getPrint()+1);
                }else{
                    trade.setPrint(1);
                }
                tradeService.update(trade);
//                trades.add(trade);
            }
            for(OrderItem orderItem :trade.getOrderItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("create_date", sdf.format(trade.getCreateDate()));
                map.put("order_sn", trade.getOrder().getSn());
                map.put("delivery_center_address", trade.getDeliveryCenter() != null ? trade.getDeliveryCenter().getAreaName()+trade.getDeliveryCenter().getAddress() : "--");//发货地址
                map.put("delivery_center_name", trade.getDeliveryCenter() != null ? trade.getDeliveryCenter().getTenant().getLinkman() : "--");//发货人
                map.put("delivery_center_telephone", trade.getDeliveryCenter() != null ? trade.getDeliveryCenter().getPhone() : "--");//发货人电话

                map.put("consignee_address",trade.getOrder() != null ? trade.getOrder().getAreaName()+trade.getOrder().getAddress() : "--");//收货人地址
                map.put("consignee_name", trade.getOrder() != null ? (trade.getOrder().getConsignee()!=null?trade.getOrder().getConsignee():"--" ): "--");//收货人
                map.put("consignee_telephone", trade.getOrder() != null ? (trade.getOrder().getPhone()!=null?trade.getOrder().getPhone():"--" ): "--");//收货人电话

                map.put("trade_amount", trade.getAmount());
                map.put("product_name", orderItem.getFullName());
                map.put("product_price", orderItem.getPrice());
                map.put("product_num", orderItem.getQuantity());
                map.put("product_sn", orderItem.getSn());
                map.put("product_barcode", orderItem.getProduct()!=null?orderItem.getProduct().getBarcode():"--");
                maps.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("导出失败");
        }
        return DataBlock.success(maps,"导出成功");
    }
    /**
     * 全部导出
     * @return
     */
    @RequestMapping(value = "/all_export", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock allExport() {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error("会员不存在");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Trade> trades=tradeService.findUnshippedListExport(member.getTenant(),QueryStatus.unshipped);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        try {
            for(Trade trade:trades){
                if(trade==null){
                    continue;
                }else{
                    if(trade.getPrint()!=null){
                        trade.setPrint(trade.getPrint()+1);
                    }else{
                        trade.setPrint(1);
                    }
                    tradeService.update(trade);
                }
                for(OrderItem orderItem :trade.getOrderItems()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("create_date", sdf.format(trade.getCreateDate()));
                    map.put("order_sn", trade.getOrder().getSn());
                    map.put("delivery_center_address", trade.getDeliveryCenter() != null ? trade.getDeliveryCenter().getAreaName()+trade.getDeliveryCenter().getAddress() : "--");//发货地址
                    map.put("delivery_center_name", trade.getDeliveryCenter() != null ? trade.getDeliveryCenter().getTenant().getLinkman() : "--");//发货人
                    map.put("delivery_center_telephone", trade.getDeliveryCenter() != null ? trade.getDeliveryCenter().getPhone() : "--");//发货人电话

                    map.put("consignee_address",trade.getOrder() != null ? trade.getOrder().getAreaName()+trade.getOrder().getAddress() : "--");//收货人地址
                    map.put("consignee_name", trade.getOrder() != null ? (trade.getOrder().getConsignee()!=null?trade.getOrder().getConsignee():"--" ): "--");//收货人
                    map.put("consignee_telephone", trade.getOrder() != null ? (trade.getOrder().getPhone()!=null?trade.getOrder().getPhone():"--" ): "--");//收货人电话

                    map.put("trade_amount", trade.getAmount());
                    map.put("product_name", orderItem.getFullName());
                    map.put("product_price", orderItem.getPrice());
                    map.put("product_num", orderItem.getQuantity());
                    map.put("product_sn", orderItem.getSn());
                    map.put("product_barcode", orderItem.getProduct()!=null?orderItem.getProduct().getBarcode():"--");
                    maps.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("导出失败");
        }
        return DataBlock.success(maps,"导出成功");
    }

}