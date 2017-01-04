/*
 * Copyright 2005-2013 rsico.net. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Ad.Type;
import net.wit.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * Controller - 广告
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminAdController")
@RequestMapping("/admin/ad")
public class AdController extends BaseController {

    @Resource(name = "adServiceImpl")
    private AdService adService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "singleProductPositionServiceImpl")
    private SingleProductPositionService singleProductPositionService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;
    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Long adPositionId, ModelMap model) {
        model.addAttribute("productChannels", productChannelService.findAll());
        model.addAttribute("types", Type.values());
        model.addAttribute("area", areaService.getCurrent());
        model.addAttribute("linkTypes", Ad.LinkType.values());
        model.addAttribute("adPositionId", adPositionId);
        model.addAttribute("adPositions", adPositionService.findAll());
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("status", Filter.Operator.eq, Tenant.Status.success));
        model.addAttribute("tenants", tenantService.openList(null, areaService.getCurrent(), null, null, null, null, null, filters, Tenant.OrderType.scoreDesc));
        model.addAttribute("activityPlannings", activityPlanningService.findUnionActivity());
        return "/admin/ad/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Ad ad, Long adPositionId, Long areaId,Long productChannelId,Long unionActivityId, RedirectAttributes redirectAttributes) {
        ad.setAdPosition(adPositionService.find(adPositionId));
        if (!isValid(ad)) {
            return ERROR_VIEW;
        }
//		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
//			return ERROR_VIEW;
//		}
        if (ad.getType() == Type.text) {
            ad.setPath(null);
        } else {
            ad.setContent(null);
        }
        ProductChannel productChannel = productChannelService.find(productChannelId);
        Set<ProductChannel> productChannels = new HashSet<>();
        if(productChannel!=null){
            productChannels.add(productChannel);
        }
        if(unionActivityId!=null){
            ad.setLinkId(unionActivityId);
        }
        ad.setProductChannels(productChannels);
        ad.setArea(areaService.find(areaId));
        adService.save(ad);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Ad ad = adService.find(id);

        if (ad == null) {
            addFlashMessage(redirectAttributes, Message.error("无效的广告编号"));
            return "redirect:list.jhtml";
        }

        if (ad.getLinkType() == Ad.LinkType.tenant) {
            model.addAttribute("_tenant", tenantService.find(ad.getLinkId()));
        }

        if (ad.getLinkType() == Ad.LinkType.product) {
            model.addAttribute("_product", productService.find(ad.getLinkId()));
        }

        if(ad.getLinkType().equals(Ad.LinkType.unionActivity)){
            model.addAttribute("_activity", activityPlanningService.find(ad.getLinkId()));
        }
        model.addAttribute("activityPlannings", activityPlanningService.findUnionActivity());
        model.addAttribute("types", Type.values());
        model.addAttribute("linkTypes", Ad.LinkType.values());
        model.addAttribute("ad", ad);
        model.addAttribute("adPositions", adPositionService.findAll());
        model.addAttribute("productChannels", productChannelService.findAll());
        return "/admin/ad/edit";
    }

    /**
     * 根据店铺获取商品列表
     */
    @RequestMapping(value = "/getProductOrTenant", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getProduct(String searchValue, String type, Long id) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        if (searchValue != null) {
            if ("product".equals(type)) {
                List<Product> products = productService.openList(null, null, null, true, true, null, null, null, searchValue, null, null, null, null, null, null, Product.OrderType.dateDesc);
                for (Product product : products) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", product.getId());
                    map.put("name", product.getFullName());
                    mapList.add(map);
                }
            } else if ("tenant".equals(type)) {
                List<Filter> filters = new ArrayList<Filter>();
                filters.add(new Filter("status", Filter.Operator.eq, Tenant.Status.success));
                List<Tenant> tenants = tenantService.openList(null, null, null, null, searchValue, null, null, filters, Tenant.OrderType.scoreDesc);
                for (Tenant tenant : tenants) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tenant.getId());
                    map.put("name", tenant.getName());
                    mapList.add(map);
                }
            }
        }
        if (id != null) {
            if ("product".equals(type)) {
                Product product = productService.find(id);
                if(product!=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", product.getId());
                    map.put("name", product.getFullName());
                    mapList.add(map);
                }

            } else if ("tenant".equals(type)) {
                Tenant tenant = tenantService.find(id);
                if(tenant!=null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tenant.getId());
                    map.put("name", tenant.getName());
                    mapList.add(map);
                }
            }
        }

        return mapList;
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Ad ad, Long adPositionId, Long areaId,Long productChannelId,Long unionActivityId, RedirectAttributes redirectAttributes) {
        ad.setAdPosition(adPositionService.find(adPositionId));
        if (!isValid(ad)) {
            return ERROR_VIEW;
        }
//		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
//			return ERROR_VIEW;
//		}
        if (ad.getType() == Type.text) {
            ad.setPath(null);
        } else {
            ad.setContent(null);
        }

        ProductChannel productChannel = productChannelService.find(productChannelId);
        Set<ProductChannel> productChannels = new HashSet<>();
        if(productChannel!=null){
            productChannels.add(productChannel);
        }
        ad.setProductChannels(productChannels);
        if(unionActivityId!=null){
            ad.setLinkId(unionActivityId);
        }
        ad.setArea(areaService.find(areaId));
        adService.update(ad);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Long adPositionId, Pageable pageable, ModelMap model) {
        pageable.setSearchProperty("title");
        if (adPositionId != null) {
            AdPosition adPosition = adPositionService.find(adPositionId);
            model.addAttribute("page", adService.findPage(adPosition, pageable));
        } else {
            model.addAttribute("page", adService.findPage(pageable));
        }
        model.addAttribute("adPositionId", adPositionId);
        return "/admin/ad/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        adService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    /**
     * 获取社区信息
     */
    @RequestMapping(value = "/get_community", method = RequestMethod.GET)
    public @ResponseBody
    List<Map<String,Object>> getCommunity(Long areaId) {
        List<Map<String, Object>> data = new ArrayList<>();
        Area area = areaService.find(areaId);
        List<Community> communitys = communityService.findList(area);
        for (Community community : communitys) {
            Map<String,Object> map=new HashMap<>();
            map.put("id",community.getId());
            map.put("name",community.getName());
            map.put("location",community.getLocation());
            data.add(map);
        }
        return data;
    }

}