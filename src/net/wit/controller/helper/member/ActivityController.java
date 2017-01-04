package net.wit.controller.helper.member;

import net.sf.json.JSONArray;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.constant.Constant;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Administrator on 2016/7/13.
 */
@Controller("helperMemberActivityController")
@RequestMapping("/helper/member/activity")
public class ActivityController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "activityInventoryServiceImpl")
    private ActivityInventoryService activityInventoryService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    /**
     * 查看
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ActivityRules.Type type, Pageable pageable, ModelMap model) {

        if(type==ActivityRules.Type.growth){
            pageable.setPageSize(60);
        }

        Page<ActivityRules> activityRulesPage =activityRulesService.openPage(type,pageable);

        List<Map<String,Object>> mapList = new ArrayList<>();
        for(ActivityRules activityRules:activityRulesPage.getContent()){
            Map<String,Object> map = new HashMap<>();
            map.put("id",activityRules.getId());
            map.put("title",activityRules.getTitle());
            map.put("description",activityRules.getDescription());
            map.put("type",activityRules.getType());
            map.put("amount",activityRules.getAmount());
            map.put("point",activityRules.getPoint());
            map.put("url",activityRules.getUrl());
            if(activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRules)){
                map.put("isActivity",true);
            }else {
                map.put("isActivity",false);
            }
            mapList.add(map);
        }

        model.addAttribute("page", mapList);
        model.addAttribute("type", type);
        model.addAttribute("point", memberService.getCurrent().getTenant().getPoint());
        model.addAttribute("member",memberService.getCurrent());
        return "/helper/member/activity/list";
    }

    @RequestMapping(value = "/exchange", method = RequestMethod.POST)
    public @ResponseBody Message exchange(Long id, String description, Long tenantPoint, Long point, RedirectAttributes redirectAttributes) {
        Tenant tenant =tenantService.find(id);
        if(tenant==null){
            return Message.warn("无效的企业编号,请确认后重新申请");
        }

        tenant.setPoint(tenantPoint);
        tenantService.update(tenant);
        ActivityInventory activityInventory=new ActivityInventory();
        activityInventory.setPoint(point);
        activityInventory.setTenantPoint(tenantPoint);
        activityInventory.setTenant(tenant);
        activityInventory.setApplyDate(new Date());
        activityInventory.setDescription(description);
        activityInventory.setStatus(ActivityInventory.Status.wait);
        activityInventoryService.save(activityInventory);

        return  Message.success("已提交成功，请耐心等待！");
    }

    /**
     *  测试积分添加功能
     */
    @RequestMapping(value = "/point", method = RequestMethod.GET)
    public @ResponseBody Message point(Long memberid,Long tenantid,Long activityrulesid){

        Member member = null;
        Tenant tenant = null;
        ActivityRules activityRules = null;

        if(memberid != null){
            member = memberService.find(memberid);
        }else if(tenantid != null){
            tenant = tenantService.find(tenantid);
        }

        if(activityrulesid != null){
            activityRules = activityRulesService.find(activityrulesid);
        }

        activityDetailService.addPoint(member,tenant,activityRules);

        return  Message.success("已提交成功，请耐心等待！");
    }
}
