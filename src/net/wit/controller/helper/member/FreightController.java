/**
 *====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.helper.member;

import javax.annotation.Resource;

import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.entity.Freight;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

/**
 * @ClassName: TenantController
 * @Description: TODO(店铺管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("helperMembertfreightController")
@RequestMapping("/helper/member/freight")
public class FreightController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String view(ModelMap modelMap ) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return ERROR_VIEW;			
		} 
//		FreightModel model = new FreightModel();
//		Freight freight=tenant.getFreight();
		modelMap.addAttribute("tenant", tenant);
		modelMap.addAttribute("member", member);
		return "/helper/member/freight/edit";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Freight freight,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		Tenant tenant = member.getTenant();		
		tenant.setFreight(freight);
		tenantService.update(tenant);
		activityDetailService.addPoint(null, tenant, activityRulesService.find(6L));
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/helper/member/freight/edit.jhtml";
		
	}
	
}
