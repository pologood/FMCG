/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.CommunityModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.Tag;
import net.wit.service.AreaService;
import net.wit.service.CommunityService;
import net.wit.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 地区
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinAreaController")
@RequestMapping("/weixin/area")
public class AreaController extends BaseController {

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    /**
     * 全城
     */
    @RequestMapping(value = "/community", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock community() {
        Map<String, Object> map = null;
        try {
            Area current = areaService.getCurrent();
            map = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            for (Area area1 : current.getChildren()) {
                Map<String, Object> areaMap = new HashMap<>();
                Area area=areaService.find(area1.getId());
                areaMap.put("id", area.getId());
                areaMap.put("name", area.getName());
                areaMap.put("communities", CommunityModel.bindData(area.getCommunities()));
                list.add(areaMap);
            }
            Long[] ids = {7L};
            List<Tag> tags = tagService.findList(ids);
            List<Community> communities = communityService.findHot(current, tags);
            map.put("area", list);
            map.put("hotCommunity", CommunityModel.bindData(communities));
        } catch (Exception e) {
            System.out.println("全城接口出错：");
            e.printStackTrace();
        }
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 区域选择（获取下级区域）
     */
    @RequestMapping(value = "/children", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock children(Long areaId) {
        Area parent = areaService.find(areaId);
        List<Area> areas;
        List<Map<String, Object>> list = new ArrayList<>();
        if (parent == null) {
            areas = areaService.findRoots();
        } else {
            areas = new ArrayList<>(parent.getChildren());
        }
        for (Area area : areas) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", area.getId());
            map.put("name", area.getName());
            map.put("fullName", area.getFullName());
            map.put("hasChildren", area.getChildren().size() > 0);
            list.add(map);
        }
        return DataBlock.success(list, "执行成功");
    }

}