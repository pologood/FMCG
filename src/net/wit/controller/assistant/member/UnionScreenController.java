package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller("assistantMemberUnionScreenController")
@RequestMapping("/assistant/member/unionscreen")
/**
 * 屏联盟
 */
public class UnionScreenController extends BaseController {

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

    /**
     * 进入屏联盟
     */
    @RequestMapping(value = "/getInto", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock getInto() {
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
        Map map = new HashedMap();
        //我投放的商家数量
        Long meToOthercount = unionTenantService.count(null,tenant,UnionTenant.Status.confirmed);
        //其他人投放我的商家数量
        Equipment equipment =  equipmentService.findEquipment(tenant,null);
        Long otherToMecount = 0l;
        if(equipment!=null){
            otherToMecount = unionTenantService.count(equipment,null,UnionTenant.Status.confirmed);
        }

        //申请记录数
        Long meToOtherPppplyCount = unionTenantService.count(null,tenant,UnionTenant.Status.freezed);
        Long otherToMeCount = 0l;
        if(equipment!=null){
            otherToMeCount = unionTenantService.count(equipment,null,UnionTenant.Status.freezed);
        }

        map.put("meToOthercount",meToOthercount);
        map.put("otherToMecount",otherToMecount);
        map.put("appplyRecordCount",meToOtherPppplyCount+otherToMeCount);
        map.put("agency",tenant.getAgency().multiply(new BigDecimal(100)));
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 全部联盟
     */
    @RequestMapping(value = "/all_union", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock all_union(String keyword,Pageable pageable) {
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
        Page<Union> page = unionService.findPage(keyword,pageable);

        return DataBlock.success(UnionListModel.bindData(page.getContent()),page, "查询成功");
    }

    /**
     * 投放列表统计
     * 0:我投放的，1：投放我的
     */
    @RequestMapping(value = "/delivery", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock delivery(String type,Pageable pageable) {
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
        Page<UnionTenant> page = new Page<UnionTenant>();

        if(type.equals("0")){
            //我投放的商家
            page = unionTenantService.findUnionTenantPage(null,tenant,UnionTenant.Status.confirmed,null,pageable);
        }else{
            //投放我的商家
            Equipment equipment =  equipmentService.findEquipment(tenant,Equipment.Status.enabled);

            if(equipment==null){
                return DataBlock.error("您还没有开通任何设备");
            }
            page = unionTenantService.findUnionTenantPage(equipment,null,UnionTenant.Status.confirmed,null,pageable);
        }

        return DataBlock.success(UnionTenantScreenListModel.bindData(page.getContent()),page, "查询成功");
    }
    /**
     * 申请投放商家屏列表
     */
    @RequestMapping(value = "/screenList", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock screenList(String keyword,Long unionId,Boolean isRecommended ,Pageable pageable) {
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
        List<Tag> tags = null;
        if(isRecommended!=null){
          tags = tagService.findList(Tag.Type.tenant);
        }
        Page<Equipment> page = equipmentService.findPage(unionId,keyword,tags,Equipment.Status.enabled,pageable);
        List<UnionTenant> unionTenantList = unionTenantService.findUnionTenantList(null,tenant,null,null);
        List<ScreenEquipmentListModel> screenEquipmentListModel = ScreenEquipmentListModel.bindData(page.getContent());
        for(ScreenEquipmentListModel model:screenEquipmentListModel){
            model.setIsJoin(false);
            for(UnionTenant unionTenant:unionTenantList){
                if(unionTenant.getEquipment().getId().equals(model.getId())){
                    model.setIsJoin(true);
                    model.setStatus(unionTenant.getStatus());
                    if (unionTenant.getStatus() == UnionTenant.Status.freezed) {
                        model.setDesc("待同意");
                    }
                    if (unionTenant.getStatus() == UnionTenant.Status.confirmed) {
                        model.setDesc("已同意");
                    }
                    if (unionTenant.getStatus() == UnionTenant.Status.canceled) {
                        model.setDesc("已拒绝");
                    }
                    break;
                }
            }
        }

        return DataBlock.success(screenEquipmentListModel,page, "查询成功");
    }


    /**
     * 申请记录列表
     * 0:我投放的，1：投放我的
     */
    @RequestMapping(value = "/applyList", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock applyList(String type,UnionTenant.Status status, Pageable pageable) {
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
        Page<UnionTenant> page = new Page<UnionTenant>();

        if(type.equals("0")){
            //我投放的商家
            page = unionTenantService.findUnionTenantPage(null,tenant,status,null,pageable);
        }else{
            //投放我的商家
            Equipment equipment =  equipmentService.findEquipment(tenant,Equipment.Status.enabled);
            if(equipment==null){
                return DataBlock.error("您还没有开通任何设备");
            }
               page = unionTenantService.findUnionTenantPage(equipment,null,status,null,pageable);


        }

        return DataBlock.success(ScreenEquipmentListModel.bindScreenData(page.getContent(),type),page, "查询成功");
    }
    /**
     * 申请加入屏联盟商家
     */
    @RequestMapping(value = "/applyUnionTenant", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock applyUnionTenant(Long equipmentId) {
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
        if(member!=tenant.getMember()){
            return DataBlock.error("您不是店主，没有权限操作。");
        }
        Equipment equipment = equipmentService.find(equipmentId);
        if(equipment==null){
            return DataBlock.error("该设备不存在");
        }
        Union union = equipment.getUnions();
        if(union==null){
            return DataBlock.error("该商盟不存在");
        }

        return DataBlock.success(union.getPrice(), "申请成功");
    }

    /**
     * 生成支付单号
     */
    @RequestMapping(value = "/unionPayment", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock unionPayment(Long equipmentId) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Tenant tenant = member.getTenant();
            if (tenant==null) {
                return DataBlock.error(DataBlock.TENANT_INVAILD);
            }
            if(member!=tenant.getMember()){
                return DataBlock.error("您不是店主，没有权限操作");
            }
            Equipment equipment = equipmentService.find(equipmentId);
            if(equipment==null){
                return DataBlock.error("该设备不存在");
            }
            Union union = equipment.getUnions();
            if(union==null){
                return DataBlock.error("该商盟不存在");
            }
            UnionTenant unionTenant = new UnionTenant();
            List<UnionTenant> unionTenants = unionTenantService.findUnionTenantList(equipment,tenant,null,union);
               if(unionTenants.size()>0){
                UnionTenant.Status status =  unionTenants.get(0).getStatus();
                if(status == UnionTenant.Status.unconfirmed){
                    unionTenant = unionTenants.get(0);
                }
                if(status ==UnionTenant.Status.freezed){
                    return DataBlock.error("已申请正在处理中请耐心等待");
                }
                if(status ==UnionTenant.Status.confirmed){
                    return DataBlock.error("已加入屏联盟");
                }
                if(status ==UnionTenant.Status.canceled){
                    return DataBlock.error("该申请已被拒");
                }
            }else{
                   unionTenant.setUnion(union);
                   unionTenant.setTenant(tenant);
                   unionTenant.setEquipment(equipment);
                   unionTenant.setType(UnionTenant.Type.device);
                   unionTenant.setPrice(union.getPrice());
                   unionTenant.setStatus(UnionTenant.Status.unconfirmed);
                   Calendar curr = Calendar.getInstance();
                   curr.add(Calendar.YEAR, 1);
                   Date date = curr.getTime();
                   unionTenant.setExpire(date);
               }
                Payment payment = new Payment();
                payment.setMember(member);
                payment.setPayer(member.getName());
                payment.setMemo("屏联盟加盟费");
                payment.setSn(snService.generate(Sn.Type.payment));
                payment.setType(Payment.Type.union);
                payment.setMethod(Payment.Method.online);
                payment.setStatus(Payment.Status.wait);
                payment.setPaymentMethod("");
                payment.setFee(BigDecimal.ZERO);
                payment.setAmount(unionTenant.getPrice());
                payment.setPaymentPluginId("");
                payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
                unionTenantService.pay(unionTenant,payment);
            return DataBlock.success(payment.getSn(),"执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("提交支付异常");

        }

    }

    /**
     * 对我投放的进行撤销操作
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock cancel(Long unionTenantId) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Tenant tenant = member.getTenant();
            if (tenant==null) {
                return DataBlock.error(DataBlock.TENANT_INVAILD);
            }
            if(member!=tenant.getMember()){
                return DataBlock.error("您不是店主，没有权限操作");
            }
            UnionTenant unionTenant = unionTenantService.find(unionTenantId);
            if (unionTenant==null) {
                return DataBlock.error("无效的联盟ID");
            }
            if (UnionTenant.Status.unconfirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家没有付款无权操作撤销");
            }
            if (UnionTenant.Status.confirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家已经同意不能撤销");
            }
            if (UnionTenant.Status.canceled == unionTenant.getStatus()) {
                return DataBlock.error("商家已经拒绝不能撤销");
            }
            unionTenant.setStatus(UnionTenant.Status.canceled);
            unionTenantService.cancel(unionTenant);
            return DataBlock.success("success","撤销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("撤销异常");
        }

    }

    /**
     * 对我投放的进行重新申请操作
     */
    @RequestMapping(value = "/reApply", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock reApply(Long unionTenantId) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Tenant tenant = member.getTenant();
            if (tenant==null) {
                return DataBlock.error(DataBlock.TENANT_INVAILD);
            }
            if(member!=tenant.getMember()){
                return DataBlock.error("您不是店主，没有权限操作");
            }
            UnionTenant unionTenant = unionTenantService.find(unionTenantId);
            if (unionTenant==null) {
                return DataBlock.error("无效的联盟ID");
            }
            if (UnionTenant.Status.unconfirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家没有付款请先付款");
            }
            if (UnionTenant.Status.freezed == unionTenant.getStatus()) {
                return DataBlock.error("商家已经付款请等待商家同意");
            }
            if (UnionTenant.Status.confirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家已经同意不能重新申请");
            }

            unionTenant.setStatus(UnionTenant.Status.unconfirmed);
            unionTenantService.update(unionTenant);
            return DataBlock.success("success","重新申请成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("提交申请异常");
        }

    }

    /**
     * 对投放我的进行同意操作
     */
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock agree(Long unionTenantId) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Tenant tenant = member.getTenant();
            if (tenant==null) {
                return DataBlock.error(DataBlock.TENANT_INVAILD);
            }
            if(member!=tenant.getMember()){
                return DataBlock.error("您不是店主，没有权限操作");
            }
            UnionTenant unionTenant = unionTenantService.find(unionTenantId);
            if (unionTenant==null) {
                return DataBlock.error("无效的联盟ID");
            }
            if (UnionTenant.Status.unconfirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家没有付款不能进行同意操作");
            }
            if (UnionTenant.Status.confirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家已经同意");
            }
            if (UnionTenant.Status.canceled == unionTenant.getStatus()) {
                return DataBlock.error("商家已经拒绝请重新申请");
            }
            unionTenant.setStatus(UnionTenant.Status.confirmed);
            unionTenantService.update(unionTenant);
            return DataBlock.success("success","同意成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("同意异常");
        }

    }

    /**
     * 对我投放的进行拒绝操作
     */
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock refuse(Long unionTenantId) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Tenant tenant = member.getTenant();
            if (tenant==null) {
                return DataBlock.error(DataBlock.TENANT_INVAILD);
            }
            if(member!=tenant.getMember()){
                return DataBlock.error("您不是店主，没有权限操作");
            }
            UnionTenant unionTenant = unionTenantService.find(unionTenantId);
            if (unionTenant==null) {
                return DataBlock.error("无效的联盟ID");
            }
            if (UnionTenant.Status.unconfirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家还没有付款");
            }
            if (UnionTenant.Status.confirmed == unionTenant.getStatus()) {
                return DataBlock.error("商家已经同意不能进行拒绝操作");
            }
            if (UnionTenant.Status.canceled == unionTenant.getStatus()) {
                return DataBlock.error("商家已经拒绝");
            }
            unionTenant.setStatus(UnionTenant.Status.canceled);
            unionTenantService.cancel(unionTenant);
            return DataBlock.success("success","拒绝成功");
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("拒绝异常");
        }

    }

}