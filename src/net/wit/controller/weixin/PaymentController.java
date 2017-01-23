/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.BaseController;
import net.wit.domain.AuthenticodeStrategy;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Controller - 移动支付
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinPaymentController")
@RequestMapping("/weixin/payment")
public class PaymentController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    /**
     * 提交
     * @param paymentPluginId 支付插件
     * @param sn 支付单号
     * enPassword 支付密码
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock submit(String paymentPluginId, String sn, HttpServletRequest request) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Payment payment = paymentService.findBySn(sn);
            if ("weixinPayPlugin".equals(paymentPluginId)) {
                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
                if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
                    return DataBlock.warn("支付插件无效");
                }
                payment.setMethod(Method.online);
                payment.setPaymentMethod(paymentPlugin.getName());
                payment.setPaymentPluginId(paymentPluginId);
                paymentService.update(payment);
                Map<String, Object> parameters = paymentPlugin.getParameterMap(payment.getSn(), payment.getMemo(), request, "/wap");
                return DataBlock.success(parameters, "success");
            }
            if ("balancePayPlugin".equals(paymentPluginId)) {
                String password = rsaService.decryptParameter("enPassword", request);
                if (password == null) {
                    return DataBlock.warn("无效的支付密码");
                }
                if (!MD5Utils.getMD5Str(password).equals(member.getPaymentPassword())) {
                    return DataBlock.warn("无效的支付密码");
                }
                if (payment.getType().equals(Type.recharge)) {
                    return DataBlock.warn("充值业务不能使用余额支付");
                }
                if (payment.getAmount().compareTo(member.getBalance()) > 0) {
                    return DataBlock.warn("钱包余额不足，不能完成付款");
                }
                if (!payment.getStatus().equals(Status.wait)) {
                    return DataBlock.warn("已经重复发起付款操作。");
                }
                payment.setMethod(Method.deposit);
                payment.setPaymentMethod("钱包支付");
                payment.setPaymentPluginId(paymentPluginId);
                paymentService.update(payment);
                try {
                    BigDecimal amount = payment.getAmount();
                    paymentService.handle(payment);
                    if (payment.getAmount().equals(BigDecimal.ZERO)) {
                        payment.setAmount(amount);
                        payment.setStatus(Status.wait);
                        paymentService.update(payment);
                        return DataBlock.warn("钱包余额不足");
                    } else {
                        // Order order = payment.getOrder();
                        // if (order!=null&&order.getPaymentStatus().equals(PaymentStatus.paid)) {
                        //	  authenticodeStrategy.sendNotify(order);
                        //	  orderService.pushTo(order);
                        // }
                        return DataBlock.success("success", "付款成功");
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return DataBlock.error("付款失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataBlock.error("提交出错了");
    }

}