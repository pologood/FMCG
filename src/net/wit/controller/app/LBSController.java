/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.controller.app.model.AreaModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Area;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.util.IPUtil;
import net.wit.util.JsonUtils;
import net.wit.util.WebUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 地理信息控制
 * @author rsico Team
 * @version 3.0
 */
@Controller("appLBSController")
@RequestMapping("/app/lbs")
public class LBSController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	public static final String LOCATION_COOKIE = "Location_Cookie";

	/**
	 * 区域--当前地理位置
	 */
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock currentArea(HttpServletRequest request, HttpServletResponse response) {
		AreaModel model = new AreaModel();
		model.copyFrom(areaService.getCurrent());
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 经纬度获取城市 
	 * 
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock get(String lat, String lng,HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> data = new HashMap<String,Object>();
		Area currentArea = areaService.getCurrent();
		Area area = areaService.findByLbs(Double.valueOf(lat).doubleValue(), Double.valueOf(lng).doubleValue());
		AreaModel model = new AreaModel();
		if (area==null && currentArea!=null) {
			area = currentArea;
		} 
		if (area!=null) {
		    model.copyFrom(area);
		}
			Area sArea = (Area) request.getSession().getAttribute(Member.AREA_ATTRIBUTE_NAME);
			if (sArea==null) {
			   data.put("update",!area.equals(currentArea));
			} else 
			{
			   data.put("update",false);
			}
	
		data.put("area", model);
		if (currentArea!=null) {
		    request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, currentArea);
		}
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 区域--更新地理位置
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock update(String lat, String lng,Long areaId, String username, HttpServletRequest request, HttpServletResponse response) {
		Member member = null;
		if (username==null) {
			member = memberService.getCurrent();
		} else {
			member = memberService.findByUsername(username);
		}
		Area area = null;
		if (areaId!=null) {
        	area = areaService.find(areaId);
		} else {
			area = areaService.getCurrent();
		}
        if (lat!=null && lng!=null) {
    		Location lcn = new Location();
    		lcn.setLat(new BigDecimal(lng));
    		lcn.setLng(new BigDecimal(lat));
            if (areaId==null) {
        	    area = areaService.findByLbs(Double.valueOf(lat).doubleValue(), Double.valueOf(lng).doubleValue());
            }
    		if (member != null) {
    			member.setLocation(lcn);
    			member.setLbsDate(new Date());
    			member.setLbsCity(area);
    			memberService.update(member);
    		}
        }
		request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);
		AreaModel model = new AreaModel();
		model.copyFrom(areaService.getCurrent());
		return DataBlock.success(model,"执行成功");
	}

}