package net.wit.controller.helper.member;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.constant.Constant;
import net.wit.entity.Order;
import net.wit.entity.Tenant;
import net.wit.service.*;
import net.wit.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.wit.controller.helper.BaseController;
import net.wit.entity.Member;
import net.wit.util.DateUtil;

@Controller("helperMemberController")
@RequestMapping("/helper/member")
public class IndexController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "consumerServiceImpl")
	public ConsumerService cousumerService;

	@Resource(name = "depositServiceImpl")
	public DepositService depositService;

	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	/**
	 * 主页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index( ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();

		if (tenant == null) {
			return "redirect:/helper/member/tenant/status.jhtml";
		}
		if (tenant.getStatus().equals(Tenant.Status.fail) || tenant.getStatus().equals(Tenant.Status.none)) {
			return "redirect:/helper/member/tenant/status.jhtml";
		}

		try {

			roleService.initializeRole(tenant);

			model.addAttribute("unshippedCount", tradeService.count(null, member.getTenant(), Order.QueryStatus.unshipped, null));
			model.addAttribute("unpaidCount", tradeService.count(null, member.getTenant(), Order.QueryStatus.unpaid, null));
			model.addAttribute("shippedCount", tradeService.count(null, member.getTenant(), Order.QueryStatus.shipped, null));
			model.addAttribute("shippedApplyCount", tradeService.count(null, member.getTenant(), Order.QueryStatus.unrefunded, null));

			Map<String, String> memberCountMap = cousumerService.memberCounts(tenant.getId());
			model.addAttribute("memberCounts", memberCountMap.get("memberCounts"));

			Map<String, String> vip0countMap = cousumerService.vipcounts(tenant.getId(), "VIP0");
			model.addAttribute("VIP0", vip0countMap.get("VIP0"));

			Map<String, String> vip1countMap = cousumerService.vipcounts(tenant.getId(), "VIP1");
			model.addAttribute("VIP1", vip1countMap.get("VIP1"));

			Map<String, String> vip2countMap = cousumerService.vipcounts(tenant.getId(), "VIP2");
			model.addAttribute("VIP2", vip2countMap.get("VIP2"));

			Map<String, String> vip3countMap = cousumerService.vipcounts(tenant.getId(), "VIP3");
			model.addAttribute("VIP3", vip3countMap.get("VIP3"));

			Map<String, String> vip4countMap = cousumerService.vipcounts(tenant.getId(), "VIP4");
			model.addAttribute("VIP4", vip4countMap.get("VIP4"));

			Map<String, String> mencountsMap = cousumerService.ganercounts(tenant.getId(), 0);
			Map<String, String> womencountsMap = cousumerService.ganercounts(tenant.getId(), 1);

			Double iman = Double.parseDouble(mencountsMap.get("男"));
			Double iwoman = Double.parseDouble(womencountsMap.get("女"));

			String mens = "0";
			String womens = "0";

			if ((iman + iwoman) != 0) {
				Double iim = iman / (iman + iwoman) * 100;
				Double iiw = iwoman / (iman + iwoman) * 100;
				DecimalFormat    df   = new DecimalFormat("######0.00");   
				mens = df.format(iim);
				womens = df.format(iiw);
				
				if (iim % 1.0 == 0) {
					mens = String.valueOf(iim);
				}

				if (iiw % 1.0 == 0) {
					womens = String.valueOf(iiw);
				}
			}

			model.addAttribute("mens", mens);
			model.addAttribute("womens", womens);

			model.addAttribute("mencounts", mencountsMap.get("男"));
			model.addAttribute("womencounts", womencountsMap.get("女"));

			Calendar cal = Calendar.getInstance();
			BigDecimal income = depositService.income(member, null, DateUtil.setBeginDayOfMonth(cal),
					DateUtil.setLastDayOfMonth(cal),null);
			BigDecimal outcome = depositService.outcome(member, null, DateUtil.setBeginDayOfMonth(cal),
					DateUtil.setLastDayOfMonth(cal),null);

			double inorout = income.subtract(outcome).doubleValue();

			model.addAttribute("income", income);
			model.addAttribute("outcome", outcome);
			model.addAttribute("inorout", inorout);
			model.addAttribute("member", member);
			return "/helper/member/index";
		} catch (Exception e) {
			//e.printStackTrace();
			return "/helper/index";
		}
	}

	/**
	 * 注销
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		WebUtils.removeCookie(request, response, Member.USERNAME_COOKIE_NAME);
		WebUtils.removeCookie(request, response, Constant.Cookies.UC_TOKEN);
		return "redirect:/helper/index.jhtml";
	}
}
