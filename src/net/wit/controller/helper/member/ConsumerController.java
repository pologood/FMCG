/**
 *====================================================
 * 文件名称: ConsumberController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.helper.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.MemberModel;
import net.wit.controller.app.model.MemberRankModel;
import net.wit.controller.app.model.TradeListModel;
import net.wit.controller.helper.model.MemberListModel;
import net.wit.entity.Consumer;
import net.wit.entity.Consumer.Status;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Employee;
import net.wit.entity.Member;
import net.wit.entity.Member.Gender;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.service.ConsumerService;
import net.wit.service.EmployeeService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.TradeService;

/**
 * @ClassName: ConsumberController
 * @Description: TODO(会员管理)
 * @author Administrator
 * @date 2015-12-21 上午11:31:34
 */
@Controller("helperMemberConsumerController")
@RequestMapping("/helper/member/consumer")
public class ConsumerController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "consumerServiceImpl")
	private ConsumerService consumerService;
	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;

	/**
	 * 我的会员
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public  String list(Long memberRank,String keyword, Status status,String gender,Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return "redirect:"+ERROR_VIEW;
		}
		Gender gen = null;
		if(StringUtils.isNotBlank(gender)){
			gen = "0".equals(gender)?Gender.male:Gender.female;
		}
				
		Tenant tenant = member.getTenant();
		model.addAttribute("status", status);
		model.addAttribute("statuss", Status.values());
		model.addAttribute("memberRank",memberRank);
		model.addAttribute("keyword",keyword);
		model.addAttribute("member", member);

		List<MemberListModel> models = new ArrayList<MemberListModel>();
		Page<Consumer> consumerPage=null;
		try {
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("memberRank", Filter.Operator.eq, memberRank));
			pageable.setFilters(filters);
			consumerPage  = consumerService.findPage(tenant, keyword, status,gen, pageable);
			for (Consumer consumer:consumerPage.getContent() ) {
				MemberListModel memberListModel = new MemberListModel();
				memberListModel.copyFrom(consumer.getId(),consumer.getMember(),tenant.getDefaultDeliveryCenter()==null?null:tenant.getDefaultDeliveryCenter().getLocation());
				MemberRankModel rank = new MemberRankModel();
				rank.copyFrom(consumer.getMemberRank());
				memberListModel.setMemberRank(rank);
				String role = "";
				if(tenant.getMember()!=consumer.getMember()) {
					Pageable pageablee = new Pageable();
					filters = new ArrayList<>();
					filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
					filters.add(new Filter("member", Filter.Operator.eq, member));
					pageablee.setFilters(filters);
					Page<Employee> emps = employeeService.findPage(pageablee);
					if (!emps.getContent().isEmpty()) {
						role = emps.getContent().get(0).getRole();
					}
				}else{
					role=",owner";
				}
				memberListModel.setStatus(consumer.getStatus());
				memberListModel.setRole(role);
				models.add(memberListModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:"+ERROR_VIEW;
		}
		Page<MemberListModel> page=new Page<MemberListModel>(models,consumerPage.getTotal(),pageable);
		model.addAttribute("page",page);
		model.addAttribute("pageActive",2);
		return "/helper/member/constomer/list";
	}

	/**
	 * 附近的人
	 */
	@RequestMapping(value = "/nearby", method = RequestMethod.GET)
	public String nearby(Pageable pageable,ModelMap model) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return "redirect:"+ERROR_VIEW;
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return "redirect:"+ERROR_VIEW;
		}
		List<MemberListModel> models = new ArrayList<MemberListModel>();
		DeliveryCenter delivery = tenant.getDefaultDeliveryCenter();
		if(delivery.getLocation()!=null){
			Page<Member> memberPage = memberService.findNearBy(delivery.getLocation(), pageable);
			for (Member member1:memberPage.getContent() ) {
				MemberListModel memberListModel = new MemberListModel();
				memberListModel.copyFrom(member1,tenant.getDefaultDeliveryCenter().getLocation());
				models.add(memberListModel);
			}
			model.addAttribute("models",models);
		}
		Page<MemberListModel> page=new Page<MemberListModel>(models,models.size(),pageable);
		model.addAttribute("page",page);
		model.addAttribute("pageActive",2);
		return "/helper/member/constomer/list";
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			consumerService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id,String status, ModelMap model,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return ERROR_VIEW;
		}

		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return ERROR_VIEW;
		}

		member = memberService.find(id);
		if (member == null) {
			return ERROR_VIEW;
		}

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
		filters.add(new Filter("member", Filter.Operator.eq, member));
		List<Consumer> consumers = consumerService.findList(10, filters, null);

		MemberModel models = new MemberModel();
		if (consumers.size()==0) {
			models.copyFrom(member);
		} else {
			Consumer consumer = consumers.get(0);
			models.copyConsumer(consumer);
			model.addAttribute("consumer", consumer.getId());
		}

		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("pageActive",2);
		model.addAttribute("models", models);
		model.addAttribute("status", status);
		model.addAttribute("orders",member.getOrders().size());
		model.addAttribute("amount",member.getAmount());

		Page<Trade> page = tradeService.findPage(member, pageable);
		model.addAttribute("trades", TradeListModel.bindData(page.getContent()));
		return "/helper/member/constomer/edit";
	}


	/**
	 * 更新
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String update(Long id, @RequestParam(defaultValue = "none") Status status, Long memberRankId, RedirectAttributes redirectAttributes) {
		Consumer consumer = consumerService.find(id);
		if (consumer == null) {
			return ERROR_VIEW;
		}
		consumer.setMemberRank(memberRankService.find(memberRankId));
		consumer.setStatus(status);
		consumerService.update(consumer);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?status=enable";
	}
	/**
	 * 查看收藏我店铺的用户
	 * @param pageable
	 * @param keyword
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/collect_list", method = RequestMethod.GET)
	public String collectList( Pageable pageable,String keyword,ModelMap model) {
		Member member=memberService.getCurrent();
		if(member==null){
			return "redirect:helper/login.jhtml";
		}
		Page<Member> page=new Page<Member>();
		List<Filter> filters = new ArrayList<Filter>();
		if(member.getTenant()!=null){
			filters.add(new Filter("username", Filter.Operator.like, keyword));
			pageable.setFilters(filters);
			page=memberService.findFavoritePage(member, pageable);
		}
		model.addAttribute("page",page);
		model.addAttribute("status","none");
		return "helper/member/constomer/collect_list";
	}
	
}
