package net.wit.controller.wap.member;

import net.wit.controller.wap.BaseController;
import net.wit.service.MemberService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/6.
 */
@Controller("wapMemberChatController")
@RequestMapping("/wap/member/chat")
public class ChatController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Long targetid,Long tenantid,ModelMap model){
        model.addAttribute("tscpath","/wap/member/chat");
        model.addAttribute("member",memberService.getCurrent());
        model.addAttribute("guide",memberService.find(targetid));
        model.addAttribute("tenant",tenantService.find(tenantid));
        return "/wap/member/chat/index";
    }
}
