/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.model.AttributeModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ParameterGroupModel;
import net.wit.controller.app.model.ProductModel;
import net.wit.controller.pos.model.ProductListModel;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.ProductVo;
import net.wit.entity.Brand;
import net.wit.entity.Goods;
import net.wit.entity.Location;
import net.wit.entity.MemberRank;
import net.wit.entity.Parameter;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.ProductImage;
import net.wit.entity.ProductItem;
import net.wit.entity.Promotion;
import net.wit.entity.Specification;
import net.wit.entity.SpecificationValue;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.GoodsService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.ProductImageService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SearchService;
import net.wit.service.SpecificationService;
import net.wit.service.SpecificationValueService;
import net.wit.service.TagService;
import net.wit.service.TenantService;
import net.wit.util.SettingUtils;

/**
 * Controller - 商品
 * @author rsico Team
 * @version 3.0
 */
@Controller("posProductsController")
@RequestMapping("/pos/products")
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

	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
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
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "productDisplay")
	private DisplayEngine<Product, ProductVo> productDisplay;


	/**
	 * 根据id获取商品详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody DataBlock view(Long tenantId,String key,Long id) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		ProductModel model = new ProductModel();
		model.copyFrom(product);
		model.bind(product.getGoods());
		return DataBlock.success(model,"success");
	}
	

	/**
	 * 根据id获取商品详情
	 */
	@RequestMapping(value = "/view_sn", method = RequestMethod.GET)
	public @ResponseBody DataBlock viewSn(Long tenantId,String key,String sn) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant==null) {
        	return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		String myKey = DigestUtils.md5Hex(tenantId.toString()+sn+bundle.getString("appKey"));
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		Product product = productService.findBySn(sn);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		ProductModel model = new ProductModel();
		model.copyFrom(product);
		model.bind(product.getGoods());
		return DataBlock.success(model,"success");
	}
	
	/**
	 * 检查编号是否唯一
	 */
	@RequestMapping(value = "/check_sn", method = RequestMethod.GET)
	public @ResponseBody DataBlock checkSn(Long tenantId,String key,String previousSn, String sn) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+sn+previousSn+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		if (StringUtils.isEmpty(sn)) {
			return DataBlock.error("ajax.product.sn.empty");
		}
		if (productService.snUnique(previousSn, sn)) {
			return DataBlock.success("ajax.product.sn.enable","ajax.product.sn.enable");
		} else {
			return DataBlock.error("ajax.product.sn.repeat");
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody DataBlock delete(Long tenantId,String key,Long id) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Product product = productService.find(id);
		if (product.getSales().compareTo(0L)>0) {
			return DataBlock.error("已销售的商品不能删除，只能下架");
		}
		if (product.getIsList()) {
			goodsService.delete(product.getGoods());
		} else {
		    productService.delete(product);
		}
		return DataBlock.success("success", "success");
	}


	/**
	 * 删除
	 */
	@RequestMapping(value = "/canDelete", method = RequestMethod.POST)
	public @ResponseBody DataBlock canDelete(Long tenantId,String key,Long id) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Product product = productService.find(id);
		if (product.getSales().compareTo(0L)>0) {
			return DataBlock.error("已销售的商品不能删除，只能下架");
		}
		return DataBlock.success("success", "可删除");
	}
	
	/**
	 * 商品上下架  isMarketable = true 为上架
	 */
	@RequestMapping(value = "/set_marketable", method = RequestMethod.POST)
	public @ResponseBody DataBlock setMarketable(Long tenantId,String key,Long id,String sn,Boolean isMarketable) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
	        Product product = null;
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			if (sn!=null) {
				String myKey = DigestUtils.md5Hex(tenantId.toString()+sn+bundle.getString("appKey"));
				if (!myKey.equals(key)) {
					return DataBlock.error("通讯密码无效");
				}
				product = productService.findBySn(sn);
			} else {
				String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
				if (!myKey.equals(key)) {
					return DataBlock.error("通讯密码无效");
				}
				product = productService.find(id);
			}
		if (product == null) {
			return DataBlock.success("success","success");
		}
		Goods goods = product.getGoods();
		
		for (Iterator<Product> iterator = goods.getProducts().iterator(); iterator.hasNext();) {
			Product gProduct = iterator.next();
			gProduct.setIsMarketable(isMarketable);
		}
		
		try {
		    goodsService.update(goods);
	    } catch(Exception e) {
		    return DataBlock.error("操作出错了。");
	    }
		
		return DataBlock.success("success","success");
    }

	/**
	 * 商品上下架  isMarketable = true 为上架
	 */
	@RequestMapping(value = "/isMarketable", method = RequestMethod.POST)
	public @ResponseBody DataBlock isMarketable(Long tenantId,String key,String sn,Boolean isMarketable) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+sn+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		Product product = productService.findBySn(sn);
		if (product == null) {
			return DataBlock.success("success","success");
		}
		productService.delete(product);
		return DataBlock.success("success","success");
    }
	
	/**
	 * 获取参数组
	 */
	@RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
	public @ResponseBody DataBlock parameterGroups(Long tenantId,String key,Long id) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("ajax.productCategory.NotExist");
		}
		return DataBlock.success(ParameterGroupModel.bindData(productCategory.getParameterGroups()),"success");
	}

	/**
	 * 获取属性组
	 */
	@RequestMapping(value = "/attributes", method = RequestMethod.GET)
	public @ResponseBody DataBlock attributes(Long tenantId,String key,Long id) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+id.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		ProductCategory productCategory = productCategoryService.find(id);
		if (productCategory == null) {
			return DataBlock.error("ajax.productCategory.NotExist");
		}
		return DataBlock.success(AttributeModel.bindData(productCategory.getAttributes()),"success");
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long tenantId,String key,Long productCategoryId,Long productCategoryTenantId,String keyword,Long brandId,  Long promotionId, Long[] tagIds,Boolean isMarketable, BigDecimal startPrice, BigDecimal endPrice, Location location, BigDecimal distance,Boolean isGift, OrderType orderType, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = "";
			if (productCategoryTenantId==null) {
				myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
			} else {
				myKey = DigestUtils.md5Hex(tenantId.toString()+productCategoryTenantId.toString()+bundle.getString("appKey"));
			}
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		Promotion promotion = promotionService.find(promotionId);
		Brand brand = brandService.find(brandId);
		List<Tag> tags = tagService.findList(tagIds);
		Pageable pageable = new Pageable();
		pageable.setPageNumber(pageNumber);
		pageable.setPageSize(pageSize);
		orderType = OrderType.dateDesc; 
		Page<Product> page = productService.findMyPage(tenant,keyword, productCategory, productCategoryTenant, brand, promotion, tags, null, startPrice, endPrice, isMarketable, true, null, isGift, null, null,orderType, pageable);
        return DataBlock.success(ProductListModel.bindData(page.getContent()),"success");
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
	public @ResponseBody DataBlock save(Long tenantId,String key,Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long[] tagIds, Long[] specificationIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
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
		product.setTenant(tenant);
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
		product.setIsList(true);
		product.setIsGift(false);
		product.setIsTop(false);
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

		Goods goods = new Goods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			String[] specificationPrice = request.getParameterValues("specification_price");
			String[] specificationStock = request.getParameterValues("specification_stock");
			String[] specificationBarcode = request.getParameterValues("specification_barcode");
			String[] specificationCost = request.getParameterValues("specification_cost");
			String[] specificationWholePrice = request.getParameterValues("specification_wholePrice");
			String[] specificationMarketPrice = request.getParameterValues("specification_marketPrice");
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
						SpecificationValue specificationValue = specificationValueService.findByName(specification,specificationValueIds[j]);
						if (specificationValue==null) {
							specificationValue = new SpecificationValue();
							specificationValue.setName(specificationValueIds[j]);
							specificationValue.setSpecification(specification);
							specificationValue.setProducts(null);
							specificationValueService.save(specificationValue);
						}
						
						for (MemberRank memberRank : memberRankService.findAll()) {
							String[] specificationMemberPrice = request.getParameterValues("specification_memberPrice_" + memberRank.getId());
							if (specificationMemberPrice!=null && specificationMemberPrice.length>0) {
								String price = specificationMemberPrice[j];
								if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
									specificationProduct.getMemberPrice().put(memberRank, new BigDecimal(price));
								} else {
									specificationProduct.getMemberPrice().remove(memberRank);
								}
							}
						}
						
						specificationProduct.setMarketPrice(product.getMarketPrice());
						specificationProduct.setPrice(new BigDecimal(specificationPrice[j]));
						specificationProduct.setStock(new Integer(specificationStock[j]));
						if (specificationCost!=null && specificationCost.length>0) {
						   specificationProduct.setCost(new BigDecimal(specificationCost[j]));
						}
						specificationProduct.setWholePrice(product.getWholePrice());
						if (specificationWholePrice!=null && specificationWholePrice.length>0) {
							   specificationProduct.setWholePrice(new BigDecimal(specificationWholePrice[j]));
							}
						if (specificationMarketPrice!=null && specificationMarketPrice.length>0) {
							   specificationProduct.setMarketPrice(new BigDecimal(specificationMarketPrice[j]));
							}
						specificationProduct.setOrder(j);
						specificationProduct.setBarcode(specificationBarcode[j]);
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
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		goodsService.save(goods);
		return DataBlock.success(product.getSn(),"保存成功");
	}


	/**
	 * 添加规格
	 */
	@RequestMapping(value = "/sync_spec")
	public @ResponseBody DataBlock syncSpec(Long tenantId,String key,String sn,String [] alls,String productSn,String spec,String color,String barcode,BigDecimal marketPrice,BigDecimal price,Integer stock ,HttpServletRequest request, RedirectAttributes redirectAttributes) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
	        
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+sn+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
			
			Product product = productService.findBySn(sn);
			if (product==null) { 
				for (String al:alls) {
					product = productService.findBySn(al);
					if (product!=null) {
						break;
					}
				}
				if (product==null) { 
				    return DataBlock.success(null,"没关联上，需清除所有规格的关联");
				}
			}
			Goods goods = product.getGoods();
			Product specificationProduct = null;
			
            if (productSn!=null) { //不为空时，修改
            	specificationProduct = productService.findBySn(productSn);
            }
            if (specificationProduct!=null) {
            	
            	specificationProduct.getSpecifications().clear();
            	specificationProduct.getSpecificationValues().clear();
    			if (spec!=null) {
    				Specification specification1 = specificationService.find(1L);
    				SpecificationValue specification1Value = specificationValueService.findByName(specification1,spec);
    				if (specification1Value==null) {
    					specification1Value = new SpecificationValue();
    					specification1Value.setName(spec);
    					specification1Value.setSpecification(specification1);
    					specification1Value.setProducts(null);
    					specificationValueService.save(specification1Value);
    				}
    				specificationProduct.getSpecifications().add(specification1);
    				specificationProduct.getSpecificationValues().add(specification1Value);
    			}
    			
    			if (color!=null) {
    				Specification specification2 = specificationService.find(2L);
    				SpecificationValue specification2Value = specificationValueService.findByName(specification2,color);
    				if (specification2Value==null) {
    					specification2Value = new SpecificationValue();
    					specification2Value.setName(color);
    					specification2Value.setSpecification(specification2);
    					specification2Value.setProducts(null);
    					specificationValueService.save(specification2Value);
    				}
    				specificationProduct.getSpecifications().add(specification2);
    				specificationProduct.getSpecificationValues().add(specification2Value);
    			}
    			
    			specificationProduct.setPrice(price);
    			specificationProduct.setStock(stock);
    			specificationProduct.setWholePrice(price);
    			specificationProduct.setMarketPrice(marketPrice);
    			specificationProduct.setBarcode(barcode);
    			specificationProduct.setFee(product.getFee());
    			specificationProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));
    		    
    		    
            } else {
        	    specificationProduct = new Product();
        		specificationProduct.setId(null);
        		specificationProduct.setCreateDate(null);
        		specificationProduct.setModifyDate(null);
        		specificationProduct.setSn(null);
        		specificationProduct.setAllocatedStock(0);
        		specificationProduct.setIsList(false);
        		specificationProduct.setIsTop(product.getIsTop());
        		specificationProduct.setIsGift(product.getIsGift());
        		specificationProduct.setIsMarketable(true);
        		specificationProduct.setScore(0F);
        		specificationProduct.setTotalScore(0L);
        		specificationProduct.setScoreCount(0L);
        		specificationProduct.setHits(0L);
				specificationProduct.setPoint(0L);
        		specificationProduct.setWeekHits(0L);
        		specificationProduct.setMonthHits(0L);
        		specificationProduct.setSales(0L);
        		specificationProduct.setWeekSales(0L);
        		specificationProduct.setMonthSales(0L);
        		specificationProduct.setWeekHitsDate(new Date());
        		specificationProduct.setMonthHitsDate(new Date());
        		specificationProduct.setWeekSalesDate(new Date());
        		specificationProduct.setMonthSalesDate(new Date());
        		specificationProduct.setGoods(product.getGoods());
        		specificationProduct.setReviews(null);
        		specificationProduct.setConsultations(null);
        		specificationProduct.setFavoriteMembers(null);
        		specificationProduct.setSpecifications(new HashSet<Specification>());
        		specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
        		specificationProduct.setPromotions(null);
        		specificationProduct.setCartItems(null);
        		specificationProduct.setOrderItems(null);
        		specificationProduct.setGiftItems(null);
        		
				specificationProduct.setName(product.getName());
				specificationProduct.setProductCategory(product.getProductCategory());
				specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
				specificationProduct.setDescriptionapp(product.getDescriptionapp());
				specificationProduct.setUnit(product.getUnit());
				specificationProduct.setTags(new HashSet<Tag>(product.getTags()) );
				specificationProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()) );
				specificationProduct.setFee(product.getFee());
				specificationProduct.setIsGift(product.getIsGift());
				specificationProduct.setIsList(false);
				specificationProduct.setIsTop(product.getIsTop());
				specificationProduct.setIsMarketable(true);
				specificationProduct.setMarketPrice(product.getMarketPrice());
				specificationProduct.setBrand(product.getBrand());
				specificationProduct.setTenant(product.getTenant());
				specificationProduct.setSupplier(product.getSupplier());
				specificationProduct.setCost(product.getCost());
       		
        		if (spec!=null) {
        			Specification specification1 = specificationService.find(1L);
        			SpecificationValue specification1Value = specificationValueService.findByName(specification1,spec);
        			if (specification1Value==null) {
        				specification1Value = new SpecificationValue();
        				specification1Value.setName(spec);
        				specification1Value.setSpecification(specification1);
        				specification1Value.setProducts(null);
        				specificationValueService.save(specification1Value);
        			}
        			specificationProduct.getSpecifications().add(specification1);
        			specificationProduct.getSpecificationValues().add(specification1Value);
        		}
        		
        		if (color!=null) {
        			Specification specification2 = specificationService.find(2L);
        			SpecificationValue specification2Value = specificationValueService.findByName(specification2,color);
        			if (specification2Value==null) {
        				specification2Value = new SpecificationValue();
        				specification2Value.setName(color);
        				specification2Value.setSpecification(specification2);
        				specification2Value.setProducts(null);
        				specificationValueService.save(specification2Value);
        			}
        			specificationProduct.getSpecifications().add(specification2);
        			specificationProduct.getSpecificationValues().add(specification2Value);
        		}
        		specificationProduct.setPrice(price);
        		specificationProduct.setStock(stock);
        		specificationProduct.setWholePrice(price);
        		specificationProduct.setMarketPrice(marketPrice);
        		specificationProduct.setBarcode(barcode);
        		specificationProduct.setFee(product.getFee());
        		specificationProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));
        		specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
                specificationProduct.setOrder(goods.getProducts().size()+1);
            }
			
		for (Product sp:goods.getProducts()) {
			if (sp.equals(specificationProduct)) {
				goods.getProducts().remove(sp);
				break;
			}
		}
		goods.getProducts().add(specificationProduct);
		if (sn.equals(productSn)) {
			for (Product prod:goods.getProducts()) {
				prod.setIsList(false);
			}
			specificationProduct.setIsList(true);
		} 
		goodsService.update(goods);
	    return DataBlock.success(specificationProduct.getSn(),"保存成功");
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock update(Long tenantId,String key,Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long[] tagIds, Long[] specificationIds, Long[] specificationProductIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey =  DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
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
		Product pProduct = productService.find(product.getId());
		if (pProduct == null) {
			return  DataBlock.error("没找到当前商品,可以已被删除");
		}
		for (Tag tag:pProduct.getTags()) {
			if (tag.getId().compareTo(10L)>0 && !product.getTags().contains(tag)) {
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

		Goods goods = pProduct.getGoods();
		List<Product> products = new ArrayList<Product>();
		if (specificationIds != null && specificationIds.length > 0) {
			String[] specificationPrice = request.getParameterValues("specification_price");
			String[] specificationStock = request.getParameterValues("specification_stock");
			String[] specificationBarcode = request.getParameterValues("specification_barcode");
			String[] specificationCost = request.getParameterValues("specification_cost");
			String[] specificationWholePrice = request.getParameterValues("specification_wholePrice");
			String[] specificationMarketPrice = request.getParameterValues("specification_marketPrice");
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
								pProduct.setMarketPrice(product.getMarketPrice());
								pProduct.setSpecifications(new HashSet<Specification>());
								pProduct.setSpecificationValues(new HashSet<SpecificationValue>());
								pProduct.setSupplier(product.getSupplier());
								pProduct.setCost(product.getCost());
								pProduct.setFee(product.getFee());
								pProduct.setIntroduction(product.getIntroduction());
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
									specificationProduct.setTags(new HashSet<Tag>(product.getTags()) );
									specificationProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()) );
									specificationProduct.setFee(product.getFee());
									specificationProduct.setIsGift(pProduct.getIsGift());
									specificationProduct.setIsList(false);
									specificationProduct.setIsTop(pProduct.getIsTop());
									specificationProduct.setIsMarketable(true);
									specificationProduct.setMarketPrice(product.getMarketPrice());
									specificationProduct.setSupplier(product.getSupplier());
									specificationProduct.setCost(product.getCost());
									specificationProduct.setFee(product.getFee());
									specificationProduct.setSpecifications(new HashSet<Specification>());
									specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
									specificationProduct.setIntroduction(product.getIntroduction());
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
									specificationProduct.setPoint(0L);
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
									specificationProduct.setSupplier(product.getSupplier());
									specificationProduct.setCost(product.getCost());
									specificationProduct.setFee(product.getFee());
									
									specificationProduct.setPromotions(null);
									specificationProduct.setCartItems(null);
									specificationProduct.setOrderItems(null);
									specificationProduct.setGiftItems(null);
									products.add(specificationProduct);
								}
							}
						}
						
						Product specificationProduct = products.get(j);
						specificationProduct.setOrder(j);
						SpecificationValue specificationValue = specificationValueService.findByName(specification,specificationValueIds[j]);
						if (specificationValue==null) {
							specificationValue = new SpecificationValue();
							specificationValue.setName(specificationValueIds[j]);
							specificationValue.setSpecification(specification);
							specificationValue.setProducts(null);
							specificationValueService.save(specificationValue);
						}
						specificationProduct.setPrice(new BigDecimal(specificationPrice[j]));
						specificationProduct.setStock(new Integer(specificationStock[j]));
						if (specificationCost!=null && specificationCost.length>0) {
						    specificationProduct.setCost(new BigDecimal(specificationCost[j]));
						}
						specificationProduct.setWholePrice(product.getWholePrice());
						if (specificationWholePrice!=null && specificationWholePrice.length>0) {
						    specificationProduct.setWholePrice(new BigDecimal(specificationWholePrice[j]));
						}
						specificationProduct.setBarcode(specificationBarcode[j]);
						specificationProduct.setFee(product.getFee());
						specificationProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));
						specificationProduct.setMarketPrice(product.getMarketPrice());
						if (specificationMarketPrice!=null && specificationMarketPrice.length>0) {
							   specificationProduct.setMarketPrice(new BigDecimal(specificationMarketPrice[j]));
							}
						
						for (MemberRank memberRank : memberRankService.findAll()) {
							String[] specificationMemberPrice = request.getParameterValues("specification_memberPrice_" + memberRank.getId());
							if (specificationMemberPrice!=null && specificationMemberPrice.length>0) {
								String price = specificationMemberPrice[j];
								if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
									specificationProduct.getMemberPrice().put(memberRank, new BigDecimal(price));
								} else {
									specificationProduct.getMemberPrice().remove(memberRank);
								}
							}
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
			pProduct.setIntroduction(product.getIntroduction());
			pProduct.setUnit(product.getUnit());
			pProduct.setTags(new HashSet<Tag>(product.getTags()) );
			pProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()) );
            pProduct.setFee(product.getFee());
            pProduct.setMemberPrice(product.getMemberPrice());
            pProduct.setMarketPrice(product.getMarketPrice());
            pProduct.setBarcode(product.getBarcode());
            pProduct.setPrice(product.getPrice());
            pProduct.setWholePrice(product.getWholePrice());
            pProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));
            pProduct.setSupplier(product.getSupplier());
            pProduct.setCost(product.getCost());
            pProduct.setFee(product.getFee());
            pProduct.setSpecifications(null);
            pProduct.setSpecificationValues(null);
            pProduct.setStock(product.getStock());
            pProduct.setOrder(1);
			products.add(pProduct);
		}
		
		goods.getProducts().clear();
		goods.getProducts().addAll(products);
		for (Product tempProduct:goods.getProducts()) {
			tempProduct.setFullName(null);
		}
		goodsService.update(goods);
		
		return DataBlock.success(pProduct.getId(),"修改成功");
	}
	
	/** 数据同步 */
	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock update(Long tenantId,String key,String sn,BigDecimal stock,BigDecimal price,BigDecimal marketPrice) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}

		try {
			Product product = productService.findBySn(sn);
			if (product!=null && product.getTenant().getId().equals(tenant.getId())) {
			   if  (stock!=null) {
				  product.setStock(stock.intValue());
			   }
			   if (price!=null) {
			      product.setPrice(price);
			   }
    		   if (marketPrice!=null) {
			      product.setMarketPrice(marketPrice);
			   }
			   productService.save(product);
			   return DataBlock.success("success","success");
			} else {
				   return DataBlock.success("success","success");
			}
		} catch (Exception e) {
			return DataBlock.error("出错了");
		}
	}

	
	/** 数据同步 */
	@RequestMapping(value = "/sync_batch", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock update(Long tenantId,String key,String[] sns,BigDecimal[] stocks,BigDecimal[] prices,BigDecimal[] marketPrices) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        }
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}

		try {
			for (int i=0;i<sns.length;i++) {
				Product product = productService.findBySn(sns[i]);
				if (product!=null && product.getTenant().getId().equals(tenant.getId())) {
				   if  (stocks[i]!=null) {
					   if (stocks[i].intValue()>0) {
 					      product.setStock(stocks[i].intValue());
					   } else {
	 					  product.setStock(0);
					   }
				   }
				   if (prices[i]!=null) {
				      product.setPrice(prices[i]);
				   }
	    		   if (marketPrices[i]!=null) {
				      product.setMarketPrice(marketPrices[i]);
				   }
				   productService.save(product);
				}
			}
			return DataBlock.success("success","success");
		} catch (Exception e) {
			return DataBlock.error("出错了");
		}
	}
	
}