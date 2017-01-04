/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2b;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.AdModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.AdPosition;
import net.wit.entity.Tenant;
import net.wit.service.AdPositionService;
import net.wit.service.AreaService;
import net.wit.service.TenantService;

/**
 * Controller - 广告
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2bAdController")
@RequestMapping("/app/b2b/ad")
public class AdController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 商家广告
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(@PathVariable Long id,Integer count, HttpServletRequest request) {
		Tenant tenant = tenantService.find(id);
		if (tenant==null) {
			DataBlock.error("企业ID无效");
		}
		
		AdPosition adPosition = adPositionService.find(80L, tenant,null, count);
		 
		return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");
		
	}

	/**
	 * 平台广告
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Integer count, HttpServletRequest request) {
		AdPosition adPosition = adPositionService.find(70L, null,areaService.getCurrent(), count);
		 
		return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");
		
	}
	
	/**
	 * 频道广告
	 */
	@RequestMapping(value = "/channel/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock channel(@PathVariable Long id,Integer count, HttpServletRequest request) {
		AdPosition adPosition = adPositionService.find(80L, null,areaService.getCurrent(), count);
		 
		return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");
		
	}
	
}