/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Equipment;
import net.wit.entity.Member;
import net.wit.entity.Services;
import net.wit.entity.Tenant;
import net.wit.service.EquipmentService;
import net.wit.service.MemberService;
import net.wit.service.ServicesService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 二维码管理
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminEquipmentController")
@RequestMapping("/admin/equipment")
public class EquipmentController extends BaseController {

	@Resource(name = "equipmentServiceImpl")
	private EquipmentService equipmentService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "servicesServiceImpl")
	private ServicesService servicesService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, Equipment.Status status,String searchValue, ModelMap model) {
		model.addAttribute("page", equipmentService.findPage(searchValue,status,pageable));
		model.addAttribute("status", status);
		return "/admin/equipment/list";
	}

	/**
	 * 绑定
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		model.addAttribute("equipment", equipmentService.find(id));
		return "/admin/equipment/edit";
	}

	/**
	 * 店铺绑定
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public @ResponseBody Message update(@PathVariable Long id,String tenantMobile,String storeMobile) {
		Member member = memberService.findByBindTel(tenantMobile);

		if(member==null){
			return  Message.error("无效的供货商");
		}

		Tenant tenant = member.getTenant();
		if(tenant==null){
			return  Message.error("无效企业");
		}


		Member member1 = memberService.findByBindTel(storeMobile);

		if(member1==null){
			return  Message.error("无效的零售商");
		}

		Tenant tenant1 = member1.getTenant();
		if(tenant1==null){
			return  Message.error("无效企业");
		}


		Equipment equipment = equipmentService.find(id);
		equipment.setStatus(Equipment.Status.enabled);
		equipment.setStore(tenant1);
		equipment.setTenant(tenant);
		equipmentService.update(equipment);

		boolean b = servicesService.checkServicesType(tenant, Services.Status.shoppingScreen);

		if(b){
			Services services = servicesService.findServicesByTenant(tenant, Services.Status.shoppingScreen);
			if(services==null){
				return Message.error("无效的编号");
			}
			services.setType(Services.Type.success);
			services.setState(Services.State.success);
			services.setContent("屏绑定并开通服务");
			servicesService.update(services);
		}

		return Message.success("绑定成功");
	}

	/**
	 * 解除绑定
	 */
	@RequestMapping(value = "/unbundling", method = RequestMethod.GET)
	public String unbundling( Long id,RedirectAttributes redirectAttributes) {
		Equipment equipment = equipmentService.find(id);
		equipment.setStatus(Equipment.Status.disable);
		equipment.setStore(null);
		equipment.setTenant(null);
		equipmentService.update(equipment);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 验证输入的号码是否是企业号码
	 */
	@RequestMapping(value = "/exits", method = RequestMethod.POST)
	public @ResponseBody Message exits(String mobile) {
		Member member = memberService.findByBindTel(mobile);

		if(member==null){
			return  Message.error("无效的用户");
		}

		Tenant tenant = member.getTenant();
		if(tenant==null){
			return  Message.error("无效企业");
		}
		return Message.success("验证成功");
	}
}