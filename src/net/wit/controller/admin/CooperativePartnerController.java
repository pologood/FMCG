package net.wit.controller.admin;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.CooperativePartner;
import net.wit.service.CooperativePartnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/8/22.
 */
@Controller("adminCooperativePartnerController")
@RequestMapping("/admin/cooperative/partner")
public class CooperativePartnerController extends BaseController {

    @Resource(name = "cooperativePartnerServiceImpl")
    private CooperativePartnerService cooperativePartnerService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable,String searchValue, ModelMap model) {
        pageable.setSearchProperty("name");
        pageable.setSearchValue(searchValue);
        model.addAttribute("page", cooperativePartnerService.findPage(pageable));
        return "/admin/cooperative_partner/list";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "/admin/cooperative_partner/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(CooperativePartner cooperativePartner,  RedirectAttributes redirectAttributes) {
        cooperativePartnerService.save(cooperativePartner);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id,ModelMap model) {
        model.addAttribute("cooperativePartner", cooperativePartnerService.find(id));
        return "/admin/cooperative_partner/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long id, String licensePhoto,String licensePhoto1,Integer order, RedirectAttributes redirectAttributes) {
        CooperativePartner cooperativePartner = cooperativePartnerService.find(id);
        cooperativePartner.setLicensePhoto(licensePhoto);
        cooperativePartner.setLicensePhoto1(licensePhoto1);
        cooperativePartner.setOrder(order);
        cooperativePartnerService.update(cooperativePartner);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids != null) {
            cooperativePartnerService.delete(ids);
        }
        return SUCCESS_MESSAGE;
    }
}
