package net.wit.controller.admin;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.entity.Admin;
import net.wit.entity.InvitationCode;
import net.wit.entity.Sn;
import net.wit.service.AdminService;
import net.wit.service.InvitationCodeService;
import net.wit.service.SnService;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by WangChao on 2016-1-11.
 */
@Controller("adminInvitationCodeController")
@RequestMapping("/admin/invitationCode")
public class InvitationCodeController extends BaseController {

    @Resource(name = "invitationCodeServiceImpl")
    private InvitationCodeService invitationCodeService;
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "snServiceImpl")
    private SnService snService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        Admin admin = adminService.getCurrent();
        Page<InvitationCode> page = invitationCodeService.findPage(admin, pageable);
        model.addAttribute("page", page);
        return "/admin/invitation_code/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        String code = generateInviteCode();
        model.addAttribute("code", code);
        Setting setting = SettingUtils.get();
        model.addAttribute("functionFee", setting.getFunctionFee());
        model.addAttribute("minInvitePrice", setting.getMinInvitePrice());
        return "/admin/invitation_code/add";
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(InvitationCode invitationCode) {
        try {
            Admin admin = adminService.getCurrent();
            invitationCode.setAdmin(admin);
            invitationCodeService.save(invitationCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        InvitationCode invitationCode = invitationCodeService.find(id);
        model.addAttribute("invitationCode", invitationCode);
        return "/admin/invitation_code/edit";
    }

    @RequestMapping(value = "/generateInviteCode", method = RequestMethod.GET)
    public String generateInviteCode() {
        String code = snService.generate(Sn.Type.inviteCode);
        if (code.length() < 6) {
            int n = 6 - code.length();
            for (int i = 0; i < n; i++) {
                code = "0" + code;
            }
        }
        return code;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        invitationCodeService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    /**
     * 更新
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(InvitationCode invitationCode) {
        try {
            Admin admin = adminService.getCurrent();
            invitationCode.setAdmin(admin);
            invitationCodeService.update(invitationCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }
        return "redirect:list.jhtml";
    }

}
