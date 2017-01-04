/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.sf.json.JSONArray;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.TradeListModel;
import net.wit.controller.assistant.model.TradeModel;
import net.wit.entity.*;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.QueryStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.*;
import net.wit.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 商家订单
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberTradeController")
@RequestMapping("/assistant/member/trade")
public class TradeController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "deliveryCorpServiceImpl")
    private DeliveryCorpService deliveryCorpService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;
    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock list(String keyword, String type, Pageable pageable, ModelMap model, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

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

        }

        Page<Trade> page = tradeService.findPage(pageable, member.getTenant(), queryStatus, keyword);

        return DataBlock.success(TradeListModel.bindData(page.getContent()),page, "执行成功");

    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock view(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.find(id);
        Payment payment = paymentService.findByTrade(trade,Payment.Status.success);
        TradeModel model = new TradeModel();
        model.copyFrom(trade);
        if(payment!=null){
            model.setPay_date(payment.getPaymentDate());
        }
        return DataBlock.success(model, "执行成功");
    }
    /**
     * 扫一扫根据二维码定位详情页
     */
    @RequestMapping(value = "/scanView", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock scanView(String sn) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.findBySn(sn);
        Payment payment = paymentService.findByTrade(trade,Payment.Status.success);
        if(trade!=null){
            TradeModel model = new TradeModel();
            model.copyFrom(trade);
            if(payment!=null){
                model.setPay_date(payment.getPaymentDate());
            }
            return DataBlock.success(model, "执行成功");
        }else {
            return DataBlock.error("无法识别的提货码/条码");
        }

    }
    /**
     * 统计
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock count(ModelMap model) {
        Map<String, Object> data = new HashMap<String, Object>();
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        data.put("waitShipping", tradeService.count(null, member.getTenant(), QueryStatus.unshipped, null));
        data.put("Shipped", tradeService.count(null, member.getTenant(), QueryStatus.shipped, null));
        data.put("waitPayment", tradeService.count(null, member.getTenant(), QueryStatus.unpaid, null));
        data.put("waitReturn", tradeService.count(null, member.getTenant(), QueryStatus.unrefunded, null));
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 订单锁定
     */
    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock lock(String sn) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Order order = orderService.findBySn(sn);
        if (order != null && !order.isLocked(member)) {
            order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
            order.setOperator(member);
            orderService.update(order);
            return DataBlock.success(true, "执行成功");
        }
        return DataBlock.success(false, "执行成功");
    }
    /**
     * 确认订单
     */
    @RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock confirmOrder(Long tradeId,Long start_date) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();
        if (order.isLocked(member)) {
            return DataBlock.error("已经锁定,此订单正在处理中");
        }
        if(start_date!=null){
            SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(start_date);
            Date date = DateUtil.changeStrToDate(time);
            trade.setDeliveryDate(date);
        }
        tradeService.confirmOrder(trade,order);


        return DataBlock.success("success", "订单确认成功");
    }

    /**
     * 发货
     */
    @RequestMapping(value = "/shipping", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock shipping(Long tradeId, Long deliveryCorpId, String trackingNo, String sn, RedirectAttributes redirectAttributes, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();
        if (order.isLocked(member)) {
            return DataBlock.error("已经锁定,此订单正在处理中");
        }
        if (OrderStatus.cancelled == order.getOrderStatus()) {
            return DataBlock.error("订单已经取消!");
        }

        if (ShippingStatus.shipped == trade.getShippingStatus()) {
            return DataBlock.error("提交订单已经发货!");
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

        shipping.setDeliveryCorp("手机端发货");
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

        if (sn != null) {
            if (!sn.equals(trade.getSn())) {
                return DataBlock.error("提货码无效");
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
        shipping.setOperator("pos");
        orderService.shipping(order, shipping, null);

        if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(29L))) {
            activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(29L));
        }

        return DataBlock.success("success", "发货完成");
    }
    /**
     * 核销提货码
     */
    @RequestMapping(value = "/cancelSn", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock cancelSn(Long tradeId,String sn) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        Trade trade = tradeService.find(tradeId);
        Tenant tenant2 = trade.getTenant();
        if(tenant != tenant2 ){
            return DataBlock.error("不是本店商品不能提货");
        }
        Trade tradeSn = tradeService.findBySn(sn);
        if(trade != tradeSn ){
            return DataBlock.error("此提货码与订单不匹配");
        }
        if (trade==null) {
            return DataBlock.error("无效的id");
        }
        Order order = trade.getOrder();
        if (order.isLocked(member)) {
            return DataBlock.error("已经锁定,此订单正在处理中");
        }
        if (OrderStatus.cancelled == order.getOrderStatus()) {
            return DataBlock.error("订单已经取消!");
        }
        if (OrderStatus.unconfirmed == order.getOrderStatus()) {
            return DataBlock.error("请商家先确认订单!");
        }
        if (OrderStatus.completed == trade.getOrderStatus()) {
            return DataBlock.error("提交订单已经核销!");
        }
        if (sn != null) {
            if (!sn.equals(trade.getSn())) {
                return DataBlock.error("提货码无效");
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

        return DataBlock.success("success", "执行成功");
    }
    /**
     * 关闭
     */
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock close(Long tradeId, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();
        if (order.isLocked(member)) {
            return DataBlock.error("已经锁定,此订单正在处理中");
        }
        if (OrderStatus.cancelled == order.getOrderStatus()) {
            return DataBlock.error("订单已经取消!");
        }
        if (OrderStatus.unconfirmed != order.getOrderStatus()) {
            return DataBlock.error("不能取消进行中的订单!");
        }
        orderService.cancel(trade, member);

        if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(31L))) {
            activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(31L));
        }
        return DataBlock.success("success", "关闭订单完成");
    }

    /**
     * 拒绝
     */
    @RequestMapping(value = "/rejected", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock rejected(Long tradeId, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();

        if (order.isLocked(member)) {
            return DataBlock.error("已经锁定,此订单正在处理中");
        }
        if (OrderStatus.cancelled == order.getOrderStatus()) {
            return DataBlock.error("订单已经取消!");
        }
        SpReturns current = null;
        for (SpReturns spReturns : trade.getSpReturns()) {
            if (spReturns.getReturnStatus().equals(ReturnStatus.unconfirmed)) {
                current = spReturns;
            }
        }
        if (current == null) {
            return DataBlock.error("没找到退货申请!");
        }
        orderService.spRejected(trade, current, member);
        return DataBlock.success("success", "操作成功");
    }

    /**
     * 调价
     */
    @RequestMapping(value = "/update_price", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock updatePrice(Long tradeId, BigDecimal amount, BigDecimal freight, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();
        if (!trade.getOrderStatus().equals(OrderStatus.unconfirmed)) {
            return DataBlock.error("只能对末确认的订单进行调价");
        }
        if (order.isLocked(member)) {
            return DataBlock.error("已经锁定,此订单正在处理中");
        }
        orderService.updatePrice(trade, amount, freight, member.getUsername());

        if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(30L))) {
            activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(30L));
        }
        return DataBlock.success("success", "调价成功");
    }

    /**
     * 退货
     */
    @RequestMapping(value = "/returns", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock returns(Long tradeId, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Trade trade = tradeService.find(tradeId);
        Order order = trade.getOrder();

        if (order.isLocked(member)) {
            return DataBlock.error("已经锁定,此订单正在处理中");
        } else if (OrderStatus.cancelled == order.getOrderStatus()) {
            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(31L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(31L));
            }
            return DataBlock.error("订单已经取消");
        }
        try {
            if (trade.getShippingStatus().equals(ShippingStatus.unshipped) && trade.getPaymentStatus().equals(PaymentStatus.unpaid)) {
                orderService.cancel(trade, member);
                return DataBlock.success("success", "退货成功");
            }
            if (trade.getShippingStatus().equals(ShippingStatus.returned) || trade.getPaymentStatus().equals(PaymentStatus.refunded)) {
                return DataBlock.error("已经退货了!");
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
            	return DataBlock.error("退货处理了!");
            }
            orderService.spConfirm(trade, current, member);
 
            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(32L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(32L));
            }

            return DataBlock.success("success", "退货成功");
        } catch (BalanceNotEnoughException e) {
            // TODO Auto-generated catch block
            return DataBlock.success("success", "账户余额不足,不能完成退款");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return DataBlock.success("success", "退货失败了");
        }
    }

}