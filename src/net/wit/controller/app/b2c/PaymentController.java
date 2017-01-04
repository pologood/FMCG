/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2c;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Payment;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.SnService;

/**
 * Controller - 移动支付
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2cPaymentController")
@RequestMapping("/app/b2c/payment")
public class PaymentController extends BaseController {

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    /**
     * 提交
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock submit(String paymentPluginId, String sn, HttpServletRequest request) {
        try {
            Payment payment = paymentService.findBySn(sn);
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
            if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
                return DataBlock.error("支付插件无效");
            }
            payment.setPaymentMethod("微信支付");
            payment.setPaymentPluginId(paymentPluginId);
            paymentService.save(payment);
            Map<String, Object> data = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request, "/wap");
            data.put("sn", payment.getSn());
            if ("".equals(data.get("code_url"))) {
                return DataBlock.error("提交微信服务器失败");
            } else {
                return DataBlock.success(data, "执行成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataBlock.error("提交支付异常");
    }

    /**
     * 提交
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock query(String sn, HttpServletRequest request) {
        Payment payment = paymentService.findBySn(sn);
        try {
            if(payment!=null){
                String paymentPluginId = payment.getPaymentPluginId();
                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
                if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
                    return DataBlock.error("支付插件无效");
                }
                String resp = paymentPlugin.queryOrder(payment);
                if (resp.equals("0000")) {
                    return DataBlock.success("success", "收款成功");
                }
                if (resp.equals("0001")) {
                    return DataBlock.error("支付失败");
                }
                if (resp.equals("9999")) {
                    return DataBlock.warn("等待支付");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataBlock.error("查询出错");
    }

}