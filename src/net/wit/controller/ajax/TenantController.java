/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.Host;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.entity.ProductImage;
import net.wit.entity.Sn;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.Tenant.TenantType;
import net.wit.entity.TenantCategory;
import net.wit.plugin.PaymentPlugin;
import net.wit.plugin.PaymentPlugin.NotifyMethod;
import net.wit.service.AreaService;
import net.wit.service.CommunityService;
import net.wit.service.FileService;
import net.wit.service.HostService;
import net.wit.service.MemberService;
import net.wit.service.PaymentService;
import net.wit.service.PluginService;
import net.wit.service.ProductImageService;
import net.wit.service.SnService;
import net.wit.service.TagService;
import net.wit.service.TenantCategoryService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 商家
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxTenantController")
@RequestMapping("/ajax/tenant")
public class TenantController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;

	@Resource(name = "hostServiceImpl")
	private HostService hostService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;
	
	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{tenantCategoryId}", method = RequestMethod.GET)
	@ResponseBody
	public Message list(@PathVariable Long tenantCategoryId, Long communityId, Long areaId, Long[] tagIds, Integer pageNumber, Integer pageSize) {
		TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
		if (tenantCategoryId == null) {
			return null;
		}
		List<Tag> tags = tagService.findList(tagIds);
		Area area = areaService.find(areaId);
		Community community = communityService.find(communityId);

		if (tenantCategory == null || (tagIds != null && tags.isEmpty()) || (areaId != null && area == null) || (communityId != null && community == null)) {
			return null;
		} else {
			Pageable pageable = new Pageable();
			pageable.setPageNumber(pageNumber);
			if (pageSize != null) {
				pageable.setPageSize(pageSize);
			} else {
				pageable.setPageSize(PAGESIZE);
			}
			Set<TenantCategory> tenantCategorys = new HashSet<TenantCategory>();
			tenantCategorys.add(tenantCategory);
			Page<Tenant> page = tenantService.findPage(tenantCategorys, tags, area, community, true, null, null, pageable);
			return Message.success(JsonUtils.toJson(page));
		}
	}

	/**
	 * 店铺申请提交
	 */
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public @ResponseBody Message apply(Tenant tenant, String username, String tenantImageStr, String name, String address, String linkman, String telephone, Long areaId, Long tenantCategoryId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Member member = memberService.findByUsername(username);
		if (member == null) {
			return Message.error("用户不存在");
		}
		Tenant saveTenant = member.getTenant();
		tenant = EntitySupport.createInitTenant();
		if (saveTenant == null) {
			saveTenant = EntitySupport.createInitTenant();
			if (member.getArea() != null) {
				saveTenant.setArea(member.getArea());
			}
			saveTenant.setScore(0F);
			saveTenant.setTotalScore(0L);
			saveTenant.setScoreCount(0L);
			saveTenant.setHits(0L);
			saveTenant.setWeekHits(0L);
			saveTenant.setMonthHits(0L);
			tenant.setName(name);
			tenant.setAddress(address);
			tenant.setLinkman(linkman);
			tenant.setTelephone(telephone);
		}

		BeanUtils.copyProperties(tenant, saveTenant, new String[] { "code", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "createDate", "modifyDate", "status", "logo", "licensePhoto" });

		if (tenantImageStr != null) {
			Setting setting = SettingUtils.get();
			String[] split = tenantImageStr.split(",");
			for (String temp : split) {
				if (StringUtils.isNotEmpty(temp)) {
					File file = new File(request.getSession().getServletContext().getRealPath("/") + temp.substring(setting.getSiteUrl().length(), temp.length()));
					if (file.isFile()) {
						ProductImage image = new ProductImage();
						image.setLocalFile(file);
						productImageService.build(image);
						saveTenant.getTenantImages().add(image);
					}
				}
			}
			ProductImage productImage = saveTenant.getTenantImages().get(0);
			if (productImage != null) {
				saveTenant.setLogo(productImage.getThumbnail());
			}
		}

		if (saveTenant.getCode() == null) {
			saveTenant.setCode("1");
		}
		if (tenantImageStr != null) {
			Setting setting = SettingUtils.get();
			String[] split = tenantImageStr.split(",");
			for (String temp : split) {
				if (StringUtils.isNotEmpty(temp)) {
					File file = new File(request.getSession().getServletContext().getRealPath("/") + temp.substring(setting.getSiteUrl().length(), temp.length()));
					if (file.isFile()) {
						ProductImage image = new ProductImage();
						image.setLocalFile(file);
						productImageService.build(image);
						saveTenant.getTenantImages().add(image);
					}
				}
			}
			ProductImage productImage = saveTenant.getTenantImages().get(0);
			if (productImage != null) {
				saveTenant.setLogo(productImage.getThumbnail());
			}
		}
		saveTenant.setArea(areaService.find(areaId));
		saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
		saveTenant.setShortName(tenant.getName());
		saveTenant.setTenantType(TenantType.tenant);
		saveTenant.setStatus(Tenant.Status.none);
		saveTenant.setTotalAssistant(0L);
		tenantService.save(saveTenant, member, null);
		return Message.success(memberService.getToken(member));
	}

	/**
	 * 获取店铺
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Tenant get(@PathVariable Long id) {
		return tenantService.find(id);
	}
	
	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
	public @ResponseBody Message hits(@PathVariable Long id) {
		return Message.success(JsonUtils.toJson(tenantService.viewHits(id)));
	}
	
	/**
	 * 开通
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public @ResponseBody Message payment(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return Message.error("会话已经过期，请重新登录");
		}
		Setting setting = SettingUtils.get();
		if (setting.getFunctionFee().equals(BigDecimal.ZERO)) {
			Tenant	tenant = member.getTenant();
			if (tenant==null) {
				tenant = EntitySupport.createInitTenant();
				tenant.setArea(member.getArea());
				tenant.setTenantType(Tenant.TenantType.tenant);
				tenant.setAddress("无");
				tenant.setLinkman(member.getMobile());
				tenant.setTelephone(member.getMobile());
				tenant.setName(member.getMobile());
				tenant.setShortName(member.getMobile());
				tenant.setTenantCategory(tenantCategoryService.find((long)1));
				Host host = hostService.find(new Long(1));
				tenant.setHost(host);
			}
			
			tenant.setStatus(Tenant.Status.none);
			tenantService.save(tenant, member, null);
			return Message.success("恭喜您，已经开通了。");
		}
        		
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin("chinapayMobilePlugin");
		Payment payment = new Payment();
		payment.setSn(snService.generate(Sn.Type.payment));
		payment.setType(Type.function);
		payment.setMethod(Method.online);
		payment.setStatus(Status.wait);
		payment.setPaymentMethod(paymentPlugin.getPaymentName());
		payment.setFee(BigDecimal.ZERO);
		payment.setAmount(setting.getFunctionFee());
		payment.setPaymentPluginId("chinapayMobilePlugin");
		payment.setExpire(paymentPlugin.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentPlugin.getTimeout()) : null);
		payment.setMember(member);
		paymentService.save(payment);
		String resp = paymentPlugin.get(paymentPlugin.getRequestUrl(), paymentPlugin.getParameterMap(payment.getSn(), "function", request, "/ajax/tenant"));
		try {
			Document document = DocumentHelper.parseText(resp);

			Element root = document.getRootElement();
			String tn = ((Element) root.selectNodes("tn").get(0)).getText();
			String qyOrderNO = ((Element) root.selectNodes("qyOrderNO").get(0)).getText();
			payment.setPaySn(qyOrderNO);
			paymentService.save(payment);
			return Message.success("tn=" + tn);
		} catch (Exception e) {
			return Message.error("提交银行出错");
		}
	}
	
	/**
	 * 通知
	 */
	@RequestMapping("/payment/notify/{notifyMethod}/{sn}")
	public String notify(@PathVariable NotifyMethod notifyMethod, @PathVariable String sn, HttpServletRequest request, ModelMap model) {
		Payment payment = paymentService.findBySn(sn);
		if (payment != null) {
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
			if (paymentPlugin != null && notifyMethod.equals(NotifyMethod.async)) {
				if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
					try {
						Member member = payment.getMember();
						Tenant	tenant = member.getTenant();
						if (tenant==null) {
							tenant = EntitySupport.createInitTenant();
							tenant.setArea(member.getArea());
							tenant.setTenantType(Tenant.TenantType.tenant);
							tenant.setAddress("无");
							tenant.setLinkman(member.getMobile());
							tenant.setTelephone(member.getMobile());
							tenant.setName(member.getMobile());
							tenant.setShortName(member.getMobile());
							tenant.setTenantCategory(tenantCategoryService.find((long)1));
							Host host = hostService.find(new Long(1));
							tenant.setHost(host);
							tenantService.save(tenant, member, null);
						}
						paymentService.handle(payment);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				model.addAttribute("notifyMessage", paymentPlugin.getNotifyMessage(sn, notifyMethod, request));
			}
			model.addAttribute("payment", payment);
		}
		return "mobile/payment/notify";
	}
	
	/**
	 * 店铺资料修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Message update(String logo, String name, String address, String linkman, String telephone, Long areaId, Long tenantCategoryId, HttpServletRequest request) {
	    Member member = memberService.getCurrent();
	    if (member==null) {
	 	   return Message.success("会话已经期过，请重新登录");
	    }
	    Tenant tenant = member.getTenant();
	  
	   if (tenant==null) {
			tenant = EntitySupport.createInitTenant();
			tenant.setArea(member.getArea());
			tenant.setTenantType(Tenant.TenantType.tenant);
			tenant.setAddress("无");
			tenant.setLinkman(member.getMobile());
			tenant.setTelephone(member.getMobile());
			tenant.setName(member.getMobile());
			tenant.setShortName(member.getMobile());
			tenant.setTenantCategory(tenantCategoryService.find((long)1));
			Host host = hostService.find(new Long(1));
			tenant.setHost(host);
			tenantService.save(tenant, member, null);
	   }
	   
	   if (logo!=null) {
		   tenant.setLogo(logo);
	   }
	   if (name!=null) {
		   tenant.setName(name);
	   }
	   if (address!=null) {
		   tenant.setAddress(address);
	   }
	   	
	   if (linkman!=null) {
		   tenant.setLinkman(linkman);
	   }
	   
	   if (telephone!=null) {
		   tenant.setTelephone(telephone);
	   }
	   
	   if (areaId!=null) {
		   Area area = areaService.find(areaId);
		   if (area!=null) {
			   tenant.setArea(area);
		   }
	   }
	   
	   if (tenantCategoryId!=null) {
		   TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
		   if (tenantCategory!=null) {
			   tenant.setTenantCategory(tenantCategory);
		   }
	   }
	   tenantService.update(tenant);
	
	   return Message.success("更新成功");
	  
    }	
	
	public static void main(String[] args) {
	}
}