/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.Location;
import net.wit.service.AreaService;
import net.wit.service.CommunityService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 社区
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxCommunityController")
@RequestMapping("/ajax/community")
public class CommunityController extends BaseController {

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long id) {
		List<Filter> filters = new ArrayList<Filter>();
		Area area = areaService.find(id);
		filters.add(new Filter("area", Operator.eq, area));
		List<Community> list = communityService.findList(null, filters, null);
		return Message.success(JsonUtils.toJson(list));
	}

	/**
	 * 通过经纬度获取社区
	 */
	@RequestMapping(value = "/getbyLocation", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Location location) {
		System.out.println("lat:"+location.getLat().toString()+",lng:"+location.getLng().toString());
		Community community = communityService.findbyLocation(location);
		return Message.success(JsonUtils.toJson(community));
	}

}