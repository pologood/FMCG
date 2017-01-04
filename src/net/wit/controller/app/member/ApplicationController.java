/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.member;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.ApplicationModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Admin;
import net.wit.entity.Application;
import net.wit.entity.InvitationCode;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Sn;
import net.wit.entity.Tenant;
import net.wit.service.ApplicationService;
import net.wit.service.InvitationCodeService;
import net.wit.service.MemberService;
import net.wit.service.PaymentService;
import net.wit.service.SnService;
import net.wit.service.TenantService;
import net.wit.util.SettingUtils;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("appMemberApplicationController")
@RequestMapping("/app/member/application")
public class ApplicationController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "applicationServiceImpl")
	private ApplicationService applicationService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "invitationCodeServiceImpl")
	private InvitationCodeService invitationCodeService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	
	/**
	 * 查询应用状态
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(String type,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
        Tenant tenant = member.getTenant();
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Application> apps = applicationService.findList(tenant,net.wit.entity.Application.Type.erp);
        
        	Map<String,Object> data = new HashMap<String,Object>();
        	Date validityDate = null;
        	for (Application app:apps) {
        		if (!app.getStatus().equals(Application.Status.closed)) {
        			if (validityDate==null) {
        			   validityDate = app.getValidityDate();
        	        } else {
        		       if (app.getValidityDate()!=null && app.getValidityDate().compareTo(validityDate)<0 ) {
            			  validityDate = app.getValidityDate();
        		       }
        		    }
        	    }
        	}
        		
        	data.put("validityDate", validityDate);
        	if (validityDate==null) {
            	data.put("descr", "未安装");
        	} else {
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        	    data.put("descr", sdf.format(validityDate));
        	}
    		return DataBlock.success(data, "执行成功");
	}
	
	/**
	 * 查询推广分润
	 */
	@RequestMapping(value = "/rebate", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock rebate(String type,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
        Tenant tenant = member.getTenant();
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Map<String,Object> data = new HashMap<String,Object>();
        BigDecimal amount = BigDecimal.ZERO;
     	Setting setting = SettingUtils.get();
    	BigDecimal price = setting.getFunctionFee(); 
        amount = price.multiply(new BigDecimal("0.9"));
        data.put("rebate",setting.setScale(amount.multiply(new BigDecimal("0.25"))).toString());
        data.put("descr","推荐好友开店获取"+data.get("rebate")+"元佣金");
        return DataBlock.success(data, "执行成功");
	}

	/**
	 * 打开 /关闭  
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock select(Long id,Boolean selected,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
        Tenant tenant = member.getTenant();
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Application app = applicationService.find(id);
        if (selected) {
        	app.setStatus(Application.Status.none);
        } else {
        	app.setStatus(Application.Status.closed);
        }
        return DataBlock.success("success", "执行成功");
	}
	
	/**
	 * 购买界面显示应用 code 
	 */
	@RequestMapping(value = "/buy", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock buy(String code,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
        Tenant tenant = member.getTenant();
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Application> apps = applicationService.findList(tenant,net.wit.entity.Application.Type.erp);
        
        	Map<String,Object> data = new HashMap<String,Object>();
        	Date validityDate = null;
        	for (Application app:apps) {
        		if (!app.getStatus().equals(Application.Status.closed)) {
        			if (validityDate==null) {
        			   validityDate = app.getValidityDate();
        	        } else {
        		       if (app.getValidityDate()!=null && app.getValidityDate().compareTo(validityDate)<0 ) {
            			  validityDate = app.getValidityDate();
        		       }
        		    }
        	    }
        	}
			Setting setting = SettingUtils.get();
			BigDecimal price = setting.getFunctionFee(); 
			BigDecimal market_price = setting.getFunctionFee(); 
        	if (code!=null) {
            	InvitationCode invitation = invitationCodeService.findByCode(code);
            	if (invitation!=null) {
            		price = invitation.getPrice();
            	} else {
            		Long id = Long.parseLong(code)-100000;
            		Member export = memberService.find(id);
            		if (export==null) {
            		   price = market_price;
            		} else {
             		   price = market_price.multiply(new BigDecimal("0.9"));
            		}
            	}
        	} else {
        		price = market_price;
        	}
        	
			BigDecimal total_price = BigDecimal.ZERO;
			BigDecimal total_market_price = BigDecimal.ZERO;
        	for (Application app:apps) {
        		ApplicationModel model = new ApplicationModel();
        		model.copyFrom(app);
        		total_price = total_price.add(model.calc(validityDate, price));
        		total_market_price = total_market_price.add(model.calc(validityDate, market_price));
        	}
        	
        	data.put("validityDate", validityDate);
        	data.put("price", total_price);
        	data.put("market_price", total_market_price);
        	data.put("descr", "店家助手一年服务费用");
    		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 购买提交
	 */
	@RequestMapping(value = "/buy", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sumbit(String code,BigDecimal amount,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
        Tenant tenant = member.getTenant();
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
			Setting setting = SettingUtils.get();
			BigDecimal price = setting.getFunctionFee(); 
			BigDecimal market_price = setting.getFunctionFee(); 
			Admin admin = null;
			Member export = null; 
        	if (code!=null) {
            	InvitationCode invitation = invitationCodeService.findByCode(code);
            	if (invitation!=null) {
            		price = invitation.getPrice();
            		admin = invitation.getAdmin();
            	} else {
            		Long id = Long.parseLong(code)-100000;
            	    export = memberService.find(id);
            		if (export==null) {
            		   price = market_price;
            		} else {
             		   price = market_price.multiply(new BigDecimal("0.9"));
            		}
            	}
        	} else {
        		price = market_price;
        	}
        	
		Payment payment = applicationService.create(tenant, price, admin, export);
		return DataBlock.success(payment.getSn(),"执行成功");
	}
	/**
	 * 查询明细流水
	 */
	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock deposit(String code,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
        Tenant tenant = member.getTenant();
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Application> apps = applicationService.findList(tenant,net.wit.entity.Application.Type.erp);
    	Date validityDate = null;
    	for (Application app:apps) {
    		if (!app.getStatus().equals(Application.Status.closed)) {
    			if (validityDate==null) {
    			   validityDate = app.getValidityDate();
    	        } else {
    		       if (app.getValidityDate()!=null && app.getValidityDate().compareTo(validityDate)<0 ) {
        			  validityDate = app.getValidityDate();
    		       }
    		    }
    	    }
    	}
 		Setting setting = SettingUtils.get();
		BigDecimal price = setting.getFunctionFee(); 
		BigDecimal market_price = setting.getFunctionFee(); 
		Admin admin = null;
		Member export = null; 
    	if (code!=null) {
        	InvitationCode invitation = invitationCodeService.findByCode(code);
        	if (invitation!=null) {
        		price = invitation.getPrice();
        		admin = invitation.getAdmin();
        	} else {
        		Long id = Long.parseLong(code)-100000;
        	    export = memberService.find(id);
        		if (export==null) {
        		   price = market_price;
        		} else {
         		   price = market_price.multiply(new BigDecimal("0.9"));
        		}
        	}
    	} else {
    		price = market_price.multiply(new BigDecimal("0.9"));
    	}
      	return DataBlock.success(ApplicationModel.bindData(apps,validityDate,price), "执行成功");
        
	}
	
	/**
	 *  实体店列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long id,HttpServletRequest request) {
        Tenant tenant = tenantService.find(id);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Application> apps = applicationService.findList(tenant, Application.Type.erp);
        return DataBlock.success(ApplicationModel.bindData(apps), "执行成功");
	}
	
}