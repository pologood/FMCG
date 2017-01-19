package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.RedPacketListModel;
import net.wit.controller.app.model.RedPacketModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 店铺红包
 * Created by WangChao on 2016-10-11.
 */
@Controller("assistantMemberRedPacketController")
@RequestMapping("/assistant/member/redPacket")
public class RedPacketController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "rebateServiceImpl")
    private RebateService rebateService;

    @Resource(name = "couponNumberServiceImpl")
    private CouponNumberService couponNumberService;

    @Resource(name ="unionTenantServiceImpl")
    private UnionTenantService unionTenantService;

    /**
     * 红包列表
     *
     * @param status 状态
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(String status, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
//        couponService.refreshStatus(tenant);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        filters.add(new Filter("type", Filter.Operator.eq, Coupon.Type.tenantBonus));
        pageable.setFilters(filters);
        Page<Coupon> page = couponService.findPage(status, pageable);
        return DataBlock.success(RedPacketModel.bindData(page.getContent()), "执行成功");
    }

    /**
     * 添加红包
     *
     * @param amount       金额
     * @param count        数量
     * @param minimumPrice 红包使用门槛
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock add(BigDecimal amount, Integer count, BigDecimal minimumPrice, BigDecimal freezeAmount,HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Coupon coupon = new Coupon();
        coupon.setIsEnabled(true);
        coupon.setMinimumQuantity(null);
        coupon.setMaximumQuantity(null);
        coupon.setMaximumPrice(null);
        coupon.setMinimumPrice(minimumPrice);
        coupon.setAmount(amount);
        coupon.setCount(count);
        coupon.setName(amount + "元红包");
        coupon.setType(Coupon.Type.tenantBonus);
        coupon.setStatus(Coupon.Status.unconfirmed);
        coupon.setUsedCount(0);
        coupon.setSendCount(0);
        coupon.setPoint(0L);
        coupon.setEffectiveDays(1);
        coupon.setIsReceiveMore(true);
        coupon.setPrefix("c");
        coupon.setPriceExpression("price-".concat(coupon.getAmount().toString()));
        coupon.setTenant(tenant);
        coupon.setIsExchange(false);
        coupon.setReceiveTimes(1L);
        Date startDate=new Date();
        Date endDate=new Date(startDate.getYear(),startDate.getMonth(),startDate.getDate()+1,23,59,59);
        coupon.setStartDate(startDate);
        coupon.setEndDate(endDate);
        coupon.setIntroduction(coupon.getName());
        coupon.setFreezePrice(coupon.calcFreezePrice());
        couponService.save(coupon);
        couponService.upgrade(coupon);

        Map<String, Object> map = new HashMap<>();
        map.put("id", coupon.getId());
        map.put("sn", coupon.getPayment().getSn());
//        if(member.getTenant().getIsUnion()){
//            List<UnionTenant> unionTenants = unionTenantService.findUnionTenant(member.getTenant().getUnion(), null, null);
//            for(UnionTenant unionTenant:unionTenants){
//                List<Employee> employeeList = employeeService.findList(unionTenant.getTenant(),null);
//                for(Employee employee:employeeList){
//                    Message message = new Message();
//                    message.setType(Message.Type.activity);
//                    message.setCreateDate(new Date());
//                    message.setModifyDate(new Date());
//                    message.setContent(member.getTenant().getName()+"发了新红包快来看看吧！");
//                    message.setWay(Message.Way.tenant);
//                    message.setTitle("新红包");
//                    message.setIsDraft(false);
//                    message.setSenderRead(true);
//                    message.setReceiverRead(false);
//                    message.setSenderDelete(false);
//                    message.setReceiverDelete(false);
//                    message.setSender(member);
//                    message.setReceiver(employee.getMember());
//                    message.setIp(request.getRemoteAddr());
//                    messageService.save(message);
//                }
//            }
//        }
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 计算冻结金额
     * amount 金额
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock freeze(BigDecimal amount, Integer count) {
        Map<String, Object> map = new HashMap<>();
        Coupon coupon = new Coupon();
        coupon.setAmount(amount);
        coupon.setCount(count);
        coupon.setFreezePrice(coupon.calcFreezePrice());
        map.put("freezePrice", coupon.getFreezePrice());
        map.put("freezeAmount", coupon.getFreezeAmount());
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 红包添加成功后页面数据
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return DataBlock.error("红包不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("freezeAmount", coupon.getFreezeAmount());
        map.put("amount", coupon.getAmount());
        map.put("count", coupon.getCount());
        map.put("startDate", coupon.getStartDate());
        map.put("endDate", coupon.getEndDate());
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 刪除紅包
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock delete(Long id) {
        try {
            couponService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("刪除失败");
        }
        return DataBlock.success("success", "刪除成功");
    }

    /**
     * 关闭紅包
     */
        @RequestMapping(value = "/cancelled", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock cancelled(Long id) {
        try {
            Coupon coupon = couponService.find(id);
            coupon.setStatus(Coupon.Status.cancelled);
            couponService.update(coupon);
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("关闭失败");
        }
        return DataBlock.success("success", "成功关闭");
    }

    /**
     * 提醒设置
     *
     * @param id    红包Id
     * @param count 数量
     */
    @RequestMapping(value = "/remind", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock remind(Long id, Integer count) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return DataBlock.error("无效红包id");
        }
        coupon.setRemindQuantity(count);
        couponService.update(coupon);
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 提醒用户
     *
     * @param id    红包Id
     */
    @RequestMapping(value = "/remind/users", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock remindUsers(Long id) {
        CouponCode couponCode = couponCodeService.find(id);
        if (couponCode == null) {
            return DataBlock.error("无效红包id");
        }

        boolean _reminder = false;
        Setting setting = SettingUtils.get();
        if(couponCode.getReminderTime()!=null){

            Long reminder = (new Date().getTime()-couponCode.getReminderTime().getTime())/(3600*1000);

            Long l=new Long(setting.getBonusReminderTime()) ;

            if(reminder.compareTo(l)<=0){
                _reminder=true;
            }
        }
        if(_reminder){
            return DataBlock.error(setting.getBonusReminderTime()+"个小时内不可以反复提醒");
        }else {
            couponCode.setReminderTime(new Date());
            couponCodeService.update(couponCode);
            Message message= EntitySupport.createInitMessage(Message.Type.account,"您领取的红包即将到期，请尽快前往商家消费。",null,couponCode.getMember(),null);
            message.setCoupon(couponCode.getCoupon());
            message.setTemplete(Message.Templete.coupon);
            message.setWay(Message.Way.member);
            messageService.save(message);
        }
        return DataBlock.success("success", "您的消息已送达");
    }

    /**
     * 红包领用详情
     *
     * @param id 红包Id
     */
    @RequestMapping(value = "/receive/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock viewList(Long id,String type, Pageable pageable) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return DataBlock.error("无效红包id");
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("coupon", Filter.Operator.eq, coupon));

        if("0".equals(type)){
            filters.add(new Filter("isUsed", Filter.Operator.eq, false));
        }else if("1".equals(type)){
            filters.add(new Filter("isUsed", Filter.Operator.eq, true));
        }

        pageable.setFilters(filters);
        Page<CouponCode> couponCodes = couponCodeService.findPage(pageable);
        return DataBlock.success(RedPacketListModel.bindData(couponCodes.getContent()),couponCodes,"执行成功");
    }

    /**
     * 查看分润
     */
    @RequestMapping(value = "/profit", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock profit(Long id) {
        CouponCode couponCode = couponCodeService.find(id);
        if(couponCode==null){
            return DataBlock.error("无效编号");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("codeid", id);
        map.put("amount", couponCode.getCoupon().getAmount());
        List<Rebate> rebates = rebateService.openCodeRebate(couponCode,memberService.getCurrent());
        for(Rebate rebate:rebates){
            if(rebate.getType().equals(Rebate.Type.platform)){
                map.put("tenantName", rebate.getMember().getDisplayName());
                map.put("tenantProfit", rebate.getPercent());
                map.put("tenantAmount", rebate.getAmount());
            }else if(rebate.getType().equals(Rebate.Type.extension)){
                map.put("operateMember", rebate.getMember().getDisplayName());
                map.put("operateMemberProfit", rebate.getPercent());
                map.put("operateMemberAmount", rebate.getAmount());
            }else if(rebate.getType().equals(Rebate.Type.sale)){
                map.put("guideMemberTenant", rebate.getMember().getDisplayName());
                map.put("guideMemberProfit", rebate.getPercent());
                map.put("guideMemberAmount", rebate.getAmount());
            }else if(rebate.getType().equals(Rebate.Type.rebate)){
                map.put("meiber", rebate.getMember().getDisplayName());
                map.put("meiberProfit", rebate.getPercent());
                map.put("meiberAmount", rebate.getAmount());
            }
        }

        return DataBlock.success(map, "执行成功");
    }

    /**
     * 我的推广-推广领红包
     */
    @RequestMapping(value = "/extension/bouns", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock extensionBouns() {
        Member member = memberService.getCurrent();

        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        //已推广红包分润
        BigDecimal extensionAmount = rebateService.getAmount(member, Rebate.Type.extension);

        //已核销红包分润
        BigDecimal verificationAmount = rebateService.getAmount(member, Rebate.Type.sale);

        BigDecimal commissions = extensionAmount.add(verificationAmount);

        List<CouponNumber> couponNumbers = couponNumberService.findList(null,null,member,null);
        Integer useds=0,bounds=0;
        BigDecimal brokerage = BigDecimal.ZERO;
        if(couponNumbers!=null&&couponNumbers.size()>0){
            bounds=couponNumbers.size();
            for(CouponNumber couponNumber:couponNumbers){
                if(couponNumber.getMember()!=null){
                    useds=useds+1;
                    if(couponNumber.getBrokerage()!=null){
                        brokerage=brokerage.add(couponNumber.getBrokerage());
                    }
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("extensionCount", couponCodeService.findCoupon(member,"0").size());
        map.put("verificationCount", couponCodeService.findCoupon(member,"1").size());
        map.put("commissions", commissions);
        map.put("headimg", member.getHeadImg());
        map.put("useds", useds);//被领用数量
        map.put("bounds", bounds);//总共登记数量
        map.put("brokerage",brokerage);//获取佣金
        return DataBlock.success(map, "执行成功");
    }

    @RequestMapping(value = "/extension/bouns/type", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock extensionBouns(String type,Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        List<RedPacketListModel> redPacketListModels = null;
        Page<CouponCode> couponCodes = null;
        if(type.equals("0")){//推广
            couponCodes = couponCodeService.findCoupon(member,type,pageable);
            redPacketListModels = RedPacketListModel.bindData(couponCodes.getContent());
            for(RedPacketListModel redPacketListModel:redPacketListModels){
                CouponCode  couponCode = couponCodeService.find(redPacketListModel.getId());
                List<Rebate>  rebates = rebateService.findList(member,Rebate.Type.extension,couponCode);
                for(Rebate rebate:rebates){
                    if(redPacketListModel.getId().equals(rebate.getCouponCode().getId())){
                        redPacketListModel.setCommission(rebate.getAmount());
                    }
                }
            }
        }else if(type.equals("1")){//核销
             couponCodes = couponCodeService.findCoupon(member,type,pageable);
            redPacketListModels = RedPacketListModel.bindData(couponCodes.getContent());
             for(RedPacketListModel redPacketListModel:redPacketListModels){
                CouponCode couponCode = couponCodeService.find(redPacketListModel.getId());
                List<Rebate> rebates = rebateService.findList(member,Rebate.Type.sale,couponCode);
                for(Rebate rebate:rebates){
                    if(redPacketListModel.getId().equals(rebate.getCouponCode().getId())){
                        redPacketListModel.setCommission(rebate.getAmount());
                    }
                }

            }
        }

        return DataBlock.success(redPacketListModels,couponCodes,"执行成功");
    }


    /**
     * 核销页面
     */
    @RequestMapping(value = "/verification", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock verification(String code) {
        Member member=memberService.getCurrent();
        if(member==null){
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        CouponCode couponCode=couponCodeService.findByCode(code);
        if(couponCode==null){
            return DataBlock.error("无效红包");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("headimg",couponCode.getMember().getHeadImg());//使用者头像
        map.put("member", couponCode.getMember().getDisplayName());//使用者
        map.put("amount", couponCode.getCoupon().getAmount());//红包面额
        map.put("tenantName", couponCode.getCoupon().getTenant().getName());//推广店铺
        map.put("guidemember", couponCode.getGuideMember()==null?null:couponCode.getGuideMember().getDisplayName());//推广导购
        map.put("createDate", couponCode.getCreateDate());//领取时间
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 核销
     */
    @RequestMapping(value = "/write/off", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock writeOff(String code) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        CouponCode couponCode = couponCodeService.findByCode(code);
        if (couponCode == null) {
            return DataBlock.error("无效红包");
        }
        if(couponCode.getIsUsed()){
            return DataBlock.error("该红包已被使用");
        }
        if(couponCode.getCoupon().getEndDate().compareTo(new Date())<0){
            return DataBlock.error("红包已过期");
        }
        Tenant tenant=couponCode.getCoupon().getTenant();
        if(!Objects.equals(tenant.getMember().getId(), member.getId())){
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("tenant", Filter.Operator.eq,tenant));
            filters.add(new Filter("member", Filter.Operator.eq,member));
            List<Employee> employees=employeeService.findList(null,filters,null);
            if(employees.size()==0){
                return DataBlock.error("无权限操作");
            }
        }
        couponService.complete(couponCode,member);
        Map<String, Object> map = new HashMap<>();
        map.put("headimg",couponCode.getMember().getHeadImg());//使用者头像
        map.put("amount", couponCode.getCoupon().getAmount());//红包面额
        map.put("tenantName", couponCode.getCoupon().getTenant().getName());//推广店铺
        map.put("guidemember", couponCode.getGuideMember()==null?null:couponCode.getGuideMember().getDisplayName());//推广导购
        map.put("createDate", couponCode.getCreateDate());//领取时间
        map.put("operateDate", couponCode.getOperateDate());//核销时间
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 推广红包二维码
     */
    @RequestMapping(value = "/qrcode/json", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock qrcodeJson(HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.getCurrent();

        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

            String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/redPacket/index.jhtml?extension=" + (member != null ? member.getUsername() : ""));

            return DataBlock.success(url, "获取成功");
        } catch (Exception e) {
            return DataBlock.error("获取二维码失败");
        }
    }

    /**
     * 分享链接
     */
    @RequestMapping(value = "/code/json",method = RequestMethod.GET)
    @ResponseBody
    public DataBlock codeJson(Long id, HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(net.wit.controller.assistant.model.DataBlock.SESSION_INVAILD);
        }
        CouponCode couponCode = couponCodeService.find(id);
        if (couponCode==null) {
            return DataBlock.error("无效设备号");
        }
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            if (!member.getTenant().getStatus().equals(Tenant.Status.success)) {
                return DataBlock.error("没有开通不能分享");
            }
            String url =  MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/becomevip.jhtml?extension=" + (member != null ? member.getUsername() : ""));
            Map<String,Object> map = new HashMap<>();
            map.put("url",url);
            map.put("text",couponCode.getCoupon().getName());
            return DataBlock.success(map,"获取成功");
        } catch (Exception e) {
            return  DataBlock.error("获取失败");
        }
    }

}
