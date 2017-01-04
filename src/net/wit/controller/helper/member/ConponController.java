/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Coupon;
import net.wit.entity.Coupon.Status;
import net.wit.entity.Coupon.Type;
import net.wit.entity.Member;
import net.wit.entity.Promotion;
import net.wit.entity.Tenant;
import net.wit.entity.model.CouponSumerModel;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller - 代金卷
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberCouponController")
@RequestMapping("/helper/member/coupon")
public class ConponController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    /**
     * 代金券查列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Type type, Status status, String processStatus, Pageable pageable, HttpServletRequest request, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:"+ERROR_VIEW;
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return "redirect:"+ERROR_VIEW;
        }

        couponService.refreshStatus(tenant);

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("status", Operator.eq, status));
        filters.add(new Filter("tenant", Operator.eq, tenant));
        filters.add(new Filter("type", Operator.eq, type));
        pageable.setFilters(filters);
        if(processStatus==null){
            processStatus="canUse";
        }
        Page<Coupon> page = couponService.findPage(processStatus,pageable);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url="";
        if ("weixin".equals(type)) {
//            url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/coupon/view/" + id + ".jhtml?extension=" + (member != null ? member.getUsername() : "")));
        } else {
            url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/coupon/view.jhtml?id=ID&extension=" + (member != null ? member.getUsername() :"")));
        }
        model.addAttribute("title", "亲,“" + tenant.getShortName() + "”请您快来领代金券。");
        model.addAttribute("thumbnail", tenant.getLogo());
        model.addAttribute("url", url);
        model.addAttribute("shareAppKey", bundle.getString("shareAppKey"));
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("statusValues", Status.values());
        model.addAttribute("processStatus", processStatus);
        model.addAttribute("page", page);
        model.addAttribute("member", member);
        return "/helper/member/coupon/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(String status, Type type, ModelMap model) {
    	Member member = memberService.getCurrent();
        model.addAttribute("status", status);
        model.addAttribute("type", type);
        model.addAttribute("member", member);
        return "/helper/member/coupon/add";
    }

    /**
     * 添加代金券
     *
     * @param coupon
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Coupon coupon, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }

        coupon.setIsEnabled(true);
        coupon.setMinimumQuantity(null);
        coupon.setMaximumQuantity(null);
        coupon.setMaximumPrice(null);
        coupon.setName(coupon.getAmount() + "元代金券");
        coupon.setUsedCount(0);
        coupon.setSendCount(0);
        coupon.setPoint(0L);
        coupon.setEffectiveDays(0);
        coupon.setIsReceiveMore(true);
        coupon.setPrefix("c");
        coupon.setType(Type.tenantCoupon);
        coupon.setStatus(Status.confirmed);
        coupon.setPriceExpression("price-".concat(coupon.getAmount().toString()));
        coupon.setTenant(tenant);
        coupon.setIsExchange(false);
        couponService.save(coupon);

        if(!activityDetailService.isActivity(null,tenant,activityRulesService.find(26L))){
            activityDetailService.addPoint(null,tenant,activityRulesService.find(26L));
        }

        return "redirect:list.jhtml?type=" + coupon.getType() + "&status=" + Status.confirmed;
    }

    /**
     * 删除代金券
     *
     * @param ids
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message add(Long[] ids, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        for (Long id : ids) {
            Coupon coupon = couponService.find(id);
            if(coupon==null){
            	return Message.error("找不到该优惠券");
            }
            if (coupon.getCouponCodes().size() > 0) {
                return Message.error("已领用券不能删除");
            }
        }
        couponService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    /**
     * 统计
     *
     * @param type
     * @param id
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "/sumer", method = RequestMethod.GET)
    public String sumer(CouponSumerModel.Type type, Long id, String status, Pageable pageable, HttpServletRequest request, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return ERROR_VIEW;
        }
        Page<CouponSumerModel> page = couponService.sumer(coupon, type, pageable);
        model.addAttribute("type", type);
        model.addAttribute("id", id);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        model.addAttribute("total", couponService.count(coupon, type));
        model.addAttribute("member", member);
        return "/helper/member/coupon/sumer";
    }

}