/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax.member;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.ajax.model.ProductSpecificationModel;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.ProductVo;
import net.wit.entity.Area;
import net.wit.entity.Brand;
import net.wit.entity.Goods;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.MemberRank;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.ProductImage;
import net.wit.entity.Promotion;
import net.wit.entity.Specification;
import net.wit.entity.SpecificationValue;
import net.wit.entity.Tag;
import net.wit.plugin.StoragePlugin;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.FileService;
import net.wit.service.GoodsService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.PluginService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.ProductImageService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SpecificationService;
import net.wit.service.SpecificationValueService;
import net.wit.service.TagService;
import net.wit.util.FreemarkerUtils;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import freemarker.template.TemplateException;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("ajaxMemberProductController")
@RequestMapping("/ajax/member/product")
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

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;  

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;  

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	
	@Resource(name = "specificationServiceImpl")
	private SpecificationService specificationService;

	@Resource(name = "specificationValueServiceImpl")
	private SpecificationValueService specificationValueService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	
	
	@Resource(name = "productDisplay")
	private DisplayEngine<Product, ProductVo> productDisplay;
	
	/**
	 * 根据id获取商品
	 */
	@RequestMapping(value = "/member/rank", method = RequestMethod.GET)
	public @ResponseBody List<MemberRank> member_Rank() {
		return memberRankService.findAll();
	}
	
	/**
	 * 删除商品
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody Message delete(@PathVariable Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return Message.error("该商品不存在！");
		}
		Goods goods = product.getGoods();
		try {
		  goodsService.delete(goods);
		} catch(Exception e) {
			return Message.error("删除失败，已经启用的商品不能删除。");
		}
		return Message.success("success");
	}
	
	/**
	 * 商品上下架  isMarketable = true 为上架
	 */
	@RequestMapping(value = "/marketable/{id}", method = RequestMethod.POST)
	public @ResponseBody Message marketable(@PathVariable Long id,Boolean isMarketable) {
		Product product = productService.find(id);
		if (product == null) {
			return Message.error("该商品不存在！");
		}
		Goods goods = product.getGoods();
		
		for (Iterator<Product> iterator = goods.getProducts().iterator(); iterator.hasNext();) {
			Product gProduct = iterator.next();
			gProduct.setIsMarketable(isMarketable);
		}
		
		try {
		    goodsService.update(goods);
	    } catch(Exception e) {
		    return Message.error("操作出错了。");
	    }
		
		return Message.success("success");
  }
	/**
	 * 根据id获取商品
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> detail(@PathVariable Long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		Product product = productService.find(id);
		if (product == null) {
			data.put("message", Message.error("该商品不存在！"));
		} else {
			data.put("id", product.getId());
			data.put("name", product.getName());
			data.put("fullName", product.getFullName());
			data.put("descriptionapp", product.getDescriptionapp());
			data.put("unit", product.getUnit());
			data.put("price", product.getPrice());
			data.put("stock", product.getStock());
			data.put("wholePrice", product.getWholePrice());
			data.put("specifications", product.getSpecifications());
			data.put("specificationValues", product.getSpecificationValues());
			data.put("productImages",product.getProductImages());
			data.put("productCategory",product.getProductCategory());
			data.put("productCategoryTenant",product.getProductCategoryTenant());
			data.put("tags",product.getTags());
			data.put("fee", product.getFee());
			Map<String, Object> prices = new HashMap<String, Object>();
			for(Map.Entry<MemberRank, BigDecimal> entry:product.getMemberPrice().entrySet()) {
				prices.put(entry.getKey().getId().toString(),entry.getValue());
			}
			data.put("memberPrices",prices);
			List<ProductSpecificationModel> models = new ArrayList<ProductSpecificationModel>();
			
			for (Product pd:product.getSiblings()) {
				ProductSpecificationModel model = new ProductSpecificationModel();
				model.setId(pd.getId());
				model.setPrice(pd.getPrice());
				model.setStock(pd.getStock());
				model.setSpecifications(pd.getSpecifications());
				model.setSpecificationValues(pd.getSpecificationValues());
				models.add(model);
			}
			data.put("products",models);
		}
		return data;
	}

	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody Message checkSn(String previousSn, String sn) {
		if (StringUtils.isEmpty(sn)) {
			return Message.error("ajax.product.sn.empty");
		}
		if (productService.snUnique(previousSn, sn)) {
			return Message.success("ajax.product.sn.enable");
		} else {
			return Message.error("ajax.product.sn.repeat");
		}
	}

	/**
	 * 获取参数组
	 */
	@RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
	public @ResponseBody Message parameterGroups(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return Message.error("ajax.productCategory.NotExist");
		}
		return Message.success(JsonUtils.toJson(productCategory.getParameterGroups()));
	}

	/**
	 * 获取属性组
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody Message attributes(Long id) {
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return Message.error("ajax.productCategory.NotExist");
		}
		return Message.success(JsonUtils.toJson(productCategory.getAttributes()));
	}

	
	/**
	 * 上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> upload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
			if (request == null) {
				data.put("message", Message.warn("admin.upload.error"));
			} else {
				Setting setting = SettingUtils.get();
				String uploadPath;
				String url = null;

					uploadPath = setting.getImageUploadPath();
					data.put("fileType", FileType.image);
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("uuid", UUID.randomUUID().toString());
					String path;
					try {
						path = FreemarkerUtils.process(uploadPath, model);
						String destPath = path + UUID.randomUUID() + ".jpg";
						File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
						if (!tempFile.getParentFile().exists()) {
							tempFile.getParentFile().mkdirs();
						}
						
						// 输入流
						InputStream in = request.getInputStream();
						FileOutputStream out = new FileOutputStream(tempFile);

						byte[] buffer = new byte[1024];
						int length = -1;
						/* 从文件读取数据至缓冲区 */
						while ((length = in.read(buffer)) != -1) {
							/* 将资料写入FileOutputStream中 */
							out.write(buffer, 0, length);
						}
						in.close();
						out.flush();
						out.close();				
						
						StoragePlugin storagePlugin = pluginService.getStoragePlugin("filePlugin");
						storagePlugin.upload(destPath, tempFile,"image/jpeg");
						url = storagePlugin.getUrl(destPath);
						ServletContext servletContext = request.getSession().getServletContext();
						data.put("local",servletContext.getRealPath(destPath));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TemplateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (url == null) {
					data.put("message", Message.warn("admin.upload.error"));
				} else {
					data.put("message", SUCCESS_MESSAGE);
					data.put("url", url);
				}
			}
	    return data;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{productCategoryId}", method = RequestMethod.GET)
	@ResponseBody
	public Message list(@PathVariable Long productCategoryId,Long productCategoryTenantId,String keyword,Long brandId,  Long promotionId, Long[] tagIds,Boolean isMarketable, BigDecimal startPrice, BigDecimal endPrice, Location location, BigDecimal distance, OrderType orderType, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		if (productCategory == null) {
			return Message.error("ajax.productCategory.NotExist");
		}
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		Promotion promotion = promotionService.find(promotionId);
		Brand brand = brandService.find(brandId);
		List<Tag> tags = tagService.findList(tagIds);
		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(15);
		orderType = OrderType.dateDesc; 
		Member member = memberService.getCurrent();
		Page<Product> page = productService.findMyPage(member.getTenant(),keyword, productCategory, productCategoryTenant, brand, promotion, tags, null, startPrice, endPrice, isMarketable, true, null, false, null, null,orderType, pageable);

		Page<ProductVo> pageVo = new Page<ProductVo>(productDisplay.convertList(page.getContent()), page.getTotal(), pageable);
		return Message.success(JsonUtils.toJson(pageVo));
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long productCategoryTagId, Long productCategoryTenantId,String keyword, Long brandId, Long areaId, Long promotionId,Boolean isMarketable, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Location location, BigDecimal distance, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
		Brand brand = brandService.find(brandId);
		Promotion promotion = promotionService.find(promotionId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		List<Tag> tags = tagService.findList(tagIds);
		Area area = areaService.find(areaId);
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(15);
		orderType = OrderType.dateDesc; 
		Page<Product> page = productService.findMyPage(member.getTenant(), keyword,null, productCategoryTenant, brand, promotion, tags, null, startPrice, endPrice, isMarketable, true, null, false, null, null, orderType, pageable);
		Page<ProductVo> pageVo = new Page<ProductVo>(productDisplay.convertList(page.getContent()), page.getTotal(), pageable);
		return Message.success(JsonUtils.toJson(pageVo));
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
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Message save(Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long[] tagIds, Long[] specificationIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("会话过期");
		}
		if (member.getTenant() == null) {
			return Message.error("您还没有开通");
		}
		for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}
		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.setProductCategoryTenant(productCategoryTenantService.find(productCategoryTenantId));
		product.setBrand(brandService.find(brandId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		product.setTenant(member.getTenant());
		if (!isValid(product)) {
			return Message.error("参数传入有错");
		}
		if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
			return Message.error("货号不能重置");
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
		product.setIsMarketable(true);
		if (product.getFee()==null) {
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
			if (productImage.getLocal()!=null) {
			   productImage.setLocalFile(new File(productImage.getLocal()));
			   productImageService.build(productImage);
			}
		}
		
		/**
		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null) {
			product.setImage(product.getThumbnail());
		}

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

		List<PackagUnit> packagUnits = product.getPackagUnits();
		ArrayList<PackagUnit> pkus = new ArrayList<PackagUnit>();
		for (PackagUnit packagUnit : packagUnits) {
			packagUnit.setProduct(product);
			pkus.add(packagUnit);
		}
		product.setPackagUnits(pkus);
        **/
		
		Goods goods = new Goods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			String[] specificationPrice = request.getParameterValues("specification_price");
			String[] specificationStock = request.getParameterValues("specification_stock");
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
								// specificationProduct.setPackagUnits(product.getPackagUnits());
								products.add(specificationProduct);
                                 
								/**
								List<PackagUnit> specificationPackagUnits = product.getPackagUnits();
								ArrayList<PackagUnit> sfpkus = new ArrayList<PackagUnit>();
								for (PackagUnit sfpackagUnit : specificationPackagUnits) {
									PackagUnit spu = new PackagUnit();
									BeanUtils.copyProperties(sfpackagUnit, spu);
									spu.setProduct(specificationProduct);
									sfpkus.add(spu);
								}
								specificationProduct.setPackagUnits(sfpkus);
                                **/
							}
						}
						Product specificationProduct = products.get(j);
						SpecificationValue specificationValue = specificationValueService.findByName(specification,specificationValueIds[j]);
						if (specificationValue==null) {
							specificationValue = new SpecificationValue();
							specificationValue.setName(specificationValueIds[j]);
							specificationValue.setSpecification(specification);
							specificationValueService.save(specificationValue);
						}
						specificationProduct.setPrice(new BigDecimal(specificationPrice[j]));
						specificationProduct.setStock(new Integer(specificationStock[j]));
						if (specificationProduct.getMarketPrice()==null || (specificationProduct.getMarketPrice().compareTo(specificationProduct.getPrice())<0)) {
							specificationProduct.setMarketPrice(specificationProduct.getPrice());
						}
						specificationProduct.setWholePrice(product.getWholePrice());
						
						/**
						for (MemberRank memberRank : memberRankService.findAll()) {
							String price = request.getParameter("memberPrice_" + memberRank.getId());
							if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
								specificationProduct.getMemberPrice().put(memberRank, new BigDecimal(price));
							} else {
								specificationProduct.getMemberPrice().remove(memberRank);
							}
						} 
						**/

						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
					}
				}
			}
		} else {
			product.setGoods(goods);
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			products.add(product);
		}
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.save(goods);

		//if (productCategoryTenantId != null) {
		//	ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		//	productCategoryTenant.getProducts().addAll(products);
		//	productCategoryTenantService.save(productCategoryTenant);
		//}
		
		return Message.success("保存成功");
	}


	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Message update(Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long[] tagIds, Long[] specificationIds, Long[] specificationProductIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("会话已过期，请重新登录");
		}
		
		for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext();) {
			ProductImage productImage = iterator.next();
			if (productImage == null || productImage.isEmpty()) {
				iterator.remove();
				continue;
			}
		}

		product.setProductCategory(productCategoryService.find(productCategoryId));
		product.setProductCategoryTenant(productCategoryTenantService.find(productCategoryTenantId));
		product.setBrand(brandService.find(brandId));
		product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		if (!isValid(product)) {
			return Message.error("商品参数传入有误");
		}
		Product pProduct = productService.find(product.getId());
		if (pProduct == null) {
			System.out.println("product.id="+product.getId());
			System.out.println("product.name="+product.getName());
			return  Message.error("没找到当前修改商品");
		}
		product.setTenant(pProduct.getTenant());
		if (StringUtils.isNotEmpty(product.getSn()) && !productService.snUnique(pProduct.getSn(), product.getSn())) {
			return Message.error("货号重复");
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
			if (productImage.getLocal()!=null) {
				   productImage.setLocalFile(new File(productImage.getLocal()));
				   productImageService.build(productImage);
				}
		}
		Collections.sort(product.getProductImages());
		if (product.getImage() == null && product.getThumbnail() != null) {
			product.setImage(product.getThumbnail());
		}
		if (product.getFee()==null) {
			product.setFee(BigDecimal.ZERO);
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
		
		//Set<ProductCategoryTenant> p_productCategoryTenants = pProduct.getProductCategoryTenants();
		
		Goods goods = pProduct.getGoods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			String[] specificationPrice = request.getParameterValues("specification_price");
			String[] specificationStock = request.getParameterValues("specification_stock");
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
								pProduct.setTags(new HashSet<Tag>(product.getTags()) );
								pProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()) );
								pProduct.setFee(product.getFee());
								//BeanUtils.copyProperties(product, pProduct, new String[] { "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales",
								//		"monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "specifications", "specificationValues", "promotions", "cartItems",
								//		"orderItems", "giftItems", "productNotifies", "packagUnits" });
								pProduct.setSpecifications(new HashSet<Specification>());
								pProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								products.add(pProduct);
							} else {
								if (specificationProductIds != null && j < specificationProductIds.length) {
									Product specificationProduct = productService.find(specificationProductIds[j]);
									if (specificationProduct == null || (specificationProduct.getGoods() != null && !specificationProduct.getGoods().equals(goods))) {
										return Message.error("传入的规格商品无效");
									}
									specificationProduct.setName(product.getName());
									specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
									specificationProduct.setDescriptionapp(product.getDescriptionapp());
									specificationProduct.setUnit(product.getUnit());
									specificationProduct.setTags(new HashSet<Tag>(product.getTags()) );
									specificationProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()) );
									specificationProduct.setFee(product.getFee());
									
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
									specificationProduct.setFullName(null);
									specificationProduct.setAllocatedStock(0);
									specificationProduct.setIsList(false);
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

									/**
									List<PackagUnit> specificationPackagUnits = product.getPackagUnits();
									ArrayList<PackagUnit> sfpkus = new ArrayList<PackagUnit>();
									for (PackagUnit sfpackagUnit : specificationPackagUnits) {
										PackagUnit spu = new PackagUnit();
										spu.setName(sfpackagUnit.getName());
										spu.setPrice(sfpackagUnit.getPrice());
										spu.setWholePrice(sfpackagUnit.getWholePrice());
										spu.setCoefficient(sfpackagUnit.getCoefficient());
										spu.setBarcode(sfpackagUnit.getBarcode());
										spu.setProduct(specificationProduct);
										sfpkus.add(spu);
									}
									specificationProduct.setPackagUnits(sfpkus);
									**/
									
								}
							}
						}
						
						Product specificationProduct = products.get(j);
						SpecificationValue specificationValue = specificationValueService.findByName(specification,specificationValueIds[j]);
						if (specificationValue==null) {
							specificationValue = new SpecificationValue();
							specificationValue.setName(specificationValueIds[j]);
							specificationValue.setSpecification(specification);
							specificationValueService.save(specificationValue);
						}
						specificationProduct.setPrice(new BigDecimal(specificationPrice[j]));
						if (specificationProduct.getMarketPrice()==null || (specificationProduct.getMarketPrice().compareTo(specificationProduct.getPrice())<0)) {
							specificationProduct.setMarketPrice(specificationProduct.getPrice());
						}
						specificationProduct.setStock(new Integer(specificationStock[j]));
						specificationProduct.setWholePrice(product.getWholePrice());
						specificationProduct.setFee(product.getFee());
						specificationProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));

						/**
						for (MemberRank memberRank : memberRankService.findAll()) {
							String price = request.getParameter("memberPrice_" + memberRank.getId());
							if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
								specificationProduct.getMemberPrice().put(memberRank, new BigDecimal(price));
							} else {
								specificationProduct.getMemberPrice().remove(memberRank);
							}
						}**/

						specificationProduct.getSpecifications().add(specification);
						specificationProduct.getSpecificationValues().add(specificationValue);
						specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
						//specificationProduct.setProductCategoryTenants(new HashSet<ProductCategoryTenant>(productCategoryTenantService.findList(productCategoryTenantId)));
						//for (ProductCategoryTenant productCategoryTenant : p_productCategoryTenants) {
						//	productCategoryTenant.getProducts().remove(specificationProduct);
					
					}
				}
			}
		} else {
			pProduct.setName(product.getName());
			pProduct.setProductCategoryTenant(product.getProductCategoryTenant());
			pProduct.setDescriptionapp(product.getDescriptionapp());
			pProduct.setUnit(product.getUnit());
			pProduct.setTags(new HashSet<Tag>(product.getTags()) );
			pProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()) );
            pProduct.setFee(product.getFee());
            pProduct.setMemberPrice(product.getMemberPrice());
            pProduct.setMarketPrice(product.getMarketPrice());
            pProduct.setPrice(product.getPrice());
            pProduct.setSpecifications(null);
            pProduct.setSpecificationValues(null);
 			//BeanUtils.copyProperties(product, pProduct, new String[] { "id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales",
			//		"weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies", "packagUnits" });
			products.add(pProduct);
		}

		/**
		pProduct.getPackagUnits().clear();
		for (PackagUnit p : product.getPackagUnits()) {
			p.setProduct(pProduct);
			pProduct.getPackagUnits().add(p);
		}
        **/
		
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.update(goods);
		//if (productCategoryTenantId != null) {
		//	ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		//	productCategoryTenant.getProducts().addAll(new HashSet<Product>(products));
		//	productCategoryTenantService.save(productCategoryTenant);
		//}
		return SUCCESS_MESSAGE;
	}
	
}