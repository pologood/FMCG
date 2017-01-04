package net.wit.controller.admin;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.entity.Promotion;
import net.wit.service.PromotionService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 红包
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminPromotionCouponController")
@RequestMapping("/admin/promotion/coupon")
public class PromotionCouponController  extends BaseController {

	@Resource(name = "promotionServiceImpl")
	protected PromotionService promotionService;
	
	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		Filter filter = new Filter("type", Operator.eq, Promotion.Type.coupon);
		pageable.getFilters().add(filter);
		model.addAttribute("page", promotionService.findPage(pageable));
		return "/admin/promotion/coupon/list";
	}
}
