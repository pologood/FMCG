/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2c;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Setting;
import net.wit.controller.app.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.entity.Product.OrderType;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2cProductController")
@RequestMapping("/app/b2c/product")
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

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;

	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	@Resource(name = "visitRecordServiceImpl")
	private VisitRecordService visitRecordService;

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
		model.copyFrom(product);
		model.bind(product.getGoods());
		visitRecordService.add(memberService.getCurrent(),null,product,null, VisitRecord.VisitType.app);
		return DataBlock.success(model,"执行成功");
	}

	/**
	 * 获取商品属性
     */
	@RequestMapping(value = "/attribute", method = RequestMethod.GET)
	public @ResponseBody DataBlock attribute(Long id){
		Product product=productService.find(id);
		if(product==null){
			return DataBlock.error("无效商品id");
		}
		return DataBlock.success(ProductAttributeModel.bindData(product.getProductCategory().getAttributes(),product),"执行成功");
	}

	/**
	 * 商品详情页首条评价和导购
	 * @param id	商品Id
	 * @return	好评度，评价，导购
     */
	@RequestMapping(value = "/review_guide", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock review_guide(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("无效商品Id");
		}

		Double positivePercent=0.98D;
		Long positiveCount=reviewService.count(null,product,Review.Type.positive,null);

		ReviewModel reviewModel = null;
		Iterator<Review> ite=product.getReviews().iterator();
		if(ite.hasNext()){
			positivePercent=positiveCount*1.0/product.getReviews().size();
			reviewModel = new ReviewModel();
			reviewModel.copyFrom(ite.next());
		}

		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("tenant", Filter.Operator.eq, product.getTenant()));
		List<Employee> employeeList = employeeService.findList(1, filters, null);
		GuideModel guideModel=null;
		if(employeeList.size()>0){
			guideModel=new GuideModel();
			Employee employee=employeeList.get(0);
			guideModel.copyFrom(employee,memberService.findFans(employee.getMember()).size(),memberService.getCurrent());
		}

		Map<String, Object> map = new HashMap<>();
		map.put("positivePercent",positivePercent);
		map.put("review", reviewModel);
		map.put("guide", guideModel);

		return DataBlock.success(map, "执行成功");
	}

	/**
	 * 获取推荐，搭配商品列表
	 * id 商品
	 */
	@RequestMapping(value = "/recommend/{id}", method = RequestMethod.GET)
	public @ResponseBody DataBlock recommend(@PathVariable Long id,Pageable pageable) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
	    Long[] tagIds = {5L};
		List<Tag> tags = tagService.findList(tagIds);
		Page<Product> page = productService.openPage(pageable, product.getTenant(), null, true, true, null, null, tags, null, null, null, null, null, null, OrderType.weight);
	    return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"执行成功");
	}
	
	/**
	 * 邻家好货，指联明商品的商品
	 * id 商品
	 */
	@RequestMapping(value = "/unions/{id}", method = RequestMethod.GET)
	public @ResponseBody DataBlock unions(@PathVariable Long id,Pageable pageable) {
		Tenant tenant = tenantService.find(id);
		if (tenant==null) {
			DataBlock.error("企业ID无效");
		}
		Long [] tagIds = {5L};
		List<Tag> tags = tagService.findList(tagIds);
		Page<Product> page = productService.openPage(pageable, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, OrderType.weight);
		List<ProductListModel> models = new ArrayList<>();
		for (Product product:page.getContent()) {
			ProductListModel model = new ProductListModel();
			model.copyFrom(product);
			Long positiveCount=reviewService.count(null,product,Review.Type.positive,null);
			if(product.getReviews().size()>0){
				model.setPositivePercent(positiveCount*1.0/product.getReviews().size());
			}
			models.add(model);
		}
	    return DataBlock.success(models,page,"执行成功");
	}
	
	/**
	 * 获取指定商家的商品列表
	 * id 商家编号
	 * productCategoryTenantId 商家分类 id
	 * keyword 搜索关键词
	 * tagIds 商品签标
	 * brandId 品牌
	 * startPrice endPrice 介位段
	 * orderType 排序 {综合排序 weight,置顶降序 topDesc, 价格升序 priceAsc,价格降序 priceDesc,销量降序 salesDesc,评分降序 scoreDesc, 日期降序 dateDesc,人气降序 hitsDesc}
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(@PathVariable Long id, Long productCategoryTenantId,String keyword,Long[] tagIds, Long brandId, BigDecimal startPrice, BigDecimal endPrice,OrderType orderType,Pageable pageable, HttpServletRequest request) {
		Tenant tenant = tenantService.find(id);
		if (tenant==null) {
			DataBlock.error("企业ID无效");
		}
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		Brand brand = brandService.find(brandId);
		List<Tag> tags = tagService.findList(tagIds);
		Page<Product> page = productService.findMyPage(tenant,keyword, null, productCategoryTenant, brand, null, tags, null, startPrice, endPrice, true, true, null, null, null,null, orderType, pageable);
		List<ProductListModel> models = new ArrayList<>();
		for (Product product:page.getContent()) {
			ProductListModel model = new ProductListModel();
			model.copyFrom(product);
			Long positiveCount=reviewService.count(null,product,Review.Type.positive,null);
			if(product.getReviews().size()>0){
				model.setPositivePercent(positiveCount*1.0/product.getReviews().size());
			}
			models.add(model);
		}
	    return DataBlock.success(models,page,"执行成功");
	}


	/**
	 * 同城商品列表
	 * productCategoryId 平台分类 id
     * communityId 商圈
	 * keyword 搜索关键词
	 * tagIds 商品签标
	 * brandId 品牌
	 * startPrice endPrice 介位段
	 * isTop,
	 * isGift,
     * isOutOfStock
	 * orderType 排序 {综合排序 weight,置顶降序 topDesc, 价格升序 priceAsc,价格降序 priceDesc,销量降序 salesDesc,评分降序 scoreDesc, 日期降序 dateDesc,人气降序 hitsDesc}
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long productCategoryId,Long communityId,String keyword,Long[] tagIds, Long brandId,  Long areaId, BigDecimal startPrice, BigDecimal endPrice,Boolean isTop,Boolean isGift,Boolean isOutOfStock,OrderType orderType,Pageable pageable, HttpServletRequest request) {
		Page<Product> page = null;
		List<ProductListModel> models = null;
		try {
			ProductCategory productCategory = productCategoryService.find(productCategoryId);
			Brand brand = brandService.find(brandId);
			Area area = areaService.find(areaId);
			List<Tag> tags = tagService.findList(tagIds);
			Set<ProductCategory> productCategories = new HashSet<ProductCategory>();
			if (productCategory!=null) {
                productCategories.add(productCategory);
            }
			Community community = communityService.find(communityId);
			page = productService.openPage(pageable, area, productCategories, brand, null, tags, keyword, startPrice, endPrice, null, null, null,community, orderType);
			models = new ArrayList<>();
			for (Product product : page.getContent()) {
                ProductListModel model = new ProductListModel();
                model.copyFrom(product);
                Long positiveCount = reviewService.count(null, product, Review.Type.positive, null);
                if (product.getReviews().size() > 0) {
                    model.setPositivePercent(positiveCount * 1.0 / product.getReviews().size());
                }
                models.add(model);
            }
		} catch (Exception e) {
			System.out.println("app/b2c/product/list接口异常：");
			e.printStackTrace();
		}
		return DataBlock.success(models, page, "执行成功");
	}

	/**
	 * 首页平台推荐
	 * productCategoryId 平台分类 id
	 * keyword 搜索关键词
	 * tagIds 商品签标
	 * brandId 品牌
	 * startPrice endPrice 介位段
	 * isTop,
	 * isGift,
     * isOutOfStock
	 * orderType 排序 {综合排序 weight,置顶降序 topDesc, 价格升序 priceAsc,价格降序 priceDesc,销量降序 salesDesc,评分降序 scoreDesc, 日期降序 dateDesc,人气降序 hitsDesc}
	 */
	@RequestMapping(value = "/recommend", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock recommend(Long productCategoryId,Long[] tagIds, Long brandId,  Long areaId, BigDecimal startPrice, BigDecimal endPrice,Boolean isTop,Boolean isGift,Boolean isOutOfStock,OrderType orderType,Pageable pageable, HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		Area area = areaService.find(areaId);
		List<Tag> tags = tagService.findList(tagIds);
		Set<ProductCategory> productCategories = new HashSet<ProductCategory>(); 
		if (productCategory!=null) {
			productCategories.add(productCategory);
		}
		Page<Product> page = productService.openPage(pageable, area, productCategories, brand, null, tags, null, startPrice, endPrice, null, null, null,null, orderType);
		return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"执行成功");
	}


	/**
	 * 点击数
	 */
	@RequestMapping(value = "/hits", method = RequestMethod.GET)
	public @ResponseBody DataBlock hits(Long id) {
		return DataBlock.success(productService.viewHits(id),"执行成功");
	}

	/**
	 * 热门搜索
     */
	@RequestMapping(value = "/hot_search", method = RequestMethod.GET)
	public @ResponseBody DataBlock hotSearch(){
		Setting setting=SettingUtils.get();
		return DataBlock.success(setting.getHotSearches(),"执行成功");
	}
}