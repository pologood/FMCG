package net.wit.controller.wap;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.wit.Filter;
import net.wit.controller.wap.model.PromotionModel;
import net.wit.controller.wap.model.CouponModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Product.OrderType;

@Controller("wapTenantController")
@RequestMapping("/wap/tenant")
public class TenantController extends BaseController {

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

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name="employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    /**
     * 商家首页
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
    public String indexNew(@PathVariable Long id,String type, ModelMap model,Pageable pageable, HttpServletRequest request) {
        Tenant tenant = tenantService.find(id);

        if (tenant == null) {
            return "/wap/index";
        }

        if(tenant.getProducts().size()<4||tenant.getThumbnail()==null){
            return "redirect:/wap/tenant/indexC/"+id+".jhtml";
        }

        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);

        String isActivityTenant = "no";
        if(activityPlanning!=null){
            isActivityTenant="yes";
        }

//        if(tenant.getStatus().equals(Tenant.Status.confirm)){
//            return "redirect:";
//        }


        couponService.refreshStatus(tenant);

        String flag = "no";
        if (memberService.isAuthenticated()) {
            Member member = memberService.getCurrent();
            model.addAttribute("memberId", member.getId());
            if (member.getFavoriteTenants().contains(tenant)) {
                flag = "yes";
            }
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filters);
        Page<Employee> employeePage = employeeService.findPage(pageable,tagService.find(34L), null);
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("tenant", tenant);
        model.addAttribute("flag", flag);
        model.addAttribute("thumbnail", tenant.getThumbnail());
        model.addAttribute("name", tenant.getName());
        model.addAttribute("favoriteNum", tenant.getFavoriteMembers().size());
        model.addAttribute("hits", tenantService.viewHits(id));

        Set<Coupon> coupons=new HashSet<>();
        for(Coupon coupon:tenant.getCoupons()){
            if(coupon.getExpired()&&coupon.getType().equals(Coupon.Type.tenantCoupon)){
                coupons.add(coupon);
            }
        }
        Cart cart = cartService.getCurrent();
        model.addAttribute("coupons", CouponModel.bindData(coupons));
        model.addAttribute("cartcount", cart != null ? cart.getQuantity() : 0);
        model.addAttribute("address", tenant.getAddress());
        model.addAttribute("telephone", tenant.getTelephone());
        model.addAttribute("union",tenantRelationService.relationExists(tenant, TenantRelation.Status.success));
        String weixin_code = "";

        if (tenant.getTenantWechat() != null) {
            weixin_code = tenant.getTenantWechat().getWeixin_code();
        }

        model.addAttribute("weixin", weixin_code);
        model.addAttribute("productCategoryTenants", tenant.getProductCategoryTenants());
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        model.addAttribute("deliveryCenters", deliveryCenterService.findPage(tenant.getMember(),null,pageable));
        String desc = tenant.getIntroduction();
        if("".equals(desc)||desc==null){
            desc = tenant.getTenantCategory().getName();
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index/"+tenant.getId()+".jhtml");
        model.addAttribute("title", tenant.getName());
        model.addAttribute("desc", desc);
        model.addAttribute("link", url);
        model.addAttribute("imgUrl", tenant.getThumbnail());
        model.addAttribute("isActivityTenant",isActivityTenant);
        model.addAttribute("member",memberService.getCurrent());
        model.addAttribute("tscpath", "/wap/tenant/index3.0");
        return "/wap/tenant/index3.0";
    }

