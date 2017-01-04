/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.service.AreaService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 地区
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxAreaController")
@RequestMapping("/ajax/area")
public class AreaController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	

	/**
	 * 区域--获取根节点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public Message roots(Integer count) {
		List<Area> roots= areaService.findRoots(count);
		return Message.success(JsonUtils.toJson(roots));
	}
	/**
	 * 区域--获取子节点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public Message childrens(Long areaId) {
		Area parent = areaService.find(areaId);
		if(parent ==null){
			return Message.error("ajax.area.NotExist");
		}
		Set<Area> childrens = parent.getChildren();
		return Message.success(JsonUtils.toJson(childrens));
	}
	/**
	 * 区域--获取父节点
	 */
	@RequestMapping(value = "/parent", method = RequestMethod.GET)
	@ResponseBody
	public Message parent(Long areaId) {
		Area child = areaService.find(areaId);
		if(child==null){
			return Message.error("ajax.area.NotExist");
		}
		Area parent = child.getParent();
		return Message.success(JsonUtils.toJson(parent));
	}
	/**
	 * 区域--根据编码 获取
	 */
	@RequestMapping(value = "/findByCode", method = RequestMethod.GET)
	@ResponseBody
	public Message findByCode(String code) {
		Area area = areaService.findByCode(code);
		return Message.success(JsonUtils.toJson(area));
	}
	/**
	 * 区域--根据电话区号获取
	 */
	@RequestMapping(value = "/findByTelCode", method = RequestMethod.GET)
	@ResponseBody
	public Message findByTelCode(String telCode) {
		Area area = areaService.findByTelCode(telCode);
		return Message.success(JsonUtils.toJson(area));
	}
	/**
	 * 区域--根邮编获取
	 */
	@RequestMapping(value = "/findByZipCode", method = RequestMethod.GET)
	@ResponseBody
	public Message findByZipCode(String telCode) {
		Area area = areaService.findByZipCode(telCode);
		return Message.success(JsonUtils.toJson(area));
	}
	
}