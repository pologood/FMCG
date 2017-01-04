/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Tenant;
import net.wit.service.ActivityDetailService;
import net.wit.service.ActivityRulesService;
import net.wit.service.AreaService;
import net.wit.service.CouponService;
import net.wit.service.MemberService;
import net.wit.service.PayBillService;
import net.wit.service.PromotionService;

/**
 * Controller - 代金卷
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberPayBillController")
@RequestMapping("/helper/member/payBill")
public class PayBillController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    /**
     * 代金券查列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String keywords, Date startDate, Date endDate, PayBill.Status status, PayBill.Type type, Pageable pageable, HttpServletRequest request, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:" + ERROR_VIEW;
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return "redirect:" + ERROR_VIEW;
        }

        if (status == null) {
            status = PayBill.Status.success;
        }
        Page<PayBill> page = payBillService.findMyPage(tenant, startDate, endDate, keywords, status, type, pageable);
        model.addAttribute("status", status);
        model.addAttribute("type", type);
        model.addAttribute("types", PayBill.Type.values());
        model.addAttribute("page", page);
        model.addAttribute("member", member);
        model.addAttribute("area", areaService.getCurrent());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keywords", keywords);
        return "/helper/member/payBill/list";
    }

    /**
     * 代金券查列表
     */
    @RequestMapping(value = "/list_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> listExport(String keywords, Date startDate, Date endDate, PayBill.Status status, PayBill.Type type) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd");
        if (status == null) {
            status = PayBill.Status.success;
        }
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        List<PayBill> payBills = payBillService.findMyList(tenant, startDate, endDate, keywords, status, type);
        for (PayBill payBill : payBills) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sn", payBill.getSn());
            if (payBill.getCreateDate() != null) {
                map.put("date", sdf.format(payBill.getCreateDate()));
            } else {
                map.put("date", "--");
            }
            map.put("username", payBill.getMember().getUsername());
            map.put("tenantName", payBill.getTenant().getName());
            map.put("amount", payBill.getAmount());
            map.put("total", payBill.getClearingAmount());
            if(payBill.getType()!=null){
                map.put("type", message("PayBill.Type." + payBill.getType()));
            }else{
                map.put("type","--");
            }
            map.put("discount", payBill.getDiscount());
            if(payBill.getBackDiscount()!=null){
                map.put("backDiscount", payBill.getBackDiscount());
            }else{
                map.put("backDiscount", "--");
            }
            map.put("tenantDiscount", payBill.getTenantDiscount());
            map.put("deliveryCenter", payBill.getDeliveryCenter().getName());
            map.put("status", message("PayBill.Status." + payBill.getStatus()));
            map.put("activityName", payBill.getActivityName());
            maps.add(map);
        }
        return maps;
    }


}