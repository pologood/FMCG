package net.wit.controller.wap.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 意见反馈
 * Created by WangChao on 2016-7-26.
 */
@Controller("wapMemberFeedbackController")
@RequestMapping("/wap/member/feedback")
public class FeedbackController {

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(){
        return "redirect:/wap/member/receiver/list.jhtml";
    }
}
