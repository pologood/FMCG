package net.wit.controller.wap;

import java.util.*;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductChannel.Type;
import net.wit.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("wapIndexController")
@RequestMapping("/wap")
public class IndexController extends BaseController {

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String indexnew(ModelMap model, @RequestParam(defaultValue = "0")String updateCurrent) {
        List<ProductChannel> channels = productChannelService.findByType(Type.product);
        for (ProductChannel c : channels) {
            c.setTemplate(templateService.get(c.getTemplateId()));
        }

        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);

        model.addAttribute("productChannels", channels);
        model.addAttribute("area", areaService.getCurrent());

        model.addAttribute("deliverys", deliveryCenterService.findList(null, tagService.findList(new Long[]{6l}), areaService.getCurrent(), true, null, null, 10, null, null));

        model.addAttribute("tscpath", "/wap/indexnew");
        if(activityPlanning!=null){

            return "redirect:/wap/activity/index/"+activityPlanning.getId()+".jhtml";
//            model.addAttribute("activitystate", activityPlanning.getStatus());
//            model.addAttribute("activityid", activityPlanning.getId());
        }
        model.addAttribute("type", "index");
        model.addAttribute("updateCurrent", updateCurrent);
        return "wap/indexnew";
    }

    @RequestMapping(value = "/loadmore", method = RequestMethod.GET)
    @ResponseBody
    public Page<Product> loadmore(Pageable pageable) {
        List<Tag> tags = tagService.findList(new Long[]{21l});
        pageable.setPageSize(30);
        return productService.findPage(null, null, null, tags, null, null, null, true, true, null, false, false, null, null, areaService.getCurrent(), null, null, null, OrderType.weight, pageable);
    }

    /**
     * 首页搜索页面
     *
     * @return
     */
    @RequestMapping(value = "/top_search", method = RequestMethod.GET)
    public String topSearch(Model model) {
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("isTop"));
        orders.add(Order.desc("modifyDate"));

        List<Map<String, Object>> tenantMaps = new ArrayList<Map<String, Object>>();
        List<Tenant> tenants = tenantService.openList(4,areaService.getCurrent(),null,tagService.findList(new Long[]{32l}),null,null,null,null, Tenant.OrderType.hitsDesc);

        for (Tenant tenant : tenants) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", tenant.getId());
            map.put("shortName", tenant.getShortName());
            map.put("thumbnail", tenant.getThumbnail());
            map.put("address", tenant.getAddress());
            map.put("tenantCategoryName", tenant.getTenantCategory().getName());

            List<Map<String, Object>> promotionMaps = new ArrayList<Map<String, Object>>();
            for (Promotion promotion : tenant.getPromotions()) {
                Map<String, Object> promotionMap = new HashMap<String, Object>();
                promotionMap.put("name", promotion.getName());
                promotionMap.put("type", promotion.getType());
                promotionMaps.add(promotionMap);
            }

            ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);
            if(activityPlanning!=null){
                Map<String, Object> promotion = new HashMap<String, Object>();
                promotion.put("name", activityPlanning.getName());
                promotion.put("type", Promotion.Type.activity);
                promotionMaps.add(promotion);
            }

            clearList(promotionMaps);
            map.put("promotions", promotionMaps);
            tenantMaps.add(map);
        }

        List<Map<String, Object>> productMaps = new ArrayList<Map<String, Object>>();
        List<Product> products = productService.findListByTag(areaService.getCurrent(), tagService.findList(new Long[]{19l}), 4, orders);
        for (Product product : products) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", product.getId());
            map.put("fullName", product.getName());
            map.put("price", product.calcEffectivePrice(null));
            map.put("wholePrice", product.getWholePrice());
            map.put("thumbnail", product.getThumbnail());
            productMaps.add(map);
        }

        JSONArray jsonTenant = JSONArray.fromObject(tenantMaps);
        JSONArray jsonProduct = JSONArray.fromObject(productMaps);
        model.addAttribute("tenant", jsonTenant);
        model.addAttribute("product", jsonProduct);
        return "wap/top_search";
    }

    public static void clearList(List<Map<String, Object>> list) {
        if (list == null) {
            return;
        }
        Set<Object> set = new HashSet<Object>();
        for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext(); ) {
            //里面的map至少有一个元素，不然报错
            Object value = it.next().entrySet().iterator().next().getValue();
            if (set.contains(value)) {
                it.remove();
            } else {
                set.add(value);
            }
        }
    }
}