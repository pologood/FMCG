/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.util.WebUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 地区
 * 
 * @author rsico Teamc
 * @version 3.0
 */
@Controller("b2cAreaController")
@RequestMapping("/b2c/area")
public class AreaController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 获取省
	 */
	@RequestMapping(value = "/getProvince", method = RequestMethod.GET)
	@ResponseBody
	public Area getProvince(Long id) {
		Area area = areaService.find(id);
		Area parent = area.getParent();
		return parent;
	}
	
	/**
	 * 获取城市
	 */
	@RequestMapping(value = "/getCitys", method = RequestMethod.GET)
	@ResponseBody
	public Set<Area> getCitys(Long id) {
		Area area = areaService.find(id);
		Set<Area> citys = area.getChildren();
		return citys;
	}
	
	
	/**
	 * 获取
	 */
	@RequestMapping(value = "/getbyid", method = RequestMethod.GET)
	@ResponseBody
	public String getArea(Long id) {
		Area area = areaService.find(id);
		Set<Area> areas=area.getChildren();
		if(areas.size()>0){
			return "true";
		}
		return "false";
	}
	/**
	 * 获取全部省
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public List<Area> chooseProvince() {
		List<Area> provinces = areaService.findRoots();
		return provinces;
	}
	
	/**
	 * 获取当前session area
	 */
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	@ResponseBody
	public Area getCurrent(HttpServletRequest request,HttpServletResponse response) {
		Area area = areaService.getCurrent();
		request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME,area);
		return area;
	}
	/**
	 * 跟新session area
	 */
	@RequestMapping(value = "/update_current", method = RequestMethod.GET)
	@ResponseBody
	public Message updateCurrent(Long id,HttpServletRequest request,HttpServletResponse response) {
		if(id==null){
			return Message.error("操作失败");
		}
		Area area = areaService.find(id);
		request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME,area);
		WebUtils.addCookie(request, response, Area.AREA_NAME, area.getName());
		WebUtils.addCookie(request, response, Area.AREA_ID,area.getId().toString());
		return Message.success("操作成功");
	}

	
}