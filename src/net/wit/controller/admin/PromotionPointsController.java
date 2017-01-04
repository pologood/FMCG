package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Brand;
import net.wit.entity.Coupon;
import net.wit.entity.Location;
import net.wit.entity.Product;
import net.wit.entity.ProductCategory;
import net.wit.entity.Promotion;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.Tag.Type;
import net.wit.service.AdminService;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.MemberRankService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SpecificationService;
import net.wit.service.TagService;
import net.wit.service.TenantService;
import net.wit.util.FreemarkerUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 积分商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminPromotionPointsController")
@RequestMapping("/admin/promotion/points")
public class PromotionPointsController extends BaseController {

	@Resource(name = "promotionServiceImpl")
	protected PromotionService promotionService;
	
	@Resource(name = "tenantServiceImpl")
	protected TenantService tenantService;
	
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;
	
	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		return "/admin/promotion/points/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RedirectAttributes redirectAttributes) {
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("product", productService.find(id));
		return "/admin/promotion/points/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update() {
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long productCategoryId, Long promotionId,Boolean isMarketable, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Location location,
			BigDecimal distance, Pageable pageable, ModelMap model, HttpServletRequest request) {
		Area area = areaService.getCurrent();
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Promotion promotion = promotionService.find(promotionId);
		Admin admin=adminService.getCurrent();
		List<Product> products=new ArrayList<Product>();
		Page<Product> page=new Page<Product>(products, 0, pageable);
		if(admin!=null){
			try {
				page=productService.findPage(admin, null, productCategory, null, promotion, null, null, null, null, null, null, isMarketable, true, null, isGift, isOutOfStock, isStockAlert, null, area, null, location, distance, null, pageable);
				model.addAttribute("page", page);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isGift", isGift);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("linkTypes", LinkType.values());
		return "/admin/promotion/points/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		return SUCCESS_MESSAGE;
	}
}
