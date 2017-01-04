/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.controller.app.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Product.OrderType;
import net.wit.weixin.main.MenuManager;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("appController")
@RequestMapping("/app/product")
public class ProductController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;

	/**
	 * 根据id获取商品详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody DataBlock view(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		ProductModel model = new ProductModel();
		model.copyFrom(product,memberService.getCurrent());
		model.bind(product.getGoods());
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 根据id获取商品描述
	 */
	@RequestMapping(value = "/description", method = RequestMethod.GET)
	public @ResponseBody DataBlock descr_view(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		ProductDescriptionModel model = new ProductDescriptionModel();
		model.copyFrom(product);
		return DataBlock.success(model,"执行成功");
	}

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/barcode", method = RequestMethod.GET)
	public @ResponseBody DataBlock barcode(Long tenantId,String barcode) {
		Tenant tenant = tenantService.find(tenantId);
		List<Product> products = productService.findByBarcode(tenant, barcode);
		return DataBlock.success(ProductListModel.bindData(products),"执行成功");
	}

	/**
	 * 列表  按需求修改
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long productCategoryId, Long brandId,BigDecimal startPrice, BigDecimal endPrice, Location location, BigDecimal distance, OrderType orderType, Pageable pageable, HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Area currentArea = areaService.getCurrent();
		Map<Attribute, String> attributeValue = new HashMap<Attribute, String>();
		Set<Attribute> attributes = productCategory.getAttributes();
		for (Attribute attribute : attributes) {
			String value = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(value) && attribute.getOptions().contains(value)) {
				attributeValue.put(attribute, value);
			}
		}
		Page<Product> page = productService.findPage(productCategory, brand, null, null, attributeValue, startPrice, endPrice, true, true, null, false, null, null, null, currentArea, null, location, distance, orderType, pageable);

	    return DataBlock.success(ProductListModel.bindData(page.getContent()),"执行成功");
	}
	
	/**
	 * 搜索
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock search(String keyword, Long productCategoryId, Long communityId, Long brandId,BigDecimal startPrice, BigDecimal endPrice, Long brandSeriesId,Long promotionId, Long areaId, Location location, BigDecimal distance, OrderType orderType, Long[] tagIds, Pageable pageable, HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Area currentArea = areaService.find(areaId);
		Map<Attribute, String> attributeValue = new HashMap<Attribute, String>();
		if (productCategory != null){
		Set<Attribute> attributes = productCategory.getAttributes();
		for (Attribute attribute : attributes) {
			String value = request.getParameter("attribute_" + attribute.getId());
			if (StringUtils.isNotEmpty(value) && attribute.getOptions().contains(value)) {
				attributeValue.put(attribute, value);
			}
			}
		}
		Page<Product> page = productService.searchPage(productCategory, brand, null, null, attributeValue, startPrice, endPrice, true, true, null, false, null, null, null, currentArea, null, orderType, null, keyword, pageable);

	    return DataBlock.success(ProductListModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits", method = RequestMethod.GET)
	public @ResponseBody DataBlock hits(Long id) {
		return DataBlock.success(productService.viewHits(id),"执行成功");
	}
	
	/**
	 * 批量关注二维码
	 */
	@RequestMapping(value = "/qrcode/json",method = RequestMethod.GET)
	@ResponseBody
	public DataBlock qrcodeJson(Long[] ids,HttpServletRequest request, HttpServletResponse response) {
		List<Product> products = productService.findList(ids);
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String url = "";
	        for (Product product:products) {
	        	if ("".equals(url)) {
		        	url = url +"?ids="+product.getId().toString();
	        	} else {
		        	url = url +","+product.getId().toString();
	        	}
	        }
			  url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/member/favorite/addAll.jhtml"+url);
            return DataBlock.success(url,"获取成功");
		} catch (Exception e) {
			return DataBlock.error("获取二维码失败");
		}
	}
	/**
	 * 商品参数页面
	 */
	@RequestMapping(value = "/productParameters/{id}", method = RequestMethod.GET)
	public String productParameters(@PathVariable Long id, String backUrl, Model model, HttpServletRequest request) {
		parameters(id, model);
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/product.jhtml?extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : ""));
		model.addAttribute("link", url);
		return "/app/product/productParameters";
	}

	private void parameters(Long id, Model model) {
		Product product = productService.find(id);
		Cart cart = cartService.getCurrent();
		Member member = memberService.getCurrent();
		if (member != null) {
			if (member.getFavoriteProducts().contains(product)) {
				model.addAttribute("hasFavorite", 0);
			} else {
				model.addAttribute("hasFavorite", 1);
			}
		}
		boolean hasSpecication = product.getSpecifications().size() > 0;
		model.addAttribute("hasSpecication", hasSpecication);
		model.addAttribute("product", product);
		model.addAttribute("cart", cart != null ? cart.getCartItems().size() : 0);
		model.addAttribute("desc", product.getDescriptionapp() != null ? URLEncoder.encode(product.getDescriptionapp()) : "");
		model.addAttribute("type", "parameters");

		TenantModel tenantModel = new TenantModel();
		tenantModel.copyFrom(product.getTenant());

		model.addAttribute("tenant", tenantModel);
		model.addAttribute("status", product.getTenant().getStatus());
		model.addAttribute("isMarketable", product.getIsMarketable());
		model.addAttribute("title", product.getName());
		model.addAttribute("desc", "我在" + product.getTenant().getName() + "发现一个不错的商品，赶快来看看吧！！！");
		model.addAttribute("imgUrl", product.getThumbnail());
	}
}