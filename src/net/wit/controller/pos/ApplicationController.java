/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

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
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("posMemberApplicationController")
@RequestMapping("/pos/member/application")
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
	public DataBlock view(Long tenantId,String key,String type,HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+type+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
        List<Application> apps = applicationService.findList(tenant,net.wit.entity.Application.Type.erp);
        
        	Map<String,Object> data = new HashMap<String,Object>();
        	Date validityDate = null;
        	for (Application app:apps) {
        		if (!app.getStatus().equals(Application.Status.closed)) {
        			if (validityDate==null) {
        			   validityDate = app.getValidityDate();
        	        } else {
        		       if (app.getValidityDate()!=null && app.getValidityDate().compareTo(validityDate)<0  && !app.getStatus().equals(Application.Status.none)) {
            			  validityDate = app.getValidityDate();
        		       }
        		    }
        	    }
        	}
        		
        	if (validityDate==null) {
            	data.put("validityDate", null);
            	data.put("descr", "未安装");
            	data.put("buy", false);
        	} else {
            	data.put("validityDate", validityDate);
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        	    data.put("descr", sdf.format(validityDate));
    			Calendar   calendar   =   new   GregorianCalendar(); 
    		    calendar.setTime(validityDate); 
    		    calendar.add(calendar.DATE,15);
        	    if (calendar.getTime().compareTo((new Date()))>0) {
            	   data.put("buy", true);
        	    } else {
        	       data.put("buy", false);
        	    }
        	}
    		return DataBlock.success(data, "success");
	}
	
	/**
	 * 查询推广分润
	 */
	@RequestMapping(value = "/rebate", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock rebate(Long tenantId,String key,String type,HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+type+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
        Map<String,Object> data = new HashMap<String,Object>();
        BigDecimal amount = BigDecimal.ZERO;
     	Setting setting = SettingUtils.get();
    	BigDecimal price = setting.getFunctionFee(); 
        amount = price.multiply(new BigDecimal("0.9"));
        data.put("rebate",amount.multiply(new BigDecimal("0.25")).setScale(0).toString());
        data.put("descr","推荐好友开店获取"+data.get("rebate")+"元佣金");
        return DataBlock.success(data, "success");
	}

	/**
	 * 打开 /关闭  
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock select(Long tenantId,String key,Long id,Boolean selected,HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
        Application app = applicationService.find(id);
        if (selected) {
        	app.setStatus(Application.Status.none);
        } else {
        	app.setStatus(Application.Status.closed);
        }
        return DataBlock.success("success", "success");
	}
	
	/**
	 * 购买界面显示应用 code 
	 */
	@RequestMapping(value = "/buy", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock buy(Long tenantId,String key,String code,HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = "";
		if (code==null) {
		    myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
		} else {
		    myKey = DigestUtils.md5Hex(tenantId.toString()+code+bundle.getString("appKey"));
		}
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
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
    		return DataBlock.success(data, "success");
	}
	
	/**
	 * 根据
	 */
	@RequestMapping(value = "/qrcode",method = RequestMethod.GET)
	public void qrcode(Integer width,Integer height,String content,HttpServletRequest request, HttpServletResponse response) {
		try {
			// 第三方用户唯一凭证
			String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
			response.reset();  
			response.setContentType("image/jpeg;charset=utf-8");
			try {
				QRBarCodeUtil.encodeQRCode(content, tempFile, width, height);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			ServletOutputStream output = response.getOutputStream();// 得到输出流  
			InputStream imageIn = new FileInputStream(new File(tempFile));  
            // 得到输入的编码器，将文件流进行jpg格式编码  
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);  
            // 得到编码后的图片对象  
            BufferedImage image = decoder.decodeAsBufferedImage();  
            // 得到输出的编码器  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);  
            encoder.encode(image);// 对图片进行输出编码  
            imageIn.close();// 关闭文件流  
            output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 购买提交
	 */
	@RequestMapping(value = "/buy", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sumbit(Long tenantId,String key,String code,BigDecimal amount,HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = "";
		if (code==null) {
		    myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
		} else {
		    myKey = DigestUtils.md5Hex(tenantId.toString()+code+bundle.getString("appKey"));
		}
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
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
		return DataBlock.success(payment.getSn(),"success");
	}
	/**
	 * 查询明细流水
	 */
	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock deposit(Long tenantId,String key,String code,HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = "";
		if (code==null) {
		    myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
		} else {
		    myKey = DigestUtils.md5Hex(tenantId.toString()+code+bundle.getString("appKey"));
		}
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
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
      	return DataBlock.success(ApplicationModel.bindData(apps,validityDate,price), "success");
        
	}
	
	
}