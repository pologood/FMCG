/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.app.model.AreaModel;
import net.wit.controller.app.model.CommunityModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.Tag;
import net.wit.service.AreaService;
import net.wit.service.CommunityService;
import net.wit.service.TagService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 商圈
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("appCommunityController")
@RequestMapping("/app/community")
public class CommunityController extends BaseController {

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;
	

	/**
	 * 区域--获取城市商圈
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long areaId,Long [] tagIds) {
		Area area = areaService.find(areaId);
		List<Tag> tags = tagService.findList(tagIds);
		List<Community> communities= communityService.findHot(area,tags);
		return DataBlock.success(CommunityModel.bindData(communities),"执行成功");
	}

	/**
	 * 区域--获取城市热门商圈
	 */
	@RequestMapping(value = "/hots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long areaId) {
		Area area = areaService.find(areaId);
		Long [] ids = {7L};
		
		List<Tag> tags = tagService.findList(ids);
		List<Community> communities= communityService.findHot(area,tags);
		return DataBlock.success(CommunityModel.bindData(communities),"执行成功");
	}
	
	
}