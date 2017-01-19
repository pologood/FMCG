package net.wit.controller.assistant.member;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.AdModel;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.DataStatisticsModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.DateUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("assistantMemberDataStatisticsController")
@RequestMapping("/assistant/member/dataStatistics")
/**
 * 数据统计
 * Created by ruanx on 2017/1/3.
 */
public class DataStatisticsController extends BaseController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "orderItemServiceImpl")
    private OrderItemService orderItemService;
    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;
    @Resource(name = "visitRecordServiceImpl")
    private VisitRecordService visitRecordService;
    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;
    /**
     * 订单统计
     * type:1:7天，2:30天，3：90天
     *
     */
    @RequestMapping(value = "/order/summary", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock order(String type) throws Exception{
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        int startTime = 0,endTime=0;
        Date beginDate = null;
        Date endDate = null;
        //昨天数据
        if("1".equals(type)){
            startTime = -7;
        }
        //7天数据
        if("2".equals(type)){
            startTime = -30;
        }
        //
        // 30天数据
        if("3".equals(type)){
            startTime = -90;
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
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String endDateString = sf.format(endDate);
        String beginDateString = sf.format(beginDate);
        beginDate = sf.parse(beginDateString);
        endDate = sf.parse(endDateString);
        //时间段内下单总人数
        Long payMembers = tradeService.count(tenant,beginDate,endDate,Order.QueryStatus.paid);
        //时间段内订单总数
     //   Long orderAll = tradeService.count(tenant,beginDate,endDate,Order.QueryStatus.none);
        //时间段内下单总额
        List<OrderItem> orderItems = orderItemService.orderSettle(null,beginDate,endDate,tenant);
        BigDecimal d = new BigDecimal("0.00");
        BigDecimal averageMoney = null;
        for (OrderItem orderItem:orderItems){
            BigDecimal p = orderItem.getPrice();
            d = d.add(p);
        }
        if(payMembers!=0 && payMembers!=null){
             averageMoney =   d.divide(new BigDecimal(payMembers),2, RoundingMode.HALF_EVEN);

        }

        List<Map<String,Date>> mapsTime = new ArrayList();

        if(type.equals("1")){
            //前七天
            for(int i=7,j=6;i>=1;i--){
                Map<String, Date> mapTime = new HashMap<>();
                int startTime2 = -i,endTime2=-j;
                Calendar startCal2 = Calendar.getInstance();
                Calendar endCal2 = Calendar.getInstance();
                startCal2.setTime(currentDate);
                startCal2.add(Calendar.DAY_OF_MONTH,startTime2);
                endCal2.setTime(currentDate);
                endCal2.add(Calendar.DAY_OF_MONTH,endTime2);
                Date beginDate2 = startCal2.getTime();
                Date endDate2 = endCal2.getTime();
                String beginDateString2 = sf.format(beginDate2);
                String endDateString2 = sf.format(endDate2);
                beginDate2 = sf.parse(beginDateString2);
                endDate2 = sf.parse(endDateString2);
                mapTime.put("sTime",beginDate2);
                mapTime.put("eTime",endDate2);
                mapsTime.add(mapTime);

                j--;
            }
        }
        if(type.equals("2")){
            //前七天
            for(int i=35,j=30;i>=1;i=i-5){
                Map<String, Date> mapTime = new HashMap<>();
                int startTime2 = -i,endTime2=-j;
                Calendar startCal2 = Calendar.getInstance();
                Calendar endCal2 = Calendar.getInstance();
                startCal2.setTime(currentDate);
                startCal2.add(Calendar.DAY_OF_MONTH,startTime2);
                endCal2.setTime(currentDate);
                endCal2.add(Calendar.DAY_OF_MONTH,endTime2);
                Date beginDate2 = startCal2.getTime();
                Date endDate2 = endCal2.getTime();
                String beginDateString2 = sf.format(beginDate2);
                String endDateString2 = sf.format(endDate2);
                beginDate2 = sf.parse(beginDateString2);
                endDate2 = sf.parse(endDateString2);
                mapTime.put("sTime",beginDate2);
                mapTime.put("eTime",endDate2);
                mapsTime.add(mapTime);

                j=j-5;
            }
        }
        if(type.equals("3")){
            //前七天
            for(int i=105,j=90;i>=1;i=i-15){
                Map<String, Date> mapTime = new HashMap<>();
                int startTime2 = -i,endTime2=-j;
                Calendar startCal2 = Calendar.getInstance();
                Calendar endCal2 = Calendar.getInstance();
                startCal2.setTime(currentDate);
                startCal2.add(Calendar.DAY_OF_MONTH,startTime2);
                endCal2.setTime(currentDate);
                endCal2.add(Calendar.DAY_OF_MONTH,endTime2);
                Date beginDate2 = startCal2.getTime();
                Date endDate2 = endCal2.getTime();
                String beginDateString2 = sf.format(beginDate2);
                String endDateString2 = sf.format(endDate2);
                beginDate2 = sf.parse(beginDateString2);
                endDate2 = sf.parse(endDateString2);
                mapTime.put("sTime",beginDate2);
                mapTime.put("eTime",endDate2);
                mapsTime.add(mapTime);
                j=j-15;
            }
        }

        List ordersList = new ArrayList();
        List paysList = new ArrayList();
        List deliverysList = new ArrayList();

        for (Map<String, Date> m : mapsTime) {
            Map ormp = new HashedMap();
            Map paymp = new HashedMap();
            Map delmp = new HashedMap();
            Date beginDate2 = m.get("sTime");
            Date endDate2 = m.get("eTime");
           //每天的订单数
            Long orders = tradeService.count(tenant,beginDate2,endDate2,Order.QueryStatus.none);
            //每天的付款订单
            Long pays = tradeService.count(tenant,beginDate2,endDate2,Order.QueryStatus.paid);
            //每天的发货订单
            Long deliverys = tradeService.count(tenant,beginDate2,endDate2,Order.QueryStatus.shipped);

            Calendar endCal3 = Calendar.getInstance();
            endCal3.setTime(endDate2);
            endCal3.add(Calendar.DAY_OF_MONTH,-1);
            Date endCal3Time = endCal3.getTime();

            ormp.put("orders",orders);
            ormp.put("time",endCal3Time);
            paymp.put("pays",pays);
            paymp.put("time",endCal3Time);
            delmp.put("deliverys",deliverys);
            delmp.put("time",endCal3Time);
            ordersList.add(ormp);
            paysList.add(paymp);
            deliverysList.add(delmp);

        }
        Map maps = new HashedMap();
        maps.put("ordersList",ordersList);
        maps.put("paysList",paysList);
        maps.put("deliverysList",deliverysList);
        maps.put("payMembers",payMembers);
        maps.put("averageMoney",averageMoney);

        return DataBlock.success(maps,"执行成功");
    }

    /**
     * 流量统计
     * type:1:今天，2:7天
     * status:  pc:web,C端:app,购物屏:pad,微信:weixin,
     */
    @RequestMapping(value = "/flow/summary", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock flow(String type) throws Exception{
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        List<Map<String,Date>> mapsTime = new ArrayList();
        //当前时间（时间段结束点）
        Date current = new Date();
        String strCurren = sf.format(current);
        Date currentDate = sf.parse(strCurren);
        if(type.equals("1")){
            //今天
            Map<String, Date> mp = new HashMap<>();
            mp.put("sTime",currentDate);
            mp.put("eTime",currentDate);
            mapsTime.add(mp);
            int m=0,k=4;
            for(int i=6,j=5;i>=1;i--){
                Map<String, Date> mapTime = new HashMap<>();
                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();
                startCal.setTime(currentDate);
                startCal.add(Calendar.HOUR_OF_DAY,m);
                endCal.setTime(currentDate);
                if(i==7){
                    endCal.add(Calendar.HOUR_OF_DAY,0);
                }else{
                    endCal.add(Calendar.HOUR_OF_DAY, k);
                }
                Date beginDate = startCal.getTime();
                Date endDate = endCal.getTime();
                String beginDateString = sdf.format(beginDate);
                String endDateString = sdf.format(endDate);
                beginDate = sdf.parse(beginDateString);
                endDate = sdf.parse(endDateString);
                mapTime.put("sTime",beginDate);
                mapTime.put("eTime",endDate);
                mapsTime.add(mapTime);
                j--;
                k=k+4;
                m=m+4;
            }
        }
        if(type.equals("2")){
            //前七天
            for(int i=7,j=6;i>=1;i--){
                Map<String, Date> mapTime = new HashMap<>();
                int startTime = -i,endTime=-j;
                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();
                startCal.setTime(currentDate);
                startCal.add(Calendar.DAY_OF_MONTH,startTime);
                endCal.setTime(currentDate);
                endCal.add(Calendar.DAY_OF_MONTH,endTime);
                Date beginDate = startCal.getTime();
                Date endDate = endCal.getTime();
                String beginDateString = sf.format(beginDate);
                String endDateString = sf.format(endDate);
                beginDate = sf.parse(beginDateString);
                endDate = sf.parse(endDateString);
                mapTime.put("sTime",beginDate);
                mapTime.put("eTime",endDate);
                mapsTime.add(mapTime);

                j--;
            }
        }
        //访问量
        List fweixinList = new ArrayList();
        List fappList = new ArrayList();
        List fpingList = new ArrayList();
        List fwebList = new ArrayList();
        List rweixinList = new ArrayList();
        List rappList = new ArrayList();
        List rpingList = new ArrayList();
        List rwebList = new ArrayList();

        for (Map<String, Date> m : mapsTime) {
            Map fweixinmp = new HashedMap();
            Map fappmp = new HashedMap();
            Map fpingmp = new HashedMap();
            Map fwebmp = new HashedMap();
            Map rweixinmp = new HashedMap();
            Map rappmp = new HashedMap();
            Map rpingmp = new HashedMap();
            Map rwebmp = new HashedMap();

            Date beginDate2 = m.get("sTime");
            Date endDate2 = m.get("eTime");
            //pv访问量统计
            Long fpvWeixin = visitRecordService.count(tenant,beginDate2,endDate2,VisitRecord.VisitType.weixin);
            Long fuvWeixin = visitRecordService.uvCount(tenant,beginDate2,endDate2,VisitRecord.VisitType.weixin);
            //pv访问量统计
            Long fpvApp = visitRecordService.count(tenant,beginDate2,endDate2,VisitRecord.VisitType.app);
            Long fuvApp = visitRecordService.uvCount(tenant,beginDate2,endDate2,VisitRecord.VisitType.app);
            //pv访问量统计
            Long fpvPing = visitRecordService.count(tenant,beginDate2,endDate2,VisitRecord.VisitType.pad);
            Long fuvPing = visitRecordService.uvCount(tenant,beginDate2,endDate2,VisitRecord.VisitType.pad);
            //pv访问量统计
            Long fpvWeb = visitRecordService.count(tenant,beginDate2,endDate2,VisitRecord.VisitType.web);
            Long fuvWeb = visitRecordService.uvCount(tenant,beginDate2,endDate2,VisitRecord.VisitType.web);

            Calendar endCal3 = Calendar.getInstance();
            endCal3.setTime(endDate2);
            endCal3.add(Calendar.DAY_OF_MONTH,-1);
            Date endCal3Time = endCal3.getTime();

            fweixinmp.put("fpvWeixin",fpvWeixin);
            fweixinmp.put("fuvWeixin",fuvWeixin);
            fweixinmp.put("time",endCal3Time);
            fappmp.put("fpvApp",fpvApp);
            fappmp.put("fuvApp",fuvApp);
            fappmp.put("time",endCal3Time);
            fpingmp.put("fpvPing",fpvPing);
            fpingmp.put("fuvPing",fuvPing);
            fpingmp.put("time",endCal3Time);
            fwebmp.put("fpvWeb",fpvWeb);
            fwebmp.put("fuvWeb",fuvWeb);
            fwebmp.put("time",endCal3Time);
            fweixinList.add(fweixinmp);
            fappList.add(fweixinmp);
            fpingList.add(fpingmp);
            fwebList.add(fwebmp);



            rweixinmp.put("rweixin",fpvWeixin);
            rweixinmp.put("time",beginDate2);
            rappmp.put("rapp",fpvApp);
            rappmp.put("time",beginDate2);
            rpingmp.put("rping",fpvPing);
            rpingmp.put("time",beginDate2);
            rwebmp.put("rweb",fpvWeb);
            rwebmp.put("time",beginDate2);
            rweixinList.add(rweixinmp);
            rappList.add(rappmp);
            rpingList.add(rpingmp);
            rwebList.add(rwebmp);
        }
        Map maps = new HashedMap();
       //f访问量，r人均停留 微信:weixin，C端:app，购物屏:pad, web,
        maps.put("fweixinList",fweixinList);
        maps.put("fappList",fappList);
        maps.put("fpingList",fpingList);
        maps.put("fwebList",fwebList);
        maps.put("rweixinList",rweixinList);
        maps.put("rappList",rappList);
        maps.put("rpingList",rpingList);
        maps.put("rwebList",rwebList);

        return DataBlock.success(maps,"执行成功");
    }
    /**
     *     会员统计
     */
    @RequestMapping(value = "/consumer/summary", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock consumer(String type) throws Exception{
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        List<Map<String,Date>> mapsTime = new ArrayList();
        //当前时间（时间段结束点）
        Date currentDate = new Date();
        if(type.equals("1")){
            //前七天
            for(int i=7,j=6;i>=1;i--){
                Map<String, Date> mapTime = new HashMap<>();
                int startTime = -i,endTime=-j;
                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();
                startCal.setTime(currentDate);
                startCal.add(Calendar.DAY_OF_MONTH,startTime);
                endCal.setTime(currentDate);
                endCal.add(Calendar.DAY_OF_MONTH,endTime);
                Date beginDate = startCal.getTime();
                Date endDate = endCal.getTime();
                String beginDateString = sf.format(beginDate);
                String endDateString = sf.format(endDate);
                beginDate = sf.parse(beginDateString);
                endDate = sf.parse(endDateString);
                mapTime.put("sTime",beginDate);
                mapTime.put("eTime",endDate);
                mapsTime.add(mapTime);

                j--;
            }
        }
        if(type.equals("2")){
            //前七天
            for(int i=35,j=30;i>=1;i=i-5){
                Map<String, Date> mapTime = new HashMap<>();
                int startTime = -i,endTime=-j;
                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();
                startCal.setTime(currentDate);
                startCal.add(Calendar.DAY_OF_MONTH,startTime);
                endCal.setTime(currentDate);
                endCal.add(Calendar.DAY_OF_MONTH,endTime);
                Date beginDate = startCal.getTime();
                Date endDate = endCal.getTime();
                String beginDateString = sf.format(beginDate);
                String endDateString = sf.format(endDate);
                beginDate = sf.parse(beginDateString);
                endDate = sf.parse(endDateString);
                mapTime.put("sTime",beginDate);
                mapTime.put("eTime",endDate);
                mapsTime.add(mapTime);

                j=j-5;
            }
        }
        if(type.equals("3")){
            //前七天
            for(int i=105,j=90;i>=1;i=i-15){
                Map<String, Date> mapTime = new HashMap<>();
                int startTime = -i,endTime=-j;
                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();
                startCal.setTime(currentDate);
                startCal.add(Calendar.DAY_OF_MONTH,startTime);
                endCal.setTime(currentDate);
                endCal.add(Calendar.DAY_OF_MONTH,endTime);
                Date beginDate = startCal.getTime();
                Date endDate = endCal.getTime();
                String beginDateString = sf.format(beginDate);
                String endDateString = sf.format(endDate);
                beginDate = sf.parse(beginDateString);
                endDate = sf.parse(endDateString);
                mapTime.put("sTime",beginDate);
                mapTime.put("eTime",endDate);
                mapsTime.add(mapTime);
                j=j-15;
            }
        }

        List newAddMemberList = new ArrayList();
        List lossMemberList = new ArrayList();
        List netGrowthMemberList = new ArrayList();

        for (Map<String, Date> m : mapsTime) {
            Map ormp = new HashedMap();
            Map paymp = new HashedMap();
            Map delmp = new HashedMap();

            Date beginDate2 = m.get("sTime");
            Date endDate2 = m.get("eTime");
            //每天流失的会员数
            Long lossMember = consumerService.count(tenant,Consumer.Status.disable,beginDate2,endDate2);
            //每天净增的会员数
            Long netGrowthMember = consumerService.count(tenant,Consumer.Status.enable,beginDate2,endDate2);


            Calendar endCal3 = Calendar.getInstance();
            endCal3.setTime(endDate2);
            endCal3.add(Calendar.DAY_OF_MONTH,-1);
            Date endCal3Time = endCal3.getTime();

            ormp.put("newAddMember",lossMember+netGrowthMember);
            ormp.put("time",endCal3Time);
            paymp.put("lossMember",lossMember);
            paymp.put("time",endCal3Time);
            delmp.put("netGrowthMember",netGrowthMember);
            delmp.put("time",endCal3Time);
            newAddMemberList.add(ormp);
            lossMemberList.add(paymp);
            netGrowthMemberList.add(delmp);

        }
        Map maps = new HashedMap();
        maps.put("newAddMemberList",newAddMemberList);
        maps.put("lossMemberList",lossMemberList);
        maps.put("netGrowthMemberList",netGrowthMemberList);
        maps.put("memberNum",consumerService.count(tenant,Consumer.Status.enable,null,null));

        return DataBlock.success(maps,"执行成功");
    }

    /**
     * 获取统计模块底部广告
     */
    @RequestMapping(value = "/advertising", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock advertising() {
        AdPosition adPosition = adPositionService.find(186l, null,null, 1);
        return DataBlock.success(AdModel.bindData(adPosition.getAds()), "执行成功");
    }
}
