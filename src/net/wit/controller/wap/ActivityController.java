package net.wit.controller.wap;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TenantListModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.support.TenantComparatorByDistance;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static java.util.Collections.*;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
@Controller("ActivityController")
@RequestMapping("/wap/activity")
public class ActivityController extends BaseController {

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "adServiceImpl")
    private AdService adService;

    @Resource(name = "singleProductPositionServiceImpl")
    private SingleProductPositionService singleProductPositionService;

    /**
     * 获取省
     */
    @RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
    public String index(@PathVariable Long id, Location location, ModelMap model, Pageable pageable) {
        ActivityPlanning activityPlanning = activityPlanningService.find(id);
        model.addAttribute("activityPlanning", activityPlanning);
        model.addAttribute("area", areaService.getCurrent());

//        for(Tenant tenant:page.getContent()){
//            ActivityPlanning activityPlannings = activityPlanningService.getCurrent(tenant);
//            if(activityPlannings!=null){
//                Promotion promotion=new Promotion();
//                promotion.setName(activityPlannings.getName());
//                promotion.setType(Promotion.Type.activity);
//                tenant.getPromotions().add(promotion);
//            }
//        }

        AdPosition adPostion = adPositionService.find(141L);

        List<AdPosition> adPositions = new ArrayList<>();
        for(AdPosition adPosition:activityPlanning.getAdPositions()){
            adPositions.add(adPosition);
        }
        model.addAttribute("adPositions", adPositions);
        Page<Ad> adPage = adService.findPage(adPostion, areaService.getCurrent(),null, pageable);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        model.addAttribute("title", activityPlanning.getName());
        model.addAttribute("desc", bundle.getString("signature") + "来啦，你挑我买，最高减免100元！！！");
        model.addAttribute("imgUrl", adPage.getContent().size() > 0 ? adPage.getContent().get(0).getPath() : "");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/activity/index/" + id + ".jhtml?extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : ""));
        model.addAttribute("link", url);
        return "wap/activity/828";
    }

    @RequestMapping(value = "/union/activity/{id}", method = RequestMethod.GET)
    public String unionActivity(@PathVariable Long id,  ModelMap model, Pageable pageable) {
        ActivityPlanning activityPlanning=activityPlanningService.find(id);
        if(activityPlanning==null){
            return "redirect:/wap/index.jhtml";
        }

        if(!"begin".equals(activityPlanning.getStatus())){
            return "redirect:/wap/index.jhtml";
        }
        if(activityPlanning.getSingleProductPositions().size()>5){
            model.addAttribute("num",5);
        }
        Page<Ad> adPage = adService.findPage(adPositionService.find(166L), areaService.getCurrent(),id, pageable);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        model.addAttribute("area",areaService.getCurrent());
        model.addAttribute("activityPlanning",activityPlanning);
        model.addAttribute("title", activityPlanning.getName());
        model.addAttribute("desc", activityPlanning.getName());
        model.addAttribute("imgUrl", adPage.getContent().size() > 0 ? adPage.getContent().get(0).getPath() : "");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/activity/union/activity/" + id + ".jhtml?extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : ""));
        model.addAttribute("link", url);
        return "wap/activity/union_activity";
    }

    /**
     * 根据经纬度获取实体店地址
     */
    @RequestMapping(value = "/get/location", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock getLocation(Long id, Location location, Pageable pageable) {
        ActivityPlanning activityPlanning = activityPlanningService.find(id);
        List<Tenant> tenants = new ArrayList<>();
        tenants.addAll(activityPlanning.getTenants());
        pageable.setPageSize(activityPlanning.getTenants().size());
        TenantComparatorByDistance comparatorByDistance = new TenantComparatorByDistance();
        comparatorByDistance.setLocation(location);
        sort(tenants, comparatorByDistance);
        int fromindex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
        int endindex = fromindex + pageable.getPageSize();
        if (endindex > tenants.size()) {
            endindex = tenants.size();
        }
        Page<Tenant> page = null;
        if (endindex <= fromindex) {
            page = new Page<Tenant>(new ArrayList<Tenant>(), 0, pageable);
        }
        page = new Page<Tenant>(new ArrayList<Tenant>(tenants.subList(fromindex, endindex)), tenants.size(), pageable);
        return DataBlock.success(TenantListModel.bindData(page.getContent(), location), "成功");

    }


    /**
     * 商圈
     */
    @RequestMapping(value = "/community/{id}", method = RequestMethod.GET)
    public String community(@PathVariable int id, ModelMap model, Pageable pageable) {
        List<Map<String, Object>> teants = new ArrayList<>();
        String community = "";
        switch (id) {
            case 1:
                community = "瑶海万达广场";
                String[] tenantName1 = {"moment.cafe烘培", "王妃家的年糕火锅", "金泉家韩国料理", "板烧厨房", "肖记面馆", "菲小姐的店"};
                String[] activityName1 = {"进店送蛋挞", "进店送芝士年糕", "进店满100送可乐", "进店送饮料", "进店送精美礼品", "立即进店"};
                Long[] ids1 = {5164L, 5166L, 5155L, 5154L, 5185L, 226L};
                teants = getTenants(tenantName1, activityName1, ids1);
                break;
            case 2:
                community = "包河万达广场";
                String[] tenantName2 = {"爱情果麻辣屋", "韩国雪冰", "名家柠檬鱼", "康二姐", "台湾苏氏牛排", "菲小姐的店"};
                String[] activityName2 = {"进店送素菜2串", "立即进店", "进店送烤翅", "立即进店", "进店送饮品", "立即进店"};
                Long[] ids2 = {5191L, 5172L, 5170L, 5180L, 5186L, 226L};
                teants = getTenants(tenantName2, activityName2, ids2);
                break;
            case 3:
                community = "合肥官亭路";
                String[] tenantName3 = {"美衣坊", "都市丽人", "琪翊服饰", "露露美甲屋", "老香港蛋糕", "青树苗花店", "润安养生堂", "菲小姐的店"};
                String[] activityName3 = {"立即进店", "立即进店", "立即进店", "立即进店", "立即进店", "立即进店", "进店品尝花草茶", "立即进店"};
                Long[] ids3 = {5179L, 5146L, 5163L, 5184L, 5158L, 641L, 5167L, 226L};
                teants = getTenants(tenantName3, activityName3, ids3);
                break;
            case 4:
                String[] tenantName4 = {""};
                String[] activityName4 = {""};
                Long[] ids4 = {0L};
                teants = getTenants(tenantName4, activityName4, ids4);
                break;
            default:
                break;
        }
        model.addAttribute("community", community);
        model.addAttribute("tenants", teants);
        return "wap/activity/community";
    }

    /**
     * 临时固定数据使用
     *
     * @param tenantName
     * @return
     */
    public static List<Map<String, Object>> getTenants(String[] tenantName, String[] activityName, Long[] ids) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (int i = 0; i < tenantName.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("activityName", activityName[i]);
            map.put("name", tenantName[i]);
            map.put("id", ids[i]);
            mapList.add(map);
        }

        return mapList;
    }
}
