package net.wit.controller.b2c.member;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Account;
import net.wit.entity.Deposit;
import net.wit.entity.Idcard;
import net.wit.entity.Member;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;
import net.wit.entity.Purchase;
import net.wit.entity.PurchaseReturns;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.entity.Trade;
import net.wit.service.AccountService;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
import net.wit.service.OrderItemService;
import net.wit.service.OrderService;
import net.wit.service.ProductService;
import net.wit.service.PurchaseReturnsService;
import net.wit.service.PurchaseService;
import net.wit.service.SpReturnsService;
import net.wit.service.SupplierService;
import net.wit.service.TenantRelationService;
import net.wit.service.TenantService;
import net.wit.service.TradeService;

/**
 * 供应商后台
 * Created by WangChao on 2016-4-12.
 */
@Controller("b2cMemberSupplierController")
@RequestMapping("b2c/member/supplier")
public class SupplierController {
    @Resource(name = "supplierServiceImpl")
    private SupplierService supplierService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;
    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "accountServiceImpl")
    private AccountService accountService;
    @Resource(name = "purchaseServiceImpl")
    private PurchaseService purchaseService;
    @Resource(name = "purchaseReturnsServiceImpl")
    private PurchaseReturnsService purchaseReturnsService;
    @Resource(name = "depositServiceImpl")
    private DepositService depositService;
    @Resource(name = "spReturnsServiceImpl")
    private SpReturnsService spReturnsService;
    @Resource(name = "orderServiceImpl")
    private OrderService orderService;
    @Resource(name = "orderItemServiceImpl")
    private OrderItemService orderItemService;
    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    /**
     * 销售统计
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/saleStatistics", method = RequestMethod.GET)
    public String saleStatistics(String dateRange, Long sellerId, String begin_date, String end_date, String keyWords, Model model, Pageable pageable) {
        pageable.setPageSize(10);
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
//        Date date = new Date();
//        Date t1 = new Date(date.getYear(), date.getMonth(), date.getDate());
//        Date t2 = new Date(t1.getTime());
//        if (begin_date == null) {
//            t2.setDate(t2.getDate() - 1);
//            beginDate = new Date(t2.getTime());
//        }
//        if (end_date == null) {
//            endDate = t1;
//        }
//        if (StringUtils.isBlank(dateRange)) {
//            dateRange = "昨天";
//        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, tenant));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        BigDecimal total_sale_amount = BigDecimal.ZERO;
        BigDecimal total_settlement_amount = BigDecimal.ZERO;
        BigDecimal sale_gross_profit = BigDecimal.ZERO;
        Tenant seller = null;
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
            page = supplierService.saleStatisticsList(tenant, seller, beginDate, endDate, keyWords, pageable);
            total_sale_amount = supplierService.totalSaleAmount(tenant, seller, beginDate, endDate, keyWords);
            total_settlement_amount = supplierService.totalSettlementAmount(tenant, seller, beginDate, endDate, keyWords);
            if (total_sale_amount == null) total_sale_amount = BigDecimal.ZERO;
            if (total_settlement_amount == null) total_settlement_amount = BigDecimal.ZERO;
            sale_gross_profit = total_sale_amount.subtract(total_settlement_amount);
        }
        model.addAttribute("page", page);
        model.addAttribute("total_sale_amount", total_sale_amount);
        model.addAttribute("sale_gross_profit", sale_gross_profit);
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("begin_date", beginDate);
        model.addAttribute("end_date", endDate);
        model.addAttribute("list", list);
        model.addAttribute("keyWords", keyWords);
        model.addAttribute("seller", seller);
        model.addAttribute("menu", "sales_statistics");
        return "b2c/member/supplier/sales_statistics";
    }
    
    /**
	 * 销售统计（针对导出功能）
	 */
	@RequestMapping(value = "/sale_total", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> saleTotal(Long sellerId, String begin_date, String end_date, String keyWords) { 
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, tenant));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
        Tenant seller = null;
        List<Map<String, Object>> trade = new ArrayList<Map<String, Object>>();
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
            trade = supplierService.saleTotal(tenant, seller, beginDate, endDate, keyWords);
        }
		return trade;
	}
    /**
     * 销售明细
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sale_detail", method = RequestMethod.GET)
    public String saleDetail(String dateRange, Long sellerId, String begin_date, String end_date, String keyWords, Model model, Pageable pageable) {
        pageable.setPageSize(10);
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, tenant));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        BigDecimal total_sale_amount = BigDecimal.ZERO;
        BigDecimal total_settlement_amount = BigDecimal.ZERO;
        BigDecimal sale_gross_profit = BigDecimal.ZERO;
        Tenant seller = null;
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
            page = supplierService.saleDetailList(tenant, seller, beginDate, endDate, keyWords, pageable);
            total_sale_amount = supplierService.totalSaleAmount(tenant, seller, beginDate, endDate, keyWords);
            total_settlement_amount = supplierService.totalSettlementAmount(tenant, seller, beginDate, endDate, keyWords);
            if (total_sale_amount == null) total_sale_amount = BigDecimal.ZERO;
            if (total_settlement_amount == null) total_settlement_amount = BigDecimal.ZERO;
            sale_gross_profit = total_sale_amount.subtract(total_settlement_amount);
        }
        model.addAttribute("page", page);
        model.addAttribute("total_sale_amount", total_sale_amount);
        model.addAttribute("sale_gross_profit", sale_gross_profit);
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("begin_date", beginDate);
        model.addAttribute("end_date", endDate);
        model.addAttribute("list", list);
        model.addAttribute("keyWords", keyWords);
        model.addAttribute("seller", seller);
        model.addAttribute("menu", "sale_detail");
        return "b2c/member/supplier/sale_detail";
    }
    /**
     * 销售明细（针对导出功能）
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/sale_details", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> saleDetails(Long sellerId, String begin_date, String end_date, String keyWords, Model model) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, tenant));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
       
        Tenant seller = null;
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
            maps = supplierService.saleDetail(tenant, seller, beginDate, endDate, keyWords);
        }
        for(int i=0;i<maps.size();i++){
        	maps.get(i).put("create_date",sdf.format(maps.get(i).get("create_date")));
        }
        return maps;
    }

    /**
     * 订单查询
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String order(String dateRange, Long sellerId, String begin_date, String end_date,String keyWords, Model model, Pageable pageable){
        pageable.setPageSize(10);
        Member member = memberService.getCurrent();

        if(member==null){
            return "redirect:/b2c/supplier/login.jhtml";
        }
        Tenant tenant = member.getTenant();

        if(tenant==null){
            return "redirect:/b2c/supplier/login.jhtml";
        }

        if(tenant.getTenantType()!= Tenant.TenantType.suppier){
            return "redirect:/b2c/supplier/login.jhtml";
        }

        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, tenant));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
        Tenant seller = null;
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
        }
        Page<Trade> page=tradeService.order(tenant,seller,beginDate,endDate,keyWords,pageable);
        BigDecimal total_sale_amount = supplierService.totalSaleAmount(tenant, seller, beginDate, endDate, null);
        BigDecimal total_settlement_amount = supplierService.totalSettlementAmount(tenant, seller, beginDate, endDate, null);
        BigDecimal sale_gross_profit = total_sale_amount.subtract(total_settlement_amount);
        model.addAttribute("page", page);
        model.addAttribute("total_sale_amount", total_sale_amount);
        model.addAttribute("sale_gross_profit", sale_gross_profit);
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("begin_date", beginDate);
        model.addAttribute("end_date", endDate);
        model.addAttribute("list", list);
        model.addAttribute("seller", seller);
        model.addAttribute("menu", "order");
        return "b2c/member/supplier/order";
    }

   

    /**
     * 查看账单
     *
     * @param date
     * @param model
     * @return
     */
    @RequestMapping(value = "/bill", method = RequestMethod.GET)
    public String bill(String start_date,String end_date, ModelMap model, Pageable pageable) {
        pageable.setPageSize(15);
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Member owner = tenant.getMember();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (StringUtils.isNotBlank(start_date)) {
            beginDate = sdf.parse(start_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        Page<Deposit> page = depositService.findPage(owner, beginDate, endDate, pageable,null);
        model.addAttribute("start_time",start_date);
        model.addAttribute("end_time",end_date);
        model.addAttribute("page", page);
        model.addAttribute("recharge", depositService.income(owner, Deposit.Type.recharge, beginDate, endDate,null));
        model.addAttribute("payment", depositService.outcome(owner, Deposit.Type.payment, beginDate, endDate,null));
        model.addAttribute("withdraw", depositService.outcome(owner, Deposit.Type.withdraw, beginDate, endDate,null));
        model.addAttribute("receipts", depositService.income(owner, Deposit.Type.receipts, beginDate, endDate,null));
        model.addAttribute("profit", depositService.income(owner, Deposit.Type.profit, beginDate, endDate,null));
        model.addAttribute("rebate", depositService.outcome(owner, Deposit.Type.rebate, beginDate, endDate,null));
        model.addAttribute("income", depositService.income(owner, Deposit.Type.income, beginDate, endDate,null));
        model.addAttribute("outcome", depositService.outcome(owner, Deposit.Type.outcome, beginDate, endDate,null));
        model.addAttribute("cashier", depositService.income(owner, Deposit.Type.cashier, beginDate, endDate,null));
        model.addAttribute("total_income", depositService.income(owner, null, beginDate, endDate,null));
        model.addAttribute("total_outcome", depositService.outcome(owner, null, beginDate, endDate,null));
        return "b2c/member/supplier/bill";
    }
    
    /**
     * 查看账单
     *
     * @param date
     * @param model
     * @return
     */
    @RequestMapping(value = "/bill_detail", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> billDetail(String start_date,String end_date, ModelMap model, Pageable pageable) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        Member owner = tenant.getMember();
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (StringUtils.isNotBlank(start_date)) {
            beginDate = sdf.parse(start_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        List<Deposit> deposits=depositService.findList(owner, beginDate, endDate,null);
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for(Deposit deposit:deposits){
        	Map<String, Object> map=new HashMap<String, Object>();
        	map.put("date", sdf.format(deposit.getCreateDate()));
        	if(deposit.getType()==Deposit.Type.cashier){
        		map.put("type","收款");
        	}else if(deposit.getType()==Deposit.Type.coupon){
        		map.put("type","红包");
        	}else if(deposit.getType()==Deposit.Type.couponuse){
        		map.put("type","红包");
        	}else if(deposit.getType()==Deposit.Type.income){
        		map.put("type","其他");
        	}else if(deposit.getType()==Deposit.Type.outcome){
        		map.put("type","其他");
        	}else if(deposit.getType()==Deposit.Type.payment){
        		map.put("type","购物");
        	}else if(deposit.getType()==Deposit.Type.profit){
        		map.put("type","分润");
        	}else if(deposit.getType()==Deposit.Type.rebate){
        		map.put("type","佣金");
        	}else if(deposit.getType()==Deposit.Type.recharge){
        		map.put("type","充值");
        	}else if(deposit.getType()==Deposit.Type.withdraw){
        		map.put("type","提现");
        	}else if(deposit.getType()==Deposit.Type.receipts){
        		map.put("type","货款");
        	}
        	map.put("memo", deposit.getMemo());
        	if(deposit.getType()==Deposit.Type.cashier||deposit.getType()==Deposit.Type.receipts
        			||deposit.getType()==Deposit.Type.recharge||deposit.getType()==Deposit.Type.profit
        			||deposit.getType()==Deposit.Type.income||deposit.getType()==Deposit.Type.coupon){
        		if(deposit.getCredit().compareTo(BigDecimal.ZERO)<0){
        			map.put("money", deposit.getCredit());
        		}else{
        			map.put("money", "+"+deposit.getCredit());
        		}
        	}else{
        		if(deposit.getDebit().compareTo(BigDecimal.ZERO)<0){
        			map.put("money", "+"+deposit.getDebit());
        		}else{
        			map.put("money", "-"+deposit.getDebit());
        		}
        	}
        	if(deposit.getStatus()!=null){
        		if(deposit.getStatus()==Deposit.Status.none){
	        		map.put("status", "处理中");
	        	}else{
	        		map.put("status", "已完成");
	        	}
        	}else{
        		map.put("status", "");
        	}
        	maps.add(map);
        }
        return maps;
    }

    /**
     * 账号设置
     *
     * @return
     */
    @RequestMapping(value = "/accountSettings", method = RequestMethod.GET)
    public String accountSettings(ModelMap model) {
        Member member = memberService.getCurrent();
        Idcard idcard = member.getIdcard();
        if (idcard == null) {
            idcard = new Idcard();
        }
        model.addAttribute("idcard", idcard);
        String captchaId = UUID.randomUUID().toString();
        model.addAttribute("captchaId", captchaId);
        model.addAttribute("menu", "account_settings");
        return "b2c/member/supplier/account_settings";
    }

    /**
     * 供货商认证页
     *
     * @return
     */
    @RequestMapping(value = "/supplierCertification", method = RequestMethod.GET)
    public String supplierCertification(ModelMap model) {
        model.addAttribute("menu", "supplier_certification");
        return "b2c/member/supplier/supplier_certification";
    }

    /**
     * 供货商认证-厂商授权页
     *
     * @return
     */
    @RequestMapping(value = "/supplierCertificationR", method = RequestMethod.GET)
    public String supplierCertificationR(ModelMap model) {
        model.addAttribute("menu", "supplier_certification");
        return "b2c/member/supplier/supplier_certification_r";
    }

    /**
     * 退货管理
     *
     * @param model
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/returnsManagement", method = RequestMethod.GET)
    public String returnsManagement(ModelMap model, String start_date, String end_date, String status, String date_range, String menu, Pageable pageable) {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(start_date)) {
            try {
                start_time = simpleDateFormat.parse(start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } 
        if (StringUtils.isNotBlank(end_date)) {
            try {
                end_time = simpleDateFormat.parse(end_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (status == null || status.equals("")) {
            status = "已受理";
        }
        if (menu == null) {
            menu = "returns_management";
        }
        Page<SpReturns> page = new Page<SpReturns>();
        if (status.equals("未受理")) {
            page = spReturnsService.findBySupplier(start_time, end_time, member.getTenant(), ReturnStatus.unconfirmed, pageable);
        } else if (status.equals("已受理")) {
            page = spReturnsService.findBySupplier(start_time, end_time, member.getTenant(), ReturnStatus.confirmed, pageable);
        } else if (status.equals("已认证")) {
            page = spReturnsService.findBySupplier(start_time, end_time, member.getTenant(), ReturnStatus.audited, pageable);
        } else if (status.equals("已完成")) {
            page = spReturnsService.findBySupplier(start_time, end_time, member.getTenant(), ReturnStatus.completed, pageable);
        } else if (status.equals("已取消")) {
            page = spReturnsService.findBySupplier(start_time, end_time, member.getTenant(), ReturnStatus.cancelled, pageable);
        }
        model.addAttribute("page", page);
        model.addAttribute("start_time", start_date);
        model.addAttribute("end_time", end_date);
        model.addAttribute("date_range", date_range);
        model.addAttribute("status", status);
        model.addAttribute("menu", menu);
        return "b2c/member/supplier/returns_management";
    }

    /**
     * 确认退货
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/confirm_return", method = RequestMethod.POST)
    public
    @ResponseBody
    Message confirm(Long id) {
        Member member = memberService.getCurrent();
        SpReturns spReturns = spReturnsService.find(id);
        if (spReturns != null && spReturns.getReturnStatus() == ReturnStatus.confirmed && member.getTenant().equals(spReturns.getSupplier())) {
            spReturns.setReturnStatus(ReturnStatus.audited);
            spReturnsService.update(spReturns);
            return Message.success("确认成功");
        }
        return Message.error("确认失败");
    }

    /**
     * 拒绝退货
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/refuse_return", method = RequestMethod.POST)
    public
    @ResponseBody
    Message cancel(Long id, String memo) {
        Member member = memberService.getCurrent();
        SpReturns spReturns = spReturnsService.find(id);
        if (spReturns != null && spReturns.getReturnStatus() == ReturnStatus.confirmed && member.getTenant().equals(spReturns.getSupplier())) {
            orderService.spRejected(spReturns.getTrade(), spReturns, member);
            spReturns.setMemo(memo);
            spReturnsService.update(spReturns);
            return Message.success("操作成功");
        }
        return Message.error("操作失败");
    }

    /**
     * 经营分析
     *
     * @param model
     * @param pageable
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/management_analyse", method = RequestMethod.GET)
    public String analyse(ModelMap model, String start_date, String end_date, Long sellerId, String search_content, String date_range, String menu, Pageable pageable) throws ParseException {
    	pageable.setPageSize(10);
    	Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;

        if (StringUtils.isNotBlank(start_date)) {
            start_time = simpleDateFormat.parse(start_date,new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            end_time = simpleDateFormat.parse(end_date,new ParsePosition(0));
        }
        if (menu == null || menu.equals("")) {
            menu = "management_analyse";
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, member.getTenant()));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
        Tenant seller = new Tenant();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        BigDecimal total_sale_amount = BigDecimal.ZERO;
        BigDecimal total_settlement_amount = BigDecimal.ZERO;
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
            page = supplierService.findManagementAnalysePage(member.getTenant(), start_time, end_time, seller, search_content, pageable);
            total_sale_amount = supplierService.totalSaleAmount(member.getTenant(), seller, start_time, end_time, search_content);
            total_settlement_amount = supplierService.totalSettlementAmount(member.getTenant(), seller, start_time, end_time, search_content);
        }
        model.addAttribute("page", page);
        model.addAttribute("member", member);
        model.addAttribute("seller", seller);
        model.addAttribute("list", list);
        model.addAttribute("start_time", start_date);
        model.addAttribute("end_time", end_date);
        model.addAttribute("date_range", date_range);
        model.addAttribute("search_content", search_content);
        model.addAttribute("menu", menu);
        model.addAttribute("total_sale_amount", total_sale_amount);
        model.addAttribute("total_settlement_amount", total_settlement_amount);
        return "b2c/member/supplier/management_analyse";
    }
    /**
     * 经营分析(导出功能)
     *
     * @param model
     * @param pageable
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/management_analyse_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> analyseManagement(String start_date, String end_date, Long sellerId, String keywords,  String menu) throws ParseException {
    	Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;

        if (StringUtils.isNotBlank(start_date)) {
            start_time = simpleDateFormat.parse(start_date,new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            end_time = simpleDateFormat.parse(end_date,new ParsePosition(0));
        }
        if (menu == null || menu.equals("")) {
            menu = "management_analyse";
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, member.getTenant()));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
        Tenant seller = new Tenant();
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
            maps = supplierService.managementAnalyse(member.getTenant(), start_time, end_time, seller, keywords);
         }
       
        return maps;
    }


    /**
     * 我的商品
     *
     * @param model
     * @param pageable
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/my_product", method = RequestMethod.GET)
    public String MyProduct(ModelMap model, String start_date, String end_date, Long sellerId, String search_content, String date_range, String menu, Pageable pageable) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;

        if (StringUtils.isNotBlank(start_date)) {
            start_time = simpleDateFormat.parse(start_date);
        }

        if (StringUtils.isNotBlank(end_date)) {
            end_time = simpleDateFormat.parse(end_date);
        }

        if (menu == null) {
            menu = "my_product";
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("parent", Filter.Operator.eq, member.getTenant()));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> list = tenantRelationService.findList(null, filters, null);
        Tenant seller = new Tenant();
        Page<Product> page = new Page<Product>();
        BigDecimal total_sale_amount = BigDecimal.ZERO;
        BigDecimal total_settlement_amount = BigDecimal.ZERO;
        if (list.size() > 0) {
            if (sellerId == null) {
                seller = list.get(0).getTenant();
            } else {
                seller = tenantService.find(sellerId);
            }
            page = productService.findSupplierPage(member.getTenant(), start_time, end_time, seller, search_content, pageable);
            total_sale_amount = supplierService.totalSaleAmount(member.getTenant(), seller, start_time, end_time, search_content);
            total_settlement_amount = supplierService.totalSettlementAmount(member.getTenant(), seller, start_time, end_time, search_content);
        }
        model.addAttribute("page", page);
        model.addAttribute("member", member);
        model.addAttribute("seller", seller);
        model.addAttribute("list", list);
        model.addAttribute("start_time", start_date);
        model.addAttribute("end_time", end_date);
        model.addAttribute("date_range", date_range);
        model.addAttribute("search_content", search_content);
        model.addAttribute("menu", menu);
        model.addAttribute("total_sale_amount", total_sale_amount);
        model.addAttribute("total_settlement_amount", total_settlement_amount);
        return "b2c/member/supplier/my_product";
    }

    /**
     * 修改库存
     *
     * @param id
     * @param stock
     * @return
     */
    @RequestMapping(value = "/save_stock", method = RequestMethod.POST)
    @ResponseBody
    public Message saveStock(Long id, Integer stock) {
        Product product = productService.find(id);
        product.setStock(stock);
        productService.update(product);
        return Message.success("修改成功");
    }

    /**
     * 订单结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/order_settle_account", method = RequestMethod.GET)
    public String settle_account(Pageable pageable, Long sellerId, String start_date, String end_date, String status, String date_range, String menu, ModelMap model,
                                 RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;
//        Date now_date = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate());
//        Date back_date = new Date(now_date.getTime() - 24 * 60 * 60 * 1000);

        if (StringUtils.isNotBlank(start_date)) {
            try {
                start_time = simpleDateFormat.parse(start_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            start_time = back_date;
        }

        if (StringUtils.isNotBlank(end_date)) {
            try {
                end_time = simpleDateFormat.parse(end_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            end_time = now_date;
        }
        if (status == null || status.equals("")) {
            status = "结算状态";
        }
        Boolean b_status = null;
        if ("已结算".equals(status) || status == "已结算") {
            b_status = true;
        } else if ("未结算".equals(status) || status == "未结算") {
            b_status = false;
        } else {
            b_status = null;
        }
//        if (date_range == null || date_range.equals("")) {
//            date_range = "昨天";
//        }
        if (menu == null) {
            menu = "order_settle_account";
        }
        model.addAttribute("page", orderItemService.findPage(b_status, start_time, end_time, member.getTenant(), pageable));
        model.addAttribute("member", member);
        model.addAttribute("start_time",start_date);
        model.addAttribute("end_time", end_date);
        model.addAttribute("date_range", date_range);
        model.addAttribute("status", status);
        model.addAttribute("menu", menu);
        model.addAttribute("pageActive", 2);
        return "/b2c/member/supplier/order_settle_account";
    }
    /**
     * 订单结算列表(导出)
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/order_settle_account_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> settleAccount(Long sellerId, String start_date, String end_date, String status, String menu, 
                                 RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(start_date)) {
            try {
                start_time = simpleDateFormat.parse(start_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (StringUtils.isNotBlank(end_date)) {
            try {
                end_time = simpleDateFormat.parse(end_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Boolean b_status = null;
        if ("已结算".equals(status) || status == "已结算") {
            b_status = true;
        } else if ("未结算".equals(status) || status == "未结算") {
            b_status = false;
        } else {
            b_status = null;
        }
         List<OrderItem> orderItems=orderItemService.orderSettle(b_status, start_time, end_time, member.getTenant());
         List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
         for(OrderItem orderItem:orderItems){
        	 Map<String, Object> map=new HashMap<String, Object>();
        	 map.put("sn", orderItem.getTrade().getOrder().getSn());
        	 map.put("date",simpleDateFormat.format(orderItem.getTrade().getCreateDate()));
        	 map.put("amount", orderItem.getTrade().getAmount());
        	 map.put("cost", orderItem.getTrade().getSettle());
        	 if(orderItem.getTrade().getSupplierDate()!=null){
        		 map.put("time",simpleDateFormat.format(orderItem.getTrade().getSupplierDate()));
        	 }else{
        		 map.put("time","");
        	 }
        	 if(orderItem.getTrade().getSuppliered()!=null){
        		 if(true==orderItem.getTrade().getSuppliered()){
	        		 map.put("status","已结算");
	        	 }else{
	        		 map.put("status","未结算");
	        	 }
        	 }else{
        		 map.put("status","");
        	 }
        	 maps.add(map);
         }
        return maps;
    }


    /**
     * 退货结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/return_settle_account", method = RequestMethod.GET)
    public String settle_accoun(Pageable pageable, String start_date, String end_date, String status, String date_range, String menu, ModelMap model, RedirectAttributes redirectAttributes) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;

        if (StringUtils.isNotBlank(start_date)) {
            start_time = simpleDateFormat.parse(start_date);
        }

        if (StringUtils.isNotBlank(end_date)) {
            end_time = simpleDateFormat.parse(end_date);
        }
        if (status == null || status.equals("")) {
            status = "结算状态";
        }
        Boolean b_status = null;
        if ("已结算".equals(status) || status == "已结算") {
            b_status = true;
        } else if ("未结算".equals(status) || status == "未结算") {
            b_status = false;
        } else {
            b_status = null;
        }

        if (menu == null) {
            menu = "return_settle_account";
        }
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("suppliered", Filter.Operator.eq, b_status));
        pageable.setFilters(filters);
        model.addAttribute("page", spReturnsService.findBySupplier(start_time, end_time, member.getTenant(), null, pageable));
        model.addAttribute("member", member);
        model.addAttribute("start_time",start_date);
        model.addAttribute("end_time", end_date);
        model.addAttribute("date_range", date_range);
        model.addAttribute("status", status);
        model.addAttribute("menu", menu);
        model.addAttribute("pageActive", 2);
        return "/b2c/member/supplier/return_settle_account";
    }
    /**
     * 退货结算列表（导出）
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/return_settle_account_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> settleAccoun(String start_date, String end_date, String status,String menu ) throws ParseException {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;
        if (StringUtils.isNotBlank(start_date)) {
            start_time = simpleDateFormat.parse(start_date);
        }
        if (StringUtils.isNotBlank(end_date)) {
            end_time = simpleDateFormat.parse(end_date);
        }
        Boolean b_status = null;
        if ("已结算".equals(status) || status == "已结算") {
            b_status = true;
        } else if ("未结算".equals(status) || status == "未结算") {
            b_status = false;
        } else {
            b_status = null;
        }
        
        List<SpReturns> spReturns=spReturnsService.returnSettle(start_time, end_time, member.getTenant(), b_status);
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for(SpReturns s:spReturns){
       	 Map<String, Object> map=new HashMap<String, Object>();
       	 map.put("sn", s.getSn());
       	 map.put("date",simpleDateFormat.format(s.getCreateDate()));
       	 map.put("amount", s.getAmount());
       	 map.put("cost", s.getSettle());
       	 if(s.getSupplierDate()!=null){
       		 map.put("time",simpleDateFormat.format(s.getSupplierDate()));
       	 }else{
       		 map.put("time","");
       	 }
       	 if(s.getSuppliered()!=null){
       		 if(true==s.getSuppliered()){
        		 map.put("status","已结算");
        	 }else{
        		 map.put("status","未结算");
        	 }
       	 }else{
       		 map.put("status","");
       	 }
       	 maps.add(map);
        }
       return maps;
    }
    
    /**
     * 提现结算列表
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/withdraw_cash_settle_account", method = RequestMethod.GET)
    public String withdraw_cash_settle_account(Pageable pageable, Long sellerId, String start_date, String end_date, String status, String date_range, String menu, ModelMap model,
                                 RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;

        if (StringUtils.isNotBlank(start_date)) {
            try {
                start_time = simpleDateFormat.parse(start_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (StringUtils.isNotBlank(end_date)) {
            try {
                end_time = simpleDateFormat.parse(end_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (status == null || status.equals("")) {
            status = "结算状态";
        }
        Account.Status b_status=null;
        if ("已结算".equals(status) || status == "已结算") {
        	b_status=Account.Status.success;
        } else if ("未结算".equals(status) || status == "未结算") {
        	b_status=Account.Status.none;
        } else {
            b_status = null;
        }
        if (menu == null) {
            menu = "withdraw_cash_settle_account";
        }
        List<Filter> filters=new ArrayList<Filter>();
        filters.add(new Filter("status", Filter.Operator.eq,b_status));
    	pageable.setFilters(filters);
        model.addAttribute("page",accountService.findByTenant(member.getTenant(), start_time, end_time, null, pageable));
        model.addAttribute("member", member);
        model.addAttribute("start_time", start_date);
        model.addAttribute("end_time", end_date);
        model.addAttribute("date_range", date_range);
        model.addAttribute("status", status);
        model.addAttribute("menu", menu);
        model.addAttribute("pageActive", 2);
        return "/b2c/member/supplier/withdraw_cash_settle_account";
    }
    /**
     * 提现结算列表(导出)
     *
     * @throws ParseException
     */
    @RequestMapping(value = "/withdraw_cash_settle_account_export", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> withdrawSettle(Pageable pageable, Long sellerId, String start_date, String end_date, String status, String date_range, String menu, ModelMap model,
                                 RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;

        if (StringUtils.isNotBlank(start_date)) {
            try {
                start_time = simpleDateFormat.parse(start_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (StringUtils.isNotBlank(end_date)) {
            try {
                end_time = simpleDateFormat.parse(end_date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (status == null || status.equals("")) {
            status = "结算状态";
        }
        Account.Status b_status=null;
        if ("已结算".equals(status) || status == "已结算") {
        	b_status=Account.Status.success;
        } else if ("未结算".equals(status) || status == "未结算") {
        	b_status=Account.Status.none;
        } else {
            b_status = null;
        }
       
        List<Account> accounts=accountService.withdrawSettle(member.getTenant(), start_time, end_time,b_status);
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for(Account s:accounts){
       	 Map<String, Object> map=new HashMap<String, Object>();
       	 if(s.getSn()==null){
       		 map.put("sn","");
       	 }else{
       		map.put("sn", s.getSn());
       	 }
       	 map.put("date",simpleDateFormat.format(s.getCreateDate()));
       	 map.put("amount", s.getAmount());
       	 if(s.getStatus()!=null){
       		 if(Account.Status.success==s.getStatus()){
        		 map.put("status","已结算");
        	 }else{
        		 map.put("status","未结算");
        	 }
       	 }else{
       		 map.put("status","");
       	 }
       	 if(s.getTenant()==null){
       		map.put("tenant", "");
       	 }else{
       		 map.put("tenant", s.getTenant().getName());
       	 }
       	 maps.add(map);
        }
       return maps;
    }

    /**
     * 采购进货单
     *
     * @return
     */
    @RequestMapping(value = "/purchase/list", method = RequestMethod.GET)
    public String purchase(Pageable pageable, String start_date, String end_date,String dateRange,String keyWords, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/common/error.jhtml";
        }
        getSupplierDetails(start_date,end_date,"purchase",dateRange,keyWords,model);
        Page<Purchase> purchases = purchaseService.openPage(pageable, null,member.getTenant(), null, null,keyWords);
        model.addAttribute("page", purchases);
        return "b2c/member/supplier/purchase/list";
    }

    @RequestMapping(value = "/purchase/edit/{id}", method = RequestMethod.GET)
    public String purchaseEdit(@PathVariable Long id, Purchase.Type type, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/common/error.jhtml";
        }

        Purchase purchase = purchaseService.find(id);
        model.addAttribute("purchase", purchase);
        model.addAttribute("type", type);
        return "b2c/member/supplier/purchase/edit";
    }

    @RequestMapping(value = "/purchase/print", method = RequestMethod.GET)
    public String purchasePrint(Long id,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/common/error.jhtml";
        }

        Purchase purchase = purchaseService.find(id);
        model.addAttribute("purchase",purchase);
        model.addAttribute("member",member);
        return "/b2c/member/supplier/purchase/print_purchase";
    }

    /**
     * 采购退货单
     *
     * @return
     */
    @RequestMapping(value = "/purchase/returns/list", method = RequestMethod.GET)
    public String purchaseReturns(Pageable pageable, String start_date, String end_date,String dateRange,String keyword, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/common/error.jhtml";
        }
        getSupplierDetails(start_date,end_date,"purchase_returns",dateRange,keyword,model);

        Page<PurchaseReturns> purchases = purchaseReturnsService.openPage(pageable, null, member.getTenant(), null, null,keyword);
        model.addAttribute("page", purchases);
        return "b2c/member/supplier/purchase_returns/list";
    }

    /**
     * 采购退货单明细查看
     *
     * @param id
     * @param type
     * @param model
     * @return
     */
    @RequestMapping(value = "/purchase/returns/edit/{id}", method = RequestMethod.GET)
    public String purchaseReturnsEdit(@PathVariable Long id, PurchaseReturns.Type type, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/common/error.jhtml";
        }

        PurchaseReturns purchaseReturns = purchaseReturnsService.find(id);
        model.addAttribute("purchaseReturns", purchaseReturns);
        model.addAttribute("type", type);
        return "b2c/member/supplier/purchase_returns/edit";
    }

    @RequestMapping(value = "/purchase/returns/print", method = RequestMethod.GET)
    public String purchaseReturnsPrint(Long id,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/common/error.jhtml";
        }

        Purchase purchase = purchaseService.find(id);
        model.addAttribute("purchase",purchase);
        model.addAttribute("member",member);
        return "/b2c/member/supplier/purchase_returns/print_purchase";
    }

    public void getSupplierDetails(String start_date, String end_date, String menu,String dateRange, String keyWords, ModelMap model) {
        if (dateRange == null || dateRange.equals("")) {
            dateRange = "昨天";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start_time = null;
        Date end_time = null;
        Date now_date = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate());
        Date back_date = new Date(now_date.getTime() - 24 * 60 * 60 * 1000);

        try {
            if (StringUtils.isNotBlank(start_date)) {
                start_time = simpleDateFormat.parse(start_date);
            } else {
                start_time = back_date;
            }

            if (StringUtils.isNotBlank(end_date)) {
                end_time = simpleDateFormat.parse(end_date);
            } else {
                end_time = now_date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        model.addAttribute("begin_date", start_time);
        model.addAttribute("end_date",end_time);
        model.addAttribute("menu", menu);
        model.addAttribute("keyWords", keyWords);
        model.addAttribute("dateRange", dateRange);
    }
}
