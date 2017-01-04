/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Area;
import net.wit.entity.Attribute;
import net.wit.entity.Bonus;
import net.wit.entity.Brand;
import net.wit.entity.Goods;
import net.wit.entity.Member;
import net.wit.entity.MemberRank;
import net.wit.entity.PackagUnit;
import net.wit.entity.Parameter;
import net.wit.entity.ParameterGroup;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.ProductImage;
import net.wit.entity.Promotion;
import net.wit.entity.Specification;
import net.wit.entity.SpecificationValue;
import net.wit.entity.Tag;
import net.wit.entity.Tag.Type;
import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.FileService;
import net.wit.service.GoodsService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.ProductImageService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SpecificationService;
import net.wit.service.SpecificationValueService;
import net.wit.service.TagService;
import net.wit.service.UnionService;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import freemarker.template.TemplateModelException;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberProductController")
@RequestMapping("/b2b/member/product")
public class ProductController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;

	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;

	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "unionServiceImpl")
	private UnionService unionService;

	/**
	 * 选择页面类型
	 */
	public enum ChooseType {

		/** 全部产品 */
		all,
		/** 收藏商品 */
		collect,
		/** 最惠商品 */
		rate,
		/** 热卖商品 */
		hot,

		/** 猜你喜欢 */
		like,
		/** 最近三个月 */
		threeMonth
	}

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody boolean checkSn(String previousSn, String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}
		if (productService.snUnique(previousSn, sn)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取参数组
	 */
	@RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
	public @ResponseBody Set<ParameterGroup> parameterGroups(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		return productCategory.getParameterGroups();
	}

	/**
	 * 获取属性
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody Set<Attribute> attributes(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		return productCategory.getAttributes();
	}

	/**
	 * 商品详情页面
	 */
	@RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
	public String content(@PathVariable Long id, HttpServletRequest request, ModelMap model) {
		Product product = productService.find(id);
		Member member = memberService.getCurrent();
		model.addAttribute("product", product);
		model.addAttribute("member", member);
		model.addAttribute("productCategory", product.getProductCategory());
		return "/b2b/member/product/content";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ModelMap model, Long productCategoryId) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (member.getTenant() == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("member", member);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("productCategoryTreeTenant", productCategoryTenantService.findTree(tenant));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		return "/b2b/member/product/add";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/addProductCategory", method = RequestMethod.GET)
	public String addProductCategory(ModelMap model, Long productCategoryId) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("member", member);
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		return "/b2b/member/product/addProductCategory";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (member.getTenant() == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("member", member);
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("productCategoryTreeTenant", productCategoryTenantService.findTree(tenant));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("product", productService.find(id));
		return "/b2b/member/product/edit";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long productCategoryId, Long productCategoryTenantId, Long brandId, Long promotionId, Long tagId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable,
			ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		if (member.getTenant() == null) {
			return "redirect:/b2b/member/tenant/add.jhtml";
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		List<Tag> tags = tagService.findList(tagId);
		model.addAttribute("member", member);
		model.addAttribute("union", unionService.findAll());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findAll());
		model.addAttribute("tags", tagService.findList(Type.product));
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("promotionId", promotionId);
		model.addAttribute("tagId", tagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isGift", isGift);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", productService.findMyPage(member.getTenant(),null, productCategory, productCategoryTenant, brand, promotion, tags, null, null, null, true, isList, isTop, isGift, isOutOfStock, isStockAlert, OrderType.dateDesc, pageable));
		pageable.setSearchProperty("sn");
		return "/b2b/member/product/list";
	}

	/**
	 * 货郎联盟
	 */
	@RequestMapping(value = "/union", method = RequestMethod.GET)
	public String union(ModelMap model, Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
		if (member.getTenant() == null) {
			return "redirect:/member/tenant/add.jhtml";
		}
		model.addAttribute("member", member);
		model.addAttribute("page", productService.findByUnion(member.getTenant(), null, OrderType.dateDesc, pageable));
		return "/b2b/member/product/productUnion";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		productService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 计算默认市场价
	 * @param price 价格
	 */
	private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
		return setting.setScale(price.multiply(new BigDecimal(defaultMarketPriceScale.toString())));
	}

	/**
	 * 计算默认积分
	 * @param price 价格
	 */
	private long calculateDefaultPoint(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultPointScale = setting.getDefaultPointScale();
		return price.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
	}

	/**
	 * 批量下单
	 */
	@RequestMapping(value = "/batchOrder", method = RequestMethod.GET)
	public String batchOrder(Long brandId, Long productCategoryId, Pageable pageable, ChooseType chooseType, String phonetic, HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) throws TemplateModelException {
		if (chooseType == null) {
			chooseType = ChooseType.all;
		}
		Area area = areaService.getCurrent();
		Page<Product> page = new Page<Product>();
		List<ProductCategory> productCategorys = null;
		Brand brand = brandService.find(brandId);

		ProductCategory productCategory = productCategoryService.find(productCategoryId);

		if (productCategoryId != null) {
			productCategorys = productCategoryService.findChildren(productCategory);
		} else {
			// 初始化,根产品分类
			productCategorys = productCategoryService.findRoots();
		}

		Member currentMember = memberService.getCurrent();
		if (chooseType == ChooseType.all) {
			// 全部产品
			page = productService.searchPage(productCategory, brand, null, null, null, null, null, true, true, null, false, null, null, null, area, false, null, phonetic, null, pageable);

		} else if (chooseType == ChooseType.collect) {
			// 收藏
			if (currentMember != null) {
				page = productService.findPage(currentMember, productCategory, brand, pageable);
			} else {
				addFlashMessage(redirectAttributes, Message.error("您还未登录,请先登录!"));
				return "redirect:/b2b/login.jhtml";
			}
		} else if (chooseType == ChooseType.rate) {
			// 优惠
			page = productService.searchPage(productCategory, brand, null, null, null, null, null, true, true, null, false, null, null, null, area, false, OrderType.weight, phonetic, null, pageable);
		} else if (chooseType == ChooseType.hot) {
			// 热卖
			List<Tag> hotTags = new ArrayList<Tag>();
			long hotId = 1;
			Tag hotTag = tagService.find(hotId);
			hotTags.add(hotTag);
			page = productService.searchPage(productCategory, brand, null, hotTags, null, null, null, true, true, null, false, null, null, null, area, false, null, phonetic, null, pageable);
		} else if (chooseType == ChooseType.like) {
			// 猜你喜欢（推荐）
			List<Tag> tags = new ArrayList<Tag>();
			long id = 5;
			Tag tag = tagService.find(id);
			tags.add(tag);
			page = productService.searchPage(productCategory, brand, null, tags, null, null, null, true, true, null, false, null, null, null, area, false, null, phonetic, null, pageable);
		} else if (chooseType == ChooseType.threeMonth) {
			// 最近三个月购买
			page = productService.findPage(currentMember, -90, productCategory, pageable);
		}

		List<ProductCategory> currentProductCategories = new ArrayList<ProductCategory>();
		if (productCategory != null) {
			List<Long> paths = productCategory.getTreePaths();
			for (Long path : paths) {
				currentProductCategories.add(productCategoryService.find(path));
			}
			model.addAttribute("productCategory", productCategory);
		}
		model.addAttribute("currentProductCategories", currentProductCategories);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("productCategorys", productCategorys);
		model.addAttribute("chooseType", chooseType);
		model.addAttribute("brandId", brandId);
		model.addAttribute("phonetic", phonetic);
		model.addAttribute("page", page);

		return "/b2b/member/product/batchOrder";
	}

}