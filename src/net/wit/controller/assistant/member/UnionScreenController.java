package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.AllUnionListModel;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.UnionListModel;
import net.wit.controller.assistant.model.UnionTenantScreenListModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
        Long otherToMecount = unionTenantService.count(equipment,null,UnionTenant.Status.confirmed);
        //申请记录数
        Long meToOtherPppplyCount = unionTenantService.count(null,tenant,UnionTenant.Status.freezed);
        Long otherToMeCount = unionTenantService.count(equipment,null,UnionTenant.Status.freezed);
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
        Page<Union> page = unionService.findPage(null,keyword,pageable);

        return DataBlock.success(UnionListModel.bindData(page.getContent()),page, "执行成功");
    }

    /**
     * 投放列表
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
            page = unionTenantService.findUnionTenantPage(null,tenant,UnionTenant.Status.confirmed,pageable);
        }else{
            //投放我的商家
            Equipment equipment =  equipmentService.findEquipment(tenant,null);
            if(equipment!=null){
                page = unionTenantService.findUnionTenantPage(equipment,null,UnionTenant.Status.confirmed,pageable);
            }else{
                return DataBlock.success(null,page,"执行成功");
            }

        }

        return DataBlock.success(UnionTenantScreenListModel.bindData(page.getContent()),page, "执行成功");
    }

}