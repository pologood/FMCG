/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.AttributeModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ParameterGroupModel;
import net.wit.controller.app.model.ProductListModel;
import net.wit.controller.app.model.ProductModel;
import net.wit.entity.Product.OrderType;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;

/**
 * Controller - 商品
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberProductController")
@RequestMapping("/assistant/member/product")
public class ProductController extends BaseController {

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;

	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;

	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;


	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name ="extendCatalogServiceImpl")
	private  ExtendCatalogService extendCatalogService;
	@Resource(name ="messageServiceImpl")
	private  MessageService messageService;
	@Resource(name ="unionTenantServiceImpl")
	private  UnionTenantService unionTenantService;
	@Resource(name = "employeeServiceImpl")
	private  EmployeeService employeeService;

	/**
	 * 根据id获取商品详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public
	@ResponseBody
	DataBlock view(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		ProductModel model = new ProductModel();
		model.copyFrom(product);
		String images = null;
		if(product.getDescriptionapp()!=null){
			 images =   getImgs(product.getDescriptionapp());
		}
		model.setDescriptionapp(images);
		model.bind(product.getGoods());
		return DataBlock.success(model, "执行成功");
	}

	/**
	 * 分享地址
	 */
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public
	@ResponseBody
	DataBlock share(Long id, String type) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}


		if (product.getThumbnail()==null) {
			return DataBlock.error("请上传图片再分享");
		}
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = "";
		if ("weixin".equals(type)) {
			url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/product/details.jhtml?id="+id+"&extension=" + (member != null ? member.getUsername() : "")));
		} else if ("preview".equals(type)) {
			url = bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/preview.jhtml";
		} else {
			url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/product/details.jhtml?id="+id+"&extension=" + (member != null ? member.getUsername() : "")));
		}

		Map<String, String> data = new HashMap<String, String>();
		data.put("url", url);
		data.put("title", "亲,“" + product.getTenant().getShortName() + "”为您推荐，快快抢购吧。");
		data.put("thumbnail", product.getThumbnail());
		data.put("description", product.getFullName());

		if(!activityDetailService.isActivity(null,member.getTenant(),activityRulesService.find(43L))){
			activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(43L));
		}

		if(!activityDetailService.isActivity(null,member.getTenant(),activityRulesService.find(44L))){
			activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(44L));
		}

		return DataBlock.success(data, "执行成功");
	}

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public
	@ResponseBody
	DataBlock checkSn(String previousSn, String sn) {
		if (StringUtils.isEmpty(sn)) {
			return DataBlock.error("ajax.product.sn.empty");
		}
		if (productService.snUnique(previousSn, sn)) {
			return DataBlock.success("ajax.product.sn.enable", "ajax.product.sn.enable");
		} else {
			return DataBlock.error("ajax.product.sn.repeat");
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public
	@ResponseBody
	DataBlock delete(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Product product = productService.find(id);
		if (product.getSales().compareTo(0L) > 0) {
			return DataBlock.error("已销售的商品不能删除，只能下架");
		}
		Pageable pageable = new Pageable();
		Page<ExtendCatalog> page = extendCatalogService.findPage(product,pageable);
		if(page.getContent().size()>0){
			return DataBlock.error("已分享的商品不能删除，只能下架");
		}
		try{
			productService.delete(product);
		}catch (Exception e){
			return DataBlock.error("删除失败");
		}


		return DataBlock.success("success", "执行成功");
	}


	/**
	 * 商品上下架  isMarketable = true 为上架
	 */
	@RequestMapping(value = "/set_marketable", method = RequestMethod.POST)
	public
	@ResponseBody
	DataBlock setMarketable(Long id, Boolean isMarketable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant() == null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("该商品不存在");
		}
		Goods goods = product.getGoods();

		for (Iterator<Product> iterator = goods.getProducts().iterator(); iterator.hasNext(); ) {
			Product gProduct = iterator.next();
			gProduct.setIsMarketable(isMarketable);

			if(isMarketable){
				if(gProduct.getSpecifications().size()>0){
					if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(18L))){
						activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(18L));
					}
				}
			}

		}

		try {
			goodsService.update(goods);
		} catch (Exception e) {
			return DataBlock.error("操作出错了。");
		}

		if(isMarketable){
			List<Product> productPage = productService.openList(null,memberService.getCurrent().getTenant(),null,true,null,null,null,null,null,null,null,null,null,null,null,OrderType.dateDesc);
			if(productPage!=null){
				if(productPage.size()>20){
					if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(16L))){
						activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(16L));
					}
				}

				if(productPage.size()>50){
					if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(17L))){
						activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(17L));
					}
				}
			}
		}else {
			//TODO 进入“商品”，下架商品
			if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(19L))){
				activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(19L));
			}
		}
