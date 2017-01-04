package net.wit.controller.wap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.wit.controller.b2c.BaseController;
import net.wit.controller.support.DepositModel;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.service.DepositService;
import net.wit.service.MemberService;
import net.wit.util.DateUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 我的账单
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapDepositController")
@RequestMapping("/wap/deposit")
public class DepositController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "depositServiceImpl")
	private DepositService depositService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		List<DepositModel> deposits = new ArrayList<DepositModel>();
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		Date beginDayOfMonth = DateUtil.setBeginDayOfMonth(calendar);
		// 当前月个月
		DepositModel deposit = convertDepositModel(member, date, beginDayOfMonth);
		deposits.add(deposit);

		// 上个月
		Date date2 = DateUtil.transpositionDate(date, Calendar.MONTH, -1);
		calendar.setTime(date2);
		DepositModel deposit1 = convertDepositModel(member, DateUtil.transpositionDate(beginDayOfMonth, Calendar.MONTH, -1), DateUtil.setLastDayOfMonth(calendar));
		deposits.add(deposit1);
		// 更多
		List<Deposit> list = depositService.findList(member, null, DateUtil.transpositionDate(beginDayOfMonth, Calendar.MONTH, -1),null);
		DepositModel deposit2 = convertToDepositModel(list);
		deposits.add(deposit2);

		model.addAttribute("deposits", deposits);
		return "wap/deposit/list";
	}

	private DepositModel convertDepositModel(Member member, Date date, Date beginDayOfMonth) {
		try {
			List<Deposit> list = depositService.findList(member, beginDayOfMonth, date,null);
			return convertToDepositModel(list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private DepositModel convertToDepositModel(List<Deposit> list) {
		if (list != null && list.size() > 0) {
			DepositModel depositModel = new DepositModel();
			BigDecimal outAmount = new BigDecimal(0);
			BigDecimal inAmount = new BigDecimal(0);
			depositModel.setDeposits(list);
			for (Deposit deposit : list) {
				outAmount=outAmount.add(deposit.getDebit());
				inAmount=inAmount.add(deposit.getCredit());
			}
			depositModel.setInAmount(inAmount);
			depositModel.setOutAmount(outAmount);
			return depositModel;
		}
		return null;
	}

	/**
	 * 充值
	 */
	@RequestMapping(value = "/recharge", method = RequestMethod.GET)
	public String recharge(Long id, ModelMap model) {
		model.addAttribute("id",id);
		return "wap/deposit/recharge";
	}
}
