/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c.member;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Coupon;
import net.wit.entity.Coupon.Status;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.service.CouponCodeService;
import net.wit.service.CouponService;
import net.wit.service.MemberService;

/**
 * Controller - 会员中心 - 优惠码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cMemberCouponCodeController")
@RequestMapping("/b2c/member/coupon_code")
public class CouponCodeController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 兑换
	 */
	@RequestMapping(value = "/exchange", method = RequestMethod.GET)
	public String exchange(Integer pageNumber, ModelMap model) {
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("page", couponService.findPage(true, true, false, pageable));
		return "b2c/member/coupon_code/exchange";
	}

	/**
	 * 兑换
	 */
	@RequestMapping(value = "/exchange", method = RequestMethod.POST)
	public @ResponseBody
	Message exchange(Long id) {
		Coupon coupon = couponService.find(id);
		if (coupon == null || !coupon.getIsEnabled() || !coupon.getIsExchange() ) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (member.getPoint() < coupon.getPoint()) {
			return Message.warn("shop.member.couponCode.point");
		}
		couponCodeService.exchange(coupon, member);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model,String status,Pageable pageable) {
		Member member = memberService.getCurrent();
		Boolean isUsed=null;
		Boolean isExpired=null;
		if("unuse".equals(status)){
	        isUsed=false;
		}else if("used".equals(status)){
			isUsed=true;
		}else if("expired".equals(status)){
			isExpired=true;
		} 
		model.addAttribute("member", member);
		model.addAttribute("page", couponCodeService.findPage(member,isUsed,isExpired, pageable));
		model.addAttribute("menu","coupon");
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("status",status);
		return "b2c/member/coupon_code/list";
	}

	//领取优惠券
	@RequestMapping(value="build",method = RequestMethod.POST)
	public @ResponseBody Message build(Long id){
		Member member = memberService.getCurrent();
		if(member==null){
			return Message.warn("请登录后领取");
		}
		Coupon coupon=couponService.find(id);
		if(coupon==null){
			return Message.warn("优惠券不存在");
		}
		if(!coupon.getExpired()){
			return Message.warn("没有可用优惠券");
		}

		List<CouponCode> couponCodes=couponCodeService.findCoupon(member,coupon);
		if(couponCodes.size()>=coupon.getReceiveTimes()){
			return Message.warn("您已领完");
		}

		List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);// 生成优惠码
		if (couponCode != null) {
			coupon.setSendCount(coupon.getSendCount() + 1);
			couponService.update(coupon);
			return Message.success("领取成功");
		} else {
			return Message.error("领取失败");
		}
	}

}