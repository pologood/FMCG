/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 统计
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("statisticsController")
@RequestMapping("/admin/statistics")
public class StatisticsController extends BaseController {

	@Resource(name = "cacheServiceImpl")
	private CacheService cacheService;

	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "memberCapitalServiceImpl")
	private MemberCapitalService memberCapitalService;

	@Resource(name = "platformCapitalServiceImpl")
	private PlatformCapitalService platformCapitalService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view() {
		return "/admin/statistics/view";
	}

	/**
	 * 自动签收
	 */
	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public String sign() {
		return "/admin/statistics/sign";
	}

	/**
	 * 签收
     */
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public
	@ResponseBody
	Map<String, Object> build(Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		if (first == null || first < 0) {
			first = 0;
		}
		if (count == null || count <= 0) {
			count = 50;
		}
		int buildCount = 0;
		boolean isCompleted = true;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-7);
		List<Trade> overCompleteList = tradeService.getTradList(Order.ShippingStatus.shipped, calendar.getTime(),first,count);

		if(overCompleteList.size()>0){
			for (Trade trade : overCompleteList) {
				Order order = trade.getOrder();
				if (order != null && trade.getShippingStatus() == Order.ShippingStatus.shipped) {
					try{
						orderService.jobSign(trade.getId());
						System.out.println("订单号："+order.getSn()+"被自动签收。签收时间："+new Date());
					}catch(Exception ex){
						System.out.println("订单号："+order.getSn()+"签收失败。签收时间："+new Date()+"原因：");
						ex.printStackTrace();
						continue;
					}
				}
				buildCount++;
				if (overCompleteList.size() == count) {
					isCompleted = false;
				}
			}
			first += overCompleteList.size();
		}

		long endTime = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<>();
		map.put("first", first);
		map.put("buildCount", buildCount);
		map.put("buildTime", endTime - startTime);
		map.put("isCompleted", isCompleted);
		return map;
	}

	/**
	 * 自动评价
	 */
	@RequestMapping(value = "/review", method = RequestMethod.GET)
	public String review() {
		return "/admin/statistics/review";
	}

	/**
	 * 评价
	 */
	@RequestMapping(value = "/review", method = RequestMethod.POST)
	public
	@ResponseBody
	Map<String, Object> review(Integer first, Integer count) {
		long startTime = System.currentTimeMillis();
		if (first == null || first < 0) {
			first = 0;
		}
		if (count == null || count <= 0) {
			count = 50;
		}
		int buildCount = 0;
		boolean isCompleted = true;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-15);
		List<Trade> list = tradeService.findUnreviewList(first,count,calendar.getTime(),null,null);

		if(list.size()>0){
			for(Trade trade:list){
				Review review=new Review();
				review.setFlag(Review.Flag.trade);
				review.setScore(5);
				review.setAssistant(5);
				review.setContent("系统默认好评");
				review.setIsShow(false);
				review.setIp("127.0.0.1");
				review.setMember(trade.getOrder().getMember());
				review.setType(Review.Type.positive);
				try {
					reviewService.reviewTrade("weixin", trade.getOrder().getMember(), trade, null, review);
					System.out.println("订单号："+trade.getSn()+"被自动评价。评价时间："+new Date());
				} catch (Exception e) {
					System.out.println("订单号："+trade.getSn()+"自动评价失败。评价时间："+new Date()+"原因：");
					e.printStackTrace();
					continue;
				}

				buildCount++;
				if (list.size() == count) {
					isCompleted = false;
				}
			}
			first += list.size();
		}

		long endTime = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<>();
		map.put("first", first);
		map.put("buildCount", buildCount);
		map.put("buildTime", endTime - startTime);
		map.put("isCompleted", isCompleted);
		return map;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting() {
		return "/admin/statistics/setting";
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.POST)
	public String setting(@RequestParam(defaultValue = "false") Boolean isEnabled, RedirectAttributes redirectAttributes) {
		Setting setting = SettingUtils.get();
		if (isEnabled) {
			if (StringUtils.isEmpty(setting.getCnzzSiteId()) || StringUtils.isEmpty(setting.getCnzzPassword())) {
				try {
					String createAccountUrl = "http://intf.cnzz.com/user/companion/wit.php?domain=" + setting.getSiteUrl() + "&key=" + DigestUtils.md5Hex(setting.getSiteUrl() + "Lfg4uP0H");
					URLConnection urlConnection = new URL(createAccountUrl).openConnection();
					BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					String line = null;
					while ((line = in.readLine()) != null) {
						if (line.contains("@")) {
							break;
						}
					}
					if (line != null) {
						setting.setCnzzSiteId(StringUtils.substringBefore(line, "@"));
						setting.setCnzzPassword(StringUtils.substringAfter(line, "@"));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		setting.setIsCnzzEnabled(isEnabled);
		SettingUtils.set(setting);
		cacheService.clear();
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:setting.jhtml";
	}

	/**
	 *平台对涨流水记录
	 */
	@RequestMapping(value = "/platform_capital_total", method = RequestMethod.GET)
	public String platform(String beginDate,String endDate,Pageable pageable, ModelMap model) {
		Date beginTime = null;
		Date endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginDate)) {
			beginTime = sdf.parse(beginDate, new ParsePosition(0));
		}
		if (StringUtils.isNotBlank(endDate)) {
			endTime = sdf.parse(endDate, new ParsePosition(0));
		}
 		Page<PlatformCapital> page=platformCapitalService.findPageByDate(beginTime,endTime,pageable);
		model.addAttribute("page",page);
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		return "/admin/statistics/platform_capital_total";
	}
	/**
	 *平台对涨流水记录导出
	 */
	@RequestMapping(value = "/platform_capital_total_export", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String , Object >> platformExport(String beginDate,String endDate,Pageable pageable, ModelMap model) {
		List<Map<String,Object>> maps=new ArrayList<Map<String , Object >>();
		Date beginTime = null;
		Date endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginDate)) {
			beginTime = sdf.parse(beginDate, new ParsePosition(0));
		}
		if (StringUtils.isNotBlank(endDate)) {
			endTime = sdf.parse(endDate, new ParsePosition(0));
		}
		List<PlatformCapital> platformCapitals=platformCapitalService.findListByDate(beginTime,endTime,null);
		if(platformCapitals!=null){
			for(PlatformCapital platformCapital:platformCapitals){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("create_date",sdf.format(platformCapital.getCreateDate()));
				map.put("gathering",platformCapital.getGathering());
				map.put("withdrawCash",platformCapital.getWithdrawCash());
				map.put("fee",platformCapital.getFee());
				map.put("brokerage",platformCapital.getBrokerage());
				maps.add(map);
			}
		}
		return maps;
	}
	/**
	 *会员对涨流水记录
	 */
	@RequestMapping(value = "/member_capital_total", method = RequestMethod.GET)
	public String member(String beginDate,String endDate,Pageable pageable,String keyword, ModelMap model) {
		Date beginTime = null;
		Date endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginDate)) {
			beginTime = sdf.parse(beginDate, new ParsePosition(0));
		}
		if (StringUtils.isNotBlank(endDate)) {
			endTime = sdf.parse(endDate, new ParsePosition(0));
		}
		Page<MemberCapital> page=memberCapitalService.findPageByDate(beginTime,endTime,keyword,pageable);
		model.addAttribute("page",page);
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		model.addAttribute("keyword",keyword);
		return "/admin/statistics/member_capital_total";
	}

	/**
	 *会员对涨流水记录导出
	 */
	@RequestMapping(value = "/member_capital_total_export", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String , Object >> memberCapitalExport(String beginDate,String endDate,String keyword) {
		List<Map<String,Object>> maps=new ArrayList<Map<String , Object >>();
		Date beginTime = null;
		Date endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginDate)) {
			beginTime = sdf.parse(beginDate, new ParsePosition(0));
		}
		if (StringUtils.isNotBlank(endDate)) {
			endTime = sdf.parse(endDate, new ParsePosition(0));
		}
		List<MemberCapital> memberCapitals=memberCapitalService.findListByDate(beginTime,endTime,keyword,null);
		if(memberCapitals!=null){
			for(MemberCapital memberCapital:memberCapitals){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("create_date",sdf.format(memberCapital.getCreateDate()));
				map.put("name",memberCapital.getMember()!=null?memberCapital.getMember().getName():"--");
				map.put("mobile",memberCapital.getMember()!=null?memberCapital.getMember().getUsername():"--");
				map.put("credit",memberCapital.getCredit());
				map.put("debit",memberCapital.getDebit());
				map.put("recharge",memberCapital.getRecharge());
				map.put("payment",memberCapital.getPayment());
				map.put("withdraw",memberCapital.getWithdraw());
				map.put("receipts",memberCapital.getReceipts());
				map.put("profit",memberCapital.getProfit());
				map.put("rebate",memberCapital.getRebate());
				map.put("cashier",memberCapital.getCashier());
				map.put("income",memberCapital.getIncome());
				map.put("outcome",memberCapital.getOutcome());
				map.put("coupon",memberCapital.getCoupon());
				map.put("couponuse",memberCapital.getCouponuse());
				maps.add(map);
			}
		}
		return maps;
	}
	/**
	 *生成平台和会员资金流水记录
	 */
	@RequestMapping(value = "/create_date", method = RequestMethod.GET)
	@ResponseBody
	public Message createDate() {
		try {
			exectProcedure();
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("处理平台数据失败");
		}
		return Message.success("处理成功");
	}

	/**
	 *删除平台和会员资金流水记录
	 */
	@RequestMapping(value = "/delete_today_capital", method = RequestMethod.GET)
	@ResponseBody
	public Message deleteDayDate() {
		try {
			exectAginstProcedure();
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("处理平台数据失败");
		}
		return Message.success("处理成功");
	}

	/**
	 *执行存储过程
	 */
	public void exectProcedure() throws Exception {
		System.out.println("-------  start 测试调用存储过程：返回值是简单值非列表");
		Connection conn = null;
		CallableStatement stmt = null;
		ResourceBundle wit = PropertyResourceBundle.getBundle("wit");
		try {
			Class.forName(wit.getString("jdbc.driver"));
			conn = DriverManager.getConnection(wit.getString("jdbc.url"), wit.getString("jdbc.username"), wit.getString("jdbc.password"));
			stmt = conn.prepareCall("{call capital_detail()}");
			stmt.execute();
			System.out.println("-------  Test End.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			if (null != stmt) {
				stmt.close();
			}
			if (null != conn) {
				conn.close();
			}
		}
	}

	/**
	 *执行存储过程2
	 */
	public void exectAginstProcedure() throws Exception {
		System.out.println("-------  start 测试调用存储过程：返回值是简单值非列表");
		Connection conn = null;
		CallableStatement stmt = null;
		ResourceBundle wit = PropertyResourceBundle.getBundle("wit");
		try {
			Class.forName(wit.getString("jdbc.driver"));
			conn = DriverManager.getConnection(wit.getString("jdbc.url"), wit.getString("jdbc.username"), wit.getString("jdbc.password"));
			stmt = conn.prepareCall("{call against_capital()}");
			stmt.execute();
			System.out.println("-------  Test End.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			if (null != stmt) {
				stmt.close();
			}
			if (null != conn) {
				conn.close();
			}
		}
	}
}