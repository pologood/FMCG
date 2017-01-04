package net.wit.controller.app.member;

import net.wit.Filter;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TagModel;
import net.wit.entity.*;
import net.wit.service.EmployeeService;
import net.wit.service.EquipmentService;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * 购物屏
 * Created by WangChao on 2016-4-25.
 */
@Controller("appMemberEquipmentController")
@RequestMapping("/app/member/equipment")
public class EquipmentController {

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock List(String type) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        if (type.equals("tenant")) {
            filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        } else if (type.equals("store")) {
            filters.add(new Filter("store", Filter.Operator.eq, tenant));
        }
        List<Equipment> equipments = equipmentService.findList(null, filters, null);

        Set<Area> cityList = new HashSet<>();
        for (Equipment equipment : equipments) {
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
            cityList.add(city);
        }

        List<Map<String, Object>> maps = new ArrayList<>();
        for (Area city : cityList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", city.getId());
            map.put("name", city.getName());

            List<Map<String, Object>> tenantMaps = new ArrayList<>();
            for (Equipment equipment : equipments) {
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
                Area city1 = new Area();
                if (area.getIsCity()) {
                    city1 = area;
                } else {
                    if (area.getParent() == null) {
                        city1 = area;
                    } else if (area.getParent().getParent() == null) {
                        city1 = area;
                    } else if (area.getParent().getParent().getParent() == null) {
                        city1 = area.getParent();
                    }
                }
                if (city == city1) {
                    Map<String, Object> tenantMap = new HashMap<>();

                    TagModel tagModel = new TagModel();
                    tenantMap.put("equipmentId", equipment.getId());
                    tenantMap.put("status", equipment.getStatus());
                    if (new Date().getTime() - equipment.getModifyDate().getTime() <= 30 * 60 * 1000) {
                        tenantMap.put("setStatus", "success");
                    } else {
                        tenantMap.put("setStatus", "none");
                    }

                    if(type.equals("tenant")) {
                        tenantMap.put("id", equipment.getStore().getId());
                        tenantMap.put("name", equipment.getStore().getName());
                        tenantMap.put("address", area.getFullName() + equipment.getStore().getAddress());
                        tenantMap.put("tags", tagModel.bindData(equipment.getStore().getTags()));
                    }else{
                        if(equipment.getDeliveryCenter()!=null) {
                            tenantMap.put("id", equipment.getDeliveryCenter().getTenant().getId());
                            tenantMap.put("name", equipment.getDeliveryCenter().getName());
                            tenantMap.put("address", equipment.getDeliveryCenter().getAreaName() + equipment.getDeliveryCenter().getAddress());
                            tenantMap.put("tags", tagModel.bindData(equipment.getDeliveryCenter().getTenant().getTags()));
                        }else{
                            tenantMap.put("id", equipment.getTenant().getId());
                            tenantMap.put("name", equipment.getTenant().getName());
                            tenantMap.put("address", area.getFullName() + equipment.getTenant().getAddress());
                            tenantMap.put("tags", tagModel.bindData(equipment.getTenant().getTags()));
                        }
                    }
                    tenantMaps.add(tenantMap);
                }
            }
            map.put("tenants", tenantMaps);
            maps.add(map);
        }
        return DataBlock.success(maps, "执行成功");
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock Detail(String type, Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
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
            return DataBlock.error(DataBlock.TENANT_INVAILD);
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
        map.put("employee", employeeMaps);
        map.put("total_amount", equipment.getTotalAmount());
        map.put("total_sale_amount", equipment.getTotalSaleAmount());
        if (new Date().getTime() - equipment.getModifyDate().getTime() <= 30 * 60 * 1000) {
            map.put("setStatus", "success");
        } else {
            map.put("setStatus", "none");
        }
        map.put("status", equipment.getStatus());
        return DataBlock.success(map, "执行成功");
    }
}
