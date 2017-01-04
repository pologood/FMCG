package net.wit.controller.app;

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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller("appIndexController")
@RequestMapping("/app")
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
    public String indexnew(String areaId,String lat,String lng, ModelMap model,HttpServletRequest request) {
        List<ProductChannel> channels = productChannelService.findByType(Type.product);
        for (ProductChannel c : channels) {
            c.setTemplate(templateService.get(c.getTemplateId()));
        }

        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(null, ActivityPlanning.Type.random);

        model.addAttribute("productChannels", channels);

        //地区校正
        Area area = areaService.getCurrent();
        if(areaId!=null&&!areaId.equals(area.getId().toString())) {
            Area areaApp = areaService.find(Long.valueOf(areaId));
            request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, areaApp);
        }
        model.addAttribute("area", areaService.getCurrent());
        if (lat!=null){
            model.addAttribute("lat",Float.valueOf(lat) );
        }else{
            model.addAttribute("lat","31.820587" );
        }
        if (lng!=null){
            model.addAttribute("lng", Float.valueOf(lng));
        }else {
            model.addAttribute("lng","117.227239");
        }

        model.addAttribute("deliverys", deliveryCenterService.findList(null, tagService.findList(new Long[]{6l}), areaService.getCurrent(), true, null, null, 10, null, null));

        model.addAttribute("tscpath", "/app/indexnew");
//        if(activityPlanning!=null){
//            model.addAttribute("activitystate", activityPlanning.getStatus());
//            model.addAttribute("activityid", activityPlanning.getId());
//        }
        model.addAttribute("type", "index");
        return "app/indexnew";
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
        return "app/top_search";
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