package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.controller.app.BaseController;
import net.wit.controller.assistant.model.AdModel;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.SingleTenantModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.DateUtil;
import org.jaxen.saxpath.Operator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("assistantMemberIndexController")
@RequestMapping("/assistant/member/index")
public class IndexController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;
    @Resource(name = "adPositionServiceImpl")
    private  AdPositionService adPositionService;
    @Resource(name = "taskServiceImpl")
    private TaskService taskService;
    @Resource(name = "visitRecordServiceImpl")
    private  VisitRecordService visitRecordService;
    @Resource(name ="depositServiceImpl")
    private  DepositService depositService;

    /**
     *    首页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock index() {

        Member member = memberService.getCurrent();
        if (member==null) {
           return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
          return   DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        boolean isownerMember = false;
        if(member.equals(tenant.getMember())){
            isownerMember=true;
        }
        Map<String, Object> data = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date();//获取当前时间
        String start_time = sdf.format(date.getTime());
        String end_time = sdf.format(date.getTime()+1 * 24 * 60 * 60 * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
        calendar.getTime();//获取一年前的时间，或者一个月前的时间
        String start_time2 = sdf.format(calendar.getTime());
        Date begin_date =  DateUtil.changeStrToDate(start_time);
        Date end_date = DateUtil.changeStrToDate(end_time);
        Date begin_date2 = DateUtil.changeStrToDate(start_time2);

        //今日订单
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("orderStatus", Filter.Operator.ne,Order.OrderStatus.unconfirmed));
        Map map1 =  tradeService.findTradingCount(filters, tenant, null, null, begin_date, end_date);
        //今日收入
        BigDecimal income = depositService.income(tenant.getMember(),null,begin_date,end_date, null);
        BigDecimal outcome = depositService.outcome(tenant.getMember(),null,begin_date,end_date, null);
        //近30天收入
        BigDecimal thincome = depositService.income(tenant.getMember(),null,begin_date2,end_date, null);
        BigDecimal thoutcome = depositService.outcome(tenant.getMember(),null,begin_date2,end_date, null);

        AdPosition adPosition = adPositionService.find(176L, null,null, 1);
        SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyyMM");
        long month = Long.parseLong(sdfMonth.format(new Date()));
        Map<String, Object> taskMap = taskService.findByTenant(null,tenant,month);

        //今日访客
        data.put("todayVisitor",visitRecordService.findByVisitRecordList(tenant,begin_date,end_date).size());
        data.put("todayIncomeTotal",income.subtract(outcome));
        data.put("todayTradeSum",map1.get("tradeSum"));
        data.put("ThirtyIncomeTotal",thincome.subtract(thoutcome));
        SingleTenantModel singleTenantModel = new SingleTenantModel();
        singleTenantModel.copyFrom(tenant);
        data.put("tenant",singleTenantModel);
        data.put("ads", AdModel.bindData(adPosition.getAds()));
        data.put("isownerMember",isownerMember);

        DecimalFormat df = new DecimalFormat("#");
        if("0".equals(taskMap.get("sale").toString())){
            data.put("proSale","0");
        }else {
            data.put("proSale", df.format(new BigDecimal(Double.parseDouble((taskMap.get("doSale").toString()))).divide(new BigDecimal(Double.parseDouble((taskMap.get("sale").toString()))), 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100));
        }
        if("0".equals(taskMap.get("share").toString())){
            data.put("proShare","0");
        }else {
            data.put("proShare", df.format(Double.parseDouble(taskMap.get("doShare").toString()) / Double.parseDouble(taskMap.get("share").toString()) * 100));
        }
        if("0".equals(taskMap.get("invite").toString())){
            data.put("proInvite","0");
        }else {
            data.put("proInvite", df.format(Double.parseDouble(taskMap.get("doInvite").toString()) / Double.parseDouble(taskMap.get("invite").toString()) * 100));
        }
        if("0".equals(taskMap.get("coupon").toString())){
            data.put("proCoupon","0");
        }else {
            data.put("proCoupon", df.format(Double.parseDouble(taskMap.get("doCoupon").toString()) / Double.parseDouble(taskMap.get("coupon").toString()) * 100));
        }


        return DataBlock.success(data, "执行成功");
    }

/**
 * 获取营销模块底部广告
 */
@RequestMapping(value = "/advertising", method = RequestMethod.GET)
@ResponseBody
public DataBlock advertising() {
    AdPosition adPosition = adPositionService.find(185l, null,null, 1);
    return DataBlock.success(AdModel.bindData(adPosition.getAds()), "执行成功");
}

}