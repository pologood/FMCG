/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.AreaModel;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.Area;
import net.wit.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * Controller - 地区
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantAreaController")
@RequestMapping("/assistant/area")
public class AreaController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	/**
	 * 城市--获取数据包
	 * timestamp 时间戳
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock get(String timestamp) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("timestamp",1000);
		data.put("url",bundle.getString("WeiXinSiteUrl")+"/resources/data/area.json");
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 区域--获取根节点
	 */
	@RequestMapping(value = "/alls", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock alls(Long areaId) {
		Area area = areaService.find(areaId);
		Set<Area> areas= area.getChildren();
		return DataBlock.success(AreaModel.bindAllData(areas),"执行成功");
	}
	
	/**
	 * 区域--获取根节点
	 */
	@RequestMapping(value = "/roots", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock roots() {
		List<Area> roots= areaService.findRoots();
		return DataBlock.success(AreaModel.bindData(roots),"执行成功");
	}
	/**
	 * 区域--获取子节点
	 */
	@RequestMapping(value = "/childrens", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock childrens(Long areaId) {
		Area parent = areaService.find(areaId);
		if(parent ==null){
			return DataBlock.error("ajax.area.NotExist");
		}
		Set<Area> childrens = parent.getChildren();
		return DataBlock.success(AreaModel.bindData(childrens),"执行成功");
	}
	/**
	 * 区域--获取子节点
	 */
	@RequestMapping(value = "/allChildrens", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock allChildrens(Long areaId) {
		Area parent = areaService.find(areaId);
		if(parent ==null){
			return DataBlock.error("ajax.area.NotExist");
		}
		Set<Area> childrens = parent.getChildren();
		List<AreaModel> models = new ArrayList<>();
		AreaModel parentModel = new AreaModel();
		parentModel.copyFrom(parent);
		parentModel.setName("全部");
		parentModel.setFullName("全部");
		models.add(parentModel);
		for (Area area:childrens) {
			AreaModel model = new AreaModel();
			model.copyFrom(area);
			models.add(model);
		}
		return DataBlock.success(models,"执行成功");
	}
	/**
	 * 区域--获取父节点
	 */
	@RequestMapping(value = "/parent", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock parent(Long areaId) {
		Area child = areaService.find(areaId);
		if(child==null){
			return DataBlock.error("ajax.area.NotExist");
		}
		Area parent = child.getParent();
		return DataBlock.success(parent,"执行成功");
	}
	
	
	
	
}