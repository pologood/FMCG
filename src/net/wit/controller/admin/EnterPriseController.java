/*
 * Copyright 2005-2013 rsico.net. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.FileInfo.FileType;
import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.BrandSeries;
import net.wit.entity.Enterprise;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Member;
import net.wit.entity.ProductImage;
import net.wit.entity.Role;
import net.wit.entity.Role.RoleType;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.entity.Tenant;
import net.wit.service.AdminService;
import net.wit.service.AreaService;
import net.wit.service.DepartmentService;
import net.wit.service.EnterpriseService;
import net.wit.service.FileService;
import net.wit.service.MemberService;
import net.wit.service.ProductImageService;
import net.wit.service.TenantService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 广告  
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminEnterPriseController")
@RequestMapping("/admin/enterPrise")
public class EnterPriseController extends BaseController {

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Admin admin = adminService.getCurrent();
		if(!admin.getUsername().equals("admin")){
			EnterpriseType enterpriseType = admin.getEnterprise().getEnterprisetype();
			List<EnterpriseType> enterpriseTypes= new ArrayList<Enterprise.EnterpriseType>();
			for(int i=enterpriseType.ordinal()+1;i<EnterpriseType.values().length;i++){
				enterpriseTypes.add(EnterpriseType.values()[i]);
			}
			model.addAttribute("enterpriseTypes",enterpriseTypes);
		}else{
			model.addAttribute("enterpriseTypes",EnterpriseType.values());
		}
		model.addAttribute("areas", areaService.findRoots());
		model.addAttribute("tenants", tenantService.findAll());
		return "/admin/enter_prise/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Enterprise enterprise, MultipartFile file, Long areaId, Long memberId, Long[] seriesIds,RedirectAttributes redirectAttributes) {
		if(areaId!=null){
			Area area = areaService.find(areaId);
			enterprise.setArea(area);
		}
		if(memberId!=null){
			Member member = memberService.find(memberId);
			enterprise.setMember(member);
		}
		if (!isValid(enterprise)) {
			return ERROR_VIEW;
		}
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				enterprise.setLicensePhoto(img.getThumbnail());
			}
		}
		enterprise.setTenants(new HashSet<Tenant>(tenantService.findList(seriesIds)));
		enterpriseService.save(enterprise);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Admin admin = adminService.getCurrent();
		try {
			if(!admin.getUsername().equals("admin")){
				EnterpriseType enterpriseType = admin.getEnterprise().getEnterprisetype();
				List<EnterpriseType> enterpriseTypes= new ArrayList<Enterprise.EnterpriseType>();
				for(int i=enterpriseType.ordinal()+1;i<EnterpriseType.values().length;i++){
					enterpriseTypes.add(EnterpriseType.values()[i]);
				}
				model.addAttribute("enterpriseTypes",enterpriseTypes);
			}else{
				model.addAttribute("enterpriseTypes",EnterpriseType.values());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("enterprise", enterpriseService.find(id));
		model.addAttribute("tenants", tenantService.findAll());
		Set<Tenant> tenantset = enterpriseService.find(id).getTenants();
		if (tenantset != null) {
			List<Tenant> tenantlist = new ArrayList<Tenant>();
			for (Tenant tenant : tenantset) {
				tenantlist.add(tenant);
			}
		model.addAttribute("tenantlist", tenantlist);
		}
		return "/admin/enter_prise/edit";
	}
	
	
	/**
	 * 充值
	 */
	@RequestMapping(value = "/charge", method = RequestMethod.GET)
	public String charge(Long id, ModelMap model) {
		Admin admin = adminService.getCurrent();
		if(!admin.getUsername().equals("admin")){
			EnterpriseType enterpriseType = admin.getEnterprise().getEnterprisetype();
			List<EnterpriseType> enterpriseTypes= new ArrayList<Enterprise.EnterpriseType>();
			for(int i=enterpriseType.ordinal()+1;i<EnterpriseType.values().length;i++){
				enterpriseTypes.add(EnterpriseType.values()[i]);
			}
			model.addAttribute("enterpriseTypes",enterpriseTypes);
		}else{
			model.addAttribute("enterpriseTypes",EnterpriseType.values());
		}
		model.addAttribute("enterprise", enterpriseService.find(id));
		model.addAttribute("tenants", tenantService.findAll());
		Set<Tenant> tenantset = enterpriseService.find(id).getTenants();
		if (tenantset != null) {
			List<Tenant> tenantlist = new ArrayList<Tenant>();
			for (Tenant tenant : tenantset) {
				tenantlist.add(tenant);
			}
			model.addAttribute("tenantlist", tenantlist);
		}
		return "/admin/enter_prise/charge";
	}
	
	
	/**
	 * 充值许可数
	 */
	@RequestMapping(value = "/charge", method = RequestMethod.POST)
	public String charge(Long id, int snNumber, RedirectAttributes redirectAttributes) {
		Admin admin = adminService.getCurrent();
		if(admin.getEnterprise()==null){
			addFlashMessage(redirectAttributes, Message.error("您没有所属企业，无法进行充值操作！"));
			return "redirect:charge.jhtml?id="+id;
		}
		try {
			enterpriseService.charge(admin.getEnterprise(), enterpriseService.find(id), snNumber, admin.getUsername());
		} catch (BalanceNotEnoughException e) {
			e.printStackTrace();
			addFlashMessage(redirectAttributes, Message.error("剩余许可数不足，无法充值！"));
			return "redirect:charge.jhtml?id="+id;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR_VIEW;
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Enterprise enterprise, MultipartFile file, Long areaId, Long memberId, Long[] seriesIds, RedirectAttributes redirectAttributes) {
		if(memberId!=null){
			Member member = memberService.find(memberId);
			enterprise.setMember(member);
		}
		if(areaId!=null){
			Area area = areaService.find(areaId);
			enterprise.setArea(area);
		}
		if (!isValid(enterprise)) {
			return ERROR_VIEW;
		}
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				enterprise.setLicensePhoto(img.getThumbnail());
			}
		}
		enterprise.setTenants(new HashSet<Tenant>(tenantService.findList(seriesIds)));
		enterpriseService.update(enterprise);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Admin admin = adminService.getCurrent();
		if(admin.getEnterprise()==null){
			if(admin.getEnterprise()==null){
				if(admin.getUsername().equals("admin")){
					model.addAttribute("page", enterpriseService.findPage(pageable));
				}else{
					model.addAttribute("page",new Page(new ArrayList<Enterprise>(),0,pageable));
				}
				return "/admin/enter_prise/list";
			}
		}
		model.addAttribute("page", enterpriseService.findPage(admin,pageable));
		return "/admin/enter_prise/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		enterpriseService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public List<Member> search(String username) {
		List<Filter> filters = new ArrayList<Filter>();
		int limit = 10000;
		if (StringUtils.isNotBlank(username)) {
			filters.add(new Filter("username", Operator.like, "%" + username + "%"));
			limit = 100;
		}
		return memberService.findList(0, limit, filters, null);
	}

}