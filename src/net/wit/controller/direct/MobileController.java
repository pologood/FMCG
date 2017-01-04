/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.direct;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.Member;
import net.wit.entity.Mobile;
import net.wit.entity.MobilePrice;
import net.wit.entity.Sn.Type;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.MobilePriceService;
import net.wit.service.MobileService;
import net.wit.service.SnService;
import net.wit.service.TenantService;
import net.wit.util.JsonUtils;
import net.wit.web.mobile.MobileFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 会员
 * @author rsico Team
 * @version 3.0
 */
@Controller("directController")
@RequestMapping("/direct/mobile")
public class MobileController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "mobileFactory")
	private MobileFactory mobileFactory;
	
	@Resource(name = "mobilePriceServiceImpl")
	private MobilePriceService mobilePriceService;

	@Resource(name = "mobileServiceImpl")
	private MobileService mobileService;
	
	@Resource(name = "snServiceImpl")
	private SnService snService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	/**
	 * 获取当前列表
	 */
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public @ResponseBody
	Message product(String username,String key) {
		String sign = DigestUtils.md5Hex(username+DigestUtils.md5Hex(username+"myjsy2014$$"));
		if (!sign.equals(key)) {
			return Message.error("签名无效。");
		}
		//mobileFactory.directProduct();
		List<MobilePrice> products = mobilePriceService.findAll();
		return Message.success(JsonUtils.toJson(products));
	}

	/**
	 * 号码段查询
	 */
	@RequestMapping(value = "/segment", method = RequestMethod.GET)
	@ResponseBody
	public Message segment(String mobile,String username,String key) {
		String sign = DigestUtils.md5Hex(mobile+username+DigestUtils.md5Hex(username+"myjsy2014$$"));
		if (!sign.equals(key)) {
			return Message.error("签名无效。");
		}
		Map<String,Object> map = mobileFactory.getSegment(mobile);
		if (!map.get("code").equals("0000")) {
			return  Message.error(mobileFactory.getSegError(map.get("code").toString()));
		}
		return Message.success(JsonUtils.toJson(map));
	}


	/**
	 * 获取账户余额
	 */
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	@ResponseBody
	public Message balance(String username,Long tenantId,String key) {
		if (tenantId!=null) {
			String sign = DigestUtils.md5Hex(tenantId+DigestUtils.md5Hex(tenantId+"myjsy2014$$"));
			if (!sign.equals(key)) {
				return Message.error("签名无效。");
			}
		} else {
			String sign = DigestUtils.md5Hex(username+DigestUtils.md5Hex(username+"myjsy2014$$"));
			if (!sign.equals(key)) {
				return Message.error("签名无效。");
			}
		}
		Member member = null;
		if (tenantId!=null) {
			Tenant tenant = tenantService.find(tenantId);
			member = tenant.getMember();
		} else {
			member = memberService.findByUsername(username);
		}
		if (member==null) {
			return Message.error("用户名无效。");
		}
		
		return Message.success(member.getBalance().toString());
	}

	/**
	 * 充值准备
	 */
	@RequestMapping(value = "/ready", method = RequestMethod.GET)
	@ResponseBody
	public Message ready(String mobile,String username,Long tenantId,String key) {
		if (tenantId!=null) {
			String sign = DigestUtils.md5Hex(mobile+tenantId+DigestUtils.md5Hex(tenantId+"myjsy2014$$"));
			if (!sign.equals(key)) {
				return Message.error("签名无效。");
			}
		} else {
			String sign = DigestUtils.md5Hex(mobile+username+DigestUtils.md5Hex(username+"myjsy2014$$"));
			if (!sign.equals(key)) {
				return Message.error("签名无效。");
			}
		}
			
		Map<String,Object> map = mobileFactory.getSegment(mobile);
		if (!map.get("code").equals("0000")) {
			return  Message.error(mobileFactory.getSegError(map.get("code").toString()));
		}
		String provincename = map.get("provincename").toString();
        String isptype = map.get("isptype").toString();
		List<MobilePrice> prices = mobilePriceService.findBySegment(provincename, isptype);
		if (prices.size()==0) {
			return Message.error("对应城市没有开通。");
		}
		return Message.success(JsonUtils.toJson(prices));
	}
	
	/**
	 * 冲值请求
	 */
	@RequestMapping(value = "/fill", method = RequestMethod.GET)
	@ResponseBody
	public Message fill(String mobile,String prodId,String username,Long tenantId,String key) {
		return Message.error("暂末开通。");
		/**
		Member member = null;
		if (tenantId!=null) {
			Tenant tenant = tenantService.find(tenantId);
			member = tenant.getMember();
		} else {
			member = memberService.findByUsername(username);
		}
		if (member==null) {
			return Message.error("用户名无效。");
		}
		
		if (tenantId!=null) {
			String sign = DigestUtils.md5Hex(mobile+prodId+tenantId+DigestUtils.md5Hex(tenantId+"myjsy2014$$"));
			if (!sign.equals(key)) {
				return Message.error("签名无效。");
			}
		} else {
			String sign = DigestUtils.md5Hex(mobile+prodId+username+DigestUtils.md5Hex(username+"myjsy2014$$"));
			if (!sign.equals(key)) {
				return Message.error("签名无效。");
			}
		}
		
		MobilePrice prod = mobilePriceService.findByProdId(prodId);
		if (prod==null) {
			return Message.error("无效的产品编号。");
		}
        Mobile mob = new Mobile();
        mob.setMember(member);
        mob.setAmount(prod.getPrice());
        mob.setCost(prod.getCost());
        mob.setMobile(mobile);
        mob.setFee(BigDecimal.ZERO);
        mob.setSn(snService.generate(Type.mobile));
        mob.setProdId(prod.getProdId());
        mob.setDescr(prod.getProvince()+prod.getIspType()+ "  "+prod.getDenomination().toString()+"元");
        mob.setOperator(username);
        Message msg = mobileService.fill(mob);
        
        if (msg.getType().equals(Message.Type.error)) {
	        return msg;
        }
        
        Map<String,Object> map = null;
        map = mobileFactory.directFill(prodId, mob.getSn(),mobile);
		if (!map.get("code").equals("0000")) {
	        mob.setRespCode(map.get("code").toString());
	        mob.setRespMsg(mobileFactory.getFillError(map.get("code").toString()));
	        mob.setRespTime(new Date());
	        mob.setBusiRefNo(map.get("tranid").toString());
	        mobileService.notify(mob);
			return  Message.error(mob.getRespMsg());
		}
        mob.setRespCode(map.get("resultno").toString());
        mob.setRespMsg(mobileFactory.getFillError(map.get("resultno").toString()));
        mob.setRespTime(new Date());
        mobileService.notify(mob);
        if ("0000".equals(mob.getRespCode())) {
        	Map<String,Object> ret = new HashMap<String, Object>();
        	ret.put("price",mob.getAmount());
        	ret.put("sn", mob.getSn());
        	ret.put("prodId", mob.getProdId());
           return Message.success(JsonUtils.toJson(ret));
        } else {
        	return Message.error(mob.getRespMsg());
        }
        
        
        */
	}

	/**
	 * 查询请求
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public Message search(String sn,String username,String key) {
		String sign = DigestUtils.md5Hex(sn+username+DigestUtils.md5Hex(username+"myjsy2014$$") );
		if (!sign.equals(key)) {
			return Message.error("签名无效。");
		}
		Mobile mobile = mobileService.findbySn(sn);
	    Map<String,Object> map = mobileFactory.directSearch(sn);
	    if (map.get("code").equals("0000")) {
	    	mobile.setRespCode(map.get("resultno").toString());
	        mobile.setRespMsg(mobileFactory.getFillError(map.get("resultno").toString()));
	        mobile.setRespTime(new Date());
	        mobileService.save(mobile);
			return Message.success(JsonUtils.toJson(map));
	    } else {
	    	return Message.error(mobileFactory.getSearchError(map.get("code").toString()) );
	    }
	}
	
	/**
	 * 回复通知
	 */
	@RequestMapping(value = "/notify", method = RequestMethod.POST)
	public String notify(String orderid,String status,String ordermoney,String verifystring,String mobileBalance, ModelMap model) {
		String sign=DigestUtils.md5Hex("orderid="+orderid+"&status="+status+"&ordermoney="+ordermoney+"&merchantKey="+MobileFactory.key);
		if (sign.equals(verifystring)) {
		   Mobile mob = mobileService.findbySn(orderid);
			if (mob.getRespCode().equals("0000") ) {
			   mobileService.notify(mob);
			}
		}
		model.addAttribute("notifyMessage", status);
        return "box/payment/notify";
	}
	
}

