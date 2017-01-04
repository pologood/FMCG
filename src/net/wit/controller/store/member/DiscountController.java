/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.store.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.PromotionSecKillModel;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.entity.Promotion.Type;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 限时折扣
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("storeMemberDiscountController")
@RequestMapping("/store/member/discount")
public class DiscountController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "brandSeriesServiceImpl")
	private BrandSeriesService brandSeriesService;

	@Resource(name = "unionServiceImpl")
	private UnionService unionService;

	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list( Pageable pageable,ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}

		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return ERROR_VIEW;
		}

		List<Promotion> promotionList = promotionService.findList(Type.seckill,tenant,null);

		for(Promotion promotion:promotionList){
			if(promotion.getProducts().size()>0){

			}else {
				promotionService.delete(promotion);
			}

			if(promotion.hasEnded()){
				promotionService.delete(promotion);
			}
		}

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		filters.add(new Filter("type", Operator.eq, Type.seckill));
		pageable.setFilters(filters);
		Page<Promotion> page = promotionService.findPage(pageable);

		List<PromotionSecKillModel> promotions = null;
		if (page.getContent().size() > 0) {
			promotions = PromotionSecKillModel.bindData(page.getContent());
		}

		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		String url=bundle.getString("WeiXinSiteUrl") + "/wap/product/content/ID/product.jhtml?extension=" + (member != null ? member.getUsername() : "");
		model.addAttribute("member", member);
		model.addAttribute("page", page);
		model.addAttribute("promotion", promotions);
		model.addAttribute("url", url);
		model.addAttribute("title","亲,“"+tenant.getShortName()+"”限时折扣开始了，快快抢购吧。");
		model.addAttribute("tenantId", tenant.getId());
		model.addAttribute("shareAppKey", bundle.getString("shareAppKey"));
		model.addAttribute("menu","discount");
		return "/store/member/discount/list";

	}

	/**
	 * 所有商品列表
	 */
	@RequestMapping(value = "/listproduct", method = RequestMethod.GET)
	public String listproduct(Long productCategoryId, Long productCategoryTenantId, Long brandId,String searchValue,  Long promotionId,
			Long tagId, @RequestParam(defaultValue = "true") Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		List<ProductCategoryTenant> productCategoryTenant = productCategoryTenantService.findTree(member.getTenant());
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		List<Tag> tags = tagService.findList(tagId);
		model.addAttribute("union", unionService.findAll());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Tag.Type.product));
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("productCategoryTenantId", productCategoryTenantId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isGift", isGift);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("linkTypes", LinkType.values());
		model.addAttribute("productCategoryTenants", productCategoryTenant);
		model.addAttribute("page",
				productService.findMyPage(member.getTenant(),searchValue, productCategory, productCategoryTenantService.find(productCategoryTenantId), brand,
						promotion, tags, null, null, null, isMarketable, isList, isTop, isGift, isOutOfStock,
						isStockAlert, OrderType.dateDesc, pageable));
		model.addAttribute("pageActive", 2);
		model.addAttribute("member", member);
		model.addAttribute("tenantId", member.getTenant().getId());
		model.addAttribute("menu","discount");
		return "/store/member/discount/listproduct";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();

		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return "redirect:/box/register/add.jhtml";
		}
		Product product = productService.find(id);

		if (product == null) {
			return ERROR_VIEW;
		}

		List<Promotion> promotions = promotionService.findList(Type.seckill, tenant, product);
		for (Promotion p : promotions) {
			if(p!=null){
				addFlashMessage(redirectAttributes, Message.error("此商品已有折扣方案，请不要重复添加。"));
				return "redirect:listproduct.jhtml";
			}
		}

		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("productCategoryTreeTenant", productCategoryTenantService.findTree(tenant));
		ProductCategory productCategory = productCategoryService.find(product.getProductCategory().getId());
		model.addAttribute("brands", productCategory.getBrands());
		model.addAttribute("brand", product.getBrand());
		model.addAttribute("tags", tagService.findList(Tag.Type.product));
		model.addAttribute("brandSerieses", brandSeriesService.findRoots());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("product", product);
		model.addAttribute("pageActive", 2);
		model.addAttribute("member", member);
		model.addAttribute("menu","discount");
		return "/store/member/discount/edit";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Long id, BigDecimal price, String beginDate, String endDate, String zhekouj, String limitation,
			HttpServletRequest request,String activityName, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}

		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return ERROR_VIEW;
		}

		Product product = productService.find(id);
		if (product == null) {
			return ERROR_VIEW;
		}

		List<Promotion> promotions = promotionService.findList(Type.seckill, tenant, product);
		Promotion promotion = null;
		for (Promotion p : promotions) {
			if(p!=null){
				addFlashMessage(redirectAttributes, Message.error("此商品已有折扣方案，请不要重复添加。"));
				return "redirect:list.jhtml?type=seckill";
			}
			if (promotion == null) {
				promotion = p;
			} else {
				promotionService.delete(p);
			}
		}
		if (promotion == null) {
			promotion = new Promotion();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date beDate = null, eDate = null;
		try {
			beDate = sdf.parse(beginDate);
			eDate = sdf.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		promotion.setTenant(tenant);
		promotion.setName(activityName);
		promotion.setTitle(activityName);
		promotion.setMinimumQuantity(null);
		promotion.setMaximumQuantity(Integer.parseInt(limitation));
		promotion.setMinimumPrice(null);
		promotion.setMaximumPrice(promotion.getMaximumPrice());
		promotion.setIsFreeShipping(false);
		promotion.setIsCouponAllowed(true);
		promotion.setType(Type.seckill);
		promotion.setMember(member);
		promotion.setPriceExpression(zhekouj);
		promotion.setBeginDate(beDate);
		promotion.setEndDate(eDate);
		List<PromotionProduct> promotionProducts = new ArrayList<PromotionProduct>();
		PromotionProduct promotionProduct = new PromotionProduct();
		promotionProduct.setPrice(new BigDecimal(zhekouj));
		promotionProduct.setProduct(product);
		promotionProduct.setPromotion(promotion);
		promotionProduct.setQuantity(0);
		promotionProducts.add(promotionProduct);
		promotion.setPromotionProducts(promotionProducts);
		promotionService.save(promotion);


		if(!activityDetailService.isActivity(null,tenant,activityRulesService.find(27L))){
			activityDetailService.addPoint(null,tenant,activityRulesService.find(27L));
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml?type=seckill";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids, HttpServletRequest request) {
		try {
			promotionService.delete(ids);
		} catch (Exception e) {
			return ERROR_MESSAGE;
		}
//		Promotion promotion = promotionService.find(ids);
//		promotionService.delete(promotion);
		return SUCCESS_MESSAGE;
	}

}