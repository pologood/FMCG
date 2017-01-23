/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin.member;

import net.wit.Setting;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Controller - 移动支付
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinMemberCashierController")
@RequestMapping("/weixin/member/cashier")
public class CashierController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    /**
     * 微信扫码支付提交
     *
     * @param paymentPluginId （微信扫码支付插件：weixinQrcodePayPlugin）
     * @param amount          支付金额
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock submit(String paymentPluginId, BigDecimal amount, HttpServletRequest request) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Tenant tenant = member.getTenant();
            if (tenant == null) {
                return DataBlock.error(DataBlock.TENANT_INVAILD);
            }
            Member owner = tenant.getMember();
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
            if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
                return DataBlock.error("支付插件无效");
            }
            Payment payment = new Payment();
            String description = "线下代收";
            Setting setting = SettingUtils.get();
            if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > setting.getPriceScale()) {
                return DataBlock.error("amount.error");
            }
            payment.setMember(owner);
            payment.setPayer(member.getName());
            payment.setMemo("线下代收");
            payment.setSn(snService.generate(Sn.Type.payment));
            payment.setType(Type.cashier);
            payment.setMethod(Method.online);
            payment.setStatus(Status.wait);
            payment.setPaymentMethod(paymentPlugin.getPaymentName());
            payment.setFee(paymentPlugin.calculateFee(amount.setScale(2)));
            payment.setAmount(amount.setScale(2));
            payment.setPaymentPluginId(paymentPluginId);
            payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
            paymentService.save(payment);
            Map<String, Object> data = paymentPlugin.getParameterMap(payment.getSn(), description, request, "/wap");
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
     * 查询支付状态
     */
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock query(String sn) {
        Payment payment = paymentService.findBySn(sn);
        try {
            String paymentPluginId = payment.getPaymentPluginId();
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataBlock.error("查询出错");
    }

}