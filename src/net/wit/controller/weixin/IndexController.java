package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Member;
import net.wit.service.MemberService;
import net.wit.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * 首页
 * Created by WangChao on 2016/11/28.
 */
@Controller("weixinIndexController")
@RequestMapping("/weixin/index")
public class IndexController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    /**
     * 首页
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(String extension,HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/index";
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String redirectUrl, String extension, HttpServletRequest request, HttpServletResponse response) {
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        Member member = memberService.getCurrent();
        WebUtils.addCookie(request, response, "session_member_username", member == null ? "" : member.getUsername());
        return "redirect:" + URLDecoder.decode(redirectUrl).replaceAll("extension=", "extension_back=");
    }

    /**
     * 检测登录
     */
    @RequestMapping(value = "/check_login", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock checkLogin() {
        return DataBlock.success(memberService.getCurrent() != null, "执行成功");
    }



}
