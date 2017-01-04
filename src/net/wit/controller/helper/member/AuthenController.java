/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.wit.controller.helper.BaseController;
import net.wit.entity.Authen;
import net.wit.entity.Authen.AuthenStatus;
import net.wit.entity.Authen.AuthenType;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.AuthenService;
import net.wit.service.MemberService;
import net.wit.service.ProductImageService;
import net.wit.service.TagService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;

/**
 * Controller - 会员中心 - 密码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberAuthenController")
@RequestMapping("/helper/member/authen")
public class AuthenController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	
	@Resource(name = "authenServiceImpl")
	private AuthenService authenService;
	
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	//
	
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	/**
	 * 主页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(String keyword, HttpServletRequest request, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			tenant = EntitySupport.createInitTenant();
			if (member.getArea() != null) {
				tenant.setArea(member.getArea());
			}
			tenant.setTenantType(Tenant.TenantType.tenant);
			tenant.setAddress(member.getAddress());
			tenant.setLinkman(member.getName());
			tenant.setTelephone(member.getMobile());
		}
		String storesPhoto=null;
		String manufacturersPhoto=null;
		AuthenStatus enterpriseStatus;
		AuthenStatus certifiedStatus;
		AuthenStatus manufacturersStatus;
		if(authenService.findByType(tenant, AuthenType.enterprise)!=null){
			enterpriseStatus = authenService.findByType(tenant, AuthenType.enterprise).getAuthenStatus();
			
		}else{
			enterpriseStatus = AuthenStatus.none;
		}
		if(authenService.findByType(tenant, AuthenType.certified)!=null){
			certifiedStatus = authenService.findByType(tenant, AuthenType.certified).getAuthenStatus();
			storesPhoto=authenService.findByType(tenant, AuthenType.certified).getPathFront();
		}else{
			certifiedStatus = AuthenStatus.none;
		}
		if(authenService.findByType(tenant, AuthenType.manufacturers)!=null){
			manufacturersStatus = authenService.findByType(tenant, AuthenType.manufacturers).getAuthenStatus();
			manufacturersPhoto=authenService.findByType(tenant, AuthenType.manufacturers).getPathFront();
		}else{
			manufacturersStatus = AuthenStatus.none;
		}
		model.addAttribute("member", member);
		model.addAttribute("tenant", tenant);
		model.addAttribute("enterpriseStatus", enterpriseStatus);
		model.addAttribute("certifiedStatus", certifiedStatus);
		model.addAttribute("manufacturersStatus", manufacturersStatus);
		model.addAttribute("storesPhoto", storesPhoto);
		model.addAttribute("manufacturersPhoto", manufacturersPhoto);
		model.addAttribute("pageActive",2);
		return "/helper/member/authen/index";
	}
	
	/**
	 * 企业认证
	 */
	@RequestMapping(value = "/enterpriseCertification", method = RequestMethod.POST)
	public String enterpriseCertification(String keyword,long id,long tagId, long areaId,Tenant tenant, HttpServletRequest request, ModelMap model) {
		//System.out.println("企业认证");
		Member member = memberService.getCurrent();
		Tenant saveTenant = null;
		if (tenant.getId()!=null) {
			   saveTenant = tenantService.find(tenant.getId());
			}
		saveTenant.setName(tenant.getName());
		saveTenant.setLicenseCode(tenant.getLicenseCode());
		saveTenant.setLicensePhoto(tenant.getLicensePhoto());
		saveTenant.setLegalRepr(tenant.getLegalRepr());
		saveTenant.setArea(areaService.find(areaId));
		saveTenant.setAddress(tenant.getAddress());
		//saveTenant.setStatus(Status.wait);
		tenantService.save(saveTenant, member,null);
		Authen authen = new Authen();
		if (tenant.getId()!=null) {
			authen = authenService.findByType(tenant, AuthenType.enterprise);
			}
		if(authen==null){
			authen = EntitySupport.createInitAuthen();
			authen.setTenant(tenantService.find(id));
			authen.setAuthenStatus(AuthenStatus.none);
			authen.setAuthenType(Authen.AuthenType.enterprise);
			authenService.save(authen);
		}
		authen.setAddress(tenant.getAddress());
		authen.setAuthenType(Authen.AuthenType.enterprise);
		authen.setAuthenStatus(AuthenStatus.wait);
		authen.setTenant(tenantService.find(id));
		authen.setNo(tenant.getLicenseCode());
		authen.setPathFront(tenant.getLicensePhoto());
		authen.setTag(tagService.find(tagId));
		authen.setArea(areaService.find(areaId));
		authenService.update(authen);
		String storesPhoto=null;
		String manufacturersPhoto=null;
		AuthenStatus enterpriseStatus;
		AuthenStatus certifiedStatus;
		AuthenStatus manufacturersStatus;
		if(authenService.findByType(tenant, AuthenType.enterprise)!=null){
			enterpriseStatus = authenService.findByType(tenant, AuthenType.enterprise).getAuthenStatus();
			
		}else{
			enterpriseStatus = AuthenStatus.none;
		}
		if(authenService.findByType(tenant, AuthenType.certified)!=null){
			certifiedStatus = authenService.findByType(tenant, AuthenType.certified).getAuthenStatus();
			storesPhoto=authenService.findByType(tenant, AuthenType.certified).getPathFront();
		}else{
			certifiedStatus = AuthenStatus.none;
		}
		if(authenService.findByType(tenant, AuthenType.manufacturers)!=null){
			manufacturersStatus = authenService.findByType(tenant, AuthenType.manufacturers).getAuthenStatus();
			manufacturersPhoto=authenService.findByType(tenant, AuthenType.manufacturers).getPathFront();
		}else{
			manufacturersStatus = AuthenStatus.none;
		}
		model.addAttribute("enterpriseStatus", enterpriseStatus);
		model.addAttribute("certifiedStatus", certifiedStatus);
		model.addAttribute("manufacturersStatus", manufacturersStatus);
		model.addAttribute("storesPhoto", storesPhoto);
		model.addAttribute("manufacturersPhoto", manufacturersPhoto);
		model.addAttribute("member", member);
		model.addAttribute("tenant", saveTenant);
		model.addAttribute("pageActive",2);
		return "/helper/member/authen/index";
	}
	
	/**
	 * 门店认证
	 */
	@RequestMapping(value = "/certifiedStores", method = RequestMethod.POST)
	public String certifiedStores(String keyword,long id,long tagId,String storesPhoto,Tenant tenant, HttpServletRequest request, ModelMap model) {
		//System.out.println("门店认证");
		Member member = memberService.getCurrent();
		Tenant saveTenant = null;
		if (tenant.getId()!=null) {
			   saveTenant = tenantService.find(tenant.getId());
			}
		//saveTenant.setStatus(Status.wait);
		tenantService.save(saveTenant, member, null);
		Authen authen = new Authen();
		if (tenant.getId()!=null) {
			authen = authenService.findByType(tenant, AuthenType.certified);
			}
		if(authen==null){
			authen = EntitySupport.createInitAuthen();
			authen.setTenant(tenantService.find(id));
			authen.setAuthenStatus(AuthenStatus.none);
			authen.setAuthenType(Authen.AuthenType.certified);
			authenService.save(authen);
		}
		authen.setAuthenType(Authen.AuthenType.certified);
		authen.setAuthenStatus(AuthenStatus.wait);
		authen.setTenant(tenantService.find(id));
		authen.setNo(tenant.getLicenseCode());
		authen.setPathFront(storesPhoto);
		authen.setTag(tagService.find(tagId));
		authenService.update(authen);
		String manufacturersPhoto=null;
		AuthenStatus enterpriseStatus;
		AuthenStatus manufacturersStatus;
		if(authenService.findByType(tenant, AuthenType.enterprise)!=null){
			enterpriseStatus = authenService.findByType(tenant, AuthenType.enterprise).getAuthenStatus();
			
		}else{
			enterpriseStatus = AuthenStatus.none;
		}
		if(authenService.findByType(tenant, AuthenType.manufacturers)!=null){
			manufacturersStatus = authenService.findByType(tenant, AuthenType.manufacturers).getAuthenStatus();
			manufacturersPhoto=authenService.findByType(tenant, AuthenType.manufacturers).getPathFront();
		}else{
			manufacturersStatus = AuthenStatus.none;
		}
		model.addAttribute("enterpriseStatus", enterpriseStatus);
		model.addAttribute("certifiedStatus", authen.getAuthenStatus());
		model.addAttribute("manufacturersStatus", manufacturersStatus);
		model.addAttribute("member", member);
		model.addAttribute("storesPhoto", storesPhoto);
		model.addAttribute("manufacturersPhoto", manufacturersPhoto);
		model.addAttribute("tenant", member.getTenant());
		model.addAttribute("pageActive",2);
		return "/helper/member/authen/index";
	}
	
	/**
	 * 厂家认证
	 */
	@RequestMapping(value = "/manufacturersCertification", method = RequestMethod.POST)
	public String manufacturersCertification(String keyword,long id,long tagId,String manufacturersPhoto, Tenant tenant, HttpServletRequest request, ModelMap model) {
		//System.out.println("厂家认证");
		Member member = memberService.getCurrent();
		Tenant saveTenant = null;
		if (tenant.getId()!=null) {
			   saveTenant = tenantService.find(tenant.getId());
			}
		saveTenant.setAuthorization(tenant.getAuthorization());
		//saveTenant.setStatus(Status.wait);
		tenantService.save(saveTenant, member, null);
		Authen authen = new Authen();
		if (tenant.getId()!=null) {
			authen = authenService.findByType(tenant, AuthenType.manufacturers);
			}
		if(authen==null){
			authen = EntitySupport.createInitAuthen();
			authen.setTenant(tenantService.find(id));
			authen.setAuthenStatus(AuthenStatus.none);
			authen.setAuthenType(Authen.AuthenType.manufacturers);
			authenService.save(authen);
		}
		authen.setAuthenType(Authen.AuthenType.manufacturers);
		authen.setAuthenStatus(AuthenStatus.wait);
		authen.setTenant(tenantService.find(id));
		authen.setPathFront(manufacturersPhoto);
		authen.setTag(tagService.find(tagId));
		authenService.update(authen);
		String storesPhoto="";
		AuthenStatus enterpriseStatus;
		AuthenStatus certifiedStatus;
		if(authenService.findByType(tenant, AuthenType.enterprise)!=null){
			enterpriseStatus = authenService.findByType(tenant, AuthenType.enterprise).getAuthenStatus();
			
		}else{
			enterpriseStatus = AuthenStatus.none;
		}
		if(authenService.findByType(tenant, AuthenType.certified)!=null){
			certifiedStatus = authenService.findByType(tenant, AuthenType.certified).getAuthenStatus();
			storesPhoto=authenService.findByType(tenant, AuthenType.certified).getPathFront();
		}else{
			certifiedStatus = AuthenStatus.none;
		}
		model.addAttribute("enterpriseStatus", enterpriseStatus);
		model.addAttribute("certifiedStatus", certifiedStatus);
		model.addAttribute("manufacturersStatus", authen.getAuthenStatus());
		model.addAttribute("member", member);
		model.addAttribute("tenant", saveTenant);
		model.addAttribute("storesPhoto", storesPhoto);
		model.addAttribute("manufacturersPhoto", manufacturersPhoto);
		model.addAttribute("pageActive",2);
		return "/helper/member/authen/index";
	}
	
}