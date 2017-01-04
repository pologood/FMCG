/**
 * ====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.TenantListModel;
import net.wit.controller.assistant.model.TenantModel;
import net.wit.controller.assistant.model.TenantViewModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author Administrator
 * @ClassName: TenantController
 * @Description: TODO(店铺管理)
 * @date 2014-9-11 上午11:31:34
 */
@Controller("assistantMemberTenantController")
@RequestMapping("/assistant/member/tenant")
public class TenantController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "tenantCategoryServiceImpl")
    private TenantCategoryService tenantCategoryService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;
    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;
    @Resource(name = "qrcodeServiceImpl")
    private QrcodeService qrcodeService;
    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;
    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;
    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("无效店铺id");

        }
        TenantViewModel model = new TenantViewModel();
        model.copyFrom(tenant);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        Long employees = new Long(employeeService.findList(null, filters, null).size());
        model.setEmployees(employees);
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
    public DataBlock list(Long tenantCategoryId, Long areaId, Long channelId, Location location, BigDecimal distatce, Long[] tagIds, Long communityId, Boolean isPromotion, Boolean isUnion, String keyword, Tenant.OrderType orderType, Long tenantId, Pageable pageable, HttpServletRequest request) {
        Union union = new Union();
        Tenant te = tenantService.find(tenantId);
        if (te != null) {
            union = te.getUnion();
        } else {
            union = null;
        }
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
                orderType = Tenant.OrderType.distance;
            }
            List<Tag> tags = tagService.findList(tagIds);
            Community community = communityService.find(communityId);
            page = tenantService.openPage(pageable, area, tenantCategorys, tags, keyword, location, distatce, community, orderType, isPromotion, isUnion, union);

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
            System.out.println("接口异常");
            e.printStackTrace();
        }
        return DataBlock.success(TenantListModel.bindData(page.getContent(), location), "执行成功");
    }

    /**
     * 开通店铺
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock add(String shortName, String thumbnail, Long tenantCategoryId, Location location) {
        //System.out.println(thumbnail);
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            tenant = EntitySupport.createInitTenant();
            if (member.getArea() != null) {
                tenant.setArea(member.getArea());
            }
            tenant.setScore(0F);
            tenant.setTotalScore(0L);
            tenant.setScoreCount(0L);
            tenant.setHits(0L);
            tenant.setWeekHits(0L);
            tenant.setMonthHits(0L);
            tenant.setAddress(member.getAddress());
            tenant.setLinkman(member.getName());
            tenant.setTelephone(member.getMobile());

        }
        tenant.setPoint(0L);
        tenant.setName(shortName);
        tenant.setShortName(shortName);
        tenant.setThumbnail(thumbnail);

        if (tenant.getCode() == null) {
            tenant.setCode("1");
        }

        if (tenant.getArea() == null) {
            tenant.setArea(areaService.getCurrent());
        }
        if (tenantCategoryId != null) {
            tenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
        }
        tenantService.save(tenant, member, location);

        if (member.getMember() != null && member.getMember().getTenant() != null) {
            if (!activityDetailService.isActivity(null, member.getMember().getTenant(), activityRulesService.find(45L))) {
                //TODO 首次推荐好友开店领取积分
                activityDetailService.addPoint(null, member.getMember().getTenant(), activityRulesService.find(45L));
            }

            if (!activityDetailService.isActivity(null, member.getMember().getTenant(), activityRulesService.find(51L))) {
                //TODO 推荐好友开店领取积分，每日一次
                activityDetailService.addPoint(null, member.getMember().getTenant(), activityRulesService.find(51L));
            }
        }

        TenantModel model = new TenantModel();
        model.copyFrom(tenant);
        Employee employee = employeeService.findMember(member,tenant);
        if (employee==null) {
            model.setRole(",owner");
        } else {
            model.setRole(employee.getRole());
        }

        return DataBlock.success(model, "执行成功");

    }

    /**
     * 绑定微信
     */
    @RequestMapping(value = "/bind_weixin", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock bind_weixin(String appId, String weixin, String appSecret) {

        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error("无效店铺id");

        }
        if (appId == null) {
            return DataBlock.error("微信公众号ID不能为空");
        }
        if (weixin == null) {
            return DataBlock.error("微信公众号不能为空");
        }
        if (appSecret == null) {
            return DataBlock.error("微信公众号密钥不能为空");
        }
        TenantWechat wechat = tenant.getTenantWechat();
        wechat.setWeixin_code(weixin);
        wechat.setAppId(appId);
        wechat.setAppSecret(appSecret);
        tenant.setTenantWechat(wechat);

        tenantService.update(tenant);
        return DataBlock.success("success", "绑定成功");

    }

    /**
     * 修改店铺信息
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock update(String shortName, String name, String thumbnail, String logo, String introduction, String weixin, Long tenantCategoryId, Boolean toPay, Boolean tamPo, Boolean noReason) {

        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Tenant tenant = member.getTenant();
        if (shortName != null) {
            tenant.setName(shortName);
            tenant.setShortName(shortName);

            if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(1L))) {
                activityDetailService.addPoint(null, tenant, activityRulesService.find(1L));
            }
        }
        if (name != null) {
            tenant.setName(name);
            tenant.setShortName(name);

            if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(1L))) {
                activityDetailService.addPoint(null, tenant, activityRulesService.find(1L));
            }
        }
        if (thumbnail != null) {
            tenant.setThumbnail(thumbnail);

            if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(4L))) {
                activityDetailService.addPoint(null, tenant, activityRulesService.find(4L));
            }
        }
        if (logo != null) {
            tenant.setLogo(logo);

            if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(3L))) {
                activityDetailService.addPoint(null, tenant, activityRulesService.find(3L));
            }
        }
        if (toPay != null) {
            tenant.setToPay(toPay);
        }
        if (tamPo != null) {
            tenant.setTamPo(tamPo);
        }
        if (noReason != null) {
            tenant.setNoReason(noReason);
        }
        if (introduction != null) {
            tenant.setIntroduction(introduction);
        }
        if (weixin != null) {
            TenantWechat wechat = tenant.getTenantWechat();
            if (wechat == null) {
                wechat = new TenantWechat();
            }
            wechat.setWeixin_code(weixin);
            tenant.setTenantWechat(wechat);
        }
        if (tenantCategoryId != null) {
            tenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
        }

        tenantService.update(tenant);
        return DataBlock.success("success", "执行成功");

    }

    /**
     * 分享推荐您也来开店地址
     */
    @RequestMapping(value = "/invite", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock invite(Long id, String type) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("店铺ID不存在");
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        if (tenant.getThumbnail() == null) {
            return DataBlock.error("请上传店招再分享");
        }
        if (!tenant.getStatus().equals(Tenant.Status.success)) {
            return DataBlock.error("没有开通不能分享");
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        String wxMenu = bundle.getString("wxMenu");

        String _url = bundle.getString("WeiXinSiteUrl") + "/wap/invite/index.jhtml?extension=" + (member != null ? member.getUsername() : "");
        if (wxMenu.equals("101")) {
            _url = bundle.getString("WeiXinSiteUrl") + "/www/invite/index_ztb.html?extension=" + (member != null ? member.getUsername() : "");
        }

        String url = "";
        if (!"app".equals(type)) {
            url = MenuManager.codeUrlO2(URLEncoder.encode(_url));
        } else {
            url = _url;
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("url", url);
        data.put("title", "亲,“" + tenant.getShortName() + "”推荐您也来开店。");
        data.put("thumbnail", tenant.getThumbnail() + "@200w_200h_1e_1c_100Q");
        data.put("description", tenant.getIntroduction());
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 分享店铺地址
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock share(Long id, String type) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("店铺ID不存在");
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = "";
        DeliveryCenter delivery = tenant.getDefaultDeliveryCenter();
        if (delivery == null) {
            return DataBlock.error("没有实体店不能做活动推广");
        }
        if ("preview".equals(type)) {
            url = bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index_static/" + id.toString() + ".jhtml?extension=" + (member != null ? member.getUsername() : "");
        } else if (!"app".equals(type)) {
            if (!tenant.getStatus().equals(Tenant.Status.success)) {
                return DataBlock.error("没有开通不能分享");
            }
            url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/index.jhtml?id="+id+"&extension=" + (member != null ? member.getUsername() : "")));
        } else {
            url = bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/index.jhtml?id="+id+"&extension=" + (member != null ? member.getUsername() : "");
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("url", url);
        data.put("title", "欢迎您光临“" + tenant.getShortName() + "”，您身边的优质实体店，购实惠，购惊喜，购保证");
        data.put("thumbnail", tenant.getThumbnail() + "@200w_200h_1e_1c_100Q");
        data.put("description", tenant.getIntroduction());
        return DataBlock.success(data, "执行成功");
    }

    @RequestMapping(value = "/qrcode/json", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock qrcodeJson(HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            if (!member.getTenant().getStatus().equals(Tenant.Status.success)) {
                return DataBlock.error("没有开通不能分享");
            }
            // 第三方用户唯一凭证
            Qrcode qrcode = qrcodeService.findbyTenant(member.getTenant());
            String url = "";
            if (qrcode == null || qrcode.getUrl() == null) {
                url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/becomevip.jhtml?extension=" + (member != null ? member.getUsername() : ""));
            } else {
                url = qrcode.getUrl();
            }

            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(41L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(41L));
            }

            if (!activityDetailService.isActivity(null, member.getTenant(), activityRulesService.find(42L))) {
                activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(42L));
            }

            return DataBlock.success(url, "获取成功");
        } catch (Exception e) {
            return DataBlock.error("获取二维码失败");
        }
    }

}
