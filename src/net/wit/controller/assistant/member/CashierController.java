package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.PayBillModel;
import net.wit.controller.assistant.model.PaymentDetailsModel;
import net.wit.controller.assistant.model.SingelMemberModel;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Payment;
import net.wit.entity.Tenant;
import net.wit.service.*;
import net.wit.util.DateUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("assistantMemberCashierController")
@RequestMapping("/assistant/member/cashier")
/**
 * 收银台
 */
public class CashierController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;
    @Resource(name ="payBillServiceImpl")
    private  PayBillService payBillService;
    @Resource(name = "paymentServiceImpl")
    private  PaymentService paymentService;
    /**
     * 进入收银台
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list() {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return   DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Map<String,Object> data = new HashMap<String,Object>();
        Member ownerMember =member.getTenant().getMember();
        SingelMemberModel ownerMemberModel = new SingelMemberModel();
        ownerMemberModel.copyFrom(ownerMember);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date();//获取当前时间
        String start_time = sdf.format(date.getTime());
        String end_time = sdf.format(date.getTime()+1 * 24 * 60 * 60 * 1000);
        Date begin_date =  DateUtil.changeStrToDate(start_time);
        Date end_date = DateUtil.changeStrToDate(end_time);
        //今日收款
        BigDecimal bigDecimal =  payBillService.findPayBillSum(tenant, null, PayBill.Status.success, begin_date, end_date);
        data.put("ownerMember",ownerMemberModel);
        data.put("todayCollection",bigDecimal);
        return DataBlock.success(data, "执行成功");
    }
    /**
     * 七日明细账单查询
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public DataBlock query(long id) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return   DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        PayBill payBill = payBillService.find(id);
        PaymentDetailsModel paymentDetailsModel = new  PaymentDetailsModel();
        Payment payment = paymentService.findByPayMent(payBill);
        if(payment!=null){
            paymentDetailsModel.copyFrom(payment) ;
        }
        if(payBill.getTenant()!=null){
            paymentDetailsModel.setTenantName(payBill.getTenant().getName());
        }
        return DataBlock.success(paymentDetailsModel,"执行成功");
    }

    /**
     * begin_date 开始时间
     * end_date 结束时间
     * 七天明细,
     */
    @RequestMapping(value = "/sevenlist", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock sevenlist(Long begin_date, Long end_date, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String begin_time = sdf.format(begin_date);
        String end_time = sdf.format(end_date);
        Date beginDate = DateUtil.changeStrToDate(begin_time);
        Date endDate = DateUtil.changeStrToDate(end_time);

        List<Map<String,String>> mapsTime = new ArrayList();
        //前六天
        for(int i=6,j=5;i>=1;i--){
            Map<String, String> mapTime = new HashMap<>();
            String start = sdf.format(new Date().getTime()-i * 24 * 60 * 60 * 1000);
            String end = sdf.format(new Date().getTime()-j * 24 * 60 * 60 * 1000);
            mapTime.put("startTime",start);
            mapTime.put("endTime",end);
            mapsTime.add(mapTime);
            j--;
        }
        //今天
        Map<String, String> mapTime2 = new HashMap<>();
        mapTime2.put("startTime",sdf.format(new Date().getTime()));
        mapTime2.put("endTime",sdf.format(new Date().getTime()+1 * 24 * 60 * 60 * 1000));
        mapsTime.add(mapTime2);

        Map maps = new HashedMap();
        Page<PayBill> page = payBillService.findMyPage(member.getTenant(),beginDate,endDate,null,PayBill.Status.success,null,pageable);

            List<Integer> mapInteger = new ArrayList<Integer>();
            List<Date> mapWeeks = new ArrayList<Date>();
            for (Map<String, String> m : mapsTime) {
                Date beginDate2 = DateUtil.changeStrToDate(m.get("startTime"));
                Date endDate2 = DateUtil.changeStrToDate(m.get("endTime"));
                if (pageable.getPageNumber()==1) {
                List<PayBill> list = payBillService.findMyList(member.getTenant(), beginDate2, endDate2, null, PayBill.Status.success, null);
                mapInteger.add(list.size());
                } else {
                    mapInteger.add(0);
                }
                mapWeeks.add(beginDate2);

            }

        maps.put("list", PayBillModel.bindData(page.getContent()));
        maps.put("total",mapInteger);
        maps.put("weeks",mapWeeks);
        return DataBlock.success(maps,page,"执行成功");
    }
}