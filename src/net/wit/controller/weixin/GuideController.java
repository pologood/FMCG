package net.wit.controller.weixin;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.GuideListModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导购
 * Created by WangChao on 2017/1/10.
 */
@Controller("weixinGuideController")
@RequestMapping("/weixin/guide")
public class GuideController {
    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;
    @Resource(name = "tagServiceImpl")
    private TagService tagService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "tenantCategoryServiceImpl")
    private TenantCategoryService tenantCategoryService;
    @Resource(name = "visitRecordServiceImpl")
    private VisitRecordService visitRecordService;
    @Resource(name = "memberAttributeServiceImpl")
    private MemberAttributeService memberAttributeService;


    /**
     * 导购列表
     *
     * @param pageable         分页
     * @param location         位置
     * @param tenantCategoryId 商家分类Id
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Pageable pageable, Location location, Long tenantCategoryId) {
        Member member = memberService.getCurrent();
        TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
        Page<Employee> page = employeeService.findPage(pageable, tenantCategory, null, location, null, "distance");
        List<GuideListModel> models = new ArrayList<>();
        for (Employee employee : page.getContent()) {
            if (employee.getMember() != null) {
                GuideListModel model = new GuideListModel();
                Filter[] filters = new Filter[]{new Filter("member", Filter.Operator.eq, member), new Filter("tenant", Filter.Operator.eq, employee.getTenant())};
                model.copyFrom(employee, memberService.findFans(employee.getMember()).size(), visitRecordService.count(filters), member, location);
                models.add(model);
            }
        }
        return DataBlock.success(models, page, "执行成功");
    }

    /**
     * 导购详情
     *
     * @param employeeId 导购id
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Long employeeId, Location location) {
        Member member = memberService.getCurrent();
        Employee employee = employeeService.find(employeeId);
        Member guideMember = employee.getMember();
        if (guideMember == null) {
            return DataBlock.error("导购不存在");
        }
        GuideListModel guideListModel = new GuideListModel();
        Filter[] filters = new Filter[]{new Filter("member", Filter.Operator.eq, member), new Filter("tenant", Filter.Operator.eq, employee.getTenant())};
        guideListModel.copyFrom(employee, memberService.findFans(guideMember).size(), visitRecordService.count(filters), member, location, guideMember, memberAttributeService.find(MemberAttribute.GUIDE_SIGNATUREID), memberAttributeService.find(MemberAttribute.GUIDE_PERSONALITYTAGID));
        return DataBlock.success(guideListModel, "执行成功");
    }

    /**
     * 导购粉丝
     *
     * @param employeeId 导购id
     */
    @RequestMapping(value = "/fans/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock fansList(Long employeeId, Pageable pageable) {
        Employee employee = employeeService.find(employeeId);
        Member guideMember = employee.getMember();
        if (guideMember == null) {
            return DataBlock.error("导购不存在");
        }
        Page<Member> page = memberService.findFanPage(guideMember, pageable);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Member member : page.getContent()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", member.getId());
            map.put("headImg", member.getHeadImg());
            map.put("name", member.getDisplayName());
            list.add(map);
        }
        return DataBlock.success(list, page, "执行成功");
    }


}
