package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.assistant.model.*;
import net.wit.entity.Consumer;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.entity.VisitRecord;
import net.wit.service.ConsumerService;
import net.wit.service.MemberService;
import net.wit.service.VisitRecordService;
import net.wit.util.DateUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("assistantMemberAssociatorController")
@RequestMapping("/assistant/member/associator")
/**
 * 会员
 */
public class AssociatorController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "visitRecordServiceImpl")
    private VisitRecordService visitRecordService;
    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    /**
     * 会员列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(String keyword,Pageable pageable) {

        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        Page<Consumer> page = consumerService.findPage(tenant, Consumer.Status.enable, pageable);
        data.put("members", ConsumerModel.bindData(page.getContent()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date endDate = DateUtil.changeStrToDate(sdf.format(new Date().getTime()+1 * 24 * 60 * 60 * 1000));
        Date beginDate = DateUtil.changeStrToDate(sdf.format(new Date().getTime()-6 * 24 * 60 * 60 * 1000));
        data.put("sevenVisit",visitRecordService.findByVisitRecordList(tenant,beginDate,endDate).size());
        data.put("sevenAdd",memberService.findByAddList(member,beginDate,endDate).size());
        data.put("payAttention",memberService.findFavoriteList(member).size());

        return DataBlock.success(data, "执行成功");
    }
    /**
     * 七日访问
     */
    @RequestMapping(value = "/sevenVisit", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock sevenVisit() {

        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Map<String,String>> mapsTime = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        //前六天
        int j=5;
        for(int i=6;i>=1;i--){
            Map<String, String> mapTime = new HashMap<>();
            String start_time = sdf.format(new Date().getTime()-i * 24 * 60 * 60 * 1000);
            String end_time = sdf.format(new Date().getTime()-j * 24 * 60 * 60 * 1000);
            mapTime.put("startTime",start_time);
            mapTime.put("endTime",end_time);
            mapsTime.add(mapTime);
            j--;
        }
        //今天
        Map<String, String> mapTime2 = new HashMap<>();
        mapTime2.put("startTime",sdf.format(new Date().getTime()));
        mapTime2.put("endTime",sdf.format(new Date().getTime()+1 * 24 * 60 * 60 * 1000));
        mapsTime.add(mapTime2);

        Map<String,List> maps = new HashedMap();
        List<Date> mapWeeks = new ArrayList<>();
        int g=6;
        for(Map<String,String> m:mapsTime){
            Date begin_date = DateUtil.changeStrToDate(m.get("startTime"));
            Date end_date = DateUtil.changeStrToDate(m.get("endTime"));

            List<VisitRecord> list = visitRecordService.findByVisitRecordList(tenant,begin_date,end_date);
            maps.put("day"+g,VisitRecordListModel.bindData(list));
            mapWeeks.add(begin_date);
            g--;
        }
        maps.put("weesks",mapWeeks);
        return DataBlock.success(maps,"执行成功");
    }
    /**
     * 关注我的
     */
    @RequestMapping(value = "/payAttention", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock payAttention(Pageable pageable) {

        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }

        Page<Member> page = memberService.findFavoritePage(member,pageable);

        return DataBlock.success(AssociatorModel.bindData(page.getContent()),page, "执行成功");
    }
    /**
     * 七日会员新增
     */
    @RequestMapping(value = "/sevenAdd", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock sevenAdd() {

        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }

        List<Map<String,String>> mapsTime = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        //前六天
        int j=5;
        for(int i=6;i>=1;i--){
            Map<String, String> mapTime = new HashMap<>();
            String start_time = sdf.format(new Date().getTime()-i * 24 * 60 * 60 * 1000);
            String end_time = sdf.format(new Date().getTime()-j * 24 * 60 * 60 * 1000);
            mapTime.put("startTime",start_time);
            mapTime.put("endTime",end_time);
            mapsTime.add(mapTime);
            j--;
        }
        //今天
        Map<String, String> mapTime2 = new HashMap<>();
        mapTime2.put("startTime",sdf.format(new Date().getTime()));
        mapTime2.put("endTime",sdf.format(new Date().getTime()+1 * 24 * 60 * 60 * 1000));
        mapsTime.add(mapTime2);

        Map<String,List> maps = new HashedMap();
        List<Date> mapWeeks = new ArrayList<>();
        int g=6;
        for(Map<String,String> m:mapsTime){
            Date begin_date = DateUtil.changeStrToDate(m.get("startTime"));
            Date end_date = DateUtil.changeStrToDate(m.get("endTime"));

            List<Member> list = memberService.findByAddList(member,begin_date,end_date);
            maps.put("day"+g, SevenAddListModel.bindData(list));
            mapWeeks.add(begin_date);
            g--;
        }
        maps.put("weesks",mapWeeks);
        return DataBlock.success(maps,"执行成功");
    }



}