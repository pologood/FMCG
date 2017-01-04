/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c.member;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Mobile;
import net.wit.entity.MobilePrice;
import net.wit.entity.Sn;
import net.wit.service.MemberService;
import net.wit.service.MobilePriceService;
import net.wit.service.MobileService;
import net.wit.service.SmsSendService;
import net.wit.service.SnService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cMemberMobileController")
@RequestMapping("/b2c/member/mobile")
public class MobileController extends BaseController {
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;


	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "mobileServiceImpl")
	private MobileService mobileService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "mobilePriceServiceImpl")
	private MobilePriceService mobilePriceService;
	
	public MobilePrice findPrice(List<MobilePrice> prc,BigDecimal amount) {
		Iterator<MobilePrice> iter = prc.iterator();  
		while(iter.hasNext()){  
			MobilePrice mobilePrice = iter.next();
			if (amount.equals(new BigDecimal(mobilePrice.getDenomination().toString()))) {
				return mobilePrice;
			}
		}  
		return null;
	}
	/**
	 * 计算支付金额
	 * 
	 * @param amount
	 *            金额
	 * @return 支付金额
	 */
	public Map<String, BigDecimal>  calcFee(String mobile,BigDecimal amount) {
		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
	    List<MobilePrice> prices =	mobilePriceService.findbyMobile(mobile);
    	map.put("fee",new BigDecimal(0));
    	map.put("cost",new BigDecimal(0));
	    if (prices!=null && !prices.isEmpty()) {
	   
	    		 BigDecimal tmp = amount;
	    		 BigDecimal fee = new BigDecimal(0);
	    		 BigDecimal cost = new BigDecimal(0);
	   	         MobilePrice p500 =	findPrice(prices,new BigDecimal(500));
	    		 while ((tmp.compareTo(new BigDecimal(500))>=0) && (p500!=null))  {
		   	         tmp = tmp.subtract(new BigDecimal(500));
		   	         fee = fee.add(p500.getPrice());
		   	         cost = cost.add(p500.getCost()); 
	    		 }

	    		 MobilePrice p300 =	findPrice(prices,new BigDecimal(300));
	    		 while ((tmp.compareTo(new BigDecimal(300))>=0) && (p300!=null))  {
	    			 tmp = tmp.subtract(new BigDecimal(300));
	    			 fee= fee.add(p300.getPrice());
	    			 cost = cost.add(p300.getCost()); 
	    		 }

	   	         MobilePrice p200 =	findPrice(prices,new BigDecimal(200));
	    		 while ((tmp.compareTo(new BigDecimal(200))>=0) && (p200!=null))  {
	    			 tmp = tmp.subtract(new BigDecimal(200));
	    			 fee= fee.add(p200.getPrice());
	    			 cost = cost.add(p200.getCost()); 
	    		 }

	    		 MobilePrice p100 =	findPrice(prices,new BigDecimal(100));
	    		 while ((tmp.compareTo(new BigDecimal(100))>=0) && (p100!=null))  {
	    			 tmp = tmp.subtract(new BigDecimal(100));
	    			 fee= fee.add(p100.getPrice());
	    			 cost = cost.add(p100.getCost()); 
	    		 }
	    		 
	   	         MobilePrice p50 =	findPrice(prices,new BigDecimal(50));
	    		 while ((tmp.compareTo(new BigDecimal(50))>=0) && (p50!=null))  {
	    			 tmp = tmp.subtract(new BigDecimal(50));
	    			 fee= fee.add(p50.getPrice());
	    			 cost = cost.add(p50.getCost()); 
	    		 }
	    		 
	   	         MobilePrice p30 =	findPrice(prices,new BigDecimal(30));
	    		 while ((tmp.compareTo(new BigDecimal(30))>=0) && (p30!=null))  {
	    			 tmp = tmp.subtract(new BigDecimal(30));
	    			 fee= fee.add(p30.getPrice());
	    			 cost = cost.add(p30.getCost()); 
	    		 }
	    		 
	   	         MobilePrice p20 =	findPrice(prices,new BigDecimal(20));
	    		 while ((tmp.compareTo(new BigDecimal(20))>=0) && (p20!=null))  {
	    			 tmp = tmp.subtract(new BigDecimal(20));
	    			 fee= fee.add(p20.getPrice());
	    			 cost = cost.add(p20.getCost()); 
	    		 }
	    		 
	   	         MobilePrice p10 =	findPrice(prices,new BigDecimal(10));
	    		 while ((tmp.compareTo(new BigDecimal(10))>=0) && (p10!=null))  {
	    			 tmp = tmp.subtract(new BigDecimal(10));
	    			 fee= fee.add(p10.getPrice());
	    			 cost = cost.add(p10.getCost()); 
	    		 }
	    		if (!fee.equals(new BigDecimal(0)) && tmp.equals(new BigDecimal(0))) {
		 	    	map.put("fee",fee);
			    	map.put("cost",cost);
	    		} else {
	    	    	map.put("fee",new BigDecimal(0));
	    	    	map.put("cost",new BigDecimal(0));
	    		}
	       }
		return  map;
	}
	
	/**
	 * 计算支付手续费
	 */
	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculateFee(String mobile,BigDecimal amount) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (amount == null || amount.compareTo(new BigDecimal(0)) < 0) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		Map<String, BigDecimal> map = calcFee(mobile,amount); 
		data.put("fee",map.get("fee"));
		return data;
	}

	/**
	 * 提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String mobile, BigDecimal amount, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
    	if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.precision() > 15 || amount.scale() > 0) {
			return ERROR_VIEW;
		}
    	Mobile entity = new Mobile();
    	entity.setSn(snService.generate(Sn.Type.mobile));
    	entity.setMobile(mobile);
    	entity.setAmount(amount);
    	Map<String, BigDecimal> map = calcFee(mobile,amount); 
    	entity.setFee(map.get("fee"));				
    	entity.setCost(map.get("cost"));
    	entity.setMember(member);
    	
    	mobileService.fill(entity);
		model.addAttribute("mobile", entity);
	    if ("01".equals(entity.getRespCode())) {	
		   model.addAttribute("status",Message.success("您的手机充值申请已提交成功，系统正在处理请稍候。。。"));
	    } else {
		   model.addAttribute("status",Message.error(entity.getRespMsg()));
	    }
	    if ("99".equals(entity.getRespCode()) ) {
		   return ERROR_VIEW;
	    } else {
	       return "b2c/member/mobile/notify";
	    }
	    
	}

	/**
	 * 检查余额
	 */
	@RequestMapping(value = "/check_balance", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		data.put("balance", member.getBalance() );
		return data;
	}

	/**
	 * 充值
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
 		return "b2c/member/mobile/index";
	}

	/**
	 * 通知
	 */
	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	public @ResponseBody 
	Map<String, Object> notify(Long id,ModelMap model) {
		Map<String, Object> data = new HashMap<String, Object>();
		Mobile mobile = mobileService.find(id);
		mobileService.notify(mobile);
		return data;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber,ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("member", member);
		model.addAttribute("page", mobileService.findPage(member, pageable));
		return "b2c/member/mobile/list";
	}
	
}