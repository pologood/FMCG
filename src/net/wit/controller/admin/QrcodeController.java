/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;


import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Qrcode;
import net.wit.entity.Qrcode.QrcodeType;
import net.wit.entity.Tenant;
import net.wit.service.QrcodeService;
import net.wit.service.TenantService;
import net.wit.weixin.main.QrcodeManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 二维码管理
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminQrcodeController")
@RequestMapping("/admin/qrcode")
public class QrcodeController extends BaseController {

	@Resource(name = "qrcodeServiceImpl")
	private QrcodeService qrcodeService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		for (int i=0;i<10;i++) {
		   Qrcode code = new Qrcode();
		   code.setTicket(null);
		   code.setQrcodeType(QrcodeType.tenant);
		   qrcodeService.save(code);
		   JSONObject json = QrcodeManager.createQrcode("{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+code.getId()+"\"}}}");
			if (json.containsKey("ticket")) {
				   code.setTicket(json.getString("ticket"));
				   code.setUrl(json.getString("url"));
				   qrcodeService.update(code);
			}
		}
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable,String searchValue, ModelMap model) {
		model.addAttribute("page", qrcodeService.openPage(searchValue,pageable));
		return "/admin/qrcode/list";
	}

	/**
	 * 绑定
	 */
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Long id, ModelMap model) {
		model.addAttribute("qrcode", qrcodeService.find(id));
		return "/admin/qrcode/edit";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long id) {
		Qrcode qrcode = qrcodeService.find(id);
		if(qrcode==null){
			return Message.error("错误编码");
		}

		qrcodeService.delete(id);
		return Message.success("删除成功");
	}

	/**
	 * 店铺绑定
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public @ResponseBody Message update(@PathVariable Long id,String mobile) {
		Tenant tenant = tenantService.findByTelephone(mobile);
		if(tenant==null){
			return  Message.error("无效企业");
		}

		if(qrcodeService.tenantExists(tenant)){
			return  Message.error("您已经绑定过二维码，若要重新绑定，请");
		}

		Qrcode qrcode = qrcodeService.find(id);
		qrcode.setTenant(tenant);
		qrcodeService.update(qrcode);
		return Message.success("绑定成功");
	}


	/**
	 * 验证输入的号码是否是企业号码
	 */
	@RequestMapping(value = "/exits", method = RequestMethod.POST)
	public @ResponseBody Message exits(String mobile) {
		Tenant tenant = tenantService.findByTelephone(mobile);
		if(tenant==null){
			return  Message.error("无效企业");
		}
		return Message.success("验证成功");
	}
}