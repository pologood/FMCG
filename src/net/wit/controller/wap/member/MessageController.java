package net.wit.controller.wap.member;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Consultation;
import net.wit.entity.Member;
import net.wit.entity.ProductNotify;
import net.wit.entity.Review;
import net.wit.service.ConsultationService;
import net.wit.service.MemberService;
import net.wit.service.ProductNotifyService;
import net.wit.service.ReviewService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 我的账单
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapMemberMessageController")
@RequestMapping("/wap/member/message")
public class MessageController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 4;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

	@Resource(name = "productNotifyServiceImpl")
	ProductNotifyService productNotifyService;
	
	@Resource(name="consultationServiceImpl")
	private ConsultationService consultationService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		Long reCount = reviewService.count(member, null, null, true);
		Page<ProductNotify> notifyCount = productNotifyService.findMyPage(member, true, null, true, pageable);
		Page<Consultation> csCount = consultationService.findMyPage(member, null, true, pageable);
		model.addAttribute("reCount", reCount);
		model.addAttribute("notifyCount", notifyCount.getContent().size());
		model.addAttribute("csCount", csCount.getContent().size());
		return "wap/member/message/index";
	}

	/**
	 * 商品评价列表
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("page", reviewService.findPage(member, null, null, null, pageable));
		return "wap/member/message/manager";
	}

	/**
	 * 加载更多
	 */
	@RequestMapping(value = "/review/addMore", method = RequestMethod.GET)
	@ResponseBody
	public Page<Review> addMore(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Review> page = null;
		page = reviewService.findPage(member, null, null, null, pageable);
		return page;
	}

	/**
	 * 到货通知列表
	 */
	@RequestMapping(value = "/productNotify", method = RequestMethod.GET)
	public String productNotify(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("page", productNotifyService.findMyPage(member, null, null, null, pageable));
		return "wap/member/productNotify/list";
	}

	/**
	 * 加载更多到货通知
	 */
	@RequestMapping(value = "/productNotify/addMore", method = RequestMethod.GET)
	@ResponseBody
	public Page<ProductNotify> productNotifyMore(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<ProductNotify> page = null;
		page = productNotifyService.findMyPage(member, null, null, null, pageable);
		return page;
	}
}