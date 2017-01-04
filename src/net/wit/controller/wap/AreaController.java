/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.wap;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.entity.Area;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.util.WebUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 地区
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("AreaController")
@RequestMapping("/wap/area")
public class AreaController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

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
    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public String getCitys(HttpServletRequest request, Long areaId, ModelMap model) {
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        model.addAttribute("area", area);
        return "wap/area/city";
    }

    /**
     * 获取
     */
    @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
    @ResponseBody
    public Area getArea(Long id) {
        Area area = areaService.find(id);
        return area;
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
    public Area getCurrent(HttpServletRequest request, HttpServletResponse response) {
        Area area = areaService.getCurrent();
        request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);
        return area;
    }

    /**
     * 跟新session area
     */
    @RequestMapping(value = "/update_current", method = RequestMethod.GET)
    public String updateCurrent(Long id, String lat, String lng, HttpServletRequest request, HttpServletResponse response) {
        Area area = null;
        if (id == null) {
            if (lat != null && lng != null) {
                area = areaService.findByLbs(Double.parseDouble(lat), Double.parseDouble(lng));
            }
        } else {
            area = areaService.find(id);
        }

        //未开通城市
        if (area.getIsOpen() == null || !area.getIsOpen()) {
            return "redirect:../html/city/recruitment.html";
        }

        request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);

        Member member = memberService.getCurrent();

        if (member != null) {
            if (lat != null && lng != null) {
                member.setLocation(
                        new Location(
                                new BigDecimal(lat), new BigDecimal(lng)
                        )
                );
            }
            member.setLbsDate(new Date());
            member.setLbsCity(area);
            memberService.update(member);
        }

        WebUtils.addCookie(request, response, Area.AREA_NAME, area.getName());
        WebUtils.addCookie(request, response, Area.AREA_ID, area.getId().toString());
        return "redirect:/wap/index.jhtml?updateCurrent=1";
    }


}