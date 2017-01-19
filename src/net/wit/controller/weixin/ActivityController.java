package net.wit.controller.weixin;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.model.AdModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.TenantListModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

/**
 * 活动
 * Created by WangChao on 2016/12/23.
 */
@Controller("weixinActivityController")
@RequestMapping("/weixin/activity")
public class ActivityController {

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
     * 活动页
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Long linkId, String extension, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("linkId",linkId);
        return "weixin/activity/index";
    }

    @RequestMapping(value = "/union/activity", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock unionActivity(Long linkId, Pageable pageable) {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Page<Ad> adPage = adService.findPage(adPositionService.find(166L), areaService.getCurrent(), linkId, pageable);
        ActivityPlanning activityPlanning = activityPlanningService.find(linkId);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String title = "";
        String desc = "";
        String imgUrl = "";
        String link = "";
        if (activityPlanning != null && "begin".equals(activityPlanning.getStatus())) {
            for (SingleProductPosition singleProductPosition : activityPlanning.getSingleProductPositions()) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", singleProductPosition.getName());
                List<Map<String, Object>> list1 = new ArrayList<>();
                for (SingleProduct singleProduct : singleProductPosition.getSingleProducts()) {
                    if(singleProduct.getProduct()!=null){
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("id", singleProduct.getProduct().getId());
                        map1.put("title", singleProduct.getTitle());
                        map1.put("thumbnail", singleProduct.getProduct().getThumbnail());
                        map1.put("name", singleProduct.getProduct().getName());
                        map1.put("fullName", singleProduct.getProduct().getFullName());
                        map1.put("price", singleProduct.getProduct().calcEffectivePrice(null));
                        map1.put("marketPrice", singleProduct.getProduct().getMarketPrice());
                        map1.put("hits", singleProduct.getProduct().getHits());
                        map1.put("monthSales", singleProduct.getProduct().getMonthSales());
                        list1.add(map1);
                    }
                }
                map.put("singleProducts", list1);
                list.add(map);
            }
            title = activityPlanning.getName();
            desc = activityPlanning.getName();
            imgUrl = adPage.getContent().size() > 0 ? adPage.getContent().get(0).getPath() : "";
            link = bundle.getString("WeiXinSiteUrl") + "/weixin/activity/index.jhtml?linkId=" + linkId + "&extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : "");
        }
        //分享数据
        Map<String, Object> share = new HashMap<>();
        share.put("title", title);
        share.put("desc", desc);
        share.put("imgUrl", imgUrl);
        share.put("link", link);
        data.put("share", share);
        data.put("adPosition", AdModel.bindData(adPage.getContent()));
        data.put("singleProductPositions", list);
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 活动商家
     * @param linkId 广告位链接Id
     */
    @RequestMapping(value = "/tenant/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock tenantList(Long linkId) {
        ActivityPlanning activityPlanning = activityPlanningService.find(linkId);
        return DataBlock.success(TenantListModel.bindData(new ArrayList<>(activityPlanning.getTenants())), "执行成功");

    }
}
