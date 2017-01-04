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
import net.wit.service.ReturnsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 退货单
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminReturnsController")
@RequestMapping("/admin/returns")
public class ReturnsController extends BaseController {

	@Resource(name = "returnsServiceImpl")
	private ReturnsService returnsService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("returns", returnsService.find(id));
		return "/admin/returns/view";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Date beginDate, Date endDate,Pageable pageable, ModelMap model) {
		
//		if(beginDate!=null){
//			//beginDate=new Date();
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
		model.addAttribute("page", returnsService.findPage(beginDate, endDate,pageable));
		return "/admin/returns/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		returnsService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}