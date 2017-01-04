/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.MobilePrice;
import net.wit.service.AreaService;
import net.wit.service.MobilePriceService;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 手机充值价格表
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("mobilePriceController")
@RequestMapping("/admin/mobile/price")
public class MobilePriceController extends BaseController {

	@Resource(name = "mobilePriceServiceImpl")
	private MobilePriceService mobilePriceService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", mobilePriceService.findPage(pageable));
		return "/admin/mobile/price/list";
	}

}