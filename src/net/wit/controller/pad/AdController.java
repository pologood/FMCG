package net.wit.controller.pad;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.pad.model.AdModel;
import net.wit.entity.AdPosition;
import net.wit.entity.Equipment;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.AdPositionService;
import net.wit.service.EquipmentService;
import net.wit.service.MemberService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/11/12.
 */
@Controller("padAdController")
@RequestMapping("/pad/ad")
public class AdController extends BaseController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;


    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;
    /**
     * 获取商家广告
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(String uuid) {
        Equipment equipment = equipmentService.findByUUID(uuid);
        Tenant tenant = equipment.getTenant();
        System.out.print(tenant.getId());
        if (tenant==null) {
            DataBlock.error("企业ID无效");
        }
        AdPosition adPosition = adPositionService.find(177L, tenant,null, null);

        return DataBlock.success(AdModel.bindData(adPosition.getAds()),"执行成功");

    }

}
