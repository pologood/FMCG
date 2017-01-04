package net.wit.controller.app.member;

import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Services;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.ServicesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by My-PC on 16/06/01.
 */
@Controller("appMemberServicesController")
@RequestMapping("/app/member/services")
public class ServicesController extends BaseController {

    @Resource(name = "servicesServiceImpl")
    private ServicesService servicesService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock submit(Services.Status status) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            DataBlock.error(DataBlock.TENANT_INVAILD);
        }

        try {
            boolean b = servicesService.checkServicesType(tenant, status);
            if (!b) {
                Services services = new Services();
                services.setTenant(tenant);
                services.setStatus(status);
                services.setState(Services.State.none);
                services.setType(Services.Type.wait);
                servicesService.save(services);
                return DataBlock.success("success", "申请成功");
            }
            return DataBlock.success("success","还在审核中，请耐心等待！");
        } catch (Exception e) {
            return DataBlock.error("不好意思，申请失败了！");
        }
    }

    /**
     * 检查是否申请成功
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock check(Services.Status status) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            DataBlock.error(DataBlock.TENANT_INVAILD);
        }
//        boolean b = servicesService.checkServicesType(tenant, status);
//        if (!b) {
//            return DataBlock.error("您还没有提交申请，请先提交申请！");
//        }
        Map<String,String> data= servicesService.getType(tenant,status);
        return DataBlock.success(data, "申请....");
    }
}
