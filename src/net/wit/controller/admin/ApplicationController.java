package net.wit.controller.admin;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Application;
import net.wit.service.ApplicationService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
@Controller("adminApplicationController")
@RequestMapping("/admin/application")
public class ApplicationController {

    @Resource(name = "applicationServiceImpl")
    private ApplicationService applicationService;
    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable,Application.Status status, String searchValue, ModelMap model) {

        model.addAttribute("page",  applicationService.openPage(searchValue,pageable,status));
        model.addAttribute("nowDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("statusList", Application.Status.values());
        model.addAttribute("status",status);
        return "/admin/application/list";
    }
    /**
     *延期页面
     */
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public  String edit(@PathVariable Long id,ModelMap model){
        model.addAttribute("application",applicationService.find(id));
        model.addAttribute("status", Application.Status.values());
        return  "/admin/application/edit";
    }
    /**
     *更新日期
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public @ResponseBody    Message update(@PathVariable Long id, String time,Application.Status status) {
        Application application = applicationService.find(id);
        if(application==null){
            return  Message.error("无效应用");
        }
        //检查时间合法性
        if(application==null){
            return  Message.error("有效时间错误");
        }
       try{

           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           Date date=sdf.parse(time);
           application.setValidityDate(date);
           application.setStatus(status);
           applicationService.update(application);
           return Message.success("延期成功");
       }catch (Exception e){
           e.printStackTrace();
           return  Message.error("延期失败");
       }
    }
}
