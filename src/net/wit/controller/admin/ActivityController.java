package net.wit.controller.admin;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ActivityInventory;
import net.wit.entity.ActivityRules;
import net.wit.service.ActivityInventoryService;
import net.wit.service.ActivityRulesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/7/15.
 */
@Controller("adminActivityController")
@RequestMapping("/admin/activity")
public class ActivityController extends BaseController {

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @Resource(name = "activityInventoryServiceImpl")
    private ActivityInventoryService activityInventoryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ActivityRules.Type type,Pageable pageable, ModelMap model) {
        Page<ActivityRules> activityRulesPage =activityRulesService.openPage(type,pageable);
        model.addAttribute("page", activityRulesPage);
        model.addAttribute("type", type);
        model.addAttribute("ActivityRulesTypes", ActivityRules.Type.values());
        return "/admin/activity/list";
    }

    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    public String inventory(ActivityInventory.Status status,String keyword, Pageable pageable, ModelMap model) {
        Page<ActivityInventory> page =activityInventoryService.openPage(status,keyword,pageable);
        model.addAttribute("page", page);
        model.addAttribute("status", status);
        model.addAttribute("ActivityInventoryStatus", ActivityInventory.Status.values());
        return "/admin/activity/inventory";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("ActivityRulesTypes", ActivityRules.Type.values());
        model.addAttribute("ActivityRulesStatus", ActivityRules.Status.values());
        return "/admin/activity/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(ActivityRules activityRules, RedirectAttributes redirectAttributes) {
        if(activityRules.getPoint()==null){
            activityRules.setPoint(0l);
        }
        if(activityRules.getAmount()==null){
            activityRules.setAmount(BigDecimal.ZERO);
        }
        activityRulesService.save(activityRules);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        ActivityRules activityRules = activityRulesService.find(id);
        model.addAttribute("activityRules",activityRules);
        model.addAttribute("ActivityRulesTypes", ActivityRules.Type.values());
        model.addAttribute("ActivityRulesStatus", ActivityRules.Status.values());
        return "/admin/activity/edit";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/inventory/edit/{id}", method = RequestMethod.GET)
    public String inventoryEdit(@PathVariable Long id, ActivityInventory.Status status, ModelMap model) {
        ActivityInventory activityInventory = activityInventoryService.find(id);
        model.addAttribute("activityInventory",activityInventory);
        model.addAttribute("status",status);
        model.addAttribute("ActivityInventoryStatus", ActivityInventory.Status.values());
        return "/admin/activity/inventory_edit";
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long id, ActivityRules.Type type, ActivityRules.Status status, String description, @RequestParam(defaultValue = "0")Long point, @RequestParam(defaultValue = "0")BigDecimal amount,String remarks, RedirectAttributes redirectAttributes) {
        ActivityRules activityRules = activityRulesService.find(id);
        activityRules.setType(type);
        activityRules.setStatus(status);
        activityRules.setDescription(description);
        activityRules.setPoint(point);
        activityRules.setAmount(amount);
        activityRules.setRemarks(remarks);
        activityRulesService.update(activityRules);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/inventory/update", method = RequestMethod.POST)
    public String inventoryUpdate(Long id, ActivityInventory.Status status, String remarks, RedirectAttributes redirectAttributes) {
        ActivityInventory activityInventory = activityInventoryService.find(id);
        activityInventory.setStatus(status);
        activityInventory.setRemarks(remarks);
        activityInventoryService.update(activityInventory);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:../inventory.jhtml";
    }


    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        activityRulesService.delete(ids);
        return SUCCESS_MESSAGE;
    }
}
