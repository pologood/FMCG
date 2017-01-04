/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Union;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;

import net.wit.util.DesUtils;
import net.wit.util.HttpClientUtil;
import net.wit.util.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 收款单
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminPaymentController")
@RequestMapping("/admin/payment")
public class PaymentController extends BaseController {

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        model.addAttribute("payment", paymentService.find(id));
        return "/admin/payment/view";
    }

    /**
     * 检查支付状态
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Payment payment = paymentService.find(id);
        if (payment != null) {
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
            if (paymentPlugin != null) {
                String status = paymentPlugin.queryOrder(payment);
                if ("0000".equals(status)) {
                    try {
                        paymentService.handle(payment);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if ("0001".equals(status)) {
                    try {
                        paymentService.close(payment);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                if (payment.getPaymentPluginId().equals("op_service_alipay_submit") || payment.getPaymentPluginId().equals("op_service_wechat_submit")) {
                    try {
                        paymentService.opService(payment);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        model.addAttribute("payment", payment);
        addFlashMessage(redirectAttributes, net.wit.Message.success("对账成功"));
        return "redirect:view.jhtml?id=" + id;
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Date beginDate, Date endDate, Method method, Status status, String keyword,Pageable pageable, ModelMap model) {
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("method", method);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", paymentService.findPage(method, status, beginDate, endDate, keyword, pageable));
        return "/admin/payment/list";
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
                Payment payment = paymentService.find(id);
                if (payment != null && payment.getExpire() != null && !payment.hasExpired()) {
                    return Message.error("admin.payment.deleteUnexpiredNotAllowed");
                }
            }
            paymentService.delete(ids);
        }
        return SUCCESS_MESSAGE;
    }

}