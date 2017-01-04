/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.*;
import net.wit.Order;
import net.wit.controller.weixin.model.AdModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller - 广告
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinAdController")
@RequestMapping("/weixin/ad")
public class AdController extends BaseController {

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "adServiceImpl")
    private AdService adService;


    /**
     * 获取城市广告
     * position {70 首页横幅广告,其他按位置ID来}
     * count 读取前几个广告图
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Long position, Integer count) {
        Long adposition = 70L;
        if (position != null) {
            adposition = position;
        }
        Area area = areaService.getCurrent();
        AdPosition adPosition = adPositionService.find(adposition, null, area, count);
        return DataBlock.success(AdModel.bindData(adPosition.getAds()), "执行成功");

    }

    /**
     * 获取商家广告
     * params position {80 商家首页横幅广告}
     * id 店铺 id
     * count 读取前几个广告图
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(@PathVariable Long id, Long position, Integer count) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Long adposition = 80L;
        if (position != null) {
            adposition = position;
        }
        AdPosition adPosition = adPositionService.find(adposition, tenant, null, count);
        return DataBlock.success(AdModel.bindData(adPosition.getAds()), "执行成功");
    }

    /**
     * 获取频道广告位
     * @param id 广告位Id
     * @param productChannelId 频道id
     * @param areaId 区域Id
     * @param count 数量
     */
    @RequestMapping(value = "/channel", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock channel(Long id, Long productChannelId, Long areaId, Integer count) {
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        ProductChannel productChannel = productChannelService.find(productChannelId);
        List<ProductChannel> productChannels=new ArrayList<>();
        productChannels.add(productChannel);
        AdPosition adPosition = adPositionService.find(id);
        Pageable pageable=new Pageable();
        if(count!=null){
            pageable.setPageSize(count);
        }
        Page<Ad> page = adService.openPage(null,adPosition,area,productChannel,pageable);
        return DataBlock.success(AdModel.bindData(page.getContent()), "执行成功");
    }

}