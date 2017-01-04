/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.service.RefundsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 退款单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminRefundsController")
@RequestMapping("/admin/refunds")
public class RefundsController extends BaseController {

	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("refunds", refundsService.find(id));
		return "/admin/refunds/view";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Date beginDate, Date endDate,Pageable pageable, ModelMap model) {
//		if(beginDate==null){
//			beginDate=new Date();
//			Long time=beginDate.getTime();
//			Long begin=time-24*60*60*1000*7;
//			beginDate=new Date(begin);
//		}
//		if(endDate!=null){
//			Long time=endDate.getTime();
//			Long end=time+24*60*60*1000-1;
//			endDate=new Date(end);
//		}
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("page", refundsService.findPage(beginDate, endDate,pageable));
		return "/admin/refunds/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		refundsService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}