/** 
 * @ClassName: DepositController 
 * @Description: 我的账单
 * @author Administrator 
 * @date 2014-9-5 下午1:45:14  
 */
package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.DepositModel;
import net.wit.controller.assistant.model.PayBillModel;
import net.wit.controller.assistant.model.PaymentDetailsModel;
import net.wit.entity.*;
import net.wit.entity.Deposit.Type;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.service.*;
import net.wit.util.DateUtil;
import net.wit.util.SettingUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("assistantMemberDepositController")
@RequestMapping("/assistant/member/deposit")
public class DepositController extends BaseController {
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;
	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name ="payBillServiceImpl")
	private PayBillService payBillService;
	
	/**
	 * 登录者所属店主账单明细
	 * begin_date 开始时间
	 * end_date 结束时间
	 */
	@RequestMapping(value = "/list/owner", method = RequestMethod.GET)
	public @ResponseBody DataBlock listOwner(Date begin_date,Date end_date,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String start_time = sdf.format(begin_date);
		String end_time = sdf.format(end_date.getTime()+1 * 24 * 60 * 60 * 1000);
		Date startTime = DateUtil.changeStrToDate(start_time);
		Date endTime = DateUtil.changeStrToDate(end_time);
		Page<Deposit> page = depositService.findPage(member, startTime, endTime, pageable,null);
		
		return DataBlock.success(DepositModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 查看账单详情
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock query(long id, HttpServletRequest request) {
		        Deposit deposit = depositService.find(id);
				PaymentDetailsModel paymentDetailsModel = new  PaymentDetailsModel();
				if(deposit.getPayment()!=null){
					paymentDetailsModel.copyFrom(deposit.getPayment()) ;
				}

				return DataBlock.success(paymentDetailsModel,"执行成功");
		}
	/**
	 * 店主汇总账单
	 * begin_date 开始时间
	 * end_date 结束时间
	 */
	@RequestMapping(value = "/sumer/owner", method = RequestMethod.GET)
	public @ResponseBody DataBlock sumerOwner(Date begin_date,Date end_date) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("recharge", depositService.income(member,Type.recharge,begin_date,end_date,null));
		data.put("payment", depositService.outcome(member,Type.payment,begin_date,end_date,null));
		data.put("withdraw", depositService.outcome(member,Type.withdraw,begin_date,end_date,null));
		data.put("receipts", depositService.income(member,Type.receipts,begin_date,end_date,null));
		data.put("profit", depositService.income(member,Type.profit,begin_date,end_date,null));
		data.put("rebate", depositService.outcome(member,Type.rebate,begin_date,end_date,null));
		data.put("income", depositService.income(member,Type.income,begin_date,end_date,null));
		data.put("outcome", depositService.outcome(member,Type.outcome,begin_date,end_date,null));
		data.put("cashier", depositService.income(member,Type.cashier,begin_date,end_date,null));
		data.put("total_income", depositService.income(member,null,begin_date,end_date,null));
		data.put("total_outcome", depositService.outcome(member,null,begin_date,end_date,null));
		return DataBlock.success(data,"执行成功");
	}
}
