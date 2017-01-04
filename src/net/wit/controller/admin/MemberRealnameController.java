package net.wit.controller.admin;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Idcard;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.*;

import net.wit.support.EntitySupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 实名认证
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminMemberRealnameController")
@RequestMapping("/admin/member_realname")
public class MemberRealnameController extends BaseController {

	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(defaultValue = "wait") AuthStatus authStatus, Pageable pageable,String searchValue, ModelMap model) {
		pageable.setSearchProperty("username");
		pageable.setSearchValue(searchValue);
		model.addAttribute("authStatus", authStatus);
		model.addAttribute("page", idcardService.findMemberPage(authStatus, pageable));
		return "/admin/mamber_realname/list";
	}

	/**
	 * 详细信息
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
		Member member = memberService.find(id);
		if (member == null || member.getIdcard() == null) {
			addFlashMessage(redirectAttributes, Message.error("该商户未提交身份认证信息！"));
			return "redirect:list.jhtml";
		}
		model.addAttribute("member", member);
		return "/admin/mamber_realname/edit";
	}

	/**
	 * 认证
	 */
	@RequestMapping(value = "/certification", method = RequestMethod.POST)
	public String certification(Long id, String memo, AuthStatus status, RedirectAttributes redirectAttributes) {
		Member member = memberService.find(id);
		Idcard idcard = member.getIdcard();
		if (idcard == null) {
			addFlashMessage(redirectAttributes, Message.error("参数错误"));
		}
		//认证消息
		if(!status.equals(idcard.getAuthStatus())&&
				(status==Idcard.AuthStatus.success||status==Idcard.AuthStatus.fail)){
			//认证状态发生改变
			String 	authMessage=((status==Idcard.AuthStatus.success)?"通过":"不通过");
			String _memo="审核原因：";
			if (memo!=null&&memo!=""){
				_memo+=memo;
			}else{
				_memo+="无";
			}


			net.wit.entity.Message message = EntitySupport.createInitMessage(net.wit.entity.Message.Type.message,
					"您好，您提交的实名认证已审核，审核结果："+authMessage+","+_memo+"。",
					null,member,null);
			message.setTitle("实名认证");
			message.setOrderStatus(authMessage);
			message.setWay(net.wit.entity.Message.Way.member);
			messageService.save(message);
		}
		if(status==AuthStatus.success){
			member.setName(idcard.getName());
		}
		idcard.setAuthStatus(status);
		idcard.setMemo(memo);
		idcardService.update(idcard);
		memberService.update(member);
		/**
		if (Idcard.AuthStatus.fail == status) {
			Tenant tenant = member.getTenant();
			if (tenant != null) {
				tenant.setStatus(Tenant.Status.fail);
				tenantService.save(tenant);
			}
		}*/

		if(status==AuthStatus.success){
			Tenant tenant = member.getTenant();
			if(tenant!=null){
				if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(11L))){
					activityDetailService.addPoint(null,tenant,activityRulesService.find(11L));
				}

				if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(12L))){
					activityDetailService.addPoint(null,tenant,activityRulesService.find(12L));
				}

				if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(13L))){
					activityDetailService.addPoint(null,tenant,activityRulesService.find(13L));
				}
			}
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
}