//		if(member.getTenant().getIsUnion()&&isMarketable){
//			List<UnionTenant> unionTenants = unionTenantService.findUnionTenant(member.getTenant().getUnion(), null, null);
//			for(UnionTenant unionTenant:unionTenants){
//				List<Employee> employeeList = employeeService.findList(unionTenant.getTenant(),null);
//				for(Employee employee:employeeList){
//					Message message = new Message();
//					message.setType(Message.Type.activity);
//					message.setCreateDate(new Date());
//					message.setModifyDate(new Date());
//					message.setContent(member.getTenant().getName()+"上传了新商品快来看看吧！");
//					message.setWay(Message.Way.tenant);
//					message.setTitle("新商品");
//					message.setIsDraft(false);
//					message.setSenderRead(true);
//					message.setReceiverRead(false);
//					message.setSenderDelete(false);
//					message.setReceiverDelete(false);
//					message.setSender(member);
//					message.setReceiver(employee.getMember());
//					message.setIp(request.getRemoteAddr());
//					messageService.save(message);
//				}
//			}
//		}
		return DataBlock.success("success", "执行成功");
	}

	/**
	 * 获取参数组
	 */
	@RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
	public
	@ResponseBody
	DataBlock parameterGroups(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("ajax.productCategory.NotExist");
		}
		return DataBlock.success(ParameterGroupModel.bindData(productCategory.getParameterGroups()), "执行成功");
	}

	/**
	 * 获取属性组
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public
	@ResponseBody
	DataBlock attributes(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("ajax.productCategory.NotExist");
		}
		return DataBlock.success(AttributeModel.bindData(productCategory.getAttributes()), "执行成功");
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
	public DataBlock list(@PathVariable Long id, Long productCategoryTenantId, String keyword, Long[] tagIds, Long brandId, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable, HttpServletRequest request) {
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
			model.setPositivePercent(0D);
			Long positiveCount=reviewService.count(null,product,Review.Type.positive,null);
			if(product.getReviews().size()>0){
				model.setPositivePercent(positiveCount*1.0/product.getReviews().size());
			}
			models.add(model);
		}
		return DataBlock.success(models,page,"执行成功");
	}
	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long productCategoryId, Long productCategoryTenantId, String keyword, Long brandId, Long promotionId, Long[] tagIds, Boolean isMarketable, OrderType orderType,Pageable pageable, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant() == null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		Promotion promotion = promotionService.find(promotionId);
		Brand brand = brandService.find(brandId);
		List<Tag> tags = tagService.findList(tagIds);
		orderType = OrderType.dateDesc;
		Page<Product> page = productService.findMyPage(member.getTenant(), keyword, productCategory, productCategoryTenant, brand, promotion, tags, null, null, null, isMarketable, true, null, null, null, null, orderType, pageable);
		return DataBlock.success(ProductListModel.bindData(page.getContent()),page, "执行成功");
	}

	/**
	 * 计算默认市场价
	 *
	 * @param price 价格
	 */
	private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
		return setting.setScale(price.multiply(new BigDecimal(defaultMarketPriceScale.toString())));
	}

	/**
	 * 计算默认积分
	 *
	 * @param price 价格
	 */
	private long calculateDefaultPoint(BigDecimal price) {
		Setting setting = SettingUtils.get();
		Double defaultPointScale = setting.getDefaultPointScale();
		return price.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public
	@ResponseBody
	DataBlock save(Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long[] tagIds, Long[] specificationIds, String[] specificationTitles,Boolean isMarketable, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant() == null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}
			String  html = null;
			String htmlMiddle= null;
			String htmlHead = "<html lang=\"zh-cmn-Hans\"><head>\n" +
					"    <meta charset=\"UTF-8\">\n" +
					"    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,user-scalable=0\">\n" +
					"    <title>预览</title>\n" +
					"    <meta http-equiv=\"Cache-Control\" content=\"no-cache, no-store, must-revalidate\">\n" +
					"    <meta http-equiv=\"Pragma\" content=\"no-cache\">\n" +
					"    <meta http-equiv=\"Expires\" content=\"0\">\n" +
					"    <style type=\"text/css\">\n" +
					"    \tbody,h2 {padding: 2%;margin: 0;font-family: Microsoft YaHei,-apple-system-font,Helvetica Neue,Helvetica,sans-serif;font-size: 14px;line-height: 1;color: #333;}\n" +
					"    \timg {max-width: 100%;height: auto;line-height: 0;display: block;}\n" +
					"    </style>\n" +
					"</head>\n" +
					"<body ontouchstart=\"\">\n" +
					"<!--BEGIN previewIcons-->\n" +
					"<h2>当前为预览模式：</h2>\n" +
					"<div id=\"previewIcons\">";


			 String htmlFoot = "\n" +
					"</div>\n" +
					"<!--END previewIcons-->\n" +
					"\n" +
					"\n" +
					"\n" +
					"</body></html>";
			if(product.getDescriptionapp()!=null){
              String[] images = product.getDescriptionapp().split("@");
				for(int i=0;i<images.length;i++){
					htmlMiddle = "<img src="+images[i]+" />";
				}
				html = htmlHead+htmlMiddle+htmlFoot;
			}else{
				html = htmlHead+htmlFoot;
			}
		product.setDescriptionapp(html);

		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.setProductCategoryTenant(productCategoryTenantService.find(productCategoryTenantId));
		product.setBrand(brandService.find(brandId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		product.setTenant(member.getTenant());
		if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
			return DataBlock.error("货号不能重复");
		}
		if (product.getMarketPrice() == null) {
			BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
			product.setMarketPrice(defaultMarketPrice);
		}
		if (product.getWholePrice() == null) {
			product.setWholePrice(product.getPrice());
		}
		if (product.getPoint() == null) {
			long point = calculateDefaultPoint(product.getPrice());
			product.setPoint(point);
		}
		product.setFullName(null);
		product.setAllocatedStock(0);
		product.setScore(0F);
		product.setTotalScore(0L);
		product.setScoreCount(0L);
		product.setPriority(0L);
		product.setHits(0L);
		product.setIsList(true);
		product.setIsGift(false);
		product.setIsTop(false);
		product.setWeekHits(0L);
		product.setMonthHits(0L);
		product.setSales(0L);
		product.setWeekSales(0L);
		product.setMonthSales(0L);
		product.setWeekHitsDate(new Date());
		product.setMonthHitsDate(new Date());
		product.setWeekSalesDate(new Date());
		product.setMonthSalesDate(new Date());
		product.setReviews(null);
		product.setConsultations(null);
		product.setFavoriteMembers(null);
		product.setPromotions(null);
		product.setCartItems(null);
		product.setOrderItems(null);
		product.setGiftItems(null);
		product.setProductNotifies(null);
		product.setIsMarketable(isMarketable);
		if (product.getFee() == null) {
			product.setFee(BigDecimal.ZERO);
		}

		for (MemberRank memberRank : memberRankService.findAll()) {
			String price = request.getParameter("memberPrice_" + memberRank.getId());
			if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
				product.getMemberPrice().put(memberRank, new BigDecimal(price));
			} else {
				product.getMemberPrice().remove(memberRank);
			}
		}

		for (ProductImage productImage : product.getProductImages()) {
			if (productImage.getLocal() != null) {
				productImage.setLocalFile(new File(productImage.getLocal()));
				productImageService.build(productImage);
			}
		}

		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null) {
			product.setImage(product.getThumbnail());
		}

		/**
		 for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
		 for (Parameter parameter : parameterGroup.getParameters()) {
		 String parameterValue = request.getParameter("parameter_" + parameter.getId());
		 if (StringUtils.isNotEmpty(parameterValue)) {
		 product.getParameterValue().put(parameter, parameterValue);
		 } else {
		 product.getParameterValue().remove(parameter);
		 }
		 }
		 }

		 for (Attribute attribute : product.getProductCategory().getAttributes()) {
		 String attributeValue = request.getParameter("attribute_" + attribute.getId());
		 if (StringUtils.isNotEmpty(attributeValue)) {
		 product.setAttributeValue(attribute, attributeValue);
		 } else {
		 product.setAttributeValue(attribute, null);
		 }
		 }
		 **/

		if (!isValid(product)) {
			return DataBlock.error("参数传入有错");
		}

		Goods goods = new Goods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			String[] specificationPrice = request.getParameterValues("specification_price");
			String[] specificationStock = request.getParameterValues("specification_stock");
			String[] specificationBarcode = request.getParameterValues("specification_barcode");
			String[] specificationMarketPrice = request.getParameterValues("specification_market_price");

			String[] barcodes = request.getParameterValues("barcode");
			for (int i = 0; i < specificationIds.length; i++) {
				Specification specification = specificationService.find(specificationIds[i]);
				String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
				if (specificationValueIds != null && specificationValueIds.length > 0) {
					for (int j = 0; j < specificationValueIds.length; j++) {
						if (i == 0) {
							if (j == 0) {
								product.setGoods(goods);
								product.setSpecifications(new HashSet<Specification>());
								product.setSpecificationValues(new HashSet<SpecificationValue>());
								products.add(product);
							} else {
								Product specificationProduct = new Product();
								BeanUtils.copyProperties(product, specificationProduct);
								specificationProduct.setId(null);
								specificationProduct.setCreateDate(null);
								specificationProduct.setModifyDate(null);
								specificationProduct.setSn(null);
								specificationProduct.setFullName(null);
								specificationProduct.setAllocatedStock(0);
								specificationProduct.setIsList(false);
								specificationProduct.setScore(0F);
								specificationProduct.setPriority(0L);
								specificationProduct.setTotalScore(0L);
								specificationProduct.setScoreCount(0L);
								specificationProduct.setHits(0L);
								specificationProduct.setWeekHits(0L);
								specificationProduct.setMonthHits(0L);
								specificationProduct.setSales(0L);
								specificationProduct.setWeekSales(0L);
								specificationProduct.setMonthSales(0L);
								specificationProduct.setWeekHitsDate(new Date());
								specificationProduct.setMonthHitsDate(new Date());
								specificationProduct.setWeekSalesDate(new Date());
								specificationProduct.setMonthSalesDate(new Date());
								specificationProduct.setGoods(goods);
								specificationProduct.setReviews(null);
								specificationProduct.setConsultations(null);
								specificationProduct.setFavoriteMembers(null);
								specificationProduct.setSpecifications(new HashSet<Specification>());
								specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								specificationProduct.setPromotions(null);
								specificationProduct.setCartItems(null);
								specificationProduct.setOrderItems(null);
								specificationProduct.setGiftItems(null);
								specificationProduct.setProductNotifies(null);
								products.add(specificationProduct);
							}
						}
						Product specificationProduct = products.get(j);
						specificationProduct.setOrder(j);
						SpecificationValue specificationValue = specificationValueService.findByName(specification, specificationValueIds[j]);
						if (specificationValue == null) {
							specificationValue = new SpecificationValue();
							specificationValue.setName(specificationValueIds[j]);
							specificationValue.setSpecification(specification);
							specificationValue.setProducts(null);
							specificationValueService.save(specificationValue);
						}
						specificationProduct.setPrice(new BigDecimal(specificationPrice[j].trim()));
						specificationProduct.setStock(new Integer(specificationStock[j].trim()));
						//specificationProduct.setWholePrice(product.getWholePrice());
						specificationProduct.setFee(product.getFee());
						specificationProduct.setBarcode(specificationBarcode[j]);
						//if (specificationProduct.getMarketPrice()==null || (specificationProduct.getMarketPrice().compareTo(specificationProduct.getPrice())<0)) {
						if (specificationMarketPrice!=null && specificationMarketPrice.length>0) {
							specificationProduct.setMarketPrice(new BigDecimal(specificationMarketPrice[j].trim()));
						} else {
							specificationProduct.setMarketPrice(product.getMarketPrice());
						}
						//}
						specificationProduct.setWholePrice(product.getWholePrice());
						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
					}
				}
			}
		} else {
			product.setOrder(1);
			product.setGoods(goods);
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			products.add(product);
		}

		if (specificationTitles!=null) {
			if (specificationTitles.length==0) {
				goods.setSpecification1Title(null);
				goods.setSpecification2Title(null);
			} else if (specificationTitles.length==1) {
				goods.setSpecification1Title(specificationTitles[0]);
				goods.setSpecification2Title(null);
			}
			if (specificationTitles.length==2) {
				goods.setSpecification1Title(specificationTitles[0]);
				goods.setSpecification2Title(specificationTitles[1]);
			}
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.save(goods);

		if(!activityDetailService.isActivity(null,member.getTenant(),activityRulesService.find(49L))){
			activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(49L));
		}
