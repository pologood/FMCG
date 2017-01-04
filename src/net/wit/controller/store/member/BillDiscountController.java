/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.helper.member.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Promotion;
import net.wit.entity.Promotion.Type;
import net.wit.entity.Tenant;
import net.wit.service.CouponService;
import net.wit.service.MemberService;
import net.wit.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Controller - 买单折扣
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeMemberBillDiscountController")
@RequestMapping("/store/member/bill/discount")
public class BillDiscountController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable,ModelMap model) {
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
		filters.add(new Filter("type", Operator.eq, Type.discount));
		pageable.setFilters(filters);
		pageable.setSearchProperty("name");
		Page<Promotion> page = promotionService.findPage(pageable);

		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		String url=bundle.getString("WeiXinSiteUrl") + "/wap/product/content/ID/product.jhtml?extension=" + (member != null ? member.getUsername() : "");
		model.addAttribute("member", member);
		model.addAttribute("page", page);
		model.addAttribute("url", url);
		model.addAttribute("title","亲,“"+tenant.getShortName()+"”限时折扣开始了，快快抢购吧。");
		model.addAttribute("tenantId", tenant.getId());
		model.addAttribute("shareAppKey", bundle.getString("shareAppKey"));
		model.addAttribute("menu","bill_discount");
		return "/store/member/bill_discount/list";

	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Promotion promotion,ModelMap modelMap) {
		modelMap.addAttribute("promotion",promotion);
		modelMap.addAttribute("menu","bill_discount");
		return "/store/member/bill_discount/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Promotion promotion,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}

		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return ERROR_VIEW;
		}

		if(promotionService.isPromotion(Type.discount,tenant,promotion.getBeginDate(),promotion.getEndDate())){
			addFlashMessage(redirectAttributes, Message.error("当前时间段存在买单折扣活动，请重新设置！"));
			String introduction = promotion.getIntroduction()==null?"":"&introduction="+promotion.getIntroduction();
			return "redirect:add.jhtml?name="+promotion.getName()+"&agioRate="+promotion.getAgioRate()+"&backRate="+promotion.getBackRate()+introduction;
		}

		promotion.setTenant(tenant);
		promotion.setTitle(promotion.getName());
		promotion.setMinimumQuantity(null);
		promotion.setMaximumQuantity(null);
		promotion.setMinimumPrice(null);
		promotion.setMaximumPrice(null);
		promotion.setIsFreeShipping(false);
		promotion.setIsCouponAllowed(true);
		promotion.setType(Type.discount);
		promotion.setMember(member);
		promotionService.save(promotion);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		try {
			promotionService.delete(ids);
		} catch (Exception e) {
			return ERROR_MESSAGE;
		}
		return SUCCESS_MESSAGE;
	}

}