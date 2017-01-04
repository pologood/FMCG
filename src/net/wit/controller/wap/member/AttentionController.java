/**
 *====================================================
 * 文件名称: AttentionController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2015年1月25日			Chenlf(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.wap.member;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.controller.wap.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.TenantService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName: AttentionController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Chenlf
 * @date 2015年1月25日 下午12:11:09
 */
@Controller("wapMemberAttentionController")
@RequestMapping("/wap/member/attention")
public class AttentionController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 关注商家
	 */
	@RequestMapping(value = "/tenant/{id}", method = RequestMethod.GET)
	public String attentionTenant(@PathVariable Long id, HttpServletRequest request, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = tenantService.find(id);
		boolean b = member.getFavoriteTenants().contains(tenant);
		if (b) {
			model.addAttribute("notifyMessage", "您已关注过该商家！");
		} else {
			member.getFavoriteTenants().add(tenant);
			memberService.update(member);
			model.addAttribute("notifyMessage", "关注成功！");
		}
		return "/wap/member/attention/notify";
	}

	/**
	 * 关注会员
	 */
	@RequestMapping(value = "/member", method = RequestMethod.GET)
	public String attentionMember(String username, HttpServletRequest request, ModelMap model) {
		Member current = memberService.getCurrent();
		Member member = memberService.findByUsername(username);
		Tenant tenant = member.getTenant();
		StringBuffer notifyMessage = new StringBuffer();
		if (tenant != null) {
			boolean b = current.getFavoriteTenants().contains(tenant);
			if (!b) {
				current.getFavoriteTenants().add(tenant);
				notifyMessage.append("关注会员商家成功！");
			}
		}
		if (current.getMember() == null) {
			current.setMember(member);
			notifyMessage.append(" 关注会员成功！");
		} else {
			notifyMessage.append(" 已经关注过其他会员！");
		}
		memberService.update(current);
		model.addAttribute("notifyMessage", notifyMessage.toString());
		return "/wap/member/attention/notify";
	}

}