//		if(member.getTenant().getIsUnion()){
//			List<UnionTenant> unionTenants = unionTenantService.findUnionTenant(member.getTenant().getUnion(), null, null);
//			for(UnionTenant unionTenant:unionTenants){
//				List<Employee> employeeList = employeeService.findList(unionTenant.getTenant(),null);
//				for(Employee employee:employeeList){
//					Message message = new Message();
//					message.setType(Message.Type.activity);
//					message.setCreateDate(new Date());
//					message.setModifyDate(new Date());
//					message.setContent(member.getTenant().getName()+"上传了新商品快来看看吧！");
//					message.setWay(Message.Way.tenant);
//					message.setTitle("新商品");
//					message.setIsDraft(false);
//					message.setSenderRead(true);
//					message.setReceiverRead(false);
//					message.setSenderDelete(false);
//					message.setReceiverDelete(false);
//					message.setSender(member);
//					message.setReceiver(employee.getMember());
//					message.setIp(request.getRemoteAddr());
//					messageService.save(message);
//				}
//			}
//		}

		return DataBlock.success("success", "保存成功");
	}


	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public
	@ResponseBody
	DataBlock update(Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long[] tagIds, Long[] specificationIds,String [] specificationTitles, Long[] specificationProductIds,Boolean isMarketable, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant() == null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}

		for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}
		String  html = null;
		String htmlMiddle= null;
		String htmlHead = "<html lang=\"zh-cmn-Hans\"><head>\n" +
				"    <meta charset=\"UTF-8\">\n" +
				"    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1,user-scalable=0\">\n" +
				"    <title>预览</title>\n" +
				"    <meta http-equiv=\"Cache-Control\" content=\"no-cache, no-store, must-revalidate\">\n" +
				"    <meta http-equiv=\"Pragma\" content=\"no-cache\">\n" +
				"    <meta http-equiv=\"Expires\" content=\"0\">\n" +
				"    <style type=\"text/css\">\n" +
				"    \tbody,h2 {padding: 2%;margin: 0;font-family: Microsoft YaHei,-apple-system-font,Helvetica Neue,Helvetica,sans-serif;font-size: 14px;line-height: 1;color: #333;}\n" +
				"    \timg {max-width: 100%;height: auto;line-height: 0;display: block;}\n" +
				"    </style>\n" +
				"</head>\n" +
				"<body ontouchstart=\"\">\n" +
				"<!--BEGIN previewIcons-->\n" +
				"<h2>当前为预览模式：</h2>\n" +
				"<div id=\"previewIcons\">";


		String htmlFoot = "\n" +
				"</div>\n" +
				"<!--END previewIcons-->\n" +
				"\n" +
				"\n" +
				"\n" +
				"</body></html>";
		if(product.getDescriptionapp()!=null){
			String[] images = product.getDescriptionapp().split("@");
			for(int i=0;i<images.length;i++){
				htmlMiddle += "<img src="+images[i]+" />";
			}
			html = htmlHead+htmlMiddle+htmlFoot;
		}else{
			html = htmlHead+htmlFoot;
		}
		product.setDescriptionapp(html);
		product.setIsMarketable(isMarketable);
		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.setProductCategoryTenant(productCategoryTenantService.find(productCategoryTenantId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		product.setBrand(brandService.find(brandId));
		Product pProduct = productService.find(product.getId());
		if (pProduct == null) {
			return DataBlock.error("没找到当前商品,可以已被删除");
		}
		for (Tag tag : pProduct.getTags()) {
			if (tag.getId().compareTo(10L) > 0 && !product.getTags().contains(tag)) {
				product.getTags().add(tag);
			}
		}
		product.setTenant(pProduct.getTenant());
		if (StringUtils.isNotEmpty(product.getSn()) && !productService.snUnique(pProduct.getSn(), product.getSn())) {
			return DataBlock.error("货号不能重复");
		}
		if (product.getMarketPrice() == null) {
			BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
			product.setMarketPrice(defaultMarketPrice);
		}
		if (product.getWholePrice() == null) {
			product.setWholePrice(product.getPrice());
		}
		if (product.getPoint() == null) {
			long point = calculateDefaultPoint(product.getPrice());
			product.setPoint(point);
		}

		for (MemberRank memberRank : memberRankService.findAll()) {
			String price = request.getParameter("memberPrice_" + memberRank.getId());
			if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
				product.getMemberPrice().put(memberRank, new BigDecimal(price));
			} else {
				product.getMemberPrice().remove(memberRank);
			}
		}

		for (ProductImage productImage : product.getProductImages()) {
			if (productImage.getLocal() != null) {
				productImage.setLocalFile(new File(productImage.getLocal()));
				productImageService.build(productImage);
			}
		}
		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null) {
			product.setImage(product.getThumbnail());
		}

		if (product.getFee() == null) {
			product.setFee(BigDecimal.ZERO);
		}

		Goods goods = pProduct.getGoods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			String[] specificationPrice = request.getParameterValues("specification_price");
			String[] specificationStock = request.getParameterValues("specification_stock");
			String[] specificationBarcode = request.getParameterValues("specification_barcode");
			String[] specificationMarketPrice = request.getParameterValues("specification_market_price");
			for (int i = 0; i < specificationIds.length; i++) {
				Specification specification = specificationService.find(specificationIds[i]);
				String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
				if (specificationValueIds != null && specificationValueIds.length > 0) {
					for (int j = 0; j < specificationValueIds.length; j++) {
						if (i == 0) {
							if (j == 0) {
								pProduct.setName(product.getName());
								pProduct.setProductCategoryTenant(product.getProductCategoryTenant());
								pProduct.setDescriptionapp(product.getDescriptionapp());
								pProduct.setUnit(product.getUnit());
								pProduct.setTags(new HashSet<Tag>(product.getTags()));
								pProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()));
								pProduct.setFee(product.getFee());
								pProduct.setMarketPrice(product.getMarketPrice());
								pProduct.setSpecifications(new HashSet<Specification>());
								pProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								//pProduct.setSupplier(product.getSupplier());
								pProduct.setCost(product.getCost());
								pProduct.setFee(product.getFee());
								products.add(pProduct);
							} else {
								if (specificationProductIds != null && j < specificationProductIds.length) {
									Product specificationProduct = productService.find(specificationProductIds[j]);
									if (specificationProduct == null || (specificationProduct.getGoods() != null && !specificationProduct.getGoods().equals(goods))) {
										return DataBlock.error("传入的规格id无效");
									}
									specificationProduct.setName(product.getName());
									specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
									specificationProduct.setDescriptionapp(product.getDescriptionapp());
									specificationProduct.setUnit(product.getUnit());
									specificationProduct.setTags(new HashSet<Tag>(product.getTags()));
									specificationProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()));
									//specificationProduct.setSupplier(product.getSupplier());
									specificationProduct.setCost(product.getCost());
									specificationProduct.setFee(product.getFee());
									specificationProduct.setMarketPrice(product.getMarketPrice());
									specificationProduct.setIsMarketable(true);
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									products.add(specificationProduct);
								} else {
									Product specificationProduct = new Product();
									BeanUtils.copyProperties(product, specificationProduct);
									specificationProduct.setId(null);
									specificationProduct.setCreateDate(null);
									specificationProduct.setModifyDate(null);
									specificationProduct.setSn(null);
									specificationProduct.setAllocatedStock(0);
									specificationProduct.setIsList(false);
									specificationProduct.setIsTop(pProduct.getIsTop());
									specificationProduct.setIsGift(pProduct.getIsGift());
									specificationProduct.setIsMarketable(true);
									specificationProduct.setScore(0F);
									specificationProduct.setTotalScore(0L);
									specificationProduct.setScoreCount(0L);
									specificationProduct.setHits(0L);
									specificationProduct.setWeekHits(0L);
									specificationProduct.setMonthHits(0L);
									specificationProduct.setSales(0L);
									specificationProduct.setWeekSales(0L);
									specificationProduct.setMonthSales(0L);
									specificationProduct.setWeekHitsDate(new Date());
									specificationProduct.setMonthHitsDate(new Date());
									specificationProduct.setWeekSalesDate(new Date());
									specificationProduct.setMonthSalesDate(new Date());
									specificationProduct.setGoods(goods);
									specificationProduct.setReviews(null);
									//specificationProduct.setSupplier(product.getSupplier());
									specificationProduct.setCost(product.getCost());
									specificationProduct.setFee(product.getFee());
									specificationProduct.setConsultations(null);
									specificationProduct.setFavoriteMembers(null);
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									specificationProduct.setPromotions(null);
									specificationProduct.setCartItems(null);
									specificationProduct.setOrderItems(null);
									specificationProduct.setGiftItems(null);
									specificationProduct.setProductNotifies(null);
									products.add(specificationProduct);
								}
							}
						}

						Product specificationProduct = products.get(j);
						specificationProduct.setOrder(j);
						SpecificationValue specificationValue = specificationValueService.findByName(specification, specificationValueIds[j]);
						if (specificationValue == null) {
							specificationValue = new SpecificationValue();
							specificationValue.setName(specificationValueIds[j]);
							specificationValue.setSpecification(specification);
							specificationValue.setProducts(null);
							specificationValueService.save(specificationValue);
						}
						specificationProduct.setPrice(new BigDecimal(specificationPrice[j].trim()));
						specificationProduct.setStock(new Integer(specificationStock[j].trim()));
						specificationProduct.setWholePrice(product.getWholePrice());
						specificationProduct.setFee(product.getFee());
						specificationProduct.setBarcode(specificationBarcode[j]);
						//specificationProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));
						if (specificationMarketPrice!=null && specificationMarketPrice.length>0) {
							specificationProduct.setMarketPrice(new BigDecimal(specificationMarketPrice[j].trim()));
						} else {
							specificationProduct.setMarketPrice(product.getMarketPrice());
						}

						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
						specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
					}
				}
			}
		} else {
			pProduct.setName(product.getName());
			pProduct.setProductCategoryTenant(product.getProductCategoryTenant());
			pProduct.setDescriptionapp(product.getDescriptionapp());
			pProduct.setUnit(product.getUnit());
			pProduct.setTags(new HashSet<Tag>(product.getTags()));
			pProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()));
			pProduct.setFee(product.getFee());
			pProduct.setMemberPrice(product.getMemberPrice());
			pProduct.setMarketPrice(product.getMarketPrice());
			pProduct.setBarcode(product.getBarcode());
			pProduct.setIsMarketable(true);
			pProduct.setPrice(product.getPrice());
			pProduct.setWholePrice(product.getWholePrice());
			//pProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));
			pProduct.setSpecifications(null);
			pProduct.setSpecificationValues(null);
			pProduct.setStock(product.getStock());
			//pProduct.setSupplier(product.getSupplier());
			pProduct.setCost(product.getCost());
			pProduct.setFee(product.getFee());
			pProduct.setOrder(1);
			products.add(pProduct);
		}

		if (specificationTitles!=null) {
			if (specificationTitles.length==0) {
				goods.setSpecification1Title(null);
				goods.setSpecification2Title(null);
			} else if (specificationTitles.length==1) {
				goods.setSpecification1Title(specificationTitles[0]);
				goods.setSpecification2Title(null);
			}
			if (specificationTitles.length==2) {
				goods.setSpecification1Title(specificationTitles[0]);
				goods.setSpecification2Title(specificationTitles[1]);
			}
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		for (Product tempProduct : goods.getProducts()) {
			tempProduct.setFullName(null);
		}
		goodsService.update(goods);

		return DataBlock.success("success", "修改成功");
	}

	/**
	 * 取出html中img的路径
	 * @param content
     * @return
     */
	private String getImgs(String content) {
		String img = "";
		Pattern p_image;
		Matcher m_image;
		String str = "";
		String regEx_img = "(<img.*src\\s*=\\s*(.*?)[^>]*?>)";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(content);
		while (m_image.find()) {
			img = m_image.group();
			Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)")
					.matcher(img);
			while (m.find()) {
				String tempSelected = m.group(1);
				if ("".equals(str)) {
					str = tempSelected;
				} else {
					String temp = tempSelected;
					str = str + "@" + temp;
				}
			}
		}
		return str;
	}

}