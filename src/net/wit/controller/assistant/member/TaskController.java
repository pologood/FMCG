package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.EmployeeModel;
import net.wit.controller.assistant.model.TaskModel;
import net.wit.entity.Employee;
import net.wit.entity.Member;
import net.wit.entity.Task;
import net.wit.entity.Tenant;
import net.wit.service.EmployeeService;
import net.wit.service.MemberService;
import net.wit.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("assistantMemberTaskController")
@RequestMapping("/assistant/member/task")
/**
 * 任务管理
 * Created by ruanx on 2016/11/3.
 */
public class TaskController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "taskServiceImpl")
    private TaskService taskService;

    /*
     *进入任务管理器
     * month:月份
     * type：1：任务管理
     * */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Long month,String type) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        //店主
        Member memberBoss = tenant.getMember();
        Boolean isManager = false;
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        if("1".equals(type)){
            //店主、店长rw，收银、财务r，导购无权限
            if(member.getId().equals(memberBoss.getId())){
                member = null;
                isManager = true;
            }else{
                Employee employee = employeeService.findMember(member,tenant);
                String roles = employee.getRole();
                String[] ids = roles.split(",");
                for (String id:ids) {
                    if("manager".equals(id)){
                        member = null;
                        isManager = true;
                    }else if("cashier".equals(id)||"account".equals(id)){
                        member = null;
                    }else{

                    }
                }
            }
        }
        Map<String, Object> taskMap = taskService.findByTenant(member,tenant,month);
        if (taskMap == null) {
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("id",tenant.getId());
            data.put("sale",0L);
            data.put("doSale",0L);
            data.put("proSale","0");
            data.put("share",0L);
            data.put("doShare",0L);
            data.put("proShare","0");
            data.put("invite",0L);
            data.put("doInvite",0L);
            data.put("proInvite","0");
            data.put("coupon",0L);
            data.put("doCoupon",0L);
            data.put("proCoupon","0");
            data.put("isManager",isManager);
            return DataBlock.success(data, "执行成功");
        }
        DecimalFormat df = new DecimalFormat("#");
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("id",tenant.getId());
        data.put("sale",taskMap.get("sale"));
        data.put("doSale",taskMap.get("doSale"));
        if("0".equals(taskMap.get("sale").toString())){
            data.put("proSale","0");
        }else {
            data.put("proSale", df.format(new BigDecimal(Double.parseDouble((taskMap.get("doSale").toString()))).divide(new BigDecimal(Double.parseDouble((taskMap.get("sale").toString()))), 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100));
        }
        data.put("share",taskMap.get("share"));
        data.put("doShare",taskMap.get("doShare"));
        if("0".equals(taskMap.get("share").toString())){
            data.put("proShare","0");
        }else {
            data.put("proShare", df.format(Double.parseDouble(taskMap.get("doShare").toString()) / Double.parseDouble(taskMap.get("share").toString()) * 100));
        }
        data.put("invite",taskMap.get("invite"));
        data.put("doInvite",taskMap.get("doInvite"));
        if("0".equals(taskMap.get("invite").toString())){
            data.put("proInvite","0");
        }else {
            data.put("proInvite", df.format(Double.parseDouble(taskMap.get("doInvite").toString()) / Double.parseDouble(taskMap.get("invite").toString()) * 100));
        }
        data.put("coupon",taskMap.get("coupon"));
        data.put("doCoupon",taskMap.get("doCoupon"));
        if("0".equals(taskMap.get("coupon").toString())){
            data.put("proCoupon","0");
        }else {
            data.put("proCoupon", df.format(Double.parseDouble(taskMap.get("doCoupon").toString()) / Double.parseDouble(taskMap.get("coupon").toString()) * 100));
        }
        data.put("isManager",isManager);
        return DataBlock.success(data, "执行成功");
    }

    //查看任务
    @RequestMapping(value = "/queryTask", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock queryTask(String type, Long month, Pageable pageable,String order) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        Page<Task> page = taskService.findPage(tenant,month,type,pageable,order);
        return DataBlock.success(TaskModel.bindData(page.getContent()), "执行成功");
    }

    //添加任务
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock addTask(BigDecimal sale, Long share, Long invite, Long coupon, String employeIds, Long month) {
        try{
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        Member memberBoss = tenant.getMember();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        if (employeIds != null) {
            String[] str1 = employeIds.split(",");
            Long[] employeId = new Long[str1.length];
            for (int i = 0; i < str1.length; i++) {
                employeId[i] = Long.valueOf(str1[i]);
            }
            //如果店主
            for (int m = 0; m < str1.length; m++) {
            if(memberBoss.getId().equals(Long.parseLong(str1[m]))){
                Task task = new Task();
                task.setType(Task.Type.month);
                task.setTenant(tenant);
                task.setSale(sale);
                task.setShare(share);
                task.setInvite(invite);
                task.setCoupon(coupon);
                task.setMonth(month);
                task.setDoSale(new BigDecimal("0.00"));
                task.setDoShare(0L);
                task.setDoInvite(0L);
                task.setDoCoupon(0L);
                task.setMember(memberBoss);
                Task tasked = taskService.findByMember(memberBoss,month);
                if (tasked != null) { //本月员工已添加过，则修改
                    task.setId(tasked.getId());
                    task.setDoSale(tasked.getDoSale());
                    task.setDoShare(tasked.getDoShare());
                    task.setDoInvite(tasked.getDoInvite());
                    task.setDoCoupon(tasked.getDoCoupon());
                    taskService.update(task);
                } else {
                    taskService.save(task);
                }
            }
            }
                List<Employee> emps = employeeService.findList(employeId);
                for (int i = 0; i < emps.size(); i++) {
                    Task task = new Task();
                    Employee empss = emps.get(i);
                    task.setType(Task.Type.month);
                    task.setTenant(tenant);
                    task.setSale(sale);
                    task.setShare(share);
                    task.setInvite(invite);
                    task.setCoupon(coupon);
                    task.setMonth(month);
                    task.setDoSale(new BigDecimal("0.00"));
                    task.setDoShare(0L);
                    task.setDoInvite(0L);
                    task.setDoCoupon(0L);
                    task.setMember(empss.getMember());
                    Task tasked = taskService.findByMember(empss.getMember(), month);
                    if (tasked != null) { //本月员工已添加过，则修改
                        task.setId(tasked.getId());
                        task.setDoSale(tasked.getDoSale());
                        task.setDoShare(tasked.getDoShare());
                        task.setDoInvite(tasked.getDoInvite());
                        task.setDoCoupon(tasked.getDoCoupon());
                        taskService.update(task);
                    } else {
                        taskService.save(task);
                    }
                }
            return DataBlock.success("success", "添加成功");
        }else{
            return DataBlock.error("请添加员工！");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataBlock.error("提交失败！");
    }

    //店铺员工列表
    @RequestMapping(value = "/employeelist", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock employeeList(Pageable pageable){
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        Member memberBoss = tenant.getMember();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filters);
        Page<Employee> page = employeeService.findPage(pageable);
        return DataBlock.success(EmployeeModel.bindData(page.getContent(),memberBoss,null),page,"执行成功");

    }

}