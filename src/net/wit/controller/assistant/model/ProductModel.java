package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.*;
import net.wit.controller.assistant.model.PromotionModel;
import net.wit.controller.assistant.model.SingleModel;
import net.wit.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductModel extends BaseModel {
	/*商品ID*/
	private Long id;
	/*商品SN*/
	private String sn;
	/*条码码*/
	private String barcode;
	/*商品名称*/
	private String name;
	/*全名描述*/
	private String fullName;
	/*缩略图*/
	private String thumbnail;
	/*运费描述*/
	private String freightDesc;
	/*销售价*/
	private BigDecimal price;
	/*成本价*/
	private BigDecimal cost;
	/*批发价*/
	private BigDecimal wholePrice;
	/*市场价*/
	private BigDecimal marketPrice;
	/*有效价*/
	private BigDecimal effectivePrice;
	/*分润金额*/
	private BigDecimal fee;
	/*分类*/
	private ProductCategoryModel productCategory;
	/*商品属性*/
	private Set<ProductAttributeModel> attributes;
	/*店内分类*/
	private ProductCategoryTenantModel productCategoryTenant;
	/*图片*/
	private List<ProductImageModel> productImages;

	/*型号*/
	private String spec;
	/*颜色*/
	private String color;

	public List<SpecificationValueModel> getSpecification() {
		return specification;
	}

	public void setSpecification(List<SpecificationValueModel> specification) {
		this.specification = specification;
	}

	/*规格*/
	private List<SpecificationValueModel> specification;

	/*是否列表*/
	private Boolean isList;

	/*商品id*/
	private Long goodsId;

	/*商品id*/
	private SingleModel tenant;

	/*会员价*/
	private Set<MemberPriceModel> memberPrices;
	/*标签*/
	private Set<TagModel> tags;
	/*促销*/
	private Set<PromotionModel> promotions;
	/*参数*/
	private Set<ParameterValueModel> parameterValues;
	/*单位*/
	private String unit;
	/*库存*/
	private Integer stock;
	/*可用库存*/
	private Integer availableStock;


	private Integer allocatedStock;

	/*销量*/
	private Long sales;
	/*点击数*/
	private Long hits;
	/*评分人数*/
	private Long scoreCount;
	/*评分*/
	private Float score;
	/*商品详情描述 HTML 格式*/
	private String introduction;
	/*商品描述，纯文本*/
	private String descriptionapp;

	/*图片*/
	private List<ProductModel> products;
	/** 月销量 */
	private Long monthSales;

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getBarcode() {
		return barcode;
	}


	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getSpec() {
		return spec;
	}


	public void setSpec(String spec) {
		this.spec = spec;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public BigDecimal getEffectivePrice() {
		return effectivePrice;
	}


	public void setEffectivePrice(BigDecimal effectivePrice) {
		this.effectivePrice = effectivePrice;
	}


	public String getFreightDesc() {
		return freightDesc;
	}


	public void setFreightDesc(String freightDesc) {
		this.freightDesc = freightDesc;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public BigDecimal getWholePrice() {
		return wholePrice;
	}


	public void setWholePrice(BigDecimal wholePrice) {
		this.wholePrice = wholePrice;
	}


	public BigDecimal getMarketPrice() {
		return marketPrice;
	}


	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}


	public BigDecimal getFee() {
		return fee;
	}


	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}


	public ProductCategoryModel getProductCategory() {
		return productCategory;
	}


	public void setProductCategory(ProductCategoryModel productCategory) {
		this.productCategory = productCategory;
	}

	public Set<ProductAttributeModel> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<ProductAttributeModel> attributes) {
		this.attributes = attributes;
	}

	public ProductCategoryTenantModel getProductCategoryTenant() {
		return productCategoryTenant;
	}


	public void setProductCategoryTenant(ProductCategoryTenantModel productCategoryTenant) {
		this.productCategoryTenant = productCategoryTenant;
	}


	public List<ProductImageModel> getProductImages() {
		return productImages;
	}


	public void setProductImages(List<ProductImageModel> productImages) {
		this.productImages = productImages;
	}


	public Set<TagModel> getTags() {
		return tags;
	}


	public void setTags(Set<TagModel> tags) {
		this.tags = tags;
	}


	public Set<PromotionModel> getPromotions() {
		return promotions;
	}


	public void setPromotions(Set<PromotionModel> promotions) {
		this.promotions = promotions;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public Integer getStock() {
		return stock;
	}


	public void setStock(Integer stock) {
		this.stock = stock;
	}


	public Long getSales() {
		return sales;
	}


	public void setSales(Long sales) {
		this.sales = sales;
	}


	public Long getHits() {
		return hits;
	}


	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Long getScoreCount() {
		return scoreCount;
	}


	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}


	public SingleModel getTenant() {
		return tenant;
	}


	public void setTenant(SingleModel tenant) {
		this.tenant = tenant;
	}


	public Set<MemberPriceModel> getMemberPrices() {
		return memberPrices;
	}


	public void setMemberPrices(Set<MemberPriceModel> memberPrices) {
		this.memberPrices = memberPrices;
	}


	public List<ProductModel> getProducts() {
		return products;
	}


	public void setProducts(List<ProductModel> products) {
		this.products = products;
	}


	public String getDescriptionapp() {
		return descriptionapp;
	}


	public void setDescriptionapp(String descriptionapp) {
		this.descriptionapp = descriptionapp;
	}


	public Set<ParameterValueModel> getParameterValues() {
		return parameterValues;
	}


	public void setParameterValues(Set<ParameterValueModel> parameterValues) {
		this.parameterValues = parameterValues;
	}


	public Float getScore() {
		return score;
	}


	public void setScore(Float score) {
		this.score = score;
	}

	public Boolean getIsList() {
		return isList;
	}


	public void setIsList(Boolean isList) {
		this.isList = isList;
	}


	public Long getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}


	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}


	public Integer getAllocatedStock() {
		return allocatedStock;
	}


	public void setAllocatedStock(Integer allocatedStock) {
		this.allocatedStock = allocatedStock;
	}

	public String getIntroduction() {
		return introduction;
	}


	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Long getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
	}

	public BigDecimal getCost() {
		return cost;
	}


	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}


	public void copyFrom(Product product) {
		this.id = product.getId();
		this.sn = product.getSn();
		this.name = product.getName();
		this.fee = product.getFee();
		this.fullName = product.getFullName();
		this.marketPrice = product.getMarketPrice();
		this.price = product.getPrice();
		this.cost = product.getCost();

		ProductCategoryModel productCategory = new ProductCategoryModel();
		productCategory.copyFrom(product.getProductCategory());
		this.productCategory = productCategory;
		if(product.getProductCategory()!=null){
			this.attributes=ProductAttributeModel.bindData(product.getProductCategory().getAttributes(),product);
		}
		ProductCategoryTenantModel productCategoryTenant = new ProductCategoryTenantModel();
		if (product.getProductCategoryTenant()!=null) {
		   productCategoryTenant.copyFrom(product.getProductCategoryTenant());
		}

		this.productCategoryTenant = productCategoryTenant;
		this.productImages = ProductImageModel.bindData(product.getProductImages());
		this.spec = "";
		this.color = "";

		for (SpecificationValue specificationValue:product.getSpecificationValues()) {
            if (specificationValue.getSpecification().getId().equals(1L)) {
               this.spec = specificationValue.getName();
            }
            if (specificationValue.getSpecification().getId().equals(2L)) {
                this.color = specificationValue.getName();
             }
		}

		SingleModel tenant = new SingleModel();
		tenant.setId(product.getTenant().getId());
		tenant.setName(product.getTenant().getName());
		this.tenant = tenant;
		this.sales = product.getSales();
		this.hits = product.getHits();
		this.stock = product.getStock();
		this.availableStock = product.getAvailableStock();
		this.tags = TagModel.bindData(product.getTags());
		this.score = product.getScore();
		this.scoreCount = product.getScoreCount();
		this.isList = product.getIsList();
		this.goodsId = product.getGoods().getId();
		this.effectivePrice = product.calcEffectivePrice(null);
		this.freightDesc = product.calculateFreightDesc();

		Set<PromotionModel> promotions = new HashSet<PromotionModel>();
		for (Promotion promotion:product.getPromotions()) {
			PromotionModel model = new PromotionModel();
			model.copyFrom(promotion);
			promotions.add(model);
		}

		this.promotions = promotions;
		this.thumbnail = product.getThumbnail();
		this.unit = product.getUnit();
		this.wholePrice = product.getWholePrice();
		this.barcode = product.getBarcode();
		this.memberPrices = MemberPriceModel.bindData(product.getMemberPrice());
		this.descriptionapp = product.getDescriptionapp();
		this.introduction = product.getIntroduction();

		this.parameterValues = ParameterValueModel.bindData(product.getParameterValue());
		this.specification =	SpecificationValueModel.bindData(product.getSpecificationValues());
		for (SpecificationValueModel specification:this.specification) {
            if (specification.getId().equals(1L) && product.getGoods().getSpecification1Title()!=null) {
            	specification.setTitle(product.getGoods().getSpecification1Title());
            }
            if (specification.getId().equals(2L) && product.getGoods().getSpecification2Title()!=null) {
            	specification.setTitle(product.getGoods().getSpecification2Title());
             }
		}
		this.monthSales=product.getMonthSales();
	}

	public void copyFrom(Product product,Member member) {
		this.id = product.getId();
		this.sn = product.getSn();
		this.name = product.getName();
		this.fee = product.getFee();
		this.fullName = product.getFullName();
		this.marketPrice = product.getMarketPrice();
		this.price = product.getPrice();
		this.cost = product.getCost();

		ProductCategoryModel productCategory = new ProductCategoryModel();
		productCategory.copyFrom(product.getProductCategory());
		this.productCategory = productCategory;
		if(product.getProductCategory()!=null){
			this.attributes=ProductAttributeModel.bindData(product.getProductCategory().getAttributes(),product);
		}
		ProductCategoryTenantModel productCategoryTenant = new ProductCategoryTenantModel();
		if (product.getProductCategoryTenant()!=null) {
		   productCategoryTenant.copyFrom(product.getProductCategoryTenant());
		}

		this.productCategoryTenant = productCategoryTenant;
		this.productImages = ProductImageModel.bindData(product.getProductImages());
		this.spec = "";
		this.color = "";
		for (SpecificationValue specificationValue:product.getSpecificationValues()) {
            if (specificationValue.getSpecification().getId().equals(1L)) {
               this.spec = specificationValue.getName();
            }
            if (specificationValue.getSpecification().getId().equals(2L)) {
                this.color = specificationValue.getName();
             }
		}

		SingleModel tenant = new SingleModel();
		tenant.setId(product.getTenant().getId());
		tenant.setName(product.getTenant().getName());
		this.tenant = tenant;
		this.sales = product.getSales();
		this.hits = product.getHits();
		this.stock = product.getStock();
		this.availableStock = product.getAvailableStock();
		this.tags = TagModel.bindData(product.getTags());
		this.score = product.getScore();
		this.scoreCount = product.getScoreCount();
		this.isList = product.getIsList();
		this.goodsId = product.getGoods().getId();
		this.effectivePrice = product.calcEffectivePrice(member);
		this.freightDesc = product.calculateFreightDesc();

		Set<PromotionModel> promotions = new HashSet<PromotionModel>();
		for (Promotion promotion:product.getPromotions()) {
			PromotionModel model = new PromotionModel();
			model.copyFrom(promotion);
			promotions.add(model);
		}
		this.promotions = promotions;
		this.thumbnail = product.getThumbnail();
		this.unit = product.getUnit();
		this.wholePrice = product.getWholePrice();
		this.barcode = product.getBarcode();
		this.memberPrices = MemberPriceModel.bindData(product.getMemberPrice());
		this.descriptionapp = product.getDescriptionapp();
		
		this.parameterValues = ParameterValueModel.bindData(product.getParameterValue());
	    this.specification =	SpecificationValueModel.bindData(product.getSpecificationValues());
		this.monthSales=product.getMonthSales();
	}
	
	public void bind(Goods goods) {
		ArrayList<ProductModel> models = new ArrayList<ProductModel>();
		for (Product product:goods.getSortProducts()) {
			if (!product.getId().equals(this.id)) {
				ProductModel model = new ProductModel();
				model.copyFrom(product);
				models.add(model);
			}
		}
		this.products = models;
	}		
	
	public static List<ProductModel> bindData(List<Product> products) {
		List<ProductModel> models = new ArrayList<ProductModel>();
		for (Product product:products) {
			ProductModel model = new ProductModel();
			model.copyFrom(product);
			models.add(model);
		}
		return models;
	}
}
