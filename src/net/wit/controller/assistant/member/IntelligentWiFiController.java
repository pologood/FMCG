package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.ScreenEquipmentListModel;
import net.wit.controller.assistant.model.UnionListModel;
import net.wit.controller.assistant.model.UnionTenantScreenListModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.DateUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("assistantMemberIntelligentWiFiController")
@RequestMapping("/assistant/member/intelligentwifi")
/**
 * 智能wifi
 */
public class IntelligentWiFiController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "unionServiceImpl")
    private UnionService unionService;
    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @Resource(name = "unionTenantServiceImpl")
    private UnionTenantService unionTenantService;
    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;
    @Resource(name = "extendCatalogServiceImpl")
    private ExtendCatalogService extendCatalogService;
    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;
    @Resource(name = "snServiceImpl")
    private SnService snService;
    @Resource (name = "tagServiceImpl")
    private TagService tagService;
    @Resource(name = "wifiRecordServiceImpl")
    private WifiRecordService wifiRecordService;

    /**
     * 客流量统计
     */
    @RequestMapping(value = "/trafficStatistics", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock trafficStatistics(Long time) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        if (tenant.getStatus() != Tenant.Status.success) {
            return DataBlock.error("请先开通店铺");
        }
        if(time==null||time==0){
            return DataBlock.error("请选择时间");
        }
        Map maps = new HashedMap();
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = sdf.format(time);
        //会员等级初始化
        List listVip = new ArrayList();
        List<Object> vips = new ArrayList();
        for(int i=0;i<5;i++){
            listVip.add("VIP"+i);
            vips.add(0);
        }
        //时间初始化
        List listTime = new ArrayList();
        List<Object> times = new ArrayList();
        for(int i=0;i<24;i++){
            listTime.add(""+i+"");
            times.add(0);
        }

        List<Map<String,Object>> wifiRecords = wifiRecordService.findList(dateTime,tenant);
        List<Map<String,Object>> memberRecords = wifiRecordService.findMemberList(dateTime,tenant);

        int ii = 0;
        if(wifiRecords.size()>0){
            for(Object obj:listTime){
                for(Map<String,Object> map:wifiRecords){
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        Object  key  = entry.getKey();
                        Object val = entry.getValue();
                        if(key.equals(obj)){
                            times.set(ii,val) ;
                        }
                    }
                }
                ii++;
            }
        }
        int jj = 0;
        if(memberRecords.size()>0){
            for(Object obj:listVip){
                for(Map<String,Object> map:memberRecords){
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        Object  key  = entry.getKey();
                        Object val = entry.getValue();
                        if(key.equals(obj)){
                            vips.set(jj,val) ;
                        }
                    }
                }
                jj++;
            }
        }
        int memberTotals = 0;
        for(int i=0;i<5;i++){
           int a =  Integer.valueOf(String.valueOf(vips.get(i)));
            memberTotals +=a;
        }
        maps.put("timeRecords",times);
        maps.put("memberRecords",vips);
        maps.put("memberTotals",memberTotals);
        return DataBlock.success(maps, "执行成功");
    }

    /**
     * 智能识别
     * type:昨天(1),近七天(2),近三十天(3)
     */
    @RequestMapping(value = "/wifi/summary", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock summary(String type,Pageable pageable)throws Exception {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        if (tenant.getStatus() != Tenant.Status.success) {
            return DataBlock.error("请先开通店铺");
        }
        int startTime = 0,endTime=1;
        Date beginDate = null;
        Date endDate = null;
        //昨天数据
        if("1".equals(type)){
            startTime = -1;
        }
        //7天数据
        if("2".equals(type)){
            startTime = -6;
        }
        //
        // 30天数据
        if("3".equals(type)){
            startTime = -29;
        }
        //当前时间（时间段结束点）
        Date currentDate = new Date();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(currentDate);
        startCal.add(Calendar.DAY_OF_MONTH,startTime);
        endCal.setTime(currentDate);
        endCal.add(Calendar.DAY_OF_MONTH,endTime);
        beginDate = startCal.getTime();
        endDate = endCal.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String beginDateString = sf.format(beginDate);
        String endDateString = sf.format(endDate);
        beginDate = sf.parse(beginDateString);
        endDate = sf.parse(endDateString);
        Page<Map<String,Object>> page = wifiRecordService.findSummaryPage(beginDate,endDate,tenant,pageable);

        return DataBlock.success(page.getContent(),page, "执行成功");
    }

}