package net.wit.controller.store.member;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.TagModel;
import net.wit.entity.*;
import net.wit.service.EmployeeService;
import net.wit.service.EquipmentService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.*;

/**
 * 购物屏
 * Created by WangChao on 2016-4-25.
 */
@Controller("storeMemberEquipmentController")
@RequestMapping("/store/member/equipment")
public class EquipmentController extends BaseController {

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String List(String type, String keyWord, Pageable pageable, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        if (type == null) type = "tenant";
        List<Filter> filters = new ArrayList<>();
        if (type.equals("tenant")) {
            filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        } else if (type.equals("store")) {
            filters.add(new Filter("store", Filter.Operator.eq, tenant));
        }
        pageable.setFilters(filters);
        Page<Equipment> page = equipmentService.findPage(keyWord,null,pageable);
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Equipment equipment : page.getContent()) {
            Area area;
            if (type.equals("tenant")) {
                area = equipment.getStore().getArea();
            } else {
                if(equipment.getDeliveryCenter()!=null){
                    area = equipment.getDeliveryCenter().getArea();
                }else{
                    area = equipment.getTenant().getArea();
                }
            }
            Area city = new Area();
            if (area.getIsCity()) {
                city = area;
            } else {
                if (area.getParent() == null) {
                    city = area;
                } else if (area.getParent().getParent() == null) {
                    city = area;
                } else if (area.getParent().getParent().getParent() == null) {
                    city = area.getParent();
                }
            }
            Map<String, Object> map = new HashMap<>();
            TagModel tagModel = new TagModel();
            map.put("id",equipment.getId());
            map.put("areaName", city.getName());

            System.out.println("----------------"+new Date().getTime()+"=================="+(new Date().getTime() - equipment.getModifyDate().getTime() <= 30 * 60 * 1000));

            if (new Date().getTime() - equipment.getModifyDate().getTime() <= 30 * 60 * 1000) {
                map.put("setStatus", "success");
            } else {
                map.put("setStatus", "none");
            }
            if(type.equals("tenant")) {
                map.put("tenantName", equipment.getStore().getName());
                map.put("tags", tagModel.bindData(equipment.getStore().getTags()));
                map.put("address", area.getFullName() + equipment.getStore().getAddress());
            }else{
                if(equipment.getDeliveryCenter()!=null) {
                    map.put("tenantName", equipment.getDeliveryCenter().getName());
                    map.put("tags", tagModel.bindData(equipment.getDeliveryCenter().getTenant().getTags()));
                    map.put("address", equipment.getDeliveryCenter().getAreaName() + equipment.getDeliveryCenter().getAddress());
                }else{
                    map.put("tenantName", equipment.getTenant().getName());
                    map.put("tags", tagModel.bindData(equipment.getTenant().getTags()));
                    map.put("address", area.getFullName() + equipment.getTenant().getAddress());
                }
            }
            maps.add(map);
        }
        model.addAttribute("page", new Page<>(maps, page.getTotal(), pageable));
        model.addAttribute("type", type);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("menu","tenant_equiment");
        return "store/member/equipment/list";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String Detail(String type, Long id, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Equipment equipment = equipmentService.find(id);

        Tenant tenant = null;
        if (type.equals("tenant")) {
            tenant = equipment.getStore();
        } else if (type.equals("store")) {
            if(equipment.getDeliveryCenter()!=null) {
                tenant = equipment.getDeliveryCenter().getTenant();
            }else{
                tenant = equipment.getTenant();
            }
        }
        if (tenant == null) {
            return ERROR_VIEW;
        }

        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("nickName", tenant.getMember().getDisplayName());
        memberMap.put("role", ",owner");

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        List<Employee> employees = employeeService.findList(null, filters, null);

        List<Map<String, Object>> employeeMaps = new ArrayList<>();
        employeeMaps.add(memberMap);
        for (Employee employee : employees) {
            Map<String, Object> employeeMap = new HashMap<>();
            employeeMap.put("nickName", employee.getMember().getDisplayName());
            employeeMap.put("role", employee.getRole());
            employeeMaps.add(employeeMap);
        }

        Map<String, Object> map = new HashMap<>();
        TagModel tagModel = new TagModel();
        map.put("tags", tagModel.bindData(tenant.getTags()));
        map.put("name", tenant.getName());
        map.put("address", tenant.getArea().getFullName() + tenant.getAddress());
        if(!type.equals("tenant")&&equipment.getDeliveryCenter()!=null) {
            map.put("name", equipment.getDeliveryCenter().getName());
            map.put("address", equipment.getDeliveryCenter().getAreaName() + equipment.getDeliveryCenter().getAddress());
        }
        map.put("employees", employeeMaps);
        map.put("total_amount", equipment.getTotalAmount());
        map.put("total_sale_amount", equipment.getTotalSaleAmount());

        System.out.println("----------------"+new Date().getTime());
        if (new Date().getTime() - equipment.getModifyDate().getTime() <= 30 * 60 * 1000) {
            map.put("setStatus", "success");
        } else {
            map.put("setStatus", "none");
        }
        map.put("status", equipment.getStatus());
        model.addAttribute("equipment",map);
        model.addAttribute("type",type);
        return "store/member/equipment/detail";
    }
}
