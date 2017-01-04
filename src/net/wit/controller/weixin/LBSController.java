/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.AreaModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Area;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller - 地理信息控制
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinLBSController")
@RequestMapping("/weixin/lbs")
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
    public DataBlock currentArea() {
        AreaModel model = new AreaModel();
        model.copyFrom(areaService.getCurrent());
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 经纬度获取城市
     *
     * @param lat 纬度
     * @param lng 经度
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock get(String lat, String lng, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        Area currentArea = areaService.getCurrent();
        Area area = areaService.findByLbs(Double.valueOf(lat), Double.valueOf(lng));
        AreaModel model = new AreaModel();
        if (area == null && currentArea != null) {
            area = currentArea;
        }
        if (area != null) {
            model.copyFrom(area);
        }
        Area sArea = (Area) request.getSession().getAttribute(Member.AREA_ATTRIBUTE_NAME);
        if (sArea == null) {
            data.put("update", !area.equals(currentArea));
        } else {
            data.put("update", false);
        }
        data.put("area", model);
        if (currentArea != null) {
            request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, currentArea);
        }
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 区域--更新地理位置
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock update(String lat, String lng, Long areaId, String username, HttpServletRequest request) {
        Member member;
        if (username == null) {
            member = memberService.getCurrent();
        } else {
            member = memberService.findByUsername(username);
        }
        Area area;
        if (areaId != null) {
            area = areaService.find(areaId);
        } else {
            area = areaService.getCurrent();
        }
        Location lcn = new Location();
        if (lat != null && lng != null) {
            lcn.setLat(new BigDecimal(lng));
            lcn.setLng(new BigDecimal(lat));
            if (areaId == null) {
                area = areaService.findByLbs(Double.valueOf(lat), Double.valueOf(lng));
            }
        }else{
            lcn=area.getLocation();
        }
        if (member != null) {
            member.setLocation(lcn);
            member.setLbsDate(new Date());
            member.setLbsCity(area);
            memberService.update(member);
        }
        request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);
        AreaModel model = new AreaModel();
        model.copyFrom(areaService.getCurrent());
        return DataBlock.success(model, "执行成功");
    }

}