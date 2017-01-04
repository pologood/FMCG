/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2c;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.CouponCodeModel;
import net.wit.controller.app.model.CouponModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Coupon;
import net.wit.entity.Coupon.Status;
import net.wit.entity.Coupon.Type;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.entity.model.CouponSumerModel;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

/**
 * Controller - 我的优惠券
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2cCouponController")
@RequestMapping("/app/b2c/coupon")
public class ConponController extends BaseController {

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	/**
	 * 买家端-查询指定店铺优惠券列表
	 *
	 * @param tenantId 店铺Id
	 * @param type { 店内红包 tenantCoupon }
	 * @param status { 可领用canUse,未开始 unBegin,已领完 unUsed,已结束 Expired}
	 * @param pageable 分页信息
	 *
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long tenantId,Type type,Status status,Pageable pageable,HttpServletRequest request) {
		Member member=memberService.getCurrent();
		Tenant tenant = tenantService.find(tenantId);
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		
		couponService.refreshStatus(tenant);
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("status", Operator.eq, status));
		filters.add(new Filter("tenant", Operator.eq, tenant));
		filters.add(new Filter("type", Operator.eq, type));
		pageable.setFilters(filters);
		Page<Coupon> page =couponService.findPage(pageable);
		List<CouponModel> models = new ArrayList<>();
		for (Coupon coupon:page.getContent()) {
			CouponModel model = new CouponModel();
			model.copyFrom(coupon);
			if(member!=null){
				Long hasReceiveTimes=0L;
				for(CouponCode couponCode:coupon.getCouponCodes()){
					if(couponCode.getMember()==member){
						hasReceiveTimes++;
					}
				}
				model.setHasReceiveTimes(hasReceiveTimes);
				if(hasReceiveTimes<coupon.getReceiveTimes()){
					models.add(model);
				}
			}else{
				models.add(model);
			}

		}
		return DataBlock.success(models,"执行成功");
	}

	/**
	 * 领取优惠券
	 * @param couponId	代金券Id
	 * @return
     */
	@RequestMapping(value = "/pickup", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock pickup(Long couponId){
		Member member=memberService.getCurrent();
		if(member==null){
			return DataBlock.error("请登录后领取");
		}
		Coupon coupon=couponService.find(couponId);
		if(coupon==null){
			return DataBlock.error("优惠券不存在");
		}
		if(!coupon.getExpired()){
			return DataBlock.error("没有可用优惠券");
		}
		List<CouponCode> couponCodes=couponCodeService.findCoupon(member,coupon);
		if(couponCodes.size()>=coupon.getReceiveTimes()){
			return DataBlock.error("您已领完");
		}
		List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);// 生成优惠码
		if (couponCode == null) {
			return DataBlock.error("领取失败");
		}
		coupon.setSendCount(coupon.getSendCount() + 1);
		couponService.update(coupon);
		return DataBlock.success("success","领取成功");
	}

	
}