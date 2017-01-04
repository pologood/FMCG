/**
 * ====================================================
 * 文件名称: ExtensionController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2016-4-29			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.controller.helper.member;

import net.wit.Message;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.ActivityDetailService;
import net.wit.service.ActivityRulesService;
import net.wit.service.MemberService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @ClassName: ExtensionController
 * @Description: TODO(推广管理)
 * @date 2016-4-29 上午9:51:34
 */
@Controller("helperMemberExtensionController")
@RequestMapping("/helper/member/extension")
public class ExtensionController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("brokerage", tenant.getGeneralize());
        data.put("agency", tenant.getAgency());
        data.put("isUnion", tenant.getIsUnion());
        model.addAttribute("brokerage", tenant.getGeneralize());//推广佣金
        model.addAttribute("agency", tenant.getAgency());//代理佣金
        model.addAttribute("isUnion", tenant.getIsUnion());//是否为商盟成员
        return "helper/member/extension/index";
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Message update(BigDecimal brokerage, BigDecimal agency, Boolean isUnion) {

        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error("无效会员");
        }
        Tenant tenant = member.getTenant();
        if (new BigDecimal("0.01").compareTo(brokerage) > 0) {
            return Message.error("推广佣金不能小于1%");
        }
        if (isUnion != null && isUnion) {
            if (tenant.getBrokerage().compareTo(agency) > 0) {
                return Message.error("联盟佣金不能小于" + tenant.getBrokerage().multiply(new BigDecimal(100)) + "%");
            }
        } else {
            isUnion = false;
        }
        tenant.setGeneralize(brokerage);
        tenant.setAgency(agency);
        tenant.setIsUnion(isUnion);
        tenantService.update(tenant);

        if(!activityDetailService.isActivity(null,tenant,activityRulesService.find(23L))){
            activityDetailService.addPoint(null,tenant,activityRulesService.find(23L));
        }

        if(!activityDetailService.isActivity(null,tenant,activityRulesService.find(24L))){
            activityDetailService.addPoint(null,tenant,activityRulesService.find(24L));
        }

        return Message.success("执行成功");

    }

}
