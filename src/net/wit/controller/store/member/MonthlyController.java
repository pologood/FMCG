package net.wit.controller.store.member;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.helper.BaseController;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller("storeMemberMonthlyController")
@RequestMapping("/store/member/monthly")
public class MonthlyController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "monthlyServiceImpl")
    private MonthlyService monthlyService;


	/**
	 * 月结帐表
	 */
	@RequestMapping(value = "/monthly_list", method = RequestMethod.GET)
	public String adAdd(Long tenantId,String begin_date, String end_date, ModelMap model,Pageable pageable) throws ParseException {
		Member member = memberService.getCurrent();
        Tenant tenant=member.getTenant();
        List<Trade> trades=tradeService.findUnshippedList(tenant);
        BigDecimal amount=BigDecimal.ZERO;
        if(trades.size()>0){
            for (Trade trade:trades){
                amount=amount.add(trade.getAmount());
            }
        }
        List<Product> products=productService.openList(null,tenant,null,true,null,null,null,null,null,null, null, null, null,null,null,null);
        Integer stock=0;
        Integer lockStock=0;
        for(Product product:products){
            stock=stock+product.getStock();
            lockStock=lockStock+product.getAllocatedStock();
        }
        Date beginDate = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(begin_date)) {
            beginDate = sdf.parse(begin_date, new ParsePosition(0));
        }
        if (StringUtils.isNotBlank(end_date)) {
            endDate = sdf.parse(end_date, new ParsePosition(0));
        }
        Page<Monthly> page=monthlyService.findMonthlyPage(tenant,beginDate,endDate,pageable);
        model.addAttribute("page",page);
        model.addAttribute("amount",amount);
        model.addAttribute("tenant", tenant);
        model.addAttribute("area", areaService.getCurrent());
		model.addAttribute("member", member);
        model.addAttribute("begin_date", begin_date);
        model.addAttribute("end_date", end_date);
        model.addAttribute("stock", stock);
        model.addAttribute("lockStock", lockStock);
        model.addAttribute("menu","monthly");
		return "/store/member/monthly/monthly_list";
	}

    /**
     * 月末结账确定
     * @return
     */
    @RequestMapping(value = "/confirm_monthly", method = RequestMethod.GET)
    @ResponseBody
    public Message confirmMonthly() {
        Member member = memberService.getCurrent();
        Tenant tenant=member.getTenant();
        List<Trade> trades=tradeService.findUnshippedList(tenant);
        BigDecimal amount=BigDecimal.ZERO;
        if(trades.size()>0){
            for (Trade trade:trades){
                amount=amount.add(trade.getAmount());
            }
        }
        List<Product> products=productService.openList(null,tenant,null,true,null,null,null,null,null,null, null, null, null,null,null,null);
        Integer stock=0;
        Integer lockStock=0;
        for(Product product:products){
            stock=stock+product.getStock();
            lockStock=lockStock+product.getAllocatedStock();
        }

        Monthly monthly=new Monthly();
        Map<String,BigDecimal> maps=productService.getStockAmount(tenant.getId());
        if(maps!=null){
            monthly.setStockAmount(maps.get("amount"));
        }else{
            monthly.setStockAmount(BigDecimal.ZERO);
        }
        monthly.setTenant(tenant);
        monthly.setBalance(member.getTenant().getMember().getBalance());
        monthly.setFreezeBalance(member.getTenant().getMember().getFreezeBalance());
        monthly.setUnShippingBalance(amount);
        monthly.setCreateDate(new Date());
        monthly.setStock(stock);
        monthly.setLockStock(lockStock);
        monthlyService.save(monthly);
        return Message.success("操作成功");
    }
    /**
     * 月结帐删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody Message monthlyDelete(Long[] ids) {
        monthlyService.delete(ids);
        return SUCCESS_MESSAGE;
    }

}