    /**
     * C类商家首页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/indexC/{id}", method = RequestMethod.GET)
    public String indexnewC(@PathVariable Long id, ModelMap model) {
        model.addAttribute("tenant",tenantService.find(id));
        model.addAttribute("tscpath", "/wap/tenant/indexC");
        return "/wap/tenant/indexC";
    }

    /**
     * 商家首页
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/index_static/{id}", method = RequestMethod.GET)
    public String indexStatic(@PathVariable Long id,String type, ModelMap model,Pageable pageable, HttpServletRequest request) {
        Tenant tenant = tenantService.find(id);

        if (tenant == null) {
            return "/wap/index";
        }

        String flag = "no";
        if (memberService.isAuthenticated()) {
            Member member = memberService.getCurrent();
            model.addAttribute("memberId", member.getId());
            if (member.getFavoriteTenants().contains(tenant)) {
                flag = "yes";
            }
        }
        model.addAttribute("flag", flag);
        model.addAttribute("thumbnail", tenant.getThumbnail());
        model.addAttribute("name", tenant.getName());
        model.addAttribute("favoriteNum", tenant.getFavoriteMembers().size());
        model.addAttribute("coupons", CouponModel.bindData(tenant.getCoupons()));

        model.addAttribute("address", tenant.getAddress());
        model.addAttribute("telephone", tenant.getTelephone());

        String weixin_code = "";

        if (tenant.getTenantWechat() != null) {
            weixin_code = tenant.getTenantWechat().getWeixin_code();
        }

        model.addAttribute("weixin", weixin_code);
        model.addAttribute("productCategoryTenants", tenant.getProductCategoryTenants());
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        model.addAttribute("deliveryCenters", deliveryCenterService.findPage(tenant.getMember(),null,pageable));
        String desc = tenant.getIntroduction();
        if("".equals(desc)||desc==null){
            desc = tenant.getTenantCategory().getName();
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index/"+tenant.getId()+".jhtml");
        model.addAttribute("title", tenant.getName());
        model.addAttribute("desc", desc);
        model.addAttribute("link", url);
        model.addAttribute("imgUrl", tenant.getThumbnail());

        return "/wap/tenant/index_static";
    }

    /**
     * 附近商家列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String nearby(Long channelId,Long tenantCategoryId,ModelMap model) {
        List<TenantCategory> tenantCategoryList = tenantCategoryService.findRoots();
        model.addAttribute("productChannel", productChannelService.find(channelId));
        model.addAttribute("area", areaService.getCurrent());
        model.addAttribute("tenantCategoryList", tenantCategoryList);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        model.addAttribute("orderTypes", net.wit.entity.Tenant.OrderType.values());
        model.addAttribute("type", "nearby");
        model.addAttribute("tenantCategory", tenantCategoryService.find(tenantCategoryId));
        return "/wap/tenant/list";
    }


    /**
     * 附近商家列表
     */
    @RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
    public String contact(@PathVariable Long id,ModelMap model) {
        DeliveryCenter deliveryCenter = deliveryCenterService.find(id);

        if (deliveryCenter == null) {
            return "/wap/index";
        }

        List<Employee> employees = employeeService.findByDeliveryCenterId(id);

        List<Map<String,Object>> maps = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("pos","店主");
        map.put("name",deliveryCenter.getContact());
        map.put("id",deliveryCenter.getTenant().getMember().getId());
        map.put("telnum",deliveryCenter.getPhone()==null?deliveryCenter.getMobile():deliveryCenter.getPhone());
        maps.add(map);
        for(Employee employee:employees){
            Member member = employee.getMember();
            String pos = getPos(employee.getRole());
            Map<String,Object> _map = new HashMap<>();
            _map.put("id",member.getId());
            _map.put("pos",pos);
            _map.put("name",member.getDisplayName()==null?"":member.getDisplayName());
            _map.put("telnum",member.getPhone()==null?member.getMobile():member.getPhone());
            _map.put("salenum",employee.getQuertity());
            _map.put("fansnum",member.getFans());
            maps.add(_map);
        }

        model.addAttribute("areaName", deliveryCenter.getArea().getName());
        model.addAttribute("tenantName", deliveryCenter.getName());
        model.addAttribute("thumbnail", deliveryCenter.getTenant().getThumbnail());
        model.addAttribute("address", deliveryCenter.getAddress());
        model.addAttribute("lat", deliveryCenter.getLocation()==null?"":deliveryCenter.getLocation().getLat());
        model.addAttribute("lng", deliveryCenter.getLocation()==null?"":deliveryCenter.getLocation().getLng());
        JSONArray jsonArray = JSONArray.fromObject(maps);
        model.addAttribute("employees", jsonArray);
        model.addAttribute("member", memberService.getCurrent());
        return "/wap/tenant/contact";
    }
    /**
     * 全城/分类  一级菜单
     *
     * @param type area 区域   category   分类      order  排序
     * @return
     */
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getCategory(Long channelId,String type) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if ("area".equals(type)) {
            Set<Area> areaSet = areaService.find(areaService.getCurrent().getId()).getChildren();
            for (Area area : areaSet) {
            	if(tenantService.findTenants(area).size()>0){
            		Map<String, Object> map = new HashMap<>();
	                map.put("id", area.getId());
	                map.put("name", area.getName());
//	                map.put("num", tenantService.findTenants(area).size());
	                mapList.add(map);
            	}
            }
        } else if ("category".equals(type)) {
        	if (channelId!=null) {
        	    ProductChannel productChannel = productChannelService.find(channelId);
        	    for (TenantCategory tc:productChannel.getTenantCategorys()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tc.getId());
                    map.put("name", tc.getName());
//                   map.put("num", tenantService.findList(tenantCategory, null, null, null, null, null).size());
                    mapList.add(map);
        	    }
        	} else {
                List<TenantCategory> tenantCategoryList = tenantCategoryService.findRoots();
                for (TenantCategory tenantCategory : tenantCategoryList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tenantCategory.getId());
                    map.put("name", tenantCategory.getName());
//                    map.put("num", tenantService.findList(tenantCategory, null, null, null, null, null).size());
                    mapList.add(map);
                }
        	}
        } else if ("order".equals(type)) {
            //mapList.add(net.wit.entity.Tenant.OrderType.values());
        }

        return mapList;
    }

    /**
     * 获得店铺中经营的商品
     *
     * @param id        店铺编号
     * @param type      查询类型   index-店家推荐  推荐标签编码【5】； all-全部  ；  new-新品 推荐标签编码【2】 ； promotion-促销 推荐标签编码【15】
     * @param orderType 排序
     * @return
     */
    @RequestMapping(value = "/get_tenant_product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getTenantProduct(@PathVariable Long id, String type, Pageable pageable, OrderType orderType) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return null;
        }
        Member member = memberService.getCurrent();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        List<Tag> tags = new ArrayList<>();
        if ("index".equals(type)) {
            Tag tag = tagService.find(5l);
            tags.add(tag);
        } else if ("new".equals(type)) {
            Tag tag = tagService.find(2l);
            tags.add(tag);
        } else if ("promotion".equals(type)) {
            Tag tag = tagService.find(15l);
            tags.add(tag);
        }

        Page<Product> productPage = productService.openPage(pageable, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, orderType);

        for (Product product : productPage.getContent()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("thumbnail", product.getThumbnail());
            map.put("fullName", product.getName());
            map.put("price", product.calcEffectivePrice(member));
            map.put("wholePrice", product.getWholePrice());

            List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
            for (Tag tag1 : product.getTags()) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", tag1.getId());
                tagMap.put("name", tag1.getName());
                tagList.add(tagMap);
            }
            map.put("tags", tagList);
            mapList.add(map);
        }
        return mapList;
    }

    @RequestMapping(value = "/get_promotion_product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<PromotionModel> getPromotionProduct(@PathVariable Long id, Pageable pageable) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return null;
        }
        Page<Promotion> promotionPage =promotionService.findPage(tenant,pageable);
        return PromotionModel.bindData(promotionPage.getContent());
    }

    /**
     * 根据店铺分类查找商品
     *
     * @param id
     * @param pageable
     * @param productCategoryId
     * @param tagIds
     * @param orderType
     * @return
     */
    @RequestMapping(value = "/get_category_product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getCategoryProducts(@PathVariable Long id, Pageable pageable, Long productCategoryId, Long tagIds, OrderType orderType) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return null;
        }
        List<Tag> tags = tagService.findList(tagIds);
        ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryId);
        Page<Product> page = productService.findMyPage(tenant, null, null, productCategoryTenant, null, null, tags, null, null, null, true, true, null, null, null, null, orderType, pageable);

        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Product product : page.getContent()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("thumbnail", product.getThumbnail());
            map.put("fullName", product.getName());
            map.put("price", product.calcEffectivePrice(memberService.getCurrent()));
            map.put("wholePrice", product.getWholePrice());

            List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
            for (Tag tag1 : product.getTags()) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", tag1.getId());
                tagMap.put("name", tag1.getName());
                tagList.add(tagMap);
            }
            map.put("tags", tagList);
            mapList.add(map);
        }
        return mapList;
    }


    /**
     * 在店铺中搜索商品
     */
    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public String search(@PathVariable Long id, ModelMap model) {
        model.addAttribute("area", areaService.getCurrent());
        model.addAttribute("id", id);
        return "/wap/tenant/search";
    }

    /**
     * 在线咨询
     */
    @RequestMapping(value = "/consulting/{id}", method = RequestMethod.GET)
    public String consulting(@PathVariable String id, ModelMap model) {
        Member member = memberService.findByTel(id);
        if(member==null){
            return "/wap/index";
        }

        List<Employee> employees = employeeService.findByMember(member);

        String pos = "店主";
        if(employees.size()>0){
            member = employees.get(0).getMember();
            pos = getPos(employees.get(0).getRole());
        }
        model.addAttribute("pos", pos);
        model.addAttribute("member", member);
        model.addAttribute("area", areaService.getCurrent());

        List<Message> message = messageService.findList(memberService.getCurrent(),null,null,null,Message.Type.consultation);

        model.addAttribute("messages", message);
        model.addAttribute("receiver", id);
        return "/wap/tenant/consulting";
    }

    @RequestMapping(value = "/consult", method = RequestMethod.POST)
    public @ResponseBody DataBlock consult(String receiver,String content,HttpServletRequest request){
        Member sender = memberService.getCurrent();
        if(sender==null){
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Member receive = null;
        if (StringUtils.isNotEmpty(receiver)) {
            receive = memberService.findByUsername(receiver);
            if (sender.equals(receive)) {
                return DataBlock.error("无效用户");
            }
        }
        Message message = new Message();
        message.setTitle("咨询回复");
        message.setContent(content);
        message.setType(Message.Type.consultation);
        message.setIp(request.getRemoteAddr());
        message.setIsDraft(false);
        message.setSenderRead(true);
        message.setReceiverRead(false);
        message.setSenderDelete(false);
        message.setReceiverDelete(false);
        message.setSender(sender);
        message.setReceiver(receive);
        message.setWay(Message.Way.tenant);
        messageService.save(message);
        return DataBlock.success("success","success");
    }


    private String getPos(String role){
        String pos = "";
        if(role.indexOf("owner")!=-1){
            pos = "店主";
        }else if(role.indexOf("cashier")!=-1){
            pos = "收银";
        }else if(role.indexOf("guide")!=-1){
            pos = "导购";
        }else if(role.indexOf("manager")!=-1){
            pos = "店长";
        }else if(role.indexOf("account")!=-1){
            pos = "财务";
        }
        return pos;
    }

    /**
     * 在线咨询
     */
    @RequestMapping(value = "/union/{id}", method = RequestMethod.GET)
    public String union(@PathVariable Long id,Pageable pageable, ModelMap model) {
        Tenant tenant = tenantService.find(id);
        if(tenant==null){
            return "/wap/index";
        }
        model.addAttribute("tenant",tenant);
        model.addAttribute("page", tenantRelationService.findMyParent(tenant, TenantRelation.Status.success,pageable));
        model.addAttribute("id",id);
        model.addAttribute("tscpath","/wap/tenant/union");
        return "/wap/tenant/union";
    }


}
