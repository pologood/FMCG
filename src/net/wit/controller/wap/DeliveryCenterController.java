package net.wit.controller.wap;

import java.net.URLEncoder;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.wit.*;
import net.wit.controller.ajax.LBSController;
import net.wit.controller.app.model.SingleModel;
import net.wit.controller.app.model.TagModel;
import net.wit.controller.wap.model.*;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.DeliveryVo;
import net.wit.display.vo.ProductVo;
import net.wit.entity.*;
import net.wit.entity.Order;
import net.wit.entity.Product.OrderType;
import net.wit.service.*;
import net.wit.util.BrowseUtil;
import net.wit.util.JsonUtils;
import net.wit.util.WebUtils;
import net.wit.weixin.main.MenuManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("wapDeliveryCenterController")
@RequestMapping("/wap/delivery")
public class DeliveryCenterController extends BaseController {

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "productCategoryTenantServiceImpl")
    private ProductCategoryTenantService productCategoryTenantService;

    @Resource(name = "tenantCategoryServiceImpl")
    private TenantCategoryService tenantCategoryService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "searchServiceImpl")
    private SearchService searchService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "deliveryDisplay")
    private DisplayEngine<DeliveryCenter, DeliveryVo> deliveryDisplay;

    @Resource(name = "productDisplay")
    private DisplayEngine<Product, ProductVo> productDisplay;

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    /**
     * 获取商家首页
     */
    @RequestMapping(value = "/{id}/index", method = RequestMethod.GET)
    public String index(@PathVariable Long id, Long productCategoryTenantId, String back, Model model, HttpServletRequest request, Pageable pageable) {
        DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
        if (deliveryCenter == null) {
            return ERROR_VIEW;
        }
        Tenant tenant = deliveryCenter.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        Area area = areaService.getCurrent();
        Set<ProductCategoryTenant> productCategoryTenants = tenant.getProductCategoryTenants();
        Member member = memberService.getCurrent();
        if (member != null) {
            if (member.getFavoriteTenants().contains(tenant)) {
                model.addAttribute("hasFavorite", 0);
            } else {
                model.addAttribute("hasFavorite", 1);
            }
        }
        model.addAttribute("back", back);
        model.addAttribute("tenant", tenant);
        model.addAttribute("pageable", pageable);
        model.addAttribute("productCategoryTenants", productCategoryTenants);
        model.addAttribute("tenantCategory", tenant.getTenantCategory());
        model.addAttribute("productCategoryTenantId", productCategoryTenantId);
        model.addAttribute("area", area);
        model.addAttribute("member", member);
        model.addAttribute("deliveryCenter", deliveryCenter);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String header = request.getHeader("User-Agent");
        String browseVersion = BrowseUtil.checkBrowse(header);
        if (BrowseUtil.WEIXIN.equalsIgnoreCase(browseVersion)) {
            model.addAttribute("sharedUrl", MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/delivery/" + deliveryCenter.getId() + "/index.jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        } else {
            model.addAttribute("sharedUrl", URLEncoder.encode(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/delivery/" + deliveryCenter.getId() + "/index.jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        }
        return "/wap/delivery/index";
    }

    /**
     * 预览商家首页
     */
    @RequestMapping(value = "/{id}/preview", method = RequestMethod.GET)
    public String preview(@PathVariable Long id, Long productCategoryTenantId, String back, Model model, HttpServletRequest request, Pageable pageable) {
        DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
        if (deliveryCenter == null) {
            return ERROR_VIEW;
        }
        Tenant tenant = deliveryCenter.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        Area area = areaService.getCurrent();
        Set<ProductCategoryTenant> productCategoryTenants = tenant.getProductCategoryTenants();
        Member member = memberService.getCurrent();
        model.addAttribute("back", back);
        model.addAttribute("tenant", tenant);
        model.addAttribute("pageable", pageable);
        model.addAttribute("productCategoryTenants", productCategoryTenants);
        model.addAttribute("tenantCategory", tenant.getTenantCategory());
        model.addAttribute("productCategoryTenantId", productCategoryTenantId);
        model.addAttribute("area", area);
        model.addAttribute("member", member);
        model.addAttribute("deliveryCenter", deliveryCenter);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String header = request.getHeader("User-Agent");
        String browseVersion = BrowseUtil.checkBrowse(header);
        if (BrowseUtil.WEIXIN.equalsIgnoreCase(browseVersion)) {
            model.addAttribute("sharedUrl", MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/delivery/" + deliveryCenter.getId() + "/index.jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        } else {
            model.addAttribute("sharedUrl", URLEncoder.encode(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/delivery/" + deliveryCenter.getId() + "/index.jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        }
        return "/wap/delivery/preview";
    }

    /**
     * 加载更多
     */
    @RequestMapping(value = "/{id}/loadmore", method = RequestMethod.GET)
    @ResponseBody
    public Page<ProductVo> loadmore(@PathVariable Long id, Long productCategoryTenantId, OrderType orderType, Pageable pageable) {
        DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
        if (deliveryCenter == null) {
            return new Page<ProductVo>(new ArrayList<ProductVo>(), 0, pageable);
        }
        Tenant tenant = deliveryCenter.getTenant();
        if (tenant == null) {
            return new Page<ProductVo>(new ArrayList<ProductVo>(), 0, pageable);
        }
        ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
        Page<Product> page = productService.findMyPage(tenant, null, null, productCategoryTenant, null, null, null, null, null, null, true, true, null, false, false, null, orderType, pageable);
        return new Page<ProductVo>(productDisplay.convertList(page.getContent()), page.getTotal(), pageable);
    }

    /**
     * 商铺分类列表
     */
    @RequestMapping(value = "/list/{tenantCategoryId}", method = RequestMethod.GET)
    public String list(@PathVariable Long tenantCategoryId, @RequestParam(defaultValue = "false") Boolean force, HttpServletRequest request, HttpServletResponse response, Long memberId, Long communityId, Long areaId, Location location, Double distance,
                       Pageable pageable, Model model) {
        if (distance == null && communityId == null && areaId == null && memberId == null) {
            distance = Double.valueOf(5000);
        }
        List<TenantCategory> tenantCategoryList = tenantCategoryService.findRoots();
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        model.addAttribute("force", force);
        model.addAttribute("communityId", communityId);
        model.addAttribute("distance", distance);
        model.addAttribute("tenantCategoryList", tenantCategoryList);
        model.addAttribute("current", areaService.find(areaService.getCurrent().getId()));
        model.addAttribute("orderTypes", net.wit.entity.Tenant.OrderType.values());
        model.addAttribute("tenantCategoryId", tenantCategoryId);
        model.addAttribute("areaId", areaId);
        List<Community> communityList = communityService.findList(areaService.getCurrent());
        model.addAttribute("communityList", communityList);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        model.addAttribute("communityId", communityId);
        if (location != null && location.isExists()) {
            String locationStr = JsonUtils.toJson(location);
            WebUtils.addCookie(request, response, LBSController.LOCATION_COOKIE, locationStr);
        } else {
            String locationStr = WebUtils.getCookie(request, LBSController.LOCATION_COOKIE);
            if (StringUtils.isNotBlank(locationStr)) {
                location = JsonUtils.toObject(locationStr, Location.class);
            }
        }
        model.addAttribute("location", location);
        model.addAttribute("pageable", pageable);
        return "/wap/delivery/list";
    }

    /**
     * 商铺分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Long tenantCategoryId, Boolean force, Long communityId, HttpServletRequest request, HttpServletResponse response, Long memberId, Long areaId, Location location, Double distance, Model model, Pageable pageable) {
        Community community = communityService.find(communityId);
        List<TenantCategory> tenantCategoryList = tenantCategoryService.findRoots();
        TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        model.addAttribute("communityId", communityId);
        model.addAttribute("force", force);
        if (distance == null && communityId == null && areaId == null && memberId == null) {
            distance = Double.valueOf(5000);
        }
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        model.addAttribute("distance", distance);
        model.addAttribute("tenantCategoryList", tenantCategoryList);
        model.addAttribute("current", areaService.find(areaService.getCurrent().getId()));
        model.addAttribute("orderTypes", net.wit.entity.Tenant.OrderType.values());
        model.addAttribute("area", area);
        List<Community> communityList = communityService.findList(areaService.getCurrent());
        model.addAttribute("communityList", communityList);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        model.addAttribute("community", community);
        if (location != null && location.isExists()) {
            String locationStr = JsonUtils.toJson(location);
            WebUtils.addCookie(request, response, LBSController.LOCATION_COOKIE, locationStr);
        } else {
            String locationStr = WebUtils.getCookie(request, LBSController.LOCATION_COOKIE);
            if (StringUtils.isNotBlank(locationStr)) {
                location = JsonUtils.toObject(locationStr, Location.class);
            }
        }
        model.addAttribute("location", location);
        model.addAttribute("tenantCategory", tenantCategory);
        model.addAttribute("pageable", pageable);
        return "/wap/delivery/list";
    }

    /**
     * 商铺分类列表
     */

    @RequestMapping(value = "/loadmore", method = RequestMethod.GET)
    public
    @ResponseBody
    Page<DeliveryVo> loadmore(HttpServletRequest request, Long tenantCategoryId, String areaName, Long communityId, Long memberId, Long areaId, Location location, Double distance, net.wit.entity.Tenant.OrderType orderType,
                              Pageable pageable) {
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.findByAreaName(areaName);
            if (area == null) {
                area = areaService.getCurrent();
            }
        }
        if (location != null && !location.isExists()) {
            String lcn = WebUtils.getCookie(request, LBSController.LOCATION_COOKIE);
            if (StringUtils.isNotBlank(lcn)) {
                location = JsonUtils.toObject(lcn, Location.class);
            }
        }
        Community community = communityService.find(communityId);
        TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
        Set<TenantCategory> tenantCategorys = new HashSet<TenantCategory>();
        if (tenantCategory != null) {
            tenantCategorys.add(tenantCategory);
        }
        try {
            Page<DeliveryCenter> page = deliveryCenterService.findPage(tenantCategorys, area, community, location, distance, orderType, pageable);
            return new Page<DeliveryVo>(deliveryDisplay.convertList(page.getContent()), page.getTotal(), pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<DeliveryVo>();
        }
    }

    /**
     * 搜索
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(String keyword, Long tenantCategoryId, Location location, HttpServletRequest request, HttpServletResponse response, Long communityId, Long areaId, net.wit.entity.Tenant.OrderType orderType, Pageable pageable, ModelMap model) {
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
        model.addAttribute("communityId", communityId);
        model.addAttribute("tenantCategory", tenantCategory);
        List<Community> communityList = communityService.findList(area);
        model.addAttribute("communityList", communityList);
        List<TenantCategory> tenantCategoryRootList = tenantCategoryService.findRoots();
        model.addAttribute("tenantCategoryRootList", tenantCategoryRootList);
        model.addAttribute("area", area);
        model.addAttribute("orderTypes", net.wit.entity.Tenant.OrderType.values());
        if (location != null && location.isExists()) {
            String locationStr = JsonUtils.toJson(location);
            WebUtils.addCookie(request, response, LBSController.LOCATION_COOKIE, locationStr);
        } else {
            String locationStr = WebUtils.getCookie(request, LBSController.LOCATION_COOKIE);
            if (StringUtils.isNotBlank(locationStr)) {
                location = JsonUtils.toObject(locationStr, Location.class);
            }
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        model.addAttribute("orderType", orderType);
        model.addAttribute("pageable", pageable);
        return "wap/delivery/search";
    }


    /**
     * 商家简介
     */
    @RequestMapping(value = "/introduction", method = RequestMethod.GET)
    public String introduction(Long deliveryCenterId, HttpServletRequest request, ModelMap model) {
        DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
        model.addAttribute("tenant", deliveryCenter.getTenant());
        model.addAttribute("deliveryCenter", deliveryCenter);
        return "/wap/delivery/introduction";
    }

    /**
     * 附近商家列表
     */
    @RequestMapping(value = "/nearby", method = RequestMethod.GET)
    public String nearby(ModelMap model) {
        model.addAttribute("type", "nearby");
        model.addAttribute("area", "安徽省合肥市瑶海区中绿二期");
        return "/wap/delivery/nearby";
    }

    /**
     * 明星商家
     */
    @RequestMapping(value = "/starBusiness", method = RequestMethod.GET)
    public String starBusiness(ModelMap model) {
        Long[] ids = {6L};
        List<Tag> tags = tagService.findList(ids);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        List<DeliveryCenter> deliverys = deliveryCenterService.findList(null, tags, areaService.getCurrent(), true, null, null, 10, null, null);
        for (DeliveryCenter deliveryCenter : deliverys) {
            Tenant tenant = deliveryCenter.getTenant();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", tenant.getId());
            map.put("shortName", tenant.getShortName());
            map.put("thumbnail", tenant.getThumbnail());
            List<Map<String, Object>> coupons = new ArrayList<>();

            //AdPosition adPosition = adPositionService.find(80l,tenant,null,1);
            Map<String, Object> map1 = adPositionService.find(80l, tenant.getId());
            map.put("path", map1.get("path"));
            for (Coupon coupon : tenant.getCoupons()) {
                Map<String, Object> couponMap = new HashMap<>();
                couponMap.put("name", coupon.getName());
                couponMap.put("status", coupon.getStatus());
                coupons.add(couponMap);
            }
            map.put("coupons", coupons);
            List<Map<String, Object>> productImages = new ArrayList<>();

            int xp = 0, cx = 0;
            for (Product product : tenant.getProducts()) {
                for (Tag tag : product.getTags()) {
                    if (5 == tag.getId()) {
                        Map<String, Object> imageMap = new HashMap<>();
                        imageMap.put("id", product.getId());
                        String thumbnail = "";
                        if(product.getProductImages().size()>0){
                            thumbnail = product.getProductImages().get(0).getMedium();
                        }
                        imageMap.put("thumbnail", thumbnail);
                        productImages.add(imageMap);
                    }

                    if (2 == tag.getId()) {
                        xp++;
                    }
                    if (15 == tag.getId()) {
                        cx++;
                    }
                }
            }
            if (productImages.size() >= 3) {
                for (int ii = 0; ii < productImages.size(); ii++) {
                    if (ii == 0) {
                        map.put("products1", productImages.get(ii));
                    } else if (ii == 1) {
                        map.put("products2", productImages.get(ii));
                    } else if (ii == 2) {
                        map.put("products3", productImages.get(ii));
                    }
                }
            }
            map.put("xp", xp);
            map.put("cx", cx);
            map.put("qb", tenant.getProducts().size());
            map.put("favoriteNum", tenant.getFavoriteMembers().size());//关注会员人数
            mapList.add(map);
        }
        JSONArray jsonArray = JSONArray.fromObject(mapList);
        model.addAttribute("count", deliverys.size());
        model.addAttribute("tenants", jsonArray);
        model.addAttribute("starBusiness", "starBusiness");
        return "/wap/delivery/starBusiness";
    }
}
