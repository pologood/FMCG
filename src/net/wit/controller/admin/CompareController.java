/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import cn.jpush.api.push.model.Platform;
import net.wit.entity.Payment;
import net.wit.entity.PlatformCapital;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.PaymentService;
import net.wit.service.PlatformCapitalService;
import net.wit.service.PluginService;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 对账单
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminCompareController")
@RequestMapping("/admin/compare")
public class CompareController extends BaseController {

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;

    @Resource(name = "platformCapitalServiceImpl")
    private PlatformCapitalService platformCapitalService;


    /**
     * 对账
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate=null;
        Date endDate=null;
        try {
            beginDate = sdf.parse(sdf.format(new Date().getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDate = sdf.parse(sdf.format(new Date().getTime()+1 * 24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String isCapital="false";
        List<PlatformCapital> platformCapitals=platformCapitalService.findListByDate(beginDate,endDate,null);
        if(platformCapitals!=null){
            if(platformCapitals.size()>0){
                isCapital="true";
            }
        }
        model.addAttribute("is_capital",isCapital);
        return "/admin/compare/index";
    }

    /**
     * 对账
     */
    @RequestMapping(value = "/compare", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> build(Integer first, Integer count) {
        long startTime = System.currentTimeMillis();
        if (first == null || first < 0) {
            first = 0;
        }
        if (count == null || count <= 0) {
            count = 50;
        }
        int buildCount = 0;
        boolean isCompleted = true;
        List<Payment> payments = paymentService.findWaitReleaseList(first, count);
        List<Payment> successData=new ArrayList<Payment>();
        List<Payment> errorData=new ArrayList<Payment>();
        List<Map<String ,Object>> abnormalData=new ArrayList<Map<String ,Object>>();
        if (payments != null) {
            for (Payment payment : payments) {
                PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());

                if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
                    try {
                        paymentService.close(payment);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        abnormalData.add(compareMap(payment));
                        e.printStackTrace();
                    }
                } else {
                    String resultCode = "";
                    try {
                        resultCode = paymentPlugin.queryOrder(payment);
                    } catch (Exception e) {
                        e.printStackTrace();
                        abnormalData.add(compareMap(payment));
                    }

                    if (!"".equals(resultCode)) {
                        if (resultCode.equals("0000")) {
                            try {
                                paymentService.handle(payment);
                                successData.add(payment);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                abnormalData.add(compareMap(payment));
                                e.printStackTrace();
                            }
                        } else if (!resultCode.equals("9999")) {
                            try {
                                paymentService.close(payment);
                                errorData.add(payment);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                abnormalData.add(compareMap(payment));
                                e.printStackTrace();
                            }
                        }
                    }
                }
                buildCount++;
                if (payments.size() == count) {
                    isCompleted = false;
                }
            }
            first += payments.size();
        }
        long endTime = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("first", first);
        map.put("buildCount", buildCount);
        map.put("buildTime", endTime - startTime);
        map.put("isCompleted", isCompleted);
        map.put("successData",successData);
        map.put("errorData",errorData);
        map.put("abnormalData",abnormalData);
        return map;
    }

    public Map<String ,Object> compareMap(Payment payment){
        Map<String ,Object> map=new HashMap<String ,Object>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        map.put("create_date",sdf.format(payment.getCreateDate()));
        map.put("order_sn",payment.getOrder()!=null?payment.getOrder().getSn():"--");
        map.put("payment_sn",payment.getSn());
        map.put("amount",payment.getAmount());
        map.put("member_name",payment.getMember()!=null?payment.getMember().getDisplayName():"--");
        map.put("tenant_name",payment.getMember()!=null?payment.getMember().getTenant()!=null?payment.getMember().getTenant().getName():"--":"--");
        if(payment.getMethod()== Payment.Method.deposit){
            map.put("method","账单支付");
        }else if(payment.getMethod()== Payment.Method.offline){
            map.put("method","先下支付");
        }else if(payment.getMethod()== Payment.Method.online){
            map.put("method","线上支付");
        }
        if(payment.getStatus()== Payment.Status.failure){
            map.put("status","支付失败");
        }else if(payment.getStatus()== Payment.Status.success){
            map.put("status","支付成功");
        }else if(payment.getStatus()== Payment.Status.wait){
            map.put("status","待支付");
        }
        map.put("payment_method",payment.getPaymentMethod());
        return map;
    }
}