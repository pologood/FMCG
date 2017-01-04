package net.wit.controller.admin;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Services;
import net.wit.service.ServicesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by My-PC on 16/05/31.
 */
@Controller("servicesController")
@RequestMapping("/admin/services")
public class ServicesController extends BaseController {

    @Resource(name = "servicesServiceImpl")
    private ServicesService servicesService;
    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Date beginDate, Date endDate, Pageable pageable, ModelMap model) {

        if(beginDate!=null){
            Long time=beginDate.getTime();
            Long begin=time-24*60*60*1000*7;
            beginDate=new Date(begin);
        }
        if(endDate!=null){
            Long time=endDate.getTime();
            Long end=time+24*60*60*1000-1;
            endDate=new Date(end);
        }
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("page", servicesService.findPage(beginDate, endDate,null,pageable));
        return "/admin/services/list";
    }

    /**
     * 跟踪
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("types", Services.Type.values());
        model.addAttribute("services", servicesService.find(id));
        return "/admin/services/edit";
    }

    /**
     * 处理提交的服务申请
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Message update(Long id, Services.Type type, String content, RedirectAttributes redirectAttributes) {
        Services services = servicesService.find(id);
        if(services==null){
            //addFlashMessage(redirectAttributes, Message.error("无效的编号"));
            return Message.error("无效的编号");
        }
        //services.setModifyDate(new Date());
        services.setType(type);
        services.setState(Services.State.success);
        services.setContent(content);
        servicesService.update(services);
        //addFlashMessage(redirectAttributes, Message.success("操作成功"));
        return Message.success("操作成功");
    }
}
