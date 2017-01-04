package net.wit.controller.pad;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.pad.model.TenantModel;
import net.wit.controller.pad.model.UnionModel;
import net.wit.entity.Equipment;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.service.EquipmentService;
import net.wit.service.MemberService;
import net.wit.service.UnionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商盟
 * Created by ruanx on 2016/11/10.
 */
@Controller("padUnionController")
@RequestMapping("/pad/union")
public class UnionController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "unionServiceImpl")
    private UnionService unionService;

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;
    //列表
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(String uuid, Pageable pageable) {
        Equipment equipment = equipmentService.findByUUID(uuid);
        Tenant tenant = equipment.getTenant();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        Page<Union> page = unionService.findPage(pageable);
        return DataBlock.success(UnionModel.bindData(page.getContent()),page, "执行成功");
    }

}
