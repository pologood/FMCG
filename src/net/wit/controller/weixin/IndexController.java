package net.wit.controller.weixin;

import net.wit.entity.Member;
import net.wit.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

}
