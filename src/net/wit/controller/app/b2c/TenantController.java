/**
 * ====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.controller.app.b2c;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Setting;
import net.wit.controller.app.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.entity.Tenant.OrderType;

/**
 * @author Administrator
 * @ClassName: TenantController
 * @Description: 商家
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appB2cTenantController")
@RequestMapping("/app/b2c/tenant")
public class TenantController extends BaseController {

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;
    @Resource(name = "tenantCategoryServiceImpl")
    private TenantCategoryService tenantCategoryService;
    @Resource(name = "tagServiceImpl")
    private TagService tagService;
    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;
    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;
    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;
    @Resource(name = "visitRecordServiceImpl")
    private VisitRecordService visitRecordService;

    /**
     * 商家首页
     *
     * @param id        店铺Id
     * @param promotion 是否绑定促销
     * @param location  当前地理经纬度
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock index(Long id, Boolean promotion, Location location) {
        Member member = memberService.getCurrent();
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("无效店铺id");
        }
        TenantModel model = new TenantModel();
        model.copyFrom(tenant);
        if (promotion != null) {
            if (promotion) {
                model.bindPromoton(tenant.getVaildPromotions());
            }
        }
        model.setHits(tenantService.viewHits(tenant.getId()));

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        //==============================================================================================
//        filters.add(new Filter("role", Filter.Operator.like, Employee.Role.guide.toString()));
        List<Employee> guides = employeeService.findList(null, filters, null);

        Boolean hasFavorited = false;
        if (member != null) {
            hasFavorited = tenant.getFavoriteMembers().contains(member);
        }

        Set<Coupon> coupons = new HashSet<>();
        for (Coupon coupon : tenant.getCoupons()) {
            if (coupon.getExpired()) {
                coupons.add(coupon);
            }
        }

        List<Tag> tags = tagService.findList(new Long[]{1L});
        List<Product> hotPorducts = productService.openList(4, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, null, null);

        tags = tagService.findList(new Long[]{2L});
        List<Product> newProducts = productService.openList(4, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, null, null);

        tags = tagService.findList(new Long[]{5L});
        List<Product> recommendedProducts = productService.openList(4, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, null, null);

        filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        List<DeliveryCenter> deliveryCenters = deliveryCenterService.findList(null, filters, null);

        visitRecordService.add(member,tenant,null,null, VisitRecord.VisitType.app);

        Map<String, Object> map = new HashMap<>();
        map.put("tenant", model);
        map.put("guides", GuideModel.bindData(guides));
        map.put("favoriteMembers", tenant.getFavoriteMembers().size());
        map.put("hasFavorited", hasFavorited);
        map.put("coupons", TenantCouponModel.bindData(new ArrayList<>(coupons), member));
        map.put("hotPorducts", ProductListModel.bindData(hotPorducts));
        map.put("newProducts", ProductListModel.bindData(newProducts));
        map.put("recommendedProducts", ProductListModel.bindData(recommendedProducts));
        map.put("productCategoryTenants", ProductCategoryTenantModel.bindData(new ArrayList<>(tenant.getProductCategoryTenants())));
        map.put("deliveryCenters", DeliveryCenterListModel.bindData(deliveryCenters, location));

        return DataBlock.success(map, "执行成功");
    }

    /**
     * 商家详情
     *
     * @param id        店铺ID
     * @param promotion 是否绑定促销
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id, Boolean promotion, Location location) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("无效店铺id");
        }
        TenantModel model = new TenantModel();
        model.copyFrom(tenant, location, memberService.getCurrent());
        if (promotion != null) {
            if (promotion) {
                model.bindPromoton(tenant.getVaildPromotions());
            }
        }
        visitRecordService.add(memberService.getCurrent(), tenant, null, null, VisitRecord.VisitType.app);
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 商家列表
     * tenantCategoryId 商家分类
     * areaId 区域
     * communityId 商圈
     * keyword 搜索关键词
     * orderType 排序 {默认排序 weight,点击降序 hitsDesc,评分降序 scoreDesc,日期降序 dateDesc}
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Long tenantCategoryId, Long areaId, Long channelId, Location location, BigDecimal distatce, Long[] tagIds, Long communityId, Boolean isPromotion, Boolean isUnion,String keyword, OrderType orderType, Pageable pageable, HttpServletRequest request) {
        Page<Tenant> page = null;
        try {
            Area area = areaService.find(areaId);
            if (area == null) {
                area = areaService.getCurrent();
            }
            TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
            Set<TenantCategory> tenantCategorys = new HashSet<TenantCategory>();
            if (tenantCategory != null) {
                tenantCategorys.add(tenantCategory);
            } else {
                if (channelId != null) {
                    ProductChannel channel = productChannelService.find(channelId);
                    if (channel != null) {
                        for (TenantCategory tc : channel.getTenantCategorys()) {
                            tenantCategorys.add(tc);
                        }
                        if (tenantCategorys.size() == 0) {
                            return DataBlock.success(TenantListModel.bindData(new ArrayList<Tenant>(), location), "执行成功");
                        }
                    }
                }
            }
            if (orderType == null) {
                orderType = OrderType.distance;
            }
            List<Tag> tags = tagService.findList(tagIds);
            Community community = communityService.find(communityId);
            page = tenantService.openPage(pageable, area, tenantCategorys, tags, keyword, location, distatce, community, orderType, isPromotion,isUnion,null);

            for (Tenant tenant : page.getContent()) {
                ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);
                if (activityPlanning != null) {
                    Promotion promotion = new Promotion();
                    promotion.setName(activityPlanning.getName());
                    promotion.setType(Promotion.Type.activity);
                    tenant.getPromotions().add(promotion);
                }
            }
        } catch (Exception e) {
            System.out.println("app/b2c/tenant/list接口异常：");
            e.printStackTrace();
        }
        return DataBlock.success(TenantListModel.bindData(page.getContent(), location), "执行成功");
    }

    /**
     * 联盟商家
     *
     * @param id       店铺Id
     * @param location 地理经纬度（lng,lat）
     * @param pageable 分页信息
     */
    @RequestMapping(value = "/supplier/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock suppliers(Long id, Location location, Pageable pageable) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("无效店铺id");
        }
        Page<TenantRelation> tenantRelationPage = tenantRelationService.findMyParent(tenant, TenantRelation.Status.success, pageable);
        List<Tenant> tenants = new ArrayList<>();
        for (TenantRelation tenantRelation : tenantRelationPage.getContent()) {
            tenants.add(tenantRelation.getParent());
        }
        return DataBlock.success(SupplierListModel.bindData(tenants, location), "执行成功");
    }

    /**
     * 店铺导购列表
     *
     * @param id       店铺Id
     * @param pageable 分页信息
     */
    @RequestMapping(value = "/guide/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock guides(Long id, Pageable pageable) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("无效店铺id");
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        //==============================================================================
//        filters.add(new Filter("role", Filter.Operator.like, "%" + Employee.Role.guide.toString() + "%"));
        pageable.setFilters(filters);
        Page<Employee> page = employeeService.findPage(pageable);
        List<GuideModel> models = new ArrayList<>();
        for (Employee employee : page.getContent()) {
            if (employee.getMember() != null) {
                GuideModel model = new GuideModel();
                model.copyFrom(employee, memberService.findFans(employee.getMember()).size(), memberService.getCurrent());
                models.add(model);
            }
        }
        return DataBlock.success(models, "执行成功");
    }

    /**
     * 活动商家
     * tenantCategoryId 商家分类
     * areaId 区域
     */
    @RequestMapping(value = "/promotion", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock promotion(Long areaId, Pageable pageable, HttpServletRequest request) {
        Area area = null;// areaService.getCurrent();
        Page page = tenantService.findPage(null, area, true, pageable);
        List<TenantListModel> models = TenantListModel.bindData(page.getContent());
        for (TenantListModel model : models) {
            Tenant tenant = tenantService.find(model.getId());
            model.bindPromoton(tenant.getVaildPromotions());
        }
        return DataBlock.success(models, "执行成功");
    }

    /**
     * 热门搜索
     */
    @RequestMapping(value = "/hot_search", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock hotSearch() {
        Setting setting = SettingUtils.get();
        return DataBlock.success(setting.getHotTenantSearches(), "执行成功");
    }


//    /**
//     * 附近商家列表  这个微信专用，将关闭
//     * tenantCategoryId 商家分类
//     * areaId 区域
//     * communityId 商圈
//     * keyword 搜索关键词
//     */
//	@RequestMapping(value = "/nearby", method = RequestMethod.GET)
//    @ResponseBody
//    public DataBlock nearby(Long tenantCategoryId,Long communityId,String keyword,String id, Pageable pageable) {
//    	TenantCategory tenantCategorys=new TenantCategory();
//    	if(tenantCategoryId!=null){
//    		tenantCategorys=tenantCategoryService.find(tenantCategoryId);
//    	}
//    	Community community=new Community();
//    	if(communityId!=null){
//    		community=communityService.find(communityId);
//    	}
//    	List<Order> orders =new ArrayList<>();
//        if ("001".equals(id)) {
//            orders.add(Order.desc("createDate"));
//        } else if ("002".equals(id)) {
//            orders.add(Order.desc("hits"));
//        } else if ("003".equals(id)) {
//            orders.add(Order.desc("score"));
//        } else if ("004".equals(id)) {
//            orders.add(Order.desc("createDate"));
//        }else{
//        	orders.add(Order.desc("createDate"));
//        }
//        List<Tenant> tenants=new ArrayList<Tenant>();
//        if(tenantCategoryId==null&&communityId==null){
//        	tenants=tenantService.findList(null, null, areaService.getCurrent(), null, false, null, null, orders);
//        }else if(tenantCategoryId!=null&&communityId==null){
//        	tenants=tenantService.findList(tenantCategorys, null, areaService.getCurrent(), null, false, null, null, orders);
//        }else if(tenantCategoryId==null&&communityId!=null){
//        	tenants=tenantService.findList(null, null, areaService.getCurrent(), community, false, null, null, orders);
//        }
//        List<Map<String, Object>> tenantMaps = new ArrayList<>();
//        for (Tenant tenant : tenants) {
//            Map<String, Object> _map = new HashMap<>();
//            _map.put("id", tenant.getId());
//            _map.put("shortName", tenant.getShortName());
//            _map.put("thumbnail", tenant.getThumbnail());
//            _map.put("address", tenant.getAddress());
//            _map.put("tenantCategoryName", tenant.getTenantCategory().getName());
//
//            List<Map<String, Object>> promotionMaps = new ArrayList<>();
//            for (Promotion promotion : tenant.getPromotions()) {
//                Map<String, Object> promotionMap = new HashMap<>();
//                promotionMap.put("name", promotion.getName());
//                promotionMap.put("type", promotion.getType());
//                promotionMaps.add(promotionMap);
//            }
//            clearList(promotionMaps);
//            _map.put("promotions", promotionMaps);
//            tenantMaps.add(_map);
//        }
//        return DataBlock.success(tenantMaps, "执行成功");
//    }

//    public static void clearList(List<Map<String, Object>> list) {
//        if (list == null) {
//            return;
//        }
//        Set<Object> set = new HashSet<Object>();
//        for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext(); ) {
//            //里面的map至少有一个元素，不然报错
//            Object value = it.next().entrySet().iterator().next().getValue();
//            if (set.contains(value)) {
//                it.remove();
//            } else {
//                set.add(value);
//            }
//        }
//    }

}