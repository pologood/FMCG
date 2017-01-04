/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.*;
import net.wit.Filter.Operator;
import net.wit.controller.app.model.*;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.*;
import net.wit.controller.assistant.model.CouponCodeModel;
import net.wit.controller.assistant.model.CouponModel;
import net.wit.controller.assistant.model.CouponSumerModel;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.Coupon.Status;
import net.wit.entity.Coupon.Type;
import net.wit.entity.model.*;
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
@Controller("assistantMemberCouponController")
@RequestMapping("/assistant/member/coupon")
public class CouopnController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;
	
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;
	
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "couponNumberServiceImpl")
	private CouponNumberService couponNumberService;
	
	/**Promotion
	 * 
	 * 买家端-查询我已领的优惠券列表 
	*/
	
	@RequestMapping(value = "/conpons", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock conpons(Pageable pageable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}		
//		List<CouponCode> codeponCodes =couponCodeService.findEnabledList(member);
		Page<CouponCode> page=couponCodeService.findPage(member,false,false,pageable);
		return DataBlock.success(CouponCodeModel.bindData(page.getContent()),"执行成功");
	}
	
	/**
	 * 领取优惠券
	 * @param couponId	代金券Id
	 * @return
     */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public DataBlock get(Long couponId){
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

	/**Promotion
	 *
	 * 商家端-查询优惠券列表
	 * params type { 店内红包 tenantCoupon }
	 * params Status { 可领用canUse,未开始 unBegin,已领完 unUsed,已结束 Expired}
	*/
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Type type,String status,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		couponService.refreshStatus(tenant);
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("status", Operator.eq, Status.confirmed));
		filters.add(new Filter("tenant", Operator.eq, tenant));
		filters.add(new Filter("type", Operator.eq, type));
		pageable.setFilters(filters);
		if(status==null){
			status="canUse";
		}
		Page<Coupon> page =couponService.findPage(status,pageable);
		return DataBlock.success(CouponModel.bindData(page.getContent()),"执行成功");
	}

	/**Promotion
	 *
	 * 商家端-单个优惠券的统计
	 * params isUsed,使用情 true ，未使用 false}
	 * params id 优惠券的 id
	*/

	@RequestMapping(value = "/sumer", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock sumer(Boolean isUsed,Long id,Pageable pageable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Coupon coupon = couponService.find(id);
		if (coupon==null) {
			return DataBlock.error("无效活动优惠券id");
		}
		Page<CouponCode> page =couponCodeService.sumerStatistics(coupon,isUsed, pageable);
		return DataBlock.success(CouponSumerModel.bindData(page.getContent()),page,"执行成功");
	}
	/**Promotion
	 *
	 * 商家端-单个优惠券的统计
	 * params type {领用情况 send,使用情况 used }
	 * params id 优惠券的 id
	 */

	@RequestMapping(value = "/sumer2", method = RequestMethod.GET)
	@ResponseBody
	public net.wit.controller.app.model.DataBlock sumer(net.wit.entity.model.CouponSumerModel.Type type, Long id, Pageable pageable, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return net.wit.controller.app.model.DataBlock.error(net.wit.controller.app.model.DataBlock.SESSION_INVAILD);
		}
		Coupon coupon = couponService.find(id);
		if (coupon==null) {
			return net.wit.controller.app.model.DataBlock.error("无效活动优惠券id");
		}
		Page<net.wit.entity.model.CouponSumerModel> page =couponService.sumer(coupon, type, pageable);
		Map<String,Object> data = new HashMap<String,Object>();
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("coupon", Operator.eq, coupon));
		data.put("total",couponService.count(coupon,type));
		data.put("items",page.getContent());
		return net.wit.controller.app.model.DataBlock.success(data,"执行成功");
	}
	/**Promotion
	 *
	 * 商家端-添加优惠券
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(CouponModel model,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}

		Coupon coupon = new Coupon();
		coupon.setAmount(model.getAmount());
		coupon.setCount(model.getCount());
		coupon.setEndDate(model.getEndDate());
		coupon.setStartDate(model.getStartDate());
		coupon.setIsEnabled(true);
		coupon.setMinimumQuantity(null);
		coupon.setMaximumQuantity(null);
		coupon.setMinimumPrice(model.getMinimumPrice());
		coupon.setMaximumPrice(null);
		coupon.setName(model.getName());
		coupon.setUsedCount(0);
		coupon.setSendCount(0);
		coupon.setPoint(0L);
		coupon.setEffectiveDays(0);
		coupon.setIsReceiveMore(true);
		coupon.setReceiveTimes(model.getReceiveTimes());
		coupon.setPrefix("c");
		coupon.setStatus(Status.confirmed);
		coupon.setIntroduction(model.getIntroduction());
		coupon.setPriceExpression("price-".concat(model.getAmount().toString()));
		coupon.setTenant(tenant);
		coupon.setType(model.getType());
		coupon.setIsExchange(false);
		couponService.save(coupon);
		if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(26L))) {
			activityDetailService.addPoint(null, tenant, activityRulesService.find(26L));
		}
		return DataBlock.success("success","保存成功");
	}

	/**Promotion
	 * 
	 * 商家端-删除优惠券
	 * params id 优惠券的 id	 
	*/
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(Long id,HttpServletRequest request) {
		Coupon coupon = couponService.find(id);
		if (coupon.getCouponCodes().size()>0) {
			return DataBlock.error("已领用不能删除");
		}
		couponService.delete(coupon);
		return DataBlock.success("success","删除成功");
	}
	
	/**Promotion
	 * 
	 * 商家端-分享优惠券
	 * params type {weixin 微信分享}	 
	*/
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock share(String type,Long id,HttpServletRequest request) {
		Coupon coupon = couponService.find(id);
		if (coupon == null) {
			return DataBlock.error("优惠券id不存在");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = "";
	    if (!"app".equals(type)) {
	    	url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/coupon/bshare.jhtml?id=" + id + "&extension=" + (member != null ? member.getUsername() : "")));
	    } else {
	    	url = bundle.getString("WeiXinSiteUrl") + "/weixin/coupon/bshare.jhtml?id=" + id + "&extension=" + (member != null ? member.getUsername() : "");
	    }
	    
	    Map<String,String> data = new HashMap<String,String>();
	    data.put("url",url);
	    data.put("title","亲,“"+tenant.getShortName()+"”请您快来领代金券。");
	    data.put("thumbnail",tenant.getLogo()+"@200w_200h_1e_1c_100Q");
	    data.put("description",coupon.getIntroduction());
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 绑定平台套券
	 * @param id  coupon编号
	 * @param no  扫码获取的序号
	 */
	@RequestMapping(value = "/bound", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock bound(Long id, Long no, HttpServletRequest request) {
		Coupon coupon=couponService.find(id);
		if(coupon==null){
			return DataBlock.error("无效的优惠券编码");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		List<CouponNumber> couponNumbers = couponNumberService.findList(coupon,null,null,no);
		if(couponNumbers!=null&&couponNumbers.size()>0){
			return DataBlock.error("此平台券此前已登记");
		}
		CouponCode couponCode = couponCodeService.build(coupon, member, 1, no, CouponNumber.Status.bound);// 生成优惠码
		if (couponCode == null) {
			return DataBlock.error("登记失败");
		}
		return  DataBlock.success("success","登记成功");
	}

	/**
	 * 查询已被领取的套券信息
	 */
	@RequestMapping(value = "/multiple/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock multipleList(Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Page<CouponNumber> page=couponNumberService.openPage(null,null,member,null,pageable);
		return DataBlock.success(MultipleCouponModel.bindData(page.getContent()),"执行成功");
	}
}