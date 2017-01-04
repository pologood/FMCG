package net.wit.controller.pad;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.EquipmentService;
import net.wit.service.MemberService;
import net.wit.service.OrderService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * 明星导购
 * Created by ruanx on 2016/11/16.
 */
@Controller("padEmployeeController")
@RequestMapping("/pad/employee")
public class EmployeeController extends BaseController {
    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;
    //明星导购
    @RequestMapping(value = "/guiderStar", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock guiderStar(String uuid) {
        //TODO
        Equipment equipment = equipmentService.findByUUID(uuid);
        Tenant tenant = equipment.getTenant();
        Member member = memberService.find(tenant.getId());
        //明星导购id
        Long guiderStarId = memberService.findGuiderStar(tenant);
        Member guiderStar = memberService.find(guiderStarId);
        //发展会员
        int promotingMembers = memberService.findList(member).size();
        //发展订单
        List<Order> orders = orderService.myOrder(member);
        Map data =new HashMap();
        data.put("id",guiderStar.getId());
        data.put("name",guiderStar.getName());
        data.put("image",guiderStar.getHeadImg());
        data.put("score",guiderStar.getTotalScore());
        data.put("popularity",promotingMembers);
        data.put("monthSales",orders.size());
        data.put("mobile",guiderStar.getMobile());
        data.put("description","精品推荐、大额红包不停歇");
        return  DataBlock.success(data, "执行成功");
    }
}
