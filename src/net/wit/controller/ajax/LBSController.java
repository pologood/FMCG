/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.wit.Message;
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
@Controller("ajaxLBSController")
@RequestMapping("/ajax/lbs")
public class LBSController extends BaseController {

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	public static final String LOCATION_COOKIE = "Location_Cookie";

	/**
	 * 区域--当前地理位置
	 */
	@RequestMapping(value = "/currentArea", method = RequestMethod.GET)
	@ResponseBody
	public Area currentArea(HttpServletRequest request, HttpServletResponse response) {
		
		return areaService.getCurrent();
	}
	/**
	 * 区域--更新地理位置
	 */
	@RequestMapping(value = "/location", method = RequestMethod.GET)
	@ResponseBody
	public Message location(Boolean force, HttpServletRequest request, HttpServletResponse response) {
		Area area = (Area) request.getSession().getAttribute(Member.AREA_ATTRIBUTE_NAME);
		//强制更新
		if (area==null || force) {
			String ip = request.getRemoteAddr();
			//System.out.println("lbs="+ip);
			String cityId=IPUtil.getIPInfo(ip);  
			if (cityId!=null) {
				area = areaService.findByCode(cityId);
				if (area!=null) {
				   request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);
				   //System.out.println("lbs="+area.getFullName());
				}
			}
		}
		return Message.success("success");
	}
	/**
	 * 区域--更新地理位置
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public Message update(String lat, String lng, String username, String sign, Boolean force, HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.findByUsername(username);

		Location lcn = new Location();
		lcn.setLat(new BigDecimal(lng));
		lcn.setLng(new BigDecimal(lat));

		WebUtils.addCookie(request, response, LBSController.LOCATION_COOKIE, JsonUtils.toJson(lcn));

		if (member != null) {
			member.setLocation(lcn);
			member.setLbsDate(new Date());
			memberService.update(member);
		}
		Area area = (Area) request.getSession().getAttribute(Member.AREA_ATTRIBUTE_NAME);

		// 强制更新
		if ((force != null) && force) {
			area = areaService.findByLbs(Double.valueOf(lat).doubleValue(), Double.valueOf(lng).doubleValue());
			if (area == null) {
				area = areaService.getCurrent();
			}
			request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);
		} else {
			if (area == null) {
				area = areaService.findByLbs(Double.valueOf(lat).doubleValue(), Double.valueOf(lng).doubleValue());
				if (area == null) {
					area = areaService.getCurrent();
				}
				request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);
			}
		}

		return Message.success(area.getId().toString());
	}

}