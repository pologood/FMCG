/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.entity.Rebate;
import net.wit.service.RebateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Profit;
import net.wit.entity.Profit.Status;
import net.wit.service.AdminService;
import net.wit.service.ProfitService;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 分润核算
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminProfitController")
@RequestMapping("/admin/profit")
public class ProfitShareController extends BaseController {

	@Resource(name = "profitServiceImpl")
	private ProfitService profitService;
	
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "rebateServiceImpl")
	private RebateService rebateService;



	/**
	 * 分润列表
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(String beginDate, String endDate,String rebate_status,String rebate_type,String order_type,ModelMap model,String keyword,Pageable pageable) {
		Date begin_date = null;
		Date end_date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginDate)) {
			try {
				begin_date = sdf.parse(beginDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(endDate)) {
			try {
				end_date = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Rebate.Status rebateStatus=null;
		if("none".equals(rebate_status)){
			rebateStatus= Rebate.Status.none;
		}else if("success".equals(rebate_status)){
			rebateStatus= Rebate.Status.success;
		}
		Rebate.Type rebateType=null;
		if("platform".equals(rebate_type)){
			rebateType= Rebate.Type.platform;
		}else if("extension".equals(rebate_type)){
			rebateType= Rebate.Type.extension;
		}else if("sale".equals(rebate_type)){
			rebateType= Rebate.Type.sale;
		}else if("rebate".equals(rebate_type)){
			rebateType= Rebate.Type.rebate;
		}
		Rebate.OrderType orderType=null;
		if("none".equals(order_type)){
			orderType= Rebate.OrderType.none;
		}else if("order".equals(order_type)){
			orderType= Rebate.OrderType.order;
		}else if("payBill".equals(order_type)){
			orderType= Rebate.OrderType.payBill;
		}else if("coupon".equals(order_type)){
			orderType= Rebate.OrderType.coupon;
		}
		List<Filter> filter = new ArrayList<Filter>();
		filter.add(new Filter("status", Filter.Operator.eq, rebateStatus));
		filter.add(new Filter("type", Filter.Operator.eq, rebateType));
		filter.add(new Filter("orderType", Filter.Operator.eq, orderType));
		pageable.setFilters(filter);
		Page<Rebate> page=rebateService.findPage(begin_date, end_date,keyword, pageable);
		model.addAttribute("status",rebate_status);
		model.addAttribute("order_type",order_type);
		model.addAttribute("rebate_type",rebate_type);
		model.addAttribute("beginDate", begin_date);
		model.addAttribute("endDate", end_date);
		model.addAttribute("keyword",keyword);
		model.addAttribute("page",page);
		return "/admin/profit/index";
	}
	/**
	 * 分润列表导出
	 */
	@RequestMapping(value = "/index_export", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> indexExport(String beginDate, String endDate, String rebate_status, String rebate_type, String order_type, String keyword) {
		List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
		Date begin_date = null;
		Date end_date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginDate)) {
			try {
				begin_date = sdf.parse(beginDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isNotBlank(endDate)) {
			try {
				end_date = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Rebate.Status rebateStatus=null;
		if("none".equals(rebate_status)){
			rebateStatus= Rebate.Status.none;
		}else if("success".equals(rebate_status)){
			rebateStatus= Rebate.Status.success;
		}
		Rebate.Type rebateType=null;
		if("platform".equals(rebate_type)){
			rebateType= Rebate.Type.platform;
		}else if("extension".equals(rebate_type)){
			rebateType= Rebate.Type.extension;
		}else if("sale".equals(rebate_type)){
			rebateType= Rebate.Type.sale;
		}else if("rebate".equals(rebate_type)){
			rebateType= Rebate.Type.rebate;
		}
		Rebate.OrderType orderType=null;
		if("none".equals(order_type)){
			orderType= Rebate.OrderType.none;
		}else if("order".equals(order_type)){
			orderType= Rebate.OrderType.order;
		}else if("payBill".equals(order_type)){
			orderType= Rebate.OrderType.payBill;
		}else if("coupon".equals(order_type)){
			orderType= Rebate.OrderType.coupon;
		}
		List<Filter> filter = new ArrayList<Filter>();
		filter.add(new Filter("status", Filter.Operator.eq, rebateStatus));
		filter.add(new Filter("type", Filter.Operator.eq, rebateType));
		filter.add(new Filter("orderType", Filter.Operator.eq, orderType));
		List<Rebate> rebates=rebateService.findList(begin_date, end_date,keyword,filter);
		for(Rebate rebate:rebates){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("create_date",sdf.format(rebate.getCreateDate()));
			map.put("description",rebate.getDescription());
			if(rebate.getOrderType()==Rebate.OrderType.none){
				map.put("order_type","其他");
			}else if(rebate.getOrderType()==Rebate.OrderType.coupon){
				map.put("order_type","代金券");
			}else if(rebate.getOrderType()==Rebate.OrderType.order){
				map.put("order_type","订单分润");
			}else if(rebate.getOrderType()==Rebate.OrderType.payBill){
				map.put("order_type","优惠买单");
			}
			if(rebate.getType()==Rebate.Type.platform){
				map.put("rebate_type","平台佣金");
			}else if(rebate.getType()==Rebate.Type.extension){
				map.put("rebate_type","推广佣金");
			}else if(rebate.getType()==Rebate.Type.sale){
				map.put("rebate_type","销售佣金");
			}else if(rebate.getType()==Rebate.Type.rebate){
				map.put("rebate_type","销售分润");
			}
			if(rebate.getStatus()== Rebate.Status.none){
				map.put("rebate_status","未到账");
			}else{
				map.put("rebate_status","已到帐");
			}
			if(rebate.getTrade()!=null){
				map.put("order_sn",rebate.getTrade().getOrder().getSn());
			}else{
				map.put("order_sn","--");
			}
			if(rebate.getPayBill()!=null){
				map.put("paybill_sn",rebate.getPayBill().getSn());
			}else{
				map.put("paybill_sn","--");
			}
			if(rebate.getCouponCode()!=null){
				map.put("coupon_code",rebate.getCouponCode().getCode());
			}else{
				map.put("coupon_code","--");
			}
			if(rebate.getMember()!=null){
				map.put("name",rebate.getMember().getName());
			}else{
				map.put("name","--");
			}
			if(rebate.getAmount()!=null){
				map.put("amount",rebate.getAmount());
			}else{
				map.put("amount","0");
			}
			if(rebate.getBrokerage()!=null){
				map.put("brokerage",rebate.getBrokerage());
			}else{
				map.put("brokerage","0");
			}
			try {
				if(rebate.getOrderType()==Rebate.OrderType.none){
                    map.put("tenant_name","--");
                }else if(rebate.getOrderType()==Rebate.OrderType.coupon){
                    if(rebate.getCouponCode()!=null){
                        map.put("tenant_name",rebate.getCouponCode().getCoupon().getTenant().getName());
                    }else{
                        map.put("tenant_name","--");
                    }
                }else if(rebate.getOrderType()==Rebate.OrderType.order){
                    if(rebate.getTrade()!=null){
                        map.put("tenant_name",rebate.getTrade().getTenant().getName());
                    }else{
                        map.put("tenant_name","--");
                    }
                }else if(rebate.getOrderType()==Rebate.OrderType.payBill){
                    if(rebate.getPayBill()!=null){
                        map.put("tenant_name",rebate.getPayBill().getTenant().getName());
                    }else{
                        map.put("tenant_name","--");
                    }
                }
			} catch (Exception e) {
				map.put("tenant_name","--");
			}
			map.put("percent",rebate.getPercent().multiply(new BigDecimal(100)));
			maps.add(map);
		}
		return maps;
	}

//	/**
//	 * 对账
//	 */
//	@RequestMapping(value = "/calc", method = RequestMethod.POST)
//	public @ResponseBody
//	Map<String, Object> build(Integer level) {
//		long startTime = System.currentTimeMillis();
//		boolean isCompleted = true;
//              Calendar cal=Calendar.getInstance();
//              cal.add(Calendar.DATE,-1);
//              Date endDate = cal.getTime();
//              
// 		      int buildCount = profitService.share(null,endDate, level);
//
//			  if (buildCount == 20) {
//				 isCompleted = false;
//			  }
//
//		long endTime = System.currentTimeMillis();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("buildTime", endTime - startTime);
//		map.put("isCompleted", isCompleted);
//		return map;
//	}
	/**
	 * 分润详情
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(ModelMap model,Long id) {
		Profit profit=profitService.find(id);
		if(profit==null){
			return "redirect:/admin/profit/index.jhtml";
		}
		model.addAttribute("profit",profit);
		return "/admin/profit/detail";
	}
}