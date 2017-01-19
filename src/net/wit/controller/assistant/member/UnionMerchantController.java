package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.model.*;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.*;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.*;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller("assistantMemberUnionMerchantController")
@RequestMapping("/assistant/member/unionmerchant")
/**
 * 挑货商盟
 */
public class UnionMerchantController extends BaseController {

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
    @Resource(name = "snServiceImpl")
    private SnService snService;
    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;


    /**
     * 我的商盟
     */
    @RequestMapping(value = "/my_union", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock my_union() {
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
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("status", Filter.Operator.eq, UnionTenant.Status.confirmed));
        filters.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.tenant));
        List<UnionTenant> unionTenants = unionTenantService.findUnionTenant(null, tenant, filters);
      /*  if(unionTenants.size()>0){
            if((unionTenants.get(0).getUnion().getBrokerage().compareTo(tenant.getAgency()))>0){
                tenant.setAgency(unionTenants.get(0).getUnion().getBrokerage());
                tenant.setModifyDate(new Date());
                tenantService.update(tenant);
            }
        }*/

        return DataBlock.success(UnionTenantListModel.bindData(unionTenants, tenant), "查询成功");
    }
    /**
     * 修改我的佣金
     */
    @RequestMapping(value = "/myBrokerage", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock myBrokerage(Long unionId,BigDecimal brokerage) {
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
        Union union=unionService.find(unionId);
        if(union==null){
            return DataBlock.error("该商盟不存在");
        }
        if(brokerage.compareTo(union.getBrokerage())<1){
            return DataBlock.error("我的佣金比例不能小于联盟佣金");
        }
        tenant.setAgency(brokerage);
        tenant.setModifyDate(new Date());
        tenantService.update(tenant);
        return DataBlock.success("success", "修改成功");
    }
    /**
     * 全部商盟
     * status:加入：joined，未加入：notJoined,全部：null
     */
    @RequestMapping(value = "/all_union", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock all_union(String status,Pageable pageable) {
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
        List<Union> unions = unionService.findList(null,null,null);

        return DataBlock.success(AllUnionListModel.bindData(unions,tenant,status), "查询成功");
    }

    /**
     * 加入挑货联盟
     */
    @RequestMapping(value = "/create_unionTenant", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock createUnionTenant(Long unionId) {
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
        Union union=unionService.find(unionId);
        if(union==null){
            return DataBlock.error("该商盟不存在");
        }
        Map<String,Object> map = new HashMap<String,Object>();
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.tenant));
        List<UnionTenant> unionTenants = unionTenants = unionTenantService.findUnionTenant(union ,tenant,filters);
            if(unionTenants.size()>0){
                unionTenants.get(0).setStatus(UnionTenant.Status.unconfirmed);
                unionTenants.get(0).setUnion(union);
                unionTenants.get(0).setPrice(union.getPrice());
                unionTenantService.update(unionTenants.get(0));
                map.put("unionTenantId",unionTenants.get(0).getId());
                map.put("price",unionTenants.get(0).getPrice());
                map.put("unionBrokerage",unionTenants.get(0).getUnion().getBrokerage());
                map.put("tenentBrokerage",tenant.getBrokerage());
                return DataBlock.success(map,"加入成功");
            }
        UnionTenant unionTenant= null;
        try {
            unionTenant = new UnionTenant();
            unionTenant.setUnion(union);
            unionTenant.setTenant(tenant);
            unionTenant.setType(UnionTenant.Type.tenant);
            unionTenant.setEquipment(null);
            unionTenant.setPrice(union.getPrice());
            unionTenant.setStatus(UnionTenant.Status.unconfirmed);
            Calendar curr = Calendar.getInstance();
            curr.add(Calendar.YEAR, 1);
            Date date=curr.getTime();
            unionTenant.setExpire(date);
            unionTenantService.save(unionTenant);
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("申请失败");
        }

        map.put("unionTenantId",unionTenant.getId());
        map.put("price",unionTenant.getPrice());
        map.put("unionBrokerage",unionTenant.getUnion().getBrokerage());
        map.put("tenentBrokerage",tenant.getBrokerage());
        return DataBlock.success(map, "加入成功");
    }
    /**
     * 生成支付单号
     */
    @RequestMapping(value = "/unionPayment", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock submit(Long unionTenantId) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return DataBlock.error(DataBlock.SESSION_INVAILD);
            }
            Tenant tenant = member.getTenant();
            if (tenant==null) {
                return DataBlock.error(DataBlock.TENANT_INVAILD);
            }
            UnionTenant unionTenant = unionTenantService.find(unionTenantId);
            if (unionTenant==null) {
                return DataBlock.error("无效的联盟ID");
            }
            Payment payment = new Payment();
            payment.setMember(member);
            payment.setPayer(member.getName());
            payment.setMemo("联盟加盟费");
            payment.setSn(snService.generate(Sn.Type.payment));
            payment.setType(Payment.Type.union);
            payment.setMethod(Payment.Method.online);
            payment.setStatus(Payment.Status.wait);
            payment.setPaymentMethod("");
            payment.setFee(BigDecimal.ZERO);
            payment.setAmount(unionTenant.getPrice());
            payment.setPaymentPluginId("");
            payment.setExpire(DateUtils.addMinutes(new Date(), 3600));
            payment.setUnionTenant(unionTenant);

            unionTenantService.pay(unionTenant,payment);
            return DataBlock.success(payment.getSn(),"提交成功");
    } catch (Exception e) {
             e.printStackTrace();
            return DataBlock.error("提交支付异常");

    }

    }
    /**
     * 支付完成后调用该方法(已废除但不能删除该方法)
     */
    /*@RequestMapping(value = "/update_unionTenant", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock updateUnionTenant(Long unionTenantId) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        UnionTenant unionTenant = unionTenantService.find(unionTenantId);
        unionTenant.setStatus(UnionTenant.Status.confirmed);
        Union union=unionTenant.getUnion();
        if (union==null){
            return DataBlock.error("找不到该商盟");
        }
        Tenant tenant=unionTenant.getTenant();
        if (tenant==null){
            return DataBlock.error("店铺不存在");
        }
        if(tenant.getIsUnion()==false){
            union.setTenantNumber(union.getTenantNumber()+1);
            tenant.setIsUnion(true);
            Calendar curr = Calendar.getInstance();
            curr.add(Calendar.YEAR, 1);
            Date date=curr.getTime();
            unionTenant.setExpire(date);
        }
        tenant.setUnion(union);
        if(tenant.getAgency().compareTo(union.getBrokerage())<0){
            tenant.setAgency(union.getBrokerage());//店铺的联盟佣金
        }
        tenantService.update(tenant);
        unionService.update(union);
        unionTenantService.update(unionTenant);
        return DataBlock.success("success","加入成功");
    }*/

    /**
     * 退出商盟
     */
    @RequestMapping(value = "/return_union", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock returnUnion(Long unionId) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        if (member != tenant.getMember()) {
            return DataBlock.error("您不是店主，没有权限操作。");
        }
        Union union = unionService.find(unionId);
        if (union == null) {
            return DataBlock.error("该商盟不存在");
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("status", Filter.Operator.eq, UnionTenant.Status.confirmed));
        List<UnionTenant> unionTenants = unionTenantService.findUnionTenant(union, tenant, filters);
        if (unionTenants.size() > 0) {
            if (union.getTenantNumber() > 0) {
                union.setTenantNumber(union.getTenantNumber() - 1);
            }
            unionTenants.get(0).setStatus(UnionTenant.Status.canceled);
            unionTenantService.update(unionTenants.get(0));
            unionService.update(union);
            return DataBlock.success("success", "退出成功");
        } else {
            return DataBlock.error("error", "退出失败");
        }

    }

    /**
     * 查看推广信息
     */
    @RequestMapping(value = "/look_extend", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock lookExtend(Long tenantId) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Map<String,Object> map=new HashMap<String,Object>();
        List<Filter> filters=new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        filters.add(new Filter("tenant", Filter.Operator.eq,tenant));
        List<ExtendCatalog> extendCatalogs = extendCatalogService.findList(null,filters,null);
        if(extendCatalogs.size()!=0){
            for(ExtendCatalog extendCatalog:extendCatalogs){
                    map.put("i_to_he_valume",extendCatalog.getVolume());
                    map.put("i_to_he_amount",extendCatalog.getAmount());
                    map.put("i_to_he_total",extendCatalog.getSalsePrice());
            }
        }else{
            map.put("i_to_he_valume",0);
            map.put("i_to_he_amount",0);
            map.put("i_to_he_total",0);
        }


        Set<Member> members=tenant.getMembers();
        Long heVlume=0l;
        BigDecimal heAmount=BigDecimal.ZERO;
        BigDecimal hePrice=BigDecimal.ZERO;
        List<Filter> filter=new ArrayList<Filter>();
        filter.add(new Filter("tenant", Filter.Operator.eq,tenant));
        if(members.size()>0){
            for(Member meb:members){
                filter.add(new Filter("member", Filter.Operator.eq,meb));
                List<ExtendCatalog> extendCatalogs2 = extendCatalogService.findList(null,filter,null);
                for(ExtendCatalog extendCatalog:extendCatalogs2){
                    heAmount=heAmount.add(extendCatalog.getAmount());
                    heVlume=heVlume+extendCatalog.getVolume();
                    hePrice=hePrice.add(extendCatalog.getSalsePrice());
                }
            }
        }
        map.put("he_to_i_valume",heVlume);
        map.put("he_to_i_amount",heAmount);
        map.put("he_to_i_total",hePrice);
        return DataBlock.success(map, "执行成功");
    }
}