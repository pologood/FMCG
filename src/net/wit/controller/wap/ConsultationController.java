package net.wit.controller.wap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.ResourceNotFoundException;
import net.wit.Setting;
import net.wit.Setting.ConsultationAuthority;
import net.wit.entity.Consultation;
import net.wit.entity.Consultation.Type;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.service.ConsultationService;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 咨询
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapConsultationController")
@RequestMapping("/wap/consultation")
public class ConsultationController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 4;

	@Resource(name = "consultationServiceImpl")
	private ConsultationService consultationService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 发表
	 */
	@RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
	public String add(@PathVariable Long id, ModelMap model) {
		Setting setting = SettingUtils.get();
		if (!setting.getIsConsultationEnabled()) {
			throw new ResourceNotFoundException();
		}
		Product product = productService.find(id);
		if (product == null) {
			throw new ResourceNotFoundException();
		}
		model.addAttribute("product", product);
		return "/wap/consultation/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Message save(Long id, String content, Consultation.Type type, HttpServletRequest request) {
		Setting setting = SettingUtils.get();
		if (!setting.getIsConsultationEnabled()) {
			return Message.error("shop.consultation.disabled");
		}
		if (!isValid(Consultation.class, "content", content)) {
			return ERROR_MESSAGE;
		}
		Member member = memberService.getCurrent();
		if (setting.getConsultationAuthority() != ConsultationAuthority.anyone && member == null) {
			return Message.error("shop.consultation.accessDenied");
		}
		Consultation consultation = new Consultation();
		consultation.setContent(content);
		consultation.setIp(request.getRemoteAddr());
		consultation.setMember(member);
		if (type == Type.product) {
			Product product = productService.find(id);
			if (product == null) {
				return ERROR_MESSAGE;
			}
			consultation.setProduct(product);
			consultation.setType(Type.product);
		} else {
		}

		if (setting.getIsConsultationCheck()) {
			consultation.setIsShow(false);
			consultationService.save(consultation);
			return Message.success("shop.consultation.check");
		} else {
			consultation.setIsShow(true);
			consultationService.save(consultation);
			return Message.success("shop.consultation.success");
		}
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		// 发出的咨询（不含回复的咨询）
		Page<Consultation> page = consultationService.findPage(null, member, null, null, pageable);
		model.addAttribute("member", member);
		model.addAttribute("page", page);
		return "wap/consultation/list";
	}

	/**
	 * 加载更多
	 */
	@RequestMapping(value = "/addMore", method = RequestMethod.GET)
	@ResponseBody
	public Page<Consultation> addMore(Integer pageNumber) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		// 发出的咨询（不含回复的咨询）
		Page<Consultation> page = consultationService.findPage(null, member, null, null, pageable);
		return page;
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(Long id, ModelMap model) {
		model.addAttribute("consultation", consultationService.find(id));
		return "wap/consultation/reply";
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(Long id, String content, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!isValid(Consultation.class, "content", content)) {
			return ERROR_VIEW;
		}
		Consultation consultation = consultationService.find(id);
		if (consultation == null) {
			return ERROR_VIEW;
		}
		Consultation replyConsultation = new Consultation();
		replyConsultation.setContent(content);
		replyConsultation.setIp(request.getRemoteAddr());
		consultationService.reply(consultation, replyConsultation);

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

}