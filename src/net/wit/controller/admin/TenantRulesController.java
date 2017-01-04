package net.wit.controller.admin;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Barcode;
import net.wit.entity.ProductImage;
import net.wit.entity.TenantRules;
import net.wit.service.TenantRulesService;
import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商家规则控制器
 * Created by Administrator on 2016/8/8 1153.
 */
@Controller("adminTenantRulesController")
@RequestMapping("/admin/tenant_rules")
public class TenantRulesController extends BaseController {
    @Resource(name = "tenantRulesServiceImpl")
    private TenantRulesService tenantRulesService;

    /**
     * 列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, String searchValue, ModelMap model) {

        model.addAttribute("page", tenantRulesService.openPage(searchValue, pageable));
        return "/admin/tenant_rules/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, Pageable pageable, ModelMap model) {
        tenantRulesService.delete(id);
        model.addAttribute("page", tenantRulesService.findPage(pageable));
        return "/admin/tenant_rules/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        try {
            if (ids.length >= tenantRulesService.count()) {
                return Message.error("admin.message.error");
            }
            tenantRulesService.delete(ids);
        } catch (Exception e) {
            return Message.error("admin.message.error");
        }
        return SUCCESS_MESSAGE;
    }

    /**
     * 编辑页面
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model, Pageable pageable) {
        List<Filter> filter=new ArrayList<Filter>();
        filter.add(new Filter("lev", Filter.Operator.eq,1));
        pageable.setFilters(filter);
        model.addAttribute("tenantRules", tenantRulesService.find(id));
        model.addAttribute("tenantRulesParent", tenantRulesService.openPage(null,pageable).getContent() );
        model.addAttribute("types", TenantRules.Type.values());
        return "/admin/tenant_rules/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/edit/update", method = RequestMethod.POST)
    public String update(TenantRules tenantRules,
                         String[] operTypes,
                         RedirectAttributes redirectAttributes) {
        try {
            String oper = "";
            if (operTypes!=null) {
                for (String item : operTypes) {
                    oper += item + ",";
                }
                oper = oper.substring(0, oper.length() - 1);
            }
            tenantRules.setOper(oper);
            if (tenantRules.getParent().getId()==null||tenantRules.getLev()==1) {
                tenantRules.setParent(null);
            }
            tenantRulesService.update(tenantRules);
            return "redirect:/admin/tenant_rules/list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }

    }


    /**
     * 添加页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model, Pageable pageable) {
      List<Filter> filter=new ArrayList<Filter>();
        filter.add(new Filter("lev", Filter.Operator.eq,1));
        pageable.setFilters(filter);
        model.addAttribute("tenantRulesParent", tenantRulesService.openPage(null,pageable).getContent() );
        model.addAttribute("types", TenantRules.Type.values());
        return "/admin/tenant_rules/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(TenantRules tenantRules,
                       String[] operTypes,
                       RedirectAttributes redirectAttributes) {
        try {
            String oper = "";
            if (operTypes!=null) {
                for (String item : operTypes) {
                    oper += item + ",";
                }
                oper = oper.substring(0, oper.length() - 1);
            }
            tenantRules.setOper(oper);
            if (tenantRules.getParent().getId()==null||tenantRules.getLev()==1) {
                tenantRules.setParent(null);
            }
            tenantRulesService.save(tenantRules);
            return "redirect:/admin/tenant_rules/list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }

    }

}
