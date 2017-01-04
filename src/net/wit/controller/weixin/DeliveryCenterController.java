/**
 * ====================================================
 * 文件名称: DeliveryCenterController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2015年1月13日			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.controller.weixin;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.DeliveryCenterModel;
import net.wit.controller.weixin.model.GuideListModel;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Employee;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DeliveryCenterController
 * @Description:
 * @author Administrator
 * @date 2015年1月13日 下午1:21:42
 */
@Controller("weixinDeliveryCenterController")
@RequestMapping("/weixin/delivery_center")
public class DeliveryCenterController extends BaseController {

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    /**
     * 获取实体店详情
     * @param id 实体店Id
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id) {
        DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
        DeliveryCenterModel model = new DeliveryCenterModel();
        model.copyFrom(deliveryCenter);
        return DataBlock.success(model, "执行成功");
    }


    /**
     * 获取实体店员工列表
     * id 实体店Id
     */
    @RequestMapping(value = "/employee/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock employeeList(Long id, Pageable pageable) {
        Member member = memberService.getCurrent();
        DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("deliveryCenter", Operator.eq, deliveryCenter));
        pageable.setFilters(filters);
        Page<Employee> page = employeeService.findPage(pageable);
        List<GuideListModel> models = new ArrayList<>();
        for (Employee employee : page.getContent()) {
            if (employee.getMember() != null) {
                GuideListModel model = new GuideListModel();
                model.copyFrom(employee, memberService.findFans(member).size(), member);
                models.add(model);
            }
        }
        return DataBlock.success(models, "执行成功");
    }

}
