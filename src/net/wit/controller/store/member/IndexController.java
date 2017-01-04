package net.wit.controller.store.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.constant.Constant;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.DateUtil;
import net.wit.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("storeMemberController")
@RequestMapping("/store/member")
public class IndexController extends net.wit.controller.store.BaseController {

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
	@Resource(name = "spReturnsServiceImpl")
	private SpReturnsService spReturnsService;
	@Resource(name="orderItemServiceImpl")
	private OrderItemService orderItemService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
    @Resource(name = "messageServiceImpl")
    private MessageService messageService;
	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;
	@Resource(name="areaServiceImpl")
	private AreaService areaService;
	@Resource(name="tenantRelationServiceImpl")
	private  TenantRelationService tenantRelationService;


	/**
	 * 主页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index( ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return "redirect:/store/login.jhtml";
		}
		try {
			roleService.initializeRole(tenant);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            model.addAttribute("spreturnApplyCount",spReturnsService.returnApplyCount(null,member.getTenant(),null, null));//退货申请单（维权订单）
			model.addAttribute("unshippedCount", tradeService.count(null, member.getTenant(), Order.QueryStatus.unshipped, null));//待发货
			//昨日订单
			Date endDate = sdf.parse(sdf.format(new Date().getTime()));
			Date beginDate = sdf.parse(sdf.format(new Date().getTime()-1 * 24 * 60 * 60 * 1000));
			List<Trade> yestodayTrade=tradeService.findTradeByTenant(tenant,beginDate, endDate,null,null,"order_total");
			model.addAttribute("yestoday_order_num",yestodayTrade.size());
			BigDecimal yestodayAmount=BigDecimal.ZERO;
			for(Trade trade:yestodayTrade){
				yestodayAmount= yestodayAmount.add(trade.getAmount());
			}
			model.addAttribute("yestodayAmount",yestodayAmount);

			String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
			Calendar calen = Calendar.getInstance();
			String[] new_weeks = new String[7];

			for(int i=1;i<=7;i++){
				String strTime = sdf.format(new Date().getTime()-i * 24 * 60 * 60 * 1000);
				calen.setTime(DateUtil.changeStrToDate(strTime));
				int week_index = calen.get(Calendar.DAY_OF_WEEK) - 1;
				if(week_index<0){
					week_index = 0;
				}
				new_weeks[i-1] = weeks[week_index];
			}
			model.addAttribute("new_weeks",new_weeks);
			model.addAttribute("sevenTradeList", tradeService.sevenDayTradingCount(null, member.getTenant(), null, null,null,null));//七天交易数据

			Pageable pageable = new Pageable(1,6);
			Page<TenantRelation> tenantRelationPage = tenantRelationService.findMyParent(member.getTenant(), TenantRelation.Status.success, pageable);
			List<Tenant> tenants = new ArrayList<Tenant>();
			for (TenantRelation tenantRelation : tenantRelationPage.getContent()) {
				tenants.add(tenantRelation.getParent());
			}
			model.addAttribute("unionStoreList",tenants);//联盟商店

			model.addAttribute("ourStoreList", deliveryCenterService.findourStoreList(member.getTenant()));//我们的门店

			DeliveryCenter deliveryCenter = deliveryCenterService.findDefault(member.getTenant());
			Location location =null;
			if(deliveryCenter !=null){
				location =deliveryCenter.getLocation();
			}
			model.addAttribute("nearStoreList", tenantService.openList(6,areaService.getCurrent(),null,null,null,location,new BigDecimal(5000),null,null));//附近门店

			Pageable pageable2 = new Pageable(1,6);
			Page<Map<String, Object>> page = orderItemService.openPageGroupBy(null, null, member.getTenant(), null,pageable2);
			List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
			for(Map<String, Object> m:page.getContent()){
				Map<String, Object> map=new HashMap<String,Object>();
				map.put("date", m.get("create_date"));
				map.put("time",m.get("date"));
				map.put("name", m.get("fullName"));
				map.put("sn", m.get("sn"));
				if(m.get("barcode")!=null){
					map.put("barcode", m.get("barcode"));
				}else{
					map.put("barcode", "--");
				}
				map.put("unit", m.get("unit"));
				map.put("shippedQuantity", m.get("shippedQuantity"));
				map.put("returnQuantity", m.get("returnQuantity"));
				map.put("price", m.get("price"));
				map.put("cost", m.get("cost"));
				map.put("totalPrice", m.get("totalPrice"));
				map.put("totalCost", m.get("totalCost"));
				map.put("reTotalPrice", m.get("reTotalPrice"));
				map.put("reTotalCost", m.get("reTotalCost"));
				maps.add(map);
			}
			model.addAttribute("mapsList", maps);//单品销量排行
            model.addAttribute("currentTime", DateUtil.getCurrentLinkTimeStr());//当前时间

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
			model.addAttribute("area",areaService.getCurrent());
			return "/store/member/index";
		} catch (Exception e) {
			//e.printStackTrace();
			return "/store/index";
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
		return "redirect:/store/login.jhtml";
	}

	/**
	 * 获取信息数量
	 */
	@RequestMapping(value = "/get_msg_num", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getmsg(Long parentId) {
		Map<String, Object> options = new HashMap<String, Object>();
		Member member=memberService.getCurrent();
		if(messageService.findMessageList(member,false,null, null,null)!=null){
			options.put("notice_num",messageService.findMessageList(member,false,null, null,null).size());;//系统消息
		}
		if(messageService.findMsgOrderList(member,false,null, null,null)!=null){
			options.put("msg_num",messageService.findMsgOrderList(member,false,null, null,null).size());//消息类型(订单、账单)
		}
		return options;
	}
}
