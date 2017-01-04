package net.wit.controller.weixin;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.weixin.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 店铺
 * Created by WangChao on 2016-10-11.
 */
@Controller("weixinTenantController")
@RequestMapping("/weixin/tenant")
public class TenantController {
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
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;
    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;
    @Resource(name = "unionTenantServiceImpl")
    private UnionTenantService unionTenantService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "cartServiceImpl")
    private CartService cartService;
    @Resource(name = "visitRecordServiceImpl")
    private VisitRecordService visitRecordService;

    /**
     * 优惠买单
     * @param extension 推广人
     * @param id 商家Id
     */
    @RequestMapping(value = "/offerToPay", method = RequestMethod.GET)
    public String offerToPay(String extension, Long id, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("id",id);
        return "weixin/tenant/offerToPay";
    }

    /**
     * 优惠买单页分享
     *
     * @param id 商品Id
     */
    @RequestMapping(value = "/offerToPay/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock offerToPayShare(Long id) {
        Member member = memberService.getCurrent();
        Tenant tenant = tenantService.find(id);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/offerToPay.jhtml?id=" + id + "&extension=" + (member != null ? member.getUsername() : "")));
        Map<String, Object> map = new HashMap<>();
        map.put("link", url);
        map.put("title", tenant.getName());
        map.put("desc", "买单立减，这里有您想象不到的优惠活动！！！");
        map.put("imgUrl", tenant.getThumbnail() == null ? tenant.getLogo() : tenant.getThumbnail());
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 商家列表
     *
     * @param tenantCategoryId 店铺分类Id
     * @param areaId           区域Id
     * @param channelId        频道Id
     * @param location         坐标
     * @param distatce         距离内
     * @param tagIds           标签Ids
     * @param communityId      商圈Id
     * @param isPromotion      优惠商家true，全部商家false或null
     * @param isUnion          是否是联盟商家
     * @param keyword          关键字
     * @param orderType        排序方式（默认排序 weight,点击降序 hitsDesc,评分降序 scoreDesc,日期降序 dateDesc,距离优先 distance）
     * @param pageable         分页参数（pageSize每页记录数,pageNumber页码）
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock List(Long tenantCategoryId, Long areaId, Long channelId, Location location, BigDecimal distatce, Long[] tagIds, Long communityId, Boolean isPromotion, Boolean isUnion, String keyword, Tenant.OrderType orderType, Pageable pageable) {
        Object data = null;
        Page<Tenant> page = new Page<>();
        try {
            Area area = areaService.find(areaId);
            if (area == null) {
                area = areaService.getCurrent();
            }
            TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
            Set<TenantCategory> tenantCategorys = new HashSet<>();
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
                            return DataBlock.success(TenantListModel.bindData(new ArrayList<Tenant>()), "执行成功");
                        }
                    }
                }
            }
            if (orderType == null) {
                orderType = Tenant.OrderType.distance;
            }
            List<Tag> tags = tagService.findList(tagIds);
            Community community = communityService.find(communityId);
            page = tenantService.openPage(pageable, area, tenantCategorys, tags, keyword, location, distatce, community, orderType, isPromotion, isUnion, null);
            data = TenantListModel.bindData(page.getContent(), location);
        } catch (Exception e) {
            System.out.println("weixin/tenant/list接口异常：");
            e.printStackTrace();
        }
        return DataBlock.success(data, page, "执行成功");
    }

    /**
     * 商家首页
     * @param extension 推广人
     * @param id 商家Id
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(String extension, Long id, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("id",id);
        return "weixin/tenant/index";
    }

    /**
     * 附近页面
     * @param extension 推广人
     */
    @RequestMapping(value = "/nearby", method = RequestMethod.GET)
    public String index(String extension, HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/tenant/nearby";
    }

    /**
     * 附近页分享
     */
    @RequestMapping(value = "nearby/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock nearbyShare(){
        Member member=memberService.getCurrent();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/nearby.jhtml?extension=" + (member != null ? member.getUsername() : "")));
        Map<String,Object> map=new HashMap<>();
        map.put("link", url);
        return DataBlock.success(map,"执行成功");
    }

    /**
     * 商家详情
     *
     * @param id 店铺ID
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id, Location location) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("无效店铺id");
        }
        TenantViewModel model = new TenantViewModel();
        model.copyFrom(tenant, memberService.getCurrent(), location, cartService.getCurrent());
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 商家首页页分享
     * @param id 商品Id
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock share(Long id){
        Member member=memberService.getCurrent();
        Tenant tenant=tenantService.find(id);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/index.jhtml?id="+id+"&extension=" + (member != null ? member.getUsername() : "")));
        Map<String,Object> map=new HashMap<>();
        map.put("link", url);
        map.put("title", tenant.getName());
        map.put("desc", StringUtils.isBlank(tenant.getIntroduction())?tenant.getTenantCategory().getName():tenant.getIntroduction());
        map.put("imgUrl", tenant.getThumbnail());
        return DataBlock.success(map,"执行成功");
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
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filters);
        Page<Employee> page = employeeService.findPage(pageable);
        List<GuideListModel> models = new ArrayList<>();
        for (Employee employee : page.getContent()) {
            if (employee.getMember() != null) {
                GuideListModel model = new GuideListModel();
                model.copyFrom(employee, memberService.findFans(employee.getMember()).size(), memberService.getCurrent());
                models.add(model);
            }
        }
        return DataBlock.success(models, page, "执行成功");
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

    /**
     * 获取商家门店列表
     *
     * @param id 店铺Id
     */
    @RequestMapping(value = "/deliveryCenter/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock deliveryCenterList(Long id, Pageable pageable) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filters);
        Page<DeliveryCenter> page = deliveryCenterService.findPage(pageable);
        return DataBlock.success(DeliveryCenterListModel.bindData(page.getContent()), "执行成功");
    }

    /**
     * 查询联盟商家
     *
     * @param id       商家Id
     * @param pageable 分页
     */
    @RequestMapping(value = "/union", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock union(Long id, Location location, Pageable pageable) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Page<UnionTenant> page =  null;
        Union union = tenant.getUnion();
        if (union==null) {
            page =  new Page<UnionTenant>(Collections.<UnionTenant>emptyList(), 0, pageable);
        } else {
            page = unionTenantService.findPage(union, UnionTenant.Status.confirmed, pageable);
        }
        return DataBlock.success(TenantListModel.bindUnionTenant(page.getContent(), location), "执行成功");
    }

    /**
     * 根据商品条形码查询商品列表
     *
     * @param id      商家Id
     * @param barcode 商品条形码
     */
    @RequestMapping(value = "/barcode", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock barcode(Long id, String barcode) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Product> products = productService.findByBarcode(tenant, barcode);
        return DataBlock.success(ProductListModel.bindData(products), "执行成功");
    }


}
