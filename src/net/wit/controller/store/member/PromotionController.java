/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.store.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;
import net.wit.entity.Tenant;
import net.wit.service.ActivityDetailService;
import net.wit.service.ActivityRulesService;
import net.wit.service.MemberService;
import net.wit.service.PromotionService;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller - 满包邮
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeMemberPromotionController")
@RequestMapping("/store/member/promotion")
public class PromotionController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Type type, Pageable pageable, HttpServletRequest request, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:"+ERROR_VIEW;
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return "redirect:"+ERROR_VIEW;
        }
        String url = "";
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenant", Operator.eq, tenant));
        filters.add(new Filter("type", Operator.eq, type));
        pageable.setFilters(filters);
        Page<Promotion> page = promotionService.findPage(pageable);
        if (type.equals(Type.mail)) {
            if (!page.getContent().isEmpty()) {
                if ("weixin".equals(type)) {
                    url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index/" + tenant.getId().toString() + ".jhtml?extension=" + (member != null ? member.getUsername() : "")));
                } else {
                    url = bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index/" + tenant.getId().toString() + ".jhtml?extension=" + (member != null ? member.getUsername() : "");
                }
                model.addAttribute("title", "亲,“" + tenant.getShortName() + "”全场包邮了，快快抢购吧。");
                model.addAttribute("thumbnail", tenant.getLogo());
                model.addAttribute("description", page.getContent().get(0).getName());
                model.addAttribute("url", url);
                model.addAttribute("shareAppKey", bundle.getString("shareAppKey"));
                model.addAttribute("promotionMailModel", page.getContent().get(0));
                model.addAttribute("member", member);
                model.addAttribute("menu","freight_free");
                return "/store/member/promotion/list";
            }
            return "redirect:add.jhtml?type=mail";
        }
        return "redirect:"+ERROR_VIEW;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Type type, Pageable pageable, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenant", Operator.eq, tenant));
        filters.add(new Filter("type", Operator.eq, type));
        pageable.setFilters(filters);
        Page<Promotion> page = promotionService.findPage(pageable);
        if (type.equals(Type.mail)) {
            if (!page.getContent().isEmpty()) {
                model.addAttribute("promotionMailModel", page.getContent().get(0));
            }
            model.addAttribute("member",member);
            model.addAttribute("menu","freight_free");
            return "/store/member/promotion/add";
        }
        return ERROR_VIEW;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Type type, Promotion model, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        if (Type.mail.equals(type)) {
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter("tenant", Operator.eq, tenant));
            List<Promotion> promotions = promotionService.findList(Type.mail, null, null, null, null, filters, null);
            Promotion promotion = null;
            for (Promotion p : promotions) {
                if (promotion == null) {
                    promotion = p;
                } else {
                    promotionService.delete(p);
                }
            }
            if (promotion == null) {
                promotion = new Promotion();
            }

            String _name="消费满" + model.getMinimumPrice() + "元包邮";
            if(model.getMinimumPrice().compareTo(BigDecimal.ZERO)==0){
                _name= "全场包邮";
            }

            promotion.setTenant(tenant);
            promotion.setName(_name);
            promotion.setTitle(promotion.getName());
            promotion.setMinimumQuantity(null);
            promotion.setMaximumQuantity(null);
            promotion.setMinimumPrice(model.getMinimumPrice());
            promotion.setMaximumPrice(null);
            promotion.setIsFreeShipping(true);
            promotion.setIsCouponAllowed(true);
            promotion.setType(type);
            promotion.setMember(member);
            promotionService.save(promotion);

            if(!activityDetailService.isActivity(null,tenant,activityRulesService.find(22L))){
                activityDetailService.addPoint(null,tenant,activityRulesService.find(22L));
            }
        } else {
            return ERROR_VIEW;
        }
        return "redirect:list.jhtml?type=mail";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock delete(Long id, HttpServletRequest request) {
        Promotion promotion = promotionService.find(id);
        promotionService.delete(promotion);
        return DataBlock.success("success", "删除成功");
    }

}