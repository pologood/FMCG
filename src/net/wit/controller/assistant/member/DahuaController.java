/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.entity.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.BindUser.Type;
import net.wit.service.BindUserService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.DeviceService;
import net.wit.service.MemberService;

/**
 * Controller - 大华设备支持
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantDahuaController")
@RequestMapping("/assistant/dahua")
public class DahuaController extends BaseController {

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "deviceServiceImpl")
	private DeviceService deviceService;
	
	/**
	 * 获取绑定用户
	 * 
	 */
	@RequestMapping(value = "/user_get", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock user_get() {
		Member member = memberService.getCurrent();
		Map<String,String> data = new HashMap<String,String>();
		if (member.getTenant()==null) {
			return DataBlock.error("不是店铺账号不能使用。");
		}
		Member Owner = member.getTenant().getMember();
		BindUser bindUser = bindUserService.findByMember(Owner,Type.dahua);
		if (bindUser==null) {
			data.put("status","none");
			data.put("mobile",Owner.getMobile());
		} else {
			data.put("status","binded");
			data.put("mobile",bindUser.getUsername());
		}
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 绑定用户
	 * 
	 */
	@RequestMapping(value = "/user_bind", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock user_post(String mobile) {
		Member member = memberService.getCurrent();
		if (member.getTenant()==null) {
			return DataBlock.error("不是店铺账号不能使用。");
		}
		Member Owner = member.getTenant().getMember();
		BindUser bindUser = bindUserService.findByMember(Owner,Type.dahua);
		if (bindUser==null) {
			bindUser = new BindUser();
			bindUser.setUsername(mobile);
			bindUser.setType(Type.dahua);
			bindUser.setMember(Owner);
			bindUser.setPassword(Owner.getPassword());
			bindUserService.save(bindUser);
		}
		return DataBlock.success("success","执行成功");
	}

	/**
	 * 设备添加
	 * 
	 */
	@RequestMapping(value = "/device/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock deviceAdd(String equipId,String name,Long deliveryCenterId,String url) {
		DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
		if (deliveryCenter==null) {
			return DataBlock.error("无效门店");
		}
		Device device = deviceService.find(equipId);
		if (device==null) {
			device = new Device();
			device.setEquipId(equipId);
			device.setName(name);
			device.setDeliveryCenter(deliveryCenter);
			device.setUrl(url);
			deviceService.save(device);
		} else {
			device.setEquipId(equipId);
			device.setName(name);
			device.setDeliveryCenter(deliveryCenter);
			device.setUrl(url);
			deviceService.update(device);

		}
		return DataBlock.success("success","执行成功");
	}

	/**
	 * 设备删除
	 *
	 */
	@RequestMapping(value = "/device/del", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock deviceDel(String equipId) {

		Device device = deviceService.find(equipId);
		if (device==null) {
			return DataBlock.error("无效设备号");
		}
		deviceService.delete(device);
		return DataBlock.success("success","执行成功");
	}

	/**
	 * 分享链接
	 */
	@RequestMapping(value = "/qrcode/json",method = RequestMethod.GET)
	@ResponseBody
	public DataBlock qrcodeJson(String equipId,HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Device device = deviceService.find(equipId);
		if (device==null) {
			return DataBlock.error("无效设备号");
		}
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//			if (!member.getTenant().getStatus().equals(Tenant.Status.success)) {
//				return DataBlock.error("没有开通不能分享");
//			}
			String url =  MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/index.jhtml?id="+member.getTenant().getId()+"&extension=" + (member != null ? member.getUsername() : ""));

			Map<String,Object> map = new HashMap<>();
			map.put("url",url);
			map.put("text",device.getName());
			return DataBlock.success(map,"获取成功");
		} catch (Exception e) {
			return DataBlock.error("获取失败");
		}
	}
}