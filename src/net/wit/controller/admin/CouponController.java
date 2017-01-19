/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.*;
import net.wit.Message;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.FreemarkerUtils;

import net.wit.util.QRBarCodeUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 优惠券
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminCouponController")
@RequestMapping("/admin/coupon")
public class CouponController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;
	
	@Resource(name = "couponServiceImpl")
	private CouponService couponService;
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;
	@Resource(name = "couponNumberServiceImpl")
	private CouponNumberService couponNumberService;
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 检查价格运算表达式是否正确
	 */
	@RequestMapping(value = "/check_price_expression", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkPriceExpression(String priceExpression) {
		if (StringUtils.isEmpty(priceExpression)) {
			return false;
		}
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("quantity", 111);
			model.put("price", new BigDecimal(9.99));
			new BigDecimal(FreemarkerUtils.process("#{(" + priceExpression + ");M50}", model));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** 添加 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/coupon/add";
	}

	/** 保存 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String name,BigDecimal amount, String introduction, RedirectAttributes redirectAttributes) {
		Coupon coupon=new Coupon();
		if(StringUtils.isBlank(name)){
			coupon.setName(amount+"元套券");
		}else{
			coupon.setName(name);
		}
		coupon.setAmount(amount);
		coupon.setType(Coupon.Type.multipleCoupon);
		coupon.setStatus(Coupon.Status.confirmed);
		coupon.setPrefix("m");
		coupon.setIsEnabled(true);
		coupon.setIsExchange(false);
		coupon.setIsReceiveMore(false);
		coupon.setReceiveTimes(1L);
		coupon.setPoint(0L);
		coupon.setIntroduction(introduction);
		couponService.save(coupon);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/** 编辑 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Coupon coupon = couponService.find(id);
		model.addAttribute("coupon", coupon);
		return "/admin/coupon/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Long id,String name,BigDecimal amount, String introduction, RedirectAttributes redirectAttributes) {
		Coupon coupon=couponService.find(id);
		if(StringUtils.isBlank(name)){
			coupon.setName(amount+"元套券");
		}else{
			coupon.setName(name);
		}
		coupon.setAmount(amount);
		coupon.setIntroduction(introduction);
		couponService.update(coupon);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 平台套券列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		List<Filter> filters = new ArrayList<>();
		List<Map<String ,Object>> maps = new ArrayList<Map<String ,Object>>();
		filters.add(new Filter("type", Filter.Operator.eq, Coupon.Type.multipleCoupon));
		pageable.setFilters(filters);
		for(Coupon coupon:couponService.findPage(pageable).getContent()){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id",coupon.getId());
			map.put("name",coupon.getName());
			map.put("amount",coupon.getAmount());
			map.put("send_count",coupon.getSendCount());
			map.put("used_count",coupon.getUsedCount());
			if(couponNumberService.findCouponNumberList(coupon,null)!=null){
				Integer count=0;
				for(Map<String,Object> m:couponNumberService.findCouponNumberList(coupon,null)){
					count+=Integer.valueOf(m.get("mark_count").toString());
				}
				map.put("mark_count",count);
			}else{
				map.put("mark_count","0");
			}
			maps.add(map);
		}
		model.addAttribute("currentMember", adminService.getCurrent());
		model.addAttribute("page", couponService.findPage(pageable));
		model.addAttribute("maps", maps);
		return "/admin/coupon/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		couponService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/** 生成优惠码 */
	@RequestMapping(value = "/build", method = RequestMethod.GET)
	public String build(Long id, ModelMap model) {
		model.addAttribute("coupon", couponService.find(id));
		return "/admin/coupon/build";
	}
	
	/** 下级商品分类 */
	@RequestMapping(value = "/productCategorySeries", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> productCategorySeries(Long parentId) {
		List<ProductCategory> productCategorySeries = new ArrayList<ProductCategory>();
		Map<Long, String> options = new HashMap<Long, String>();
		if (parentId != null) {
			ProductCategory parent = productCategoryService.find(parentId);
			productCategorySeries = new ArrayList<ProductCategory>(parent.getChildren());
		} else {
			productCategorySeries = productCategoryService.findRoots();
		}
		for (ProductCategory productCategory : productCategorySeries) {
			options.put(productCategory.getId(), productCategory.getName());
		}
		return options;
	}

	/** 
	 * -----------暂时不用--------------
	 * 下载优惠码
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public ModelAndView download(Long id, Integer count, ModelMap model) {
		if (count == null || count <= 0) {
			count = 50;
		}
		Coupon coupon = couponService.find(id);
		List<CouponCode> data = couponCodeService.build(coupon, null, count);
		String filename = "coupon_code_" + new SimpleDateFormat("yyyyMM").format(new Date()) + ".xls";
		String[] contents = new String[4];
		contents[0] = message("admin.coupon.type") + ": " + coupon.getName();
		contents[1] = message("admin.coupon.count") + ": " + count;
		contents[2] = message("admin.coupon.operator") + ": " + adminService.getCurrentUsername();
		contents[3] = message("admin.coupon.date") + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return new ModelAndView(new ExcelView(filename, null, new String[] { "code" }, new String[] { message("admin.coupon.title") }, null, null, data, contents), model);
	}
	
	/** 设置红包种类下面最大的红包码生成数 */
	@RequestMapping(value = "/setCouponCodeQuantity", method = RequestMethod.POST)
	public String setCouponCodeQuantity(Long id, Integer count, RedirectAttributes redirectAttributes) {
		Coupon coupon = couponService.find(id);
		coupon.setCount(coupon.getCount()+count);
		couponService.update(coupon);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	/** 发送红包界面 */
	@RequestMapping(value = "/sendCouponCode", method = RequestMethod.GET)
	public String sendCouponCode(Long[] ids, ModelMap model) {
		model.addAttribute("coupons", couponService.findList(ids));
		return "/admin/coupon/sendCouponCode";
	}
	
	/** 查询符合条件的会员列表 */
	@RequestMapping(value = "/queryMember", method = RequestMethod.GET)
	public @ResponseBody List<Member> queryMember(Long area, String searchValue) {
		return memberService.findByCondition(area, searchValue);
	}
	
	/** 发送红包 */
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> send(String mids, String cids, String rCounts, String aCount) {
		return couponCodeService.send(mids, cids, rCounts, aCount);
	}
	
	/** 保存 */
	@RequestMapping(value = "/saveCouponCode", method = RequestMethod.POST)
	public @ResponseBody boolean saveCouponCode(Long id, Integer count, ModelMap model) {
		if (count == null || count <= 0) {
			count = 50;
		}
		couponCodeService.build(couponService.find(id), null, count);
		return false;
	}
	
	/** 列表 */
	@RequestMapping(value = "/sendedList", method = RequestMethod.GET)
	public String sendedList(String beginDate,String endDate,Pageable pageable,String keyword,Long id, ModelMap model) {
		pageable.setPageSize(PAGE_SIZE);
		Date beginTime = null;
		Date endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(beginDate)) {
			beginTime = sdf.parse(beginDate, new ParsePosition(0));
		}
		if (StringUtils.isNotBlank(endDate)) {
			endTime = sdf.parse(endDate, new ParsePosition(0));
		}
		model.addAttribute("sendedListCouponId",id);
//		model.addAttribute("page", couponCodeService.findDrawedCouponCodeByCoupon(beginTime,endTime,keyword,couponService.find(id), pageable));
		model.addAttribute("page", couponCodeService.findPage(beginTime,endTime,keyword,couponService.find(id),null,pageable));
		model.addAttribute("beginDate",beginDate);
		model.addAttribute("endDate",endDate);
		model.addAttribute("keyword",keyword);
		return "/admin/coupon/sendedList";
	}
	
	/** 列表 */
	@RequestMapping(value = "/usedList", method = RequestMethod.GET)
	public String usedList(Long id,String keyword, Pageable pageable, ModelMap model) {
		pageable.setPageSize(PAGE_SIZE);
		model.addAttribute("usedListCouponId",id);
		model.addAttribute("page", couponCodeService.findUsedCouponCodeByKeyword(keyword,couponService.find(id), pageable));
		model.addAttribute("keyword",keyword);
		return "/admin/coupon/usedList";
	}

	/** 平台套券》登记列表 */
	@RequestMapping(value = "/markedList", method = RequestMethod.GET)
	public String markedList(Long id, Pageable pageable,String keyword, ModelMap model) {
		pageable.setPageSize(PAGE_SIZE);
		Page<Map<String,Object>> page=couponNumberService.findCouponNumberPage(couponService.find(id),keyword,pageable);
		List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
		for(Map<String, Object> m:page.getContent()){
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("mark_count", m.get("mark_count"));
			map.put("user_count",m.get("user_count"));
			map.put("brokerage", m.get("brokerage"));
			map.put("tenant_name", m.get("tenant_name"));
			map.put("tenant_category_name", m.get("tenant_category_name"));
			map.put("area_name", m.get("area_name"));
			map.put("guider_name", m.get("guider_name"));
			map.put("guider_name_id", m.get("guider_name_id"));
			maps.add(map);
		}
		model.addAttribute("maps",maps);
		model.addAttribute("page",page);
		model.addAttribute("keyword",keyword);
		model.addAttribute("markedListCouponId",id);
		return "/admin/coupon/markedList";
	}

	/** 平台套券》登记》已登记数量列表 */
	@RequestMapping(value = "/marked_count_list", method = RequestMethod.GET)
	public String markedCount(Long guideMemberId,Long couponId, Pageable pageable,String keyword, ModelMap model) {
		Member guideMember=memberService.find(guideMemberId);
		Coupon coupon=couponService.find(couponId);
		List<Filter> filter = new ArrayList<Filter>();
		filter.add(new Filter("status", Filter.Operator.eq, CouponNumber.Status.bound));
		filter.add(new Filter("guideMember", Filter.Operator.eq, guideMember));
		filter.add(new Filter("coupon", Filter.Operator.eq, coupon));
		pageable.setFilters(filter);
		Page<CouponNumber> page=couponNumberService.findPage(pageable);
		model.addAttribute("page",page);
		model.addAttribute("keyword",keyword);
		model.addAttribute("guideMemberId",guideMemberId);
		model.addAttribute("couponId",couponId);
		return "/admin/coupon/marked_count_list";
	}

	/** 平台套券》登记》已领用数量列表 */
	@RequestMapping(value = "/token_count_list", method = RequestMethod.GET)
	public String tokenCount(Long guideMemberId,Long couponId, Pageable pageable,String keyword, ModelMap model) {
		Member guideMember=memberService.find(guideMemberId);
		Coupon coupon=couponService.find(couponId);
		List<Filter> filter = new ArrayList<Filter>();
		filter.add(new Filter("status", Filter.Operator.eq, CouponNumber.Status.receive));
		filter.add(new Filter("guideMember", Filter.Operator.eq, guideMember));
		filter.add(new Filter("coupon", Filter.Operator.eq, coupon));
		pageable.setFilters(filter);
		Page<CouponNumber> page=couponNumberService.findPage(pageable);
		model.addAttribute("page",page);
		model.addAttribute("keyword",keyword);
		model.addAttribute("guideMemberId",guideMemberId);
		model.addAttribute("couponId",couponId);
		return "/admin/coupon/token_count_list";
	}

//	/** 已登记列表导出 */
//	@RequestMapping(value = "/markedList_export", method = RequestMethod.POST)
//	@ResponseBody
//	public List<Map<String,Object>> markedListExport(Long id, Pageable pageable,String keyword, ModelMap model) {
//		pageable.setPageSize(PAGE_SIZE);
//		List<Map<String,Object>> list=couponNumberService.findCouponNumberList(couponService.find(id),keyword);
//		List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
//		for(Map<String, Object> m:list){
//			Map<String, Object> map=new HashMap<String,Object>();
//			map.put("mark_count", m.get("mark_count"));
//			map.put("user_count",m.get("user_count"));
//			map.put("brokerage", m.get("brokerage"));
//			map.put("tenant_name", m.get("tenant_name"));
//			map.put("tenant_category_name", m.get("tenant_category_name"));
//			map.put("area_name", m.get("area_name"));
//			map.put("guider_name", m.get("guider_name"));
//			maps.add(map);
//		}
//		return maps;
//	}

	/** 列表 */
	@RequestMapping(value = "/get/code", method = RequestMethod.POST)
	public @ResponseBody DataBlock getCode(Long id) {

		Coupon coupon = couponService.find(id);
		if(coupon==null){
			return DataBlock.error("无效的平台套券");
		}

		Long code = couponNumberService.getLastCode(coupon);
		if(code==null){
			code=0L;
		}
		return DataBlock.success(code,"返回成功");
	}


	/**
	 * 根据
	 */
	@RequestMapping(value = "/qrcode/coupon", method = RequestMethod.GET)
	public void qrcodeEmployee(Long id,Long code, HttpServletResponse response) {
		try {
			System.out.println("adminCouponController code:"+code);
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String url =bundle.getString("WeiXinSiteUrl") + "/weixin/coupon/view.jhtml?id="+id+"&code="+code;

			System.out.println("adminCouponController url:"+url);

			String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
			response.reset();
			response.setContentType("image/jpeg;charset=utf-8");
			try {
				QRBarCodeUtil.encodeQRCode(url, tempFile, 200, 200);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ServletOutputStream output = response.getOutputStream();// 得到输出流
			InputStream imageIn = new FileInputStream(new File(tempFile));
			// 得到输入的编码器，将文件流进行jpg格式编码
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
			// 得到编码后的图片对象
			BufferedImage image = decoder.decodeAsBufferedImage();
			// 得到输出的编码器
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
			encoder.encode(image);// 对图片进行输出编码
			imageIn.close();// 关闭文件流
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}